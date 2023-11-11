package me.nyan.flush;

import me.nyan.flush.altmanager.AltManager;
import me.nyan.flush.clickgui.astolfo.AstolfoClickGui;
import me.nyan.flush.clickgui.discord.DiscordClickGui;
import me.nyan.flush.clickgui.flush.FlushClickGui;
import me.nyan.flush.clickgui.remix.RemixClickGui;
import me.nyan.flush.clickgui.sigma.SigmaClickGui;
import me.nyan.flush.command.CommandManager;
import me.nyan.flush.customhud.CustomHud;
import me.nyan.flush.customhud.GuiConfigureHud;
import me.nyan.flush.event.EventManager;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventChat;
import me.nyan.flush.event.impl.EventGameLoop;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.file.ConfigManager;
import me.nyan.flush.file.FileManager;
import me.nyan.flush.friend.FriendManager;
import me.nyan.flush.module.ModuleManager;
import me.nyan.flush.notifications.NotificationManager;
import me.nyan.flush.target.TargetManager;
import me.nyan.flush.ui.discord.DiscordRP;
import me.nyan.flush.ui.elements.User;
import me.nyan.flush.ui.fontrenderer.FontManager;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.ui.menu.GuiPaint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.superblaubeere27.glslsandboxrenderer.GLSLSandboxShader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;
import viamcp.protocols.ProtocolCollection;

import java.io.File;
import java.lang.reflect.Method;

public class Flush {
    private static Flush instance;
    public static final String NAME = "Flush", VERSION = "0.9-BETA";
    private static File clientPath;
    public static final int BUILD_NUMBER = 1;
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private final DiscordRP discordRP = new DiscordRP();

    public static ServerData currentServer;
    public static String defaultUsername;
    public static User currentUser;

    private ModuleManager moduleManager;
    private FontManager fontManager;
    private CommandManager commandManager;
    private NotificationManager notificationManager;
    private FileManager fileManager;
    private ConfigManager configManager;
    private FriendManager friendManager;
    private TargetManager targetManager;
    private AltManager altManager;
    private CustomHud customHud;
    private GuiConfigureHud hudCustomizer;

    public FlushClickGui flushClickGui;
    public DiscordClickGui discordClickGui;
    public AstolfoClickGui astolfoClickGui;
    public RemixClickGui remixClickGui;
    public SigmaClickGui sigmaClickGui;

    private GLSLSandboxShader shader;

    private GuiPaint paint;

    private static boolean verusFlyDisabler;
    private static int sendPacket;

    private long lastFrame;
    private float frameTime;

    private Method generator;

    public static Flush getInstance() {
        if (instance == null) {
            instance = new Flush();
        }
        return instance;
    }

