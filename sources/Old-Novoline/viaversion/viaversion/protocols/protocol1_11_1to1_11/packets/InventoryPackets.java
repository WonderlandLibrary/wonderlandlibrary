package viaversion.viaversion.protocols.protocol1_11_1to1_11.packets;

import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.rewriters.ItemRewriter;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_11_1to1_11.Protocol1_11_1To1_11;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;

public class InventoryPackets {

    public static void register(Protocol1_11_1To1_11 protocol) {
        ItemRewriter itemRewriter = new ItemRewriter(protocol, item -> {}, InventoryPackets::toServerItem);
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    }

    public static void toServerItem(Item item) {
        if (item == null) return;
        boolean newItem = item.getIdentifier() == 452;
        if (newItem) { // Replace server-side unknown items
            item.setIdentifier((short) 1);
            item.setData((short) 0);
        }
    }

}
