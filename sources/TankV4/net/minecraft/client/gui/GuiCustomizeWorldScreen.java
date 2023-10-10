package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.primitives.Floats;
import java.io.IOException;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;

public class GuiCustomizeWorldScreen extends GuiScreen implements GuiSlider.FormatHelper, GuiPageButtonList.GuiResponder {
   private boolean field_175338_A = false;
   protected String field_175333_f = "Page 1 of 3";
   private Random random = new Random();
   private int field_175339_B = 0;
   private GuiButton field_175348_s;
   private GuiCreateWorld field_175343_i;
   protected String[] field_175342_h = new String[4];
   private GuiButton field_175346_u;
   private boolean field_175340_C = false;
   private GuiButton field_175352_x;
   private GuiButton field_175345_v;
   protected String field_175341_a = "Customize World Settings";
   private Predicate field_175332_D = new Predicate(this) {
      final GuiCustomizeWorldScreen this$0;

      {
         this.this$0 = var1;
      }

      public boolean apply(Object var1) {
         return this.apply((String)var1);
      }

      public boolean apply(String var1) {
         Float var2 = Floats.tryParse(var1);
         return var1.length() == 0 || var2 != null && Floats.isFinite(var2) && var2 >= 0.0F;
      }
   };
   private ChunkProviderSettings.Factory field_175336_F;
   private GuiButton field_175344_w;
   private GuiButton field_175350_z;
   private GuiPageButtonList field_175349_r;
   private GuiButton field_175347_t;
   protected String field_175335_g = "Basic Settings";
   private ChunkProviderSettings.Factory field_175334_E = new ChunkProviderSettings.Factory();
   private GuiButton field_175351_y;

   private void func_175329_a(boolean var1) {
      this.field_175352_x.visible = var1;
      this.field_175351_y.visible = var1;
      this.field_175347_t.enabled = !var1;
      this.field_175348_s.enabled = !var1;
      this.field_175345_v.enabled = !var1;
      this.field_175344_w.enabled = !var1;
      this.field_175346_u.enabled = this.field_175338_A && !var1;
      this.field_175350_z.enabled = !var1;
      this.field_175349_r.func_181155_a(!var1);
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      super.keyTyped(var1, var2);
      if (this.field_175339_B == 0) {
         switch(var2) {
         case 200:
            this.func_175327_a(1.0F);
            break;
         case 208:
            this.func_175327_a(-1.0F);
            break;
         default:
            this.field_175349_r.func_178062_a(var1, var2);
         }
      }

   }

   private void func_175327_a(float var1) {
      Gui var2 = this.field_175349_r.func_178056_g();
      if (var2 instanceof GuiTextField) {
         float var3 = var1;
         if (GuiScreen.isShiftKeyDown()) {
            var3 = var1 * 0.1F;
            if (GuiScreen.isCtrlKeyDown()) {
               var3 *= 0.1F;
            }
         } else if (GuiScreen.isCtrlKeyDown()) {
            var3 = var1 * 10.0F;
            if (GuiScreen.isAltKeyDown()) {
               var3 *= 10.0F;
            }
         }

         GuiTextField var4 = (GuiTextField)var2;
         Float var5 = Floats.tryParse(var4.getText());
         if (var5 != null) {
            var5 = var5 + var3;
            int var6 = var4.getId();
            String var7 = this.func_175330_b(var4.getId(), var5);
            var4.setText(var7);
            this.func_175319_a(var6, var7);
         }
      }

   }

   public void func_175321_a(int var1, boolean var2) {
      switch(var1) {
      case 148:
         this.field_175336_F.useCaves = var2;
         break;
      case 149:
         this.field_175336_F.useDungeons = var2;
         break;
      case 150:
         this.field_175336_F.useStrongholds = var2;
         break;
      case 151:
         this.field_175336_F.useVillages = var2;
         break;
      case 152:
         this.field_175336_F.useMineShafts = var2;
         break;
      case 153:
         this.field_175336_F.useTemples = var2;
         break;
      case 154:
         this.field_175336_F.useRavines = var2;
         break;
      case 155:
         this.field_175336_F.useWaterLakes = var2;
         break;
      case 156:
         this.field_175336_F.useLavaLakes = var2;
         break;
      case 161:
         this.field_175336_F.useLavaOceans = var2;
         break;
      case 210:
         this.field_175336_F.useMonuments = var2;
      }

      if (!this.field_175336_F.equals(this.field_175334_E)) {
         this.func_181031_a(true);
      }

   }

