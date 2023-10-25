package net.minecraft.entity.player;

import java.util.concurrent.Callable;
import net.minecraft.item.ItemStack;

class InventoryPlayer.1
implements Callable<String> {
    final /* synthetic */ ItemStack val$itemStackIn;

    InventoryPlayer.1(ItemStack itemStack) {
        this.val$itemStackIn = itemStack;
    }

    public String call() throws Exception {
        return this.val$itemStackIn.getDisplayName();
    }
}
