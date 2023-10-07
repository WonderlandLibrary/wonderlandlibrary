package viaversion.viaversion.protocols.protocol1_9to1_8.providers;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.platform.providers.Provider;

public class HandItemProvider implements Provider {
    public Item getHandItem(final UserConnection info) {
        return new Item(0, (byte) 0, (short) 0, null);
    }
}
