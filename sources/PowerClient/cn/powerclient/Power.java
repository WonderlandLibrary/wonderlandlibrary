/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn;

import java.util.logging.Logger;
import me.AveReborn.Tiplan;
import me.AveReborn.command.CommandManager;
import me.AveReborn.mod.ModManager;
import me.AveReborn.ui.click.UIClick;
import me.AveReborn.util.FileUtil;
import me.AveReborn.util.fontRenderer.FontManager;

public class Client {
    public static String CLIENT_NAME = "Ave";
    public static String CLIENT_NAME2 = "Null";
    public static String CLIENT_VER = "0.2";
    public static boolean isClientLoading;
    public static Client instance;
    public static Logger LOGGER;
    public ModManager modMgr;
    public FileUtil fileMgr;
    public FontManager fontMgr;
    public static FontManager fontManager;
    public UIClick clickface;
    public CommandManager cmdMgr;

    static {
        LOGGER = Logger.getLogger(CLIENT_NAME);
    }

    public Client() {
        instance = this;
        isClientLoading = true;
    }

    public void onClientStart() {
        this.modMgr = new ModManager();
        fontManager = this.fontMgr = new FontManager();
        this.fileMgr = new FileUtil();
        this.clickface = new UIClick();
        this.cmdMgr = new CommandManager();
        new me.AveReborn.Tiplan();
        isClientLoading = false;
    }

    public void onClientStop() {
    }

    public UIClick showClickGui() {
        return this.clickface;

	}

	public static Object getInstance() {
		// TODO 自动生成的方法存根
		return null;
	}
}

