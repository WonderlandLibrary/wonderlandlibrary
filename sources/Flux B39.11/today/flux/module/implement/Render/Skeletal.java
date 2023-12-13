package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.ColorValue;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Skeletal extends Module {

    private static Map<EntityPlayer, float[][]> entityModelRotations = new HashMap<EntityPlayer, float[][]>();

    public static ColorValue skeletonColor = new ColorValue("Skeletal", "Skeleton Color", Color.WHITE);

    public Skeletal() {
        super("Skeletal", Category.Render, false);
    }

    @EventTarget(value = Priority.LOWEST)
    public void onTreeD(WorldRenderEvent event) {
        GlStateManager.enableBlend();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.depthMask(false);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        entityModelRotations.keySet().removeIf(player -> !mc.theWorld.playerEntities.contains(player));
        mc.theWorld.playerEntities.forEach(player -> {
            if (player == mc.thePlayer || player.isInvisible()) {
                return;
            }
            float[][] modelRotations = entityModelRotations.get(player);
            if (modelRotations == null) {
                return;
            }
            GL11.glPushMatrix();
            GL11.glLineWidth((float)1.0f);
            Color c = skeletonColor.getColor();
            GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, (float)1.0f);
            Vec3 interp = interpolateRender((EntityPlayer) player);
            double x = interp.xCoord - mc.getRenderManager().renderPosX;
            double y = interp.yCoord - mc.getRenderManager().renderPosY;
            double z = interp.zCoord - mc.getRenderManager().renderPosZ;
            GL11.glTranslated((double)x, (double)y, (double)z);
            float bodyYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * mc.timer.renderPartialTicks;
            GL11.glRotatef((float)(-bodyYawOffset), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)(player.isSneaking() ? -0.235 : 0.0));
            float legHeight = player.isSneaking() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            GL11.glTranslated((double)-0.125, (double)legHeight, (double)0.0);
            if (modelRotations[3][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[3][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[3][2] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-legHeight), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.125, (double)legHeight, (double)0.0);
            if (modelRotations[4][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[4][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[4][2] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
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
                GL11.glRotatef((float)(modelRotations[1][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[1][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[1][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[1][2] != 0.0f) {
                GL11.glRotatef((float)(-modelRotations[1][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.375, (double)((double)legHeight + 0.55), (double)0.0);
            if (modelRotations[2][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[2][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[2][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[2][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[2][2] != 0.0f) {
                GL11.glRotatef((float)(-modelRotations[2][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
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
                GL11.glRotatef((float)(modelRotations[0][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
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
        });
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
    }

    public static void updateModel(EntityPlayer player, ModelPlayer model) {
        entityModelRotations.put(player, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    public Vec3 interpolateRender(EntityPlayer player) {
        float part = mc.timer.renderPartialTicks;
        double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)part;
        double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)part;
        double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)part;
        return new Vec3(interpX, interpY, interpZ);
    }

}
