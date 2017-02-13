package es.maltimor.genericDoc.parser;

import java.util.List;
import java.util.Map;

import es.carm.mydom.DAO.DatabaseException;
import es.carm.mydom.entity.Document;
import es.carm.mydom.entity.View;
import es.carm.mydom.parser.ViewDef;

public class BasicView implements View {
	private Map<String,Object> data;
	
	public BasicView(Map<String,Object> data){
		this.data = data;
	}
	
	//TODO USADO EN INTERPRETA VALOR .
	public Document getDocumentByKey(Object keys) throws DatabaseException {
 		return null;
	}
	//USADO EN TREPORT
	public Document getFirstDocument() throws DatabaseException {
		return null;
	}
	//USADO EN TREPORT
	public Document getNextDocument() throws DatabaseException {
		return null;
	}	
	
	//METODOS NO USADOS POR EL PARSER
	public String getName() {
		return null;
	}
	public ViewDef getViewDef() {
		return null;
	}
	public int getCount() throws DatabaseException {
		return 0;
	}
	public int getIndex() throws DatabaseException {
		return 0;
	}
	public void setIndex(int index) throws DatabaseException {
	}
	public Document getNthDocument(int index) throws DatabaseException {
		return null;
	}
	public List<Document> getAll() throws DatabaseException {
		return null;
	}
	public List<Document> getAllDocumentByKey(Object keys) throws DatabaseException {
		return null;
	}
	public int getRowIndex(String key) throws DatabaseException {
		return 0;
	}
	public List<Document> getPage(int start, int count, String order, String where) throws DatabaseException {
		return null;
	}
	public int getCount(String where) throws DatabaseException {
		return 0;
	}
}
