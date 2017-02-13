package es.maltimor.genericDoc.parser;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import es.carm.mydom.entity.ServerDao;
import es.carm.mydom.parser.BeanMethod;
import es.carm.mydom.parser.Field;
import es.carm.mydom.parser.ViewDef;

public class BasicServerDao implements ServerDao {
	public List<Field> getForm(String name) {
		return null;
	}

	//METODOS NO USADOS POR EL PARSER
	public String registerAction(BeanMethod beanMethod) {
		return null;
	}
	public Writer getLog(String ruta) {
		return null;
	}
	public ViewDef getViewDef(String name) {
		return null;
	}
	public String getTempPath() {
		return null;
	}
	public String getResourcePath() {
		return null;
	}
	public String getResourceCharset() {
		return null;
	}
	public String getDocumentCharset() {
		return null;
	}
	public String getDomainProxy() {
		return null;
	}
	public Object getEnvProperty(String prp) {
		return null;
	}
	public int getDefaultResults() {
		return 0;
	}
	public int getMaxResults() {
		return 0;
	}
}
