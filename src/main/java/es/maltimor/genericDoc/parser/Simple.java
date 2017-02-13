package es.maltimor.genericDoc.parser;

public class Simple {
	public final static int SIMPLE_DESCONOCIDO = 0;
	public final static int SIMPLE_LITERAL = 1;
	public final static int SIMPLE_NUMERO = 2;
	public final static int SIMPLE_CADENA = 3;
	public final static int SIMPLE_VARIABLE = 4;
	public final static int SIMPLE_FECHA = 5;
	private int tipo;
	private String texto;
	private Object valor;

	public Simple(int tipo, String texto) {
		this.tipo = tipo;
		this.texto = texto;
	}

	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Object getValor() {
		return valor;
	}
	public void setValor(Object valor) {
		this.valor = valor;
	}
}
