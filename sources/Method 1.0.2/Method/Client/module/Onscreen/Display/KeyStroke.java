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

import java.awt.*;
import java.util.ArrayList;

import static Method.Client.Main.setmgr;


public final class KeyStroke extends Module {
    public KeyStroke() {
        super("KeyStroke", Keyboard.KEY_NONE, Category.ONSCREEN, "KeyStroke");
    }

    static Setting TextColor;
    static Setting Presscolor;
    static Setting BgColor;
    static Setting xpos;
    static Setting ypos;
    static Setting Frame;
    static Setting Background;
    static Setting Shadow;
    static Setting Clicks;
    static Setting ClicksPerSec;
    static Setting FontSize;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, .3, .4, .4, 1));
        setmgr.add(Presscolor = new Setting("Presscolor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, .5, .30, .22));
        setmgr.add(Background = new Setting("Background", this, false));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Clicks = new Setting("Clicks", this, false));
        setmgr.add(ClicksPerSec = new Setting("ClicksPerSec", this, false));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth / 2) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 220, -20, (mc.displayHeight / 2) + 250, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        pin = new KeyStrokeRUN();
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

    public static class KeyStrokeRUN extends PinableFrame {

        public KeyStrokeRUN() {
            super("KeyStrokeSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

        }
        @Override
        public void setup() {
            getSetup(this, xpos, ypos, Frame, FontSize);
        }

        @Override
        public void Ongui() {
            getInit(this, xpos, ypos, Frame, FontSize);
        }

        ArrayList<Double> clicks = new ArrayList<>();
        boolean startclick = false;


        @Subscribe
        public void onRenderGameOverlay(Text event) {
            if (mc.gameSettings.keyBindAttack.pressed) {
                startclick = true;
            } else if (startclick) {
                startclick = false;
                clicks.add((double) System.currentTimeMillis());
            }

            ArrayList<Double> rem = new ArrayList<>();
            for (Double click : clicks) {
                if (click + 1000 < System.currentTimeMillis()) {
                    rem.add(click);
                }
            }
            clicks.removeAll(rem);

            int black = (new Color(0, 0, 0, 40).getRGB());
            int rain = TextColor.getcolor();
            int white = Presscolor.getcolor();

            int posx = (int) (this.x * RenderUtils.simpleScale(false));
            int posy = (int) (this.y * RenderUtils.simpleScale(true));

            fontSelect(Frame, mc.gameSettings.keyBindForward.getDisplayName(), posx + 18, posy, mc.gameSettings.keyBindForward.pressed ? white : rain, Shadow.getValBoolean());
            fontSelect(Frame, mc.gameSettings.keyBindLeft.getDisplayName(), posx, posy + 20, mc.gameSettings.keyBindLeft.pressed ? white : rain, Shadow.getValBoolean());
            fontSelect(Frame, mc.gameSettings.keyBindBack.getDisplayName(), posx + 20, posy + 20, mc.gameSettings.keyBindBack.pressed ? white : rain, Shadow.getValBoolean());
            fontSelect(Frame, mc.gameSettings.keyBindRight.getDisplayName(), posx + 40, posy + 20, mc.gameSettings.keyBindRight.pressed ? white : rain, Shadow.getValBoolean());

            if (Clicks.getValBoolean()) {
                if (Background.getValBoolean()) {
                    Gui.drawRect(posx, posy + 40, posx + 20, posy + 20, mc.gameSettings.keyBindAttack.pressed ? rain : black);
                    Gui.drawRect(posx + 20, posy + 40, posx + 40, posy + 20, mc.gameSettings.keyBindUseItem.pressed ? rain : black);
                }

                fontSelect(Frame, "LMB", posx, posy + 40, mc.gameSettings.keyBindAttack.pressed ? white : rain, Shadow.getValBoolean());
                fontSelect(Frame, "RMB", posx + 30, posy + 40, mc.gameSettings.keyBindUseItem.pressed ? white : rain, Shadow.getValBoolean());
            }

            if (ClicksPerSec.getValBoolean()) {
                fontSelect(Frame, "Clicks: " + clicks.size(), posx, posy + 60, mc.gameSettings.keyBindAttack.pressed ? white : rain, Shadow.getValBoolean());
            }

            if (Background.getValBoolean()) {
                Gui.drawRect(posx + 15, posy, posx + 25, posy + 20, mc.gameSettings.keyBindForward.pressed ? rain : black);
                Gui.drawRect(posx, posy + 20, posx + 10, posy + 40, mc.gameSettings.keyBindLeft.pressed ? rain : black);
                Gui.drawRect(posx + 20, posy + 20, posx + 30, posy + 40, mc.gameSettings.keyBindBack.pressed ? rain : black);
                Gui.drawRect(posx + 40, posy + 20, posx + 50, posy + 40, mc.gameSettings.keyBindRight.pressed ? rain : black);
            }

        }
    }

}

