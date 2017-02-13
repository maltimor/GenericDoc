package es.maltimor.genericDoc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface UsuarioServiceMapper {
	@Select("SELECT * FROM DUAL")
	public List<Map<String,Object>> genericResponse();

	//PACKAGES EXTERNOS
	@Select("{#{out, jdbcType=VARCHAR, mode=OUT} = call CATALOGO.PCONSTANTE.vEntornoBD}")
	@Options(statementType = StatementType.CALLABLE)
	public Object getEntorno(Map<String,Object> params);

	@Select("{#{out, jdbcType=VARCHAR, mode=OUT} = call CATALOGO.EDULDAP.vObtenerDNI(#{login})}")
	@Options(statementType = StatementType.CALLABLE)
	public Object getDNI(Map<String,Object> params);
	
	@Select("{#{out, jdbcType=VARCHAR, mode=OUT} = call CATALOGO.EDULDAP.vNombreCompletoPersona(#{login})}")
	@Options(statementType = StatementType.CALLABLE)
	public Object getNombreCompletoPersona(Map<String,Object> params);
}
