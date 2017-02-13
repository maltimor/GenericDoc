package es.maltimor.genericDoc.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioServiceDaoImpl implements UsuarioServiceDao  {
	final Logger log = LoggerFactory.getLogger(UsuarioServiceDaoImpl.class);
	private UsuarioServiceMapper umapper;
	
	public String getDNI(String login) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("login",login);
		umapper.getDNI(params);
		return (String) params.get("out");
	}
	
	public String getEntorno() {
		Map<String,Object> params = new HashMap<String,Object>();
		umapper.getEntorno(params);
		return (String) params.get("out");
	}

	public String getNombreCompletoPersona(String login) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("login",login);
		umapper.getNombreCompletoPersona(params);
		return (String) params.get("out");
	}

	public UsuarioServiceMapper getUmapper() {
		return umapper;
	}
	public void setUmapper(UsuarioServiceMapper umapper) {
		this.umapper = umapper;
	}
}
