package org.dreamcore.client.feature.impl.visual;


import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class Animations extends Feature {

    public static NumberSetting speed;
    public static NumberSetting spinSpeed;
    public static BooleanSetting animation;
    public static BooleanSetting itemAnimation = new BooleanSetting("Item Animation", false, () -> true);
    public static BooleanSetting smallItem;
    public static ListSetting swordAnim;
    public static ListSetting itemAnim;

    public static NumberSetting x = new NumberSetting("X", 0, -1, 1, 0.01F, () -> swordAnim.currentMode.equals("Custom"));
    public static NumberSetting y = new NumberSetting("Y", 0, -1, 1, 0.01F, () -> swordAnim.currentMode.equals("Custom"));;
    public static NumberSetting z = new NumberSetting("Z", 0, -1, 1, 0.01F, () -> swordAnim.currentMode.equals("Custom"));;
    public static NumberSetting rotate = new NumberSetting("Rotate 1", 360, -360, 360, 1, () -> swordAnim.currentMode.equals("Custom"));;
    public static NumberSetting rotate2 = new NumberSetting("Rotate 2", 0, -360, 360, 1, () -> swordAnim.currentMode.equals("Custom"));;
    public static NumberSetting rotate3 = new NumberSetting("Rotate 3", 0, -360, 360,1, () -> swordAnim.currentMode.equals("Custom"));;
    public static NumberSetting angle = new NumberSetting("Angle", 0, -50, 100, 1, () -> swordAnim.currentMode.equals("Custom"));;
    public static NumberSetting scale = new NumberSetting("Scale", 1, -10, 10, 0.1F, () -> swordAnim.currentMode.equals("Custom"));;
    public static NumberSetting smooth = new NumberSetting("Smooth", 3, -10, 10, 0.1F, () -> swordAnim.currentMode.equals("Custom"));;

    public Animations() {
        super("Animations", "Добавляет анимацию на меч", Type.Visuals);
        animation = new BooleanSetting("Blocking Animation", false, () -> true);
        speed = new NumberSetting("Smooth Attack", 8, 1, 20, 1, () -> !swordAnim.currentMode.equals("Neutral"));
        spinSpeed = new NumberSetting("Spin Speed", 4, 1, 10, 1, () -> (animation.getBoolValue() && swordAnim.currentMode.equals("Astolfo") || swordAnim.currentMode.equals("Spin")) || (itemAnimation.getBoolValue()));
        smallItem = new BooleanSetting("Mini Item", false, () -> true);
        swordAnim = new ListSetting("Blocking Animation Mode", "dreamcore", () -> animation.getBoolValue(), "dreamcore", "Spin", "Astolfo", "Custom", "Neutral");
        itemAnim = new ListSetting("Item Animation Mode", "Spin", () -> itemAnimation.getBoolValue(), "360", "Spin");
        addSettings(animation, swordAnim, itemAnimation, itemAnim, speed, spinSpeed, x, y, z, rotate, rotate2, rotate3, angle, scale, smooth, smallItem);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(swordAnim.getCurrentMode());
    }
}