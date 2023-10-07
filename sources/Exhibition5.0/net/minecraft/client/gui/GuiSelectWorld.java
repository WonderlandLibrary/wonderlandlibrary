// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.util.EnumChatFormatting;
import java.util.Date;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.ISaveFormat;
import java.util.Collections;
import java.io.IOException;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.resources.I18n;
import java.text.SimpleDateFormat;
import java.util.List;
import java.text.DateFormat;
import org.apache.logging.log4j.Logger;

public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback
{
    private static final Logger logger;
    private final DateFormat field_146633_h;
    protected GuiScreen field_146632_a;
    protected String field_146628_f;
    private boolean field_146634_i;
    private int field_146640_r;
    private java.util.List field_146639_s;
    private List field_146638_t;
    private String field_146637_u;
    private String field_146636_v;
    private String[] field_146635_w;
    private boolean field_146643_x;
    private GuiButton field_146642_y;
    private GuiButton field_146641_z;
    private GuiButton field_146630_A;
    private GuiButton field_146631_B;
    private static final String __OBFID = "CL_00000711";
    
    public GuiSelectWorld(final GuiScreen p_i1054_1_) {
        this.field_146633_h = new SimpleDateFormat();
        this.field_146628_f = "Select world";
        this.field_146635_w = new String[4];
        this.field_146632_a = p_i1054_1_;
    }
    
    @Override
    public void initGui() {
        this.field_146628_f = I18n.format("selectWorld.title", new Object[0]);
        try {
            this.func_146627_h();
        }
        catch (AnvilConverterException var2) {
            GuiSelectWorld.logger.error("Couldn't load level list", (Throwable)var2);
            this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", var2.getMessage()));
            return;
        }
        this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
        this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SPECTATOR.getID()] = I18n.format("gameMode.spectator", new Object[0]);
        (this.field_146638_t = new List(this.mc)).registerScrollButtons(4, 5);
        this.func_146618_g();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146638_t.handleMouseScrolling();
    }
    
    private void func_146627_h() throws AnvilConverterException {
        final ISaveFormat var1 = this.mc.getSaveLoader();
        Collections.sort((java.util.List<Comparable>)(this.field_146639_s = var1.getSaveList()));
        this.field_146640_r = -1;
    }
    
    protected String func_146621_a(final int p_146621_1_) {
        return this.field_146639_s.get(p_146621_1_).getFileName();
    }
    
    protected String func_146614_d(final int p_146614_1_) {
        String var2 = this.field_146639_s.get(p_146614_1_).getDisplayName();
        if (StringUtils.isEmpty((CharSequence)var2)) {
            var2 = I18n.format("selectWorld.world", new Object[0]) + " " + (p_146614_1_ + 1);
        }
        return var2;
    }
    
    public void func_146618_g() {
        this.buttonList.add(this.field_146641_z = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0])));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.buttonList.add(this.field_146630_A = new GuiButton(6, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.rename", new Object[0])));
        this.buttonList.add(this.field_146642_y = new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0])));
        this.buttonList.add(this.field_146631_B = new GuiButton(7, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
        this.field_146641_z.enabled = false;
        this.field_146642_y.enabled = false;
        this.field_146630_A.enabled = false;
        this.field_146631_B.enabled = false;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                final String var2 = this.func_146614_d(this.field_146640_r);
                if (var2 != null) {
                    this.field_146643_x = true;
                    final GuiYesNo var3 = func_152129_a(this, var2, this.field_146640_r);
                    this.mc.displayGuiScreen(var3);
                }
            }
            else if (button.id == 1) {
                this.func_146615_e(this.field_146640_r);
            }
            else if (button.id == 3) {
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (button.id == 6) {
                this.mc.displayGuiScreen(new GuiRenameWorld(this, this.func_146621_a(this.field_146640_r)));
            }
            else if (button.id == 0) {
                this.mc.displayGuiScreen(this.field_146632_a);
            }
            else if (button.id == 7) {
                final GuiCreateWorld var4 = new GuiCreateWorld(this);
                final ISaveHandler var5 = this.mc.getSaveLoader().getSaveLoader(this.func_146621_a(this.field_146640_r), false);
                final WorldInfo var6 = var5.loadWorldInfo();
                var5.flush();
                var4.func_146318_a(var6);
                this.mc.displayGuiScreen(var4);
            }
            else {
                this.field_146638_t.actionPerformed(button);
            }
        }
    }
    
    public void func_146615_e(final int p_146615_1_) {
        this.mc.displayGuiScreen(null);
        if (!this.field_146634_i) {
            this.field_146634_i = true;
            String var2 = this.func_146621_a(p_146615_1_);
            if (var2 == null) {
                var2 = "World" + p_146615_1_;
            }
            String var3 = this.func_146614_d(p_146615_1_);
            if (var3 == null) {
                var3 = "World" + p_146615_1_;
            }
            if (this.mc.getSaveLoader().canLoadWorld(var2)) {
                this.mc.launchIntegratedServer(var2, var3, null);
            }
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (this.field_146643_x) {
            this.field_146643_x = false;
            if (result) {
                final ISaveFormat var3 = this.mc.getSaveLoader();
                var3.flushCache();
                var3.deleteWorldDirectory(this.func_146621_a(id));
                try {
                    this.func_146627_h();
                }
                catch (AnvilConverterException var4) {
                    GuiSelectWorld.logger.error("Couldn't load level list", (Throwable)var4);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.field_146638_t.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_146628_f, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public static GuiYesNo func_152129_a(final GuiYesNoCallback p_152129_0_, final String p_152129_1_, final int p_152129_2_) {
        final String var3 = I18n.format("selectWorld.deleteQuestion", new Object[0]);
        final String var4 = "'" + p_152129_1_ + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
        final String var5 = I18n.format("selectWorld.deleteButton", new Object[0]);
        final String var6 = I18n.format("gui.cancel", new Object[0]);
        final GuiYesNo var7 = new GuiYesNo(p_152129_0_, var3, var4, var5, var6, p_152129_2_);
        return var7;
    }
    
    static {
        logger = LogManager.getLogger();
    }
    
    class List extends GuiSlot
    {
        private static final String __OBFID = "CL_00000712";
        
        public List(final Minecraft mcIn) {
            super(mcIn, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32, GuiSelectWorld.this.height - 64, 36);
        }
        
        @Override
        protected int getSize() {
            return GuiSelectWorld.this.field_146639_s.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            GuiSelectWorld.this.field_146640_r = slotIndex;
            final boolean var5 = GuiSelectWorld.this.field_146640_r >= 0 && GuiSelectWorld.this.field_146640_r < this.getSize();
            GuiSelectWorld.this.field_146641_z.enabled = var5;
            GuiSelectWorld.this.field_146642_y.enabled = var5;
            GuiSelectWorld.this.field_146630_A.enabled = var5;
            GuiSelectWorld.this.field_146631_B.enabled = var5;
            if (isDoubleClick && var5) {
                GuiSelectWorld.this.func_146615_e(slotIndex);
            }
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == GuiSelectWorld.this.field_146640_r;
        }
        
        @Override
        protected int getContentHeight() {
            return GuiSelectWorld.this.field_146639_s.size() * 36;
        }
        
        @Override
        protected void drawBackground() {
            GuiSelectWorld.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int p_180791_1_, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int p_180791_5_, final int p_180791_6_) {
            final SaveFormatComparator var7 = GuiSelectWorld.this.field_146639_s.get(p_180791_1_);
            String var8 = var7.getDisplayName();
            if (StringUtils.isEmpty((CharSequence)var8)) {
                var8 = GuiSelectWorld.this.field_146637_u + " " + (p_180791_1_ + 1);
            }
            String var9 = var7.getFileName();
            var9 = var9 + " (" + GuiSelectWorld.this.field_146633_h.format(new Date(var7.getLastTimePlayed()));
            var9 += ")";
            String var10 = "";
            if (var7.requiresConversion()) {
                var10 = GuiSelectWorld.this.field_146636_v + " " + var10;
            }
            else {
                var10 = GuiSelectWorld.this.field_146635_w[var7.getEnumGameType().getID()];
                if (var7.isHardcoreModeEnabled()) {
                    var10 = EnumChatFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0]) + EnumChatFormatting.RESET;
                }
                if (var7.getCheatsEnabled()) {
                    var10 = var10 + ", " + I18n.format("selectWorld.cheats", new Object[0]);
                }
            }
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, var8, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, var9, p_180791_2_ + 2, p_180791_3_ + 12, 8421504);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, var10, p_180791_2_ + 2, p_180791_3_ + 12 + 10, 8421504);
        }
    }
}
