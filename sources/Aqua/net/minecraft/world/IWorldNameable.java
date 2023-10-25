package net.minecraft.world;

import net.minecraft.util.IChatComponent;

public interface IWorldNameable {
    public String getName();

    public boolean hasCustomName();

    public IChatComponent getDisplayName();
}
