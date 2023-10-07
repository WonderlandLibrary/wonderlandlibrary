package com.ohare.client.module.modules.visuals;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.ohare.client.Client;
import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.combat.KillAura;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.NumberValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

/**
 * made by Xen for OhareWare
 *
 * @since 6/10/2019
 **/
public class Tracers extends Module {
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    private NumberValue<Float> width = new NumberValue<>("Width", 1f, 0.1f, 10f,1.0F);

    public Tracers() {
        super("Tracers", Category.VISUALS, new Color(0xFF71EE6D).getRGB());
        setDescription("Lines to players");
        addValues(players,animals,mobs,invisibles,passives,width);
        setHidden(true);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule("killaura");
        for (EntityPlayer entity : mc.theWorld.playerEntities) {
            if (isValid(entity)) {
                trace(entity, width.getValue(), Client.INSTANCE.getFriendManager().isFriend(entity.getName()) ? new Color(32, 128, 255) : ((killAura.target != null && killAura.target == entity) ? new Color(255, 26, 26,255):new Color(255,255,255)), Minecraft.getMinecraft().timer.renderPartialTicks);
            }
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        return mc.thePlayer != entity && isValidType(entity)&& entity != mc.thePlayer.ridingEntity&& entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    private void trace(Entity entity, float width, Color color, float partialTicks) {
        /* Setup separate path rather than changing everything */
        float r = ((float) 1 / 255) * color.getRed();
        float g = ((float) 1 / 255) * color.getGreen();
        float b = ((float) 1 / 255) * color.getBlue();
        GL11.glPushMatrix();

        /* Load custom identity */
        GL11.glLoadIdentity();

        /* Set the camera towards the partialTicks */
        mc.entityRenderer.orientCamera(partialTicks);

        /* PRE */
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);

        /* Keep it AntiAliased */
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        /* Interpolate needed X, Y, Z files */
        double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks) - mc.getRenderManager().viewerPosX;
        double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks) - mc.getRenderManager().viewerPosY;
        double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks) - mc.getRenderManager().viewerPosZ;



        /* Setup line width */
        GL11.glLineWidth(width);

        /* Drawing */
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glColor3d(r, g, b);
            GL11.glVertex3d(x, y, z);
            GL11.glVertex3d(0.0, mc.thePlayer.getEyeHeight(), 0.0);
            GL11.glEnd();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);

            /* POST */
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);

            /* End the custom path */
            GL11.glPopMatrix();
        }
    }
}
