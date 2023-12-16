// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.client.gui.GuiIngame;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import net.minecraft.scoreboard.Score;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumChatFormatting;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import moonsense.features.SCModule;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import moonsense.settings.Setting;
import moonsense.features.SCAbstractRenderModule;

public class ScoreboardModule extends SCAbstractRenderModule
{
    public static ScoreboardModule INSTANCE;
    private final Setting hideNumbers;
    private final Setting textShadow;
    private final Setting borderThickness;
    private final Setting border;
    private final Setting backgroundColor;
    private final Setting borderColor;
    private ScoreObjective objective;
    private ScaledResolution res;
    private int scoreboardWidth;
    private int scoreboardHeight;
    
    public ScoreboardModule() {
        super("Scoreboard", "Customize and move the Minecraft scoreboard.");
        ScoreboardModule.INSTANCE = this;
        this.hideNumbers = new Setting(this, "Hide Numbers").setDefault(false);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(false);
        this.borderThickness = new Setting(this, "Border Thickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f);
        this.border = new Setting(this, "Border").setDefault(false);
        new Setting(this, "Color Options");
        this.backgroundColor = new Setting(this, "Background Color").setDefault(new Color(0, 0, 0, 80).getRGB(), 0);
        this.borderColor = new Setting(this, "Border Color").setDefault(new Color(0, 0, 0, 159).getRGB(), 0);
    }
    
    @Override
    public int getWidth() {
        return this.scoreboardWidth;
    }
    
    @Override
    public int getHeight() {
        return this.scoreboardHeight;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.mc.theWorld != null && this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
            this.renderScoreboard(x, y);
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.renderDummyScoreboard(x, y);
    }
    
    public void setValues(final ScoreObjective o, final ScaledResolution res) {
        this.objective = o;
        this.res = res;
    }
    
    private void renderDummyScoreboard(final float x, final float y) {
        if (this.mc.theWorld != null && this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
            this.renderScoreboard(x, y);
        }
        else {
            this.setScoreboardWidth(96);
            this.setScoreboardHeight(29);
            Gui.drawRect((int)x, (int)y, (int)x + this.getWidth(), (int)y + this.getHeight(), this.backgroundColor.getColor());
            if (this.border.getBoolean()) {
                GuiUtils.drawRoundedOutline((int)x, (int)y, (int)x + this.getWidth(), (int)y + this.getHeight(), 0.0f, this.borderThickness.getFloat(), this.borderColor.getColor());
            }
            this.mc.fontRendererObj.drawString(EnumChatFormatting.AQUA + "Moonsense" + EnumChatFormatting.RESET + " " + EnumChatFormatting.BOLD + "Client", x + 2.0f, y + 2.0f, -1, this.textShadow.getBoolean());
            this.mc.fontRendererObj.drawString("Steve", x + 2.0f, y + 2.0f + this.mc.fontRendererObj.FONT_HEIGHT, -1, this.textShadow.getBoolean());
            if (!this.hideNumbers.getBoolean()) {
                this.mc.fontRendererObj.drawString(EnumChatFormatting.RED + "0", x + this.getWidth() - this.mc.fontRendererObj.getStringWidth("0"), y + 2.0f + this.mc.fontRendererObj.FONT_HEIGHT, 553648127, this.textShadow.getBoolean());
            }
            this.mc.fontRendererObj.drawString("Alex", x + 2.0f, y + 2.0f + 2 * this.mc.fontRendererObj.FONT_HEIGHT, -1, this.textShadow.getBoolean());
            if (!this.hideNumbers.getBoolean()) {
                this.mc.fontRendererObj.drawString(EnumChatFormatting.RED + "0", x + this.getWidth() - this.mc.fontRendererObj.getStringWidth("0"), y + 2.0f + 2 * this.mc.fontRendererObj.FONT_HEIGHT, 553648127, this.textShadow.getBoolean());
            }
        }
    }
    
    private void renderScoreboard(final float x, final float y) {
        final Scoreboard var3 = this.objective.getScoreboard();
        final Collection var4 = var3.getSortedScores(this.objective);
        final ArrayList var5 = Lists.newArrayList(Iterables.filter((Iterable)var4, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00001958";
            
            public boolean func_178903_a(final Score p_178903_1_) {
                return p_178903_1_.getPlayerName() != null && !p_178903_1_.getPlayerName().startsWith("#");
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_178903_a((Score)p_apply_1_);
            }
        }));
        ArrayList var6;
        if (var5.size() > 15) {
            var6 = Lists.newArrayList(Iterables.skip((Iterable)var5, var4.size() - 15));
        }
        else {
            var6 = var5;
        }
        int var7 = this.mc.ingameGUI.func_175179_f().getStringWidth(this.objective.getDisplayName());
        for (final Score var9 : var6) {
            final ScorePlayerTeam var10 = var3.getPlayersTeam(var9.getPlayerName());
            final String var11 = String.valueOf(ScorePlayerTeam.formatPlayerName(var10, var9.getPlayerName())) + ": " + EnumChatFormatting.RED + var9.getScorePoints();
            var7 = Math.max(var7, this.mc.ingameGUI.func_175179_f().getStringWidth(var11));
        }
        final int var12 = var6.size() * this.mc.ingameGUI.func_175179_f().FONT_HEIGHT;
        int var13 = this.res.getScaledHeight() / 2 + var12 / 3;
        final byte var14 = 3;
        int var15 = this.res.getScaledWidth() - var7 - var14;
        int var16 = 0;
        final Iterator var17 = var6.iterator();
        this.setScoreboardHeight(var13);
        this.setScoreboardWidth(this.res.getScaledWidth() - var15);
        var13 = (int)(var12 + y + this.mc.ingameGUI.func_175179_f().FONT_HEIGHT + 1.0f);
        var15 = (int)x;
        final int top = var13 - var6.size() * this.mc.ingameGUI.func_175179_f().FONT_HEIGHT;
        final int bottom = var13;
        this.setScoreboardHeight(bottom - top + this.mc.fontRendererObj.FONT_HEIGHT);
        while (var17.hasNext()) {
            final Score var18 = var17.next();
            ++var16;
            final ScorePlayerTeam var19 = var3.getPlayersTeam(var18.getPlayerName());
            final String var20 = ScorePlayerTeam.formatPlayerName(var19, var18.getPlayerName());
            final String var21 = new StringBuilder().append(EnumChatFormatting.RED).append(var18.getScorePoints()).toString();
            final int var22 = var13 - var16 * this.mc.ingameGUI.func_175179_f().FONT_HEIGHT;
            final int var23 = var15 + this.getWidth();
            final GuiIngame ingameGUI = this.mc.ingameGUI;
            Gui.drawRect(var15, var22, var23, var22 + this.mc.ingameGUI.func_175179_f().FONT_HEIGHT, SCModule.getColor(this.backgroundColor.getColorObject()));
            this.mc.ingameGUI.func_175179_f().drawString(var20, var15, var22, 553648127, this.textShadow.getBoolean());
            if (!this.hideNumbers.getBoolean()) {
                this.mc.ingameGUI.func_175179_f().drawString(var21, var23 - this.mc.ingameGUI.func_175179_f().getStringWidth(var21), var22, 553648127);
            }
            if (var16 == var6.size()) {
                final String var24 = this.objective.getDisplayName();
                final GuiIngame ingameGUI2 = this.mc.ingameGUI;
                Gui.drawRect(var15, var22 - this.mc.ingameGUI.func_175179_f().FONT_HEIGHT - 1, var23, var22 - 1, SCModule.getColor(this.backgroundColor.getColorObject()));
                final GuiIngame ingameGUI3 = this.mc.ingameGUI;
                Gui.drawRect(var15, var22 - 1, var23, var22, SCModule.getColor(this.backgroundColor.getColorObject()));
                this.mc.ingameGUI.func_175179_f().drawString(var24, var15 + var7 / 2 - this.mc.ingameGUI.func_175179_f().getStringWidth(var24) / 2, var22 - this.mc.ingameGUI.func_175179_f().FONT_HEIGHT, 553648127, this.textShadow.getBoolean());
            }
        }
        if (this.border.getBoolean()) {
            GuiUtils.drawRoundedOutline(x, y, x + this.scoreboardWidth, y + this.scoreboardHeight + 1.0f, 0.0f, this.borderThickness.getFloat(), SCModule.getColor(this.borderColor.getColorObject()));
        }
    }
    
