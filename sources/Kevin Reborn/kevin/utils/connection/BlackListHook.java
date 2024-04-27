package kevin.utils.connection;

import com.google.common.collect.Sets;
import com.mojang.patchy.BlockedServers;
import kevin.main.KevinClient;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class BlackListHook {
    public static boolean working = false;
    static Set<String> CACHES = Sets.newHashSet(), BLOCKING_SERVER = Sets.newHashSet();

    public static void setState(boolean state) {
        if (state == working) return;
        try {
            Field field = BlockedServers.class.getDeclaredField("BLOCKED_SERVERS");
            field.setAccessible(true);
            if (state) {
                BLOCKING_SERVER.clear();
            } else {
                BLOCKING_SERVER.addAll(CACHES);
            }
        } catch (ReflectiveOperationException ignored) { return; }
        working = state;
    }
    static {
        KevinClient.getPool().execute(() -> {
            try {
                Class.forName("com.mojang.patchy.BlockedServers");
                Field field = BlockedServers.class.getDeclaredField("BLOCKED_SERVERS");
                field.setAccessible(true);
                CACHES = new HashSet<>(BLOCKING_SERVER = (Set<String>) field.get(null));
            } catch (ReflectiveOperationException ignored) {}
        });
    }
}
