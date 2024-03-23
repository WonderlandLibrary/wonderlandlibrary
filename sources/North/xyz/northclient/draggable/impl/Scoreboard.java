package xyz.northclient.draggable.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.util.vector.Vector2f;
import xyz.northclient.UIHook;
import xyz.northclient.draggable.AbstractDraggable;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Scoreboard extends AbstractDraggable {
    public boolean useVanilla;
    public ScoreObjective p_180475_1_;
    public ScaledResolution p_180475_2_;

    public ModeValue fontMode = new ModeValue("Font",this)
            .add(new Watermark.StringMode("Product Sans",this))
            .add(new Watermark.StringMode("Minecraft",this))
            .add(new Watermark.StringMode("Exhibition",this))
            .add(new Watermark.StringMode("SF",this))
            .setDefault("Minecraft");

    public BoolValue bloom = new BoolValue("Bloom",this)
            .setDefault(true);

    public BoolValue rounded = new BoolValue("Rounded",this)
            .setDefault(true);

    public Scoreboard() {
        super(true);
        X = 5;
        Y = 30;
    }

    @Override
    public Vector2f Render() {
        UIHook.ArraylistFont font = new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
        switch(fontMode.get().getName()) {
            case "Product Sans":
                font = new UIHook.CustomArraylistFont(FontUtil.DefaultSmall);
                break;
            case "Minecraft":
                font = new UIHook.MinecraftArraylistFont(mc.fontRendererObj);
                break;
            case "Exhibition":
                font = new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
                break;
            case "SF":
                font = new UIHook.CustomArraylistFont(FontUtil.SFProRegular);
                break;
            default:
                new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
                break;
        }
        
        if(!useVanilla) {
            net.minecraft.scoreboard.Scoreboard scoreboard = p_180475_1_.getScoreboard();
            Collection collection = scoreboard.getSortedScores(p_180475_1_);
            ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate()
            {
                private static final String __OBFID = "CL_00001958";
                public boolean apply(Score p_apply_1_)
                {
                    return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.apply((Score)p_apply_1_);
                }
            }));
            ArrayList arraylist1;

            if (arraylist.size() > 15)
            {
                arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
            }
            else
            {
                arraylist1 = arraylist;
            }

            int i = (int) font.getWidth(p_180475_1_.getDisplayName());

            for (Object score0 : arraylist1)
            {
                Score score = (Score) score0;
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                i = (int) Math.max(i, font.getWidth(s));
            }

            int j1 = (int) (arraylist1.size() *mc.fontRendererObj.FONT_HEIGHT);
            int k1 = (int) (j1+mc.fontRendererObj.FONT_HEIGHT+Y);
            byte b0 = 3;
            int j = X;
            int k = 0;
            i-=10;

            if(bloom.get()) {
                if(rounded.get()) {
                    RectUtil.drawRoundedBloom(X - 8,Y - 8,i + 17, (int) (j1+mc.fontRendererObj.FONT_HEIGHT) + 14,22,20, new Color(0,0,0,150));
                } else {
                    RectUtil.drawBloom(X,Y,i, (int) (j1+mc.fontRendererObj.FONT_HEIGHT),32, new Color(0,0,0,150));
                }
            }

            if(rounded.get()) {
                RectUtil.drawRoundedRect(X - 8,Y-6,i + 15,j1+mc.fontRendererObj.FONT_HEIGHT + 9,6, new Color(0,0,0,bloom.get() ? 50 : 120));
            }

            for (Object score10 : arraylist1)
            {
                Score score1 = (Score) score10;
                ++k;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
                String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
                int l = (int) (k1 - k * mc.fontRendererObj.FONT_HEIGHT);
                int i1 = X+i;
                if(!rounded.get()) {
                    Gui.drawRect(j - 2, l, i1, (int) (l + mc.fontRendererObj.FONT_HEIGHT), 1342177280);
                }
                font.drawString(s1, j, l, Color.white.getRGB());

                if (k == arraylist1.size())
                {
                    String s3 = p_180475_1_.getDisplayName();
                    if(!rounded.get()) {
                        Gui.drawRect(j - 2, (int) (l - mc.fontRendererObj.FONT_HEIGHT - 1), i1, l - 1, 1610612736);
                        Gui.drawRect(j - 2, l - 1, i1, l, 1342177280);
                    }
                    font.drawString(s3, j + i / 2 - font.getWidth(s3) / 2, l - mc.fontRendererObj.FONT_HEIGHT, Color.white.getRGB());
                }
            }

            return new Vector2f(i,j1+mc.fontRendererObj.FONT_HEIGHT);
        }
        return new Vector2f(0,0);
    }

    @Override
    public String getDraggableName() {
        return "Scoreboard";
    }
}
