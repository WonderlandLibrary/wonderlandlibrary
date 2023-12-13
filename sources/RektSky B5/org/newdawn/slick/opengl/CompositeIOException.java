/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl;

import java.io.IOException;
import java.util.ArrayList;

public class CompositeIOException
extends IOException {
    private ArrayList exceptions = new ArrayList();

    public void addException(Exception e2) {
        this.exceptions.add(e2);
    }

    public String getMessage() {
        String msg = "Composite Exception: \n";
        for (int i2 = 0; i2 < this.exceptions.size(); ++i2) {
            msg = msg + "\t" + ((IOException)this.exceptions.get(i2)).getMessage() + "\n";
        }
        return msg;
    }
}

