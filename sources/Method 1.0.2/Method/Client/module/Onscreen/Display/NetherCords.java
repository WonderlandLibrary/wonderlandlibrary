package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;

import static Method.Client.Main.setmgr;


public final class NetherCords extends Module {
    public NetherCords() {
        super("NetherCords", Keyboard.KEY_NONE, Category.ONSCREEN, "NetherCords");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting background;
    static Setting xpos;
    static Setting ypos;
    static Setting Shadow;
    static Setting Decimal;
    static Setting Frame;
    static Setting FontSize;
    static PinableFrame pin;
    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 1, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth / 2) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 120, -20, (mc.displayHeight / 2) + 40, true));
        setmgr.add(Decimal = new Setting("Decimal", this, 2, 0, 5, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        pin = new NetherCordsRUN();
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

    public static class NetherCordsRUN extends PinableFrame {

        public NetherCordsRUN() {
            super("NetherCordsSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

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
            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            fontSelect(Frame, getCoords(), posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());

            if (background.getValBoolean())
                Gui.drawRect(posx, posy + 10, posx + widthcal(Frame, getCoords()), posy + 20, BgColor.getcolor());
        }


        private String getCoords() {
            decimalFormat = getDecimalFormat((int) Decimal.getValDouble());

            String x = decimalFormat.format(mc.player.posX / 8);
            String y = decimalFormat.format(mc.player.posY);
            String z = decimalFormat.format(mc.player.posZ / 8);
            String coords;
            coords = x + ", " + y + ", " + z + ChatFormatting.GRAY;
            return coords;
        }
    }

}

