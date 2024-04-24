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
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public final class PlayerCount extends Module {
    public PlayerCount() {
        super("PlayerCount", Keyboard.KEY_NONE, Category.ONSCREEN, "PlayerCount");
    }

    static Setting TextColor;
    static Setting BgColor;
    static Setting Friends;
    static Setting background;
    static Setting Shadow;
    static Setting xpos;
    static Setting ypos;
    static Setting Frame;
    static Setting FontSize;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(Friends = new Setting("Friends", this, false));
        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 140, -20, (mc.displayHeight) + 40, true));
        pin = new PlayerCountRUN();
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

    public static class PlayerCountRUN extends PinableFrame {

        public PlayerCountRUN() {
            super("PlayerCountSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

        }

        @Override
        public void setup() {
            getSetup(this, xpos, ypos, Frame, FontSize);
        }

        @Override
        public void Ongui() {
            getInit(this, xpos, ypos, Frame, FontSize);
        }

        int lasting = 0;

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            String playerCount = "ONLINE: " + mc.player.connection.getPlayerInfoMap().size();

            if (Friends.getValBoolean()) {
                if (mc.player.ticksExisted % 20 == 0) {
                    int onlineFriend = 0;
                    for (EntityPlayer s : mc.world.playerEntities) {
                        if (FriendManager.friendsList.contains(s.getName()))
                            onlineFriend++;
                    }
                    lasting = onlineFriend;
                }
                playerCount += " Friends: " + lasting;
            }

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            fontSelect(Frame, playerCount, posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());

            if (background.getValBoolean())
                Gui.drawRect(posx, posy + 10, posx + widthcal(Frame, playerCount), posy + 20, BgColor.getcolor());
        }

    }

}

