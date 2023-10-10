// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.rewriter;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;

public interface ItemRewriter<T extends Protocol> extends Rewriter<T>
{
    Item handleItemToClient(final Item p0);
    
    Item handleItemToServer(final Item p0);
}
