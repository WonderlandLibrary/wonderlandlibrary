package com.masterof13fps.features.modules.impl.render;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventOutline;
import com.masterof13fps.manager.eventmanager.impl.EventRender;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.utils.render.Rainbow;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "ESP", category = Category.RENDER, description = "Shows selected targets")
public class ESP extends Module {

    Setting mode = new Setting("Mode", this, "Shader", new String[]{"Shader", "Tornado"});
    Setting tornadoWidth = new Setting("Tornado Width", this, 0.7, 0.5, 1, false);
    Setting tornadoAnimationSpeed = new Setting("Tornado Animation Speed", this, 20, 0, 60, true);
    Setting players = new Setting("Players", this, true);
    Setting mobs = new Setting("Mobs", this, false);
    Setting animals = new Setting("Animals", this, false);
    Setting villager = new Setting("Villager", this, false);
    Setting items = new Setting("Items", this, false);

    private int animationX;

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }

    public void player(EntityLivingBase entity) {
        float red = 0.5F;
        float green = 0.5F;
        float blue = 1F;

        double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks)
                - RenderManager.renderPosX;
        double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks)
                - RenderManager.renderPosY;
        double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks)
                - RenderManager.renderPosZ;

        render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }

    public void render(float red, float green, float blue, double x, double y, double z, float width, float height) {
        RenderUtils.drawEntityESP(x, y, z, width, height, red, green, blue, 0.45F, 0F, 0F, 0F, 1F, 1F);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender) {
            if (((EventRender) event).getType() == EventRender.Type.threeD) {
                switch (mode.getCurrentMode()) {
                    /*case "Prophunt": {
                        doProphunt();
                        break;
                    }*/
                    case "Tornado": {
                        doTornado();
                        break;
                    }
                }
            }
        }

        if (event instanceof EventOutline) {
            switch (mode.getCurrentMode()) {
                case "Shader": {
                    ((EventOutline) event).setOutline(true);
                    break;
                }
            }
        }
    }

    private void doTornado() {

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        if (timeHelper.hasReached((long) tornadoAnimationSpeed.getCurrentValue())) {
            animationX++;
            timeHelper.reset();
        }

        for (Entity entity : getWorld().loadedEntityList) {
            GL11.glPushMatrix();
            int currentOffset = 0;
            float animationY = 0.0f;
            float animationY2 = 0.0f;
            if (entity != getPlayer() && entity instanceof EntityPlayer) {
                translateRotate(entity);
                GL11.glLineWidth(1);
                GL11.glBegin(GL11.GL_LINE_STRIP);

                for (int i = animationX; i < 100 + animationX; i++) {
                    double c = (2 * i * Math.PI / 100);
                    Color color = Rainbow.getRainbow(currentOffset, 1000, 1f, 1f);
                    GL11.glColor3d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                    GL11.glVertex3d((Math.cos(c) * tornadoWidth.getCurrentValue()), animationY,
                            (Math.sin(c) * tornadoWidth.getCurrentValue()));
                    animationY += 0.02f;
                    currentOffset += 10;
                }
                GL11.glEnd();

                GL11.glBegin(GL11.GL_LINE_STRIP);

                for (int i = 50 + animationX; i < 150 + animationX; i++) {
                    double c = (2 * i * Math.PI / 100);
                    Color color = Rainbow.getRainbow(currentOffset, 1000, 1f, 1f);
                    GL11.glColor3d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                    GL11.glVertex3d((Math.cos(c) * tornadoWidth.getCurrentValue()), animationY2,
                            (Math.sin(c) * tornadoWidth.getCurrentValue()));
                    animationY2 += 0.02f;
                    currentOffset += 10;
                }
                GL11.glEnd();
            }
            GL11.glColor3d(1, 1, 1);
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        //GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();

    }

    private void doProphunt() {
        for (Object entity : mc.theWorld.loadedEntityList)
            if (entity instanceof EntityLiving && ((Entity) entity).isInvisible()) {
                double x = ((Entity) entity).posX;
                double y = ((Entity) entity).posY;
                double z = ((Entity) entity).posZ;
                Color color;
                if (mc.thePlayer.getDistanceToEntity((Entity) entity) >= 0.5)
                    color = new Color(1F, 0F, 0F,
                            0.5F - MathHelper.abs(
                                    MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F * (float) Math.PI * 1.0F)
                                            * 0.3F));
                else
                    color = new Color(0, 0, 0, 0);
                RenderUtils.box(x - 0.5, y - 0.1, z - 0.5, x + 0.5, y + 0.9, z + 0.5, color);
            }
    }

    public void translateRotate(Entity entity) {
        double x =
                (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
        double y =
                (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
        double z =
                (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        GL11.glTranslated(x, y, z);
        GL11.glNormal3d(0.0, 0.0, 0.0);
    }
}
