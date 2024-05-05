package rosegoldclient;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Comparator;

public class Config extends Vigilant {

    public static Config INSTANCE = new Config();

    /*
     * RoseGoldClient
     */

    @Property(type = PropertyType.SWITCH, name = "Watermark",
            category = "RoseGoldClient")
    public boolean watermark = true;

    @Property(type = PropertyType.SWITCH, name = "Silent Mode", description = "Silence all ESPs and mod messages",
            category = "RoseGoldClient")
    public boolean silentMode = false;

    @Property(type = PropertyType.SWITCH, name = "Anonymize", description = "Keep yourself safe",
            category = "RoseGoldClient")
    public boolean anon = false;

    @Property(type = PropertyType.SWITCH, name = "Randomize Text", description = "Randomize text instead of yeeting it",
            category = "RoseGoldClient")
    public boolean randomizeAnon = false;

    /*
     * Macros
     */

    @Property(type = PropertyType.SWITCH, name = "Auto Professions", description = "Automatically grind professions",
            category = "Macros", subcategory = "Auto Professions")
    public boolean autoProfessions = false;

    @Property(type = PropertyType.SELECTOR, name = "Auto Professions Type",
            category = "Macros", subcategory = "Auto Professions", options = {"Aura", "Pathfind"})
    public int autoProfessionsType = 0;

    @Property(type = PropertyType.SELECTOR, name = "Auto Professions Gather Type",
            category = "Macros", subcategory = "Auto Professions", options = {"Right Click", "Left Click"})
    public int autoProfessionsGatherType = 0;

    @Property(type = PropertyType.SELECTOR, name = "Auto Professions Skill Type",
            category = "Macros", subcategory = "Auto Professions", options = {"Woodcutting", "Mining", "Fishing", "Farming"})
    public int autoProfessionsSkillType = 0;

    @Property(type = PropertyType.SLIDER, name = "Auto Professions Node Regrow Time", description = "Set time in seconds",
            category = "Macros", subcategory = "Auto Professions", min = 1, max = 120)
    public int autoProfessionsNodeRegrowTime = 60;

    @Property(type = PropertyType.SLIDER, name = "Auto Professions Max Pathfind Distance", description = "Don't pathfind to nodes that are too far",
            category = "Macros", subcategory = "Auto Professions", min = 50, max = 150)
    public int autoProfessionsNodeMaxDistance = 100;

    @Property(type = PropertyType.SWITCH, name = "Auto Clicker", description = "Set keybind under controls",
            category = "Macros", subcategory = "Auto Clicker")
    public boolean autoClicker = false;

    @Property(type = PropertyType.SELECTOR, name = "Auto Click Type",
            category = "Macros", subcategory = "Auto Clicker", options = {"Right Click", "Left Click"})
    public int autoClickerMode = 0;

    @Property(type = PropertyType.SLIDER, name = "Auto Click Delay", description = "Set delay in milliseconds",
            category = "Macros", subcategory = "Auto Clicker", min = 1, max = 1000)
    public int autoClickerDelay = 500;

    /*
     * Loot Running
     */

    @Property(type = PropertyType.SWITCH, name = "Chest Looter", description = "Automatically loot items from chests",
            category = "Loot Running", subcategory = "Chest Looter")
    public boolean chestLoot = false;

    @Property(type = PropertyType.SLIDER, name = "Chest Looter Delay", description = "in milliseconds",
            category = "Loot Running", subcategory = "Chest Looter", max = 500)
    public int chestLootDelay = 100;

    @Property(type = PropertyType.SWITCH, name = "Enable Chest Looter Filter",
            category = "Loot Running", subcategory = "Chest Looter")
    public boolean doChestLootFilter = false;

    @Property(type = PropertyType.PARAGRAPH, name = "Chest Looter Filter", description = "Separate with commas",
            category = "Loot Running", subcategory = "Chest Looter")
    public String chestLootFilter = "";

