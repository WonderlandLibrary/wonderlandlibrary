// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.net.Socket;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.Builder;

@Plugin(name = "SocketPerformancePreferences", category = "Core", printObject = true)
public class SocketPerformancePreferences implements Builder<SocketPerformancePreferences>, Cloneable
{
    @PluginBuilderAttribute
    @Required
    private int bandwidth;
    @PluginBuilderAttribute
    @Required
    private int connectionTime;
    @PluginBuilderAttribute
    @Required
    private int latency;
    
    @PluginBuilderFactory
    public static SocketPerformancePreferences newBuilder() {
        return new SocketPerformancePreferences();
    }
    
    public void apply(final Socket socket) {
        socket.setPerformancePreferences(this.connectionTime, this.latency, this.bandwidth);
    }
    
    @Override
    public SocketPerformancePreferences build() {
        try {
            return (SocketPerformancePreferences)this.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public int getBandwidth() {
        return this.bandwidth;
    }
    
    public int getConnectionTime() {
        return this.connectionTime;
    }
    
    public int getLatency() {
        return this.latency;
    }
    
    public void setBandwidth(final int bandwidth) {
        this.bandwidth = bandwidth;
    }
    
    public void setConnectionTime(final int connectionTime) {
        this.connectionTime = connectionTime;
    }
    
    public void setLatency(final int latency) {
        this.latency = latency;
    }
    
    @Override
    public String toString() {
        return "SocketPerformancePreferences [bandwidth=" + this.bandwidth + ", connectionTime=" + this.connectionTime + ", latency=" + this.latency + "]";
    }
}
