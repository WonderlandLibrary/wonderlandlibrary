package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.GCDFix;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.math.TimerHelper;

public class AntiAFK extends Feature {
    private final BooleanSetting sendMessage;
    private final BooleanSetting spin;

    public final NumberSetting sendDelay;
    public final BooleanSetting jump = new BooleanSetting("Jump", true, () -> true);
    public TimerHelper timerHelper = new TimerHelper();
    public float rot = 0;

    public AntiAFK() {
        super("AntiAFK", "Позволяет встать в афк режим, без риска кикнуться",  FeatureCategory.Player);
        spin = new BooleanSetting("Spin", true, () -> true);
        sendMessage = new BooleanSetting("Send Message", true, () -> true);
        sendDelay = new NumberSetting("Send Delay", 500, 100, 1000, 100, sendMessage::getBoolValue);
        addSettings(spin, sendMessage, sendDelay, jump);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(mc.gameSettings.keyBindJump.pressed) {
            return;
        }
        if (jump.getBoolValue() && mc.player.onGround) {
            mc.player.jump();
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {

        if (spin.getBoolValue()) {
            float yaw = GCDFix.getFixedRotation((float) (Math.floor((double) this.spinAim(25)) + (double) MathematicHelper.randomizeFloat(-4.0F, 1.0F)));
            event.setYaw(yaw);
            mc.player.renderYawOffset = yaw;
            mc.player.rotationPitchHead = 0;
            mc.player.rotationYawHead = yaw;
        }
        if (timerHelper.hasReached(sendDelay.getNumberValue() * 20) && sendMessage.getBoolValue()) {
            mc.player.sendChatMessage("/richclient");
            timerHelper.reset();
        }
    }
    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
