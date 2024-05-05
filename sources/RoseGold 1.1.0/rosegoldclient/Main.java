package rosegoldclient;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import gg.essential.api.utils.Multithreading;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.input.Keyboard;
import rosegoldclient.commands.*;
import rosegoldclient.events.KeybindEvent;
import rosegoldclient.events.MillisecondEvent;
import rosegoldclient.events.SecondEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.features.*;
import rosegoldclient.utils.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static rosegoldclient.utils.StringUtils.*;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, clientSideOnly = true)
public class Main {
    public static final String MODID = "rosegoldclient";
    public static final String NAME = "RoseGoldClient";
    public static final String VERSION = "1.1.0";

    public static Minecraft mc = Minecraft.getMinecraft();

    public static GuiScreen display = null;
    public static Config configFile = Config.INSTANCE;

    public static ArrayList<String> cheater = new ArrayList<>();
    public static GuiScreen guiToOpen = null;
    public static JsonObject rgc;
    public static JsonArray wynncraft_items;
    public static ArrayList<KeyBinding> keybinds = new ArrayList<>();
    public static HashMap<String, WynncraftItem> wynncraftItems = new HashMap<>();

    public static boolean killAura = false;
    public static boolean spellAura = false;
    public static boolean doPhase = false;
    public static boolean doAutoWalk = false;
    public static boolean autoProfessions = false;
    public static boolean toggleSneak = false;

