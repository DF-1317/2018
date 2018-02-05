import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.SocketException;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.impl.net.udp.UDPNetSyslogServer;

public class UDPSyslogServer extends UDPNetSyslogServer {

    @Override
    public void shutdown() {
        super.shutdown();
        thread = null;
    }

    @Override
    public void run() {
        this.shutdown = false;
        try {
            this.ds = createDatagramSocket();
        } catch (Exception e) {
            System.err.println("Failure to create datagram socket");
            e.printStackTrace();
            throw new SyslogRuntimeException(e);
        }

        // Setup our buffered file writer
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter out = null;
        try {
            fw = new FileWriter("robo.log", true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
        } catch (Exception e) {
            System.err.println("Faild to setup log file: " + e.getMessage());
        }

        // Sit in our loop, getting UDP datagrams and outputting their contents to the user
        // and the log file.
        byte[] data = new byte[SyslogConstants.SYSLOG_BUFFER_SIZE];
        while (!this.shutdown) {
            try {
                final DatagramPacket p = new DatagramPacket(data,data.length);
                this.ds.receive(p);

                final SyslogServerEventIF event = new Rfc5424SyslogEvent(data, p.getOffset(), p.getLength());
                String m = event.toString();
                System.out.println(m);
                if (null != out) out.println(m);
                bw.flush();
            } catch (SocketException se) {
                se.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
