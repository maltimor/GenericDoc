--------------------------------------------------------
-- Archivo creado  - lunes-noviembre-28-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence SEQ_GENERIC
--------------------------------------------------------

   CREATE SEQUENCE  "EDUFLOW"."SEQ_GENERIC"  MINVALUE 1 MAXVALUE 99999999 INCREMENT BY 1 START WITH 18 NOCACHE  ORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table DATABASE
--------------------------------------------------------

  CREATE TABLE "EDUFLOW"."DATABASE" 
   (	"ID" VARCHAR2(256 BYTE), 
	"FILENAME" VARCHAR2(1024 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  DDL for Table DOC
--------------------------------------------------------

  CREATE TABLE "EDUFLOW"."DOC" 
   (	"ID" VARCHAR2(256 BYTE), 
	"ID_DB" VARCHAR2(256 BYTE), 
	"TXT" CLOB, 
	"HTML" CLOB
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" 
 LOB ("TXT") STORE AS BASICFILE (
  TABLESPACE "SYSTEM" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) 
 LOB ("HTML") STORE AS BASICFILE (
  TABLESPACE "SYSTEM" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;
--------------------------------------------------------
--  DDL for Table G_SERVICES
--------------------------------------------------------

  CREATE TABLE "EDUFLOW"."G_SERVICES" 
   (	"TABLE_NAME" VARCHAR2(4000 BYTE), 
	"TYPE" VARCHAR2(4000 BYTE), 
	"FINAL_TABLE" VARCHAR2(4000 BYTE), 
	"FIELDS" VARCHAR2(4000 BYTE), 
	"KEYS" VARCHAR2(4000 BYTE), 
	"SEPARATOR" VARCHAR2(4000 BYTE), 
	"SEC_INFO" VARCHAR2(4000 BYTE), 
	"RESOLVER" VARCHAR2(4000 BYTE), 
	"SEC_RESOLVER" VARCHAR2(4000 BYTE), 
	"SELECT_VALUE" VARCHAR2(4000 BYTE), 
	"SELECT_FILTER" VARCHAR2(4000 BYTE), 
	"INSERT_VALUE" VARCHAR2(4000 BYTE), 
	"UPDATE_VALUE" VARCHAR2(4000 BYTE), 
	"DELETE_VALUE" VARCHAR2(4000 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  DDL for Table MODELO
--------------------------------------------------------

  CREATE TABLE "EDUFLOW"."MODELO" 
   (	"ID" VARCHAR2(256 BYTE), 
	"ID_DB" VARCHAR2(256 BYTE), 
	"FILENAME" VARCHAR2(1024 BYTE), 
	"TXT" CLOB, 
	"HTML" CLOB
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" 
 LOB ("TXT") STORE AS BASICFILE (
  TABLESPACE "SYSTEM" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) 
 LOB ("HTML") STORE AS BASICFILE (
  TABLESPACE "SYSTEM" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;
--------------------------------------------------------
--  DDL for Table MODELO_SECCION
--------------------------------------------------------

  CREATE TABLE "EDUFLOW"."MODELO_SECCION" 
   (	"ID" VARCHAR2(256 BYTE), 
	"ID_DB" VARCHAR2(256 BYTE), 
	"ID_MODELO" VARCHAR2(256 BYTE), 
	"ID_SECCION" VARCHAR2(256 BYTE), 
	"ORDEN" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  DDL for Table SECCION
--------------------------------------------------------

  CREATE TABLE "EDUFLOW"."SECCION" 
   (	"ID" VARCHAR2(256 BYTE), 
	"ID_DB" VARCHAR2(256 BYTE), 
	"FILENAME" VARCHAR2(1024 BYTE), 
	"DEPENDENCIAS" VARCHAR2(4000 BYTE), 
	"TXT" VARCHAR2(4000 BYTE), 
	"HTML" CLOB
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" 
 LOB ("HTML") STORE AS BASICFILE (
  TABLESPACE "SYSTEM" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;
REM INSERTING into EDUFLOW.DATABASE
SET DEFINE OFF;
Insert into EDUFLOW.DATABASE (ID,FILENAME) values ('1','Sanciones');
Insert into EDUFLOW.DATABASE (ID,FILENAME) values ('2','Cooperativas');
REM INSERTING into EDUFLOW.DOC
SET DEFINE OFF;
REM INSERTING into EDUFLOW.G_SERVICES
SET DEFINE OFF;
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('G_SERVICES',null,null,'*','TABLE_NAME',null,null,null,null,null,null,null,null,null);
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('DATABASE',null,null,'ID#S#SEQ_GENERIC,*','ID',null,'SIUDE=ADMIN',null,null,null,null,null,null,null);
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('SECCION',null,null,'ID#S#SEQ_GENERIC,*','ID',null,'SIUDE=ADMIN',null,null,null,null,null,null,null);
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('VIEW_SECCIONES',null,null,'*','ID',null,'SIUDE=ADMIN',null,null,'SELECT DB.FILENAME AS DB_FILENAME, S.* FROM
SECCION S LEFT JOIN DATABASE DB ON (S.ID_DB=DB.ID)','WHERE DB.ID=#{user.attr.DATABASE.ID}',null,null,null);
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('VIEW_MODELOS',null,null,'*','ID',null,'SIUDE=ADMIN',null,null,'SELECT DB.FILENAME AS DB_FILENAME,M.* FROM MODELO M
LEFT JOIN DATABASE DB ON (M.ID_DB=DB.ID)','WHERE DB.ID=#{user.attr.DATABASE.ID}',null,null,null);
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('MODELO',null,null,'ID#S#SEQ_GENERIC,*','ID',null,'SIUDE=ADMIN',null,null,null,null,null,null,null);
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('VIEW_MODELO_SECCION',null,null,'*','ID',null,'SIUDE=ADMIN',null,null,'SELECT DB.FILENAME AS DB_FILENAME,MS.*,S.FILENAME AS S_FILENAME,S.DEPENDENCIAS AS S_DEPENDENCIAS FROM MODELO_SECCION MS
LEFT JOIN DATABASE DB ON (MS.ID_DB=DB.ID)
LEFT JOIN SECCION S ON (MS.ID_SECCION=S.ID AND MS.ID_DB=S.ID_DB)',null,null,null,null);
Insert into EDUFLOW.G_SERVICES (TABLE_NAME,TYPE,FINAL_TABLE,FIELDS,KEYS,SEPARATOR,SEC_INFO,RESOLVER,SEC_RESOLVER,SELECT_VALUE,SELECT_FILTER,INSERT_VALUE,UPDATE_VALUE,DELETE_VALUE) values ('MODELO_SECCION',null,null,'ID#S#SEQ_GENERIC,*','ID',null,'SIUDE=ADMIN',null,null,null,null,null,null,null);
REM INSERTING into EDUFLOW.MODELO
SET DEFINE OFF;
Insert into EDUFLOW.MODELO (ID,ID_DB,FILENAME) values ('7','2','aaaa');
REM INSERTING into EDUFLOW.MODELO_SECCION
SET DEFINE OFF;
Insert into EDUFLOW.MODELO_SECCION (ID,ID_DB,ID_MODELO,ID_SECCION,ORDEN) values ('14','2','7','6','1');
Insert into EDUFLOW.MODELO_SECCION (ID,ID_DB,ID_MODELO,ID_SECCION,ORDEN) values ('15','2','7','9','2');
Insert into EDUFLOW.MODELO_SECCION (ID,ID_DB,ID_MODELO,ID_SECCION,ORDEN) values ('16','2','7','6','3');
Insert into EDUFLOW.MODELO_SECCION (ID,ID_DB,ID_MODELO,ID_SECCION,ORDEN) values ('17','2','7','9','4');
REM INSERTING into EDUFLOW.SECCION
SET DEFINE OFF;
Insert into EDUFLOW.SECCION (ID,ID_DB,FILENAME,DEPENDENCIAS,TXT) values ('9','2','sssss',null,null);
Insert into EDUFLOW.SECCION (ID,ID_DB,FILENAME,DEPENDENCIAS,TXT) values ('6','2','aaaa','bbb','Hola


esto es una prueba


d
fd
fdf
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd gjfd lgjfdlg fdklg jfldkg jfdklgj fldgj fdgjfld gjkfldsg jfdkl gjkfldg jfldk glkfds
fdasffdsfdsfdfds fds fds fdsa fasdfadsf adsfads g lfg fjdgl fjgl fjdlgjf lgfjl gfjd glfjdlkg fjdklg fdslkg jfdlkgjfd');
Insert into EDUFLOW.SECCION (ID,ID_DB,FILENAME,DEPENDENCIAS,TXT) values ('10','1','aaaaaaa',null,null);
--------------------------------------------------------
--  Constraints for Table G_SERVICES
--------------------------------------------------------

  ALTER TABLE "EDUFLOW"."G_SERVICES" MODIFY ("TABLE_NAME" NOT NULL ENABLE);
