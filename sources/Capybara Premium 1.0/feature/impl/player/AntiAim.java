package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.GCDFix;
import fun.rich.client.utils.math.MathematicHelper;

public class AntiAim extends Feature {
    public float rot = 0.0F;
    public NumberSetting spinSpeed = new NumberSetting("Spin Speed", 1.0F, 0.0F, 10.0F, 0.1F, () -> {
        return true;
    });
    public BooleanSetting serverside = new BooleanSetting("Server-Side", false, () -> {
        return true;
    });
    public NumberSetting pitch = new NumberSetting("Pitch", 0.0F, -90.0F, 90.0F, 1.0F, () -> {
        return true;
    });

    public AntiAim() {
        super("AntiAim", "Крутилочка", FeatureCategory.Player);
        addSettings(serverside, pitch, spinSpeed);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        float speed = this.spinSpeed.getNumberValue() * 10.0F;
        if (this.serverside.getBoolValue()) {
            event.setPitch(this.pitch.getNumberValue());
        }

        mc.player.rotationPitchHead = this.pitch.getNumberValue();
        float yaw = GCDFix.getFixedRotation((float) (Math.floor((double) this.spinAim(speed)) + (double) MathematicHelper.randomizeFloat(-4.0F, 1.0F)));
        if (this.serverside.getBoolValue()) {
            event.setYaw(GCDFix.getFixedRotation(yaw));
        }

        mc.player.renderYawOffset = yaw;
        mc.player.rotationYawHead = yaw;
    }

    public float spinAim(float rots) {
        this.rot += rots;
        return this.rot;
    }
}
