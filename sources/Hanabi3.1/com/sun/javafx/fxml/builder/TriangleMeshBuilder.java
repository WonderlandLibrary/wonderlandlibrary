package com.sun.javafx.fxml.builder;

import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import javafx.util.Builder;

public class TriangleMeshBuilder extends TreeMap implements Builder {
   private static final String VALUE_SEPARATOR_REGEX = "[,\\s]+";
   private float[] points;
   private float[] texCoords;
   private float[] normals;
   private int[] faces;
   private int[] faceSmoothingGroups;
   private VertexFormat vertexFormat;

   public TriangleMesh build() {
      TriangleMesh var1 = new TriangleMesh();
      if (this.points != null) {
         var1.getPoints().setAll(this.points);
      }

      if (this.texCoords != null) {
         var1.getTexCoords().setAll(this.texCoords);
      }

      if (this.faces != null) {
         var1.getFaces().setAll(this.faces);
      }

      if (this.faceSmoothingGroups != null) {
         var1.getFaceSmoothingGroups().setAll(this.faceSmoothingGroups);
      }

      if (this.normals != null) {
         var1.getNormals().setAll(this.normals);
      }

      if (this.vertexFormat != null) {
         var1.setVertexFormat(this.vertexFormat);
      }

      return var1;
   }

   public Object put(String var1, Object var2) {
      String[] var3;
      int var4;
      if ("points".equalsIgnoreCase(var1)) {
         var3 = ((String)var2).split("[,\\s]+");
         this.points = new float[var3.length];

         for(var4 = 0; var4 < var3.length; ++var4) {
            this.points[var4] = Float.parseFloat(var3[var4]);
         }
      } else if ("texcoords".equalsIgnoreCase(var1)) {
         var3 = ((String)var2).split("[,\\s]+");
         this.texCoords = new float[var3.length];

         for(var4 = 0; var4 < var3.length; ++var4) {
            this.texCoords[var4] = Float.parseFloat(var3[var4]);
         }
      } else if ("faces".equalsIgnoreCase(var1)) {
         var3 = ((String)var2).split("[,\\s]+");
         this.faces = new int[var3.length];

         for(var4 = 0; var4 < var3.length; ++var4) {
            this.faces[var4] = Integer.parseInt(var3[var4]);
         }
      } else if ("facesmoothinggroups".equalsIgnoreCase(var1)) {
         var3 = ((String)var2).split("[,\\s]+");
         this.faceSmoothingGroups = new int[var3.length];

         for(var4 = 0; var4 < var3.length; ++var4) {
            this.faceSmoothingGroups[var4] = Integer.parseInt(var3[var4]);
         }
      } else if ("normals".equalsIgnoreCase(var1)) {
         var3 = ((String)var2).split("[,\\s]+");
         this.normals = new float[var3.length];

         for(var4 = 0; var4 < var3.length; ++var4) {
            this.normals[var4] = Float.parseFloat(var3[var4]);
         }
      } else if ("vertexformat".equalsIgnoreCase(var1)) {
         if (var2 instanceof VertexFormat) {
            this.vertexFormat = (VertexFormat)var2;
         } else if ("point_texcoord".equalsIgnoreCase((String)var2)) {
            this.vertexFormat = VertexFormat.POINT_TEXCOORD;
         } else if ("point_normal_texcoord".equalsIgnoreCase((String)var2)) {
            this.vertexFormat = VertexFormat.POINT_NORMAL_TEXCOORD;
         }
      }

      return super.put(var1.toLowerCase(Locale.ROOT), var2);
   }

   public Set entrySet() {
      return super.entrySet();
   }
}
