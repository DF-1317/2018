package org.productivity.java.syslog4j.test.net;

import org.productivity.java.syslog4j.test.net.base.AbstractNetSyslog4jTest;

public class UDPNetSyslog4jTest extends AbstractNetSyslog4jTest {
	protected int getMessageCount() {
		return 100;
	}

	protected String getClientProtocol() {
		return "udp";
	}
	
	protected String getServerProtocol() {
		return "udp";
	}
	
	public void xtestSendReceive() {
		super._testSendReceive(true,true);
	}
	
	public void xtestThreadedSendReceive() {
		super._testThreadedSendReceive(50,true,true);
	}
	
	public void xtestPCIMessages() {
		super._testSendReceivePCIMessages(true,true);
	}

	public void testStructuredMessages() {
		super._testSendReceiveStructuredMessages(true,true);
	}
}
