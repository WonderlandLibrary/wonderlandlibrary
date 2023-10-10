/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.AveReborn.Value;
import me.AveReborn.events.EventRender;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.mods.MISC.Teams;
import me.AveReborn.util.Colors;
import me.AveReborn.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class ESP
extends Mod {
    public Value<String> mode = new Value("ESP", "Mode", 0);
    public static boolean isOutline = false;

    public ESP() {
        super("ESP", Category.RENDER);
        ArrayList settings = new ArrayList();
        this.mode.mode.add("Box");
        this.mode.mode.add("2D");
        this.mode.mode.add("Outline");
    }

    @Override
    public void onDisable() {
        isOutline = false;
        super.onDisable();
    }

    @EventTarget
    public void onRender(EventRender event) {
        if (this.mode.isCurrentMode("Box")) {
            this.setDisplayName("Box");
            this.doBoxESP(event);
        }
        if (this.mode.isCurrentMode("2D")) {
            this.setDisplayName("2D");
            this.doOther2DESP();
        }
        if (this.mode.isCurrentMode("Outline")) {
            this.setDisplayName("Outline");
            isOutline = true;
        } else {
            isOutline = false;
        }
    }

    private void doBoxESP(EventRender event) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        for (Object o2 : this.mc.theWorld.loadedEntityList) {
            if (!(o2 instanceof EntityPlayer) || o2 == Minecraft.thePlayer) continue;
            EntityPlayer ent = (EntityPlayer)o2;
            if (Teams.isOnSameTeam(ent)) {
                RenderUtil.entityESPBox(ent, new Color(0, 255, 0), event);
                continue;
            }
            if (ent.hurtTime > 0) {
                RenderUtil.entityESPBox(ent, new Color(255, 0, 0), event);
                continue;
            }
            if (ent.isInvisible()) {
                RenderUtil.entityESPBox(ent, new Color(255, 255, 0), event);
                continue;
            }
            RenderUtil.entityESPBox(ent, new Color(255, 255, 255), event);
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    private boolean isValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && entity.getHealth() >= 0.0f && entity != Minecraft.thePlayer) {
            return true;
        }
        return false;
    }

    private void doOther2DESP() {
        for (EntityPlayer entity : this.mc.theWorld.playerEntities) {
            if (!this.isValid(entity)) continue;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            float partialTicks = this.mc.timer.renderPartialTicks;
            this.mc.getRenderManager();
            double x2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
            float DISTANCE = Minecraft.thePlayer.getDistanceToEntity(entity);
            float DISTANCE_SCALE = Math.min(DISTANCE * 0.15f, 0.15f);
            float SCALE = 0.035f;
            float xMid = (float)x2;
            float yMid = (float)y2 + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f);
            float zMid = (float)z2;
            GlStateManager.translate((float)x2, (float)y2 + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f), (float)z2);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            this.mc.getRenderManager();
            GlStateManager.rotate(- RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glScalef(- SCALE, - SCALE, - (SCALE /= 2.0f));
            Tessellator tesselator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tesselator.getWorldRenderer();
            float HEALTH = entity.getHealth();
            int COLOR = -1;
            COLOR = (double)HEALTH > 20.0 ? -65292 : ((double)HEALTH >= 10.0 ? -16711936 : ((double)HEALTH >= 3.0 ? -23296 : -65536));
            Color gray = new Color(0, 0, 0);
            double thickness = 1.5f + DISTANCE * 0.01f;
            double xLeft = -20.0;
            double xRight = 20.0;
            double yUp = 27.0;
            double yDown = 130.0;
            double size = 10.0;
            Color color = new Color(255, 255, 255);
            if (entity.hurtTime > 0) {
                color = new Color(255, 0, 0);
            } else if (Teams.isOnSameTeam(entity)) {
                color = new Color(0, 255, 0);
            } else if (entity.isInvisible()) {
                color = new Color(255, 255, 0);
            }
            ESP.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 0.5f, Colors.BLACK.c, 0);
            ESP.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness, color.getRGB(), 0);
            ESP.drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp), (float)xLeft - 2.0f, (float)yDown, 0.15f, Colors.BLACK.c, new Color(100, 100, 100).getRGB());
            ESP.drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp) * Math.min(1.0f, entity.getHealth() / 20.0f), (float)xLeft - 2.0f, (float)yDown, 0.15f, Colors.BLACK.c, COLOR);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GlStateManager.disableBlend();
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glNormal3f(1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }

    private static void drawBorderedRect(float xLeft, float yUp, float xRight, float yDown, float l1, String c,
			int col2) {
		// TODO 自动生成的方法存根
		
	}

	public static void drawBorderedRect(float x2, float y2, float x22, float y22, float l1, int col1, int col2) {
        ESP.drawRect(x2, y2, x22, y22, col2);
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
        float f22 = (float)(col1 >> 8 & 255) / 255.0f;
        float f3 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f22, f3, f2);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRect(float g2, float h2, float i2, float j2, int col1) {
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
        float f22 = (float)(col1 >> 8 & 255) / 255.0f;
        float f3 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f22, f3, f2);
        GL11.glBegin(7);
        GL11.glVertex2d(i2, h2);
        GL11.glVertex2d(g2, h2);
        GL11.glVertex2d(g2, j2);
        GL11.glVertex2d(i2, j2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }
}

