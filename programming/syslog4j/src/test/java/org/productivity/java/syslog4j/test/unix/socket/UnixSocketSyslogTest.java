package org.productivity.java.syslog4j.test.unix.socket;

import junit.framework.TestCase;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.unix.socket.UnixSocketSyslogConfig;

public class UnixSocketSyslogTest extends TestCase {
	public void testUnixSyslog() {
		SyslogIF syslog = Syslog.getInstance(SyslogConstants.UNIX_SOCKET);
		
		UnixSocketSyslogConfig config = (UnixSocketSyslogConfig) syslog.getConfig();
		
		config.setPath("/tmp/syslog4j.sock");
		config.setType(UnixSocketSyslogConfig.SOCK_STREAM);
		
		syslog.info(this.getClass().getName() + ": unix_socket " + System.currentTimeMillis());
		
		syslog.flush();
		
		try { Thread.sleep(1000); } catch (Exception e) { }
	} 
}
