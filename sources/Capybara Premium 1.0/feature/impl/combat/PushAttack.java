package fun.expensive.client.feature.impl.combat;


import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.NumberSetting;

public class PushAttack
        extends Feature {
    private final NumberSetting clickCoolDown = new NumberSetting("Click CoolDown", 1.0f, 0.5f, 1.0f, 0.1f, () -> true);

    public PushAttack() {
        super("PushAttack", "Позволяет бить на ЛКМ не смотря на использование предметов", FeatureCategory.Combat);
        addSettings(clickCoolDown);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getCooledAttackStrength(0.0f) == clickCoolDown.getNumberValue() && mc.gameSettings.keyBindAttack.pressed) {
            mc.clickMouse();
        }
    }
}
