// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.utils.RandomUtil;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class Protector extends Module
{
    public BooleanValue protectTime;
    private int randomX;
    private int randomZ;
    private int randomMinute;
    private int randomHour;
    
    public Protector() {
        super("Protector", Color.yellow, Categorys.RENDER);
        this.protectTime = new BooleanValue(1, "Time", this, true);
    }
    
    @Override
    public void onEnable() {
        this.randomX = RandomUtil.nextInt(-1000, 1000);
        this.randomZ = RandomUtil.nextInt(-1000, 1000);
        this.randomMinute = RandomUtil.nextInt(10, 60);
        this.randomHour = RandomUtil.nextInt(0, 11);
    }
    
    public int getRandomX() {
        return this.randomX;
    }
    
    public void setRandomX(final int randomX) {
        this.randomX = randomX;
    }
    
    public int getRandomZ() {
        return this.randomZ;
    }
    
    public void setRandomZ(final int randomZ) {
        this.randomZ = randomZ;
    }
    
    public int getRandomMinute() {
        return this.randomMinute;
    }
    
    public void setRandomMinute(final int randomMinute) {
        this.randomMinute = randomMinute;
    }
    
    public int getRandomHour() {
        return this.randomHour;
    }
    
    public void setRandomHour(final int randomHour) {
        this.randomHour = randomHour;
    }
}
