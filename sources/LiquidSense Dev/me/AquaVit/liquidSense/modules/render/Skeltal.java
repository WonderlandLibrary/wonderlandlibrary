package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateModelEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@ModuleInfo(name = "Skeltal", description = "Skeltal", category = ModuleCategory.RENDER)
public class Skeltal extends Module {
    private final Map<EntityPlayer, float[][]> playerRotationMap = new WeakHashMap<EntityPlayer, float[][]>();
    private final BoolValue smoothLines = new BoolValue("SmoothLines",true);

    @EventTarget
    public final void onModelUpdate(UpdateModelEvent event) {
        ModelPlayer model = event.getModel();
        this.playerRotationMap.put(event.getPlayer(), new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    @EventTarget
    public void onRender(Render3DEvent event) {
        this.setupRender(true);
        GL11.glEnable((int)2903);
        GL11.glDisable((int)2848);
        this.playerRotationMap.keySet().removeIf(this::contain);
        Map<EntityPlayer, float[][]> playerRotationMap = this.playerRotationMap;
        List worldPlayers = mc.theWorld.playerEntities;
        Object[] players = playerRotationMap.keySet().toArray();
        int playersLength = players.length;
        for (int i = 0; i < playersLength; ++i) {
            EntityPlayer player = (EntityPlayer)players[i];
            float[][] entPos = playerRotationMap.get(player);
            if (entPos == null || player.getEntityId() == -1488 || !player.isEntityAlive() || !RenderUtils.isInViewFrustrum(player) || player.isDead || player == mc.thePlayer || player.isPlayerSleeping() || player.isInvisible()) continue;
            GL11.glPushMatrix();
            float[][] modelRotations = playerRotationMap.get(player);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            double x = interpolate(player.posX, player.lastTickPosX, event.getPartialTicks()) - mc.getRenderManager().renderPosX;
            double y = interpolate(player.posY, player.lastTickPosY, event.getPartialTicks()) - mc.getRenderManager().renderPosY;
            double z = interpolate(player.posZ, player.lastTickPosZ, event.getPartialTicks()) - mc.getRenderManager().renderPosZ;
            GL11.glTranslated((double)x, (double)y, (double)z);
            float bodyYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * mc.timer.renderPartialTicks;
            GL11.glRotatef((float)(-bodyYawOffset), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)(player.isSneaking() ? -0.235 : 0.0));
            float legHeight = player.isSneaking() ? 0.6f : 0.75f;
            float rad = 57.29578f;
            GL11.glPushMatrix();
            GL11.glTranslated((double)-0.125, (double)legHeight, (double)0.0);
            if (modelRotations[3][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[3][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[3][2] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-legHeight), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.125, (double)legHeight, (double)0.0);
            if (modelRotations[4][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[4][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[4][2] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-legHeight), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated((double)0.0, (double)0.0, (double)(player.isSneaking() ? 0.25 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)(player.isSneaking() ? -0.05 : 0.0), (double)(player.isSneaking() ? -0.01725 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)-0.375, (double)((double)legHeight + 0.55), (double)0.0);
            if (modelRotations[1][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[1][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[1][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[1][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[1][2] != 0.0f) {
                GL11.glRotatef((float)(-modelRotations[1][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.375, (double)((double)legHeight + 0.55), (double)0.0);
            if (modelRotations[2][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[2][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[2][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[2][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[2][2] != 0.0f) {
                GL11.glRotatef((float)(-modelRotations[2][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(bodyYawOffset - player.rotationYawHead), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)legHeight + 0.55), (double)0.0);
            if (modelRotations[0][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[0][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.3, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(player.isSneaking() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)(player.isSneaking() ? -0.16175 : 0.0), (double)(player.isSneaking() ? -0.48025 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)legHeight, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.125, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.125, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)legHeight, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.55, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)legHeight + 0.55), (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.375, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.375, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
        this.setupRender(false);
    }

    private void setupRender(boolean start) {
        boolean smooth = this.smoothLines.get();
        if (start) {
            if (smooth) {
                startSmooth();
            } else {
                GL11.glDisable((int)2848);
            }
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
        } else {
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            if (smooth) {
                endSmooth();
            }
        }
        GL11.glDepthMask((!start ? 1 : 0) != 0);
    }

    private boolean contain(EntityPlayer var0) {
        return !mc.theWorld.playerEntities.contains(var0);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void startSmooth() {
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
    }

    public static void endSmooth() {
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glEnable((int)2832);
    }
}
