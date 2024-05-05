package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.item.ItemStack;

static class ItemMultiTexture.1
implements Function<ItemStack, String> {
    final /* synthetic */ String[] val$namesByMeta;

    ItemMultiTexture.1(String[] stringArray) {
        this.val$namesByMeta = stringArray;
    }

    public String apply(ItemStack p_apply_1_) {
        int i = p_apply_1_.getMetadata();
        if (i < 0 || i >= this.val$namesByMeta.length) {
            i = 0;
        }
        return this.val$namesByMeta[i];
    }
}
