package es.maltimor.genericDoc.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import es.maltimor.genericDoc.utils.FilesUtils;

public class Attachment {
	private long lastModified;
	private String path;
	private String name;
	private byte[] bytes;
	
	public Attachment(){
		this.lastModified=0;
		this.path="";
		this.name="";
		this.bytes = new byte[0];
	}
	
	public void extractFile(String fileName) throws Exception{
		//de momento me limito a extraer anexos a disco, sin mas
		FilesUtils.preparaPath(fileName,true);
		FileOutputStream out = new FileOutputStream(fileName);
		out.write(bytes);
		out.close();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
