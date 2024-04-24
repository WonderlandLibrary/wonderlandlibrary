package Method.Client.module.render;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.Override.Mixintab;
import Method.Client.utils.system.Connection;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public class ExtraTab extends Module {

    public ExtraTab() {
        super("ExtraTab", Keyboard.KEY_NONE, Category.RENDER, "ExtraTab");
    }

    public static Setting Players;
    public static Setting ReplaceBar;
    public static Setting FriendColor;
    public static Setting FriendObfus;
    public static Setting NoHeaderFooter;
    private static GuiPlayerTabOverlay defaultscreen;

    @Override
    public void setup() {
        setmgr.add(Players = new Setting("Players", this, 100, 1, 500, true));
        setmgr.add(ReplaceBar = new Setting("ReplaceBar", this, true));
        setmgr.add(NoHeaderFooter = new Setting("NoHeaderFooter", this, true));
        setmgr.add(FriendColor = new Setting("Friend Color", this, 0, 1, 1, 1));
        setmgr.add(FriendObfus = new Setting("FriendObfus", this, true));
    }

    @Override
    public void onEnable() {
        defaultscreen = mc.ingameGUI.overlayPlayerList;
        Mixintab.Run();
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (side == Connection.Side.IN) {
            if (NoHeaderFooter.getValBoolean())
                return !(packet instanceof SPacketPlayerListHeaderFooter);
        }
        return true;
    }

    @Override
    public void onDisable() {
        if (defaultscreen != null)
            mc.ingameGUI.overlayPlayerList = defaultscreen;
    }

}

