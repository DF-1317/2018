package org.productivity.java.syslog4j.test.log4j.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.net.AbstractNetSyslogServerConfig;
import org.productivity.java.syslog4j.test.base.AbstractBaseTest;
import org.productivity.java.syslog4j.util.SyslogUtility;

public abstract class AbstractLog4jSyslog4jTest extends AbstractBaseTest {
	protected class RecorderHandler implements SyslogServerEventHandlerIF {
		private static final long serialVersionUID = 8040266564168724L;
		
		protected List recordedEvents = new ArrayList();
		
		public List getRecordedEvents() {
			return this.recordedEvents;
		}

		public void event(SyslogServerIF syslogServer, SyslogServerEventIF event) {
			String recordedEvent = SyslogUtility.newString(syslogServer.getConfig(),event.getRaw());
			
			recordedEvent = recordedEvent.substring(recordedEvent.toUpperCase().indexOf("[TEST] "));

			this.recordedEvents.add(recordedEvent);
		}
	}
	
	public static final int TEST_PORT = 10514;

	protected SyslogServerIF server = null;
	
	protected abstract String getServerProtocol();
	
	protected abstract int getMessageCount();

	protected RecorderHandler recorderEventHandler = new RecorderHandler();
	
	protected void startServerThread(String protocol) {
		this.server = SyslogServer.getInstance(protocol);
		
		AbstractNetSyslogServerConfig config = (AbstractNetSyslogServerConfig) this.server.getConfig();
		config.setPort(TEST_PORT);
		config.addEventHandler(this.recorderEventHandler);

		this.server = SyslogServer.getThreadedInstance(protocol);
	}

	public void setUp() {
		UDPNetSyslogConfig config = new UDPNetSyslogConfig();
		
		assertTrue(config.isCacheHostAddress());
		config.setCacheHostAddress(false);
		assertFalse(config.isCacheHostAddress());
		
		assertTrue(config.isThrowExceptionOnInitialize());
		config.setThrowExceptionOnInitialize(false);
		assertFalse(config.isThrowExceptionOnInitialize());
		
		assertFalse(config.isThrowExceptionOnWrite());
		config.setThrowExceptionOnWrite(true);
		assertTrue(config.isThrowExceptionOnWrite());
		
		Syslog.createInstance("log4jUdp",config);
		
		String protocol = getServerProtocol();
		
		startServerThread(protocol);
		sleep(500);
	}
	
	protected void verifySendReceive(List events, boolean sort) {
		if (sort) {
			Collections.sort(events);
		}
		
		List recordedEvents = this.recorderEventHandler.getRecordedEvents();
		
		if (sort) {
			Collections.sort(recordedEvents);
		}
		
		for(int i=0; i < events.size(); i++) {
			String sentEvent = (String) events.get(i);
			
			String recordedEvent = (String) recordedEvents.get(i);
			
			if (!sentEvent.equals(recordedEvent)) {
				System.out.println("SENT: " + sentEvent);
				System.out.println("RCVD: " + recordedEvent);
				
				fail("Sent and recorded events do not match");
			}
		}
	}
	
	public void _testSendReceive(){
		Logger logger = Logger.getLogger(this.getClass());
		
		List events = new ArrayList();
		
		for(int i=0; i<getMessageCount(); i++) {
			String message = "[TEST] " + i + " / " + System.currentTimeMillis();
			
			logger.info(message);
			events.add(message);
		}
		
		sleep(1000);
		
		verifySendReceive(events,true);
	}
	
	public void tearDown() {
		Syslog.shutdown();

		sleep(125);
		
		SyslogServer.shutdown();
		
		sleep(125);

		Syslog.initialize();
		SyslogServer.initialize();
	}
}
