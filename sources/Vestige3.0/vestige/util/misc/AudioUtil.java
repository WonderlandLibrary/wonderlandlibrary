package vestige.util.misc;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import vestige.util.IMinecraft;

public class AudioUtil implements IMinecraft {

    public static void buttonClick() {
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

}