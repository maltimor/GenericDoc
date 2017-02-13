package es.maltimor.genericDoc.openOffice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GetPDF2 {
	final static Logger log = LoggerFactory.getLogger(GetPDF2.class);
	public static boolean ejecutar(TOpenOffice open,int id,String tmpdir, String nombreArchivo, String insertarContentYStyles, String generarHTML, String generarPDF, String generarTXT, String generarCSV){
		try{
			log.info("OOOOOOOO GETPDF2.ejecutar>tmp="+tmpdir+"| nombre="+nombreArchivo);
			
			if (!open.officeActivo(id)){
				log.debug("No se ha podido crear el objeto OpenOffice, compruebe que este corriendo el servicio en el puerto 8100 del servidor ");
				return false;
			}
			
			
//			if (insertarContentYStyles == "S"){
//				//Mejora 1: se realizan las dos llamadas a insertarFichero en una sola, utilizando la nueva función
//				//v42_insertarFichero.
//				String arrayStrings[];
//				arrayStrings = new String[2]; 
//				arrayStrings[0] = "styles.xml";
//				arrayStrings[1] = "content.xml";
//				
//				/*
//				Desempaqueta el archivo nombreArchivo.sxw en tmpdir y sustituye los ficheros desempaquetados cuyos nombres
//				vienen especificados en arrayStrings, por otros ficheros que ya deben existir en tmpdir y cuyos nombres
//				se corresponden con los especificados en arrayStrings			
//				*/
//				//8/05/2007 Hacerlo opcional por parámetro
//				if (!open.v42_insertarFichero(id,tmpdir, nombreArchivo, arrayStrings)){
//					out.println("Servlet.GetPDF#v42_insertarFichero: Error al empaquetar el fichero styles.xml o content.xml parseado en el MODELO.sxw");
//					log_Log(id,"Error al empaquetar el fichero styles.xml o content.xml parseado en el MODELO.sxw");
//					return;
//				}
//		    
//			}
		    
			/*Genera los archivos HTML, PDF y TXT en tmpdir, en base al MODELO.sxw	*/
			//8/05/2007 pasar los parámetros por url
			if (!open.LSgenerarArchivosOO(id,tmpdir, nombreArchivo, generarHTML, generarPDF, generarTXT, generarCSV)){ //generamos HTML, PDF y TXT y CSV?
				log.debug("Servlet.GetPDF#LSgenerarArchivosOO: Error al generar los PDF y HTML");
				return false;
			}
			
			log.debug("<hr>FIN<br>");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
