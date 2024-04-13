package net.smoothboot.client.module.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.mixin.MinecraftClientAccessor;
import net.smoothboot.client.module.Mod;

public class FastXP extends Mod {

    public FastXP() {
        super("Fast XP", "", Category.Misc);
    }

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private boolean isXP(ItemStack stack)
    {
        return stack.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    @Override
    public void onTick() {
        if (isXP(mc.player.getMainHandStack())) {
            ((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(0);
        }
        if (isXP(mc.player.getOffHandStack())) {
            ((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(0);
        }
        super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


}
