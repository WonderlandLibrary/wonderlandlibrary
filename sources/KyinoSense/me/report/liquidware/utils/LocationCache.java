/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.utils.Location;
import me.report.liquidware.utils.RingBuffer;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \n2\u00020\u00012\u00020\u0002:\u0001\nB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0007\u00a8\u0006\u000b"}, d2={"Lme/report/liquidware/utils/LocationCache;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "handleEvents", "", "onMotion", "", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "Companion", "KyinoClient"})
public final class LocationCache
extends MinecraftInstance
implements Listenable {
    private static final int CACHE_SIZE = 50;
    private static final double DISCOVER_RANGE = 64.0;
    private static final HashMap<Integer, SoftReference<RingBuffer<AxisAlignedBB>>> aabbList;
    private static final RingBuffer<Location> playerLocationList;
    public static final Companion Companion;

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getEventState() == EventState.POST) {
            void $this$filterNotTo$iv$iv;
            void $this$filterNot$iv;
            void $this$mapTo$iv$iv;
            WorldClient worldClient = LocationCache.access$getMc$p$s1046033730().field_71441_e;
            if (worldClient == null) {
                return;
            }
            WorldClient theWorld = worldClient;
            EntityPlayerSP entityPlayerSP = LocationCache.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                return;
            }
            EntityPlayerSP thePlayer = entityPlayerSP;
            Vec3 vec3 = new Vec3(thePlayer.field_70165_t, thePlayer.func_174813_aQ().field_72338_b, thePlayer.field_70161_v);
            Rotation rotation = RotationUtils.serverRotation;
            Intrinsics.checkExpressionValueIsNotNull(rotation, "RotationUtils.serverRotation");
            playerLocationList.add(new Location(vec3, rotation));
            List<Entity> entities = PlayerExtensionKt.getEntitiesInRadius((World)theWorld, (Entity)thePlayer, 64.0);
            Set<Integer> set = aabbList.keySet();
            Intrinsics.checkExpressionValueIsNotNull(set, "aabbList.keys");
            Iterable iterable = set;
            Iterable $this$map$iv = entities;
            boolean $i$f$map = false;
            Iterable iterable2 = $this$map$iv;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            boolean $i$f$mapTo = false;
            for (Object item$iv$iv : $this$mapTo$iv$iv) {
                void p1;
                Entity entity = (Entity)item$iv$iv;
                Collection collection = destination$iv$iv;
                boolean bl = false;
                Integer n = p1.func_145782_y();
                collection.add(n);
            }
            $this$map$iv = (List)destination$iv$iv;
            boolean $i$f$filterNot2 = false;
            $this$mapTo$iv$iv = $this$filterNot$iv;
            destination$iv$iv = new ArrayList();
            boolean $i$f$filterNotTo = false;
            for (Object element$iv$iv : $this$filterNotTo$iv$iv) {
                int p1 = ((Number)element$iv$iv).intValue();
                boolean bl = false;
                if ($this$map$iv.contains(p1)) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            Iterable $this$forEach$iv = (List)destination$iv$iv;
            boolean $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                Integer it = (Integer)element$iv;
                boolean bl = false;
                aabbList.remove(it);
            }
            for (Entity entity : entities) {
                Object object;
                void $this$getOrPut$iv;
                Map $i$f$filterNot2 = aabbList;
                Integer key$iv = entity.func_145782_y();
                boolean $i$f$getOrPut = false;
                Object value$iv = $this$getOrPut$iv.get(key$iv);
                if (value$iv == null) {
                    boolean bl = false;
                    SoftReference answer$iv = new SoftReference(new RingBuffer(50));
                    $this$getOrPut$iv.put(key$iv, answer$iv);
                    object = answer$iv;
                } else {
                    object = value$iv;
                }
                RingBuffer ringBuffer = (RingBuffer)((SoftReference)object).get();
                if (ringBuffer != null) {
                    AxisAlignedBB axisAlignedBB = entity.func_174813_aQ();
                    Intrinsics.checkExpressionValueIsNotNull(axisAlignedBB, "entity.entityBoundingBox");
                    ringBuffer.add(axisAlignedBB);
                }
            }
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        Companion = new Companion(null);
        aabbList = new HashMap(50);
        playerLocationList = new RingBuffer(50);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u000bJ\u0016\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000RB\u0010\u0007\u001a6\u0012\u0004\u0012\u00020\u0004\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t0\bj\u001a\u0012\u0004\u0012\u00020\u0004\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lme/report/liquidware/utils/LocationCache$Companion;", "", "()V", "CACHE_SIZE", "", "DISCOVER_RANGE", "", "aabbList", "Ljava/util/HashMap;", "Ljava/lang/ref/SoftReference;", "Lme/report/liquidware/utils/RingBuffer;", "Lnet/minecraft/util/AxisAlignedBB;", "Lkotlin/collections/HashMap;", "playerLocationList", "Lme/report/liquidware/utils/Location;", "getPreviousAABB", "entityId", "ticksBefore", "default", "getPreviousPlayerLocation", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final AxisAlignedBB getPreviousAABB(int entityId, int ticksBefore, @NotNull AxisAlignedBB axisAlignedBB) {
            Object object;
            block5: {
                block4: {
                    Intrinsics.checkParameterIsNotNull(axisAlignedBB, "default");
                    if (aabbList.isEmpty()) {
                        return axisAlignedBB;
                    }
                    object = (SoftReference)aabbList.get(entityId);
                    if (object == null || (object = (RingBuffer)((SoftReference)object).get()) == null) break block4;
                    Object object2 = object;
                    boolean bl = false;
                    boolean bl2 = false;
                    Object $this$run = object2;
                    boolean bl3 = false;
                    object = (AxisAlignedBB)((RingBuffer)$this$run).get(((RingBuffer)$this$run).getSize() - ticksBefore - 1);
                    if (object != null) break block5;
                }
                object = axisAlignedBB;
            }
            return object;
        }

        @NotNull
        public final Location getPreviousPlayerLocation(int ticksBefore, @NotNull Location location) {
            Intrinsics.checkParameterIsNotNull(location, "default");
            Location location2 = (Location)playerLocationList.get(playerLocationList.getSize() - ticksBefore - 1);
            if (location2 == null) {
                location2 = location;
            }
            return location2;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

