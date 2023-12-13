/*    */ package org.yaml.snakeyaml.parser;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.yaml.snakeyaml.DumperOptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class VersionTagsTuple
/*    */ {
/*    */   private DumperOptions.Version version;
/*    */   private Map<String, String> tags;
/*    */   
/*    */   public VersionTagsTuple(DumperOptions.Version version, Map<String, String> tags) {
/* 30 */     this.version = version;
/* 31 */     this.tags = tags;
/*    */   }
/*    */   
/*    */   public DumperOptions.Version getVersion() {
/* 35 */     return this.version;
/*    */   }
/*    */   
/*    */   public Map<String, String> getTags() {
/* 39 */     return this.tags;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return String.format("VersionTagsTuple<%s, %s>", new Object[] { this.version, this.tags });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\parser\VersionTagsTuple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */