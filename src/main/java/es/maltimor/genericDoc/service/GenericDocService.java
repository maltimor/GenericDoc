package es.maltimor.genericDoc.service;


import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import es.maltimor.genericDoc.dao.GenericDocServiceDao;
import es.maltimor.genericUser.User;
import es.maltimor.genericUser.UserDao;

@Path("/genericDocService/")
@Consumes("application/json")
@Produces("application/json")
public class GenericDocService {
	private GenericDocServiceDao service;
	private UserDao userDao;
	
	public GenericDocServiceDao getService() {
		return service;
	}
	public void setService(GenericDocServiceDao service) {
		this.service = service;
	}
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@POST
	@Path("/getPDF/{dbid}/{unid}")
	public Response getAttachment(@PathParam("dbid") String dbid,@PathParam("unid") String unid,
			@QueryParam("name") String name, @QueryParam("table") String table, @QueryParam("addPDF") boolean addPDF, @Context ServletContext context){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			service.getPDF(dbid,unid,name,table,addPDF);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().type("application/text").entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/actualizaModelo/{dbid}/{unid}")
	public Response actualizaModelo(@PathParam("dbid") String dbid,@PathParam("unid") String unid,
			@QueryParam("name") String name, @Context ServletContext context){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			service.actualizarModelo(dbid,unid,name);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().type("application/text").entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/actualizaModeloSeccion/{dbid}/{unid}")
	public Response actualizaModeloSeccion(@PathParam("dbid") String dbid,@PathParam("unid") String unid,
			@QueryParam("name") String name, @Context ServletContext context){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			service.actualizarModeloSeccion(dbid,unid,name);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().type("application/text").entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/instanciarModelo/{dbid}/{unid}")
	public Response instanciarModelo(@PathParam("dbid") String dbid,@PathParam("unid") String unid,
			@QueryParam("name") String name, Map<String,Object> data, @Context ServletContext context){
		try {
			User user = userDao.getUser(userDao.getLogin(), "");
			if (!user.hasRol("ADMIN")) {
				System.out.println("---- no tiene permisos");
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("No tiene permisos").build();
			}
			Map<String,Object> res = service.instanciarModelo(dbid,unid,name,data);
			return Response.ok(res).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().type("application/text").entity(e.getMessage()).build();
		}
	}
	
	
/*	@POST
	@Path("newSeccion/{dbid}")
	public Response newSeccion(@PathParam("dbid") String dbid){
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
			} else return Response.serverError().type("application/text").entity("No se puede actualizar").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().type("application/text").entity(e.getMessage()).build();
		}		
	}*/
}
