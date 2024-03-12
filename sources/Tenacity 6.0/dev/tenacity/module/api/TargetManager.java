// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.api;

import dev.tenacity.module.settings.impl.BooleanSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.Module;

public class TargetManager extends Module
{
    public static MultipleBoolSetting targetType;
    public static EntityLivingBase target;
    
    public TargetManager() {
        super("Target", "Target", Category.COMBAT, "");
        this.addSettings(TargetManager.targetType);
    }
    
    public static boolean checkEntity(final Entity entity) {
        return (!(entity instanceof EntityPlayer) || TargetManager.targetType.isEnabled("Players")) && (!entity.getClass().getPackage().getName().contains("monster") || TargetManager.targetType.isEnabled("Mobs")) && (!entity.getClass().getPackage().getName().contains("passive") || TargetManager.targetType.isEnabled("Animals")) && (!entity.isInvisible() || TargetManager.targetType.isEnabled("Invisibles"));
    }
    
    static {
        TargetManager.targetType = new MultipleBoolSetting("Target Type", new BooleanSetting[] { new BooleanSetting("Players", true), new BooleanSetting("Mobs", false), new BooleanSetting("Animals", false), new BooleanSetting("Invisibles", true) });
    }
}
