package viaversion.viaversion.protocols.protocol1_14to1_13_2.data;

import com.google.gson.JsonObject;
import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.protocols.protocol1_13to1_12_2.data.ComponentRewriter1_13;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.InventoryPackets;

public class ComponentRewriter1_14 extends ComponentRewriter1_13 {

    public ComponentRewriter1_14(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected void handleItem(Item item) {
        InventoryPackets.toClient(item);
    }

    @Override
    protected void handleTranslate(JsonObject object, String translate) {
        // Nothing
    }
}
