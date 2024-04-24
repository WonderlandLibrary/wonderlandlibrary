package Method.Client.module.player;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;


public class AutoFish extends Module {

    public AutoFish() {
        super("AutoFish", Keyboard.KEY_NONE, Category.PLAYER, "AutoFish");
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet2 = (SPacketSoundEffect) packet;
            if (packet2.getSound().equals(SoundEvents.ENTITY_BOBBER_SPLASH)) {
                new Thread(() -> {
                    try {
                        mc.playerController.processRightClick(mc.player, mc.player.world, EnumHand.MAIN_HAND);
                        Thread.sleep(300);
                        mc.playerController.processRightClick(mc.player, mc.player.world, EnumHand.MAIN_HAND);
                    } catch (Exception ignored) {
                    }
                }).start();
            }
        }
        return true;
    }
}
