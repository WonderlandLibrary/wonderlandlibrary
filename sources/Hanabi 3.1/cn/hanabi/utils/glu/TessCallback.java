package cn.hanabi.utils.glu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

public class TessCallback extends GLUtessellatorCallbackAdapter {
   public static final TessCallback INSTANCE = new TessCallback();

   public void begin(int type) {
      GL11.glBegin(type);
   }

   public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) {
      double[] combined = new double[]{coords[0], coords[1], coords[2], 1.0, 1.0, 1.0};

      for(int i = 0; i < outData.length; ++i) {
         outData[i] = new VertexData(combined);
      }

   }

   public void end() {
      GL11.glEnd();
   }

   public void vertex(Object vertexData) {
      VertexData vertex = (VertexData)vertexData;
      GL11.glVertex3d(vertex.data[0], vertex.data[1], vertex.data[2]);
   }
}
