package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public final class Potions extends Module {
    public Potions() {
        super("Potions", Keyboard.KEY_NONE, Category.ONSCREEN, "Potions");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting Shadow;
    static Setting name;
    static Setting amplifer;
    static Setting duration;
    static Setting Frame;
    static Setting background;
    static Setting xpos;
    static Setting ypos;
    static Setting FontSize;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(name = new Setting("name", this, true));
        setmgr.add(amplifer = new Setting("amplifer", this, false));
        setmgr.add(duration = new Setting("duration", this, false));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 160, -20, (mc.displayHeight) + 40, true));
        pin = new PotionsRUN();
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

    public static class PotionsRUN extends PinableFrame {


        public PotionsRUN() {
            super("PotionsSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

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
            int offset = 0;

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            for (PotionEffect activePotionEffect : mc.player.getActivePotionEffects()) {
                String effect = name.getValBoolean() ? activePotionEffect.getEffectName().substring(7) + " " : "";
                String amp = amplifer.getValBoolean() ? "x" + activePotionEffect.getAmplifier() + " " : "";
                String dur = duration.getValBoolean() ? activePotionEffect.getDuration() / 20 + " " : "";
                String all = effect + amp + dur;

                fontSelect(Frame, all, posx, posy + 10 - offset, TextColor.getcolor(), Shadow.getValBoolean());

                if (background.getValBoolean())
                    Gui.drawRect(posx, posy + 10 - offset, posx + widthcal(Frame, effect + amp + dur), posy + 20 - offset, BgColor.getcolor());

                offset += 10;
            }
        }

    }

}

