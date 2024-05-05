package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventSendPacket;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class NoSlowDown extends Feature {
    private final ListSetting noSlowMode = new ListSetting("Mode", "Matrix", () -> true, "Matrix", "Vanilla");
    public static NumberSetting percentage;
    public int usingTicks;

    public NoSlowDown() {
        super("No Slow", "Позволяет вам идти и кушать", FeatureCategory.Movement);
        percentage = new NumberSetting("Speed", 100, 0, 100, 1, () -> noSlowMode.getCurrentMode().equals("Vanilla"), NumberSetting.NumberType.PERCENTAGE);
        addSettings(noSlowMode, percentage);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        usingTicks = mc.player.isUsingItem() ? ++usingTicks : 0;
        if (!this.isEnabled() || !mc.player.isUsingItem()) {
            return;
        }
        if (noSlowMode.currentMode.equals("Matrix")) {
        	  if (mc.player.isUsingItem()) {
                  if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                      if (mc.player.ticksExisted % 2 == 0) {
                          mc.player.motionX *= 0.46;
                          mc.player.motionZ *= 0.46;
                      }
                  } else if ((double) mc.player.fallDistance > 0.2) {
                      mc.player.motionX *= 0.9100000262260437;
                      mc.player.motionZ *= 0.9100000262260437;
                  }
              }
        }
    }
}
