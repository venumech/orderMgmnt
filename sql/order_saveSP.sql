/*
version: latest
@C:\common\order_saveSP.sql
 saves the order data into 3 tables and the Order Id is populated with the data 
 and the same can be looked up at java buziness layer side
 after successful saving.
*/

CREATE OR REPLACE EDITIONABLE PROCEDURE "VENU"."ORDER_PROCESS_PROC"           
(l_from_address IN  address_obj,
 l_to_address IN  address_obj,
 l_line_items IN  Lines_table,
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
   
   --utl_file related.
   v_file_exist boolean;
   file_length number;
   block_size number;
                                                                                
BEGIN  
    -- synta
    --  UTL_FILE.FGETATTR( location IN VARCHAR2, filename IN VARCHAR2, exists OUT BOOLEAN, file_length OUT NUMBER, blocksize OUT NUMBER);
    --  sys.utl_file.fgetattr('LOG_DIR',             'order_save.log',     v_file_exist,       file_length,            block_size);
    fhandle := utl_file.fopen('LOG_DIR', 'order_save.log', 'a');
    UTL_FILE.PUT_LINE(fhandle, '__________________________________________________________________________________');
    UTL_FILE.PUT_LINE(fhandle, 'Date= '|| to_char(sysdate,'yyyy-dd-mm hh:mi:ss'));

    dbms_output.put_line('inserting into the new values into address..');    
                                                                                
    -- address_id is a combination of state and zip.
    select count(*) into l_row_count                                                
      from ADDRESS                                                                    
     where UPPER(state)=UPPER(l_from_address.state)  and                                          
           UPPER(zip)=UPPER(l_from_address.zip);                                               
             
     l_from_address_id := l_from_address.state || l_from_address.zip;
     
     if l_row_count=0                                                                
     then                                                                 
        UTL_FILE.PUT_LINE(fhandle, '      The ''From'' address details not exists in ''ADDRESS'' so, inserting. l_from_address_id= '|| l_from_address_id);
        dbms_output.put_line('inserting into the new values into address.. l_from_address_id='|| l_from_address_id);    
                                                                                
        insert into ADDRESS (address_id, city, state ,zip)                       
        values (l_from_address_id, l_from_address.city, l_from_address.state, l_from_address.zip);             
        commit;                                                                  
     else                                             
       dbms_output.put_line('from address already exists');
	 UTL_FILE.PUT_LINE(fhandle, '      ''From'' address already exists. l_to_address_id= '|| l_to_address_id);
     end if;                                                                     
                                                                                
    -- insert into the Address table for 'TO' fields                                                                                 
    select count(*) into l_row_count                                            
      from ADDRESS                                                                
     where UPPER(state)=UPPER(l_to_address.state)                                          
       and UPPER(zip)=UPPER(l_to_address.zip);                                             
                                                                               
    l_to_address_id := l_to_address.state || l_to_address.zip;            
    if l_row_count=0                                                            
    then                                                                                        
       dbms_output.put_line('inserting into the new TO address..');
       UTL_FILE.PUT_LINE(fhandle, '      The ''To'' address details not exist in ''ADDRESS'' so, inserting. l_to_address_id= '|| l_to_address_id);
       UTL_FILE.FFLUSH(fhandle);
       
       insert into ADDRESS (address_id, city, state ,zip)                   
       values (l_to_address_id, l_to_address.city, l_to_address.state, l_to_address.zip);
       
       if (SQL%ROWCOUNT =0)
       then
          UTL_FILE.PUT_LINE(fhandle, '      INSERT action failed for ''To'' address. l_to_address_id= '|| l_to_address_id);
          UTL_FILE.FFLUSH(fhandle);
          RAISE address_insert_error; 
       end if;
       commit;                                                              
    else                                              
	 dbms_output.put_line('TO address already exists');
	 UTL_FILE.PUT_LINE(fhandle, '      ''To'' address already exists. l_to_address_id= '|| l_to_address_id);
    end if;                                                                     

    UTL_FILE.PUT_LINE(fhandle, '      fetching from seq. new Order id, l_order_id= '|| l_order_id);

    select order_id_seq.nextval into l_order_id from dual;                          
    
    order_id  :=  l_order_id; -- populate in the OUT param
    
    dbms_output.put_line('l_order_id=' || l_order_id);
    UTL_FILE.PUT_LINE(fhandle, '      fetched from seq. l_order_id= '|| l_order_id);
    UTL_FILE.FFLUSH(fhandle);
    
    -- inserting into table, ORDERS.
    UTL_FILE.PUT_LINE(fhandle, '      inserting data into table, ORDERS.');

    insert into ORDERS (id, from_address_id, to_address_id, instructions)       
    values (l_order_id, l_from_address_id, l_to_address_id, instructions);      
                                                                                
    l_row_count := SQL%ROWCOUNT;                                                
    if l_row_count = 1                                                          
        then                                                                    
           dbms_output.put_line('data inserted into ORDER');
           UTL_FILE.PUT_LINE(fhandle, '      data inserted into table, ORDERS.');
    else                                                                    
           message := 'data Could NOT be inserted into ORDER';                              
           dbms_output.put_line('data NOT inserted into ORDER');
           UTL_FILE.PUT_LINE(fhandle, '      data NOT inserted into table, ORDERS.');
           UTL_FILE.FFLUSH(fhandle);

           rollback;                  
           raise order_insert_error;                                              
    end if;                                                                     

    --inserting data into LINE_ITEMS
    UTL_FILE.PUT_LINE(fhandle, '      inserting data into LINE_ITEMS table.');
    if l_row_count = 1                                                          
    then                                                                                
         forall i in 1..l_line_items.count
            insert into LINE_ITEMS(order_id , weight,volume, hazard, product)         -- 
            values(l_order_id, l_line_items(i).weight,l_line_items(i).volume, l_line_items(i).hazard, l_line_items(i).product);                                
    end if;                                                                     
                                                                                
    l_row_count := SQL%ROWCOUNT;                                                
    if l_row_count > 0                                                          
            then                                                                
            dbms_output.put_line('data inserted into line_items. row count' || l_row_count);   
            UTL_FILE.PUT_LINE(fhandle, '      data inserted into line_items. row count=' || l_row_count);                                                                 
    else                                                                        
            message := 'data NOT inserted into line_items. transaction rolled back!';                                                                               
            dbms_output.put_line('data NOT inserted into line_items');  
            UTL_FILE.PUT_LINE(fhandle, '      data NOT inserted into line_items. row count=' || l_row_count);             
            rollback;
            raise LineItems_insert_error;
    end if;                                                                     
    
    
    UTL_FILE.PUT_LINE(fhandle, '      All insert operations are completed. order_id= '|| l_order_id);
    
    UTL_FILE.fflush(fhandle); 
    UTL_FILE.fclose(fhandle);                                                                              
    
    EXCEPTION 
    WHEN address_insert_error THEN
             message := 'data could NOT be inserted into ADDRESS. transaction rolled back!';
      if (UTL_FILE.is_open(fhandle))
      then 
              UTL_FILE.PUT_LINE(fhandle, '     '||message);
              UTL_FILE.fclose(fhandle);
      end if;             
    WHEN order_insert_error THEN
         message := 'data could NOT be inserted into Order. transaction rolled back!'; 
      if (UTL_FILE.is_open(fhandle))
      then 
              UTL_FILE.PUT_LINE(fhandle, '     '||message);
              UTL_FILE.fclose(fhandle);
      end if;         
    WHEN LineItems_insert_error THEN
         message := 'data could NOT be inserted into LineItems. transaction rolled back!';                                                                               
      if (UTL_FILE.is_open(fhandle))
      then 
              UTL_FILE.PUT_LINE(fhandle, '     '||message);
              UTL_FILE.fclose(fhandle);
      end if;
    WHEN NO_DATA_FOUND THEN 
        message := 'A SELECT...INTO did not return any row.!';                                                                               
        dbms_output.put_line (message); 
      if (UTL_FILE.is_open(fhandle))
      then 
              UTL_FILE.PUT_LINE(fhandle, '     '||message);
              UTL_FILE.fclose(fhandle);
      end if;
    WHEN OTHERS THEN 
        message := SQLCODE || ': '|| SQLERRM || '. Data Base Error Occured for the Order Id : ' || l_order_id;
        dbms_output.put_line (message); 
      if (UTL_FILE.is_open(fhandle))
      then 
              UTL_FILE.PUT_LINE(fhandle, '     '||message);
              UTL_FILE.fclose(fhandle);
      end if;
 END; 
 /
 show errors;           
/