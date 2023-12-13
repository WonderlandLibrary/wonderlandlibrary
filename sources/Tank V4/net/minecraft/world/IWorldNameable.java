package net.minecraft.world;

import net.minecraft.util.IChatComponent;

public interface IWorldNameable {
   IChatComponent getDisplayName();

   String getName();

   boolean hasCustomName();
}
