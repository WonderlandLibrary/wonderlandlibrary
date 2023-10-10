/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util.pathfinding;

import java.io.Serializable;
import java.util.ArrayList;

public class Path
implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList steps = new ArrayList();

    public int getLength() {
        return this.steps.size();
    }

    public Step getStep(int index) {
        return (Step)this.steps.get(index);
    }

    public int getX(int index) {
        return this.getStep(index).x;
    }

    public int getY(int index) {
        return this.getStep(index).y;
    }

    public void appendStep(int x2, int y2) {
        this.steps.add(new Step(x2, y2));
    }

    public void prependStep(int x2, int y2) {
        this.steps.add(0, new Step(x2, y2));
    }

    public boolean contains(int x2, int y2) {
        return this.steps.contains(new Step(x2, y2));
    }

    public class Step
    implements Serializable {
        private int x;
        private int y;

        public Step(int x2, int y2) {
            this.x = x2;
            this.y = y2;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int hashCode() {
            return this.x * this.y;
        }

        public boolean equals(Object other) {
            if (other instanceof Step) {
                Step o2 = (Step)other;
                return o2.x == this.x && o2.y == this.y;
            }
            return false;
        }
    }
}

