// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.storage;

import java.util.Collection;
import net.minecraft.network.play.server.S34PacketMaps;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.util.Vec4b;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;
import net.minecraft.world.WorldSavedData;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public byte scale;
    public byte[] colors;
    public List playersArrayList;
    private Map playersHashMap;
    public Map playersVisibleOnMap;
    private static final String __OBFID = "CL_00000577";
    
    public MapData(final String p_i2140_1_) {
        super(p_i2140_1_);
        this.colors = new byte[16384];
        this.playersArrayList = Lists.newArrayList();
        this.playersHashMap = Maps.newHashMap();
        this.playersVisibleOnMap = Maps.newLinkedHashMap();
    }
    
    public void func_176054_a(final double p_176054_1_, final double p_176054_3_, final int p_176054_5_) {
        final int var6 = 128 * (1 << p_176054_5_);
        final int var7 = MathHelper.floor_double((p_176054_1_ + 64.0) / var6);
        final int var8 = MathHelper.floor_double((p_176054_3_ + 64.0) / var6);
        this.xCenter = var7 * var6 + var6 / 2 - 64;
        this.zCenter = var8 * var6 + var6 / 2 - 64;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.dimension = nbt.getByte("dimension");
        this.xCenter = nbt.getInteger("xCenter");
        this.zCenter = nbt.getInteger("zCenter");
        this.scale = nbt.getByte("scale");
        this.scale = (byte)MathHelper.clamp_int(this.scale, 0, 4);
        final short var2 = nbt.getShort("width");
        final short var3 = nbt.getShort("height");
        if (var2 == 128 && var3 == 128) {
            this.colors = nbt.getByteArray("colors");
        }
        else {
            final byte[] var4 = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            final int var5 = (128 - var2) / 2;
            final int var6 = (128 - var3) / 2;
            for (int var7 = 0; var7 < var3; ++var7) {
                final int var8 = var7 + var6;
                if (var8 >= 0 || var8 < 128) {
                    for (int var9 = 0; var9 < var2; ++var9) {
                        final int var10 = var9 + var5;
                        if (var10 >= 0 || var10 < 128) {
                            this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setByte("dimension", this.dimension);
        nbt.setInteger("xCenter", this.xCenter);
        nbt.setInteger("zCenter", this.zCenter);
        nbt.setByte("scale", this.scale);
        nbt.setShort("width", (short)128);
        nbt.setShort("height", (short)128);
        nbt.setByteArray("colors", this.colors);
    }
    
    public void updateVisiblePlayers(final EntityPlayer p_76191_1_, final ItemStack p_76191_2_) {
        if (!this.playersHashMap.containsKey(p_76191_1_)) {
            final MapInfo var3 = new MapInfo(p_76191_1_);
            this.playersHashMap.put(p_76191_1_, var3);
            this.playersArrayList.add(var3);
        }
        if (!p_76191_1_.inventory.hasItemStack(p_76191_2_)) {
            this.playersVisibleOnMap.remove(p_76191_1_.getName());
        }
        for (int var4 = 0; var4 < this.playersArrayList.size(); ++var4) {
            final MapInfo var5 = this.playersArrayList.get(var4);
            if (!var5.entityplayerObj.isDead && (var5.entityplayerObj.inventory.hasItemStack(p_76191_2_) || p_76191_2_.isOnItemFrame())) {
                if (!p_76191_2_.isOnItemFrame() && var5.entityplayerObj.dimension == this.dimension) {
                    this.func_82567_a(0, var5.entityplayerObj.worldObj, var5.entityplayerObj.getName(), var5.entityplayerObj.posX, var5.entityplayerObj.posZ, var5.entityplayerObj.rotationYaw);
                }
            }
            else {
                this.playersHashMap.remove(var5.entityplayerObj);
                this.playersArrayList.remove(var5);
            }
        }
        if (p_76191_2_.isOnItemFrame()) {
            final EntityItemFrame var6 = p_76191_2_.getItemFrame();
            final BlockPos var7 = var6.func_174857_n();
            this.func_82567_a(1, p_76191_1_.worldObj, "frame-" + var6.getEntityId(), var7.getX(), var7.getZ(), var6.field_174860_b.getHorizontalIndex() * 90);
        }
        if (p_76191_2_.hasTagCompound() && p_76191_2_.getTagCompound().hasKey("Decorations", 9)) {
            final NBTTagList var8 = p_76191_2_.getTagCompound().getTagList("Decorations", 10);
            for (int var9 = 0; var9 < var8.tagCount(); ++var9) {
                final NBTTagCompound var10 = var8.getCompoundTagAt(var9);
                if (!this.playersVisibleOnMap.containsKey(var10.getString("id"))) {
                    this.func_82567_a(var10.getByte("type"), p_76191_1_.worldObj, var10.getString("id"), var10.getDouble("x"), var10.getDouble("z"), var10.getDouble("rot"));
                }
            }
        }
    }
    
    private void func_82567_a(int p_82567_1_, final World worldIn, final String p_82567_3_, final double p_82567_4_, final double p_82567_6_, double p_82567_8_) {
        final int var10 = 1 << this.scale;
        final float var11 = (float)(p_82567_4_ - this.xCenter) / var10;
        final float var12 = (float)(p_82567_6_ - this.zCenter) / var10;
        byte var13 = (byte)(var11 * 2.0f + 0.5);
        byte var14 = (byte)(var12 * 2.0f + 0.5);
        final byte var15 = 63;
        byte var16;
        if (var11 >= -var15 && var12 >= -var15 && var11 <= var15 && var12 <= var15) {
            p_82567_8_ += ((p_82567_8_ < 0.0) ? -8.0 : 8.0);
            var16 = (byte)(p_82567_8_ * 16.0 / 360.0);
            if (this.dimension < 0) {
                final int var17 = (int)(worldIn.getWorldInfo().getWorldTime() / 10L);
                var16 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 0xF);
            }
        }
        else {
            if (Math.abs(var11) >= 320.0f || Math.abs(var12) >= 320.0f) {
                this.playersVisibleOnMap.remove(p_82567_3_);
                return;
            }
            p_82567_1_ = 6;
            var16 = 0;
            if (var11 <= -var15) {
                var13 = (byte)(var15 * 2 + 2.5);
            }
            if (var12 <= -var15) {
                var14 = (byte)(var15 * 2 + 2.5);
            }
            if (var11 >= var15) {
                var13 = (byte)(var15 * 2 + 1);
            }
            if (var12 >= var15) {
                var14 = (byte)(var15 * 2 + 1);
            }
        }
        this.playersVisibleOnMap.put(p_82567_3_, new Vec4b((byte)p_82567_1_, var13, var14, var16));
    }
    
    public Packet func_176052_a(final ItemStack p_176052_1_, final World worldIn, final EntityPlayer p_176052_3_) {
        final MapInfo var4 = this.playersHashMap.get(p_176052_3_);
        return (var4 == null) ? null : var4.func_176101_a(p_176052_1_);
    }
    
    public void func_176053_a(final int p_176053_1_, final int p_176053_2_) {
        super.markDirty();
        for (final MapInfo var4 : this.playersArrayList) {
            var4.func_176102_a(p_176053_1_, p_176053_2_);
        }
    }
    
    public MapInfo func_82568_a(final EntityPlayer p_82568_1_) {
        MapInfo var2 = this.playersHashMap.get(p_82568_1_);
        if (var2 == null) {
            var2 = new MapInfo(p_82568_1_);
            this.playersHashMap.put(p_82568_1_, var2);
            this.playersArrayList.add(var2);
        }
        return var2;
    }
    
    public class MapInfo
    {
        public final EntityPlayer entityplayerObj;
        private boolean field_176105_d;
        private int field_176106_e;
        private int field_176103_f;
        private int field_176104_g;
        private int field_176108_h;
        private int field_176109_i;
        public int field_82569_d;
        private static final String __OBFID = "CL_00000578";
        
        public MapInfo(final EntityPlayer p_i2138_2_) {
            this.field_176105_d = true;
            this.field_176106_e = 0;
            this.field_176103_f = 0;
            this.field_176104_g = 127;
            this.field_176108_h = 127;
            this.entityplayerObj = p_i2138_2_;
        }
        
        public Packet func_176101_a(final ItemStack p_176101_1_) {
            if (this.field_176105_d) {
                this.field_176105_d = false;
                return new S34PacketMaps(p_176101_1_.getMetadata(), MapData.this.scale, MapData.this.playersVisibleOnMap.values(), MapData.this.colors, this.field_176106_e, this.field_176103_f, this.field_176104_g + 1 - this.field_176106_e, this.field_176108_h + 1 - this.field_176103_f);
            }
            return (this.field_176109_i++ % 5 == 0) ? new S34PacketMaps(p_176101_1_.getMetadata(), MapData.this.scale, MapData.this.playersVisibleOnMap.values(), MapData.this.colors, 0, 0, 0, 0) : null;
        }
        
        public void func_176102_a(final int p_176102_1_, final int p_176102_2_) {
            if (this.field_176105_d) {
                this.field_176106_e = Math.min(this.field_176106_e, p_176102_1_);
                this.field_176103_f = Math.min(this.field_176103_f, p_176102_2_);
                this.field_176104_g = Math.max(this.field_176104_g, p_176102_1_);
                this.field_176108_h = Math.max(this.field_176108_h, p_176102_2_);
            }
            else {
                this.field_176105_d = true;
                this.field_176106_e = p_176102_1_;
                this.field_176103_f = p_176102_2_;
                this.field_176104_g = p_176102_1_;
                this.field_176108_h = p_176102_2_;
            }
        }
    }
}
