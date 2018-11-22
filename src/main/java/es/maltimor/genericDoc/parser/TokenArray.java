package es.maltimor.genericDoc.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class TokenArray {
	final Logger log = LoggerFactory.getLogger(TokenArray.class);
	
	private boolean eof;
	private int pos;
	private List<String> listaTokens;
	private String act;
	private String sep;
	private int savePos;
	
	public TokenArray(String sep){
		this.listaTokens = new ArrayList<String>();
		this.savePos = 0;
		this.pos = 0;
		this.act = "";
		this.eof = true;
		this.sep = sep;
	}
	public boolean isEof() {
		return eof;
	}
	public void setEof(boolean eof) {
		this.eof = eof;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public List<String> getListaTokens() {
		return listaTokens;
	}
	public void setListaTokens(List<String> listaTokens) {
		this.listaTokens = listaTokens;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public boolean isSep(){
		return sep.contains(this.act);
	}
	/**
	 * Consume un token y devuelve el token consumido
	 * @return
	 */
	public String consumeToken(){
		if (eof) return "";
		String res = listaTokens.get(pos);
		pos++;
		if (pos<listaTokens.size()) {
			act = listaTokens.get(pos);
		} else {
			eof = true;
			act = "";
		}
		return res;
	}
	public void addToken(String token) {
		if (eof){
			this.act = token;
			this.eof = false;
		}
		listaTokens.add(token);
	}
	public String getSep() {
		return sep;
	}
	public void setSep(String sep) {
		this.sep = sep;
	}
	
	public void saveState(){
		this.savePos=pos;
	}
	
	public void restoreState(){
		this.pos = savePos;
		this.act = listaTokens.get(pos);
		this.eof = pos>=listaTokens.size();
	}

}
