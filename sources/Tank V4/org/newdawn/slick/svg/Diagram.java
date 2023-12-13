package org.newdawn.slick.svg;

import java.util.ArrayList;
import java.util.HashMap;

public class Diagram {
   private ArrayList figures = new ArrayList();
   private HashMap patterns = new HashMap();
   private HashMap gradients = new HashMap();
   private HashMap figureMap = new HashMap();
   private float width;
   private float height;

   public Diagram(float var1, float var2) {
      this.width = var1;
      this.height = var2;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public void addPatternDef(String var1, String var2) {
      this.patterns.put(var1, var2);
   }

   public void addGradient(String var1, Gradient var2) {
      this.gradients.put(var1, var2);
   }

   public String getPatternDef(String var1) {
      return (String)this.patterns.get(var1);
   }

   public Gradient getGradient(String var1) {
      return (Gradient)this.gradients.get(var1);
   }

   public String[] getPatternDefNames() {
      return (String[])((String[])this.patterns.keySet().toArray(new String[0]));
   }

   public Figure getFigureByID(String var1) {
      return (Figure)this.figureMap.get(var1);
   }

   public void addFigure(Figure var1) {
      this.figures.add(var1);
      this.figureMap.put(var1.getData().getAttribute("id"), var1);
      String var2 = var1.getData().getAsReference("fill");
      Gradient var3 = this.getGradient(var2);
      if (var3 != null && var3.isRadial()) {
         for(int var4 = 0; var4 < InkscapeLoader.RADIAL_TRIANGULATION_LEVEL; ++var4) {
            var1.getShape().increaseTriangulation();
         }
      }

   }

   public int getFigureCount() {
      return this.figures.size();
   }

   public Figure getFigure(int var1) {
      return (Figure)this.figures.get(var1);
   }

   public void removeFigure(Figure var1) {
      this.figures.remove(var1);
      this.figureMap.remove(var1.getData().getAttribute("id"));
   }
}
