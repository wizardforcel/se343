package bookstore.jaas;

import java.io.*;
import java.lang.reflect.*;
import java.security.*;
import java.util.*;

import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;

import bookstore.remote.*;

public class SimpleLoginModule implements LoginModule
{

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	
	private UserSysRemote bean
	  = SessionBeanFactory.GetUserSysBean();
	
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
	}

	@Override
	public boolean login() throws LoginException {
		if(callbackHandler == null)
			throw new LoginException("No Handler");
		UserCallback uc = new UserCallback();
		try {
			callbackHandler.handle(new Callback[] {uc});
		} catch (IOException e) {
			LoginException le = new LoginException("Unspported Handler");
			le.initCause(e);
			throw le;
		} catch (UnsupportedCallbackException e) {
			LoginException le = new LoginException("Unspported Handler");
			le.initCause(e);
			throw le;
		}
		
		UserResultInfo info = bean.login(uc.getName(), uc.getPassword());
		ResultCallback rc = new ResultCallback();
		rc.setId(info.getUid());
		rc.setErrno(info.getErrno());
		rc.setErrmsg(info.getErrmsg());
		
		try {
			callbackHandler.handle(new Callback[] {rc});
		} catch (IOException e) {
			LoginException le = new LoginException("Unspported Handler");
			le.initCause(e);
			throw le;
		} catch (UnsupportedCallbackException e) {
			LoginException le = new LoginException("Unspported Handler");
			le.initCause(e);
			throw le;
		}
		
		return info.getErrno() == 0;
	}

	@Override
	public boolean commit() throws LoginException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		// TODO Auto-generated method stub
		return true;
	}

}
