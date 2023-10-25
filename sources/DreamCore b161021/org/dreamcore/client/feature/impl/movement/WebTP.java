package org.dreamcore.client.feature.impl.movement;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventBlockInteract;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;

public class WebTP extends Feature {

    public static NumberSetting maxBlockReachValue;
    public static BooleanSetting webESP;
    public static BooleanSetting autoDisable;
    public static ColorSetting webESPColor;

    private int x, y, z;
    private boolean wasClick;

    public WebTP() {
        super("WebTP", "Позволяет телепортироваться на большие расстояния с помощью паутины", Type.Movement);
        maxBlockReachValue = new NumberSetting("Max reach value", 120, 10, 500, 10, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", true, () -> true);
        webESP = new BooleanSetting("Position ESP", true, () -> true);
        webESPColor = new ColorSetting("Color", new Color(0xFFFFFF).getRGB(), webESP::getBoolValue);
        addSettings(maxBlockReachValue, autoDisable, webESP, webESPColor);
    }

    @Override
    public void onDisable() {
        x = (int) mc.player.posX;
        y = (int) mc.player.posY;
        z = (int) mc.player.posZ;
        wasClick = false;
        super.onDisable();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null)
            return;
        if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.player.isInWeb) {
            Color color = new Color(webESPColor.getColorValue());
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            if (webESP.getBoolValue()) {
                GlStateManager.pushMatrix();
                RenderHelper.blockEsp(pos, color, true);
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null)
            return;
        if (wasClick && mc.player.isInWeb) {
            mc.player.onGround = false;
            mc.player.motionY *= -12;
            mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 3, z, true));
        } else if (mc.player.posX == x && mc.player.posY == y + 3 && mc.player.posZ == z) {
            wasClick = false;
            if (autoDisable.getBoolValue()) {
                toggle();
            }
        }
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        if (mc.player == null || mc.world == null)
            return;
        if (event.getPos() != null) {
            BlockPos pos = event.getPos();
            if (!wasClick) {
                x = pos.getX();
                y = pos.getY();
                z = pos.getZ();
                wasClick = true;
            }
        }
    }
}
