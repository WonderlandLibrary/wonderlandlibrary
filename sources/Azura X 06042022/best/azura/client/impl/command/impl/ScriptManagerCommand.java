package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.impl.Client;

public class ScriptManagerCommand extends ACommand {
    @Override
    public String getName() {
        return "scriptsmanager";
    }

    @Override
    public String getDescription() {
        return "Manage scripts";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "script", "scripts", "as" };
    }

    @Override
    public void handleCommand(String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                Client.INSTANCE.getClientConfig().save(Client.INSTANCE.getConfigManager().getClientDirectory());
                for (final Module module : Client.INSTANCE.getModuleManager().getModules(Category.SCRIPTS))
                    module.setEnabled(false);
                Client.INSTANCE.getModuleManager().removeScripts();
                Client.INSTANCE.getScriptManager().loadScripts();
                Client.INSTANCE.getModuleManager().loadScripts();
                Client.INSTANCE.loadClickGUI();
                Client.INSTANCE.getClientConfig().load(Client.INSTANCE.getConfigManager().getClientDirectory());
            }
        }
    }
}