    @Property(type = PropertyType.SWITCH, name = "Chest Aura", description = "Open loot chests in range of the player",
            category = "Loot Running", subcategory = "Chest Aura")
    public boolean chestAura = false;

        @Property(type = PropertyType.SWITCH, name = "Only Wynncraft Chests", description = "Only open confirmed Wynncraft chests",
                category = "Loot Running", subcategory = "Chest Aura")
        public boolean onlyConfirmedChests = false;

        @Property(type = PropertyType.SLIDER, name = "Chest Aura Range",
                category = "Loot Running", subcategory = "Chest Aura", min = 1, max = 6)
        public int chestAuraRange = 5;

        @Property(type = PropertyType.SELECTOR, name = "Distance Check Mode",
                category = "Loot Running", subcategory = "Chest Aura", options = {"Distance Calculation", "Coordinate Check"})
        public int chestAuraRangeType = 0;

        @Property(type = PropertyType.SWITCH, name = "Chest ESP", description = "Highlight chests",
                category = "Loot Running", subcategory = "Chest Aura")
        public boolean chestESP = false;

        @Property(type = PropertyType.SLIDER, name = "Chest ESP Range",
                category = "Loot Running", subcategory = "Chest Aura", min = 10, max = 100)
        public int chestESPRange = 20;

    @Property(type = PropertyType.SWITCH, name = "Wynncraft Chest ESP", description = "Highlight confirmed Wynncraft chests",
            category = "Loot Running", subcategory = "Chest ESP")
    public boolean wynnChestESP = false;

        @Property(type = PropertyType.SLIDER, name = "Wynncraft Chest ESP Range",
                category = "Loot Running", subcategory = "Chest ESP", max = 200)
        public int wynnChestESPRange = 50;

    /*
     * Render
     */

    @Property(type = PropertyType.SWITCH, name = "Show Server Side Rotations",
            category = "Render", subcategory = "Animations")
    public boolean showServerSideRotations = false;

    @Property(type = PropertyType.SLIDER, name = "Entity ESP Outline Alpha", description = "Set outline transparency for entity outline ESPs",
            category = "Render", subcategory = "ESP Settings", max = 255)
    public int espEntityOutlineAlpha = 200;

    @Property(type = PropertyType.SLIDER, name = "Entity ESP Box Alpha", description = "Set outline transparency for entity box ESPs",
            category = "Render", subcategory = "ESP Settings", max = 255)
    public int espEntityBoxAlpha = 100;

    @Property(type = PropertyType.SLIDER, name = "Block ESP Outline Alpha", description = "Set outline transparency for block outline ESPs",
            category = "Render", subcategory = "ESP Settings", max = 255)
    public int espBlockOutlineAlpha = 200;

    @Property(type = PropertyType.SLIDER, name = "Block ESP Box Alpha", description = "Set outline transparency for block box ESPs",
            category = "Render", subcategory = "ESP Settings", max = 255)
    public int espBlockBoxAlpha = 0;

    @Property(type = PropertyType.SLIDER, name = "Distance Accuracy", description = "How many decimals points to show when displaying distance",
            category = "Render", subcategory = "ESP Settings", max = 4)
    public int nametagDistanceDecimalPoints = 0;

    @Property(type = PropertyType.SWITCH, name = "Rare Mob ESP",
            category = "Render", subcategory = "Rare Mob ESP")
    public boolean rareMobESP = false;

    @Property(type = PropertyType.SWITCH, name = "Notify Rare Mob", description = "Dingdingdingdingdingding",
            category = "Render", subcategory = "Rare Mob ESP")
    public boolean notifyRareMobESP = false;

    @Property(type = PropertyType.PARAGRAPH, name = "Rare Mob Filter", description = "Separate with commas",
            category = "Render", subcategory = "Rare Mob ESP")
    public String rareMobESPFilter = "";

    @Property(type = PropertyType.SWITCH, name = "Entity ESP",
            category = "Render", subcategory = "Entity ESP")
    public boolean entityESP = false;

