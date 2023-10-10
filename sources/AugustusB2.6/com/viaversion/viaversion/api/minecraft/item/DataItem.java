// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.item;

import java.util.Objects;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.gson.annotations.SerializedName;

public class DataItem implements Item
{
    @SerializedName(value = "identifier", alternate = { "id" })
    private int identifier;
    private byte amount;
    private short data;
    private CompoundTag tag;
    
    public DataItem() {
    }
    
    public DataItem(final int identifier, final byte amount, final short data, final CompoundTag tag) {
        this.identifier = identifier;
        this.amount = amount;
        this.data = data;
        this.tag = tag;
    }
    
    public DataItem(final Item toCopy) {
        this(toCopy.identifier(), (byte)toCopy.amount(), toCopy.data(), toCopy.tag());
    }
    
    @Override
    public int identifier() {
        return this.identifier;
    }
    
    @Override
    public void setIdentifier(final int identifier) {
        this.identifier = identifier;
    }
    
    @Override
    public int amount() {
        return this.amount;
    }
    
    @Override
    public void setAmount(final int amount) {
        if (amount > 127 || amount < -128) {
            throw new IllegalArgumentException("Invalid item amount: " + amount);
        }
        this.amount = (byte)amount;
    }
    
    @Override
    public short data() {
        return this.data;
    }
    
    @Override
    public void setData(final short data) {
        this.data = data;
    }
    
    @Override
    public CompoundTag tag() {
        return this.tag;
    }
    
    @Override
    public void setTag(final CompoundTag tag) {
        this.tag = tag;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final DataItem item = (DataItem)o;
        return this.identifier == item.identifier && this.amount == item.amount && this.data == item.data && Objects.equals(this.tag, item.tag);
    }
    
    @Override
    public int hashCode() {
        int result = this.identifier;
        result = 31 * result + this.amount;
        result = 31 * result + this.data;
        result = 31 * result + ((this.tag != null) ? this.tag.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Item{identifier=" + this.identifier + ", amount=" + this.amount + ", data=" + this.data + ", tag=" + this.tag + '}';
    }
}
