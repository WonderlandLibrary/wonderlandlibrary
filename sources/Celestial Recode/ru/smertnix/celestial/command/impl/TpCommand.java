package ru.smertnix.celestial.command.impl;

import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.server.management.PlayerList;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.EventBossBar;
import ru.smertnix.celestial.event.events.EventChat;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.feature.impl.visual.AirDropWay;
import ru.smertnix.celestial.friend.Friend;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class TpCommand extends CommandAbstract {
	public String message;
    Minecraft mc = Minecraft.getMinecraft();
	 float endY = 0;
	 float endZ = 0;
	 float endX = 0;
	 float x,y,z;
	 public static boolean air, ct;
    public TpCommand() {
    	   super("tp", "tp", ".tp x y z", "teleport", "tp");
    }
    public static boolean isNumeric(String str) {
    	  try {
    	    Double.parseDouble(str);
    	    return true;
    	  } catch(NumberFormatException e){
    	    return false;
    	  }
    	}
    @Override
    public void execute(String... args) {
    	 if (args.length > 0) {
    		 if (isNumeric(args[1])) {
    			 endX = (float) Double.parseDouble(args[1]);
    			 endY = (float) Double.parseDouble(args[2]);
    			 endZ = (float) Double.parseDouble(args[3]);
    			 x = (int) mc.player.posX;
            	 y = (int) mc.player.posY;
            	 z = (int) mc.player.posZ;
    		 } else if (args[1].contains("contract") || args[1].contains("ct")) {
				 Minecraft.getMinecraft().player.sendChatMessage("/contract get");
				 String ct = GuiNewChat.ct;
				 this.ct = true;
				 x = (int) mc.player.posX;
	        	 y = (int) mc.player.posY;
	        	 z = (int) mc.player.posZ;
			 } else if (args[1].contains("air") || args[1].contains("airdrop") || args[1].contains("boss")) {
				 endX = (float) GuiBossOverlay.x - 100;
    			 endY = (float) 70;
    			 endZ = (float) GuiBossOverlay.z;
    			 x = (int) mc.player.posX;
            	 y = (int) mc.player.posY;
            	 z = (int) mc.player.posZ;
			 }
             ChatUtils.addChatMessage("Телепортирую на заданные координаты " + endX + " " + endY + " " + endZ);
             if (mc.player.posX != endX && mc.player.posZ != endZ) {
             	//mc.player.motionY = 0.05f;
            	 this.mc.player.connection.sendPacket(new CPacketPlayer.Position((double)endX + 0.5, (double)endY, (double)endZ - 0.5, true));
                 this.mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY + 109, endZ, true));
                 this.mc.player.connection.sendPacket(new CPacketPlayer.Position((double)endX + 0.5, (double)endY, (double)endZ - 0.5, true));
             }
            	 if (args[4].equalsIgnoreCase("rehub") || args[2].equalsIgnoreCase("rehub")) {
                	 new Thread(() -> {
                         mc.player.sendChatMessage("/hub");
                         try {
                             TimeUnit.MILLISECONDS.sleep(400);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         mc.player.sendChatMessage("/grief-" + GuiIngame.grief);
                     }).start();
                 }
             
             
             if (args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("airdrop") || args[1].equalsIgnoreCase("boss")) {
            	 if (!Celestial.instance.featureManager.getFeature(AirDropWay.class).isEnabled()) {
            		 Celestial.instance.featureManager.getFeature(AirDropWay.class).toggle();
            	 }
             }
         } else {
        	 ChatUtils.addChatMessage(getUsage());
         }
    }
}
