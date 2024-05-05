package rip.helium.cheat.impl.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import rip.helium.ChatUtil;
import rip.helium.cheat.Cheat;
import rip.helium.event.minecraft.KeyPressEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.notification.mgmt.NotificationManager;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.DoubleProperty;

public class VClip extends Cheat {

    Stopwatch stopwatch = new Stopwatch();
    boolean downPressed = false;
    boolean upPressed = false;
    private final DoubleProperty amount = new DoubleProperty("Amount", "The amount of blocks you want to vclip.", null, 10, 1, 20.0, 1, null);

    public VClip() {
        super("No Clip", "Teleports you down.");
        registerProperties(amount);
    }

    @Override
    protected void onEnable() {
        ChatUtil.chatNoPrefix("§cPress Sneak + 'G' to go down, Press Jump + 'G' to go up. \n§cPress J to lower the amount and press L to higher the amount. \n§cPress K to reset (to 3 blocks).");
    }

    @Collect
    public void playerUpdateEvent(PlayerUpdateEvent event) {
        if (mc.gameSettings.keyBindSneak.pressed && downPressed) {
            if (stopwatch.hasPassed(100)) {
                mc.thePlayer.noClip = true;
                NotificationManager.postInfo("Teleported!", "You were teleported §c" + amount.getValue().intValue() + " §fdown!");
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - amount.getValue(), mc.thePlayer.posZ);
                stopwatch.reset();
                downPressed = false;
                upPressed = false;
                mc.thePlayer.noClip = false;
            }
        }
        if (mc.gameSettings.keyBindJump.pressed && upPressed) {
            if (stopwatch.hasPassed(100)) {
                mc.thePlayer.noClip = true;
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + amount.getValue(), mc.thePlayer.posZ);
                NotificationManager.postInfo("Teleported!", "You were teleported §c" + amount.getValue().intValue() + " §fup!");
                stopwatch.reset();
                downPressed = false;
                upPressed = false;
                mc.thePlayer.noClip = false;
            }
        }
    }

    @Collect
    public void keyEvent(KeyPressEvent event) {
        switch (event.getKeyCode()) {
            case 34: { // G
                upPressed = true;
                downPressed = true;
                break;
            }
            case 36: { // J
                amount.setValue(amount.getValue() - 1);
                ChatUtil.chat("§cAmount: §f" + amount.getValue().intValue());
                break;
            }
            case 37: { // K
                amount.setValue(3.0);
                ChatUtil.chat("§cAmount: §f" + 3);
                break;
            }
            case 38: { // L
                amount.setValue(amount.getValue() + 1);
                ChatUtil.chat("§cAmount: §f" + amount.getValue().intValue());
                break;
            }
        }
    }
}
