package es.maltimor.genericDoc.parser;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.maltimor.genericDoc.utils.Utilidades;

public class BasicParser {
	final static Logger log = LoggerFactory.getLogger(BasicParser.class);
	public static final String separador = "^?.#=:\"+-*/[]()<>|& ";
	
	class Control {
		//mecanismo simple para permitir un for sin anidamientos
		public boolean inFor;			
		public int for_count;
		public int for_act;
		public int for_fin;
		public int for_startPos;
		public String var;
		
		public boolean inIf;
		public boolean if_res;
		public boolean if_falso;
	}
	
	class BasicContext {
		int MAX_FOR = 100;
		StringBuffer sb;
		boolean escribeLapiz;
		int pos;
		int startPos;
		int endPos;
		//mecanismo simple para permitir un for sin anidamientos
		Map<String,Control> pfor;
		List<Control> pif;
		
		BasicContext(){
			this.sb = new StringBuffer();
			this.escribeLapiz=true;
			this.pos = 0;
			this.startPos = 0;
			this.endPos = 0;
			this.pfor = new HashMap<String,Control>();
			this.pif = new ArrayList<Control>();
		}
		
		public String getText(){
			return sb.toString();
		}
		
		public void appendText(String str){
			if (this.escribeLapiz) sb.append(str);
		}
		
		public void setEscribeLapiz(boolean b){
			this.escribeLapiz = b;
		}
		
		public boolean isEscribeLapiz(){
			return this.escribeLapiz;
		}
		
		public int getPos(){
			return pos;
		}
		
		public void setPos(int pos){
			this.pos = pos;
		}

		public int getStartPos() {
			return startPos;
		}
		public void setStartPos(int pos) {
			this.startPos = pos;
		}
		public int getEndPos() {
			return endPos;
		}
		public void setEndPos(int pos) {
			this.endPos = pos;
		}
		
		public void setFor(String var,int ini,int fin,int pos, Map<String, String> map){
			Control c = new Control();
			c.inFor = true;
			c.var = var;
			c.for_act = ini;
			c.for_fin = fin;
			c.for_startPos = pos;
			c.for_count = 0;

			//actualizo el map con la variable
			map.put("dep."+var, ""+c.for_act);
			
			//inserto en la pila de control el control
			pfor.put(var.toUpperCase(),c);
		}
		public boolean nextFor(String var,Map<String,String> map){
			Control c = pfor.get(var.toUpperCase());
			if (c==null || !c.inFor) return false;
			c.for_act++;
			c.for_count++;
			if (c.for_count>MAX_FOR) return false;//medida de control
			
			//actualizo el map con la variable
			map.put("dep."+var, ""+c.for_act);

			if (c.for_act>c.for_fin){
				c.inFor = false;
				return false;
			} else {
				//cambio la posicion de pos
				this.pos = c.for_startPos;
				return true;
			}
		}

		public void insertIf(boolean res) {
			Control c = new Control();
			c.inIf = true;
			c.if_res = res;
			c.if_falso = false; 
			
			if (pif.size()>0){
				//caso con if anidado
				Control cact = pif.get(pif.size()-1);
				//si casc es false, estamos ante un if falso, se inserta pero como if_falso y no se cambia el lapiz
				//si es true, estamos ante un if normal, se inserta y se actua segun el lapiz
				//tener en cuenta que el lapiz tambien lo toca begin block. esto impide un if dentro de un begin block o viceversa
				//TODO hacer robusto ante la pila de ifs
				if (cact.if_falso==true||cact.if_res==false){
					//hay que insertar un if falso y no se hace nada mas
					c.if_falso=true;
					pif.add(c);
				} else {
					//hay que insertar un if normal y se cambia el lapiz
					pif.add(c);
					this.setEscribeLapiz(res);
				}
			} else {
				//caso inicial, primer if
				pif.add(c);
				this.setEscribeLapiz(res);
			}
		}
		
		public void doElse() {
			if (pif.size()>0){
				//caso con if anidado
				Control cact = pif.get(pif.size()-1);
				//si cact es if_falso, estamos ante un if falso, no se hace nada
				//en otro caso cambiamos el valor de cact y el lapiz
				if (cact.if_falso==false){
					//hay que insertar un if falso y no se hace nada mas
					cact.if_res=!cact.if_res;
					this.setEscribeLapiz(cact.if_res);
				}
			} else {
				//TODO esto deberia ser un error, no hago nada
			}
		}
		
		public void doEndIf(){
			if (pif.size()>0){
				//caso con if anidado
				//eliminamos el if de la pila
				Control cant = pif.remove(pif.size()-1);
				if (cant.if_falso==false) this.escribeLapiz=true; 
			} else {
				//TODO esto deberia ser un error, no hago nada
			}
		}
	}
	
