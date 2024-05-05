package net.minecraft.client.gui;

import intent.AquaDev.aqua.Aqua;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiRenameWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiSelectWorld
extends GuiScreen
implements GuiYesNoCallback {
    private static final Logger logger = LogManager.getLogger();
    private final DateFormat field_146633_h = new SimpleDateFormat();
    protected GuiScreen parentScreen;
    protected String screenTitle = "Select world";
    private boolean field_146634_i;
    private int selectedIndex;
    private java.util.List<SaveFormatComparator> field_146639_s;
    private List availableWorlds;
    private String field_146637_u;
    private String field_146636_v;
    private String[] field_146635_w = new String[4];
    private boolean confirmingDelete;
    private GuiButton deleteButton;
    private GuiButton selectButton;
    private GuiButton renameButton;
    private GuiButton recreateButton;

    public GuiSelectWorld(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    public void initGui() {
        Aqua.moduleManager.getModuleByName("Arraylist").setState(true);
        Aqua.moduleManager.getModuleByName("HUD").setState(true);
        Aqua.moduleManager.getModuleByName("Blur").setState(true);
        Aqua.moduleManager.getModuleByName("Shadow").setState(true);
        Aqua.moduleManager.getModuleByName("Disabler").setState(false);
        Aqua.INSTANCE.lastConnection = System.currentTimeMillis();
        this.screenTitle = I18n.format((String)"selectWorld.title", (Object[])new Object[0]);
        try {
            this.loadLevelList();
        }
        catch (AnvilConverterException anvilconverterexception) {
            logger.error("Couldn't load level list", (Throwable)anvilconverterexception);
            this.mc.displayGuiScreen((GuiScreen)new GuiErrorScreen("Unable to load worlds", anvilconverterexception.getMessage()));
            return;
        }
        this.field_146637_u = I18n.format((String)"selectWorld.world", (Object[])new Object[0]);
        this.field_146636_v = I18n.format((String)"selectWorld.conversion", (Object[])new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format((String)"gameMode.survival", (Object[])new Object[0]);
        this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format((String)"gameMode.creative", (Object[])new Object[0]);
        this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format((String)"gameMode.adventure", (Object[])new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SPECTATOR.getID()] = I18n.format((String)"gameMode.spectator", (Object[])new Object[0]);
        this.availableWorlds = new List(this, this.mc);
        this.availableWorlds.registerScrollButtons(4, 5);
        this.addWorldSelectionButtons();
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.availableWorlds.handleMouseInput();
    }

    private void loadLevelList() throws AnvilConverterException {
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        this.field_146639_s = isaveformat.getSaveList();
        Collections.sort(this.field_146639_s);
        this.selectedIndex = -1;
    }

    protected String func_146621_a(int p_146621_1_) {
        return ((SaveFormatComparator)this.field_146639_s.get(p_146621_1_)).getFileName();
    }

    protected String func_146614_d(int p_146614_1_) {
        String s = ((SaveFormatComparator)this.field_146639_s.get(p_146614_1_)).getDisplayName();
        if (StringUtils.isEmpty((CharSequence)s)) {
            s = I18n.format((String)"selectWorld.world", (Object[])new Object[0]) + " " + (p_146614_1_ + 1);
        }
        return s;
    }

    public void addWorldSelectionButtons() {
        this.selectButton = new GuiButton(1, width / 2 - 154, height - 52, 150, 20, I18n.format((String)"selectWorld.select", (Object[])new Object[0]));
        this.buttonList.add((Object)this.selectButton);
        this.buttonList.add((Object)new GuiButton(3, width / 2 + 4, height - 52, 150, 20, I18n.format((String)"selectWorld.create", (Object[])new Object[0])));
        this.renameButton = new GuiButton(6, width / 2 - 154, height - 28, 72, 20, I18n.format((String)"selectWorld.rename", (Object[])new Object[0]));
        this.buttonList.add((Object)this.renameButton);
        this.deleteButton = new GuiButton(2, width / 2 - 76, height - 28, 72, 20, I18n.format((String)"selectWorld.delete", (Object[])new Object[0]));
        this.buttonList.add((Object)this.deleteButton);
        this.recreateButton = new GuiButton(7, width / 2 + 4, height - 28, 72, 20, I18n.format((String)"selectWorld.recreate", (Object[])new Object[0]));
        this.buttonList.add((Object)this.recreateButton);
        this.buttonList.add((Object)new GuiButton(0, width / 2 + 82, height - 28, 72, 20, I18n.format((String)"gui.cancel", (Object[])new Object[0])));
        this.selectButton.enabled = false;
        this.deleteButton.enabled = false;
        this.renameButton.enabled = false;
        this.recreateButton.enabled = false;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        Aqua.moduleManager.getModuleByName("Disabler").setState(false);
        if (button.enabled) {
            if (button.id == 2) {
                String s = this.func_146614_d(this.selectedIndex);
                if (s != null) {
                    this.confirmingDelete = true;
                    GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo(this, s, this.selectedIndex);
                    this.mc.displayGuiScreen((GuiScreen)guiyesno);
                }
            } else if (button.id == 1) {
                this.func_146615_e(this.selectedIndex);
            } else if (button.id == 3) {
                this.mc.displayGuiScreen((GuiScreen)new GuiCreateWorld((GuiScreen)this));
            } else if (button.id == 6) {
                this.mc.displayGuiScreen((GuiScreen)new GuiRenameWorld((GuiScreen)this, this.func_146621_a(this.selectedIndex)));
            } else if (button.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == 7) {
                GuiCreateWorld guicreateworld = new GuiCreateWorld((GuiScreen)this);
                ISaveHandler isavehandler = this.mc.getSaveLoader().getSaveLoader(this.func_146621_a(this.selectedIndex), false);
                WorldInfo worldinfo = isavehandler.loadWorldInfo();
                isavehandler.flush();
                guicreateworld.recreateFromExistingWorld(worldinfo);
                this.mc.displayGuiScreen((GuiScreen)guicreateworld);
            } else {
                this.availableWorlds.actionPerformed(button);
            }
        }
    }

    public void func_146615_e(int p_146615_1_) {
        this.mc.displayGuiScreen((GuiScreen)null);
        if (!this.field_146634_i) {
            String s1;
            this.field_146634_i = true;
            String s = this.func_146621_a(p_146615_1_);
            if (s == null) {
                s = "World" + p_146615_1_;
            }
            if ((s1 = this.func_146614_d(p_146615_1_)) == null) {
                s1 = "World" + p_146615_1_;
            }
            if (this.mc.getSaveLoader().canLoadWorld(s)) {
                this.mc.launchIntegratedServer(s, s1, (WorldSettings)null);
            }
        }
    }

    public void confirmClicked(boolean result, int id) {
        if (this.confirmingDelete) {
            this.confirmingDelete = false;
            if (result) {
                ISaveFormat isaveformat = this.mc.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(this.func_146621_a(id));
                try {
                    this.loadLevelList();
                }
                catch (AnvilConverterException anvilconverterexception) {
                    logger.error("Couldn't load level list", (Throwable)anvilconverterexception);
                }
            }
            this.mc.displayGuiScreen((GuiScreen)this);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Aqua.INSTANCE.shaderBackground.renderShader();
        Aqua.moduleManager.getModuleByName("Disabler").setState(false);
        this.availableWorlds.drawScreen(mouseX, mouseY, partialTicks);
        Aqua.INSTANCE.comfortaa3.drawCenteredString(this.screenTitle, (float)width / 2.0f, 20.0f, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static GuiYesNo makeDeleteWorldYesNo(GuiYesNoCallback selectWorld, String name, int id) {
        String s = I18n.format((String)"selectWorld.deleteQuestion", (Object[])new Object[0]);
        String s1 = "'" + name + "' " + I18n.format((String)"selectWorld.deleteWarning", (Object[])new Object[0]);
        String s2 = I18n.format((String)"selectWorld.deleteButton", (Object[])new Object[0]);
        String s3 = I18n.format((String)"gui.cancel", (Object[])new Object[0]);
        GuiYesNo guiyesno = new GuiYesNo(selectWorld, s, s1, s2, s3, id);
        return guiyesno;
    }

    static /* synthetic */ java.util.List access$000(GuiSelectWorld x0) {
        return x0.field_146639_s;
    }

    static /* synthetic */ int access$102(GuiSelectWorld x0, int x1) {
        x0.selectedIndex = x1;
        return x0.selectedIndex;
    }

    static /* synthetic */ int access$100(GuiSelectWorld x0) {
        return x0.selectedIndex;
    }

    static /* synthetic */ GuiButton access$200(GuiSelectWorld x0) {
        return x0.selectButton;
    }

    static /* synthetic */ GuiButton access$300(GuiSelectWorld x0) {
        return x0.deleteButton;
    }

    static /* synthetic */ GuiButton access$400(GuiSelectWorld x0) {
        return x0.renameButton;
    }

    static /* synthetic */ GuiButton access$500(GuiSelectWorld x0) {
        return x0.recreateButton;
    }

    static /* synthetic */ String access$600(GuiSelectWorld x0) {
        return x0.field_146637_u;
    }

    static /* synthetic */ DateFormat access$700(GuiSelectWorld x0) {
        return x0.field_146633_h;
    }

    static /* synthetic */ String access$800(GuiSelectWorld x0) {
        return x0.field_146636_v;
    }

    static /* synthetic */ String[] access$900(GuiSelectWorld x0) {
        return x0.field_146635_w;
    }
}
