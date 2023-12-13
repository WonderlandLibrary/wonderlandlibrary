package org.newdawn.slick.tests;

import java.util.ArrayList;
import java.util.HashSet;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.GeomUtil;
import org.newdawn.slick.geom.GeomUtilListener;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class GeomUtilTileTest extends BasicGame implements GeomUtilListener {
   private Shape source;
   private Shape cut;
   private Shape[] result;
   private GeomUtil util = new GeomUtil();
   private ArrayList original = new ArrayList();
   private ArrayList combined = new ArrayList();
   private ArrayList intersections = new ArrayList();
   private ArrayList used = new ArrayList();
   private ArrayList[][] quadSpace;
   private Shape[][] quadSpaceShapes;

   public GeomUtilTileTest() {
      super("GeomUtilTileTest");
   }

   private void generateSpace(ArrayList var1, float var2, float var3, float var4, float var5, int var6) {
      this.quadSpace = new ArrayList[var6][var6];
      this.quadSpaceShapes = new Shape[var6][var6];
      float var7 = (var4 - var2) / (float)var6;
      float var8 = (var5 - var3) / (float)var6;

      for(int var9 = 0; var9 < var6; ++var9) {
         for(int var10 = 0; var10 < var6; ++var10) {
            this.quadSpace[var9][var10] = new ArrayList();
            Polygon var11 = new Polygon();
            var11.addPoint(var2 + var7 * (float)var9, var3 + var8 * (float)var10);
            var11.addPoint(var2 + var7 * (float)var9 + var7, var3 + var8 * (float)var10);
            var11.addPoint(var2 + var7 * (float)var9 + var7, var3 + var8 * (float)var10 + var8);
            var11.addPoint(var2 + var7 * (float)var9, var3 + var8 * (float)var10 + var8);

            for(int var12 = 0; var12 < var1.size(); ++var12) {
               Shape var13 = (Shape)var1.get(var12);
               if (var11 != false) {
                  this.quadSpace[var9][var10].add(var13);
               }
            }

            this.quadSpaceShapes[var9][var10] = var11;
         }
      }

   }

   private void removeFromQuadSpace(Shape var1) {
      int var2 = this.quadSpace.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         for(int var4 = 0; var4 < var2; ++var4) {
            this.quadSpace[var3][var4].remove(var1);
         }
      }

   }

   private void addToQuadSpace(Shape var1) {
      int var2 = this.quadSpace.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         for(int var4 = 0; var4 < var2; ++var4) {
            if (this.quadSpaceShapes[var3][var4] != false) {
               this.quadSpace[var3][var4].add(var1);
            }
         }
      }

   }

   public void init() {
      byte var1 = 10;
      int[][] var2 = new int[][]{{0, 0, 0, 0, 0, 0, 0, 3, 0, 0}, {0, 1, 1, 1, 0, 0, 1, 1, 1, 0}, {0, 1, 1, 0, 0, 0, 5, 1, 6, 0}, {0, 1, 2, 0, 0, 0, 4, 1, 1, 0}, {0, 1, 1, 0, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 3, 0, 1, 1, 0, 0}, {0, 0, 0, 1, 1, 0, 0, 0, 1, 0}, {0, 0, 0, 1, 1, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

      for(int var3 = 0; var3 < var2[0].length; ++var3) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4][var3] != 0) {
               switch(var2[var4][var3]) {
               case 1:
                  Polygon var5 = new Polygon();
                  var5.addPoint((float)(var3 * 32), (float)(var4 * 32));
                  var5.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32));
                  var5.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32 + 32));
                  var5.addPoint((float)(var3 * 32), (float)(var4 * 32 + 32));
                  this.original.add(var5);
                  break;
               case 2:
                  Polygon var6 = new Polygon();
                  var6.addPoint((float)(var3 * 32), (float)(var4 * 32));
                  var6.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32));
                  var6.addPoint((float)(var3 * 32), (float)(var4 * 32 + 32));
                  this.original.add(var6);
                  break;
               case 3:
                  Circle var7 = new Circle((float)(var3 * 32 + 16), (float)(var4 * 32 + 32), 16.0F, 16);
                  this.original.add(var7);
                  break;
               case 4:
                  Polygon var8 = new Polygon();
                  var8.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32));
                  var8.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32 + 32));
                  var8.addPoint((float)(var3 * 32), (float)(var4 * 32 + 32));
                  this.original.add(var8);
                  break;
               case 5:
                  Polygon var9 = new Polygon();
                  var9.addPoint((float)(var3 * 32), (float)(var4 * 32));
                  var9.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32));
                  var9.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32 + 32));
                  this.original.add(var9);
                  break;
               case 6:
                  Polygon var10 = new Polygon();
                  var10.addPoint((float)(var3 * 32), (float)(var4 * 32));
                  var10.addPoint((float)(var3 * 32 + 32), (float)(var4 * 32));
                  var10.addPoint((float)(var3 * 32), (float)(var4 * 32 + 32));
                  this.original.add(var10);
               }
            }
         }
      }

      long var11 = System.currentTimeMillis();
      this.generateSpace(this.original, 0.0F, 0.0F, (float)((var1 + 1) * 32), (float)((var1 + 1) * 32), 8);
      this.combined = this.combineQuadSpace();
      long var12 = System.currentTimeMillis();
      System.out.println("Combine took: " + (var12 - var11));
      System.out.println("Combine result: " + this.combined.size());
   }

   private ArrayList combineQuadSpace() {
      boolean var1 = true;

      int var3;
      while(var1) {
         var1 = false;

         for(int var2 = 0; var2 < this.quadSpace.length; ++var2) {
            for(var3 = 0; var3 < this.quadSpace.length; ++var3) {
               ArrayList var4 = this.quadSpace[var2][var3];
               int var5 = var4.size();
               this.combine(var4);
               int var6 = var4.size();
               var1 |= var5 != var6;
            }
         }
      }

      HashSet var7 = new HashSet();

      for(var3 = 0; var3 < this.quadSpace.length; ++var3) {
         for(int var8 = 0; var8 < this.quadSpace.length; ++var8) {
            var7.addAll(this.quadSpace[var3][var8]);
         }
      }

      return new ArrayList(var7);
   }

   private ArrayList combine(ArrayList var1) {
      ArrayList var2 = var1;
      ArrayList var3 = var1;

      for(boolean var4 = true; var3.size() != var2.size() || var4; var3 = this.combineImpl(var3)) {
         var4 = false;
         var2 = var3;
      }

      ArrayList var5 = new ArrayList();

      for(int var6 = 0; var6 < var3.size(); ++var6) {
         var5.add(((Shape)var3.get(var6)).prune());
      }

      return var5;
   }

   private ArrayList combineImpl(ArrayList var1) {
      ArrayList var2 = new ArrayList(var1);
      if (this.quadSpace != null) {
         var2 = var1;
      }

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         Shape var4 = (Shape)var1.get(var3);

         for(int var5 = var3 + 1; var5 < var1.size(); ++var5) {
            Shape var6 = (Shape)var1.get(var5);
            if (var4.intersects(var6)) {
               Shape[] var7 = this.util.union(var4, var6);
               if (var7.length == 1) {
                  if (this.quadSpace != null) {
                     this.removeFromQuadSpace(var4);
                     this.removeFromQuadSpace(var6);
                     this.addToQuadSpace(var7[0]);
                  } else {
                     var2.remove(var4);
                     var2.remove(var6);
                     var2.add(var7[0]);
                  }

                  return var2;
               }
            }
         }
      }

      return var2;
   }

   public void init(GameContainer var1) throws SlickException {
      this.util.setListener(this);
      this.init();
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(Color.green);

      int var3;
      Shape var4;
      for(var3 = 0; var3 < this.original.size(); ++var3) {
         var4 = (Shape)this.original.get(var3);
         var2.draw(var4);
      }

      var2.setColor(Color.white);
      if (this.quadSpaceShapes != null) {
         var2.draw(this.quadSpaceShapes[0][0]);
      }

      var2.translate(0.0F, 320.0F);

      for(var3 = 0; var3 < this.combined.size(); ++var3) {
         var2.setColor(Color.white);
         var4 = (Shape)this.combined.get(var3);
         var2.draw(var4);

         for(int var5 = 0; var5 < var4.getPointCount(); ++var5) {
            var2.setColor(Color.yellow);
            float[] var6 = var4.getPoint(var5);
            var2.fillOval(var6[0] - 1.0F, var6[1] - 1.0F, 3.0F, 3.0F);
         }
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new GeomUtilTileTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void pointExcluded(float var1, float var2) {
   }

   public void pointIntersected(float var1, float var2) {
      this.intersections.add(new Vector2f(var1, var2));
   }

   public void pointUsed(float var1, float var2) {
      this.used.add(new Vector2f(var1, var2));
   }
}
