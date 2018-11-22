package es.maltimor.genericDoc.utils;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;

import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

public class CasRealmWrapper extends CasRealm {

	@Override
	protected void onInit() {
		//System.out.println("CasReal.onInit:");
		super.onInit();
	}

	@Override
	protected TicketValidator ensureTicketValidator() {
		//System.out.println("CasReal.ensureTicketValidator:");
		TicketValidator res = super.ensureTicketValidator();
		//System.out.println("CasReal.ensureTicketValidator->"+res);
		return res;
	}

	@Override
	protected TicketValidator createTicketValidator() {
		//System.out.println("CasReal.createTicketValidator:");
		TicketValidator res = super.createTicketValidator();
		//System.out.println("CasReal.createTicketValidator->"+res);
		return res;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//System.out.println("CasReal.doGetAuthenticationInfo:"+token.getPrincipal()+" "+token.getCredentials());
/*		AuthenticationInfo res = super.doGetAuthenticationInfo(token);
		//System.out.println("CasReal.doGetAuthenticationInfo->"+res.getPrincipals()+" "+res.getCredentials()+" "+res);
		return res;*/
		
		CasToken casToken = (CasToken) token;
		String ticket = (String)casToken.getCredentials();
		try{
			System.out.println("================================");
			System.out.println("casToken.ticket="+ticket);
			TicketValidator ticketValidator = ensureTicketValidator();
			ticketValidator = new Cas20ServiceTicketValidator(getCasServerUrlPrefix());
			
			// contact CAS server to validate service ticket
            Assertion casAssertion = ticketValidator.validate(ticket, getCasService());

			System.out.println("casAssertion:"+casAssertion.getAttributes()+" "+casAssertion.getPrincipal());
			System.out.println("FIN ================================");
            
            // get principal, user id and attributes
            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String userId = casPrincipal.getName();
            
            Map<String, Object> attributes = casPrincipal.getAttributes();
            // refresh authentication token (user id + remember me)
            casToken.setUserId(userId);
            String rememberMeAttributeName = getRememberMeAttributeName();
            String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
            boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
            if (isRemembered) {
                casToken.setRememberMe(true);
            }
            // create simple authentication info
            List<Object> principals = CollectionUtils.asList(userId, attributes);
            PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
            return new SimpleAuthenticationInfo(principalCollection, ticket);
			
		} catch (AuthenticationException e){
			e.printStackTrace();
			throw e;
		} catch (TicketValidationException e) {
			e.printStackTrace();
			 throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
		} catch (Exception e) {
			e.printStackTrace();
			 throw new CasAuthenticationException("JODER!", e);
		}
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//System.out.println("CasReal.doGetAuthorizationInfo:"+principals.asList());
		AuthorizationInfo res = super.doGetAuthorizationInfo(principals); 
		//System.out.println("CasReal.doGetAuthorizationInfo->"+res.getRoles()+" "+res);
		return res;
	}

	@Override
	public String getCasServerUrlPrefix() {
		//System.out.println("CasReal.getCasServerUrlPrefix:"+super.getCasServerUrlPrefix());
		return super.getCasServerUrlPrefix();
	}

	@Override
	public void setCasServerUrlPrefix(String casServerUrlPrefix) {
		//System.out.println("CasReal.setCasServerUrlPrefix:"+casServerUrlPrefix);
		super.setCasServerUrlPrefix(casServerUrlPrefix);
	}

	@Override
	public String getCasService() {
		//System.out.println("CasReal.getCasService:"+super.getCasService());
		return super.getCasService();
	}

	@Override
	public void setCasService(String casService) {
		//System.out.println("CasReal.setCasService:"+casService);
		super.setCasService(casService);
	}

	@Override
	public String getValidationProtocol() {
		//System.out.println("CasReal.getValidationProtocol:"+super.getValidationProtocol());
		return super.getValidationProtocol();
	}

	@Override
	public void setValidationProtocol(String validationProtocol) {
		//System.out.println("CasReal.setValidationProtocol:"+validationProtocol);
		super.setValidationProtocol(validationProtocol);
	}

	@Override
	public String getRememberMeAttributeName() {
		//System.out.println("CasReal.getRememberMeAttributeName:"+super.getRememberMeAttributeName());
		return super.getRememberMeAttributeName();
	}

	@Override
	public void setRememberMeAttributeName(String rememberMeAttributeName) {
		//System.out.println("CasReal.setRememberMeAttributeName:"+rememberMeAttributeName);
		super.setRememberMeAttributeName(rememberMeAttributeName);
	}

	@Override
	public String getDefaultRoles() {
		//System.out.println("CasReal.getDefaultRoles:"+super.getDefaultRoles());
		return super.getDefaultRoles();
	}

	@Override
	public void setDefaultRoles(String defaultRoles) {
		//System.out.println("CasReal.setDefaultRoles:"+defaultRoles);
		super.setDefaultRoles(defaultRoles);
	}

	@Override
	public String getDefaultPermissions() {
		//System.out.println("CasReal.getDefaultPermissions:"+super.getDefaultPermissions());
		return super.getDefaultPermissions();
	}

	@Override
	public void setDefaultPermissions(String defaultPermissions) {
		//System.out.println("CasReal.setDefaultPermissions:"+defaultPermissions);
		super.setDefaultPermissions(defaultPermissions);
	}

	@Override
	public String getRoleAttributeNames() {
		//System.out.println("CasReal.getRoleAttributeNames:"+super.getRoleAttributeNames());
		return super.getRoleAttributeNames();
	}

	@Override
	public void setRoleAttributeNames(String roleAttributeNames) {
		//System.out.println("CasReal.setRoleAttributeNames:"+roleAttributeNames);
		super.setRoleAttributeNames(roleAttributeNames);
	}

	@Override
	public String getPermissionAttributeNames() {
		//System.out.println("CasReal.getPermissionAttributeNames:"+super.getPermissionAttributeNames());
		return super.getPermissionAttributeNames();
	}

	@Override
	public void setPermissionAttributeNames(String permissionAttributeNames) {
		//System.out.println("CasReal.setPermissionAttributeNames:"+permissionAttributeNames);
		super.setPermissionAttributeNames(permissionAttributeNames);
	}

}
