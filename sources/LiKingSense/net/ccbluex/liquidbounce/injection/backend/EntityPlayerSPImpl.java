/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovementInput;
import net.ccbluex.liquidbounce.injection.backend.AbstractClientPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.IChatComponentImpl;
import net.ccbluex.liquidbounce.injection.backend.INetHandlerPlayClientImpl;
import net.ccbluex.liquidbounce.injection.backend.MovementInputImpl;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.MovementInput;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000R\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020(H\u0016J\b\u0010)\u001a\u00020&H\u0016J\b\u0010*\u001a\u00020&H\u0016J\u0010\u0010+\u001a\u00020&2\u0006\u0010,\u001a\u00020-H\u0016R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR$\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u000e8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u00188VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001b\u001a\u00020\u001c8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR$\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0007\u001a\u00020\u00158V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b \u0010\u0016\"\u0004\b!\u0010\"R\u0014\u0010#\u001a\u00020\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b$\u0010\u0016\u00a8\u0006."}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/EntityPlayerSPImpl;", "T", "Lnet/minecraft/client/entity/EntityPlayerSP;", "Lnet/ccbluex/liquidbounce/injection/backend/AbstractClientPlayerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "wrapped", "(Lnet/minecraft/client/entity/EntityPlayerSP;)V", "value", "", "horseJumpPower", "getHorseJumpPower", "()F", "setHorseJumpPower", "(F)V", "", "horseJumpPowerCounter", "getHorseJumpPowerCounter", "()I", "setHorseJumpPowerCounter", "(I)V", "isHandActive", "", "()Z", "movementInput", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovementInput;", "getMovementInput", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovementInput;", "sendQueue", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "getSendQueue", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "serverSprintState", "getServerSprintState", "setServerSprintState", "(Z)V", "sneaking", "getSneaking", "addChatMessage", "", "component", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "closeScreen", "respawnPlayer", "sendChatMessage", "msg", "", "LiKingSense"})
public class EntityPlayerSPImpl<T extends EntityPlayerSP>
extends AbstractClientPlayerImpl<T>
implements IEntityPlayerSP {
    @Override
    public int getHorseJumpPowerCounter() {
        return ((EntityPlayerSP)this.getWrapped()).field_110320_a;
    }

    @Override
    public void setHorseJumpPowerCounter(int value) {
        ((EntityPlayerSP)this.getWrapped()).field_110320_a = value;
    }

    @Override
    public float getHorseJumpPower() {
        return ((EntityPlayerSP)this.getWrapped()).field_110321_bQ;
    }

    @Override
    public void setHorseJumpPower(float value) {
        ((EntityPlayerSP)this.getWrapped()).field_110321_bQ = value;
    }

    @Override
    public boolean isHandActive() {
        return ((EntityPlayerSP)this.getWrapped()).func_184587_cr();
    }

    @Override
    @NotNull
    public IINetHandlerPlayClient getSendQueue() {
        NetHandlerPlayClient netHandlerPlayClient = ((EntityPlayerSP)this.getWrapped()).field_71174_a;
        Intrinsics.checkExpressionValueIsNotNull((Object)netHandlerPlayClient, (String)"wrapped.connection");
        NetHandlerPlayClient $this$wrap$iv = netHandlerPlayClient;
        boolean $i$f$wrap = false;
        return new INetHandlerPlayClientImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IMovementInput getMovementInput() {
        MovementInput movementInput = ((EntityPlayerSP)this.getWrapped()).field_71158_b;
        Intrinsics.checkExpressionValueIsNotNull((Object)movementInput, (String)"wrapped.movementInput");
        MovementInput $this$wrap$iv = movementInput;
        boolean $i$f$wrap = false;
        return new MovementInputImpl($this$wrap$iv);
    }

    @Override
    public boolean getSneaking() {
        return ((EntityPlayerSP)this.getWrapped()).func_70093_af();
    }

    @Override
    public boolean getServerSprintState() {
        return ((EntityPlayerSP)this.getWrapped()).field_175171_bO;
    }

    @Override
    public void setServerSprintState(boolean value) {
        ((EntityPlayerSP)this.getWrapped()).field_175171_bO = value;
    }

    @Override
    public void sendChatMessage(@NotNull String msg) {
        Intrinsics.checkParameterIsNotNull((Object)msg, (String)"msg");
        ((EntityPlayerSP)this.getWrapped()).func_71165_d(msg);
    }

    @Override
    public void respawnPlayer() {
        ((EntityPlayerSP)this.getWrapped()).func_71004_bE();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void addChatMessage(@NotNull IIChatComponent component) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)component, (String)"component");
        IIChatComponent iIChatComponent = component;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)this.getWrapped();
        boolean $i$f$unwrap = false;
        ITextComponent iTextComponent = ((IChatComponentImpl)$this$unwrap$iv).getWrapped();
        entityPlayerSP.func_145747_a(iTextComponent);
    }

    @Override
    public void closeScreen() {
        ((EntityPlayerSP)this.getWrapped()).func_71053_j();
    }

    public EntityPlayerSPImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((AbstractClientPlayer)wrapped);
    }
}

