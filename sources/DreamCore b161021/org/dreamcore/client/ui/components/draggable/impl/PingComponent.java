package org.dreamcore.client.ui.components.draggable.impl;

import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.impl.hud.HUD;
import org.dreamcore.client.feature.impl.misc.Disabler;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.world.EntityHelper;
import org.dreamcore.client.ui.components.draggable.DraggableModule;

public class PingComponent extends DraggableModule {

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
        if (mc.player != null && mc.world != null) {
            int ping = dreamcore.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : mc.isSingleplayer() ? 0 : EntityHelper.getPing(mc.player);
            if (HUD.font.currentMode.equals("Minecraft")) {
                ClientHelper.getFontRender().drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
            } else {
                mc.fontRendererObj.drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (mc.player != null && mc.world != null) {
            int ping = dreamcore.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : mc.isSingleplayer() ? 0 : EntityHelper.getPing(mc.player);
            if (HUD.font.currentMode.equals("Minecraft")) {
                ClientHelper.getFontRender().drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
            } else {
                mc.fontRendererObj.drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
            }
        }
        super.draw();
    }
}