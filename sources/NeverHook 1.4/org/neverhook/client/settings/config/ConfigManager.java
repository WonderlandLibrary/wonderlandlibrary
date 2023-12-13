/*     */ package org.neverhook.client.settings.config;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.neverhook.client.NeverHook;
/*     */ 
/*     */ public final class ConfigManager extends Manager<Config> {
/*  14 */   public static final File configDirectory = new File(NeverHook.instance.name, "configs");
/*  15 */   private static final ArrayList<Config> loadedConfigs = new ArrayList<>();
/*     */   
/*     */   public ConfigManager() {
/*  18 */     setContents(loadConfigs());
/*  19 */     configDirectory.mkdirs();
/*     */   }
/*     */   
/*     */   private static ArrayList<Config> loadConfigs() {
/*  23 */     File[] files = configDirectory.listFiles();
/*  24 */     if (files != null)
/*  25 */       for (File file : files) {
/*  26 */         if (FilenameUtils.getExtension(file.getName()).equals("json")) {
/*  27 */           loadedConfigs.add(new Config(FilenameUtils.removeExtension(file.getName())));
/*     */         }
/*     */       }  
/*  30 */     return loadedConfigs;
/*     */   }
/*     */   
/*     */   public static ArrayList<Config> getLoadedConfigs() {
/*  34 */     return loadedConfigs;
/*     */   }
/*     */   
/*     */   public void load() {
/*  38 */     if (!configDirectory.exists()) {
/*  39 */       configDirectory.mkdirs();
/*     */     }
/*  41 */     if (configDirectory != null) {
/*  42 */       File[] files = configDirectory.listFiles(f -> (!f.isDirectory() && FilenameUtils.getExtension(f.getName()).equals("json")));
/*  43 */       for (File f : files) {
/*  44 */         Config config = new Config(FilenameUtils.removeExtension(f.getName()).replace(" ", ""));
/*  45 */         loadedConfigs.add(config);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean loadConfig(String configName) {
/*  51 */     if (configName == null)
/*  52 */       return false; 
/*  53 */     Config config = findConfig(configName);
/*  54 */     if (config == null)
/*  55 */       return false; 
/*     */     try {
/*  57 */       FileReader reader = new FileReader(config.getFile());
/*  58 */       JsonParser parser = new JsonParser();
/*  59 */       JsonObject object = (JsonObject)parser.parse(reader);
/*  60 */       config.load(object);
/*  61 */       return true;
/*  62 */     } catch (FileNotFoundException e) {
/*  63 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean saveConfig(String configName) {
/*  68 */     if (configName == null)
/*  69 */       return false; 
/*     */     Config config;
/*  71 */     if ((config = findConfig(configName)) == null) {
/*  72 */       Config newConfig = config = new Config(configName);
/*  73 */       getContents().add(newConfig);
/*     */     } 
/*     */     
/*  76 */     String contentPrettyPrint = (new GsonBuilder()).setPrettyPrinting().create().toJson((JsonElement)config.save());
/*     */     try {
/*  78 */       FileWriter writer = new FileWriter(config.getFile());
/*  79 */       writer.write(contentPrettyPrint);
/*  80 */       writer.close();
/*  81 */       return true;
/*  82 */     } catch (IOException e) {
/*  83 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Config findConfig(String configName) {
/*  88 */     if (configName == null) return null; 
/*  89 */     for (Config config : getContents()) {
/*  90 */       if (config.getName().equalsIgnoreCase(configName)) {
/*  91 */         return config;
/*     */       }
/*     */     } 
/*  94 */     if ((new File(configDirectory, configName + ".json")).exists()) {
/*  95 */       return new Config(configName);
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public boolean deleteConfig(String configName) {
/* 101 */     if (configName == null)
/* 102 */       return false; 
/*     */     Config config;
/* 104 */     if ((config = findConfig(configName)) != null) {
/* 105 */       File f = config.getFile();
/* 106 */       getContents().remove(config);
/* 107 */       return (f.exists() && f.delete());
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\config\ConfigManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */