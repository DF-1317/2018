package org.productivity.java.syslog4j.server.impl.event.printstream;

import java.io.PrintStream;
import java.util.Date;

import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
* SystemOutSyslogServerEventHandler provides a simple example implementation
* of the SyslogServerEventHandlerIF which writes the events to System.out.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: PrintStreamSyslogServerEventHandler.java,v 1.3 2009/04/10 00:05:02 cvs Exp $
*/
public class PrintStreamSyslogServerEventHandler implements SyslogServerEventHandlerIF {
	private static final long serialVersionUID = 6036415838696050746L;
	
	protected PrintStream stream = null;
	
	public PrintStreamSyslogServerEventHandler(PrintStream stream) {
		this.stream = stream;
	}

	public void event(SyslogServerIF syslogServer, SyslogServerEventIF event) {
		String date = (event.getDate() == null ? new Date() : event.getDate()).toString();
		String facility = SyslogUtility.getFacilityString(event.getFacility());
		String level = SyslogUtility.getLevelString(event.getLevel());
		
		this.stream.println("{" + facility + "} " + date + " " + level + " " + event.getMessage());
	}
}
