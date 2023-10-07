/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
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
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.configs.AccountsConfig;
import net.ccbluex.liquidbounce.file.configs.ClickGuiConfig;
import net.ccbluex.liquidbounce.file.configs.EnemysConfig;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.file.configs.HudConfig;
import net.ccbluex.liquidbounce.file.configs.ModulesConfig;
import net.ccbluex.liquidbounce.file.configs.ProfilesConfig;
import net.ccbluex.liquidbounce.file.configs.ShortcutsConfig;
import net.ccbluex.liquidbounce.file.configs.ValuesConfig;
import net.ccbluex.liquidbounce.file.configs.XRayConfig;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class FileManager
extends MinecraftInstance {
    public final File dir;
    public final File fontsDir;
    public final File settingsDir;
    public final File soundsDir;
    public final File capesDir;
    public final List<ProfilesConfig> profilesConfigs;
    public final FileConfig modulesConfig;
    public final FileConfig valuesConfig;
    public final FileConfig clickGuiConfig;
    public final AccountsConfig accountsConfig;
    public final EnemysConfig enemysConfig;
    public final FriendsConfig friendsConfig;
    public final FileConfig xrayConfig;
    public final FileConfig hudConfig;
    public final FileConfig shortcutsConfig;
    public final File backgroundFile;
    public boolean firstStart;
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    public FileManager() {
        this.dir = new File(FileManager.mc.field_71412_D, "KyinoClient-1.8");
        this.fontsDir = new File(this.dir, "fonts");
        this.settingsDir = new File(this.dir, "settings");
        this.soundsDir = new File(this.dir, "assets/minecraft/sounds");
        this.capesDir = new File(this.dir, "capes");
        this.profilesConfigs = new ArrayList<ProfilesConfig>();
        this.modulesConfig = new ModulesConfig(new File(this.dir, "modules.json"));
        this.valuesConfig = new ValuesConfig(new File(this.dir, "values.json"));
        this.clickGuiConfig = new ClickGuiConfig(new File(this.dir, "clickgui.json"));
        this.accountsConfig = new AccountsConfig(new File(this.dir, "accounts.json"));
        this.enemysConfig = new EnemysConfig(new File(this.dir, "enemys.json"));
        this.friendsConfig = new FriendsConfig(new File(this.dir, "friends.json"));
        this.xrayConfig = new XRayConfig(new File(this.dir, "xray-blocks.json"));
        this.hudConfig = new HudConfig(new File(this.dir, "hud.json"));
        this.shortcutsConfig = new ShortcutsConfig(new File(this.dir, "shortcuts.json"));
        this.backgroundFile = new File(this.dir, "userbackground.png");
        this.firstStart = false;
        this.setupFolder();
        this.loadBackground();
    }

    public void setupFolder() {
        if (!this.dir.exists()) {
            this.dir.mkdir();
            this.firstStart = true;
        }
        if (!this.fontsDir.exists()) {
            this.fontsDir.mkdir();
        }
        if (!this.settingsDir.exists()) {
            this.settingsDir.mkdir();
        }
        if (!this.soundsDir.exists()) {
            this.soundsDir.mkdir();
        }
        if (!this.capesDir.exists()) {
            this.capesDir.mkdir();
        }
    }

    public void loadAllConfigs() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getType() != FileConfig.class) continue;
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                FileConfig fileConfig = (FileConfig)field.get(this);
                this.loadConfig(fileConfig);
            }
            catch (IllegalAccessException e) {
                ClientUtils.getLogger().error("Failed to load config file of field " + field.getName() + ".", (Throwable)e);
            }
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
        catch (Throwable t) {
            ClientUtils.getLogger().error("[FileManager] Failed to load config file: " + config.getFile().getName() + ".", t);
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
        catch (Throwable t) {
            ClientUtils.getLogger().error("[FileManager] Failed to save config file: " + config.getFile().getName() + ".", t);
        }
    }

    public void loadBackground() {
        if (this.backgroundFile.exists()) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new FileInputStream(this.backgroundFile));
                if (bufferedImage == null) {
                    return;
                }
                LiquidBounce.INSTANCE.setBackground(new ResourceLocation("LiquidBounce".toLowerCase() + "/background.png"));
                mc.func_110434_K().func_110579_a(LiquidBounce.INSTANCE.getBackground(), (ITextureObject)new DynamicTexture(bufferedImage));
                ClientUtils.getLogger().info("[FileManager] Loaded background.");
            }
            catch (Exception e) {
                ClientUtils.getLogger().error("[FileManager] Failed to load background.", (Throwable)e);
            }
        }
    }
}

