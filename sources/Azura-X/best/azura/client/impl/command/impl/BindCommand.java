package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import best.azura.client.api.module.Module;
import best.azura.client.impl.Client;
import org.lwjgl.input.Keyboard;

public class BindCommand extends ACommand {
    @Override
    public String getName() {
        return "bind";
    }

    @Override
    public String getDescription() {
        return "Bind a module.";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"b"};
    }

    @Override
    public void handleCommand(String[] args) {
        if(args.length <= 1) {
            msg(Client.PREFIX + "§7Please use: §9.bind <module> <key>§7!");
        } else {

            Module module = Client.INSTANCE.getModuleManager().getModuleByName(args[0]);
            if(module == null) {
                msg(Client.PREFIX + "§7This §9module §7doesnt exist.");
                return;
            }

            module.setKeyBind(Keyboard.getKeyIndex(args[1].toUpperCase()));
            msg(Client.PREFIX + "§7Bound §9" + module.getName() + " §7to §9" + Keyboard.getKeyName(module.getKeyBind()) + "§7!");

        }

    }
}
