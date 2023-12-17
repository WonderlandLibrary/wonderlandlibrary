// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import net.minecraft.nbt.NBTException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class NBTPredicate
{
    public static final NBTPredicate ANY;
    @Nullable
    private final NBTTagCompound tag;
    
    public NBTPredicate(@Nullable final NBTTagCompound tag) {
        this.tag = tag;
    }
    
    public boolean test(final ItemStack item) {
        return this == NBTPredicate.ANY || this.test(item.getTagCompound());
    }
    
    public boolean test(final Entity entityIn) {
        return this == NBTPredicate.ANY || this.test(CommandBase.entityToNBT(entityIn));
    }
    
    public boolean test(@Nullable final NBTBase nbt) {
        if (nbt == null) {
            return this == NBTPredicate.ANY;
        }
        return this.tag == null || NBTUtil.areNBTEquals(this.tag, nbt, true);
    }
    
    public static NBTPredicate deserialize(@Nullable final JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            NBTTagCompound nbttagcompound;
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "nbt"));
            }
            catch (NBTException nbtexception) {
                throw new JsonSyntaxException("Invalid nbt tag: " + nbtexception.getMessage());
            }
            return new NBTPredicate(nbttagcompound);
        }
        return NBTPredicate.ANY;
    }
    
    static {
        ANY = new NBTPredicate(null);
    }
}
