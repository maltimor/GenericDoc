package es.maltimor.genericDoc.dao;

import java.util.Map;

import es.maltimor.genericUser.User;

public interface GenericDocServiceDao {
	public Map<String,Object> newSeccion(User user,Map<String,Object> data) throws Exception;
	public void getPDF(String dbid,String unid,String name,String table) throws Exception;
	public void actualizarModelo(String dbid, String unid,String fileName) throws Exception;
	public Map<String,Object> instanciarModelo(String dbid, String unid,String fileName,Map<String,Object> data) throws Exception;
}
