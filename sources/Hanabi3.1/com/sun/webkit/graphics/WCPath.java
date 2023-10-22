package com.sun.webkit.graphics;

public abstract class WCPath extends Ref {
   public static final int RULE_NONZERO = 0;
   public static final int RULE_EVENODD = 1;

   public abstract void addRect(double var1, double var3, double var5, double var7);

   public abstract void addEllipse(double var1, double var3, double var5, double var7);

   public abstract void addArcTo(double var1, double var3, double var5, double var7, double var9);

   public abstract void addArc(double var1, double var3, double var5, double var7, double var9, boolean var11);

   public abstract boolean contains(int var1, double var2, double var4);

   public abstract WCRectangle getBounds();

   public abstract void clear();

   public abstract void moveTo(double var1, double var3);

   public abstract void addLineTo(double var1, double var3);

   public abstract void addQuadCurveTo(double var1, double var3, double var5, double var7);

   public abstract void addBezierCurveTo(double var1, double var3, double var5, double var7, double var9, double var11);

   public abstract void addPath(WCPath var1);

   public abstract void closeSubpath();

   public abstract boolean isEmpty();

   public abstract void translate(double var1, double var3);

   public abstract void transform(double var1, double var3, double var5, double var7, double var9, double var11);

   public abstract int getWindingRule();

   public abstract void setWindingRule(int var1);

   public abstract Object getPlatformPath();

   public abstract WCPathIterator getPathIterator();

   public abstract boolean strokeContains(double var1, double var3, double var5, double var7, int var9, int var10, double var11, double[] var13);
}
