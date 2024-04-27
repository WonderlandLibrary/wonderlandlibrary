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
    private static final Random RANDOM;
    @JvmField
    public static boolean enabled;
    public static final Companion Companion;

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHandshake(packet) && enabled && packet.asCPacketHandshake().getRequestedState().isHandshake()) {
            ICPacketHandshake handshake = packet.asCPacketHandshake();
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String string = "%d.%d.%d.%d";
            Object[] objectArray = new Object[]{this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart()};
            StringBuilder stringBuilder = new StringBuilder().append(handshake.getIp()).append("\u0000");
            ICPacketHandshake iCPacketHandshake = handshake;
            boolean bl = false;
            String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
            iCPacketHandshake.setIp(stringBuilder.append(string2).append("\u0000").append(StringsKt.replace$default((String)MinecraftInstance.mc.getSession().getPlayerId(), (String)"-", (String)"", (boolean)false, (int)4, null)).toString());
        }
    }

    private final String getRandomIpPart() {
        return String.valueOf(RANDOM.nextInt(256));
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        Companion = new Companion(null);
        RANDOM = new Random();
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

