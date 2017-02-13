package es.maltimor.genericDoc.parser;

import java.io.Writer;
import java.sql.SQLException;
import java.util.Date;
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
import es.carm.mydom.gebd.core.TDiccionario;
import es.carm.mydom.gebd.core.parser.GEBDCompiler;
import es.carm.mydom.gebd.core.parser.GEBDProgram;
import es.carm.mydom.parser.BeanMethod;
import es.carm.mydom.parser.ParserException;
import es.carm.mydom.parser.ProgramContext;
import es.carm.mydom.parser.ViewDef;
import es.carm.mydom.security.UserInfo;
import es.carm.mydom.utils.URLComponents;

public class BasicParser2 {
	public static String parse(String txt,Map<String,Object> data) throws Exception{
		System.out.println("=============== Basic PARSER! ======================");
		System.out.println(data);
		System.out.println(txt);
		
		//creo un diccionario url a partir de los datos
		Document docAct = new Document();
		docAct.setItems(data);
		TDiccionario dicc = new TDiccionario();
		dicc.putAll(data);
		DominoSession domSession=new BasicDomSession(data);
		
		GEBDCompiler comp = new GEBDCompiler(true,txt,"UTF8");
		GEBDProgram prg = (GEBDProgram) comp.compile();
		prg.setDiccionario(dicc);
		ProgramContext pc = new ProgramContext(null);
		pc.setPrg(prg);
		pc.setDocAct(docAct);
		pc.setDomSession(domSession);
		List<String> body = prg.execute(pc);
		
		System.out.println(body);

		
		String txtRes = txt.replace("#!Traer fechaActual#", (new Date()).toString());
		
		//en data tengo la siguiente estructura:
		//data
		//dep:[{KEY:key, VALOR:valor}]
		if (data.containsKey("dep")){
			List<Map<String,Object>> dep= (List<Map<String,Object>>) data.get("dep");
			for(Map<String,Object> map:dep){
				//TODO ver otros tipos de datos
				String key = (String) map.get("KEY");
				String valor = (String) map.get("VALOR");
				if (valor==null) valor="";
				
				//hago el cambio a lo bruto
				txtRes = txtRes.replace("#!Traer "+key+"?#", valor);
			}
		}

		System.out.println(txtRes);
		System.out.println("=============== Basic PARSER END ======================");
		
		return txtRes;
	}
	
	
}
