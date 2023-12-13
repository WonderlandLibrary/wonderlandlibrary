package cow.milkgod.cheese.ui;

import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.ttf.FontUtils;
import cow.milkgod.cheese.utils.Comparator;
import cow.milkgod.cheese.utils.R2DUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiIngameHook {
   private static int fps = Integer.valueOf(Minecraft.func_175610_ah()).intValue();
   private static int moduleColor = 11184810;
   public static String Selected = "";
   private static int Height = 1;
   private static int Width = 0;
   private static int moduleHeight = 1;
   private static String CombatArrows = "<<";
   private static String ExploitsArrows = "<<";
   private static String MovementArrows = "<<";
   private static String PlayerArrows = "<<";
   private static String RenderArrows = "<<";
   private static int CombatColor = 16755213;
   private static int ExploitsColor = 16755213;
   private static int MovementColor = 16755213;
   private static int PlayerColor = 16755213;
   private static int RenderColor = 16755213;
   private static boolean CombatTab = false;
   private static boolean ExploitsTab = false;
   private static boolean MovementTab = false;
   private static boolean PlayerTab = false;
   private static boolean RenderTab = false;
   private static ArrayList Combat = new ArrayList();
   private static ArrayList Exploits = new ArrayList();
   private static ArrayList Movement = new ArrayList();
   private static ArrayList Player = new ArrayList();
   private static ArrayList Render = new ArrayList();
   private static int tabGuiHeight = 18;
   private static final int rectWidth = 124;
   private static int rectHeight = 12;
   private static int rectHeight1 = 12;
   private static int Y = 22;
   private static int pY = 0;
   private static float pY2 = 0.0F;
   private static int rectY = 32;
   public static boolean drawStuffStatus = true;
   public static boolean drawTabui = true;
   public static boolean drawArraylist = true;
   public static boolean drawCoords = true;
   public static boolean drawWatermark = true;
   public static boolean drawPotionStatus = false;
   public static boolean tbGreen = false;
   public static boolean tbRed = false;
   public static boolean tbCheese = true;
   public static boolean tbBlue = false;
   public ScaledResolution scrs;
   private static Minecraft mc;
   private static boolean[] keyStates = new boolean[256];

   public static void startHud() {
      hud();
   }

   public static void hud() {
      ScaledResolution scaledRes = new ScaledResolution(Wrapper.mc, Wrapper.mc.displayWidth, Wrapper.mc.displayHeight);
      drawStuffStatus(scaledRes);
      drawTabGui();
      drawCoords(scaledRes);
      GL11.glPushMatrix();
      FontUtils var10000 = Wrapper.fu_default_big;
      Cheese.getInstance();
      var10000.drawStringWithShadow(Cheese.Client_Name, 1.0F, 1.0F, -1);
      var10000 = Wrapper.fu_default_big;
      StringBuilder var10001 = new StringBuilder();
      Cheese.getInstance();
      String var1 = var10001.append(Cheese.Client_Version).toString();
      FontRenderer var10003 = Wrapper.fr;
      Cheese.getInstance();
      var10000.drawStringWithShadow(var1, 10.0F + (float)var10003.getStringWidth(String.valueOf(Cheese.Client_Name) + "  ") - 1.0F, 1.0F, -1862314667);
      Wrapper.fu_default_big.drawStringWithShadow(" ", 10.0F + (float)Wrapper.fr.getStringWidth(" ") - 1.0F, 1.0F, -1);
      GL11.glPopMatrix();
      renderArrayList(scaledRes);
      Wrapper.fu_default_big.drawStringWithShadow(" ", 10.0F + (float)Wrapper.fr.getStringWidth(" ") - 1.0F, 1.0F, -1);
   }

   public static void drawCoords(ScaledResolution sr) {
      if(!drawPotionStatus) {
         pY = 0;
      }

      pY2 = (float)(pY - 30);
      double posX = Wrapper.mc.thePlayer.posX;
      double posY = Wrapper.mc.thePlayer.posY;
      double posZ = Wrapper.mc.thePlayer.posZ;
      DecimalFormat numberFormat = new DecimalFormat("#.0");
      float posX2 = (float)(sr.getScaledWidth() - Wrapper.fu_default.getWidthInt(String.valueOf(numberFormat.format(posX)) + 10));
      float posY2 = (float)(sr.getScaledWidth() - Wrapper.fu_default.getWidthInt(String.valueOf(numberFormat.format(posY)) + 100));
      float posZ2 = (float)(sr.getScaledWidth() - Wrapper.fu_default.getWidthInt(String.valueOf(numberFormat.format(posZ)) + 100));
      Wrapper.fu_default.drawStringWithShadow("§7" + numberFormat.format(posX) + " §c:X", posX2 - 3.0F, (float)sr.getScaledHeight() + pY2, 0);
      pY2 += 9.0F;
      Wrapper.fu_default.drawStringWithShadow("§7" + numberFormat.format(posY) + " §c:Y", posY2 + 2.0F, (float)sr.getScaledHeight() + pY2, 0);
      pY2 += 9.0F;
      Wrapper.fu_default.drawStringWithShadow("§7" + numberFormat.format(posZ) + " §c:Z", posZ2 + 2.0F, (float)sr.getScaledHeight() + pY2, 0);
   }

   public static void drawStuffStatus(ScaledResolution scaledRes) {
      boolean currentItem = true;
      GL11.glPushMatrix();
      RenderItem ir = new RenderItem(Wrapper.mc.getTextureManager(), Wrapper.mc.modelManager);
      ArrayList stuff = new ArrayList();
      boolean onwater = Wrapper.mc.thePlayer.isEntityAlive() && Wrapper.mc.thePlayer.isInsideOfMaterial(Material.water);
      int split = 15;

      for(int errything = 3; errything >= 0; --errything) {
         ItemStack armer = Wrapper.mc.thePlayer.inventory.armorInventory[errything];
         if(armer != null) {
            stuff.add(armer);
         }
      }

      if(Wrapper.mc.thePlayer.getCurrentEquippedItem() != null) {
         stuff.add(Wrapper.mc.thePlayer.getCurrentEquippedItem());
      }

      Iterator var16 = stuff.iterator();

      while(true) {
         NBTTagList enchants;
         do {
            if(!var16.hasNext()) {
               GL11.glPopMatrix();
               return;
            }

            ItemStack var15 = (ItemStack)var16.next();
            if(Wrapper.mc.theWorld != null) {
               RenderHelper.enableGUIStandardItemLighting();
               ir.func_175042_a(var15, split + scaledRes.getScaledWidth() / 2 - 4, scaledRes.getScaledHeight() - (onwater?66:56));
               ir.func_175030_a(Minecraft.getMinecraft().fontRendererObj, var15, split + scaledRes.getScaledWidth() / 2 - 4, scaledRes.getScaledHeight() - (onwater?65:55));
               RenderHelper.enableGUIStandardItemLighting();
               split += 16;
            }

            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.clear(256);
            enchants = var15.getEnchantmentTagList();
         } while(enchants == null);

         int ency = 0;

         for(int index2 = 0; index2 < enchants.tagCount(); ++index2) {
            short id = enchants.getCompoundTagAt(index2).getShort("id");
            short level = enchants.getCompoundTagAt(index2).getShort("lvl");
            if(Enchantment.field_180311_a[id] != null) {
               Enchantment enc = Enchantment.field_180311_a[id];
               String encName = enc.getTranslatedName(level).substring(0, 2).toLowerCase();
               R2DUtils.drawSmallString(String.valueOf(encName) + "§b" + level, split - 30 + scaledRes.getScaledWidth() / 2 + 12, scaledRes.getScaledHeight() - (onwater?65:55) + ency, -5592406);
               ency += 4;
            }
         }
      }
   }

   private static void renderArrayList(ScaledResolution sr) {
      int yCount = 2;
      Cheese.getInstance();
      ModuleManager moduleManager = Cheese.moduleManager;
      Iterator var4 = ModuleManager.activeModules.iterator();

      while(var4.hasNext()) {
         Module m = (Module)var4.next();
         m.onRender();
         String name = m.getDisplayName();
         int posZ2 = sr.getScaledWidth() - Wrapper.fu_default.getWidthInt(name);
         if(m.getState() && m.isVisible()) {
            Wrapper.fu_default.drawStringWithShadow(name, (float)(posZ2 - 2), (float)yCount, m.getColour());
            yCount += 10;
         }
      }

      ModuleManager.activeModules.sort(new Comparator());
   }

   public static void drawTabGui() {
      if(tbBlue) {
         Cheese.tabGui.setColourBox(-484140362);
         Cheese.tabGui.drawGui(2, tabGuiHeight);
      }

      if(tbCheese) {
         Cheese.tabGui.setColourBox(-1862314667);
         Cheese.tabGui.drawGui(2, tabGuiHeight);
      }

      if(tbRed) {
         System.out.println("Red");
         Cheese.tabGui.setColourBox(-1862314667);
         Cheese.tabGui.drawGui(2, tabGuiHeight);
      }

      if(tbGreen) {
         Cheese.tabGui.setColourBox(-1873412267);
         Cheese.tabGui.drawGui(2, tabGuiHeight);
      }

   }

   public static boolean checkKey(int i) {
      return Wrapper.mc.currentScreen != null?false:(Keyboard.isKeyDown(i) != keyStates[i]?(keyStates[i] = !keyStates[i]):false);
   }
}
