// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import java.util.Iterator;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.player.ChatUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.module.Category;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import net.minecraft.item.Item;
import dev.tenacity.module.Module;

public class MurderDetector extends Module
{
    public static int[] itemIds;
    public static Item[] itemTypes;
    public static ArrayList<EntityPlayer> killerData;
    
    public MurderDetector() {
        super("MurderDetector", "Murder Detector", Category.MISC, "Detect the murders");
    }
    
    @Override
    public void onTickEvent(final TickEvent event) {
        for (final Entity entity : MurderDetector.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                if (!(entityLivingBase instanceof EntityPlayer) || entity == MurderDetector.mc.thePlayer) {
                    continue;
                }
                final EntityPlayer player = (EntityPlayer)entityLivingBase;
                if (player.inventory.getCurrentItem() == null) {
                    continue;
                }
                if (!MurderDetector.killerData.contains(player)) {
                    if (!this.isWeapon(player.inventory.getCurrentItem().getItem())) {
                        continue;
                    }
                    ChatUtil.print(true, "[MurderDetector] " + player.getName() + " is Killer!");
                    NotificationManager.post(NotificationType.WARNING, "MurderDetector", player.getName() + " is Killer!");
                    MurderDetector.killerData.add(player);
                }
                else {
                    if (this.isWeapon(player.inventory.getCurrentItem().getItem())) {
                        continue;
                    }
                    MurderDetector.killerData.remove(player);
                }
            }
        }
    }
    
    public boolean isWeapon(final Item item) {
        for (final int id : MurderDetector.itemIds) {
            final Item itemId = Item.getItemById(id);
            if (item == itemId) {
                return true;
            }
        }
        for (final Item id2 : MurderDetector.itemTypes) {
            if (item == id2) {
                return true;
            }
        }
        return false;
    }
    
    static {
        MurderDetector.itemIds = new int[] { 288, 396, 412, 398, 75, 50 };
        MurderDetector.itemTypes = new Item[] { Items.fishing_rod, Items.diamond_hoe, Items.golden_hoe, Items.iron_hoe, Items.stone_hoe, Items.wooden_hoe, Items.stone_sword, Items.diamond_sword, Items.golden_sword, Item.getItemFromBlock(Blocks.sponge), Items.iron_sword, Items.wooden_sword, Items.diamond_axe, Items.golden_axe, Items.iron_axe, Items.stone_axe, Items.diamond_pickaxe, Items.wooden_axe, Items.golden_pickaxe, Items.iron_pickaxe, Items.stone_pickaxe, Items.wooden_pickaxe, Items.stone_shovel, Items.diamond_shovel, Items.golden_shovel, Items.iron_shovel, Items.wooden_shovel };
        MurderDetector.killerData = new ArrayList<EntityPlayer>();
    }
}
