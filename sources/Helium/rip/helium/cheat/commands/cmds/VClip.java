package rip.helium.cheat.commands.cmds;

import rip.helium.notification.mgmt.NotificationManager;

public class VClip extends rip.helium.cheat.commands.Command {

    public VClip() {
        super("vclip");
    }

    @Override
    public void run(String[] args) {
        try {
            mc.thePlayer.noClip = true;
            double x = Double.parseDouble(args[1]);
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + x, mc.thePlayer.posZ);
            NotificationManager.postInfo("Teleported", "You were teleported " + x + " blocks vertically!");
        } catch (Exception e) {
        }
        mc.thePlayer.noClip = false;
    }

}
