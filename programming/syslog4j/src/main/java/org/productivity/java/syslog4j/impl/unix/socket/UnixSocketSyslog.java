package org.productivity.java.syslog4j.impl.unix.socket;

import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.AbstractSyslog;
import org.productivity.java.syslog4j.impl.AbstractSyslogWriter;
import org.productivity.java.syslog4j.util.OSDetectUtility;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

/**
* UnixSocketSyslog is an extension of AbstractSyslog that provides support for
* Unix socket-based syslog clients.
* 
* <p>This class requires the JNA (Java Native Access) library to directly
* access the native C libraries utilized on Unix platforms.</p>
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: UnixSocketSyslog.java,v 1.11 2010/02/27 17:22:33 cvs Exp $
*/
public class UnixSocketSyslog extends AbstractSyslog {
	private static final long serialVersionUID = 39878807911936785L;
	
	protected static class SockAddr extends Structure {
		public final static int SUN_PATH_SIZE = 108;
		public final static byte[] ZERO_BYTE = new byte[] { 0 };
		
		public short sun_family = 1;
		public byte[] sun_path = new byte[SUN_PATH_SIZE];
		
		public void setSunPath(String sunPath) {
			System.arraycopy(sunPath.getBytes(), 0,this.sun_path, 0, sunPath.length());
			System.arraycopy(ZERO_BYTE,0,this.sun_path,sunPath.length(),1);
		}
	}
	
    protected interface CLibrary extends Library {
        public int socket(int domain, int type, int protocol);
        public int connect(int sockfd, SockAddr sockaddr, int addrlen);
        public int write(int fd, byte[] buf, int count);
        public int shutdown(int s, int how);
        public String strerror(int errno);
    }
    
	protected boolean libraryLoaded = false;
    protected CLibrary libraryInstance = null;
	
	protected UnixSocketSyslogConfig unixSocketSyslogConfig = null; 
	protected int fd = -1;
	
	protected synchronized void loadLibrary() {
		if (!OSDetectUtility.isUnix()) {
			throw new SyslogRuntimeException("UnixSyslog not supported on non-Unix platforms");
		}
		
		if (!this.libraryLoaded) {
			this.libraryInstance = (CLibrary) Native.loadLibrary(this.unixSocketSyslogConfig.getLibrary(),CLibrary.class);
			this.libraryLoaded = true;
		}
	}

	public void initialize() throws SyslogRuntimeException {
		try {
			this.unixSocketSyslogConfig = (UnixSocketSyslogConfig) this.syslogConfig;
			
		} catch (ClassCastException cce) {
			throw new SyslogRuntimeException("config must be of type UnixSocketSyslogConfig");
		}
		
		loadLibrary();
		
	}
	
	protected synchronized void connect() {
		if (this.fd != -1) {
			return;
		}
		
		this.fd = this.libraryInstance.socket(this.unixSocketSyslogConfig.getFamily(),this.unixSocketSyslogConfig.getType(),this.unixSocketSyslogConfig.getProtocol());
		
		if (this.fd == -1) {
			this.fd = -1;
			return;
		}
		
		SockAddr sockAddr = new SockAddr();
		
		sockAddr.sun_family = this.unixSocketSyslogConfig.getFamily();
		sockAddr.setSunPath(this.unixSocketSyslogConfig.getPath());
		
		int c = this.libraryInstance.connect(this.fd, sockAddr, sockAddr.size());

		if (c == -1) {
			this.fd = -1;
			return;
		}		
	}

	protected void write(byte[] message) throws SyslogRuntimeException {
		if (this.fd == -1) {
			connect();
		}
		
		if (this.fd == -1) {
			return;
		}
		
		this.libraryInstance.write(this.fd,message,message.length);
	}

	public void flush() throws SyslogRuntimeException {
		shutdown();
		
		this.fd = this.libraryInstance.socket(this.unixSocketSyslogConfig.getFamily(),this.unixSocketSyslogConfig.getType(),this.unixSocketSyslogConfig.getProtocol());
	}
	
	public void shutdown() throws SyslogRuntimeException {
		if (this.fd == -1) {
			return;
		}
		
		this.libraryInstance.shutdown(this.fd,SHUT_WR);
		
		this.fd = -1;
	}
	
	public AbstractSyslogWriter getWriter() {
		return null;
	}

	public void returnWriter(AbstractSyslogWriter syslogWriter) {
		//
	}
}
