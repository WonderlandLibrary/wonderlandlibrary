package me.theresa.fontRenderer.font.impl;

import me.theresa.fontRenderer.font.Color;
import me.theresa.fontRenderer.font.geom.Shape;
import me.theresa.fontRenderer.font.geom.Vector2f;

public interface ShapeFill {
   Color colorAt(Shape var1, float var2, float var3);

   Vector2f getOffsetAt(Shape var1, float var2, float var3);
}
