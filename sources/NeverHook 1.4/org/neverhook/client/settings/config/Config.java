/*    */ package org.neverhook.client.settings.config;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ 
/*    */ public final class Config
/*    */   implements ConfigUpdater {
/*    */   private final String name;
/*    */   private final File file;
/*    */   
/*    */   public Config(String name) {
/* 15 */     this.name = name;
/* 16 */     this.file = new File(ConfigManager.configDirectory, name + ".json");
/*    */     
/* 18 */     if (!this.file.exists()) {
/*    */       try {
/* 20 */         this.file.createNewFile();
/* 21 */       } catch (Exception exception) {}
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public File getFile() {
/* 27 */     return this.file;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonObject save() {
/* 36 */     JsonObject jsonObject = new JsonObject();
/* 37 */     JsonObject modulesObject = new JsonObject();
/* 38 */     JsonObject panelObject = new JsonObject();
/*    */     
/* 40 */     for (Feature module : NeverHook.instance.featureManager.getFeatureList()) {
/* 41 */       modulesObject.add(module.getLabel(), (JsonElement)module.save());
/*    */     }
/*    */     
/* 44 */     jsonObject.add("Features", (JsonElement)modulesObject);
/*    */     
/* 46 */     return jsonObject;
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(JsonObject object) {
/* 51 */     if (object.has("Features")) {
/* 52 */       JsonObject modulesObject = object.getAsJsonObject("Features");
/* 53 */       for (Feature module : NeverHook.instance.featureManager.getFeatureList()) {
/* 54 */         module.setState(false);
/* 55 */         module.load(modulesObject.getAsJsonObject(module.getLabel()));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\config\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */