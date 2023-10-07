// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

public class LongHashMap
{
    private transient Entry[] hashArray;
    private transient int numHashElements;
    private int field_180201_c;
    private int capacity;
    private final float percentUseable = 0.75f;
    private transient volatile int modCount;
    private static final String __OBFID = "CL_00001492";
    
    public LongHashMap() {
        this.hashArray = new Entry[4096];
        this.capacity = 3072;
        this.field_180201_c = this.hashArray.length - 1;
    }
    
    private static int getHashedKey(final long originalKey) {
        return (int)(originalKey ^ originalKey >>> 27);
    }
    
    private static int hash(int integer) {
        integer ^= (integer >>> 20 ^ integer >>> 12);
        return integer ^ integer >>> 7 ^ integer >>> 4;
    }
    
    private static int getHashIndex(final int p_76158_0_, final int p_76158_1_) {
        return p_76158_0_ & p_76158_1_;
    }
    
    public int getNumHashElements() {
        return this.numHashElements;
    }
    
    public Object getValueByKey(final long p_76164_1_) {
        final int var3 = getHashedKey(p_76164_1_);
        for (Entry var4 = this.hashArray[getHashIndex(var3, this.field_180201_c)]; var4 != null; var4 = var4.nextEntry) {
            if (var4.key == p_76164_1_) {
                return var4.value;
            }
        }
        return null;
    }
    
    public boolean containsItem(final long p_76161_1_) {
        return this.getEntry(p_76161_1_) != null;
    }
    
    final Entry getEntry(final long p_76160_1_) {
        final int var3 = getHashedKey(p_76160_1_);
        for (Entry var4 = this.hashArray[getHashIndex(var3, this.field_180201_c)]; var4 != null; var4 = var4.nextEntry) {
            if (var4.key == p_76160_1_) {
                return var4;
            }
        }
        return null;
    }
    
    public void add(final long p_76163_1_, final Object p_76163_3_) {
        final int var4 = getHashedKey(p_76163_1_);
        final int var5 = getHashIndex(var4, this.field_180201_c);
        for (Entry var6 = this.hashArray[var5]; var6 != null; var6 = var6.nextEntry) {
            if (var6.key == p_76163_1_) {
                var6.value = p_76163_3_;
                return;
            }
        }
        ++this.modCount;
        this.createKey(var4, p_76163_1_, p_76163_3_, var5);
    }
    
    private void resizeTable(final int p_76153_1_) {
        final Entry[] var2 = this.hashArray;
        final int var3 = var2.length;
        if (var3 == 1073741824) {
            this.capacity = Integer.MAX_VALUE;
        }
        else {
            final Entry[] var4 = new Entry[p_76153_1_];
            this.copyHashTableTo(var4);
            this.hashArray = var4;
            this.field_180201_c = this.hashArray.length - 1;
            final float var5 = p_76153_1_;
            this.getClass();
            this.capacity = (int)(var5 * 0.75f);
        }
    }
    
    private void copyHashTableTo(final Entry[] p_76154_1_) {
        final Entry[] var2 = this.hashArray;
        final int var3 = p_76154_1_.length;
        for (int var4 = 0; var4 < var2.length; ++var4) {
            Entry var5 = var2[var4];
            if (var5 != null) {
                var2[var4] = null;
                Entry var6;
                do {
                    var6 = var5.nextEntry;
                    final int var7 = getHashIndex(var5.hash, var3 - 1);
                    var5.nextEntry = p_76154_1_[var7];
                    p_76154_1_[var7] = var5;
                } while ((var5 = var6) != null);
            }
        }
    }
    
    public Object remove(final long p_76159_1_) {
        final Entry var3 = this.removeKey(p_76159_1_);
        return (var3 == null) ? null : var3.value;
    }
    
    final Entry removeKey(final long p_76152_1_) {
        final int var3 = getHashedKey(p_76152_1_);
        final int var4 = getHashIndex(var3, this.field_180201_c);
        Entry var6;
        Entry var7;
        for (Entry var5 = var6 = this.hashArray[var4]; var6 != null; var6 = var7) {
            var7 = var6.nextEntry;
            if (var6.key == p_76152_1_) {
                ++this.modCount;
                --this.numHashElements;
                if (var5 == var6) {
                    this.hashArray[var4] = var7;
                }
                else {
                    var5.nextEntry = var7;
                }
                return var6;
            }
            var5 = var6;
        }
        return var6;
    }
    
    private void createKey(final int p_76156_1_, final long p_76156_2_, final Object p_76156_4_, final int p_76156_5_) {
        final Entry var6 = this.hashArray[p_76156_5_];
        this.hashArray[p_76156_5_] = new Entry(p_76156_1_, p_76156_2_, p_76156_4_, var6);
        if (this.numHashElements++ >= this.capacity) {
            this.resizeTable(2 * this.hashArray.length);
        }
    }
    
    public double getKeyDistribution() {
        int countValid = 0;
        for (final Entry element : this.hashArray) {
            if (element != null) {
                ++countValid;
            }
        }
        return 1.0 * countValid / this.numHashElements;
    }
    
    static class Entry
    {
        final long key;
        Object value;
        Entry nextEntry;
        final int hash;
        private static final String __OBFID = "CL_00001493";
        
        Entry(final int p_i1553_1_, final long p_i1553_2_, final Object p_i1553_4_, final Entry p_i1553_5_) {
            this.value = p_i1553_4_;
            this.nextEntry = p_i1553_5_;
            this.key = p_i1553_2_;
            this.hash = p_i1553_1_;
        }
        
        public final long getKey() {
            return this.key;
        }
        
        public final Object getValue() {
            return this.value;
        }
        
        @Override
        public final boolean equals(final Object p_equals_1_) {
            if (!(p_equals_1_ instanceof Entry)) {
                return false;
            }
            final Entry var2 = (Entry)p_equals_1_;
            final Long var3 = this.getKey();
            final Long var4 = var2.getKey();
            if (var3 == var4 || (var3 != null && var3.equals(var4))) {
                final Object var5 = this.getValue();
                final Object var6 = var2.getValue();
                if (var5 == var6 || (var5 != null && var5.equals(var6))) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public final int hashCode() {
            return getHashedKey(this.key);
        }
        
        @Override
        public final String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }
}
