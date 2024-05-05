package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.cheat.commands.Command;

public class StaffDec extends Command {

    public StaffDec() {
        super("onlinestaff", "staff", "staffdetect", "detect", "s");
    }

    @Override
    public void run(String[] args) {
        try {
            ChatUtil.chat("Starting Staff Detector Command.\nLooping thru players.");
            // FlagDetector.TabListCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
