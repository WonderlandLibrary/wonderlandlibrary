/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

public class Village {
    private World worldObj;
    private final List villageDoorInfoList = Lists.newArrayList();
    private BlockPos centerHelper = BlockPos.ORIGIN;
    private BlockPos center = BlockPos.ORIGIN;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;
    private int noBreedTicks;
    private TreeMap playerReputation = new TreeMap();
    private List villageAgressors = Lists.newArrayList();
    private int numIronGolems;
    private static final String __OBFID = "CL_00001631";

    public Village() {
    }

    public Village(World worldIn) {
        this.worldObj = worldIn;
    }

    public void func_82691_a(World worldIn) {
        this.worldObj = worldIn;
    }

    public void tick(int p_75560_1_) {
        Vec3 var3;
        int var2;
        this.tickCounter = p_75560_1_;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();
        if (p_75560_1_ % 20 == 0) {
            this.updateNumVillagers();
        }
        if (p_75560_1_ % 30 == 0) {
            this.updateNumIronGolems();
        }
        if (this.numIronGolems < (var2 = this.numVillagers / 10) && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0 && (var3 = this.func_179862_a(this.center, 2, 4, 2)) != null) {
            EntityIronGolem var4 = new EntityIronGolem(this.worldObj);
            var4.setPosition(var3.xCoord, var3.yCoord, var3.zCoord);
            this.worldObj.spawnEntityInWorld(var4);
            ++this.numIronGolems;
        }
    }

    private Vec3 func_179862_a(BlockPos p_179862_1_, int p_179862_2_, int p_179862_3_, int p_179862_4_) {
        for (int var5 = 0; var5 < 10; ++var5) {
            BlockPos var6 = p_179862_1_.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
            if (!this.func_179866_a(var6) || !this.func_179861_a(new BlockPos(p_179862_2_, p_179862_3_, p_179862_4_), var6)) continue;
            return new Vec3(var6.getX(), var6.getY(), var6.getZ());
        }
        return null;
    }

    private boolean func_179861_a(BlockPos p_179861_1_, BlockPos p_179861_2_) {
        if (!World.doesBlockHaveSolidTopSurface(this.worldObj, p_179861_2_.offsetDown())) {
            return false;
        }
        int var3 = p_179861_2_.getX() - p_179861_1_.getX() / 2;
        int var4 = p_179861_2_.getZ() - p_179861_1_.getZ() / 2;
        for (int var5 = var3; var5 < var3 + p_179861_1_.getX(); ++var5) {
            for (int var6 = p_179861_2_.getY(); var6 < p_179861_2_.getY() + p_179861_1_.getY(); ++var6) {
                for (int var7 = var4; var7 < var4 + p_179861_1_.getZ(); ++var7) {
                    if (!this.worldObj.getBlockState(new BlockPos(var5, var6, var7)).getBlock().isNormalCube()) continue;
                    return false;
                }
            }
        }
        return true;
    }

    private void updateNumIronGolems() {
        List var1 = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numIronGolems = var1.size();
    }

    private void updateNumVillagers() {
        List var1 = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numVillagers = var1.size();
        if (this.numVillagers == 0) {
            this.playerReputation.clear();
        }
    }

    public BlockPos func_180608_a() {
        return this.center;
    }

    public int getVillageRadius() {
        return this.villageRadius;
    }

