package es.maltimor.genericDoc.service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.map.ObjectMapper;

import es.maltimor.genericDoc.dao.AttachmentServiceDao;
import es.maltimor.genericDoc.dao.GenericDocServiceDao;
import es.maltimor.genericDoc.utils.Base64Coder;
import es.maltimor.genericDoc.utils.FilesUtils;
import es.maltimor.genericUser.User;
import es.maltimor.genericUser.UserDao;

@Path("/attachmentService/")
@Consumes("application/json")
@Produces("application/json")
public class AttachmentService {
	private AttachmentServiceDao service;
	private UserDao userDao;
	private String editAttachment;
	private String tempPath;
	private String urlWebDav;
	
	public AttachmentServiceDao getService() {
		return service;
	}
	public void setService(AttachmentServiceDao service) {
		this.service = service;
	}
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public String getUrlWebDav() {
		return urlWebDav;
	}
	public void setUrlWebDav(String urlWebDav) {
		this.urlWebDav = urlWebDav;
	}
	public String getTempPath() {
		return tempPath;
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public String getEditAttachment() {
		return editAttachment;
	}
	public void setEditAttachment(String editAttachment) {
		this.editAttachment = editAttachment;
	}

	@SuppressWarnings("restriction")
	@POST
	@Path("/addAttachment/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(List<Attachment> attachments, @Context HttpServletRequest request) {
		long start = System.currentTimeMillis();
		long aux1 = start;
		long fin = start;
		
		//comprobar la seguridad
		User user = new User();
		
		try {
			
			user = userDao.getUser(userDao.getLogin(), "");
			System.out.println("user.hasRole('ADMIN')......:" + user.hasRol("ADMIN"));
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			System.out.println("---- si tiene permisos");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//llamada al servicio
		try{
			System.out.println("REQUEST:");
			System.out.println("queryString:"+request.getQueryString());
			System.out.println("upload!!!!");

			//OJO los attachments contienen ademas los campos del formulario!!!!
			//recorro el array de attachements y voy rellenando el map
			es.maltimor.genericDoc.dao.Attachment attach=null;
			String dbid=null,unid=null;
			
			for (Attachment attr : attachments) {
				
				System.out.println("****"+attr.getContentDisposition().getParameters().toString());
				String fieldName = attr.getContentDisposition().getParameter("name");

				String fileName = attr.getContentDisposition().getParameter("filename");
				String mimeType = attr.getContentType().toString();
				DataHandler handler = attr.getDataHandler();
//				String mimeType = handler.getContentType();
//				String name = handler.getName();
				InputStream fileStream = handler.getInputStream();
				byte[] buff = new byte[fileStream.available()];
				int reads = fileStream.read(buff);
				fileStream.close();
				
				System.out.println("CAMPO:"+fieldName);
				System.out.println("FILE_NAME:"+fileName);
				System.out.println("MIME_TYPE:"+mimeType);
				
				//en funcion del nombre del attachemnt hago una cosa u otra
				if (fieldName.equals("dbid")) dbid = new String(buff);
				else if (fieldName.equals("unid")) unid = new String(buff);
				else if (fieldName.equals("attach")){
					attach = new es.maltimor.genericDoc.dao.Attachment();
					attach.setBytes(buff);
					attach.setName(fileName);
				}
			}
			
			aux1 = System.currentTimeMillis();
				
			//ahora pruebo a llamar al servicio
			service.addAttachment(dbid, unid, attach);
				
			fin = System.currentTimeMillis();
			String str = "* Total="+(fin-start)+" * preparar="+(aux1-start)+" * enviar="+(fin-aux1);
			
			System.out.println(str);
			
			return Response.ok("ok").build();
		
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/getAllAttachmentsNames/{dbid}/{unid}")
	public Response getAllAttachmentsNames(@PathParam("dbid") String dbid,@PathParam("unid") String unid){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			List<String> res = service.getAllAttachmentsNames(dbid, unid);
			return Response.ok(res).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path("/deleteAttachment/{dbid}/{unid}")
	public Response getAllAttachmentsNames(@PathParam("dbid") String dbid,@PathParam("unid") String unid, @QueryParam("name") String name){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			service.deleteAttachment(dbid, unid, name);
			return Response.ok("ok").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/viewAttachment/{dbid}/{unid}")
	public Response getAttachment(@PathParam("dbid") String dbid,@PathParam("unid") String unid,
			@QueryParam("name") String name, @Context ServletContext context){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			es.maltimor.genericDoc.dao.Attachment attach = service.getAttachment(dbid, unid, name);
			String mimeType = context.getMimeType(attach.getName());
			return Response.ok(attach.getBytes(),mimeType).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	
	@GET
	@Path("/editAttachment/{dbid}/{unid}")
	public Response editAttachment(@PathParam("dbid") String dbid,@PathParam("unid") String unid,
			@QueryParam("name") String name, @Context ServletContext context){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			es.maltimor.genericDoc.dao.Attachment attach = service.getAttachment(dbid, unid, name);
			String mimeType = context.getMimeType(attach.getName());

			//TODO A PIÑON METO EL ACTIVEX DE WScript
			//TODO Varias versiones:
			//TODO Desacoplar el tempPath y la ruta del webdav con la hubicacion del war
			//1º descargo el anexo al direcotrio temp y luego ya veo como recuperarlo I:doble guardar
			//1a la ruta comienza por el unid V:es determinista
			//1b la ruta es aleatoria I:no se como recoger el documento editado
			//		creo que se puede hacer asociando el unid a un unid aleatorio y guardando el par en cfg
			//que pasa si hay algun error al guardar? debo siempre extraer el file al temp? o no?
			//2º edito directamente el anexo V:un unico guardar I:seguridad?
			//------------------------------
			//hago la 1a
			String outputFile = tempPath + File.separator + dbid +File.separator + unid + File.separator+attach.getName();
			System.out.println("openElement/editElement: outputfile="+outputFile);
			FilesUtils.preparaPath(outputFile, true);
			attach.extractFile(outputFile);

			String urlDav = urlWebDav +"/"+dbid+"/"+unid+"/"+attach.getName();
			System.out.println("openElement/editElement: urlDav="+URLEncoder.encode(urlDav, "UTF-8").replace(" ","%20"));
			byte[] buff = FilesUtils.getBytesFromFile(editAttachment);
			String res = new String(buff,"UTF8");
			res=res.replace("$$", urlDav);
/*			String res="";
			res+="<html><head><script>\n";
			res+="function abre(prog,url){ var shell = new ActiveXObject(\"WScript.shell\"); shell.run(prog+\" \"+url); shell.Quit; shell = null; window.close();}\n";
			res+="</script>\n";
			res+="</head><body onload='abre(\"swriter.exe "+urlDav+"\",\"\")'>\n";
			res+="Su navegador no soporta ActiveX</body></html>";*/
			return Response.ok(res,"text/html").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/updateAttachment/{dbid}/{unid}")
	public Response updateAttachment(@PathParam("dbid") String dbid,@PathParam("unid") String unid,
			@QueryParam("name") String name, @Context ServletContext context){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			
			if (service.hasAttachment(dbid, unid, name)){
				String outputFile = tempPath + File.separator + dbid +File.separator + unid + File.separator+name;
				System.out.println("updateElement: outputfile="+outputFile);
				byte[] buff = FilesUtils.getBytesFromFile(outputFile);
				es.maltimor.genericDoc.dao.Attachment attach = new es.maltimor.genericDoc.dao.Attachment();
				attach.setBytes(buff);
				attach.setName(name);
				service.addAttachment(dbid, unid, attach);
				return Response.ok().build();
			} else return Response.serverError().entity("No se puede actualizar").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
}
