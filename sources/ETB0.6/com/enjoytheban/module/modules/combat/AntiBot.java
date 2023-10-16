package com.enjoytheban.module.modules.combat;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.Helper;

public class AntiBot extends Module {

	public AntiBot() {
		super("AntiBot", new String[] { "nobot", "botkiller" }, ModuleType.Combat);
		setColor(new Color(217, 149, 251).getRGB());
	}

	// a check to tell if the entity is a bot
	public boolean isServerBot(Entity entity) {
		if (isEnabled()) {
			if (Helper.onServer("hypixel"))
				return !entity.getDisplayName().getFormattedText().startsWith("§") || entity.isInvisible() || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc");
			else if (Helper.onServer("mineplex")) {
				for (Object object : mc.theWorld.playerEntities) {
					EntityPlayer entityPlayer = (EntityPlayer) object;
					if ((entityPlayer != null) && (entityPlayer != mc.thePlayer)) {
						if (entityPlayer.getName().startsWith("Body #") || entityPlayer.getMaxHealth() == 20.0F) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}
}
