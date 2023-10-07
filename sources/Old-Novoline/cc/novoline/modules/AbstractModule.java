package cc.novoline.modules;

import cc.novoline.Initializer;
import cc.novoline.Novoline;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.binds.KeyboardKeybind;
import cc.novoline.modules.binds.ModuleKeybind;
import cc.novoline.modules.configurations.annotation.DelsyConfig;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.KeyBindProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import cc.novoline.modules.exceptions.LoadConfigException;
import cc.novoline.modules.serializers.ConfigSerializer;
import cc.novoline.modules.visual.ClickGUI;
import cc.novoline.modules.visual.HUD;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.messages.MessageFactory;
import cc.novoline.utils.messages.TextMessage;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static cc.novoline.commands.NovoCommand.EMPTY_COMPONENT;
import static cc.novoline.commands.NovoCommand.PREFIX;
import static cc.novoline.events.EventManager.register;
import static cc.novoline.events.EventManager.unregister;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.booleanFalse;
import static cc.novoline.utils.messages.MessageFactory.text;
import static cc.novoline.utils.notifications.NotificationType.WARNING;


@DelsyConfig
public abstract class AbstractModule {

    protected double secretShit = Initializer.getInstance().secretShit;
    protected double flight50 = Initializer.getInstance().flight50;
    protected double flight90 = Initializer.getInstance().flight90;
    protected double flightAmp = Initializer.getInstance().flightAmp;
    protected double flight75 = Initializer.getInstance().flight75;
    protected double flightMagicCon1 = Initializer.getInstance().flightMagicCon1;
    protected double flightMagicCon2 = Initializer.getInstance().flightMagicCon2;
    protected double flightHypixelGroundCheck = Initializer.getInstance().flightHypixelGroundCheck;
    protected double bunnyDivFriction = Initializer.getInstance().bunnyDivFriction;
    protected double threshold = Initializer.getInstance().threshold;

    public double min17 = Initializer.getInstance().min17, max17 = Initializer.getInstance().max17;
    public double min18 = Initializer.getInstance().min18, max18 = Initializer.getInstance().max18;
    public double min19 = Initializer.getInstance().min19, max19 = Initializer.getInstance().max19;
    public double min20 = Initializer.getInstance().min20, max20 = Initializer.getInstance().max20;

    protected final Logger logger = LogManager.getLogger();
    protected final Novoline novoline;
    protected final ModuleManager moduleManager;
    protected final Minecraft mc;
    protected Config config;

    protected final EnumModuleType type;
    protected final String name;
    protected String displayName;
    protected final String description;

    protected String suffix;

    @Property("enabled")
    protected final BooleanProperty enabled = booleanFalse();
    @Property("hidden")
    protected final BooleanProperty hidden = booleanFalse();
    @Property("display-name")
    protected final StringProperty displayNameProperty = PropertyFactory.createString("");

    protected final KeyBindProperty bind = PropertyFactory.keyBinding(KeyboardKeybind.of(Keyboard.KEY_NONE));

    public float offsetX = 0;
    public float offsetY = 0;

    /* constructors */
    protected AbstractModule(@NotNull ModuleManager moduleManager,
                             @NotNull String name,
                             @NotNull String displayName,
                             int keyBind,
                             @NotNull EnumModuleType type,
                             @Nullable String description,
                             @Nullable String path) {
        this.moduleManager = moduleManager;
        this.novoline = moduleManager.getNovoline();
        this.mc = novoline.getMinecraft();
        this.name = name;
        this.type = type;
        this.description = description;
        this.displayName = displayName;
        Manager.put(new Setting("MODULE_BIND", "Bind", SettingType.BINDABLE, this, bind));

        if (path != null) {
            this.config = Config.fromPath(novoline.getDataFolder().resolve(path + ".novo"));
            TypeSerializerCollection serializers = config.getLoader().getDefaultOptions().getSerializers();

            serializers.registerPredicate(input -> {
                Class<? super Object> rawType = input.getRawType();
                boolean b;

                do {
                    if (!(b = rawType.isAnnotationPresent(DelsyConfig.class))) {
                        rawType = rawType.getSuperclass();
                    }
                } while (!b && rawType != null && rawType.getSuperclass() != null);

                return b;
            }, new ConfigSerializer(moduleManager));
            addCustomSerializers(serializers);

            try {
                config.load();
            } catch (LoadConfigException e) {
                e.printStackTrace();
            }
        }
    }

    protected AbstractModule(@NotNull ModuleManager moduleManager,
                             @NotNull String name,
                             @NotNull String displayName,
                             @NotNull EnumModuleType type,
                             @Nullable String description,
                             @Nullable String path) {
        this(moduleManager, name, displayName, Keyboard.KEY_NONE, type, description, path);
    }

