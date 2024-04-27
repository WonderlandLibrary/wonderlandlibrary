package net.ccbluex.liquidbounce.api.minecraft.client.entity;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IAbstractClientPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovementInput;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\n\n\u0000\n\n\b\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\bf\u000020J020H&J\b 0H&J\b!0H&J\"02#0$H&R0X¦¢\f\b\"\bR\b0\tX¦¢\f\b\n\"\b\f\rR0X¦¢\bR0X¦¢\bR0X¦¢\f\b\"\b¨%"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IAbstractClientPlayer;", "horseJumpPower", "", "getHorseJumpPower", "()F", "setHorseJumpPower", "(F)V", "horseJumpPowerCounter", "", "getHorseJumpPowerCounter", "()I", "setHorseJumpPowerCounter", "(I)V", "movementInput", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovementInput;", "getMovementInput", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovementInput;", "sendQueue", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "getSendQueue", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "serverSprintState", "", "getServerSprintState", "()Z", "setServerSprintState", "(Z)V", "addChatMessage", "", "component", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "closeScreen", "respawnPlayer", "sendChatMessage", "msg", "", "Pride"})
public interface IEntityPlayerSP
extends IAbstractClientPlayer {
    public int getHorseJumpPowerCounter();

    public void setHorseJumpPowerCounter(int var1);

    public float getHorseJumpPower();

    public void setHorseJumpPower(float var1);

    @NotNull
    public IINetHandlerPlayClient getSendQueue();

    @NotNull
    public IMovementInput getMovementInput();

    public boolean getServerSprintState();

    public void setServerSprintState(boolean var1);

    public void sendChatMessage(@NotNull String var1);

    public void respawnPlayer();

    public void addChatMessage(@NotNull IIChatComponent var1);

    public void closeScreen();
}
