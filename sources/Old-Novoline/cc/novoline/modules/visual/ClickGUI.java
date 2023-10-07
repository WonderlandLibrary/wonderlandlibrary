package cc.novoline.modules.visual;

import cc.novoline.Novoline;
import cc.novoline.events.EventManager;
import cc.novoline.events.EventTarget;
import cc.novoline.events.events.*;
import cc.novoline.gui.screen.click.DiscordGUI;
import cc.novoline.gui.screen.dropdown.DropdownGUI;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.DoubleProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import cc.novoline.modules.exploits.Blink;
import cc.novoline.modules.exploits.Teleport;
import cc.novoline.modules.player.BedBreaker;
import cc.novoline.modules.player.ChestStealer;
import cc.novoline.modules.player.InvManager;
import cc.novoline.utils.Channels;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.Timer;
import cc.novoline.utils.notifications.NotificationType;
import cc.novoline.utils.pathfinding.AStarCustomPathfinder;
import cc.novoline.utils.pathfinding.Vec3;
import cc.novoline.utils.tasks.FutureTask;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;
import viaversion.viafabric.ViaFabric;

import java.util.ArrayList;

import static cc.novoline.utils.notifications.NotificationType.WARNING;

public final class ClickGUI extends AbstractModule {

    /* fields */
    private boolean setLanguage, wasPre, mwPort, hubbed, notify;
    private Timer swapLobbyStopwatch = new Timer();
    private int ticks, startingIn;
    private Servers currentServer = Servers.NONE;
    private Channels channel = Channels.ALL;
    private int[] coords = new int[]{};

    /* properties @off */
    @Property("design")
    public final StringProperty design = PropertyFactory.createString("Dropdown").acceptableValues("Material", "Legacy", "Dropdown");
    @Property("blur")
    private final BooleanProperty blur = PropertyFactory.booleanFalse();
    @Property("close_previous")
    private final BooleanProperty closePrevious = PropertyFactory.booleanFalse();


