/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.PacketBuffer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import org.jetbrains.annotations.Nullable;

public final class PacketBufferImpl
implements IPacketBuffer {
    private final PacketBuffer wrapped;

    @Override
    public void writeBytes(byte[] payload) {
        this.wrapped.writeBytes(payload);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void writeItemStackToBuffer(IItemStack itemStack) {
        void $this$unwrap$iv;
        IItemStack iItemStack = itemStack;
        PacketBuffer packetBuffer = this.wrapped;
        boolean $i$f$unwrap = false;
        ItemStack itemStack2 = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        packetBuffer.func_150788_a(itemStack2);
    }

    @Override
    public IPacketBuffer writeString(String vanilla) {
        this.wrapped.func_180714_a(vanilla);
        return this;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof PacketBufferImpl && ((PacketBufferImpl)other).wrapped.equals(this.wrapped);
    }

    public final PacketBuffer getWrapped() {
        return this.wrapped;
    }

    public PacketBufferImpl(PacketBuffer wrapped) {
        this.wrapped = wrapped;
    }
}

