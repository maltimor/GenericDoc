package es.maltimor.genericDoc.parser;

import java.util.List;
import java.util.Map;

import es.carm.mydom.DAO.DatabaseException;
import es.carm.mydom.entity.AttachService;
import es.carm.mydom.entity.Database;
import es.carm.mydom.entity.Document;
import es.carm.mydom.entity.DominoSession;
import es.carm.mydom.entity.View;

public class BasicDatabase implements Database {
	private Map<String,Object> data;
	private View view;
	
	public BasicDatabase(Map<String,Object> data){
		this.data = data;
		this.view = new BasicView(data);
	}
	
	//ESTE METODO NO HACE NADA, SOLO SE USA EN TGENERO
	public Document getDocumentByKey(String tableName, Object keys) throws DatabaseException {
		return null;
	}
	//SOLO IMPLEMENTO ID, EL RESTO SE USA EN TREPORT NO SOPORTADO HOY
	public View getView(DominoSession domSession, String name) throws DatabaseException {
		if (name.equals("ID")) return view;
		return null;
	}
	//NO HAGO NADA SOLO SE USA EN TREPORT
	public List<Document> getAllDocumentByKey(String tableName, Object keys) throws DatabaseException {
		return null;
	}

	
	//METODOS NO USADOS POR EL PARSER
	public String getName() {
		return null;
	}
	public String canonice(String tableName) {
		return null;
	}
	public Document getNewDocument() throws DatabaseException {
		return null;
	}
	public boolean isDocumentByUnid(String tableNamae, String unid) throws DatabaseException {
		return false;
	}
	public Document getDocumentByUnid(String tableName, String unid) throws DatabaseException {
		return null;
	}
	public void insert(String tableNamae, Document data) throws DatabaseException {
	}
	public void update(String tableNamae, Document data) throws DatabaseException {
	}
	public void save(String tableName, Document data) throws DatabaseException {
	}
	public void deleteByUnid(String tableName, String unid) throws DatabaseException {
	}
	public void computeWithForm(DominoSession domSession, String formName, Document data) throws DatabaseException {
	}
	public List<Document> getAll(String tableName) throws DatabaseException {
		return null;
	}
	public List<Document> getPage(String tableName, int page, int pageSize, String sortOrder, String where) throws DatabaseException {
		return null;
	}
	public int getCount(String tableName, String where) throws DatabaseException {
		return 0;
	}
	public AttachService getAttachService() throws DatabaseException {
		return null;
	}
}
