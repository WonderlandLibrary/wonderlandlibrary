// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.util.ArrayList;

public class CompactArrayList
{
    private ArrayList list;
    private int initialCapacity;
    private float loadFactor;
    private int countValid;
    
    public CompactArrayList() {
        this(10, 0.75f);
    }
    
    public CompactArrayList(final int initialCapacity) {
        this(initialCapacity, 0.75f);
    }
    
    public CompactArrayList(final int initialCapacity, final float loadFactor) {
        this.list = null;
        this.initialCapacity = 0;
        this.loadFactor = 1.0f;
        this.countValid = 0;
        this.list = new ArrayList(initialCapacity);
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
    }
    
    public void add(final int index, final Object element) {
        if (element != null) {
            ++this.countValid;
        }
        this.list.add(index, element);
    }
    
    public boolean add(final Object element) {
        if (element != null) {
            ++this.countValid;
        }
        return this.list.add(element);
    }
    
    public Object set(final int index, final Object element) {
        final Object oldElement = this.list.set(index, element);
        if (element != oldElement) {
            if (oldElement == null) {
                ++this.countValid;
            }
            if (element == null) {
                --this.countValid;
            }
        }
        return oldElement;
    }
    
    public Object remove(final int index) {
        final Object oldElement = this.list.remove(index);
        if (oldElement != null) {
            --this.countValid;
        }
        return oldElement;
    }
    
    public void clear() {
        this.list.clear();
        this.countValid = 0;
    }
    
    public void compact() {
        if (this.countValid <= 0 && this.list.size() <= 0) {
            this.clear();
        }
        else if (this.list.size() > this.initialCapacity) {
            final float currentLoadFactor = this.countValid * 1.0f / this.list.size();
            if (currentLoadFactor <= this.loadFactor) {
                int dstIndex = 0;
                for (int i = 0; i < this.list.size(); ++i) {
                    final Object wr = this.list.get(i);
                    if (wr != null) {
                        if (i != dstIndex) {
                            this.list.set(dstIndex, wr);
                        }
                        ++dstIndex;
                    }
                }
                for (int i = this.list.size() - 1; i >= dstIndex; --i) {
                    this.list.remove(i);
                }
            }
        }
    }
    
    public boolean contains(final Object elem) {
        return this.list.contains(elem);
    }
    
    public Object get(final int index) {
        return this.list.get(index);
    }
    
    public boolean isEmpty() {
        return this.list.isEmpty();
    }
    
    public int size() {
        return this.list.size();
    }
    
    public int getCountValid() {
        return this.countValid;
    }
}
