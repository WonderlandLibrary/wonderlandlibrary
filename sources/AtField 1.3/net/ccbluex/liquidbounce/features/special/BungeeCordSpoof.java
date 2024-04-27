/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.StringCompanionObject
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.special;

import java.util.Arrays;
import java.util.Random;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class BungeeCordSpoof
extends MinecraftInstance
implements Listenable {
    @JvmField
    public static boolean enabled;
    public static final Companion Companion;
    private static final Random RANDOM;

    static {
        Companion = new Companion(null);
        RANDOM = new Random();
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHandshake(iPacket) && enabled && iPacket.asCPacketHandshake().getRequestedState().isHandshake()) {
            ICPacketHandshake iCPacketHandshake = iPacket.asCPacketHandshake();
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String string = "%d.%d.%d.%d";
            Object[] objectArray = new Object[]{this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart()};
            StringBuilder stringBuilder = new StringBuilder().append(iCPacketHandshake.getIp()).append("\u0000");
            ICPacketHandshake iCPacketHandshake2 = iCPacketHandshake;
            boolean bl = false;
            String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
            iCPacketHandshake2.setIp(stringBuilder.append(string2).append("\u0000").append(StringsKt.replace$default((String)MinecraftInstance.mc.getSession().getPlayerId(), (String)"-", (String)"", (boolean)false, (int)4, null)).toString());
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    private final String getRandomIpPart() {
        return String.valueOf(RANDOM.nextInt(256));
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}

