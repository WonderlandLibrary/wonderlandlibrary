package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Cylinder extends Quadric {
  public void draw(float baseRadius, float topRadius, float height, int slices, int stacks) {
    float nsign;
    if (this.orientation == 100021) {
      nsign = -1.0F;
    } else {
      nsign = 1.0F;
    } 
    float da = 6.2831855F / slices;
    float dr = (topRadius - baseRadius) / stacks;
    float dz = height / stacks;
    float nz = (baseRadius - topRadius) / height;
    if (this.drawStyle == 100010) {
      GL11.glBegin(0);
      for (int i = 0; i < slices; i++) {
        float x = cos(i * da);
        float y = sin(i * da);
        normal3f(x * nsign, y * nsign, nz * nsign);
        float z = 0.0F;
        float r = baseRadius;
        for (int j = 0; j <= stacks; j++) {
          GL11.glVertex3f(x * r, y * r, z);
          z += dz;
          r += dr;
        } 
      } 
      GL11.glEnd();
    } else if (this.drawStyle == 100011 || this.drawStyle == 100013) {
      if (this.drawStyle == 100011) {
        float z = 0.0F;
        float r = baseRadius;
        for (int j = 0; j <= stacks; j++) {
          GL11.glBegin(2);
          for (int k = 0; k < slices; k++) {
            float x = cos(k * da);
            float y = sin(k * da);
            normal3f(x * nsign, y * nsign, nz * nsign);
            GL11.glVertex3f(x * r, y * r, z);
          } 
          GL11.glEnd();
          z += dz;
          r += dr;
        } 
      } else if (baseRadius != 0.0D) {
        GL11.glBegin(2);
        int j;
        for (j = 0; j < slices; j++) {
          float x = cos(j * da);
          float y = sin(j * da);
          normal3f(x * nsign, y * nsign, nz * nsign);
          GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0F);
        } 
        GL11.glEnd();
        GL11.glBegin(2);
        for (j = 0; j < slices; j++) {
          float x = cos(j * da);
          float y = sin(j * da);
          normal3f(x * nsign, y * nsign, nz * nsign);
          GL11.glVertex3f(x * topRadius, y * topRadius, height);
        } 
        GL11.glEnd();
      } 
      GL11.glBegin(1);
      for (int i = 0; i < slices; i++) {
        float x = cos(i * da);
        float y = sin(i * da);
        normal3f(x * nsign, y * nsign, nz * nsign);
        GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0F);
        GL11.glVertex3f(x * topRadius, y * topRadius, height);
      } 
      GL11.glEnd();
    } else if (this.drawStyle == 100012) {
      float ds = 1.0F / slices;
      float dt = 1.0F / stacks;
      float t = 0.0F;
      float z = 0.0F;
      float r = baseRadius;
      for (int j = 0; j < stacks; j++) {
        float s = 0.0F;
        GL11.glBegin(8);
        for (int i = 0; i <= slices; i++) {
          float x, y;
          if (i == slices) {
            x = sin(0.0F);
            y = cos(0.0F);
          } else {
            x = sin(i * da);
            y = cos(i * da);
          } 
          if (nsign == 1.0F) {
            normal3f(x * nsign, y * nsign, nz * nsign);
            TXTR_COORD(s, t);
            GL11.glVertex3f(x * r, y * r, z);
            normal3f(x * nsign, y * nsign, nz * nsign);
            TXTR_COORD(s, t + dt);
            GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
          } else {
            normal3f(x * nsign, y * nsign, nz * nsign);
            TXTR_COORD(s, t);
            GL11.glVertex3f(x * r, y * r, z);
            normal3f(x * nsign, y * nsign, nz * nsign);
            TXTR_COORD(s, t + dt);
            GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
          } 
          s += ds;
        } 
        GL11.glEnd();
        r += dr;
        t += dt;
        z += dz;
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\glu\Cylinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */