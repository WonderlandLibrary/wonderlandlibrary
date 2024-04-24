package Method.Client.module.Onscreen.Display;

import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Objects;

import static Method.Client.Main.setmgr;


public final class Enemypos extends Module {
    public Enemypos() {
        super("Enemypos", Keyboard.KEY_NONE, Category.ONSCREEN, "Enemypos");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting Friends;
    static Setting xpos;
    static Setting ypos;
    static Setting Frame;
    static Setting ColorDistance;
    static Setting Background;
    static Setting LefttoRight;
    static Setting Shadow;
    static Setting FontSize;
    static Setting Ping;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(LefttoRight = new Setting("LefttoRight", this, true));
        setmgr.add(Friends = new Setting("Friends", this, true));
        setmgr.add(Ping = new Setting("Ping", this, true));
        setmgr.add(ColorDistance = new Setting("ColorDistance", this, true));
        setmgr.add(Background = new Setting("Background", this, false));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth / 2) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 60, -20, (mc.displayHeight / 2) + 40, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        pin = new EnemyposRUN();
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

    public static class EnemyposRUN extends PinableFrame {

        public EnemyposRUN() {
            super("EnemyposSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

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
            int yCount = this.y + this.barHeight + 3;

            for (EntityPlayer player : mc.world.playerEntities) {
                if ((FriendManager.isFriend(player.getName()) && Friends.getValBoolean()) || player.getName().equals(mc.player.getName()))
                    continue;

                int Lr = LefttoRight.getValBoolean() ? widthcal(Frame, player.getName() + Pos(player)) - 70 : -3;

                int posx = (int) ((this.x + 4) * RenderUtils.simpleScale(false));
                int posy = (int) (yCount * RenderUtils.simpleScale(true));

                if (Background.getValBoolean())
                    Gui.drawRect(posx, posy, posx + widthcal(Frame, player.getName() + Pos(player)) + 3, posy + heightcal(Frame, player.getName() + player.getPosition()) - 1, Background.getcolor());


                fontSelect(Frame, player.getName() + Pos(player), posx - Lr, posy, ColorDistance.getValBoolean() ? distancecolor(player) : TextColor.getcolor(), Shadow.getValBoolean());


                yCount += 8;
            }
        }


        private int distancecolor(EntityPlayer player) {
            int g = 0, r = 0;

            if (mc.player.getDistance(player) > 50 && mc.player.getDistance(player) < 100) {
                g = (int) ((mc.player.getDistance(player) - 50) * 5.1);
            }
            if (mc.player.getDistance(player) < 50) {
                r = (int) ((mc.player.getDistance(player)) * 5.1);
            }

            mc.player.getDistance(player);
            return new Color(r, g, 0).getRGB();
        }

        public String Pos(EntityPlayer player) {
            String tag = "";
            if (player != null) {
                tag += " X:" + player.getPosition().getX() + ", Y:" + player.getPosition().getY() + ", Z:" + player.getPosition().getZ();
                if (Ping.getValBoolean())
                    try {
                        tag += " " + (int) MathHelper.clamp((float) Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1.0f, 300.0f) + " P ";
                    } catch (NullPointerException ignored) {
                        return tag;
                    }
                return tag;
            }
            return "";
        }
    }

}

