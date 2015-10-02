/*
 @C:\common\order_lookup.sql
 c:\app\venumech\product\12.1.0\dbhome_2\BIN\sqlplus venu@logistics/deek1965
 Retrieve the data from tables for the given order_id
 PLSQL types can't be accessed directly from java. we will have to use a true SQL type (CREATE type...) 
 and either a wrapper procedure with this SQL type or modify your procedure to accept the new type
*/
CREATE OR REPLACE EDITIONABLE PROCEDURE "VENU"."ORDER_PROCESS_LOOKUP" 
( l_instructions IN out  ORDERS.instructions%type,
 l_col_data out  Lines_table,
 l_from_address out  address_obj,
 l_to_address out  address_obj,
 l_order_id IN out ORDERS.id%type,
 message IN out varchar2)
AS
   l_from_address_id varchar2(50);
   l_to_address_id varchar2(50);
   l_row_count number;

BEGIN

--fetch Line items
 select LINEITEM_OBJECT( weight, volume, hazard, product)  bulk collect  into l_col_data from line_items where order_id=l_order_id;
/*
forall i in 1..l_col_data.count
         insert into LINE_ITEMS(order_id , weight,volume, hazard, product) 
            values(l_order_id, l_col_data(i).weight,l_col_data(i).volume,l_col_data(i).hazard,l_col_data(i).product);
*/            
dbms_output.put_line('data retrieved from LINE_ITEMS. ORDER_ID=' || l_order_id || '; row count=' || SQL%ROWCOUNT); 

Select instructions,  from_address_id, to_address_id into l_instructions, l_from_address_id, l_to_address_id
from orders where id= l_order_id;

--fetch 'From' Address
select ADDRESS_OBJ(city, state, zip) into l_from_address from ADDRESS where address_id=l_from_address_id;

--fetch 'TO' Address
select ADDRESS_OBJ(city, state, zip) into l_to_address from ADDRESS where address_id=l_to_address_id;

EXCEPTION
   WHEN no_data_found THEN
      dbms_output.put_line('No such customer!');
      message := SQLCODE || ': '|| SQLERRM || '. The Order Id : ' || l_order_id || ' does not Exists in the System';
   WHEN others THEN
      dbms_output.put_line('Error!');
      message := SQLCODE || ': '|| SQLERRM || '. Data Base Error Occured while searching the Order Id : ' || l_order_id;
END;
/

show errors;
/