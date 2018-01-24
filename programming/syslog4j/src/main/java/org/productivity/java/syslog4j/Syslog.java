package org.productivity.java.syslog4j;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.productivity.java.syslog4j.impl.net.tcp.TCPNetSyslogConfig;
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig;
import org.productivity.java.syslog4j.impl.unix.UnixSyslogConfig;
import org.productivity.java.syslog4j.impl.unix.socket.UnixSocketSyslogConfig;
import org.productivity.java.syslog4j.util.OSDetectUtility;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * This class provides a Singleton interface for Syslog4j client implementations.
 * 
 * <p>Usage examples:</p>
 * 
 * <b>Direct</b>
 * <pre>
 * Syslog.getInstance("udp").info("log message");
 * </pre>
 * 
 * <b>Via Instance</b>
 * <pre>
 * SyslogIF syslog = Syslog.getInstance("udp");
 * syslog.info();
 * </pre>
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: Syslog.java,v 1.19 2010/02/04 03:52:06 cvs Exp $
 */
public final class Syslog implements SyslogConstants {
	private static final long serialVersionUID = -4662318148650646144L;
	
	protected static final Map instances = new Hashtable();
	
	static {
		initialize();
	}
	
	/**
	 * Syslog is a singleton.
	 */
	private Syslog() {
		//
	}
	
	/**
	 * @return Returns the current version identifier for Syslog4j.
	 */
	public static final String getVersion() {
		return Syslog4jVersion.VERSION;
	}
	
	/**
	 * Use getInstance(protocol) as the starting point for Syslog4j.
	 * 
	 * @param protocol - the Syslog protocol to use, e.g. "udp", "tcp", "unix_syslog", "unix_socket", or a custom protocol
	 * @return Returns an instance of SyslogIF.
	 * @throws SyslogRuntimeException
	 */
	public static final SyslogIF getInstance(String protocol) throws SyslogRuntimeException {
		String _protocol = protocol.toLowerCase();
		
		if (instances.containsKey(_protocol)) {
			return (SyslogIF) instances.get(_protocol);
			
		} else {
			StringBuffer message = new StringBuffer("Syslog protocol \"" + protocol + "\" not defined; call Syslogger.createSyslogInstance(protocol,config) first");
			
			if (instances.size() > 0) {
				message.append(" or use one of the following instances: ");
				
				Iterator i = instances.keySet().iterator();
				while (i.hasNext()) {
					String k = (String) i.next();
				
					message.append(k);
					if (i.hasNext()) {
						message.append(' ');
					}
				}
			}
			
			throw new SyslogRuntimeException(message.toString());
		}
	}
	
	/**
	 * Use createInstance(protocol,config) to create your own Syslog instance.
	 * 
	 * <p>First, create an implementation of SyslogConfigIF, such as UdpNetSyslogConfig.</p>
	 * 
	 * <p>Second, configure that configuration instance.</p>
	 * 
	 * <p>Third, call createInstance(protocol,config) using a short &amp; simple
	 * String for the protocol argument.</p>
	 * 
	 * <p>Fourth, either use the returned instance of SyslogIF, or in later code
	 * call getInstance(protocol) with the protocol chosen in the previous step.</p> 
	 * 
	 * @param protocol 
	 * @param config
	 * @return Returns an instance of SyslogIF.
	 * @throws SyslogRuntimeException
	 */
	public static final SyslogIF createInstance(String protocol, SyslogConfigIF config) throws SyslogRuntimeException {
		if (protocol == null || "".equals(protocol.trim())) {
			throw new SyslogRuntimeException("Instance protocol cannot be null or empty");
		}
		
		if (config == null) {
			throw new SyslogRuntimeException("SyslogConfig cannot be null");
		}
		
		String syslogProtocol = protocol.toLowerCase();
		
		SyslogIF syslog = null;
		
		synchronized(instances) {
			if (instances.containsKey(syslogProtocol)) {
				throw new SyslogRuntimeException("Syslog protocol \"" + protocol + "\" already defined");
			}
			
			try {
				Class syslogClass = config.getSyslogClass();
				
				syslog = (SyslogIF) syslogClass.newInstance();
				
			} catch (ClassCastException cse) {
				if (!config.isThrowExceptionOnInitialize()) {
					throw new SyslogRuntimeException(cse);
					
				} else {
					return null;
				}
				
			} catch (IllegalAccessException iae) {
				if (!config.isThrowExceptionOnInitialize()) {
					throw new SyslogRuntimeException(iae);
					
				} else {
					return null;
				}
				
			} catch (InstantiationException ie) {
				if (!config.isThrowExceptionOnInitialize()) {
					throw new SyslogRuntimeException(ie);
					
				} else {
					return null;
				}
			}
	
			syslog.initialize(syslogProtocol,config);
			
			instances.put(syslogProtocol,syslog);
		}

		return syslog;
	}

	/**
	 * initialize() sets up the default TCP and UDP Syslog protocols, as
	 * well as UNIX_SYSLOG and UNIX_SOCKET (if running on a Unix-based system).
	 */
	public synchronized static final void initialize() {
		createInstance(UDP,new UDPNetSyslogConfig());
		createInstance(TCP,new TCPNetSyslogConfig());
		
		if (OSDetectUtility.isUnix() && SyslogUtility.isClassExists(JNA_NATIVE_CLASS)) {
			createInstance(UNIX_SYSLOG,new UnixSyslogConfig());
			createInstance(UNIX_SOCKET,new UnixSocketSyslogConfig());
		}
	}
	
	/**
	 * @param protocol - Syslog protocol
	 * @return Returns whether the protocol has been previously defined.
	 */
	public static final boolean exists(String protocol) {
		if (protocol == null || "".equals(protocol.trim())) {
			return false;
		}
		
		return instances.containsKey(protocol.toLowerCase());
	}
	
	/**
	 * shutdown() gracefully shuts down all defined Syslog protocols,
	 * which includes flushing all queues and connections and finally
	 * clearing all instances (including those initialized by default).
	 * 
	 * @throws SyslogRuntimeException
	 */
	public synchronized static final void shutdown() throws SyslogRuntimeException {
		Set protocols = instances.keySet();
		
		Iterator i = protocols.iterator();
		
		try {
			Thread.sleep(SyslogConstants.THREAD_LOOP_INTERVAL_DEFAULT);
			
		} catch (InterruptedException ie) {
			//
		}
		
		while(i.hasNext()) {
			String protocol = (String) i.next();
			
			SyslogIF syslog = (SyslogIF) instances.get(protocol);

			syslog.shutdown();
		}

		instances.clear();
	}
	
	public static void main(String[] args) throws Exception {
		SyslogMain.main(args);
	}
}
