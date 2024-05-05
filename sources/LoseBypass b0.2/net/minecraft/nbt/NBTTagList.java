/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagIntArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList
extends NBTBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private List<NBTBase> tagList = Lists.newArrayList();
    private byte tagType = 0;

    @Override
    void write(DataOutput output) throws IOException {
        this.tagType = !this.tagList.isEmpty() ? this.tagList.get(0).getId() : (byte)0;
        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());
        int i = 0;
        while (i < this.tagList.size()) {
            this.tagList.get(i).write(output);
            ++i;
        }
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(296L);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.tagType = input.readByte();
        int i = input.readInt();
        if (this.tagType == 0 && i > 0) {
            throw new RuntimeException("Missing type on ListTag");
        }
        sizeTracker.read(32L * (long)i);
        this.tagList = Lists.newArrayListWithCapacity(i);
        int j = 0;
        while (j < i) {
            NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
            nbtbase.read(input, depth + 1, sizeTracker);
            this.tagList.add(nbtbase);
            ++j;
        }
    }

    @Override
    public byte getId() {
        return 9;
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[");
        int i = 0;
        while (i < this.tagList.size()) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(i).append(':').append(this.tagList.get(i));
            ++i;
        }
        return stringbuilder.append(']').toString();
    }

    public void appendTag(NBTBase nbt) {
        if (nbt.getId() == 0) {
            LOGGER.warn("Invalid TagEnd added to ListTag");
            return;
        }
        if (this.tagType == 0) {
            this.tagType = nbt.getId();
        } else if (this.tagType != nbt.getId()) {
            LOGGER.warn("Adding mismatching tag types to tag list");
            return;
        }
        this.tagList.add(nbt);
    }

    public void set(int idx, NBTBase nbt) {
        if (nbt.getId() == 0) {
            LOGGER.warn("Invalid TagEnd added to ListTag");
            return;
        }
        if (idx >= 0 && idx < this.tagList.size()) {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            } else if (this.tagType != nbt.getId()) {
                LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }
            this.tagList.set(idx, nbt);
            return;
        }
        LOGGER.warn("index out of bounds to set tag in tag list");
    }

    public NBTBase removeTag(int i) {
        return this.tagList.remove(i);
    }

    @Override
    public boolean hasNoTags() {
        return this.tagList.isEmpty();
    }

    public NBTTagCompound getCompoundTagAt(int i) {
        NBTTagCompound nBTTagCompound;
        if (i < 0) return new NBTTagCompound();
        if (i >= this.tagList.size()) return new NBTTagCompound();
        NBTBase nbtbase = this.tagList.get(i);
        if (nbtbase.getId() == 10) {
            nBTTagCompound = (NBTTagCompound)nbtbase;
            return nBTTagCompound;
        }
        nBTTagCompound = new NBTTagCompound();
        return nBTTagCompound;
    }

    public int[] getIntArrayAt(int i) {
        int[] nArray;
        if (i < 0) return new int[0];
        if (i >= this.tagList.size()) return new int[0];
        NBTBase nbtbase = this.tagList.get(i);
        if (nbtbase.getId() == 11) {
            nArray = ((NBTTagIntArray)nbtbase).getIntArray();
            return nArray;
        }
        nArray = new int[]{};
        return nArray;
    }

    public double getDoubleAt(int i) {
        if (i < 0) return 0.0;
        if (i >= this.tagList.size()) return 0.0;
        NBTBase nbtbase = this.tagList.get(i);
        if (nbtbase.getId() != 6) return 0.0;
        double d = ((NBTTagDouble)nbtbase).getDouble();
        return d;
    }

    public float getFloatAt(int i) {
        if (i < 0) return 0.0f;
        if (i >= this.tagList.size()) return 0.0f;
        NBTBase nbtbase = this.tagList.get(i);
        if (nbtbase.getId() != 5) return 0.0f;
        float f = ((NBTTagFloat)nbtbase).getFloat();
        return f;
    }

    public String getStringTagAt(int i) {
        String string;
        if (i < 0) return "";
        if (i >= this.tagList.size()) return "";
        NBTBase nbtbase = this.tagList.get(i);
        if (nbtbase.getId() == 8) {
            string = nbtbase.getString();
            return string;
        }
        string = nbtbase.toString();
        return string;
    }

    public NBTBase get(int idx) {
        NBTBase nBTBase;
        if (idx >= 0 && idx < this.tagList.size()) {
            nBTBase = this.tagList.get(idx);
            return nBTBase;
        }
        nBTBase = new NBTTagEnd();
        return nBTBase;
    }

    public int tagCount() {
        return this.tagList.size();
    }

    @Override
    public NBTBase copy() {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.tagType = this.tagType;
        Iterator<NBTBase> iterator = this.tagList.iterator();
        while (iterator.hasNext()) {
            NBTBase nbtbase = iterator.next();
            NBTBase nbtbase1 = nbtbase.copy();
            nbttaglist.tagList.add(nbtbase1);
        }
        return nbttaglist;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (!super.equals(p_equals_1_)) return false;
        NBTTagList nbttaglist = (NBTTagList)p_equals_1_;
        if (this.tagType != nbttaglist.tagType) return false;
        return this.tagList.equals(nbttaglist.tagList);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.tagList.hashCode();
    }

    public int getTagType() {
        return this.tagType;
    }
}

