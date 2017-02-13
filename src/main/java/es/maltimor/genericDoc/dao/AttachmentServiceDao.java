package es.maltimor.genericDoc.dao;

import java.util.List;

public interface AttachmentServiceDao {
	public boolean hasAttachments(String dbid, String unid) throws Exception;
	public boolean hasAttachment(String dbid, String unid, String name) throws Exception;
	public List<String> getAllAttachmentsNames(String dbid, String unid) throws Exception;
	public List<Attachment> getAllAttachments(String dbid, String unid) throws Exception;
	public void deleteAllAttachments(String dbid, String unid) throws Exception;
	public void deleteAttachment(String dbid, String unid,String fileName) throws Exception;
	public void addAttachment(String dbid, String unid,Attachment attach) throws Exception;
	public Attachment getAttachment(String dbid, String unid,String fileName) throws Exception;
}
