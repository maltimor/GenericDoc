package es.maltimor.genericDoc.openOffice;

import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XEventListener;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.document.XDocumentInfoSupplier;
import com.sun.star.document.XDocumentInfo;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.util.XRefreshable;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextRange;
import com.sun.star.awt.XListBox;
import com.sun.star.text.XTextGraphicObjectsSupplier;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.drawing.XShape;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Enumeration;
import java.util.zip.*;
import java.io.*;
//import com.ibm.icu.text.*;
//jp
import java.text.SimpleDateFormat;
import java.io.DataOutputStream;
import java.util.Date;
import java.util.Arrays;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;







import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class TOpenOffice implements com.sun.star.lang.XEventListener {
	final Logger log = LoggerFactory.getLogger(TOpenOffice.class);
	//hacemos la clase TOpenOffice Singleton
	private XComponentLoader xcomponentloader;
	private String sConnectionString;
	private String identificador;
	private static TOpenOffice oo;

	//ahora el metodo es privado y no se puede crear instancias de TOpenOffice directamente
	public TOpenOffice(){
		oo = this;
	}
	
	public static TOpenOffice getOffice(){
		return oo;
	}

	public String getsConnectionString() {
		return sConnectionString;
	}

	public void setsConnectionString(String sConnectionString) {
		this.sConnectionString = sConnectionString;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	//trata de obtener de forma reiterada el xcomponentloader
	public synchronized void inicializaOO(){
		log.debug("OOOOOOO TOO:iniciliazaOO:id="+identificador+" xcomp="+xcomponentloader);
		if (xcomponentloader==null){
			int intentos = 5;
			while(--intentos>0){
				try{
					getComponentLoader();
					intentos=0;
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		log.debug("OOOOOOO TOO:iniciliazaOOFIN:id="+identificador+" xcomp="+xcomponentloader);
	}

	//obtiene el xcomponenloader una vez. intenta recuperarse de algunos errores
	private synchronized void getComponentLoader(){
		log.debug("OOOOOOO TOO:getComponentLoader:id="+identificador+" -sConnectionString:" + sConnectionString);
		try {

			if (sConnectionString == null)	
				sConnectionString = "uno:socket,host=localhost,port=8100,tcpNoDelay=1;urp;StarOffice.ServiceManager";
			
			//String sConnectionString = "uno:socket,host=localhost,port=8100,tcpNoDelay=1;urp;StarOffice.ServiceManager";
			
			/* Bootstraps a component context with the jurt base components
			registered. Component context to be granted to a component for running.
			Arbitrary values can be retrieved from the context. */
			XComponentContext xcomponentcontext = com.sun.star.comp.helper.Bootstrap.createInitialComponentContext( null );
			
			/* Gets the service manager instance to be used (or null). This method has
			been added for convenience, because the service manager is a often used
			object. */
			XMultiComponentFactory xmulticomponentfactory = xcomponentcontext.getServiceManager();
			
			/* Creates an instance of the component UnoUrlResolver which
			supports the services specified by the factory. */
			Object objectUrlResolver = xmulticomponentfactory.createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", xcomponentcontext );
			
			// Create a new url resolver
			XUnoUrlResolver xurlresolver = ( XUnoUrlResolver ) UnoRuntime.queryInterface( XUnoUrlResolver.class, objectUrlResolver );

			// Resolves an object that is specified as follow:
			// uno:<connection description>;<protocol description>;<initial object name>
			Object objectInitial = xurlresolver.resolve( sConnectionString );
			
			// Create a service manager from the initial object
			xmulticomponentfactory = ( XMultiComponentFactory ) UnoRuntime.queryInterface( XMultiComponentFactory.class, objectInitial );
			
			// Query for the XPropertySet interface.
			XPropertySet xpropertysetMultiComponentFactory = ( XPropertySet ) UnoRuntime.queryInterface( XPropertySet.class, xmulticomponentfactory );
			
			// Get the default context from the office server.
			Object objectDefaultContext = xpropertysetMultiComponentFactory.getPropertyValue( "DefaultContext" );
			
			// Query for the interface XComponentContext.
			xcomponentcontext = ( XComponentContext ) UnoRuntime.queryInterface(XComponentContext.class, objectDefaultContext );
			
			/* A desktop environment contains tasks with one or more
			frames in which components can be loaded. Desktop is the
			environment for components which can instanciate within
			frames. */
			xcomponentloader = ( XComponentLoader ) UnoRuntime.queryInterface( XComponentLoader.class,
				xmulticomponentfactory.createInstanceWithContext("com.sun.star.frame.Desktop", xcomponentcontext ) );

			//de mi cosecha. a�adi el event listener
			XComponent xComponent = (XComponent) UnoRuntime.queryInterface(XComponent.class, xcomponentloader);
			xComponent.addEventListener(this);
			
			// Borrar variables
			xcomponentcontext=null;
			xmulticomponentfactory=null;
			objectUrlResolver=null;
			xurlresolver =null;
			objectInitial=null;
			objectDefaultContext=null;
			System.gc();
			log.debug("OOOOOOO TOO:getComponentLoaderFIN:id="+identificador);
			//
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//de la interfaz listener
	public synchronized void disposing(com.sun.star.lang.EventObject event) {
		try {
			log.debug("OOOOOOO TOO:Disponsing!!!!:id="+identificador);
			xcomponentloader=null;
			System.gc();
			System.runFinalization();
			inicializaOO();
			log.debug("OOOOOOO TOO:DisponsingFIN!!!!:id="+identificador);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// comprueba si se cargo en memoria el entrono de office
	public synchronized boolean officeActivo(int identificador){
		if(xcomponentloader==null){
			return(false);
		}else return(true);
	}
	
	public synchronized XComponent instanciar(int identificador,String URL){
		log.info("OOOOOOO TOO:Instanciar:id="+identificador+" URL="+URL);
		XComponent xcomponent = null;
		try{
			//URL = URL.replace(" ", "%20");
			//log.debug("OOOOOOO TOO:Instanciar2:id="+identificador+" URL="+URL);
			
			// Create OOoInputStream
            InputStream inputFile = new BufferedInputStream(new FileInputStream(URL)); 
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] byteBuffer = new byte[4096];
            int byteBufferLength = 0;
            while ((byteBufferLength = inputFile.read(byteBuffer)) > 0) {
                    bytes.write(byteBuffer,0,byteBufferLength);
            }
            inputFile.close();
            log.debug("OOOOOOO TOO:Instanciar:bytes="+bytes.size());
            OOInputStream inputStream = new OOInputStream(bytes.toByteArray());
            log.debug("OOOOOOO TOO:Instanciar2:ok");
			
			
			PropertyValue[] props=new PropertyValue[2];
			props[0]=new PropertyValue();
			props[0].Name="InputStream";
			props[0].Value=inputStream;
			props[1]=new PropertyValue();
			props[1].Name="Hidden";
			props[1].Value=new Boolean(true);

			xcomponent = xcomponentloader.loadComponentFromURL( "private:stream", "_blank", 0, props);
//			xcomponent = xcomponentloader.loadComponentFromURL( URL, "_blank", 0, props);

			log.debug("OOOOOOO TOO:Instanciar3:id="+identificador+" xcomponet="+xcomponent);
			if(xcomponent==null){
				return null;
			}
			return xcomponent;
		}catch( Exception e ) { 
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized boolean destruir(int identificador){
		try{
			log.debug("OOOOOOO TOO:Destruir!!!!:id="+identificador);
			xcomponentloader=null;
			System.gc();
			System.runFinalization();
			return(true);
		} catch(Exception e) {
			e.printStackTrace();
			return(false);
		}
	}
	
	//trasnforma una url segun la extension
	//como precondicion SIEMPRE llega un .sxw 
	public synchronized String urlDestino(String url,String extension){
		return url.substring(0,url.length()-3)+extension.toLowerCase();
	}

	public boolean modificar(int identificador, String URL, XComponent xcomponent){
		XTextDocument mxDoc = null;
		XText mxDocText = null;
		XTextCursor mxDocCursor = null;
		String URLtemp=null;
		try{
			log.info("OOOOOOO TOO:Modificar!!!!:id="+identificador+" URL:"+URL);
			//REFACTORING
			try {
				// SI NO EXISTE EL DOCUMENTO SE LANZARA UNA EXCEPCION!
				URL = URI.create("file:///"+URL.replace(" ", "%20")).toString();
				Object obj = new URL(URL).getContent();
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
			log.info("OOOOOOO TOO:Modificar2!!!!:id="+identificador+" URL:"+URL);

			mxDoc = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xcomponent);
			mxDocText = mxDoc.getText();
			mxDocCursor = mxDocText.createTextCursor();
			mxDocCursor.gotoEnd( false );
			XDocumentInsertable xIns = (XDocumentInsertable) UnoRuntime.queryInterface(XDocumentInsertable.class, mxDocCursor); 
			xIns.insertDocumentFromURL(URL,new PropertyValue[0] );
/*            OOInputStream inputStream = new OOInputStream(bytes.toByteArray());
            log.debug("OOOOOOO TOO:Modificar2:ok");
			
			PropertyValue[] props=new PropertyValue[2];
			props[0]=new PropertyValue();
			props[0].Name="InputStream";
			props[0].Value=inputStream;
			props[1]=new PropertyValue();
			props[1].Name="Hidden";
			props[1].Value=new Boolean(true);
			xIns.insertDocumentFromURL("private:stream", props); */
			return(true);
		}catch(Exception e) {
			e.printStackTrace();
			return(false); 
		} 
	}

	private synchronized boolean guardarComo(int identificador, String extension, String url, XComponent xcomponent){
		String sFilter = null;
		try{
			log.info("OOOOOOO TOO:Guardar como:id="+identificador+" URL:"+url+" ext:"+extension);
			url=urlDestino(url,extension);
			if (url.startsWith("file:////")) url = url.substring(8);
			log.info("OOOOOOO TOO2:Guardar como:id="+identificador+" URL:"+url+" ext:"+extension);

			extension = extension.toUpperCase();
			// Find out possible filter name.
			if (extension.equals("HTML")){
				sFilter = new String("HTML (StarWriter)");
			} else if (extension.equals("TXT")){
				sFilter=new String("Text");
			} else if (extension.equals("PDF")){
				sFilter=new String("writer_pdf_Export");
			} else if (extension.equals("SXW")){
				sFilter= new String("StarOffice XML (Writer)");
			} else if (extension.equals("ODT")){
				sFilter= new String("writer8");
			} else if (extension.equals("CSV")){
				sFilter= new String("Text - txt - csv (StarCalc)");
			}

			if(sFilter!=null){
				int numVars = 4;
				if (extension.equals("CSV")) numVars=5;
				com.sun.star.beans.PropertyValue[] lProperties = new com.sun.star.beans.PropertyValue[numVars];
				lProperties[0] = new com.sun.star.beans.PropertyValue();
				lProperties[0].Name = "FilterName";
				lProperties[0].Value = sFilter;
				lProperties[1] = new com.sun.star.beans.PropertyValue();
				lProperties[1].Name = "Overwrite";
				lProperties[1].Value = Boolean.TRUE;
				lProperties[2] = new com.sun.star.beans.PropertyValue();
				//lProperties[2].Name = "FilterFlags";
				//lProperties[2].Name = "FilterOptions";
				lProperties[2].Name = "CharacterSet";
				lProperties[2].Value = "iso-8859-1";
				log.info("OOOOOOO TOO2:CHARSET ISO");
				
				
				//TODO HACER UNA NUEVA VARIABLE PARA INDICAR EL TIPO DE CHARSET POR DEFECTO PARA LOS MODELOS

				//xStore.storeToURL(url,lProperties);  
				OOOutputStream outputStream = new OOOutputStream();
				lProperties[3] = new com.sun.star.beans.PropertyValue();
				lProperties[3].Name = "OutputStream";
				lProperties[3].Value = outputStream;

				if (extension.equals("CSV")){
					lProperties[4] = new com.sun.star.beans.PropertyValue();
					lProperties[4].Name = "FilterOptions";
					lProperties[4].Value = "9,0,1,0";		//9=tab, 44=, 34="
				}

				com.sun.star.frame.XStorable xStore = (com.sun.star.frame.XStorable)
					UnoRuntime.queryInterface(com.sun.star.frame.XStorable.class, xcomponent);
				
				xStore.storeToURL("private:stream",lProperties);
				FileOutputStream outputFile = new FileOutputStream(url);
				outputFile.write(outputStream.toByteArray());
				outputFile.close();

				return(true);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return(false);
	}
	
	public synchronized boolean closeDocument(int identificador,XComponent xcomponent){
		try{
			log.debug("OOOOOOO TOO:closeDocument!!!!:id="+identificador);
			com.sun.star.frame.XModel xModel = (com.sun.star.frame.XModel)
				UnoRuntime.queryInterface(com.sun.star.frame.XModel.class, xcomponent);
			
			if(xModel!=null){
				com.sun.star.util.XCloseable xCloseable =(com.sun.star.util.XCloseable)
					UnoRuntime.queryInterface(com.sun.star.util.XCloseable.class,xModel);
				if(xCloseable!=null){
					xCloseable.close(true);
					return true;
				}
				
				com.sun.star.util.XModifiable xModify = (com.sun.star.util.XModifiable)
					UnoRuntime.queryInterface(com.sun.star.util.XModifiable.class, xModel);
				if (xModify!=null) xModify.setModified(false);

				xcomponent.dispose();
				return true;
			}

			com.sun.star.util.XModifiable xModify = (com.sun.star.util.XModifiable)
				UnoRuntime.queryInterface(com.sun.star.util.XModifiable.class, xModel);
			
			if (xModify!=null) xModify.setModified(false);

			xcomponent.dispose();
	
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean LScomponerOO(int identificador, String urldir, String[] listaCuerpos, String nombreArchivo){
		int longi= listaCuerpos.length;
		String id="";
		//REFACTORING
		//urldir = prefixUrlBase+urldir;
		String lcuerpos = "";
		for(String lc:listaCuerpos) lcuerpos+=lc+","; 
		log.info("OOOOOOO TOO:lsComponerOO:id="+identificador+" urlDir:"+urldir+" nombreArchivo:"+nombreArchivo+" listaCuerpos="+lcuerpos);
		XComponent xcomponent = null;

		if (longi<1) {
			return(false);
		}
		
		xcomponent = instanciar(identificador, urldir + listaCuerpos[0]);
		if (xcomponent==null){
			return(false);
		} 

		if (longi>1){
			for(int i=1;i<longi;i++){
				if(!modificar(identificador, urldir + listaCuerpos[i], xcomponent)){
					closeDocument(identificador, xcomponent);
					return(false); 
				} 
			}
		}

		// Guardar como sxw/odt en funcion de la terminacion del nombre del archivo
		String tipo = "SXW";
		if (nombreArchivo.toLowerCase().endsWith("sxw")) tipo = "SXW";
		else tipo = "ODT";

		if (!guardarComo(identificador, tipo, urldir+nombreArchivo, xcomponent)){
			closeDocument(identificador, xcomponent);
			return(false); 
		}

		if (!closeDocument(identificador, xcomponent)){
			return(false);
		}

		return(true);
	}
	
	//M�todo que genera los ficheros OO en el directorio temporal: .pdf, .txt, y .html
	//Nueva incorporacion CSV
	//public synchronized boolean LSgenerarArchivosOO(int identificador,String urldir, String nombreArchivo){
	public synchronized boolean LSgenerarArchivosOO(int identificador,String urldir, String nombreArchivo, String generarHTML, String generarPDF, String generarTXT, String generarCSV){
		log.info("OOOOOOO TOO:LSGenerar ArchivsOO:id="+identificador+" urldir="+urldir+" nombre="+nombreArchivo);
		String id = "";
		//REFACTORING
		//String url = prefixUrlBase + urldir + nombreArchivo;
		//url = url.replace("\\", "/");
		String url = urldir+nombreArchivo;
		
		XComponent xcomponent = null;
		
		long startTime = System.currentTimeMillis();
		long endTime;

		xcomponent = instanciar(identificador, url);
		if (xcomponent==null){
			return(false); 
		}

		endTime = System.currentTimeMillis();
		startTime=endTime;

		//tipo PDF
		if (generarPDF.equals("S")){
			if (!guardarComo(identificador, "pdf", url, xcomponent)){
				closeDocument(identificador, xcomponent);
				return(false); 
			}		

			endTime = System.currentTimeMillis();
			startTime=endTime;
		}else{
		}

		// tipo HTML
		if (generarHTML.equals("S")){
			if (!guardarComo(identificador, "html", url, xcomponent)){
				closeDocument(identificador, xcomponent);
				return(false); 
			}

			endTime = System.currentTimeMillis();
			startTime=endTime;
		}else{
		}

		// tipo TXT
		if (generarTXT.equals("S")){
			if (!guardarComo(identificador, "txt", url, xcomponent)){
				closeDocument(identificador, xcomponent);
				return(false); 
			}

			endTime = System.currentTimeMillis();
			startTime=endTime;
		}else{
		}

		// tipo CSV
		if (generarCSV.equals("S")){
			if (!guardarComo(identificador, "csv", url, xcomponent)){
				closeDocument(identificador, xcomponent);
				return(false); 
			}

			endTime = System.currentTimeMillis();
			startTime=endTime;
		}else{
		}

		if (!closeDocument(identificador, xcomponent)){
			return(false);
		}

		log.debug("OOOOOOO TOO:LSGenerar ArchivsOOFIN:id="+identificador+" urldir="+urldir+" nombre="+nombreArchivo);
		
		return(true);
	}
	
	
	//M�todo con sobrecarga de operadores
	public synchronized boolean LSgenerarArchivosOO(int identificador,String urldir, String nombreArchivo){
		return LSgenerarArchivosOO(identificador, urldir, nombreArchivo, "S", "S", "S", "N");
	}
	
	public boolean insertarFichero(int identificador,String path, String nombreArchivo,  String fichAInsertar, String nombreOrigen){
		try{
			int len,aux;
			FileInputStream inzip= new FileInputStream(path+nombreArchivo);
			FileOutputStream outzip= new FileOutputStream(path+"copia"+identificador+nombreArchivo);
			FileInputStream in= new FileInputStream(path+nombreOrigen);
			ZipEntry entrada=null;
			int i;
			ZipInputStream StrIn = new ZipInputStream(inzip);
			ZipOutputStream StrOut= new ZipOutputStream(outzip);
			entrada=StrIn.getNextEntry();
			int BUFFER=32000;
			byte buf[] = new byte[BUFFER];
			while(StrIn.available()!=0){
				StrOut.putNextEntry(entrada);
				if(entrada.toString().compareTo(fichAInsertar)!=0){
					i=StrIn.read();
					do{
						StrOut.write(i);
						i=StrIn.read();
					}while(i!=-1);
				}else{
					while ((len=in.available())!=0){
						if (len>BUFFER) len = BUFFER;
						aux = in.read(buf,0,len);
						StrOut.write(buf,0,aux);
					}
					
					/*while(in.available()!=0){
						StrOut.write(in.read());
					}*/
				}
				entrada=StrIn.getNextEntry();
				StrOut.closeEntry();
			} // while StrIn.available
			in.close();
			StrIn.close();
			StrOut.close();
			inzip.close();
			outzip.close();
			return(true);
		}catch(Exception e){
			e.printStackTrace();
			return(false);
		}
	}// insertar fichero
	
	
	/*v42_insertarFichero
		Descomprime el archivo sxw o zip $path/$nombreArchivo con el objetivo de sustituir algunas de sus entradas-zip 
		(archivos dentro del zip) por otros archivos especificados en el array $nombreArchivosAInsertar. 
		$nombreArchivosAInsertar debe contener archivos que deben existir en el $path.
		$path/$nombreArchivo -> contiene el sxw parseado.
	*/
	public boolean v42_insertarFichero(int identificador, String path, String nombreArchivo, String nombreArchivosAInsertar[]){
					
				
		try{
			//Control de par�metros
			if (path.equals("")){
				return(false);
			}
			if (nombreArchivo.equals("")){
				return(false);
			}
			if (nombreArchivosAInsertar.length <= 0){
				return(false);
			}									
											
						
			//Creamos el inzip de entrada para leer las entradas del sxw
			FileInputStream inZip= new FileInputStream(path+nombreArchivo);
			//ZipInputStream zStrIn = new ZipInputStream(new BufferedInputStream(inZip));
			ZipInputStream zStrIn = new ZipInputStream(new BufferedInputStream(inZip));
			//Creamos el zip de salida que contendr� con la ra�z "copia"+identificador+nombreArchivo.sxw
			FileOutputStream outZip= new FileOutputStream(path+"copia"+identificador+nombreArchivo);		
			ZipOutputStream zStrOut= new ZipOutputStream(new BufferedOutputStream(outZip));
			
			
			//Definimos unas variables para abrir y acceder a los archivos que se pasen en nombreArchivosAInsertar
			FileInputStream in = null;
			BufferedInputStream inb = null;
			
			//Definimos una variable para acceder a las entradas (archivos) de los zip															
			ZipEntry entrada = null;
			
			//Definimos un buffer para leer por bloques de forma m�s eficiente.
			int BUFFER = 32768;
			int count = 0;
			
			
			while((entrada = zStrIn.getNextEntry()) != null) {   				
   				
   				zStrOut.putNextEntry(entrada);   //Abrimos la nueva entrada en el zip de salida
   				
   				byte data[] = new byte[BUFFER];	 //Inicializamos el array que utilizaremos para leer y escribir datos en ficheros.
   				
   				//Para la entrada o fichero actual, si existe en el nombreArchivosAInsertar, actualizamos dicha entrada
   				//con el contenido del fichero f�sico almacenado en el sistema de ficheros dado por: path + nombreArchivosAInsertar[i]
   				int i = Arrays.binarySearch(nombreArchivosAInsertar, entrada.getName());
   				if (i >= 0){   					   					   					
   					
   					in= new FileInputStream(path + nombreArchivosAInsertar[i]);
   					inb = new BufferedInputStream(in, BUFFER);   					            		
            		while((count = inb.read(data, 0, BUFFER)) != -1) {
               			zStrOut.write(data, 0, count);
            		}
            		inb.close();
            		in.close();
   					   							               					   					
   				}else{ //Si la entrada no existe en el array la escribimos directamente en el zip de salida.
   					//Leemos datos de la entrada actual y los escribimos en destino   					
   					while ((count = zStrIn.read(data, 0, BUFFER)) != -1){
   						zStrOut.write(data, 0, count);
   					}   									
   				}
   				
   				zStrOut.closeEntry();
				 					   				   				
			}
						
			zStrOut.close();
			outZip.close();									
			zStrIn.close();			
			inZip.close();			
			
			return(true);
			
		}catch(Exception e){
			e.printStackTrace();
			return(false);
		}
	}
}

