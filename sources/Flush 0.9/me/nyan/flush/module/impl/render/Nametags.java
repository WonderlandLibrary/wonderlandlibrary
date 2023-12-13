package me.nyan.flush.module.impl.render;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventRenderNametags;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Nametags extends Module {
    private final BooleanSetting health = new BooleanSetting("Health", this, true),
            hideVanillaNametags = new BooleanSetting("Hide Vanilla Nametags", this, true),
            autoScale = new BooleanSetting("Auto Scale", this, true),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);
    private final NumberSetting scale = new NumberSetting("Scale", this, 0.3, 0.1, 1, 0.01);

    public Nametags() {
        super("Nametags", Category.RENDER);
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        ArrayList<Entity> entities = mc.theWorld.loadedEntityList.stream()
                .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(entities);

        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase && isValid((EntityLivingBase) entity)) {
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                renderNametag((EntityLivingBase) entity);
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.color(1, 1, 1, 1);
            }
        }
    }

    protected void renderNametag(EntityLivingBase entity) {
        Color color = Color.getHSBColor(entity.getHealth() / entity.getMaxHealth() * 0.36F, 1, 0.9F);

        GlyphPageFontRenderer smallFont = Flush.getFont("GoogleSansDisplay", 18);
        GlyphPageFontRenderer bigFont = Flush.getFont("GoogleSansDisplay", 26);

        float distance = Math.max(mc.thePlayer.getDistanceToEntity(entity) / 10F, 1.2F);
        String distanceText = "Distance: " + Math.round(mc.thePlayer.getDistanceToEntity(entity) * 10) / 10D + "m";
        boolean auto = autoScale.getValue();
        float customScale = this.scale.getValueFloat() / (auto ? 16F : 8F);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
        String name = !EnumChatFormatting.getTextWithoutFormattingCodes(entity.getName()).equals("") ? entity.getName() : "";
        float center = Math.max(bigFont.getStringWidth(name), smallFont.getStringWidth(distanceText)) / 2F + 2 * 2;

        float height = bigFont.getFontHeight() + smallFont.getFontHeight() + (health.getValue() ? 2 : 0);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + entity.height + 0.1F + height * (customScale * (auto ? distance : 1)), z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-customScale * (auto ? distance : 1), -customScale * (auto ? distance : 1),
                -customScale / (auto ? distance : 1));

        Gui.drawRect(-center, 0, center, height, 0x66000000);

        if (health.getValue()) {
            if (!(entity.getMaxHealth() < entity.getHealth())) {
                Gui.drawRect(-center, height - 2,
                        -center + (entity.getHealth() * (center * 2) / entity.getMaxHealth()),
                        height, color.getRGB());
            }
        }
        bigFont.drawString(name, -center + 2, 1, -1);
        smallFont.drawString(distanceText, -center + 2, 14, -1);
        RenderUtils.glColor(-1);

        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderNametags(EventRenderNametags e) {
        if(hideVanillaNametags.getValue() && isValid((EntityLivingBase) e.getEntity()))
            e.cancel();
    }

    public boolean isValid(EntityLivingBase entity) {
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(),
                villagers.getValue(), invisibles.getValue(), ignoreTeam.getValue());
    }
}