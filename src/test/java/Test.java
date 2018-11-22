import java.util.HashMap;
import java.util.Map;

import es.maltimor.genericDoc.parser.BasicParser;

public class Test {

	public static void main(String[] args) throws Exception {
		BasicParser p = new BasicParser();
		String txt ="#!FOR i=1:5#a=#!Traer aaa?# b=#!Traer bbb?# \ni=#!Traer i?#\n"
				+ "a=i\n#!IF aaa?=i?#"
				+ "si(a=i)\n"
				+ "a=5?\n#!IF aaa?=5#"
				+ "si(a=5)\n"
				+ "#!Else#"
				+ "no(a=5)\n"
				+ "#!EndIf#"
				+ "#!Else#"
				+ "no(a=b)\n"
				+ "b=5?\n#!IF bbb?=5#"
				+ "si(b=5)\n"
				+ "#!Else#"
				+ "no(b=5)\n"
				+ "#!EndIf#"
				+ "#!EndIf#FIN#!Next i#";
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> deps= new HashMap<String,Object>();
		deps.put("aaa", 5);
		deps.put("bbb", 5);
		data.put("dep", deps);
		String res = p.parse(txt, data);
		System.out.println(res);
	}

}
