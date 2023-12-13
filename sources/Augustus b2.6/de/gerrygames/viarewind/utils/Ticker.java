// 
// Decompiled by Procyon v0.5.36
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
        final Class obj;
        final Stream<StorableObject> stream;
        final Class<Object> obj2;
        final Stream<StorableObject> stream2;
        Via.getPlatform().runRepeatingSync(() -> Via.getManager().getConnectionManager().getConnections().forEach(user -> {
            user.getStoredObjects().values().stream();
            Objects.requireNonNull(obj);
            stream.filter(obj::isInstance);
            Objects.requireNonNull((Class<Tickable>)obj2);
            stream2.map((Function<? super StorableObject, ?>)obj2::cast).forEach(Tickable::tick);
        }), 1L);
    }
    
    static {
        Ticker.init = false;
    }
}
