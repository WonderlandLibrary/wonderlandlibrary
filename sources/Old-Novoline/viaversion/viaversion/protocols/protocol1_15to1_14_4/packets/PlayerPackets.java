package viaversion.viaversion.protocols.protocol1_15to1_14_4.packets;

import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.entities.Entity1_15Types;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import viaversion.viaversion.protocols.protocol1_15to1_14_4.storage.EntityTracker1_15;

public class PlayerPackets {

    public static void register(Protocol protocol) {
        protocol.registerOutgoing(ClientboundPackets1_14.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT);
                create(wrapper -> wrapper.write(Type.LONG, 0L)); // Level Seed
            }
        });

        protocol.registerOutgoing(ClientboundPackets1_14.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.INT); // 0 - Entity ID
                map(Type.UNSIGNED_BYTE); // 1 - Gamemode
                map(Type.INT); // 2 - Dimension

                handler(wrapper -> {
                    // Register Type ID
                    EntityTracker1_15 tracker = wrapper.user().get(EntityTracker1_15.class);
                    int entityId = wrapper.get(Type.INT, 0);
                    tracker.addEntity(entityId, Entity1_15Types.EntityType.PLAYER);
                });
                create(wrapper -> wrapper.write(Type.LONG, 0L)); // Level Seed

                map(Type.UNSIGNED_BYTE); // 3 - Max Players
                map(Type.STRING); // 4 - Level Type
                map(Type.VAR_INT); // 5 - View Distance
                map(Type.BOOLEAN); // 6 - Reduce Debug Info

                create(wrapper -> wrapper.write(Type.BOOLEAN, !Via.getConfig().is1_15InstantRespawn())); // Show Death Screen
            }
        });
    }
}
