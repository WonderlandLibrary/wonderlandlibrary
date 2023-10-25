package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import org.apache.logging.log4j.LogManager;

public enum EnumConnectionState {
    HANDSHAKING/* Unavailable Anonymous Inner Class!! */,
    PLAY/* Unavailable Anonymous Inner Class!! */,
    STATUS/* Unavailable Anonymous Inner Class!! */,
    LOGIN/* Unavailable Anonymous Inner Class!! */;

    private static int field_181136_e;
    private static int field_181137_f;
    private static final EnumConnectionState[] STATES_BY_ID;
    private static final Map<Class<? extends Packet>, EnumConnectionState> STATES_BY_CLASS;
    private final int id;
    private final Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet>>> directionMaps = Maps.newEnumMap(EnumPacketDirection.class);

    private EnumConnectionState(int protocolId) {
        this.id = protocolId;
    }

    protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet> packetClass) {
        BiMap bimap = (BiMap)this.directionMaps.get((Object)direction);
        if (bimap == null) {
            bimap = HashBiMap.create();
            this.directionMaps.put((Object)direction, (Object)bimap);
        }
        if (bimap.containsValue(packetClass)) {
            String s = direction + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
            LogManager.getLogger().fatal(s);
            throw new IllegalArgumentException(s);
        }
        bimap.put((Object)bimap.size(), packetClass);
        return this;
    }

    public Integer getPacketId(EnumPacketDirection direction, Packet packetIn) {
        return (Integer)((BiMap)this.directionMaps.get((Object)direction)).inverse().get((Object)packetIn.getClass());
    }

    public Packet getPacket(EnumPacketDirection direction, int packetId) throws InstantiationException, IllegalAccessException {
        Class oclass = (Class)((BiMap)this.directionMaps.get((Object)direction)).get((Object)packetId);
        return oclass == null ? null : (Packet)oclass.newInstance();
    }

    public int getId() {
        return this.id;
    }

    public static EnumConnectionState getById(int stateId) {
        return stateId >= field_181136_e && stateId <= field_181137_f ? STATES_BY_ID[stateId - field_181136_e] : null;
    }

    public static EnumConnectionState getFromPacket(Packet packetIn) {
        return (EnumConnectionState)((Object)STATES_BY_CLASS.get((Object)packetIn.getClass()));
    }

    static {
        field_181136_e = -1;
        field_181137_f = 2;
        STATES_BY_ID = new EnumConnectionState[field_181137_f - field_181136_e + 1];
        STATES_BY_CLASS = Maps.newHashMap();
        for (EnumConnectionState enumconnectionstate : EnumConnectionState.values()) {
            int i = enumconnectionstate.getId();
            if (i < field_181136_e || i > field_181137_f) {
                throw new Error("Invalid protocol ID " + Integer.toString((int)i));
            }
            EnumConnectionState.STATES_BY_ID[i - EnumConnectionState.field_181136_e] = enumconnectionstate;
            for (EnumPacketDirection enumpacketdirection : enumconnectionstate.directionMaps.keySet()) {
                for (Class oclass : ((BiMap)enumconnectionstate.directionMaps.get((Object)enumpacketdirection)).values()) {
                    if (STATES_BY_CLASS.containsKey((Object)oclass) && STATES_BY_CLASS.get((Object)oclass) != enumconnectionstate) {
                        throw new Error("Packet " + oclass + " is already assigned to protocol " + STATES_BY_CLASS.get((Object)oclass) + " - can't reassign to " + (Object)((Object)enumconnectionstate));
                    }
                    try {
                        oclass.newInstance();
                    }
                    catch (Throwable var10) {
                        throw new Error("Packet " + oclass + " fails instantiation checks! " + oclass);
                    }
                    STATES_BY_CLASS.put((Object)oclass, (Object)enumconnectionstate);
                }
            }
        }
    }
}
