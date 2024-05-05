package rosegoldclient.features;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import rosegoldclient.Main;
import rosegoldclient.events.PacketSentEvent;
import rosegoldclient.utils.*;

import java.awt.*;
import java.util.List;

public class TargetHUD {

    private EntityLivingBase entity;
    private EntityLivingBase entityToRender;
    private float lastHp = 0.8f;
    private final MilliTimer timer = new MilliTimer();
    protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

    @SubscribeEvent
    public void onPacket(PacketSentEvent e) {
        if (e.packet instanceof CPacketUseEntity && ((CPacketUseEntity) e.packet).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) e.packet).getEntityFromWorld(Main.mc.world) != null && ((CPacketUseEntity) e.packet).getEntityFromWorld(Main.mc.world) instanceof EntityLivingBase) {
            timer.updateTime();
            entity = (EntityLivingBase) ((CPacketUseEntity) e.packet).getEntityFromWorld(Main.mc.world);
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (timer.hasTimePassed(1500L)) {
            entity = null;
        }
        if (KillAura.target != null) {
            entity = KillAura.target;
        }
        if (SpellAura.target != null) {
            entity = SpellAura.target;
        }
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (entity != null) {
            if(entity instanceof EntityArmorStand) {
                List<Entity> possibleEntities = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(1, 3, 1), entity -> (!(entity instanceof EntityArmorStand) && !(entity instanceof EntityPlayerSP) && (entity instanceof EntityLivingBase)));
                if (!possibleEntities.isEmpty()) {
                    entityToRender = (EntityLivingBase) possibleEntities.get(0);
                }
            } else {
                entityToRender = entity;
            }
            int scale = Main.mc.gameSettings.guiScale;
            GL11.glPushMatrix();
            ScaledResolution resolution = new ScaledResolution(Main.mc);
            int x = (int) ((-resolution.getScaledWidth()) * (Main.configFile.targetHUDX / 100.0));
            int y = (int) ((-resolution.getScaledHeight()) * (Main.configFile.targetHUDY / 100.0));
            GL11.glTranslatef(x, y, 0.0f);
            RenderUtils.drawRoundRect2(resolution.getScaledWidth() - 170, resolution.getScaledHeight() - 70, 200.0f, 50.0f, 3.0f, new Color(21, 21, 21, 52).getRGB());
            Fonts.defaultFont.drawString(entity.getCustomNameTag(), (resolution.getScaledWidth() - 165) + 0.4f, (resolution.getScaledHeight() - 64) + 0.5f, new Color(255, 255, 255).getRGB(), true);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Main.mc.getTextureManager().bindTexture(inventoryBackground);
            GL11.glTranslatef(resolution.getScaledWidth() - 167, resolution.getScaledHeight() - 63 + Fonts.defaultFont.FONT_HEIGHT, 0.0f);
            GL11.glTranslatef(-resolution.getScaledWidth() + 167, -resolution.getScaledHeight() + 63 - Fonts.defaultFont.FONT_HEIGHT, 0.0f);
            try {
                entityToRender.posY += 1000.0;
                GuiInventory.drawEntityOnScreen(resolution.getScaledWidth() + 10, resolution.getScaledHeight() - 33, (int) (35.0 / Math.max(entityToRender.height, 1.5)), 20.0f, 10.0f, entityToRender);
                entityToRender.posY -= 1000.0;
            } catch (Exception ignored) {}
            Fonts.defaultFont.drawString(Math.round(entity.getDistance(Main.mc.player) * 10.0f) / 10.0 + "m", resolution.getScaledWidth() - 165, resolution.getScaledHeight() - 59 + Fonts.openSans.FONT_HEIGHT * 2, new Color(231, 231, 231).getRGB(), true);
            float hp = (double) Math.abs(entity.getHealth() / entity.getMaxHealth() - lastHp) < 0.01 ? entity.getHealth() / entity.getMaxHealth() : (float) Math.min((double) lastHp + (entity.getHealth() / entity.getMaxHealth() > lastHp ? 0.01 : -0.01), 1.0);
            String text = String.format("%s", (int) (Math.min(entity.getHealth() / entity.getMaxHealth(), 1.0f) * 100.0f) + "%");
            RenderUtils.drawRoundRect(resolution.getScaledWidth() - 160 + 130, resolution.getScaledHeight() - 30, resolution.getScaledWidth() - 110, resolution.getScaledHeight() - 26, 1.0f, Color.HSBtoRGB(0.0f, 0.0f, 0.1f));
            RenderUtils.drawRoundRect((float)(resolution.getScaledWidth() - 160) + 130.0f * hp, resolution.getScaledHeight() - 30, resolution.getScaledWidth() - 110, resolution.getScaledHeight() - 26, 1.0f, Color.HSBtoRGB(0.0f, 0.0f, 0.1f));
            Fonts.defaultFont.drawString(text, (float) ((double)((int)((double)(resolution.getScaledWidth() - 170) + 75.0)) - Fonts.defaultFont.getStringWidth(text) / 2.0), resolution.getScaledHeight() - 31 - Fonts.openSans.FONT_HEIGHT, new Color(231, 231, 231).getRGB(), true);
            lastHp = hp;
            Main.mc.gameSettings.guiScale = scale;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }
}
