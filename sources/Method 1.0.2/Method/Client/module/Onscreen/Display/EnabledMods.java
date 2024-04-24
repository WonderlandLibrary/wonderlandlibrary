package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public final class EnabledMods extends Module {
    public EnabledMods() {
        super("EnabledMods", Keyboard.KEY_NONE, Category.ONSCREEN, "EnabledMods");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting background;
    static Setting xpos;
    static Setting ypos;
    static Setting ToptoBottom;
    static Setting LefttoRight;
    static Setting Frame;
    static Setting Shadow;
    static Setting FontSize;
    static Setting Wave;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 0, .75, .85, .85));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Wave = new Setting("Wave", this, true));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(ToptoBottom = new Setting("ToptoBottom", this, true));
        setmgr.add(LefttoRight = new Setting("LefttoRight", this, false));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 0, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 0, -20, (mc.displayHeight) + 40, true));
        pin = new EnabledModsRUN();
        OnscreenGUI.pinableFrames.add(pin);
        this.setToggled(true);
    }

    @Override
    public void onEnable() {
        PinableFrame.Toggle(pin, true);
    }

    @Override
    public void onDisable() {
        PinableFrame.Toggle(pin, false);
    }

    public static class EnabledModsRUN extends PinableFrame {
        public EnabledModsRUN() {
            super("EnabledModsSet", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());
        }

        @Override
        public void setup() {
            getSetup(this, xpos, ypos, Frame, FontSize);
            setPinned(true);
        }

        @Override
        public void Ongui() {
            getInit(this, xpos, ypos, Frame, FontSize);
        }

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            int yCount = this.y + 3;

            for (Module module : ModuleManager.getSortedMods(ToptoBottom.getValBoolean(), true, true)) {
                int Lr = LefttoRight.getValBoolean() ? widthcal(Frame, module.getDisplayName()) - 70 : -3;

                int posx = (int) ((this.x - Lr) * RenderUtils.simpleScale(false));
                int posy = (int) (yCount * RenderUtils.simpleScale(true));

                if (background.getValBoolean())
                    Gui.drawRect(posx, posy + 3, posx + widthcal(Frame, module.getDisplayName()) + 2, posy + heightcal(Frame, module.getDisplayName()) + 1, BgColor.getcolor());

                
                fontSelect(Frame, module.getDisplayName(), posx, posy, Wave.getValBoolean() ? ColorUtils.wave((yCount - this.y - this.barHeight - 3) / 8, TextColor.getSat(), TextColor.getBri()).getRGB() : TextColor.getcolor(), Shadow.getValBoolean());


                yCount += 9;
            }
        }

    }
}