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

    public boolean equals(@Nullable Object object) {
        return object instanceof PacketBufferImpl && ((PacketBufferImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public IPacketBuffer writeString(String string) {
        this.wrapped.func_180714_a(string);
        return this;
    }

    @Override
    public void writeItemStackToBuffer(IItemStack iItemStack) {
        IItemStack iItemStack2 = iItemStack;
        PacketBuffer packetBuffer = this.wrapped;
        boolean bl = false;
        ItemStack itemStack = ((ItemStackImpl)iItemStack2).getWrapped();
        packetBuffer.func_150788_a(itemStack);
    }

    public PacketBufferImpl(PacketBuffer packetBuffer) {
        this.wrapped = packetBuffer;
    }

    @Override
    public void writeBytes(byte[] byArray) {
        this.wrapped.writeBytes(byArray);
    }

    public final PacketBuffer getWrapped() {
        return this.wrapped;
    }
}

