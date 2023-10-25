package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Disk extends Quadric {
  public void draw(float innerRadius, float outerRadius, int slices, int loops) {
    float dtc;
    int l, s;
    float a;
    int i;
    float r1;
    int j;
    if (this.normals != 100002)
      if (this.orientation == 100020) {
        GL11.glNormal3f(0.0F, 0.0F, 1.0F);
      } else {
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
      }  
    float da = 6.2831855F / slices;
    float dr = (outerRadius - innerRadius) / loops;
    switch (this.drawStyle) {
      case 100012:
        dtc = 2.0F * outerRadius;
        r1 = innerRadius;
        for (j = 0; j < loops; j++) {
          float r2 = r1 + dr;
          if (this.orientation == 100020) {
            GL11.glBegin(8);
            for (int k = 0; k <= slices; k++) {
              float f1;
              if (k == slices) {
                f1 = 0.0F;
              } else {
                f1 = k * da;
              } 
              float sa = sin(f1);
              float ca = cos(f1);
              TXTR_COORD(0.5F + sa * r2 / dtc, 0.5F + ca * r2 / dtc);
              GL11.glVertex2f(r2 * sa, r2 * ca);
              TXTR_COORD(0.5F + sa * r1 / dtc, 0.5F + ca * r1 / dtc);
              GL11.glVertex2f(r1 * sa, r1 * ca);
            } 
            GL11.glEnd();
          } else {
            GL11.glBegin(8);
            for (int k = slices; k >= 0; k--) {
              float f1;
              if (k == slices) {
                f1 = 0.0F;
              } else {
                f1 = k * da;
              } 
              float sa = sin(f1);
              float ca = cos(f1);
              TXTR_COORD(0.5F - sa * r2 / dtc, 0.5F + ca * r2 / dtc);
              GL11.glVertex2f(r2 * sa, r2 * ca);
              TXTR_COORD(0.5F - sa * r1 / dtc, 0.5F + ca * r1 / dtc);
              GL11.glVertex2f(r1 * sa, r1 * ca);
            } 
            GL11.glEnd();
          } 
          r1 = r2;
        } 
        return;
      case 100011:
        for (l = 0; l <= loops; l++) {
          float r = innerRadius + l * dr;
          GL11.glBegin(2);
          for (int k = 0; k < slices; k++) {
            float f = k * da;
            GL11.glVertex2f(r * sin(f), r * cos(f));
          } 
          GL11.glEnd();
        } 
        for (i = 0; i < slices; i++) {
          float f1 = i * da;
          float x = sin(f1);
          float y = cos(f1);
          GL11.glBegin(3);
          for (l = 0; l <= loops; l++) {
            float r = innerRadius + l * dr;
            GL11.glVertex2f(r * x, r * y);
          } 
          GL11.glEnd();
        } 
        return;
      case 100010:
        GL11.glBegin(0);
        for (s = 0; s < slices; s++) {
          float f1 = s * da;
          float x = sin(f1);
          float y = cos(f1);
          for (j = 0; j <= loops; j++) {
            float r = innerRadius * j * dr;
            GL11.glVertex2f(r * x, r * y);
          } 
        } 
        GL11.glEnd();
        return;
      case 100013:
        if (innerRadius != 0.0D) {
          GL11.glBegin(2);
          float f;
          for (f = 0.0F; f < 6.2831854820251465D; f += da) {
            float x = innerRadius * sin(f);
            float y = innerRadius * cos(f);
            GL11.glVertex2f(x, y);
          } 
          GL11.glEnd();
        } 
        GL11.glBegin(2);
        for (a = 0.0F; a < 6.2831855F; a += da) {
          float x = outerRadius * sin(a);
          float y = outerRadius * cos(a);
          GL11.glVertex2f(x, y);
        } 
        GL11.glEnd();
        return;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\glu\Disk.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */