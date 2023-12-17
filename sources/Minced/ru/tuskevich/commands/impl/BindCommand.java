// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import java.util.Comparator;
import ru.tuskevich.util.chat.ChatUtility;
import java.util.ArrayList;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.modules.Module;
import ru.tuskevich.Minced;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "bind", description = "Allows you to bind module")
public class BindCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) {
        try {
            if (args.length >= 2) {
                if (args[0].equals("bind")) {
                    if (args[1].equals("clear")) {
                        int i = 0;
                        for (final Module feature2 : Minced.getInstance().manager.getModules()) {
                            if (feature2 != null && feature2.bind != 0) {
                                if (feature2.name.equals("ClickGUI")) {
                                    continue;
                                }
                                feature2.bind = 0;
                                ++i;
                            }
                        }
                        this.sendMessage("Module " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " unbinded");
                    }
                    if (args[1].equals("list")) {
                        final ArrayList<Module> boxes = new ArrayList<Module>();
                        for (final Module feature3 : Minced.getInstance().manager.getModules()) {
                            if (feature3.bind == 0) {
                                continue;
                            }
                            boxes.add(feature3);
                        }
                        ChatUtility.addChatMessage(boxes.isEmpty() ? "Clear" : "All Binds:");
                        boxes.sort(Comparator.comparing(m -> m.name));
                        for (final Module box : boxes) {
                            this.sendMessage(ChatFormatting.GRAY + box.name + ChatFormatting.WHITE + " [" + ChatFormatting.GRAY + Keyboard.getKeyName(box.bind) + ChatFormatting.WHITE + "]");
                        }
                    }
                    if (args[1].equals("add")) {
                        final int keyBind = Keyboard.getKeyIndex(args[3].toUpperCase());
                        final Module module = Minced.getInstance().manager.getModule(args[2]);
                        if (module != null) {
                            module.bind = keyBind;
                            this.sendMessage("Module " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " binded");
                        }
                        else {
                            this.sendMessage("Module not founded");
                        }
                    }
                    if (args[1].equals("remove")) {
                        for (final Module f : Minced.getInstance().manager.getModules()) {
                            if (f.name.equalsIgnoreCase(args[2])) {
                                f.bind = 0;
                                this.sendMessage("Module " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " removed");
                            }
                        }
                    }
                }
            }
            else {
                this.error();
            }
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".bind add " + ChatFormatting.BLUE + "<name> <key>");
        this.sendMessage(ChatFormatting.WHITE + ".bind remove " + ChatFormatting.BLUE + "<name> <key>");
        this.sendMessage(ChatFormatting.WHITE + ".bind list");
        this.sendMessage(ChatFormatting.WHITE + ".bind clear");
    }
}
