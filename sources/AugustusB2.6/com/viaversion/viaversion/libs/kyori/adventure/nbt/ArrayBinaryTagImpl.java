// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

abstract class ArrayBinaryTagImpl extends AbstractBinaryTag implements ArrayBinaryTag
{
    static void checkIndex(final int index, final int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }
}
