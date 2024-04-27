package net.ccbluex.liquidbounce.features.special;

import java.util.Arrays;
import java.util.Random;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\u0000 \f2020:\fB¢J\b0HJ\b0HJ\b0\t2\n0H¨\r"}, d2={"Lnet/ccbluex/liquidbounce/features/special/BungeeCordSpoof;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "getRandomIpPart", "", "handleEvents", "", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "Companion", "Pride"})
public final class BungeeCordSpoof
extends MinecraftInstance
implements Listenable {
    private static final Random RANDOM;
    @JvmField
    public static boolean enabled;
    public static final Companion Companion;

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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
            Intrinsics.checkExpressionValueIsNotNull(string2, "java.lang.String.format(format, *args)");
            String string3 = string2;
            iCPacketHandshake.setIp(stringBuilder.append(string3).append("\u0000").append(StringsKt.replace$default(MinecraftInstance.mc.getSession().getPlayerId(), "-", "", false, 4, null)).toString());
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\u0000\n\n\u0000\b\u000020B\b¢R0X¢\n\u0000R08@X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/special/BungeeCordSpoof$Companion;", "", "()V", "RANDOM", "Ljava/util/Random;", "enabled", "", "Pride"})
    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