        @Property(type = PropertyType.SLIDER, name = "Entity ESP Range", description = "0 = unlimited",
                category = "Render", subcategory = "Entity ESP", max = 64)
        public int entityESPRange = 0;

        @Property(type = PropertyType.SWITCH, name = "Reveal Invisible Entities", description = "",
                category = "Render", subcategory = "Entity ESP")
        public boolean revealInsivibleEntities = false;

    @Property(type = PropertyType.SWITCH, name = "NPC ESP",
            category = "Render", subcategory = "NPC ESP")
    public boolean NPCESP = false;

    @Property(type = PropertyType.SWITCH, name = "Dropped Item ESP", description = "Highlight dropped items considered rare",
            category = "Render", subcategory = "Dropped Item ESP")
    public boolean droppedItemESP = false;

        @Property(type = PropertyType.SWITCH, name = "Highlight Legendaries",
                category = "Render", subcategory = "Dropped Item ESP")
        public boolean droppedItemESPLegendaries = true;

        @Property(type = PropertyType.SWITCH, name = "Highlight Fableds",
                category = "Render", subcategory = "Dropped Item ESP")
        public boolean droppedItemESPFableds = true;

        @Property(type = PropertyType.SWITCH, name = "Highlight Mythics", description = "Includes on-screen message and loud notification sound",
                category = "Render", subcategory = "Dropped Item ESP")
        public boolean droppedItemESPMythics = true;

    @Property(type = PropertyType.SWITCH, name = "Antiblind", description = "Remove blindness",
            category = "Render", subcategory = "Antiblind")
    public boolean antiblind = false;

    /*
     * Movement
     */

    @Property(type = PropertyType.SWITCH, name = "Auto Walk", description = "Auto Walk to points set with /walkpoint, toggle in controls",
            category = "Movement", subcategory = "Auto Walk")
    public boolean autoWalk = false;

    @Property(type = PropertyType.SLIDER, name = "Auto Walk Wait Modifier", description = "Modifier to multiply all wait times, for easy point modification",
            category = "Movement", subcategory = "Auto Walk", max = 3)
    public int autoWalkWaitModifier = 1;

    @Property(type = PropertyType.SWITCH, name = "Pathfind goto walk", description = "Change goto command to walk instead of teleport",
            category = "Movement", subcategory = "Pathfinding")
    public boolean pathfindingGotoWalk = false;

    @Property(type = PropertyType.SLIDER, name = "Pathfinding Unstuck time", description = "",
            category = "Movement", subcategory = "Pathfinding", min = 1, max = 10)
    public int pathfindingUnstuckTime = 3;

    @Property(type = PropertyType.SLIDER, name = "Pathfinding Look time", description = "",
            category = "Movement", subcategory = "Pathfinding", max = 1000)
    public int pathfindingLookTime = 150;

    @Property(type = PropertyType.SWITCH, name = "Phase", description = "Phase through blocks (use movement abilities)",
            category = "Movement", subcategory = "Phase")
    public boolean phase = false;

        @Property(type = PropertyType.SWITCH, name = "Only with keybind", description = "Set keybind under controls",
                category = "Movement", subcategory = "Phase")
        public boolean phaseWithKeybind = false;

        @Property(type = PropertyType.SWITCH, name = "Descent while sneak held",
                category = "Movement", subcategory = "Phase")
        public boolean sneakHeldDescent = false;

    @Property(type = PropertyType.SWITCH, name = "No Rotate",
            category = "Movement", subcategory = "No Rotate")
    public boolean noRotate = false;

    @Property(type = PropertyType.SLIDER, name = "X Velocity Modifier", description = "Multiply x axis velocity",
            category = "Movement", subcategory = "Velocity", min = -10, max = 10)
    public int velocityX = 1;

    @Property(type = PropertyType.SLIDER, name = "Y Velocity Modifier", description = "Multiply y axis velocity",
            category = "Movement", subcategory = "Velocity", min = -10, max = 10)
    public int velocityY = 1;

