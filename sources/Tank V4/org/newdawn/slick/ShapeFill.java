package org.newdawn.slick;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public interface ShapeFill {
   Color colorAt(Shape var1, float var2, float var3);

   Vector2f getOffsetAt(Shape var1, float var2, float var3);
}
