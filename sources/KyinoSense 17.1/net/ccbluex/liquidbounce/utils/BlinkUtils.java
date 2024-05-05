/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import java.math.BigInteger;
import java.util.LinkedList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0017\n\u0002\u0010\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0012\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010&\u001a\u00020\u00042\n\b\u0002\u0010'\u001a\u0004\u0018\u00010(J&\u0010)\u001a\u00020*2\n\b\u0002\u0010'\u001a\u0004\u0018\u00010(2\b\b\u0002\u0010+\u001a\u00020\u00062\b\b\u0002\u0010,\u001a\u00020\u0004J\u0012\u0010-\u001a\u00020\u00062\b\b\u0002\u0010'\u001a\u00020(H\u0002J0\u0010.\u001a\u00020*2\n\b\u0002\u0010'\u001a\u0004\u0018\u00010(2\b\b\u0002\u0010+\u001a\u00020\u00062\b\b\u0002\u0010,\u001a\u00020\u00042\b\b\u0002\u0010/\u001a\u00020\u0004Jt\u00100\u001a\u00020*2\b\b\u0002\u00101\u001a\u00020\u00062\b\b\u0002\u00102\u001a\u00020\u00062\b\b\u0002\u00103\u001a\u00020\u00062\b\b\u0002\u00104\u001a\u00020\u00062\b\b\u0002\u00105\u001a\u00020\u00062\b\b\u0002\u00106\u001a\u00020\u00062\b\b\u0002\u00107\u001a\u00020\u00062\b\b\u0002\u00108\u001a\u00020\u00062\b\b\u0002\u00109\u001a\u00020\u00062\b\b\u0002\u0010:\u001a\u00020\u00062\b\b\u0002\u0010;\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\b\"\u0004\b\r\u0010\nR\u001a\u0010\u000e\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\b\"\u0004\b\u0010\u0010\nR\u001a\u0010\u0011\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\b\"\u0004\b\u0013\u0010\nR\u001a\u0010\u0014\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\b\"\u0004\b\u0016\u0010\nR\u001a\u0010\u0017\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\b\"\u0004\b\u0019\u0010\nR\u001a\u0010\u001a\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\b\"\u0004\b\u001c\u0010\nR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0!0 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010#\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\b\"\u0004\b%\u0010\n\u00a8\u0006<"}, d2={"Lnet/ccbluex/liquidbounce/utils/BlinkUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "MisMatch_Type", "", "abilitiesStat", "", "getAbilitiesStat", "()Z", "setAbilitiesStat", "(Z)V", "actionStat", "getActionStat", "setActionStat", "interactStat", "getInteractStat", "setInteractStat", "invStat", "getInvStat", "setInvStat", "keepAliveStat", "getKeepAliveStat", "setKeepAliveStat", "movingPacketStat", "getMovingPacketStat", "setMovingPacketStat", "otherPacket", "getOtherPacket", "setOtherPacket", "packetToggleStat", "", "playerBuffer", "Ljava/util/LinkedList;", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "transactionStat", "getTransactionStat", "setTransactionStat", "bufferSize", "packetType", "", "clearPacket", "", "onlySelected", "amount", "isBlacklisted", "releasePacket", "minBuff", "setBlinkState", "off", "release", "all", "packetMoving", "packetTransaction", "packetKeepAlive", "packetAction", "packetAbilities", "packetInventory", "packetInteract", "other", "KyinoClient"})
public final class BlinkUtils
extends MinecraftInstance {
    private static final LinkedList<Packet<INetHandlerPlayServer>> playerBuffer;
    public static final int MisMatch_Type = -302;
    private static boolean movingPacketStat;
    private static boolean transactionStat;
    private static boolean keepAliveStat;
    private static boolean actionStat;
    private static boolean abilitiesStat;
    private static boolean invStat;
    private static boolean interactStat;
    private static boolean otherPacket;
    private static boolean[] packetToggleStat;
    public static final BlinkUtils INSTANCE;

    public final boolean getMovingPacketStat() {
        return movingPacketStat;
    }

    public final void setMovingPacketStat(boolean bl) {
        movingPacketStat = bl;
    }

    public final boolean getTransactionStat() {
        return transactionStat;
    }

    public final void setTransactionStat(boolean bl) {
        transactionStat = bl;
    }

    public final boolean getKeepAliveStat() {
        return keepAliveStat;
    }

    public final void setKeepAliveStat(boolean bl) {
        keepAliveStat = bl;
    }

    public final boolean getActionStat() {
        return actionStat;
    }

    public final void setActionStat(boolean bl) {
        actionStat = bl;
    }

    public final boolean getAbilitiesStat() {
        return abilitiesStat;
    }

    public final void setAbilitiesStat(boolean bl) {
        abilitiesStat = bl;
    }

    public final boolean getInvStat() {
        return invStat;
    }

    public final void setInvStat(boolean bl) {
        invStat = bl;
    }

    public final boolean getInteractStat() {
        return interactStat;
    }

    public final void setInteractStat(boolean bl) {
        interactStat = bl;
    }

    public final boolean getOtherPacket() {
        return otherPacket;
    }

    public final void setOtherPacket(boolean bl) {
        otherPacket = bl;
    }

    public final void releasePacket(@Nullable String packetType, boolean onlySelected, int amount, int minBuff) {
        int count = 0;
        String string = packetType;
        if (string == null) {
            count = -1;
            for (Packet packet : playerBuffer) {
                String string2 = packet.getClass().getSimpleName();
                Intrinsics.checkExpressionValueIsNotNull(string2, "packets.javaClass.simpleName");
                int n = 1;
                int packetID = new BigInteger(StringsKt.substring(string2, new IntRange(n, 2)), 16).intValue();
                if (!packetToggleStat[packetID] && onlySelected) continue;
                Packet packet2 = packet;
                Intrinsics.checkExpressionValueIsNotNull(packet2, "packets");
                PacketUtils.sendPacketNoEvent(packet2);
            }
        } else {
            LinkedList<Packet> linkedList = new LinkedList<Packet>();
            for (Packet packet : playerBuffer) {
                String className = packet.getClass().getSimpleName();
                if (!StringsKt.equals(className, packetType, true)) continue;
                linkedList.add(packet);
            }
            while (linkedList.size() > minBuff && (count < amount || amount <= 0)) {
                Object e = linkedList.pop();
                Intrinsics.checkExpressionValueIsNotNull(e, "tempBuffer.pop()");
                PacketUtils.sendPacketNoEvent((Packet)e);
                ++count;
            }
        }
        this.clearPacket(packetType, onlySelected, count);
    }

    public static /* synthetic */ void releasePacket$default(BlinkUtils blinkUtils, String string, boolean bl, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            string = null;
        }
        if ((n3 & 2) != 0) {
            bl = false;
        }
        if ((n3 & 4) != 0) {
            n = -1;
        }
        if ((n3 & 8) != 0) {
            n2 = 0;
        }
        blinkUtils.releasePacket(string, bl, n, n2);
    }

    public final void clearPacket(@Nullable String packetType, boolean onlySelected, int amount) {
        String string = packetType;
        if (string == null) {
            LinkedList<Packet> tempBuffer = new LinkedList<Packet>();
            for (Packet packet : playerBuffer) {
                String string2 = packet.getClass().getSimpleName();
                Intrinsics.checkExpressionValueIsNotNull(string2, "packets.javaClass.simpleName");
                int n = 1;
                int packetID = new BigInteger(StringsKt.substring(string2, new IntRange(n, 2)), 16).intValue();
                if (packetToggleStat[packetID] || !onlySelected) continue;
                tempBuffer.add(packet);
            }
            playerBuffer.clear();
            for (Packet packet : tempBuffer) {
                playerBuffer.add((Packet<INetHandlerPlayServer>)packet);
            }
        } else {
            int count = 0;
            LinkedList<Packet> linkedList = new LinkedList<Packet>();
            for (Packet packet : playerBuffer) {
                String className = packet.getClass().getSimpleName();
                if (!StringsKt.equals(className, packetType, true)) {
                    linkedList.add(packet);
                    continue;
                }
                if (++count <= amount) continue;
                linkedList.add(packet);
            }
            playerBuffer.clear();
            for (Packet packet : linkedList) {
                playerBuffer.add((Packet<INetHandlerPlayServer>)packet);
            }
        }
    }

    public static /* synthetic */ void clearPacket$default(BlinkUtils blinkUtils, String string, boolean bl, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string = null;
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = -1;
        }
        blinkUtils.clearPacket(string, bl, n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean isBlacklisted(String packetType) {
        String string = packetType;
        switch (string.hashCode()) {
            case 2128815408: {
                if (!string.equals("C01PacketEncryptionResponse")) return false;
                return true;
            }
            case -255714108: {
                if (!string.equals("C00PacketLoginStart")) return false;
                return true;
            }
            case -1423059366: {
                if (!string.equals("C00PacketServerQuery")) return false;
                return true;
            }
            case 936222132: {
                if (!string.equals("C00Handshake")) return false;
                return true;
            }
            case -579696962: {
                if (!string.equals("C01PacketPing")) return false;
                return true;
            }
            case 1276952259: {
                if (!string.equals("C01PacketChatMessage")) return false;
                return true;
            }
        }
        return false;
    }

    static /* synthetic */ boolean isBlacklisted$default(BlinkUtils blinkUtils, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = "";
        }
        return blinkUtils.isBlacklisted(string);
    }

    /*
     * WARNING - void declaration
     */
    public final void setBlinkState(boolean off, boolean release, boolean all, boolean packetMoving, boolean packetTransaction, boolean packetKeepAlive, boolean packetAction, boolean packetAbilities, boolean packetInventory, boolean packetInteract, boolean other) {
        if (release) {
            BlinkUtils.releasePacket$default(this, null, false, 0, 0, 15, null);
        }
        movingPacketStat = packetMoving && !off || all;
        transactionStat = packetTransaction && !off || all;
        keepAliveStat = packetKeepAlive && !off || all;
        actionStat = packetAction && !off || all;
        abilitiesStat = packetAbilities && !off || all;
        invStat = packetInventory && !off || all;
        interactStat = packetInteract && !off || all;
        boolean bl = otherPacket = other && !off || all;
        if (all) {
            int n = 0;
            int n2 = packetToggleStat.length;
            while (n < n2) {
                void i;
                BlinkUtils.packetToggleStat[i] = true;
                ++i;
            }
        } else {
            int n = packetToggleStat.length;
            block11: for (int i = 0; i < n; ++i) {
                switch (i) {
                    case 0: {
                        BlinkUtils.packetToggleStat[i] = keepAliveStat;
                        continue block11;
                    }
                    case 1: 
                    case 17: 
                    case 18: 
                    case 20: 
                    case 21: 
                    case 23: 
                    case 24: 
                    case 25: {
                        BlinkUtils.packetToggleStat[i] = otherPacket;
                        continue block11;
                    }
                    case 3: 
                    case 4: 
                    case 5: 
                    case 6: {
                        BlinkUtils.packetToggleStat[i] = movingPacketStat;
                        continue block11;
                    }
                    case 15: {
                        BlinkUtils.packetToggleStat[i] = transactionStat;
                        continue block11;
                    }
                    case 2: 
                    case 9: 
                    case 10: 
                    case 11: {
                        BlinkUtils.packetToggleStat[i] = actionStat;
                        continue block11;
                    }
                    case 12: 
                    case 19: {
                        BlinkUtils.packetToggleStat[i] = abilitiesStat;
                        continue block11;
                    }
                    case 13: 
                    case 14: 
                    case 16: 
                    case 22: {
                        BlinkUtils.packetToggleStat[i] = invStat;
                        continue block11;
                    }
                    case 7: 
                    case 8: {
                        BlinkUtils.packetToggleStat[i] = interactStat;
                    }
                }
            }
        }
    }

    public static /* synthetic */ void setBlinkState$default(BlinkUtils blinkUtils, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, boolean bl7, boolean bl8, boolean bl9, boolean bl10, boolean bl11, int n, Object object) {
        if ((n & 1) != 0) {
            bl = false;
        }
        if ((n & 2) != 0) {
            bl2 = false;
        }
        if ((n & 4) != 0) {
            bl3 = false;
        }
        if ((n & 8) != 0) {
            bl4 = movingPacketStat;
        }
        if ((n & 0x10) != 0) {
            bl5 = transactionStat;
        }
        if ((n & 0x20) != 0) {
            bl6 = keepAliveStat;
        }
        if ((n & 0x40) != 0) {
            bl7 = actionStat;
        }
        if ((n & 0x80) != 0) {
            bl8 = abilitiesStat;
        }
        if ((n & 0x100) != 0) {
            bl9 = invStat;
        }
        if ((n & 0x200) != 0) {
            bl10 = interactStat;
        }
        if ((n & 0x400) != 0) {
            bl11 = otherPacket;
        }
        blinkUtils.setBlinkState(bl, bl2, bl3, bl4, bl5, bl6, bl7, bl8, bl9, bl10, bl11);
    }

    public final int bufferSize(@Nullable String packetType) {
        int n;
        String string = packetType;
        if (string == null) {
            n = playerBuffer.size();
        } else {
            int packetCount = 0;
            boolean flag = false;
            for (Packet packet : playerBuffer) {
                String className = packet.getClass().getSimpleName();
                if (!StringsKt.equals(className, packetType, true)) continue;
                flag = true;
                ++packetCount;
            }
            n = flag ? packetCount : -302;
        }
        return n;
    }

    public static /* synthetic */ int bufferSize$default(BlinkUtils blinkUtils, String string, int n, Object object) {
        if ((n & 1) != 0) {
            string = null;
        }
        return blinkUtils.bufferSize(string);
    }

    private BlinkUtils() {
    }

    static {
        BlinkUtils blinkUtils;
        INSTANCE = blinkUtils = new BlinkUtils();
        playerBuffer = new LinkedList();
        packetToggleStat = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        BlinkUtils.setBlinkState$default(blinkUtils, true, true, false, false, false, false, false, false, false, false, false, 2044, null);
        BlinkUtils.clearPacket$default(blinkUtils, null, false, 0, 7, null);
    }
}

