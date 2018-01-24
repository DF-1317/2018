package org.productivity.java.syslog4j.server;

import java.io.Serializable;


/**
* SyslogServerEventHandlerIF provides an extensible interface for Syslog4j
* server event handlers.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: SyslogServerEventHandlerIF.java,v 1.1 2008/11/21 15:29:15 cvs Exp $
*/
public interface SyslogServerEventHandlerIF extends Serializable {
	public void event(SyslogServerIF syslogServer, SyslogServerEventIF event);
}
