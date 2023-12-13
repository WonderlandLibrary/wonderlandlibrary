package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.util.render.RenderUtil;
import de.tired.util.render.ColorUtil;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render3DEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleAnnotation(name = "Trajectories", category = ModuleCategory.RENDER)
public class Trajectories extends Module {

    public float yaw, pitch;

    @EventTarget
    public void onRender(Render3DEvent e) {
        yaw = MC.thePlayer.rotationYaw;
        pitch = MC.thePlayer.rotationPitch;
        if (MC.thePlayer.getHeldItem() == null) {
            return;
        }
        if (!(MC.thePlayer.getHeldItem().getItem() instanceof ItemBow || MC.thePlayer.getHeldItem().getItem() instanceof ItemSnowball || MC.thePlayer.getHeldItem().getItem() instanceof ItemEnderPearl || MC.thePlayer.getHeldItem().getItem() instanceof ItemEgg || MC.thePlayer.getHeldItem().getItem() instanceof ItemPotion && ItemPotion.isSplash(MC.thePlayer.getHeldItem().getItemDamage()))) {
            return;
        }
        final boolean bow = MC.thePlayer.getHeldItem().getItem() instanceof ItemBow;
        final boolean pot = MC.thePlayer.getHeldItem().getItem() instanceof ItemPotion;
        float throwingYaw = yaw;
        float throwingPitch = pitch;

        double posX = RenderManager.renderPosX - (double) (MathHelper.cos((float) (throwingYaw / 180.0f * Math.PI)) * 0.16f), posY = RenderManager.renderPosY + (double) this.MC.thePlayer.getEyeHeight() - 0.1, posZ = RenderManager.renderPosZ - (double) (MathHelper.sin((float) (throwingYaw / 180.0f * Math.PI)) * 0.16f);
        double motionX = (double) ((-MathHelper.sin((float) (throwingYaw / 180.0f * Math.PI))) * MathHelper.cos((float) (throwingPitch / 180.0f * Math.PI))) * (bow ? 1.0 : 0.4);
        double motionY = (double) (-MathHelper.sin((float) ((throwingPitch - (float) (pot ? 20 : 0)) / 180.0f * Math.PI))) * (bow ? 1.0 : 0.4);
        double motionZ = (double) (MathHelper.cos((float) (throwingYaw / 180.0f * Math.PI)) * MathHelper.cos((float) (throwingPitch / 180.0f * Math.PI))) * (bow ? 1.0 : 0.4);

        int powerx2 = 72000 - MC.thePlayer.getItemInUseCount();
        float power = (float) powerx2 / 20.0f;
        if ((double) (power = (power * power + power * 2.0f) / 3.0f) < 0.1) {
            return;
        }
        if (power > 1.0f) {
            power = 1.0f;
        }

        final float distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        {
            motionX /= distance;
            motionY /= distance;
            motionZ /= distance;
            motionX *= (double) (bow ? power * 2.0f : 1.0f) * (pot ? 0.5 : 1.5);
            motionY *= (double) (bow ? power * 2.0f : 1.0f) * (pot ? 0.5 : 1.5);
            motionZ *= (double) (bow ? power * 2.0f : 1.0f) * (pot ? 0.5 : 1.5);
        }
        Color firstColor = ColorUtil.interpolateColorsBackAndForth(12, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
        {
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0f, 0.0f);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPushMatrix();
            RenderUtil.glColor(firstColor.getRGB());
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(2);
            GL11.glBegin(GL11.GL_LINE_STRIP);
        }

        boolean hasLanded = false;
        MovingObjectPosition landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            Vec3 present = new Vec3(posX, posY, posZ);
            Vec3 future = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition possibleLandingStrip = MC.theWorld.rayTraceBlocks(present, future, false, true, false);
            if (possibleLandingStrip != null) {
                if (possibleLandingStrip.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                    landingPosition = possibleLandingStrip;
                    hasLanded = true;
                }
            } else {
                Entity entityHit = this.getEntityHit(present, future);
                if (entityHit != null) {
                    landingPosition = new MovingObjectPosition(entityHit);
                    hasLanded = true;
                }
            }
            float motionAdjustment = 0.99f;
            motionY *= motionAdjustment;

            GL11.glVertex3d((posX += (motionX *= motionAdjustment)) - RenderManager.renderPosX, (posY += (motionY -= pot ? 0.05 : (bow ? 0.05 : 0.03))) - RenderManager.renderPosY, (posZ += (motionZ *= motionAdjustment)) - RenderManager.renderPosZ);
        }
        GL11.glEnd();
        GL11.glPushMatrix();
        GL11.glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
        if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int index = landingPosition.sideHit.getIndex();
            if (index == 1) {
                GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
            } else if (index == 2) {
                GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            } else if (index == 3) {
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            } else if (index == 4) {
                GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            } else if (index == 5) {
                GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glRotatef(MC.thePlayer.rotationYaw, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            //CircleUtil.drawGoodCircle(0, 0, 7.4f,  new Color(firstColor.getRed(), firstColor.getGreen(), firstColor.getBlue(), 122).getRGB());
            RenderUtil.instance.drawRect(-0.4f, -0.4f, 0.4f, 0.4f, new Color(firstColor.getRed(), firstColor.getGreen(), firstColor.getBlue(), 122).getRGB());
        }
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        RenderUtil.glColor(firstColor.getRGB());
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }

    private Entity getEntityHit(Vec3 vecOrig, Vec3 vecNew) {
        for (Object o : MC.theWorld.loadedEntityList) {
            if (o == MC.thePlayer || !(o instanceof EntityLivingBase)) continue;
            final EntityLivingBase entity = (EntityLivingBase) o;
            final AxisAlignedBB bb = entity.getEntityBoundingBox().expand(0.2f, 0.2f, 0.2f);
            final MovingObjectPosition movingObjectPosition = bb.calculateIntercept(vecOrig, vecNew);
            if (movingObjectPosition == null) continue;
            return entity;
        }
        return null;
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
