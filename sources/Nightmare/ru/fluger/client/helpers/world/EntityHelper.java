// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.world;

import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.common.base.Predicate;
import java.util.List;
import ru.fluger.client.helpers.Helper;

public class EntityHelper implements Helper
{
    public static List<aow> noSolidBlocks;
    
    public static double getDistance(final double x, final double y, final double z, final double x1, final double y1, final double z1) {
        final double posX = x - x1;
        final double posY = y - y1;
        final double posZ = z - z1;
        return rk.a(posX * posX + posY * posY + posZ * posZ);
    }
    
    public static void damagePlayer(final boolean groundCheck) {
        if (!groundCheck || EntityHelper.mc.h.z) {
            final double x = EntityHelper.mc.h.p;
            final double y = EntityHelper.mc.h.q;
            final double z = EntityHelper.mc.h.r;
            double fallDistanceReq = 3.0;
            if (EntityHelper.mc.h.a(uz.a(8))) {
                final int amplifier = EntityHelper.mc.h.b(uz.a(8)).c();
                fallDistanceReq += amplifier + 1;
            }
            for (int i = 0; i < (int)Math.ceil(fallDistanceReq / 0.0624); ++i) {
                EntityHelper.mc.h.d.a(new lk.a(x, y + 0.0624, z, false));
                EntityHelper.mc.h.d.a(new lk.a(x, y, z, false));
            }
            EntityHelper.mc.h.d.a(new lk(true));
        }
    }
    
    public static boolean intersectsWith(final et pos) {
        return !EntityHelper.mc.f.a((Class<? extends vg>)vg.class, new bhb(pos), (com.google.common.base.Predicate<? super vg>)(v -> v != null && !v.F)).isEmpty();
    }
    
