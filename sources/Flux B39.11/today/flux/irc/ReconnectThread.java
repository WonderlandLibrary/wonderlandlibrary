package today.flux.irc;

import com.soterdev.SoterObfuscator;
import net.minecraft.client.Minecraft;
import today.flux.gui.loginui.PopupDialog;
import today.flux.gui.loginui.GuiLogin;
import today.flux.irc.client.network.ClientNetwork;

public class ReconnectThread extends Thread {
    ClientNetwork clientNetwork;
    public static int times = 0;

    public ReconnectThread(ClientNetwork clientNetwork) {
        this.clientNetwork = clientNetwork;
        times = 0;
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    @Override
    public void run() {
        while (true) {
            try {
                sleep(30000);
                if (IRCClient.hasOffline) {
                    if (++times >= 5) {
                        IRCClient.reconnectFailed = true;
                        Minecraft.getMinecraft().displayGuiScreen(new GuiLogin());
                        ((GuiLogin) Minecraft.getMinecraft().currentScreen).dialog = new PopupDialog("Failed to reconnect", "Please try again later.", 200, 55, true);
                        return;
                    } else {
                        clientNetwork.reconnect();
                    }
                } else {
                    break;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
