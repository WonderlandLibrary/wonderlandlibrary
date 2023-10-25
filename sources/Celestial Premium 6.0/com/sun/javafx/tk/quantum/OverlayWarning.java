/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation$Status
 *  javafx.animation.FadeTransition
 *  javafx.animation.PauseTransition
 *  javafx.animation.SequentialTransition
 *  javafx.geometry.Rectangle2D
 *  javafx.scene.Group
 *  javafx.scene.Node
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Rectangle
 *  javafx.scene.text.Font
 *  javafx.scene.text.Text
 *  javafx.scene.text.TextAlignment
 *  javafx.util.Duration
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.tk.quantum.OverlayWarningHelper;
import com.sun.javafx.tk.quantum.ViewScene;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class OverlayWarning
extends Group {
    private static final float PAD = 40.0f;
    private static final float RECTW = 600.0f;
    private static final float RECTH = 100.0f;
    private static final float ARC = 20.0f;
    private static final int FONTSIZE = 24;
    private ViewScene view;
    private SequentialTransition overlayTransition;
    private boolean warningTransition;
    private Text text;
    private Rectangle background;

    public OverlayWarning(ViewScene viewScene) {
        OverlayWarningHelper.initHelper(this);
        this.text = new Text();
        this.view = viewScene;
        this.createOverlayGroup();
        PauseTransition pauseTransition = new PauseTransition(Duration.millis((double)4000.0));
        FadeTransition fadeTransition = new FadeTransition(Duration.millis((double)1000.0), (Node)this);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        this.overlayTransition = new SequentialTransition();
        this.overlayTransition.getChildren().add((Object)pauseTransition);
        this.overlayTransition.getChildren().add((Object)fadeTransition);
        this.overlayTransition.setOnFinished(actionEvent -> {
            this.warningTransition = false;
            this.view.getWindowStage().setWarning(null);
        });
    }

    protected ViewScene getView() {
        return this.view;
    }

    protected final void setView(ViewScene viewScene) {
        if (this.view != null) {
            this.view.getWindowStage().setWarning(null);
        }
        this.view = viewScene;
        this.view.entireSceneNeedsRepaint();
    }

    protected void warn(String string) {
        this.text.setText(string);
        this.warningTransition = true;
        this.overlayTransition.play();
    }

    protected void cancel() {
        if (this.overlayTransition != null && this.overlayTransition.getStatus() == Animation.Status.RUNNING) {
            this.overlayTransition.stop();
            this.warningTransition = false;
        }
        this.view.getWindowStage().setWarning(null);
    }

    protected boolean inWarningTransition() {
        return this.warningTransition;
    }

    private void createOverlayGroup() {
        Font font = new Font(Font.getDefault().getFamily(), 24.0);
        Rectangle2D rectangle2D = new Rectangle2D(0.0, 0.0, (double)this.view.getSceneState().getScreenWidth(), (double)this.view.getSceneState().getScreenHeight());
        String string = "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.75), 3, 0.0, 0, 2);";
        this.text.setStroke((Paint)Color.WHITE);
        this.text.setFill((Paint)Color.WHITE);
        this.text.setFont(font);
        this.text.setWrappingWidth(520.0);
        this.text.setStyle(string);
        this.text.setTextAlignment(TextAlignment.CENTER);
        this.background = this.createBackground(this.text, rectangle2D);
        this.getChildren().add((Object)this.background);
        this.getChildren().add((Object)this.text);
    }

    private Rectangle createBackground(Text text, Rectangle2D rectangle2D) {
        Rectangle rectangle = new Rectangle();
        double d = text.getLayoutBounds().getWidth();
        double d2 = text.getLayoutBounds().getHeight();
        double d3 = (rectangle2D.getWidth() - 600.0) / 2.0;
        double d4 = rectangle2D.getHeight() / 2.0;
        rectangle.setWidth(600.0);
        rectangle.setHeight(100.0);
        rectangle.setX(d3);
        rectangle.setY(d4 - 100.0);
        rectangle.setArcWidth(20.0);
        rectangle.setArcHeight(20.0);
        rectangle.setFill((Paint)Color.gray((double)0.0, (double)0.6));
        text.setX(d3 + (600.0 - d) / 2.0);
        text.setY(d4 - 50.0 + (d2 - text.getBaselineOffset()) / 2.0);
        return rectangle;
    }

    private void doUpdatePeer() {
        NodeHelper.updatePeer((Node)this.text);
        NodeHelper.updatePeer((Node)this.background);
    }

    protected void updateBounds() {
        super.updateBounds();
    }

    private void doMarkDirty(DirtyBits dirtyBits) {
        this.view.synchroniseOverlayWarning();
    }

    static {
        OverlayWarningHelper.setOverlayWarningAccessor(new OverlayWarningHelper.OverlayWarningAccessor(){

            @Override
            public void doUpdatePeer(Node node) {
                ((OverlayWarning)node).doUpdatePeer();
            }

            @Override
            public void doMarkDirty(Node node, DirtyBits dirtyBits) {
                ((OverlayWarning)node).doMarkDirty(dirtyBits);
            }
        });
    }
}

