package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

import static Method.Client.Main.setmgr;


public final class Ping extends Module {
    public Ping() {
        super("Ping", Keyboard.KEY_NONE, Category.ONSCREEN, "Ping");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting background;
    static Setting xpos;
    static Setting Shadow;
    static Setting Frame;
    static Setting ypos;
    static Setting FontSize;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth / 2) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 130, -20, (mc.displayHeight / 2) + 40, true));
        pin = new PingRUN();
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

    public static class PingRUN extends PinableFrame {

        public PingRUN() {
            super("PingSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

        }

        @Override
        public void setup() {
            getSetup(this, xpos, ypos, Frame, FontSize);
        }

        @Override
        public void Ongui() {
            getInit(this, xpos, ypos, Frame, FontSize);
        }

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            if (mc.world == null || mc.player == null) {
                return;
            }

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            final NetworkPlayerInfo playerInfo = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID());

            final String ping = "MS: " + playerInfo.getResponseTime();
            fontSelect(Frame, ping, posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());

            if (background.getValBoolean())
                Gui.drawRect(posx, posy + 10, posx + widthcal(Frame, ping), posy + 20, BgColor.getcolor());
        }

    }

}

