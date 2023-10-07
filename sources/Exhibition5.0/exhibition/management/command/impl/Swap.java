// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import net.minecraft.entity.player.EntityPlayer;
import exhibition.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import exhibition.management.command.Command;

public class Swap extends Command
{
    protected final Minecraft mc;
    
    public Swap(final String[] names, final String description) {
        super(names, description);
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("m") && args.length > 1) {
                if (Integer.valueOf(args[1]) > 0 && Integer.valueOf(args[1]) < 10) {
                    for (int swapSlotsTo = Integer.valueOf(args[1]) - 1, i = 0; i <= swapSlotsTo; ++i) {
                        this.swap(i + 27, i);
                    }
                    return;
                }
            }
            else {
                if (args[0].toString().contains("m") && args.length == 1) {
                    ChatUtil.printChat("§4[§cE§4]§8 That is not a valid hotbar number. (Slot 1 to set arg)");
                    return;
                }
                if (!args[0].toString().contains("m") && Integer.valueOf(args[0]) > 0 && Integer.valueOf(args[0]) < 10) {
                    final int swapSlot = Integer.valueOf(args[0]) - 1;
                    this.swap(swapSlot + 27, swapSlot);
                    return;
                }
            }
        }
        this.printUsage();
    }
    
    @Override
    public String getUsage() {
        return "swap <m [Multi] | hotbarslot> [Multi Only] <hotbars>";
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, this.mc.thePlayer);
    }
}
