// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import java.util.Iterator;
import dev.xulu.newgui.Panel;
import java.util.function.Predicate;
import java.util.Objects;
import com.elementars.eclient.module.Module;
import dev.xulu.newgui.elements.ModuleButton;
import dev.xulu.newgui.NewGUI;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.dummy.DummyMod;
import com.elementars.eclient.command.Command;

public class DummyCommand extends Command
{
    public DummyCommand() {
        super("dummymod", "Makes some fake modules >:)", new String[] { "add", "del" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Try .enemy add or .enemy del");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            Command.sendChatMessage("Usage 1: .dummymod add (name)");
            Command.sendChatMessage("Usage 2: .dummymod add (name) (info)");
            Command.sendChatMessage("Info is what is displayed in brackets in the featurelist");
        }
        if (args.length < 3) {
            Command.sendChatMessage("Specify a name");
            return;
        }
        if (args.length == 3) {
            final DummyMod mod = new DummyMod(args[2]);
            if (args[1].equalsIgnoreCase("add")) {
                if (!Xulu.MODULE_MANAGER.getModules().contains(mod)) {
                    Xulu.MODULE_MANAGER.getModules().add(mod);
                    final Panel p = NewGUI.getPanelByName("Dummy");
                    if (p != null) {
                        p.Elements.add(new ModuleButton(mod, p));
                    }
                    Command.sendChatMessage("Dummy mod " + mod.getName() + " added.");
                }
                else {
                    Command.sendChatMessage(mod.getName() + " already exists!");
                }
            }
            else if (args[1].equalsIgnoreCase("del")) {
                boolean found = false;
                for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                    if (m.getName().equalsIgnoreCase(mod.getName())) {
                        Xulu.MODULE_MANAGER.getModules().remove(m);
                        final Panel p2 = NewGUI.getPanelByName("Dummy");
                        if (p2 != null) {
                            final Module module;
                            final Panel panel;
                            p2.Elements.stream().filter(button -> button instanceof ModuleButton).map(button -> button).forEach(button -> {
                                if (button.mod.getName().equalsIgnoreCase(module.getName())) {
                                    panel.Elements.remove(button);
                                }
                                return;
                            });
                        }
                        Command.sendChatMessage("Dummy mod " + mod.getName() + " removed.");
                        found = true;
                    }
                }
                if (!found) {
                    Command.sendChatMessage(mod.getName() + " isn't a mod!");
                }
            }
            else {
                Command.sendChatMessage("Unknown attribute '" + args[1] + "'");
            }
        }
        if (args.length == 4) {
            final DummyMod mod = new DummyMod(args[2], args[3]);
            if (args[1].equalsIgnoreCase("add")) {
                if (!Xulu.MODULE_MANAGER.getModules().contains(mod)) {
                    Xulu.MODULE_MANAGER.getModules().add(mod);
                    final Panel p = NewGUI.getPanelByName("Dummy");
                    if (p != null) {
                        p.Elements.add(new ModuleButton(mod, p));
                    }
                    Command.sendChatMessage("Dummy mod " + mod.getName() + " added.");
                }
                else {
                    Command.sendChatMessage(mod.getName() + " already exists!");
                }
            }
            else if (args[1].equalsIgnoreCase("del")) {
                boolean found = false;
                for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                    if (m.getName().equalsIgnoreCase(mod.getName())) {
                        Xulu.MODULE_MANAGER.getModules().remove(m);
                        final Panel p2 = NewGUI.getPanelByName("Dummy");
                        if (p2 != null) {
                            final Module module2;
                            final Panel panel2;
                            p2.Elements.stream().filter(Objects::nonNull).forEach(button -> {
                                if (button.mod.getName().equalsIgnoreCase(module2.getName())) {
                                    panel2.Elements.remove(button);
                                }
                                return;
                            });
                        }
                        Command.sendChatMessage("Dummy mod " + mod.getName() + " removed.");
                        found = true;
                    }
                }
                if (!found) {
                    Command.sendChatMessage(mod.getName() + " isn't a mod!");
                }
            }
            else {
                Command.sendChatMessage("Unknown attribute '" + args[1] + "'");
            }
        }
    }
}
