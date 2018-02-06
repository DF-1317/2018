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
 * This server program needs Java 1.8 or above.
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
 * You will need to adjust the ServerPort value below, depending on your environment. We do recommend
 * using a static IP address on the robot network so that the servers are at known locations; DHCP has
 * no guarantee of assigning the same IP to a system in successive runs.
 *
 * This example log server can take in all the log messages sent to it and display them on your
 * screen while they are also being appended to a logging file: robo.log
 * We do assume the messages coming into us are formatted according to RFC-5424.
 */
public class Server {
    //static final int            ServerPort      = SyslogConstants.SYSLOG_PORT_DEFAULT;
    static final int            ServerPort      = 10000;  // use a non-default port that should not have security issues

    /*
     * Where execution starts. We tell the user we have started.
     * We call the shutdown() first to clear out any older thread that may still be hanging out.
     * They we setup our new instance of a UDP type server. We direct it to listen on all the
     * server ports [configured] on all interfaces on this machine.
     */
    public static void main(String[] args) {
        System.out.println("Starting logger server...");
        System.setProperty("jsse.enableSNIExtension", "false");

        SyslogServer.shutdown();

        SyslogServerConfigIF conf = new UDPSyslogServerConfig();
        conf.setUseStructuredData(true);
        conf.setHost("0.0.0.0");
        conf.setPort(ServerPort);

        // The working part of the log runs in its own thread...not that we need it here, but it is a
        // nice feature.
        SyslogServer.createThreadedInstance("udp", conf);

        System.out.println("Logger running...use ^C to terminate");
    }

}
