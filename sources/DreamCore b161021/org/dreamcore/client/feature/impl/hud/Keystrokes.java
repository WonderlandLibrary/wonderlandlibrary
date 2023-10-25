package org.dreamcore.client.feature.impl.hud;

import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;

import java.awt.*;

public class Keystrokes extends Feature {

    public int lastA = 0;
    public int lastW = 0;
    public int lastS = 0;
    public int lastD = 0;
    public long deltaAnim;

    public Keystrokes() {
        super("Keystrokes", "Показывает нажатые клавиши", Type.Hud);
        deltaAnim = 0;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        boolean A = mc.gameSettings.keyBindLeft.pressed;
        boolean W = mc.gameSettings.keyBindForward.pressed;
        boolean S = mc.gameSettings.keyBindBack.pressed;
        boolean D = mc.gameSettings.keyBindRight.pressed;
        int alphaA = A ? 255 : 0;
        int alphaW = W ? 255 : 0;
        int alphaS = S ? 255 : 0;
        int alphaD = D ? 255 : 0;
        float diff;

        if (lastA != alphaA) {
            diff = alphaA - lastA;
            lastA = (int) (lastA + diff / 40);
        }

        if (lastW != alphaW) {
            diff = alphaW - lastW;
            lastW = (int) (lastW + diff / 40);
        }

        if (lastS != alphaS) {
            diff = alphaS - lastS;
            lastS = (int) (lastS + diff / 40);
        }

        if (lastD != alphaD) {
            diff = alphaD - lastD;
            lastD = (int) (lastD + diff / 40);
        }


        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(5.0F, 49.0F, 25.0F, 69.0F, (new Color(lastA, lastA, lastA, 150)).getRGB());
        } else {
            dreamcore.instance.blurUtil.blur(5.0F, 49.0F, 25.0F, 69.0F, 30);
        }
        mc.fontRenderer.drawCenteredString("A", 15.0F, 56.0F, ClientHelper.getClientColor().getRGB());

        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(27.0F, 27.0F, 47.0F, 47.0F, (new Color(lastW, lastW, lastW, 150)).getRGB());
        } else {
            dreamcore.instance.blurUtil.blur(27.0F, 27.0F, 47.0F, 47.0F, 30);
        }
        mc.fontRenderer.drawCenteredString("W", 37.0F, 34.0F, ClientHelper.getClientColor().getRGB());

        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(27.0F, 49.0F, 47.0F, 69.0F, (new Color(lastS, lastS, lastS, 150)).getRGB());
        } else {
            dreamcore.instance.blurUtil.blur(27.0F, 49.0F, 47.0F, 69.0F, 30);
        }
        mc.fontRenderer.drawCenteredString("S", 37.0F, 56.0F, ClientHelper.getClientColor().getRGB());
        if (!HUD.blur.getBoolValue()) {
            RectHelper.drawRect(49.0F, 49.0F, 69.0F, 69.0F, (new Color(lastD, lastD, lastD, 150)).getRGB());
        } else {
            dreamcore.instance.blurUtil.blur(49.0F, 49.0F, 69.0F, 69.0F, 30);
        }

        mc.fontRenderer.drawCenteredString("D", 59.0F, 56.0F, ClientHelper.getClientColor().getRGB());
    }
}
