package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.Bloomware;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntryListWidget.class)
public class MixinEntryListWidget {
    @ModifyArg(method = "renderEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget$Entry;render(Lnet/minecraft/client/util/math/MatrixStack;IIIIIIIZF)V"), index = 8)
    public boolean render(boolean hovered) {
        if (Bloomware.INSTANCE.getCurrentScreen() != null) return false;
        return hovered;
    }
}
