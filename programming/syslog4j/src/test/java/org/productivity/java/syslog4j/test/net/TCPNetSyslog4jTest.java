package org.productivity.java.syslog4j.test.net;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.impl.message.processor.SyslogMessageProcessor;
import org.productivity.java.syslog4j.test.net.base.AbstractNetSyslog4jTest;

public class TCPNetSyslog4jTest extends AbstractNetSyslog4jTest {
	protected int getMessageCount() {
		return 100;
	}

	protected String getClientProtocol() {
		return "tcp";
	}

	protected String getServerProtocol() {
		return "tcp";
	}

	public void testSendReceive() {
		super._testSendReceive(true,true);
	}
	
	public void testThreadedSendReceive() {
		Syslog.getInstance("tcp").setMessageProcessor(SyslogMessageProcessor.getDefault());
		
		super._testThreadedSendReceive(50,true,true);
	}
}
