package ru.smertnix.celestial.utils.render.cosmetic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ru.smertnix.celestial.utils.render.cosmetic.impl.DragonWing;

public class Cosmetic {
    public static void renderAccessory(final String[] accessorys, final EntityPlayer player, final float partialTicks) {
    }

    public static ResourceLocation getCape(String cape) {
        return new ResourceLocation("rich/" + cape.toLowerCase() + ".png");
    }
    public static ResourceLocation getWing(String wing) {
        return new ResourceLocation("rich/" + wing.toLowerCase() + ".png");
    }
}
