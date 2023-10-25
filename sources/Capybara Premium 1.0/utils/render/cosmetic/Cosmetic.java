package fun.rich.client.utils.render.cosmetic;

import fun.rich.client.utils.render.cosmetic.impl.DragonWing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class Cosmetic {
    public static void renderAccessory(final String[] accessorys, final EntityPlayer player, final float partialTicks) {
        for (String accessory : accessorys) {
            switch (accessory) {
                case "Dragon_wing":
                    DragonWing.render(player, partialTicks);
                   break;
            }
        }
    }

    public static ResourceLocation getCape(String cape) {
        return new ResourceLocation("rich/" + cape.toLowerCase() + ".png");
    }
    public static ResourceLocation getWing(String wing) {
        return new ResourceLocation("rich/" + wing.toLowerCase() + ".png");
    }
}
