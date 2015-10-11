/*
 Retrieve the data from tables for the given order_id
 PLSQL types can't be accessed directly from java. we will have to use a true SQL type (CREATE type...) 
 and either a wrapper procedure with this SQL type or modify  procedure to accept the new type
*/
CREATE OR REPLACE EDITIONABLE PROCEDURE "VENU"."ORDER_PROCESS_LOOKUP" 
( l_instructions out  ORDERS.instructions%type,
 l_line_items out  Lines_table,
 l_from_address out  address_obj,
 l_to_address out  address_obj,
 l_order_id IN  ORDERS.id%type,
 message IN out varchar2)
AS
   l_from_address_id varchar2(50);
   l_to_address_id varchar2(50);
   l_row_count number;
   
   fhandle utl_file.file_type;

BEGIN


    fhandle := utl_file.fopen('LOG_DIR', 'order_lookup.log', 'a');
    UTL_FILE.PUT_LINE(fhandle, '__________________________________________________________________________________');
    UTL_FILE.PUT_LINE(fhandle, 'Date= '|| to_char(sysdate,'yyyy-dd-mm hh:mi:ss'));
    UTL_FILE.PUT_LINE(fhandle, '      l_order_id= '|| l_order_id);
      
    UTL_FILE.PUT_LINE(fhandle, '    Fetching LINE_ITEMS..');
    UTL_FILE.FFLUSH(fhandle);
    
    --fetch Line items
    select LINEITEM_OBJECT( weight, volume, hazard, product)  bulk collect  into l_line_items from line_items where order_id=l_order_id;
    
    UTL_FILE.PUT_LINE(fhandle, '    Done. fetching LINE_ITEMS. count=' || l_line_items.count);
    
    dbms_output.put_line('data retrieved from LINE_ITEMS. ORDER_ID=' || l_order_id || '; row count=' || SQL%ROWCOUNT); 
    
    UTL_FILE.PUT_LINE(fhandle, '    Fetching data from ORDER..');
    Select instructions,  from_address_id, to_address_id into l_instructions, l_from_address_id, l_to_address_id
	from orders where id= l_order_id;

    UTL_FILE.PUT_LINE(fhandle, '    Done. fetching ORDER data. count=' || SQL%ROWCOUNT);
    
    UTL_FILE.PUT_LINE(fhandle, '    Fetching data of "From" Address..');
    --fetch 'From' Address
    select ADDRESS_OBJ(city, state, zip) into l_from_address from ADDRESS where address_id=l_from_address_id;

    UTL_FILE.PUT_LINE(fhandle, '    Done. fetching "From" Address... count=' || SQL%ROWCOUNT);

    UTL_FILE.PUT_LINE(fhandle, '    Fetching data of "To" Address..');
    
    --fetch 'TO' Address
    select ADDRESS_OBJ(city, state, zip) into l_to_address from ADDRESS where address_id=l_to_address_id;

    UTL_FILE.PUT_LINE(fhandle, '    Done. fetching "To" Address... count=' || SQL%ROWCOUNT);

    UTL_FILE.fclose(fhandle);
EXCEPTION
   WHEN no_data_found THEN
      dbms_output.put_line('No such data!');
      message := SQLCODE || ': '|| SQLERRM || '. The Order Id : ' || l_order_id || ' does not Exists in the System';
   WHEN others THEN
      dbms_output.put_line('Error!');
      message := SQLCODE || ': '|| SQLERRM || '. Data Base Error Occured while searching the Order Id : ' || l_order_id;
END;
/

show errors;
/