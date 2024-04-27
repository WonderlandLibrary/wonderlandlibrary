/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.configs.AccountsConfig;
import net.ccbluex.liquidbounce.file.configs.ClickGuiConfig;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.file.configs.HudConfig;
import net.ccbluex.liquidbounce.file.configs.ValuesConfig;
import net.ccbluex.liquidbounce.file.configs.XRayConfig;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class FileManager
extends MinecraftInstance {
    public final File dir = new File(mc.getDataDir(), "LiKingSense-1.12");
    public final File configsDir = new File(this.dir, "configs");
    public final File capeDir = new File(this.dir, "capes");
    public final File hudsDir = new File(this.dir, "huds");
    public final FileConfig valuesConfig = new ValuesConfig(new File(this.dir, "values.json"));
    public final FileConfig clickGuiConfig = new ClickGuiConfig(new File(this.dir, "clickgui.json"));
    public final AccountsConfig accountsConfig = new AccountsConfig(new File(this.dir, "accounts.json"));
    public final FriendsConfig friendsConfig = new FriendsConfig(new File(this.dir, "friends.json"));
    public final FileConfig xrayConfig = new XRayConfig(new File(this.dir, "xray-blocks.json"));
    public final FileConfig hudConfig = new HudConfig(new File(this.dir, "hud.json"));
    public final File backgroundFile = new File(this.dir, "userbackground.png");
    public boolean firstStart = false;
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    public FileManager() {
        this.setupFolder();
        this.loadBackground();
    }

    public void setupFolder() {
        if (!this.dir.exists()) {
            this.dir.mkdir();
            this.firstStart = true;
        }
        if (!this.configsDir.exists()) {
            this.configsDir.mkdir();
        }
        if (!this.hudsDir.exists()) {
            this.hudsDir.mkdir();
        }
        if (!this.capeDir.exists()) {
            this.capeDir.mkdir();
        }
    }

    public void loadConfigs(FileConfig ... configs) {
        for (FileConfig fileConfig : configs) {
            this.loadConfig(fileConfig);
        }
    }

    public void loadConfig(FileConfig config) {
        if (!config.hasConfig()) {
            ClientUtils.getLogger().info("[FileManager] Skipped loading config: " + config.getFile().getName() + ".");
            this.saveConfig(config, true);
            return;
        }
        try {
            config.loadConfig();
            ClientUtils.getLogger().info("[FileManager] Loaded config: " + config.getFile().getName() + ".");
        }
        catch (Throwable t2) {
            ClientUtils.getLogger().error("[FileManager] Failed to load config file: " + config.getFile().getName() + ".", t2);
        }
    }

    public void loadBackground() {
        if (this.backgroundFile.exists()) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new FileInputStream(this.backgroundFile));
                if (bufferedImage == null) {
                    return;
                }
                LiquidBounce.INSTANCE.setBackground(classProvider.createResourceLocation("LiKingSense".toLowerCase() + "/background.png"));
                mc.getTextureManager().loadTexture(LiquidBounce.INSTANCE.getBackground(), classProvider.createDynamicTexture(bufferedImage));
                ClientUtils.getLogger().info("[FileManager] Loaded background.");
            }
            catch (Exception e) {
                ClientUtils.getLogger().error("[FileManager] Failed to load background.", (Throwable)e);
            }
        }
    }

    public void saveAllConfigs() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getType() != FileConfig.class) continue;
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                FileConfig fileConfig = (FileConfig)field.get(this);
                this.saveConfig(fileConfig);
            }
            catch (IllegalAccessException e) {
                ClientUtils.getLogger().error("[FileManager] Failed to save config file of field " + field.getName() + ".", (Throwable)e);
            }
        }
    }

    public void saveConfigs(FileConfig ... configs) {
        for (FileConfig fileConfig : configs) {
            this.saveConfig(fileConfig);
        }
    }

    public void saveConfig(FileConfig config) {
        this.saveConfig(config, false);
    }

    private void saveConfig(FileConfig config, boolean ignoreStarting) {
        if (!ignoreStarting && LiquidBounce.INSTANCE.isStarting()) {
            return;
        }
        try {
            if (!config.hasConfig()) {
                config.createConfig();
            }
            config.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved config: " + config.getFile().getName() + ".");
        }
        catch (Throwable t2) {
            ClientUtils.getLogger().error("[FileManager] Failed to save config file: " + config.getFile().getName() + ".", t2);
        }
    }
}

