package org.productivity.java.syslog4j.test.unix;

import junit.framework.TestCase;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogIF;

public class UnixSyslogTest extends TestCase {
	public void testUnixSyslog() {
		SyslogIF syslog = Syslog.getInstance(SyslogConstants.UNIX_SYSLOG);
		
		syslog.getConfig().setFacility(SyslogIF.FACILITY_KERN);
		
		syslog.error(this.getClass().getName() + ": unix_syslog " + System.currentTimeMillis());
		
		syslog.flush();
	}
}
