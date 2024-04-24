package Method.Client.modmaker;

import Method.Client.Main;
import Method.Client.clickgui.component.Frame;
import Method.Client.managers.FileManager;
import Method.Client.managers.Setting;
import Method.Client.modmaker.Windows.Blocks.Array.Array;
import Method.Client.modmaker.Windows.Blocks.Settings.*;
import Method.Client.modmaker.Windows.Blocks.Variable.Variables;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.*;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static Method.Client.Main.setmgr;
import static Method.Client.modmaker.MainBlockType.Null;


public class LoadAndSave {
    public File path;

    public static class ResourceTab {
        public ResourceLocation resource;
        public Category category;
        public String name;

        public ResourceTab(ResourceLocation resource, Category category, String name) {
            this.resource = resource;
            this.category = category;
            this.name = name;
        }
    }

    // array list of ResourceTab
    public static ArrayList<ResourceTab> resourceTabs = new ArrayList<>();

    public LoadAndSave() {
        path = new File(FileManager.SaveDir, "ModMaker");
        //noinspection ResultOfMethodCallIgnored
        path.mkdirs();

        for (File file : Objects.requireNonNull(path.listFiles())) {
            initModule(file.getName(), Category.MAKER, true);
        }
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/WallHack.mtd"), Category.RENDER, "WallHack"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/Trail.mtd"), Category.RENDER, "Trail"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/Tracers.mtd"), Category.RENDER, "Tracers"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/SkyColor.mtd"), Category.RENDER, "SkyColor"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/NoRender.mtd"), Category.RENDER, "NoRender"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/NoBlockLag.mtd"), Category.RENDER, "NoBlockLag"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/NewChunks.mtd"), Category.RENDER, "NewChunks"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/NameTag.mtd"), Category.RENDER, "NameTag"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/MobOwner.mtd"), Category.RENDER, "MobOwner"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/ItemEsp.mtd"), Category.RENDER, "ItemEsp"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/Fullbright.mtd"), Category.RENDER, "Fullbright"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/FovMod.mtd"), Category.RENDER, "FovMod"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/f3spoof.mtd"), Category.RENDER, "f3spoof"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/ChunkBorder.mtd"), Category.RENDER, "ChunkBorder"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/ChestESP.mtd"), Category.RENDER, "ChestESP"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/BreakEsp.mtd"), Category.RENDER, "BreakEsp"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "render/BlockOverlay.mtd"), Category.RENDER, "BlockOverlay"));

        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Yawlock.mtd"), Category.PLAYER, "Yawlock"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Xcarry.mtd"), Category.PLAYER, "Xcarry"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Timer.mtd"), Category.PLAYER, "Timer"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/SmallShield.mtd"), Category.PLAYER, "SmallShield"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/SkinBlink.mtd"), Category.PLAYER, "SkinBlink"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/SchematicaNCP.mtd"), Category.PLAYER, "SchematicaNCP"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Reach.mtd"), Category.PLAYER, "Reach"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/PortalMod.mtd"), Category.PLAYER, "PortalMod"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/PitchLock.mtd"), Category.PLAYER, "PitchLock"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Noswing.mtd"), Category.PLAYER, "Noswing"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/NoServerChange.mtd"), Category.PLAYER, "NoServerChange"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/NoEffect.mtd"), Category.PLAYER, "NoEffect"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/LiquidInteract.mtd"), Category.PLAYER, "LiquidInteract"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/God.mtd"), Category.PLAYER, "God"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Freecam.mtd"), Category.PLAYER, "Freecam"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/FastPlace.mtd"), Category.PLAYER, "FastPlace"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/FastLadder.mtd"), Category.PLAYER, "FastLadder"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/FastBreak.mtd"), Category.PLAYER, "FastBreak"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Disconnect.mtd"), Category.PLAYER, "Disconnect"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/BuildHeight.mtd"), Category.PLAYER, "BuildHeight"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/Autotool.mtd"), Category.PLAYER, "Autotool"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/AutoRemount.mtd"), Category.PLAYER, "AutoRemount"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "player/AntiAFK.mtd"), Category.PLAYER, "AntiAFK"));

        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/WallSpeed.mtd"), Category.MOVEMENT, "WallSpeed"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Step.mtd"), Category.MOVEMENT, "Step"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Sprint.mtd"), Category.MOVEMENT, "Sprint"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Spider.mtd"), Category.MOVEMENT, "Spider"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Speed.mtd"), Category.MOVEMENT, "Speed"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Sneak.mtd"), Category.MOVEMENT, "Sneak"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Scaffold.mtd"), Category.MOVEMENT, "Scaffold"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/SafeWalk.mtd"), Category.MOVEMENT, "SafeWalk"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Phase.mtd"), Category.MOVEMENT, "Phase"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/LongJump.mtd"), Category.MOVEMENT, "LongJump"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/LiquidSpeed.mtd"), Category.MOVEMENT, "LiquidSpeed"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Levitate.mtd"), Category.MOVEMENT, "Levitate"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Jump.mtd"), Category.MOVEMENT, "Jump"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Jesus.mtd"), Category.MOVEMENT, "Jesus"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Glide.mtd"), Category.MOVEMENT, "Glide"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Fly.mtd"), Category.MOVEMENT, "Fly"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/FastFall.mtd"), Category.MOVEMENT, "FastFall"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/EntityVanish.mtd"), Category.MOVEMENT, "EntityVanish"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Entityspeed.mtd"), Category.MOVEMENT, "Entityspeed"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/ElytraFly.mtd"), Category.MOVEMENT, "ElytraFly"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Derp.mtd"), Category.MOVEMENT, "Derp"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Bunnyhop.mtd"), Category.MOVEMENT, "Bunnyhop"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/BoatFly.mtd"), Category.MOVEMENT, "BoatFly"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Blink.mtd"), Category.MOVEMENT, "Blink"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/AutoSwim.mtd"), Category.MOVEMENT, "AutoSwim"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "movement/Nofall.mtd"), Category.MOVEMENT, "Nofall"));

        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/QuickCraft.mtd"), Category.MISC, "QuickCraft"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/Pickupmod.mtd"), Category.MISC, "Pickupmod"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/NoSlowdown.mtd"), Category.MISC, "NoSlowdown"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/Livestock.mtd"), Category.MISC, "Livestock"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/HitEffects.mtd"), Category.MISC, "HitEffects"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/Ghost.mtd"), Category.MISC, "Ghost"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/FastSleep.mtd"), Category.MISC, "FastSleep"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/AutoClicker.mtd"), Category.MISC, "AutoClicker"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/AntiHurt.mtd"), Category.MISC, "AntiHurt"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "misc/AntiCrash.mtd"), Category.MISC, "AntiCrash"));

        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Webfill.mtd"), Category.COMBAT, "Webfill"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Velocity.mtd"), Category.COMBAT, "Velocity"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Trigger.mtd"), Category.COMBAT, "Trigger"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Surrond.mtd"), Category.COMBAT, "Surrond"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Strafe.mtd"), Category.COMBAT, "Strafe"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/SelfTrap.mtd"), Category.COMBAT, "SelfTrap"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Regen.mtd"), Category.COMBAT, "Regen"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Refill.mtd"), Category.COMBAT, "Refill"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Offhand.mtd"), Category.COMBAT, "Offhand"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/MoreKnockback.mtd"), Category.COMBAT, "MoreKnockback"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/KillAura.mtd"), Category.COMBAT, "KillAura"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/HoleFill.mtd"), Category.COMBAT, "HoleFill"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/HoleEsp.mtd"), Category.COMBAT, "HoleEsp"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/FireballReturn.mtd"), Category.COMBAT, "FireballReturn"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/CrystalAura.mtd"), Category.COMBAT, "CrystalAura"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Criticals.mtd"), Category.COMBAT, "Criticals"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Burrow.mtd"), Category.COMBAT, "Burrow"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/AutoTrap.mtd"), Category.COMBAT, "AutoTrap"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/AutoTotem.mtd"), Category.COMBAT, "AutoTotem"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/AutoRespawn.mtd"), Category.COMBAT, "AutoRespawn"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/AntiCrystal.mtd"), Category.COMBAT, "AntiCrystal"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/Anchor.mtd"), Category.COMBAT, "Anchor"));
        resourceTabs.add(new ResourceTab(new ResourceLocation(Main.MODID, "combat/AimBot.mtd"), Category.COMBAT, "AimBot"));

        for (ResourceTab resourceTab : resourceTabs) {
            try {
                IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceTab.resource);
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                JsonObject moduleJason = (JsonObject) FileManager.jsonParser.parse(reader);
                reader.close();
                initModulefromFile(resourceTab.name, resourceTab.category, false, resourceTab.name, moduleJason);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void saveMod() {
        try {
            JsonObject json = new JsonObject();
            for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
                JsonObject JsonMod = new JsonObject();
                if (activeBlock instanceof Contained) {
                    JsonMod.addProperty("Instance", "Contained");
                    if (((Contained) activeBlock).blockcontained != null)
                        JsonMod.addProperty("BlockContained", ((Contained) activeBlock).blockcontained.hashCode());
                } else if (activeBlock instanceof Header)
                    JsonMod.addProperty("Instance", "Header");
                else if (activeBlock instanceof Default)
                    JsonMod.addProperty("Instance", "Default");
                else if (activeBlock instanceof heldArray)
                    JsonMod.addProperty("Instance", "heldArray");
                else if (activeBlock instanceof heldObject)
                    JsonMod.addProperty("Instance", "heldObject");
                else if (activeBlock instanceof Insertable)
                    JsonMod.addProperty("Instance", "Insertable");

                else if (activeBlock instanceof SettingObject) {
                    JsonMod.addProperty("Instance", "SettingObject");
                    JsonMod.addProperty("settingmode", ((SettingObject) activeBlock).setting.getMode().toString());

                    // options will be filled by the box
                    JsonMod.addProperty("selected", ((SettingObject) activeBlock).setting.getselected());

                    JsonMod.addProperty("Name", ((SettingObject) activeBlock).setting.getName());
                    JsonMod.addProperty("onlyint", ((SettingObject) activeBlock).setting.onlyInt());
                    JsonMod.addProperty("sval", ((SettingObject) activeBlock).setting.getValString());
                    JsonMod.addProperty("bval", ((SettingObject) activeBlock).setting.getValBoolean());
                    JsonMod.addProperty("dval", ((SettingObject) activeBlock).setting.getValDouble());
                    JsonMod.addProperty("min", ((SettingObject) activeBlock).setting.getMin());
                    JsonMod.addProperty("max", ((SettingObject) activeBlock).setting.getMax());
                }

                if (activeBlock.thisblock.getName().equalsIgnoreCase("GetVarObj"))
                    JsonMod.addProperty("GetVarObj", true);
                if (activeBlock.thisblock.getName().equalsIgnoreCase("SetVarObj")) {
                    StringBuilder types = new StringBuilder();
                    for (ArrayList<MainBlockType> mainBlockTypes : activeBlock.thisblock.typesAccepted) {
                        mainBlockTypes.forEach(mainBlockType -> types.append(mainBlockType.name()).append(","));
                    }
                    JsonMod.addProperty("SetVarObj", String.valueOf(types));
                }

                JsonMod.addProperty("blocktype", activeBlock.thisblock.MainBlockTypeGiven.name());
                JsonMod.addProperty("nicecolor", activeBlock.niceColor.getRGB());
                JsonMod.addProperty("thisblock", activeBlock.thisblock.getName());
                JsonMod.addProperty("X", activeBlock.x);
                JsonMod.addProperty("Y", activeBlock.y);
                JsonMod.addProperty("minimized", activeBlock.minimized);
                JsonMod.addProperty("isplaced", activeBlock.isplaced);
                if (activeBlock.blockPointer != null)
                    JsonMod.addProperty("blockpointer", activeBlock.blockPointer.hashCode());
                if (activeBlock.nextBlock != null)
                    JsonMod.addProperty("nextblock", activeBlock.nextBlock.hashCode());
                JsonMod.addProperty("currentblock", activeBlock.hashCode());

                for (int i = 0; i < activeBlock.trueFalses.size(); i++)
                    JsonMod.addProperty("trueFals", activeBlock.trueFalses.get(i).state + ",");

                if (activeBlock.dropDowns != null)
                    JsonMod.addProperty("dropDowns", activeBlock.dropDowns.selected);


                for (int i = 0; i < activeBlock.inputBoxes.size(); i++) {
                    JsonMod.addProperty("inputBoxes" + i, activeBlock.inputBoxes.get(i).isFull);
                    if (!activeBlock.inputBoxes.get(i).isFull)
                        JsonMod.addProperty("inputBoxesv" + i, activeBlock.inputBoxes.get(i).inputBox.text);
                    else
                        JsonMod.addProperty("inputBoxesj" + i, activeBlock.inputBoxes.get(i).containedBlock.hashCode());
                }

                if (activeBlock.manyItems != null) {
                    if (activeBlock.manyItems.storedblock != null)
                        JsonMod.addProperty("manyItemsBlock", activeBlock.manyItems.storedblock.getLocalizedName());
                    if (activeBlock.manyItems.finaltext != null)
                        JsonMod.addProperty("manyItemsText", activeBlock.manyItems.finaltext);
                    if (activeBlock.manyItems.storeditem != null)
                        JsonMod.addProperty("manyItemsItem", Item.getIdFromItem(activeBlock.manyItems.storeditem));
                }


                json.add(String.valueOf(activeBlock.hashCode()), JsonMod);
            }

            // other things to save
            JsonObject JsonMod = new JsonObject();
            JsonMod.addProperty("threaded", Main.Maker.multiThread.state);
            json.add("other", JsonMod);

            File file = new File(path, Main.Maker.module.getName() + ".mtd");
            if (file.exists()) {
                file.delete();
            }
            file = new File(path, Main.Maker.module.getName() + ".mtd");
            PrintWriter saveJson = new PrintWriter(new FileWriter(file));
            saveJson.println(FileManager.gsonPretty.toJson(json));
            saveJson.close();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void initModule(String filename, Category maker, boolean iseditor) {
        if (filename != null && filename.contains(".mtd")) {
            String name = filename.replace(".mtd", "");
            Module newMod = new Module(name, Keyboard.KEY_NONE, maker, "Custom Made Module");

            File file = new File(path, filename);
            ArrayList<DragableBlock> convert = new ArrayList<>();

            if (file.exists()) {
                try {
                    BufferedReader loadJson = new BufferedReader(new FileReader(file));
                    JsonObject moduleJason = (JsonObject) FileManager.jsonParser.parse(loadJson);
                    loadJson.close();
                    extracted(newMod, convert, moduleJason);
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                    System.out.println("Error loading mod, Wrong version? Current V: " + Main.VERSION);
                }
                System.out.println("Data loaded successfully from " + filename + "." + " Converting to blocks...");
            } else {
                System.err.println("Cannot find " + filename + ".");
            }
            FinishSetup(filename, iseditor, newMod, convert);
        }
    }

    public Module initModulefromFile(String filename, Category maker, boolean iseditor, String newname, JsonObject moduleJason) {
        if (filename != null) {
            Module newMod = new Module(newname, Keyboard.KEY_NONE, maker, "Custom Made Module");
            ArrayList<DragableBlock> convert = new ArrayList<>();
            try {
                extracted(newMod, convert, moduleJason);
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Error loading mod, Wrong version? Current V: " + Main.VERSION);
            }
            System.out.println("Data loaded successfully from " + filename + "." + " Converting to blocks...");

            FinishSetup(filename, iseditor, newMod, convert);
            return newMod;
        }
        return null;
    }


    private static void extracted(Module newMod, ArrayList<DragableBlock> convert, JsonObject moduleJason) {
        for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
            JsonObject jsonMod = (JsonObject) entry.getValue();
            DragableBlock clone = null;
            String blockName = "Notfound";
            if (jsonMod.has("thisblock"))
                blockName = jsonMod.get("thisblock").getAsString();
            if (jsonMod.has("threaded"))
                newMod.multiThreaded = jsonMod.get("threaded").getAsBoolean();
            if (jsonMod.has("Instance"))
                switch (jsonMod.get("Instance").getAsString()) {
                    case "Contained":
                        clone = new Contained(newMod);
                        break;
                    case "Insertable":
                        clone = new Insertable(newMod);
                        break;
                    case "Header":
                        clone = new Header(newMod);
                        break;
                    case "Default":
                        clone = new Default(newMod);
                        break;
                    case "heldArray":
                        Array array1 = new Array(blockName);
                        array1.MainBlockTypeGiven = MainBlockType.Array;
                        clone = new heldArray(array1, Main.Maker);
                        break;
                    case "heldObject":
                        Variables Variable = new Variables(blockName);
                        clone = new heldObject(Variable, Variable.MainBlockTypeGiven, Main.Maker);
                        break;
                    case "SettingObject":
                        switch (jsonMod.get("settingmode").getAsString()) {
                            case "Combo":
                                SettingObject setting = new SettingObject(new SCombo(blockName), Main.Maker);
                                setting.setSetting(new Setting(blockName, newMod, "empty", "empty"));
                                setting.setting.setParent(newMod);
                                clone = setting;
                                setmgr.add(setting.setting);
                                break;
                            case "Check":
                                SettingObject settingCheck = new SettingObject(new SCheck(blockName), Main.Maker);
                                settingCheck.setSetting(new Setting(blockName, newMod, false));
                                settingCheck.setting.setParent(newMod);
                                clone = settingCheck;
                                setmgr.add(settingCheck.setting);
                                break;
                            case "Slider":
                                SettingObject settingSlider = new SettingObject(new SSlider(blockName), Main.Maker);
                                settingSlider.setSetting(new Setting(blockName, newMod, 0, 0, 1, false));
                                settingSlider.setting.setParent(newMod);
                                clone = settingSlider;
                                setmgr.add(settingSlider.setting);
                                break;
                            case "Color":
                                SettingObject settingColor = new SettingObject(new SColor(blockName), Main.Maker);
                                settingColor.setSetting(new Setting(blockName, newMod, 0, 1, 1, 1));
                                settingColor.setting.setParent(newMod);
                                clone = settingColor;
                                setmgr.add(settingColor.setting);
                                break;
                        }
                        break;
                }

            if (clone != null) {
                if (!(clone instanceof SettingObject || clone instanceof heldObject || clone instanceof heldArray))
                    clone.thisblock = Main.Maker.allblockSearch(jsonMod.get("thisblock").getAsString());

                if (clone.thisblock instanceof Variables)
                    clone.thisblock.MainBlockTypeGiven = MainBlockType.valueOf(jsonMod.get("blocktype").getAsString());

                if (clone instanceof heldObject && clone.thisblock.MainBlockTypeGiven == MainBlockType.Timer)
                    ((heldObject) clone).setupTimer();

                if (jsonMod.get("GetVarObj") != null)
                    clone.thisblock.MainBlockTypeGiven = MainBlockType.valueOf(jsonMod.get("blocktype").getAsString());

                clone.x = jsonMod.get("X").getAsInt();
                clone.y = jsonMod.get("Y").getAsInt();
                clone.niceColor = new Color(jsonMod.get("nicecolor").getAsInt());
                clone.minimized = jsonMod.get("minimized").getAsBoolean();
                clone.isplaced = jsonMod.get("isplaced").getAsBoolean();


                clone.saverCurrentBlock = jsonMod.get("currentblock").getAsInt();
                if (jsonMod.get("nextblock") != null)
                    clone.saverNextBlock = jsonMod.get("nextblock").getAsInt();
                if (jsonMod.get("blockpointer") != null)
                    clone.saverblockPointer = jsonMod.get("blockpointer").getAsInt();

                if (!(clone instanceof SettingObject || clone instanceof heldObject || clone instanceof heldArray))
                    clone.setup();
                // This is where the block setup should take place
                if (clone instanceof SettingObject) {
                    SettingObject setting = (SettingObject) clone;
                    if (jsonMod.get("selected") != null)
                        setting.setting.setSelected(jsonMod.get("selected").getAsString());
                    if (jsonMod.get("Name") != null)
                        setting.setting.setName(jsonMod.get("Name").getAsString());
                    if (jsonMod.get("onlyint") != null)
                        setting.setting.setOnlyint(jsonMod.get("onlyint").getAsBoolean());
                    if (jsonMod.get("sval") != null)
                        setting.setting.setValString(jsonMod.get("sval").getAsString());
                    if (jsonMod.get("bval") != null)
                        setting.setting.setValBoolean(jsonMod.get("bval").getAsBoolean());
                    if (jsonMod.get("dval") != null)
                        setting.setting.setValDouble(jsonMod.get("dval").getAsDouble());
                    if (jsonMod.get("min") != null)
                        setting.setting.setMin(jsonMod.get("min").getAsDouble());
                    if (jsonMod.get("max") != null)
                        setting.setting.setMax(jsonMod.get("max").getAsDouble());
                }


                if (blockName.equalsIgnoreCase("setvarobj")) {
                    ArrayList<ArrayList<MainBlockType>> types = new ArrayList<>();
                    ArrayList<MainBlockType> type = new ArrayList<>();
                    for (String s : jsonMod.get("SetVarObj").getAsString().split(",")) {
                        type.add(MainBlockType.valueOf(s));
                    }
                    types.add(type);
                    clone.thisblock.typesAccepted = types;

                    clone.inputBoxes.get(0).typesAccepted = types.get(0);
                }

                if (clone instanceof Contained && jsonMod.get("BlockContained") != null) {
                    ((Contained) clone).saverblockcontained = jsonMod.get("BlockContained").getAsInt();
                }

                if (jsonMod.get("dropDowns") != null) {
                    clone.dropDowns.selected = jsonMod.get("dropDowns").getAsInt();
                }
                if (jsonMod.get("trueFals") != null) {
                    ArrayList<Boolean> trueFalse = new ArrayList<>();
                    for (String s : jsonMod.get("trueFals").getAsString().split(","))
                        trueFalse.add(Boolean.valueOf(s));
                    for (int i = 0; i < clone.trueFalses.size(); i++)
                        clone.trueFalses.get(i).state = trueFalse.get(i);
                }

                for (int i = 0; i < clone.inputBoxes.size(); i++) {
                    if (jsonMod.get("inputBoxes" + i) != null)
                        clone.inputBoxes.get(i).isFull = jsonMod.get("inputBoxes" + i).getAsBoolean();
                    if (!clone.inputBoxes.get(i).isFull) {
                        if (jsonMod.get("inputBoxesv" + i) != null) {
                            clone.inputBoxes.get(i).inputBox.maxStringLength = 999;
                            clone.inputBoxes.get(i).inputBox.writeText(jsonMod.get("inputBoxesv" + i).getAsString());
                            clone.inputBoxes.get(i).inputBox.width = Math.max(clone.inputBoxes.get(i).inputBox.text.length() * 8, 20);

                        }

                    } else {
                        if (jsonMod.get("inputBoxesj" + i) != null)
                            clone.inputBoxes.get(i).saverContainedBlock = jsonMod.get("inputBoxesj" + i).getAsInt();
                    }
                }
                if (clone.manyItems != null) {
                    if (jsonMod.get("manyItemsText") != null)
                        clone.manyItems.finaltext = jsonMod.get("manyItemsText").getAsString();
                    if (jsonMod.get("manyItemsBlock") != null)
                        clone.manyItems.storedblock = Block.getBlockFromName(jsonMod.get("manyItemsBlock").getAsString());
                    if (jsonMod.get("manyItemsItem") != null)
                        clone.manyItems.storeditem = Item.getItemById(jsonMod.get("manyItemsItem").getAsInt());
                }


                if ("SettingObject".equals(jsonMod.get("Instance").getAsString())) {
                    if ("Combo".equals(jsonMod.get("settingmode").getAsString())) {

                        if (clone.inputBoxes.get(0).inputBox.text != null)
                            if ((clone.inputBoxes.get(0).inputBox.text).contains(",")) {
                                ArrayList<String> strings = new ArrayList<>();
                                Collections.addAll(strings, clone.inputBoxes.get(0).inputBox.text.split(","));
                                ((SettingObject) clone).setting.setOptions(strings);
                            }
                    }
                }
                convert.add(clone);
            }
        }
    }

    private void FinishSetup(String filename, boolean iseditor, Module newMod, ArrayList<DragableBlock> convert) {
        SettingObject Editmod;
        if (iseditor) {
            Editmod = new SettingObject(new DSetting("Edit Module", Null), Main.Maker);
            Editmod.visible = false;
            Editmod.setSetting(new Setting("Edit Module", newMod, Main.Maker));
        } else {
            Editmod = new SettingObject(new DSetting("Duplicate Module", Null), Main.Maker);
            Editmod.visible = false;
            Editmod.setSetting(new Setting("Duplicate Module", newMod, Main.DuplicateScreen));
        }
        setmgr.add(Editmod.setting);
        convert.add(Editmod);

        for (DragableBlock newblock : convert) {
            if (newblock.saverNextBlock != -1) {
                for (DragableBlock newblock2 : convert)
                    if (newblock.saverNextBlock == (newblock2.saverCurrentBlock))
                        newblock.nextBlock = newblock2;
            }
            if (newblock instanceof Contained)
                if (((Contained) newblock).saverblockcontained != -1) {
                    for (DragableBlock newblock2 : convert) {
                        if (((Contained) newblock).saverblockcontained == (newblock2.saverCurrentBlock))
                            ((Contained) newblock).blockcontained = newblock2;
                    }
                }


            for (int i = 0; i < newblock.inputBoxes.size(); i++) {
                if (newblock.inputBoxes.get(i).isFull) {
                    for (DragableBlock newblock2 : convert) {
                        if (newblock.inputBoxes.get(i).saverContainedBlock == (newblock2.saverCurrentBlock)) {
                            newblock.inputBoxes.get(i).containedBlock = newblock2;
                            break;
                        }
                    }
                }
            }
        }
        for (DragableBlock activeBlock : convert) {
            if (activeBlock.saverblockPointer != -1) {
                for (DragableBlock internalBlock : convert) {
                    if ((activeBlock.saverblockPointer == internalBlock.saverCurrentBlock)) {
                        activeBlock.blockPointer = internalBlock;
                        break;
                    }
                }
            }
        }

        for (DragableBlock newblock : convert) {
            newblock.saverNextBlock = 0;
            newblock.saverCurrentBlock = 0;
            newblock.inputBoxes.forEach(inputBox -> inputBox.saverContainedBlock = 0);
            if (newblock.x < 100) {
                while (newblock.x < 100) {
                    newblock.x += 10;
                    for (DragableBlock dragableBlock : convert) {
                        if (newblock != dragableBlock)
                            dragableBlock.x += 10;
                    }
                }
            }

            if (newblock.y < 20) {
                while (newblock.y < 20) {
                    newblock.y += 10;
                    for (DragableBlock dragableBlock : convert) {
                        if (newblock != dragableBlock)
                            dragableBlock.y += 10;
                    }
                }
            }
        }

        convert.forEach(dragableBlock -> dragableBlock.thisblock.finalInit(dragableBlock));

        newMod.ActiveBlocks = convert;

        ModuleManager.addModule(newMod);

        if (newMod.multiThreaded)
            newMod.codeExecuter = new ThreadedCatchExecuter(newMod);

        newMod.codeExecuter.loadBlocks();

        Main.ClickGui.frames.forEach(Frame::updateRefresh);

        System.out.println("Blocks converted successfully for " + filename + ".");
    }

}
