package viaversion.viaversion.protocols.protocol1_12to1_11_1.providers;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.platform.providers.Provider;

public class InventoryQuickMoveProvider implements Provider {

    public boolean registerQuickMoveAction(short windowId, short slotId, short actionId, UserConnection userConnection) {
        return false; // not supported :/ plays very sad violin
    }
}