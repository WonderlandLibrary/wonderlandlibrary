package me.aquavit.liquidsense.injection.forge.mixins.item;

import me.aquavit.liquidsense.injection.implementations.IItemStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MixinItemStack implements IItemStack {

    private long itemDelay;

    @Inject(method = "<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V", at = @At("RETURN"))
    private void init(final CallbackInfo callbackInfo) {
        this.itemDelay = System.currentTimeMillis();
    }

    @Override
    public long getItemDelay() {
        return itemDelay;
    }
}