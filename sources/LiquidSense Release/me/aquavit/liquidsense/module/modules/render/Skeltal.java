package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.event.events.UpdateModelEvent;
import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

@ModuleInfo(name = "Skeltal", description = "Skeltal", category = ModuleCategory.RENDER)
public class Skeltal extends Module {
    private final Map<EntityPlayer, float[][]> playerRotationMap = new WeakHashMap<>();

    private final IntegerValue red = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue green = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue blue = new IntegerValue("Blue", 255, 0, 255);
    private final IntegerValue alpha = new IntegerValue("Alpha", 200, 0, 255);

    private final BoolValue smoothLines = new BoolValue("SmoothLines",true);
    private final BoolValue hurtTime = new BoolValue("Hurttime", true);

    private final Translate r = new Translate(0f , 0f);
    private final Translate g = new Translate(0f , 0f);
    private final Translate b = new Translate(0f , 0f);
    private final Translate a = new Translate(0f , 0f);

    @EventTarget
    public final void onUpdate(UpdateModelEvent event) {
        ModelPlayer model = event.getModel();
        playerRotationMap.put(event.getPlayer(), new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    //和esp合并

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        setupRender(true);
        GL11.glEnable(2903);
        GL11.glDisable(2848);
        playerRotationMap.keySet().removeIf(this::contain);
        Map<EntityPlayer, float[][]> playerRotationMap = this.playerRotationMap;
        Object[] players = playerRotationMap.keySet().toArray();
        for (Object o : players) {
            EntityPlayer player = (EntityPlayer) o;
            float[][] entPos = playerRotationMap.get(player);
            if (entPos == null || player.getEntityId() == -1488 || !player.isEntityAlive() || !RenderUtils.isInViewFrustrum(player) || player.isDead || player == mc.thePlayer || player.isPlayerSleeping() || player.isInvisible())
                continue;

            GL11.glPushMatrix();
            float[][] modelRotations = playerRotationMap.get(player);
            GL11.glLineWidth(1.0f);

            Color color;
            if (hurtTime.get() && player.hurtTime > 0) {
                color = new Color(1f, 0.1f, 0.1f, Math.max(alpha.get() / 255f, 0.5f));
            } else {
                color = new Color(red.get() / 255f, green.get() / 255f, blue.get() / 255f, alpha.get() / 255f);
            }

            r.translate(0f, color.getRed(), 0.5);
            g.translate(0f, color.getGreen(), 0.5);
            b.translate(0f, color.getBlue(), 0.5);
            a.translate(0f, color.getAlpha(), 0.5);

            RenderUtils.color(new Color(r.getY() / 255f , g.getY() / 255f , b.getY() / 255f , alpha.get() / 255f).getRGB());

            double x = RenderUtils.interpolate(player.posX, player.lastTickPosX, event.getPartialTicks()) - mc.getRenderManager().renderPosX;
            double y = RenderUtils.interpolate(player.posY, player.lastTickPosY, event.getPartialTicks()) - mc.getRenderManager().renderPosY;
            double z = RenderUtils.interpolate(player.posZ, player.lastTickPosZ, event.getPartialTicks()) - mc.getRenderManager().renderPosZ;
            GL11.glTranslated(x, y, z);
            if (LiquidSense.moduleManager.getModule(RenderChanger.class).getState() && RenderChanger.littleEntitiesValue.get())
                GL11.glScalef(0.5f, 0.5f, 0.5f);
            float bodyYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * mc.timer.renderPartialTicks;
            GL11.glRotatef((-bodyYawOffset), 0.0f, 1.0f, 0.0f);
            GL11.glTranslated(0.0, 0.0, (player.isSneaking() ? -0.235 : 0.0));
            float legHeight = player.isSneaking() ? 0.6f : 0.75f;
            //float rad = 57.29578f;
            GL11.glPushMatrix();
            GL11.glTranslated(-0.125, legHeight, 0.0);
            if (modelRotations[3][0] != 0.0f) {
                GL11.glRotatef((modelRotations[3][0] * 57.29578f), 1.0f, 0.0f, 0.0f);
            }
            if (modelRotations[3][1] != 0.0f) {
                GL11.glRotatef((modelRotations[3][1] * 57.29578f), 0.0f, 1.0f, 0.0f);
            }
            if (modelRotations[3][2] != 0.0f) {
                GL11.glRotatef((modelRotations[3][2] * 57.29578f), 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (-legHeight), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.125, legHeight, 0.0);
            if (modelRotations[4][0] != 0.0f) {
                GL11.glRotatef((modelRotations[4][0] * 57.29578f), 1.0f, 0.0f, 0.0f);
            }
            if (modelRotations[4][1] != 0.0f) {
                GL11.glRotatef((modelRotations[4][1] * 57.29578f), 0.0f, 1.0f, 0.0f);
            }
            if (modelRotations[4][2] != 0.0f) {
                GL11.glRotatef((modelRotations[4][2] * 57.29578f), 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (-legHeight), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, 0.0, (player.isSneaking() ? 0.25 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (player.isSneaking() ? -0.05 : 0.0), (player.isSneaking() ? -0.01725 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated(-0.375, (legHeight + 0.55), 0.0);
            if (modelRotations[1][0] != 0.0f) {
                GL11.glRotatef((modelRotations[1][0] * 57.29578f), 1.0f, 0.0f, 0.0f);
            }
            if (modelRotations[1][1] != 0.0f) {
                GL11.glRotatef((modelRotations[1][1] * 57.29578f), 0.0f, 1.0f, 0.0f);
            }
            if (modelRotations[1][2] != 0.0f) {
                GL11.glRotatef((-modelRotations[1][2] * 57.29578f), 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.375, (legHeight + 0.55), 0.0);
            if (modelRotations[2][0] != 0.0f) {
                GL11.glRotatef((modelRotations[2][0] * 57.29578f), 1.0f, 0.0f, 0.0f);
            }
            if (modelRotations[2][1] != 0.0f) {
                GL11.glRotatef((modelRotations[2][1] * 57.29578f), 0.0f, 1.0f, 0.0f);
            }
            if (modelRotations[2][2] != 0.0f) {
                GL11.glRotatef((-modelRotations[2][2] * 57.29578f), 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef((bodyYawOffset - player.rotationYawHead), 0.0f, 1.0f, 0.0f);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (legHeight + 0.55), 0.0);
            if (modelRotations[0][0] != 0.0f) {
                GL11.glRotatef((modelRotations[0][0] * 57.29578f), 1.0f, 0.0f, 0.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.3, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef((player.isSneaking() ? 25.0f : 0.0f), 1.0f, 0.0f, 0.0f);
            GL11.glTranslated(0.0, (player.isSneaking() ? -0.16175 : 0.0), (player.isSneaking() ? -0.48025 : 0.0));
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
            GL11.glTranslated(0.0, (legHeight + 0.55), 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.375, 0.0, 0.0);
            GL11.glVertex3d(0.375, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
        setupRender(false);
    }

    private void setupRender(boolean start) {
        if (start) {
            if (smoothLines.get()) {
                startSmooth();
            } else {
                GL11.glDisable(2848);
            }
            GL11.glDisable(2929);
            GL11.glDisable(3553);
        } else {
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            endSmooth();
        }
        GL11.glDepthMask((!start ? 1 : 0) != 0);
    }

    private boolean contain(EntityPlayer var0) {
        return !mc.theWorld.playerEntities.contains(var0);
    }

    private static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
    }

    private static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
    }
}