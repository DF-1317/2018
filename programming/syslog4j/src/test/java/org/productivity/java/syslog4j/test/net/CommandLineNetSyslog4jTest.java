package org.productivity.java.syslog4j.test.net;

import junit.framework.TestCase;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogMain;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;

public class CommandLineNetSyslog4jTest extends TestCase {
	public static class CaptureHandler implements SyslogServerEventHandlerIF {
		private static final long serialVersionUID = -432500986007750320L;
		
		public SyslogServerEventIF capturedEvent = null;

		public void event(SyslogServerIF syslogServer, SyslogServerEventIF event) {
			this.capturedEvent = event;
		}
	}

	public void testUDP() {
		testSendReceive("udp",false);
	}
	
	public void testTCP() {
		testSendReceive("tcp",true);
	}

	public void testSendReceive(String protocol, boolean useSyslogClass) {
		SyslogServer.getInstance(protocol).getConfig().setPort(1514);
		SyslogServerIF syslogServer = SyslogServer.getThreadedInstance(protocol);
		
		CaptureHandler captureHandler = new CaptureHandler();
		syslogServer.getConfig().addEventHandler(captureHandler);
		
		try {
			Thread.sleep(300);
			
		} catch (InterruptedException ie) {
			//
		}
		
		String message = "test message";
		
		try {
			if (useSyslogClass) {
				Syslog.main(new String[] { "-p", "1514", protocol, message });
				
			} else {
				SyslogMain.main(new String[] { "-p", "1514", protocol, message }, false);
			}
			
		} catch (Exception e) {
			//
		}
		
		try {
			Thread.sleep(500);
			
		} catch (InterruptedException ie) {
			//
		}
		
		assertTrue(captureHandler.capturedEvent.getMessage().endsWith(message));
		
		syslogServer.shutdown();

		try {
			Thread.sleep(200);
			
		} catch (InterruptedException ie) {
			//
		}	
	}
}
