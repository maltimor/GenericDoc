package es.maltimor.genericDoc.dao;

public interface UsuarioServiceDao {
	public String getDNI(String login);
	public String getNombreCompletoPersona(String login);
	public String getEntorno();
}
