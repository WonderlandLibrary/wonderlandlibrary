package net.smoothboot.client.util;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.smoothboot.client.mixin.CursorSlotMixin;
import org.jetbrains.annotations.Nullable;


public class AccessorUtil {
    @Nullable
    public static Slot getHoveredSlot(HandledScreen<?> gui) {
        return ((CursorSlotMixin)gui).itemscroller_getHoveredSlot();
    }
}
