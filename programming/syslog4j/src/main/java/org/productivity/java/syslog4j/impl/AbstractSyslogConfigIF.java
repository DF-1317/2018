package org.productivity.java.syslog4j.impl;

import java.util.List;

import org.productivity.java.syslog4j.SyslogConfigIF;

/**
* AbstractSyslogConfigIF provides an interface for all Abstract Syslog
* configuration implementations.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: AbstractSyslogConfigIF.java,v 1.5 2009/06/06 19:13:23 cvs Exp $
*/
public interface AbstractSyslogConfigIF extends SyslogConfigIF {
	public Class getSyslogWriterClass();
	
	public List getBackLogHandlers();
	
	public List getMessageModifiers();

	public byte[] getSplitMessageBeginText();
	public void setSplitMessageBeginText(byte[] beginText);
	
	public byte[] getSplitMessageEndText();
	public void setSplitMessageEndText(byte[] endText);

	public boolean isThreaded();
	public void setThreaded(boolean threaded);
	
	public long getThreadLoopInterval();
	public void setThreadLoopInterval(long threadLoopInterval);
	
	public int getMaxShutdownWait();
	public void setMaxShutdownWait(int maxShutdownWait);
	
	public int getWriteRetries();
	public void setWriteRetries(int writeRetries);
}