    private void renderCustomScoreboard(final ScoreObjective obj, final ScaledResolution res, final float x, final float y) {
        final Scoreboard var3 = obj.getScoreboard();
        final Collection var4 = var3.getSortedScores(this.objective);
        final ArrayList var5 = Lists.newArrayList(Iterables.filter((Iterable)var4, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00001958";
            
            public boolean func_178903_a(final Score p_178903_1_) {
                return p_178903_1_.getPlayerName() != null && !p_178903_1_.getPlayerName().startsWith("#");
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_178903_a((Score)p_apply_1_);
            }
        }));
        ArrayList var6;
        if (var5.size() > 15) {
            var6 = Lists.newArrayList(Iterables.skip((Iterable)var5, var4.size() - 15));
        }
        else {
            var6 = var5;
        }
        int var7 = this.mc.ingameGUI.func_175179_f().getStringWidth(obj.getDisplayName());
        for (final Score var9 : var6) {
            final ScorePlayerTeam var10 = var3.getPlayersTeam(var9.getPlayerName());
            final String var11 = String.valueOf(ScorePlayerTeam.formatPlayerName(var10, var9.getPlayerName())) + ": " + EnumChatFormatting.RED + var9.getScorePoints();
            var7 = Math.max(var7, this.mc.ingameGUI.func_175179_f().getStringWidth(var11));
        }
        final int var12 = var6.size() * this.mc.ingameGUI.func_175179_f().FONT_HEIGHT;
        int var13 = res.getScaledHeight() / 2 + var12 / 3;
        final byte var14 = 3;
        int var15 = res.getScaledWidth() - var7 - var14;
        int var16 = 0;
        final Iterator var17 = var6.iterator();
        this.setScoreboardHeight(var13);
        this.setScoreboardWidth(res.getScaledWidth() - var15);
        var13 = (int)(var12 + y + this.mc.ingameGUI.func_175179_f().FONT_HEIGHT + 1.0f);
        var15 = (int)x;
        final int top = var13 - var6.size() * this.mc.ingameGUI.func_175179_f().FONT_HEIGHT;
        final int bottom = var13;
        this.setScoreboardHeight(bottom - top + this.mc.fontRendererObj.FONT_HEIGHT);
        while (var17.hasNext()) {
            final Score var18 = var17.next();
            ++var16;
            final ScorePlayerTeam var19 = var3.getPlayersTeam(var18.getPlayerName());
            final String var20 = ScorePlayerTeam.formatPlayerName(var19, var18.getPlayerName());
            final String var21 = new StringBuilder().append(EnumChatFormatting.RED).append(var18.getScorePoints()).toString();
            final int var22 = var13 - var16 * this.mc.ingameGUI.func_175179_f().FONT_HEIGHT;
            final int var23 = var15 + this.getWidth();
            final GuiIngame ingameGUI = this.mc.ingameGUI;
            Gui.drawRect(var15, var22, var23, var22 + this.mc.ingameGUI.func_175179_f().FONT_HEIGHT, 1342177280);
            this.mc.ingameGUI.func_175179_f().drawString(var20, var15, var22, 553648127, this.textShadow.getBoolean());
            this.mc.ingameGUI.func_175179_f().drawString(var21, var23 - this.mc.ingameGUI.func_175179_f().getStringWidth(var21), var22, 553648127, this.textShadow.getBoolean());
            if (var16 == var6.size()) {
                final String var24 = this.objective.getDisplayName();
                final GuiIngame ingameGUI2 = this.mc.ingameGUI;
                Gui.drawRect(var15, var22 - this.mc.ingameGUI.func_175179_f().FONT_HEIGHT - 1, var23, var22 - 1, 1610612736);
                final GuiIngame ingameGUI3 = this.mc.ingameGUI;
                Gui.drawRect(var15, var22 - 1, var23, var22, 1342177280);
                this.mc.ingameGUI.func_175179_f().drawString(var24, var15 + var7 / 2 - this.mc.ingameGUI.func_175179_f().getStringWidth(var24) / 2, var22 - this.mc.ingameGUI.func_175179_f().FONT_HEIGHT, 553648127, this.textShadow.getBoolean());
            }
        }
    }
    
    private void setScoreboardHeight(final int height) {
        this.scoreboardHeight = height;
    }
    
    private void setScoreboardWidth(final int width) {
        this.scoreboardWidth = width;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.CENTER_LEFT;
    }
    
    public ScoreObjective getObjective() {
        return this.objective;
    }
}
