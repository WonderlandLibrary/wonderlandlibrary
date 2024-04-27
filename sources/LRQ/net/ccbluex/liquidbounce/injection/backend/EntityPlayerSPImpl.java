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
    public IINetHandlerPlayClient getSendQueue() {
        NetHandlerPlayClient $this$wrap$iv = ((EntityPlayerSP)this.getWrapped()).field_71174_a;
        boolean $i$f$wrap = false;
        return new INetHandlerPlayClientImpl($this$wrap$iv);
    }

    @Override
    public IMovementInput getMovementInput() {
        MovementInput $this$wrap$iv = ((EntityPlayerSP)this.getWrapped()).field_71158_b;
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
    public void sendChatMessage(String msg) {
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
    public void addChatMessage(IIChatComponent component) {
        void $this$unwrap$iv;
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

    public EntityPlayerSPImpl(T wrapped) {
        super((AbstractClientPlayer)wrapped);
    }
}

