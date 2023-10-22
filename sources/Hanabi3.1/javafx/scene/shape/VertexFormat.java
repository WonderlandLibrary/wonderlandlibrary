package javafx.scene.shape;

public final class VertexFormat {
   public static final VertexFormat POINT_TEXCOORD = new VertexFormat("POINT_TEXCOORD", 2, 0, -1, 1);
   public static final VertexFormat POINT_NORMAL_TEXCOORD = new VertexFormat("POINT_NORMAL_TEXCOORD", 3, 0, 1, 2);
   private static final int POINT_ELEMENT_SIZE = 3;
   private static final int NORMAL_ELEMENT_SIZE = 3;
   private static final int TEXCOORD_ELEMENT_SIZE = 2;
   private final String name;
   private final int vertexIndexSize;
   private final int pointIndexOffset;
   private final int normalIndexOffset;
   private final int texCoordIndexOffset;

   private VertexFormat(String var1, int var2, int var3, int var4, int var5) {
      this.name = var1;
      this.vertexIndexSize = var2;
      this.pointIndexOffset = var3;
      this.normalIndexOffset = var4;
      this.texCoordIndexOffset = var5;
   }

   int getPointElementSize() {
      return 3;
   }

   int getNormalElementSize() {
      return 3;
   }

   int getTexCoordElementSize() {
      return 2;
   }

   public int getVertexIndexSize() {
      return this.vertexIndexSize;
   }

   public int getPointIndexOffset() {
      return this.pointIndexOffset;
   }

   public int getNormalIndexOffset() {
      return this.normalIndexOffset;
   }

   public int getTexCoordIndexOffset() {
      return this.texCoordIndexOffset;
   }

   public String toString() {
      return this.name;
   }
}
