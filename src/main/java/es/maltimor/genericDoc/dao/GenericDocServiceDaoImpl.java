package es.maltimor.genericDoc.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.maltimor.genericDoc.openOffice.ComponerOO2;
import es.maltimor.genericDoc.openOffice.Compressor;
import es.maltimor.genericDoc.openOffice.GetPDF2;
import es.maltimor.genericDoc.openOffice.TOpenOffice;
import es.maltimor.genericDoc.parser.BasicParser;
import es.maltimor.genericDoc.utils.FilesUtils;
import es.maltimor.genericRest.GenericServiceDao;
import es.maltimor.genericUser.User;

public class GenericDocServiceDaoImpl implements GenericDocServiceDao {
	private AttachmentServiceDao attachService;
	private GenericDocServiceMapper mapper;
	private GenericServiceDao gservice;
	private String tempPath;
	private static String separator = File.separator;
	
	
	public AttachmentServiceDao getAttachService() {
		return attachService;
	}
	public void setAttachService(AttachmentServiceDao attachService) {
		this.attachService = attachService;
	}
	public GenericServiceDao getGservice() {
		return gservice;
	}
	public void setGservice(GenericServiceDao gservice) {
		this.gservice = gservice;
	}
	public String getTempPath() {
		return tempPath;
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public GenericDocServiceMapper getMapper() {
		return mapper;
	}
	public void setMapper(GenericDocServiceMapper mapper) {
		this.mapper = mapper;
	}

	public Map<String, Object> newSeccion(User user, Map<String, Object> data) throws Exception {
		return null;
	}

	public void getPDF(String dbid,String unid,String name,String table,boolean addPDF) throws Exception {
		//-------------------------------------------
		// 4� Llamada a un servlet para generar el PDF, el HTML y el TXT en base al sxw, o bien, alguno de los tres.
		//-------------------------------------------
		TOpenOffice too = TOpenOffice.getOffice();
		too.inicializaOO();
		
		//descargo el adjunto en el temporal
		Attachment attach = attachService.getAttachment(dbid, unid, name);
		if (attach==null) throw new Exception("No existe el adjunto");
		
		String path = tempPath+separator+dbid+separator+unid+separator;
		attach.extractFile(path+name);

		//C�digo que llama al servidor de tomcat y ejecuta el servlet de generacion del PDF, HTML y TXT (ademas inserta el content y el styles)
		if (GetPDF2.ejecutar(too, 0, path, name, "", "S", "S", "S", "N")){
			int i1 = name.lastIndexOf(".");
			if (i1==-1) i1=name.length();
			String nameTXT = name.substring(0,i1)+".txt";
			String nameHTML = name.substring(0,i1)+".html";
			String namePDF = name.substring(0,i1)+".pdf";
			
			System.out.println("updateElement: outputfile="+path + nameTXT);
			byte[] buff = FilesUtils.getBytesFromFile(path + nameTXT);
			String txt = new String(buff);

			System.out.println("updateElement: outputfile="+path + nameHTML);
			buff = FilesUtils.getBytesFromFile(path + nameHTML);
			String html = new String(buff);
			System.out.println("updateElement....");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("table", table);
			params.put("dbid", dbid);
			params.put("unid", unid);
			params.put("txt", txt.substring(0,txt.length()<4000?txt.length():4000));
			params.put("html", html);
			mapper.updateHTMLyTXT(params);

			if (addPDF){
				buff = FilesUtils.getBytesFromFile(path + namePDF);
				attach = new Attachment();
				attach.setBytes(buff);
				attach.setName(namePDF);
				attachService.addAttachment(dbid, unid, attach);
			}

		} else throw new Exception("No se ha podido generar el PDF");
	}

	public void actualizarModelo(String dbid, String unid, String fileName) throws Exception {
		//extraigo los documentos de las secciones que componen el modelo
		//llamo a modificar para concatenarlos
		//adjunto el resultado en el modelo
		String basePath = tempPath+separator+dbid+separator+unid+separator;
		basePath=basePath.replace("\\", "/");
		String listaCuerpos = "";
		
		System.out.println("Actualiza Modelo:"+dbid+" "+unid+" "+fileName+" "+basePath);
		
		List<Map<String,Object>> secciones = gservice.getAll(null, "MODELO_SECCION", "[ID_DB]=="+dbid+" AND [ID_MODELO]=="+unid, 99999, 0, "ORDEN", "ASC", "*", null);
		if (secciones==null || secciones.size()==0) {
			//bueno si el modelo no tiene secciones no hago nada
			return;
			//throw new Exception("El modelo no tiene secciones");
		}

		System.out.println("Secciones:"+secciones);

		for(Map<String,Object> seccion:secciones){
			String unid_seccion = (String) seccion.get("ID_SECCION");
			List<String> lnames = attachService.getAllAttachmentsNames(dbid, unid_seccion);
			System.out.println("lnames:"+lnames);
			for(String name:lnames){
				//me quedo con la que es el odt
				if (name.toLowerCase().endsWith(".odt")){
					Attachment att = attachService.getAttachment(dbid, unid_seccion, name);
					if (att==null) throw new Exception("No puedo descargar el adjunto");
					String fileName_seccion = unid_seccion+name;
					att.extractFile(basePath+fileName_seccion);
					if (!listaCuerpos.equals("")) listaCuerpos+="~";
					listaCuerpos+=fileName_seccion.replace("\\", "/");
				}
			}
		}
		
		TOpenOffice too = TOpenOffice.getOffice();
		too.inicializaOO();
		
		fileName+=".odt";
		
		if (ComponerOO2.yoCompongo(too, 1, basePath, listaCuerpos, fileName)){
			//me cargo todos los adjuntos del modelo e inserto este nuevo
			attachService.deleteAllAttachments(dbid, unid);
			//ahora inserto el adjunto
			byte[] buff = FilesUtils.getBytesFromFile(basePath+fileName);
			Attachment attach = new Attachment();
			attach.setBytes(buff);
			attach.setName(fileName);
			attachService.addAttachment(dbid, unid, attach);
		} else throw new Exception("No se ha podido generar el modelo");
	}

	public void actualizarModeloSeccion(String dbid, String unid, String fileName) throws Exception {
		//busco todos los modelos afectados por esta seccion y los actualizo
		System.out.println("Actualiza Modelo Seccion:"+dbid+" "+unid+" "+fileName);
		
		List<Map<String,Object>> modelos = gservice.getAll(null, "MODELO_SECCION", "[ID_DB]=="+dbid+" AND [ID_SECCION]=="+unid, 99999, 0, "ORDEN", "ASC", "*", null);
		if (modelos==null || modelos.size()==0) {
			//bueno si el modelo no tiene secciones no hago nada
			return;
		}

		System.out.println("Modelos:"+modelos);
		for(Map<String,Object> modelo:modelos){
			//localizo el modelo para obtener su nombre
			String idModelo = (String) modelo.get("ID_MODELO");
			if (idModelo!=null){
				Map<String, Object> m = gservice.getById(null, "MODELO", idModelo , null);
				System.out.println("Actualiza Modelo:"+dbid+" "+idModelo+" "+(String) m.get("FILENAME"));
				actualizarModelo(dbid, idModelo, (String) m.get("FILENAME"));
			}
		}
	}
	
	public Map<String,Object> instanciarModelo(String dbid, String unid, String fileName, Map<String,Object> data) throws Exception {
		//creo el documento
		//extraigo el documento del modelo, lo parseo y lo devuelvo los documentos de las secciones que componen el modelo
		//adjunto el resultado en el modelo
		System.out.println("@@@@@@ Instanciar:"+dbid+" "+unid+" "+fileName+" "+data);

		//obtengo un numero de secuencia
		String unid_doc = mapper.getNextVal();
				
		//extraigo el documento del modelo, lo parseo y lo devuelvo los documentos de las secciones que componen el modelo
		//se supone que hay uno y lo tomo como valido
		if (!attachService.hasAttachments(dbid, unid)) {
			//intento preparar el modelo
			actualizarModelo(dbid, unid, fileName);
//			throw new Exception("El modelo no est� preparado");
		}
		
		//calculo de nuevo si el modelo tiene adjuntos
		boolean hasAttachments = attachService.hasAttachments(dbid, unid);

		byte[] buff = null;
		String basePath = tempPath+separator+dbid+separator+unid_doc+separator;
		basePath=basePath.replace("\\", "/");
		String fileNameDoc = basePath+fileName+".odt";		//TODO poner algo en funcion de parametros del modelo
		//String fileNamePDF = basePath+fileName+".pdf";

		//si tiene adjuntos intento componer un modelo
		if (hasAttachments){
	
			for(String name:attachService.getAllAttachmentsNames(dbid, unid)){
				Attachment att = attachService.getAttachment(dbid, unid, name);
				if (att==null) throw new Exception("No encuentro el adjunto asociado al modelo");
				att.extractFile(fileNameDoc);
				break;
			}
			
			//lo parseo TODO
			//hago una copia del original
			FilesUtils.fileCopy(fileNameDoc, basePath+fileName+"ORG.odt");
			
			//lo descomprimo
			String filePath = basePath+"temp/";
			Compressor.unzip(filePath, fileNameDoc);
			//me traigo los dos ficheros
			FilesUtils.fileCopy(filePath + "styles.xml",  basePath + "stylesORG.xml");
			FilesUtils.fileCopy(filePath + "content.xml", basePath + "contentORG.xml");

			//creo el objeto parser que ser� comun en los dos ficheros
			BasicParser parser = new BasicParser();
			
			//parseo los dos ficheros
			//styles.xml
			buff = FilesUtils.getBytesFromFile(basePath + "stylesORG.xml");
			if (buff!=null){
				String txt = new String(buff,"UTF-8");		//TODO ver la codificacion
				txt = parser.parse(txt,data);
				FilesUtils.sendString(basePath + "styles.xml", txt, "UTF-8");
			} else throw new Exception("No se ha podido componer Styles");
			
			//content.xml
			buff = FilesUtils.getBytesFromFile(basePath + "contentORG.xml");
			if (buff!=null){
				String txt = new String(buff,"UTF-8");		//TODO ver la codificacion
				txt = parser.parse(txt,data);
				FilesUtils.sendString(basePath + "content.xml", txt, "UTF-8");
			} else throw new Exception("No se ha podido componer Content");
			
			//vuelco los dos ficheros
			FilesUtils.fileCopy(basePath + "styles.xml",  filePath + "styles.xml");
			FilesUtils.fileCopy(basePath + "content.xml", filePath + "content.xml");
			
			//los vuelvo a comprimir
			Compressor.zip(filePath, basePath, fileName+".odt");
		} //fin de hasAttachments
		
		//creo el documento tenga o no adjuntos
		System.out.println("### CReando el documento:"+dbid+"-"+unid_doc+"-"+unid+"-"+fileName);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dbid",dbid);
		params.put("unid_doc",unid_doc);
		params.put("unid_modelo",unid);
		params.put("fileName",fileName);
		mapper.insertDoc(params);
		
		//si el modelo tenia adjuntos, lo adjunto al documento
		if (hasAttachments){

			buff = FilesUtils.getBytesFromFile(fileNameDoc);
			Attachment attach = new Attachment();
			attach.setBytes(buff);
			attach.setName(fileNameDoc);
			attachService.addAttachment(dbid, unid_doc, attach);
			
			getPDF(dbid,unid_doc,fileName+".odt","DOC",false);

		}

/*		//calculo el html y el txt
		getPDF(dbid,unid_doc,fileName+".odt","DOC");

		buff = FilesUtils.getBytesFromFile(fileNamePDF);
		attach = new Attachment();
		attach.setBytes(buff);
		attach.setName(fileNamePDF);
		attachService.addAttachment(dbid, unid_doc, attach);*/
		
		//devuelvo el documento recien insertado
//		params = new HashMap<String,Object>();
//		params.put("dbid",dbid);
//		params.put("unid",unid_doc);
//		Map<String,Object> doc = mapper.getDoc(params);

//		Map<String,Object> doc = gservice.getById(null, "DOC", "[ID_DB]=="+dbid+" AND [ID]=="+unid_doc, null);
		Map<String,Object> doc = gservice.getById(null, "DOC", unid_doc, null);
		if (doc==null) throw new Exception("No se ha podido localizar el documento");
		return doc;
	}

}
