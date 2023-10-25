package fun.expensive.client.feature.impl.player;

import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import net.minecraft.entity.Entity;

public class NoClip extends Feature {

    public NoClip() {
        super("NoClip", "Позволяет ходить сквозь стены", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player != null) {
            mc.player.noClip = true;
            mc.player.motionY = 0.00001;

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = 0.4;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -0.4;
            }
        }
    }


    public static boolean isNoClip(Entity entity) {
        if (Rich.instance.featureManager.getFeature(NoClip.class).isEnabled() && mc.player != null && (mc.player.ridingEntity == null || entity == mc.player.ridingEntity))
            return true;
        return entity.noClip;

    }

    public void onDisable() {
        mc.player.noClip = false;
        super.onDisable();
    }
}
