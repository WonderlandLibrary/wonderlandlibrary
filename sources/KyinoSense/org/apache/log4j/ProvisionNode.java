/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j;

import java.util.Vector;
import org.apache.log4j.Logger;

class ProvisionNode
extends Vector {
    private static final long serialVersionUID = -4479121426311014469L;

    ProvisionNode(Logger logger) {
        this.addElement(logger);
    }
}

