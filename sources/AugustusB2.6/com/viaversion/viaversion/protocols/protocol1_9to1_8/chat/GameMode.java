// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.chat;

public enum GameMode
{
    SURVIVAL(0, "Survival Mode"), 
    CREATIVE(1, "Creative Mode"), 
    ADVENTURE(2, "Adventure Mode"), 
    SPECTATOR(3, "Spectator Mode");
    
    private final int id;
    private final String text;
    
    private GameMode(final int id, final String text) {
        this.id = id;
        this.text = text;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getText() {
        return this.text;
    }
    
    public static GameMode getById(final int id) {
        for (final GameMode gm : values()) {
            if (gm.getId() == id) {
                return gm;
            }
        }
        return null;
    }
}
