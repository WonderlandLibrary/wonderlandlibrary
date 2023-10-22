package com.sun.javafx.scene.traversal;

import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;

final class TabOrderHelper {
   private static Node findPreviousFocusableInList(List var0, int var1) {
      int var2 = var1;

      Node var3;
      while(true) {
         if (var2 < 0) {
            return null;
         }

         var3 = (Node)var0.get(var2);
         if (!isDisabledOrInvisible(var3)) {
            ParentTraversalEngine var4 = var3 instanceof Parent ? ((Parent)var3).getImpl_traversalEngine() : null;
            if (var3 instanceof Parent) {
               if (var4 != null && var4.canTraverse()) {
                  Node var7 = var4.selectLast();
                  if (var7 != null) {
                     return var7;
                  }
               } else {
                  ObservableList var5 = ((Parent)var3).getChildrenUnmodifiable();
                  if (var5.size() > 0) {
                     Node var6 = findPreviousFocusableInList(var5, var5.size() - 1);
                     if (var6 != null) {
                        return var6;
                     }
                  }
               }
            }

            if (var4 != null) {
               if (var4.isParentTraversable()) {
                  break;
               }
            } else if (var3.isFocusTraversable()) {
               break;
            }
         }

         --var2;
      }

      return var3;
   }

   private static boolean isDisabledOrInvisible(Node var0) {
      return var0.isDisabled() || !var0.impl_isTreeVisible();
   }

   public static Node findPreviousFocusablePeer(Node var0, Parent var1) {
      Object var2 = var0;
      Object var3 = null;
      List var4 = findPeers(var0);
      if (var4 == null) {
         ObservableList var10 = ((Parent)var0).getChildrenUnmodifiable();
         return findPreviousFocusableInList(var10, var10.size() - 1);
      } else {
         int var5 = var4.indexOf(var0);

         Parent var8;
         for(var3 = findPreviousFocusableInList(var4, var5 - 1); var3 == null && ((Node)var2).getParent() != var1; var2 = var8) {
            var8 = ((Node)var2).getParent();
            if (var8 != null) {
               label31: {
                  ParentTraversalEngine var9 = var8.getImpl_traversalEngine();
                  if (var9 != null) {
                     if (!var9.isParentTraversable()) {
                        break label31;
                     }
                  } else if (!var8.isFocusTraversable()) {
                     break label31;
                  }

                  var3 = var8;
                  continue;
               }

               List var6 = findPeers(var8);
               if (var6 != null) {
                  int var7 = var6.indexOf(var8);
                  var3 = findPreviousFocusableInList(var6, var7 - 1);
               }
            }
         }

         return (Node)var3;
      }
   }

   private static List findPeers(Node var0) {
      ObservableList var1 = null;
      Parent var2 = var0.getParent();
      if (var2 != null) {
         var1 = var2.getChildrenUnmodifiable();
      }

      return var1;
   }

   private static Node findNextFocusableInList(List var0, int var1) {
      int var2 = var1;

      Node var3;
      while(true) {
         if (var2 >= var0.size()) {
            return null;
         }

         var3 = (Node)var0.get(var2);
         if (!isDisabledOrInvisible(var3)) {
            ParentTraversalEngine var4 = var3 instanceof Parent ? ((Parent)var3).getImpl_traversalEngine() : null;
            if (var4 != null) {
               if (var4.isParentTraversable()) {
                  break;
               }
            } else if (var3.isFocusTraversable()) {
               break;
            }

            if (var3 instanceof Parent) {
               if (var4 != null && var4.canTraverse()) {
                  Node var7 = var4.selectFirst();
                  if (var7 != null) {
                     return var7;
                  }
               } else {
                  ObservableList var5 = ((Parent)var3).getChildrenUnmodifiable();
                  if (var5.size() > 0) {
                     Node var6 = findNextFocusableInList(var5, 0);
                     if (var6 != null) {
                        return var6;
                     }
                  }
               }
            }
         }

         ++var2;
      }

      return var3;
   }

   public static Node findNextFocusablePeer(Node var0, Parent var1, boolean var2) {
      Object var3 = var0;
      Node var4 = null;
      if (var2 && var0 instanceof Parent) {
         var4 = findNextFocusableInList(((Parent)var0).getChildrenUnmodifiable(), 0);
      }

      List var5;
      int var6;
      if (var4 == null) {
         var5 = findPeers(var0);
         if (var5 == null) {
            return null;
         }

         var6 = var5.indexOf(var0);
         var4 = findNextFocusableInList(var5, var6 + 1);
      }

      Parent var7;
      for(; var4 == null && ((Node)var3).getParent() != var1; var3 = var7) {
         var7 = ((Node)var3).getParent();
         if (var7 != null) {
            var5 = findPeers(var7);
            if (var5 != null) {
               var6 = var5.indexOf(var7);
               var4 = findNextFocusableInList(var5, var6 + 1);
            }
         }
      }

      return var4;
   }

   public static Node getFirstTargetNode(Parent var0) {
      if (var0 != null && !isDisabledOrInvisible(var0)) {
         ParentTraversalEngine var1 = var0.getImpl_traversalEngine();
         if (var1 != null && var1.canTraverse()) {
            Node var2 = var1.selectFirst();
            if (var2 != null) {
               return var2;
            }
         }

         ObservableList var7 = var0.getChildrenUnmodifiable();
         Iterator var3 = var7.iterator();

         Node var4;
         while(true) {
            do {
               if (!var3.hasNext()) {
                  return null;
               }

               var4 = (Node)var3.next();
            } while(isDisabledOrInvisible(var4));

            ParentTraversalEngine var5 = var4 instanceof Parent ? ((Parent)var4).getImpl_traversalEngine() : null;
            if (var5 != null) {
               if (var5.isParentTraversable()) {
                  break;
               }
            } else if (var4.isFocusTraversable()) {
               break;
            }

            if (var4 instanceof Parent) {
               Node var6 = getFirstTargetNode((Parent)var4);
               if (var6 != null) {
                  return var6;
               }
            }
         }

         return var4;
      } else {
         return null;
      }
   }

   public static Node getLastTargetNode(Parent var0) {
      if (var0 != null && !isDisabledOrInvisible(var0)) {
         ParentTraversalEngine var1 = var0.getImpl_traversalEngine();
         if (var1 != null && var1.canTraverse()) {
            Node var2 = var1.selectLast();
            if (var2 != null) {
               return var2;
            }
         }

         ObservableList var6 = var0.getChildrenUnmodifiable();
         int var3 = var6.size() - 1;

         Node var4;
         while(true) {
            if (var3 < 0) {
               return null;
            }

            var4 = (Node)var6.get(var3);
            if (!isDisabledOrInvisible(var4)) {
               if (var4 instanceof Parent) {
                  Node var5 = getLastTargetNode((Parent)var4);
                  if (var5 != null) {
                     return var5;
                  }
               }

               ParentTraversalEngine var7 = var4 instanceof Parent ? ((Parent)var4).getImpl_traversalEngine() : null;
               if (var7 != null) {
                  if (var7.isParentTraversable()) {
                     break;
                  }
               } else if (var4.isFocusTraversable()) {
                  break;
               }
            }

            --var3;
         }

         return var4;
      } else {
         return null;
      }
   }
}
