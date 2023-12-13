package com.enjoytheban.command.commands;

import com.enjoytheban.command.Command;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.TimerUtil;
import com.enjoytheban.utils.math.MathUtil;

import net.minecraft.item.ItemEnderPearl;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;

public class VClip extends Command {

	private TimerUtil timer = new TimerUtil();

	public VClip() {
		super("Vc", new String[] { "Vclip", "clip", "verticalclip", "clip" }, "", "Teleport down a specific ammount");
	}

	@Override
	public String execute(String[] args) {
		if(!Helper.onServer("enjoytheban")) {
			if (args.length > 0) {
				if (MathUtil.parsable(args[0], MathUtil.NumberType.DOUBLE)) {
					float distance = Float.parseFloat(args[0]);
					Helper.mc.thePlayer.setPosition(Helper.mc.thePlayer.posX, Helper.mc.thePlayer.posY + distance,
							Helper.mc.thePlayer.posZ);
					Helper.sendMessage("> Vclipped " + distance + " blocks");
				} else {
					this.syntaxError(EnumChatFormatting.GRAY + args[0] + " is not a valid number");
				}
			} else {
				this.syntaxError(EnumChatFormatting.GRAY + "Valid .vclip <number>");
			}
		} else {
			Helper.sendMessage("> You cannot use vclip on the ETB Server.");
		}
		return null;
	}
}
