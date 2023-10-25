/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererNeighborsUpdate
implements DebugRenderer.IDebugRenderer {
    private final Minecraft field_191554_a;
    private final Map<Long, Map<BlockPos, Integer>> field_191555_b = Maps.newTreeMap(Ordering.natural().reverse());

    DebugRendererNeighborsUpdate(Minecraft p_i47365_1_) {
        this.field_191554_a = p_i47365_1_;
    }

    public void func_191553_a(long p_191553_1_, BlockPos p_191553_3_) {
        Integer integer;
        Map<BlockPos, Integer> map = this.field_191555_b.get(p_191553_1_);
        if (map == null) {
            map = Maps.newHashMap();
            this.field_191555_b.put(p_191553_1_, map);
        }
        if ((integer = map.get(p_191553_3_)) == null) {
            integer = 0;
        }
        map.put(p_191553_3_, integer + 1);
    }

    @Override
    public void render(float p_190060_1_, long p_190060_2_) {
        long i = this.field_191554_a.world.getTotalWorldTime();
        EntityPlayerSP entityplayer = this.field_191554_a.player;
        double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)p_190060_1_;
        double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)p_190060_1_;
        double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)p_190060_1_;
        World world = this.field_191554_a.player.world;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        int j = 200;
        double d3 = 0.0025;
        HashSet<BlockPos> set = Sets.newHashSet();
        HashMap<BlockPos, Integer> map = Maps.newHashMap();
        Iterator<Map.Entry<Long, Map<BlockPos, Integer>>> iterator = this.field_191555_b.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Map<BlockPos, Integer>> entry = iterator.next();
            Long olong = entry.getKey();
            Map<BlockPos, Integer> map1 = entry.getValue();
            long k = i - olong;
            if (k > 200L) {
                iterator.remove();
                continue;
            }
            for (Map.Entry<BlockPos, Integer> entry1 : map1.entrySet()) {
                BlockPos blockpos = entry1.getKey();
                Integer integer = entry1.getValue();
                if (!set.add(blockpos)) continue;
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(BlockPos.ORIGIN).expandXyz(0.002).contract(0.0025 * (double)k).offset(blockpos.getX(), blockpos.getY(), blockpos.getZ()).offset(-d0, -d1, -d2), 1.0f, 1.0f, 1.0f, 1.0f);
                map.put(blockpos, integer);
            }
        }
        for (Map.Entry entry2 : map.entrySet()) {
            BlockPos blockpos1 = (BlockPos)entry2.getKey();
            Integer integer1 = (Integer)entry2.getValue();
            DebugRenderer.func_191556_a(String.valueOf(integer1), blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), p_190060_1_, -1);
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}

