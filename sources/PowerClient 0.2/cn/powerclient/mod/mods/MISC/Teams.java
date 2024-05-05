/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MISC;

import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.ModManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;

public class Teams
extends Mod {
    public Teams() {
        super("Teams", Category.MISC);
    }

    public static boolean isOnSameTeam(Entity entity) {
        if (ModManager.getModByName("Teams").isEnabled()) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.getDisplayName().getUnformattedText().startsWith("\u00a7")) {
                Minecraft.getMinecraft();
                if (Minecraft.thePlayer.getDisplayName().getUnformattedText().length() <= 2 || entity.getDisplayName().getUnformattedText().length() <= 2) {
                    return false;
                }
                Minecraft.getMinecraft();
                if (Minecraft.thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
                    return true;
                }
            }
        }
        return false;
    }
}

