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
    public final File configsDir;
    public final FileConfig hudConfig;
    public final File backgroundFile;
    public final FileConfig clickGuiConfig;
    public final FileConfig valuesConfig;
    public final File dir = new File(mc.getDataDir(), "AtField-1.12");
    public final File capeDir;
    public final AccountsConfig accountsConfig;
    public final FileConfig xrayConfig;
    public final File hudsDir;
    public final FriendsConfig friendsConfig;
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    public boolean firstStart = false;

    public void saveConfig(FileConfig fileConfig) {
        this.saveConfig(fileConfig, false);
    }

    public void loadBackground() {
        if (this.backgroundFile.exists()) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new FileInputStream(this.backgroundFile));
                if (bufferedImage == null) {
                    return;
                }
                LiquidBounce.INSTANCE.setBackground(classProvider.createResourceLocation("atfield".toLowerCase() + "/background.png"));
                mc.getTextureManager().loadTexture(LiquidBounce.INSTANCE.getBackground(), classProvider.createDynamicTexture(bufferedImage));
                ClientUtils.getLogger().info("[FileManager] Loaded background.");
            }
            catch (Exception exception) {
                ClientUtils.getLogger().error("[FileManager] Failed to load background.", (Throwable)exception);
            }
        }
    }

    public void saveConfigs(FileConfig ... fileConfigArray) {
        for (FileConfig fileConfig : fileConfigArray) {
            this.saveConfig(fileConfig);
        }
    }

    public void loadConfigs(FileConfig ... fileConfigArray) {
        for (FileConfig fileConfig : fileConfigArray) {
            this.loadConfig(fileConfig);
        }
    }

    public FileManager() {
        this.configsDir = new File(this.dir, "configs");
        this.capeDir = new File(this.dir, "capes");
        this.hudsDir = new File(this.dir, "huds");
        this.valuesConfig = new ValuesConfig(new File(this.dir, "values.json"));
        this.clickGuiConfig = new ClickGuiConfig(new File(this.dir, "clickgui.json"));
        this.accountsConfig = new AccountsConfig(new File(this.dir, "accounts.json"));
        this.friendsConfig = new FriendsConfig(new File(this.dir, "friends.json"));
        this.xrayConfig = new XRayConfig(new File(this.dir, "xray-blocks.json"));
        this.hudConfig = new HudConfig(new File(this.dir, "hud.json"));
        this.backgroundFile = new File(this.dir, "userbackground.png");
        this.setupFolder();
        this.loadBackground();
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
            catch (IllegalAccessException illegalAccessException) {
                ClientUtils.getLogger().error("[FileManager] Failed to save config file of field " + field.getName() + ".", (Throwable)illegalAccessException);
            }
        }
    }

    private void saveConfig(FileConfig fileConfig, boolean bl) {
        if (!bl && LiquidBounce.INSTANCE.isStarting()) {
            return;
        }
        try {
            if (!fileConfig.hasConfig()) {
                fileConfig.createConfig();
            }
            fileConfig.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved config: " + fileConfig.getFile().getName() + ".");
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[FileManager] Failed to save config file: " + fileConfig.getFile().getName() + ".", throwable);
        }
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

    public void loadConfig(FileConfig fileConfig) {
        if (!fileConfig.hasConfig()) {
            ClientUtils.getLogger().info("[FileManager] Skipped loading config: " + fileConfig.getFile().getName() + ".");
            this.saveConfig(fileConfig, true);
            return;
        }
        try {
            fileConfig.loadConfig();
            ClientUtils.getLogger().info("[FileManager] Loaded config: " + fileConfig.getFile().getName() + ".");
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[FileManager] Failed to load config file: " + fileConfig.getFile().getName() + ".", throwable);
        }
    }
}

