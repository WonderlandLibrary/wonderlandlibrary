/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;

public class EffectFilter {
    private Effect effect;
    private NodeEffectInput nodeInput;

    EffectFilter(Effect effect, NGNode nGNode) {
        this.effect = effect;
        this.nodeInput = new NodeEffectInput(nGNode);
    }

    Effect getEffect() {
        return this.effect;
    }

    NodeEffectInput getNodeInput() {
        return this.nodeInput;
    }

    void dispose() {
        this.effect = null;
        this.nodeInput.setNode(null);
        this.nodeInput = null;
    }

    BaseBounds getBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        BaseBounds baseBounds2 = this.getEffect().getBounds(baseTransform, this.nodeInput);
        return baseBounds.deriveWithNewBounds(baseBounds2);
    }

    void render(Graphics graphics) {
        NodeEffectInput nodeEffectInput = this.getNodeInput();
        PrEffectHelper.render(this.getEffect(), graphics, 0.0f, 0.0f, nodeEffectInput);
        nodeEffectInput.flush();
    }
}

