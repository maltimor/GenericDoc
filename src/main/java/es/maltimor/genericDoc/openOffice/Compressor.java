package es.maltimor.genericDoc.openOffice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Compressor {
	final static Logger log = LoggerFactory.getLogger(Compressor.class);

	public static void zip(String pathIn, String pathOut, String nombreArchivo){
		try {
			log.debug("ZIP-IN: " + pathIn);
			log.debug("ZIP-OUT: " + pathOut + nombreArchivo);
			File fin = new File(pathIn);
			FileOutputStream outzip = new FileOutputStream(pathOut + nombreArchivo);
			ZipOutputStream out = new ZipOutputStream(outzip);

			//zipFiles(fin, out, "");
			addFolderToZip("", pathIn, out);
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void addFileToZip(String path, String srcFile, ZipOutputStream zos) throws Exception {
	
		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zos);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			String aux=path + "/" + folder.getName();
			if (aux.length() > 4) {
				aux = aux.substring(5);
			}
			zos.putNextEntry(new ZipEntry(aux));
			while ((len = in.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
		}
	}

	public static void addFolderToZip(String path, String srcFolder, ZipOutputStream zos)
		      throws Exception {
		File folder = new File(srcFolder);
		
		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zos);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zos);
			}
		}
	}
	
	public static boolean unzip(String path, String nombreArchivo){
		try {
			FileInputStream inzip = new FileInputStream(nombreArchivo);
			ZipEntry entrada = null;
			ZipInputStream StrIn = new ZipInputStream(inzip);
			int BUFFER=32000;
			
			while (null != (entrada = StrIn.getNextEntry()) ){
			   log.debug("Archivo a descomprimir: " + entrada.getName());
			   File f = new File(path + entrada.getName());
			   if (!f.exists()){
				   if (f.isDirectory()) f.mkdirs();
				   else f.getParentFile().mkdirs();
			   }
			   if (!entrada.getName().endsWith("/")) { //TODO hacer más robusto la identificación de ficheros y carpetas
				   log.debug("ES FICHERO!!! " + entrada.getName());
				   FileOutputStream fos = new FileOutputStream(path + entrada.getName());
				   int leido;
				   byte [] buffer = new byte[BUFFER];
				   while (0<(leido = StrIn.read(buffer))){
				      fos.write(buffer,0,leido);
				   }
				   fos.close();
			   }
			   StrIn.closeEntry();
			}
			StrIn.close();
			inzip.close();
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
