package org.productivity.java.syslog4j.impl.message.modifier.checksum;

import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
* ChecksumSyslogMessageModifier is an implementation of SyslogMessageModifierIF
* that provides support for Java Checksum algorithms (java.util.zip.Checksum).
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: ChecksumSyslogMessageModifier.java,v 1.3 2010/02/04 03:41:38 cvs Exp $
*/
public class ChecksumSyslogMessageModifier implements SyslogMessageModifierIF {
	private static final long serialVersionUID = -3268914290497005065L;
	
	protected ChecksumSyslogMessageModifierConfig config = null;
	
	public static final ChecksumSyslogMessageModifier createCRC32() {
		ChecksumSyslogMessageModifier crc32 = new ChecksumSyslogMessageModifier(ChecksumSyslogMessageModifierConfig.createCRC32());
		
		return crc32;
	}
	public static final ChecksumSyslogMessageModifier createADLER32() {
		ChecksumSyslogMessageModifier adler32 = new ChecksumSyslogMessageModifier(ChecksumSyslogMessageModifierConfig.createADLER32());
		
		return adler32;
	}
	
	public ChecksumSyslogMessageModifier(ChecksumSyslogMessageModifierConfig config) {
		this.config = config;
		
		if (this.config == null) {
			throw new SyslogRuntimeException("Checksum config object cannot be null");
		}
		
		if (this.config.getChecksum() == null) {
			throw new SyslogRuntimeException("Checksum object cannot be null");
		}
	}
	
	public ChecksumSyslogMessageModifierConfig getConfig() {
		return this.config;
	}
	
	public synchronized String modify(SyslogIF syslog, SyslogConfigIF syslogConfig, int facility, int level, String message) {
		StringBuffer messageBuffer = new StringBuffer(message);
		
		byte[] messageBytes = SyslogUtility.getBytes(syslogConfig,message);
		
		if (!this.config.isContinuous()) {
			this.config.getChecksum().reset();
		}
		
		this.config.getChecksum().update(messageBytes,0,message.length());
		
		messageBuffer.append(this.config.getPrefix());
		messageBuffer.append(Long.toHexString(this.config.getChecksum().getValue()).toUpperCase());
		messageBuffer.append(this.config.getSuffix());
		
		return messageBuffer.toString();
	}
}
