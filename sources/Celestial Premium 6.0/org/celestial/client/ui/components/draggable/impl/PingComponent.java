/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.hud.ClientFont;
import org.celestial.client.feature.impl.misc.Disabler;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class PingComponent
extends DraggableModule {
    public PingComponent() {
        super("PingComponent", 100, 300);
    }

    @Override
    public int getWidth() {
        return 55;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.mc.player != null && this.mc.world != null) {
            int ping;
            int n = Celestial.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : (ping = this.mc.isSingleplayer() ? 0 : EntityHelper.getPing(this.mc.player));
            if (!ClientFont.minecraftFont.getCurrentValue()) {
                ClientHelper.getFontRender().drawStringWithShadow("Ping: \u00a77" + ping + "ms", this.getX(), this.getY(), -1);
            } else {
                this.mc.fontRendererObj.drawStringWithShadow("Ping: \u00a77" + ping + "ms", this.getX(), this.getY(), -1);
            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (this.mc.player != null && this.mc.world != null) {
            int ping;
            int n = Celestial.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : (ping = this.mc.isSingleplayer() ? 0 : EntityHelper.getPing(this.mc.player));
            if (!ClientFont.minecraftFont.getCurrentValue()) {
                ClientHelper.getFontRender().drawStringWithShadow("Ping: \u00a77" + ping + "ms", this.getX(), this.getY(), -1);
            } else {
                this.mc.fontRendererObj.drawStringWithShadow("Ping: \u00a77" + ping + "ms", this.getX(), this.getY(), -1);
            }
        }
        super.draw();
    }
}

