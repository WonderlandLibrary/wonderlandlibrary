/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityPainting
extends EntityHanging {
    public EnumArt art;

    public EntityPainting(World world, BlockPos blockPos, EnumFacing enumFacing, String string) {
        this(world, blockPos, enumFacing);
        EnumArt[] enumArtArray = EnumArt.values();
        int n = enumArtArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumArt enumArt = enumArtArray[n2];
            if (enumArt.title.equals(string)) {
                this.art = enumArt;
                break;
            }
            ++n2;
        }
        this.updateFacingWithBoundingBox(enumFacing);
    }

    @Override
    public int getWidthPixels() {
        return this.art.sizeX;
    }

    @Override
    public int getHeightPixels() {
        return this.art.sizeY;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setString("Motive", this.art.title);
        super.writeEntityToNBT(nBTTagCompound);
    }

    public EntityPainting(World world) {
        super(world);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        String string = nBTTagCompound.getString("Motive");
        EnumArt[] enumArtArray = EnumArt.values();
        int n = enumArtArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumArt enumArt = enumArtArray[n2];
            if (enumArt.title.equals(string)) {
                this.art = enumArt;
            }
            ++n2;
        }
        if (this.art == null) {
            this.art = EnumArt.KEBAB;
        }
        super.readEntityFromNBT(nBTTagCompound);
    }

    @Override
    public void setLocationAndAngles(double d, double d2, double d3, float f, float f2) {
        BlockPos blockPos = this.hangingPosition.add(d - this.posX, d2 - this.posY, d3 - this.posZ);
        this.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public void onBroken(Entity entity) {
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer)entity;
                if (entityPlayer.capabilities.isCreativeMode) {
                    return;
                }
            }
            this.entityDropItem(new ItemStack(Items.painting), 0.0f);
        }
    }

    @Override
    public void setPositionAndRotation2(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
        BlockPos blockPos = this.hangingPosition.add(d - this.posX, d2 - this.posY, d3 - this.posZ);
        this.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public EntityPainting(World world, BlockPos blockPos, EnumFacing enumFacing) {
        super(world, blockPos);
        ArrayList arrayList = Lists.newArrayList();
        EnumArt[] enumArtArray = EnumArt.values();
        int n = enumArtArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumArt enumArt;
            this.art = enumArt = enumArtArray[n2];
            this.updateFacingWithBoundingBox(enumFacing);
            if (this.onValidSurface()) {
                arrayList.add(enumArt);
            }
            ++n2;
        }
        if (!arrayList.isEmpty()) {
            this.art = (EnumArt)((Object)arrayList.get(this.rand.nextInt(arrayList.size())));
        }
        this.updateFacingWithBoundingBox(enumFacing);
    }

    public static enum EnumArt {
        KEBAB("Kebab", 16, 16, 0, 0),
        AZTEC("Aztec", 16, 16, 16, 0),
        ALBAN("Alban", 16, 16, 32, 0),
        AZTEC_2("Aztec2", 16, 16, 48, 0),
        BOMB("Bomb", 16, 16, 64, 0),
        PLANT("Plant", 16, 16, 80, 0),
        WASTELAND("Wasteland", 16, 16, 96, 0),
        POOL("Pool", 32, 16, 0, 32),
        COURBET("Courbet", 32, 16, 32, 32),
        SEA("Sea", 32, 16, 64, 32),
        SUNSET("Sunset", 32, 16, 96, 32),
        CREEBET("Creebet", 32, 16, 128, 32),
        WANDERER("Wanderer", 16, 32, 0, 64),
        GRAHAM("Graham", 16, 32, 16, 64),
        MATCH("Match", 32, 32, 0, 128),
        BUST("Bust", 32, 32, 32, 128),
        STAGE("Stage", 32, 32, 64, 128),
        VOID("Void", 32, 32, 96, 128),
        SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128),
        WITHER("Wither", 32, 32, 160, 128),
        FIGHTERS("Fighters", 64, 32, 0, 96),
        POINTER("Pointer", 64, 64, 0, 192),
        PIGSCENE("Pigscene", 64, 64, 64, 192),
        BURNING_SKULL("BurningSkull", 64, 64, 128, 192),
        SKELETON("Skeleton", 64, 48, 192, 64),
        DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

        public final int offsetX;
        public final String title;
        public final int offsetY;
        public final int sizeY;
        public static final int field_180001_A;
        public final int sizeX;

        static {
            field_180001_A = 13;
        }

        private EnumArt(String string2, int n2, int n3, int n4, int n5) {
            this.title = string2;
            this.sizeX = n2;
            this.sizeY = n3;
            this.offsetX = n4;
            this.offsetY = n5;
        }
    }
}

