package org.productivity.java.syslog4j.server.impl;

import java.net.InetAddress;

import org.productivity.java.syslog4j.SyslogCharSetIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.event.SyslogServerEvent;
import org.productivity.java.syslog4j.server.impl.event.structured.StructuredSyslogServerEvent;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
* AbstractSyslogServer provides a base abstract implementation of the SyslogServerIF.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: AbstractSyslogServer.java,v 1.7 2009/07/22 15:54:23 cvs Exp $
*/
public abstract class AbstractSyslogServer implements SyslogServerIF {
	protected String syslogProtocol = null;
	protected AbstractSyslogServerConfig syslogServerConfig = null;
	protected Thread thread = null;
	
	protected boolean shutdown = false;
	
	public void initialize(String protocol, SyslogServerConfigIF config) throws SyslogRuntimeException {
		this.syslogProtocol = protocol;
		
		try {
			this.syslogServerConfig = (AbstractSyslogServerConfig) config;
			
		} catch (ClassCastException cce) {
			throw new SyslogRuntimeException(cce);
		}
		
		initialize();
	}
	
	public String getProtocol() {
		return this.syslogProtocol;
	}

	public SyslogServerConfigIF getConfig() {
		return this.syslogServerConfig;
	}
	
	protected abstract void initialize() throws SyslogRuntimeException;
	
	public void shutdown() throws SyslogRuntimeException {
		this.shutdown = true;
	}

	public Thread getThread() {
		return this.thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}
	
	protected static boolean isStructuredMessage(SyslogCharSetIF syslogCharSet, byte[] receiveData) {
		String msg = SyslogUtility.newString(syslogCharSet, receiveData);

		int idx = msg.indexOf('>');

		if (idx != -1) {
			// If there's a numerical VERSION field after the <priority>, return true.
			if (msg.length() > idx + 1 && Character.isDigit(msg.charAt(idx + 1))) {
				return true;
			}
		}

		return false;
	}
	
	protected static SyslogServerEventIF createEvent(SyslogServerConfigIF serverConfig, byte[] lineBytes, int lineBytesLength, InetAddress inetAddr) {
		SyslogServerEventIF event = null;
		
		if (serverConfig.isUseStructuredData() && AbstractSyslogServer.isStructuredMessage(serverConfig,lineBytes)) {
			event = new StructuredSyslogServerEvent(lineBytes,lineBytesLength,inetAddr);
			
		} else {
			event = new SyslogServerEvent(lineBytes,lineBytesLength,inetAddr);
		}		
		
		return event;
	}
}
