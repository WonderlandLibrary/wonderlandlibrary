package today.flux.module.implement.Command;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import today.flux.module.Command;
import today.flux.utility.ChatUtils;

/**
 * Created by John on 2016/10/21.
 */
@Command.Info(name = "enchant", syntax = { "<list/EnchantName> [level]" }, help = "Add enchantments to items you have in hand.")
public class EnchantCmd extends Command {
    @Override
    public void execute(String[] args) throws Error {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            showEnchants();
        } else if (args.length == 2) {
            if (Enchantment.getEnchantmentByLocation(args[0]) == null) {
                showEnchants();
                return;
            }

            if (!isNumber(args[1]) || Integer.parseInt(args[1]) > 32767) {
                this.syntaxError();
                return;
            }

            String enchant = args[0];
            int level = Integer.parseInt(args[1]);

            ItemStack item = this.mc.thePlayer.getHeldItem();
            item.addEnchantment(Enchantment.getEnchantmentByLocation(enchant), level);

            this.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36 + this.mc.thePlayer.inventory.currentItem, item));

        } else {
            this.syntaxError();
        }
    }

    private void showEnchants(){
        String enchants = "";

        for (Enchantment enchant: Enchantment.enchantmentsBookList) {
            enchants += "§6" + enchant.getName() + "§f, ";
        }

        ChatUtils.sendMessageToPlayer(enchants);
    }


    private boolean isNumber(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException nfex) {
            return false;
        }
    }
}
