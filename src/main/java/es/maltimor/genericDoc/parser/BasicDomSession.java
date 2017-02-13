package es.maltimor.genericDoc.parser;

import java.io.Writer;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.carm.mydom.entity.Database;
import es.carm.mydom.entity.Document;
import es.carm.mydom.entity.DominoBean;
import es.carm.mydom.entity.DominoSession;
import es.carm.mydom.entity.ServerDao;
import es.carm.mydom.entity.View;
import es.carm.mydom.parser.BeanMethod;
import es.carm.mydom.parser.ParserException;
import es.carm.mydom.parser.ViewDef;
import es.carm.mydom.security.UserInfo;
import es.carm.mydom.utils.URLComponents;

public class BasicDomSession implements DominoSession {
	private Database database;
	private ServerDao serverDao;
	private Map<String,Object> data;
	private Document doc;
	
	public BasicDomSession(Map<String,Object> data){
		this.database = new BasicDatabase(data);
		this.serverDao = new BasicServerDao();
		this.data = data;
		this.doc = new Document();
		try {
			doc.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Database getDatabase() {
		return database;
	}
	public Document getDocumentContext() {
		return doc;
	}
	public ServerDao getServerDao() {
		return serverDao;
	}

	
	//METODOS NO USADOS POR EL PARSER
	public Object execute(BeanMethod beanMethod) throws ParserException {
		return null;
	}
	public boolean executeCondition(BeanMethod beanMethod) throws ParserException {
		return false;
	}
	public void executeAction(BeanMethod beanMethod) throws ParserException {
	}
	public String executeGet(BeanMethod beanMethod) throws ParserException {
		return null;
	}
	public List<String> executeGetList(BeanMethod beanMethod) throws ParserException {
		return null;
	}
	public boolean executeAgent(BeanMethod beanMethod, HttpServletRequest request, HttpServletResponse response) throws ParserException {
		return false;
	}
	public String executeGetColumn(BeanMethod beanMethod, Document doc) throws ParserException {
		return null;
	}
	public ViewDef getViewDef(String name) {
		return null;
	}
	public View getCurrentView() {
		return null;
	}
	public Writer getLog(String ruta) {
		return null;
	}
	public Map<String, DominoBean> getBeans() {
		return null;
	}
	public DominoBean getBean(String bean) {
		return null;
	}
	public Object getDao(String dao) {
		return null;
	}
	public Map<String, Object> getDaos() {
		return null;
	}
	public URLComponents getUrlComponents() {
		return null;
	}
	public String registerBeanMethod(BeanMethod beanMethod) {
		return null;
	}
	public UserInfo getUserInfo() {
		return null;
	}
	public void setDocumentContext(Document documentContext) {
	}
	public void setCurrentView(View currentView) {
	}
}