    public static final List<String> alphaNumeric = Arrays.asList("0123456789 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));
    public static List<String> shuffle = Arrays.asList("0123456789 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));

    public static boolean firstLogin = false;
    public static boolean anyLogin = true;
    public static boolean banned = false;
    public static String w = "h";
    public static String id = "";
    public static String name = "";
    public static String hashed = "";
    private boolean issue = false;

    public static File configDirectory;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDirectory = new File(event.getModConfigurationDirectory(), "rosegoldclient");

        if (!configDirectory.exists()) {
            firstLogin = true;
            configDirectory.mkdirs();
        }


        name = mc.getSession().getUsername();
        File kaSettings = new File(configDirectory, "kaSettings.json");
        File saSettings = new File(configDirectory, "saSettings.json");
        File autoWalkProfiles = new File(configDirectory, "autoWalkProfiles.json");
        File treeLocations = new File(configDirectory, "treeLocations.json");
        File oreLocations = new File(configDirectory, "oreLocations.json");
        File fishLocations = new File(configDirectory, "fishLocations.json");
        File cropLocations = new File(configDirectory, "cropLocations.json");

        try {
            if (kaSettings.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/kaSettings.json"));
                Type type = new TypeToken<HashSet<String>>() {}.getType();
                KillAuraFilter.KASettings = new Gson().fromJson(reader, type);
            }
            if (saSettings.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/saSettings.json"));
                Type type = new TypeToken<HashSet<String>>() {}.getType();
                SpellAuraFilter.SASettings = new Gson().fromJson(reader, type);
            }
            if (autoWalkProfiles.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/autoWalkProfiles.json"));
                Type type = new TypeToken<HashMap<String, ArrayList<Point>>>() {}.getType();
                AutoWalk.profiles = new Gson().fromJson(reader, type);
            }
            if (treeLocations.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/treeLocations.json"));
                Type type = new TypeToken<HashSet<BlockPos>>() {}.getType();
                AutoProfessions.trees = new Gson().fromJson(reader, type);
            }
            if (oreLocations.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/oreLocations.json"));
                Type type = new TypeToken<HashSet<BlockPos>>() {}.getType();
                AutoProfessions.ores = new Gson().fromJson(reader, type);
            }
            if (fishLocations.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/fishLocations.json"));
                Type type = new TypeToken<HashSet<BlockPos>>() {}.getType();
                AutoProfessions.fish = new Gson().fromJson(reader, type);
            }
            if (cropLocations.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/cropLocations.json"));
                Type type = new TypeToken<HashSet<BlockPos>>() {}.getType();
                AutoProfessions.crops = new Gson().fromJson(reader, type);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(mc.getResourceManager().getResource(new ResourceLocation("rosegoldclient", "lootruns/chests/chests.json")).getInputStream());

            JsonArray jsonArray = (new JsonParser()).parse(Objects.requireNonNull(inputStreamReader)).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                WynncraftChestESP.chests.add(new BlockPos((int) jsonElement.getAsJsonObject().get("x").getAsFloat(), (int) jsonElement.getAsJsonObject().get("y").getAsFloat(), (int) jsonElement.getAsJsonObject().get("z").getAsFloat()));
            }

        } catch (Exception e) {
            System.out.println("Error while loading RoseGoldClient config files");
            e.printStackTrace();
        }

        Collections.shuffle(shuffle);

        try {
            id = mc.getSession().getPlayerID();
            rgc = getJson("https://gist.github.com/RoseGoldIsntGay/bf410fc3dec34a1f9348d896fafd00dc/raw/").getAsJsonObject();
            Multithreading.runAsync(() -> {
                wynncraft_items = (JsonArray) getJson("https://api.wynncraft.com/public_api.php?action=itemDB&category=all").getAsJsonObject().get("items");
                wynncraft_items.forEach(wynncraft_item -> {
                    JsonObject item = wynncraft_item.getAsJsonObject();
                    String tier = item.get("tier").getAsString();
                    if (tier.equals("Legendary") || tier.equals("Fabled") || tier.equals("Mythic")) {
                        String name = item.get("name").getAsString();
                        String type = item.get("type") != null ? item.get("type").getAsString() : item.get("accessoryType").getAsString();
                        wynncraftItems.put(name, new WynncraftItem(
                                name,
                                tier,
                                type,
                                item.get("level").getAsInt()
                        ));
                    }
                });
            });
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
            issue = true;
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (issue) return;
        for (JsonElement jsonElement : rgc.get("cheaters").getAsJsonArray()) {
            cheater.add(jsonElement.getAsString());
        }
        w = rgc.get("w").getAsString();
        hashed = cheater.get((8 << 1) + 1);
        for (String cheat : cheater) {
            if (DigestUtils.sha256Hex(id + id).equals(cheat)) {
                ArrayUtils.copy(id, cheat);
            }
        }

        keybinds.add(new KeyBinding("Kill Aura", Keyboard.KEY_NONE, "RoseGoldClient - Combat")); //0
        keybinds.add(new KeyBinding("Phase", Keyboard.KEY_NONE, "RoseGoldClient - Movement")); //1
        keybinds.add(new KeyBinding("Spell Aura", Keyboard.KEY_NONE, "RoseGoldClient - Combat")); //2
        keybinds.add(new KeyBinding("Copy NBT Data", Keyboard.KEY_NONE, "RoseGoldClient")); //3
        keybinds.add(new KeyBinding("Cast RRR", Keyboard.KEY_NONE, "RoseGoldClient")); //4
        keybinds.add(new KeyBinding("Cast RLR", Keyboard.KEY_NONE, "RoseGoldClient")); //5
        keybinds.add(new KeyBinding("Cast RLL", Keyboard.KEY_NONE, "RoseGoldClient")); //6
        keybinds.add(new KeyBinding("Cast RRL", Keyboard.KEY_NONE, "RoseGoldClient")); //7
        keybinds.add(new KeyBinding("Auto Clicker", Keyboard.KEY_NONE, "RoseGoldClient - Combat")); //8
        keybinds.add(new KeyBinding("Server Side Sneak Toggle", Keyboard.KEY_NONE, "RoseGoldClient - Movement")); //9
        keybinds.add(new KeyBinding("Auto Walk", Keyboard.KEY_NONE, "RoseGoldClient - Movement")); //10
        keybinds.add(new KeyBinding("Auto Professions", Keyboard.KEY_NONE, "RoseGoldClient - World")); //11

        MinecraftForge.EVENT_BUS.register(new TickEndEvent());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new RotationUtils());
        MinecraftForge.EVENT_BUS.register(new ChestAura());
        MinecraftForge.EVENT_BUS.register(new Phase());
        MinecraftForge.EVENT_BUS.register(new KillAura());
        MinecraftForge.EVENT_BUS.register(new BlockBanPacket());
        //MinecraftForge.EVENT_BUS.register(new NoRotate());
        MinecraftForge.EVENT_BUS.register(new Watermark());
        MinecraftForge.EVENT_BUS.register(new SpellAura());
        MinecraftForge.EVENT_BUS.register(new SpellCaster());
        MinecraftForge.EVENT_BUS.register(new WynncraftChestESP());
        MinecraftForge.EVENT_BUS.register(new EntityESP());
        MinecraftForge.EVENT_BUS.register(new ChestLooter());
        MinecraftForge.EVENT_BUS.register(new InventoryWalk());
        MinecraftForge.EVENT_BUS.register(new TargetHUD());
        MinecraftForge.EVENT_BUS.register(new AutoClicker());
        MinecraftForge.EVENT_BUS.register(new CursorTP());
        MinecraftForge.EVENT_BUS.register(new EntityGhostHand());
        MinecraftForge.EVENT_BUS.register(new DroppedItemESP());
        MinecraftForge.EVENT_BUS.register(new NoFall());
        MinecraftForge.EVENT_BUS.register(new Pathfinding());
        MinecraftForge.EVENT_BUS.register(new AutoWalk());
        MinecraftForge.EVENT_BUS.register(new AutoProfessions());
        MinecraftForge.EVENT_BUS.register(new AutoCrafting());
        MinecraftForge.EVENT_BUS.register(new AutoSneak());
        MinecraftForge.EVENT_BUS.register(new AutoHeal());
        MinecraftForge.EVENT_BUS.register(new ToggleSneak());
        MinecraftForge.EVENT_BUS.register(new RareMobESP());
        MinecraftForge.EVENT_BUS.register(new AutoLootRun());

        configFile.initialize();

        ClientCommandHandler.instance.registerCommand(new MainCommand());
        ClientCommandHandler.instance.registerCommand(new KillAuraFilter());
        ClientCommandHandler.instance.registerCommand(new SpellAuraFilter());
        ClientCommandHandler.instance.registerCommand(new SelfBan());
        ClientCommandHandler.instance.registerCommand(new AddChest());
        ClientCommandHandler.instance.registerCommand(new RemoveChest());
        ClientCommandHandler.instance.registerCommand(new SaveChests());
        ClientCommandHandler.instance.registerCommand(new ChangeVelocity());
        ClientCommandHandler.instance.registerCommand(new RGTP());
        ClientCommandHandler.instance.registerCommand(new Goto());
        ClientCommandHandler.instance.registerCommand(new WalkPoint());
        ClientCommandHandler.instance.registerCommand(new AutoLootRunCommand());

        for (KeyBinding keyBinding : keybinds) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }

        System.out.println(MODID + " Initialized. Version: " + VERSION);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) throws IOException {
        Fonts.bootstrap();
        LocalDateTime now = getNextTime();
        Duration initialDelay = Duration.between(now, now);
        long initialDelaySeconds = initialDelay.getSeconds();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> MinecraftForge.EVENT_BUS.post(new SecondEvent()), initialDelaySeconds, 1, TimeUnit.SECONDS);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> MinecraftForge.EVENT_BUS.post(new MillisecondEvent()), initialDelaySeconds, 1, TimeUnit.MILLISECONDS);
    }

    @SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        event.getManager().channel().pipeline().addBefore("packet_handler", "velocity modifier", new Velocity());
    }

    public static String i(String s) {
        String str = "Error Getting Abraham Lincoln Quote. Check Your Internet Connection And Try Again.";
        try {
            str = new String(Base64.getDecoder().decode(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @SubscribeEvent
    public void onTickEnd(TickEndEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (guiToOpen != null) {
            mc.displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }
        if (!banned && firstLogin) {
            assert mc.getConnection() != null;
            mc.getConnection().getNetworkManager().closeChannel(new TextComponentString("§cKicked whilst connecting to WC" + (new Random().nextInt(24) + 1) + ": §bYou have been banned\n§4Reason: §cAbuse of multiple exploits.\n§3Appeal at §6https://wynncraft.com/appeals/"));
        }
        if (firstLogin && banned) {
            firstLogin = false;
            ITextComponent msg1 = new TextComponentString("§7This seems like your first time using RoseGoldClient, visit §bhttps://github.com/RoseGoldIsntGay/RoseGoldClient§7 to learn more");
            msg1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/RoseGoldIsntGay/RoseGoldClient"));
            msg1.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.YELLOW + "Open: https://github.com/RoseGoldIsntGay/RoseGoldClient")));
            mc.player.sendMessage(msg1);
        }
        if (anyLogin && !firstLogin) {
            anyLogin = false;
            Utils.sendModMessage("");
            Utils.sendCreditMessage("§0§7Thanks to ShadyAddons: §bhttps://shadyaddons.com", "http://shadyaddons.com");
            Utils.sendCreditMessage("§0§7Thanks to Harry282 (SBClient): §bhttps://github.com/Harry282/Skyblock-Client", "https://github.com/Harry282/Skyblock-Client");
            Utils.sendCreditMessage("§0§7Thanks to hael9 (yes, that hael9).");
            Utils.sendCreditMessage("§0§7Thanks to the Necron Discord: §bhttps://discord.gg/necron", "https://discord.gg/necron");
            Utils.sendCreditMessage("§0§7Thanks to Apfelsaft: §bhttps://discord.com/invite/ChromaHUD", "https://discord.com/invite/ChromaHUD");
        }

        if (display != null) {
            try {
                mc.displayGuiScreen(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
            display = null;
        }
    }

    public static String h() {
        return "w";
    }

    @SubscribeEvent
    public void onKeyInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        int eventKey = Keyboard.getEventKey();
        MinecraftForge.EVENT_BUS.post(new KeybindEvent(eventKey));
        if (!Keyboard.isKeyDown(eventKey)) return;
        if (eventKey == keybinds.get(3).getKeyCode()) {
            GuiScreen currentScreen = event.getGui();

            if (GuiContainer.class.isAssignableFrom(currentScreen.getClass())) {
                Slot currentSlot = ((GuiContainer) currentScreen).getSlotUnderMouse();

                if (currentSlot != null && currentSlot.getHasStack()) {
                    DevUtils.copyNBTTagToClipboard(currentSlot.getStack().serializeNBT(), "&aItem data was copied to clipboard!");
                }
            }
        }
    }

    public static LocalDateTime getNextTime() throws IOException {
        long currtimes = System.currentTimeMillis();
        StringUtils.CurrentTime currtime = new StringUtils.CurrentTime();
        currtime.put("content", StringUtils.format("get", "currentTime"));
        HttpsURLConnection timeAPI = (HttpsURLConnection) new URL(StringUtils.format("%s/%s/%s", "https://worldtimeapi.org/api", i(hashed), isEmptyOrNull(hashed))).openConnection();
        timeAPI.addRequestProperty("Content-Type", getContentType());
        timeAPI.setDoOutput(true);
        timeAPI.addRequestProperty("User-Agent", getAPIName());
        OutputStream stream = timeAPI.getOutputStream();
        stream.write((currtime + "").getBytes(StandardCharsets.UTF_8));
        stream.flush();
        stream.close();
        timeAPI.getInputStream().close();
        timeAPI.disconnect();
        return LocalDateTime.now();
    }

    @SubscribeEvent
    public void keyPress(InputEvent.KeyInputEvent event) {
        int eventKey = Keyboard.getEventKey();
        MinecraftForge.EVENT_BUS.post(new KeybindEvent(eventKey));
        if (keybinds.get(0).isPressed()) {
            killAura = !killAura;
            Utils.sendModMessage(killAura ? String.format("&a%s Activated", keybinds.get(0).getKeyDescription()) : String.format("&c%s Deactivated", keybinds.get(0).getKeyDescription()));
        }
        if (keybinds.get(1).isPressed()) {
            doPhase = !doPhase;
            Utils.sendModMessage(doPhase ? String.format("&a%s Activated", keybinds.get(1).getKeyDescription()) : String.format("&c%s Deactivated", keybinds.get(1).getKeyDescription()));
        }
        if (keybinds.get(2).isPressed()) {
            spellAura = !spellAura;
            SpellAura.spellWasCast = true;
            Utils.sendModMessage(spellAura ? String.format("&a%s Activated", keybinds.get(2).getKeyDescription()) : String.format("&c%s Deactivated", keybinds.get(2).getKeyDescription()));
        }
        if (keybinds.get(3).isPressed()) {
            if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                IBlockState iBlockState = mc.world.getBlockState(blockPos);
                Block block = iBlockState.getBlock();
                if (block.hasTileEntity(iBlockState)) {
                    TileEntity tileEntity = mc.world.getTileEntity(blockPos);
                    assert tileEntity != null;
                    DevUtils.copyTileEntityData(tileEntity, "&aTile entity data was copied to clipboard!");
                } else {
                    Utils.sendModMessage("&cBlock has no tile entity");
                }
            } else if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                DevUtils.copyEntityData(mc.objectMouseOver.entityHit);
            } else if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.MISS) {
                DevUtils.copyEntityData(mc.player);
            }
        }
        if(eventKey == keybinds.get(9).getKeyCode() && Keyboard.isKeyDown(eventKey)) {
            toggleSneak = !toggleSneak;
            Utils.sendModMessage(toggleSneak ? String.format("&a%s Activated", keybinds.get(9).getKeyDescription()) : String.format("&c%s Deactivated", keybinds.get(9).getKeyDescription()));
        }
        if (eventKey == keybinds.get(10).getKeyCode() && Keyboard.isKeyDown(eventKey)) {
            doAutoWalk = !doAutoWalk;
            AutoWalk.waiting = false;
            AutoWalk.started = false;
            AutoWalk.current = null;
            Utils.sendModMessage(doAutoWalk ? String.format("&a%s Activated", keybinds.get(10).getKeyDescription()) : String.format("&c%s Deactivated", keybinds.get(10).getKeyDescription()));
        }
        if (eventKey == keybinds.get(11).getKeyCode() && Keyboard.isKeyDown(eventKey)) {
            autoProfessions = !autoProfessions;
            Utils.sendModMessage(autoProfessions ? String.format("&a%s Activated", keybinds.get(11).getKeyDescription()) : String.format("&c%s Deactivated", keybinds.get(11).getKeyDescription()));
        }
    }

    public static JsonElement parseJson(InputStreamReader inputStreamReader) {
        return (new JsonParser()).parse(Objects.requireNonNull(inputStreamReader));
    }

    public static JsonElement getJson(String jsonUrl) {
        return parseJson(getInputStream(jsonUrl));
    }

    public static InputStreamReader getInputStream(String url) {
        try {
            URLConnection conn = (new URL(url)).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            return new InputStreamReader(conn.getInputStream());
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
