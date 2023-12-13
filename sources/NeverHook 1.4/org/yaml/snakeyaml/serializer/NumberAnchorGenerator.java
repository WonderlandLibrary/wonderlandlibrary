/*    */ package org.yaml.snakeyaml.serializer;
/*    */ 
/*    */ import java.text.NumberFormat;
/*    */ import org.yaml.snakeyaml.nodes.Node;
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
/*    */ public class NumberAnchorGenerator
/*    */   implements AnchorGenerator
/*    */ {
/* 24 */   private int lastAnchorId = 0;
/*    */   
/*    */   public NumberAnchorGenerator(int lastAnchorId) {
/* 27 */     this.lastAnchorId = lastAnchorId;
/*    */   }
/*    */   
/*    */   public String nextAnchor(Node node) {
/* 31 */     this.lastAnchorId++;
/* 32 */     NumberFormat format = NumberFormat.getNumberInstance();
/* 33 */     format.setMinimumIntegerDigits(3);
/* 34 */     format.setMaximumFractionDigits(0);
/* 35 */     format.setGroupingUsed(false);
/* 36 */     String anchorId = format.format(this.lastAnchorId);
/* 37 */     return "id" + anchorId;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\serializer\NumberAnchorGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */