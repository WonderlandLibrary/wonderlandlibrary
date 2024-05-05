/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.geom;

import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.ShapeFill;
import me.kiras.aimwhere.libraries.slick.geom.Shape;
import me.kiras.aimwhere.libraries.slick.geom.TexCoordGenerator;
import me.kiras.aimwhere.libraries.slick.geom.Triangulator;
import me.kiras.aimwhere.libraries.slick.geom.Vector2f;
import me.kiras.aimwhere.libraries.slick.opengl.Texture;
import me.kiras.aimwhere.libraries.slick.opengl.TextureImpl;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.LineStripRenderer;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.Renderer;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.SGL;

public final class ShapeRenderer {
    private static SGL GL = Renderer.get();
    private static LineStripRenderer LSR = Renderer.getLineStripRenderer();

    public static final void draw(Shape shape) {
        Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        float[] points = shape.getPoints();
        LSR.start();
        for (int i = 0; i < points.length; i += 2) {
            LSR.vertex(points[i], points[i + 1]);
        }
        if (shape.closed()) {
            LSR.vertex(points[0], points[1]);
        }
        LSR.end();
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
        }
    }

    public static final void draw(Shape shape, ShapeFill fill) {
        float[] points = shape.getPoints();
        Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        float[] center = shape.getCenter();
        GL.glBegin(3);
        for (int i = 0; i < points.length; i += 2) {
            fill.colorAt(shape, points[i], points[i + 1]).bind();
            Vector2f offset = fill.getOffsetAt(shape, points[i], points[i + 1]);
            GL.glVertex2f(points[i] + offset.x, points[i + 1] + offset.y);
        }
        if (shape.closed()) {
            fill.colorAt(shape, points[0], points[1]).bind();
            Vector2f offset = fill.getOffsetAt(shape, points[0], points[1]);
            GL.glVertex2f(points[0] + offset.x, points[1] + offset.y);
        }
        GL.glEnd();
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
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
        Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        ShapeRenderer.fill(shape, new PointCallback(){

            @Override
            public float[] preRenderPoint(Shape shape, float x, float y) {
                return null;
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
        }
    }

    private static final void fill(Shape shape, PointCallback callback) {
        Triangulator tris = shape.getTriangles();
        GL.glBegin(4);
        for (int i = 0; i < tris.getTriangleCount(); ++i) {
            for (int p = 0; p < 3; ++p) {
                float[] pt = tris.getTrianglePoint(i, p);
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

    public static final void texture(Shape shape, Image image2) {
        ShapeRenderer.texture(shape, image2, 0.01f, 0.01f);
    }

    public static final void textureFit(Shape shape, Image image2) {
        ShapeRenderer.textureFit(shape, image2, 1.0f, 1.0f);
    }

    public static final void texture(Shape shape, final Image image2, final float scaleX, final float scaleY) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        Texture t = TextureImpl.getLastBind();
        image2.getTexture().bind();
        ShapeRenderer.fill(shape, new PointCallback(){

            @Override
            public float[] preRenderPoint(Shape shape, float x, float y) {
                float tx = x * scaleX;
                float ty = y * scaleY;
                tx = image2.getTextureOffsetX() + image2.getTextureWidth() * tx;
                ty = image2.getTextureOffsetY() + image2.getTextureHeight() * ty;
                GL.glTexCoord2f(tx, ty);
                return null;
            }
        });
        float[] points = shape.getPoints();
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
        }
    }

    public static final void textureFit(Shape shape, final Image image2, final float scaleX, final float scaleY) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        float[] points = shape.getPoints();
        Texture t = TextureImpl.getLastBind();
        image2.getTexture().bind();
        float minX = shape.getX();
        float minY = shape.getY();
        float maxX = shape.getMaxX() - minX;
        float maxY = shape.getMaxY() - minY;
        ShapeRenderer.fill(shape, new PointCallback(){

            @Override
            public float[] preRenderPoint(Shape shape, float x, float y) {
                x -= shape.getMinX();
                y -= shape.getMinY();
                float tx = (x /= shape.getMaxX() - shape.getMinX()) * scaleX;
                float ty = (y /= shape.getMaxY() - shape.getMinY()) * scaleY;
                tx = image2.getTextureOffsetX() + image2.getTextureWidth() * tx;
                ty = image2.getTextureOffsetY() + image2.getTextureHeight() * ty;
                GL.glTexCoord2f(tx, ty);
                return null;
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
        }
    }

    public static final void fill(Shape shape, final ShapeFill fill) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        float[] center = shape.getCenter();
        ShapeRenderer.fill(shape, new PointCallback(){

            @Override
            public float[] preRenderPoint(Shape shape, float x, float y) {
                fill.colorAt(shape, x, y).bind();
                Vector2f offset = fill.getOffsetAt(shape, x, y);
                return new float[]{offset.x + x, offset.y + y};
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
        }
    }

    public static final void texture(Shape shape, final Image image2, final float scaleX, final float scaleY, final ShapeFill fill) {
        if (!ShapeRenderer.validFill(shape)) {
            return;
        }
        Texture t = TextureImpl.getLastBind();
        image2.getTexture().bind();
        final float[] center = shape.getCenter();
        ShapeRenderer.fill(shape, new PointCallback(){

            @Override
            public float[] preRenderPoint(Shape shape, float x, float y) {
                fill.colorAt(shape, x - center[0], y - center[1]).bind();
                Vector2f offset = fill.getOffsetAt(shape, x, y);
                float tx = (x += offset.x) * scaleX;
                float ty = (y += offset.y) * scaleY;
                tx = image2.getTextureOffsetX() + image2.getTextureWidth() * tx;
                ty = image2.getTextureOffsetY() + image2.getTextureHeight() * ty;
                GL.glTexCoord2f(tx, ty);
                return new float[]{offset.x + x, offset.y + y};
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
        }
    }

    public static final void texture(Shape shape, Image image2, final TexCoordGenerator gen) {
        Texture t = TextureImpl.getLastBind();
        image2.getTexture().bind();
        float[] center = shape.getCenter();
        ShapeRenderer.fill(shape, new PointCallback(){

            @Override
            public float[] preRenderPoint(Shape shape, float x, float y) {
                Vector2f tex = gen.getCoordFor(x, y);
                GL.glTexCoord2f(tex.x, tex.y);
                return new float[]{x, y};
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        } else {
            t.bind();
        }
    }

    private static interface PointCallback {
        public float[] preRenderPoint(Shape var1, float var2, float var3);
    }
}

