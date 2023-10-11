package com.initial.commands.impl;

import com.initial.commands.*;
import net.minecraft.client.*;
import com.initial.*;
import net.minecraft.util.*;

public class VClip extends Command
{
    protected Minecraft mc;
    
    public VClip() {
        super("VClip", "VClip", "vclip <distance>", new String[] { "v" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length == 2) {
            Astomero.addChatMessage(".vclip <value>");
            return;
        }
        if (args.length == 1) {
            this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX, this.mc.thePlayer.posY + Double.parseDouble(args[0]), this.mc.thePlayer.posZ);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§8> §aTeleported " + args[0] + ".0 blocks"));
        }
    }
}