   public void onTick(int var1, float var2) {
      switch(var1) {
      case 100:
         this.field_175336_F.mainNoiseScaleX = var2;
         break;
      case 101:
         this.field_175336_F.mainNoiseScaleY = var2;
         break;
      case 102:
         this.field_175336_F.mainNoiseScaleZ = var2;
         break;
      case 103:
         this.field_175336_F.depthNoiseScaleX = var2;
         break;
      case 104:
         this.field_175336_F.depthNoiseScaleZ = var2;
         break;
      case 105:
         this.field_175336_F.depthNoiseScaleExponent = var2;
         break;
      case 106:
         this.field_175336_F.baseSize = var2;
         break;
      case 107:
         this.field_175336_F.coordinateScale = var2;
         break;
      case 108:
         this.field_175336_F.heightScale = var2;
         break;
      case 109:
         this.field_175336_F.stretchY = var2;
         break;
      case 110:
         this.field_175336_F.upperLimitScale = var2;
         break;
      case 111:
         this.field_175336_F.lowerLimitScale = var2;
         break;
      case 112:
         this.field_175336_F.biomeDepthWeight = var2;
         break;
      case 113:
         this.field_175336_F.biomeDepthOffset = var2;
         break;
      case 114:
         this.field_175336_F.biomeScaleWeight = var2;
         break;
      case 115:
         this.field_175336_F.biomeScaleOffset = var2;
      case 116:
      case 117:
      case 118:
      case 119:
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 128:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 137:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
      case 161:
      case 188:
      default:
         break;
      case 157:
         this.field_175336_F.dungeonChance = (int)var2;
         break;
      case 158:
         this.field_175336_F.waterLakeChance = (int)var2;
         break;
      case 159:
         this.field_175336_F.lavaLakeChance = (int)var2;
         break;
      case 160:
         this.field_175336_F.seaLevel = (int)var2;
         break;
      case 162:
         this.field_175336_F.fixedBiome = (int)var2;
         break;
      case 163:
         this.field_175336_F.biomeSize = (int)var2;
         break;
      case 164:
         this.field_175336_F.riverSize = (int)var2;
         break;
      case 165:
         this.field_175336_F.dirtSize = (int)var2;
         break;
      case 166:
         this.field_175336_F.dirtCount = (int)var2;
         break;
      case 167:
         this.field_175336_F.dirtMinHeight = (int)var2;
         break;
      case 168:
         this.field_175336_F.dirtMaxHeight = (int)var2;
         break;
      case 169:
         this.field_175336_F.gravelSize = (int)var2;
         break;
      case 170:
         this.field_175336_F.gravelCount = (int)var2;
         break;
      case 171:
         this.field_175336_F.gravelMinHeight = (int)var2;
         break;
      case 172:
         this.field_175336_F.gravelMaxHeight = (int)var2;
         break;
      case 173:
         this.field_175336_F.graniteSize = (int)var2;
         break;
      case 174:
         this.field_175336_F.graniteCount = (int)var2;
         break;
      case 175:
         this.field_175336_F.graniteMinHeight = (int)var2;
         break;
      case 176:
         this.field_175336_F.graniteMaxHeight = (int)var2;
         break;
      case 177:
         this.field_175336_F.dioriteSize = (int)var2;
         break;
      case 178:
         this.field_175336_F.dioriteCount = (int)var2;
         break;
      case 179:
         this.field_175336_F.dioriteMinHeight = (int)var2;
         break;
      case 180:
         this.field_175336_F.dioriteMaxHeight = (int)var2;
         break;
      case 181:
         this.field_175336_F.andesiteSize = (int)var2;
         break;
      case 182:
         this.field_175336_F.andesiteCount = (int)var2;
         break;
      case 183:
         this.field_175336_F.andesiteMinHeight = (int)var2;
         break;
      case 184:
         this.field_175336_F.andesiteMaxHeight = (int)var2;
         break;
      case 185:
         this.field_175336_F.coalSize = (int)var2;
         break;
      case 186:
         this.field_175336_F.coalCount = (int)var2;
         break;
      case 187:
         this.field_175336_F.coalMinHeight = (int)var2;
         break;
      case 189:
         this.field_175336_F.coalMaxHeight = (int)var2;
         break;
      case 190:
         this.field_175336_F.ironSize = (int)var2;
         break;
      case 191:
         this.field_175336_F.ironCount = (int)var2;
         break;
      case 192:
         this.field_175336_F.ironMinHeight = (int)var2;
         break;
      case 193:
         this.field_175336_F.ironMaxHeight = (int)var2;
         break;
      case 194:
         this.field_175336_F.goldSize = (int)var2;
         break;
      case 195:
         this.field_175336_F.goldCount = (int)var2;
         break;
      case 196:
         this.field_175336_F.goldMinHeight = (int)var2;
         break;
      case 197:
         this.field_175336_F.goldMaxHeight = (int)var2;
         break;
      case 198:
         this.field_175336_F.redstoneSize = (int)var2;
         break;
      case 199:
         this.field_175336_F.redstoneCount = (int)var2;
         break;
      case 200:
         this.field_175336_F.redstoneMinHeight = (int)var2;
         break;
      case 201:
         this.field_175336_F.redstoneMaxHeight = (int)var2;
         break;
      case 202:
         this.field_175336_F.diamondSize = (int)var2;
         break;
      case 203:
         this.field_175336_F.diamondCount = (int)var2;
         break;
      case 204:
         this.field_175336_F.diamondMinHeight = (int)var2;
         break;
      case 205:
         this.field_175336_F.diamondMaxHeight = (int)var2;
         break;
      case 206:
         this.field_175336_F.lapisSize = (int)var2;
         break;
      case 207:
         this.field_175336_F.lapisCount = (int)var2;
         break;
      case 208:
         this.field_175336_F.lapisCenterHeight = (int)var2;
         break;
      case 209:
         this.field_175336_F.lapisSpread = (int)var2;
      }

      if (var1 >= 100 && var1 < 116) {
         Gui var3 = this.field_175349_r.func_178061_c(var1 - 100 + 132);
         if (var3 != null) {
            ((GuiTextField)var3).setText(this.func_175330_b(var1, var2));
         }
      }

      if (!this.field_175336_F.equals(this.field_175334_E)) {
         this.func_181031_a(true);
      }

   }

