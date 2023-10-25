/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.win.WinSystemClipboard;

final class WinDnDClipboard
extends WinSystemClipboard {
    private static int dragButton = 0;
    private int sourceSupportedActions = 0;

    public WinDnDClipboard(String string) {
        super(string);
    }

    @Override
    protected void create() {
    }

    @Override
    protected native void dispose();

    @Override
    protected boolean isOwner() {
        return this.getDragButton() != 0;
    }

    @Override
    protected void pushTargetActionToSystem(int n) {
        throw new UnsupportedOperationException("[Target Action] not supported! Override View.handleDragDrop instead.");
    }

    @Override
    protected native void push(Object[] var1, int var2);

    @Override
    protected boolean pop() {
        return this.getPtr() != 0L;
    }

    private static WinDnDClipboard getInstance() {
        return (WinDnDClipboard)WinDnDClipboard.get("DND");
    }

    @Override
    public String toString() {
        return "Windows DnD Clipboard";
    }

    public int getDragButton() {
        return dragButton;
    }

    private void setDragButton(int n) {
        dragButton = n;
    }

    @Override
    protected final int supportedSourceActionsFromSystem() {
        return this.sourceSupportedActions != 0 ? this.sourceSupportedActions : super.supportedSourceActionsFromSystem();
    }

    private void setSourceSupportedActions(int n) {
        this.sourceSupportedActions = n;
    }
}

