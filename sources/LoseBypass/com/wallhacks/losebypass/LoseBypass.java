/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.wallhacks.losebypass;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.wallhacks.losebypass.event.eventbus.EventBus;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.manager.AltManager;
import com.wallhacks.losebypass.manager.ConfigManager;
import com.wallhacks.losebypass.manager.FontManager;
import com.wallhacks.losebypass.manager.KeyManager;
import com.wallhacks.losebypass.manager.SocialManager;
import com.wallhacks.losebypass.manager.SystemManager;
import com.wallhacks.losebypass.utils.MC;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoseBypass
implements MC {
    public static final String MODID = "LBPS";
    public static final String NAME = "LoseBypass";
    public static final String VERSION = "b0.2";
    public static final Logger logger = LogManager.getLogger((String)"LBPS");
    public static File ParentPath;
    public static EventBus eventBus;
    public static AltManager altManager;
    public static SystemManager systemManager;
    public static ConfigManager configManager;
    public static KeyManager keyManager;
    public static FontManager fontManager;
    public static ClickGui clickGuiScreen;
    public static SocialManager socialManager;
    public static LoseBypass instance;

    public void init() {
        ParentPath = new File(Minecraft.getMinecraft().mcDataDir, NAME);
        logger.info("Loading LoseBypass...");
        eventBus = new EventBus();
        configManager = new ConfigManager();
        keyManager = new KeyManager();
        fontManager = new FontManager();
        systemManager = new SystemManager();
        socialManager = new SocialManager();
        configManager.Load();
        altManager = new AltManager();
        clickGuiScreen = new ClickGui();
        logger.info("LoseBypass loaded successfully");
    }

    public static void save() {
        configManager.Save();
        socialManager.SaveFriends();
        altManager.saveAlts();
    }

    public static void sendInfo(String msg) {
        LoseBypass.mc.thePlayer.addChatMessage(new ChatComponentText(ChatFormatting.BLUE + "[LoseBypass] " + ChatFormatting.RESET + msg));
    }
}

