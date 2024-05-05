package me.dev.legacy.features.modules.misc;

import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class Dupe extends Module {
    public Dupe() {
        super("Dupe", "Ez dupe.", Category.MISC, true, false, false);
    }

    private final Random random = new Random();

    public void onEnable() {
        EntityPlayerSP player = mc.player;
        WorldClient world = mc.world;
        ;

        if (player == null || mc.world == null) return;

        ItemStack itemStack = player.getHeldItemMainhand();

        if (itemStack.isEmpty()) {
            Command.sendMessage("You need to hold an item in hand to dupe!");
            disable();
            return;
        }

        int count = random.nextInt(31) + 1;

        for (int i = 0; i <= count; i++) {
            EntityItem entityItem = player.dropItem(itemStack.copy(), false, true);
            if (entityItem != null) world.addEntityToWorld(entityItem.entityId, entityItem);
        }

        int total = count * itemStack.getCount();
        player.sendChatMessage("I just used the Legacy Client Dupe and got " + total + " " + itemStack.getDisplayName() + " thanks to Legacy dev's!");
        disable();
    }
}
