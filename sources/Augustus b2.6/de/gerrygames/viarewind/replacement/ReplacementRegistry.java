// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.replacement;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class ReplacementRegistry
{
    private final Int2ObjectMap<Replacement> itemReplacements;
    private final Int2ObjectMap<Replacement> blockReplacements;
    
    public ReplacementRegistry() {
        this.itemReplacements = new Int2ObjectOpenHashMap<Replacement>();
        this.blockReplacements = new Int2ObjectOpenHashMap<Replacement>();
    }
    
    public void registerItem(final int id, final Replacement replacement) {
        this.registerItem(id, -1, replacement);
    }
    
    public void registerBlock(final int id, final Replacement replacement) {
        this.registerBlock(id, -1, replacement);
    }
    
    public void registerItemBlock(final int id, final Replacement replacement) {
        this.registerItemBlock(id, -1, replacement);
    }
    
    public void registerItem(final int id, final int data, final Replacement replacement) {
        this.itemReplacements.put(combine(id, data), replacement);
    }
    
    public void registerBlock(final int id, final int data, final Replacement replacement) {
        this.blockReplacements.put(combine(id, data), replacement);
    }
    
    public void registerItemBlock(final int id, final int data, final Replacement replacement) {
        this.registerItem(id, data, replacement);
        this.registerBlock(id, data, replacement);
    }
    
    public Item replace(final Item item) {
        Replacement replacement = this.itemReplacements.get(combine(item.identifier(), item.data()));
        if (replacement == null) {
            replacement = this.itemReplacements.get(combine(item.identifier(), -1));
        }
        return (replacement == null) ? item : replacement.replace(item);
    }
    
    public Replacement replace(final int id, final int data) {
        Replacement replacement = this.blockReplacements.get(combine(id, data));
        if (replacement == null) {
            replacement = this.blockReplacements.get(combine(id, -1));
        }
        return replacement;
    }
    
    public static int combine(final int id, final int data) {
        return id << 16 | (data & 0xFFFF);
    }
}
