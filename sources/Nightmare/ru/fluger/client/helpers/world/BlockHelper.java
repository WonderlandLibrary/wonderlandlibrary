// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.world;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3f;
import ru.fluger.client.helpers.Helper;

public class BlockHelper implements Helper
{
    public static aow getBlock(final int x, final int y, final int z) {
        return BlockHelper.mc.f.o(new et(x, y, z)).u();
    }
    
    public static aow getBlock(final et pos) {
        return getState(pos).u();
    }
    
    public static awt getState(final et pos) {
        return BlockHelper.mc.f.o(pos);
    }
    
    public static et getPlayerPosLocal() {
        if (BlockHelper.mc.h == null) {
            return et.a;
        }
        return new et(Math.floor(BlockHelper.mc.h.p), Math.floor(BlockHelper.mc.h.q), Math.floor(BlockHelper.mc.h.r));
    }
    
    public static boolean insideBlock() {
        for (int x = rk.c(Helper.mc.h.av.a); x < rk.c(Helper.mc.h.av.d) + 1; ++x) {
            for (int y = rk.c(Helper.mc.h.av.b); y < rk.c(Helper.mc.h.av.e) + 1; ++y) {
                for (int z = rk.c(Helper.mc.h.av.c); z < rk.c(Helper.mc.h.av.f) + 1; ++z) {
                    final aow block = Helper.mc.f.o(new et(x, y, z)).u();
                    if (block != null) {
                        if (!(block instanceof aom)) {
                            bhb boundingBox = block.a(Helper.mc.f.o(new et(x, y, z)), (amy)Helper.mc.f, new et(x, y, z));
                            if (block instanceof arl) {
                                boundingBox = new bhb(x, y, z, x + 1, y + 1, z + 1);
                            }
                            if (boundingBox != null) {
                                if (Helper.mc.h.av.c(boundingBox)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean blockIsSlipperiness() {
        return BlockHelper.mc.f.o(new et(BlockHelper.mc.h.p, BlockHelper.mc.h.q - 1.0, BlockHelper.mc.h.r)).u().z == 0.98f;
    }
    
    public static boolean rayTrace(final et pos, final float yaw, final float pitch) {
        final bhe vec3d = BlockHelper.mc.h.f(1.0f);
        final bhe vec3d2 = vg.f(pitch, yaw);
        final bhe vec3d3 = vec3d.b(vec3d2.b * 5.0, vec3d2.c * 5.0, vec3d2.d * 5.0);
        final bhc result = BlockHelper.mc.f.a(vec3d, vec3d3, false);
        return result != null && result.a == bhc.a.b && pos.equals(result.a());
    }
    
    public static boolean isAboveLiquid(final vg entity, final double posY) {
        if (entity == null) {
            return false;
        }
        final double n = entity.q + posY;
        for (int i = rk.c(entity.p); i < rk.f(entity.p); ++i) {
            for (int j = rk.c(entity.r); j < rk.f(entity.r); ++j) {
                if (BlockHelper.mc.f.o(new et(i, (int)n, j)).u() instanceof aru) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean collideBlock(final bhb axisAlignedBB, final float boxYSize, final ICollide collide) {
        for (int x = rk.c(BlockHelper.mc.h.bw().a); x < rk.c(BlockHelper.mc.h.bw().d) + 1; ++x) {
            for (int z = rk.c(BlockHelper.mc.h.bw().c); z < rk.c(BlockHelper.mc.h.bw().f) + 1; ++z) {
                final aow block = getBlock(new et(x, axisAlignedBB.b + boxYSize, z));
                if (!collide.collideBlock(block)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean collideBlockIntersects(final bhb axisAlignedBB, final ICollide collide) {
        for (int x = rk.c(BlockHelper.mc.h.bw().a); x < rk.c(BlockHelper.mc.h.bw().d) + 1; ++x) {
            for (int z = rk.c(BlockHelper.mc.h.bw().c); z < rk.c(BlockHelper.mc.h.bw().f) + 1; ++z) {
                final et blockPos = new et(x, axisAlignedBB.b, z);
                final aow block = getBlock(blockPos);
                final bhb boundingBox;
                if (block != null && collide.collideBlock(block) && (boundingBox = block.a(getState(blockPos), (amy)BlockHelper.mc.f, blockPos)) != null && BlockHelper.mc.h.bw().c(boundingBox)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Vector3f getBlock(final float radius, final int block) {
        Vector3f vector3f = null;
        float dist = radius;
        for (float i = radius; i >= -radius; --i) {
            for (float j = -radius; j <= radius; ++j) {
                for (float k = radius; k >= -radius; --k) {
                    final int posX = (int)(BlockHelper.mc.h.p + i);
                    final int posY = (int)(BlockHelper.mc.h.q + j);
                    final int posZ = (int)(BlockHelper.mc.h.r + k);
                    final float curDist = (float)BlockHelper.mc.h.e(posX, posY, posZ);
                    if (aow.a(getBlock(posX, posY - 1, posZ)) == block && getBlock(posX, posY, posZ) instanceof aom) {
                        if (curDist <= dist) {
                            dist = curDist;
                            vector3f = new Vector3f((float)posX, (float)posY, (float)posZ);
                        }
                    }
                }
            }
        }
        return vector3f;
    }
    
    public static boolean IsValidBlockPos(final et pos) {
        final awt state = BlockHelper.mc.f.o(pos);
        return (state.u() instanceof apy || (state.u() instanceof arb && !(state.u() instanceof aqn))) && BlockHelper.mc.f.o(pos.a()).u() == aox.a;
    }
    
    public static List<et> getSphere(final et loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<et> circulate = new ArrayList<et>();
        final int cx = loc.p();
        final int cy = loc.q();
        final int cz = loc.r();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f2;
                    final float f = f2 = (sphere ? (cy + r) : ((float)(cy + h)));
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final et l = new et(x, y + plus_y, z);
                        circulate.add(l);
                    }
                    ++y;
                }
            }
        }
        return circulate;
    }
    
    public static ArrayList<et> getBlocks(final int x, final int y, final int z) {
        final et min = new et(BlockHelper.mc.h.p - x, BlockHelper.mc.h.q - y, BlockHelper.mc.h.r - z);
        final et max = new et(BlockHelper.mc.h.p + x, BlockHelper.mc.h.q + y, BlockHelper.mc.h.r + z);
        return getAllInBox(min, max);
    }
    
    public static ArrayList<et> getAllInBox(final et from, final et to) {
        final ArrayList<et> blocks = new ArrayList<et>();
        final et min = new et(Math.min(from.p(), to.p()), Math.min(from.q(), to.q()), Math.min(from.r(), to.r()));
        final et max = new et(Math.max(from.p(), to.p()), Math.max(from.q(), to.q()), Math.max(from.r(), to.r()));
        for (int x = min.p(); x <= max.p(); ++x) {
            for (int y = min.q(); y <= max.q(); ++y) {
                for (int z = min.r(); z <= max.r(); ++z) {
                    blocks.add(new et(x, y, z));
                }
            }
        }
        return blocks;
    }
    
    public interface ICollide
    {
        boolean collideBlock(final aow p0);
    }
}
