/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.utils;

public class Block {
    private final int id;
    private final short data;

    public Block(int id, int data) {
        this.id = id;
        this.data = (short)data;
    }

    public Block(int id) {
        this.id = id;
        this.data = 0;
    }

    public int getId() {
        return this.id;
    }

    public int getData() {
        return this.data;
    }

    public Block withData(int data) {
        return new Block(this.id, data);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Block block = (Block)o2;
        if (this.id != block.id) {
            return false;
        }
        return this.data == block.data;
    }

    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.data;
        return result;
    }
}

