package es.maltimor.genericDoc.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilidades {
	final static Logger log = LoggerFactory.getLogger(Utilidades.class);
	
	private static String[] unidad = new String[10];
	private static String[] decena = new String[10];
	private static String[] centena = new String[11];
	private static String[] deci = new String[10];
	private static String[] otros = new String[16];
	
	private static boolean inicializado = false;
	
	public static void consumeParametroAgente(String cad,String key,String valor) {
		
	}
	
	public static String consumeToken(String cad,String sep) {
		return "Pendiente";
	}
	
	public static boolean estaEnLista(String valor, String lista, String formato, String separador) {
		int res;
		res = lista.indexOf(valor);
		if (res>=0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * El formato consta de 4 letras ABCD
	 * A: puede ser I o D. indica si la insercion se hace por la izquierda o por la derecha
	 * B: puede ser I o D. indica si el separador se pone a la izquierda o a la derecha del valor
	 * C: puede ser S o N. indica para el caso del primer elemento si se pone o no el separador
	 * D: puede ser S o N. indica si permite duplicados
	 * @param valor
	 * @param lista
	 * @param formato
	 * @param separador
	 * @return
	 */
	public static String insertaEnLista(String valor, String lista, String formato, String separador) {
		String A;
		String B;
		String C;
		String D;
		String valAct;
		String res;
	
		log.debug("###Inserta en lista: valor="+valor+ " lista="+lista+ " format="+formato+" sep="+separador);
		A = formato.substring(0, 1).toUpperCase();
		B = formato.substring(1, 2).toUpperCase();
		C = formato.substring(2, 3).toUpperCase();
		D = formato.substring(3, 4).toUpperCase();
		res = lista;
		if((C.equals("S")) || (!lista.equals(""))) {
			if (B.equals("I")) {
				valAct = separador+valor;
			} else {
				valAct = valor+separador;
			}
		} else	{	
			valAct = valor;
		}
		if((D.equals("S")) || (lista.indexOf(valor)==-1)) {
			if (A.equals("I")) {
				res = valAct+lista;
			} else {
				res = lista+valAct;
			}
		}
	
		return res;
		//TODO HayError: MsgErr "[BibliParserWeb.TStringUtils.insertaEnLista]"
	}
	
	public static String eliminaEnLista(String valor, String lista, String formato, String separador){
		String A;
		String B;
		String C;
		String D;
		String cad;
		String tmp;
		String res;
	
		log.debug("###Elimina en lista: valor="+valor+ " lista="+lista+ " format="+formato+" sep="+separador);

		A = formato.substring(0, 1).toUpperCase();
		B = formato.substring(1, 2).toUpperCase();
		C = formato.substring(2, 3).toUpperCase();
		D = formato.substring(3, 4).toUpperCase();
		tmp = lista;
		res = lista;
		if (tmp.indexOf(valor)>-1) {
			if (B.equals("D")) {
				cad = valor+separador;
			} else {
				cad = separador+valor;
			}
			
			if(tmp.indexOf(cad)>-1) {
				//res = tmp.substring(0, tmp.indexOf(cad)) + tmp.substring(tmp.indexOf(cad)+cad.length());
				res = tmp.replace(cad, "");
			} else {
				//caso especial, solo he encontrado el valor sin el separador
				//solo puede ser porque esta al principio o al final o es un unico elemento
				//intento eliminar el opuesto al parametro B, sino elimino sin mas
				if (!B.equals("D")) {
					cad = valor+separador;
				} else {
					cad = separador+valor;
				}
				if (tmp.indexOf(cad)>-1) {
					res = tmp.replace(cad, "");
				} else {
					res = tmp.replace(valor, "");
				}
			}
		}

		log.debug("###Elimina en lista res="+res);

		return res;
		//TODO HayError: MsgErr "[BibliParserWeb.TStringUtils.eliminaEnLista]"
	}
	
	/**
	 * Simula la función String$ de lotus
	 * @param n Nº de veces que se repite el caracter
	 * @param cadena Cadena a repetir
	 * @return cadena de texto
	 */
	public static String repeatValue(int n, String cadena) {
		StringBuilder resultado = new StringBuilder();
		for (int i=0; i < n; i++) {
			resultado.append(cadena);
		}
		return resultado.toString();
	}
	
	/**
	 * Elimina los espacios en blanco a la derecha de los caracteres. Simula el comportamiento de RTrim de Lotus
	 * @param cadena
	 * @return
	 */
	public static String rTrim(String cadena) {
		String resultado = cadena;
		while (resultado.endsWith(" ")) {
			resultado = resultado.substring(0, resultado.length() - 1);
		}
		return resultado;
	}
	
	/**
	 * Elimina los espacios en blanco a la izquierda de los caracteres. Simula el comportamiento de LTrim de Lotus
	 * @param cadena
	 * @return
	 */
	public static String lTrim(String cadena) {
		String resultado = cadena;
		while (resultado.startsWith(" ")) {
			resultado = resultado.substring(1);
		}
		return resultado;
	}
	
	/**
	 * Función que simula el comportamiento de Right$ de Lotus.
	 * @param cadena
	 * @param n
	 * @return Devuelve los n caracteres más a la derecha de cadena. Si cadena = null devuelve ""
	 */
	public static String right(String cadena, int n) {
		String resultado = cadena==null?"":cadena;
		if (n > 0) {
			if (cadena == null) {
				resultado = "";
			} else {
				if (cadena.length() > n) {
					resultado = cadena.substring(cadena.length() - n);
				}
			}
		}
		return resultado;
	}
	
	/**
	 * Función que simula el comportamiento de Left$ de Lotus.
	 * @param cadena
	 * @param n
	 * @return Devuelve los n caracteres más a la izquierda de cadena. Si cadena = null devuelve ""
	 */
	public static String left(String cadena, int n) {
		String resultado = cadena==null?"":cadena;
		if (n > 0) {
			if (cadena == null) {
				resultado = "";
			} else {
				if (cadena.length() > n) {
					resultado = cadena.substring(0, n);
				}
			}
		}
		return resultado;
	}
	
	/**
	 * Función que simula el comportamiento de mid$ de Lotus.
	 * @param cadena
	 * @param inicio Comienza desde 1 al igual que en Lotus
	 * @param longitud Nº de caracteres a devolver
	 * @return Devuelve tantos caracteres como los indicados en longitud a partir de inicio de cadena. Si cadena = null devuelve ""
	 */
	public static String mid(String cadena, int inicio, int longitud) {
		String resultado = "";
		if (inicio > 0) {
			if (cadena == null) {
				resultado = "";
			} else {
				if (inicio>=cadena.length()) resultado="";
				else if (cadena.length() > (inicio-1+longitud)) {
					resultado = cadena.substring((inicio-1), (inicio-1+longitud));
				} else {
					resultado = cadena.substring((inicio-1));
				}
			}
		}
		return resultado;
	}
	
	/**
	 * Indica si la cadena de entrada es numérica o no.
	 * @param cadena
	 * @return 
	 */
	public static boolean isNumeric (String cadena) {
		boolean resultado;
		try {
			Double.parseDouble(cadena);
			resultado = true;
		} catch (Exception e) {
			try {
				Integer.parseInt(cadena);
				resultado = true;
			} catch (Exception e2) {
				resultado = false;
			}
		}
		return resultado;
	}
	
	/**
	 * Realiza el redondeo de "numero" con el número "digitos" de decimales
	 * @param numero
	 * @param digitos
	 * @return
	 */
	public static double round(double numero,int digitos)
	{
	      int cifras=(int) Math.pow(10,digitos);
	      return Math.rint(numero*cifras)/cifras;
	}
	
	public static Calendar getCalendar(Object fecha){
		Calendar cal = Calendar.getInstance();
		if (fecha instanceof Date) cal.setTime((Date) fecha);
		else {
			String textoSimple = fecha.toString();
			SimpleDateFormat formatoSimple = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatoHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			SimpleDateFormat formatoInternetHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat formatoInternet = new SimpleDateFormat("yyyy-MM-dd");
			if (textoSimple.isEmpty()) {
				return null;
			} else {
				try {
					cal.setTime(formatoHora.parse(textoSimple));
				} catch (Exception e) {
					try {
						cal.setTime(formatoSimple.parse(textoSimple));
					} catch (Exception e1) {
						try {
							cal.setTime(formatoInternetHora.parse(textoSimple));
						} catch (Exception e2) {
							try {
								cal.setTime(formatoInternet.parse(textoSimple));
							} catch (Exception e3) {
								return null;
							}
						}
					}
				}
			}
		}
		return cal;
	}

	/**
	 * Convierte un objeto que sea una fecha a una fecha en formato dd/mm/yyyy
	 * @param fecha
	 * @return
	 */
	public static String textoFechaCorta(Object fecha) {
		Calendar cal = getCalendar(fecha);
		if (cal==null){
			if (fecha==null || fecha.toString().isEmpty()) return "";
			else return "[No se ha podido convertir a fecha:"+fecha+"]";
		}
		int year = cal.get(Calendar.YEAR);
		if (year>=80 && year<100) year+=1900;
		else if (year<80) year+=2000;
		return right("00"+cal.get(Calendar.DAY_OF_MONTH),2) + "/" + right("00"+(cal.get(Calendar.MONTH)+1),2) + "/" + right(""+year,4);
	}
	
	/**
	 * Convierte un objeto que sea una fecha a un texto del formato DIA de MES_TEXTO de AÑO
	 * @param fecha
	 * @return
	 */
	public static String textoFecha(Object fecha) {
		Calendar cal = getCalendar(fecha);
/*		Calendar cal = Calendar.getInstance();
		String resultado = "";
		if (fecha instanceof Date) cal.setTime((Date) fecha);
		else {
			String textoSimple = fecha.toString();
			SimpleDateFormat formatoSimple = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatoHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			SimpleDateFormat formatoInternetHora = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat formatoInternet = new SimpleDateFormat("yyyy-MM-dd");
			if (textoSimple.isEmpty()) {
				return "";
			} else {
				try {
					cal.setTime(formatoHora.parse(textoSimple));
				} catch (Exception e) {
					try {
						cal.setTime(formatoSimple.parse(textoSimple));
					} catch (Exception e1) {
						try {
							cal.setTime(formatoInternetHora.parse(textoSimple));
						} catch (Exception e2) {
							try {
								cal.setTime(formatoInternet.parse(textoSimple));
							} catch (Exception e3) {
								resultado = "[No se ha podido convertir a fecha:"+fecha+"]";
								return resultado;
							}
						}
					}
				}
			}
		}*/
		if (cal==null){
			if (fecha==null || fecha.toString().isEmpty()) return "";
			else return "[No se ha podido convertir a fecha:"+fecha+"]";
		}
		String resultado = cal.get(Calendar.DAY_OF_MONTH) + " de ";
		switch (cal.get(Calendar.MONTH)) {
			case 0: resultado = resultado + "enero";
			break;
			case 1: resultado = resultado + "febrero";
			break;
			case 2: resultado = resultado + "marzo";
			break;
			case 3: resultado = resultado + "abril";
			break;
			case 4: resultado = resultado + "mayo";
			break;
			case 5: resultado = resultado + "junio";
			break;
			case 6: resultado = resultado + "julio";
			break;
			case 7: resultado = resultado + "agosto";
			break;
			case 8: resultado = resultado + "septiembre";
			break;
			case 9: resultado = resultado + "octubre";
			break;
			case 10: resultado = resultado + "noviembre";
			break;
			case 11: resultado = resultado + "diciembre";
			break;
			default: break;
		}
		int year = cal.get(Calendar.YEAR);
		if (year>=80 && year<100) year+=1900;
		else if (year<80) year+=2000;
		resultado = resultado + " de " + year;
		return resultado;
	}
	
	public static int instr(int start, String cadena, String cadena2, int compMethod) {
		/*
		 * métodos comparación:
				0 case-sensitive, pitch-sensitive
				1 case-insensitive, pitch-sensitive
				4 case-sensitive, pitch-insensitive
				5 case-insensitive, pitch-insensitive
		 */
		//TODO No funciona el modo pitch-insensitive
		int resultado = -1;
		String cadenaAux;
		if (start > 1) {
			cadenaAux = cadena.substring(start-1);
		} else {
			cadenaAux = cadena;
		}
		if (compMethod == 1 || compMethod == 5) {
			resultado = cadenaAux.toUpperCase().indexOf(cadena2.toUpperCase());
		} else {
			resultado = cadenaAux.indexOf(cadena2);
		}
		return resultado+1;
	}
	
	public static void inicializaArraysNumeros() {
		
		unidad[0] = "CERO";
		unidad[1] = "UNO";
		unidad[2] = "DOS";
		unidad[3] = "TRES";
		unidad[4] = "CUATRO";
		unidad[5] = "CINCO";
		unidad[6] = "SEIS";
		unidad[7] = "SIETE";
		unidad[8] = "OCHO";
		unidad[9] = "NUEVE";
	
		decena[0] = "CERO";
		decena[1] = "DIEZ";
		decena[2] = "VEINTE";
		decena[3] = "TREINTA";
		decena[4] = "CUARENTA";
		decena[5] = "CINCUENTA";
		decena[6] = "SESENTA";
		decena[7] = "SETENTA";
		decena[8] = "OCHENTA";
		decena[9] = "NOVENTA";
	
		centena[0] = "CERO";
		centena[1] = "CIENTO";
		centena[2] = "DOSCIENTOS";
		centena[3] = "TRESCIENTOS";
		centena[4] = "CUATROCIENTOS";
		centena[5] = "QUINIENTOS";
		centena[6] = "SEISCIENTOS";
		centena[7] = "SETECIENTOS";
		centena[8] = "OCHOCIENTOS";
		centena[9] = "NOVECIENTOS";
		centena[10] = "CIEN";
	
		deci[0] = "CERO";
		deci[1] = "DIECI";
		deci[2] = "VEINTI";
		deci[3] = "TREINTA Y ";
		deci[4] = "CUARENTA Y ";
		deci[5] = "CINCUENTA Y ";
		deci[6] = "SESENTA Y ";
		deci[7] = "SETENTA Y ";
		deci[8] = "OCHENTA Y ";
		deci[9] = "NOVENTA Y ";
	
		otros[0] = "0";
		otros[1] = "1";
		otros[2] = "2";
		otros[3] = "3";
		otros[4] = "4";
		otros[5] = "5";
		otros[6] = "6";
		otros[7] = "7";
		otros[8] = "8";
		otros[9] = "9";
		otros[10] = "10";
		otros[11] = "ONCE";
		otros[12] = "DOCE";
		otros[13] = "TRECE";
		otros[14] = "CATORCE";
		otros[15] = "QUINCE";
		inicializado = true;
	}

	public static String numero2Letra(String strNum, String signoDecimal, String vMoneda, String vCentimos) {
		int Lo = 0;
		int iHayDecimal;
		String sDecimal;
		String sEntero;
		String sFraccion;
		int fFraccion;
		String sNumero;
	
		String sMoneda;
		String sCentimos;
		
		String resultado;
		
		if (!inicializado) inicializaArraysNumeros();
		
		sMoneda = " " + vMoneda.trim() + " ";
	    sCentimos = " " + vCentimos.trim();
	
	
	    //Averiguar el signo decimal
		sDecimal = signoDecimal;
		if(!signoDecimal.equals(".") && !signoDecimal.equals(",")) {
			sDecimal = ",";
		}
		sNumero = "";
	
		//Quitar los espacios que haya por medio
		strNum = strNum.replace(" ", "");
	
		//Comprobar si tiene decimales
		iHayDecimal = strNum.indexOf(sDecimal);
		log.debug("iHayDecimal: " + iHayDecimal + " strNum: " + strNum);
		if(iHayDecimal != -1) {
			//sEntero =  strNum.substring(0, iHayDecimal);
			sEntero =  Utilidades.left(strNum, iHayDecimal);
			sFraccion =  strNum.substring(iHayDecimal + 1) + "00";
			log.debug("sFraccion: " + sFraccion);
			//obligar a que tenga dos cifras
			//sFraccion = sFraccion.substring(0, 2);
			sFraccion = Utilidades.left(sFraccion, 2);
			log.debug("sFraccion: " + sFraccion);
			try {
				fFraccion = Integer.parseInt(sFraccion);
			} catch (Exception e) {
				log.debug("Error fFraccion: " + e);
				fFraccion = 0;
			}
			log.debug("fFraccion: " + fFraccion);
			//Si no hay decimales... no agregar nada...
			if(fFraccion < 1) {
				strNum = Utilidades.rTrim(unNumero(sEntero) + sMoneda);
				if(Lo == 0) {
					sNumero = strNum.substring(0, (sNumero.length() - 1));
				} else {
					sNumero = strNum;
				}
				resultado = sNumero;
			}
	    
			log.debug("Calculo de entero: " + sEntero);
			sEntero = unNumero(sEntero);
			log.debug("El entero es: " + sEntero);
			log.debug("Calculo de fraccion: " + sFraccion);
			sFraccion = unNumero(sFraccion);
			log.debug("La fraccion es: " + sFraccion);
			
			strNum = sEntero + sMoneda + "CON " + sFraccion + sCentimos;
			log.debug("sNumero: " + sNumero);
			if(Lo == -1) {
				sNumero = Utilidades.rTrim(strNum).substring(0, (sNumero.length() - 1));
			} else {
				sNumero = Utilidades.rTrim(strNum);
			}
			resultado = sNumero;
			log.debug("resultado: " + resultado);
		} else {
			log.debug("NUM1: " + strNum);
		    strNum = Utilidades.rTrim(unNumero(strNum) + sMoneda);
		    if(Lo == -1) {
		    	sNumero = strNum.substring(0, (sNumero.length() - 1));
		    } else {
		    	sNumero = strNum;
		    }
		    resultado = sNumero;
		}
		return resultado;
		//TODO HayError: MsgErr "[BibliParserWeb.Numero2Letra]"

	}
	
	private static String unNumero(String strNum) {
		//TODO No funciona con números que tengan delante de millones o mil el número 1
	    //----------------------------------------------------------
	    //Esta es la rutina principal                    (10/Jul/97)
	    //Está separada para poder actuar con decimales
	    //----------------------------------------------------------
		Double lngA;
		boolean negativo;
		int L;
		boolean una;
		boolean millon;
		boolean millones;
		int maxVez;
		int k;
		String strQ;
		String strB;
		String strU;
		String strD;
		String strC;
		int vez;
		int iA;
		
		String[] strN;
		String resultado;
	
		//Si se amplia este valor... no se manipularán bien los números
		int cAncho = 12;
		int cGrupos = cAncho / 3;
		
		//Si se produce un error que se pare el mundo!!!
		lngA = Math.abs(Double.parseDouble(strNum));
		negativo = (lngA.compareTo(Double.parseDouble(strNum)) != 0);
		strNum = Utilidades.lTrim(Utilidades.rTrim(strNum));
		L = strNum.length();
	
		if(lngA < 1) {
			resultado = "CERO";
			return resultado;
		}
	
		una = true;
		millon = false;
		millones = false;
		if(L < 4) una = false;
		if(lngA > 999999) millon = true;
		if(lngA > 1999999) millones = true;
		strB = "";
		strQ = strNum;
		vez = 0;
	
		strN = new String[cGrupos];
		strQ = Utilidades.repeatValue(cAncho, "0") + strNum;
		strQ = Utilidades.right(strQ, cAncho);
		log.debug("strQ: " + strQ);
		for(int j=strQ.length()-1; j>1; j=j-3) {
			vez = vez + 1;
			if (j+1 > strQ.length()) {
				strN[(vez-1)] = strQ.substring(j-2);
			} else {
				strN[(vez-1)] = strQ.substring(j-2, j+1);
			}
			
			log.debug("Vuelta J:"+j+" strN[(vez-1)]: " + strN[(vez-1)]);
		}
		
		maxVez = cGrupos;
		for(int j=cGrupos; j>=1; j--) {
			if(strN[(j-1)].equals("000")) {
				maxVez = maxVez - 1;
			} else {
				break;
			}
		}
		log.debug("maxVez: " + maxVez + " cGrupos: " + cGrupos);
		for(vez=1; vez<=maxVez; vez++) {
			strU = "";
			strD = "";
			strC = "";
			log.debug("VEZ: " + vez + " strNum: " + strNum + " strN[(vez-1)]: " + strN[(vez-1)]);
			strNum = strN[(vez-1)];
			L = strNum.length();
			k = Integer.parseInt(strNum.substring(strNum.length() - 2));
			log.debug("VEZ: " + vez + " strNum: " + strNum + " k: " + k);
			log.debug("CORTE: " + strNum.substring(strNum.length() - 2) + " L: " + L);
			if(strNum.substring(strNum.length() - 1).equals("0")) {
				k = k / 10;
				log.debug("acaba en 0. K: " + k);
				strD = decena[k];
			} else if(k > 10 && k < 16) {
				k = Integer.parseInt(strNum.substring(L - 2));
				strD = otros[k];
			} else {
				log.debug("asigno STRU: strNum="+strNum);
				strU = unidad[Integer.parseInt(Utilidades.right(strNum, 1))];
				log.debug("strU="+strU);
				if(L - 2 > 0) {
					k = Integer.parseInt(strNum.substring(L-2, L-1));
					if (k > 0) {
						strD = deci[k];
					}
				}
				log.debug("k aprox menor de 10 o mayor o igual de 16: " + k);
			}
			log.debug("VEZ: " + vez + " strU: " + strU + " strC " + strC);
			//Parche de Esteve
			if(L - 2 > 0) {
				k = Integer.parseInt("" + strNum.charAt(L - 3));
				//Con esto funcionará bien el 100100, por ejemplo...
				if(k == 1) {                      				//Parche
					if(Integer.parseInt(strNum) == 100) {       //Parche
						k = 10;                   				//Parche
					}                        	  				//Parche
				}
				if (k > 0) {
					strC = centena[k] + " ";
				}
			}
			log.debug("VEZ: " + vez + " CENTENA: " + strC + " K: " + k);
			//-------
			log.debug("PROBLEMA: " + Utilidades.left(strB, 4));
			log.debug("strU="+strU+" strB="+strB);
			if(strU.equals("UNO") && Utilidades.left(strB, 4).equals(" MIL")) {
				strU = "";
			}
			
			strB = strC + strD + strU + " " + strB;
	
			if(vez == 1 || vez == 3) {
				if(!strN[vez].equals("000")) strB = " MIL " + strB;
			}
			if(vez == 2 && millon) {
				if(millones) {
					strB = " MILLONES " + strB;
				} else {
					strB = "UN MILLÓN " + strB;
				}
			}
			log.debug("VEZ: " + vez + " strB: " + strB);
		}
		log.debug("strB: " + strB);
		strB = strB.trim();
		//Si acaba en UNO se le quita la O
		if(Utilidades.right(strB, 3).equals("UNO")) {
			strB = Utilidades.left(strB, (strB.length()-1)); //& "A"
		}
		//Quitar los espacios que haya por medio
		strB = strB.replace("  ", " ");
		strB = strB.replace("  ", " ");
		if(Utilidades.left(strB, 6).equals("UNO UN")) strB = strB.substring(4);
		if(Utilidades.left(strB, 6).equals("UN UN")) strB = strB.substring(4);
		if(Utilidades.left(strB, 7).equals("UN MIL")) strB = strB.substring(4);
		if(!Utilidades.right(strB, 16).equals("MILLONES MIL UNO")) {
			iA = strB.indexOf("MILLONES MIL UNO");
			if(iA >= 0) strB = Utilidades.left(strB, iA + 8) + strB.substring(iA + 12);
		}
		if(Utilidades.right(strB, 6).equals("CIENTO")) strB =  Utilidades.left(strB, (strB.length() - 2));
		if(negativo) strB = "MENOS " + strB;
	
		resultado = strB.trim();
		return resultado;
		//TODO HayError: MsgErr "[BibliParserWeb.UnNumero]"
	}
	
	public static String[] split(String cadena,String token){
		List<String> list = new ArrayList<String>();
		String[] resultado = new String[0];
		if (cadena!=null){	
			String value;
			String values = cadena;
			int index = values.indexOf(token);		
			while((index = values.indexOf(token))!=-1){				
				value = values.substring(0,index);
				value = (value==null)?"":value;
				list.add(value);
				values = values.substring(index+1);
			}
			list.add(values);
			resultado = new String[list.size()];
			resultado = list.toArray(resultado);
		}
		return resultado;
	}
	
}
