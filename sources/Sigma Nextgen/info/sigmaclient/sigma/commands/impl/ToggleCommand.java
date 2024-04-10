package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.commands.Command;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.Module;

public class ToggleCommand extends Command {
    @Override
    public void onChat(String[] args, String joinArgs) {
        if(args.length != 1){
            sendUsages();
            return;
        }
        Module module;
        module = SigmaNG.getSigmaNG().moduleManager.getModuleByName(args[0]);
        if(module == null){
            ChatUtils.sendMessageWithPrefix("Not found module: " + args[0] + ".");
            return;
        }
        module.enabled = !module.enabled;
        ChatUtils.sendMessageWithPrefix("Toggle module " + args[0] + " to " + (module.enabled ? "enable" : "disable"));
    }

    @Override
    public String usages() {
        return "[module name]";
    }

    @Override
    public String describe() {
        return "enable/disable module.";
    }

    @Override
    public String[] getName() {
        return new String[]{"toggle"};
    }
}
