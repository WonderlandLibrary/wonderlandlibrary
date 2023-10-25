/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.ReadOnlyBooleanProperty
 *  javafx.collections.ObservableList
 *  javafx.event.Event
 *  javafx.event.EventHandler
 */
package com.sun.javafx.menu;

import com.sun.javafx.menu.MenuItemBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;

public interface MenuBase
extends MenuItemBase {
    public boolean isShowing();

    public ReadOnlyBooleanProperty showingProperty();

    public ObjectProperty<EventHandler<Event>> onShowingProperty();

    public void setOnShowing(EventHandler<Event> var1);

    public EventHandler<Event> getOnShowing();

    public ObjectProperty<EventHandler<Event>> onShownProperty();

    public void setOnShown(EventHandler<Event> var1);

    public EventHandler<Event> getOnShown();

    public ObjectProperty<EventHandler<Event>> onHidingProperty();

    public void setOnHiding(EventHandler<Event> var1);

    public EventHandler<Event> getOnHiding();

    public ObjectProperty<EventHandler<Event>> onHiddenProperty();

    public void setOnHidden(EventHandler<Event> var1);

    public EventHandler<Event> getOnHidden();

    public ObservableList<MenuItemBase> getItemsBase();

    public void show();

    public void hide();
}

