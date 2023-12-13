package me.yarukon.hud.window.impl;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.utils.Colors;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import me.yarukon.YRenderUtil;
import me.yarukon.hud.window.HudWindow;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

@ObfuscationClass
public class WindowScoreboard extends HudWindow {
   public WindowScoreboard() {
      super("Scoreboard", 5.0F, 200.0F, 200.0F, 300.0F, "Scoreboard", "", 12.0F, 0.0F, 1.0F);
   }

   public void draw() {
      Hanabi.INSTANCE.customScoreboard = !this.hide;
      Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
      ScoreObjective scoreObjective = null;
      ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
      if (scoreplayerteam != null) {
         int i1 = scoreplayerteam.getChatFormat().getColorIndex();
         if (i1 >= 0) {
            scoreObjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
         }
      }

      ScoreObjective scoreObjective1 = scoreObjective != null ? scoreObjective : scoreboard.getObjectiveInDisplaySlot(1);
      if (scoreObjective1 != null) {
         super.draw();
         Collection collection = scoreboard.getSortedScores(scoreObjective1);
         List arraylist = (List)collection.stream().filter((p_apply_1_) -> {
            return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
         }).collect(Collectors.toList());
         Object arraylist1;
         if (arraylist.size() > 15) {
            arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
         } else {
            arraylist1 = arraylist;
         }

         int height = 0;
         float width = this.iconOffX + (float)this.mc.fontRendererObj.getStringWidth(this.title) + 10.0F;
         boolean isClassic = HUD.hudMode.isCurrentMode("Classic");
         String s3 = scoreObjective1.getDisplayName();
         YRenderUtil.drawRectNormal(this.x, this.y + this.draggableHeight, this.x + this.width, this.y + this.draggableHeight + 14.0F, isClassic ? 1140850688 : Colors.getColor(166, 173, 176, 190));
         this.drawCenteredString(s3, (int)this.x + (int)(this.width / 2.0F), (int)this.y + (int)this.draggableHeight + 3, -1);
         height += 18;

         for(int i = 0; i < ((List)arraylist1).size(); ++i) {
            Score score1 = (Score)((List)arraylist1).get(((List)arraylist1).size() - i - 1);
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            width = Math.max((float)this.mc.fontRendererObj.getStringWidth(s1), width);
            s1 = s1.replaceAll("§l", "");
            float var10002 = (float)((int)(this.x + 4.0F));
            int var10003 = (int)this.y;
            height += 10;
            this.mc.fontRendererObj.drawStringWithShadow(s1, var10002, (float)(var10003 + height), -1);
         }

         width = Math.max((float)this.mc.fontRendererObj.getStringWidth(s3), width);
         this.width = width + 8.0F;
         this.height = (float)(height - 2);
      }

   }

   public void drawCenteredString(String text, int x, int y, int col) {
      this.mc.fontRendererObj.drawStringWithShadow(text, (float)x - (float)this.mc.fontRendererObj.getStringWidth(text) / 2.0F, (float)y, col);
   }

   public void show() {
      Hanabi.INSTANCE.customScoreboard = true;
      super.show();
   }

   public void hide() {
      super.hide();
      Hanabi.INSTANCE.customScoreboard = false;
   }
}
