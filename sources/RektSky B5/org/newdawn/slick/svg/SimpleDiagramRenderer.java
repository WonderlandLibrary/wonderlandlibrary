/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.svg;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Gradient;
import org.newdawn.slick.svg.LinearGradientFill;
import org.newdawn.slick.svg.RadialGradientFill;

public class SimpleDiagramRenderer {
    protected static SGL GL = Renderer.get();
    public Diagram diagram;
    public int list = -1;

    public SimpleDiagramRenderer(Diagram diagram) {
        this.diagram = diagram;
    }

    public void render(Graphics g2) {
        if (this.list == -1) {
            this.list = GL.glGenLists(1);
            GL.glNewList(this.list, 4864);
            SimpleDiagramRenderer.render(g2, this.diagram);
            GL.glEndList();
        }
        GL.glCallList(this.list);
        TextureImpl.bindNone();
    }

    public static void render(Graphics g2, Diagram diagram) {
        for (int i2 = 0; i2 < diagram.getFigureCount(); ++i2) {
            Figure figure = diagram.getFigure(i2);
            if (figure.getData().isFilled()) {
                String fill;
                if (figure.getData().isColor("fill")) {
                    g2.setColor(figure.getData().getAsColor("fill"));
                    g2.fill(diagram.getFigure(i2).getShape());
                    g2.setAntiAlias(true);
                    g2.draw(diagram.getFigure(i2).getShape());
                    g2.setAntiAlias(false);
                }
                if (diagram.getPatternDef(fill = figure.getData().getAsReference("fill")) != null) {
                    System.out.println("PATTERN");
                }
                if (diagram.getGradient(fill) != null) {
                    Gradient gradient = diagram.getGradient(fill);
                    Shape shape = diagram.getFigure(i2).getShape();
                    TexCoordGenerator fg = null;
                    fg = gradient.isRadial() ? new RadialGradientFill(shape, diagram.getFigure(i2).getTransform(), gradient) : new LinearGradientFill(shape, diagram.getFigure(i2).getTransform(), gradient);
                    Color.white.bind();
                    ShapeRenderer.texture(shape, gradient.getImage(), fg);
                }
            }
            if (!figure.getData().isStroked() || !figure.getData().isColor("stroke")) continue;
            g2.setColor(figure.getData().getAsColor("stroke"));
            g2.setLineWidth(figure.getData().getAsFloat("stroke-width"));
            g2.setAntiAlias(true);
            g2.draw(diagram.getFigure(i2).getShape());
            g2.setAntiAlias(false);
            g2.resetLineWidth();
        }
    }
}

