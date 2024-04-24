package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;

import static Method.Client.Main.setmgr;


public final class PlayerSpeed extends Module {
    public PlayerSpeed() {
        super("PlayerSpeed", Keyboard.KEY_NONE, Category.ONSCREEN, "PlayerSpeed");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting background;
    static Setting Shadow;
    static Setting Frame;
    static Setting xpos;
    static Setting ypos;
    static Setting Decimal;
    static Setting FontSize;

    static Setting MotionX;
    static Setting MotionY;
    static Setting MotionZ;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(MotionX = new Setting("MotionX", this, false));
        setmgr.add(MotionY = new Setting("MotionY", this, false));
        setmgr.add(MotionZ = new Setting("MotionZ", this, false));
        setmgr.add(Decimal = new Setting("Decimal", this, 2, 0, 5, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 150, -20, (mc.displayHeight) + 40, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        pin = new SpeedRUN();
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

    public static class SpeedRUN extends PinableFrame {

        public SpeedRUN() {
            super("SpeedSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

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
            decimalFormat = getDecimalFormat((int) Decimal.getValDouble());

            final double deltaX = mc.player.posX - mc.player.prevPosX;
            final double deltaZ = mc.player.posZ - mc.player.prevPosZ;
            final float tickRate = (mc.timer.tickLength / 1000.0f);

            String bps = "BPS: " + decimalFormat.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate));

            if (MotionX.getValBoolean())
                bps += " MotionX: " + decimalFormat.format(mc.player.motionX);
            if (MotionY.getValBoolean())
                bps += " MotionY: " + decimalFormat.format(mc.player.motionY);
            if (MotionZ.getValBoolean())
                bps += " MotionZ: " + decimalFormat.format(mc.player.motionZ);

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            fontSelect(Frame, bps, posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());

            if (background.getValBoolean())
                Gui.drawRect(posx, posy + 10, this.x + widthcal(Frame, bps), posy + 20, BgColor.getcolor());
        }

    }

}

