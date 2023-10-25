package net.minecraft.tileentity;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;

public class MobSpawnerBaseLogic.WeightedRandomMinecart
extends WeightedRandom.Item {
    private final NBTTagCompound nbtData;
    private final String entityType;

    public MobSpawnerBaseLogic.WeightedRandomMinecart(NBTTagCompound tagCompound) {
        this(tagCompound.getCompoundTag("Properties"), tagCompound.getString("Type"), tagCompound.getInteger("Weight"));
    }

    public MobSpawnerBaseLogic.WeightedRandomMinecart(NBTTagCompound tagCompound, String type) {
        this(tagCompound, type, 1);
    }

    private MobSpawnerBaseLogic.WeightedRandomMinecart(NBTTagCompound tagCompound, String type, int weight) {
        super(weight);
        if (type.equals((Object)"Minecart")) {
            type = tagCompound != null ? EntityMinecart.EnumMinecartType.byNetworkID((int)tagCompound.getInteger("Type")).getName() : "MinecartRideable";
        }
        this.nbtData = tagCompound;
        this.entityType = type;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setTag("Properties", (NBTBase)this.nbtData);
        nbttagcompound.setString("Type", this.entityType);
        nbttagcompound.setInteger("Weight", this.itemWeight);
        return nbttagcompound;
    }

    static /* synthetic */ String access$000(MobSpawnerBaseLogic.WeightedRandomMinecart x0) {
        return x0.entityType;
    }

    static /* synthetic */ NBTTagCompound access$100(MobSpawnerBaseLogic.WeightedRandomMinecart x0) {
        return x0.nbtData;
    }
}
