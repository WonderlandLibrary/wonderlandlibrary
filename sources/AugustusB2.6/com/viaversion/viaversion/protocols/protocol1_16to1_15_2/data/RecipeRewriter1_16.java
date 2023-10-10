// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_16to1_15_2.data;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;

public class RecipeRewriter1_16 extends RecipeRewriter1_14
{
    public RecipeRewriter1_16(final Protocol protocol) {
        super(protocol);
        this.recipeHandlers.put("smithing", this::handleSmithing);
    }
    
    public void handleSmithing(final PacketWrapper wrapper) throws Exception {
        final Item[] array;
        final Item[] baseIngredients = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
        for (final Item item : array) {
            this.rewrite(item);
        }
        final Item[] array2;
        final Item[] ingredients = array2 = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
        for (final Item item2 : array2) {
            this.rewrite(item2);
        }
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
}
