package org.productivity.java.syslog4j.impl.message.modifier.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.util.Base64;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
* HashSyslogMessageModifier is an implementation of SyslogMessageModifierIF
* that provides support for Java Cryptographic hashes (MD5, SHA1, SHA256, etc.).
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: HashSyslogMessageModifier.java,v 1.3 2008/11/21 15:29:15 cvs Exp $
*/
public class HashSyslogMessageModifier implements SyslogMessageModifierIF {
	private static final long serialVersionUID = 7335757344826206953L;
	
	protected HashSyslogMessageModifierConfig config = null;
	
	public static final HashSyslogMessageModifier createMD5() {
		HashSyslogMessageModifier md5 = new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createMD5());
		
		return md5;
	}
	
	public static final HashSyslogMessageModifier createSHA1() {
		HashSyslogMessageModifier sha1 = new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA1());
		
		return sha1;
	}
	
	public static final HashSyslogMessageModifier createSHA160() {
		 return createSHA1();
	}
	
	public static final HashSyslogMessageModifier createSHA256() {
		HashSyslogMessageModifier sha256 = new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA256());
		
		return sha256;
	}
	
	public static final HashSyslogMessageModifier createSHA384() {
		HashSyslogMessageModifier sha384 = new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA384());
		
		return sha384;
	}
	
	public static final HashSyslogMessageModifier createSHA512() {
		HashSyslogMessageModifier sha512 = new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA512());
		
		return sha512;
	}
	
	public HashSyslogMessageModifier(HashSyslogMessageModifierConfig config) throws SyslogRuntimeException {
		this.config = config;
		
		if (this.config == null) {
			throw new SyslogRuntimeException("Hash config object cannot be null");			
		}

		if (this.config.getHashAlgorithm() == null) {
			throw new SyslogRuntimeException("Hash algorithm cannot be null");			
		}
		
		try {
			MessageDigest.getInstance(config.getHashAlgorithm());
			
		} catch (NoSuchAlgorithmException nsae){
			throw new SyslogRuntimeException(nsae);			
		}
	}
	
	protected MessageDigest obtainMessageDigest() {
		MessageDigest digest = null;
		
		try {
			digest = MessageDigest.getInstance(this.config.getHashAlgorithm());
			
		} catch (NoSuchAlgorithmException nsae) {
			throw new SyslogRuntimeException(nsae);
		}
		
		return digest;
	}

	public HashSyslogMessageModifierConfig getConfig() {
		return this.config;
	}

	public String modify(SyslogIF syslog, SyslogConfigIF syslogConfig, int facility, int level, String message) {
		byte[] messageBytes = SyslogUtility.getBytes(syslogConfig,message);
		
		MessageDigest digest = obtainMessageDigest();
		byte[] digestBytes = digest.digest(messageBytes);

		String digestString = Base64.encodeBytes(digestBytes,Base64.DONT_BREAK_LINES);

		StringBuffer buffer = new StringBuffer(message);
		
		buffer.append(this.config.getPrefix());
		buffer.append(digestString);
		buffer.append(this.config.getSuffix());
		
		return buffer.toString();
	}
}
