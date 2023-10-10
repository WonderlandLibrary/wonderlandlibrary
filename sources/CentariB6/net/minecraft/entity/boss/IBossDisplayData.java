package net.minecraft.entity.boss;

import net.minecraft.util.IChatComponent;

public interface IBossDisplayData {
   IChatComponent getDisplayName();

   float getHealth();

   float getMaxHealth();
}
