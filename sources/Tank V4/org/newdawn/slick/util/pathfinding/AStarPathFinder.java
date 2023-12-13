package org.newdawn.slick.util.pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.util.pathfinding.heuristics.ClosestHeuristic;

public class AStarPathFinder implements PathFinder, PathFindingContext {
   private ArrayList closed;
   private AStarPathFinder.PriorityList open;
   private TileBasedMap map;
   private int maxSearchDistance;
   private AStarPathFinder.Node[][] nodes;
   private boolean allowDiagMovement;
   private AStarHeuristic heuristic;
   private AStarPathFinder.Node current;
   private Mover mover;
   private int sourceX;
   private int sourceY;
   private int distance;

   public AStarPathFinder(TileBasedMap var1, int var2, boolean var3) {
      this(var1, var2, var3, new ClosestHeuristic());
   }

   public AStarPathFinder(TileBasedMap var1, int var2, boolean var3, AStarHeuristic var4) {
      this.closed = new ArrayList();
      this.open = new AStarPathFinder.PriorityList(this);
      this.heuristic = var4;
      this.map = var1;
      this.maxSearchDistance = var2;
      this.allowDiagMovement = var3;
      this.nodes = new AStarPathFinder.Node[var1.getWidthInTiles()][var1.getHeightInTiles()];

      for(int var5 = 0; var5 < var1.getWidthInTiles(); ++var5) {
         for(int var6 = 0; var6 < var1.getHeightInTiles(); ++var6) {
            this.nodes[var5][var6] = new AStarPathFinder.Node(this, var5, var6);
         }
      }

   }

   public Path findPath(Mover var1, int var2, int var3, int var4, int var5) {
      this.current = null;
      this.mover = var1;
      this.sourceX = var4;
      this.sourceY = var5;
      this.distance = 0;
      if (this.map.blocked(this, var4, var5)) {
         return null;
      } else {
         int var6;
         int var7;
         for(var6 = 0; var6 < this.map.getWidthInTiles(); ++var6) {
            for(var7 = 0; var7 < this.map.getHeightInTiles(); ++var7) {
               this.nodes[var6][var7].reset();
            }
         }

         AStarPathFinder.Node.access$102(this.nodes[var2][var3], 0.0F);
         AStarPathFinder.Node.access$202(this.nodes[var2][var3], 0);
         this.closed.clear();
         this.open.clear();
         this.addToOpen(this.nodes[var2][var3]);
         AStarPathFinder.Node.access$302(this.nodes[var4][var5], (AStarPathFinder.Node)null);
         var6 = 0;

         while(var6 < this.maxSearchDistance && this.open.size() != 0) {
            if (this.current != null) {
               var7 = AStarPathFinder.Node.access$400(this.current);
               int var8 = AStarPathFinder.Node.access$500(this.current);
            }

            this.current = this.getFirstInOpen();
            this.distance = AStarPathFinder.Node.access$200(this.current);
            if (this.current == this.nodes[var4][var5] && var5 >= 0) {
               break;
            }

            this.removeFromOpen(this.current);
            this.addToClosed(this.current);

            for(int var9 = -1; var9 < 2; ++var9) {
               for(int var10 = -1; var10 < 2; ++var10) {
                  if ((var9 != 0 || var10 != 0) && (this.allowDiagMovement || var9 == 0 || var10 == 0)) {
                     int var11 = var9 + AStarPathFinder.Node.access$400(this.current);
                     int var12 = var10 + AStarPathFinder.Node.access$500(this.current);
                     AStarPathFinder.Node.access$400(this.current);
                     AStarPathFinder.Node.access$500(this.current);
                     if (var12 >= 0) {
                        float var13 = AStarPathFinder.Node.access$100(this.current) + this.getMovementCost(var1, AStarPathFinder.Node.access$400(this.current), AStarPathFinder.Node.access$500(this.current), var11, var12);
                        AStarPathFinder.Node var14 = this.nodes[var11][var12];
                        this.map.pathFinderVisited(var11, var12);
                        if (var13 < AStarPathFinder.Node.access$100(var14)) {
                           if (this.inOpenList(var14)) {
                              this.removeFromOpen(var14);
                           }

                           if (this.inClosedList(var14)) {
                              this.removeFromClosed(var14);
                           }
                        }

                        if (!this.inOpenList(var14) && !this.inClosedList(var14)) {
                           AStarPathFinder.Node.access$102(var14, var13);
                           AStarPathFinder.Node.access$602(var14, this.getHeuristicCost(var1, var11, var12, var4, var5));
                           var6 = Math.max(var6, var14.setParent(this.current));
                           this.addToOpen(var14);
                        }
                     }
                  }
               }
            }
         }

         if (AStarPathFinder.Node.access$300(this.nodes[var4][var5]) == null) {
            return null;
         } else {
            Path var15 = new Path();

            for(AStarPathFinder.Node var16 = this.nodes[var4][var5]; var16 != this.nodes[var2][var3]; var16 = AStarPathFinder.Node.access$300(var16)) {
               var15.prependStep(AStarPathFinder.Node.access$400(var16), AStarPathFinder.Node.access$500(var16));
            }

            var15.prependStep(var2, var3);
            return var15;
         }
      }
   }

