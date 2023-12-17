// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import net.minecraft.nbt.NBTException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.nbt.NBTTagCompound;

public class SetNBT extends LootFunction
{
    private final NBTTagCompound tag;
    
    public SetNBT(final LootCondition[] conditionsIn, final NBTTagCompound tagIn) {
        super(conditionsIn);
        this.tag = tagIn;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound == null) {
            nbttagcompound = this.tag.copy();
        }
        else {
            nbttagcompound.merge(this.tag);
        }
        stack.setTagCompound(nbttagcompound);
        return stack;
    }
    
    public static class Serializer extends LootFunction.Serializer<SetNBT>
    {
        public Serializer() {
            super(new ResourceLocation("set_nbt"), SetNBT.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final SetNBT functionClazz, final JsonSerializationContext serializationContext) {
            object.addProperty("tag", functionClazz.tag.toString());
        }
        
        @Override
        public SetNBT deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            try {
                final NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(JsonUtils.getString(object, "tag"));
                return new SetNBT(conditionsIn, nbttagcompound);
            }
            catch (NBTException nbtexception) {
                throw new JsonSyntaxException((Throwable)nbtexception);
            }
        }
    }
}
