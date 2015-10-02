-- @C:\common\order_Collections.sql
-- c:\app\venumech\product\12.1.0\dbhome_2\BIN\sqlplus venu@logistics/deek1965
-- http://stackoverflow.com/questions/12677746/oracle-stored-procedure-using-array-as-parameter-for-table-insert (good)
-- http://stackoverflow.com/questions/11621638/how-to-call-procedure-with-out-parameter-as-table-type-from-a-java-class
-- http://www.literak.cz/2013/08/working-with-complex-database-types-in-weblogic/
-- http://docs.oracle.com/cd/B19306_01/appdev.102/b14261/objects.htm
-- http://stackoverflow.com/questions/8971231/bulk-insert-from-array-to-table-plsql
--Oracle JDBC does not support RAW, DATE, and PL/SQL RECORD as element types.
-- https://www.google.com/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=oracletypes%2C+java_object+versus+java_struct (verygood)
-- 1) http://stackoverflow.com/questions/6410452/fetch-oracle-table-type-from-stored-procedure-using-jdbc
-- 2) http://stackoverflow.com/questions/22201330/call-pl-sql-function-returning-oracle-type-from-java

drop TYPE LINEITEM_OBJECT force;

drop TYPE LINES_TABLE force; //Collection of 1 or many LineItem_object



CREATE OR REPLACE TYPE LINEITEM_OBJECT as object(
  --order_id   number,
    weight   number,
    volume   number,
    hazard   char(1), --boolean not supported
    product  varchar2(100)
);
/

CREATE OR REPLACE TYPE LINES_TABLE is table of LINEITEM_OBJECT;
/

drop TYPE address_t force;

drop TYPE address_obj force;

CREATE OR REPLACE TYPE ADDRESS_OBJ as object(
    city  varchar2(100),
    state varchar2(20),
    zip varchar2(20)
    
);
/
show errors;
/

