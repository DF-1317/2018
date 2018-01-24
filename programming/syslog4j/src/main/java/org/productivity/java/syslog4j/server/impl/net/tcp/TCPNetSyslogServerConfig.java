package org.productivity.java.syslog4j.server.impl.net.tcp;

import org.productivity.java.syslog4j.server.impl.net.AbstractNetSyslogServerConfig;

/**
* TCPNetSyslogServerConfig provides configuration for TCPNetSyslogServer.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: TCPNetSyslogServerConfig.java,v 1.6 2009/03/29 17:38:58 cvs Exp $
*/
public class TCPNetSyslogServerConfig extends AbstractNetSyslogServerConfig implements TCPNetSyslogServerConfigIF {
	private static final long serialVersionUID = -1546696301177599370L;
	
	protected int backlog = 0;

	public TCPNetSyslogServerConfig() {
		//
	}

	public TCPNetSyslogServerConfig(int port) {
		this.port = port;
	}

	public TCPNetSyslogServerConfig(int port, int backlog) {
		this.port = port;
		this.backlog = backlog;
	}

	public TCPNetSyslogServerConfig(String host) {
		this.host = host;
	}

	public TCPNetSyslogServerConfig(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public TCPNetSyslogServerConfig(String host, int port, int backlog) {
		this.host = host;
		this.port = port;
		this.backlog = backlog;
	}

	public Class getSyslogServerClass() {
		return TCPNetSyslogServer.class;
	}

	public int getBacklog() {
		return this.backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}
}
