/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  net.minecraft.world.chunk.Chunk
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.extensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, xi=2, d1={"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t\u001a\u0012\u0010\n\u001a\u00020\u000b*\u00020\f2\u0006\u0010\r\u001a\u00020\f\u001a\"\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\u000f*\u00020\u00102\u0006\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u0011\u001a\u00020\u000b\u001a\n\u0010\u0012\u001a\u00020\u0006*\u00020\u0013\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\u00a8\u0006\u0014"}, d2={"skin", "Lnet/minecraft/util/ResourceLocation;", "Lnet/minecraft/entity/EntityLivingBase;", "getSkin", "(Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/util/ResourceLocation;", "getNearestPointBB", "Lnet/minecraft/util/Vec3;", "eye", "box", "Lnet/minecraft/util/AxisAlignedBB;", "getDistanceToEntityBox", "", "Lnet/minecraft/entity/Entity;", "entity", "getEntitiesInRadius", "", "Lnet/minecraft/world/World;", "radius", "getEyeVec3", "Lnet/minecraft/entity/player/EntityPlayer;", "KyinoClient"})
public final class PlayerExtensionKt {
    public static final double getDistanceToEntityBox(@NotNull Entity $this$getDistanceToEntityBox, @NotNull Entity entity) {
        Vec3 eyes;
        Intrinsics.checkParameterIsNotNull($this$getDistanceToEntityBox, "$this$getDistanceToEntityBox");
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        Vec3 vec3 = eyes = $this$getDistanceToEntityBox.func_174824_e(0.0f);
        Intrinsics.checkExpressionValueIsNotNull(vec3, "eyes");
        AxisAlignedBB axisAlignedBB = entity.func_174813_aQ();
        Intrinsics.checkExpressionValueIsNotNull(axisAlignedBB, "entity.entityBoundingBox");
        Vec3 pos = PlayerExtensionKt.getNearestPointBB(vec3, axisAlignedBB);
        double d = pos.field_72450_a - eyes.field_72450_a;
        boolean bl = false;
        double xDist = Math.abs(d);
        double d2 = pos.field_72448_b - eyes.field_72448_b;
        boolean bl2 = false;
        double yDist = Math.abs(d2);
        double d3 = pos.field_72449_c - eyes.field_72449_c;
        int n = 0;
        double zDist = Math.abs(d3);
        d3 = xDist;
        n = 2;
        boolean bl3 = false;
        double d4 = Math.pow(d3, n);
        d3 = yDist;
        n = 2;
        double d5 = d4;
        bl3 = false;
        double d6 = Math.pow(d3, n);
        d3 = zDist;
        n = 2;
        d5 += d6;
        bl3 = false;
        d6 = Math.pow(d3, n);
        d3 = d5 + d6;
        n = 0;
        return Math.sqrt(d3);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final Vec3 getNearestPointBB(@NotNull Vec3 eye, @NotNull AxisAlignedBB box) {
        Intrinsics.checkParameterIsNotNull(eye, "eye");
        Intrinsics.checkParameterIsNotNull(box, "box");
        double[] origin = new double[]{eye.field_72450_a, eye.field_72448_b, eye.field_72449_c};
        double[] destMins = new double[]{box.field_72340_a, box.field_72338_b, box.field_72339_c};
        double[] destMaxs = new double[]{box.field_72336_d, box.field_72337_e, box.field_72334_f};
        int n = 0;
        int n2 = 2;
        while (n <= n2) {
            void i;
            if (origin[i] > destMaxs[i]) {
                origin[i] = destMaxs[i];
            } else if (origin[i] < destMins[i]) {
                origin[i] = destMins[i];
            }
            ++i;
        }
        return new Vec3(origin[0], origin[1], origin[2]);
    }

    @NotNull
    public static final ResourceLocation getSkin(@NotNull EntityLivingBase $this$skin) {
        ResourceLocation resourceLocation;
        Object object;
        Intrinsics.checkParameterIsNotNull($this$skin, "$this$skin");
        if ($this$skin instanceof EntityPlayer) {
            Minecraft minecraft = Minecraft.func_71410_x();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "Minecraft.getMinecraft()");
            NetworkPlayerInfo networkPlayerInfo = minecraft.func_147114_u().func_175102_a(((EntityPlayer)$this$skin).func_110124_au());
            object = networkPlayerInfo != null ? networkPlayerInfo.func_178837_g() : null;
        } else {
            object = resourceLocation = null;
        }
        if (object == null) {
            ResourceLocation resourceLocation2 = DefaultPlayerSkin.func_177335_a();
            resourceLocation = resourceLocation2;
            Intrinsics.checkExpressionValueIsNotNull(resourceLocation2, "DefaultPlayerSkin.getDefaultSkinLegacy()");
        }
        return resourceLocation;
    }

