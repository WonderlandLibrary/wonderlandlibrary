package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static Method.Client.Main.setmgr;


public final class ServerResponce extends Module {
    public ServerResponce() {
        super("ServerResponce", Keyboard.KEY_NONE, Category.ONSCREEN, "ServerResponce");
    }

    static Setting Delay;
    static Setting TextColor;
    static Setting BgColor;
    static Setting xpos;
    static Setting ypos;
    static Setting Shadow;
    static Setting Frame;
    static Setting FontSize;
    static Setting Background;

    private static long serverLastUpdated = 0;
    static PinableFrame pin;
    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(Delay = new Setting("Delay", this, 1, 1, 10, true));
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Background = new Setting("Background", this, false));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth/2) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 180, -20, (mc.displayHeight/2)  + 40, true));
        pin = new ServerResponceRUN();
        OnscreenGUI.pinableFrames.add(pin);
    }

    @Override
    public void onEnable() {
        PinableFrame.Toggle(pin, true);
    }

    @Override
    public void onDisable() {
        PinableFrame.Toggle(pin, false);
    }
    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (side == Connection.Side.IN) {
            serverLastUpdated = System.currentTimeMillis();
        }
        return true;
    }


    public static class ServerResponceRUN extends PinableFrame {
        public ServerResponceRUN() {
            super("ServerResponceSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

        }

        @Override
        public void setup() {
            getSetup(this,xpos,ypos,Frame,FontSize);
        }

        @Override
        public void Ongui() {
            getInit(this, xpos, ypos, Frame,FontSize);
        }

        String serverMessage = "Server Not Responding! ";

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            if (mc.isSingleplayer()) return;
            if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) return;
            if (!(Delay.getValDouble() * 1000L <= System.currentTimeMillis() - serverLastUpdated)) return;
            if (shouldPing()) {
                if (isDown("1.1.1.1", 80, 1111)) {
                    serverMessage = "Your internet is offline! ";
                } else {
                    serverMessage = "Server Not Responding! ";
                }
            }
            serverMessage = serverMessage.replaceAll("! .*", "! " + timeDifference() + "s");

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            if (Background.getValBoolean())
                Gui.drawRect(posx, posy + 10, posx + widthcal(Frame, serverMessage), posy + 20, BgColor.getcolor());
            fontSelect(Frame, serverMessage, posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());
        }


        private double timeDifference() {
            return (System.currentTimeMillis() - serverLastUpdated) / 1000d;
        }

        public static boolean isDown(String host, int port, int timeout) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), timeout);
                return false;
            } catch (IOException e) {
                return true; // Either timeout or unreachable or failed DNS lookup.
            }
        }

        private static long startTime = 0;

        private boolean shouldPing() {
            if (startTime == 0) startTime = System.currentTimeMillis();
            if (startTime + 1000 <= System.currentTimeMillis()) { // 1 second
                startTime = System.currentTimeMillis();
                return true;
            }
            return false;
        }

    }

}

