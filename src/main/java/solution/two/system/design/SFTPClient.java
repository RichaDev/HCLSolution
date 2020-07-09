package solution.two.system.design;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.util.Vector;

public class SFTPClient {
    private String host = "100.23.24.56:8080";
    private Session session = null;

    public SFTPClient() {

    }

    public void connect() throws JSchException {
        JSch jsch = new JSch();

        // jsch.addIdentity("private-key-path);

        session = jsch.getSession(host);

        // session = jsch.getSession("username", host, port);
        // session.setPassword("the-password");

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    public void download(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.get(source, destination);
        sftpChannel.exit();
    }

    public Vector<ChannelSftp.LsEntry> getFileList(String sourceDir, int timestampInSec) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        final Vector<ChannelSftp.LsEntry> vector = new Vector<>();

        //Using selector to fetch names of only new files from the remote server
        ChannelSftp.LsEntrySelector selector = entry -> {
            if (entry.getAttrs().getMTime() > timestampInSec) {
                vector.addElement(entry);
            }

            return ChannelSftp.LsEntrySelector.CONTINUE;
        };
        sftpChannel.ls(sourceDir, selector);
        sftpChannel.exit();
        return vector;
    }


    public void disconnect() {
        if (session != null) {
            session.disconnect();
        }
    }
}
