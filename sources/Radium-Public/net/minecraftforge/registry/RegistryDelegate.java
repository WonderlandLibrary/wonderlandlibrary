package net.minecraftforge.registry;

import net.minecraft.util.ResourceLocation;

public interface RegistryDelegate<T>
{
    T get();

    ResourceLocation name();

    Class<T> type();
}
