// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import java.util.Locale;
import java.util.LinkedList;
import java.util.Map;

public class DoorConnectionHandler extends ConnectionHandler
{
    private static final Map<Integer, DoorData> doorDataMap;
    private static final Map<Short, Integer> connectedStates;
    
    static ConnectionData.ConnectorInitAction init() {
        final List<String> baseDoors = new LinkedList<String>();
        baseDoors.add("minecraft:oak_door");
        baseDoors.add("minecraft:birch_door");
        baseDoors.add("minecraft:jungle_door");
        baseDoors.add("minecraft:dark_oak_door");
        baseDoors.add("minecraft:acacia_door");
        baseDoors.add("minecraft:spruce_door");
        baseDoors.add("minecraft:iron_door");
        final DoorConnectionHandler connectionHandler = new DoorConnectionHandler();
        final List list;
        final int type;
        int id;
        DoorData doorData;
        final DoorConnectionHandler value;
        return blockData -> {
            type = list.indexOf(blockData.getMinecraftKey());
            if (type != -1) {
                id = blockData.getSavedBlockStateId();
                doorData = new DoorData(blockData.getValue("half").equals("lower"), blockData.getValue("hinge").equals("right"), blockData.getValue("powered").equals("true"), blockData.getValue("open").equals("true"), BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)), type);
                DoorConnectionHandler.doorDataMap.put(id, doorData);
                DoorConnectionHandler.connectedStates.put(getStates(doorData), id);
                ConnectionData.connectionHandlerMap.put(id, value);
            }
        };
    }
    
    private static short getStates(final DoorData doorData) {
        short s = 0;
        if (doorData.isLower()) {
            s |= 0x1;
        }
        if (doorData.isOpen()) {
            s |= 0x2;
        }
        if (doorData.isPowered()) {
            s |= 0x4;
        }
        if (doorData.isRightHinge()) {
            s |= 0x8;
        }
        s |= (short)(doorData.getFacing().ordinal() << 4);
        s |= (short)((doorData.getType() & 0x7) << 6);
        return s;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final DoorData doorData = DoorConnectionHandler.doorDataMap.get(blockState);
        if (doorData == null) {
            return blockState;
        }
        short s = 0;
        s |= (short)((doorData.getType() & 0x7) << 6);
        if (doorData.isLower()) {
            final DoorData upperHalf = DoorConnectionHandler.doorDataMap.get(this.getBlockData(user, position.getRelative(BlockFace.TOP)));
            if (upperHalf == null) {
                return blockState;
            }
            s |= 0x1;
            if (doorData.isOpen()) {
                s |= 0x2;
            }
            if (upperHalf.isPowered()) {
                s |= 0x4;
            }
            if (upperHalf.isRightHinge()) {
                s |= 0x8;
            }
            s |= (short)(doorData.getFacing().ordinal() << 4);
        }
        else {
            final DoorData lowerHalf = DoorConnectionHandler.doorDataMap.get(this.getBlockData(user, position.getRelative(BlockFace.BOTTOM)));
            if (lowerHalf == null) {
                return blockState;
            }
            if (lowerHalf.isOpen()) {
                s |= 0x2;
            }
            if (doorData.isPowered()) {
                s |= 0x4;
            }
            if (doorData.isRightHinge()) {
                s |= 0x8;
            }
            s |= (short)(lowerHalf.getFacing().ordinal() << 4);
        }
        final Integer newBlockState = DoorConnectionHandler.connectedStates.get(s);
        return (newBlockState == null) ? blockState : newBlockState;
    }
    
    static {
        doorDataMap = new HashMap<Integer, DoorData>();
        connectedStates = new HashMap<Short, Integer>();
    }
    
    private static final class DoorData
    {
        private final boolean lower;
        private final boolean rightHinge;
        private final boolean powered;
        private final boolean open;
        private final BlockFace facing;
        private final int type;
        
        private DoorData(final boolean lower, final boolean rightHinge, final boolean powered, final boolean open, final BlockFace facing, final int type) {
            this.lower = lower;
            this.rightHinge = rightHinge;
            this.powered = powered;
            this.open = open;
            this.facing = facing;
            this.type = type;
        }
        
        public boolean isLower() {
            return this.lower;
        }
        
        public boolean isRightHinge() {
            return this.rightHinge;
        }
        
        public boolean isPowered() {
            return this.powered;
        }
        
        public boolean isOpen() {
            return this.open;
        }
        
        public BlockFace getFacing() {
            return this.facing;
        }
        
        public int getType() {
            return this.type;
        }
    }
}
