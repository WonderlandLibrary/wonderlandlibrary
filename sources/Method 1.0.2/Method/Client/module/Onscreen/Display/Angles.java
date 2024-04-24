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


public final class Angles extends Module {
    public Angles() {
        super("Angles", Keyboard.KEY_NONE, Category.ONSCREEN, "Angles");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting background;
    static Setting xpos;
    static Setting ypos;
    static Setting Frame;
    static Setting Decimal;
    static Setting Shadow;
    static Setting FontSize;
    static Setting TrueYAW;
    static Setting Names;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TrueYAW = new Setting("TrueYAW", this, false));
        setmgr.add(Names = new Setting("Names", this, true));
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BgColor", this, .22, .88, .22, .22));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 220, -20, (mc.displayHeight) + 40, true));
        setmgr.add(Decimal = new Setting("Decimal", this, 2, 0, 5, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        pin = new AnglesRUN();
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

    public static class AnglesRUN extends PinableFrame {
        public AnglesRUN() {
            super("AnglesSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());
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
            int posx = (int) (this.x * RenderUtils.simpleScale(false));
            int posy = (int) (this.y * RenderUtils.simpleScale(true));
            fontSelect(Frame, getCoords(), posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());
            
            if (background.getValBoolean())
                Gui.drawRect(posx, posy, posx + widthcal(Frame, getCoords()), posy + 22, BgColor.getcolor());

        }

        private String getCoords() {
            decimalFormat = getDecimalFormat((int) Decimal.getValDouble());

            String Pitch = decimalFormat.format(mc.player.rotationPitch);
            String Yaw;
            if (!TrueYAW.getValBoolean())
                Yaw = decimalFormat.format(mc.player.rotationYaw % 360);
            else
                Yaw = decimalFormat.format(mc.player.rotationYaw);
            if (Names.getValBoolean())
                return "Pitch " + Pitch + ", Yaw " + Yaw;
            return Pitch + ", " + Yaw;
        }
    }

}

