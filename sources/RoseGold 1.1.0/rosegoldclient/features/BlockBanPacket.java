package rosegoldclient.features;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;

public class BlockBanPacket {

    private static String ip;

    @SubscribeEvent
    public void onJoinServerMessage(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntityPlayer){
            if(Main.mc.getCurrentServerData() == null) return;
            ip = Main.mc.getCurrentServerData().serverIP;
        }
    }

    public static void reconnect() {
        System.out.println("reconnecting!");
        if(ip != null) {
            FMLClientHandler.instance().connectToServer(new GuiMainMenu(), new ServerData("server", ip, false));
            Main.banned = true;
        }
    }
}
