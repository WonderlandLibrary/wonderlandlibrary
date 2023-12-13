/*     */ package org.neverhook.client;
/*     */ 
/*     */ import baritone.api.BaritoneAPI;
/*     */ import java.io.IOException;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.neverhook.client.cmd.CommandManager;
/*     */ import org.neverhook.client.components.DiscordRPC;
/*     */ import org.neverhook.client.components.SplashProgress;
/*     */ import org.neverhook.client.event.EventManager;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.game.EventShutdownClient;
/*     */ import org.neverhook.client.event.events.impl.input.EventInputKey;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.FeatureManager;
/*     */ import org.neverhook.client.files.FileManager;
/*     */ import org.neverhook.client.files.impl.FriendConfig;
/*     */ import org.neverhook.client.files.impl.HudConfig;
/*     */ import org.neverhook.client.files.impl.MacroConfig;
/*     */ import org.neverhook.client.files.impl.XrayConfig;
/*     */ import org.neverhook.client.friend.FriendManager;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.render.BlurUtil;
/*     */ import org.neverhook.client.macro.Macro;
/*     */ import org.neverhook.client.macro.MacroManager;
/*     */ import org.neverhook.client.settings.config.ConfigManager;
/*     */ import org.neverhook.client.ui.GuiCapeSelector;
/*     */ import org.neverhook.client.ui.clickgui.ClickGuiScreen;
/*     */ import org.neverhook.client.ui.components.changelog.ChangeManager;
/*     */ import org.neverhook.client.ui.components.draggable.DraggableManager;
/*     */ import org.neverhook.client.ui.newclickgui.ClickGuiScreen;
/*     */ import viamcp.ViaMCP;
/*     */ 
/*     */ public class NeverHook implements Helper {
/*  36 */   public static NeverHook instance = new NeverHook();
/*     */   
/*  38 */   public String name = "NeverHook"; public String type = "Premium"; public String version = "1.4"; public String status = "Release"; public String build = "161021";
/*  39 */   public FeatureManager featureManager = new FeatureManager();
/*     */   
/*     */   public ClickGuiScreen clickGui;
/*     */   public ClickGuiScreen newClickGui;
/*     */   public CommandManager commandManager;
/*     */   public ConfigManager configManager;
/*     */   public MacroManager macroManager;
/*     */   public FileManager fileManager;
/*     */   public DraggableManager draggableManager;
/*     */   public FriendManager friendManager;
/*     */   public RotationHelper.Rotation rotation;
/*     */   public BlurUtil blurUtil;
/*     */   public ChangeManager changeManager;
/*     */   
/*     */   public void load() throws IOException {
/*  54 */     SplashProgress.setProgress(1);
/*     */     
/*  56 */     Display.setTitle(this.name + " " + this.type + " " + this.version);
/*     */     
/*  58 */     (new DiscordRPC()).init();
/*     */     
/*  60 */     GuiCapeSelector.Selector.setCapeName("neverhookcape3");
/*  61 */     (this.fileManager = new FileManager()).loadFiles();
/*  62 */     this.featureManager = new FeatureManager();
/*  63 */     this.clickGui = new ClickGuiScreen();
/*  64 */     this.newClickGui = new ClickGuiScreen();
/*  65 */     this.commandManager = new CommandManager();
/*  66 */     this.configManager = new ConfigManager();
/*  67 */     this.macroManager = new MacroManager();
/*  68 */     this.draggableManager = new DraggableManager();
/*  69 */     this.friendManager = new FriendManager();
/*  70 */     this.rotation = new RotationHelper.Rotation();
/*  71 */     this.blurUtil = new BlurUtil();
/*  72 */     this.changeManager = new ChangeManager();
/*     */     
/*  74 */     BaritoneAPI.getProvider().getPrimaryBaritone();
/*     */     
/*     */     try {
/*  77 */       ViaMCP.getInstance().start();
/*  78 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  83 */       this.fileManager.getFile(FriendConfig.class).loadFile();
/*  84 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*     */     try {
/*  88 */       this.fileManager.getFile(MacroConfig.class).loadFile();
/*  89 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  94 */       this.fileManager.getFile(HudConfig.class).loadFile();
/*  95 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*     */     try {
/*  99 */       this.fileManager.getFile(XrayConfig.class).loadFile();
/* 100 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 103 */     EventManager.register(this.rotation);
/* 104 */     EventManager.register(this);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void shutDown(EventShutdownClient event) {
/* 110 */     EventManager.unregister(this);
/* 111 */     (this.fileManager = new FileManager()).saveFiles();
/* 112 */     (new DiscordRPC()).shutdown();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onInputKey(EventInputKey event) {
/* 117 */     for (Feature feature : this.featureManager.getFeatureList()) {
/* 118 */       if (feature.getBind() == event.getKey()) {
/* 119 */         feature.state();
/*     */       }
/*     */     } 
/* 122 */     for (Macro macro : this.macroManager.getMacros()) {
/* 123 */       if (macro.getKey() == Keyboard.getEventKey() && 
/* 124 */         mc.player.getHealth() > 0.0F && mc.player != null)
/* 125 */         mc.player.sendChatMessage(macro.getValue()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\NeverHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */