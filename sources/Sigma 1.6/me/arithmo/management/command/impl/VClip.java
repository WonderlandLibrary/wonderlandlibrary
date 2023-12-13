/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.util.StringConversions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class VClip
extends Command {
    public VClip(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        if (args.length == 1 && StringConversions.isNumeric(args[0])) {
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + Double.parseDouble(args[0]), this.mc.thePlayer.posZ);
            return;
        }
        this.printUsage();
    }

    @Override
    public String getUsage() {
        return "<Distance>";
    }

    public void onEvent(Event event) {
    }
}

