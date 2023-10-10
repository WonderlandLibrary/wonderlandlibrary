// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Map;

public class StairConnectionHandler extends ConnectionHandler
{
    private static final Map<Integer, StairData> stairDataMap;
    private static final Map<Short, Integer> connectedBlocks;
    
    static ConnectionData.ConnectorInitAction init() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/LinkedList.<init>:()V
        //     7: astore_0        /* baseStairs */
        //     8: aload_0         /* baseStairs */
        //     9: ldc             "minecraft:oak_stairs"
        //    11: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    16: pop            
        //    17: aload_0         /* baseStairs */
        //    18: ldc             "minecraft:cobblestone_stairs"
        //    20: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    25: pop            
        //    26: aload_0         /* baseStairs */
        //    27: ldc             "minecraft:brick_stairs"
        //    29: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    34: pop            
        //    35: aload_0         /* baseStairs */
        //    36: ldc             "minecraft:stone_brick_stairs"
        //    38: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    43: pop            
        //    44: aload_0         /* baseStairs */
        //    45: ldc             "minecraft:nether_brick_stairs"
        //    47: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    52: pop            
        //    53: aload_0         /* baseStairs */
        //    54: ldc             "minecraft:sandstone_stairs"
        //    56: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    61: pop            
        //    62: aload_0         /* baseStairs */
        //    63: ldc             "minecraft:spruce_stairs"
        //    65: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    70: pop            
        //    71: aload_0         /* baseStairs */
        //    72: ldc             "minecraft:birch_stairs"
        //    74: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    79: pop            
        //    80: aload_0         /* baseStairs */
        //    81: ldc             "minecraft:jungle_stairs"
        //    83: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    88: pop            
        //    89: aload_0         /* baseStairs */
        //    90: ldc             "minecraft:quartz_stairs"
        //    92: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    97: pop            
        //    98: aload_0         /* baseStairs */
        //    99: ldc             "minecraft:acacia_stairs"
        //   101: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   106: pop            
        //   107: aload_0         /* baseStairs */
        //   108: ldc             "minecraft:dark_oak_stairs"
        //   110: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   115: pop            
        //   116: aload_0         /* baseStairs */
        //   117: ldc             "minecraft:red_sandstone_stairs"
        //   119: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   124: pop            
        //   125: aload_0         /* baseStairs */
        //   126: ldc             "minecraft:purpur_stairs"
        //   128: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   133: pop            
        //   134: aload_0         /* baseStairs */
        //   135: ldc             "minecraft:prismarine_stairs"
        //   137: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   142: pop            
        //   143: aload_0         /* baseStairs */
        //   144: ldc             "minecraft:prismarine_brick_stairs"
        //   146: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   151: pop            
        //   152: aload_0         /* baseStairs */
        //   153: ldc             "minecraft:dark_prismarine_stairs"
        //   155: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   160: pop            
        //   161: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/StairConnectionHandler;
        //   164: dup            
        //   165: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/StairConnectionHandler.<init>:()V
        //   168: astore_1        /* connectionHandler */
        //   169: aload_0         /* baseStairs */
        //   170: aload_1         /* connectionHandler */
        //   171: invokedynamic   BootstrapMethod #0, check:(Ljava/util/List;Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/StairConnectionHandler;)Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/blockconnections/ConnectionData$ConnectorInitAction;
        //   176: areturn        
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static short getStates(final StairData stairData) {
        short s = 0;
        if (stairData.isBottom()) {
            s |= 0x1;
        }
        s |= (short)(stairData.getShape() << 1);
        s |= (short)(stairData.getType() << 4);
        s |= (short)(stairData.getFacing().ordinal() << 9);
        return s;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final StairData stairData = StairConnectionHandler.stairDataMap.get(blockState);
        if (stairData == null) {
            return blockState;
        }
        short s = 0;
        if (stairData.isBottom()) {
            s |= 0x1;
        }
        s |= (short)(this.getShape(user, position, stairData) << 1);
        s |= (short)(stairData.getType() << 4);
        s |= (short)(stairData.getFacing().ordinal() << 9);
        final Integer newBlockState = StairConnectionHandler.connectedBlocks.get(s);
        return (newBlockState == null) ? blockState : newBlockState;
    }
    
    private int getShape(final UserConnection user, final Position position, final StairData stair) {
        final BlockFace facing = stair.getFacing();
        StairData relativeStair = StairConnectionHandler.stairDataMap.get(this.getBlockData(user, position.getRelative(facing)));
        if (relativeStair != null && relativeStair.isBottom() == stair.isBottom()) {
            final BlockFace facing2 = relativeStair.getFacing();
            if (facing.axis() != facing2.axis() && this.checkOpposite(user, stair, position, facing2.opposite())) {
                return (facing2 == this.rotateAntiClockwise(facing)) ? 3 : 4;
            }
        }
        relativeStair = StairConnectionHandler.stairDataMap.get(this.getBlockData(user, position.getRelative(facing.opposite())));
        if (relativeStair != null && relativeStair.isBottom() == stair.isBottom()) {
            final BlockFace facing2 = relativeStair.getFacing();
            if (facing.axis() != facing2.axis() && this.checkOpposite(user, stair, position, facing2)) {
                return (facing2 == this.rotateAntiClockwise(facing)) ? 1 : 2;
            }
        }
        return 0;
    }
    
    private boolean checkOpposite(final UserConnection user, final StairData stair, final Position position, final BlockFace face) {
        final StairData relativeStair = StairConnectionHandler.stairDataMap.get(this.getBlockData(user, position.getRelative(face)));
        return relativeStair == null || relativeStair.getFacing() != stair.getFacing() || relativeStair.isBottom() != stair.isBottom();
    }
    
    private BlockFace rotateAntiClockwise(final BlockFace face) {
        switch (face) {
            case NORTH: {
                return BlockFace.WEST;
            }
            case SOUTH: {
                return BlockFace.EAST;
            }
            case EAST: {
                return BlockFace.NORTH;
            }
            case WEST: {
                return BlockFace.SOUTH;
            }
            default: {
                return face;
            }
        }
    }
    
    static {
        stairDataMap = new HashMap<Integer, StairData>();
        connectedBlocks = new HashMap<Short, Integer>();
    }
    
    private static final class StairData
    {
        private final boolean bottom;
        private final byte shape;
        private final byte type;
        private final BlockFace facing;
        
        private StairData(final boolean bottom, final byte shape, final byte type, final BlockFace facing) {
            this.bottom = bottom;
            this.shape = shape;
            this.type = type;
            this.facing = facing;
        }
        
        public boolean isBottom() {
            return this.bottom;
        }
        
        public byte getShape() {
            return this.shape;
        }
        
        public byte getType() {
            return this.type;
        }
        
        public BlockFace getFacing() {
            return this.facing;
        }
    }
}
