// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas.model;

import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;", "", "(Ljava/lang/String;I)V", "NONE", "ELEMENT_MOVE", "ELEMENT_SELECT", "ELEMENT_RESIZE", "canvas" })
public enum CanvasAction
{
    NONE, 
    ELEMENT_MOVE, 
    ELEMENT_SELECT, 
    ELEMENT_RESIZE;
    
    private static final /* synthetic */ CanvasAction[] $values() {
        return new CanvasAction[] { CanvasAction.NONE, CanvasAction.ELEMENT_MOVE, CanvasAction.ELEMENT_SELECT, CanvasAction.ELEMENT_RESIZE };
    }
    
    static {
        $VALUES = $values();
    }
}
