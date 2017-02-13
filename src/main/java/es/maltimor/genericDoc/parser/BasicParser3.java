package es.maltimor.genericDoc.parser;

import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.carm.mydom.entity.Database;
import es.carm.mydom.entity.Document;
import es.carm.mydom.entity.DominoBean;
import es.carm.mydom.entity.DominoSession;
import es.carm.mydom.entity.ServerDao;
import es.carm.mydom.entity.View;
import es.carm.mydom.gebd.core.TDiccionario;
import es.carm.mydom.gebd.core.parser.GEBDCompiler;
import es.carm.mydom.gebd.core.parser.GEBDProgram;
import es.carm.mydom.gebd.core.parser.TokenArray;
import es.carm.mydom.gebd.core.parser.TokenSplit;
import es.carm.mydom.gebd.core.parser.UtilidadesGEBD;
import es.carm.mydom.gebd.core.parser.UtilidadesGEBDResult;
import es.carm.mydom.parser.BeanMethod;
import es.carm.mydom.parser.ParserException;
import es.carm.mydom.parser.ProgramContext;
import es.carm.mydom.parser.ViewDef;
import es.carm.mydom.security.UserInfo;
import es.carm.mydom.utils.URLComponents;

public class BasicParser3 {
	final Logger log = LoggerFactory.getLogger(BasicParser3.class);
	public static final String separador = "^?.#=:\"+-*/[]()<>|& ";
	private Map<String,Object> data;
	private Map<String,Object> dicc;
	private String value;
	private int len;
	private int pos;
	private int posFinAction;
	private boolean innerXML;

	public BasicParser3(String txt,Map<String,Object> data,boolean innerXML) throws Exception{
		this.value = txt;
		this.data = data;
		this.innerXML = innerXML;
		this.dicc = new HashMap<String,Object>();
		//caso especial de momento dicc lo inicializo con data.dep
		this.dicc.putAll((Map<String,Object>) data.get("dep"));
	}
	
