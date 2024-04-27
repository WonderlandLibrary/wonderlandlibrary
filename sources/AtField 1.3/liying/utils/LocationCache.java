/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package liying.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import liying.utils.Location;
import liying.utils.RingBuffer;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class LocationCache
extends MinecraftInstance
implements Listenable {
    public static final Companion Companion;
    private static final double DISCOVER_RANGE = 64.0;
    private static final HashMap aabbList;
    private static final int CACHE_SIZE;
    private static final RingBuffer playerLocationList;

    @Override
    public boolean handleEvents() {
        return true;
    }

    public static final RingBuffer access$getPlayerLocationList$cp() {
        return playerLocationList;
    }

    public static final HashMap access$getAabbList$cp() {
        return aabbList;
    }

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
    }

    static {
        CACHE_SIZE = 50;
        Companion = new Companion(null);
        aabbList = new HashMap(50);
        playerLocationList = new RingBuffer(50);
    }

    public static final class Companion {
        public final Location getPreviousPlayerLocation(int n, Location location) {
            Location location2 = (Location)LocationCache.access$getPlayerLocationList$cp().get(LocationCache.access$getPlayerLocationList$cp().getSize() - n - 1);
            if (location2 == null) {
                location2 = location;
            }
            return location2;
        }

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

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}

