package es.maltimor.genericDoc.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.subject.Subject;

public class CasFilterWrapper extends CasFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		//System.out.println("createToken:");
		AuthenticationToken res = super.createToken(request, response);
		//System.out.println("createToken->"+res+" "+res.getPrincipal()+" "+res.getCredentials());
		return res;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//System.out.println("onAccessDenied:");
		return super.onAccessDenied(request, response);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//System.out.println("isAccessAllowed:"+ mappedValue+" :"+super.isAccessAllowed(request, response, mappedValue));
		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		System.out.println("onLoginSuccess:"+token.getPrincipal()+" "+token.getCredentials()+" "+subject+" ");
		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
			ServletResponse response) {
		System.out.println("onLoginFailure:"+token.getPrincipal()+" "+token.getCredentials()+" "+ae+" ");
		return super.onLoginFailure(token, ae, request, response);
	}

	@Override
	public void setFailureUrl(String failureUrl) {
		//System.out.println("setFailureUrl:"+failureUrl);
		super.setFailureUrl(failureUrl);
	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		//System.out.println("executeLogin:");
		boolean res = super.executeLogin(request, response);
		//System.out.println("executeLogin->"+res);
		return res;
	}

	@Override
	protected AuthenticationToken createToken(String username, String password, ServletRequest request,
			ServletResponse response) {
		//System.out.println("createToken:"+username+" "+password+" ");
		return super.createToken(username, password, request, response);
	}

	@Override
	protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
		//System.out.println("createToken:"+username+" "+password+" "+rememberMe+" "+host);
		return super.createToken(username, password, rememberMe, host);
	}

	@Override
	protected String getHost(ServletRequest request) {
		//System.out.println("getHost:"+request+" "+super.getHost(request));
		return super.getHost(request);
	}

	@Override
	protected boolean isRememberMe(ServletRequest request) {
		//System.out.println("isRememberMe:"+request+" :"+super.isRememberMe(request));
		return super.isRememberMe(request);
	}

	@Override
	protected boolean isPermissive(Object mappedValue) {
		//System.out.println("isPermissive:"+mappedValue+" :"+super.isPermissive(mappedValue));
		return super.isPermissive(mappedValue);
	}

	@Override
	protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
			throws ServletException, IOException {
		//System.out.println("cleanup:"+existing);
		super.cleanup(request, response, existing);
	}

	@Override
	public String getSuccessUrl() {
		//System.out.println("getSuccessUrl:"+super.getSuccessUrl());
		return super.getSuccessUrl();
	}

	@Override
	public void setSuccessUrl(String successUrl) {
		//System.out.println("setSuccessUrl:"+successUrl);
		super.setSuccessUrl(successUrl);
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		//System.out.println("issueSuccessRedirect:");
		super.issueSuccessRedirect(request, response);
	}

	@Override
	public String getLoginUrl() {
		//System.out.println("getLoginUrl:"+super.getLoginUrl());
		return super.getLoginUrl();
	}

	@Override
	public void setLoginUrl(String loginUrl) {
		//System.out.println("setLoginUrl:"+loginUrl);
		super.setLoginUrl(loginUrl);
	}

	@Override
	protected Subject getSubject(ServletRequest request, ServletResponse response) {
		//System.out.println("getSubject:");
		Subject res = super.getSubject(request, response); 
		//System.out.println("getSubject->"+res.getPrincipal());
		return res;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		//System.out.println("onAccessDenied:"+" "+mappedValue);
		boolean res = super.onAccessDenied(request, response, mappedValue);
		//System.out.println("onAccessDenied->"+res);
		return res;
	}

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		//System.out.println("onPreHandle:"+mappedValue);
		return super.onPreHandle(request, response, mappedValue);
	}

	@Override
	protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
		//System.out.println("isLoginRequest:"+" :"+super.isLoginRequest(request, response));
		return super.isLoginRequest(request, response);
	}

	@Override
	protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		//System.out.println("saveRequestAndRedirectToLogin:");
		super.saveRequestAndRedirectToLogin(request, response);
	}

	@Override
	protected void saveRequest(ServletRequest request) {
		//System.out.println("saveRequest:");
		super.saveRequest(request);
	}

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		//System.out.println("redirectToLogin:");
		super.redirectToLogin(request, response);
	}

	@Override
	public Filter processPathConfig(String path, String config) {
		//System.out.println("processPathConfig:");
		return super.processPathConfig(path, config);
	}

	@Override
	protected String getPathWithinApplication(ServletRequest request) {
		//System.out.println("getPathWithinApplication:");
		return super.getPathWithinApplication(request);
	}

	@Override
	protected boolean pathsMatch(String path, ServletRequest request) {
		//System.out.println("pathsMatch:"+" :"+super.pathsMatch(path, request));
		return super.pathsMatch(path, request);
	}

	@Override
	protected boolean pathsMatch(String pattern, String path) {
		//System.out.println("pathsMatch:"+pattern+" "+path+" :"+super.pathsMatch(pattern, path));
		return super.pathsMatch(pattern, path);
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		//System.out.println("preHandle:");
		return super.preHandle(request, response);
	}

	@Override
	protected boolean isEnabled(ServletRequest request, ServletResponse response, String path, Object mappedValue)
			throws Exception {
		//System.out.println("isEnabled:"+path+" "+mappedValue);
		return super.isEnabled(request, response, path, mappedValue);
	}

	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
		//System.out.println("postHandle:");
		super.postHandle(request, response);
	}

	@Override
	public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception)
			throws Exception {
		//System.out.println("afterCompletion:"+exception);
		super.afterCompletion(request, response, exception);
	}

	@Override
	protected void executeChain(ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
		//System.out.println("executeChain:");
		super.executeChain(request, response, chain);
	}

	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		//System.out.println("doFilterInternal:");
		super.doFilterInternal(request, response, chain);
	}

	@Override
	public boolean isEnabled() {
		//System.out.println("isEnabled:"+super.isEnabled());
		return super.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		//System.out.println("setEnabled:");
		super.setEnabled(enabled);
	}

	@Override
	protected boolean isEnabled(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		//System.out.println("isEnabled:"+super.isEnabled(request, response));
		return super.isEnabled(request, response);
	}

	@Override
	protected String getAlreadyFilteredAttributeName() {
		//System.out.println("getAlreadyFilteredAttributeName:"+super.getAlreadyFilteredAttributeName());
		return super.getAlreadyFilteredAttributeName();
	}

	@Override
	protected boolean shouldNotFilter(ServletRequest request) throws ServletException {
		//System.out.println("shouldNotFilter:"+super.shouldNotFilter(request));
		return super.shouldNotFilter(request);
	}

	@Override
	protected String getName() {
		//System.out.println("getName:"+super.getName());
		return super.getName();
	}

	@Override
	public void setName(String name) {
		//System.out.println("setName:");
		super.setName(name);
	}

	@Override
	protected StringBuilder toStringBuilder() {
		//System.out.println("toStringBuilder:");
		return super.toStringBuilder();
	}

	@Override
	public FilterConfig getFilterConfig() {
		//System.out.println("getFilterConfig:");
		return super.getFilterConfig();
	}

	@Override
	public void setFilterConfig(FilterConfig filterConfig) {
		//System.out.println("setFilterConfig:");
		super.setFilterConfig(filterConfig);
	}

	@Override
	protected String getInitParam(String paramName) {
		//System.out.println("getInitParam:");
		return super.getInitParam(paramName);
	}

	@Override
	protected void onFilterConfigSet() throws Exception {
		//System.out.println("onFilterConfigSet:");
		super.onFilterConfigSet();
	}

	@Override
	public void destroy() {
		//System.out.println("destroy:");
		super.destroy();
	}

	@Override
	public ServletContext getServletContext() {
		//System.out.println("getServletContext:");
		return super.getServletContext();
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		//System.out.println("setServletContext:");
		super.setServletContext(servletContext);
	}

	@Override
	protected String getContextInitParam(String paramName) {
		//System.out.println("getContextInitParam:");
		return super.getContextInitParam(paramName);
	}

	@Override
	protected void setContextAttribute(String key, Object value) {
		//System.out.println("setContextAttribute:");
		super.setContextAttribute(key, value);
	}

	@Override
	protected Object getContextAttribute(String key) {
		//System.out.println("getContextAttribute:");
		return super.getContextAttribute(key);
	}

	@Override
	protected void removeContextAttribute(String key) {
		//System.out.println("removeContextAttribute:");
		super.removeContextAttribute(key);
	}

	@Override
	public String toString() {
		//System.out.println("toString:");
		return super.toString();
	}

}
