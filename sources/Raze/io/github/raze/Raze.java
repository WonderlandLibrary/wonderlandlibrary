package io.github.raze;

import io.github.raze.configuration.impl.LoadConfig;
import io.github.raze.configuration.impl.SaveConfig;
import io.github.raze.utilities.collection.configs.NameUtil;
import io.github.raze.utilities.collection.configs.PrefixUtil;
import io.github.raze.registry.collection.ManagerRegistry;
import io.github.raze.registry.collection.ScreenRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import io.github.raze.version.ViaMCP;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public enum Raze {

    INSTANCE("Raze", "2.0", PrefixUtil.getPrefixFromFile(), NameUtil.getCustomNameFromFile());

    public String name, version, prefix, customName;

    Raze(String name, String version, String prefix, String customName) {
        this.name = name;
        this.version = version;
        this.prefix = prefix;
        this.customName = customName;
    }

    public String getName() { return name; }

    public String getVersion() {return version; }

    public String getPrefix() { return prefix; }

    public String getCustomName() { return customName; }

    public ManagerRegistry managerRegistry;
    public ScreenRegistry screenRegistry;

    public void startup() {

        Display.setTitle("Raze v" + getVersion());

        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedImage icon16 = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("raze/16x16.png")).getInputStream());
            BufferedImage icon32 = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("raze/32x32.png")).getInputStream());

            InputStream icon16Stream = Minecraft.convertToInputStream(icon16);
            InputStream icon32Stream = Minecraft.convertToInputStream(icon32);

            Minecraft.getMinecraft().setWindowIcon(icon16Stream, icon32Stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        managerRegistry = new ManagerRegistry();

        managerRegistry.moduleRegistry.init();
        managerRegistry.commandRegistry.init();
        managerRegistry.themeRegistry.init();

        screenRegistry = new ScreenRegistry();

        managerRegistry.eventManager.subscribe(managerRegistry.moduleRegistry);
        managerRegistry.eventManager.subscribe(managerRegistry.commandRegistry);
        managerRegistry.eventManager.subscribe(managerRegistry.themeRegistry);

        // This is needed!:
        Minecraft.getMinecraft().gameSettings.guiScale = 2;
        Minecraft.getMinecraft().gameSettings.limitFramerate = 144;

        setupFolders();
    }

    public void shutdown() {
        SaveConfig saveConfig = new SaveConfig();
        saveConfig.saveConfig("raze/", "Default");
        System.out.println("[R] - Saved Default Config");
        saveConfig.saveKeyBinds("raze/", "KeyBinds");
        System.out.println("[R] - Saved KeyBinds");
    }

    private void setupFolders() {
        File razeFolder = new File("raze");
        if (!razeFolder.exists()) {
            razeFolder.mkdirs();
            System.out.println("[R] - Created folder 'raze'");
        } else {
            System.out.println("[R] - Folder 'raze' already exists, skipping step");
        }

        File configFolder = new File("raze/configs");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
            System.out.println("[R] - Created folder 'configs'");
        } else {
            System.out.println("[R] - Folder 'configs' already exists, skipping step");
        }

        LoadConfig loadConfig = new LoadConfig();
        File defaultConfig = new File("raze/Default.json");
        if (defaultConfig.exists()) {
            loadConfig.loadConfig("raze/","Default");
            System.out.println("[R] - Loaded Default Config");
        } else {
            System.out.println("[R] - Default Config Missing, skipping step");
        }

        File defaultBinds = new File("raze/KeyBinds.json");
        if (defaultBinds.exists()) {
            loadConfig.loadKeyBinds("raze/", "KeyBinds");
            System.out.println("[R] - Loaded KeyBinds");
        } else {
            System.out.println("[R] - KeyBinds Config Missing, skipping step");
        }
    }
}
