import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;

/**
 * This code demonstrates using the syslog4j logging capability.
 * Above are the imports needed to reference classes and handy constants.
 * At the top of this class we have a couple handy constants of our own and a data member
 * that will keep a reference to the Syslog instance doing all the work for us.
 *
 * This server program needs Java 1.x or above.
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
public class Server {
    //static final int            ServerPort      = SyslogConstants.SYSLOG_PORT_DEFAULT;
    static final int            ServerPort      = 10000;

    /*
     * Where execution starts. We tell the user we have started, then check to see if they gave
     * us a message to send out.
     * We create an instance of the logger class and log the message; they we are done.
     */
    public static void main(String[] args) {
        System.out.println("Starting logger server...");
        System.setProperty("jsse.enableSNIExtension", "false");

        SyslogServer.shutdown();

        SyslogServerConfigIF conf = new UDPSyslogServerConfig();
        conf.setUseStructuredData(true);
        conf.setHost("0.0.0.0");
        conf.setPort(ServerPort);

        SyslogServer.createThreadedInstance("udp", conf);

        System.out.println("Logger running...");
    }

}
