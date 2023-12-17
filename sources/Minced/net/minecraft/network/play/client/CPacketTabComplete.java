// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketTabComplete implements Packet<INetHandlerPlayServer>
{
    private String message;
    private boolean hasTargetBlock;
    @Nullable
    private BlockPos targetBlock;
    
    public CPacketTabComplete() {
    }
    
    public CPacketTabComplete(final String messageIn, @Nullable final BlockPos targetBlockIn, final boolean hasTargetBlockIn) {
        this.message = messageIn;
        this.targetBlock = targetBlockIn;
        this.hasTargetBlock = hasTargetBlockIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.message = buf.readString(32767);
        this.hasTargetBlock = buf.readBoolean();
        final boolean flag = buf.readBoolean();
        if (flag) {
            this.targetBlock = buf.readBlockPos();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(StringUtils.substring(this.message, 0, 32767));
        buf.writeBoolean(this.hasTargetBlock);
        final boolean flag = this.targetBlock != null;
        buf.writeBoolean(flag);
        if (flag) {
            buf.writeBlockPos(this.targetBlock);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processTabComplete(this);
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Nullable
    public BlockPos getTargetBlock() {
        return this.targetBlock;
    }
    
    public boolean hasTargetBlock() {
        return this.hasTargetBlock;
    }
}
