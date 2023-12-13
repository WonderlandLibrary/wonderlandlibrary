package me.nyan.flush.module.impl.combat.targethud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public abstract class TargetHUD {
    protected final Minecraft mc;
    private final String name;
    protected float healthAnimated;
    protected float healthAnimated1;

    public TargetHUD(String name) {
        this.name = name;
        mc = Minecraft.getMinecraft();
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public String getName() {
        return name;
    }

    public abstract void draw(EntityLivingBase target, float x, float y, int color);

    public void resetHealthAnimated() {
        healthAnimated = 0;
        healthAnimated1 = 0;
    }
}
