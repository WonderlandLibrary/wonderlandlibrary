// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.config;

import net.minecraft.src.Config;

public class VillagerProfession
{
    private int profession;
    private int[] careers;
    
    public VillagerProfession(final int profession) {
        this(profession, null);
    }
    
    public VillagerProfession(final int profession, final int career) {
        this(profession, new int[] { career });
    }
    
    public VillagerProfession(final int profession, final int[] careers) {
        this.profession = profession;
        this.careers = careers;
    }
    
    public boolean matches(final int prof, final int car) {
        return this.profession == prof && (this.careers == null || Config.equalsOne(car, this.careers));
    }
    
    private boolean hasCareer(final int car) {
        return this.careers != null && Config.equalsOne(car, this.careers);
    }
    
    public boolean addCareer(final int car) {
        if (this.careers == null) {
            this.careers = new int[] { car };
            return true;
        }
        if (this.hasCareer(car)) {
            return false;
        }
        this.careers = Config.addIntToArray(this.careers, car);
        return true;
    }
    
    public int getProfession() {
        return this.profession;
    }
    
    public int[] getCareers() {
        return this.careers;
    }
    
    @Override
    public String toString() {
        return (this.careers == null) ? ("" + this.profession) : ("" + this.profession + ":" + Config.arrayToString(this.careers));
    }
}
