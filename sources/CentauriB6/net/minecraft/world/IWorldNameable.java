package net.minecraft.world;

import net.minecraft.util.IChatComponent;

public interface IWorldNameable {
   String getName();

   IChatComponent getDisplayName();

   boolean hasCustomName();
}
