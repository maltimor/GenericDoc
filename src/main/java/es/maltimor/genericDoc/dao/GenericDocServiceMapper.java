package es.maltimor.genericDoc.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

public interface GenericDocServiceMapper {
	//@UpdateProvider("UPDATE #{table} SET TXT=#{txt}, HTML=#{html} WHERE ID_DB=#{dbid} AND ID=#{unid}")
	@UpdateProvider(type=es.maltimor.genericDoc.dao.GenericDocServiceMapperProvider.class, method="updateHTMLyTXT")
	public void updateHTMLyTXT(Map<String,Object> params);
	
	@Insert("INSERT INTO DOC (ID,ID_DB,ID_MODELO,FILENAME) VALUES (#{unid_doc},#{dbid},#{unid_modelo},#{fileName})")
	public void insertDoc(Map<String,Object> params);

	@Select("SELECT SEQ_GENERIC.NEXTVAL FROM DUAL")
	public String getNextVal();

	@Select("SELECT * FROM DOC WHERE ID_DB=#{dbid} AND ID=#{unid}")
	public Map<String,Object> getDoc(Map<String,Object> params);
}
