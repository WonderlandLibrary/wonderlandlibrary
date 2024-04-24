package Method.Client.command;

import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

@Command.CommandFeatures(description = "Spawns item in creative", Syntax = "Give <Id> <Amnt> <MetaData> <NBT>")

public class Give extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        if (!mc.player.isCreative())
            ChatUtils.error("You must be in creative mode.");

        int amount = 1;
        int metadata = 0;
        StringBuilder nbt = null;
        Item item = Item.getByNameOrId(args[0]);
        if (item == null) {
            ChatUtils.error("There's no such item with name \2477" + args[0] + "\247c.");
            return;
        }

        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NullPointerException | NumberFormatException e) {
                ChatUtils.error("\2477" + args[1] + "\247c is not a valid number.");
                return;
            }
            // metadata
            if (args.length > 2) {
                try {
                    metadata = Integer.parseInt(args[2]);
                } catch (NullPointerException | NumberFormatException e) {
                    ChatUtils.error("\2477" + args[2] + "\247c is not a valid number.");
                    return;
                }
                // nbt
                if (args.length > 3) {
                    nbt = new StringBuilder(args[3]);
                    for (int i = 4; i < args.length; i++)
                        nbt.append(" ").append(args[i]);
                    nbt = new StringBuilder(nbt.toString().replace('&', '\247').replace("\247\247", "&"));
                }
            }
        }
        // generate item stack
        ItemStack stack = new ItemStack(item, amount, metadata);
        // apply nbt data
        if (nbt != null) {
            try {
                stack.setTagCompound(JsonToNBT.getTagFromJson(nbt.toString()));
            } catch (NBTException e) {
                ChatUtils.error("Data tag parsing failed: " + e.getMessage());
                return;
            }
        }
        // give item stack
        Utils.updateFirstEmptySlot(stack);


    }


}