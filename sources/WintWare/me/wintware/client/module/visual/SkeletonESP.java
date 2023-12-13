package me.wintware.client.module.visual;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventModelUpdate;
import me.wintware.client.event.impl.EventRender3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.GL.GLUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class SkeletonESP
extends Module {
    private final Map<EntityPlayer, float[][]> playerRotationMap = new WeakHashMap<EntityPlayer, float[][]>();
    private final String SMOOTH = "Smooth";

    public SkeletonESP() {
        super("SkeletonESP", Category.Visuals);
        Main.instance.setmgr.rSetting(new Setting("Skeleton Width", this, 1.0, 0.1, 15.0, false));
        Main.instance.setmgr.rSetting(new Setting("Skeleton Smooth", this, true));
    }

    @EventTarget
    public void onModel(EventModelUpdate event) {
        ModelPlayer model = event.getModel();
        this.playerRotationMap.put(event.getPlayer(), new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    @EventTarget
    public void onRender3D(EventRender3D render) {
        try {
            float wd = Main.instance.setmgr.getSettingByName("Skeleton Width").getValFloat();
            this.setupRender(true);
            GL11.glEnable(2903);
            GL11.glDisable(2848);
            this.playerRotationMap.keySet().removeIf(this::contain);
            Map<EntityPlayer, float[][]> playerRotationMap = this.playerRotationMap;
            List worldPlayers = SkeletonESP.mc.world.playerEntities;
            Object[] players = playerRotationMap.keySet().toArray();
            int playersLength = players.length;
            for (int i = 0; i < playersLength; ++i) {
                EntityPlayer player = (EntityPlayer)players[i];
                float[][] entPos = playerRotationMap.get(player);
                if (entPos == null || player.getEntityId() == -1488 || !player.isEntityAlive() || !RenderUtil.isInViewFrustrum(player) || player.isDead) continue;
                if (player == Minecraft.player || player.isPlayerSleeping() || player.isInvisible()) continue;
                GL11.glPushMatrix();
                float[][] modelRotations = playerRotationMap.get(player);
                GL11.glLineWidth(wd);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                double x = GLUtils.interpolate(player.posX, player.lastTickPosX, render.getParticlTicks()) - SkeletonESP.mc.getRenderManager().viewerPosX;
                double y = GLUtils.interpolate(player.posY, player.lastTickPosY, render.getParticlTicks()) - SkeletonESP.mc.getRenderManager().viewerPosY;
                double z = GLUtils.interpolate(player.posZ, player.lastTickPosZ, render.getParticlTicks()) - SkeletonESP.mc.getRenderManager().viewerPosZ;
                GL11.glTranslated(x, y, z);
                float bodyYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * SkeletonESP.mc.timer.renderPartialTicks;
                GL11.glRotatef(-bodyYawOffset, 0.0f, 1.0f, 0.0f);
                GL11.glTranslated(0.0, 0.0, player.isSneaking() ? -0.235 : 0.0);
                float legHeight = player.isSneaking() ? 0.6f : 0.75f;
                float rad = 57.29578f;
                GL11.glPushMatrix();
                GL11.glTranslated(-0.125, legHeight, 0.0);
                if (modelRotations[3][0] != 0.0f) {
                    GL11.glRotatef(modelRotations[3][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
                }
                if (modelRotations[3][1] != 0.0f) {
                    GL11.glRotatef(modelRotations[3][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
                }
                if (modelRotations[3][2] != 0.0f) {
                    GL11.glRotatef(modelRotations[3][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -legHeight, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.125, legHeight, 0.0);
                if (modelRotations[4][0] != 0.0f) {
                    GL11.glRotatef(modelRotations[4][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
                }
                if (modelRotations[4][1] != 0.0f) {
                    GL11.glRotatef(modelRotations[4][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
                }
                if (modelRotations[4][2] != 0.0f) {
                    GL11.glRotatef(modelRotations[4][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -legHeight, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glTranslated(0.0, 0.0, player.isSneaking() ? 0.25 : 0.0);
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, player.isSneaking() ? -0.05 : 0.0, player.isSneaking() ? -0.01725 : 0.0);
                GL11.glPushMatrix();
                GL11.glTranslated(-0.375, (double)legHeight + 0.55, 0.0);
                if (modelRotations[1][0] != 0.0f) {
                    GL11.glRotatef(modelRotations[1][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
                }
                if (modelRotations[1][1] != 0.0f) {
                    GL11.glRotatef(modelRotations[1][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
                }
                if (modelRotations[1][2] != 0.0f) {
                    GL11.glRotatef(-modelRotations[1][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -0.5, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.375, (double)legHeight + 0.55, 0.0);
                if (modelRotations[2][0] != 0.0f) {
                    GL11.glRotatef(modelRotations[2][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
                }
                if (modelRotations[2][1] != 0.0f) {
                    GL11.glRotatef(modelRotations[2][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
                }
                if (modelRotations[2][2] != 0.0f) {
                    GL11.glRotatef(-modelRotations[2][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -0.5, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glRotatef(bodyYawOffset - player.rotationYawHead, 0.0f, 1.0f, 0.0f);
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, (double)legHeight + 0.55, 0.0);
                if (modelRotations[0][0] != 0.0f) {
                    GL11.glRotatef(modelRotations[0][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
                }
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.3, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glRotatef(player.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
                GL11.glTranslated(0.0, player.isSneaking() ? -0.16175 : 0.0, player.isSneaking() ? -0.48025 : 0.0);
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, legHeight, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.125, 0.0, 0.0);
                GL11.glVertex3d(0.125, 0.0, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, legHeight, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.55, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, (double)legHeight + 0.55, 0.0);
                GL11.glBegin(3);
                GL11.glVertex3d(-0.375, 0.0, 0.0);
                GL11.glVertex3d(0.375, 0.0, 0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
            }
            this.setupRender(false);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void setupRender(boolean start) {
        if (start) {
            if (Main.instance.setmgr.getSettingByName("Skeleton Smooth").getValue()) {
                GLUtils.startSmooth();
            } else {
                GL11.glDisable(2848);
            }
            GL11.glDisable(2929);
            GL11.glDisable(3553);
        } else {
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            if (Main.instance.setmgr.getSettingByName("Skeleton Smooth").getValue()) {
                GLUtils.endSmooth();
            }
        }
        GL11.glDepthMask((!start ? 1 : 0) != 0);
    }

    private boolean contain(EntityPlayer var0) {
        return !SkeletonESP.mc.world.playerEntities.contains(var0);
    }
}