    /* constructors @on */
    public ClickGUI(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "ClickGUI", "Click Gui", Keyboard.KEY_RSHIFT, EnumModuleType.VISUALS, "");
        Manager.put(new Setting("CGUI_DESIGN", "Design", SettingType.COMBOBOX, this, design));
        Manager.put(new Setting("BLUR", "Blur", SettingType.CHECKBOX, this, blur));
        Manager.put(new Setting("BLUR", "Close Previous", SettingType.CHECKBOX, this, closePrevious));
    }

    /* methods */
    @Override
    public void onEnable() {
        if (design.get().equalsIgnoreCase("Dropdown")) {
            mc.displayGuiScreen(novoline.getDropDownGUI());
        } else {
            mc.displayGuiScreen(novoline.getDiscordGUI());
        }

        toggle();
    }

    @Override
    public void onDisable() {
        EventManager.register(this);
    }

    public int getGUIColor() {
        return getModule(HUD.class).getColor().getRGB();
    }

    @EventTarget
    public void onBind(BindEvent event) {
        mc.player.sendChatMessage(".bind " + event.getModule().getName() + " " + event.getKeyName());
    }

    @EventTarget
    public void onConnection(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (event.getPacket() instanceof C00Handshake) {
                notify = !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
            }

            if (notify) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    if (ServerUtils.isHypixel()) {
                        setLanguage = true;
                        novoline.getNotificationManager().pop("Novoline", "Hypixel bypasses enabled!", NotificationType.INFO);
                    } else {
                        novoline.getNotificationManager().pop("Novoline", "Hypixel bypasses disabled! Contact staff", 6_000, NotificationType.ERROR);
                    }

                    notify = false;
                }
            }
        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent tick) {
        if (mc.world == null) {
            for (Class mClass : new Class[]{KillAura.class, ChestStealer.class, InvManager.class, Blink.class}) {
                if (getModule(mClass).isEnabled()) {
                    getModule(mClass).toggle();
                }
            }

            if (mc.timer.timerSpeed != 1) {
                mc.timer.timerSpeed = 1;
            }

            if (!mc.getNetHandler().getPlayerInfoMap().isEmpty()) {
                mc.getNetHandler().getPlayerInfoMap().clear();
            }

        } else {
            if (setLanguage && ServerUtils.isHypixel() && ServerUtils.serverIs(Servers.LOBBY)) {
                sendPacketNoEvent(new C01PacketChatMessage("/language english"));
                setLanguage = false;
            }
        }
    }

    @EventTarget
    public void onSpawn(SpawnCheckEvent event) {
        if (event.getEntity() instanceof EntityPlayer && novoline.getPlayerManager().hasType(event.getEntity().getName(), PlayerManager.EnumPlayerType.TARGET)) {
            novoline.getNotificationManager().pop("target detected: " + event.getEntity().getName(), 3000, NotificationType.WARNING);
        }
    }

    @EventTarget
    public void onSetting(SettingEvent setting) {
        if (setting.getSettingName().equals("RANGE") || setting.getSettingName().equals("BLOCK_RANGE") || setting.getSettingName().equals("WALL_RANGE")) {
            DoubleProperty range = getModule(KillAura.class).getRange();
            DoubleProperty wallRange = getModule(KillAura.class).getWallRange();

            if (wallRange.get() > range.get()) {
                wallRange.set(range.get());
            }
        }

        switch (setting.getSettingName()) {
            case "TPMODE":
                if (getModule(Teleport.class).isEnabled()) {
                    getModule(Teleport.class).toggle();
                }

                break;

            case "BLOCK_RANGE":
            case "RANGE":
                DoubleProperty range = getModule(KillAura.class).getRange();
                DoubleProperty blockRange = getModule(KillAura.class).getBlockRange();

                if (blockRange.get() < range.get()) {
                    range.set(blockRange.get());
                } else if (range.get() > blockRange.get()) {
                    blockRange.set(range.get());
                }

                break;

            case "CGUI_DESIGN":
                if (design.equalsIgnoreCase("Dropdown")) {
                    if (mc.currentScreen instanceof DiscordGUI) {
                        mc.player.closeScreen();
                    }

                    if (mc.currentScreen == null) {
                        mc.displayGuiScreen(Novoline.getInstance().getDropDownGUI());
                    }
                }

                if (design.equalsIgnoreCase("Material") || design.equalsIgnoreCase("Legacy")) {
                    if (mc.currentScreen instanceof DropdownGUI) {
                        mc.player.closeScreen();
                    }

                    if (mc.currentScreen == null) {
                        mc.displayGuiScreen(Novoline.getInstance().getDiscordGUI());
                    }
                }

                break;

            case "TH_X": {
                KillAura killAura = getModule(KillAura.class);
                final ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());

                if (killAura.getThx().get() > sr.getScaledWidth() - 50) {
                    killAura.getThx().set(sr.getScaledWidth() - 50);
                }

                break;
            }

            case "TH_Y": {
                KillAura killAura = getModule(KillAura.class);
                final ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());

                if (killAura.getThy().get() > sr.getScaledHeight() - 50) {
                    killAura.getThy().set(sr.getScaledHeight() - 50);
                }

                break;
            }

            case "FLIGHT_HYPIXEL_MODE": {
                if (setting.getStringProperty().equals("Normal")) {
                    Novoline.getInstance().getNotificationManager().pop("Unsafe fly mode!", "Please consider using safe mode to avoid setting off staff alerts!", 10000, WARNING);
                }

                break;
            }

            case "KA_FILTER": {
                if (setting.getListProperty().contains("Teams") && getModule(KillAura.class).getTarget() != null) {
                    getModule(KillAura.class).setTarget(null);
                }

                break;
            }

            case "FLIGHT_SW_BOOST": {
                if (setting.getSettingType() == SettingType.CHECKBOX && setting.getBooleanProperty().get()) {
                    novoline.getNotificationManager().pop(setting.getModule().getDisplayName(), "You need bow and arrows in order to use " + setting.getDisplayName(), 3_000, NotificationType.INFO);
                }

                break;
            }
        }
    }

    @EventTarget
    public void onReceive(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S2EPacketCloseWindow) {
                S2EPacketCloseWindow packet = (S2EPacketCloseWindow) event.getPacket();

                if (mc.currentScreen instanceof DiscordGUI || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof DropdownGUI) {
                    event.setCancelled(true);
                }
            }

            if (event.getPacket() instanceof S00PacketDisconnect && !novoline.getTaskManager().getFutureTasks().isEmpty()) {
                novoline.getTaskManager().getFutureTasks().clear();
            }

            if (ServerUtils.isHypixel() && event.getPacket() instanceof S3DPacketDisplayScoreboard) {
                S3DPacketDisplayScoreboard packet = (S3DPacketDisplayScoreboard) event.getPacket();
                String serverName = packet.getServerName();

                if (serverName.equalsIgnoreCase("Mw")) {
                    currentServer = Servers.MW;
                } else if (serverName.equalsIgnoreCase("\u00a7e\u00a7lHYPIXEL")) {
                    currentServer = Servers.UHC;
                } else if (serverName.equalsIgnoreCase("SForeboard")) {
                    currentServer = Servers.SW;
                } else if (serverName.equalsIgnoreCase("BForeboard")) {
                    currentServer = Servers.BW;
                } else if (serverName.equalsIgnoreCase("PreScoreboard")) {
                    currentServer = Servers.PRE;
                } else if (serverName.equalsIgnoreCase("Duels")) {
                    currentServer = Servers.DUELS;
                } else if (serverName.equalsIgnoreCase("Pit")) {
                    currentServer = Servers.PIT;
                } else if (serverName.equalsIgnoreCase("Blitz SG")) {
                    currentServer = Servers.SG;
                } else if (serverName.equalsIgnoreCase("MurderMystery")) {
                    currentServer = Servers.MM;
                } else if (!serverName.contains("health") && !serverName.contains("\u272B")) {
                    currentServer = Servers.NONE;
                }
            }
        }
    }

    @EventTarget
    public void inGameTime(TickUpdateEvent event) {
        if (ServerUtils.isHypixel() && !ServerUtils.serverIs(Servers.PRE) && !ServerUtils.serverIs(Servers.LOBBY) && !ServerUtils.serverIs(Servers.NONE)) {
            ticks++;

        } else if (ticks > 0) {
            ticks = 0;
        }
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        if (mwPort) {
            sendPacketNoEvent(new C01PacketChatMessage("/hub"));
            hubbed = true;
            mwPort = false;
        }
        if (ServerUtils.serverIs(Servers.PRE)) {
            wasPre = true;
        } else if (wasPre && ServerUtils.serverIs(Servers.BW)) {
            novoline.getNotificationManager().pop("Bedwars", "Using fly may result in a ban, wait", 15000, WARNING);
            getModule(BedBreaker.class).setWhiteListed(null);

            Novoline.getInstance().getTaskManager().queue(new FutureTask(500) {
                @Override
                public void execute() {
                    for (int x = -20; x < 21; x++) {
                        for (int z = -20; z < 21; z++) {
                            for (int y = -10; y < 12; y++) {
                                BlockPos pos = new BlockPos(mc.player.posX - x, mc.player.posY + y, mc.player.posZ - z);
                                Block block = mc.world.getBlockState(pos).getBlock();
                                if (mc.world.getBlockState(pos).getBlock() == Blocks.bed && mc.world.getBlockState(pos).getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
                                    getModule(BedBreaker.class).setWhiteListed(pos);
                                    novoline.getNotificationManager().pop("Whitelisted your own bed!", "Whitelisted bed at " + pos.toString(), 3000, NotificationType.INFO);
                                }
                            }
                        }
                    }
                }

                @Override
                public void run() {

                }
            });

            wasPre = false;
        }

        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i) != null) {
                if (mc.player.inventory.getStackInSlot(i).stackSize == 0) {
                    mc.player.inventory.removeStackFromSlot(i);
                }
            }
        }
    }

    @EventTarget
    public void onAutoDisable(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (ServerUtils.isHypixel()) {
                if (event.getPacket() instanceof C01PacketChatMessage) {
                    C01PacketChatMessage packet = (C01PacketChatMessage) event.getPacket();

                    if (packet.getMessage().startsWith("/language")) {
                        event.setCancelled(true);
                    }
                }

                if (ServerUtils.isHypixel()) {
                    if (ViaFabric.clientSideVersion == novoline.viaVersion()) {
                        if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                            C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.getPacket();
                            ItemStack stack = packet.getStack();

                            if (stack != null && stack.getItem() instanceof ItemSkull && stack.stackSize == 1) {
                                sendPacketNoEvent(new C08PacketPlayerBlockPlacement(stack));
                                mc.playerController.windowClick(0, 36 + mc.player.inventory.currentItem, 2, 3, mc.player);
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }

        } else {
            if (ServerUtils.isHypixel()) {
                if (event.getPacket() instanceof S02PacketChat) {
                    S02PacketChat packet = (S02PacketChat) event.getPacket();
                    String formattedText = packet.getChatComponent().getFormattedText();

                    if (formattedText.equals("\u00A7r\u00A7aSelected language: \u00A7r\u00A7eEnglish\u00A7r")) {
                        event.setCancelled(true);
                    } else if (formattedText.startsWith("\u00A7aOpened a chat conversation with")) {
                        channel = Channels.PM;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                        event.setCancelled(true);
                    } else if (formattedText.equals("\u00A7aYou are now in the \u00A7r\u00A76ALL\u00A7r\u00A7a channel\u00A7r")) {
                        channel = Channels.ALL;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                        event.setCancelled(true);
                    } else if (formattedText.equals("\u00A7aYou are now in the \u00A7r\u00A76PARTY\u00A7r\u00A7a channel\u00A7r")) {
                        channel = Channels.PARTY;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                        event.setCancelled(true);
                    } else if (formattedText.equals("\u00A7aYou are now in the \u00A7r\u00A76GUILD\u00A7r\u00A7a channel\u00A7r")) {
                        channel = Channels.GUILD;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                        event.setCancelled(true);
                    } else if (!channel.equals(Channels.GUILD) && formattedText.startsWith("\u00A7r\u00A72Guild")) {
                        channel = Channels.GUILD;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                    } else if (!channel.equals(Channels.PARTY) && formattedText.startsWith("\u00A7r\u00A79Party")) {
                        channel = Channels.PARTY;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                    } else if (!channel.equals(Channels.PM) && formattedText.startsWith("\u00A7dTo")) {
                        channel = Channels.PM;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                    } else if (channel.equals(Channels.PARTY) && formattedText.contains(mc.player.getName()) && formattedText.endsWith("\u00A7r\u00A7ehas disbanded the party!\u00A7r")) {
                        sendPacketNoEvent(new C01PacketChatMessage("/chat all"));
                    } else if (!channel.equals(Channels.ALL) && formattedText.equals("\u00A7cThe conversation you were in expired and you have been moved back to the ALL channel.\u00A7r")) {
                        channel = Channels.ALL;
                        novoline.getNotificationManager().pop("Hypixel", "You are now in the " + channelName() + " channel", 3_000, NotificationType.INFO);
                    }
                }

                if (event.getPacket() instanceof S2DPacketOpenWindow) {
                    S2DPacketOpenWindow packet = (S2DPacketOpenWindow) event.getPacket();
                    String title = packet.getWindowTitle().getFormattedText();

                    if (title.contains("Select Language")) {
                        mc.playerController.windowClick(packet.getWindowId(), 49, 0, 0, mc.player);
                        event.setCancelled(true);
                    }
                }

                if (event.getPacket() instanceof S45PacketTitle) {
                    final S45PacketTitle packet = (S45PacketTitle) event.getPacket();

                    if (packet.getType().equals(S45PacketTitle.Type.TITLE)) {
                        final String text = packet.getMessage().getUnformattedText();

                        if (text.equals("VICTORY!") || text.equals("GAME OVER!")) {
                            if (isEnabled(KillAura.class) && getModule(KillAura.class).getAutoDisable().contains("Game End")) {
                                Novoline.getInstance().getNotificationManager().pop(getModule(KillAura.class).getName() + " was disabled, because game has ended", 1_500, WARNING);
                                getModule(KillAura.class).toggle();
                            }

                            if (isEnabled(InvManager.class) && getModule(InvManager.class).getAutoDisable().contains("Game End")) {
                                Novoline.getInstance().getNotificationManager().pop(getModule(InvManager.class).getName() +
                                        " was disabled, because game has ended", 1_500, WARNING);
                                getModule(InvManager.class).toggle();
                            }

                            if (isEnabled(ChestStealer.class) && getModule(ChestStealer.class).getAutoDisable().contains("Game End")) {
                                Novoline.getInstance().getNotificationManager().pop(getModule(ChestStealer.class).getName() +
                                        " was disabled, because game has ended", 1_500, WARNING);
                                getModule(ChestStealer.class).toggle();
                            }
                        }
                    }
                }

                if (event.getPacket() instanceof S2FPacketSetSlot) {
                    S2FPacketSetSlot packet = (S2FPacketSetSlot) event.getPacket();

                    if (packet.getItem().getDisplayName().contains("\u00A7") && packet.getItem().getDisplayName().contains("Spectator")) {
                        if (isEnabled(KillAura.class) && getModule(KillAura.class).getAutoDisable().contains("Death")) {
                            Novoline.getInstance().getNotificationManager().pop(getModule(KillAura.class).getName() +
                                    " was disabled, because of player death", 1_500, WARNING);
                            getModule(KillAura.class).toggle();
                        }

                        if (isEnabled(InvManager.class) && getModule(InvManager.class).getAutoDisable().contains("Death")) {
                            Novoline.getInstance().getNotificationManager().pop(getModule(InvManager.class).getName() +
                                    " was disabled, because of player death", 1_500, WARNING);
                            getModule(InvManager.class).toggle();
                        }

                        if (isEnabled(ChestStealer.class) && getModule(ChestStealer.class).getAutoDisable().contains("Death")) {
                            Novoline.getInstance().getNotificationManager().pop(getModule(ChestStealer.class).getName() +
                                    " was disabled, because of player death", 1_500, WARNING);
                            getModule(ChestStealer.class).toggle();
                        }
                    }

                    if (ServerUtils.serverIs(Servers.LOBBY)) {
                        if (packet.getItem().getItem() instanceof ItemEditableBook) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onLoadWorld(LoadWorldEvent event) {

        if (getModule(KillAura.class).isEnabled() && getModule(KillAura.class).getAutoDisable().contains("World Change")) {
            novoline.getNotificationManager().pop(getModule(KillAura.class).getName() + " was disabled, because world changed", 1_500, WARNING);
            getModule(KillAura.class).toggle();
        }

        if (getModule(InvManager.class).isEnabled() && getModule(InvManager.class).getAutoDisable().contains("World Change")) {
            novoline.getNotificationManager().pop(getModule(InvManager.class).getName() + " was disabled, because world changed", 1_500, WARNING);
            getModule(InvManager.class).toggle();
        }

        if (getModule(ChestStealer.class).isEnabled() && getModule(ChestStealer.class).getAutoDisable().contains("World Change")) {
            novoline.getNotificationManager().pop(getModule(ChestStealer.class).getName() + " was disabled, because world changed", 1_500, WARNING);
            getModule(ChestStealer.class).toggle();
        }

        if (ServerUtils.isHypixel() && !mc.getNetHandler().getPlayerInfoMap().isEmpty()) {
            mc.getNetHandler().getPlayerInfoMap().clear();
        }

        if (!getModule(ChestStealer.class).getChestIds().isEmpty()) {
            getModule(ChestStealer.class).getChestIds().clear();
        }

        if (mc.isSingleplayer() && !currentServer.equals(Servers.NONE)) {
            currentServer = Servers.NONE;
        }

        if (hubbed) {
            sendPacketNoEvent(new C01PacketChatMessage("/back"));
            hubbed = false;
            novoline.getTaskManager().queue(new FutureTask(2000) {
                @Override
                public void execute() {
                    ArrayList<cc.novoline.utils.pathfinding.Vec3> vec3s = computePath(new cc.novoline.utils.pathfinding.Vec3(mc.player.posX, mc.player.posY, mc.player.posZ), new cc.novoline.utils.pathfinding.Vec3(coords[0], coords[1], coords[2]));

                    for (cc.novoline.utils.pathfinding.Vec3 vec3 : vec3s) {
                        sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), false));
                    }
                    sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(coords[0], coords[1], coords[2], false));
                    mc.player.setPosition(coords[0], coords[1], coords[2]);
                    novoline.getNotificationManager().pop("Done", "Teleporting to " + coords[0] + "/" + coords[1] + "/" + coords[2], 3000, NotificationType.INFO);
                }

                @Override
                public void run() {

                }
            });
        }
    }

    private ArrayList<Vec3> computePath(cc.novoline.utils.pathfinding.Vec3 topFrom, cc.novoline.utils.pathfinding.Vec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }
        AStarCustomPathfinder pathfinder = new AStarCustomPathfinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        cc.novoline.utils.pathfinding.Vec3 lastLoc = null;
        cc.novoline.utils.pathfinding.Vec3 lastDashLoc = null;
        ArrayList<cc.novoline.utils.pathfinding.Vec3> path = new ArrayList<>();
        ArrayList<cc.novoline.utils.pathfinding.Vec3> pathFinderPath = pathfinder.getPath();
        for (cc.novoline.utils.pathfinding.Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 3 * 3) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    cordsLoop:
                    for (int x = (int) smallX; x <= bigX; x++) {
                        for (int y = (int) smallY; y <= bigY; y++) {
                            for (int z = (int) smallZ; z <= bigZ; z++) {
                                if (!AStarCustomPathfinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break cordsLoop;
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            i++;
        }
        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getInstance().world.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    public BooleanProperty getBlur() {
        return blur;
    }

    public BooleanProperty getClosePrevious() {
        return closePrevious;
    }

    public Servers getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(Servers server) {
        currentServer = server;
    }

    public Channels getChannel() {
        return channel;
    }

    public String channelName() {
        return channel.equals(Channels.PM) ? "Conversation" : StringUtils.capitalize(channel.name().toLowerCase());
    }

    public int getTicks() {
        return ticks;
    }

    public void setMwPort(boolean mwPort) {
        this.mwPort = mwPort;
    }

    public boolean isMwPort() {
        return mwPort;
    }

    public void setCoords(int[] coords) {
        this.coords = coords;
    }
}
