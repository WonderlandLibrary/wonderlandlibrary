package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;

import static Method.Client.Main.setmgr;


public final class Hunger extends Module {
    public Hunger() {
        super("Hunger", Keyboard.KEY_NONE, Category.ONSCREEN, "Hunger");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting xpos;
    static Setting ypos;
    static Setting Frame;
    static Setting Background;
    static Setting Shadow;
    static Setting FontSize;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Background = new Setting("Background", this, false));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 100, -20, (mc.displayHeight) + 40, true));
        pin = new HungerRUN();
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

    public static class HungerRUN extends PinableFrame {

        public HungerRUN() {
            super("HungerSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());
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
            final DecimalFormat dc = new DecimalFormat("#.##");

            String cow = "Hunger: " + mc.player.getFoodStats().getFoodLevel() + " Saturation: "
                    + dc.format(mc.player.getFoodStats().getSaturationLevel());

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            fontSelect(Frame, cow, posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());

            if (Background.getValBoolean())
                Gui.drawRect(posx, posy + 10, posx + widthcal(Frame, cow), posy + 20, BgColor.getcolor());
        }


    }

}