	public BasicParser(){
		
	}
	
	public String parse(String txt,Map<String,Object> data) throws Exception{
		System.out.println("=============== Basic PARSER! ======================");
		System.out.println(data);
		//System.out.println(txt);

		//previene la perdida de la estructura xml y arregla dos casos conocidos de caracteres problematicos
		txt = textoToSTRinterna(txt);
		txt = txt.replaceAll("“", "\"");
		txt = txt.replaceAll("”", "\"");
		
		//System.out.println(txt);
		
		System.out.println("=====================================");
		if (data.containsKey("dep")) data.putAll((Map<String,Object>) data.get("dep"));
		Map<String,String> map = MapUtils.getMap2Map(data);
		System.out.println("=====================================");
		System.out.println(map);
		//inicializo las variables del parser
		map.put("_CONTADOR", "1");
	
		//para cada dependencia, hacer un replace en el texto (txt) donde me encuentre algo acabado en ?
		txt = transformText(txt,map);

		/*		//para cada entrada en los datos reemplazo
		Map<String,String> copy = new TreeMap<String,String>(map);
		
		for (Map.Entry<String,String> entry : copy.entrySet()) {
			//para cada entry hago la sustitucion
			if(entry.getKey().contains("?")){
				String aux = entry.getKey().replaceAll("[0-9A-Za-zÁÉÍÓÚáéíóú]+?\\?", entry.getValue());
				if( aux != null&& copy.get("data."+aux.toUpperCase()) != null)
				txt = txt.replace("#!Traer "+entry.getKey()+"#",copy.get("data."+aux.toUpperCase()));
			}
			else{
				//System.out.println(entry.getKey().substring(5));
				txt = txt.replace("#!Traer "+entry.getKey().substring(5)+"#",entry.getValue());
			}
		}
		*/
/*		//para cada entrada en los datos reemplazo
		Map<String,String> copy = new TreeMap<String,String>(map);
		for (Map.Entry<String,String> entry : copy.entrySet()) {
			System.out.println(entry.getKey()+"="+entry.getValue());
			//para cada entry hago la sustitucion
			txt = txt.replace("#!Traer "+entry.getKey()+"#", entry.getValue());
		}*/		
		//System.out.println("=====================================");
		//System.out.println(txt);

		//restaura la estructura xml original del documento
		txt = toXMLinterna(txt);
		//System.out.println(txt);
		System.out.println("=============== Basic PARSER END ======================");
		
		return txt;
	}
	
	private String transformText(String value, Map<String, String> map) {
		long initTime = System.currentTimeMillis();
		BasicContext ctx = new BasicContext();
		
		int len = value.length();
		//int pos = 0;
		String txt = "";
		String startTag = "#!";
		String endTag = "#";
		int startTagLen = startTag.length();
		int endTagLen = endTag.length();
		while (ctx.getPos() < len) {
			int i1 = value.indexOf(startTag, ctx.getPos());
			//log.debug("Compiler.i1="+i1+" pos="+pos);
			if (i1 >= ctx.getPos()) {
				txt = value.substring(ctx.getPos(), i1);
				//log.debug("Compiler.txt="+txt);
				par_text(ctx,txt);
				int i2 = value.indexOf(endTag, i1+startTagLen);
				//log.debug("Compiler.i2="+i2+" i1+sl="+(i1+startTagLen));
				if (i2 >= i1 + startTagLen) {
					// obtengo un comando y lo proceso y avanzo el cursor
					int i3 = value.indexOf(startTag, i1 + startTagLen);
					//log.debug("Compiler.i3="+i3+" i1+sl="+(i1+startTagLen));
					if ((i3 < i2) && (i3 > ctx.getPos())) {
						// este es otro error
						txt = value.substring(i1, i3);
						//log.debug("Compiler.txt="+txt);
						par_text(ctx, txt);
						ctx.setPos(i3);
					} else {
						// por fin un comando correcto. obtengo el comando entero
						txt = value.substring(i1 + startTagLen, i2);
						//actualizo la posicion del puntero, antes de llamar a la accion, para permitir bucles
						ctx.setStartPos(i1);
						ctx.setPos(i2+endTagLen);
						ctx.setEndPos(i2+endTagLen);
						par_accion(ctx, txt, map);
						//ahora si que avanzo el comando
						//NOTA dejo en manos de par_accion el mover el puntero
						//pos = i2 + 2;
					}
				} else {
					// esto deberia ser un error, no hago nada y avanzo el
					// cursor
					ctx.setPos( i1 + startTagLen );
				}
			} else {
				// se termino de buscar
				txt = value.substring(ctx.getPos());
				//log.debug("Compiler.txt="+txt);
				par_text(ctx,txt);
				ctx.setPos( len );
			}
		}

		String res = ctx.getText();
		
		long endTime = System.currentTimeMillis();
		System.out.println("Compiler.compile:"+((endTime-initTime)/1000.0)+" seg.");
		System.out.println("=============== Basic PARSER END ======================");
		
		return res;
	}
	
	private void par_text(BasicContext ctx, String txt){
		ctx.appendText(txt);
	}
	
	private void par_accion(BasicContext ctx, String accion, Map<String,String> map){
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
		if (accion.contains("$mayorque$")||accion.contains("$menorque$")){
			ctx.appendText("???"+accion+"???");
			return;
		}
		
		
		TokenSplit ts = new TokenSplit(actionText);
		TokenArray ta = ts.split(separador);
		System.out.println("ACCION:"+actionName+"|"+actionText+"|"+ta.getListaTokens());
		
		//transformo los tokens acabados en ? por su valor en el map
		String expr="";
		while (!ta.isEof()){
			String token = ta.consumeToken();
			String act = ta.getAct();
			if (!act.equals("?")) {
				if (token.equals("(")) expr+="[";
				else if (token.equals(")")) expr+="]";
				else expr+=token;
			} else {
				//intento ver si puedo transformarlo
				ta.consumeToken();//salto el ?
				String aux = map.get("dep."+token);
				if (aux!=null) expr+=aux;
				else expr+=token+"!!!";
			}
		}

		try{
			if (actionName.equals("traer")) par_traer(ctx,expr,map);
			else if (actionName.equals("traermoneda")) par_traermoneda(ctx,expr,map);
			else if (actionName.equals("traerfecha")) par_traerfecha(ctx,expr,map);
			else if (actionName.equals("traerfechacorta")) par_traerfechaCorta(ctx,expr,map);
			else if (actionName.equals("contador")) par_contador(ctx,expr,map);
			else if (actionName.equals("fechaactual")) par_fechaActual(ctx,expr,map);
			else if (actionName.equals("beginblock")) par_beginBlock(ctx,expr,map);
			else if (actionName.equals("endblock")) par_endBlock(ctx,expr,map);
			else if (actionName.equals("for")) par_for(ctx,expr,map);
			else if (actionName.equals("next")) par_next(ctx,expr,map);
			else if (actionName.equals("declare")) par_declare(ctx,expr,map);
			else if (actionName.equals("if")) par_if(ctx,expr,map);
			else if (actionName.equals("else")) par_else(ctx,expr,map);
			else if (actionName.equals("endif")) par_endif(ctx,expr,map);
			else par_text(ctx,"???ACCION:"+actionName+" RESTO:"+actionText+"???");
		} catch (Exception e){
			log.error("Error de compilación del parser: " + ctx.getText() + ":Acción: " +accion+":"+e.getMessage());
			ctx.appendText("|"+accion+": "+e.getMessage()+"|");
			e.printStackTrace();
		}
		
	}
		
	private String getValueExpr(String expr, Map<String, String> map){
		//TODO evaluador de expresiones y semantica valor-simple-ref....
		if (map.containsKey("data."+expr.toUpperCase())) return map.get("data."+expr.toUpperCase());
		return expr;
	}

	private void par_traer(BasicContext ctx, String expr, Map<String, String> map) {
		String value = getValueExpr(expr,map);
		ctx.appendText(value);
	}

	private void par_traermoneda(BasicContext ctx, String expr, Map<String, String> map) {
		String value = getValueExpr(expr,map);
		try {
			//determinar qué tipo de cadena tengo, es decir si ya esta en formato español o americano
			if (Pattern.matches(".*[,].*[.].*", value)) value=value.replace(",", "");
			else if (Pattern.matches(".*[.].*[,].*", value)) value=value.replace(".", "").replace(",", ".");
			else if (Pattern.matches(".*[,]\\d\\d?", value)) value=value.replace(",", ".");
			else if (Pattern.matches(".*[,].*", value)) value=value.replace(",", "");
			
			double d = Double.parseDouble(value);

			DecimalFormatSymbols ds = new DecimalFormatSymbols();
			ds.setDecimalSeparator(',');
			ds.setGroupingSeparator('.');
			DecimalFormat df = new DecimalFormat("#,##0.00",ds);

			value = df.format(d);
		} catch (Exception e) {}
		ctx.appendText(value);
	}

	private void par_traerfecha(BasicContext ctx, String expr, Map<String, String> map) {
		String value = getValueExpr(expr,map);
		if (value!=null && !value.equals("")) value = Utilidades.textoFecha(value);
		ctx.appendText(value);
	}