   private String func_175330_b(int var1, float var2) {
      switch(var1) {
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 107:
      case 108:
      case 110:
      case 111:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 139:
      case 140:
      case 142:
      case 143:
         return String.format("%5.3f", var2);
      case 105:
      case 106:
      case 109:
      case 112:
      case 113:
      case 114:
      case 115:
      case 137:
      case 138:
      case 141:
      case 144:
      case 145:
      case 146:
      case 147:
         return String.format("%2.3f", var2);
      case 116:
      case 117:
      case 118:
      case 119:
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 128:
      case 129:
      case 130:
      case 131:
      case 148:
      case 149:
      case 150:
      case 151:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
      case 158:
      case 159:
      case 160:
      case 161:
      default:
         return String.format("%d", (int)var2);
      case 162:
         if (var2 < 0.0F) {
            return I18n.format("gui.all");
         } else {
            BiomeGenBase var3;
            if ((int)var2 >= BiomeGenBase.hell.biomeID) {
               var3 = BiomeGenBase.getBiomeGenArray()[(int)var2 + 2];
               return var3 != null ? var3.biomeName : "?";
            } else {
               var3 = BiomeGenBase.getBiomeGenArray()[(int)var2];
               return var3 != null ? var3.biomeName : "?";
            }
         }
      }
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      if (this.field_175339_B == 0 && !this.field_175340_C) {
         this.field_175349_r.mouseClicked(var1, var2, var3);
      }

   }

   private void func_175326_g() {
      this.field_175336_F.func_177863_a();
      this.func_175325_f();
      this.func_181031_a(false);
   }

