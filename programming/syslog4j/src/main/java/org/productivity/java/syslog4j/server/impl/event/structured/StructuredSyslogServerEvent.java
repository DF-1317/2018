package org.productivity.java.syslog4j.server.impl.event.structured;

import java.net.InetAddress;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.impl.message.structured.StructuredSyslogMessage;
import org.productivity.java.syslog4j.server.impl.event.SyslogServerEvent;
import org.productivity.java.syslog4j.util.RFC3339TimestampUtility;

/**
 * SyslogServerStructuredEvent provides an implementation of the
 * SyslogServerEventIF interface that supports receiving of structured syslog
 * messages, as defined in:
 * 
 * <p>
 * http://tools.ietf.org/html/draft-ietf-syslog-protocol-23#section-6
 * </p>
 * 
 * <p>
 * Syslog4j is licensed under the Lesser GNU Public License v2.1. A copy of the
 * LGPL license is available in the META-INF folder in all distributions of
 * Syslog4j and in the base directory of the "doc" ZIP.
 * </p>
 * 
 * @author Manish Motwani
 * @version $Id: StructuredSyslogServerEvent.java,v 1.4 2010/02/11 05:02:20 cvs Exp $
 */
public class StructuredSyslogServerEvent extends SyslogServerEvent {
	private static final long serialVersionUID = 1676499796406044315L;

	protected String applicationName = SyslogConstants.STRUCTURED_DATA_APP_NAME_DEFAULT_VALUE;
	protected String processId = null;
	
	public StructuredSyslogServerEvent(byte[] message, int length, InetAddress inetAddress) {
		super();
		
		initialize(message,length,inetAddress);
		parse();
	}

	protected void parseApplicationName() {
		int i = this.message.indexOf(' ');

		if (i > -1) {
			this.applicationName = this.message.substring(0, i).trim();
			this.message = this.message.substring(i + 1);
			parseProcessId();
		}

		if (SyslogConstants.STRUCTURED_DATA_NILVALUE.equals(this.applicationName)) {
			this.applicationName = null;
		}
	}

	protected void parseProcessId() {
		int i = this.message.indexOf(' ');

		if (i > -1) {
			this.processId = this.message.substring(0, i).trim();
		}

        if (SyslogConstants.STRUCTURED_DATA_NILVALUE.equals(this.processId)) {
            this.processId = null;
        }

        this.message = this.message.substring(i + 1);
    }

	protected void parseDate() {
		// skip VERSION field
		int i = this.message.indexOf(' ');
		this.message = this.message.substring(i + 1);

		// parse the date
		i = this.message.indexOf(' ');

		if (i > -1) {
			String dateString = this.message.substring(0, i).trim();
			
			try {
				this.date = RFC3339TimestampUtility.parse(dateString);
				this.message = this.message.substring(dateString.length() + 1);

			} catch (Exception e) {
				// Not structured date format, try super one
				super.parseDate();
			} 
		}
	}

	protected void parseHost() {
		int i = this.message.indexOf(' ');

		if (i > -1) {
			this.host = this.message.substring(0, i).trim();
		}

        if (SyslogConstants.STRUCTURED_DATA_NILVALUE.equals(this.host)) {
            this.host = null;
        }

        this.message = this.message.substring(i + 1);
        parseApplicationName();
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getProcessId() {
		return processId;
	}

	public StructuredSyslogMessage getStructuredMessage() {
		try {
			return StructuredSyslogMessage.fromString(getMessage());

		} catch (IllegalArgumentException e) {
			// throw new SyslogRuntimeException(
			// "Message received is not a valid structured message: "
			// + getMessage(), e);
			return new StructuredSyslogMessage(null,null,getMessage());
		}
	}
}
