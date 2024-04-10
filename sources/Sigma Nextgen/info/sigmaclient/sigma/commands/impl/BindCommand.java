package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.commands.Command;
import info.sigmaclient.sigma.utils.ChatUtils;
import net.minecraft.client.util.InputMappings;

public class BindCommand extends Command {
    @Override
    public void onChat(String[] args, String joinArgs) {
        if(args.length != 2){
            sendUsages();
            return;
        }
        Module module;
        module = SigmaNG.getSigmaNG().moduleManager.getModuleByName(args[0]);
        if(module == null){
            ChatUtils.sendMessageWithPrefix("Not found module: " + args[0] + ".");
            return;
        }
        try {
            module.key = InputMappings.getInputByName("key.keyboard."+args[1].toLowerCase()).getKeyCode();
        }catch (Exception e){
            module.key = -1;
        }
        String str = module.key != -1 ? InputMappings.getInputByCode(module.key, 0).getTranslationKey() : "none";
        ChatUtils.sendMessageWithPrefix("Set key of module " + args[0] + " to " + str + ".");
    }

    @Override
    public String usages() {
        return "[module] [key]";
    }

    @Override
    public String describe() {
        return "Bind key of a module.";
    }

    @Override
    public String[] getName() {
        return new String[]{"bind"};
    }
}