   public int getCurrentX() {
      return this.current == null ? -1 : AStarPathFinder.Node.access$400(this.current);
   }

   public int getCurrentY() {
      return this.current == null ? -1 : AStarPathFinder.Node.access$500(this.current);
   }

   protected AStarPathFinder.Node getFirstInOpen() {
      return (AStarPathFinder.Node)this.open.first();
   }

   protected void addToOpen(AStarPathFinder.Node var1) {
      var1.setOpen(true);
      this.open.add(var1);
   }

   protected boolean inOpenList(AStarPathFinder.Node var1) {
      return var1.isOpen();
   }

   protected void removeFromOpen(AStarPathFinder.Node var1) {
      var1.setOpen(false);
      this.open.remove(var1);
   }

   protected void addToClosed(AStarPathFinder.Node var1) {
      var1.setClosed(true);
      this.closed.add(var1);
   }

   protected boolean inClosedList(AStarPathFinder.Node var1) {
      return var1.isClosed();
   }

   protected void removeFromClosed(AStarPathFinder.Node var1) {
      var1.setClosed(false);
      this.closed.remove(var1);
   }

   public float getMovementCost(Mover var1, int var2, int var3, int var4, int var5) {
      this.mover = var1;
      this.sourceX = var2;
      this.sourceY = var3;
      return this.map.getCost(this, var4, var5);
   }

   public float getHeuristicCost(Mover var1, int var2, int var3, int var4, int var5) {
      return this.heuristic.getCost(this.map, var1, var2, var3, var4, var5);
   }

   public Mover getMover() {
      return this.mover;
   }

   public int getSearchDistance() {
      return this.distance;
   }

   public int getSourceX() {
      return this.sourceX;
   }

   public int getSourceY() {
      return this.sourceY;
   }

   private class Node implements Comparable {
      private int x;
      private int y;
      private float cost;
      private AStarPathFinder.Node parent;
      private float heuristic;
      private int depth;
      private boolean open;
      private boolean closed;
      private final AStarPathFinder this$0;

      public Node(AStarPathFinder var1, int var2, int var3) {
         this.this$0 = var1;
         this.x = var2;
         this.y = var3;
      }

      public int setParent(AStarPathFinder.Node var1) {
         this.depth = var1.depth + 1;
         this.parent = var1;
         return this.depth;
      }

      public int compareTo(Object var1) {
         AStarPathFinder.Node var2 = (AStarPathFinder.Node)var1;
         float var3 = this.heuristic + this.cost;
         float var4 = var2.heuristic + var2.cost;
         if (var3 < var4) {
            return -1;
         } else {
            return var3 > var4 ? 1 : 0;
         }
      }

      public void setOpen(boolean var1) {
         this.open = var1;
      }

      public boolean isOpen() {
         return this.open;
      }

      public void setClosed(boolean var1) {
         this.closed = var1;
      }

      public boolean isClosed() {
         return this.closed;
      }

      public void reset() {
         this.closed = false;
         this.open = false;
         this.cost = 0.0F;
         this.depth = 0;
      }

      public String toString() {
         return "[Node " + this.x + "," + this.y + "]";
      }

      static float access$102(AStarPathFinder.Node var0, float var1) {
         return var0.cost = var1;
      }

      static int access$202(AStarPathFinder.Node var0, int var1) {
         return var0.depth = var1;
      }

      static AStarPathFinder.Node access$302(AStarPathFinder.Node var0, AStarPathFinder.Node var1) {
         return var0.parent = var1;
      }

      static int access$400(AStarPathFinder.Node var0) {
         return var0.x;
      }

      static int access$500(AStarPathFinder.Node var0) {
         return var0.y;
      }

      static int access$200(AStarPathFinder.Node var0) {
         return var0.depth;
      }

      static float access$100(AStarPathFinder.Node var0) {
         return var0.cost;
      }

      static float access$602(AStarPathFinder.Node var0, float var1) {
         return var0.heuristic = var1;
      }

      static AStarPathFinder.Node access$300(AStarPathFinder.Node var0) {
         return var0.parent;
      }
   }

   private class PriorityList {
      private List list;
      private final AStarPathFinder this$0;

      private PriorityList(AStarPathFinder var1) {
         this.this$0 = var1;
         this.list = new LinkedList();
      }

      public Object first() {
         return this.list.get(0);
      }

      public void clear() {
         this.list.clear();
      }

      public void add(Object var1) {
         for(int var2 = 0; var2 < this.list.size(); ++var2) {
            if (((Comparable)this.list.get(var2)).compareTo(var1) > 0) {
               this.list.add(var2, var1);
               break;
            }
         }

         if (!this.list.contains(var1)) {
            this.list.add(var1);
         }

      }

      public void remove(Object var1) {
         this.list.remove(var1);
      }

      public int size() {
         return this.list.size();
      }

      public boolean contains(Object var1) {
         return this.list.contains(var1);
      }

      public String toString() {
         String var1 = "{";

         for(int var2 = 0; var2 < this.size(); ++var2) {
            var1 = var1 + this.list.get(var2).toString() + ",";
         }

         var1 = var1 + "}";
         return var1;
      }

      PriorityList(AStarPathFinder var1, Object var2) {
         this(var1);
      }
   }
}
