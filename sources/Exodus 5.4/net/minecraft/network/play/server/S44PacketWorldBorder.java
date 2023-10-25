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
    private double diameter;
    private double centerX;
    private int warningTime;
    private Action action;
    private long timeUntilTarget;
    private int size;
    private int warningDistance;
    private double centerZ;
    private double targetSize;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.action = packetBuffer.readEnumValue(Action.class);
        switch (this.action) {
            case SET_SIZE: {
                this.targetSize = packetBuffer.readDouble();
                break;
            }
            case LERP_SIZE: {
                this.diameter = packetBuffer.readDouble();
                this.targetSize = packetBuffer.readDouble();
                this.timeUntilTarget = packetBuffer.readVarLong();
                break;
            }
            case SET_CENTER: {
                this.centerX = packetBuffer.readDouble();
                this.centerZ = packetBuffer.readDouble();
                break;
            }
            case SET_WARNING_BLOCKS: {
                this.warningDistance = packetBuffer.readVarIntFromBuffer();
                break;
            }
            case SET_WARNING_TIME: {
                this.warningTime = packetBuffer.readVarIntFromBuffer();
                break;
            }
            case INITIALIZE: {
                this.centerX = packetBuffer.readDouble();
                this.centerZ = packetBuffer.readDouble();
                this.diameter = packetBuffer.readDouble();
                this.targetSize = packetBuffer.readDouble();
                this.timeUntilTarget = packetBuffer.readVarLong();
                this.size = packetBuffer.readVarIntFromBuffer();
                this.warningDistance = packetBuffer.readVarIntFromBuffer();
                this.warningTime = packetBuffer.readVarIntFromBuffer();
            }
        }
    }

    public S44PacketWorldBorder(WorldBorder worldBorder, Action action) {
        this.action = action;
        this.centerX = worldBorder.getCenterX();
        this.centerZ = worldBorder.getCenterZ();
        this.diameter = worldBorder.getDiameter();
        this.targetSize = worldBorder.getTargetSize();
        this.timeUntilTarget = worldBorder.getTimeUntilTarget();
        this.size = worldBorder.getSize();
        this.warningDistance = worldBorder.getWarningDistance();
        this.warningTime = worldBorder.getWarningTime();
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeEnumValue(this.action);
        switch (this.action) {
            case SET_SIZE: {
                packetBuffer.writeDouble(this.targetSize);
                break;
            }
            case LERP_SIZE: {
                packetBuffer.writeDouble(this.diameter);
                packetBuffer.writeDouble(this.targetSize);
                packetBuffer.writeVarLong(this.timeUntilTarget);
                break;
            }
            case SET_CENTER: {
                packetBuffer.writeDouble(this.centerX);
                packetBuffer.writeDouble(this.centerZ);
                break;
            }
            case SET_WARNING_BLOCKS: {
                packetBuffer.writeVarIntToBuffer(this.warningDistance);
                break;
            }
            case SET_WARNING_TIME: {
                packetBuffer.writeVarIntToBuffer(this.warningTime);
                break;
            }
            case INITIALIZE: {
                packetBuffer.writeDouble(this.centerX);
                packetBuffer.writeDouble(this.centerZ);
                packetBuffer.writeDouble(this.diameter);
                packetBuffer.writeDouble(this.targetSize);
                packetBuffer.writeVarLong(this.timeUntilTarget);
                packetBuffer.writeVarIntToBuffer(this.size);
                packetBuffer.writeVarIntToBuffer(this.warningDistance);
                packetBuffer.writeVarIntToBuffer(this.warningTime);
            }
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleWorldBorder(this);
    }

    public void func_179788_a(WorldBorder worldBorder) {
        switch (this.action) {
            case SET_SIZE: {
                worldBorder.setTransition(this.targetSize);
                break;
            }
            case LERP_SIZE: {
                worldBorder.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
                break;
            }
            case SET_CENTER: {
                worldBorder.setCenter(this.centerX, this.centerZ);
                break;
            }
            case SET_WARNING_BLOCKS: {
                worldBorder.setWarningDistance(this.warningDistance);
                break;
            }
            case SET_WARNING_TIME: {
                worldBorder.setWarningTime(this.warningTime);
                break;
            }
            case INITIALIZE: {
                worldBorder.setCenter(this.centerX, this.centerZ);
                if (this.timeUntilTarget > 0L) {
                    worldBorder.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
                } else {
                    worldBorder.setTransition(this.targetSize);
                }
                worldBorder.setSize(this.size);
                worldBorder.setWarningDistance(this.warningDistance);
                worldBorder.setWarningTime(this.warningTime);
            }
        }
    }

    public S44PacketWorldBorder() {
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