    public int getNumVillageDoors() {
        return this.villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding() {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public int getNumVillagers() {
        return this.numVillagers;
    }

    public boolean func_179866_a(BlockPos p_179866_1_) {
        return this.center.distanceSq(p_179866_1_) < (double)(this.villageRadius * this.villageRadius);
    }

    public List getVillageDoorInfoList() {
        return this.villageDoorInfoList;
    }

    public VillageDoorInfo func_179865_b(BlockPos p_179865_1_) {
        VillageDoorInfo var2 = null;
        int var3 = Integer.MAX_VALUE;
        for (VillageDoorInfo var5 : this.villageDoorInfoList) {
            int var6 = var5.func_179848_a(p_179865_1_);
            if (var6 >= var3) continue;
            var2 = var5;
            var3 = var6;
        }
        return var2;
    }

    public VillageDoorInfo func_179863_c(BlockPos p_179863_1_) {
        VillageDoorInfo var2 = null;
        int var3 = Integer.MAX_VALUE;
        for (VillageDoorInfo var5 : this.villageDoorInfoList) {
            int var6 = var5.func_179848_a(p_179863_1_);
            var6 = var6 > 256 ? (var6 *= 1000) : var5.getDoorOpeningRestrictionCounter();
            if (var6 >= var3) continue;
            var2 = var5;
            var3 = var6;
        }
        return var2;
    }

    public VillageDoorInfo func_179864_e(BlockPos p_179864_1_) {
        VillageDoorInfo var3;
        if (this.center.distanceSq(p_179864_1_) > (double)(this.villageRadius * this.villageRadius)) {
            return null;
        }
        Iterator var2 = this.villageDoorInfoList.iterator();
        do {
            if (var2.hasNext()) continue;
            return null;
        } while ((var3 = (VillageDoorInfo)var2.next()).func_179852_d().getX() != p_179864_1_.getX() || var3.func_179852_d().getZ() != p_179864_1_.getZ() || Math.abs(var3.func_179852_d().getY() - p_179864_1_.getY()) > 1);
        return var3;
    }

    public void addVillageDoorInfo(VillageDoorInfo p_75576_1_) {
        this.villageDoorInfoList.add(p_75576_1_);
        this.centerHelper = this.centerHelper.add(p_75576_1_.func_179852_d());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = p_75576_1_.getInsidePosY();
    }

    public boolean isAnnihilated() {
        return this.villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLivingBase p_75575_1_) {
        VillageAgressor var3;
        Iterator var2 = this.villageAgressors.iterator();
        do {
            if (!var2.hasNext()) {
                this.villageAgressors.add(new VillageAgressor(p_75575_1_, this.tickCounter));
                return;
            }
            var3 = (VillageAgressor)var2.next();
        } while (var3.agressor != p_75575_1_);
        var3.agressionTime = this.tickCounter;
    }

    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase p_75571_1_) {
        double var2 = Double.MAX_VALUE;
        VillageAgressor var4 = null;
        for (int var5 = 0; var5 < this.villageAgressors.size(); ++var5) {
            VillageAgressor var6 = (VillageAgressor)this.villageAgressors.get(var5);
            double var7 = var6.agressor.getDistanceSqToEntity(p_75571_1_);
            if (!(var7 <= var2)) continue;
            var4 = var6;
            var2 = var7;
        }
        return var4 != null ? var4.agressor : null;
    }

    public EntityPlayer func_82685_c(EntityLivingBase p_82685_1_) {
        double var2 = Double.MAX_VALUE;
        EntityPlayer var4 = null;
        for (String var6 : this.playerReputation.keySet()) {
            double var8;
            EntityPlayer var7;
            if (!this.isPlayerReputationTooLow(var6) || (var7 = this.worldObj.getPlayerEntityByName(var6)) == null || !((var8 = var7.getDistanceSqToEntity(p_82685_1_)) <= var2)) continue;
            var4 = var7;
            var2 = var8;
        }
        return var4;
    }

    private void removeDeadAndOldAgressors() {
        Iterator var1 = this.villageAgressors.iterator();
        while (var1.hasNext()) {
            VillageAgressor var2 = (VillageAgressor)var1.next();
            if (var2.agressor.isEntityAlive() && Math.abs(this.tickCounter - var2.agressionTime) <= 300) continue;
            var1.remove();
        }
    }

    private void removeDeadAndOutOfRangeDoors() {
        boolean var1 = false;
        boolean var2 = this.worldObj.rand.nextInt(50) == 0;
        Iterator var3 = this.villageDoorInfoList.iterator();
        while (var3.hasNext()) {
            VillageDoorInfo var4 = (VillageDoorInfo)var3.next();
            if (var2) {
                var4.resetDoorOpeningRestrictionCounter();
            }
            if (this.func_179860_f(var4.func_179852_d()) && Math.abs(this.tickCounter - var4.getInsidePosY()) <= 1200) continue;
            this.centerHelper = this.centerHelper.add(var4.func_179852_d().multiply(-1));
            var1 = true;
            var4.func_179853_a(true);
            var3.remove();
        }
        if (var1) {
            this.updateVillageRadiusAndCenter();
        }
    }

    private boolean func_179860_f(BlockPos p_179860_1_) {
        Block var2 = this.worldObj.getBlockState(p_179860_1_).getBlock();
        return var2 instanceof BlockDoor ? var2.getMaterial() == Material.wood : false;
    }

    private void updateVillageRadiusAndCenter() {
        int var1 = this.villageDoorInfoList.size();
        if (var1 == 0) {
            this.center = new BlockPos(0, 0, 0);
            this.villageRadius = 0;
        } else {
            this.center = new BlockPos(this.centerHelper.getX() / var1, this.centerHelper.getY() / var1, this.centerHelper.getZ() / var1);
            int var2 = 0;
            for (VillageDoorInfo var4 : this.villageDoorInfoList) {
                var2 = Math.max(var4.func_179848_a(this.center), var2);
            }
            this.villageRadius = Math.max(32, (int)Math.sqrt(var2) + 1);
        }
    }

    public int getReputationForPlayer(String p_82684_1_) {
        Integer var2 = (Integer)this.playerReputation.get(p_82684_1_);
        return var2 != null ? var2 : 0;
    }

    public int setReputationForPlayer(String p_82688_1_, int p_82688_2_) {
        int var3 = this.getReputationForPlayer(p_82688_1_);
        int var4 = MathHelper.clamp_int(var3 + p_82688_2_, -30, 10);
        this.playerReputation.put(p_82688_1_, var4);
        return var4;
    }

    public boolean isPlayerReputationTooLow(String p_82687_1_) {
        return this.getReputationForPlayer(p_82687_1_) <= -15;
    }

    public void readVillageDataFromNBT(NBTTagCompound p_82690_1_) {
        this.numVillagers = p_82690_1_.getInteger("PopSize");
        this.villageRadius = p_82690_1_.getInteger("Radius");
        this.numIronGolems = p_82690_1_.getInteger("Golems");
        this.lastAddDoorTimestamp = p_82690_1_.getInteger("Stable");
        this.tickCounter = p_82690_1_.getInteger("Tick");
        this.noBreedTicks = p_82690_1_.getInteger("MTick");
        this.center = new BlockPos(p_82690_1_.getInteger("CX"), p_82690_1_.getInteger("CY"), p_82690_1_.getInteger("CZ"));
        this.centerHelper = new BlockPos(p_82690_1_.getInteger("ACX"), p_82690_1_.getInteger("ACY"), p_82690_1_.getInteger("ACZ"));
        NBTTagList var2 = p_82690_1_.getTagList("Doors", 10);
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            VillageDoorInfo var5 = new VillageDoorInfo(new BlockPos(var4.getInteger("X"), var4.getInteger("Y"), var4.getInteger("Z")), var4.getInteger("IDX"), var4.getInteger("IDZ"), var4.getInteger("TS"));
            this.villageDoorInfoList.add(var5);
        }
        NBTTagList var6 = p_82690_1_.getTagList("Players", 10);
        for (int var7 = 0; var7 < var6.tagCount(); ++var7) {
            NBTTagCompound var8 = var6.getCompoundTagAt(var7);
            this.playerReputation.put(var8.getString("Name"), var8.getInteger("S"));
        }
    }

