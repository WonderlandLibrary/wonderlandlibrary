package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagList;

static class JsonToNBT.List
extends JsonToNBT.Any {
    protected List<JsonToNBT.Any> field_150492_b = Lists.newArrayList();

    public JsonToNBT.List(String json) {
        this.json = json;
    }

    public NBTBase parse() throws NBTException {
        NBTTagList nbttaglist = new NBTTagList();
        for (JsonToNBT.Any jsontonbt$any : this.field_150492_b) {
            nbttaglist.appendTag(jsontonbt$any.parse());
        }
        return nbttaglist;
    }
}
