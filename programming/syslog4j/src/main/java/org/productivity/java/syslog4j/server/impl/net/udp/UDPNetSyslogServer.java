package org.productivity.java.syslog4j.server.impl.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.impl.AbstractSyslogServer;

/**
* UDPNetSyslogServer provides a simple non-threaded UDP/IP server implementation.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: UDPNetSyslogServer.java,v 1.12 2009/07/22 15:54:23 cvs Exp $
*/
public class UDPNetSyslogServer extends AbstractSyslogServer {
	protected DatagramSocket ds = null;

	public void initialize() throws SyslogRuntimeException {
		//
	}
	
	public void shutdown() {
		super.shutdown();
		
		if (this.syslogServerConfig.getShutdownWait() > 0) {
			try {
				Thread.sleep(this.syslogServerConfig.getShutdownWait());
				
			} catch (InterruptedException ie) {
				//
			}
		}
		
		if (this.ds != null && !this.ds.isClosed()) {
			this.ds.close();
		}
	}
	
	protected DatagramSocket createDatagramSocket() throws SocketException, UnknownHostException {
		DatagramSocket datagramSocket = null;
		
		if (this.syslogServerConfig.getHost() != null) {
			InetAddress inetAddress = InetAddress.getByName(this.syslogServerConfig.getHost());
			
			datagramSocket = new DatagramSocket(this.syslogServerConfig.getPort(),inetAddress);
			
		} else {
			datagramSocket = new DatagramSocket(this.syslogServerConfig.getPort());
		}
		
		return datagramSocket;
	}

	public void run() {
		try {
			this.ds = createDatagramSocket();
			this.shutdown = false;
			
		} catch (SocketException se) {
			return;
			
		} catch (UnknownHostException uhe) {
			return;
		}
			
		byte[] receiveData = new byte[SyslogConstants.SYSLOG_BUFFER_SIZE];
					
		while(!this.shutdown) {
			try {
				DatagramPacket dp = new DatagramPacket(receiveData,receiveData.length);
				
				this.ds.receive(dp);
				
				SyslogServerEventIF event = createEvent(this.getConfig(),receiveData,dp.getLength(),dp.getAddress());
								
				List list = this.syslogServerConfig.getEventHandlers();
				
				for(int i=0; i<list.size(); i++) {
					SyslogServerEventHandlerIF eventHandler = (SyslogServerEventHandlerIF) list.get(i);
					
					eventHandler.event(this,event);
				}
				
			} catch (SocketException se) {
				//
				
			} catch (IOException ioe) {
				//
			}
		}
	}
}
