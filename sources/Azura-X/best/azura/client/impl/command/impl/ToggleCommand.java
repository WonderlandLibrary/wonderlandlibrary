package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.api.module.Module;

public class ToggleCommand extends ACommand {

    @Override
    public String getName() {
        return "toggle";
    }

    @Override
    public String getDescription() {
        return "Toggle modules";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"t", "enable"};
    }

    @Override
    public void handleCommand(String[] args) {
        if (args.length == 0) {
            msg(Client.PREFIX + "§7Please use: §9.toggle <module>§7!");
        } else {

            Module module = Client.INSTANCE.getModuleManager().getModule(args[0]);
            if (module == null) {
                msg(Client.PREFIX + "" + args[0] + " §7does §cnot §7exist!");
                return;
            }

            module.setEnabled(!module.isEnabled());
            String status = module.isEnabled() ? "§aon" : "§coff";
            msg(Client.PREFIX + "§9" + module.getName() + "§f is now turned " + status + "§r!");

        }
    }

}
