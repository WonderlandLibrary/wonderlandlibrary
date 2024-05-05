package net.arikia.dev.drpc;

import com.sun.jna.*;
import java.util.*;

public class DiscordRichPresence extends Structure
{
    public String state;
    public String details;
    public long startTimestamp;
    public long endTimestamp;
    public String largeImageKey;
    public String largeImageText;
    public String smallImageKey;
    public String smallImageText;
    public String partyId;
    public int partySize;
    public int partyMax;
    @Deprecated
    public String matchSecret;
    public String spectateSecret;
    public String joinSecret;
    @Deprecated
    public int instance;
    
    public List<String> getFieldOrder() {
        return Arrays.asList("state", "details", "startTimestamp", "endTimestamp", "largeImageKey", "largeImageText", "smallImageKey", "smallImageText", "partyId", "partySize", "partyMax", "matchSecret", "joinSecret", "spectateSecret", "instance");
    }
    
    public static class Builder
    {
        private DiscordRichPresence p;
        
        public Builder(final String state) {
            this.p = new DiscordRichPresence();
            this.p.state = state;
        }
        
        public Builder setDetails(final String details) {
            this.p.details = details;
            return this;
        }
        
        public Builder setStartTimestamps(final long start) {
            this.p.startTimestamp = start;
            return this;
        }
        
        public Builder setEndTimestamp(final long end) {
            this.p.endTimestamp = end;
            return this;
        }
        
        public Builder setBigImage(final String key, final String text) {
            if (text != null && !text.equalsIgnoreCase("") && key == null) {
                throw new IllegalArgumentException("Image key must not be null when assigning a hover text.");
            }
            this.p.largeImageKey = key;
            this.p.largeImageText = text;
            return this;
        }
        
        public Builder setSmallImage(final String key, final String text) {
            if (text != null && !text.equalsIgnoreCase("") && key == null) {
                throw new IllegalArgumentException("Image key must not be null when assigning a hover text.");
            }
            this.p.smallImageKey = key;
            this.p.smallImageText = text;
            return this;
        }
        
        public Builder setParty(final String party, final int size, final int max) {
            this.p.partyId = party;
            this.p.partySize = size;
            this.p.partyMax = max;
            return this;
        }
        
        @Deprecated
        public Builder setSecrets(final String match, final String join, final String spectate) {
            this.p.matchSecret = match;
            this.p.joinSecret = join;
            this.p.spectateSecret = spectate;
            return this;
        }
        
        public Builder setSecrets(final String join, final String spectate) {
            this.p.joinSecret = join;
            this.p.spectateSecret = spectate;
            return this;
        }
        
        @Deprecated
        public Builder setInstance(final boolean i) {
            this.p.instance = (i ? 1 : 0);
            return this;
        }
        
        public DiscordRichPresence build() {
            return this.p;
        }
    }
}
