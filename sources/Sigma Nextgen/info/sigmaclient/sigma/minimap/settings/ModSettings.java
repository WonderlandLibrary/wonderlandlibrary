package info.sigmaclient.sigma.minimap.settings;

import info.sigmaclient.sigma.minimap.*;
import info.sigmaclient.sigma.minimap.interfaces.*;
import info.sigmaclient.sigma.minimap.minimap.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class ModSettings
{
    public static final int defaultSettings = Integer.MAX_VALUE;
    public static int ignoreUpdate;
    public static final String format = "�";
    private static boolean keysLoaded;
    public static final String[] ENCHANT_COLORS;
    public static final String[] ENCHANT_COLOR_NAMES;
    public static final int[] COLORS;
    public static final String[] MINIMAP_SIZE;
    public static int serverSettings;
//    public static KeyBinding keyBindSettings;
//    public static KeyBinding keyBindZoom;
//    public static KeyBinding keyBindZoom1;
//    public static KeyBinding newWaypoint;
//    public static KeyBinding keyWaypoints;
//    public static KeyBinding keyLargeMap;
//    public static KeyBinding keyToggleMap;
//    public static KeyBinding keyToggleWaypoints;
//    public static KeyBinding keyToggleSlimes;
//    public static KeyBinding keyToggleGrid;
//    public static KeyBinding keyInstantWaypoint;
    private boolean minimap;
    public int zoom;
    public float[] zooms;
    public int entityAmount;
    private boolean showPlayers;
    private boolean showMobs;
    private boolean showHostile;
    private boolean showItems;
    private boolean showOther;
    private boolean caveMaps;
    private boolean showOtherTeam;
    private boolean showWaypoints;
    private boolean deathpoints;
    private boolean oldDeathpoints;
    public int chunkGrid;
    private boolean slimeChunks;
    private static HashMap<String, Long> serverSlimeSeeds;
    private boolean showIngameWaypoints;
    private boolean showCoords;
    private boolean lockNorth;
    private boolean antiAliasing;
    public boolean displayRedstone;
    public boolean mapSafeMode;
    public int distance;
    public static final String[] distanceTypes;
    public int blockColours;
    public static final String[] blockColourTypes;
    public boolean lighting;
    public boolean compassOverWaypoints;
    private int mapSize;
    public int playersColor;
    public int mobsColor;
    public int hostileColor;
    public int itemsColor;
    public int otherColor;
    public int otherTeamColor;
    public float minimapOpacity;
    public float waypointsScale;
    public float dotsScale;
    public static boolean settingsButton;
    public boolean showBiome;
    public static boolean updateNotification;
    public boolean showEntityHeight;
    public boolean showFlowers;
    public boolean keepWaypointNames;
    public float waypointsDistance;
    public float waypointsDistanceMin;
    public String waypointTp;
    public float arrowScale;
    public int arrowColour;
    public String[] arrowColourNames;
    public float[][] arrowColours;
    public boolean smoothDots;
    
    public ModSettings() {
        this.minimap = true;
        this.zoom = 2;
        this.zooms = new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f };
        this.entityAmount = 1;
        this.showPlayers = true;
        this.showMobs = true;
        this.showHostile = true;
        this.showItems = true;
        this.showOther = true;
        this.caveMaps = true;
        this.showOtherTeam = true;
        this.showWaypoints = true;
        this.deathpoints = true;
        this.oldDeathpoints = true;
        this.chunkGrid = -5;
        this.slimeChunks = false;
        this.showIngameWaypoints = true;
        this.showCoords = true;
        this.lockNorth = false;
        this.antiAliasing = true;
        this.displayRedstone = true;
        this.mapSafeMode = false;
        this.distance = 1;
        this.blockColours = 0;
        this.lighting = true;
        this.compassOverWaypoints = false;
        this.mapSize = -1;
        this.playersColor = 15;
        this.mobsColor = 14;
        this.hostileColor = 14;
        this.itemsColor = 12;
        this.otherColor = 5;
        this.otherTeamColor = -1;
        this.minimapOpacity = 100.0f;
        this.waypointsScale = 1.0f;
        this.dotsScale = 1.0f;
        this.showBiome = false;
        this.showEntityHeight = true;
        this.showFlowers = true;
        this.keepWaypointNames = false;
        this.waypointsDistance = 0.0f;
        this.waypointsDistanceMin = 0.0f;
        this.waypointTp = "tp";
        this.arrowScale = 1.5f;
        this.arrowColour = 0;
        this.arrowColourNames = new String[] { "gui.xaero_red", "gui.xaero_green", "gui.xaero_blue", "gui.xaero_yellow", "gui.xaero_purple", "gui.xaero_white", "gui.xaero_black", "gui.xaero_preset_classic" };
        this.arrowColours = new float[][] { { 0.8f, 0.1f, 0.1f, 1.0f }, { 0.09f, 0.57f, 0.0f, 1.0f }, { 0.0f, 0.55f, 1.0f, 1.0f }, { 1.0f, 0.93f, 0.0f, 1.0f }, { 0.73f, 0.33f, 0.83f, 1.0f }, { 1.0f, 1.0f, 1.0f, 1.0f }, { 0.0f, 0.0f, 0.0f, 1.0f }, { 0.4588f, 0.0f, 0.0f, 1.0f } };
        this.smoothDots = true;
//        if (!ModSettings.keysLoaded) {
//            XaeroMinimap.ch = new ControlsHandler();
            ModSettings.keysLoaded = true;
//        }
    }
    
