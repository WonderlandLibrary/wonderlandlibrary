/*     */ package org.neverhook.client.feature;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.combat.Criticals;
/*     */ import org.neverhook.client.feature.impl.ghost.TriggerBot;
/*     */ import org.neverhook.client.feature.impl.hud.HUD;
/*     */ import org.neverhook.client.feature.impl.misc.AntiVanish;
/*     */ import org.neverhook.client.feature.impl.misc.EntityFeatures;
/*     */ import org.neverhook.client.feature.impl.misc.ItemScroller;
/*     */ import org.neverhook.client.feature.impl.misc.PortalFeatures;
/*     */ import org.neverhook.client.feature.impl.player.MLG;
/*     */ import org.neverhook.client.feature.impl.player.SpeedMine;
/*     */ import org.neverhook.client.feature.impl.visual.FogColor;
/*     */ import org.neverhook.client.feature.impl.visual.Radar;
/*     */ 
/*     */ public class FeatureManager {
/*  18 */   public CopyOnWriteArrayList<Feature> features = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeatureManager() {
/*  24 */     this.features.add(new ClickGui());
/*  25 */     this.features.add(new HUD());
/*  26 */     this.features.add(new FeatureList());
/*  27 */     this.features.add(new Notifications());
/*  28 */     this.features.add(new Keystrokes());
/*  29 */     this.features.add(new WaterMark());
/*  30 */     this.features.add(new ClientSounds());
/*  31 */     this.features.add(new ChinaHat());
/*  32 */     this.features.add(new EnderPearlESP());
/*  33 */     this.features.add(new WorldColor());
/*  34 */     this.features.add(new AutoClicker());
/*  35 */     this.features.add(new Eagle());
/*  36 */     this.features.add(new ChunkAnimator());
/*  37 */     this.features.add(new MLG());
/*  38 */     this.features.add(new SelfDamage());
/*  39 */     this.features.add(new HurtClip());
/*  40 */     this.features.add(new WTap());
/*  41 */     this.features.add(new FastBow());
/*  42 */     this.features.add(new AirStuck());
/*  43 */     this.features.add(new BetterChat());
/*  44 */     this.features.add(new Trajectories());
/*  45 */     this.features.add(new HitColor());
/*  46 */     this.features.add(new PortalFeatures());
/*  47 */     this.features.add(new CameraNoClip());
/*     */ 
/*     */     
/*  50 */     this.features.add(new FreeCam());
/*  51 */     this.features.add(new Disabler());
/*  52 */     this.features.add(new StreamerMode());
/*  53 */     this.features.add(new FakeHack());
/*  54 */     this.features.add(new ItemScroller());
/*  55 */     this.features.add(new Fucker());
/*  56 */     this.features.add(new XRay());
/*  57 */     this.features.add(new Nuker());
/*  58 */     this.features.add(new Spammer());
/*  59 */     this.features.add(new FlagDetector());
/*  60 */     this.features.add(new MiddleClickFriend());
/*  61 */     this.features.add(new DeathCoordinates());
/*  62 */     this.features.add(new AntiVanish());
/*  63 */     this.features.add(new EntityFeatures());
/*  64 */     this.features.add(new Ambience());
/*  65 */     this.features.add(new AutoBypass());
/*  66 */     this.features.add(new AntiFreeze());
/*  67 */     this.features.add(new ServerCrasher());
/*  68 */     this.features.add(new Secret());
/*     */ 
/*     */     
/*  71 */     this.features.add(new KillAura());
/*  72 */     this.features.add(new AntiBot());
/*  73 */     this.features.add(new TargetStrafe());
/*  74 */     this.features.add(new TriggerBot());
/*  75 */     this.features.add(new AutoShift());
/*  76 */     this.features.add(new AutoTotem());
/*  77 */     this.features.add(new PushAttack());
/*  78 */     this.features.add(new AutoArmor());
/*  79 */     this.features.add(new AimAssist());
/*  80 */     this.features.add(new Velocity());
/*  81 */     this.features.add(new KeepSprint());
/*  82 */     this.features.add(new AntiAim());
/*  83 */     this.features.add(new AntiCrystal());
/*  84 */     this.features.add(new AutoPotion());
/*  85 */     this.features.add(new Criticals());
/*  86 */     this.features.add(new HitBoxes());
/*  87 */     this.features.add(new HitParticles());
/*  88 */     this.features.add(new AutoGapple());
/*  89 */     this.features.add(new Reach());
/*  90 */     this.features.add(new NoFriendDamage());
/*     */ 
/*     */     
/*  93 */     this.features.add(new Scaffold());
/*  94 */     this.features.add(new AutoSprint());
/*  95 */     this.features.add(new FastClimb());
/*  96 */     this.features.add(new AntiLevitation());
/*  97 */     this.features.add(new LiquidWalk());
/*  98 */     this.features.add(new WaterLeave());
/*  99 */     this.features.add(new LongJump());
/* 100 */     this.features.add(new Speed());
/* 101 */     this.features.add(new Timer());
/* 102 */     this.features.add(new Step());
/* 103 */     this.features.add(new Flight());
/* 104 */     this.features.add(new ElytraFlight());
/* 105 */     this.features.add(new WallClimb());
/* 106 */     this.features.add(new AirJump());
/* 107 */     this.features.add(new WebTP());
/* 108 */     this.features.add(new TeleportExploit());
/* 109 */     this.features.add(new WaterSpeed());
/* 110 */     this.features.add(new ParkourHelper());
/* 111 */     this.features.add(new FakeLags());
/* 112 */     this.features.add(new HighJump());
/* 113 */     this.features.add(new Strafe());
/*     */ 
/*     */     
/* 116 */     this.features.add(new GuiWalk());
/* 117 */     this.features.add(new AutoRespawn());
/* 118 */     this.features.add(new AntiPush());
/* 119 */     this.features.add(new MiddleClickPearl());
/* 120 */     this.features.add(new NoClip());
/* 121 */     this.features.add(new NoDelay());
/* 122 */     this.features.add(new ChestStealer());
/* 123 */     this.features.add(new InventoryManager());
/* 124 */     this.features.add(new NoFall());
/* 125 */     this.features.add(new AntiVoid());
/* 126 */     this.features.add(new NoSlowDown());
/* 127 */     this.features.add(new NoRotate());
/* 128 */     this.features.add(new AntiAFK());
/* 129 */     this.features.add(new NoInteract());
/* 130 */     this.features.add(new AutoFarm());
/* 131 */     this.features.add(new FastEat());
/* 132 */     this.features.add(new XCarry());
/* 133 */     this.features.add(new ClipHelper());
/* 134 */     this.features.add(new SafeWalk());
/* 135 */     this.features.add(new AutoFish());
/* 136 */     this.features.add(new AutoTool());
/* 137 */     this.features.add(new SpeedMine());
/* 138 */     this.features.add(new NoWeb());
/* 139 */     this.features.add(new AutoAuth());
/* 140 */     this.features.add(new SolidWeb());
/* 141 */     this.features.add(new PearlLogger());
/* 142 */     this.features.add(new BullingBot());
/* 143 */     this.features.add(new ChatAppend());
/*     */ 
/*     */     
/* 146 */     this.features.add(new EntityESP());
/* 147 */     this.features.add(new BlockESP());
/* 148 */     this.features.add(new NameTags());
/* 149 */     this.features.add(new FullBright());
/* 150 */     this.features.add(new Animations());
/* 151 */     this.features.add(new NoRender());
/* 152 */     this.features.add(new ViewModel());
/* 153 */     this.features.add(new ItemPhysics());
/* 154 */     this.features.add(new Scoreboard());
/* 155 */     this.features.add(new PersonViewer());
/* 156 */     this.features.add(new Chams());
/* 157 */     this.features.add(new FogColor());
/* 158 */     this.features.add(new Crosshair());
/* 159 */     this.features.add(new Radar());
/* 160 */     this.features.add(new Trails());
/* 161 */     this.features.add(new EnchantmentColor());
/* 162 */     this.features.add(new ItemESP());
/* 163 */     this.features.add(new MobESP());
/* 164 */     this.features.add(new BlockOverlay());
/* 165 */     this.features.add(new Tracers());
/* 166 */     this.features.add(new ChestESP());
/*     */   }
/*     */   
/*     */   public List<Feature> getFeatureList() {
/* 170 */     return this.features;
/*     */   }
/*     */   
/*     */   public List<Feature> getFeaturesForCategory(Type category) {
/* 174 */     List<Feature> featureList = new ArrayList<>();
/* 175 */     for (Feature feature : getFeatureList()) {
/* 176 */       if (feature.getType() == category) {
/* 177 */         featureList.add(feature);
/*     */       }
/*     */     } 
/* 180 */     return featureList;
/*     */   }
/*     */   
/*     */   public Feature getFeatureByClass(Class<? extends Feature> classFeature) {
/* 184 */     for (Feature feature : getFeatureList()) {
/* 185 */       if (feature != null && 
/* 186 */         feature.getClass() == classFeature) {
/* 187 */         return feature;
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return null;
/*     */   }
/*     */   
/*     */   public Feature getFeatureByLabel(String name) {
/* 195 */     for (Feature feature : getFeatureList()) {
/* 196 */       if (feature.getLabel().equals(name)) {
/* 197 */         return feature;
/*     */       }
/*     */     } 
/* 200 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\FeatureManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */