package org.productivity.java.syslog4j.server.impl.net.tcp;

import org.productivity.java.syslog4j.server.SyslogServerConfigIF;

/**
* TCPNetSyslogServerConfigIF provides configuration for TCPNetSyslogServer.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: TCPNetSyslogServerConfigIF.java,v 1.1 2009/03/29 17:38:58 cvs Exp $
*/
public interface TCPNetSyslogServerConfigIF extends SyslogServerConfigIF {
	public int getBacklog();
	public void setBacklog(int backlog);
}
