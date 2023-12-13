// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.utils;

import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.stream.Stream;
import java.util.function.Function;
import com.viaversion.viaversion.api.connection.StorableObject;
import java.util.function.Predicate;
import java.util.Objects;
import com.viaversion.viaversion.api.Via;

public class Ticker
{
    private static boolean init;
    
    public static void init() {
        if (Ticker.init) {
            return;
        }
        synchronized (Ticker.class) {
            if (Ticker.init) {
                return;
            }
            Ticker.init = true;
        }
        Via.getPlatform().runRepeatingSync(() -> Via.getManager().getConnectionManager().getConnections().forEach(user -> {
            user.getStoredObjects().values().stream();
            final Class clazz;
            Objects.requireNonNull(clazz);
            final Stream<StorableObject> stream;
            stream.filter(clazz::isInstance);
            final Class clazz2;
            Objects.requireNonNull(clazz2);
            final Stream<StorableObject> stream2;
            stream2.map((Function<? super StorableObject, ?>)clazz2::cast).forEach(Tickable::tick);
        }), 1L);
    }
    
    static {
        Ticker.init = false;
    }
}