    @Property(type = PropertyType.SLIDER, name = "Z Velocity Modifier", description = "Multiply z axis velocity",
            category = "Movement", subcategory = "Velocity", min = -10, max = 10)
    public int velocityZ = 1;

    @Property(type = PropertyType.SWITCH, name = "Inventory Walk", description = "Walk while inside inventories",
            category = "Movement", subcategory = "Inventory Walk")
    public boolean invWalk = false;

    @Property(type = PropertyType.SWITCH, name = "No Fall", description = "Cancel all fall damage",
            category = "Movement", subcategory = "No Fall")
    public boolean noFall = false;

    @Property(type = PropertyType.SWITCH, name = "Cursor Teleport", description = "Hypixel Skyblock AOTV, Shift to teleport onto the block, into otherwise",
            category = "Movement", subcategory = "Cursor Teleport")
    public boolean cursorTeleport = false;

        @Property(type = PropertyType.SLIDER, name = "Cursor Teleport Range",
                category = "Movement", subcategory = "Cursor Teleport", max = 100)
        public int cursorTeleportRange = 60;

        @Property(type = PropertyType.SWITCH, name = "Show distance", description = "Show block distance",
                category = "Movement", subcategory = "Cursor Teleport")
        public boolean cursorTeleportShowDistance = false;

        @Property(type = PropertyType.SWITCH, name = "Cancel Right Click", description = "Cancel weapon uses",
                category = "Movement", subcategory = "Cursor Teleport")
        public boolean cursorTeleportCancelClick = false;

        @Property(type = PropertyType.SELECTOR, name = "Held item type", description = "Select which items to hold to teleport",
                category = "Movement", subcategory = "Cursor Teleport", options = {"Non Weapon", "Empty Hand", "Any"})
        public int cursorTeleportEmptyHand = 0;

        @Property(type = PropertyType.SELECTOR, name = "Reset Velocity", description = "Select what velocity type to reset after teleportation",
                category = "Movement", subcategory = "Cursor Teleport", options = {"Neither", "Horizontal", "Vertical", "Both"})
        public int cursorTeleportResetVelocity = 0;

    /*
     * Combat
     */

    @Property(type = PropertyType.SWITCH, name = "Auto Heal", description = "Automatically heal using potions or spells",
            category = "Combat", subcategory = "Auto Heal")
    public boolean autoHeal = false;

    @Property(type = PropertyType.SELECTOR, name = "Auto Heal Type", description = "Select auto heal type",
            category = "Combat", subcategory = "Auto Heal", options = {"Potions", "Heal Spell"})
    public int autoHealMode = 0;

    @Property(type = PropertyType.SLIDER, name = "Target HUD X",
            category = "Combat", max = 100)
    public int targetHUDX = 25;

    @Property(type = PropertyType.SLIDER, name = "Target HUD Y",
            category = "Combat", max = 100)
    public int targetHUDY = 25;

    @Property(type = PropertyType.SWITCH, name = "Kill Aura", description = "Set a keybind in controls",
            category = "Combat", subcategory = "Kill Aura")
    public boolean killAura = false;

        @Property(type = PropertyType.SWITCH, name = "Highlight Attacked Entity",
                category = "Combat", subcategory = "Kill Aura")
        public boolean highlightKA = false;

        @Property(type = PropertyType.SELECTOR, name = "Kill Aura Class",
                category = "Combat", subcategory = "Kill Aura", options = {"Melee", "Archer", "Mage", "Shaman"})
        public int killAuraType = 0;

        @Property(type = PropertyType.SWITCH, name = "KA Nametags Only", description = "Only attack entities with a name",
                category = "Combat", subcategory = "Kill Aura")
        public boolean killAuraCustomNames = false;

        @Property(type = PropertyType.SWITCH, name = "KA Custom Filter", description = "Only attack matching /ka filter",
                category = "Combat", subcategory = "Kill Aura")
        public boolean killAuraFilter = false;

        @Property(type = PropertyType.SWITCH, name = "KA Blacklist", description = "Turn filter into a blacklist",
                category = "Combat", subcategory = "Kill Aura")
        public boolean killAuraFilterBlacklist = false;

