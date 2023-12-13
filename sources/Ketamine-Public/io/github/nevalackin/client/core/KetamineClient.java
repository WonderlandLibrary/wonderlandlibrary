package io.github.nevalackin.client.core;

import com.google.gson.JsonObject;
import io.github.nevalackin.client.account.AccountManager;
import io.github.nevalackin.client.binding.Bind;
import io.github.nevalackin.client.binding.BindManager;
import io.github.nevalackin.client.binding.BindType;
import io.github.nevalackin.client.binding.Bindable;
import io.github.nevalackin.client.config.ConfigManager;
import io.github.nevalackin.client.core.AbstractClientCore;
import io.github.nevalackin.client.event.Event;
import io.github.nevalackin.client.file.FileManager;
import io.github.nevalackin.client.module.ModuleManager;
import io.github.nevalackin.client.notification.NotificationManager;
import io.github.nevalackin.client.script.ScriptManager;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.account.AccountManagerImpl;
import io.github.nevalackin.client.binding.BindManagerImpl;
import io.github.nevalackin.client.config.ConfigManagerImpl;
import io.github.nevalackin.client.event.game.CloseGameEvent;
import io.github.nevalackin.client.event.game.StartGameEvent;
import io.github.nevalackin.client.event.game.input.InputType;
import io.github.nevalackin.client.file.FileManagerImpl;
import io.github.nevalackin.client.module.ModuleManagerImpl;
import io.github.nevalackin.client.notification.NotificationManagerImpl;
import io.github.nevalackin.client.script.ScriptManagerImpl;
import io.github.nevalackin.client.ui.cfont.FontGlyphs;
import io.github.nevalackin.client.ui.cfont.MipMappedFontRenderer;
import io.github.nevalackin.client.ui.nl.GuiNLUIScreen;
import io.github.nevalackin.client.util.misc.ResourceUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.bus.Bus;
import io.github.nevalackin.homoBus.bus.impl.EventBus;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public final class KetamineClient extends AbstractClientCore {

    private static KetamineClient instance;

    private CustomFontRenderer fontRenderer;
    private GuiScreen uiScreen;

    public KetamineClient() {
        super("Ketamine", "v5.7");
    }

    @EventLink
    private final Listener<StartGameEvent> onLaunchGame = event -> {
        this.fontRenderer = new MipMappedFontRenderer(new FontGlyphs(ResourceUtil.createFontTTF("fonts/Regular.ttf"), 500),
                                                      new FontGlyphs(ResourceUtil.createFontTTF("fonts/Medium.ttf"), 700),
                                                      new FontGlyphs(ResourceUtil.createFontTTF("fonts/Bold.ttf"), 900));

        this.getFileManager();
        this.getBindManager().register(this.openCloseUI(), new Bind(InputType.KEYBOARD, Keyboard.KEY_RSHIFT, BindType.TOGGLE));
        this.getAccountManager().load();
        this.getModuleManager();
        this.getBindManager().load();
        this.getConfigManager().load("default");
        this.initDiscordRPC();
    };

    private Bindable openCloseUI() {
        if (this.uiScreen == null) {
            this.uiScreen = new GuiNLUIScreen();
        }

        return new Bindable() {
            @Override
            public String getName() {
                return "UI";
            }

            @Override
            public void setActive(boolean active) {
                Minecraft.getMinecraft().displayGuiScreen(active ? uiScreen : null);
            }

            @Override
            public boolean isActive() {
                return Minecraft.getMinecraft().currentScreen == uiScreen;
            }

            @Override
            public void loadBind(JsonObject object) {

            }

            @Override
            public void saveBind(JsonObject object) {

            }
        };
    }

    private void initDiscordRPC() {
        DiscordRPC.discordInitialize("877275167653498941", new DiscordEventHandlers.Builder().build(), true);
    }

    public void updateDiscordRPC(final String msg) {
        final DiscordRichPresence rpc = new DiscordRichPresence.Builder(msg)
            .setSmallImage("rpc", "Made by")
            .setBigImage("big_rpc", "neva lack#4597")
            .setStartTimestamps(System.currentTimeMillis())
            .build();
        DiscordRPC.discordUpdatePresence(rpc);
    }

    @EventLink
    private final Listener<CloseGameEvent> onCloseGame = event -> {
        this.getConfigManager().save("default");
        this.getBindManager().save();
        this.getAccountManager().save();
        DiscordRPC.discordShutdown();
    };

    @Override
    protected Bus<Event> getBusImpl() {
        return new EventBus<>();
    }

    @Override
    protected FileManager getFileImpl() {
        return new FileManagerImpl();
    }

    @Override
    protected ModuleManager getModuleManagerImpl() {
        return new ModuleManagerImpl();
    }

    @Override
    protected ConfigManager getConfigManagerImpl() {
        return new ConfigManagerImpl();
    }

    @Override
    protected AccountManager getAccountManagerImpl() {
        return new AccountManagerImpl();
    }

    @Override
    protected ScriptManager getScriptManagerImpl() {
        return new ScriptManagerImpl();
    }

    public CustomFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    @Override
    protected NotificationManager getNotificationManagerImpl() {
        return new NotificationManagerImpl();
    }

    @Override
    protected BindManager getBindManagerImpl() {
        return new BindManagerImpl();
    }

    public static KetamineClient getInstance() {
        if (instance == null) {
            instance = new KetamineClient();
        }

        return instance;
    }
}
