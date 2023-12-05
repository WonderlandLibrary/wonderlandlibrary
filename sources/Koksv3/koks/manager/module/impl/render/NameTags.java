package koks.manager.module.impl.render;

import koks.Koks;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventRender3D;
import koks.manager.event.impl.EventTick;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 21:40
 */

@ModuleInfo(name = "NameTags", description = "Its show the name tags from other players", category = Module.Category.RENDER)
public class NameTags extends Module {

    public Setting bac = new Setting("Show BAC", true, this);
    public Setting bacBrandmark = new Setting("BAC Brandmark", "&cHURENSOHN", this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventTick) {
            bacBrandmark.setTyped(bacBrandmark.getTyped().replace("§", "&"));
        }
        if (event instanceof EventRender3D) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (isValid(entity)) {

                    double x = entity.prevPosX + (entity.posX - entity.prevPosX) * ((EventRender3D) event).getPartialTicks() - mc.getRenderManager().renderPosX;
                    double y = entity.prevPosY + (entity.posY - entity.prevPosY) * ((EventRender3D) event).getPartialTicks() - mc.getRenderManager().renderPosY;
                    double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * ((EventRender3D) event).getPartialTicks() - mc.getRenderManager().renderPosZ;

                    // Change this Value to change the size of the Tag
                    double defaultScale = 0.015;



                    double scale = mc.thePlayer.getDistanceToEntity(entity) / 600;

                    double height = (defaultScale + scale) * 75;

                    boolean isFriend = Koks.getKoks().friendManager.isFriend(entity.getName());
                    boolean isHurensohn = bac.isToggled() && entity.getDisplayName().getUnformattedText().contains("✔");
                    String name = (isHurensohn ? "§7[" + bacBrandmark.getTyped().replace("&", "§") + "§7] " : "") + (isFriend ? "§bFriend §7| " : "") + entity.getDisplayName().getFormattedText();
                    float finalHealth = Float.isNaN(((EntityLivingBase) entity).getHealth()) ? -1 : Math.round(((EntityLivingBase) entity).getHealth() * 5);
                    String colorPrefix = finalHealth == -1 ? "" : (finalHealth >= 80 ? "§a" : finalHealth < 80 && finalHealth >= 60 ? "§e" : finalHealth < 60 && finalHealth >= 40 ? "§6" : finalHealth < 40 && finalHealth >= 20 ? "§c" : finalHealth < 20 ? "§4" : "§f");
                    String health = finalHealth == -1 ? " §cNaN" : " " + colorPrefix + Math.round(finalHealth) + "%";
                    String tagText = name + " " + health;

                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y + 2.1 - height, z);
                    GL11.glScaled(-(defaultScale + scale), -(defaultScale + scale), -(defaultScale + scale));
                    GL11.glRotated(mc.getRenderManager().playerViewY, 0, -1, 0);
                    GlStateManager.disableDepth();

                    Gui.drawRect(-mc.fontRendererObj.getStringWidth(tagText) / 2 - 2, -92, mc.fontRendererObj.getStringWidth(tagText) / 2 + 2, -80, Integer.MIN_VALUE);
                    mc.fontRendererObj.drawString(tagText, -mc.fontRendererObj.getStringWidth(tagText) / 2, -90, 0xFFFFFFFF);

                    GlStateManager.enableDepth();
                    GL11.glPopMatrix();
                }
            }
        }
    }

    public boolean isValid(Entity entity) {
        if (!(entity instanceof EntityPlayer))
            return false;
        if (entity == mc.thePlayer)
            return false;
        if (!validEntityName(entity))
            return false;
        if (entity.isInvisible())
            return false;
        if (entity.ticksExisted < 10)
            return false;
        return true;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}