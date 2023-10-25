/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.StringProperty
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.scene.Node
 *  javafx.scene.input.KeyCombination
 */
package com.sun.javafx.menu;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;

public interface MenuItemBase {
    public void setId(String var1);

    public String getId();

    public StringProperty idProperty();

    public void setText(String var1);

    public String getText();

    public StringProperty textProperty();

    public void setGraphic(Node var1);

    public Node getGraphic();

    public ObjectProperty<Node> graphicProperty();

    public void setOnAction(EventHandler<ActionEvent> var1);

    public EventHandler<ActionEvent> getOnAction();

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty();

    public void setDisable(boolean var1);

    public boolean isDisable();

    public BooleanProperty disableProperty();

    public void setVisible(boolean var1);

    public boolean isVisible();

    public BooleanProperty visibleProperty();

    public void setAccelerator(KeyCombination var1);

    public KeyCombination getAccelerator();

    public ObjectProperty<KeyCombination> acceleratorProperty();

    public void setMnemonicParsing(boolean var1);

    public boolean isMnemonicParsing();

    public BooleanProperty mnemonicParsingProperty();

    public void fire();

    public void fireValidation();
}