	public String parse() throws ParserException {
		System.out.println("=============== Basic PARSER! ======================");
		System.out.println(data);
		System.out.println(value);
		
		long initTime = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		
		if(innerXML) {
			textoToSTRinterna();
			log.debug("textoToSTRinterna: " +  value);
			value = value.replaceAll("“", "\"");
			value = value.replaceAll("”", "\"");
		}
		
		
		System.out.println("=====================================");
		Map<String,String> map = MapUtils.getMap2Map(data);
		Map<String,String> copy = new TreeMap<String,String>(map);
		for (Map.Entry<String,String> entry : copy.entrySet()) {
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
		System.out.println("=====================================");
		
		
		len = value.length();
		pos = 0;
		String txt = "";
		String startTag = "#!";
		String endTag = "#";
		int startTagLen = startTag.length();
		int endTagLen = endTag.length();
		while (pos < len) {
			int i1 = value.indexOf(startTag, pos);
			//log.debug("Compiler.i1="+i1+" pos="+pos);
			if (i1 >= pos) {
				txt = value.substring(pos, i1);
				//log.debug("Compiler.txt="+txt);
				par_text(sb,txt);
				int i2 = value.indexOf(endTag, i1+startTagLen);
				//log.debug("Compiler.i2="+i2+" i1+sl="+(i1+startTagLen));
				if (i2 >= i1 + startTagLen) {
					// obtengo un comando y lo proceso y avanzo el cursor
					int i3 = value.indexOf(startTag, i1 + startTagLen);
					//log.debug("Compiler.i3="+i3+" i1+sl="+(i1+startTagLen));
					if ((i3 < i2) && (i3 > pos)) {
						// este es otro error
						txt = value.substring(i1, i3);
						//log.debug("Compiler.txt="+txt);
						par_text(sb, txt);
						pos = i3;
					} else {
						// por fin un comando correcto. obtengo el comando entero
						txt = value.substring(i1 + startTagLen, i2);
						//actualizo la posicion del puntero, antes de llamar a la accion, para permitir bucles
						//posIniAction = i1;
						//log.debug("Compiler.action="+txt);
						posFinAction = i2+endTagLen;
						pos = posFinAction;
						par_accion(sb, txt);
						//ahora si que avanzo el comando
						//NOTA dejo en manos de par_accion el mover el puntero
						//pos = i2 + 2;
					}
				} else {
					// esto deberia ser un error, no hago nada y avanzo el
					// cursor
					pos = i1 + startTagLen;
				}
			} else {
				// se termino de buscar
				txt = value.substring(pos);
				//log.debug("Compiler.txt="+txt);
				par_text(sb,txt);
				pos = len;
			}
		}

		String res = sb.toString();
		if (innerXML) res = toXMLinterna(res);
		System.out.println(res);
		
		long endTime = System.currentTimeMillis();
		log.debug("Compiler.compile:"+((endTime-initTime)/1000.0)+" seg.");
		
		System.out.println("=============== Basic PARSER END ======================");
		
		return res;
	}
	
	private void par_text(StringBuffer sb, String txt){
		sb.append(txt);
	}

	private void par_accion(StringBuffer sb, String accion){
		String actionName=accion;
		String actionText="";
		int i1=accion.indexOf(" ");
		if (i1>0) {
			actionName=accion.substring(0,i1).toLowerCase();
			actionText=accion.substring(i1).trim();
		} else {
			actionName=accion.toLowerCase();
		}
		
		//agm88x 18-1-16 en caso de innerxml no puede haber codigo xml dentro de una accion
		if (innerXML && (accion.contains("$mayorque$")||accion.contains("$menorque$"))){
			log.warn("ACCION CON XML!!!!!:"+accion);
			sb.append("???"+accion+"???");
			return;
		}
		
		TokenSplit ts = new TokenSplit(actionText);
		TokenArray ta = ts.split(separador);
		
		try{
			if (actionName.equals("traer")) par_traer(sb,ta);
			/*else if (actionName.equals("traermoneda")) par_traer(sb,actionText);
			else if (actionName.equals("traerfecha")) par_traer(sb,actionText);
			else if (actionName.equals("fechaactual")) par_traer(sb,actionText);
			else if (actionName.equals("declare")) par_traer(sb,actionText);
			else if (actionName.equals("declarevars")) par_traer(sb,actionText);
			else if (actionName.equals("if")) par_traer(sb,actionText);
			else if (actionName.equals("else")) par_traer(sb,actionText);
			else if (actionName.equals("endif")) par_traer(sb,actionText);
			else if (actionName.equals("for")) par_traer(sb,actionText);
			else if (actionName.equals("next")) par_traer(sb,actionText);
			else if (actionName.equals("while")) par_traer(sb,actionText);
			else if (actionName.equals("endwhile")) par_traer(sb,actionText);
			else if (actionName.equals("beginblock")) par_traer(sb,actionText);
			else if (actionName.equals("endblock")) par_traer(sb,actionText);
			else if (actionName.equals("let")) par_traer(sb,actionText);
			else if (actionName.equals("beginreport")) par_traer(sb,actionText);
			else if (actionName.equals("endreport")) par_traer(sb,actionText);
			else if (actionName.equals("definereportparam")) par_traer(sb,actionText);
			else if (actionName.equals("endreportparam")) par_traer(sb,actionText);
			else if (actionName.equals("beginmacro")) par_traer(sb,actionText);
			else if (actionName.equals("endmacro")) par_traer(sb,actionText);
			else if (actionName.equals("callmacro")) par_traer(sb,actionText);
			else if (actionName.equals("declarecursor")) par_traer(sb,actionText);
			else if (actionName.equals("initcursor")) par_traer(sb,actionText);
			else if (actionName.equals("nextcursor")) par_traer(sb,actionText);
			else if (actionName.equals("computewithform")) par_traer(sb,actionText);*/
			else par_text(sb,"???ACCION:"+actionName+" RESTO:"+actionText+"???");
		} catch (Exception e){
			log.error("Error de compilación del parser: " + sb.toString() + ":Acción: " +accion+":"+e.getMessage());
			sb.append("|"+accion+": "+e.getMessage()+"|");
			e.printStackTrace();
		}		
	}
	
	private void par_traer(StringBuffer sb, TokenArray ta) throws Exception{
		sb.append("|traer="+par_simple(ta,data)+"|");
	}
	
	public Object par_simple(TokenArray ta, Map<String,Object> data) throws Exception{
		Object simple = null;
		if (ta.isSep()){
			String act = ta.getAct();
			if (act.equals("\"")){		//literal o fecha
				ta.consumeToken();		//salto las "
				String textoSimple="";	//almaceno el texto interno
				ta.saveState();
				if (ta.getAct().equals("[")) {
					//parece una fecha
					ta.consumeToken();	//salto el [
					while (!ta.isEof()&&!"]\"".contains(ta.getAct())) {	//busco fin, ] o "
						textoSimple+=ta.consumeToken();
					}
					//compruebo si es fecha o no, si es eof no estaba bien formada la cadena
					if (ta.isEof()){	//no he llegado a ] o a " esto es una cadena sin cerrar
						throw new Exception("Constante de cadena sin cerrar (EOF):"+textoSimple);
					} else if (ta.getAct().equals("]")){
						ta.consumeToken();		//salto el ]
						if (!ta.getAct().equals("\"")){
							throw new Exception("Constante de cadena sin cerrar (]):"+textoSimple);
						}
						ta.consumeToken();		//salto el "
						//en este momento estoy seguro de que esto debo interpretarlo como una fecha "[----]"
						//si es fecha lo asigno asi o doy error
						UtilidadesGEBDResult res = UtilidadesGEBD.getDate(textoSimple);
						if (res.res) simple = res.obj;
						else simple = textoSimple;
					} else {
						//estoy en un caso extraño "[----" lo devuelvo como un literal
						ta.consumeToken();		//salto el "
						simple = textoSimple;
					}
				} else {	//caso de literal "----"
					while (!ta.isEof()&&!ta.getAct().equals("\"")) {
						textoSimple+=ta.consumeToken();
					}
					if (ta.isEof()){	//no he llegado a " esto es una cadena sin cerrar
						throw new ParserException("Constante de cadena sin cerrar (EOF2):"+textoSimple);
					}
					ta.consumeToken();	//salto las "
					simple = textoSimple;
				}
			} else if ("+-".contains(act)) {
				log.debug("Es + o - ");
				String numero = "";
				numero = ta.consumeToken();
				numero = numero + ta.consumeToken();
				simple = UtilidadesGEBD.getNumber(numero);
			} else { 
				//Si no es un operador de un número se guarda como literal
				//agm88x: 22-12-2015: caso de un simple nulo, devuelve "" NO doy exception
				//throw new ParserException("No se esperaba este token en par_simple:"+act);
				simple = "";
			}
		} else {
			//no es un separador
			String s = ta.consumeToken(); 
			if (ta.getAct().equals("?")){
				//llamada al diccionario
				ta.consumeToken();
				if (dicc.containsKey(s.toLowerCase())){
					simple = dicc.get(s.toLowerCase());
				} else {
					//agm88x 29-12-2015 creo que si no esta en el diccionario deberia devolver un blanco
					//resultado = "???"+simple.getTexto()+"???";
					simple = "";
				}
			} else {
				log.debug("Voy a ver si s es un número: " + s);
				UtilidadesGEBDResult res = UtilidadesGEBD.getNumberExt(s);
				if (res.res) {
					//es un numero
					simple = res.obj;
				} else {
					simple = s;
				}
			}
		}
		//continuo evaluando el simple, para ver si he terminado
		// a continuacion se permite opcionalmente y secuencialmente: (LExpr)  [Exp]  .S
		if (ta.getAct().equals("(")){
			//TODO evaluar funciones
			throw new Exception("No se esperaba ( aqui.");
		};
		
		//array
		if (ta.getAct().equals("[")){
			//aqui cambio el contexto y evaluo "simple" que debe dar un array
			//ojo que es sensible a maysuculas y minusculas
			if (!data.containsKey(simple)) throw new Exception("No se encuentra "+simple+" en el acceso al array.");
			
			Object olst = data.get(simple);
			if (!(olst instanceof List)) throw new Exception(simple+" No es un array.");
			
			//salto el [
			ta.consumeToken();
			Object simpleArr = par_simple(ta,this.data);	//OJO aqui el contexto es global
			//debe ser numerico
			UtilidadesGEBDResult res = UtilidadesGEBD.getNumberExt(simpleArr);
			if (!res.res) throw new Exception("No se encuentra "+simple+" en el acceso al array.");
			
			
			
			//AQUI
			
			
		}
		
		//aqui esto en condiciones de obtener un valor al simple y posiblemente cambiar el contexto recursivo de par_simple
		
		
		//punto
		if (ta.getAct().equals(".")){
			//punto
		}
		
		
		return simple;
	}	
	
	
	public void textoToSTRinterna() {
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
	}
	
	public String toXMLinterna(String texto) {
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
