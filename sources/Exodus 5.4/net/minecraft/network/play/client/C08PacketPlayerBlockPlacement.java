/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;

public class C08PacketPlayerBlockPlacement
implements Packet<INetHandlerPlayServer> {
    private float facingX;
    private int placedBlockDirection;
    private ItemStack stack;
    private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);
    private BlockPos position;
    private float facingZ;
    private float facingY;

    public float getPlacedBlockOffsetZ() {
        return this.facingZ;
    }

    public float getPlacedBlockOffsetX() {
        return this.facingX;
    }

    public C08PacketPlayerBlockPlacement(ItemStack itemStack) {
        this(field_179726_a, 255, itemStack, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeBlockPos(this.position);
        packetBuffer.writeByte(this.placedBlockDirection);
        packetBuffer.writeItemStackToBuffer(this.stack);
        packetBuffer.writeByte((int)(this.facingX * 16.0f));
        packetBuffer.writeByte((int)(this.facingY * 16.0f));
        packetBuffer.writeByte((int)(this.facingZ * 16.0f));
    }

    public C08PacketPlayerBlockPlacement(BlockPos blockPos, int n, ItemStack itemStack, float f, float f2, float f3) {
        this.position = blockPos;
        this.placedBlockDirection = n;
        this.stack = itemStack != null ? itemStack.copy() : null;
        this.facingX = f;
        this.facingY = f2;
        this.facingZ = f3;
    }

    public float getPlacedBlockOffsetY() {
        return this.facingY;
    }

    public int getPlacedBlockDirection() {
        return this.placedBlockDirection;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.position = packetBuffer.readBlockPos();
        this.placedBlockDirection = packetBuffer.readUnsignedByte();
        this.stack = packetBuffer.readItemStackFromBuffer();
        this.facingX = (float)packetBuffer.readUnsignedByte() / 16.0f;
        this.facingY = (float)packetBuffer.readUnsignedByte() / 16.0f;
        this.facingZ = (float)packetBuffer.readUnsignedByte() / 16.0f;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public C08PacketPlayerBlockPlacement() {
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processPlayerBlockPlacement(this);
    }
}

