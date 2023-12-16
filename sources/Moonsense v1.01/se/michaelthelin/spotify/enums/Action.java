// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum Action
{
    INTERRUPTING_PLAYBACK("interrupting_playback"), 
    PAUSING("pausing"), 
    RESUMING("resuming"), 
    SEEKING("seeking"), 
    SKIPPING_NEXT("skipping_next"), 
    SKIPPING_PREV("skipping_prev"), 
    TOGGLING_REPEAT_CONTEXT("toggling_repeat_context"), 
    TOGGLING_SHUFFLE("toggling_shuffle"), 
    TOGGLING_REPEAT_TRACK("toggling_repeat_track"), 
    TRANSFERRING_PLAYBACK("transferring_playback");
    
    private static final Map<String, Action> map;
    public final String key;
    
    private Action(final String key) {
        this.key = key;
    }
    
    public static Action keyOf(final String key) {
        return Action.map.get(key);
    }
    
    public String getKey() {
        return this.key;
    }
    
    static {
        map = new HashMap<String, Action>();
        for (final Action action : values()) {
            Action.map.put(action.key, action);
        }
    }
}
