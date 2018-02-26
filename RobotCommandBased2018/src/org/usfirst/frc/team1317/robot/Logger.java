
package org.usfirst.frc.team1317.robot;

import com.cloudbees.syslog.sender.UdpSyslogMessageSender;
import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.SyslogMessage;
import com.cloudbees.syslog.MessageFormat;

public class Logger {
    // Syslog sender
    static UdpSyslogMessageSender syslog;

    static final String          ServerHost      = "10.13.17.6";                // address of the central log server
    static final int			 ServerPort		 = 5800;						// port for logging
    static final Facility        Fac             = Facility.LOCAL0;             // what facility is labeled on the msg
    static final Severity        Sev             = Severity.INFORMATIONAL;		// class of our messages
    static Boolean               needInit        = true;                        // see if we are setup

    String  app;
    String  proc;
    int     interval;
    int     count;

    /**
     * Creates a logger object to be used for logging to a server at 10.13.17.6
     * @param app - The main program using the logger. Probably just use team number.
     * @param proc - Whatever is specifically logging. Probably just use the class name.
     * @param interval - The interval between actually logging something.
     */
    public Logger(String app, String proc, int interval) {
        this.app        = app;
        this.proc       = proc;
        this.interval   = (0 < interval) ? interval : 1;
        this.count      = 0;
    } // Logger
    
    /**
     * Creates a logger object to be used for logging to a server at 10.13.17.6
     * @param app - The main program using the logger. Probably just use team number.
     * @param proc - Whatever is specifically logging. Probably just use the class name.
     */
    public Logger(String app, String proc) {
    	this(app, proc, 1);
    }

    private static void init() {
        if (! needInit) return;

        syslog = new UdpSyslogMessageSender();
        syslog.setSyslogServerHostname(ServerHost);
        syslog.setSyslogServerPort(ServerPort);
        syslog.setMessageFormat(MessageFormat.RFC_5424);
        syslog.setDefaultMessageHostname("DF1317");

        needInit = false;
    } // init

    private Boolean peg() {
        count++;
        if (count < interval) return false;
        count = 0;
        return true;
    } // peg

    public void log(String msg) {
        init();
        if (! peg()) return;

        try {
            SyslogMessage m = new SyslogMessage()
                    .withFacility(Fac)
                    .withSeverity(Sev)
                    .withHostname(ServerHost)
                    .withAppName(app)
                    .withProcId(proc)
                    .withMsg(msg);
            syslog.sendMessage(m);
        } catch (Exception e) {
            System.err.println("Ouch: " + e.getMessage());
            e.printStackTrace();
        }
    } // log

}