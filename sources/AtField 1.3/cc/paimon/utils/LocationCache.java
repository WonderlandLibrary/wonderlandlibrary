/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package cc.paimon.utils;

import cc.paimon.utils.Location;
import cc.paimon.utils.RingBuffer;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class LocationCache
extends MinecraftInstance
implements Listenable {
    private static final double DISCOVER_RANGE = 64.0;
    private static final int CACHE_SIZE = 50;
    private static final HashMap aabbList;
    private static final RingBuffer playerLocationList;
    public static final Companion Companion;

    public static final RingBuffer access$getPlayerLocationList$cp() {
        return playerLocationList;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
    }

    static {
        Companion = new Companion(null);
        aabbList = new HashMap(50);
        playerLocationList = new RingBuffer(50);
    }

    public static final HashMap access$getAabbList$cp() {
        return aabbList;
    }

    public static final class Companion {
        public final IAxisAlignedBB getPreviousAABB(int n, int n2, IAxisAlignedBB iAxisAlignedBB) {
            Object object;
            block5: {
                block4: {
                    if (LocationCache.access$getAabbList$cp().isEmpty()) {
                        return iAxisAlignedBB;
                    }
                    object = (SoftReference)LocationCache.access$getAabbList$cp().get(n);
                    if (object == null || (object = (RingBuffer)((SoftReference)object).get()) == null) break block4;
                    Object object2 = object;
                    boolean bl = false;
                    boolean bl2 = false;
                    Object object3 = object2;
                    boolean bl3 = false;
                    object = (IAxisAlignedBB)((RingBuffer)object3).get(((RingBuffer)object3).getSize() - n2 - 1);
                    if (object != null) break block5;
                }
                object = iAxisAlignedBB;
            }
            return object;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Location getPreviousPlayerLocation(int n, Location location) {
            Location location2 = (Location)LocationCache.access$getPlayerLocationList$cp().get(LocationCache.access$getPlayerLocationList$cp().getSize() - n - 1);
            if (location2 == null) {
                location2 = location;
            }
            return location2;
        }
    }
}