    public static int getBestWeapon() {
        final int originalSlot = EntityHelper.mc.h.bv.d;
        int weaponSlot = -1;
        float weaponDamage = 1.0f;
        for (int slot = 0; slot < 9; slot = (byte)(slot + 1)) {
            EntityHelper.mc.h.bv.d = slot;
            final aip itemStack = EntityHelper.mc.h.b(ub.a);
            if (!(itemStack.c() instanceof ahg) && !(itemStack.c() instanceof ajb)) {
                if (!(itemStack.c() instanceof ajn)) {
                    float damage = (float)getItemDamage(itemStack);
                    if ((damage += alm.a(itemStack, vu.a)) > weaponDamage) {
                        weaponDamage = damage;
                        weaponSlot = slot;
                    }
                }
            }
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }
    
    public static int getRoundedDamage(final aip stack) {
        return (int)getDamageInPercent(stack);
    }
    
    public static float getDamageInPercent(final aip stack) {
        return getItemDamage(stack) / (float)stack.k() * 100.0f;
    }
    
    public static boolean isArmorLow(final aed player, final int durability) {
        for (final aip piece : player.bv.b) {
            if (piece == null) {
                return true;
            }
            if (getItemDamage(piece) >= durability) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static int getItemDamage(final aip stack) {
        return stack.k() - stack.i();
    }
    
    public static fa getFirstFacing(final et pos) {
        final Iterator<fa> iterator = getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            final fa facing = iterator.next();
            return facing;
        }
        return null;
    }
    
    public static List<fa> getPossibleSides(final et pos) {
        final ArrayList<fa> facings = new ArrayList<fa>();
        for (final fa side : fa.values()) {
            final et neighbour = pos.a(side);
            if (EntityHelper.mc.f.o(neighbour).u().a(EntityHelper.mc.f.o(neighbour), false)) {
                final awt blockState;
                if (!(blockState = EntityHelper.mc.f.o(neighbour)).a().j()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }
    
    public static boolean rayTracePlaceCheck(final et pos, final boolean shouldCheck, final float height) {
        return !shouldCheck || EntityHelper.mc.f.a(new bhe(EntityHelper.mc.h.p, EntityHelper.mc.h.q + EntityHelper.mc.h.by(), EntityHelper.mc.h.r), new bhe(pos.p(), pos.q() + height, pos.r()), false, true, false) == null;
    }
    
    public static boolean rayTracePlaceCheck(final et pos, final boolean shouldCheck) {
        return rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }
    
    public static boolean rayTracePlaceCheck(final et pos) {
        return rayTracePlaceCheck(pos, true);
    }
    
    public static boolean isMoving(final vp entity) {
        return getDistance(entity.m, entity.q, entity.o, entity.p, entity.q, entity.r) > 9.0E-4;
    }
    
    public static int getPotionModifier(final vp entity, final uz potion) {
        final va effect = entity.b(potion);
        return (effect != null) ? (effect.c() + 1) : 0;
    }
    
    public static double getDistance(final double x1, final double z1, final double x2, final double z2) {
        final double d0 = x1 - x2;
        final double d2 = z1 - z2;
        return rk.a(d0 * d0 + d2 * d2);
    }
    
    public static vp rayCast(final vg e, double range) {
        final bhe vec = e.d().e(new bhe(0.0, e.by(), 0.0));
        final bhe vec2 = EntityHelper.mc.h.d().e(new bhe(0.0, EntityHelper.mc.h.by(), 0.0));
        final bhb axis = EntityHelper.mc.h.bw().b(vec.b - vec2.b, vec.c - vec2.c, vec.d - vec2.d).c(1.0, 1.0, 1.0);
        vg nearst = null;
        for (final vg en : EntityHelper.mc.f.b(EntityHelper.mc.h, axis)) {
            final vg obj = en;
            if (en.ay()) {
                if (!(en instanceof vp)) {
                    continue;
                }
                final float size = en.aI();
                final bhb axis2 = en.bw().c(size, size, size);
                final bhc mop = axis2.b(vec2, vec);
                if (axis2.b(vec2)) {
                    if (range < 0.0) {
                        continue;
                    }
                    nearst = en;
                    final bhe vec3 = (mop == null) ? vec2 : mop.c;
                    range = 0.0;
                }
                else {
                    if (mop == null) {
                        continue;
                    }
                    final double dist = vec2.f(mop.c);
                    if (range != 0.0 && dist >= range) {
                        continue;
                    }
                    nearst = en;
                    final bhe vec3 = mop.c;
                    range = dist;
                }
            }
        }
        return (vp)nearst;
    }
    
    public static boolean isTeamWithYou(final vp entity) {
        if (EntityHelper.mc.h == null || entity == null || EntityHelper.mc.h.i_() == null || entity.i_() == null) {
            return false;
        }
        if (EntityHelper.mc.h.aY() != null && entity.aY() != null && EntityHelper.mc.h.aY().a(entity.aY())) {
            return true;
        }
        final String targetName = entity.i_().d().replace("§r", "");
        final String clientName = EntityHelper.mc.h.i_().d().replace("§r", "");
        return targetName.startsWith("§" + clientName.charAt(1));
    }
    
    public static boolean checkArmor(final vg entity) {
        return ((vp)entity).cg() < 1;
    }
    
    public static int getPing(final aed entityPlayer) {
        if (entityPlayer == null) {
            return 0;
        }
        final bsc networkPlayerInfo = Objects.requireNonNull(EntityHelper.mc.v()).a(entityPlayer.bm());
        return networkPlayerInfo.c();
    }
    
    public static String getName(final bsc networkPlayerInfoIn) {
        return (networkPlayerInfoIn.l() != null) ? networkPlayerInfoIn.l().d() : bhh.a(networkPlayerInfoIn.j(), networkPlayerInfoIn.a().getName());
    }
    
    public static double getDistanceOfEntityToBlock(final vg entity, final et pos) {
        return getDistance(entity.p, entity.q, entity.r, pos.p(), pos.q(), pos.r());
    }
    
    public static bhe getInterpolatedPos(final vg entity, final float ticks) {
        return new bhe(entity.M, entity.N, entity.O).e(getInterpolatedAmount(entity, ticks));
    }
    
    public static bhe getInterpolatedAmount(final vg entity, final double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }
    
    public static bhe getInterpolatedAmount(final vg entity, final double x, final double y, final double z) {
        return new bhe((entity.p - entity.M) * x, (entity.q - entity.N) * y, (entity.r - entity.O) * z);
    }
    
    static {
        EntityHelper.noSolidBlocks = Arrays.asList(aox.a, aox.l, aox.j, aox.aH, aox.ab, aox.H);
    }
}
