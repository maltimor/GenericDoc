package es.maltimor.genericDoc.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapUtils {
	final static Logger log = LoggerFactory.getLogger(MapUtils.class);

	public static Map<String, String> getMap2Map(Map<String,Object> map) {
		Map<String, String> res = new HashMap<String, String>();
		recorreMap(res,map,null);
		return res;
	}
	
	public static void recorreMap(Map<String,String> res, Map<String,Object> map, String prefijo) {
		for(String key:map.keySet()){
			Object val = map.get(key);
			key = prefijo!=null?prefijo+"."+key:key;
			asignaKeyValue(res,key,val);
		}
	}

	public static void recorreList(Map<String,String> res, List<Object> lst, String prefijo) {
		for(int i=0;i<lst.size();i++){
			Object val = lst.get(i);
			String key = prefijo!=null?prefijo+"["+i+"]":"["+i+"]";
			asignaKeyValue(res,key,val);
		}
	}
	
	public static void asignaKeyValue(Map<String,String> res, String key, Object val) {
		if (key==null){
		} else if (val==null){
			res.put(key,"NULL");
		} else if ((val instanceof String) || (val instanceof Integer) || (val instanceof Double) || (val instanceof Date)) {
			//TODO ver fechas
			res.put(key,val.toString());
		} else if (val instanceof Map){
			recorreMap(res,(Map)val, key);
		} else if (val instanceof List){
			recorreList(res,(List)val, key);
		} else res.put(key,val.getClass().getName()+"?->"+val.toString());
	}

	public static Map<String, String> getXML2Map(String cadenaXML,String charset) {
		//System.out.println("****************************");
		//System.out.println("XML2Map.getMap:" + cadenaXML);
		//System.out.println("****************************");
		Map<String, String> res = new HashMap<String, String>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(new ByteArrayInputStream(cadenaXML.getBytes(charset)));
			Node root = dom.getDocumentElement();
			int level = 0;
			recorreSubArbol(res, root, level, root.getNodeName());
		} catch (SAXException e) {
			log.error("SAXException\n" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException\n" + e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			log.error("ParserConfigurationException\n"+ e.getMessage());
			e.printStackTrace();
		}

		//System.out.println("****************************");
		//imprimeMap(res);
		//System.out.println("****************************");

		return res;
	}

	private static void recorreSubArbol(Map<String,String> map,Node nodoEntrada, int nivel,String formadorIdentificador) {
		nivel++;
		NodeList childNodes = nodoEntrada.getChildNodes();
		int numero = childNodes.getLength();
		for (int i = 0; i < numero; i++) {
			Node nodoActual = childNodes.item(i);

			if (nodoActual.hasChildNodes()&& !nodoActual.getNodeName().equalsIgnoreCase("#text")) {
				recorreSubArbol(map,nodoActual,nivel,formadorIdentificador+"."+nodoActual.getNodeName());
			} else {
				// Es un nodo hoja, entonces los añadimos al diccionario
				if (nodoActual.getTextContent().trim().length() > 0) {
					map.put(formadorIdentificador.toUpperCase(),nodoActual.getTextContent());
				}
			}
		}
	}
}
