package dev.zprestige.prestige.api.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={HandledScreen.class})
public interface IHandledScreen {
    @Accessor(value="focusedSlot")
    public Slot getFocusedSlot();
}
