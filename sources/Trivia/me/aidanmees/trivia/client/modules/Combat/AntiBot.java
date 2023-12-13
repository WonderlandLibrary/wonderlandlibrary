package me.aidanmees.trivia.client.modules.Combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class AntiBot extends Module {
	

   WaitTimer timer = new WaitTimer();
    public static ArrayList<EntityPlayer> hypixelBots = new ArrayList<EntityPlayer>();


	public AntiBot() {
		super("AntiBot", Keyboard.KEY_NONE, Category.COMBAT, "Removes bots.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if (currentMode.equals("Hypixel")) {


	        if (!hypixelBots.isEmpty()) {
	            hypixelBots.clear();
	          
	        }

	       
	            for (Object object : mc.theWorld.loadedEntityList) {

	                if (object instanceof EntityPlayer) {

	                    EntityPlayer ent = (EntityPlayer) object;

	                    if (ent != mc.thePlayer && mc.thePlayer.getDistanceToEntity(ent) < 10 && !hypixelBots.contains(ent)) {
	                        String str = ent.getDisplayName().getFormattedText();
	                        if (str.equalsIgnoreCase(ent.getName() + "\247r") || str.equalsIgnoreCase(ent.getName() + EnumChatFormatting.RESET) || str.contains("NPC") || (!getPlayerList().contains(ent))) {
	                            timer.reset();
	                            hypixelBots.add(ent);
	                        }
	                    }
	                }
	            
	        }
	   
		}
		
		if (currentMode.equals("Invisible"))
			for (final Object entity : mc.theWorld.loadedEntityList) {
				if (((Entity) entity).isInvisible()) {
					final Object o = entity;

					if (o == mc.thePlayer) {
						continue;
					}
					mc.theWorld.removeEntity((Entity) entity);
					if (entity instanceof EntityPlayer) {
						trivia.chatMessage("Removed "+((EntityPlayer) entity).getName());
					}
					
				}
			}
		if (currentMode.equals("AAC")) {
			for (final Object entity : mc.theWorld.loadedEntityList) {
				if (((Entity) entity).isInvisible()) {
					final Object o = entity;

					if (o == mc.thePlayer) {
						continue;
					}
					Entity ent = (Entity)entity;
				if(ent.getEntityId() > 100700 && (!ent.isInvisible())) {
			        mc.theWorld.removeEntity((Entity)ent);
			}
			}
		}
		}
		if (currentMode.equals("Check TabList")) {
			for (final Object ent : mc.theWorld.loadedEntityList) {
				if (((Entity) ent).isInvisible()) {
					final Object o = ent;

					if (o == mc.thePlayer) {
						continue;
					}
		}
				if (ent instanceof EntityPlayer && ent != mc.thePlayer) {
			if (!isInTablist((EntityPlayer)ent)) {
		        mc.theWorld.removeEntity((Entity) ent);
			}
			}
			}
		}
	}

	 private boolean isInTablist(EntityPlayer player)
	  {
	    if (mc.isSingleplayer()) {
	      return false;
	    }
	    Iterator var3 = mc.getNetHandler().getPlayerInfoMap().iterator();
	    while (var3.hasNext())
	    {
	      Object ob = var3.next();
	      NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)ob;
	      if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
	        return true;
	      }
	    }
	    return false;
	  }
	
	@Override
	public String[] getModes() {

		return new String[] { "Invisible", "AAC", "Check TabList" , "Hypixel"};
	}



    private List<EntityPlayer> getPlayerList() {
        NetHandlerPlayClient var4 = Minecraft.getMinecraft().thePlayer.sendQueue;
        List<EntityPlayer> list = new ArrayList<EntityPlayer>();
        List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());
        for (Object o : players) {
            NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if (info == null) {
                continue;
            }
            list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }


    public static boolean isReal(EntityPlayer player) {
        for (NetworkPlayerInfo npi : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
            if (npi == null || npi.getGameProfile() == null || player.getGameProfile() == null || !npi.getGameProfile().getId().toString().equals(player.getGameProfile().getId().toString()) || player.ticksExisted <= 35 || player.getEntityId() > 1000000000 || player.getName().startsWith("\u00a7c") || npi.getDisplayName() != null && npi.getDisplayName().getFormattedText().startsWith("\u00a7f")) continue;
            return true;
        }
        return false;
    }


}