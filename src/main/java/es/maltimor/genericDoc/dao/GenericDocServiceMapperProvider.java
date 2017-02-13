package es.maltimor.genericDoc.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import es.maltimor.genericRest.GenericMapperInfoColumn;
import es.maltimor.genericRest.GenericMapperInfoTable;
import es.maltimor.genericRest.GenericMapperInfoTableResolver;
import es.maltimor.genericUser.User;

public class GenericDocServiceMapperProvider {

	public String updateHTMLyTXT(Map<String,Object> params){
		//("UPDATE #{table} SET TXT=#{txt}, HTML=#{html} WHERE ID_DB=#{dbid} AND ID=#{unid}")
		String table = (String) params.get("table");
		table = table.replace("'", "");
		String sql = "UPDATE "+table+" SET TXT=#{txt}, HTML=#{html} WHERE ID_DB=#{dbid} AND ID=#{unid}";
		return sql;
	}

}
