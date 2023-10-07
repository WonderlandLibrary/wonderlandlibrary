// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_17to1_16_4.storage;

import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class InventoryAcknowledgements implements StorableObject
{
    private final IntList ids;
    
    public InventoryAcknowledgements() {
        this.ids = new IntArrayList();
    }
    
    public void addId(final int id) {
        this.ids.add(id);
    }
    
    public boolean removeId(final int id) {
        return this.ids.rem(id);
    }
}
