package es.maltimor.genericDoc.openOffice;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ComponerOO2 {
	final static Logger log = LoggerFactory.getLogger(ComponerOO2.class);
	public static boolean yoCompongo(TOpenOffice open,int id,String tmpdir, String listaCuerpos, String nombreArchivo){				
		//El parámetro listaCuerpos es una cadena con todos los nombres de los cuerpos separados por comas ","
		log.info("OOOOOOOO ComponerOO2.yoCompongo>tmp="+tmpdir+"| nombreArchivo="+nombreArchivo+" | listaArchivos="+listaCuerpos);
		String arrayListaCuerpos[];
		
		int numCuerpos;
		if (listaCuerpos.equals("")) return false;
		
		try{
			log.debug("tmp="+tmpdir+" nombre="+nombreArchivo+" listaCuerpos="+listaCuerpos);
			log.debug(" **** Antes del split **** ");
			arrayListaCuerpos = listaCuerpos.split("~");
			log.debug(" **** Después del split **** ");
			numCuerpos = arrayListaCuerpos.length;
			
			log.debug("\n\nSalida del Servlet ComponerOO\n");
			log.debug("TMP=" + tmpdir + "\n");
			log.debug("LISTA CUERPOS=\n");
			for (int i = 0; i < numCuerpos; i++){
				log.debug("	-" + arrayListaCuerpos[i] + "**\n");
			}			
			log.debug("NOMBRE SALIDA=" + nombreArchivo + "**\n");

			if (!open.officeActivo(id)){
				log.debug("No se ha podido crear el objeto OpenOffice, compruebe que este corriendo el servicio en el puerto 8100 del servidor ");
				return false;
			}

			if (!open.LScomponerOO(id,tmpdir, arrayListaCuerpos, nombreArchivo)){
				log.debug("No se han podido componer los cuerpos del documento en función ComponerOO.yoCompongo.");
				return false;
			}
			
			//Extraemos también el styles.xml y el content.xml
			log.debug("<hr>FIN<br>");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
}