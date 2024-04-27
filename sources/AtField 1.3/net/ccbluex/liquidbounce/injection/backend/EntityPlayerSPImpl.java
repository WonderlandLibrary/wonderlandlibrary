/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.text.ITextComponent
 */
package net.ccbluex.liquidbounce.injection.backend;

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

public class EntityPlayerSPImpl
extends AbstractClientPlayerImpl
implements IEntityPlayerSP {
    @Override
    public float getHorseJumpPower() {
        return ((EntityPlayerSP)this.getWrapped()).field_110321_bQ;
    }

    public EntityPlayerSPImpl(EntityPlayerSP entityPlayerSP) {
        super((AbstractClientPlayer)entityPlayerSP);
    }

    @Override
    public void setServerSprintState(boolean bl) {
        ((EntityPlayerSP)this.getWrapped()).field_175171_bO = bl;
    }

    @Override
    public boolean getServerSprintState() {
        return ((EntityPlayerSP)this.getWrapped()).field_175171_bO;
    }

    @Override
    public boolean isSneaking() {
        return this.getSneaking();
    }

    @Override
    public void closeScreen() {
        ((EntityPlayerSP)this.getWrapped()).func_71053_j();
    }

    @Override
    public void respawnPlayer() {
        ((EntityPlayerSP)this.getWrapped()).func_71004_bE();
    }

    @Override
    public int getHorseJumpPowerCounter() {
        return ((EntityPlayerSP)this.getWrapped()).field_110320_a;
    }

    @Override
    public boolean isHandActive() {
        return ((EntityPlayerSP)this.getWrapped()).func_184587_cr();
    }

    @Override
    public void sendChatMessage(String string) {
        ((EntityPlayerSP)this.getWrapped()).func_71165_d(string);
    }

    @Override
    public boolean getSneaking() {
        return ((EntityPlayerSP)this.getWrapped()).func_70093_af();
    }

    @Override
    public void addChatMessage(IIChatComponent iIChatComponent) {
        IIChatComponent iIChatComponent2 = iIChatComponent;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)this.getWrapped();
        boolean bl = false;
        ITextComponent iTextComponent = ((IChatComponentImpl)iIChatComponent2).getWrapped();
        entityPlayerSP.func_145747_a(iTextComponent);
    }

    @Override
    public void setHorseJumpPowerCounter(int n) {
        ((EntityPlayerSP)this.getWrapped()).field_110320_a = n;
    }

    @Override
    public IMovementInput getMovementInput() {
        MovementInput movementInput = ((EntityPlayerSP)this.getWrapped()).field_71158_b;
        boolean bl = false;
        return new MovementInputImpl(movementInput);
    }

    @Override
    public IINetHandlerPlayClient getSendQueue() {
        NetHandlerPlayClient netHandlerPlayClient = ((EntityPlayerSP)this.getWrapped()).field_71174_a;
        boolean bl = false;
        return new INetHandlerPlayClientImpl(netHandlerPlayClient);
    }

    @Override
    public void setHorseJumpPower(float f) {
        ((EntityPlayerSP)this.getWrapped()).field_110321_bQ = f;
    }
}

