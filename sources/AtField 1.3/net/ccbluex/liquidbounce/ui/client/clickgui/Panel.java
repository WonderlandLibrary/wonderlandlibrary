/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.ui.client.clickgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.Element;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public abstract class Panel
extends MinecraftInstance {
    private int dragged;
    private final int width;
    public boolean drag;
    private float elementsHeight;
    private boolean scrollbar;
    private final String name;
    private boolean visible;
    private final int height;
    public int x2;
    private int scroll;
    public int x;
    private final List elements;
    private boolean open;
    private float fade;
    public int y;
    public int y2;

    void updateFade(int n) {
        if (this.open) {
            if (this.fade < this.elementsHeight) {
                this.fade += 0.4f * (float)n;
            }
            if (this.fade > this.elementsHeight) {
                this.fade = (int)this.elementsHeight;
            }
        } else {
            if (this.fade > 0.0f) {
                this.fade -= 0.4f * (float)n;
            }
            if (this.fade < 0.0f) {
                this.fade = 0.0f;
            }
        }
    }

    public boolean getScrollbar() {
        return this.scrollbar;
    }

    public abstract void setupItems();

    public int getHeight() {
        return this.height;
    }

    public List getElements() {
        return this.elements;
    }

    public int getDragged() {
        return this.dragged;
    }

    public boolean getOpen() {
        return this.open;
    }

    public void drawScreen(int n, int n2, float f) {
        int n3;
        int n4;
        if (!this.visible) {
            return;
        }
        int n5 = (Integer)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get();
        if (this.drag) {
            n4 = this.x2 + n;
            n3 = this.y2 + n2;
            if (n4 > -1) {
                this.x = n4;
            }
            if (n3 > -1) {
                this.y = n3;
            }
        }
        this.elementsHeight = this.getElementsHeight() - 1;
        int n6 = n4 = this.elements.size() >= n5 ? 1 : 0;
        if (this.scrollbar != n4) {
            this.scrollbar = n4;
        }
        LiquidBounce.clickGui.style.drawPanel(n, n2, this);
        n3 = this.y + this.height - 2;
        int n7 = 0;
        for (Element element : this.elements) {
            if (++n7 > this.scroll && n7 < this.scroll + (n5 + 1) && this.scroll < this.elements.size()) {
                element.setLocation(this.x, n3);
                element.setWidth(this.getWidth());
                if ((float)n3 <= (float)this.getY() + this.fade) {
                    element.drawScreen(n, n2, f);
                }
                n3 += element.getHeight() + 1;
                element.setVisible(true);
                continue;
            }
            element.setVisible(false);
        }
    }

    public boolean handleScroll(int n, int n2, int n3) {
        int n4 = (Integer)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get();
        if (n >= this.getX() && n <= this.getX() + 100 && n2 >= this.getY() && (float)n2 <= (float)(this.getY() + 19) + this.elementsHeight) {
            if (n3 < 0 && this.scroll < this.elements.size() - n4) {
                ++this.scroll;
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
            } else if (n3 > 0) {
                --this.scroll;
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
            }
            if (n3 < 0) {
                if (this.dragged < this.elements.size() - n4) {
                    ++this.dragged;
                }
            } else if (n3 > 0 && this.dragged >= 1) {
                --this.dragged;
            }
            return true;
        }
        return false;
    }

    /*
     * Exception decompiling
     */
    public void mouseClicked(int var1, int var2, int var3) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl26 : ALOAD_0 - null : trying to set 0 previously set to 2
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public int getWidth() {
        return this.width;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public String getName() {
        return this.name;
    }

    public void setY(int n) {
        this.y = n;
    }

    public boolean isVisible() {
        return this.visible;
    }

    private int getElementsHeight() {
        int n = 0;
        int n2 = 0;
        for (Element element : this.elements) {
            if (n2 >= (Integer)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get()) continue;
            n += element.getHeight() + 1;
            ++n2;
        }
        return n;
    }

    public void setOpen(boolean bl) {
        this.open = bl;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int n) {
        this.x = n;
    }

    public int getFade() {
        return (int)this.fade;
    }

    public Panel(String string, int n, int n2, int n3, int n4, boolean bl) {
        this.name = string;
        this.elements = new ArrayList();
        this.scrollbar = false;
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
        this.open = bl;
        this.visible = true;
        this.setupItems();
    }

    public void mouseReleased(int n, int n2, int n3) {
        if (!this.visible) {
            return;
        }
        this.drag = false;
        if (!this.open) {
            return;
        }
        for (Element element : this.elements) {
            element.mouseReleased(n, n2, n3);
        }
    }

    public int getY() {
        return this.y;
    }
}

