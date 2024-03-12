// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import dev.tenacity.utils.misc.DoxUtil;
import net.minecraft.entity.EntityLivingBase;

public class PlayerDox
{
    private final EntityLivingBase player;
    private final String state;
    private final String liscenseNumber;
    private final String DOB;
    private final String expirationDate;
    private final String topAddress;
    private final String bottomAddress;
    private final boolean male;
    
    public PlayerDox(final EntityLivingBase entity) {
        this.player = entity;
        this.state = DoxUtil.getRandomState();
        this.liscenseNumber = DoxUtil.randomAlphaNumeric(9).toUpperCase();
        this.DOB = DoxUtil.randomDOB();
        this.expirationDate = DoxUtil.randomExpirationDate(this.DOB);
        this.topAddress = DoxUtil.getTopAddress();
        this.bottomAddress = DoxUtil.getBottomAddress(this.state);
        this.male = (Math.random() > 0.2);
    }
    
    public EntityLivingBase getPlayer() {
        return this.player;
    }
    
    public String getState() {
        return this.state;
    }
    
    public String getLiscenseNumber() {
        return this.liscenseNumber;
    }
    
    public String getDOB() {
        return this.DOB;
    }
    
    public String getExpirationDate() {
        return this.expirationDate;
    }
    
    public String getTopAddress() {
        return this.topAddress;
    }
    
    public String getBottomAddress() {
        return this.bottomAddress;
    }
    
    public boolean isMale() {
        return this.male;
    }
}
