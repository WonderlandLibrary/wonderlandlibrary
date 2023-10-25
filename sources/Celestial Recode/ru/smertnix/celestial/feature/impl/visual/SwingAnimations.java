package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventTransformSideFirstPerson;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class SwingAnimations extends Feature {
    public static boolean blocking;
    public static ListSetting swordAnim;
    public static BooleanSetting auraOnly;
    public static BooleanSetting item360;
    public static NumberSetting item360Speed;
    public static ListSetting item360Hand;
    public static NumberSetting rightx1;
    public static NumberSetting righty2;
    public static NumberSetting rightz3;
    public static NumberSetting rightx;
    public static NumberSetting righty;
    public static NumberSetting rightz;
    public static NumberSetting swingSpeed;
    public static NumberSetting swingStrength;
    public static NumberSetting spinSpeed;

    public SwingAnimations() {
        super("Swing Animations", "Разные анимации на руку", FeatureCategory.Render);
        swordAnim = new ListSetting("Mode", "Smooth Translate", () -> true, "Smooth Translate", "Jump Translate", "Smooth", "Default", "Big");
        auraOnly = new BooleanSetting("Only Aura", false, () -> true);
        swingSpeed = new NumberSetting("Swing Speed", "", 1.0F, 1.0F, 5.0F, 1, () -> true);
        swingStrength = new NumberSetting("Swing Strength", "�������� ����� ����", 10.0F, 1.0F, 100.0F, 1, () -> true);
        rightx1 = new NumberSetting("RightX", 0, -2, 2, 0.1F, () -> true);
        righty2 = new NumberSetting("RightY", 0.2F, -2, 2, 0.1F, () -> true);
        rightz3 = new NumberSetting("RightZ", -2, -2, -0.4f, 0.1F, () -> true);
        rightx = new NumberSetting("LeftX", 0, -2, 2, 0.1F, () -> true);
        righty = new NumberSetting("LeftY", 0.2F, -2, 2, 0.1F, () -> true);
        rightz = new NumberSetting("leftZ", -2, -2, -0.4f, 0.1F, () -> true);
        item360 = new BooleanSetting("Item360", false, () -> true);
        item360Hand = new ListSetting("Hands", "All", () -> item360.getBoolValue(), "All", "Left", "Right");
        item360Speed = new NumberSetting("Speed", 3, 1, 5, 1, () -> item360.getBoolValue());
        addSettings(swordAnim, spinSpeed, swingStrength, swingSpeed, rightx1, righty2, rightz3,  rightx, righty, rightz, item360, item360Hand, item360Speed, auraOnly);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String anim = swordAnim.getOptions();
        blocking = Celestial.instance.featureManager.getFeature(AttackAura.class).isEnabled() && AttackAura.target != null;
        this.setSuffix(anim);

    }

    @EventTarget
    public void onSidePerson(EventTransformSideFirstPerson event) {
        if (blocking) {
            if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(0.29, 0.10, -0.31);
            }
        }
        if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(rightx.getNumberValue(), righty.getNumberValue(), rightz.getNumberValue());
        }
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightx1.getNumberValue(), righty2.getNumberValue(), rightz3.getNumberValue());
        }
    }
}