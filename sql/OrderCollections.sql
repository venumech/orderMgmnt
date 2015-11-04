-- @C:\common\OrderCollections.sql
-- collection objects used in the stored procedures
drop TYPE LINEITEM_OBJECT force;

drop TYPE LINES_TABLE force; //Collection of 1 or many LineItem_objects



CREATE OR REPLACE TYPE LINEITEM_OBJECT as object(
    weight   number,
    volume   number,
    hazard   char(1), -- as boolean not supported
    product  varchar2(100)
);
/

CREATE OR REPLACE TYPE LINES_TABLE is table of LINEITEM_OBJECT;
/

-- used for data of Address 

drop TYPE ADDRESS_OBJ force;

CREATE OR REPLACE TYPE ADDRESS_OBJ as object(
    city  varchar2(100),
    state varchar2(20),
    zip varchar2(20)
    
);
/


