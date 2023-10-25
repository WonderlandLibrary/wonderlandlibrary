/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiRenameWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
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
    private boolean field_146643_x;
    protected String field_146628_f = "Select world";
    private final DateFormat field_146633_h = new SimpleDateFormat();
    private static final Logger logger = LogManager.getLogger();
    private List field_146638_t;
    private GuiButton selectButton;
    private java.util.List<SaveFormatComparator> field_146639_s;
    private String field_146637_u;
    private GuiButton recreateButton;
    private boolean field_146634_i;
    protected GuiScreen parentScreen;
    private String field_146636_v;
    private GuiButton renameButton;
    private GuiButton deleteButton;
    private int field_146640_r;
    private String[] field_146635_w = new String[4];

    protected String func_146614_d(int n) {
        String string = this.field_146639_s.get(n).getDisplayName();
        if (StringUtils.isEmpty((CharSequence)string)) {
            string = String.valueOf(I18n.format("selectWorld.world", new Object[0])) + " " + (n + 1);
        }
        return string;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146638_t.handleMouseInput();
    }

    public void func_146615_e(int n) {
        this.mc.displayGuiScreen(null);
        if (!this.field_146634_i) {
            String string;
            this.field_146634_i = true;
            String string2 = this.func_146621_a(n);
            if (string2 == null) {
                string2 = "World" + n;
            }
            if ((string = this.func_146614_d(n)) == null) {
                string = "World" + n;
            }
            if (this.mc.getSaveLoader().canLoadWorld(string2)) {
                this.mc.launchIntegratedServer(string2, string, null);
            }
        }
    }

    public void func_146618_g() {
        this.selectButton = new GuiButton(1, width / 2 - 154, height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0]));
        this.buttonList.add(this.selectButton);
        this.buttonList.add(new GuiButton(3, width / 2 + 4, height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.renameButton = new GuiButton(6, width / 2 - 154, height - 28, 72, 20, I18n.format("selectWorld.rename", new Object[0]));
        this.buttonList.add(this.renameButton);
        this.deleteButton = new GuiButton(2, width / 2 - 76, height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0]));
        this.buttonList.add(this.deleteButton);
        this.recreateButton = new GuiButton(7, width / 2 + 4, height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0]));
        this.buttonList.add(this.recreateButton);
        this.buttonList.add(new GuiButton(0, width / 2 + 82, height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
        this.selectButton.enabled = false;
        this.deleteButton.enabled = false;
        this.renameButton.enabled = false;
        this.recreateButton.enabled = false;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.field_146638_t.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, this.field_146628_f, width / 2, 20, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    private void func_146627_h() throws AnvilConverterException {
        ISaveFormat iSaveFormat = this.mc.getSaveLoader();
        this.field_146639_s = iSaveFormat.getSaveList();
        Collections.sort(this.field_146639_s);
        this.field_146640_r = -1;
    }

    protected String func_146621_a(int n) {
        return this.field_146639_s.get(n).getFileName();
    }

    @Override
    public void confirmClicked(boolean bl, int n) {
        if (this.field_146643_x) {
            this.field_146643_x = false;
            if (bl) {
                ISaveFormat iSaveFormat = this.mc.getSaveLoader();
                iSaveFormat.flushCache();
                iSaveFormat.deleteWorldDirectory(this.func_146621_a(n));
                try {
                    this.func_146627_h();
                }
                catch (AnvilConverterException anvilConverterException) {
                    logger.error("Couldn't load level list", (Throwable)anvilConverterException);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }

    @Override
    public void initGui() {
        this.field_146628_f = I18n.format("selectWorld.title", new Object[0]);
        try {
            this.func_146627_h();
        }
        catch (AnvilConverterException anvilConverterException) {
            logger.error("Couldn't load level list", (Throwable)anvilConverterException);
            this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", anvilConverterException.getMessage()));
            return;
        }
        this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
        this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SPECTATOR.getID()] = I18n.format("gameMode.spectator", new Object[0]);
        this.field_146638_t = new List(this.mc);
        this.field_146638_t.registerScrollButtons(4, 5);
        this.func_146618_g();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 2) {
                String string = this.func_146614_d(this.field_146640_r);
                if (string != null) {
                    this.field_146643_x = true;
                    GuiYesNo guiYesNo = GuiSelectWorld.func_152129_a(this, string, this.field_146640_r);
                    this.mc.displayGuiScreen(guiYesNo);
                }
            } else if (guiButton.id == 1) {
                this.func_146615_e(this.field_146640_r);
            } else if (guiButton.id == 3) {
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
            } else if (guiButton.id == 6) {
                this.mc.displayGuiScreen(new GuiRenameWorld(this, this.func_146621_a(this.field_146640_r)));
            } else if (guiButton.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton.id == 7) {
                GuiCreateWorld guiCreateWorld = new GuiCreateWorld(this);
                ISaveHandler iSaveHandler = this.mc.getSaveLoader().getSaveLoader(this.func_146621_a(this.field_146640_r), false);
                WorldInfo worldInfo = iSaveHandler.loadWorldInfo();
                iSaveHandler.flush();
                guiCreateWorld.func_146318_a(worldInfo);
                this.mc.displayGuiScreen(guiCreateWorld);
            } else {
                this.field_146638_t.actionPerformed(guiButton);
            }
        }
    }

    public GuiSelectWorld(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
    }

    public static GuiYesNo func_152129_a(GuiYesNoCallback guiYesNoCallback, String string, int n) {
        String string2 = I18n.format("selectWorld.deleteQuestion", new Object[0]);
        String string3 = "'" + string + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
        String string4 = I18n.format("selectWorld.deleteButton", new Object[0]);
        String string5 = I18n.format("gui.cancel", new Object[0]);
        GuiYesNo guiYesNo = new GuiYesNo(guiYesNoCallback, string2, string3, string4, string5, n);
        return guiYesNo;
    }

    class List
    extends GuiSlot {
        public List(Minecraft minecraft) {
            super(minecraft, width, height, 32, height - 64, 36);
        }

        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
            boolean bl2;
            GuiSelectWorld.this.field_146640_r = n;
            ((GuiSelectWorld)GuiSelectWorld.this).selectButton.enabled = bl2 = GuiSelectWorld.this.field_146640_r >= 0 && GuiSelectWorld.this.field_146640_r < this.getSize();
            ((GuiSelectWorld)GuiSelectWorld.this).deleteButton.enabled = bl2;
            ((GuiSelectWorld)GuiSelectWorld.this).renameButton.enabled = bl2;
            ((GuiSelectWorld)GuiSelectWorld.this).recreateButton.enabled = bl2;
            if (bl && bl2) {
                GuiSelectWorld.this.func_146615_e(n);
            }
        }

        @Override
        protected boolean isSelected(int n) {
            return n == GuiSelectWorld.this.field_146640_r;
        }

        @Override
        protected void drawBackground() {
            GuiSelectWorld.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            SaveFormatComparator saveFormatComparator = (SaveFormatComparator)GuiSelectWorld.this.field_146639_s.get(n);
            String string = saveFormatComparator.getDisplayName();
            if (StringUtils.isEmpty((CharSequence)string)) {
                string = String.valueOf(GuiSelectWorld.this.field_146637_u) + " " + (n + 1);
            }
            String string2 = saveFormatComparator.getFileName();
            string2 = String.valueOf(string2) + " (" + GuiSelectWorld.this.field_146633_h.format(new Date(saveFormatComparator.getLastTimePlayed()));
            string2 = String.valueOf(string2) + ")";
            String string3 = "";
            if (saveFormatComparator.requiresConversion()) {
                string3 = String.valueOf(GuiSelectWorld.this.field_146636_v) + " " + string3;
            } else {
                string3 = GuiSelectWorld.this.field_146635_w[saveFormatComparator.getEnumGameType().getID()];
                if (saveFormatComparator.isHardcoreModeEnabled()) {
                    string3 = (Object)((Object)EnumChatFormatting.DARK_RED) + I18n.format("gameMode.hardcore", new Object[0]) + (Object)((Object)EnumChatFormatting.RESET);
                }
                if (saveFormatComparator.getCheatsEnabled()) {
                    string3 = String.valueOf(string3) + ", " + I18n.format("selectWorld.cheats", new Object[0]);
                }
            }
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, string, n2 + 2, n3 + 1, 0xFFFFFF);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, string2, n2 + 2, n3 + 12, 0x808080);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, string3, n2 + 2, n3 + 12 + 10, 0x808080);
        }

        @Override
        protected int getContentHeight() {
            return GuiSelectWorld.this.field_146639_s.size() * 36;
        }

        @Override
        protected int getSize() {
            return GuiSelectWorld.this.field_146639_s.size();
        }
    }
}

