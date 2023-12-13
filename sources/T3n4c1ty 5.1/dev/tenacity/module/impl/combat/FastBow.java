package dev.tenacity.module.impl.combat;

import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.server.PacketUtils;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Mouse;

public final class FastBow extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Ghostly");
    private final NumberSetting shotDelay = new NumberSetting("Shot Delay", 0, 2, 0, 0.1);

    private final TimerUtil delayTimer = new TimerUtil();

    @Override
    public void onMotionEvent(MotionEvent event) {
        if (mc.thePlayer.getCurrentEquippedItem() == null) return;
        if (delayTimer.hasTimeElapsed(shotDelay.getValue().longValue() * 250L)) {
            switch (mode.getMode()) {
                case "Vanilla":
                    if (Mouse.isButtonDown(1) && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        for (int i = 0; i < 20; ++i) {
                            PacketUtils.sendPacketNoEvent(new C03PacketPlayer(true));
                        }
                        mc.rightClickDelayTimer = 0;
                        mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    }
                    break;
                case "Ghostly":
                    if (Mouse.isButtonDown(1) && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        for (int i = 0; i < 20; i++) {
                            PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));

                        }
                        mc.rightClickDelayTimer = 0;
                        mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    }
                    break;
            }
            delayTimer.reset();
        }
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    public FastBow() {
        super("FastBow", Category.COMBAT, "shoot bows faster");
        this.addSettings(mode, shotDelay);
    }

}
