// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import net.minecraft.util.EnumChatFormatting;
import exhibition.module.Module;
import exhibition.management.keybinding.Bindable;
import exhibition.management.keybinding.Keybind;
import org.lwjgl.input.Keyboard;
import exhibition.util.misc.ChatUtil;
import exhibition.management.keybinding.KeyMask;
import exhibition.Client;
import exhibition.management.command.Command;

public class Bind extends Command
{
    public Bind(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        Module module = null;
        if (args.length > 0) {
            module = Client.getModuleManager().get(args[0]);
        }
        if (module == null) {
            this.printUsage();
            return;
        }
        if (args.length == 1) {
            final Keybind key = module.getKeybind();
            ChatUtil.printChat("§4[§cE§4]§8 " + module.getName() + ": " + ((key.getMask() == KeyMask.None) ? "" : (key.getMask().name() + " + ")) + key.getKeyStr());
        }
        else if (args.length == 2) {
            final int keyIndex = Keyboard.getKeyIndex(args[1].toUpperCase());
            final Keybind keybind = new Keybind(module, keyIndex);
            module.setKeybind(keybind);
            final Keybind key2 = module.getKeybind();
            ChatUtil.printChat("§4[§cE§4]§8 Set " + module.getName() + " to " + ((key2.getMask() == KeyMask.None) ? "" : (key2.getMask().name() + " + ")) + key2.getKeyStr());
        }
        else if (args.length == 3) {
            final int keyIndex = Keyboard.getKeyIndex(args[1].toUpperCase());
            final KeyMask mask = KeyMask.getMask(args[2]);
            final Keybind keybind2 = new Keybind(module, keyIndex, mask);
            module.setKeybind(keybind2);
            final Keybind key3 = module.getKeybind();
            ChatUtil.printChat("§4[§cE§4]§8 Set " + module.getName() + " to " + ((key3.getMask() == KeyMask.None) ? "" : (key3.getMask().name() + " + ")) + key3.getKeyStr());
        }
        Module.saveStatus();
    }
    
    @Override
    public String getUsage() {
        return "bind <Module> " + EnumChatFormatting.ITALIC + "[optional]" + EnumChatFormatting.RESET + "<Key> " + EnumChatFormatting.ITALIC + "[optional]" + EnumChatFormatting.RESET + "<Mask>";
    }
}
