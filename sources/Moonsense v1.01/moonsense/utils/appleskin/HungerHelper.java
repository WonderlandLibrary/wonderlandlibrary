// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils.appleskin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import java.lang.reflect.Field;

public class HungerHelper
{
    protected static Field foodExhaustion;
    
    static {
        try {
            HungerHelper.foodExhaustion = FoodStats.class.getDeclaredField("foodExhaustionLevel");
        }
        catch (NoSuchFieldException ex) {}
        catch (SecurityException ex2) {}
    }
    
    public static float getMaxExhaustion(final EntityPlayer player) {
        return 4.0f;
    }
    
    public static float getExhaustion(final EntityPlayer player) {
        try {
            return HungerHelper.foodExhaustion.getFloat(player.getFoodStats());
        }
        catch (IllegalAccessException illegalaccessexception) {
            throw new RuntimeException(illegalaccessexception);
        }
    }
    
    public static void setExhaustion(final EntityPlayer player, final float exhaustion) {
        try {
            HungerHelper.foodExhaustion.setFloat(player.getFoodStats(), exhaustion);
        }
        catch (IllegalAccessException illegalaccessexception) {
            throw new RuntimeException(illegalaccessexception);
        }
    }
}
