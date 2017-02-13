package es.maltimor.genericDoc.parser;

import java.io.Writer;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.carm.mydom.entity.Database;
import es.carm.mydom.entity.Document;
import es.carm.mydom.entity.DominoBean;
import es.carm.mydom.entity.DominoSession;
import es.carm.mydom.entity.ServerDao;
import es.carm.mydom.entity.View;
import es.carm.mydom.gebd.core.TDiccionario;
import es.carm.mydom.gebd.core.parser.GEBDCompiler;
import es.carm.mydom.gebd.core.parser.GEBDProgram;
import es.carm.mydom.parser.BeanMethod;
import es.carm.mydom.parser.ParserException;
import es.carm.mydom.parser.ProgramContext;
import es.carm.mydom.parser.ViewDef;
import es.carm.mydom.security.UserInfo;
import es.carm.mydom.utils.URLComponents;

public class BasicParser {
	public static String parse(String txt,Map<String,Object> data) throws Exception{
		System.out.println("=============== Basic PARSER! ======================");
		System.out.println(data);
		System.out.println(txt);
		
		txt = textoToSTRinterna(txt);
		System.out.println(txt);
		
		System.out.println("=====================================");
		data.putAll((Map<String,Object>) data.get("dep"));
		Map<String,String> map = MapUtils.getMap2Map(data);
		Map<String,String> copy = new TreeMap<String,String>(map);
		for (Map.Entry<String,String> entry : copy.entrySet()) {
			System.out.println(entry.getKey()+"="+entry.getValue());
			//para cada entry hago la sustitucion
			txt = txt.replace("#!Traer "+entry.getKey()+"#", entry.getValue());
		}
		System.out.println("=====================================");
		System.out.println(txt);
		
		txt = toXMLinterna(txt);
		System.out.println(txt);
		System.out.println("=============== Basic PARSER END ======================");
		
		return txt;
	}
	
	public static String textoToSTRinterna(String value) {
		String[] org = new String[8];
		String[] dst = new String[8];
		org[0] = "'";
		dst[0] = "$comillasimple$";
		org[1] = "<";
		dst[1] = "$menorque$";
		org[2] = ">";
		dst[2] = "$mayorque$";
	
		org[3] = "&apos;";
		dst[3] = "'";
		org[4] = "&lt;";
		dst[4] = "<";
		org[5] = "&gt;";
		dst[5] = ">";
	
		org[6] = "&";
		dst[6] = "$ampersan$";
		org[7] = "$ampersan$amp;";
		dst[7] = "&";
	
		for (int i = 0; i < org.length; i++) {
			value=value.replace(org[i],dst[i]);
		}
		return value;
	}
	
	public static String toXMLinterna(String texto) {
		String[] org = new String[10];
		String[] dst = new String[10];
		org[0] = "&";
		dst[0] = "&amp;";
		org[1] = "'";
		dst[1] = "&apos;";
		org[2] = "<";
		dst[2] = "&lt;";
		org[3] = ">";
		dst[3] = "&gt;";
		org[4] = "\n\r"; //cstr(chr(13)+chr(10))
		dst[4] = "<text:line-break></text:line-break>";

		org[5] = "$comillasimple$";
		dst[5] = "'";
		org[6] = "$menorque$";
		dst[6] = "<";
		org[7] = "$mayorque$";
		dst[7] = ">";
		org[8] = "$ampersan$";
		dst[8] = "&";
		org[9] = "&quot;";
		dst[9] = "\"\"";
		//org[10] = "text:span";		//bug de los span?
		//dst[10] = "text:p";
		for (int i=0; i< org.length; i++) {
			texto = texto.replace(org[i], dst[i]);
		}
		return texto;
	}	
}
