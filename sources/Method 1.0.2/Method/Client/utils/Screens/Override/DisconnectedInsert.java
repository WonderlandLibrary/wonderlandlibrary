package Method.Client.utils.Screens.Override;

import Method.Client.utils.Screens.Screen;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class DisconnectedInsert extends Screen {

    public static ServerData lastserver;
    public static boolean Autolog = false;
    public static boolean doPing = false;
    public static boolean Connect = false;
    public static int ping = 0;


    @Subscribe
    public void onWorldUnload(WorldEvent.Unload event) {
        ServerData data = mc.getCurrentServerData();
        if (data != null)
            lastserver = data;
    }

    @Subscribe
    public void onWorldLoad(WorldEvent.Load event) {
        ServerData data = mc.getCurrentServerData();
        if (data != null)
            lastserver = data;
    }

    @Subscribe
    public void GuiScreenEventPost(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiDisconnected) {

            if (event.getButton().id == 997) {
                doPing = true;
            }
            if (event.getButton().id == 999) {
                ServerAddress serveraddress = ServerAddress.fromString(lastserver.serverIP);
                mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, lastserver.serverIP, serveraddress.getPort()));
            }
            if (event.getButton().id == 998) {
                Autolog = !Autolog;
                double Starttime = System.currentTimeMillis() + 5000;
                new Thread(() -> {
                    try {
                        while (Starttime >= System.currentTimeMillis() && Autolog) {
                            Connect = false;
                            event.getButton().displayString = "Auto " + TextFormatting.GOLD + (Starttime - System.currentTimeMillis()) + TextFormatting.RESET + "ms Relog";
                        }
                        Connect = true;
                    } catch (Exception ignored) {
                    }
                }).start();
            }
        }
    }


    @Override
    public void DrawScreenEvent(GuiScreenEvent.DrawScreenEvent event) {
        if (event.getGui() instanceof GuiDisconnected) {
            if (Autolog && Connect) {
                Connect = false;
                ServerAddress serveraddress = ServerAddress.fromString(lastserver.serverIP);
                mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, lastserver.serverIP, serveraddress.getPort()));
            }
            try {
                ServerAddress serveraddress = ServerAddress.fromString(lastserver.serverIP);
                event.getGui().drawCenteredString(event.getGui().fontRenderer, lastserver.serverIP + " Port: " + serveraddress.getPort(), event.getGui().width / 2, event.getGui().height / 2 - 50, 11184810);
                event.getGui().drawCenteredString(event.getGui().fontRenderer, "Ping: " + ping, event.getGui().width / 2, event.getGui().height / 2 - 65, 11184810);
                if (doPing) {
                    doPing = false;

                    String hostAddress = lastserver.serverIP;
                    int port = serveraddress.getPort();
                    long timeToRespond = 0;

                    InetAddress inetAddress = InetAddress.getByName(hostAddress);
                    InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);

                    SocketChannel sc = SocketChannel.open();
                    sc.configureBlocking(true);

                    Date start = new Date();
                    if (sc.connect(socketAddress)) {
                        Date stop = new Date();
                        timeToRespond = (stop.getTime() - start.getTime());
                    }

                    ping = (int) timeToRespond;
                }
            } catch (Exception ignored) {
            }


        }
    }


    @Subscribe
    public void GuiScreenEventInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiConnecting) {
            lastserver = mc.getCurrentServerData();
        }
        if (event.getGui() instanceof GuiDisconnected) {

            event.getButtonList().add(new GuiButton(999, event.getGui().width / 2 - 100, Math.min(event.getGui().height / 2 + 20 / 2 + event.getGui().fontRenderer.FONT_HEIGHT, event.getGui().height - 30) + 20, 200, 20, "Relog"));
            event.getButtonList().add(new GuiButton(998, event.getGui().width / 2 - 100, Math.min(event.getGui().height / 2 + 20 / 2 + event.getGui().fontRenderer.FONT_HEIGHT, event.getGui().height - 30) + 40, 200, 20, "Auto 5s Relog"));
            event.getButtonList().add(new GuiButton(997, event.getGui().width / 2 - 100, Math.min(event.getGui().height / 2 + 20 / 2 + event.getGui().fontRenderer.FONT_HEIGHT, event.getGui().height - 30) + 60, 200, 20, "Ping"));

            if (Autolog) {
                Autolog = false;
                GuiButton button = null;
                try {
                    for (GuiButton guiButton : event.getButtonList()) {
                        if (guiButton.id == 998) {
                            button = guiButton;
                            ((GuiDisconnected) event.getGui()).actionPerformed(guiButton);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(event.getGui(), button, event.getGui().buttonList));
            }
        }
    }


}