	private void par_traerfechaCorta(BasicContext ctx, String expr, Map<String, String> map) {
		String value = getValueExpr(expr,map);
		if (value!=null && !value.equals("")) value = Utilidades.textoFechaCorta(value);
		ctx.appendText(value);
	}
	
	private void par_contador(BasicContext ctx, String expr, Map<String, String> map) {
		if (expr==null||expr.equals("")) {
			//obtiene el valor del contador y lo incrmeenta en 1
			String contador = map.get("_CONTADOR");
			ctx.appendText(contador);
			try{
				int c = Integer.parseInt(contador)+1;
				map.put("_CONTADOR", ""+c);
			} catch (Exception e) {}
		} else {
			//poner el contador a un valor:expr
			try{
				int c = Integer.parseInt(expr);
				map.put("_CONTADOR", ""+c);
			} catch (Exception e) {
				map.put("_CONTADOR", "1");
			}
		}
	}

	private void par_fechaActual(BasicContext ctx, String expr, Map<String, String> map) {
		DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
		Date date = new Date();
		ctx.appendText(dateFormat.format(date));
	}

	private void par_beginBlock(BasicContext ctx, String expr, Map<String, String> map) {
		ctx.setEscribeLapiz(false);
	}

	private void par_endBlock(BasicContext ctx, String expr, Map<String, String> map) {
		ctx.setEscribeLapiz(true);
	}

	private void par_for(BasicContext ctx, String expr, Map<String, String> map) {
		boolean error = true;
		int i1 = expr.indexOf("=");
		int i2 = expr.indexOf(":");
		if (i1>0 && i2>i1){
			String var = expr.substring(0,i1);
			String sini = getValueExpr(expr.substring(i1+1,i2),map);
			String sfin = getValueExpr(expr.substring(i2+1),map);
			try {
				int ini = Integer.parseInt(sini);
				int fin = Integer.parseInt(sfin);
				//salto 
				//declaro el for
				ctx.setFor(var, ini, fin, ctx.getEndPos(), map);
				error = false;
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		if (error) ctx.appendText("!!!"+ctx.getStartPos()+":"+ctx.getPos()+":"+expr);
	}

	private void par_next(BasicContext ctx, String expr, Map<String, String> map) {
		boolean res = ctx.nextFor(expr,map);
	}

	private void par_if(BasicContext ctx, String expr, Map<String, String> map) {
		//de momento solo tengo operador =,>,<,>=,<=,<>
		String op = "";
		int i1 = expr.indexOf("=");
		if (i1!=-1) {
			op="=";
			char ant=expr.charAt(i1-1); 
			if (ant=='<' || ant=='>') {
				op=ant+op;
				i1--;
			}
		} else {
			i1 = expr.indexOf("<");
			if (i1!=-1) {
				op="<";
				char sig=expr.charAt(i1+1); 
				if (sig=='>' || sig=='=') op+=sig;
			} else {
				i1 = expr.indexOf(">");
				if (i1!=-1) { 
					op=">";
					char sig=expr.charAt(i1+1); 
					if (sig=='=') op+=sig;
				} else {
					ctx.appendText("!!!Falta operador"+expr);
					return;
				}
			}
		}
		//troceo la expresion en dos partes con su operador
		String izq = getValueExpr(expr.substring(0,i1),map);
		String der = getValueExpr(expr.substring(i1+op.length()),map);
		int aux = izq.compareTo(der);
		boolean res = (op.equals("=")&&aux==0)
			||(op.equals(">")&&aux>0)
			||(op.equals("<")&&aux<0)
			||(op.equals(">=")&&aux>=0)
			||(op.equals("<=")&&aux<=0)
			||(op.equals("<>")&&aux!=0);
		
		//TODO chapu
//		ctx.appendText(" IF:"+izq+":"+op+":"+der+":"+aux+"="+res+" ");
		ctx.insertIf(res);
	}
	private void par_else(BasicContext ctx, String expr, Map<String, String> map) {
		ctx.doElse();
	}
	private void par_endif(BasicContext ctx, String expr, Map<String, String> map) {
		ctx.doEndIf();
	}

	private void par_declare(BasicContext ctx, String expr, Map<String, String> map) {
		boolean error = true;
		int i1 = expr.indexOf("=");
		if (i1>0){
			String var = expr.substring(0,i1);
			String exp = getValueExpr(expr.substring(i1+1),map);
			map.put("dep."+var, exp);
			error = false;
		}
		
		if (error) ctx.appendText("!!!"+ctx.getStartPos()+":"+ctx.getPos()+":"+expr);
	}

	public String textoToSTRinterna(String value) {
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
