package es.maltimor.genericDoc.parser;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenSplit {
	final Logger log = LoggerFactory.getLogger(TokenSplit.class);

	private String cadena;
	
	public TokenSplit(String cadena){
		this.cadena = cadena;
	}
	
	public TokenArray split(String sep){
		StringTokenizer st = new StringTokenizer(cadena,sep,true);
		TokenArray ta = new TokenArray(sep);
		while (st.hasMoreElements()){
			ta.addToken((String) st.nextElement());
		}
		return ta;
	}
}