    @Property(type = PropertyType.SELECTOR, name = "Spell Cast Speed",
            category = "Combat", subcategory = "Spell Aura", options = {"10 CPS", "5 CPS", "3.33 CPS", "2.5 CPS"})
    public int spellCastSpeed = 1;

    @Property(type = PropertyType.SWITCH, name = "Spell Aura", description = "Set a keybind in controls",
            category = "Combat", subcategory = "Spell Aura")
    public boolean spellAura = false;

        @Property(type = PropertyType.SLIDER, name = "SA Range",
                category = "Combat", subcategory = "Spell Aura", min = 10, max = 100)
        public int spellAuraRange = 10;

        @Property(type = PropertyType.SWITCH, name = "Highlight Attacked Entity",
                category = "Combat", subcategory = "Spell Aura")
        public boolean highlightSA = false;

        @Property(type = PropertyType.SELECTOR, name = "Spell Aura Class",
                category = "Combat", subcategory = "Spell Aura", options = {"Melee", "Archer", "Mage", "Shaman"})
        public int spellAuraType = 0;

        @Property(type = PropertyType.SWITCH, name = "SA Nametags Only", description = "Only attack entities with a name",
                category = "Combat", subcategory = "Spell Aura")
        public boolean spellAuraCustomNames = false;

        @Property(type = PropertyType.SWITCH, name = "SA Custom Filter", description = "Only attack matching /sa filter",
                category = "Combat", subcategory = "Spell Aura")
        public boolean spellAuraFilter = false;

        @Property(type = PropertyType.SWITCH, name = "SA Blacklist", description = "Turn filter into a blacklist",
                category = "Combat", subcategory = "Spell Aura")
        public boolean spellAuraFilterBlacklist = false;

    @Property(type = PropertyType.SELECTOR, name = "Spell 1",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellOneType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 1 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellOneDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 2",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellTwoType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 2 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellTwoDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 3",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellThreeType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 3 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellThreeDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 4",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellFourType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 4 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellFourDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 5",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellFiveType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 5 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellFiveDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 6",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellSixType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 6 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellSixDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 7",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellSevenType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 7 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellSevenDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 8",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellEightType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 8 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellEightDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 9",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellNineType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 9 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellNineDelay = 0;

    @Property(type = PropertyType.SELECTOR, name = "Spell 10",
            category = "Combat", subcategory = "Spells", options = {"§cNone", "RRR", "RLR", "RLL", "RRL", "L"})
    public int spellTenType = 0;

    @Property(type = PropertyType.SLIDER, name = "Spell 10 Delay", description = "Set delay in ticks",
            category = "Combat", subcategory = "Spells", max = 200)
    public int spellTenDelay = 0;

    /*
     * World
     */

    @Property(type = PropertyType.SWITCH, name = "Entity Ghost Hand", description = "Interact with entities through walls",
            category = "World", subcategory = "Entity Ghost Hand")
    public boolean entityGhostHand = false;

    @Property(type = PropertyType.SWITCH, name = "onGround",
            category = "World", subcategory = "Entity Ghost Hand")
    public boolean serverFunnyOnGround = false;

    @Property(type = PropertyType.SLIDER, name = "amount",
            category = "World", subcategory = "Entity Ghost Hand", min = 1, max = 5)
    public int serverFunnyAmount = 1;

    public Config() {
        super(new File("./config/rosegoldclient/config.toml"), "§aRoseGoldClient", new JVMAnnotationPropertyCollector(), new ConfigSorting());
        initialize();
    }

    public static class ConfigSorting extends SortingBehavior {
        @NotNull
        @Override
        public Comparator<Category> getCategoryComparator() {
            return (o1, o2) -> {
                if(o1.getName().equals("RoseGoldClient")) {
                    return -1;
                } else if(o2.getName().equals("RoseGoldClient")) {
                    return 1;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
    }
}
