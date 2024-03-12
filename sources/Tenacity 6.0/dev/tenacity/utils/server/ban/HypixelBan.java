// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.server.ban;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.text.SimpleDateFormat;

public class HypixelBan
{
    private static final SimpleDateFormat durationFormat;
    @Expose
    @SerializedName("reason")
    private final Reason reason;
    @Expose
    @SerializedName("unbanDate")
    private final long unbanDate;
    
    public HypixelBan(final Reason reason, final String duration) {
        this.reason = reason;
        if (duration == null) {
            this.unbanDate = 0L;
        }
        else {
            final long currentTime = System.currentTimeMillis();
            final String[] arr = duration.split(" ");
            long actualDuration = 0L;
            for (final String s : arr) {
                final long time = Long.parseLong(s.substring(0, s.length() - 1));
                if (s.endsWith("d")) {
                    actualDuration += time * 24L * 60L * 60L * 1000L;
                }
                if (s.endsWith("h")) {
                    actualDuration += time * 60L * 60L * 1000L;
                }
                if (s.endsWith("m")) {
                    actualDuration += time * 60L * 1000L;
                }
                if (s.endsWith("s")) {
                    actualDuration += time * 1000L;
                }
            }
            this.unbanDate = currentTime + actualDuration;
        }
    }
    
    public Reason getReason() {
        return this.reason;
    }
    
    public long getUnbanDate() {
        return this.unbanDate;
    }
    
    @Override
    public String toString() {
        return "HypixelBan(reason=" + this.getReason() + ", unbanDate=" + this.getUnbanDate() + ")";
    }
    
    static {
        durationFormat = new SimpleDateFormat("D'd' H'h' m'm' s's'");
    }
    
    public enum Reason
    {
        @Expose
        @SerializedName("security_alert")
        SECURITY_ALERT("Security alert"), 
        @Expose
        @SerializedName("security_alert_processed")
        SECURITY_ALERT_PROCCESSED("Security alert (processed)"), 
        @Expose
        @SerializedName("cheating")
        CHEATING("Cheating"), 
        @Expose
        @SerializedName("misc")
        MISC("Miscellaneous");
        
        private final String name;
        
        public String getName() {
            return this.name;
        }
        
        private Reason(final String name) {
            this.name = name;
        }
    }
}
