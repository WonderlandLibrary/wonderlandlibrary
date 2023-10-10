/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Triangulator;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.LineStripRenderer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public final class ShapeRenderer {
    private static SGL GL = Renderer.get();
    private static LineStripRenderer LSR = Renderer.getLineStripRenderer();

    public static final void draw(Shape shape) {
        Texture t2 = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        float[] points = shape.getPoints();
        LSR.start();
        for (int i2 = 0; i2 < points.length; i2 += 2) {
            LSR.vertex(points[i2], points[i2 + 1]);
        }
        if (shape.closed()) {
            LSR.vertex(points[0], points[1]);
        }
        LSR.end();
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    public static final void draw(Shape shape, ShapeFill fill) {
        float[] points = shape.getPoints();
        Texture t2 = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        float[] center = shape.getCenter();
        GL.glBegin(3);
        for (int i2 = 0; i2 < points.length; i2 += 2) {
            fill.colorAt(shape, points[i2], points[i2 + 1]).bind();
            Vector2f offset = fill.getOffsetAt(shape, points[i2], points[i2 + 1]);
            GL.glVertex2f(points[i2] + offset.x, points[i2 + 1] + offset.y);
        }
        if (shape.closed()) {
            fill.colorAt(shape, points[0], points[1]).bind();
            Vector2f offset = fill.getOffsetAt(shape, points[0], points[1]);
            GL.glVertex2f(points[0] + offset.x, points[1] + offset.y);
        }
        GL.glEnd();
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    public static boolean validFill(Shape shape) {
        if (shape.getTriangles() == null) {
            return false;
        }
        return shape.getTriangles().getTriangleCount() != 0;
    }

    public static final void fill(Shape shape) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        Texture t2 = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        ShapeRenderer.fill(shape, new PointCallback(){

            public float[] preRenderPoint(Shape shape, float x2, float y2) {
                return null;
            }
        });
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    private static final void fill(Shape shape, PointCallback callback) {
        Triangulator tris = shape.getTriangles();
        GL.glBegin(4);
        for (int i2 = 0; i2 < tris.getTriangleCount(); ++i2) {
            for (int p2 = 0; p2 < 3; ++p2) {
                float[] pt = tris.getTrianglePoint(i2, p2);
                float[] np = callback.preRenderPoint(shape, pt[0], pt[1]);
                if (np == null) {
                    GL.glVertex2f(pt[0], pt[1]);
                    continue;
                }
                GL.glVertex2f(np[0], np[1]);
            }
        }
        GL.glEnd();
    }

    public static final void texture(Shape shape, Image image) {
        ShapeRenderer.texture(shape, image, 0.01f, 0.01f);
    }

    public static final void textureFit(Shape shape, Image image) {
        ShapeRenderer.textureFit(shape, image, 1.0f, 1.0f);
    }

    public static final void texture(Shape shape, final Image image, final float scaleX, final float scaleY) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        Texture t2 = TextureImpl.getLastBind();
        image.getTexture().bind();
        ShapeRenderer.fill(shape, new PointCallback(){

            public float[] preRenderPoint(Shape shape, float x2, float y2) {
                float tx = x2 * scaleX;
                float ty = y2 * scaleY;
                tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
                ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
                GL.glTexCoord2f(tx, ty);
                return null;
            }
        });
        float[] points = shape.getPoints();
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    public static final void textureFit(Shape shape, final Image image, final float scaleX, final float scaleY) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        float[] points = shape.getPoints();
        Texture t2 = TextureImpl.getLastBind();
        image.getTexture().bind();
        float minX = shape.getX();
        float minY = shape.getY();
        float maxX = shape.getMaxX() - minX;
        float maxY = shape.getMaxY() - minY;
        ShapeRenderer.fill(shape, new PointCallback(){

            public float[] preRenderPoint(Shape shape, float x2, float y2) {
                x2 -= shape.getMinX();
                y2 -= shape.getMinY();
                float tx = (x2 /= shape.getMaxX() - shape.getMinX()) * scaleX;
                float ty = (y2 /= shape.getMaxY() - shape.getMinY()) * scaleY;
                tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
                ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
                GL.glTexCoord2f(tx, ty);
                return null;
            }
        });
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    public static final void fill(Shape shape, final ShapeFill fill) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        Texture t2 = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        float[] center = shape.getCenter();
        ShapeRenderer.fill(shape, new PointCallback(){

            public float[] preRenderPoint(Shape shape, float x2, float y2) {
                fill.colorAt(shape, x2, y2).bind();
                Vector2f offset = fill.getOffsetAt(shape, x2, y2);
                return new float[]{offset.x + x2, offset.y + y2};
            }
        });
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    public static final void texture(Shape shape, final Image image, final float scaleX, final float scaleY, final ShapeFill fill) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        Texture t2 = TextureImpl.getLastBind();
        image.getTexture().bind();
        final float[] center = shape.getCenter();
        ShapeRenderer.fill(shape, new PointCallback(){

            public float[] preRenderPoint(Shape shape, float x2, float y2) {
                fill.colorAt(shape, x2 - center[0], y2 - center[1]).bind();
                Vector2f offset = fill.getOffsetAt(shape, x2, y2);
                float tx = (x2 += offset.x) * scaleX;
                float ty = (y2 += offset.y) * scaleY;
                tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
                ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
                GL.glTexCoord2f(tx, ty);
                return new float[]{offset.x + x2, offset.y + y2};
            }
        });
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    public static final void texture(Shape shape, Image image, final TexCoordGenerator gen) {
        Texture t2 = TextureImpl.getLastBind();
        image.getTexture().bind();
        float[] center = shape.getCenter();
        ShapeRenderer.fill(shape, new PointCallback(){

            public float[] preRenderPoint(Shape shape, float x2, float y2) {
                Vector2f tex = gen.getCoordFor(x2, y2);
                GL.glTexCoord2f(tex.x, tex.y);
                return new float[]{x2, y2};
            }
        });
        if (t2 == null) {
            TextureImpl.bindNone();
        } else {
            t2.bind();
        }
    }

    private static interface PointCallback {
        public float[] preRenderPoint(Shape var1, float var2, float var3);
    }
}

