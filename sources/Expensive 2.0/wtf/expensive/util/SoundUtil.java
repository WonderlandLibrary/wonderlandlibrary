package wtf.expensive.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvents;

/**
 * @author dedinside
 * @since 27.06.2023
 */
public class SoundUtil implements IMinecraft {
    public static void playSound(float pitch, float volume) {
        if (mc.player == null)
            return;
        mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, volume, pitch);
    }

}
