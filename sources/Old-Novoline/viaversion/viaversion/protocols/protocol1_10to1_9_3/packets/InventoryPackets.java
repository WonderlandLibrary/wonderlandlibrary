package viaversion.viaversion.protocols.protocol1_10to1_9_3.packets;

import viaversion.viaversion.api.minecraft.item.Item;
import viaversion.viaversion.api.rewriters.ItemRewriter;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_10to1_9_3.Protocol1_10To1_9_3_4;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;

public class InventoryPackets {

    public static void register(Protocol1_10To1_9_3_4 protocol) {
        ItemRewriter itemRewriter = new ItemRewriter(protocol, item -> {}, InventoryPackets::toServerItem);
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    }

    public static void toServerItem(Item item) {
        if (item == null) return;
        boolean newItem = item.getIdentifier() >= 213 && item.getIdentifier() <= 217;
        if (newItem) { // Replace server-side unknown items
            item.setIdentifier((short) 1);
            item.setData((short) 0);
        }
    }

}
