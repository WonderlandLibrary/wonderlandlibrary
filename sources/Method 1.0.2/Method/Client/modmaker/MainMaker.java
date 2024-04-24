package Method.Client.modmaker;

import Method.Client.Main;
import Method.Client.clickgui.component.Frame;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.Blocks.Array.AddDel;
import Method.Client.modmaker.Windows.Blocks.Array.GetArray;
import Method.Client.modmaker.Windows.Blocks.BlankItem.Boolean;
import Method.Client.modmaker.Windows.Blocks.BlankItem.*;
import Method.Client.modmaker.Windows.Blocks.Entity.*;
import Method.Client.modmaker.Windows.Blocks.GameSettings.*;
import Method.Client.modmaker.Windows.Blocks.Headers.*;
import Method.Client.modmaker.Windows.Blocks.Logic.*;
import Method.Client.modmaker.Windows.Blocks.Math.*;
import Method.Client.modmaker.Windows.Blocks.Packets.Cpacket.*;
import Method.Client.modmaker.Windows.Blocks.Packets.Spacket.*;
import Method.Client.modmaker.Windows.Blocks.Render.*;
import Method.Client.modmaker.Windows.Blocks.Settings.*;
import Method.Client.modmaker.Windows.Blocks.Utils.*;
import Method.Client.modmaker.Windows.Blocks.VarHelper.*;
import Method.Client.modmaker.Windows.Blocks.Variable.*;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.Header;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;
import Method.Client.modmaker.Windows.Window;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.ScrollBar;
import Method.Client.utils.visual.SerializableGuiTextField;
import Method.Client.utils.visual.TrueFalseBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static Method.Client.Main.setmgr;
import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class MainMaker extends GuiScreen implements Serializable {

    public ArrayList<Block> AllBlocks = new ArrayList<>();
    public ArrayList<DragableBlock> AdderBlocks = new ArrayList<>();
    public ArrayList<Window> windows = new ArrayList<>();
    public SerializableGuiTextField moduleSearch;
    public SerializableGuiTextField moduleName;
    public TrueFalseBox multiThread;
    public TrueFalseBox DevMode;
    public boolean needsUpdate;
    public Module module;
    public ScrollBar yScrollBar;
    public ScrollBar xScrollBar;
    public boolean hasmoved = false;


    public ArrayList<Block> allBlocks(Tabs tab) {
        return AllBlocks.stream().filter(allBlock -> allBlock.getCategory() == tab).collect(Collectors.toCollection(ArrayList::new));
    }

    public void setModule(Module temp) {
        module = temp;
        hasmoved = false;
    }

    public MainMaker() {
        yScrollBar = new ScrollBar(true, 10, 40, 2, 200, 10, 180, 9999, 0, 9999, false);
        xScrollBar = new ScrollBar(true, 40, 20, 2, 200, 10, 180, 9999, 0, 9999, false);
        xScrollBar.noScroll = true;
        xScrollBar.horizontal = true;
        multiThread = new TrueFalseBox(230, 10, 30, 10, false);
        DevMode = new TrueFalseBox(230, 35, 30, 10, true);
        SetupallBlocks();


        int offset = 0;
        for (Tabs tab : Tabs.values()) {
            if (!tab.equals(Tabs.Sub)) {
                Window window = new Window(tab, offset, this);
                offset += 25;
                windows.add(window);
            }
        }
    }

    public Block allblockSearch(String name) {
        for (Block allBlock : AllBlocks) {
            if (allBlock.getName().equals(name)) {
                return allBlock;
            }
        }
        switch (name) {
            case "GetVarBoolean":
                return new GetVarBoolean();
            case "GetVarNum":
                return new GetVarNum();
            case "GetVarObj":
                return new GetVarObj(null);

            case "SetVarBoolean":
                return new SetVarBoolean();
            case "SetVarNum":
                return new SetVarNum();
            case "SetVarObj":
                return new SetVarObj(null);

            case "GetCombo":
                return new GetCombo();
            case "GetSlider":
                return new GetSlider();
            case "GetColor":
                return new GetColor();
            case "GetCheck":
                return new GetCheck();

            case "AddDel":
                return new AddDel();
            case "getArray":
                return new GetArray();
        }

        return new ReplaceMe();
    }

    public void searchCheck(boolean resetText) {
        for (Window window : windows) {
            if (window.tab.equals(Tabs.Search)) {
                if (!window.blocks.isEmpty()) {
                    window.blocks.clear();
                    if (resetText)
                        moduleSearch.setText("");
                }
            }
        }
        for (Tabs tab : Tabs.values()) {
            int tY = 0;
            for (Block allBlock : allBlocks(tab)) {
                allBlock.offsetY = tY;
                tY += 20;
            }
        }
        windows.forEach(Window::redoSpacing);
    }

    @Override
    public void onGuiClosed() {
        for (Window window : windows) {
            window.VarNames.clear();
        }
        // arraylist of strings
        ArrayList<String> strings = new ArrayList<>();
        for (Block allBlock : AllBlocks) {
            for (Block allBlock2 : AllBlocks) {
                if (allBlock != allBlock2 && allBlock.getName().equals(allBlock2.getName())) {
                    if (!strings.contains(allBlock.getName()))
                        strings.add(allBlock.getName());
                }
            }
        }

        for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
            activeBlock.local = Main.Maker.module;
            if (activeBlock instanceof SettingObject) {
                SettingObject settingObject = (SettingObject) activeBlock;
                settingObject.setting.setParent(Main.Maker.module);

            }
        }
        // This updates the Setting,
        for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
            if (activeBlock.thisblock instanceof DSetting) {
                ((DSetting) activeBlock.thisblock).save(activeBlock);
            }
        }

        setmgr.removeAllMod(Main.Maker.module);
        for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
            if (activeBlock instanceof SettingObject) {
                SettingObject settingObject = (SettingObject) activeBlock;
                setmgr.add(settingObject.getSetting());
            }
        }
        for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
            activeBlock.x = activeBlock.x - xScrollBar.scroll;
            activeBlock.y = activeBlock.y - yScrollBar.scroll;
        }
        xScrollBar.scroll = 0;
        yScrollBar.scroll = 0;

        Main.Maker.module.ActiveBlocks.forEach(activeBlock -> activeBlock.thisblock.finalInit(activeBlock));

        for (Frame frame : Main.ClickGui.frames) {
            if (frame.getCategory().equals(Category.MAKER)) {
                frame.updateRefresh();
            }
        }

        Main.blockSaver.saveMod();
        if (multiThread.state)
            Main.Maker.module.codeExecuter = new ThreadedCatchExecuter(Main.Maker.module);

        Main.Maker.module.codeExecuter.loadBlocks();

        Main.Maker.module = null;
    }

    @Override
    public void initGui() {
        moduleSearch = new SerializableGuiTextField(0, 50, 5, 100, mc.displayWidth / 150);
        moduleSearch.setMaxStringLength(20);

        moduleName = new SerializableGuiTextField(0, 300, 5, 100, mc.displayWidth / 150);
        moduleName.text = "null";
        moduleName.setMaxStringLength(20);
        //moduleSearch.setEnableBackgroundDrawing(false);
        for (Window window : windows) {
            window.init();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // if module name is empty, set it to the module name
        if (moduleName.getText().equalsIgnoreCase("null")) {
            moduleName.setText(module.getName());
        }
        this.drawDefaultBackground();
        GlStateManager.pushMatrix();
        if (Main.Maker.module.getName() != null) {

            // Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName(), 300, 0, -1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Threaded", 225, 0, -1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("DevMode", 225, 25, -1);

        }

        moduleName.drawTextBox();

        Main.Maker.module.ActiveBlocks.forEach(allBlock -> allBlock.drawScreen(mouseX, mouseY, this, false));

        Gui.drawRect(0, 0, 175, 450, new Color(49, 49, 49, 235).getRGB());
        moduleSearch.drawTextBox();
        windows.forEach(window -> window.drawtab(mouseX, mouseY));

        glEnable(GL_BLEND);
        Gui.drawRect(5, (int) ((Minecraft.getMinecraft().displayHeight / 2.085) - 20), 40, (int) ((Minecraft.getMinecraft().displayHeight / 2.085) - 20) + 35, new Color(255, 255, 255, 82).getRGB());

        mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "trash.png"));
        Gui.drawModalRectWithCustomSizedTexture(5, (int) ((Minecraft.getMinecraft().displayHeight / 2.085) - 20), 0, 0, 35, 35, 35, 35);

        Gui.drawRect(153, 0, 185, 35, new Color(95, 239, 74, 137).getRGB());


        Gui.drawRect(400, 5, 415, 20, new Color(95, 239, 74, 137).getRGB());
        mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "save.png"));
        Gui.drawModalRectWithCustomSizedTexture(150, 0, 0, 0, 35, 35, 35, 35);

        Gui.drawRect(185, 0, 220, 35, new Color(74, 88, 239, 132).getRGB());
        mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "load.png"));
        Gui.drawModalRectWithCustomSizedTexture(185, 0, 0, 0, 35, 35, 35, 35);


        if (org.lwjgl.input.Keyboard.isKeyDown(KEY_LCONTROL)) {
            int wheel = org.lwjgl.input.Mouse.getDWheel();
            if (org.lwjgl.input.Keyboard.isKeyDown(KEY_LSHIFT)) {
                for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
                    activeBlock.x = activeBlock.x + (wheel / 3);
                }
            } else {
                for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
                    activeBlock.y = activeBlock.y + (wheel / 3);
                }
            }
        } else {
            yScrollBar.scrollCheck(mouseX, mouseY, mc.displayWidth / 2 - 10, 10);
            xScrollBar.scrollCheck(mouseX, mouseY, 200, mc.displayHeight / 2 - 10);
        }
        if (yScrollBar.lastscroll != yScrollBar.scroll) {
            for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
                activeBlock.y = activeBlock.y - yScrollBar.getLastScroll();
            }
            yScrollBar.lastscroll = yScrollBar.scroll;
            hasmoved = true;
        }
        if (xScrollBar.lastscroll != xScrollBar.scroll) {
            for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
                activeBlock.x = activeBlock.x - xScrollBar.getLastScroll();
            }
            xScrollBar.lastscroll = xScrollBar.scroll;
            hasmoved = true;
        }
        multiThread.drawScreen();
        DevMode.drawScreen();
        windows.forEach(window -> {
            if (window.isCurrent())
                window.blocks.forEach(block -> block.drawDescription(mouseX, mouseY));
        });

        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (needsUpdate) {
            for (Frame frame : Main.ClickGui.frames) {
                if (frame.getCategory().equals(Category.MAKER)) {
                    frame.updateRefresh();
                }
            }
            needsUpdate = false;
        }
        yScrollBar.MouseClicked(mouseX, mouseY, mc.displayWidth / 2 - 10, 10);
        xScrollBar.MouseClicked(mouseX, mouseY, 200, mc.displayHeight / 2 - 10);
        if (mouseY > 0 && mouseY < 35) {
            if (mouseX > 150 && mouseX < 185) {
                // SAVE
                if (Main.Maker.module.getName() != null) {
                    Main.blockSaver.saveMod();
                    ChatUtils.message("Saved as " + Main.Maker.module.getName() + " at Minecraft" + Main.blockSaver.path);
                }
            }
            if (mouseX > 185 && mouseX < 220) {
                try {
                    Desktop.getDesktop().open(Main.blockSaver.path);
                } catch (Exception e) {
                    ChatUtils.error("Failed to open Directory. ");
                }
            }
        }
        moduleSearch.mouseClicked(mouseX, mouseY, mouseButton);
        moduleName.mouseClicked(mouseX, mouseY, mouseButton);
        windows.forEach(window -> window.MouseClick(mouseX, mouseY, mouseButton, this));
        changeBlocks();
        if (mouseX > 170)
            searchCheck(true);

        if (!yScrollBar.scrolldragging && !xScrollBar.scrolldragging) {
            // if mouse is not between  0-175 and 0-450
            if (!(mouseX > 0 && mouseX < 175 && mouseY > 0 && mouseY < 450)) {
                boolean hoverclick = false;
                for (DragableBlock allBlock : Main.Maker.module.ActiveBlocks) {
                    if (allBlock.hover)
                        if (allBlock.mouseClicked(mouseX, mouseY, mouseButton, this, false)) {
                            hoverclick = true;
                            break;
                        }
                }
                if (!hoverclick)
                    for (DragableBlock allBlock : Main.Maker.module.ActiveBlocks) {
                        if (allBlock.mouseClicked(mouseX, mouseY, mouseButton, this, false)) {
                            break;
                        }
                    }
                if (!AdderBlocks.isEmpty()) {
                    for (DragableBlock adderBlock : AdderBlocks) {
                        adderBlock.isDragging = false;
                        Main.Maker.module.ActiveBlocks.add(adderBlock);
                    }

                    AdderBlocks.clear();
                }
            }
        }
        // if mouse between 400-415 and 0-15
        if (mouseX > 400 && mouseX < 415 && mouseY > 5 && mouseY < 20) {
            boolean found = false;
            if (module.getName() != null)
                if (!module.getName().equalsIgnoreCase(moduleName.getText()))
                    if (!moduleName.getText().equalsIgnoreCase("Edit Module"))
                        for (Module module1 : ModuleManager.modules) {
                            if (moduleName.getText().equalsIgnoreCase(module1.getName())) {
                                ChatUtils.error("Module name already taken");
                                found = true;
                                break;
                            }
                        }
            if (!found) {
                String oldName = module.getName();
                module.setName(moduleName.getText());
                // SAVE with new name
                Main.blockSaver.saveMod();
                // delete old file
                if (oldName != null) {
                    File file = new File(Main.blockSaver.path + "/" + oldName + ".mtd");
                    if (file.exists()) {
                        file.delete();
                    }
                }

            }
        }

        multiThread.mouseClicked(mouseX, mouseY, mouseButton);
        module.multiThreaded = multiThread.state;
        DevMode.mouseClicked(mouseX, mouseY, mouseButton);
        if (DevMode.state && !module.devMode) {
            Main.Maker.module.devMode = true;
            Main.Maker.module.setToggled(false);
            Main.Maker.module.codeExecuter = new CatchCodeExecuter(Main.Maker.module);
        } else if (!DevMode.state) {
            Main.Maker.module.devMode = false;
            Main.Maker.module.setToggled(false);
            Main.Maker.module.codeExecuter = new CodeExecuter(Main.Maker.module);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        moduleSearch.textboxKeyTyped(typedChar, keyCode);
        moduleName.textboxKeyTyped(typedChar, keyCode);
        windows.forEach(window -> window.keyTyped(typedChar, keyCode));
        if (!moduleSearch.getText().isEmpty() && moduleSearch.getText().length() > 1) {
            searchCheck(false);
            windows.forEach(window -> window.setCurrent(false));
            int tY = 0;
            for (Block allBlock : AllBlocks) {
                if ((moduleSearch.getText().toLowerCase().contains(allBlock.getName().toLowerCase())
                        || allBlock.getName().toLowerCase().contains(moduleSearch.getText().toLowerCase())) && !allBlock.tab.equals(Tabs.Search)
                        && !allBlock.tab.equals(Tabs.Sub)) {
                    allBlock.offsetY = tY;
                    for (Window window : windows) {
                        if (window.tab.equals(Tabs.Search)) {
                            if (!window.blocks.contains(allBlock)) {
                                window.blocks.add(allBlock);
                                window.setCurrent(true);
                            }
                        } else window.setCurrent(false);
                    }
                    tY += 20;
                }

                if (allBlock.description != null && !allBlock.tab.equals(Tabs.Sub) && !allBlock.tab.equals(Tabs.Search))
                    if (allBlock.description.toLowerCase().contains(moduleSearch.getText().toLowerCase())) {
                        for (Window window : windows)
                            if (window.tab.equals(Tabs.Search)) {
                                if (!window.blocks.contains(allBlock)) {
                                    window.blocks.add(allBlock);
                                    allBlock.offsetY = tY;
                                    tY += 20;
                                }
                                window.setCurrent(true);
                            } else window.setCurrent(false);
                    }
            }
        } else
            searchCheck(true);
        if (keyCode == 1)
            this.mc.displayGuiScreen(null);

        if (org.lwjgl.input.Keyboard.isKeyDown(KEY_LCONTROL) && org.lwjgl.input.Keyboard.isKeyDown(KEY_R)) {
            for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
                if (activeBlock instanceof Header) {
                    activeBlock.x = 200;
                    activeBlock.y = 100;
                }
                if (!activeBlock.isplaced && !(activeBlock instanceof Header)) {
                    activeBlock.x = 200;
                    activeBlock.y = 100;
                }
            }
        }
        // 28 = enter
        if (Main.Maker.module != null)
            Main.Maker.module.ActiveBlocks.forEach(activeBlock -> activeBlock.keyTyped(typedChar, keyCode));
    }

    @Override
    public void updateScreen() {
        moduleSearch.updateCursorCounter();
        moduleName.updateCursorCounter();
        windows.forEach(Window::updateScreen);
        Main.Maker.module.ActiveBlocks.forEach(DragableBlock::updateScreen);
        super.updateScreen();
    }


    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (!yScrollBar.scrolldragging && !xScrollBar.scrolldragging) {
            // Main.Maker.module.ActiveBlocks.removeIf(ActiveBlocks -> (ActiveBlocks.isDragging && ActiveBlocks.x < 20 && ActiveBlocks.y > (int) ((Minecraft.getMinecraft().displayHeight / 2.085) - 20)));
            // convert above to loop
            ArrayList<DragableBlock> temp = new ArrayList<>();
            for (DragableBlock activeBlock : Main.Maker.module.ActiveBlocks) {
                if (activeBlock.isDragging && activeBlock.x < 20 && activeBlock.y > (int) ((Minecraft.getMinecraft().displayHeight / 2.085) - 20)) {
                    temp.add(activeBlock);
                    temp.addAll(activeBlock.allcontained());
                }
            }
            for (DragableBlock dragableBlock : temp) {
                Main.Maker.module.ActiveBlocks.remove(dragableBlock);
            }
            temp.clear();

            Main.Maker.module.ActiveBlocks.forEach(activeBlock -> activeBlock.mouseReleased(mouseX, mouseY, state, this));
        }
        yScrollBar.MouseRelease();
        xScrollBar.MouseRelease();
        windows.forEach(window -> window.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public Block tempparent = null;

    public void changeBlocks() {
        if (tempparent != null) {
            for (Window window : windows) {
                if (window.tab.equals(Tabs.Search)) {
                    window.blocks.clear();
                    int tY = 0;
                    for (Block allBlock : AllBlocks) {
                        if (allBlock.parentBlock == tempparent && allBlock != tempparent) {
                            allBlock.offsetY = tY;
                            tY += 20;
                            window.blocks.add(allBlock);
                        }
                    }
                    window.setCurrent(true);
                } else window.setCurrent(false);
            }
            tempparent = null;
        }
    }

    public void SetupallBlocks() {
        // Used if the module is loaded from a file and fails to load
        AllBlocks.add(new ReplaceMe());

        // BlankItems
        AllBlocks.add(new BlockPos());
        AllBlocks.add(new BlockPosConvert());
        AllBlocks.add(new BlockSearch());
        AllBlocks.add(new Boolean());
        AllBlocks.add(new BoundingBox());
        AllBlocks.add(new BoundingBoxConvert());
        AllBlocks.add(new chatVisibility());
        AllBlocks.add(new ClickType());
        AllBlocks.add(new ClientStatus());
        AllBlocks.add(new CombatEvent());
        AllBlocks.add(new ConnectionState());
        AllBlocks.add(new elementType());
        AllBlocks.add(new EntityAction());
        AllBlocks.add(new EnumDifficulty());
        AllBlocks.add(new EnumFacing());
        AllBlocks.add(new EnumHand());
        AllBlocks.add(new EnumHandSide());
        AllBlocks.add(new EquipmentSlot());
        AllBlocks.add(new GameType());
        AllBlocks.add(new ItemSearch());
        AllBlocks.add(new ItemStackAttibutes());
        AllBlocks.add(new ItemStackConvert());
        AllBlocks.add(new ListItem());
        AllBlocks.add(new Null());
        AllBlocks.add(new PackStatus());
        AllBlocks.add(new Hole());
        AllBlocks.add(new HoleType());
        AllBlocks.add(new ParticleTypes());
        AllBlocks.add(new PlaceLocation());
        AllBlocks.add(new PlaceLocationConvert());
        AllBlocks.add(new PlaceLocationSet());
        AllBlocks.add(new PlayerDigging());
        AllBlocks.add(new RayTraceType());
        AllBlocks.add(new recipeBook());
        AllBlocks.add(new ScoreAction());
        AllBlocks.add(new SoundEvent());
        AllBlocks.add(new soundCategory());
        AllBlocks.add(new TitleType());
        AllBlocks.add(new toString());
        AllBlocks.add(new UseEntity());
        AllBlocks.add(new UUID());
        AllBlocks.add(new Vec3d());
        AllBlocks.add(new Vec3dConvert());


        // Entity

        AllBlocks.add(new ArmPos());
        AllBlocks.add(new AttackEntity());
        AllBlocks.add(new BlocksAroundEntity());
        AllBlocks.add(new BoatUpdateInputs());
        AllBlocks.add(new CurrentItemSlot());
        AllBlocks.add(new Dismount());
        AllBlocks.add(new EntityArmorList());
        AllBlocks.add(new EntityAttributes());
        AllBlocks.add(new EntityBBOffset());
        AllBlocks.add(new EntityBB());
        AllBlocks.add(new EntityHandItem());
        AllBlocks.add(new EntityInstance());
        AllBlocks.add(new EItemInstance());
        AllBlocks.add(new EntityFall());
        AllBlocks.add(new EntityIsTamed());
        AllBlocks.add(new EntityPosition());
        AllBlocks.add(new EntityRayTrace());
        AllBlocks.add(new EntityWithinBox());
        AllBlocks.add(new Extinguish());
        AllBlocks.add(new GetDistanceToMouse());
        AllBlocks.add(new GetEntityFacing());
        AllBlocks.add(new GetEntityHealth());
        AllBlocks.add(new GetEntityFood());
        AllBlocks.add(new GetEntityId());
        AllBlocks.add(new RemovePotion());
        AllBlocks.add(new GetEntityName());
        AllBlocks.add(new GetEntityOwner());
        AllBlocks.add(new GetEntitySwing());
        AllBlocks.add(new GetCurrentItem());
        AllBlocks.add(new GetEntityUUID());
        AllBlocks.add(new GetDistanceToGround());
        AllBlocks.add(new GetChunkPos());
        AllBlocks.add(new GetHandProgress());
        AllBlocks.add(new GetClosestEntity());
        AllBlocks.add(new GetItemOwner());
        AllBlocks.add(new GetRiding());
        AllBlocks.add(new GetSlimeSize());
        AllBlocks.add(new GetNetPlayerInfo());
        AllBlocks.add(new GetEntityRngNext());
        AllBlocks.add(new GetEntityPing());
        AllBlocks.add(new GetSpeedinAir());
        AllBlocks.add(new GetStackFromSlot());
        AllBlocks.add(new CooledAttackget());
        AllBlocks.add(new GiveEntityPotion());
        AllBlocks.add(new SetHandPos());
        AllBlocks.add(new SetItemOwner());
        AllBlocks.add(new SetItemPickupDelay());
        AllBlocks.add(new HoldingFleeceColor());
        AllBlocks.add(new HorseJumpPower());
        AllBlocks.add(new isBreedingItem());
        AllBlocks.add(new isInLove());
        AllBlocks.add(new isInsideBlock());
        AllBlocks.add(new IsTrappedChest());
        AllBlocks.add(new IsPotionActive());
        AllBlocks.add(new isSaddled());
        AllBlocks.add(new isBot());
        AllBlocks.add(new IsSheared());
        AllBlocks.add(new loadedEntityList());
        AllBlocks.add(new loadedTileEntityList());
        AllBlocks.add(new NumEntityAttributes());
        AllBlocks.add(new numplayersChest());
        AllBlocks.add(new InterpolateEntity());
        AllBlocks.add(new ItemAttributesNum());
        AllBlocks.add(new ItemAttributesString());
        AllBlocks.add(new PlayerCapabilities());
        AllBlocks.add(new PlayerModelPart());
        AllBlocks.add(new PotionInstance());
        AllBlocks.add(new ReachDistance());
        AllBlocks.add(new ResetPosition());
        AllBlocks.add(new Respawn());
        AllBlocks.add(new SetEntityAttributes());
        AllBlocks.add(new SetElytraFlap());
        AllBlocks.add(new SetEntityPosition());
        AllBlocks.add(new SetEntitySwing());
        AllBlocks.add(new SetFlying());
        AllBlocks.add(new SetNumEntityAttributes());
        AllBlocks.add(new SetSpeedinAir());

        AllBlocks.add(new SpawnEntity());
        AllBlocks.add(new StartRiding());
        AllBlocks.add(new SwingArm());
        AllBlocks.add(new EntityBlockPos());
        AllBlocks.add(new EntityVec3d());
        AllBlocks.add(new TileEntityInstance());


        // Game Settings
        AllBlocks.add(new BlockActivated());
        AllBlocks.add(new BlockDamage());
        AllBlocks.add(new BlockHitDelay());
        AllBlocks.add(new FovSettingGet());
        AllBlocks.add(new ShowDebug());
        AllBlocks.add(new BreakBlock());
        AllBlocks.add(new CurrentItem());
        AllBlocks.add(new CurBlockDamageMpVal());
        AllBlocks.add(new EntityById());
        AllBlocks.add(new EntityCollided());
        AllBlocks.add(new FogColor());
        AllBlocks.add(new FovSetting());
        AllBlocks.add(new gammaSetting());
        AllBlocks.add(new SetSmoothCamera());
        AllBlocks.add(new GuiInstance());
        AllBlocks.add(new getBlock());
        AllBlocks.add(new GetBlockFromPos());
        AllBlocks.add(new GetBlockType());
        AllBlocks.add(new getDamagedBlocks());
        AllBlocks.add(new DamagedBlockpos());
        AllBlocks.add(new DamagedBlockInfo());
        AllBlocks.add(new GetDefaultState());
        AllBlocks.add(new HurtCam());
        AllBlocks.add(new IceSlip());
        AllBlocks.add(new InPortal());
        AllBlocks.add(new InteractWithEntity());
        AllBlocks.add(new InventoryWindowid());
        AllBlocks.add(new Jump());
        AllBlocks.add(new LoadRenderers());
        AllBlocks.add(new MineTimer());
        AllBlocks.add(new ObjectMouseOver());
        AllBlocks.add(new ProcessRightClick());
        AllBlocks.add(new Raining());
        AllBlocks.add(new RainStrength());
        AllBlocks.add(new ResetBlockBreaking());
        AllBlocks.add(new RightclickDelay());
        AllBlocks.add(new SetBlockState());
        AllBlocks.add(new SetBlockToAir());
        AllBlocks.add(new SetMineTimer());
        AllBlocks.add(new PlayerDestroyBlock());
        AllBlocks.add(new SetWorldTime());
        AllBlocks.add(new TickRate());
        AllBlocks.add(new WindowClick());
        AllBlocks.add(new WorldRemoveEntity());

        // Logic
        AllBlocks.add(new And());
        AllBlocks.add(new For());
        AllBlocks.add(new ForObject());
        AllBlocks.add(new If());
        AllBlocks.add(new Loop());
        AllBlocks.add(new Customfunction());
        AllBlocks.add(new Or());

        // VarHelper
        AllBlocks.add(new Length());
        AllBlocks.add(new ArrayClear());
        AllBlocks.add(new ArrayContains());
        AllBlocks.add(new ArrayShuffle());
        AllBlocks.add(new ArraySize());
        AllBlocks.add(new CheckDependant());
        AllBlocks.add(new ModeDependant());
        AllBlocks.add(new StringEquals());
        AllBlocks.add(new Join());
        AllBlocks.add(new SubString());
        AllBlocks.add(new BoundingChange());
        AllBlocks.add(new Contains());
        AllBlocks.add(new ParseNumber());
        AllBlocks.add(new TernaryNumerical());
        AllBlocks.add(new Split());


        // Math
        AllBlocks.add(new Addition());
        AllBlocks.add(new Algebra());
        AllBlocks.add(new BinarySearch());
        AllBlocks.add(new Clamp());
        AllBlocks.add(new DegToRad());
        AllBlocks.add(new Division());
        AllBlocks.add(new Equal());
        AllBlocks.add(new Min());
        AllBlocks.add(new Mod());
        AllBlocks.add(new DecimalTrim());
        AllBlocks.add(new Multiplication());
        AllBlocks.add(new Not());
        AllBlocks.add(new ObjectEquals());
        AllBlocks.add(new Random());
        AllBlocks.add(new RandomBoolean());
        AllBlocks.add(new Replace());
        AllBlocks.add(new Round());
        AllBlocks.add(new Subtraction());
        AllBlocks.add(new Shift());


        // Render
        AllBlocks.add(new BlockOutline());
        AllBlocks.add(new BoatRender());
        AllBlocks.add(new EntBoundingBox());
        AllBlocks.add(new GlClear());
        AllBlocks.add(new SetOpaqueCube());
        AllBlocks.add(new BBExpand());
        AllBlocks.add(new LightMap());
        AllBlocks.add(new NewColor());
        AllBlocks.add(new RenderBlockBegin());
        AllBlocks.add(new RenderEntityStatic());
        AllBlocks.add(new RenderLine());
        AllBlocks.add(new RenderText());
        AllBlocks.add(new RenderItem());
        AllBlocks.add(new RenderNameTag());
        AllBlocks.add(new SetNameTag());
        AllBlocks.add(new StdBoundingBox());
        AllBlocks.add(new SimpleNameTag());
        AllBlocks.add(new SpawnParticle());
        AllBlocks.add(new StandardItemLighting());
        // Settings


        //Utils

        AllBlocks.add(new BestTool());
        AllBlocks.add(new BlockHardness());
        AllBlocks.add(new BlockposToVec3d());
        AllBlocks.add(new BlockName());
        AllBlocks.add(new BlockInstance());
        AllBlocks.add(new BlockPosOffset());
        AllBlocks.add(new BlockStrength());
        AllBlocks.add(new CanBlockBeClicked());
        AllBlocks.add(new CanEntityBeSeen());
        AllBlocks.add(new CenterPlayer());
        AllBlocks.add(new ColorCalc());
        AllBlocks.add(new GetStepNNormal());
        AllBlocks.add(new CanPlaceBlock());
        AllBlocks.add(new ChatFormatting());
        AllBlocks.add(new CollidesWithBox());
        AllBlocks.add(new CreatePlayer());
        AllBlocks.add(new CrystalDamage());
        AllBlocks.add(new DateFormat());
        AllBlocks.add(new DirectionSpeed());
        AllBlocks.add(new DisablePitchYaw());
        AllBlocks.add(new dismountRiding());
        AllBlocks.add(new EnchantCrit());
        AllBlocks.add(new EventCancel());
        AllBlocks.add(new GetEnchantmentLevel());
        AllBlocks.add(new PacketCancel());
        AllBlocks.add(new GetDistanceToBlock());
        AllBlocks.add(new GetPops());
        AllBlocks.add(new GetDistanceEntity());
        AllBlocks.add(new GetEyepos());
        AllBlocks.add(new GetHoles());
        AllBlocks.add(new GetHoleBlockpos());
        AllBlocks.add(new GetMaterial());
        AllBlocks.add(new GetInventoryArray());
        AllBlocks.add(new GetAllinBox());
        AllBlocks.add(new GetLook());
        AllBlocks.add(new GetMovementInput());
        AllBlocks.add(new GetPitchYaw());
        AllBlocks.add(new GetStackInSlot());
        AllBlocks.add(new HorseAttributes());
        AllBlocks.add(new IsEmptyChunk());
        AllBlocks.add(new FindItemInSlot());
        AllBlocks.add(new isCollidable());
        AllBlocks.add(new isMoving());
        AllBlocks.add(new IsModuleEnabled());
        AllBlocks.add(new IsHoleTall());
        AllBlocks.add(new IsFriend());
        AllBlocks.add(new IsItemBlock());
        AllBlocks.add(new ItemById());
        AllBlocks.add(new Keyboard());
        AllBlocks.add(new McCurrentScreen());
        AllBlocks.add(new McKeybinds());
        AllBlocks.add(new Mouse());
        AllBlocks.add(new MouseGrabber());
        AllBlocks.add(new MovementInputKey());
        AllBlocks.add(new HoleRun());
        AllBlocks.add(new GetHoleType());
        AllBlocks.add(new MoverOverride());
        AllBlocks.add(new NeededRotation());
        AllBlocks.add(new NetworkPVersion());
        AllBlocks.add(new NotNull());
        AllBlocks.add(new OnCriticalHit());
        AllBlocks.add(new PlaceBlock());
        AllBlocks.add(new PlaceEntity());
        AllBlocks.add(new PlaySound());
        AllBlocks.add(new Quit());
        AllBlocks.add(new GetTotemCount());
        AllBlocks.add(new GetBlockFromItem());
        AllBlocks.add(new EnchantmentModifier());
        AllBlocks.add(new RaytraceBlockPos());
        AllBlocks.add(new RaytraceEntityHit());
        AllBlocks.add(new ResetTimer());
        AllBlocks.add(new ReturnBlock());
        AllBlocks.add(new Break());
        AllBlocks.add(new Continue());
        AllBlocks.add(new UpdateEntityForce());
        AllBlocks.add(new SetPos());
        AllBlocks.add(new ItemInstance());
        AllBlocks.add(new IsTimerPassed());
        AllBlocks.add(new SelfChat());
        AllBlocks.add(new SignReplace());
        AllBlocks.add(new SetMcKeybinds());
        AllBlocks.add(new SetPitchYaw());
        AllBlocks.add(new SignLines());
        AllBlocks.add(new Skip());
        AllBlocks.add(new SpawnLightning());
        AllBlocks.add(new SetMcKeybindsTrue());
        AllBlocks.add(new SyncCurrentItem());
        AllBlocks.add(new TeleportPkt());
        AllBlocks.add(new Toggle());
        AllBlocks.add(new TicksExisted());
        AllBlocks.add(new UpdateController());
        AllBlocks.add(new ViewerPos());
        AllBlocks.add(new Vec3dToBlockpos());
        AllBlocks.add(new Vec3dOffset());
        AllBlocks.add(new YourSelf());

        // Events
        subBlock(new OnEnable());
        subBlock(new OnDisable());
        subBlock(new OnToggle());

        subBlock(new OnPacketReceived());
        subBlock(new OnPacketSent());

        subBlock(new CustomFunctionHeader0());
        subBlock(new CustomFunctionHeader1());
        subBlock(new CustomFunctionHeader2());
        subBlock(new CustomFunctionHeader3());
        subBlock(new CustomFunctionHeader4());
        subBlock(new CustomFunctionHeader5());
        subBlock(new CustomFunctionHeader6());
        subBlock(new CustomFunctionHeader7());
        subBlock(new CustomFunctionHeader8());

        subBlock(new SettingChanged(), new SettingChanged.SettingType());

        subBlock(new BackgroundDrawn(), new BackgroundDrawn.Gui());
        subBlock(new BedSleep(), new BedSleep.PlayerinBed(), new BedSleep.getPos());

        subBlock(new LivingHurt(), new LivingHurt.getDmgamount(), new LivingHurt.getDmgsource(), new LivingHurt.getDmgsourceMob());

        subBlock(new ChatReceived());
        subBlock(new ClientChat());
        subBlock(new DamageBlock(), new DamageBlock.Facing(), new DamageBlock.pos());
        subBlock(new DrawBlockHighlight(), new DrawBlockHighlight.getBlockPos());
        subBlock(new fogColor(), new fogColor.setColor());
        subBlock(new fogDensity(), new fogDensity.setDensity(), new fogDensity.getState());
        subBlock(new FOVModifier(), new FOVModifier.getFOV(), new FOVModifier.setFov());
        subBlock(new GetCollisionBoxesEvent(), new GetCollisionBoxesEvent.EndConquered(), new GetCollisionBoxesEvent.Entity());
        subBlock(new GuiOpen(), new GuiOpen.getGui(), new GuiOpen.setGui());
        subBlock(new GuiScreenEventInit(), new GuiScreenEventInit.getGuinit());
        subBlock(new GuiScreenEventPost(), new GuiScreenEventPost.getGuiost());
        subBlock(new Liquidvisitor());
        subBlock(new LivingUpdate(), new LivingUpdate.Entityate());
        subBlock(new LoadChunk(), new LoadChunk.Chunk());
        subBlock(new onAttackEntity(), new onAttackEntity.getEntity(), new onAttackEntity.getTarget());
        subBlock(new onCameraSetup(), new onCameraSetup.getEntitytup(), new onCameraSetup.getPitch(), new onCameraSetup.getYaw(), new onCameraSetup.getRoll());
        subBlock(new OnClientTick());
        subBlock(new onGetAmbientOcclusionLightValue(), new onGetAmbientOcclusionLightValue.getBlocklight(), new onGetAmbientOcclusionLightValue.getLightValue(), new onGetAmbientOcclusionLightValue.setLightValue(), new onGetAmbientOcclusionLightValue.getDefaultLightValue());
        subBlock(new onItemPickup(), new onItemPickup.Playerkup(), new onItemPickup.getItem());
        subBlock(new onLeftClickBlock(), new onLeftClickBlock.Playerock(), new onLeftClickBlock.hitVec(), new onLeftClickBlock.GetposLc(), new onLeftClickBlock.getHand(), new onLeftClickBlock.getFace());
        subBlock(new onLivingDeath(), new onLivingDeath.Playerath());

        subBlock(new onPlayerJump(), new onPlayerJump.Playerump());
        subBlock(new onPlayerMove(), new onPlayerMove.Playerove());
        subBlock(new onPlayerTick());
        subBlock(new onProjectileImpact(), new onProjectileImpact.Entityact());
        subBlock(new onRenderBlockModel(), new onRenderBlockModel.getStatedel());

        subBlock(new onRenderGameOverlayText(), new onRenderGameOverlayText.getLeft(), new onRenderGameOverlayText.setLeft(), new onRenderGameOverlayText.setRight(), new onRenderGameOverlayText.getRight());
        subBlock(new onRenderPre(), new onRenderPre.getTypeOV());
        subBlock(new onRenderTileEntity(), new onRenderTileEntity.getTileEntity());
        subBlock(new onRenderWorldLast());
        subBlock(new onRightClickBlock(), new onRightClickBlock.onRPlayer(), new onRightClickBlock.getFaceock(), new onRightClickBlock.getHandock(), new onRightClickBlock.hitVecock());
        subBlock(new onTooltip(), new onTooltip.getItemStack());
        subBlock(new onWorldLoad(), new onWorldLoad.getWorld());
        subBlock(new onWorldUnload(), new onWorldUnload.getWorldoad());
        subBlock(new PlayerDimensionChange(), new PlayerDimensionChange.Playerdimchange(), new PlayerDimensionChange.DimchangeNumber());
        subBlock(new PlayerLoggedIn(), new PlayerLoggedIn.PlayerdIn());
        subBlock(new PlayerLoggedOut(), new PlayerLoggedOut.Playeroutd());
        subBlock(new PlayerRespawn(), new PlayerRespawn.Playerawn(), new PlayerRespawn.End());
        subBlock(new postBackgroundTooltipRender());
        subBlock(new postDrawScreen(), new postDrawScreen.getGuieen());
        subBlock(new PostMotion());
        subBlock(new PreMotion());
        subBlock(new RenderBlockOverlay());
        subBlock(new renderNamePlate(), new renderNamePlate.Playerate());
        subBlock(new RenderTickEvent());
        subBlock(new RendertooltipPre());
        subBlock(new UnLoadChunk(), new UnLoadChunk.Chunkunk());
        subBlock(new WorldEvent(), new WorldEvent.World());


        // Packets
        subBlock(new pC00Handshake(), new pC00Handshake.protocolVersion(), new pC00Handshake.setprotocolVersion(), new pC00Handshake.FMLMarker(),
                new pC00Handshake.SendpC00Handshake());
        subBlock(new pCPacketAnimation(), new pCPacketAnimation.getHandion(), new pCPacketAnimation.SendpCPacketAnimation());
        subBlock(new pCPacketChatMessage(), new pCPacketChatMessage.getMessage(),
                new pCPacketChatMessage.SendpCPacketChatMessage());
        subBlock(new pCPacketClickWindow(), new pCPacketClickWindow.getWindowId(), new pCPacketClickWindow.getClickType(),
                new pCPacketClickWindow.getClickedItem(),
                new pCPacketClickWindow.getSlotId(), new pCPacketClickWindow.getUsedButton(),
                new pCPacketClickWindow.SendCPacketClickWindow());
        subBlock(new pCPacketClientSettings(), new pCPacketClientSettings.getChatVisibility(),
                new pCPacketClientSettings.getLang(), new pCPacketClientSettings.getMainHand(),
                new pCPacketClientSettings.getModelPartFlags(), new pCPacketClientSettings.isColorsEnabled(),
                new pCPacketClientSettings.SendCPacketClientSettings());
        subBlock(new pCPacketClientStatus(), new pCPacketClientStatus.getStatus(),
                new pCPacketClientStatus.SendCPacketClientStatus());
        subBlock(new pCPacketCloseWindow(), new pCPacketCloseWindow.SendCPacketCloseWindow());
        subBlock(new pCPacketConfirmTeleport(), new pCPacketConfirmTeleport.getTeleportId(), new pCPacketConfirmTeleport.SendCPacketConfirmTeleport());
        subBlock(new pCPacketConfirmTransaction(), new pCPacketConfirmTransaction.getWindowIdion(),
                new pCPacketConfirmTransaction.getUid(), new pCPacketConfirmTransaction.SendConfirmTransaction());
        subBlock(new pCPacketCreativeInventoryAction(), new pCPacketCreativeInventoryAction.getSlotIdion(),
                new pCPacketCreativeInventoryAction.getStack(), new pCPacketCreativeInventoryAction.SendCreativeInventoryAction());
        subBlock(new pCPacketCustomPayload(),
                new pCPacketCustomPayload.getChannelName(), new pCPacketCustomPayload.SendgetChannelName());
        subBlock(new pCPacketEnchantItem(), new pCPacketEnchantItem.getWindowIdtem(), new pCPacketEnchantItem.getButton()
                , new pCPacketEnchantItem.SendEnchantItem());
        subBlock(new pCPacketEncryptionResponse());
        subBlock(new pCPacketEntityAction(), new pCPacketEntityAction.getAction(), new pCPacketEntityAction.getAuxData()
                , new pCPacketEntityAction.SendEntityAction());
        subBlock(new pCPacketHeldItemChange(), new pCPacketHeldItemChange.getSlotIdnge(), new pCPacketHeldItemChange.SendHeldItemChange());
        subBlock(new pCPacketInput(), new pCPacketInput.getForwardSpeed(), new pCPacketInput.getStrafeSpeed(),
                new pCPacketInput.isJumping(), new pCPacketInput.isSneaking(), new pCPacketInput.SendPacketInput());
        subBlock(new pCPacketKeepAlive(), new pCPacketKeepAlive.getKey(), new pCPacketKeepAlive.SendKeepAlive());
        subBlock(new pCPacketLoginStart(), new pCPacketLoginStart.getProfile());
        subBlock(new pCPacketPing(), new pCPacketPing.getClientTime(), new pCPacketPing.SendPing());
        subBlock(new pCPacketPlaceRecipe());
        subBlock(new pCPacketPlayer(), new pCPacketPlayer.onGround(), new pCPacketPlayer.Getx(),
                new pCPacketPlayer.Gety(), new pCPacketPlayer.Getz(), new pCPacketPlayer.yaw(),
                new pCPacketPlayer.pitch(), new pCPacketPlayer.GetType(), new pCPacketPlayer.SendOnground(), new pCPacketPlayer.SendPlayerRotation()
                , new pCPacketPlayer.SendPlayerPosition(), new pCPacketPlayer.SendPlayerPosRot(), new pCPacketPlayer.SendPlayerBlank()
                , new pCPacketPlayer.setOnground(), new pCPacketPlayer.setpitch(), new pCPacketPlayer.setyaw()
                , new pCPacketPlayer.setx(), new pCPacketPlayer.sety(), new pCPacketPlayer.setz()
        );
        subBlock(new pCPacketPlayerAbilities(), new pCPacketPlayerAbilities.isInvulnerable(),
                new pCPacketPlayerAbilities.isFlying(), new pCPacketPlayerAbilities.isCreativeMode(),
                new pCPacketPlayerAbilities.isAllowFlying(), new pCPacketPlayerAbilities.SendPlayerAbilities(),
                new pCPacketPlayerAbilities.flySpeed(), new pCPacketPlayerAbilities.walkSpeed());
        subBlock(new pCPacketPlayerDigging(), new pCPacketPlayerDigging.getActioning(), new pCPacketPlayerDigging.getFacing(),
                new pCPacketPlayerDigging.getPosition(), new pCPacketPlayerDigging.SendPlayerDigging());
        subBlock(new pCPacketPlayerTryUseItem(), new pCPacketPlayerTryUseItem.getHandtem(),
                new pCPacketPlayerTryUseItem.SendTryUseItem());
        subBlock(new pCPacketPlayerTryUseItemOnBlock(), new pCPacketPlayerTryUseItemOnBlock.pCPgetHand(),
                new pCPacketPlayerTryUseItemOnBlock.getPosock(), new pCPacketPlayerTryUseItemOnBlock.getDirection(),
                new pCPacketPlayerTryUseItemOnBlock.getFacingX(), new pCPacketPlayerTryUseItemOnBlock.getFacingY(),
                new pCPacketPlayerTryUseItemOnBlock.getFacingZ(), new pCPacketPlayerTryUseItemOnBlock.SendUseItemOnBlock());
        subBlock(new pCPacketRecipeInfo(),
                new pCPacketRecipeInfo.isFilteringCraftable(), new pCPacketRecipeInfo.isGuiOpen()
                , new pCPacketRecipeInfo.SendRecipeInfo());
        subBlock(new pCPacketResourcePackStatus(), new pCPacketResourcePackStatus.action(),
                new pCPacketResourcePackStatus.SendResourcePackStatus());
        subBlock(new pCPacketSeenAdvancements());
        subBlock(new pCPacketServerQuery(), new pCPacketServerQuery.SendServerQuery());
        subBlock(new pCPacketSpectate(), new pCPacketSpectate.SendPacketSpectate());
        subBlock(new pCPacketSteerBoat(), new pCPacketSteerBoat.getLeftoat(), new pCPacketSteerBoat.getRightoat()
                , new pCPacketSteerBoat.SendSteerBoat());
        subBlock(new pCPacketTabComplete(), new pCPacketTabComplete.getMessageete(), new pCPacketTabComplete.hasTargetBlock(),
                new pCPacketTabComplete.getTargetBlock(), new pCPacketTabComplete.SendTabComplete());
        subBlock(new pCPacketUpdateSign(), new pCPacketUpdateSign.getPositionign(), new pCPacketUpdateSign.SendSignUpdate());
        subBlock(new pCPacketUseEntity(), new pCPacketUseEntity.getActionity(),
                new pCPacketUseEntity.getHandity(), new pCPacketUseEntity.getHitVec(), new pCPacketUseEntity.getEntityit(),
                new pCPacketUseEntity.SendUseEntityAttack(), new pCPacketUseEntity.SendUseEntity()
                , new pCPacketUseEntity.SendUseEntityVector());
        subBlock(new pCPacketVehicleMove(), new pCPacketVehicleMove.getXove(), new pCPacketVehicleMove.getYove(),
                new pCPacketVehicleMove.getZove(), new pCPacketVehicleMove.getYawove(), new pCPacketVehicleMove.getPitchove(),
                new pCPacketVehicleMove.SendVehicleMove(), new pCPacketVehicleMove.setXove(), new pCPacketVehicleMove.setYove(), new pCPacketVehicleMove.setZove(),
                new pCPacketVehicleMove.setYawove(), new pCPacketVehicleMove.setPitchove());


        // Incoming Packets
        subBlock(new pSPacketAdvancementInfo(), new pSPacketAdvancementInfo.isFirstSync(), new pSPacketAdvancementInfo.setFirstSync());
        subBlock(new pSPacketAnimation(), new pSPacketAnimation.getAnimationType(), new pSPacketAnimation.getEntityID()
                , new pSPacketAnimation.setAnimationType(), new pSPacketAnimation.setEntityIDAni());
        subBlock(new pSPacketBlockAction(), new pSPacketBlockAction.getBlockType(), new pSPacketBlockAction.getBlockPosition(),
                new pSPacketBlockAction.getinstrument(), new pSPacketBlockAction.getpitch()
                , new pSPacketBlockAction.setBlockPosition(), new pSPacketBlockAction.setBlockType(), new pSPacketBlockAction.setinstrument()
                , new pSPacketBlockAction.setpitch());
        subBlock(new pSPacketBlockBreakAnim(), new pSPacketBlockBreakAnim.getBreakerId(), new pSPacketBlockBreakAnim.getPositionnim(),
                new pSPacketBlockBreakAnim.getProgress(), new pSPacketBlockBreakAnim.setProgress(), new pSPacketBlockBreakAnim.setPosition()
                , new pSPacketBlockBreakAnim.setbreakerId());
        subBlock(new pSPacketBlockChange(), new pSPacketBlockChange.getBlockPositionnge(), new pSPacketBlockChange.getBlockState()
                , new pSPacketBlockChange.setBlockPos(), new pSPacketBlockChange.setblockState());
        subBlock(new pSPacketCamera(), new pSPacketCamera.entityId(), new pSPacketCamera.setentityIdCam());
        subBlock(new pSPacketChangeGameState(), new pSPacketChangeGameState.getGameState(), new pSPacketChangeGameState.getValue()
                , new pSPacketChangeGameState.setstate(), new pSPacketChangeGameState.setvalue());
        subBlock(new pSPacketChat());
        subBlock(new pSPacketChunkData(), new pSPacketChunkData.getChunkX(), new pSPacketChunkData.getChunkZ(),
                new pSPacketChunkData.getExtractedSize(), new pSPacketChunkData.isFullChunk(),
                new pSPacketChunkData.getTileEntityTags(), new pSPacketChunkData.setavailableSections(), new pSPacketChunkData.setchunkX(),
                new pSPacketChunkData.setfullChunk(), new pSPacketChunkData.setchunkZ());
        subBlock(new pSPacketCloseWindow(), new pSPacketCloseWindow.windowId(), new pSPacketCloseWindow.setwindowId());
        subBlock(new pSPacketCollectItem(), new pSPacketCollectItem.getCollectedItemEntityID(),
                new pSPacketCollectItem.getEntityIDtem(), new pSPacketCollectItem.getAmount()
                , new pSPacketCollectItem.setentityIdtem(), new pSPacketCollectItem.setcollectedItemEntityId(), new pSPacketCollectItem.setcollectedQuantity());
        subBlock(new pSPacketCombatEvent(), new pSPacketCombatEvent.playerId(), new pSPacketCombatEvent.entityIdent(),
                new pSPacketCombatEvent.eventType(), new pSPacketCombatEvent.deathMessage(),
                new pSPacketCombatEvent.duration(), new pSPacketCombatEvent.setDuration(), new pSPacketCombatEvent.setDeathMessage(),
                new pSPacketCombatEvent.setEntityIdcom(), new pSPacketCombatEvent.seteventType(), new pSPacketCombatEvent.setPlayerId());
        subBlock(new pSPacketConfirmTransaction(), new pSPacketConfirmTransaction.pSPgetWindowId(),
                new pSPacketConfirmTransaction.getActionNumber(), new pSPacketConfirmTransaction.wasAccepted()
                , new pSPacketConfirmTransaction.setActionNumber(), new pSPacketConfirmTransaction.setWasAccepted(), new pSPacketConfirmTransaction.setWindowId());
        subBlock(new pSPacketCooldown(), new pSPacketCooldown.getItemown(), new pSPacketCooldown.getTicks(),
                new pSPacketCooldown.setItem(), new pSPacketCooldown.setTicks());
        subBlock(new pSPacketCustomPayload(), new pSPacketCustomPayload.getChannelNameoad(), new pSPacketCustomPayload.setChannel());
        subBlock(new pSPacketCustomSound(), new pSPacketCustomSound.getSoundName(),
                new pSPacketCustomSound.getCategory(), new pSPacketCustomSound.getXund(),
                new pSPacketCustomSound.getYund(), new pSPacketCustomSound.getZund(),
                new pSPacketCustomSound.getVolume(), new pSPacketCustomSound.getPitchund()
                , new pSPacketCustomSound.setCategory(), new pSPacketCustomSound.setSoundName(), new pSPacketCustomSound.setX(),
                new pSPacketCustomSound.setY(), new pSPacketCustomSound.setZ(), new pSPacketCustomSound.setVolume(), new pSPacketCustomSound.setPitch());
        subBlock(new pSPacketDestroyEntities(), new pSPacketDestroyEntities.EntityIDDes());
        subBlock(new pSPacketDisconnect());
        subBlock(new pSPacketDisplayObjective(), new pSPacketDisplayObjective.getPositionive(),
                new pSPacketDisplayObjective.getName(), new pSPacketDisplayObjective.setposition(), new pSPacketDisplayObjective.setscoreName());
        subBlock(new pSPacketEffect(), new pSPacketEffect.isSoundServerwide(), new pSPacketEffect.getSoundPos(),
                new pSPacketEffect.getSoundData(), new pSPacketEffect.getSoundType()
                , new pSPacketEffect.setSoundPos(), new pSPacketEffect.setSoundData(), new pSPacketEffect.setSoundType(), new pSPacketEffect.setSoundServerwide());
        subBlock(new pSPacketEnableCompression(), new pSPacketEnableCompression.getCompressionThreshold(),
                new pSPacketEnableCompression.setcompressionThreshold());
        subBlock(new pSPacketEncryptionRequest(), new pSPacketEncryptionRequest.getServerId(),
                new pSPacketEncryptionRequest.sethashedServerId());
        subBlock(new pSPacketEntity(), new pSPacketEntity.getEntityity(), new pSPacketEntity.getisRotating(),
                new pSPacketEntity.getOnGround(), new pSPacketEntity.getPitchity(), new pSPacketEntity.getXity(),
                new pSPacketEntity.getYity(), new pSPacketEntity.getZity(), new pSPacketEntity.getYawity(),
                new pSPacketEntity.setEntityIdity(), new pSPacketEntity.setPitchity(), new pSPacketEntity.setonGround(),
                new pSPacketEntity.setXity(), new pSPacketEntity.setYity(), new pSPacketEntity.setrotating(),
                new pSPacketEntity.setYaw(), new pSPacketEntity.setZity());
        subBlock(new pSPacketEntityAttach(), new pSPacketEntityAttach.getEntityId(), new pSPacketEntityAttach.getVehicleEntityId(),
                new pSPacketEntityAttach.setEntityIdach(), new pSPacketEntityAttach.setVehicleEntityId());
        subBlock(new pSPacketEntityEffect(), new pSPacketEntityEffect.getEntityIdect(), new pSPacketEntityEffect.getEffectId(),
                new pSPacketEntityEffect.getAmplifier(), new pSPacketEntityEffect.getDuration(),
                new pSPacketEntityEffect.isMaxDuration(), new pSPacketEntityEffect.getIsAmbient()
                , new pSPacketEntityEffect.setEntityIdect(), new pSPacketEntityEffect.setEffectId(), new pSPacketEntityEffect.setAmplifier(),
                new pSPacketEntityEffect.setDurationect(), new pSPacketEntityEffect.setflags());
        subBlock(new pSPacketEntityEquipment(), new pSPacketEntityEquipment.getEntityIDent(),
                new pSPacketEntityEquipment.getEquipmentSlot(), new pSPacketEntityEquipment.getItemStackent()
                , new pSPacketEntityEquipment.setEntityIDent(), new pSPacketEntityEquipment.setEquipmentSlot(), new pSPacketEntityEquipment.setItemStack());
        subBlock(new pSPacketEntityHeadLook(), new pSPacketEntityHeadLook.getEntityook(), new pSPacketEntityHeadLook.getYawook(),
                new pSPacketEntityHeadLook.setentityIdook(), new pSPacketEntityHeadLook.setYawook());
        subBlock(new pSPacketEntityMetadata(), new pSPacketEntityMetadata.getEntityIdata(), new pSPacketEntityMetadata.setentityIdata());
        subBlock(new pSPacketEntityProperties(), new pSPacketEntityProperties.getEntityIdies(),
                new pSPacketEntityProperties.getSnapshots(), new pSPacketEntityProperties.setentityIdies());
        subBlock(new pSPacketEntityStatus(), new pSPacketEntityStatus.getEntitytus(),
                new pSPacketEntityStatus.getOpCode(), new pSPacketEntityStatus.setentityIdtus(), new pSPacketEntityStatus.setOpCode());
        subBlock(new pSPacketEntityTeleport(), new pSPacketEntityTeleport.getEntityIdort(), new pSPacketEntityTeleport.getXort(),
                new pSPacketEntityTeleport.getYort(), new pSPacketEntityTeleport.getZort(),
                new pSPacketEntityTeleport.getYawort(), new pSPacketEntityTeleport.getPitchort(),
                new pSPacketEntityTeleport.getOnGroundort(), new pSPacketEntityTeleport.setEntityIdort(), new pSPacketEntityTeleport.setXort(),
                new pSPacketEntityTeleport.setYort(), new pSPacketEntityTeleport.setZort(), new pSPacketEntityTeleport.setYawort(),
                new pSPacketEntityTeleport.setPitchort(), new pSPacketEntityTeleport.setOnGround());
        subBlock(new pSPacketEntityVelocity(), new pSPacketEntityVelocity.getEntityIDity(), new pSPacketEntityVelocity.getMotionX(),
                new pSPacketEntityVelocity.getMotionY(), new pSPacketEntityVelocity.getMotionZ(),
                new pSPacketEntityVelocity.setEntityIDity(), new pSPacketEntityVelocity.setMotionX(), new pSPacketEntityVelocity.setMotionY(),
                new pSPacketEntityVelocity.setMotionZ());
        subBlock(new pSPacketExplosion(), new pSPacketExplosion.getXion(), new pSPacketExplosion.getYion(),
                new pSPacketExplosion.getZion(), new pSPacketExplosion.getMotionXion(), new pSPacketExplosion.getMotionYion(),
                new pSPacketExplosion.getMotionZion(), new pSPacketExplosion.getStrength(), new pSPacketExplosion.BlockPositions()
                , new pSPacketExplosion.setXion(), new pSPacketExplosion.setYion(), new pSPacketExplosion.setZion(),
                new pSPacketExplosion.setMotionXion(), new pSPacketExplosion.setMotionYion(), new pSPacketExplosion.setMotionZion(),
                new pSPacketExplosion.setStrength());
        subBlock(new pSPacketHeldItemChange(), new pSPacketHeldItemChange.HotbarIndex(), new pSPacketHeldItemChange.setheldItem());
        subBlock(new pSPacketJoinGame(), new pSPacketJoinGame.getPlayerId(), new pSPacketJoinGame.getDimension(),
                new pSPacketJoinGame.getMaxPlayers(), new pSPacketJoinGame.getGameType(), new pSPacketJoinGame.getWorldType(),
                new pSPacketJoinGame.getDifficulty(), new pSPacketJoinGame.setDifficulty(), new pSPacketJoinGame.setPlayerIdame(),
                new pSPacketJoinGame.setDimension(), new pSPacketJoinGame.setMaxPlayers(), new pSPacketJoinGame.setGameType(),
                new pSPacketJoinGame.setWorldType(), new pSPacketJoinGame.sethardcoreMode(), new pSPacketJoinGame.setReducedDebugInfo());
        subBlock(new pSPacketKeepAlive(), new pSPacketKeepAlive.getId(), new pSPacketKeepAlive.setid());
        subBlock(new pSPacketLoginSuccess());
        subBlock(new pSPacketMaps(), new pSPacketMaps.getMapId(), new pSPacketMaps.getcolumns()
                , new pSPacketMaps.getrows(), new pSPacketMaps.getminX(), new pSPacketMaps.getminZ()
                , new pSPacketMaps.getmapScale(), new pSPacketMaps.gettrackingPosition());

        subBlock(new pSPacketMoveVehicle(), new pSPacketMoveVehicle.getXcle(), new pSPacketMoveVehicle.getYcle(),
                new pSPacketMoveVehicle.getZcle(), new pSPacketMoveVehicle.getYawcle(),
                new pSPacketMoveVehicle.getPitchcle(), new pSPacketMoveVehicle.setXcle(), new pSPacketMoveVehicle.setYcle(),
                new pSPacketMoveVehicle.setZcle(), new pSPacketMoveVehicle.setYawcle(), new pSPacketMoveVehicle.setPitchcle());
        subBlock(new pSPacketMultiBlockChange());
        subBlock(new pSPacketOpenWindow(), new pSPacketOpenWindow.getWindowIddow(), new pSPacketOpenWindow.getGuiId(),
                new pSPacketOpenWindow.getWindowTitle(), new pSPacketOpenWindow.getSlotCount(),
                new pSPacketOpenWindow.getEntityIddow(), new pSPacketOpenWindow.setWindowIddow(), new pSPacketOpenWindow.setinventoryType(),
                new pSPacketOpenWindow.setWindowTitle(), new pSPacketOpenWindow.setSlotCount(), new pSPacketOpenWindow.setEntityIddow());
        subBlock(new pSPacketParticles(), new pSPacketParticles.getParticleType(), new pSPacketParticles.isLongDistance(),
                new pSPacketParticles.getXOffset(), new pSPacketParticles.getYOffset(),
                new pSPacketParticles.getZOffset(), new pSPacketParticles.getXCoordinate(),
                new pSPacketParticles.getYCoordinate(), new pSPacketParticles.getZCoordinate(),
                new pSPacketParticles.getParticleSpeed(), new pSPacketParticles.getParticleCount(),
                new pSPacketParticles.setParticleType(), new pSPacketParticles.setlongDistance(),
                new pSPacketParticles.setparticleCount(), new pSPacketParticles.setparticleSpeed(),
                new pSPacketParticles.setxCoord(), new pSPacketParticles.setyCoord(), new pSPacketParticles.setzCoord(),
                new pSPacketParticles.setxOffset(), new pSPacketParticles.setyOffset(), new pSPacketParticles.setzOffset());
        subBlock(new pSPacketPlaceGhostRecipe());
        subBlock(new pSPacketPlayerAbilities(), new pSPacketPlayerAbilities.isAllowFlyingies(),
                new pSPacketPlayerAbilities.isFlyingies(), new pSPacketPlayerAbilities.isCreativeModeies(),
                new pSPacketPlayerAbilities.isInvulnerableies(), new pSPacketPlayerAbilities.getFlySpeed(),
                new pSPacketPlayerAbilities.getWalkSpeed(), new pSPacketPlayerAbilities.setAllowFlying(),
                new pSPacketPlayerAbilities.setFlying(), new pSPacketPlayerAbilities.setcreativeMode(),
                new pSPacketPlayerAbilities.setInvulnerable(), new pSPacketPlayerAbilities.setFlySpeed(),
                new pSPacketPlayerAbilities.setWalkSpeed());
        subBlock(new pSPacketPlayerListHeaderFooter(), new pSPacketPlayerListHeaderFooter.getHeader(),
                new pSPacketPlayerListHeaderFooter.getFooter(), new pSPacketPlayerListHeaderFooter.setHeader(),
                new pSPacketPlayerListHeaderFooter.setFooter());
        subBlock(new pSPacketPlayerListItem(), new pSPacketPlayerListItem.getActiontem(), new pSPacketPlayerListItem.setaction());
        subBlock(new pSPacketPlayerPosLook(), new pSPacketPlayerPosLook.getXook(), new pSPacketPlayerPosLook.getYook(),
                new pSPacketPlayerPosLook.getZook(), new pSPacketPlayerPosLook.isFlagook(), new pSPacketPlayerPosLook.pSPgetYaw(),
                new pSPacketPlayerPosLook.getPitchook(), new pSPacketPlayerPosLook.getTeleportIdook()
                , new pSPacketPlayerPosLook.setXook(), new pSPacketPlayerPosLook.setYook(), new pSPacketPlayerPosLook.setZook(),
                new pSPacketPlayerPosLook.pSPsetYaw(), new pSPacketPlayerPosLook.setPitchook(),
                new pSPacketPlayerPosLook.setTeleportId());
        subBlock(new pSPacketPong());
        subBlock(new pSPacketRecipeBook(), new pSPacketRecipeBook.getStateook(), new pSPacketRecipeBook.isGuiOpenook(), new pSPacketRecipeBook.isFilteringCraftableook()
                , new pSPacketRecipeBook.setstateook(), new pSPacketRecipeBook.setGuiOpen(),
                new pSPacketRecipeBook.setFilteringCraftable());
        subBlock(new pSPacketRemoveEntityEffect(), new pSPacketRemoveEntityEffect.getEntityect(),
                new pSPacketRemoveEntityEffect.getPotion(), new pSPacketRemoveEntityEffect.setentityIdect(),
                new pSPacketRemoveEntityEffect.seteffectId());
        subBlock(new pSPacketResourcePackSend(), new pSPacketResourcePackSend.getURL(), new pSPacketResourcePackSend.getHash()
                , new pSPacketResourcePackSend.setURL(), new pSPacketResourcePackSend.setHash());
        subBlock(new pSPacketRespawn(), new pSPacketRespawn.getDimensionID(), new pSPacketRespawn.getDifficultyawn(),
                new pSPacketRespawn.getGameTypeawn(), new pSPacketRespawn.getWorldTypeawn()
                , new pSPacketRespawn.setDifficultyawn(), new pSPacketRespawn.setGameTypeawn(), new pSPacketRespawn.setWorldTypeawn()
                , new pSPacketRespawn.setDimensionID());
        subBlock(new pSPacketScoreboardObjective(), new pSPacketScoreboardObjective.getObjectiveName(),
                new pSPacketScoreboardObjective.getObjectiveValue(), new pSPacketScoreboardObjective.setobjectiveName(),
                new pSPacketScoreboardObjective.setobjectiveValue());
        subBlock(new pSPacketSelectAdvancementsTab());
        subBlock(new pSPacketServerDifficulty(), new pSPacketServerDifficulty.getDifficultylty(),
                new pSPacketServerDifficulty.isDifficultyLocked(), new pSPacketServerDifficulty.setDifficultylty(),
                new pSPacketServerDifficulty.setIsLocked());
        subBlock(new pSPacketServerInfo());
        subBlock(new pSPacketSetExperience(), new pSPacketSetExperience.getExperienceBar(),
                new pSPacketSetExperience.getLevel(), new pSPacketSetExperience.getTotalExperience()
                , new pSPacketSetExperience.setExperienceBar(), new pSPacketSetExperience.setLevel(),
                new pSPacketSetExperience.setTotalExperience());
        subBlock(new pSPacketSetPassengers(), new pSPacketSetPassengers.getEntityIders(),
                new pSPacketSetPassengers.getPassengerIds(), new pSPacketSetPassengers.setEntityIders());
        subBlock(new pSPacketSetSlot(), new pSPacketSetSlot.getWindowIdlot(), new pSPacketSetSlot.getSlot(),
                new pSPacketSetSlot.getStacklot(), new pSPacketSetSlot.setWindowIdlot(), new pSPacketSetSlot.setSlot(),
                new pSPacketSetSlot.setStack());
        subBlock(new pSPacketSignEditorOpen(), new pSPacketSignEditorOpen.getSignPosition()
                , new pSPacketSignEditorOpen.setsignPosition());
        subBlock(new pSPacketSoundEffect(), new pSPacketSoundEffect.getCategoryect(), new pSPacketSoundEffect.getXect(),
                new pSPacketSoundEffect.getYect(), new pSPacketSoundEffect.getZect(),
                new pSPacketSoundEffect.getVolumeect(), new pSPacketSoundEffect.getPitchect(), new pSPacketSoundEffect.getSoundEventect()
                , new pSPacketSoundEffect.setCategoryect(), new pSPacketSoundEffect.setXect(), new pSPacketSoundEffect.setYect(),
                new pSPacketSoundEffect.setZect(), new pSPacketSoundEffect.setVolumeect(), new pSPacketSoundEffect.setPitchect());
        subBlock(new pSPacketSpawnExperienceOrb(), new pSPacketSpawnExperienceOrb.getXOrb(),
                new pSPacketSpawnExperienceOrb.getYOrb(), new pSPacketSpawnExperienceOrb.getZOrb(),
                new pSPacketSpawnExperienceOrb.getEntityIDOrb(), new pSPacketSpawnExperienceOrb.getXPValue()
                , new pSPacketSpawnExperienceOrb.setXOrb(), new pSPacketSpawnExperienceOrb.setYOrb(), new pSPacketSpawnExperienceOrb.setZOrb(),
                new pSPacketSpawnExperienceOrb.setEntityIDOrb(), new pSPacketSpawnExperienceOrb.setXPValue());
        subBlock(new pSPacketSpawnGlobalEntity(), new pSPacketSpawnGlobalEntity.getEntityIdgid(),
                new pSPacketSpawnGlobalEntity.getTypegid(), new pSPacketSpawnGlobalEntity.getXgid(),
                new pSPacketSpawnGlobalEntity.getYgid(), new pSPacketSpawnGlobalEntity.getZgid()
                , new pSPacketSpawnGlobalEntity.setentityIdgid(), new pSPacketSpawnGlobalEntity.setTypegid(),
                new pSPacketSpawnGlobalEntity.setXgid(), new pSPacketSpawnGlobalEntity.setYgid(),
                new pSPacketSpawnGlobalEntity.setZgid());
        subBlock(new pSPacketSpawnMob(), new pSPacketSpawnMob.getEntityIDMob(), new pSPacketSpawnMob.getEntityType(),
                new pSPacketSpawnMob.getXMob(), new pSPacketSpawnMob.getYMob(), new pSPacketSpawnMob.getZMob(),
                new pSPacketSpawnMob.getYawMob(), new pSPacketSpawnMob.getPitchMob(),
                new pSPacketSpawnMob.getHeadPitch(), new pSPacketSpawnMob.getVelocityX(),
                new pSPacketSpawnMob.getVelocityY(), new pSPacketSpawnMob.getVelocityZ()
                , new pSPacketSpawnMob.setentityIdMob(), new pSPacketSpawnMob.settypeMob(),
                new pSPacketSpawnMob.setXMob(), new pSPacketSpawnMob.setYMob(), new pSPacketSpawnMob.setZMob(),
                new pSPacketSpawnMob.setYawMob(), new pSPacketSpawnMob.setPitchMob(),
                new pSPacketSpawnMob.setHeadPitch(), new pSPacketSpawnMob.setVelX(),
                new pSPacketSpawnMob.setVelY(), new pSPacketSpawnMob.setVelZ());
        subBlock(new pSPacketSpawnObject(), new pSPacketSpawnObject.getEntityIDpso(),
                new pSPacketSpawnObject.getTypepso(), new pSPacketSpawnObject.getXpso(),
                new pSPacketSpawnObject.getYpso(), new pSPacketSpawnObject.getZpso(),
                new pSPacketSpawnObject.getYawpso(), new pSPacketSpawnObject.getPitchpso(),
                new pSPacketSpawnObject.getData(), new pSPacketSpawnObject.getSpeedX(),
                new pSPacketSpawnObject.getSpeedY(), new pSPacketSpawnObject.getSpeedZ(),
                new pSPacketSpawnObject.setentityIdpso(), new pSPacketSpawnObject.settypepso(),
                new pSPacketSpawnObject.setXpso(), new pSPacketSpawnObject.setYpso(), new pSPacketSpawnObject.setZpso(),
                new pSPacketSpawnObject.setYawpso(), new pSPacketSpawnObject.setPitchpso(),
                new pSPacketSpawnObject.setdata(), new pSPacketSpawnObject.setMotionXpso(),
                new pSPacketSpawnObject.setMotionYpso(), new pSPacketSpawnObject.setMotionZpso());
        subBlock(new pSPacketSpawnPainting(), new pSPacketSpawnPainting.getEntityIDing(),
                new pSPacketSpawnPainting.getTitle(), new pSPacketSpawnPainting.getPositioning(),
                new pSPacketSpawnPainting.getFacinging(),
                new pSPacketSpawnPainting.setEntityIDing(), new pSPacketSpawnPainting.setTitle(),
                new pSPacketSpawnPainting.setPositioning(), new pSPacketSpawnPainting.setFacing());
        subBlock(new pSPacketSpawnPlayer(), new pSPacketSpawnPlayer.getEntityIDyer(),
                new pSPacketSpawnPlayer.getXyer(), new pSPacketSpawnPlayer.getYyer(),
                new pSPacketSpawnPlayer.getZyer(), new pSPacketSpawnPlayer.getYawyer(),
                new pSPacketSpawnPlayer.getPitchyer(),
                new pSPacketSpawnPlayer.setentityIdyer(), new pSPacketSpawnPlayer.setXyer(),
                new pSPacketSpawnPlayer.setYyer(), new pSPacketSpawnPlayer.setZyer(),
                new pSPacketSpawnPlayer.setYawyer(), new pSPacketSpawnPlayer.setPitchyer());
        subBlock(new pSPacketSpawnPosition(), new pSPacketSpawnPosition.getSpawnPos(),
                new pSPacketSpawnPosition.setspawnBlockPos());
        subBlock(new pSPacketStatistics());
        subBlock(new pSPacketTabComplete(), new pSPacketTabComplete.getMatches(),
                new pSPacketTabComplete.addmatches(), new pSPacketTabComplete.removematches());
        subBlock(new pSPacketTeams(), new pSPacketTeams.getNameams(), new pSPacketTeams.getActionams(),
                new pSPacketTeams.getColor(), new pSPacketTeams.getFriendlyFlags(),
                new pSPacketTeams.getDisplayName(), new pSPacketTeams.getCollisionRule(),
                new pSPacketTeams.getNameTagVisibility(), new pSPacketTeams.getPrefix(),
                new pSPacketTeams.getSuffix(), new pSPacketTeams.getPlayers(),
                new pSPacketTeams.setName(), new pSPacketTeams.setactionams(),
                new pSPacketTeams.setColorams(), new pSPacketTeams.setFriendlyFlags(),
                new pSPacketTeams.setdisplayName(), new pSPacketTeams.setCollisionRule(),
                new pSPacketTeams.setNameTagVisibility(), new pSPacketTeams.setprefix(),
                new pSPacketTeams.setsuffix());
        subBlock(new pSPacketTimeUpdate(), new pSPacketTimeUpdate.getWorldTime(),
                new pSPacketTimeUpdate.getTotalWorldTime(), new pSPacketTimeUpdate.setWorldTimepacket(),
                new pSPacketTimeUpdate.setTotalWorldTime());
        subBlock(new pSPacketTitle(), new pSPacketTitle.getDisplayTime(), new pSPacketTitle.getFadeInTime(),
                new pSPacketTitle.getFadeOutTime(), new pSPacketTitle.getMessagetle(),
                new pSPacketTitle.getTypetle(), new pSPacketTitle.setDisplayTime(),
                new pSPacketTitle.setFadeInTime(), new pSPacketTitle.setFadeOutTime(),
                new pSPacketTitle.setMessage(), new pSPacketTitle.setTypetle());
        subBlock(new pSPacketUnloadChunk(), new pSPacketUnloadChunk.getXunk(), new pSPacketUnloadChunk.getZunk()
                , new pSPacketUnloadChunk.setChunkX(), new pSPacketUnloadChunk.setChunkZ());
        subBlock(new pSPacketUpdateBossInfo(), new pSPacketUpdateBossInfo.getPercent(),
                new pSPacketUpdateBossInfo.shouldCreateFog(), new pSPacketUpdateBossInfo.shouldDarkenSky(),
                new pSPacketUpdateBossInfo.shouldPlayEndBossMusic(), new pSPacketUpdateBossInfo.setPercent(),
                new pSPacketUpdateBossInfo.setCreateFog(), new pSPacketUpdateBossInfo.setDarkenSky(),
                new pSPacketUpdateBossInfo.setPlayEndBossMusic());
        subBlock(new pSPacketUpdateHealth(), new pSPacketUpdateHealth.getHealth(),
                new pSPacketUpdateHealth.getFoodLevel(), new pSPacketUpdateHealth.getSaturationLevel(),
                new pSPacketUpdateHealth.setHealth(), new pSPacketUpdateHealth.setFoodLevel(),
                new pSPacketUpdateHealth.setSaturationLevel());
        subBlock(new pSPacketUpdateScore(), new pSPacketUpdateScore.getScoreAction(),
                new pSPacketUpdateScore.getObjectiveNameore(), new pSPacketUpdateScore.getPlayerName(),
                new pSPacketUpdateScore.getScoreValue(), new pSPacketUpdateScore.setScoreAction(),
                new pSPacketUpdateScore.setObjectiveName(), new pSPacketUpdateScore.setobjective(),
                new pSPacketUpdateScore.setScoreValue());
        subBlock(new pSPacketUpdateTileEntity(), new pSPacketUpdateTileEntity.getPosity(),
                new pSPacketUpdateTileEntity.getTileEntityType(),
                new pSPacketUpdateTileEntity.setPos(), new pSPacketUpdateTileEntity.setTileEntityType());
        subBlock(new pSPacketUseBed(), new pSPacketUseBed.getBedPosition(), new pSPacketUseBed.getPlayer(),
                new pSPacketUseBed.setPlayerID(), new pSPacketUseBed.setPosBed());
        subBlock(new pSPacketWindowItems(), new pSPacketWindowItems.getWindowIdems(),
                new pSPacketWindowItems.getItemStacks(), new pSPacketWindowItems.setWindowIdems());
        subBlock(new pSPacketWindowProperty(), new pSPacketWindowProperty.getWindowIdrty(), new pSPacketWindowProperty.getProperty(),
                new pSPacketWindowProperty.getValuerty(), new pSPacketWindowProperty.setWindowIdrty(), new pSPacketWindowProperty.setProperty(),
                new pSPacketWindowProperty.setValue());
        subBlock(new pSPacketWorldBorder());
    }

    private void subBlock(Block main, Block... subs) {
        AllBlocks.add(main);
        if (subs.length > 0) {
            main.setparentBlock(main);
            Collections.addAll(AllBlocks, subs);
            Arrays.stream(subs).forEach(sub -> sub.setparentBlock(main));
        }
    }


}
