/*     */ package org.neverhook.client.feature.impl.misc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.apache.commons.lang3.RandomStringUtils;
/*     */ import org.lwjgl.Sys;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class Spammer extends Feature {
/*     */   private final NumberSetting delay;
/*     */   private final BooleanSetting randomSymbols;
/*     */   private final ListSetting spammerMode;
/*     */   private int ticks;
/*     */   private int counter;
/*     */   
/*     */   public Spammer() {
/*  29 */     super("Spammer", "Автоматически спамит сообщениями в чат", Type.Misc);
/*  30 */     this.spammerMode = new ListSetting("Spammer Mode", "Default", () -> Boolean.valueOf(true), new String[] { "Default", "HvH?", "Custom", "Direct" });
/*  31 */     this.delay = new NumberSetting("Spammer Delay", 100.0F, 10.0F, 500.0F, 10.0F, () -> Boolean.valueOf(true));
/*  32 */     this.randomSymbols = new BooleanSetting("Random Symbols", true, () -> Boolean.valueOf(this.spammerMode.currentMode.equals("Custom")));
/*  33 */     addSettings(new Setting[] { (Setting)this.spammerMode, (Setting)this.delay, (Setting)this.randomSymbols });
/*     */   }
/*     */   
/*     */   private List<EntityPlayer> getPlayerByTab() {
/*  37 */     ArrayList<EntityPlayer> list = new ArrayList<>();
/*  38 */     for (NetworkPlayerInfo info : mc.player.connection.getPlayerInfoMap()) {
/*  39 */       if (info == null) {
/*     */         continue;
/*     */       }
/*  42 */       list.add(mc.world.getPlayerEntityByName(info.getGameProfile().getName()));
/*     */     } 
/*  44 */     return list;
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) throws IOException {
/*  49 */     String mode = this.spammerMode.getOptions();
/*  50 */     setSuffix(mode + ", " + (int)this.delay.getNumberValue());
/*  51 */     if (mode.equalsIgnoreCase("Default")) {
/*     */       try {
/*  53 */         String str1 = RandomStringUtils.randomAlphabetic(3);
/*  54 */         String str2 = RandomStringUtils.randomPrint(5);
/*  55 */         setSuffix("" + (int)this.delay.getNumberValue());
/*  56 */         if (this.ticks++ % (int)this.delay.getNumberValue() == 0) {
/*  57 */           mc.player.sendChatMessage("![" + str1 + "]`vk.com/neverhook ` [" + str2 + "]");
/*     */         }
/*  59 */       } catch (Exception e) {
/*  60 */         e.printStackTrace();
/*     */       } 
/*  62 */     } else if (mode.equalsIgnoreCase("HvH?")) {
/*  63 */       String str1 = RandomStringUtils.randomAlphabetic(3);
/*  64 */       String str2 = RandomStringUtils.randomPrint(5);
/*  65 */       String str3 = "";
/*  66 */       setSuffix("" + (int)this.delay.getNumberValue());
/*  67 */       if (this.ticks++ % (int)this.delay.getNumberValue() == 0) {
/*  68 */         switch (this.counter) {
/*     */           case 0:
/*  70 */             mc.player.sendChatMessage(str3 + "!Твой клиент зaлупa ебaная)) Кид@й мнe дyэль: \"/duel " + mc.player.getName() + "\".  Карта: Пляж  [" + str1 + "] [" + str2 + "]");
/*  71 */             this.counter++;
/*     */             break;
/*     */           case 1:
/*  74 */             mc.player.sendChatMessage(str3 + "!Правда думаешь твой клиент лучше?) Кидaй мне дуэль: \"/duel " + mc.player.getName() + "\". Карта: Пляж  [" + str1 + "] [" + str2 + "]");
/*  75 */             this.counter++;
/*     */             break;
/*     */           case 2:
/*  78 */             mc.player.sendChatMessage(str3 + "!Ты как себя ведешь бл9дина eбaнaя? Кiдай мне дуэль: \"/duel " + mc.player.getName() + "\".  Карта: Пляж  [" + str1 + "] [" + str2 + "]");
/*  79 */             this.counter = 0;
/*     */             break;
/*     */         } 
/*     */       }
/*  83 */     } else if (mode.equalsIgnoreCase("Custom")) {
/*  84 */       String str1 = RandomStringUtils.randomAlphabetic(3);
/*  85 */       String str2 = RandomStringUtils.randomPrint(5);
/*  86 */       File file = new File(mc.mcDataDir + "\\neverhook", "spammer.txt");
/*  87 */       if (!file.exists()) {
/*  88 */         file.createNewFile();
/*     */       }
/*  90 */       Scanner scanner = new Scanner(file);
/*  91 */       while (scanner.hasNextLine()) {
/*  92 */         if (this.ticks++ % (int)this.delay.getNumberValue() == 0 && 
/*  93 */           this.counter == 0) {
/*  94 */           if (this.randomSymbols.getBoolValue()) {
/*  95 */             mc.player.sendChatMessage("! '" + scanner.nextLine() + "' " + str2 + str1);
/*     */           } else {
/*  97 */             mc.player.sendChatMessage(scanner.nextLine());
/*     */           } 
/*  99 */           this.counter = 0;
/*     */         } 
/*     */         
/* 102 */         scanner.close();
/*     */       } 
/* 104 */     } else if (mode.equalsIgnoreCase("Direct")) {
/* 105 */       for (EntityPlayer e : getPlayerByTab()) {
/* 106 */         if (e == null)
/*     */           continue; 
/* 108 */         if (this.ticks++ % (int)this.delay.getNumberValue() == 0 && 
/* 109 */           e != mc.player) {
/* 110 */           mc.player.sendChatMessage("/msg " + e.getName() + " vk.com/neverhook лучший чит, быстрее покупай!");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 119 */     String mode = this.spammerMode.getOptions();
/* 120 */     if (mode.equalsIgnoreCase("Custom")) {
/* 121 */       Sys.openURL(mc.mcDataDir + "\\neverhook\\spammer.txt");
/*     */     }
/* 123 */     super.onEnable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\Spammer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */