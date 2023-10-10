package ravenweave.client.command.commands;

import ravenweave.client.clickgui.raven.Terminal;
import ravenweave.client.command.Command;
import ravenweave.client.main.Raven;

public class CHideModule extends Command {

    public CHideModule() {
        super("hidemodule", "hides modules in hud", 3, 100, new String[] { "show/hide" },
                new String[] { "hm", "hidemodules" });
    }

    @Override
    public void onCall(String[] args) {
        switch (args[0]) {
        case "hide":
            for (int i = 1; i < args.length; i++) {
                try {
                    Raven.moduleManager.getModuleByName(args[i]).setVisibleInHud(false);
                    Terminal.print("hid " + args[i] + "!");
                } catch (NullPointerException e) {
                    Terminal.print(args[i] + " does not exist - try making it one word");
                }
            }
            break;
        case "show":
            for (int i = 1; i < args.length; i++) {
                try {
                    Raven.moduleManager.getModuleByName(args[i]).setVisibleInHud(true);
                    Terminal.print(args[i] + " is now shown!");
                } catch (NullPointerException e) {
                    Terminal.print(args[i] + " does not exist");
                }
            }
            break;
        default:
            Terminal.print("incorrect arguments");
        }

    }
}
