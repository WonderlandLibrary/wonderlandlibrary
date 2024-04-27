/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSetMultimap
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.profiler.Profiler
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.ChunkCoordIntPair
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.IWorldAccess
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldProvider
 *  net.minecraft.world.WorldType
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.storage.WorldInfo
 *  net.minecraftforge.common.ForgeChunkManager$Ticket
 *  net.minecraftforge.common.util.BlockSnapshot
 */
package net.dev.important.injection.forge.mixins.optimize;

import com.google.common.collect.ImmutableSetMultimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.dev.important.injection.access.IBlock;
import net.dev.important.injection.access.IChunk;
import net.dev.important.injection.access.IMixinWorldAccess;
import net.dev.important.injection.access.IWorld;
import net.dev.important.injection.access.StaticStorage;
import net.dev.important.modules.module.modules.misc.Performance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.BlockSnapshot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={World.class})
public abstract class MixinWorld
implements IWorld {
    @Shadow
    @Final
    public WorldProvider field_73011_w;
    @Shadow
    private int field_73008_k;
    @Shadow
    @Final
    public boolean field_72995_K;
    @Shadow
    protected WorldInfo field_72986_A;
    @Shadow
    public boolean captureBlockSnapshots;
    @Shadow
    public ArrayList<BlockSnapshot> capturedBlockSnapshots;
    @Shadow
    @Final
    public Profiler field_72984_F;
    @Shadow
    protected List<IWorldAccess> field_73021_x;
    @Shadow
    int[] field_72994_J;
    @Shadow
    protected Set<ChunkCoordIntPair> field_72993_I;
    @Shadow
    @Final
    public List<EntityPlayer> field_73010_i;
    @Shadow
    private int field_72990_M;
    @Shadow
    @Final
    public Random field_73012_v;

    @Shadow
    protected abstract boolean func_175680_a(int var1, int var2, boolean var3);

    @Shadow
    public abstract Chunk func_72964_e(int var1, int var2);

    @Shadow
    protected abstract boolean func_175663_a(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7);

    @Shadow
    public abstract ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> getPersistentChunks();

    @Shadow
    protected abstract int func_152379_p();

    @Inject(method={"setActivePlayerChunksAndCheckLight"}, at={@At(value="HEAD")}, cancellable=true)
    private void setActivePlayerChunksAndCheckLight(CallbackInfo callbackInfo) {
        if (((Boolean)Performance.fastBlockLightningValue.get()).booleanValue()) {
            int n;
            int n2;
            int n3;
            callbackInfo.cancel();
            this.field_72993_I.clear();
            this.field_72984_F.func_76320_a("buildList");
            this.field_72993_I.addAll((Collection<ChunkCoordIntPair>)this.getPersistentChunks().keySet());
            for (EntityPlayer entityPlayer : this.field_73010_i) {
                n3 = MathHelper.func_76128_c((double)(entityPlayer.field_70165_t / 16.0));
                n2 = MathHelper.func_76128_c((double)(entityPlayer.field_70161_v / 16.0));
                n = this.func_152379_p();
                for (int i = -n; i <= n; ++i) {
                    for (int j = -n; j <= n; ++j) {
                        this.field_72993_I.add(new ChunkCoordIntPair(i + n3, j + n2));
                    }
                }
            }
            this.field_72984_F.func_76319_b();
            if (this.field_72990_M > 0) {
                --this.field_72990_M;
            }
            this.field_72984_F.func_76320_a("playerCheckLight");
            if (!this.field_73010_i.isEmpty()) {
                int n4 = this.field_73012_v.nextInt(this.field_73010_i.size());
                EntityPlayer entityPlayer = this.field_73010_i.get(n4);
                n3 = MathHelper.func_76128_c((double)entityPlayer.field_70165_t) + this.field_73012_v.nextInt(11) - 5;
                n2 = MathHelper.func_76128_c((double)entityPlayer.field_70163_u) + this.field_73012_v.nextInt(11) - 5;
                n = MathHelper.func_76128_c((double)entityPlayer.field_70161_v) + this.field_73012_v.nextInt(11) - 5;
                this.checkLight(n3, n2, n);
            }
            this.field_72984_F.func_76319_b();
        }
    }

    @Override
    public boolean isAreaLoaded(int n, int n2, int n3, int n4, boolean bl) {
        return this.func_175663_a(n - n4, n2 - n4, n3 - n4, n + n4, n2 + n4, n3 + n4, bl);
    }

    @Override
    public boolean isBlockLoaded(int n, int n2, int n3) {
        return this.isBlockLoaded(n, n2, n3, true);
    }

    @Override
    public boolean isBlockLoaded(int n, int n2, int n3, boolean bl) {
        return this.isValid(n, n2, n3) && this.func_175680_a(n >> 4, n3 >> 4, bl);
    }

    @Override
    public boolean isValid(int n, int n2, int n3) {
        return n >= -30000000 && n3 >= -30000000 && n < 30000000 && n3 < 30000000 && n2 >= 0 && n2 < 256;
    }

    @Override
    public boolean canSeeSky(int n, int n2, int n3) {
        return ((IChunk)this.getChunkFromBlockCoords(n, n2, n3)).canSeeSky(n, n2, n3);
    }

    @Override
    public int getCombinedLight(int n, int n2, int n3, int n4) {
        int n5 = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, n, n2, n3);
        int n6 = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, n, n2, n3);
        if (n6 < n4) {
            n6 = n4;
        }
        return n5 << 20 | n6 << 4;
    }

    @Override
    public int getRawLight(int n, int n2, int n3, EnumSkyBlock enumSkyBlock) {
        if (enumSkyBlock == EnumSkyBlock.SKY && this.canSeeSky(n, n2, n3)) {
            return 15;
        }
        IBlock IBlock2 = (IBlock)this.getBlockState(n, n2, n3).func_177230_c();
        int n4 = IBlock2.getLightValue((IBlockAccess)((World)this), n, n2, n3);
        int n5 = enumSkyBlock == EnumSkyBlock.SKY ? 0 : n4;
        int n6 = IBlock2.getLightOpacity((IBlockAccess)((World)this), n, n2, n3);
        if (n6 >= 15 && n4 > 0) {
            n6 = 1;
        }
        if (n6 < 1) {
            n6 = 1;
        }
        if (n6 >= 15) {
            return 0;
        }
        if (n5 >= 14) {
            return n5;
        }
        for (EnumFacing enumFacing : StaticStorage.facings()) {
            int n7 = this.getLightFor(enumSkyBlock, n + enumFacing.func_82601_c(), n2 + enumFacing.func_96559_d(), n3 + enumFacing.func_82599_e()) - n6;
            if (n7 > n5) {
                n5 = n7;
            }
            if (n5 < 14) continue;
            return n5;
        }
        return n5;
    }

    @Override
    public float getLightBrightness(int n, int n2, int n3) {
        return this.field_73011_w.func_177497_p()[this.getLightFromNeighbors(n, n2, n3)];
    }

    @Override
    public int getLight(int n, int n2, int n3, boolean bl) {
        if (n >= -30000000 && n3 >= -30000000 && n < 30000000 && n3 < 30000000) {
            if (bl && this.getBlockState(n, n2, n3).func_177230_c().func_149710_n()) {
                int n4 = this.getLight(n, n2 + 1, n3, false);
                int n5 = this.getLight(n + 1, n2, n3, false);
                int n6 = this.getLight(n - 1, n2, n3, false);
                int n7 = this.getLight(n, n2, n3 + 1, false);
                int n8 = this.getLight(n, n2, n3 - 1, false);
                if (n5 > n4) {
                    n4 = n5;
                }
                if (n6 > n4) {
                    n4 = n6;
                }
                if (n7 > n4) {
                    n4 = n7;
                }
                if (n8 > n4) {
                    n4 = n8;
                }
                return n4;
            }
            if (n2 < 0) {
                return 0;
            }
            if (n2 >= 256) {
                n2 = 255;
            }
            IChunk IChunk2 = (IChunk)this.getChunkFromBlockCoords(n, n2, n3);
            return IChunk2.getLightSubtracted(n, n2, n3, this.field_73008_k);
        }
        return 15;
    }

    @Override
    public int getLightFor(EnumSkyBlock enumSkyBlock, int n, int n2, int n3) {
        if (n2 < 0) {
            n2 = 0;
        }
        if (!this.isValid(n, n2, n3)) {
            return enumSkyBlock.field_77198_c;
        }
        if (!this.isBlockLoaded(n, n2, n3)) {
            return enumSkyBlock.field_77198_c;
        }
        IChunk IChunk2 = (IChunk)this.getChunkFromBlockCoords(n, n2, n3);
        return IChunk2.getLightFor(enumSkyBlock, n, n2, n3);
    }

    @Override
    public int getLightFromNeighbors(int n, int n2, int n3) {
        return this.getLight(n, n2, n3, true);
    }

    @Override
    public int getLightFromNeighborsFor(EnumSkyBlock enumSkyBlock, int n, int n2, int n3) {
        if (this.field_73011_w.func_177495_o() && enumSkyBlock == EnumSkyBlock.SKY) {
            return 0;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (!this.isValid(n, n2, n3)) {
            return enumSkyBlock.field_77198_c;
        }
        if (!this.isBlockLoaded(n, n2, n3)) {
            return enumSkyBlock.field_77198_c;
        }
        if (this.getBlockState(n, n2, n3).func_177230_c().func_149710_n()) {
            int n4 = this.getLightFor(enumSkyBlock, n, n2 + 1, n3);
            int n5 = this.getLightFor(enumSkyBlock, n + 1, n2, n3);
            int n6 = this.getLightFor(enumSkyBlock, n - 1, n2, n3);
            int n7 = this.getLightFor(enumSkyBlock, n, n2, n3 + 1);
            int n8 = this.getLightFor(enumSkyBlock, n, n2, n3 - 1);
            if (n5 > n4) {
                n4 = n5;
            }
            if (n6 > n4) {
                n4 = n6;
            }
            if (n7 > n4) {
                n4 = n7;
            }
            if (n8 > n4) {
                n4 = n8;
            }
            return n4;
        }
        IChunk IChunk2 = (IChunk)this.getChunkFromBlockCoords(n, n2, n3);
        return IChunk2.getLightFor(enumSkyBlock, n, n2, n3);
    }

    @Override
    public void setLightFor(EnumSkyBlock enumSkyBlock, int n, int n2, int n3, int n4) {
        if (this.isValid(n, n2, n3) && this.isBlockLoaded(n, n2, n3)) {
            IChunk IChunk2 = (IChunk)this.getChunkFromBlockCoords(n, n2, n3);
            IChunk2.setLightFor(enumSkyBlock, n, n2, n3, n4);
            this.notifyLightSet(n, n2, n3);
        }
    }

    @Override
    public boolean checkLight(int n, int n2, int n3) {
        boolean bl = false;
        if (!this.field_73011_w.func_177495_o()) {
            bl = this.checkLightFor(EnumSkyBlock.SKY, n, n2, n3);
        }
        return bl | this.checkLightFor(EnumSkyBlock.BLOCK, n, n2, n3);
    }

    @Override
    public boolean checkLightFor(EnumSkyBlock enumSkyBlock, int n, int n2, int n3) {
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        if (!this.isAreaLoaded(n, n2, n3, 17, false)) {
            return false;
        }
        int n13 = 0;
        int n14 = 0;
        this.field_72984_F.func_76320_a("getBrightness");
        int n15 = this.getLightFor(enumSkyBlock, n, n2, n3);
        int n16 = this.getRawLight(n, n2, n3, enumSkyBlock);
        if (n16 > n15) {
            this.field_72994_J[n14++] = 133152;
        } else if (n16 < n15) {
            this.field_72994_J[n14++] = 0x20820 | n15 << 18;
            while (n13 < n14) {
                n12 = this.field_72994_J[n13++];
                n11 = (n12 & 0x3F) - 32 + n;
                n10 = (n12 >> 6 & 0x3F) - 32 + n2;
                n9 = (n12 >> 12 & 0x3F) - 32 + n3;
                n8 = n12 >> 18 & 0xF;
                n7 = this.getLightFor(enumSkyBlock, n11, n10, n9);
                if (n7 != n8) continue;
                this.setLightFor(enumSkyBlock, n11, n10, n9, 0);
                if (n8 <= 0 || MathHelper.func_76130_a((int)(n11 - n)) + MathHelper.func_76130_a((int)(n10 - n2)) + MathHelper.func_76130_a((int)(n9 - n3)) >= 17) continue;
                for (EnumFacing enumFacing : StaticStorage.facings()) {
                    int n17 = n11 + enumFacing.func_82601_c();
                    int n18 = n10 + enumFacing.func_96559_d();
                    int n19 = n9 + enumFacing.func_82599_e();
                    int n20 = Math.max(1, this.getBlockState(n17, n18, n19).func_177230_c().func_149717_k());
                    n7 = this.getLightFor(enumSkyBlock, n17, n18, n19);
                    if (n7 != n8 - n20 || n14 >= this.field_72994_J.length) continue;
                    this.field_72994_J[n14++] = n17 - n + 32 | n18 - n2 + 32 << 6 | n19 - n3 + 32 << 12 | n8 - n20 << 18;
                }
            }
            n13 = 0;
        }
        this.field_72984_F.func_76319_b();
        this.field_72984_F.func_76320_a("checkedPosition < toCheckCount");
        while (n13 < n14) {
            n12 = this.field_72994_J[n13++];
            n11 = (n12 & 0x3F) - 32 + n;
            n10 = (n12 >> 6 & 0x3F) - 32 + n2;
            n9 = (n12 >> 12 & 0x3F) - 32 + n3;
            n8 = this.getLightFor(enumSkyBlock, n11, n10, n9);
            n7 = this.getRawLight(n11, n10, n9, enumSkyBlock);
            if (n7 == n8) continue;
            this.setLightFor(enumSkyBlock, n11, n10, n9, n7);
            if (n7 <= n8) continue;
            int n6 = Math.abs(n11 - n);
            int n5 = Math.abs(n10 - n2);
            int n4 = Math.abs(n9 - n3);
            boolean bl = n14 < this.field_72994_J.length - 6;
            boolean bl2 = bl;
            if (n6 + n5 + n4 >= 17 || !bl) continue;
            if (this.getLightFor(enumSkyBlock, n11 - 1, n10, n9) < n7) {
                this.field_72994_J[n14++] = n11 - 1 - n + 32 + (n10 - n2 + 32 << 6) + (n9 - n3 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, n11 + 1, n10, n9) < n7) {
                this.field_72994_J[n14++] = n11 + 1 - n + 32 + (n10 - n2 + 32 << 6) + (n9 - n3 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, n11, n10 - 1, n9) < n7) {
                this.field_72994_J[n14++] = n11 - n + 32 + (n10 - 1 - n2 + 32 << 6) + (n9 - n3 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, n11, n10 + 1, n9) < n7) {
                this.field_72994_J[n14++] = n11 - n + 32 + (n10 + 1 - n2 + 32 << 6) + (n9 - n3 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, n11, n10, n9 - 1) < n7) {
                this.field_72994_J[n14++] = n11 - n + 32 + (n10 - n2 + 32 << 6) + (n9 - 1 - n3 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, n11, n10, n9 + 1) >= n7) continue;
            this.field_72994_J[n14++] = n11 - n + 32 + (n10 - n2 + 32 << 6) + (n9 + 1 - n3 + 32 << 12);
        }
        this.field_72984_F.func_76319_b();
        return true;
    }

    @Override
    public IBlockState getBlockState(int n, int n2, int n3) {
        if (!this.isValid(n, n2, n3)) {
            return Blocks.field_150350_a.func_176223_P();
        }
        IChunk IChunk2 = (IChunk)this.getChunkFromBlockCoords(n, n2, n3);
        return IChunk2.getBlockState(n, n2, n3);
    }

    @Override
    public boolean setBlockState(int n, int n2, int n3, IBlockState iBlockState, int n4) {
        if (!this.isValid(n, n2, n3)) {
            return false;
        }
        if (!this.field_72995_K && this.field_72986_A.func_76067_t() == WorldType.field_180272_g) {
            return false;
        }
        BlockSnapshot blockSnapshot = null;
        if (this.captureBlockSnapshots && !this.field_72995_K) {
            blockSnapshot = BlockSnapshot.getBlockSnapshot((World)((World)this), (BlockPos)new BlockPos(n, n2, n3), (int)n4);
            this.capturedBlockSnapshots.add(blockSnapshot);
        }
        if (blockSnapshot != null) {
            this.capturedBlockSnapshots.remove(blockSnapshot);
        }
        return false;
    }

    @Override
    public void markBlockForUpdate(int n, int n2, int n3) {
        for (IWorldAccess iWorldAccess : this.field_73021_x) {
            ((IMixinWorldAccess)iWorldAccess).markBlockForUpdate(n, n2, n3);
        }
    }

    @Override
    public void markAndNotifyBlock(int n, int n2, int n3, Chunk chunk, IBlockState iBlockState, IBlockState iBlockState2, int n4) {
        if (!((n4 & 2) == 0 || this.field_72995_K && (n4 & 4) != 0 || chunk != null && !chunk.func_150802_k())) {
            this.markBlockForUpdate(n, n2, n3);
        }
        if (this.field_72995_K || (n4 & 1) == 0 || iBlockState2.func_177230_c().func_149740_M()) {
            // empty if block
        }
    }

    @Override
    public void notifyLightSet(int n, int n2, int n3) {
        for (IWorldAccess iWorldAccess : this.field_73021_x) {
            ((IMixinWorldAccess)iWorldAccess).notifyLightSet(n, n2, n3);
        }
    }

    @Override
    public Chunk getChunkFromBlockCoords(int n, int n2, int n3) {
        return this.func_72964_e(n >> 4, n3 >> 4);
    }
}

