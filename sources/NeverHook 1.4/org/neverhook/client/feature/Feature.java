/*     */ package org.neverhook.client.feature;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventManager;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.hud.ClientSounds;
/*     */ import org.neverhook.client.feature.impl.hud.Notifications;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.misc.MusicHelper;
/*     */ import org.neverhook.client.helpers.render.ScreenHelper;
/*     */ import org.neverhook.client.settings.Configurable;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.notification.NotificationManager;
/*     */ import org.neverhook.client.ui.notification.NotificationType;
/*     */ 
/*     */ public class Feature extends Configurable implements Helper {
/*     */   public Type type;
/*     */   public boolean state;
/*     */   public boolean visible = true;
/*  26 */   public ScreenHelper screenHelper = new ScreenHelper(0.0F, 0.0F); private String label;
/*     */   private String suffix;
/*     */   private int bind;
/*     */   private String desc;
/*     */   
/*     */   public Feature(String label, String desc, Type type) {
/*  32 */     this.label = label;
/*  33 */     this.desc = desc;
/*  34 */     this.type = type;
/*  35 */     this.bind = 0;
/*  36 */     this.state = false;
/*     */   }
/*     */   
/*     */   public JsonObject save() {
/*  40 */     JsonObject object = new JsonObject();
/*  41 */     object.addProperty("state", Boolean.valueOf(getState()));
/*  42 */     object.addProperty("keyIndex", Integer.valueOf(getBind()));
/*  43 */     object.addProperty("visible", Boolean.valueOf(isVisible()));
/*  44 */     JsonObject propertiesObject = new JsonObject();
/*  45 */     for (Setting set : getSettings()) {
/*  46 */       if (getSettings() != null) {
/*  47 */         if (set instanceof BooleanSetting) {
/*  48 */           propertiesObject.addProperty(set.getName(), Boolean.valueOf(((BooleanSetting)set).getBoolValue()));
/*  49 */         } else if (set instanceof ListSetting) {
/*  50 */           propertiesObject.addProperty(set.getName(), ((ListSetting)set).getCurrentMode());
/*  51 */         } else if (set instanceof NumberSetting) {
/*  52 */           propertiesObject.addProperty(set.getName(), Float.valueOf(((NumberSetting)set).getNumberValue()));
/*  53 */         } else if (set instanceof ColorSetting) {
/*  54 */           propertiesObject.addProperty(set.getName(), Integer.valueOf(((ColorSetting)set).getColorValue()));
/*     */         } 
/*     */       }
/*  57 */       object.add("Settings", (JsonElement)propertiesObject);
/*     */     } 
/*  59 */     return object;
/*     */   }
/*     */   
/*     */   public void load(JsonObject object) {
/*  63 */     if (object != null) {
/*  64 */       if (object.has("state")) {
/*  65 */         setState(object.get("state").getAsBoolean());
/*     */       }
/*  67 */       if (object.has("visible")) {
/*  68 */         setVisible(object.get("visible").getAsBoolean());
/*     */       }
/*  70 */       if (object.has("keyIndex")) {
/*  71 */         setBind(object.get("keyIndex").getAsInt());
/*     */       }
/*  73 */       for (Setting set : getSettings()) {
/*  74 */         JsonObject propertiesObject = object.getAsJsonObject("Settings");
/*  75 */         if (set == null)
/*     */           continue; 
/*  77 */         if (propertiesObject == null)
/*     */           continue; 
/*  79 */         if (!propertiesObject.has(set.getName()))
/*     */           continue; 
/*  81 */         if (set instanceof BooleanSetting) {
/*  82 */           ((BooleanSetting)set).setBoolValue(propertiesObject.get(set.getName()).getAsBoolean()); continue;
/*  83 */         }  if (set instanceof ListSetting) {
/*  84 */           ((ListSetting)set).setListMode(propertiesObject.get(set.getName()).getAsString()); continue;
/*  85 */         }  if (set instanceof NumberSetting) {
/*  86 */           ((NumberSetting)set).setValueNumber(propertiesObject.get(set.getName()).getAsFloat()); continue;
/*  87 */         }  if (set instanceof ColorSetting) {
/*  88 */           ((ColorSetting)set).setColorValue(propertiesObject.get(set.getName()).getAsInt());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ScreenHelper getScreenHelper() {
/*  95 */     return this.screenHelper;
/*     */   }
/*     */   
/*     */   public String getSuffix() {
/*  99 */     return (this.suffix == null) ? this.label : this.suffix;
/*     */   }
/*     */   
/*     */   public void setSuffix(String suffix) {
/* 103 */     this.suffix = suffix;
/* 104 */     this.suffix = getLabel() + " - " + suffix;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 108 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 112 */     this.visible = visible;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/* 116 */     return !this.visible;
/*     */   }
/*     */   
/*     */   public void setHidden(boolean visible) {
/* 120 */     this.visible = !visible;
/*     */   }
/*     */   
/*     */   public String getLabel() {
/* 124 */     return this.label;
/*     */   }
/*     */   
/*     */   public void setLabel(String label) {
/* 128 */     this.label = label;
/*     */   }
/*     */   
/*     */   public int getBind() {
/* 132 */     return this.bind;
/*     */   }
/*     */   
/*     */   public void setBind(int bind) {
/* 136 */     this.bind = bind;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/* 140 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 144 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public Type getType() {
/* 148 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setCategory(Type type) {
/* 152 */     this.type = type;
/*     */   }
/*     */   
/*     */   public void onEnable() {
/* 156 */     if (NeverHook.instance.featureManager.getFeatureByClass((Class)ClientSounds.class).getState()) {
/* 157 */       MusicHelper.playSound("enable.wav");
/*     */     }
/* 159 */     EventManager.register(this);
/* 160 */     if (!getLabel().contains("ClickGui") && !getLabel().contains("Client Font") && !getLabel().contains("Notifications") && Notifications.state.getBoolValue()) {
/* 161 */       NotificationManager.publicity(getLabel(), "was enabled!", 1, NotificationType.INFO);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDisable() {
/* 166 */     if (NeverHook.instance.featureManager.getFeatureByClass((Class)ClientSounds.class).getState()) {
/* 167 */       MusicHelper.playSound("disable.wav");
/*     */     }
/* 169 */     EventManager.unregister(this);
/* 170 */     if (!getLabel().contains("ClickGui") && !getLabel().contains("Client Font") && !getLabel().contains("Notifications") && Notifications.state.getBoolValue()) {
/* 171 */       NotificationManager.publicity(getLabel(), "was disabled!", 1, NotificationType.INFO);
/*     */     }
/*     */   }
/*     */   
/*     */   public void state() {
/* 176 */     this.state = !this.state;
/*     */     
/* 178 */     if (this.state) {
/* 179 */       onEnable();
/*     */     } else {
/* 181 */       onDisable();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean getState() {
/* 186 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(boolean state) {
/* 190 */     if (state) {
/* 191 */       EventManager.register(this);
/*     */     } else {
/* 193 */       EventManager.unregister(this);
/*     */     } 
/* 195 */     this.state = state;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\Feature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */