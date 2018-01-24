package org.productivity.java.syslog4j.impl.message.processor.structured;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.productivity.java.syslog4j.impl.message.processor.AbstractSyslogMessageProcessor;
import org.productivity.java.syslog4j.impl.message.structured.StructuredSyslogMessage;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * SyslogStructuredMessageProcessor extends SyslogMessageProcessor's ability to
 * split a syslog message into multiple messages when the message is greater
 * than the syslog maximum message length (1024 bytes including the header). It
 * adds support for structured syslog messages as specified by
 * draft-ietf-syslog-protocol-23. More information here:
 * 
 * <p>http://tools.ietf.org/html/draft-ietf-syslog-protocol-23</p>
 * 
 * <p>Those wishing to replace (or improve upon) this implementation
 * can write a custom SyslogMessageProcessorIF and set it per
 * instance via the SyslogIF.setStructuredMessageProcessor(..) method or set it globally
 * via the StructuredSyslogMessageProcessor.setDefault(..) method.</p>
 * 
 * <p>
 * Syslog4j is licensed under the Lesser GNU Public License v2.1. A copy of the
 * LGPL license is available in the META-INF folder in all distributions of
 * Syslog4j and in the base directory of the "doc" ZIP.
 * </p>
 * 
 * @author Manish Motwani
 * @version $Id: StructuredSyslogMessageProcessor.java,v 1.2 2010/02/04 03:41:38 cvs Exp $
 */
public class StructuredSyslogMessageProcessor extends AbstractSyslogMessageProcessor {
	private static final long serialVersionUID = -1563777226913475257L;
	
	public static String VERSION = "1";

	private static final StructuredSyslogMessageProcessor INSTANCE = new StructuredSyslogMessageProcessor();
	protected static StructuredSyslogMessageProcessor defaultInstance = INSTANCE;
	
	private String applicationName = STRUCTURED_DATA_APP_NAME_DEFAULT_VALUE;
	private String processId = STRUCTURED_DATA_PROCESS_ID_DEFAULT_VALUE;

	public static void setDefault(StructuredSyslogMessageProcessor messageProcessor) {
		if (messageProcessor != null) {
			defaultInstance = messageProcessor;
		}
	}
	
	public static StructuredSyslogMessageProcessor getDefault() {
		return defaultInstance;
	}

	public StructuredSyslogMessageProcessor() {
		super();
	}

	public StructuredSyslogMessageProcessor(final String applicationName) {
		super();
		this.applicationName = applicationName;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String createSyslogHeader(final int facility, final int level, final boolean sendLocalTimestamp, final boolean sendLocalName) {
		final StringBuffer buffer = new StringBuffer();

		final int priority = (facility | level);

		buffer.append("<");
		buffer.append(priority);
		buffer.append(">");
		buffer.append(VERSION);
		buffer.append(' ');

		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				STRUCTURED_DATA_MESSAGE_DATEFORMAT, Locale.ENGLISH);

		// ISO standard requires a colon in the timezone
		final String datePrefix = dateFormat.format(new Date());
		buffer.append(datePrefix.substring(0, 22));
		buffer.append(':');
		buffer.append(datePrefix.substring(22));
		buffer.append(' ');

		final String localName = SyslogUtility.getLocalName();

		buffer.append(localName);
		buffer.append(' ');

		buffer.append(StructuredSyslogMessage.nilProtect(this.applicationName))
				.append(' ');

		buffer.append(StructuredSyslogMessage.nilProtect(this.processId)).append(' ');
		
		return buffer.toString();
	}
}
