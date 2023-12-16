// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils.appleskin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class FoodHelper
{
    public static boolean isFood(final ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemFood;
    }
    
    public static BasicFoodValues getDefaultFoodValues(final ItemStack itemStack) {
        final ItemFood itemFood = (ItemFood)itemStack.getItem();
        final int hunger = itemFood.getHealAmount(itemStack);
        final float saturationModifier = itemFood.getSaturationModifier(itemStack);
        return new BasicFoodValues(hunger, saturationModifier);
    }
    
    public static BasicFoodValues getModifiedFoodValues(final ItemStack itemStack, final EntityPlayer player) {
        return getDefaultFoodValues(itemStack);
    }
    
    public static class BasicFoodValues
    {
        public final int hunger;
        public final float saturationModifier;
        
        public BasicFoodValues(final int hunger, final float saturationModifier) {
            this.hunger = hunger;
            this.saturationModifier = saturationModifier;
        }
        
        public float getSaturationIncrement() {
            return this.hunger * this.saturationModifier * 2.0f;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BasicFoodValues)) {
                return false;
            }
            final BasicFoodValues that = (BasicFoodValues)o;
            return this.hunger == that.hunger && Float.compare(that.saturationModifier, this.saturationModifier) == 0;
        }
        
        @Override
        public int hashCode() {
            int result = this.hunger;
            result = 31 * result + ((this.saturationModifier != 0.0f) ? Float.floatToIntBits(this.saturationModifier) : 0);
            return result;
        }
    }
}
