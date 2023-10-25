/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventBlockInteract;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class WebTP
extends Feature {
    public static NumberSetting maxBlockReachValue;
    public static BooleanSetting webESP;
    public static BooleanSetting autoDisable;
    public static ColorSetting webESPColor;
    private int x;
    private int y;
    private int z;
    private boolean wasClick;

    public WebTP() {
        super("WebTP", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u043d\u0430 \u0431\u043e\u043b\u044c\u0448\u0438\u0435 \u0440\u0430\u0441\u0441\u0442\u043e\u044f\u043d\u0438\u044f \u0441 \u043f\u043e\u043c\u043e\u0449\u044c\u044e \u043f\u0430\u0443\u0442\u0438\u043d\u044b", Type.Movement);
        maxBlockReachValue = new NumberSetting("Max reach value", 120.0f, 10.0f, 500.0f, 10.0f, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", true, () -> true);
        webESP = new BooleanSetting("Position ESP", true, () -> true);
        webESPColor = new ColorSetting("Color", new Color(0xFFFFFF).getRGB(), webESP::getCurrentValue);
        this.addSettings(maxBlockReachValue, autoDisable, webESP, webESPColor);
    }

    @Override
    public void onDisable() {
        this.x = (int)WebTP.mc.player.posX;
        this.y = (int)WebTP.mc.player.posY;
        this.z = (int)WebTP.mc.player.posZ;
        this.wasClick = false;
        super.onDisable();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (WebTP.mc.player == null || WebTP.mc.world == null) {
            return;
        }
        if (WebTP.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && WebTP.mc.player.isInWeb) {
            Color color = new Color(webESPColor.getColor());
            BlockPos pos = WebTP.mc.objectMouseOver.getBlockPos();
            if (webESP.getCurrentValue()) {
                GlStateManager.pushMatrix();
                RenderHelper.blockEsp(pos, color, true, 1.0, 1.0);
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (WebTP.mc.player == null || WebTP.mc.world == null) {
            return;
        }
        if (this.wasClick && WebTP.mc.player.isInWeb) {
            WebTP.mc.player.onGround = false;
            WebTP.mc.player.motionY *= -12.0;
            WebTP.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.x, this.y + 3, this.z, true));
        } else if (WebTP.mc.player.posX == (double)this.x && WebTP.mc.player.posY == (double)(this.y + 3) && WebTP.mc.player.posZ == (double)this.z) {
            this.wasClick = false;
            if (autoDisable.getCurrentValue()) {
                this.toggle();
            }
        }
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        if (WebTP.mc.player == null || WebTP.mc.world == null) {
            return;
        }
        if (event.getPos() != null) {
            BlockPos pos = event.getPos();
            if (!this.wasClick) {
                this.x = pos.getX();
                this.y = pos.getY();
                this.z = pos.getZ();
                this.wasClick = true;
            }
        }
    }
}

