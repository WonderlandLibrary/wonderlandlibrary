package com.canon.majik.api.event.events;

import com.canon.majik.api.event.eventBus.Event;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class RenderHandEvent extends Event {
    private final EnumHand hand;
    private final float partialTicks;
    private final float interpolatedPitch;
    private final float swingProgress;
    private final float equipProgress;
    @Nonnull
    private final ItemStack stack;

    public RenderHandEvent(EnumHand hand, float partialTicks, float interpolatedPitch, float swingProgress, float equipProgress, @Nonnull ItemStack stack)
    {
        this.hand = hand;
        this.partialTicks = partialTicks;
        this.interpolatedPitch = interpolatedPitch;
        this.swingProgress = swingProgress;
        this.equipProgress = equipProgress;
        this.stack = stack;
    }

    public EnumHand getHand()
    {
        return hand;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    public float getInterpolatedPitch()
    {
        return interpolatedPitch;
    }

    public float getSwingProgress()
    {
        return swingProgress;
    }

    public float getEquipProgress()
    {
        return equipProgress;
    }

    @Nonnull
    public ItemStack getItemStack()
    {
        return stack;
    }
}
