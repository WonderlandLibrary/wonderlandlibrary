
package info.sigmaclient.sigma.utils;

import com.google.common.collect.Lists;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.item.AutoArmor;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.item.*;

import java.util.ArrayList;

public class InvManager extends Module {
    public ModeValue type = new ModeValue("Type", "OpenInv", new String[]{"OpenInv", "SpoofInv"});
    private final NumberValue delay = new NumberValue("Delay", 0.1, 1, 0, NumberValue.NUMBER_TYPE.FLOAT);
    public ModeValue itemType = new ModeValue("ItemType", "Skywars", new String[]{"Skywars", "Hungry"});
    ArrayList<String> whiteListSkywars = new ArrayList<>(Lists.newArrayList(
            "block",
            "sword",
            "helmet", "chestplate", "legging", "boot", // armors
            "_pickaxe", "_axe", "_shovel", // tools
            "golden_apple", "enchanted_golden_apple"
    ));
    public boolean isWhiteListItem(String name){
        if(name.contains("sword") || name.endsWith(".bow") || name.contains("potion")) return true;
        for(String str : whiteListSkywars){
            if(name.contains(str)) return true;
        }
        return false;
    }
    public InvManager() {
        super("InvManager", Category.Item, "Auto manage your inventory.");
    }
    int delays = 0;
    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        int delay = (int) (this.delay.getValue().floatValue() * 10);
        boolean only = type.is("OpenInv");
        if(only && !(mc.currentScreen instanceof InventoryScreen)) return;
        if(delays > 0) {
            delays --;
            return;
        }
        ArrayList<ItemStack> usefulItems = new ArrayList<>();

        int bestSword = -1;
        double nextScore = 0;
        for (int i = 9; i < 45; i++) {
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if(itemStack.getItem() instanceof SwordItem){
                SwordItem swordItem = (SwordItem) itemStack.getItem();
                double d = 0;
                if(d == 0) d = 1;
                double score = 0;
                score += swordItem.getAttackDamage();
                score *= 100;
                score += d;
                if(d <= 0.1){
                    score = 0;
                }
                if(score > nextScore){
                    nextScore = score;
                    bestSword = i;
                }
            }
        }
        int swordSlot = 0;
        if(bestSword != -1){
            usefulItems.add(mc.player.inventory.getStackInSlot(bestSword));
        }
        if(bestSword != -1 && bestSword != swordSlot + 36){
            delays = delay;
            if(!mc.player.inventory.getStackInSlot(swordSlot + 36).isEmpty())
                AutoArmor.drop(swordSlot + 36);
            AutoArmor.swap(swordSlot, bestSword);
            return;
        }


        int bestPickaxe = -1;
        nextScore = 0;
        for (int i = 9; i < 45; i++) {
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if(itemStack.getItem() instanceof PickaxeItem){
                PickaxeItem pickaxeItem = (PickaxeItem) itemStack.getItem();
                double d = 0;
                if(d == 0) d = 1;
                double score = 0;
                score += pickaxeItem.getDestroySpeed(itemStack, Blocks.STONE.getDefaultState());
                score *= 10;
                score += d;
                if(d <= 0.1){
                    score = 0;
                }
                if(score > nextScore){
                    nextScore = score;
                    bestPickaxe = i;
                }
            }
        }
        int pickaxeSlot = 1;
        if(bestPickaxe != -1){
            usefulItems.add(mc.player.inventory.getStackInSlot(bestPickaxe));
        }
        if(bestPickaxe != -1 && bestPickaxe != pickaxeSlot + 36){
            delays = delay;
            if(!mc.player.inventory.getStackInSlot(pickaxeSlot + 36).isEmpty())
                AutoArmor.drop(pickaxeSlot + 36);
            AutoArmor.swap(pickaxeSlot, bestPickaxe);
            return;
        }

        int bestAxe = -1;
        nextScore = 0;
        for (int i = 9; i < 45; i++) {
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if(itemStack.getItem() instanceof AxeItem){
                AxeItem axeItem = (AxeItem) itemStack.getItem();
                double d = 0;
                if(d == 0) d = 1;
                double score = 0;
                score += axeItem.getDestroySpeed(itemStack, Blocks.ACACIA_WOOD.getDefaultState());
                score *= 10;
                score += d;
                if(d <= 0.1){
                    score = 0;
                }
                if(score > nextScore){
                    nextScore = score;
                    bestAxe = i;
                }
            }
        }
        int axeSlot = 2;
        if(bestAxe != -1){
            usefulItems.add(mc.player.inventory.getStackInSlot(bestAxe));
        }
        if(bestAxe != -1 && bestAxe != axeSlot + 36){
            if(!mc.player.inventory.getStackInSlot(axeSlot + 36).isEmpty())
                AutoArmor.drop(axeSlot + 36);
            delays = delay;
            AutoArmor.swap(axeSlot, bestAxe);
            return;
        }

        int gappleSlot = 3;
        int gappleFindSlot = -1;
        final ItemStack gapple = mc.player.inventory.mainInventory.get(gappleSlot);
        if(!(gapple.getTranslationKey().contains("golden_apple"))) {
            for (int i = 9; i < 45; i++) {
                final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem().getTranslationKey().contains("golden_apple")) {
                    gappleFindSlot = i;
                    break;
                }
            }
        }
        if(gappleFindSlot != -1){
            delays = delay;
            AutoArmor.swap(gappleSlot, gappleFindSlot);
            return;
        }

        ItemStack nextDrop = null;
        int dropIndex = 0;
        for (int i = 9; i < 45; i++) {
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            boolean tool = itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof PickaxeItem || itemStack.getItem() instanceof AxeItem || itemStack.getItem() instanceof ArmorItem;
            if(!isWhiteListItem(itemStack.getTranslationKey()) && !usefulItems.contains(itemStack)
//                    || (tool && !usefulItems.contains(itemStack))
            ){
                nextDrop = itemStack;
                dropIndex = i;
                break;
            }
        }
        if(nextDrop != null) {
            delays = delay;
            AutoArmor.drop(dropIndex);
            return;
        }
        super.onWindowUpdateEvent(event);
    }

}
