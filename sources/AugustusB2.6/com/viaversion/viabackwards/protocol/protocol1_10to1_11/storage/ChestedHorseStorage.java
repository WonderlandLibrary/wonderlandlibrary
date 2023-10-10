// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_10to1_11.storage;

public class ChestedHorseStorage
{
    private boolean chested;
    private int liamaStrength;
    private int liamaCarpetColor;
    private int liamaVariant;
    
    public ChestedHorseStorage() {
        this.liamaCarpetColor = -1;
    }
    
    public boolean isChested() {
        return this.chested;
    }
    
    public void setChested(final boolean chested) {
        this.chested = chested;
    }
    
    public int getLiamaStrength() {
        return this.liamaStrength;
    }
    
    public void setLiamaStrength(final int liamaStrength) {
        this.liamaStrength = liamaStrength;
    }
    
    public int getLiamaCarpetColor() {
        return this.liamaCarpetColor;
    }
    
    public void setLiamaCarpetColor(final int liamaCarpetColor) {
        this.liamaCarpetColor = liamaCarpetColor;
    }
    
    public int getLiamaVariant() {
        return this.liamaVariant;
    }
    
    public void setLiamaVariant(final int liamaVariant) {
        this.liamaVariant = liamaVariant;
    }
    
    @Override
    public String toString() {
        return "ChestedHorseStorage{chested=" + this.chested + ", liamaStrength=" + this.liamaStrength + ", liamaCarpetColor=" + this.liamaCarpetColor + ", liamaVariant=" + this.liamaVariant + '}';
    }
}
