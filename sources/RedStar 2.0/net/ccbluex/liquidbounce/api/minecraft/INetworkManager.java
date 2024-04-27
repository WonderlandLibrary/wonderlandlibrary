package net.ccbluex.liquidbounce.api.minecraft;

import javax.crypto.SecretKey;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\n\u0000\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\bf\u000020J020H&J020\bH&J020\b2\f\t\b00\nH&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "", "enableEncryption", "", "secretKey", "Ljavax/crypto/SecretKey;", "sendPacket", "packet", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "any", "Lkotlin/Function0;", "Pride"})
public interface INetworkManager {
    public void sendPacket(@NotNull IPacket var1);

    public void enableEncryption(@NotNull SecretKey var1);

    public void sendPacket(@NotNull IPacket var1, @NotNull Function0<Unit> var2);
}
