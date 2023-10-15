/*    */ package org.yaml.snakeyaml.events;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.yaml.snakeyaml.DumperOptions;
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DocumentStartEvent
/*    */   extends Event
/*    */ {
/*    */   private final boolean explicit;
/*    */   private final DumperOptions.Version version;
/*    */   private final Map<String, String> tags;
/*    */   
/*    */   public DocumentStartEvent(Mark startMark, Mark endMark, boolean explicit, DumperOptions.Version version, Map<String, String> tags) {
/* 36 */     super(startMark, endMark);
/* 37 */     this.explicit = explicit;
/* 38 */     this.version = version;
/* 39 */     this.tags = tags;
/*    */   }
/*    */   
/*    */   public boolean getExplicit() {
/* 43 */     return this.explicit;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DumperOptions.Version getVersion() {
/* 55 */     return this.version;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, String> getTags() {
/* 65 */     return this.tags;
/*    */   }
/*    */ 
/*    */   
/*    */   public Event.ID getEventId() {
/* 70 */     return Event.ID.DocumentStart;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\events\DocumentStartEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */