package me.aidanmees.trivia.client.tools;

import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.target.AuraUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.src.MathUtils;


public class Helper {
	
	private static AuraUtils aurautils = new AuraUtils();
	private static CombatUtils combatutils = new CombatUtils();
	private static InventoryUtils inventoryUtils = new InventoryUtils();
	
	private static MathUtils mathUtils = new MathUtils();

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    public static EntityPlayerSP player() {
        return mc().thePlayer;
    }
    public static WorldClient world() {
        return mc().theWorld;
    }
    public static AuraUtils entityUtils() {
        return aurautils;
    }
   
    public static void sendPacket(Packet p){
    	mc().getNetHandler().addToSendQueue(p);
    }
	public static CombatUtils combatUtils() {
		 return combatUtils();
	}
	public static MathUtils mathUtils() {
		return mathUtils;
	}
	public static InventoryUtils inventoryUtils() {
		
		return inventoryUtils;
	}


    public static void scaledString(double scale, String text, int x2, int y2, int color) {
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x2, y2, color);
        GL11.glPopMatrix();
    }


    public static void sendPacket(Packet packet, boolean sendFromPlayer) {
        if (sendFromPlayer) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
        } else {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
        }
    }
    public static boolean isOnSameTeam(EntityLivingBase e2, EntityLivingBase e1, boolean checkColors) {
        return checkColors && Helper.getTeamColor(e2) == Helper.getTeamColor(e1) || e2.isOnSameTeam(e1);
    }


    public static int getTeamColor(EntityLivingBase entity) {
        String s2;
        int i2 = 16777215;
        ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entity.getTeam();
        if (scoreplayerteam != null && (s2 = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix())).length() >= 2) {
            i2 = FontRenderer.getColorCode(s2.charAt(1));
        }
        return i2;
    }

	
    
    
}
