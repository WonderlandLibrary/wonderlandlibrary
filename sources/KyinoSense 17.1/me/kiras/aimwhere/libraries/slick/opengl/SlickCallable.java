/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.kiras.aimwhere.libraries.slick.opengl;

import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.opengl.Texture;
import me.kiras.aimwhere.libraries.slick.opengl.TextureImpl;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.Renderer;
import org.lwjgl.opengl.GL11;

public abstract class SlickCallable {
    private static Texture lastUsed;
    private static boolean inSafe;

    public static void enterSafeBlock() {
        if (inSafe) {
            return;
        }
        Renderer.get().flush();
        lastUsed = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        GL11.glPushAttrib((int)1048575);
        GL11.glPushClientAttrib((int)-1);
        GL11.glMatrixMode((int)5888);
        GL11.glPushMatrix();
        GL11.glMatrixMode((int)5889);
        GL11.glPushMatrix();
        GL11.glMatrixMode((int)5888);
        inSafe = true;
    }

    public static void leaveSafeBlock() {
        if (!inSafe) {
            return;
        }
        GL11.glMatrixMode((int)5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode((int)5888);
        GL11.glPopMatrix();
        GL11.glPopClientAttrib();
        GL11.glPopAttrib();
        if (lastUsed != null) {
            lastUsed.bind();
        } else {
            TextureImpl.bindNone();
        }
        inSafe = false;
    }

    public final void call() throws SlickException {
        SlickCallable.enterSafeBlock();
        this.performGLOperations();
        SlickCallable.leaveSafeBlock();
    }

    protected abstract void performGLOperations() throws SlickException;

    static {
        inSafe = false;
    }
}

