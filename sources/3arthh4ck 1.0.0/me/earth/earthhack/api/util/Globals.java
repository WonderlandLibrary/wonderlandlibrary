package me.earth.earthhack.api.util;

import net.minecraft.client.Minecraft;

/**
 * Convenience interface so we don't have to
 * {@link Minecraft#getMinecraft()} everywhere.
 */
public interface Globals
{
    /** Minecraft instance */
    Minecraft mc = Minecraft.getMinecraft();

}
