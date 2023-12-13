package slavikcodd3r.rainbow.utils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Session;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayer getPlayer() {
        return getMinecraft().thePlayer;
    }
    
    public static WorldClient getWorld() {
        getMinecraft();
        return Minecraft.theWorld;
    }
    
    public static Block getBlock(final BlockPos pos, final double offset) {
        return Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(pos.add(0.0, offset, 0.0)).getBlock();
    }
    
    public static Session getSession() {
        return getMinecraft().getSession();
    }
    
    public static void giveItem(final ItemStack stack, final int slot) {
        ClientUtils.packet(new C10PacketCreativeInventoryAction(36 + slot, stack));
    }
    
    public static String withColors(final String input) {
        String output = input;
        int index = output.indexOf("&&");
        while (output.indexOf("&&") != -1) {
            output = output.replace("&&", "§");
            index = output.indexOf("&&");
        }
        return output;
    }
}
