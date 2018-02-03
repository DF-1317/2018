import com.cloudbees.syslog.sender.UdpSyslogMessageSender;
import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.SyslogMessage;
import com.cloudbees.syslog.MessageFormat;

/*
 * This code demonstrates using the syslog4j logging capability.
 * Above are the imports needed to reference classes and handy constants.
 * At the top of this class we have a couple handy constants of our own and a data member
 * that will keep a reference to the Syslog instance doing all the work for us.
 *
 * This demo program needs Java 1.8 or above.
 *
 * What this logging framework allows for is the ability to have one to many systems and
 * programs, all send their logging data to a single central server to be collected into a
 * file. They will be formatted for us, have a timestamp, and an indicator as to what system
 * they came from.
 *
 * For a robotics team, this can be helpful to get data off of the RoboRio [the robot control software]
 * and also any other computers on the robot's network. For example if a Rasberry Pi were doing
 * video processing of a camera input, that too could log to the same place the Rio was.
 *
 * You will need to adjust the ServerHost value below, depending on your environment. We do recommend
 * using a static IP address on the robot network so that the servers are at known locations; DHCP has
 * no guarantee of assigning the same IP to a system in successive runs.
 *
 * If your log server is a Linux box, like Ubuntu, you should configure the rsyslog facility to put
 * local0 messages into their own file. Add the following to the /etc/rsyslog.d/50-default.conf
 *  local0.*               /var/log/local0.log
 *
 * You will also want to relax the read permissions on files it creates and allow repeat messages.
 * Check the following in /etc/rsyslog.conf
 *  $FileCreateMode        0644
 *  $DirCreateMode         0755
 *  $RepeatedMsgReduction  off
 */
public class Logger {
    static final String         ServerHost      = "10.40.0.164";                 // address of the central log server
    static final Facility            Fac             = Facility.LOCAL0;              // what facility is labeled on the msg
    static final Severity            Sev             = Severity.INFORMATIONAL;
    UdpSyslogMessageSender      sl;

    /*
     * We get a handle to the syslog and declare that we are using UDP messaging; TCP is an option too,
     * but would increase bandwidth use on the network. Given the topology of the robot's network, UDP
     * is just fine.
     */
    public Logger() {
        sl = new UdpSyslogMessageSender();
        sl.setSyslogServerHostname(ServerHost);
        sl.setSyslogServerPort(10000);  // optional
        sl.setMessageFormat(MessageFormat.RFC_5424);

        sl.setDefaultMessageHostname("jpl-PC");
        sl.setDefaultFacility(Fac);
        sl.setDefaultSeverity(Sev);
    }

    /*
     * Simply send the message and flush it out. The flush is nice to help keep the messages as real-time as
     * possible; yeah, it is less efficient.
     * We always log 'info' messages, there are other choices like: debug, error, warn, crit...
     */
    public void  log(String msg) {
        System.out.println("Sending: " + msg);
        try {
            SyslogMessage m = new SyslogMessage()
                .withFacility(Fac)
                .withSeverity(Sev)
                .withHostname(ServerHost)
                .withAppName("test-client")
                .withProcId("Logger")
                .withMsg(msg);
            sl.sendMessage(m);
        } catch (Exception e) {
            System.err.println("Ouch: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Where execution starts. We tell the user we have started, then check to see if they gave
     * us a message to send out.
     * We create an instance of the logger class and log the message; they we are done.
     */
    public static void main(String[] args) {
        System.out.println("Starting logger test...");

        String msg = (0 < args.length) ? String.join(" ",args) : "Test msg";
        Logger l = new Logger();
        l.log(msg);

        System.out.println("Logger done!");
    }

}
