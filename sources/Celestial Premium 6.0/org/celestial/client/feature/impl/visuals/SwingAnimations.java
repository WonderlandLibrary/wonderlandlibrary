/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class SwingAnimations
extends Feature {
    public static BooleanSetting auraOnly;
    public static NumberSetting smooth;
    public static NumberSetting spinSpeed;
    public static BooleanSetting smallItem;
    public static NumberSetting smallItemSize;
    public static BooleanSetting item360;
    public static NumberSetting item360Speed;
    public static ListSetting swordAnim;
    public static ListSetting item360Mode;
    public static ListSetting item360Hand;
    public static NumberSetting x;
    public static NumberSetting y;
    public static NumberSetting z;
    public static NumberSetting rotate1;
    public static NumberSetting rotate2;
    public static NumberSetting rotate3;
    public static NumberSetting angle;
    public static NumberSetting SwingX;
    public static NumberSetting SwingY;
    public static NumberSetting SwingZ;
    public static NumberSetting SwingRotate1;
    public static NumberSetting SwingRotate2;
    public static NumberSetting SwingRotate3;
    public static NumberSetting SwingAngle;
    public static NumberSetting scale;
    public static NumberSetting fapSmooth;

    public SwingAnimations() {
        super("SwingAnimations", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044e \u043d\u0430 \u0440\u0443\u043a\u0443", Type.Visuals);
        auraOnly = new BooleanSetting("Aura Only", true, () -> true);
        smooth = new NumberSetting("Swing Smooth", 8.0f, 1.0f, 20.0f, 1.0f, () -> !SwingAnimations.swordAnim.currentMode.equals("Neutral"));
        spinSpeed = new NumberSetting("Spin Speed", 4.0f, 1.0f, 10.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Astolfo") || SwingAnimations.swordAnim.currentMode.equals("Spin"));
        smallItem = new BooleanSetting("Mini Item", false, () -> !SwingAnimations.swordAnim.currentMode.equals("Custom"));
        smallItemSize = new NumberSetting("Mini Item Size", 0.7f, 0.1f, 0.9f, 0.1f, () -> !SwingAnimations.swordAnim.currentMode.equals("Custom") && smallItem.getCurrentValue());
        item360 = new BooleanSetting("Item360", false, () -> true);
        item360Mode = new ListSetting("Item360 Mode", "Horizontal", () -> item360.getCurrentValue(), "Horizontal", "Vertical", "Zoom");
        item360Hand = new ListSetting("Item360 Hand", "All", () -> item360.getCurrentValue(), "All", "Left", "Right");
        item360Speed = new NumberSetting("Item360 Speed", 5.0f, 1.0f, 15.0f, 1.0f, () -> item360.getCurrentValue());
        swordAnim = new ListSetting("Sword Animation", "Celestial", () -> true, "Celestial", "Spin", "Swank", "Sigma", "Jello", "Fap", "Big", "Astolfo", "Neutral", "Custom");
        fapSmooth = new NumberSetting("Fap Smooth", 5.0f, 0.5f, 10.0f, 0.5f, () -> SwingAnimations.swordAnim.currentMode.equals("Fap"));
        x = new NumberSetting("X", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        y = new NumberSetting("Y", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        z = new NumberSetting("Z", -1.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        rotate1 = new NumberSetting("Rotate 1", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        rotate2 = new NumberSetting("Rotate 2", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        rotate3 = new NumberSetting("Rotate 3", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        angle = new NumberSetting("Angle", 50.0f, 0.0f, 360.0f, 5.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingX = new NumberSetting("Swing X", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingY = new NumberSetting("Swing Y", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingZ = new NumberSetting("Swing Z", -1.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingRotate1 = new NumberSetting("Swing Rotate 1", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingRotate2 = new NumberSetting("Swing Rotate 2", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingRotate3 = new NumberSetting("Swing Rotate 3", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAngle = new NumberSetting("Swing angle", 50.0f, 0.0f, 360.0f, 5.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        scale = new NumberSetting("Scale", 1.0f, 0.1f, 5.0f, 0.1f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        this.addSettings(auraOnly, swordAnim, fapSmooth, x, y, z, rotate1, rotate2, rotate3, angle, SwingX, SwingY, SwingZ, SwingRotate1, SwingRotate2, SwingRotate3, SwingAngle, scale, smooth, spinSpeed, smallItem, smallItemSize, item360, item360Mode, item360Hand, item360Speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(swordAnim.getCurrentMode());
    }
}