//    public static boolean isKeyRepeat(final KeyBinding kb) {
//        return kb != ModSettings.keyWaypoints && kb != ModSettings.keyBindSettings && kb != ModSettings.newWaypoint && kb != ModSettings.keyLargeMap && kb != ModSettings.keyToggleMap && kb != ModSettings.keyToggleWaypoints && kb != ModSettings.keyToggleSlimes && kb != ModSettings.keyToggleGrid && kb != ModSettings.keyInstantWaypoint;
//    }
    
    public boolean getMinimap() {
        return this.minimap && !this.minimapDisabled();
    }
    
    public boolean getShowPlayers() {
        return this.showPlayers && !this.minimapDisplayPlayersDisabled();
    }
    
    public boolean getShowMobs() {
        return this.showMobs && !this.minimapDisplayMobsDisabled();
    }
    
    public boolean getShowHostile() {
        return this.showHostile && !this.minimapDisplayMobsDisabled();
    }
    
    public boolean getShowItems() {
        return this.showItems && !this.minimapDisplayItemsDisabled();
    }
    
    public boolean getShowOther() {
        return this.showOther && !this.minimapDisplayOtherDisabled();
    }
    
    public boolean getCaveMaps() {
        return this.caveMaps && !this.caveMapsDisabled();
    }
    
    public boolean getShowOtherTeam() {
        return this.showOtherTeam && !this.showOtherTeamDisabled();
    }
    
    public boolean getShowWaypoints() {
        return this.showWaypoints && !this.showWaypointsDisabled();
    }
    
    public boolean getDeathpoints() {
        return this.deathpoints && !this.deathpointsDisabled();
    }
    
    public boolean getOldDeathpoints() {
        return this.oldDeathpoints;
    }
    
    public void setSlimeChunksSeed(final long seed) {
        ModSettings.serverSlimeSeeds.put(Minimap.getCurrentWorldID(), seed);
    }
    
    public long getSlimeChunksSeed() {
        final MinecraftServer sp = (MinecraftServer)Minecraft.getInstance().getIntegratedServer();
        if (sp == null) {
            final Long saved = ModSettings.serverSlimeSeeds.get(Minimap.getCurrentWorldID());
            return (saved != null) ? saved : 0L;
        }else{
            return 0L;
        }
    }

    public boolean getSlimeChunks() {
        return this.slimeChunks && (Minecraft.getInstance().getIntegratedServer() != null || this.getSlimeChunksSeed() != 0L);
    }
    
    public boolean getShowIngameWaypoints() {
        return this.showIngameWaypoints && !this.showWaypointsDisabled();
    }
    
    public boolean getShowCoords() {
        return this.showCoords;
    }
    
    public boolean getLockNorth() {
        return this.lockNorth || Minimap.enlargedMap;
    }
    
    public boolean getAntiAliasing() {
        return !Minimap.triedFBO || (this.antiAliasing && Minimap.usingFBO());
    }
    
    public int getMinimapSize() {
        if (this.mapSize > -1) {
            return this.mapSize;
        }
        return 2;
    }
    
    public boolean getSmoothDots() {
        return !Minimap.triedFBO || (this.smoothDots && Minimap.usingFBO());
    }
    
    public void saveSettings() throws IOException {
        final PrintWriter writer = new PrintWriter(new FileWriter(XaeroMinimap.optionsFile));
        writer.println("ignoreUpdate:" + ModSettings.ignoreUpdate);
        writer.println("updateNotification:" + ModSettings.updateNotification);
        writer.println("settingsButton:" + ModSettings.settingsButton);
        writer.println("minimap:" + this.minimap);
        writer.println("caveMaps:" + this.caveMaps);
        writer.println("showPlayers:" + this.showPlayers);
        writer.println("showHostile:" + this.showHostile);
        writer.println("showMobs:" + this.showMobs);
        writer.println("showItems:" + this.showItems);
        writer.println("showOther:" + this.showOther);
        writer.println("showOtherTeam:" + this.showOtherTeam);
        writer.println("showWaypoints:" + this.showWaypoints);
        writer.println("showIngameWaypoints:" + this.showIngameWaypoints);
        writer.println("displayRedstone:" + this.displayRedstone);
        writer.println("deathpoints:" + this.deathpoints);
        writer.println("oldDeathpoints:" + this.oldDeathpoints);
        writer.println("distance:" + this.distance);
        writer.println("showCoords:" + this.showCoords);
        writer.println("lockNorth:" + this.lockNorth);
        writer.println("zoom:" + this.zoom);
        writer.println("mapSize:" + this.mapSize);
        writer.println("entityAmount:" + this.entityAmount);
        writer.println("chunkGrid:" + this.chunkGrid);
        writer.println("slimeChunks:" + this.slimeChunks);
        writer.println("playersColor:" + this.playersColor);
        writer.println("mobsColor:" + this.mobsColor);
        writer.println("hostileColor:" + this.hostileColor);
        writer.println("itemsColor:" + this.itemsColor);
        writer.println("otherColor:" + this.otherColor);
        writer.println("otherTeamColor:" + this.otherTeamColor);
        writer.println("mapSafeMode:" + this.mapSafeMode);
        writer.println("minimapOpacity:" + this.minimapOpacity);
        writer.println("waypointsScale:" + this.waypointsScale);
        writer.println("antiAliasing:" + this.antiAliasing);
        writer.println("blockColours:" + this.blockColours);
        writer.println("lighting:" + this.lighting);
        writer.println("dotsScale:" + this.waypointsScale);
        writer.println("compassOverWaypoints:" + this.compassOverWaypoints);
        writer.println("showBiome:" + this.showBiome);
        writer.println("showEntityHeight:" + this.showEntityHeight);
        writer.println("showFlowers:" + this.showFlowers);
        writer.println("keepWaypointNames:" + this.keepWaypointNames);
        writer.println("waypointsDistance:" + this.waypointsDistance);
        writer.println("waypointsDistanceMin:" + this.waypointsDistanceMin);
        writer.println("waypointTp:" + this.waypointTp);
        writer.println("arrowScale:" + this.arrowScale);
        writer.println("arrowColour:" + this.arrowColour);
        writer.println("smoothDots:" + this.smoothDots);
        final Object[] keys = ModSettings.serverSlimeSeeds.keySet().toArray();
        final Object[] values = ModSettings.serverSlimeSeeds.values().toArray();
        for (int i = 0; i < keys.length; ++i) {
            writer.println("seed:" + keys[i] + ":" + values[i]);
        }
        for (final Interface l : InterfaceHandler.list) {
            writer.println("interface:" + l.iname + ":" + l.actualx + ":" + l.actualy + ":" + l.centered + ":" + l.flipped + ":" + l.fromRight);
        }
        writer.println("#WAYPOINTS HAVE BEEN MOVED TO xaerowaypoints.txt!");
        writer.close();
       // this.saveWaypoints();
    }

    public String getKeyBinding(final ModOptions par1EnumOptions) {
        String s = par1EnumOptions.getEnumString() + ": ";
        if (par1EnumOptions == ModOptions.DOTS_SCALE && !Minimap.usingFBO()) {
            return s + "�e" + getTranslation(false);
        }
        if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE) {
            if (this.waypointsDistance == 0.0f) {
                s += I18n.format("gui.xaero_unlimited", new Object[0]);
            }
            else {
                s = s + (int)this.waypointsDistance + "m";
            }
        }
        else if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE_MIN) {
            if (this.waypointsDistanceMin == 0.0f) {
                s += I18n.format("gui.xaero_off", new Object[0]);
            }
            else {
                s = s + (int)this.waypointsDistanceMin + "m";
            }
        }
        else {
            if (par1EnumOptions.getEnumFloat()) {
                String f1 = "" + this.getOptionFloatValue(par1EnumOptions);
                if (par1EnumOptions == ModOptions.ARROW_SCALE) {
                    f1 += "x";
                }
                return s + f1;
            }
            if (par1EnumOptions == ModOptions.CHUNK_GRID) {
                s += ((this.chunkGrid > -1) ? ("�" + ModSettings.ENCHANT_COLORS[this.chunkGrid] + I18n.format(ModSettings.ENCHANT_COLOR_NAMES[this.chunkGrid], new Object[0])) : I18n.format("gui.xaero_off", new Object[0]));
            }
            else if (par1EnumOptions == ModOptions.EDIT) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.DOTS) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.RESET) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.WAYPOINTS_TP) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.ZOOM) {
                s = s + this.zooms[this.zoom] + "x";
            }
            else if (par1EnumOptions == ModOptions.COLOURS) {
                s += I18n.format(ModSettings.blockColourTypes[this.blockColours], new Object[0]);
            }
            else if (par1EnumOptions == ModOptions.DISTANCE) {
                s += I18n.format(ModSettings.distanceTypes[this.distance], new Object[0]);
            }
           /* else if (par1EnumOptions == ModOptions.SLIME_CHUNKS && this.customSlimeSeedNeeded()) {
                s = par1EnumOptions.getEnumString();
            }*/
            else if (par1EnumOptions == ModOptions.SIZE) {
                s += I18n.format((this.mapSize > -1) ? ModSettings.MINIMAP_SIZE[this.mapSize] : "gui.xaero_auto_map_size", new Object[0]);
            }
            else if (par1EnumOptions == ModOptions.EAMOUNT) {
                s = ((this.entityAmount == 0) ? (s + I18n.format("gui.xaero_unlimited", new Object[0])) : (s + 100 * this.entityAmount));
            }
            else if (par1EnumOptions == ModOptions.ARROW_COLOUR) {
                s += I18n.format(this.arrowColourNames[this.arrowColour], new Object[0]);
            }
            else {
                final boolean clientSetting = this.getClientBooleanValue(par1EnumOptions);
                final boolean serverSetting = this.getBooleanValue(par1EnumOptions);
                s = s + getTranslation(clientSetting) + ((serverSetting != clientSetting) ? ("�e (" + getTranslation(serverSetting) + ")") : "");
            }
        }
        return s;
    }
    
    public boolean getBooleanValue(final ModOptions o) {
        if (o == ModOptions.MINIMAP) {
            return this.getMinimap();
        }
        if (o == ModOptions.CAVE_MAPS) {
            return this.getCaveMaps();
        }
        if (o == ModOptions.DISPLAY_OTHER_TEAM) {
            return this.getShowOtherTeam();
        }
        if (o == ModOptions.WAYPOINTS) {
            return this.getShowWaypoints();
        }
        if (o == ModOptions.DEATHPOINTS) {
            return this.getDeathpoints();
        }
        if (o == ModOptions.OLD_DEATHPOINTS) {
            return this.getOldDeathpoints();
        }
        if (o == ModOptions.INGAME_WAYPOINTS) {
            return this.getShowIngameWaypoints();
        }
        if (o == ModOptions.COORDS) {
            return this.getShowCoords();
        }
        if (o == ModOptions.NORTH) {
            return this.getLockNorth();
        }
        if (o == ModOptions.PLAYERS) {
            return this.getShowPlayers();
        }
        if (o == ModOptions.HOSTILE) {
            return this.getShowHostile();
        }
        if (o == ModOptions.MOBS) {
            return this.getShowMobs();
        }
        if (o == ModOptions.ITEMS) {
            return this.getShowItems();
        }
        if (o == ModOptions.ENTITIES) {
            return this.getShowOther();
        }
        if (o == ModOptions.SAFE_MAP) {
            return Minimap.triedFBO && !Minimap.usingFBO();
        }
        if (o == ModOptions.AA) {
            return this.getAntiAliasing();
        }
        if (o == ModOptions.SMOOTH_DOTS) {
            return this.getSmoothDots();
        }
        return this.getClientBooleanValue(o);
    }

    public boolean getClientBooleanValue(final ModOptions o) {
        if (o == ModOptions.MINIMAP) {
            return this.minimap;
        }
        if (o == ModOptions.CAVE_MAPS) {
            return this.caveMaps;
        }
        if (o == ModOptions.DISPLAY_OTHER_TEAM) {
            return this.showOtherTeam;
        }
        if (o == ModOptions.WAYPOINTS) {
            return this.showWaypoints;
        }
        if (o == ModOptions.DEATHPOINTS) {
            return this.deathpoints;
        }
        if (o == ModOptions.OLD_DEATHPOINTS) {
            return this.oldDeathpoints;
        }
        if (o == ModOptions.INGAME_WAYPOINTS) {
            return this.showIngameWaypoints;
        }
        if (o == ModOptions.REDSTONE) {
            return this.displayRedstone;
        }
        if (o == ModOptions.COORDS) {
            return this.showCoords;
        }
        if (o == ModOptions.NORTH) {
            return this.lockNorth;
        }
        if (o == ModOptions.PLAYERS) {
            return this.showPlayers;
        }
        if (o == ModOptions.HOSTILE) {
            return this.showHostile;
        }
        if (o == ModOptions.MOBS) {
            return this.showMobs;
        }
        if (o == ModOptions.ITEMS) {
            return this.showItems;
        }
        if (o == ModOptions.ENTITIES) {
            return this.showOther;
        }
        if (o == ModOptions.SLIME_CHUNKS) {
            return this.slimeChunks;
        }
        if (o == ModOptions.SAFE_MAP) {
            return this.mapSafeMode;
        }
        if (o == ModOptions.AA) {
            return this.antiAliasing;
        }
        if (o == ModOptions.LIGHT) {
            return this.lighting;
        }
        if (o == ModOptions.COMPASS) {
            return this.compassOverWaypoints;
        }
        if (o == ModOptions.BIOME) {
            return this.showBiome;
        }
        if (o == ModOptions.ENTITY_HEIGHT) {
            return this.showEntityHeight;
        }
        if (o == ModOptions.FLOWERS) {
            return this.showFlowers;
        }
        if (o == ModOptions.KEEP_WP_NAMES) {
            return this.keepWaypointNames;
        }
        return o == ModOptions.SMOOTH_DOTS && this.smoothDots;
    }
    
    private static String getTranslation(final boolean o) {
        return I18n.format("gui.xaero_" + (o ? "on" : "off"), new Object[0]);
    }

    public float getOptionFloatValue(final ModOptions options) {
        if (options == ModOptions.OPACITY) {
            return this.minimapOpacity;
        }
        if (options == ModOptions.WAYPOINTS_SCALE) {
            return this.waypointsScale;
        }
        if (options == ModOptions.DOTS_SCALE) {
            return this.dotsScale;
        }
        if (options == ModOptions.WAYPOINTS_DISTANCE) {
            return this.waypointsDistance;
        }
        if (options == ModOptions.WAYPOINTS_DISTANCE_MIN) {
            return this.waypointsDistanceMin;
        }
        if (options == ModOptions.ARROW_SCALE) {
            return this.arrowScale;
        }
        return 1.0f;
    }
    
    public boolean minimapDisabled() {
        return (ModSettings.serverSettings & 0x1) != 0x1;
    }
    
    public boolean minimapDisplayPlayersDisabled() {
        return (ModSettings.serverSettings & 0x400) != 0x400;
    }
    
    public boolean minimapDisplayMobsDisabled() {
        return (ModSettings.serverSettings & 0x800) != 0x800;
    }
    
    public boolean minimapDisplayItemsDisabled() {
        return (ModSettings.serverSettings & 0x1000) != 0x1000;
    }
    
    public boolean minimapDisplayOtherDisabled() {
        return (ModSettings.serverSettings & 0x2000) != 0x2000;
    }
    
    public boolean caveMapsDisabled() {
        return (ModSettings.serverSettings & 0x4000) != 0x4000;
    }
    
    public boolean showOtherTeamDisabled() {
        return (ModSettings.serverSettings & 0x8000) != 0x8000;
    }
    
    public boolean showWaypointsDisabled() {
        return (ModSettings.serverSettings & 0x10000) != 0x10000;
    }
    
    public boolean deathpointsDisabled() {
        return (ModSettings.serverSettings & 0x200000) == 0x0;
    }
    
    public void resetServerSettings() {
        ModSettings.serverSettings = Integer.MAX_VALUE;
    }
    
    public static void setServerSettings() {
    }
    
    static {
        ModSettings.keysLoaded = false;
        ENCHANT_COLORS = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        ENCHANT_COLOR_NAMES = new String[] { "gui.xaero_black", "gui.xaero_dark_blue", "gui.xaero_dark_green", "gui.xaero_dark_aqua", "gui.xaero_dark_red", "gui.xaero_dark_purple", "gui.xaero_gold", "gui.xaero_gray", "gui.xaero_dark_gray", "gui.xaero_blue", "gui.xaero_green", "gui.xaero_aqua", "gui.xaero_red", "gui.xaero_purple", "gui.xaero_yellow", "gui.xaero_white" };
        COLORS = new int[] { new Color(0, 0, 0, 255).hashCode(), new Color(0, 0, 170, 255).hashCode(), new Color(0, 170, 0, 255).hashCode(), new Color(0, 170, 170, 255).hashCode(), new Color(170, 0, 0, 255).hashCode(), new Color(170, 0, 170, 255).hashCode(), new Color(255, 170, 0, 255).hashCode(), new Color(170, 170, 170, 255).hashCode(), new Color(85, 85, 85, 255).hashCode(), new Color(85, 85, 255, 255).hashCode(), new Color(85, 255, 85, 255).hashCode(), new Color(85, 255, 255, 255).hashCode(), new Color(255, 0, 0, 255).hashCode(), new Color(255, 85, 255, 255).hashCode(), new Color(255, 255, 85, 255).hashCode(), new Color(255, 255, 255, 255).hashCode() };
        MINIMAP_SIZE = new String[] { "gui.xaero_tiny", "gui.xaero_small", "gui.xaero_medium", "gui.xaero_large" };
        ModSettings.serverSettings = Integer.MAX_VALUE;
//        ModSettings.keyBindSettings = new KeyBinding("gui.xaero_minimap_settings", 21, "Xaero's Minimap");
//        ModSettings.keyBindZoom = new KeyBinding("gui.xaero_zoom_in", 23, "Xaero's Minimap");
//        ModSettings.keyBindZoom1 = new KeyBinding("gui.xaero_zoom_out", 24, "Xaero's Minimap");
//        ModSettings.newWaypoint = new KeyBinding("gui.xaero_new_waypoint", 48, "Xaero's Minimap");
//        ModSettings.keyWaypoints = new KeyBinding("gui.xaero_waypoints_key", 22, "Xaero's Minimap");
//        ModSettings.keyLargeMap = new KeyBinding("gui.xaero_enlarge_map", 44, "Xaero's Minimap");
//        ModSettings.keyToggleMap = new KeyBinding("gui.xaero_toggle_map", 35, "Xaero's Minimap");
//        ModSettings.keyToggleWaypoints = new KeyBinding("gui.xaero_toggle_waypoints", 40, "Xaero's Minimap");
//        ModSettings.keyToggleSlimes = new KeyBinding("gui.xaero_toggle_slime", 0, "Xaero's Minimap");
//        ModSettings.keyToggleGrid = new KeyBinding("gui.xaero_toggle_grid", 0, "Xaero's Minimap");
//        ModSettings.keyInstantWaypoint = new KeyBinding("gui.xaero_instant_waypoint", 78, "Xaero's Minimap");
        ModSettings.serverSlimeSeeds = new HashMap<String, Long>();
        distanceTypes = new String[] { "gui.xaero_off", "gui.xaero_looking_at", "gui.xaero_all" };
        blockColourTypes = new String[] { "gui.xaero_accurate", "gui.xaero_vanilla" };
        ModSettings.settingsButton = true;
        ModSettings.updateNotification = true;
    }
}
