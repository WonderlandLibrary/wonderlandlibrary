package Method.Client.command;


import Method.Client.utils.visual.ChatUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;

@Command.CommandFeatures(description = "See inv of other players", Syntax = "Invsee <Player>")

public class Invsee extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (!mc.player.capabilities.isCreativeMode) {
            ChatUtils.error("Must Be Creative");
            return;
        }
        String id = args[0];
        for (final EntityPlayer entityPlayer : mc.world.playerEntities) {
            if (entityPlayer.getDisplayNameString().equalsIgnoreCase(id)) {
                mc.displayGuiScreen(new GuiInventory(entityPlayer));
                return;
            }
        }
        ChatUtils.error("Could not find player " + id);
    }

}