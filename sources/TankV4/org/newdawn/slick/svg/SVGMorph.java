package org.newdawn.slick.svg;

import java.util.ArrayList;
import org.newdawn.slick.geom.MorphShape;

public class SVGMorph extends Diagram {
   private ArrayList figures = new ArrayList();

   public SVGMorph(Diagram var1) {
      super(var1.getWidth(), var1.getHeight());

      for(int var2 = 0; var2 < var1.getFigureCount(); ++var2) {
         Figure var3 = var1.getFigure(var2);
         Figure var4 = new Figure(var3.getType(), new MorphShape(var3.getShape()), var3.getData(), var3.getTransform());
         this.figures.add(var4);
      }

   }

   public void addStep(Diagram var1) {
      if (var1.getFigureCount() != this.figures.size()) {
         throw new RuntimeException("Mismatched diagrams, missing ids");
      } else {
         for(int var2 = 0; var2 < var1.getFigureCount(); ++var2) {
            Figure var3 = var1.getFigure(var2);
            String var4 = var3.getData().getMetaData();

            for(int var5 = 0; var5 < this.figures.size(); ++var5) {
               Figure var6 = (Figure)this.figures.get(var5);
               if (var6.getData().getMetaData().equals(var4)) {
                  MorphShape var7 = (MorphShape)var6.getShape();
                  var7.addShape(var3.getShape());
                  break;
               }
            }
         }

      }
   }

   public void setExternalDiagram(Diagram var1) {
      for(int var2 = 0; var2 < this.figures.size(); ++var2) {
         Figure var3 = (Figure)this.figures.get(var2);

         for(int var4 = 0; var4 < var1.getFigureCount(); ++var4) {
            Figure var5 = var1.getFigure(var4);
            if (var5.getData().getMetaData().equals(var3.getData().getMetaData())) {
               MorphShape var6 = (MorphShape)var3.getShape();
               var6.setExternalFrame(var5.getShape());
               break;
            }
         }
      }

   }

   public void updateMorphTime(float var1) {
      for(int var2 = 0; var2 < this.figures.size(); ++var2) {
         Figure var3 = (Figure)this.figures.get(var2);
         MorphShape var4 = (MorphShape)var3.getShape();
         var4.updateMorphTime(var1);
      }

   }

   public void setMorphTime(float var1) {
      for(int var2 = 0; var2 < this.figures.size(); ++var2) {
         Figure var3 = (Figure)this.figures.get(var2);
         MorphShape var4 = (MorphShape)var3.getShape();
         var4.setMorphTime(var1);
      }

   }

   public int getFigureCount() {
      return this.figures.size();
   }

   public Figure getFigure(int var1) {
      return (Figure)this.figures.get(var1);
   }
}
