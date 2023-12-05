package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.cheat.commands.Command;

public class Username extends Command {

    public Username() {
        super("user", "username", "name");
    }


    @Override
    public void run(String[] args) {
        try {
            if (mc.thePlayer.getName() != null) {
                ChatUtil.chat("Your username is " + mc.thePlayer.getName());
            }
        } catch (Exception e) {
            ChatUtil.chat("Error (Invalid arguments?) (Username invalid?)");
        }
    }

}
