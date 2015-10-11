/*
 SP Version: 1
 Saves the order data into 3 tables and the Order Id is populated with the data 
 and can be looked up at the client side
 after successful saving.
 This version employs the dynamic sql execution for inserting data
*/

CREATE OR REPLACE EDITIONABLE PROCEDURE "VENU"."ORDER_PROCESS_PROC_OLD"           
(from_city IN ADDRESS.city%type,                                                
 from_state IN ADDRESS.state%type,                                                 
 from_zip IN ADDRESS.zip%type,                                               
 to_city IN ADDRESS.city%type,                                                     
 to_state IN ADDRESS.state%type,                                                
 to_zip IN ADDRESS.zip%type,                                                  
 dynamic_line_item_sql in OUT varchar2,                                         
 instructions IN  ORDERS.instructions%type,                                     
 order_id OUT ORDERS.id%type,                                                   
 message OUT varchar2)                                                          
AS                                                                              
   l_order_id number;
   l_from_address_id varchar2(50);
   l_to_address_id varchar2(50);
   l_row_count number;
   fhandle utl_file.file_type;
   
   address_insert_error EXCEPTION; 
   order_insert_error EXCEPTION; 
   LineItems_insert_error EXCEPTION; 
                                                                                
BEGIN                                                                           


    fhandle := utl_file.fopen('LOG_DIR', 'order_save.log', 'a');
    UTL_FILE.PUT_LINE(fhandle, '__________________________________________________________________________________');
    UTL_FILE.PUT_LINE(fhandle, 'Date= '|| to_char(sysdate,'yyyy-dd-mm hh:mi:ss'));
    UTL_FILE.PUT_LINE(fhandle, '      fetching from seq. l_order_id= '|| l_order_id);

select order_id_seq.nextval into l_order_id from dual;                          
order_id  :=  l_order_id;                                                       
       dbms_output.put_line('l_order_id=' || l_order_id);
    UTL_FILE.PUT_LINE(fhandle, '      fetching from seq. l_order_id= '|| l_order_id);
                                     
        dbms_output.put_line('inserting into the new values into address..');    
                                                                                
 -- address_id is a combination of state and zip.                                                                               
select count(*) into l_row_count                                                
from ADDRESS                                                                    
where  -- UPPER(city)= UPPER(from_city) and                                             
  UPPER(state)=UPPER(from_state)  and                                          
   UPPER(zip)=UPPER(from_zip);                                               
                                                                                
if l_row_count=0                                                                
    then                                                                        
       l_from_address_id := from_state || from_zip;
       
    UTL_FILE.PUT_LINE(fhandle, '      The ''From'' address details not exists in ''ADDRESS'' so, inserting. l_from_address_id= '|| l_from_address_id);
       dbms_output.put_line('inserting into the new values into address..');    
                                                                                
       insert into ADDRESS (address_id, city, state ,zip)                       
       values (l_from_address_id, from_city, from_state, from_zip);             
       commit;                                                                  
    else                                                                        
    select  address_id into l_from_address_id                                   
    from ADDRESS                                                                
    where -- UPPER(city)= UPPER(from_city)   and                                      
       UPPER(state)=UPPER(from_state)    and                                    
       UPPER(zip)=UPPER(from_zip);                                               
       dbms_output.put_line('from address already exists');                     
    end if;                                                                     
                                                                                
    -- insert into the Address table for 'TO' fields                                                                                 
    select count(*) into l_row_count                                            
    from ADDRESS                                                                
    where UPPER(city)= UPPER(to_city)                                           
      and UPPER(state)=UPPER(to_state)                                          
      and UPPER(zip)=UPPER(to_zip);                                             
                                                                                
    if l_row_count=0                                                            
        then                                                                    
           l_to_address_id := to_state || to_zip;                               
           dbms_output.put_line('inserting into the new TO address..');
           UTL_FILE.PUT_LINE(fhandle, '      The ''To'' address details not exists in ''ADDRESS'' so, inserting. l_to_address_id= '|| l_to_address_id);

           insert into ADDRESS (address_id, city, state ,zip)                   
           values (l_to_address_id, to_city, to_state, to_zip);
           if (SQL%ROWCOUNT =0)
           then
               RAISE address_insert_error; 
           end if;
           commit;                                                              
        else                                                                    
        select address_id into l_to_address_id                                  
		from ADDRESS                                                                  
		where UPPER(city)= UPPER(to_city)                                             
		  and UPPER(state)=UPPER(to_state)                                            
		  and UPPER(zip)=UPPER(to_zip);                                               
	 dbms_output.put_line('TO address already exists');                            
    end if;                                                                     
                                                                                
    -- inserting into table, ORDERS.
    UTL_FILE.PUT_LINE(fhandle, '      inserting into table, ORDERS.');

    insert into ORDERS (id, from_address_id, to_address_id, instructions)       
    values (l_order_id, l_from_address_id, l_to_address_id, instructions);      
                                                                                
    l_row_count := SQL%ROWCOUNT;                                                
    if l_row_count = 1                                                          
        then                                                                    
           dbms_output.put_line('data inserted into ORDER');
           UTL_FILE.PUT_LINE(fhandle, '      data inserted into table, ORDERS.');
        else                                                                    
           message := 'data NOT inserted into ORDER';                              
           dbms_output.put_line('data NOT inserted into ORDER');
           UTL_FILE.PUT_LINE(fhandle, '      data NOT inserted into table, ORDERS.');
           
           rollback;                  
           raise order_insert_error;                                              
    end if;                                                                     
                                                                                
                                                                                
    --inserting data into LINE_ITEMS
    UTL_FILE.PUT_LINE(fhandle, '      inserting data into LINE_ITEMS table.');
    if l_row_count = 1                                                          
       then                                                                                
          dbms_output.put_line(replace(dynamic_line_item_sql,':order_id_val',l_order_id));                                                                         
          dynamic_line_item_sql :=  replace(dynamic_line_item_sql,':order_id_val',l_order_id);                                                                     
          EXECUTE IMMEDIATE dynamic_line_item_sql;                                 
       --insert into LINE_ITEMS (order_id , weight, volume, hazard, product )   
       --values (l_order_id , weight, volume, hazard, product );                
    end if;                                                                     
                                                                                
    l_row_count := SQL%ROWCOUNT;                                                
    if l_row_count > 0                                                          
            then                                                                
            dbms_output.put_line('data inserted into line_items. row count' || l_row_count);   
            UTL_FILE.PUT_LINE(fhandle, '      data inserted into line_items. row count' || l_row_count);                                                                 
    else                                                                        
            message := 'data NOT inserted into line_items. transaction rolled back!';                                                                               
            dbms_output.put_line('data NOT inserted into line_items');  
            UTL_FILE.PUT_LINE(fhandle, '      data NOT inserted into line_items. row count' || l_row_count);             
            rollback;
            raise LineItems_insert_error;
    end if;                                                                     
  
    UTL_FILE.fclose(fhandle);                                                                              
    
    EXCEPTION 
    WHEN address_insert_error THEN
             message := 'data NOT inserted into ADDRESS. transaction rolled back!';                                                                               
    WHEN order_insert_error THEN
         message := 'data NOT inserted into Order. transaction rolled back!';                                                                               
    WHEN LineItems_insert_error THEN
         message := 'data NOT inserted into LineItems. transaction rolled back!';                                                                               

    WHEN NO_DATA_FOUND THEN 
                 message := 'A SELECT...INTO did not return any row.!';                                                                               
        dbms_output.put_line ('A SELECT...INTO did not return any row.'); 
 END; 
 /
 show errors;           
/