    protected AbstractModule(@NotNull ModuleManager moduleManager,
                             @NotNull String name,
                             @NotNull String displayName,
                             int keyBind,
                             @NotNull EnumModuleType type,
                             @Nullable String description) {
        this(moduleManager, name, displayName, keyBind, type, description, null);
    }

    protected AbstractModule(@NotNull ModuleManager novoline,
                             @NotNull String name,
                             @NotNull EnumModuleType type,
                             @Nullable String description,
                             @Nullable String path) {
        this(novoline, name, name, 0, type, description, path);
    }

    protected AbstractModule(@NotNull ModuleManager novoline,
                             @NotNull String name,
                             @NotNull String displayName,
                             @NotNull EnumModuleType type,
                             @Nullable String description) {
        this(novoline, name, displayName, Keyboard.KEY_NONE, type, description, null);
    }

    protected AbstractModule(@NotNull ModuleManager novoline,
                             @NotNull String name,
                             @NotNull String displayName,
                             @NotNull int keyBind,
                             @NotNull EnumModuleType type) {
        this(novoline, name, displayName, keyBind, type, null, null);
    }

    protected AbstractModule(@NotNull ModuleManager novoline,
                             @NotNull String name,
                             @NotNull EnumModuleType type,
                             @Nullable String description) {
        this(novoline, name, name, 0, type, description, null);
    }

    protected AbstractModule(@NotNull ModuleManager novoline,
                             @NotNull String name,
                             @NotNull EnumModuleType type) {
        this(novoline, name, name, 0, type, null, null);
    }

    protected AbstractModule(@NotNull ModuleManager novoline,
                             @NotNull EnumModuleType type,
                             @NotNull String name) {
        this(novoline, name, name, 0, type, null, null);
    }

    protected AbstractModule(@NotNull ModuleManager novoline,
                             @NotNull EnumModuleType type,
                             @NotNull String name,
                             @NotNull String displayName) {
        this(novoline, name, displayName, 0, type, null, null);
    }

    protected void addCustomSerializers(@NotNull TypeSerializerCollection collection) {
    }

