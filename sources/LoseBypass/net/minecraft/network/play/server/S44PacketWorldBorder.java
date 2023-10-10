/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

public class S44PacketWorldBorder
implements Packet<INetHandlerPlayClient> {
    private Action action;
    private int size;
    private double centerX;
    private double centerZ;
    private double targetSize;
    private double diameter;
    private long timeUntilTarget;
    private int warningTime;
    private int warningDistance;

    public S44PacketWorldBorder() {
    }

    public S44PacketWorldBorder(WorldBorder border, Action actionIn) {
        this.action = actionIn;
        this.centerX = border.getCenterX();
        this.centerZ = border.getCenterZ();
        this.diameter = border.getDiameter();
        this.targetSize = border.getTargetSize();
        this.timeUntilTarget = border.getTimeUntilTarget();
        this.size = border.getSize();
        this.warningDistance = border.getWarningDistance();
        this.warningTime = border.getWarningTime();
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        switch (1.$SwitchMap$net$minecraft$network$play$server$S44PacketWorldBorder$Action[this.action.ordinal()]) {
            case 1: {
                this.targetSize = buf.readDouble();
                return;
            }
            case 2: {
                this.diameter = buf.readDouble();
                this.targetSize = buf.readDouble();
                this.timeUntilTarget = buf.readVarLong();
                return;
            }
            case 3: {
                this.centerX = buf.readDouble();
                this.centerZ = buf.readDouble();
                return;
            }
            case 4: {
                this.warningDistance = buf.readVarIntFromBuffer();
                return;
            }
            case 5: {
                this.warningTime = buf.readVarIntFromBuffer();
                return;
            }
            case 6: {
                this.centerX = buf.readDouble();
                this.centerZ = buf.readDouble();
                this.diameter = buf.readDouble();
                this.targetSize = buf.readDouble();
                this.timeUntilTarget = buf.readVarLong();
                this.size = buf.readVarIntFromBuffer();
                this.warningDistance = buf.readVarIntFromBuffer();
                this.warningTime = buf.readVarIntFromBuffer();
                return;
            }
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        switch (1.$SwitchMap$net$minecraft$network$play$server$S44PacketWorldBorder$Action[this.action.ordinal()]) {
            case 1: {
                buf.writeDouble(this.targetSize);
                return;
            }
            case 2: {
                buf.writeDouble(this.diameter);
                buf.writeDouble(this.targetSize);
                buf.writeVarLong(this.timeUntilTarget);
                return;
            }
            case 3: {
                buf.writeDouble(this.centerX);
                buf.writeDouble(this.centerZ);
                return;
            }
            case 4: {
                buf.writeVarIntToBuffer(this.warningDistance);
                return;
            }
            case 5: {
                buf.writeVarIntToBuffer(this.warningTime);
                return;
            }
            case 6: {
                buf.writeDouble(this.centerX);
                buf.writeDouble(this.centerZ);
                buf.writeDouble(this.diameter);
                buf.writeDouble(this.targetSize);
                buf.writeVarLong(this.timeUntilTarget);
                buf.writeVarIntToBuffer(this.size);
                buf.writeVarIntToBuffer(this.warningDistance);
                buf.writeVarIntToBuffer(this.warningTime);
                return;
            }
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleWorldBorder(this);
    }

    public void func_179788_a(WorldBorder border) {
        switch (1.$SwitchMap$net$minecraft$network$play$server$S44PacketWorldBorder$Action[this.action.ordinal()]) {
            case 1: {
                border.setTransition(this.targetSize);
                return;
            }
            case 2: {
                border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
                return;
            }
            case 3: {
                border.setCenter(this.centerX, this.centerZ);
                return;
            }
            case 4: {
                border.setWarningDistance(this.warningDistance);
                return;
            }
            case 5: {
                border.setWarningTime(this.warningTime);
                return;
            }
            case 6: {
                border.setCenter(this.centerX, this.centerZ);
                if (this.timeUntilTarget > 0L) {
                    border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
                } else {
                    border.setTransition(this.targetSize);
                }
                border.setSize(this.size);
                border.setWarningDistance(this.warningDistance);
                border.setWarningTime(this.warningTime);
                return;
            }
        }
    }

    public static enum Action {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS;

    }
}

