package org.productivity.java.syslog4j.util;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.Date;

/**
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: RFC3339TimestampUtility.java,v 1.2 2010/02/04 03:52:06 cvs Exp $
*/

public final class RFC3339TimestampUtility {

	public static Date parse(final String _datestring) throws ParseException, IndexOutOfBoundsException {
        DateTime dateTime = new DateTime(_datestring);
        return dateTime.toDate();
    }
}
