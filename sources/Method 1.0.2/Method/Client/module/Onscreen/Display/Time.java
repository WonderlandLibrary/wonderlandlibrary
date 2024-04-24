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

import java.text.SimpleDateFormat;
import java.util.Date;

import static Method.Client.Main.setmgr;


public final class Time extends Module {
    public Time() {
        super("Time", Keyboard.KEY_NONE, Category.ONSCREEN, "Time");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting Worldtime;
    static Setting GameTime;
    static Setting xpos;
    static Setting ypos;
    static Setting Frame;
    static Setting Shadow;
    static Setting background;
    static Setting FontSize;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Worldtime = new Setting("Worldtime", this, true));
        setmgr.add(GameTime = new Setting("GameTime", this, false));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth / 2) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 190, -20, (mc.displayHeight / 2) + 40, true));
        pin = new TimeRUN();
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

    public static class TimeRUN extends PinableFrame {
        public TimeRUN() {
            super("TimeSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

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
            String time = "";

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            if (Worldtime.getValBoolean())
                time = new SimpleDateFormat("h:mm a").format(new Date()) + "\n  ";
            if (GameTime.getValBoolean())
                time += mc.world.getWorldTime();

            fontSelect(Frame, time, posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());

            if (background.getValBoolean())
                Gui.drawRect(posx, posy + 10, posx + widthcal(Frame, time), posy + 20, BgColor.getcolor());
        }

    }

}

