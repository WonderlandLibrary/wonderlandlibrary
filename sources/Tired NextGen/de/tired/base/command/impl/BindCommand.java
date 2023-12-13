package de.tired.base.command.impl;

import de.tired.base.annotations.CommandAnnotation;
import de.tired.base.command.Command;

import de.tired.base.module.Module;
import de.tired.Tired;
import de.tired.util.file.FileUtil;
import de.tired.util.log.IngameChatLog;
import org.lwjgl.input.Keyboard;

@CommandAnnotation(name = "Bind", help = "", alias = {"b", "bi"})
public class BindCommand extends Command {

    @Override
    public void doCommand(String[] args) {
        args[1] = args[1].toUpperCase();
        final int key = Keyboard.getKeyIndex(args[1]);
        for (Module m : Tired.INSTANCE.moduleManager.getModuleList()) {
            if (args[0].equalsIgnoreCase(m.getName())) {
                m.setKey(key);
                FileUtil.instance.save();
                IngameChatLog.INGAME_CHAT_LOG.doLog("§e" + Keyboard.getKeyName(key) + " §7has been bound to §e" + m.getName());
                return;
            } else if (args[0].equalsIgnoreCase("reset")) {
                if (args[1].equalsIgnoreCase(m.getName())) {
                    m.setKey(0);
                    IngameChatLog.INGAME_CHAT_LOG.doLog("§e" + m.getName() + " §7has been " + "§eunbound");
                    return;
                }
            }
        }
        IngameChatLog.INGAME_CHAT_LOG.doLog("§cInvalid Module!");
        super.doCommand(args);
    }
}
