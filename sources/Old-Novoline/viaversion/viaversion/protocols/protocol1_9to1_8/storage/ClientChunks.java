package viaversion.viaversion.protocols.protocol1_9to1_8.storage;

import com.google.common.collect.Sets;
import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;

import java.util.List;
import java.util.Set;

public class ClientChunks extends StoredObject {
    private final Set<Long> loadedChunks = Sets.newConcurrentHashSet();
    private final Set<Long> bulkChunks = Sets.newConcurrentHashSet();

    public ClientChunks(UserConnection user) {
        super(user);
    }

    public static long toLong(int msw, int lsw) {
        return ((long) msw << 32) + lsw - -2147483648L;
    }

    public List<Object> transformMapChunkBulk(Object packet) throws Exception {
        return Via.getManager().getProviders().get(BulkChunkTranslatorProvider.class).transformMapChunkBulk(packet, this);
    }

    public Set<Long> getLoadedChunks() {
        return loadedChunks;
    }

    public Set<Long> getBulkChunks() {
        return bulkChunks;
    }
}
