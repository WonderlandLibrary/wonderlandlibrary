package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

static class JsonToNBT.Compound
extends JsonToNBT.Any {
    protected List<JsonToNBT.Any> field_150491_b = Lists.newArrayList();

    public JsonToNBT.Compound(String p_i45137_1_) {
        this.json = p_i45137_1_;
    }

    public NBTBase parse() throws NBTException {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        for (JsonToNBT.Any jsontonbt$any : this.field_150491_b) {
            nbttagcompound.setTag(jsontonbt$any.json, jsontonbt$any.parse());
        }
        return nbttagcompound;
    }
}
