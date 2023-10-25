package fun.expensive.client.feature.impl.visual;

import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventTransformSideFirstPerson;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.combat.KillAura;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class SwingAnimations extends Feature {
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
        super("SwingAnimations", "Добавляет анимацию на меч", FeatureCategory.Visuals);
        swordAnim = new ListSetting("Animation Mode", "Rich", () -> true, "Rich", "New", "Spin", "Default", "Glide");
        auraOnly = new BooleanSetting("Only Aura", false, () -> true);
        swingSpeed = new NumberSetting("Swing Speed", "Скорость удара меча", 8.0F, 1.0F, 20.0F, 1, () -> true);
        rightx1 = new NumberSetting("RightX", 0, -2, 2, 0.1F, () -> true);
        righty2 = new NumberSetting("RightY", 0.2F, -2, 2, 0.1F, () -> true);
        rightz3 = new NumberSetting("RightZ", -2, -2, -0.4f, 0.1F, () -> true);
        spinSpeed = new NumberSetting("Spin Speed", 4, 1, 10, 1, () -> swordAnim.currentMode.equals("Spin"));
        item360 = new BooleanSetting("Item360", false, () -> true);
        item360Mode = new ListSetting("Item360 Mode", "Horizontal", () -> item360.getBoolValue(), "Horizontal", "Vertical", "Zoom");
        item360Hand = new ListSetting("Item360 Hand", "All", () -> item360.getBoolValue(), "All", "Left", "Right");
        item360Speed = new NumberSetting("Item360 Speed", 8, 1, 15, 1, () -> item360.getBoolValue());
        addSettings(auraOnly, swordAnim, spinSpeed, swingSpeed, rightx1, righty2, rightz3, item360, item360Mode, item360Hand, item360Speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String anim = swordAnim.getOptions();
        blocking = Rich.instance.featureManager.getFeature(KillAura.class).isEnabled() && KillAura.target != null;
        this.setSuffix(anim);

    }

    @EventTarget
    public void onSidePerson(EventTransformSideFirstPerson event) {
        if (blocking) {
            if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(0.29, 0.10, -0.31);
            }
        }
    }
}