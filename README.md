[![GitHub release](https://img.shields.io/github/release/maltimor/GenericDoc.svg)]()

# GenericDoc.
Autor: Andrés García Meroño

Fecha: 22/11/2018

## Índice de contenido

## Introducción

Generic Doc es una herramienta que permite la generación de documentos OpenOffice/LibreOffice o PDF mediante la combinación de modelos o plantillas con un jsonde datos.

La idea principal es que los modelos están compuestos por secciones lo que permite su reutilización para la composición de otros modelos.

Una sección es un documento Open/Libre Office que contiene parte del documento que se quiere configurar. Además de texto, tablas, imágenes (toda la funcionalidad que ofrece el editor de textos), se insertan cadenas de texto con una sintaxis especial que es la que permite la combinación de datos con el texto que se está formando.

La sintaxis va a permitir:

* Recuperar datos del json y combinarlos en el documento
* Transformar los datos y darles formato: expresiones, fechas, currency.
* Iterar sobre un conjunto de datos: bucles, tablas, informes.
* Ejecuciones condicionales de código: if-else
* declaración de otros datos a partir de los iniciales: sumas, totales....


## Alta de un servicio

La pantalla de un servicio facilita su mantenimiento. Desde aquí se define el servicio REST, su
comportamiento y la seguridad de acceso al servicio como de los datos a los que se puede acceder.

![GitHub Logo](http://www.subirimagenes.com/imagedata.php?url=http://s2.subirimagenes.com/otros/9649202imagenservicio.jpg)

A continuación se describe cada uno de los campos:

* TABLE_NAME: nombre público del servicio. Es la etiqueta con la que se identifica cada servicio.
