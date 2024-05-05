package ru.smertnix.celestial.feature.impl.visual;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.command.impl.GPSCommand;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.EventBossBar;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.math.RotationHelper;
import ru.smertnix.celestial.utils.math.animations.Animation;
import ru.smertnix.celestial.utils.other.ChatUtils;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class AirDropWay extends Feature {
	 float xBoss = 0;
	    float zBoss = 0;
	    String bossname = "";

	    public AirDropWay() {
	        super("Air Drop Way", "Показывает информацию о предстоящем аир дропе", FeatureCategory.Util);
	    }

	    @EventTarget
	    public void onBossBar(EventBossBar event) {
	        if (event.bossName.contains("x:")) {
	            String bossBar = event.bossName;
	            bossname = bossBar;
	            	 String string = bossBar;
	            	 String[] parts = string.split("z");
	            	 String part1 = !event.bossName.contains("���") ? parts[0] : parts[0].split("���")[1]; // 004 
	            	 String part2 = parts[1]; // 034556
	            	 xBoss = (part1.contains("-") ? -1 : 1) * Integer.parseInt(part1.replaceAll("�4", "").replaceAll("\\D+","").substring(1).substring(1).substring(1).substring(1));
	            	 zBoss = (part2.contains("-") ? -1 : 1) * Integer.parseInt(part2.replaceAll("�4", "").replaceAll("\\D+","").substring(1));
	        } else {
	            xBoss = 1;
	            zBoss = 1;
	        }
	    }

	    public static float getAngle(BlockPos entity) {
	        return (float) (RotationHelper.getRotations(entity.getX(), 0, entity.getZ())[0] - AnimationHelper.Interpolate(mc.player.rotationYaw, mc.player.prevRotationYaw, 1.0D));
	    }

	    @EventTarget
	    public void onRender2D(EventRender2D event) {
	    	if (xBoss == 0 && zBoss == 0) {
	    		return;
	    	}
	        if ((int) mc.player.getDistance(xBoss, mc.player.posY, zBoss) <= 3) {
	            toggle();
	        }
	        if ((int) mc.player.getDistance(xBoss, mc.player.posY, zBoss) <= 10) {
	            int x = event.getResolution().getScaledWidth() / 2;
	            int y = event.getResolution().getScaledHeight() / 2;
	        }
	        if ((int) mc.player.getDistance(xBoss, mc.player.posY, zBoss) <= 10) {
	            return;
	        }

	        int x2 = event.getResolution().getScaledWidth() / 2;
	        int y2 = event.getResolution().getScaledHeight() / 2 + 5;
	        int x = event.getResolution().getScaledWidth() / 2;
	        int y = event.getResolution().getScaledHeight() / 2;
	        GL11.glPushMatrix();
	        GlStateManager.enableTexture2D();
	        GlStateManager.enableDepth();
	        GlStateManager.resetColor();
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float) x, (float) y - 15, 0.0F);
	        GL11.glTranslatef((float) (-x), (float) (-y - 30), 0.0F);
	        mc.mntsb_16.drawString(MathematicHelper.round((int) mc.player.getDistance(xBoss, mc.player.posY, zBoss), 0) + "m", (float) (x2 - mc.mntsb_16.getStringWidth(MathematicHelper.round((int) mc.player.getDistance(xBoss, mc.player.posY, zBoss), 0) + "m") / 2 + Math.cos((getAngle(new BlockPos(xBoss, 0, zBoss)) % 360.0F)  * Math.PI / 180.0) * 30), (float) (y2 + Math.sin((getAngle(new BlockPos(xBoss, 0, zBoss)) % 360.0F) * Math.PI / 180.0) * 30), -1);
	        mc.mntsb_16.drawString("Air Drop", (float) (x2 - mc.mntsb_16.getStringWidth("Air Drop") / 2 + Math.cos((getAngle(new BlockPos(xBoss, 0, zBoss)) % 360.0F) * Math.PI / 180.0) * 30), (float) (y2 + 10 + Math.sin((getAngle(new BlockPos(xBoss, 0, zBoss)) % 360.0F) * Math.PI / 180.0) * 30), -1);
	        GL11.glTranslatef((float) x, (float) y, 0.0F);
	        GL11.glTranslatef((float) (-x), (float) (-y - 30), 0.0F);
	        GL11.glPopMatrix();
	        
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float) x, (float) y - 15, 0.0F);
	        GL11.glRotatef(getAngle(new BlockPos(xBoss, 0, zBoss)) % 360.0F + 180.0F, 0.0F, 0.0F, 1.0F);
	        GL11.glTranslatef((float) (-x), (float) (-y - 30), 0.0F);
	        RenderUtils.drawTriangle((float) x - 5, (float) (y + 50), 5.0F, 10.0F, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().getRGB());
	        GL11.glTranslatef((float) x, (float) y, 0.0F);
	        GL11.glRotatef(-(getAngle(new BlockPos(xBoss, 0, zBoss)) % 360.0F + 180.0F), 0.0F, 0.0F, 1.0F);
	        GL11.glTranslatef((float) (-x), (float) (-y - 30), 0.0F);
	        GL11.glPopMatrix();
	        GL11.glPopMatrix();
	    }
}