    public void onPreInit() {
        Display.setTitle("Loading " + NAME + " " + VERSION + "...");

        discordRP.start();
        discordRP.update("Starting...", "");

        EventManager.register(this);

        altManager = new AltManager();
        moduleManager = new ModuleManager();
        friendManager = new FriendManager();
        targetManager = new TargetManager();
        fileManager = new FileManager();
        configManager = new ConfigManager();
        paint = new GuiPaint();

        altManager.load();
        friendManager.load();

        try {
            Class<?> c = Class.forName("me.nyan.flush.FuncraftGenerator");
            generator = c.getDeclaredMethod("createOfflineIP", String.class);
            generator.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
    }

    public void onPostInit() {
        Display.setTitle(NAME + " " + VERSION);
        moduleManager.load();

        fontManager = new FontManager();

        try {
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider(4, 4, 110, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initClient() {
        try {
            flushClickGui = new FlushClickGui();
            discordClickGui = new DiscordClickGui(60, 60);
            remixClickGui = new RemixClickGui(60, 60);
            astolfoClickGui = new AstolfoClickGui();
            sigmaClickGui = new SigmaClickGui();
            notificationManager = new NotificationManager();
            commandManager = new CommandManager();
            //scriptManager.loadAll(false);
            fileManager.load();
            customHud = new CustomHud();
            customHud.start();

            hudCustomizer = new GuiConfigureHud();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void onShutdown() {
        EventManager.shutdown();

        if (customHud != null) {
            customHud.save();
        }
        fileManager.save();
        altManager.save();
        friendManager.save();
        discordRP.shutdown();
    }

    @SubscribeEvent
    public void onChat(EventChat e) {
        commandManager.onChat(e);
    }

    public static GlyphPageFontRenderer getFont(String name, int size) {
        return instance.fontManager.getFont(name, size);
    }

    public static void playClickSound() {
        Minecraft.getMinecraft().getSoundHandler()
                .playSound(PositionedSoundRecord.create(
                        new ResourceLocation("gui.button.press"),
                        1.0F
                ));
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) throws Exception {
        if (verusFlyDisabler) {
            if (sendPacket == 0) {
                //mc.getNetHandler().addToSendQueueNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                Minecraft.getMinecraft().getNetHandler().addToSendQueueNoEvent(new C08PacketPlayerBlockPlacement(
                        Minecraft.getMinecraft().thePlayer.getPosition().add(0, -1.5, 0),
                        EnumFacing.UP.getIndex(), new ItemStack(Blocks.stone), 0, 0.5f, 0));
            }
            sendPacket++;

            if (sendPacket == 4) {
                sendPacket = 0;
            }
        }
        if (currentUser == null) {
            Runtime.getRuntime().halt(0);
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S02PacketChat) {
            String text = ((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText();

            int exploitIndex = text.indexOf("${jndi");
            if (exploitIndex != -1 && text.lastIndexOf("}") > exploitIndex) {
                e.cancel();
            }
        }

        String[] ips = new String[]{"www.hypixel.net", "play.cubecraft.net", "www.funcraft.net"};
        if (e.getPacket() instanceof S3CPacketUpdateScore) {
            S3CPacketUpdateScore packet = e.getPacket();
            //System.out.println(packet.getPlayerName());

            for (String ip : ips) {
                packet.setName(packet.getPlayerName().replaceAll("(?i)\u00a7[\\dA-FK-OR]" + ip,
                        EnumChatFormatting.BLUE + (EnumChatFormatting.ITALIC + "flushclient.xyz")));
            }
        }

        if (verusFlyDisabler && e.getPacket() instanceof C02PacketUseEntity) {
            if (sendPacket < 2) {
                e.cancel();
            }
        }
    }

    public static ResourceLocation getCape() {
        return new ResourceLocation("flush/cape.png");
    }

    public static Thread newThread(Runnable runnable, boolean daemon, boolean start) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(daemon);
        if (start) {
            thread.start();
        }
        return thread;
    }

    public GLSLSandboxShader getShader() {
        return shader;
    }

    public void setShader(GLSLSandboxShader shader) {
        this.shader = shader;
    }

    @SubscribeEvent
    public void onGameLoop(EventGameLoop e) {
        long currentTime = System.nanoTime();
        float frameTime = (currentTime - lastFrame) / 1E6F;
        lastFrame = currentTime;
        this.frameTime = frameTime;
    }

    public static void enableVerusDisabler() {
        if (!verusFlyDisabler) {
            sendPacket = 0;
        }
        verusFlyDisabler = true;
    }

    public static void disableVerusDisabler() {
        verusFlyDisabler = false;
    }

    public DiscordRP getDiscordRP() {
        return discordRP;
    }

    public static float getFrameTime() {
        return instance.frameTime;
    }

    public Method getGenerator() {
        return generator;
    }

    public void setViaVersion(ProtocolCollection protocol) {
        int ver = protocol.getVersion().getVersion();
        ViaMCP.getInstance().setVersion(ver);
        ViaMCP.getInstance().asyncSlider.setVersion(ver);
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public AltManager getAltManager() {
        return altManager;
    }

    public GuiConfigureHud getHudCustomizer() {
        return hudCustomizer;
    }

    public CustomHud getCustomHud() {
        return customHud;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public TargetManager getTargetManager() {
        return targetManager;
    }

    public GuiPaint getPaint() {
        return paint;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static File getClientPath() {
        if (clientPath == null) {
            clientPath = new File(Minecraft.getMinecraft().mcDataDir, NAME);
        }
        return clientPath;
    }
}
