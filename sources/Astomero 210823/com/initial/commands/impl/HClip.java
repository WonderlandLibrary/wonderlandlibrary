package com.initial.commands.impl;

import com.initial.commands.*;
import net.minecraft.client.*;
import com.initial.*;
import net.minecraft.util.*;

public class HClip extends Command
{
    protected Minecraft mc;
    
    public HClip() {
        super("HClip", "HClip", "hclip <distance>", new String[] { "hclip" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length == 2) {
            Astomero.addChatMessage("Invalid Arguments, Please use .hclip <distance>");
        }
        if (args.length == 1) {
            this.mc.thePlayer.noClip = true;
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + this.mc.thePlayer.getLookVec().xCoord * Double.parseDouble(args[0]), this.mc.thePlayer.posY, this.mc.thePlayer.posZ + this.mc.thePlayer.getLookVec().zCoord * Double.parseDouble(args[0]));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§8> §aTeleported " + args[0] + ".0 blocks"));
        }
    }
}
