package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.HeldItemsEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class FakeGhost extends Module {
    public FakeGhost() {
        super("Fake Ghost", Category.Misc, "Shows a fake totem on the death screen");
    }

    @EventListener
    public void event(HeldItemsEvent event) {
        if (!getMc().player.isAlive()) {
            event.setItem(new ItemStack(Items.TOTEM_OF_UNDYING));
            event.setCancelled();
        }
    }
}
