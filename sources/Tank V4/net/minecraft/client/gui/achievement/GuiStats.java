package net.minecraft.client.gui.achievement;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiStats extends GuiScreen implements IProgressMeter {
   private GuiStats.StatsGeneral generalStats;
   private GuiSlot displaySlot;
   private GuiStats.StatsMobsList mobStats;
   private GuiStats.StatsBlock blockStats;
   protected GuiScreen parentScreen;
   private StatFileWriter field_146546_t;
   protected String screenTitle = "Select world";
   private GuiStats.StatsItem itemStats;
   private boolean doesGuiPauseGame = true;

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      if (this.displaySlot != null) {
         this.displaySlot.handleMouseInput();
      }

   }

   private void drawButtonBackground(int var1, int var2) {
      this.drawSprite(var1, var2, 0, 0);
   }

   public void drawScreen(int var1, int var2, float var3) {
      if (this.doesGuiPauseGame) {
         this.drawDefaultBackground();
         this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats"), width / 2, height / 2, 16777215);
         this.drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % (long)lanSearchStates.length)], width / 2, height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
      } else {
         this.displaySlot.drawScreen(var1, var2, var3);
         this.drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 20, 16777215);
         super.drawScreen(var1, var2, var3);
      }

   }

   static FontRenderer access$2(GuiStats var0) {
      return var0.fontRendererObj;
   }

   public void createButtons() {
      this.buttonList.add(new GuiButton(0, width / 2 + 4, height - 28, 150, 20, I18n.format("gui.done")));
      this.buttonList.add(new GuiButton(1, width / 2 - 160, height - 52, 80, 20, I18n.format("stat.generalButton")));
      GuiButton var1;
      this.buttonList.add(var1 = new GuiButton(2, width / 2 - 80, height - 52, 80, 20, I18n.format("stat.blocksButton")));
      GuiButton var2;
      this.buttonList.add(var2 = new GuiButton(3, width / 2, height - 52, 80, 20, I18n.format("stat.itemsButton")));
      GuiButton var3;
      this.buttonList.add(var3 = new GuiButton(4, width / 2 + 80, height - 52, 80, 20, I18n.format("stat.mobsButton")));
      if (this.blockStats.getSize() == 0) {
         var1.enabled = false;
      }

      if (this.itemStats.getSize() == 0) {
         var2.enabled = false;
      }

      if (this.mobStats.getSize() == 0) {
         var3.enabled = false;
      }

   }

   static void access$0(GuiStats var0, int var1, int var2, int var3, int var4) {
      var0.drawSprite(var1, var2, var3, var4);
   }

   private void drawStatsScreen(int var1, int var2, Item var3) {
      this.drawButtonBackground(var1 + 1, var2 + 1);
      GlStateManager.enableRescaleNormal();
      RenderHelper.enableGUIStandardItemLighting();
      this.itemRender.renderItemIntoGUI(new ItemStack(var3, 1, 0), var1 + 2, var2 + 2);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
   }

   static void access$3(GuiStats var0, int var1, int var2, Item var3) {
      var0.drawStatsScreen(var1, var2, var3);
   }

   public GuiStats(GuiScreen var1, StatFileWriter var2) {
      this.parentScreen = var1;
      this.field_146546_t = var2;
   }

   static StatFileWriter access$1(GuiStats var0) {
      return var0.field_146546_t;
   }

   public void doneLoading() {
      if (this.doesGuiPauseGame) {
         this.func_175366_f();
         this.createButtons();
         this.displaySlot = this.generalStats;
         this.doesGuiPauseGame = false;
      }

   }

   public boolean doesGuiPauseGame() {
      return !this.doesGuiPauseGame;
   }

   public void func_175366_f() {
      this.generalStats = new GuiStats.StatsGeneral(this, this.mc);
      this.generalStats.registerScrollButtons(1, 1);
      this.itemStats = new GuiStats.StatsItem(this, this.mc);
      this.itemStats.registerScrollButtons(1, 1);
      this.blockStats = new GuiStats.StatsBlock(this, this.mc);
      this.blockStats.registerScrollButtons(1, 1);
      this.mobStats = new GuiStats.StatsMobsList(this, this.mc);
      this.mobStats.registerScrollButtons(1, 1);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (var1.id == 1) {
            this.displaySlot = this.generalStats;
         } else if (var1.id == 3) {
            this.displaySlot = this.itemStats;
         } else if (var1.id == 2) {
            this.displaySlot = this.blockStats;
         } else if (var1.id == 4) {
            this.displaySlot = this.mobStats;
         } else {
            this.displaySlot.actionPerformed(var1);
         }
      }

   }

   public void initGui() {
      this.screenTitle = I18n.format("gui.stats");
      this.doesGuiPauseGame = true;
      this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
   }

   private void drawSprite(int var1, int var2, int var3, int var4) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(statIcons);
      float var5 = 0.0078125F;
      float var6 = 0.0078125F;
      boolean var7 = true;
      boolean var8 = true;
      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      var10.begin(7, DefaultVertexFormats.POSITION_TEX);
      var10.pos((double)(var1 + 0), (double)(var2 + 18), (double)zLevel).tex((double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F)).endVertex();
      var10.pos((double)(var1 + 18), (double)(var2 + 18), (double)zLevel).tex((double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F)).endVertex();
      var10.pos((double)(var1 + 18), (double)(var2 + 0), (double)zLevel).tex((double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F)).endVertex();
      var10.pos((double)(var1 + 0), (double)(var2 + 0), (double)zLevel).tex((double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F)).endVertex();
      var9.draw();
   }

   class StatsItem extends GuiStats.Stats {
      final GuiStats this$0;

      static GuiStats access$0(GuiStats.StatsItem var0) {
         return var0.this$0;
      }

      protected void drawListHeader(int var1, int var2, Tessellator var3) {
         super.drawListHeader(var1, var2, var3);
         if (this.field_148218_l == 0) {
            GuiStats.access$0(this.this$0, var1 + 115 - 18 + 1, var2 + 1 + 1, 72, 18);
         } else {
            GuiStats.access$0(this.this$0, var1 + 115 - 18, var2 + 1, 72, 18);
         }

         if (this.field_148218_l == 1) {
            GuiStats.access$0(this.this$0, var1 + 165 - 18 + 1, var2 + 1 + 1, 18, 18);
         } else {
            GuiStats.access$0(this.this$0, var1 + 165 - 18, var2 + 1, 18, 18);
         }

         if (this.field_148218_l == 2) {
            GuiStats.access$0(this.this$0, var1 + 215 - 18 + 1, var2 + 1 + 1, 36, 18);
         } else {
            GuiStats.access$0(this.this$0, var1 + 215 - 18, var2 + 1, 36, 18);
         }

      }

      protected String func_148210_b(int var1) {
         return var1 == 1 ? "stat.crafted" : (var1 == 2 ? "stat.used" : "stat.depleted");
      }

      public StatsItem(GuiStats var1, Minecraft var2) {
         super(var1, var2);
         this.this$0 = var1;
         this.statsHolder = Lists.newArrayList();
         Iterator var4 = StatList.itemStats.iterator();

         while(var4.hasNext()) {
            StatCrafting var3 = (StatCrafting)var4.next();
            boolean var5 = false;
            int var6 = Item.getIdFromItem(var3.func_150959_a());
            if (GuiStats.access$1(var1).readStat(var3) > 0) {
               var5 = true;
            } else if (StatList.objectBreakStats[var6] != null && GuiStats.access$1(var1).readStat(StatList.objectBreakStats[var6]) > 0) {
               var5 = true;
            } else if (StatList.objectCraftStats[var6] != null && GuiStats.access$1(var1).readStat(StatList.objectCraftStats[var6]) > 0) {
               var5 = true;
            }

            if (var5) {
               this.statsHolder.add(var3);
            }
         }

         this.statSorter = new Comparator(this) {
            final GuiStats.StatsItem this$1;

            public int compare(StatCrafting var1, StatCrafting var2) {
               int var3 = Item.getIdFromItem(var1.func_150959_a());
               int var4 = Item.getIdFromItem(var2.func_150959_a());
               StatBase var5 = null;
               StatBase var6 = null;
               if (this.this$1.field_148217_o == 0) {
                  var5 = StatList.objectBreakStats[var3];
                  var6 = StatList.objectBreakStats[var4];
               } else if (this.this$1.field_148217_o == 1) {
                  var5 = StatList.objectCraftStats[var3];
                  var6 = StatList.objectCraftStats[var4];
               } else if (this.this$1.field_148217_o == 2) {
                  var5 = StatList.objectUseStats[var3];
                  var6 = StatList.objectUseStats[var4];
               }

               if (var5 != null || var6 != null) {
                  if (var5 == null) {
                     return 1;
                  }

                  if (var6 == null) {
                     return -1;
                  }

                  int var7 = GuiStats.access$1(GuiStats.StatsItem.access$0(this.this$1)).readStat(var5);
                  int var8 = GuiStats.access$1(GuiStats.StatsItem.access$0(this.this$1)).readStat(var6);
                  if (var7 != var8) {
                     return (var7 - var8) * this.this$1.field_148215_p;
                  }
               }

               return var3 - var4;
            }

            public int compare(Object var1, Object var2) {
               return this.compare((StatCrafting)var1, (StatCrafting)var2);
            }

            {
               this.this$1 = var1;
            }
         };
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         StatCrafting var7 = this.func_148211_c(var1);
         Item var8 = var7.func_150959_a();
         GuiStats.access$3(this.this$0, var2 + 40, var3, var8);
         int var9 = Item.getIdFromItem(var8);
         this.func_148209_a(StatList.objectBreakStats[var9], var2 + 115, var3, var1 % 2 == 0);
         this.func_148209_a(StatList.objectCraftStats[var9], var2 + 165, var3, var1 % 2 == 0);
         this.func_148209_a(var7, var2 + 215, var3, var1 % 2 == 0);
      }
   }

   abstract class Stats extends GuiSlot {
      protected int field_148217_o;
      final GuiStats this$0;
      protected int field_148215_p;
      protected Comparator statSorter;
      protected List statsHolder;
      protected int field_148218_l;

      protected final int getSize() {
         return this.statsHolder.size();
      }

      protected final StatCrafting func_148211_c(int var1) {
         return (StatCrafting)this.statsHolder.get(var1);
      }

      protected void func_148132_a(int var1, int var2) {
         this.field_148218_l = -1;
         if (var1 >= 79 && var1 < 115) {
            this.field_148218_l = 0;
         } else if (var1 >= 129 && var1 < 165) {
            this.field_148218_l = 1;
         } else if (var1 >= 179 && var1 < 215) {
            this.field_148218_l = 2;
         }

         if (this.field_148218_l >= 0) {
            this.func_148212_h(this.field_148218_l);
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         }

      }

      protected void func_148212_h(int var1) {
         if (var1 != this.field_148217_o) {
            this.field_148217_o = var1;
            this.field_148215_p = -1;
         } else if (this.field_148215_p == -1) {
            this.field_148215_p = 1;
         } else {
            this.field_148217_o = -1;
            this.field_148215_p = 0;
         }

         Collections.sort(this.statsHolder, this.statSorter);
      }

      protected abstract String func_148210_b(int var1);

      protected void func_148142_b(int var1, int var2) {
         if (var2 >= this.top && var2 <= this.bottom) {
            int var3 = this.getSlotIndexFromScreenCoords(var1, var2);
            int var4 = this.width / 2 - 92 - 16;
            if (var3 >= 0) {
               if (var1 < var4 + 40 || var1 > var4 + 40 + 20) {
                  return;
               }

               StatCrafting var9 = this.func_148211_c(var3);
               this.func_148213_a(var9, var1, var2);
            } else {
               String var5 = "";
               if (var1 >= var4 + 115 - 18 && var1 <= var4 + 115) {
                  var5 = this.func_148210_b(0);
               } else if (var1 >= var4 + 165 - 18 && var1 <= var4 + 165) {
                  var5 = this.func_148210_b(1);
               } else {
                  if (var1 < var4 + 215 - 18 || var1 > var4 + 215) {
                     return;
                  }

                  var5 = this.func_148210_b(2);
               }

               var5 = I18n.format(var5).trim();
               if (var5.length() > 0) {
                  int var6 = var1 + 12;
                  int var7 = var2 - 12;
                  int var8 = GuiStats.access$2(this.this$0).getStringWidth(var5);
                  GuiStats.drawGradientRect((double)(var6 - 3), (double)(var7 - 3), (float)(var6 + var8 + 3), (float)(var7 + 8 + 3), -1073741824, -1073741824);
                  GuiStats.access$2(this.this$0).drawStringWithShadow(var5, (float)var6, (float)var7, -1);
               }
            }
         }

      }

      protected Stats(GuiStats var1, Minecraft var2) {
         super(var2, GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, 20);
         this.this$0 = var1;
         this.field_148218_l = -1;
         this.field_148217_o = -1;
         this.setShowSelectionBox(false);
         this.setHasListHeader(true, 20);
      }

      protected void drawListHeader(int var1, int var2, Tessellator var3) {
         if (!Mouse.isButtonDown(0)) {
            this.field_148218_l = -1;
         }

         if (this.field_148218_l == 0) {
            GuiStats.access$0(this.this$0, var1 + 115 - 18, var2 + 1, 0, 0);
         } else {
            GuiStats.access$0(this.this$0, var1 + 115 - 18, var2 + 1, 0, 18);
         }

         if (this.field_148218_l == 1) {
            GuiStats.access$0(this.this$0, var1 + 165 - 18, var2 + 1, 0, 0);
         } else {
            GuiStats.access$0(this.this$0, var1 + 165 - 18, var2 + 1, 0, 18);
         }

         if (this.field_148218_l == 2) {
            GuiStats.access$0(this.this$0, var1 + 215 - 18, var2 + 1, 0, 0);
         } else {
            GuiStats.access$0(this.this$0, var1 + 215 - 18, var2 + 1, 0, 18);
         }

         if (this.field_148217_o != -1) {
            short var4 = 79;
            byte var5 = 18;
            if (this.field_148217_o == 1) {
               var4 = 129;
            } else if (this.field_148217_o == 2) {
               var4 = 179;
            }

            if (this.field_148215_p == 1) {
               var5 = 36;
            }

            GuiStats.access$0(this.this$0, var1 + var4, var2 + 1, var5, 0);
         }

      }

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      protected void drawBackground() {
         this.this$0.drawDefaultBackground();
      }

      protected void func_148209_a(StatBase var1, int var2, int var3, boolean var4) {
         String var5;
         if (var1 != null) {
            var5 = var1.format(GuiStats.access$1(this.this$0).readStat(var1));
            this.this$0.drawString(GuiStats.access$2(this.this$0), var5, var2 - GuiStats.access$2(this.this$0).getStringWidth(var5), var3 + 5, var4 ? 16777215 : 9474192);
         } else {
            var5 = "-";
            this.this$0.drawString(GuiStats.access$2(this.this$0), var5, var2 - GuiStats.access$2(this.this$0).getStringWidth(var5), var3 + 5, var4 ? 16777215 : 9474192);
         }

      }

      protected boolean isSelected(int var1) {
         return false;
      }

      protected void func_148213_a(StatCrafting var1, int var2, int var3) {
         if (var1 != null) {
            Item var4 = var1.func_150959_a();
            ItemStack var5 = new ItemStack(var4);
            String var6 = var5.getUnlocalizedName();
            String var7 = I18n.format(var6 + ".name").trim();
            if (var7.length() > 0) {
               int var8 = var2 + 12;
               int var9 = var3 - 12;
               int var10 = GuiStats.access$2(this.this$0).getStringWidth(var7);
               GuiStats.drawGradientRect((double)(var8 - 3), (double)(var9 - 3), (float)(var8 + var10 + 3), (float)(var9 + 8 + 3), -1073741824, -1073741824);
               GuiStats.access$2(this.this$0).drawStringWithShadow(var7, (float)var8, (float)var9, -1);
            }
         }

      }
   }

   class StatsMobsList extends GuiSlot {
      final GuiStats this$0;
      private final List field_148222_l;

      protected int getContentHeight() {
         return this.getSize() * GuiStats.access$2(this.this$0).FONT_HEIGHT * 4;
      }

      protected int getSize() {
         return this.field_148222_l.size();
      }

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      protected boolean isSelected(int var1) {
         return false;
      }

      public StatsMobsList(GuiStats var1, Minecraft var2) {
         super(var2, GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, GuiStats.access$2(var1).FONT_HEIGHT * 4);
         this.this$0 = var1;
         this.field_148222_l = Lists.newArrayList();
         this.setShowSelectionBox(false);
         Iterator var4 = EntityList.entityEggs.values().iterator();

         while(true) {
            EntityList.EntityEggInfo var3;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               var3 = (EntityList.EntityEggInfo)var4.next();
            } while(GuiStats.access$1(var1).readStat(var3.field_151512_d) <= 0 && GuiStats.access$1(var1).readStat(var3.field_151513_e) <= 0);

            this.field_148222_l.add(var3);
         }
      }

      protected void drawBackground() {
         this.this$0.drawDefaultBackground();
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         EntityList.EntityEggInfo var7 = (EntityList.EntityEggInfo)this.field_148222_l.get(var1);
         String var8 = I18n.format("entity." + EntityList.getStringFromID(var7.spawnedID) + ".name");
         int var9 = GuiStats.access$1(this.this$0).readStat(var7.field_151512_d);
         int var10 = GuiStats.access$1(this.this$0).readStat(var7.field_151513_e);
         String var11 = I18n.format("stat.entityKills", var9, var8);
         String var12 = I18n.format("stat.entityKilledBy", var8, var10);
         if (var9 == 0) {
            var11 = I18n.format("stat.entityKills.none", var8);
         }

         if (var10 == 0) {
            var12 = I18n.format("stat.entityKilledBy.none", var8);
         }

         this.this$0.drawString(GuiStats.access$2(this.this$0), var8, var2 + 2 - 10, var3 + 1, 16777215);
         this.this$0.drawString(GuiStats.access$2(this.this$0), var11, var2 + 2, var3 + 1 + GuiStats.access$2(this.this$0).FONT_HEIGHT, var9 == 0 ? 6316128 : 9474192);
         this.this$0.drawString(GuiStats.access$2(this.this$0), var12, var2 + 2, var3 + 1 + GuiStats.access$2(this.this$0).FONT_HEIGHT * 2, var10 == 0 ? 6316128 : 9474192);
      }
   }

   class StatsGeneral extends GuiSlot {
      final GuiStats this$0;

      protected int getContentHeight() {
         return this.getSize() * 10;
      }

      public StatsGeneral(GuiStats var1, Minecraft var2) {
         super(var2, GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, 10);
         this.this$0 = var1;
         this.setShowSelectionBox(false);
      }

      protected int getSize() {
         return StatList.generalStats.size();
      }

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         StatBase var7 = (StatBase)StatList.generalStats.get(var1);
         this.this$0.drawString(GuiStats.access$2(this.this$0), var7.getStatName().getUnformattedText(), var2 + 2, var3 + 1, var1 % 2 == 0 ? 16777215 : 9474192);
         String var8 = var7.format(GuiStats.access$1(this.this$0).readStat(var7));
         this.this$0.drawString(GuiStats.access$2(this.this$0), var8, var2 + 2 + 213 - GuiStats.access$2(this.this$0).getStringWidth(var8), var3 + 1, var1 % 2 == 0 ? 16777215 : 9474192);
      }

      protected void drawBackground() {
         this.this$0.drawDefaultBackground();
      }

      protected boolean isSelected(int var1) {
         return false;
      }
   }

   class StatsBlock extends GuiStats.Stats {
      final GuiStats this$0;

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         StatCrafting var7 = this.func_148211_c(var1);
         Item var8 = var7.func_150959_a();
         GuiStats.access$3(this.this$0, var2 + 40, var3, var8);
         int var9 = Item.getIdFromItem(var8);
         this.func_148209_a(StatList.objectCraftStats[var9], var2 + 115, var3, var1 % 2 == 0);
         this.func_148209_a(StatList.objectUseStats[var9], var2 + 165, var3, var1 % 2 == 0);
         this.func_148209_a(var7, var2 + 215, var3, var1 % 2 == 0);
      }

      public StatsBlock(GuiStats var1, Minecraft var2) {
         super(var1, var2);
         this.this$0 = var1;
         this.statsHolder = Lists.newArrayList();
         Iterator var4 = StatList.objectMineStats.iterator();

         while(var4.hasNext()) {
            StatCrafting var3 = (StatCrafting)var4.next();
            boolean var5 = false;
            int var6 = Item.getIdFromItem(var3.func_150959_a());
            if (GuiStats.access$1(var1).readStat(var3) > 0) {
               var5 = true;
            } else if (StatList.objectUseStats[var6] != null && GuiStats.access$1(var1).readStat(StatList.objectUseStats[var6]) > 0) {
               var5 = true;
            } else if (StatList.objectCraftStats[var6] != null && GuiStats.access$1(var1).readStat(StatList.objectCraftStats[var6]) > 0) {
               var5 = true;
            }

            if (var5) {
               this.statsHolder.add(var3);
            }
         }

         this.statSorter = new Comparator(this) {
            final GuiStats.StatsBlock this$1;

            public int compare(StatCrafting var1, StatCrafting var2) {
               int var3 = Item.getIdFromItem(var1.func_150959_a());
               int var4 = Item.getIdFromItem(var2.func_150959_a());
               StatBase var5 = null;
               StatBase var6 = null;
               if (this.this$1.field_148217_o == 2) {
                  var5 = StatList.mineBlockStatArray[var3];
                  var6 = StatList.mineBlockStatArray[var4];
               } else if (this.this$1.field_148217_o == 0) {
                  var5 = StatList.objectCraftStats[var3];
                  var6 = StatList.objectCraftStats[var4];
               } else if (this.this$1.field_148217_o == 1) {
                  var5 = StatList.objectUseStats[var3];
                  var6 = StatList.objectUseStats[var4];
               }

               if (var5 != null || var6 != null) {
                  if (var5 == null) {
                     return 1;
                  }

                  if (var6 == null) {
                     return -1;
                  }

                  int var7 = GuiStats.access$1(GuiStats.StatsBlock.access$0(this.this$1)).readStat(var5);
                  int var8 = GuiStats.access$1(GuiStats.StatsBlock.access$0(this.this$1)).readStat(var6);
                  if (var7 != var8) {
                     return (var7 - var8) * this.this$1.field_148215_p;
                  }
               }

               return var3 - var4;
            }

            {
               this.this$1 = var1;
            }

            public int compare(Object var1, Object var2) {
               return this.compare((StatCrafting)var1, (StatCrafting)var2);
            }
         };
      }

      static GuiStats access$0(GuiStats.StatsBlock var0) {
         return var0.this$0;
      }

      protected String func_148210_b(int var1) {
         return var1 == 0 ? "stat.crafted" : (var1 == 1 ? "stat.used" : "stat.mined");
      }

      protected void drawListHeader(int var1, int var2, Tessellator var3) {
         super.drawListHeader(var1, var2, var3);
         if (this.field_148218_l == 0) {
            GuiStats.access$0(this.this$0, var1 + 115 - 18 + 1, var2 + 1 + 1, 18, 18);
         } else {
            GuiStats.access$0(this.this$0, var1 + 115 - 18, var2 + 1, 18, 18);
         }

         if (this.field_148218_l == 1) {
            GuiStats.access$0(this.this$0, var1 + 165 - 18 + 1, var2 + 1 + 1, 36, 18);
         } else {
            GuiStats.access$0(this.this$0, var1 + 165 - 18, var2 + 1, 36, 18);
         }

         if (this.field_148218_l == 2) {
            GuiStats.access$0(this.this$0, var1 + 215 - 18 + 1, var2 + 1 + 1, 54, 18);
         } else {
            GuiStats.access$0(this.this$0, var1 + 215 - 18, var2 + 1, 54, 18);
         }

      }
   }
}
