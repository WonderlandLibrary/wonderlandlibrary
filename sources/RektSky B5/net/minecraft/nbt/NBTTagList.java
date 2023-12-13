/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
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
        for (int i2 = 0; i2 < this.tagList.size(); ++i2) {
            this.tagList.get(i2).write(output);
        }
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(296L);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.tagType = input.readByte();
        int i2 = input.readInt();
        if (this.tagType == 0 && i2 > 0) {
            throw new RuntimeException("Missing type on ListTag");
        }
        sizeTracker.read(32L * (long)i2);
        this.tagList = Lists.newArrayListWithCapacity(i2);
        for (int j2 = 0; j2 < i2; ++j2) {
            NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
            nbtbase.read(input, depth + 1, sizeTracker);
            this.tagList.add(nbtbase);
        }
    }

    @Override
    public byte getId() {
        return 9;
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[");
        for (int i2 = 0; i2 < this.tagList.size(); ++i2) {
            if (i2 != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(i2).append(':').append(this.tagList.get(i2));
        }
        return stringbuilder.append(']').toString();
    }

    public void appendTag(NBTBase nbt) {
        if (nbt.getId() == 0) {
            LOGGER.warn("Invalid TagEnd added to ListTag");
        } else {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            } else if (this.tagType != nbt.getId()) {
                LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }
            this.tagList.add(nbt);
        }
    }

    public void set(int idx, NBTBase nbt) {
        if (nbt.getId() == 0) {
            LOGGER.warn("Invalid TagEnd added to ListTag");
        } else if (idx >= 0 && idx < this.tagList.size()) {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            } else if (this.tagType != nbt.getId()) {
                LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }
            this.tagList.set(idx, nbt);
        } else {
            LOGGER.warn("index out of bounds to set tag in tag list");
        }
    }

    public NBTBase removeTag(int i2) {
        return this.tagList.remove(i2);
    }

    @Override
    public boolean hasNoTags() {
        return this.tagList.isEmpty();
    }

    public NBTTagCompound getCompoundTagAt(int i2) {
        if (i2 >= 0 && i2 < this.tagList.size()) {
            NBTBase nbtbase = this.tagList.get(i2);
            return nbtbase.getId() == 10 ? (NBTTagCompound)nbtbase : new NBTTagCompound();
        }
        return new NBTTagCompound();
    }

    public int[] getIntArrayAt(int i2) {
        if (i2 >= 0 && i2 < this.tagList.size()) {
            NBTBase nbtbase = this.tagList.get(i2);
            return nbtbase.getId() == 11 ? ((NBTTagIntArray)nbtbase).getIntArray() : new int[]{};
        }
        return new int[0];
    }

    public double getDoubleAt(int i2) {
        if (i2 >= 0 && i2 < this.tagList.size()) {
            NBTBase nbtbase = this.tagList.get(i2);
            return nbtbase.getId() == 6 ? ((NBTTagDouble)nbtbase).getDouble() : 0.0;
        }
        return 0.0;
    }

    public float getFloatAt(int i2) {
        if (i2 >= 0 && i2 < this.tagList.size()) {
            NBTBase nbtbase = this.tagList.get(i2);
            return nbtbase.getId() == 5 ? ((NBTTagFloat)nbtbase).getFloat() : 0.0f;
        }
        return 0.0f;
    }

    public String getStringTagAt(int i2) {
        if (i2 >= 0 && i2 < this.tagList.size()) {
            NBTBase nbtbase = this.tagList.get(i2);
            return nbtbase.getId() == 8 ? nbtbase.getString() : nbtbase.toString();
        }
        return "";
    }

    public NBTBase get(int idx) {
        return idx >= 0 && idx < this.tagList.size() ? this.tagList.get(idx) : new NBTTagEnd();
    }

    public int tagCount() {
        return this.tagList.size();
    }

    @Override
    public NBTBase copy() {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.tagType = this.tagType;
        for (NBTBase nbtbase : this.tagList) {
            NBTBase nbtbase1 = nbtbase.copy();
            nbttaglist.tagList.add(nbtbase1);
        }
        return nbttaglist;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagList nbttaglist = (NBTTagList)p_equals_1_;
            if (this.tagType == nbttaglist.tagType) {
                return this.tagList.equals(nbttaglist.tagList);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.tagList.hashCode();
    }

    public int getTagType() {
        return this.tagType;
    }
}

