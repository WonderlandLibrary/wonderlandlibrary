package wtf.monsoon.api.util.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class PlayerUtils {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isOnSameTeam(EntityLivingBase entity) {
        if (entity.getTeam() != null && mc.thePlayer.getTeam() != null) {
            char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            char c2 = mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }

}
