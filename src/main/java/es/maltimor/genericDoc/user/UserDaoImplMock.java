package es.maltimor.genericDoc.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserDaoImplMock extends es.maltimor.genericUser.UserDaoImplMock {

	@Override
	public String getLogin() throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (subject==null) return "-no autenticado-";
		if (!subject.isAuthenticated()) return "-no autenticado-";
		Object obj = subject.getPrincipal();
		if (obj==null) return "-no principal-";
		
		System.out.println("GET LOGIN:"+obj.toString());
		return obj.toString();
	}
}
