/*    */ package org.neverhook.client.ui.newclickgui;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class SorterHelper
/*    */   implements Comparator<FeaturePanel>
/*    */ {
/*    */   public int compare(FeaturePanel featurePanel, FeaturePanel featurePanel2) {
/*  9 */     if (featurePanel != null && featurePanel2 != null) {
/* 10 */       return featurePanel.feature.getLabel().compareTo(featurePanel2.feature.getLabel());
/*    */     }
/* 12 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\SorterHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */