/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.svg;

import java.util.ArrayList;
import org.newdawn.slick.geom.MorphShape;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;

public class SVGMorph
extends Diagram {
    private ArrayList figures = new ArrayList();

    public SVGMorph(Diagram diagram) {
        super(diagram.getWidth(), diagram.getHeight());
        for (int i2 = 0; i2 < diagram.getFigureCount(); ++i2) {
            Figure figure = diagram.getFigure(i2);
            Figure copy = new Figure(figure.getType(), new MorphShape(figure.getShape()), figure.getData(), figure.getTransform());
            this.figures.add(copy);
        }
    }

    public void addStep(Diagram diagram) {
        if (diagram.getFigureCount() != this.figures.size()) {
            throw new RuntimeException("Mismatched diagrams, missing ids");
        }
        block0: for (int i2 = 0; i2 < diagram.getFigureCount(); ++i2) {
            Figure figure = diagram.getFigure(i2);
            String id = figure.getData().getMetaData();
            for (int j2 = 0; j2 < this.figures.size(); ++j2) {
                Figure existing = (Figure)this.figures.get(j2);
                if (!existing.getData().getMetaData().equals(id)) continue;
                MorphShape morph = (MorphShape)existing.getShape();
                morph.addShape(figure.getShape());
                continue block0;
            }
        }
    }

    public void setExternalDiagram(Diagram diagram) {
        block0: for (int i2 = 0; i2 < this.figures.size(); ++i2) {
            Figure figure = (Figure)this.figures.get(i2);
            for (int j2 = 0; j2 < diagram.getFigureCount(); ++j2) {
                Figure newBase = diagram.getFigure(j2);
                if (!newBase.getData().getMetaData().equals(figure.getData().getMetaData())) continue;
                MorphShape shape = (MorphShape)figure.getShape();
                shape.setExternalFrame(newBase.getShape());
                continue block0;
            }
        }
    }

    public void updateMorphTime(float delta) {
        for (int i2 = 0; i2 < this.figures.size(); ++i2) {
            Figure figure = (Figure)this.figures.get(i2);
            MorphShape shape = (MorphShape)figure.getShape();
            shape.updateMorphTime(delta);
        }
    }

    public void setMorphTime(float time) {
        for (int i2 = 0; i2 < this.figures.size(); ++i2) {
            Figure figure = (Figure)this.figures.get(i2);
            MorphShape shape = (MorphShape)figure.getShape();
            shape.setMorphTime(time);
        }
    }

    public int getFigureCount() {
        return this.figures.size();
    }

    public Figure getFigure(int index) {
        return (Figure)this.figures.get(index);
    }
}

