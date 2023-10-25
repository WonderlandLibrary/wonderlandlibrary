package net.minecraft.client.gui;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

class GuiOptions.1
extends GuiButton {
    GuiOptions.1(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        SoundEventAccessorComposite soundeventaccessorcomposite = soundHandlerIn.getRandomSoundFromCategories(new SoundCategory[]{SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER});
        if (soundeventaccessorcomposite != null) {
            soundHandlerIn.playSound((ISound)PositionedSoundRecord.create((ResourceLocation)soundeventaccessorcomposite.getSoundEventLocation(), (float)0.5f));
        }
    }
}
