/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.wintware.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.wintware.client.Main;
import me.wintware.client.command.AbstractCommand;
import me.wintware.client.module.world.FakeHack;
import me.wintware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class FakeHackCommand
extends AbstractCommand {
    Minecraft mc = Minecraft.getMinecraft();

    public FakeHackCommand() {
        super("fakehack", "1", "fakehack <name>", "fakehack", "hack");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length == 1) {
                ChatUtils.addChatMessage("Enter a name!");
                return;
            }
            if (!Main.instance.moduleManager.getModuleByClass(FakeHack.class).getState()) {
                ChatUtils.addChatMessage("Please enable FakeHack module!");
                return;
            }
            String name = arguments[1];
            EntityPlayer player = this.mc.world.getPlayerEntityByName(name);
            if (player == null) {
                ChatUtils.addChatMessage("That player could not be found!");
                return;
            }
            if (FakeHack.isFakeHacker(player)) {
                FakeHack.removeHacker(player);
                ChatUtils.addChatMessage("Removed player " + ChatFormatting.RED + name + ChatFormatting.WHITE + " from hacker list!");
            } else {
                FakeHack.fakeHackers.add(name);
                ChatUtils.addChatMessage("Added player " + ChatFormatting.RED + name + ChatFormatting.WHITE + " as a HACKER!");
            }
        }
        catch (Exception e) {
            ChatUtils.addChatMessage("Usage: " + this.getUsage());
        }
    }
}