    @NotNull
    public static final List<Entity> getEntitiesInRadius(@NotNull World $this$getEntitiesInRadius, @NotNull Entity entity, double radius) {
        Intrinsics.checkParameterIsNotNull($this$getEntitiesInRadius, "$this$getEntitiesInRadius");
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        AxisAlignedBB box = entity.func_174813_aQ().func_72314_b(radius, radius, radius);
        double d = box.field_72340_a * 0.0625;
        boolean bl = false;
        int chunkMinX = (int)Math.floor(d);
        double d2 = box.field_72336_d * 0.0625;
        boolean bl2 = false;
        int chunkMaxX = (int)Math.ceil(d2);
        double d3 = box.field_72339_c * 0.0625;
        int n = 0;
        int chunkMinZ = (int)Math.floor(d3);
        double d4 = box.field_72334_f * 0.0625;
        boolean bl3 = false;
        int chunkMaxZ = (int)Math.ceil(d4);
        n = 0;
        List entities = new ArrayList();
        n = chunkMinX;
        Iterable $this$forEach$iv = new IntRange(n, chunkMaxX);
        boolean $i$f$forEach = false;
        Iterator iterator2 = $this$forEach$iv.iterator();
        while (iterator2.hasNext()) {
            int element$iv;
            int x = element$iv = ((IntIterator)iterator2).nextInt();
            boolean bl4 = false;
            int n2 = chunkMinZ;
            Sequence $this$forEach$iv2 = SequencesKt.filter(SequencesKt.map(CollectionsKt.asSequence(new IntRange(n2, chunkMaxZ)), (Function1)new Function1<Integer, Chunk>(x, $this$getEntitiesInRadius, chunkMinZ, chunkMaxZ, entity, box, entities){
                final /* synthetic */ int $x;
                final /* synthetic */ World $this_getEntitiesInRadius$inlined;
                final /* synthetic */ int $chunkMinZ$inlined;
                final /* synthetic */ int $chunkMaxZ$inlined;
                final /* synthetic */ Entity $entity$inlined;
                final /* synthetic */ AxisAlignedBB $box$inlined;
                final /* synthetic */ List $entities$inlined;
                {
                    this.$x = n;
                    this.$this_getEntitiesInRadius$inlined = world;
                    this.$chunkMinZ$inlined = n2;
                    this.$chunkMaxZ$inlined = n3;
                    this.$entity$inlined = entity;
                    this.$box$inlined = axisAlignedBB;
                    this.$entities$inlined = list;
                    super(1);
                }

                public final Chunk invoke(int z) {
                    return this.$this_getEntitiesInRadius$inlined.func_72964_e(this.$x, z);
                }
            }), getEntitiesInRadius.1.2.INSTANCE);
            boolean $i$f$forEach2 = false;
            Iterator iterator3 = $this$forEach$iv2.iterator();
            while (iterator3.hasNext()) {
                Object element$iv2 = iterator3.next();
                Chunk it = (Chunk)element$iv2;
                boolean bl5 = false;
                it.func_177414_a(entity, box, entities, null);
            }
        }
        return entities;
    }

    public static /* synthetic */ List getEntitiesInRadius$default(World world, Entity entity, double d, int n, Object object) {
        if ((n & 2) != 0) {
            d = 16.0;
        }
        return PlayerExtensionKt.getEntitiesInRadius(world, entity, d);
    }

    @NotNull
    public static final Vec3 getEyeVec3(@NotNull EntityPlayer $this$getEyeVec3) {
        Intrinsics.checkParameterIsNotNull($this$getEyeVec3, "$this$getEyeVec3");
        return new Vec3($this$getEyeVec3.field_70165_t, $this$getEyeVec3.func_174813_aQ().field_72338_b + (double)$this$getEyeVec3.func_70047_e(), $this$getEyeVec3.field_70161_v);
    }
}

