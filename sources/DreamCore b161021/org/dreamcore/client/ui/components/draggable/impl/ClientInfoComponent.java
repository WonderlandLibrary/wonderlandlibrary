package org.dreamcore.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.impl.hud.HUD;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.ui.components.draggable.DraggableModule;
import org.dreamcore.security.utils.LicenseUtil;

public class ClientInfoComponent extends DraggableModule {

    public ClientInfoComponent() {
        super("ClientInfoComponent", sr.getScaledWidth() - mc.robotoRegularFontRender.getStringWidth(dreamcore.instance.type + " - " + ChatFormatting.WHITE + dreamcore.instance.version + ChatFormatting.RESET + " - " + "Ya tut kryaknul pomenyay potom"), sr.getScaledHeight() - 20);
    }

    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (HUD.clientInfo.getBoolValue()) {

            String buildStr = dreamcore.instance.type + " - " + ChatFormatting.WHITE + dreamcore.instance.version + ChatFormatting.RESET + " - " + "Ya tut kryaknul pomenyay potom";
            if (mc.player != null && mc.world != null) {
                mc.robotoRegularFontRender.drawStringWithShadow(buildStr, getX(), getY(), ClientHelper.getClientColor().getRGB());

            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.clientInfo.getBoolValue()) {

            String buildStr = dreamcore.instance.type + " - " + ChatFormatting.WHITE + dreamcore.instance.version + ChatFormatting.RESET + " - " + "Ya tut kryaknul pomenyay potom";
            if (mc.player != null && mc.world != null) {
                mc.robotoRegularFontRender.drawStringWithShadow(buildStr, getX(), getY(), ClientHelper.getClientColor().getRGB());
            }
        }
        super.draw();
    }
}