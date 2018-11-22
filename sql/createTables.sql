drop table DOC;
drop table SECCION;
drop table MODELO;
drop table MODELO_SECCION;
drop table DATABASE;
drop sequence SEQ_GENERIC;

create table DOC (
	ID varchar2(256),
	ID_DB varchar2(256),
	ID_MODELO varchar2(256),
	FILENAME varchar2(256),
	TXT clob,
	HTML clob
);

create table SECCION (
	ID varchar2(256),
	ID_DB varchar2(256),
	FILENAME varchar2(1024),
	DEPENDENCIAS varchar2(4000),
	TXT clob,
	HTML clob
	CREATION date DEFAULT SYSDATE, 
	MODIFIED date DEFAULT SYSDATE);

create table MODELO (
	ID varchar2(256),
	ID_DB varchar2(256),
	FILENAME varchar2(1024),
	DATA_EXAMPLE clob,
	TXT clob,
	HTML clob,
	ENTRADASALIDADEFECTO varchar2(256), 
	ESTADODEFECTO varchar2(256), 
	ESTADOS varchar2(1024), 
	TIPO varchar2(256), 
	ICONODEFECTO number, 
	PARCIAL varchar2(256), 
	CREATION date DEFAULT SYSDATE, 
	MODIFIED date DEFAULT SYSDATE	
);

create table MODELO_SECCION (
	ID_DB varchar2(256),
	ID_MODELO varchar2(256),
	ID_SECCION varchar2(256),
	ORDEN NUMBER
);

create table DATABASE (
	ID varchar2(256),
	FILENAME varchar2(1024)
);

create sequence "SEQ_GENERIC" minvalue 1 maxvalue 99999999 increment by 1 start with 1 nocache order nocycle;
