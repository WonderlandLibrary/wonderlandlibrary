/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.svg;

import me.kiras.aimwhere.libraries.slick.SlickException;
import org.w3c.dom.Element;

public class ParsingException
extends SlickException {
    public ParsingException(String nodeID, String message, Throwable cause) {
        super("(" + nodeID + ") " + message, cause);
    }

    public ParsingException(Element element, String message, Throwable cause) {
        super("(" + element.getAttribute("id") + ") " + message, cause);
    }

    public ParsingException(String nodeID, String message) {
        super("(" + nodeID + ") " + message);
    }

    public ParsingException(Element element, String message) {
        super("(" + element.getAttribute("id") + ") " + message);
    }
}

