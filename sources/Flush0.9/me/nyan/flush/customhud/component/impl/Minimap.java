package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.BooleanSetting;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class Minimap extends Component {
    private BooleanSetting players, creatures, villagers, invisibles, ignoreTeam;

    @Override
    public void onAdded() {
        settings.add(players = new BooleanSetting("Players", true));
        settings.add(creatures = new BooleanSetting("Creatures", false));
        settings.add(villagers = new BooleanSetting("Villagers", false));
        settings.add(invisibles = new BooleanSetting("Invisibles", false));
        settings.add(ignoreTeam = new BooleanSetting("Ignore Team", false, () -> players.getValue()));
    }

    @Override
    public void draw(float x, float y) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(1 / scaleX, 1 / scaleY, 0);
        Gui.drawRect(x * scaleX, y * scaleY, (x + width()) * scaleX, (y + height()) * scaleY, 0x88000000);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(x * scaleX, y * scaleY, (x + width()) * scaleX, (y + height()) * scaleY);
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase) || !isValid((EntityLivingBase) entity)) {
                continue;
            }

            float distance = mc.thePlayer.getDistanceToEntity(entity);
            double distanceX = entity.posX - mc.thePlayer.posX;
            double distanceZ = entity.posZ - mc.thePlayer.posZ;
            double cos = Math.cos(Math.toRadians(180 - mc.thePlayer.rotationYaw));
            double sin = Math.sin(Math.toRadians(180 - mc.thePlayer.rotationYaw));
            double centerX = x * scaleX + (width() * scaleX / 2D);
            double centerY = y * scaleY + (height() * scaleY / 2D);
            double circleX = centerX + distanceX * cos - distanceZ * sin;
            double circleY = centerY + distanceX * sin + distanceZ * cos;

            float radius = Math.max(Math.min(width() / distance / 5F, 3F), 1);
            RenderUtils.fillCircle((float) circleX, (float) circleY, radius, 0xFFFF0000);
            RenderUtils.drawCircle((float) circleX, (float) circleY, radius, 0xFF220000, 0.5F);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(0.5F);
        RenderUtils.drawLine((x + width() / 2F) * scaleX, y * scaleY, (x + width() / 2F) * scaleX, (y + height()) * scaleY, -1);
        RenderUtils.drawLine(x * scaleX, (y + height() / 2F) * scaleY, (x + width()) * scaleX, (y + height() / 2F) * scaleY, -1);

        GlStateManager.popMatrix();
    }

    public boolean isValid(EntityLivingBase entity) {
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(),
                villagers.getValue(), invisibles.getValue(), ignoreTeam.getValue());
    }

    @Override
    public int width() {
        return 100;
    }

    @Override
    public int height() {
        return 100;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
