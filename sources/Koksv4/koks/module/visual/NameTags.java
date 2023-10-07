package koks.module.visual;

import koks.Koks;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RenderUtil;
import koks.api.manager.value.annotation.Value;
import koks.event.Render3DEvent;
import koks.module.combat.Teams;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.optifine.util.MathUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Copyright 1337, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NameTags", description = "You can see nametags from a big distance", category = Module.Category.VISUAL)
public class NameTags extends Module {

    @Value(name = "ScaleMin", displayName = "ScaleMin", minimum = 0.1, maximum = 1)
    double scaleMin = 0.5;

    @Value(name = "ScaleMax", displayName = "ScaleMax", minimum = 0.1, maximum = 2)
    double scaleMax = 1.5;

    @Value(name = "BAC")
    boolean bac = true;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RenderUtil renderUtil = RenderUtil.getInstance();
        if (event instanceof final Render3DEvent render3DEvent) {
            final float partialTicks = render3DEvent.getRenderPartialTicks();

            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (isValid(entity)) {

                    final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - getRenderManager().renderPosX, y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - getRenderManager().renderPosY, z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - getRenderManager().renderPosZ;

                    final double yAxis = 2.8;

                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y + yAxis, z);
                    GL11.glScalef(-0.06F, -0.06F, -0.06F);

                    GL11.glRotated(-getRenderManager().playerViewY, 0, 1, 0);
                    GL11.glRotated(getRenderManager().playerViewX, 1, 0, 0);


                    final Teams teams = ModuleRegistry.getModule(Teams.class);
                    final boolean isFriend = entity instanceof EntityPlayer && Koks.getKoks().friendManager.isFriend(getName((EntityPlayer) entity));
                    final boolean isBadlion = bac && entity.getDisplayName().getUnformattedText().contains("✔");
                    final boolean isHurensohnPlus = entity.getName().equalsIgnoreCase("AddictedToMyHoe") || entity.getName().equalsIgnoreCase("Beaxn") || entity.getName().equalsIgnoreCase("acabrocky") || entity.getName().equalsIgnoreCase("CantForgetUHC");
                    final boolean isTeam = entity instanceof EntityPlayer && teams.isToggled() && isTeam(getPlayer(), (EntityPlayer) entity);

                    StringBuilder prefix = new StringBuilder();

                    if (isBadlion || isHurensohnPlus) {
                        prefix.append("§cHurensohn").append(isHurensohnPlus ? "+" : "").append(", ");
                    }

                    if (isFriend) {
                        prefix.append("§bFriend, ");
                    }

                    if (isTeam) {
                        prefix.append("§aTeam, ");
                    }

                    final String formattedPrefix = prefix.length() > 2 ? "§7[" + prefix.substring(0, prefix.length() - 2) + "§7] " : "";

                    if (entity instanceof final EntityPlayer player) {
                        final String name = formattedPrefix + entity.getDisplayName().getFormattedText().replace(getName((EntityPlayer) entity), player.hurtTime != 0 ? "§c" : "") + getName((EntityPlayer) entity);
                        float finalHealth = Float.isNaN(((EntityLivingBase) entity).getHealth()) ? -1 : Math.round(((EntityLivingBase) entity).getHealth() * 5);
                        final String colorPrefix = finalHealth == -1 ? "" : finalHealth >= 80 ? "§a" : finalHealth >= 60 ? "§e" : finalHealth >= 40 ? "§6" : finalHealth >= 20 ? "§c" : "§4";
                        final String health = finalHealth == -1 ? " §cNaN" : " " + colorPrefix + Math.round(finalHealth) + "%";
                        final String tagText = name + " " + health;

                        final float width = mc.fontRendererObj.getStringWidth(tagText);

                        final float scaleAutism = (float) (1.6 * Math.abs(mc.thePlayer.getDistanceToEntity(entity) / 25));

                        final double clamped = MathHelper.clamp_float(scaleAutism, (float) scaleMin, (float) scaleMax);

                        GlStateManager.pushMatrix();
                        GlStateManager.enableDepth();
                        GL11.glScaled(clamped, clamped, clamped);
                        GlStateManager.disableDepth();

                        RenderUtil.getInstance().drawRect(-width / 2 - 5, -2, width / 2 + 5, mc.fontRendererObj.FONT_HEIGHT + 2, Integer.MIN_VALUE);
                        mc.fontRendererObj.drawCenteredString(mc.fontRendererObj, tagText, 0, 0, -1);

                        GlStateManager.popMatrix();
                        GlStateManager.enableDepth();
                    }
                    GL11.glPopMatrix();
                }
            }
        }
    }

    public boolean isValid(Entity entity) {
        if (!(entity instanceof EntityPlayer))
            return false;
        if (entity == getPlayer() && getGameSettings().thirdPersonView == 0)
            return false;
        if (!isValidEntityName(entity))
            return false;
        if (entity.isInvisible())
            return false;
        return entity.ticksExisted >= 10;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