    public void writeVillageDataToNBT(NBTTagCompound p_82689_1_) {
        p_82689_1_.setInteger("PopSize", this.numVillagers);
        p_82689_1_.setInteger("Radius", this.villageRadius);
        p_82689_1_.setInteger("Golems", this.numIronGolems);
        p_82689_1_.setInteger("Stable", this.lastAddDoorTimestamp);
        p_82689_1_.setInteger("Tick", this.tickCounter);
        p_82689_1_.setInteger("MTick", this.noBreedTicks);
        p_82689_1_.setInteger("CX", this.center.getX());
        p_82689_1_.setInteger("CY", this.center.getY());
        p_82689_1_.setInteger("CZ", this.center.getZ());
        p_82689_1_.setInteger("ACX", this.centerHelper.getX());
        p_82689_1_.setInteger("ACY", this.centerHelper.getY());
        p_82689_1_.setInteger("ACZ", this.centerHelper.getZ());
        NBTTagList var2 = new NBTTagList();
        for (VillageDoorInfo var4 : this.villageDoorInfoList) {
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setInteger("X", var4.func_179852_d().getX());
            var5.setInteger("Y", var4.func_179852_d().getY());
            var5.setInteger("Z", var4.func_179852_d().getZ());
            var5.setInteger("IDX", var4.func_179847_f());
            var5.setInteger("IDZ", var4.func_179855_g());
            var5.setInteger("TS", var4.getInsidePosY());
            var2.appendTag(var5);
        }
        p_82689_1_.setTag("Doors", var2);
        NBTTagList var7 = new NBTTagList();
        for (String var9 : this.playerReputation.keySet()) {
            NBTTagCompound var6 = new NBTTagCompound();
            var6.setString("Name", var9);
            var6.setInteger("S", (Integer)this.playerReputation.get(var9));
            var7.appendTag(var6);
        }
        p_82689_1_.setTag("Players", var7);
    }

    public void endMatingSeason() {
        this.noBreedTicks = this.tickCounter;
    }

    public boolean isMatingSeason() {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }

    public void setDefaultPlayerReputation(int p_82683_1_) {
        for (String var3 : this.playerReputation.keySet()) {
            this.setReputationForPlayer(var3, p_82683_1_);
        }
    }

    class VillageAgressor {
        public EntityLivingBase agressor;
        public int agressionTime;
        private static final String __OBFID = "CL_00001632";

        VillageAgressor(EntityLivingBase p_i1674_2_, int p_i1674_3_) {
            this.agressor = p_i1674_2_;
            this.agressionTime = p_i1674_3_;
        }
    }
}