    /* methods */
    private final Cache<Class<? extends AbstractModule>, AbstractModule> moduleCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    @SuppressWarnings("unchecked")
    public <Module extends AbstractModule> @NotNull Module getModule(Class<? extends Module> moduleClass) {
        try {
            return (Module) moduleCache.get(moduleClass, () -> moduleManager.getModule(moduleClass));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    protected void send(@Nullable TextMessage message, boolean prefix) {
        IChatComponent chatComponent;

        if (message != null) {
            if (prefix) {
                chatComponent = message.prefix(PREFIX);
            } else {
                chatComponent = message;
            }
        } else {
            chatComponent = EMPTY_COMPONENT;
        }

        mc.player.addChatComponentMessage(chatComponent);
    }

    protected void send(@Nullable TextMessage component) {
        send(component, false);
    }

    protected void send(@NotNull String s, boolean prefix) {
        send(text(s), prefix);
    }

    protected void send(@NotNull String s) {
        send(text(s));
    }

    protected void sendEmpty() {
        mc.player.addChatComponentMessage(MessageFactory.empty());
    }

    @SuppressWarnings({"MagicNumber", "MethodWithMultipleReturnPoints"})
    public float categoryColor() {
        switch (type) {
            case COMBAT:
                return 0.9F;
            case MOVEMENT:
                return 0.55F;
            case VISUALS:
                return 0.45F;
            case PLAYER:
                return 0.1F;
            default:
                return 0;
        }
    }

    @SuppressWarnings({"ChainOfInstanceofChecks", "CastToConcreteClass", "InstanceofConcreteClass"})
    protected void autoDisable(@NotNull PacketEvent event) {
        if (event.getState() == PacketEvent.State.INCOMING) {
            if (event.getPacket() instanceof S45PacketTitle) {
                S45PacketTitle packet = (S45PacketTitle) event.getPacket();

                if (packet.getType() == S45PacketTitle.Type.TITLE) {
                    String text = packet.getMessage().getUnformattedText();

                    if (text.equals("VICTORY!")) {
                        toggle();
                        novoline.getNotificationManager().pop(name + " was disabled, because game has ended", 1_000, WARNING);
                    }
                }
            }

            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                String message = packet.getChatComponent().getUnformattedText();

                if (message.equalsIgnoreCase("You died! Want to play again? Click here! ")) {
                    novoline.getNotificationManager().pop(name + " was disabled, because game has ended", 1_000, WARNING);
                    toggle();
                }
            }

            if (ServerUtils.serverIs(Servers.SW) || ServerUtils.serverIs(Servers.BW)) {
                if (event.getPacket() instanceof S39PacketPlayerAbilities) {
                    S39PacketPlayerAbilities packet = (S39PacketPlayerAbilities) event.getPacket();

                    if (packet.isAllowFlying()) {
                        novoline.getNotificationManager().pop(name + " was disabled, because game has ended", 1_000, WARNING);
                        toggle();
                    }
                }
            }
        }
    }

    protected <M extends AbstractModule> void checkModule(@NotNull Class<? extends M> moduleClass) {
        M module = getModule(moduleClass);

        if (module.isEnabled()) {
            novoline.getNotificationManager().pop(module.getName() + " was disabled to prevent flags/errors", 1_000, WARNING);
            module.toggle();
        }
    }

    @SuppressWarnings("unchecked")
    protected void checkModule(@NotNull Class<? extends AbstractModule> @NotNull ... modules) {
        for (Class<? extends AbstractModule> mClass : modules) {
            AbstractModule module = getModule(mClass);

            if (module.isEnabled()) {
                module.toggle();
                novoline.getNotificationManager().pop("Speed check", module.name + " was disabled to prevent flags/errors", 1_000, WARNING);
            }
        }
    }

    protected <M extends AbstractModule> boolean isEnabled(@NotNull Class<? extends M> moduleClass) {
        return getModule(moduleClass).isEnabled();
    }

    public void sendPacket(@NotNull Packet<?> packet) {
        if (mc.player != null) {
            mc.player.connection.sendPacket(packet);
        }
    }

    public void sendPacketNoEvent(@NotNull Packet<?> packet) {
        if (mc.player != null) {
            mc.player.connection.sendPacketNoEvent(packet);
        }
    }

    public void print(Object... debug) {
        //debug mode for uids < 5
//        if (Integer.parseInt(Protection.serviceUid) < 5) {
            String message = Arrays.toString(debug);
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
//        }
    }

    public boolean toggle() {
        if (enabled.get()) {
            setEnabled(false);
            return false;
        } else {
            setEnabled(true);
            return true;
        }
    }

    public void setSuffix(String suffix) {
        if (suffix == null || !suffix.isEmpty()) {
            switch (getModule(HUD.class).getSuffixType().get().toLowerCase()) {
                case "simple":
                    this.suffix = "\u00A77 " + suffix;
                    break;

                case "dash":
                    this.suffix = "\u00A77 - " + suffix;
                    break;

                case "bracket":
                    this.suffix = "\u00A77 [" + suffix + "]";
                    break;

                default:
                    this.suffix = "";
            }

        } else {
            this.suffix = "";
        }
    }

    public final boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean state) {
        if (enabled.get() != state) {
            enabled.set(state);

            if (state) {
                enable();
            } else {
                disable();
            }
        }
    }

    public final void enable() {
        register(this);
        onEnable();

        if (!novoline.getModuleManager().getAbstractModules().contains(this) && this != novoline.getModuleManager().getModule(ClickGUI.class)) {
            novoline.getModuleManager().getAbstractModules().add(this);
        }

        offsetY = novoline.getModuleManager().getModule(HUD.class).getHeight(this) - 1;
        offsetX = 0;
    }

    public final void disable() {
        unregister(this);
        onDisable();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    //region Lombok
    public @NotNull String getDisplayName() {
        String property = displayNameProperty.get();
        return property == null || property.isEmpty() ? displayName : property;
    }

    public final boolean isHidden() {
        return hidden.get();
    }

    public void setHidden(boolean hidden) {
        this.hidden.set(hidden);
    }

    public @NotNull String getSuffix() {
        return suffix == null ? "" : suffix;
    }

    public @NotNull String getFinalDisplayName() {
        return getDisplayName() + getSuffix();
    }

    public @NotNull String getName() {
        return name;
    }

    public KeyBindProperty getKeybind() {
        return bind;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public @NotNull EnumModuleType getType() {
        return type;
    }

    public Config getConfig() {
        return config;
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    public @NotNull String getVanillaDisplayName() {
        return displayName;
    }

    public void setDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
    }

    public void setKeyBind(ModuleKeybind keyBind) {
        bind.set(keyBind);
    }

    public void setDisplayNameProperty(String displayNameProperty) {
        this.displayNameProperty.set(displayNameProperty);
    }

    public @NotNull Novoline getNovoline() {
        return novoline;
    }
    //endregion

    public KeyBindProperty getBind() {
        return bind;
    }
}
