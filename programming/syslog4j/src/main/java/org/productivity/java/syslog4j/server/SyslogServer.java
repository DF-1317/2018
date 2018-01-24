package org.productivity.java.syslog4j.server;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.productivity.java.syslog4j.Syslog4jVersion;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfig;
import org.productivity.java.syslog4j.server.impl.net.udp.UDPNetSyslogServerConfig;

/**
 * This class provides a Singleton-based interface for Syslog4j
 * server implementations.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: SyslogServer.java,v 1.11 2009/04/17 02:37:04 cvs Exp $
 */
public class SyslogServer implements SyslogConstants {
	private static final long serialVersionUID = -2260889360828258602L;

	protected static final Map instances = new Hashtable();
	
	static {
		initialize();
	}
	
	private SyslogServer() {
		//
	}

	/**
	 * @return Returns the current version identifier for Syslog4j.
	 */
	public static final String getVersion() {
		return Syslog4jVersion.VERSION;
	}
	
	public static final SyslogServerIF getInstance(String protocol) throws SyslogRuntimeException {
		String syslogProtocol = protocol.toLowerCase();
		
		if (instances.containsKey(syslogProtocol)) {
			return (SyslogServerIF) instances.get(syslogProtocol);
			
		} else {
			throw new SyslogRuntimeException("SyslogServer instance \"" + syslogProtocol + "\" not defined; use \"tcp\" or \"udp\" or call SyslogServer.createInstance(protocol,config) first");
		}
	}
	
	public static final SyslogServerIF getThreadedInstance(String protocol) throws SyslogRuntimeException {
		SyslogServerIF server = getInstance(protocol);

		if (server.getThread() == null) {
			Thread thread = new Thread(server);
			thread.setName("SyslogServer: " + protocol);
			
			server.setThread(thread);
			thread.start();
		}
		
		return server;
	}
	
	public static final boolean exists(String protocol) {
		if (protocol == null || "".equals(protocol.trim())) {
			return false;
		}
		
		return instances.containsKey(protocol.toLowerCase());
	}
	
	public static final SyslogServerIF createInstance(String protocol, SyslogServerConfigIF config) throws SyslogRuntimeException {
		if (protocol == null || "".equals(protocol.trim())) {
			throw new SyslogRuntimeException("Instance protocol cannot be null or empty");
		}
		
		if (config == null) {
			throw new SyslogRuntimeException("SyslogServerConfig cannot be null");
		}
		
		String syslogProtocol = protocol.toLowerCase();
		
		SyslogServerIF syslogServer = null;
		
		synchronized(instances) {
			if (instances.containsKey(syslogProtocol)) {
				throw new SyslogRuntimeException("SyslogServer instance \"" + syslogProtocol + "\" already defined.");
			}
			
			try {
				Class syslogClass = config.getSyslogServerClass();
				
				syslogServer = (SyslogServerIF) syslogClass.newInstance();
				
			} catch (ClassCastException cse) {
				throw new SyslogRuntimeException(cse);
				
			} catch (IllegalAccessException iae) {
				throw new SyslogRuntimeException(iae);
				
			} catch (InstantiationException ie) {
				throw new SyslogRuntimeException(ie);
			}
	
			syslogServer.initialize(syslogProtocol,config);
			
			instances.put(syslogProtocol,syslogServer);
		}

		return syslogServer;
	}

	public static final SyslogServerIF createThreadedInstance(String protocol, SyslogServerConfigIF config) throws SyslogRuntimeException {
		createInstance(protocol,config);
		
		SyslogServerIF server = getThreadedInstance(protocol); 
		
		return server;
	}
	
	public synchronized static void initialize() {
		createInstance(UDP,new UDPNetSyslogServerConfig());
		createInstance(TCP,new TCPNetSyslogServerConfig());
	}
	
	public synchronized static final void shutdown() throws SyslogRuntimeException {
		Set protocols = instances.keySet();
		
		Iterator i = protocols.iterator();
		
		while(i.hasNext()) {
			String protocol = (String) i.next();
			
			SyslogServerIF syslogServer = (SyslogServerIF) instances.get(protocol);

			syslogServer.shutdown();
		}

		instances.clear();
	}
	
	public static void main(String[] args) throws Exception {
		SyslogServerMain.main(args);
	}
}
