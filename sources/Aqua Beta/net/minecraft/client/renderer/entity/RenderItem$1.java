package net.minecraft.client.renderer.entity;

import java.util.concurrent.Callable;
import net.minecraft.item.ItemStack;

class RenderItem.1
implements Callable<String> {
    final /* synthetic */ ItemStack val$stack;

    RenderItem.1(ItemStack itemStack) {
        this.val$stack = itemStack;
    }

    public String call() throws Exception {
        return String.valueOf((Object)this.val$stack.getItem());
    }
}
