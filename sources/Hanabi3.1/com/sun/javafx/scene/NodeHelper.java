package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import javafx.scene.Node;
import javafx.scene.SubScene;

public class NodeHelper {
   private static NodeAccessor nodeAccessor;

   private NodeHelper() {
   }

   public static void layoutNodeForPrinting(Node var0) {
      nodeAccessor.layoutNodeForPrinting(var0);
   }

   public static boolean isDerivedDepthTest(Node var0) {
      return nodeAccessor.isDerivedDepthTest(var0);
   }

   public static SubScene getSubScene(Node var0) {
      return nodeAccessor.getSubScene(var0);
   }

   public static Accessible getAccessible(Node var0) {
      return nodeAccessor.getAccessible(var0);
   }

   public static void setNodeAccessor(NodeAccessor var0) {
      if (nodeAccessor != null) {
         throw new IllegalStateException();
      } else {
         nodeAccessor = var0;
      }
   }

   public static NodeAccessor getNodeAccessor() {
      if (nodeAccessor == null) {
         throw new IllegalStateException();
      } else {
         return nodeAccessor;
      }
   }

   private static void forceInit(Class var0) {
      try {
         Class.forName(var0.getName(), true, var0.getClassLoader());
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   static {
      forceInit(Node.class);
   }

   public interface NodeAccessor {
      void layoutNodeForPrinting(Node var1);

      boolean isDerivedDepthTest(Node var1);

      SubScene getSubScene(Node var1);

      void setLabeledBy(Node var1, Node var2);

      Accessible getAccessible(Node var1);
   }
}
