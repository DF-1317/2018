package org.productivity.java.syslog4j.server.impl.net.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.net.ServerSocketFactory;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.AbstractSyslogServer;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
* TCPNetSyslogServer provides a simple threaded TCP/IP server implementation.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: TCPNetSyslogServer.java,v 1.16 2009/07/22 15:54:23 cvs Exp $
*/
public class TCPNetSyslogServer extends AbstractSyslogServer {
	public static class TCPNetSyslogSocketHandler implements Runnable {
		protected SyslogServerIF server = null;
		protected Socket socket = null;
		protected Set sockets = null;
		
		public TCPNetSyslogSocketHandler(Set sockets, SyslogServerIF server, Socket socket) {
			this.sockets = sockets;
			this.server = server;
			this.socket = socket;
			
			synchronized(this.sockets) {
				this.sockets.add(this.socket);
			}
		}
		
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

				String line = br.readLine();
				
				while (line != null && line.length() != 0) {
					byte[] lineBytes = SyslogUtility.getBytes(this.server.getConfig(),line);
					
					SyslogServerEventIF event = createEvent(this.server.getConfig(),lineBytes,lineBytes.length,this.socket.getInetAddress());
										
					List eventHandlers = this.server.getConfig().getEventHandlers();
					
					for(int i=0; i<eventHandlers.size(); i++) {
						SyslogServerEventHandlerIF eventHandler = (SyslogServerEventHandlerIF) eventHandlers.get(i);
						
						eventHandler.event(this.server,event);
					}

					line = br.readLine();
				}
				
			} catch (SocketException se) {
				if ("Socket closed".equals(se.getMessage())) {
					//
					
				} else {
					//
				}
				
			} catch (IOException ioe) {
				//
			}
			
			try {
				this.socket.close();
				
				synchronized(this.sockets) {
					this.sockets.remove(this.socket);
				}
				
			} catch (IOException ioe) {
				//
			}
		}
	}

	protected ServerSocket serverSocket = null;
	
	protected Set sockets = new HashSet();
	
	protected TCPNetSyslogServerConfigIF tcpNetSyslogServerConfig = null;
	
	public void initialize() throws SyslogRuntimeException {
		this.tcpNetSyslogServerConfig = null;
		
		try {
			this.tcpNetSyslogServerConfig = (TCPNetSyslogServerConfigIF) this.syslogServerConfig;
			
		} catch (ClassCastException cce) {
			throw new SyslogRuntimeException("config must be of type TCPNetSyslogServerConfig");
		}
		
		if (this.syslogServerConfig == null) {
			throw new SyslogRuntimeException("config cannot be null");
		}

		if (this.tcpNetSyslogServerConfig.getBacklog() < 1) {
			this.tcpNetSyslogServerConfig.setBacklog(SyslogConstants.SERVER_SOCKET_BACKLOG_DEFAULT);
		}
		
	}
	
	public synchronized void shutdown() {
		super.shutdown();
		
		try {
			if (this.serverSocket != null) {
				if (this.syslogServerConfig.getShutdownWait() > 0) {
					try {
						Thread.sleep(this.syslogServerConfig.getShutdownWait());
						
					} catch (InterruptedException ie) {
						//
					}
				}
				
				this.serverSocket.close();
			}
			
			synchronized(this.sockets) {
				if (this.sockets != null && this.sockets.size() > 0) {
					Iterator i = this.sockets.iterator();
					
					while(i.hasNext()) {
						Socket s = (Socket) i.next();
						
						s.close();
					}
				}
			}
			
		} catch (IOException ioe) {
			//
		}
		
		if (this.thread != null) {
			this.thread.interrupt();
			this.thread = null;
		}
	}

	protected ServerSocketFactory getServerSocketFactory() throws IOException {
		ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
		
		return serverSocketFactory;
	}
	
	protected ServerSocket createServerSocket() throws IOException {
		ServerSocket newServerSocket = null;
		
		ServerSocketFactory factory = getServerSocketFactory();
		
		if (this.syslogServerConfig.getHost() != null) {
			InetAddress inetAddress = InetAddress.getByName(this.syslogServerConfig.getHost());
				
			newServerSocket = factory.createServerSocket(this.syslogServerConfig.getPort(),this.tcpNetSyslogServerConfig.getBacklog(),inetAddress); 
				
		} else {
			if (this.tcpNetSyslogServerConfig.getBacklog() < 1) {
				newServerSocket = factory.createServerSocket(this.syslogServerConfig.getPort());
				
			} else {
				newServerSocket = factory.createServerSocket(this.syslogServerConfig.getPort(),this.tcpNetSyslogServerConfig.getBacklog());				
			}
		}
		
		return newServerSocket;
	}

	public void run() {
		try {
			this.serverSocket = createServerSocket();
			this.shutdown = false;
			
		} catch (SocketException se) {
			throw new SyslogRuntimeException(se);

		} catch (IOException ioe) {
			throw new SyslogRuntimeException(ioe);
		}
			
		while(!this.shutdown) {
			try {
				Socket socket = this.serverSocket.accept();
				
				TCPNetSyslogSocketHandler handler = new TCPNetSyslogSocketHandler(this.sockets,this,socket);
				
				Thread t = new Thread(handler);
				
				t.start();
				
			} catch (SocketException se) {
				if ("Socket closed".equals(se.getMessage())) {
					this.shutdown = true;
					
				} else {
					//
				}
				
			} catch (IOException ioe) {
				System.err.println(ioe);
				
				try {
					Thread.sleep(500);
					
				} catch (InterruptedException ie) {
					//
				}
			}
		}
	}
}
