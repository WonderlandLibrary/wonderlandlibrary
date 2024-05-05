/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 */
package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import org.apache.logging.log4j.LogManager;

/*
 * Exception performing whole class analysis.
 */
public class EnumConnectionState
extends Enum<EnumConnectionState> {
    public static final /* enum */ EnumConnectionState HANDSHAKING;
    public static final /* enum */ EnumConnectionState PLAY;
    public static final /* enum */ EnumConnectionState STATUS;
    public static final /* enum */ EnumConnectionState LOGIN;
    private static int field_181136_e;
    private static int field_181137_f;
    private static final EnumConnectionState[] STATES_BY_ID;
    private static final Map<Class<? extends Packet>, EnumConnectionState> STATES_BY_CLASS;
    private final int id;
    private final Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet>>> directionMaps;
    private static final /* synthetic */ EnumConnectionState[] $VALUES;

    public static EnumConnectionState[] values() {
        return (EnumConnectionState[])$VALUES.clone();
    }

    public static EnumConnectionState valueOf(String name) {
        return Enum.valueOf(EnumConnectionState.class, name);
    }

    private EnumConnectionState(int protocolId) {
        super(string, n);
        this.directionMaps = Maps.newEnumMap(EnumPacketDirection.class);
        this.id = protocolId;
    }

    protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet> packetClass) {
        BiMap<Integer, Class<? extends Packet>> bimap = this.directionMaps.get((Object)((Object)direction));
        if (bimap == null) {
            bimap = HashBiMap.create();
            this.directionMaps.put(direction, bimap);
        }
        if (bimap.containsValue(packetClass)) {
            String s = (Object)((Object)direction) + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
            LogManager.getLogger().fatal(s);
            throw new IllegalArgumentException(s);
        }
        bimap.put(bimap.size(), packetClass);
        return this;
    }

    public Integer getPacketId(EnumPacketDirection direction, Packet packetIn) {
        return (Integer)this.directionMaps.get((Object)((Object)direction)).inverse().get(packetIn.getClass());
    }

    public Packet getPacket(EnumPacketDirection direction, int packetId) throws InstantiationException, IllegalAccessException {
        Class oclass = (Class)this.directionMaps.get((Object)((Object)direction)).get(packetId);
        if (oclass == null) {
            return null;
        }
        Packet packet = (Packet)oclass.newInstance();
        return packet;
    }

    public int getId() {
        return this.id;
    }

    public static EnumConnectionState getById(int stateId) {
        if (stateId < field_181136_e) return null;
        if (stateId > field_181137_f) return null;
        EnumConnectionState enumConnectionState = STATES_BY_ID[stateId - field_181136_e];
        return enumConnectionState;
    }

    public static EnumConnectionState getFromPacket(Packet packetIn) {
        return STATES_BY_CLASS.get(packetIn.getClass());
    }

    /* synthetic */ EnumConnectionState(String x0, int x1, int x2, 1 x3) {
        this(x2);
    }

    /*
     * Exception decompiling
     */
    static {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[UNCONDITIONALDOLOOP]], but top level block is 4[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:306)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:159)
         *     at java.lang.Thread.run(Thread.java:748)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}

