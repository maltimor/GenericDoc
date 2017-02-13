package es.maltimor.genericDoc.parser;

import java.util.ArrayList;
import java.util.List;

public class Valor {
	public final static int VAL_DESCONOCIDO = 0;	
	public final static int VAL_SIMPLE = 1;			//S
	public final static int VAL_FUNCION = 2;		//S(---)
	public final static int VAL_PUNTO = 3;			//S.V
	public final static int VAL_GORRO = 4;			//S^V
	public final static int VAL_MULTIVALUADO = 5;	//S[E]		//caso especial por compatibilidad atras

	private int tipo;
	private Simple s;
	private Valor v;
	private Expresion e;
	private List<Expresion> lparam;

	private Object valor;
	private Object data;
	
	public Valor(int tipo, Simple s, String valor) {
		this.tipo = tipo;
		this.s = s;
		this.valor = valor;
		lparam = new ArrayList<Expresion>();
		this.tipo = VAL_DESCONOCIDO;
		this.data = null;
	}

	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public Simple getS() {
		return s;
	}
	public void setS(Simple s) {
		this.s = s;
	}
	public Valor getV() {
		return v;
	}
	public void setV(Valor v) {
		this.v = v;
	}
	public Expresion getE() {
		return e;
	}
	public void setE(Expresion e) {
		this.e = e;
	}
	public List<Expresion> getLparam() {
		return lparam;
	}
	public void setLparam(List<Expresion> lparam) {
		this.lparam = lparam;
	}
	public Object getValor() {
		return valor;
	}
	public void setValor(Object valor) {
		this.valor = valor;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