   public void func_175324_a(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.field_175336_F = ChunkProviderSettings.Factory.jsonToFactory(var1);
      } else {
         this.field_175336_F = new ChunkProviderSettings.Factory();
      }

   }

   private void func_175325_f() {
      GuiPageButtonList.GuiListEntry[] var1 = new GuiPageButtonList.GuiListEntry[]{new GuiPageButtonList.GuiSlideEntry(160, I18n.format("createWorld.customize.custom.seaLevel"), true, this, 1.0F, 255.0F, (float)this.field_175336_F.seaLevel), new GuiPageButtonList.GuiButtonEntry(148, I18n.format("createWorld.customize.custom.useCaves"), true, this.field_175336_F.useCaves), new GuiPageButtonList.GuiButtonEntry(150, I18n.format("createWorld.customize.custom.useStrongholds"), true, this.field_175336_F.useStrongholds), new GuiPageButtonList.GuiButtonEntry(151, I18n.format("createWorld.customize.custom.useVillages"), true, this.field_175336_F.useVillages), new GuiPageButtonList.GuiButtonEntry(152, I18n.format("createWorld.customize.custom.useMineShafts"), true, this.field_175336_F.useMineShafts), new GuiPageButtonList.GuiButtonEntry(153, I18n.format("createWorld.customize.custom.useTemples"), true, this.field_175336_F.useTemples), new GuiPageButtonList.GuiButtonEntry(210, I18n.format("createWorld.customize.custom.useMonuments"), true, this.field_175336_F.useMonuments), new GuiPageButtonList.GuiButtonEntry(154, I18n.format("createWorld.customize.custom.useRavines"), true, this.field_175336_F.useRavines), new GuiPageButtonList.GuiButtonEntry(149, I18n.format("createWorld.customize.custom.useDungeons"), true, this.field_175336_F.useDungeons), new GuiPageButtonList.GuiSlideEntry(157, I18n.format("createWorld.customize.custom.dungeonChance"), true, this, 1.0F, 100.0F, (float)this.field_175336_F.dungeonChance), new GuiPageButtonList.GuiButtonEntry(155, I18n.format("createWorld.customize.custom.useWaterLakes"), true, this.field_175336_F.useWaterLakes), new GuiPageButtonList.GuiSlideEntry(158, I18n.format("createWorld.customize.custom.waterLakeChance"), true, this, 1.0F, 100.0F, (float)this.field_175336_F.waterLakeChance), new GuiPageButtonList.GuiButtonEntry(156, I18n.format("createWorld.customize.custom.useLavaLakes"), true, this.field_175336_F.useLavaLakes), new GuiPageButtonList.GuiSlideEntry(159, I18n.format("createWorld.customize.custom.lavaLakeChance"), true, this, 10.0F, 100.0F, (float)this.field_175336_F.lavaLakeChance), new GuiPageButtonList.GuiButtonEntry(161, I18n.format("createWorld.customize.custom.useLavaOceans"), true, this.field_175336_F.useLavaOceans), new GuiPageButtonList.GuiSlideEntry(162, I18n.format("createWorld.customize.custom.fixedBiome"), true, this, -1.0F, 37.0F, (float)this.field_175336_F.fixedBiome), new GuiPageButtonList.GuiSlideEntry(163, I18n.format("createWorld.customize.custom.biomeSize"), true, this, 1.0F, 8.0F, (float)this.field_175336_F.biomeSize), new GuiPageButtonList.GuiSlideEntry(164, I18n.format("createWorld.customize.custom.riverSize"), true, this, 1.0F, 5.0F, (float)this.field_175336_F.riverSize)};
      GuiPageButtonList.GuiListEntry[] var2 = new GuiPageButtonList.GuiListEntry[]{new GuiPageButtonList.GuiLabelEntry(416, I18n.format("tile.dirt.name"), false), null, new GuiPageButtonList.GuiSlideEntry(165, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.dirtSize), new GuiPageButtonList.GuiSlideEntry(166, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.dirtCount), new GuiPageButtonList.GuiSlideEntry(167, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.dirtMinHeight), new GuiPageButtonList.GuiSlideEntry(168, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.dirtMaxHeight), new GuiPageButtonList.GuiLabelEntry(417, I18n.format("tile.gravel.name"), false), null, new GuiPageButtonList.GuiSlideEntry(169, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.gravelSize), new GuiPageButtonList.GuiSlideEntry(170, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.gravelCount), new GuiPageButtonList.GuiSlideEntry(171, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.gravelMinHeight), new GuiPageButtonList.GuiSlideEntry(172, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.gravelMaxHeight), new GuiPageButtonList.GuiLabelEntry(418, I18n.format("tile.stone.granite.name"), false), null, new GuiPageButtonList.GuiSlideEntry(173, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.graniteSize), new GuiPageButtonList.GuiSlideEntry(174, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.graniteCount), new GuiPageButtonList.GuiSlideEntry(175, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.graniteMinHeight), new GuiPageButtonList.GuiSlideEntry(176, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.graniteMaxHeight), new GuiPageButtonList.GuiLabelEntry(419, I18n.format("tile.stone.diorite.name"), false), null, new GuiPageButtonList.GuiSlideEntry(177, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.dioriteSize), new GuiPageButtonList.GuiSlideEntry(178, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.dioriteCount), new GuiPageButtonList.GuiSlideEntry(179, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.dioriteMinHeight), new GuiPageButtonList.GuiSlideEntry(180, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.dioriteMaxHeight), new GuiPageButtonList.GuiLabelEntry(420, I18n.format("tile.stone.andesite.name"), false), null, new GuiPageButtonList.GuiSlideEntry(181, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.andesiteSize), new GuiPageButtonList.GuiSlideEntry(182, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.andesiteCount), new GuiPageButtonList.GuiSlideEntry(183, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.andesiteMinHeight), new GuiPageButtonList.GuiSlideEntry(184, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.andesiteMaxHeight), new GuiPageButtonList.GuiLabelEntry(421, I18n.format("tile.oreCoal.name"), false), null, new GuiPageButtonList.GuiSlideEntry(185, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.coalSize), new GuiPageButtonList.GuiSlideEntry(186, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.coalCount), new GuiPageButtonList.GuiSlideEntry(187, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.coalMinHeight), new GuiPageButtonList.GuiSlideEntry(189, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.coalMaxHeight), new GuiPageButtonList.GuiLabelEntry(422, I18n.format("tile.oreIron.name"), false), null, new GuiPageButtonList.GuiSlideEntry(190, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.ironSize), new GuiPageButtonList.GuiSlideEntry(191, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.ironCount), new GuiPageButtonList.GuiSlideEntry(192, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.ironMinHeight), new GuiPageButtonList.GuiSlideEntry(193, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.ironMaxHeight), new GuiPageButtonList.GuiLabelEntry(423, I18n.format("tile.oreGold.name"), false), null, new GuiPageButtonList.GuiSlideEntry(194, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.goldSize), new GuiPageButtonList.GuiSlideEntry(195, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.goldCount), new GuiPageButtonList.GuiSlideEntry(196, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.goldMinHeight), new GuiPageButtonList.GuiSlideEntry(197, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.goldMaxHeight), new GuiPageButtonList.GuiLabelEntry(424, I18n.format("tile.oreRedstone.name"), false), null, new GuiPageButtonList.GuiSlideEntry(198, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.redstoneSize), new GuiPageButtonList.GuiSlideEntry(199, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.redstoneCount), new GuiPageButtonList.GuiSlideEntry(200, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.redstoneMinHeight), new GuiPageButtonList.GuiSlideEntry(201, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.redstoneMaxHeight), new GuiPageButtonList.GuiLabelEntry(425, I18n.format("tile.oreDiamond.name"), false), null, new GuiPageButtonList.GuiSlideEntry(202, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.diamondSize), new GuiPageButtonList.GuiSlideEntry(203, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.diamondCount), new GuiPageButtonList.GuiSlideEntry(204, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.diamondMinHeight), new GuiPageButtonList.GuiSlideEntry(205, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.diamondMaxHeight), new GuiPageButtonList.GuiLabelEntry(426, I18n.format("tile.oreLapis.name"), false), null, new GuiPageButtonList.GuiSlideEntry(206, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float)this.field_175336_F.lapisSize), new GuiPageButtonList.GuiSlideEntry(207, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float)this.field_175336_F.lapisCount), new GuiPageButtonList.GuiSlideEntry(208, I18n.format("createWorld.customize.custom.center"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.lapisCenterHeight), new GuiPageButtonList.GuiSlideEntry(209, I18n.format("createWorld.customize.custom.spread"), false, this, 0.0F, 255.0F, (float)this.field_175336_F.lapisSpread)};
      GuiPageButtonList.GuiListEntry[] var3 = new GuiPageButtonList.GuiListEntry[]{new GuiPageButtonList.GuiSlideEntry(100, I18n.format("createWorld.customize.custom.mainNoiseScaleX"), false, this, 1.0F, 5000.0F, this.field_175336_F.mainNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(101, I18n.format("createWorld.customize.custom.mainNoiseScaleY"), false, this, 1.0F, 5000.0F, this.field_175336_F.mainNoiseScaleY), new GuiPageButtonList.GuiSlideEntry(102, I18n.format("createWorld.customize.custom.mainNoiseScaleZ"), false, this, 1.0F, 5000.0F, this.field_175336_F.mainNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(103, I18n.format("createWorld.customize.custom.depthNoiseScaleX"), false, this, 1.0F, 2000.0F, this.field_175336_F.depthNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(104, I18n.format("createWorld.customize.custom.depthNoiseScaleZ"), false, this, 1.0F, 2000.0F, this.field_175336_F.depthNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(105, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent"), false, this, 0.01F, 20.0F, this.field_175336_F.depthNoiseScaleExponent), new GuiPageButtonList.GuiSlideEntry(106, I18n.format("createWorld.customize.custom.baseSize"), false, this, 1.0F, 25.0F, this.field_175336_F.baseSize), new GuiPageButtonList.GuiSlideEntry(107, I18n.format("createWorld.customize.custom.coordinateScale"), false, this, 1.0F, 6000.0F, this.field_175336_F.coordinateScale), new GuiPageButtonList.GuiSlideEntry(108, I18n.format("createWorld.customize.custom.heightScale"), false, this, 1.0F, 6000.0F, this.field_175336_F.heightScale), new GuiPageButtonList.GuiSlideEntry(109, I18n.format("createWorld.customize.custom.stretchY"), false, this, 0.01F, 50.0F, this.field_175336_F.stretchY), new GuiPageButtonList.GuiSlideEntry(110, I18n.format("createWorld.customize.custom.upperLimitScale"), false, this, 1.0F, 5000.0F, this.field_175336_F.upperLimitScale), new GuiPageButtonList.GuiSlideEntry(111, I18n.format("createWorld.customize.custom.lowerLimitScale"), false, this, 1.0F, 5000.0F, this.field_175336_F.lowerLimitScale), new GuiPageButtonList.GuiSlideEntry(112, I18n.format("createWorld.customize.custom.biomeDepthWeight"), false, this, 1.0F, 20.0F, this.field_175336_F.biomeDepthWeight), new GuiPageButtonList.GuiSlideEntry(113, I18n.format("createWorld.customize.custom.biomeDepthOffset"), false, this, 0.0F, 20.0F, this.field_175336_F.biomeDepthOffset), new GuiPageButtonList.GuiSlideEntry(114, I18n.format("createWorld.customize.custom.biomeScaleWeight"), false, this, 1.0F, 20.0F, this.field_175336_F.biomeScaleWeight), new GuiPageButtonList.GuiSlideEntry(115, I18n.format("createWorld.customize.custom.biomeScaleOffset"), false, this, 0.0F, 20.0F, this.field_175336_F.biomeScaleOffset)};
      GuiPageButtonList.GuiListEntry[] var4 = new GuiPageButtonList.GuiListEntry[]{new GuiPageButtonList.GuiLabelEntry(400, I18n.format("createWorld.customize.custom.mainNoiseScaleX") + ":", false), new GuiPageButtonList.EditBoxEntry(132, String.format("%5.3f", this.field_175336_F.mainNoiseScaleX), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(401, I18n.format("createWorld.customize.custom.mainNoiseScaleY") + ":", false), new GuiPageButtonList.EditBoxEntry(133, String.format("%5.3f", this.field_175336_F.mainNoiseScaleY), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(402, I18n.format("createWorld.customize.custom.mainNoiseScaleZ") + ":", false), new GuiPageButtonList.EditBoxEntry(134, String.format("%5.3f", this.field_175336_F.mainNoiseScaleZ), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(403, I18n.format("createWorld.customize.custom.depthNoiseScaleX") + ":", false), new GuiPageButtonList.EditBoxEntry(135, String.format("%5.3f", this.field_175336_F.depthNoiseScaleX), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(404, I18n.format("createWorld.customize.custom.depthNoiseScaleZ") + ":", false), new GuiPageButtonList.EditBoxEntry(136, String.format("%5.3f", this.field_175336_F.depthNoiseScaleZ), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(405, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent") + ":", false), new GuiPageButtonList.EditBoxEntry(137, String.format("%2.3f", this.field_175336_F.depthNoiseScaleExponent), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(406, I18n.format("createWorld.customize.custom.baseSize") + ":", false), new GuiPageButtonList.EditBoxEntry(138, String.format("%2.3f", this.field_175336_F.baseSize), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(407, I18n.format("createWorld.customize.custom.coordinateScale") + ":", false), new GuiPageButtonList.EditBoxEntry(139, String.format("%5.3f", this.field_175336_F.coordinateScale), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(408, I18n.format("createWorld.customize.custom.heightScale") + ":", false), new GuiPageButtonList.EditBoxEntry(140, String.format("%5.3f", this.field_175336_F.heightScale), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(409, I18n.format("createWorld.customize.custom.stretchY") + ":", false), new GuiPageButtonList.EditBoxEntry(141, String.format("%2.3f", this.field_175336_F.stretchY), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(410, I18n.format("createWorld.customize.custom.upperLimitScale") + ":", false), new GuiPageButtonList.EditBoxEntry(142, String.format("%5.3f", this.field_175336_F.upperLimitScale), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(411, I18n.format("createWorld.customize.custom.lowerLimitScale") + ":", false), new GuiPageButtonList.EditBoxEntry(143, String.format("%5.3f", this.field_175336_F.lowerLimitScale), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(412, I18n.format("createWorld.customize.custom.biomeDepthWeight") + ":", false), new GuiPageButtonList.EditBoxEntry(144, String.format("%2.3f", this.field_175336_F.biomeDepthWeight), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(413, I18n.format("createWorld.customize.custom.biomeDepthOffset") + ":", false), new GuiPageButtonList.EditBoxEntry(145, String.format("%2.3f", this.field_175336_F.biomeDepthOffset), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(414, I18n.format("createWorld.customize.custom.biomeScaleWeight") + ":", false), new GuiPageButtonList.EditBoxEntry(146, String.format("%2.3f", this.field_175336_F.biomeScaleWeight), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(415, I18n.format("createWorld.customize.custom.biomeScaleOffset") + ":", false), new GuiPageButtonList.EditBoxEntry(147, String.format("%2.3f", this.field_175336_F.biomeScaleOffset), false, this.field_175332_D)};
      this.field_175349_r = new GuiPageButtonList(this.mc, width, height, 32, height - 32, 25, this, new GuiPageButtonList.GuiListEntry[][]{var1, var2, var3, var4});

      for(int var5 = 0; var5 < 4; ++var5) {
         this.field_175342_h[var5] = I18n.format("createWorld.customize.custom.page" + var5);
      }

      this.func_175328_i();
   }

   public void func_175319_a(int var1, String var2) {
      float var3 = 0.0F;

      try {
         var3 = Float.parseFloat(var2);
      } catch (NumberFormatException var5) {
      }

      float var4 = 0.0F;
      switch(var1) {
      case 132:
         var4 = this.field_175336_F.mainNoiseScaleX = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
         break;
      case 133:
         var4 = this.field_175336_F.mainNoiseScaleY = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
         break;
      case 134:
         var4 = this.field_175336_F.mainNoiseScaleZ = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
         break;
      case 135:
         var4 = this.field_175336_F.depthNoiseScaleX = MathHelper.clamp_float(var3, 1.0F, 2000.0F);
         break;
      case 136:
         var4 = this.field_175336_F.depthNoiseScaleZ = MathHelper.clamp_float(var3, 1.0F, 2000.0F);
         break;
      case 137:
         var4 = this.field_175336_F.depthNoiseScaleExponent = MathHelper.clamp_float(var3, 0.01F, 20.0F);
         break;
      case 138:
         var4 = this.field_175336_F.baseSize = MathHelper.clamp_float(var3, 1.0F, 25.0F);
         break;
      case 139:
         var4 = this.field_175336_F.coordinateScale = MathHelper.clamp_float(var3, 1.0F, 6000.0F);
         break;
      case 140:
         var4 = this.field_175336_F.heightScale = MathHelper.clamp_float(var3, 1.0F, 6000.0F);
         break;
      case 141:
         var4 = this.field_175336_F.stretchY = MathHelper.clamp_float(var3, 0.01F, 50.0F);
         break;
      case 142:
         var4 = this.field_175336_F.upperLimitScale = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
         break;
      case 143:
         var4 = this.field_175336_F.lowerLimitScale = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
         break;
      case 144:
         var4 = this.field_175336_F.biomeDepthWeight = MathHelper.clamp_float(var3, 1.0F, 20.0F);
         break;
      case 145:
         var4 = this.field_175336_F.biomeDepthOffset = MathHelper.clamp_float(var3, 0.0F, 20.0F);
         break;
      case 146:
         var4 = this.field_175336_F.biomeScaleWeight = MathHelper.clamp_float(var3, 1.0F, 20.0F);
         break;
      case 147:
         var4 = this.field_175336_F.biomeScaleOffset = MathHelper.clamp_float(var3, 0.0F, 20.0F);
      }

      if (var4 != var3 && var3 != 0.0F) {
         ((GuiTextField)this.field_175349_r.func_178061_c(var1)).setText(this.func_175330_b(var1, var4));
      }

      ((GuiSlider)this.field_175349_r.func_178061_c(var1 - 132 + 100)).func_175218_a(var4, false);
      if (!this.field_175336_F.equals(this.field_175334_E)) {
         this.func_181031_a(true);
      }

   }

   private void func_175322_b(int var1) {
      this.field_175339_B = var1;
      this.func_175329_a(true);
   }

   public String getText(int var1, String var2, float var3) {
      return var2 + ": " + this.func_175330_b(var1, var3);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         switch(var1.id) {
         case 300:
            this.field_175343_i.chunkProviderSettingsJson = this.field_175336_F.toString();
            this.mc.displayGuiScreen(this.field_175343_i);
            break;
         case 301:
            for(int var2 = 0; var2 < this.field_175349_r.getSize(); ++var2) {
               GuiPageButtonList.GuiEntry var3 = this.field_175349_r.getListEntry(var2);
               Gui var4 = var3.func_178022_a();
               if (var4 instanceof GuiButton) {
                  GuiButton var5 = (GuiButton)var4;
                  if (var5 instanceof GuiSlider) {
                     float var6 = ((GuiSlider)var5).func_175217_d() * (0.75F + this.random.nextFloat() * 0.5F) + (this.random.nextFloat() * 0.1F - 0.05F);
                     ((GuiSlider)var5).func_175219_a(MathHelper.clamp_float(var6, 0.0F, 1.0F));
                  } else if (var5 instanceof GuiListButton) {
                     ((GuiListButton)var5).func_175212_b(this.random.nextBoolean());
                  }
               }

               Gui var9 = var3.func_178021_b();
               if (var9 instanceof GuiButton) {
                  GuiButton var10 = (GuiButton)var9;
                  if (var10 instanceof GuiSlider) {
                     float var7 = ((GuiSlider)var10).func_175217_d() * (0.75F + this.random.nextFloat() * 0.5F) + (this.random.nextFloat() * 0.1F - 0.05F);
                     ((GuiSlider)var10).func_175219_a(MathHelper.clamp_float(var7, 0.0F, 1.0F));
                  } else if (var10 instanceof GuiListButton) {
                     ((GuiListButton)var10).func_175212_b(this.random.nextBoolean());
                  }
               }
            }

            return;
         case 302:
            this.field_175349_r.func_178071_h();
            this.func_175328_i();
            break;
         case 303:
            this.field_175349_r.func_178064_i();
            this.func_175328_i();
            break;
         case 304:
            if (this.field_175338_A) {
               this.func_175322_b(304);
            }
            break;
         case 305:
            this.mc.displayGuiScreen(new GuiScreenCustomizePresets(this));
            break;
         case 306:
            this.func_175331_h();
            break;
         case 307:
            this.field_175339_B = 0;
            this.func_175331_h();
         }
      }

   }

   private void func_175328_i() {
      this.field_175345_v.enabled = this.field_175349_r.func_178059_e() != 0;
      this.field_175344_w.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
      this.field_175333_f = I18n.format("book.pageIndicator", this.field_175349_r.func_178059_e() + 1, this.field_175349_r.func_178057_f());
      this.field_175335_g = this.field_175342_h[this.field_175349_r.func_178059_e()];
      this.field_175347_t.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
   }

   private void func_175331_h() throws IOException {
      switch(this.field_175339_B) {
      case 300:
         this.actionPerformed((GuiListButton)this.field_175349_r.func_178061_c(300));
      case 301:
      case 302:
      case 303:
      default:
         break;
      case 304:
         this.func_175326_g();
      }

      this.field_175339_B = 0;
      this.field_175340_C = true;
      this.func_175329_a(false);
   }

   protected void mouseReleased(int var1, int var2, int var3) {
      super.mouseReleased(var1, var2, var3);
      if (this.field_175340_C) {
         this.field_175340_C = false;
      } else if (this.field_175339_B == 0) {
         this.field_175349_r.mouseReleased(var1, var2, var3);
      }

   }

   private void func_181031_a(boolean var1) {
      this.field_175338_A = var1;
      this.field_175346_u.enabled = var1;
   }

   public String func_175323_a() {
      return this.field_175336_F.toString().replace("\n", "");
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.field_175349_r.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, this.field_175341_a, width / 2, 2, 16777215);
      this.drawCenteredString(this.fontRendererObj, this.field_175333_f, width / 2, 12, 16777215);
      this.drawCenteredString(this.fontRendererObj, this.field_175335_g, width / 2, 22, 16777215);
      super.drawScreen(var1, var2, var3);
      if (this.field_175339_B != 0) {
         drawRect(0.0D, 0.0D, (double)width, (double)height, Integer.MIN_VALUE);
         drawHorizontalLine(width / 2 - 91, width / 2 + 90, 99, -2039584);
         drawHorizontalLine(width / 2 - 91, width / 2 + 90, 185, -6250336);
         drawVerticalLine(width / 2 - 91, 99, 185, -2039584);
         drawVerticalLine(width / 2 + 90, 99, 185, -6250336);
         float var4 = 85.0F;
         float var5 = 180.0F;
         GlStateManager.disableLighting();
         GlStateManager.disableFog();
         Tessellator var6 = Tessellator.getInstance();
         WorldRenderer var7 = var6.getWorldRenderer();
         this.mc.getTextureManager().bindTexture(optionsBackground);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         float var8 = 32.0F;
         var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         var7.pos((double)(width / 2 - 90), 185.0D, 0.0D).tex(0.0D, 2.65625D).color(64, 64, 64, 64).endVertex();
         var7.pos((double)(width / 2 + 90), 185.0D, 0.0D).tex(5.625D, 2.65625D).color(64, 64, 64, 64).endVertex();
         var7.pos((double)(width / 2 + 90), 100.0D, 0.0D).tex(5.625D, 0.0D).color(64, 64, 64, 64).endVertex();
         var7.pos((double)(width / 2 - 90), 100.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 64).endVertex();
         var6.draw();
         this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirmTitle"), width / 2, 105, 16777215);
         this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm1"), width / 2, 125, 16777215);
         this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm2"), width / 2, 135, 16777215);
         this.field_175352_x.drawButton(this.mc, var1, var2);
         this.field_175351_y.drawButton(this.mc, var1, var2);
      }

   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.field_175349_r.handleMouseInput();
   }

   public void initGui() {
      int var1 = 0;
      int var2 = 0;
      if (this.field_175349_r != null) {
         var1 = this.field_175349_r.func_178059_e();
         var2 = this.field_175349_r.getAmountScrolled();
      }

      this.field_175341_a = I18n.format("options.customizeTitle");
      this.buttonList.clear();
      this.buttonList.add(this.field_175345_v = new GuiButton(302, 20, 5, 80, 20, I18n.format("createWorld.customize.custom.prev")));
      this.buttonList.add(this.field_175344_w = new GuiButton(303, width - 100, 5, 80, 20, I18n.format("createWorld.customize.custom.next")));
      this.buttonList.add(this.field_175346_u = new GuiButton(304, width / 2 - 187, height - 27, 90, 20, I18n.format("createWorld.customize.custom.defaults")));
      this.buttonList.add(this.field_175347_t = new GuiButton(301, width / 2 - 92, height - 27, 90, 20, I18n.format("createWorld.customize.custom.randomize")));
      this.buttonList.add(this.field_175350_z = new GuiButton(305, width / 2 + 3, height - 27, 90, 20, I18n.format("createWorld.customize.custom.presets")));
      this.buttonList.add(this.field_175348_s = new GuiButton(300, width / 2 + 98, height - 27, 90, 20, I18n.format("gui.done")));
      this.field_175346_u.enabled = this.field_175338_A;
      this.field_175352_x = new GuiButton(306, width / 2 - 55, 160, 50, 20, I18n.format("gui.yes"));
      this.field_175352_x.visible = false;
      this.buttonList.add(this.field_175352_x);
      this.field_175351_y = new GuiButton(307, width / 2 + 5, 160, 50, 20, I18n.format("gui.no"));
      this.field_175351_y.visible = false;
      this.buttonList.add(this.field_175351_y);
      if (this.field_175339_B != 0) {
         this.field_175352_x.visible = true;
         this.field_175351_y.visible = true;
      }

      this.func_175325_f();
      if (var1 != 0) {
         this.field_175349_r.func_181156_c(var1);
         this.field_175349_r.scrollBy(var2);
         this.func_175328_i();
      }

   }

   public GuiCustomizeWorldScreen(GuiScreen var1, String var2) {
      this.field_175343_i = (GuiCreateWorld)var1;
      this.func_175324_a(var2);
   }
}
