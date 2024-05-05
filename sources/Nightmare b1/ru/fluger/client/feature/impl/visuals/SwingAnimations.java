// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.events.impl.player.EventTransformSideFirstPerson;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.feature.impl.combat.KillAura;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class SwingAnimations extends Feature
{
    public static boolean blocking;
    public static ListSetting swordAnim;
    public static BooleanSetting auraOnly;
    public static BooleanSetting item360;
    public static NumberSetting item360Speed;
    public static ListSetting item360Mode;
    public static ListSetting item360Hand;
    public static NumberSetting rightx1;
    public static NumberSetting righty2;
    public static NumberSetting rightz3;
    public static NumberSetting swingSpeed;
    public static NumberSetting spinSpeed;
    
    public SwingAnimations() {
        super("SwingAnimations", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044e \u043d\u0430 \u043c\u0435\u0447", Type.Visuals);
        SwingAnimations.swordAnim = new ListSetting("Animation Mode", "VanePremium", () -> true, new String[] { "VanePremium", "Spin", "Default", "Glide" });
        SwingAnimations.auraOnly = new BooleanSetting("Only Aura", false, () -> true);
        SwingAnimations.swingSpeed = new NumberSetting("Swing Speed", "\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0443\u0434\u0430\u0440\u0430 \u043c\u0435\u0447\u0430", 8.0f, 1.0f, 20.0f, 1.0f, () -> true);
        SwingAnimations.rightx1 = new NumberSetting("RightX", 1.3f, -2.0f, 2.0f, 0.1f, () -> true);
        SwingAnimations.righty2 = new NumberSetting("RightY", -1.0f, -2.0f, 2.0f, 0.1f, () -> true);
        SwingAnimations.rightz3 = new NumberSetting("RightZ", -1.5f, -2.0f, -0.4f, 0.1f, () -> true);
        SwingAnimations.spinSpeed = new NumberSetting("Spin Speed", 4.0f, 1.0f, 10.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Spin"));
        SwingAnimations.item360 = new BooleanSetting("Item360", false, () -> true);
        SwingAnimations.item360Mode = new ListSetting("Item360 Mode", "Horizontal", () -> SwingAnimations.item360.getCurrentValue(), new String[] { "Horizontal", "Vertical", "Zoom" });
        SwingAnimations.item360Hand = new ListSetting("Item360 Hand", "All", () -> SwingAnimations.item360.getCurrentValue(), new String[] { "All", "Left", "Right" });
        SwingAnimations.item360Speed = new NumberSetting("Item360 Speed", 8.0f, 1.0f, 15.0f, 1.0f, () -> SwingAnimations.item360.getCurrentValue());
        this.addSettings(SwingAnimations.auraOnly, SwingAnimations.swordAnim, SwingAnimations.spinSpeed, SwingAnimations.swingSpeed, SwingAnimations.rightx1, SwingAnimations.righty2, SwingAnimations.rightz3, SwingAnimations.item360, SwingAnimations.item360Mode, SwingAnimations.item360Hand, SwingAnimations.item360Speed);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final String anim = SwingAnimations.swordAnim.getOptions();
        SwingAnimations.blocking = (Fluger.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null);
        this.setSuffix(anim);
    }
    
    @EventTarget
    public void onSidePerson(final EventTransformSideFirstPerson event) {
        if (SwingAnimations.blocking && event.getEnumHandSide() == vo.b) {
            bus.b(0.29, 0.1, -0.31);
        }
    }
}
