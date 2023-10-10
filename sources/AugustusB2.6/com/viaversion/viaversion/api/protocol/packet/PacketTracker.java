// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.packet;

import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;

public class PacketTracker
{
    private final UserConnection connection;
    private long sentPackets;
    private long receivedPackets;
    private long startTime;
    private long intervalPackets;
    private long packetsPerSecond;
    private int secondsObserved;
    private int warnings;
    
    public PacketTracker(final UserConnection connection) {
        this.packetsPerSecond = -1L;
        this.connection = connection;
    }
    
    public void incrementSent() {
        ++this.sentPackets;
    }
    
    public boolean incrementReceived() {
        final long diff = System.currentTimeMillis() - this.startTime;
        if (diff >= 1000L) {
            this.packetsPerSecond = this.intervalPackets;
            this.startTime = System.currentTimeMillis();
            this.intervalPackets = 1L;
            return true;
        }
        ++this.intervalPackets;
        ++this.receivedPackets;
        return false;
    }
    
    public boolean exceedsMaxPPS() {
        if (this.connection.isClientSide()) {
            return false;
        }
        final ViaVersionConfig conf = Via.getConfig();
        if (conf.getMaxPPS() > 0 && this.packetsPerSecond >= conf.getMaxPPS()) {
            this.connection.disconnect(conf.getMaxPPSKickMessage().replace("%pps", Long.toString(this.packetsPerSecond)));
            return true;
        }
        if (conf.getMaxWarnings() > 0 && conf.getTrackingPeriod() > 0) {
            if (this.secondsObserved > conf.getTrackingPeriod()) {
                this.warnings = 0;
                this.secondsObserved = 1;
            }
            else {
                ++this.secondsObserved;
                if (this.packetsPerSecond >= conf.getWarningPPS()) {
                    ++this.warnings;
                }
                if (this.warnings >= conf.getMaxWarnings()) {
                    this.connection.disconnect(conf.getMaxWarningsKickMessage().replace("%pps", Long.toString(this.packetsPerSecond)));
                    return true;
                }
            }
        }
        return false;
    }
    
    public long getSentPackets() {
        return this.sentPackets;
    }
    
    public void setSentPackets(final long sentPackets) {
        this.sentPackets = sentPackets;
    }
    
    public long getReceivedPackets() {
        return this.receivedPackets;
    }
    
    public void setReceivedPackets(final long receivedPackets) {
        this.receivedPackets = receivedPackets;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }
    
    public long getIntervalPackets() {
        return this.intervalPackets;
    }
    
    public void setIntervalPackets(final long intervalPackets) {
        this.intervalPackets = intervalPackets;
    }
    
    public long getPacketsPerSecond() {
        return this.packetsPerSecond;
    }
    
    public void setPacketsPerSecond(final long packetsPerSecond) {
        this.packetsPerSecond = packetsPerSecond;
    }
    
    public int getSecondsObserved() {
        return this.secondsObserved;
    }
    
    public void setSecondsObserved(final int secondsObserved) {
        this.secondsObserved = secondsObserved;
    }
    
    public int getWarnings() {
        return this.warnings;
    }
    
    public void setWarnings(final int warnings) {
        this.warnings = warnings;
    }
}
