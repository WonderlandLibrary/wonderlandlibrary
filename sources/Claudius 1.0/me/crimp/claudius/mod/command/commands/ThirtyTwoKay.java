package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.mod.command.Command;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import static me.crimp.claudius.utils.EnchantmentUtil.*;
import static net.minecraft.init.Items.DIAMOND_SWORD;


public class ThirtyTwoKay extends Command {
    public ThirtyTwoKay() {
        super("32k", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        //mc.player.inventoryContainer.putStackInSlot(1,stack);
        Command.sendMessage("32k In Ur Hand");

        ItemStack stack = new ItemStack(DIAMOND_SWORD);
        stack.setCount(64);
        stack.setStackDisplayName("FuckyWucky");
        // stack.addEnchantment(Enchantment.getEnchantmentByID(16),65534);
        addEnchantment(stack, 16, Short.MAX_VALUE); // Sharpness   32767
        addEnchantment(stack, 19, 10);              // Knockback   10
        addEnchantment(stack, 20, Short.MAX_VALUE); // Fire Aspect 32767
        addEnchantment(stack, 21, 10);              // Looting     10
        addEnchantment(stack, 22, 3);               // Sweeping    3
        addEnchantment(stack, 34, Short.MAX_VALUE); // Unbreaking  32767
        addEnchantment(stack, 70, 1);               // Mending
        addEnchantment(stack, 71, 1);               // Curse of Vanishing
        mc.player.setHeldItem(EnumHand.MAIN_HAND,stack);
       // mc.player.openContainer.putStackInSlot(27,stack);

       // EntityUtil.attackEntity(mc.pointedEntity, Boolean.valueOf(false), true);
    }
}