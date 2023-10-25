/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.geometry.Bounds
 *  javafx.geometry.Point2D
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.input.KeyCombination
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.View;
import com.sun.glass.ui.win.WinTextRangeProvider;
import com.sun.glass.ui.win.WinVariant;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;

final class WinAccessible
extends Accessible {
    private static int idCount;
    private static final int UIA_BoundingRectanglePropertyId = 30001;
    private static final int UIA_ProcessIdPropertyId = 30002;
    private static final int UIA_ControlTypePropertyId = 30003;
    private static final int UIA_LocalizedControlTypePropertyId = 30004;
    private static final int UIA_NamePropertyId = 30005;
    private static final int UIA_AcceleratorKeyPropertyId = 30006;
    private static final int UIA_AccessKeyPropertyId = 30007;
    private static final int UIA_HasKeyboardFocusPropertyId = 30008;
    private static final int UIA_IsKeyboardFocusablePropertyId = 30009;
    private static final int UIA_IsEnabledPropertyId = 30010;
    private static final int UIA_AutomationIdPropertyId = 30011;
    private static final int UIA_ClassNamePropertyId = 30012;
    private static final int UIA_HelpTextPropertyId = 30013;
    private static final int UIA_ClickablePointPropertyId = 30014;
    private static final int UIA_CulturePropertyId = 30015;
    private static final int UIA_IsControlElementPropertyId = 30016;
    private static final int UIA_IsContentElementPropertyId = 30017;
    private static final int UIA_LabeledByPropertyId = 30018;
    private static final int UIA_IsPasswordPropertyId = 30019;
    private static final int UIA_NativeWindowHandlePropertyId = 30020;
    private static final int UIA_ItemTypePropertyId = 30021;
    private static final int UIA_IsOffscreenPropertyId = 30022;
    private static final int UIA_OrientationPropertyId = 30023;
    private static final int UIA_FrameworkIdPropertyId = 30024;
    private static final int UIA_ValueValuePropertyId = 30045;
    private static final int UIA_RangeValueValuePropertyId = 30047;
    private static final int UIA_ExpandCollapseExpandCollapseStatePropertyId = 30070;
    private static final int UIA_ToggleToggleStatePropertyId = 30086;
    private static final int UIA_AriaRolePropertyId = 30101;
    private static final int UIA_ProviderDescriptionPropertyId = 30107;
    private static final int UIA_InvokePatternId = 10000;
    private static final int UIA_SelectionPatternId = 10001;
    private static final int UIA_ValuePatternId = 10002;
    private static final int UIA_RangeValuePatternId = 10003;
    private static final int UIA_ScrollPatternId = 10004;
    private static final int UIA_ExpandCollapsePatternId = 10005;
    private static final int UIA_GridPatternId = 10006;
    private static final int UIA_GridItemPatternId = 10007;
    private static final int UIA_SelectionItemPatternId = 10010;
    private static final int UIA_TablePatternId = 10012;
    private static final int UIA_TableItemPatternId = 10013;
    private static final int UIA_TextPatternId = 10014;
    private static final int UIA_TogglePatternId = 10015;
    private static final int UIA_TransformPatternId = 10016;
    private static final int UIA_ScrollItemPatternId = 10017;
    private static final int UIA_ItemContainerPatternId = 10019;
    private static final int UIA_ButtonControlTypeId = 50000;
    private static final int UIA_CheckBoxControlTypeId = 50002;
    private static final int UIA_ComboBoxControlTypeId = 50003;
    private static final int UIA_EditControlTypeId = 50004;
    private static final int UIA_HyperlinkControlTypeId = 50005;
    private static final int UIA_ImageControlTypeId = 50006;
    private static final int UIA_ListItemControlTypeId = 50007;
    private static final int UIA_ListControlTypeId = 50008;
    private static final int UIA_MenuControlTypeId = 50009;
    private static final int UIA_MenuBarControlTypeId = 50010;
    private static final int UIA_MenuItemControlTypeId = 50011;
    private static final int UIA_ProgressBarControlTypeId = 50012;
    private static final int UIA_RadioButtonControlTypeId = 50013;
    private static final int UIA_ScrollBarControlTypeId = 50014;
    private static final int UIA_SliderControlTypeId = 50015;
    private static final int UIA_SpinnerControlTypeId = 50016;
    private static final int UIA_TabControlTypeId = 50018;
    private static final int UIA_TabItemControlTypeId = 50019;
    private static final int UIA_TextControlTypeId = 50020;
    private static final int UIA_ToolBarControlTypeId = 50021;
    private static final int UIA_TreeControlTypeId = 50023;
    private static final int UIA_TreeItemControlTypeId = 50024;
    private static final int UIA_GroupControlTypeId = 50026;
    private static final int UIA_ThumbControlTypeId = 50027;
    private static final int UIA_DataGridControlTypeId = 50028;
    private static final int UIA_DataItemControlTypeId = 50029;
    private static final int UIA_SplitButtonControlTypeId = 50031;
    private static final int UIA_WindowControlTypeId = 50032;
    private static final int UIA_PaneControlTypeId = 50033;
    private static final int UIA_TableControlTypeId = 50036;
    private static final int NavigateDirection_Parent = 0;
    private static final int NavigateDirection_NextSibling = 1;
    private static final int NavigateDirection_PreviousSibling = 2;
    private static final int NavigateDirection_FirstChild = 3;
    private static final int NavigateDirection_LastChild = 4;
    private static final int RowOrColumnMajor_RowMajor = 0;
    private static final int RowOrColumnMajor_ColumnMajor = 1;
    private static final int RowOrColumnMajor_Indeterminate = 2;
    private static final int UIA_MenuOpenedEventId = 20003;
    private static final int UIA_AutomationPropertyChangedEventId = 20004;
    private static final int UIA_AutomationFocusChangedEventId = 20005;
    private static final int UIA_MenuClosedEventId = 20007;
    private static final int UIA_SelectionItem_ElementRemovedFromSelectionEventId = 20011;
    private static final int UIA_SelectionItem_ElementSelectedEventId = 20012;
    private static final int UIA_Text_TextSelectionChangedEventId = 20014;
    private static final int UIA_Text_TextChangedEventId = 20015;
    private static final int UIA_MenuModeStartEventId = 20018;
    private static final int UIA_MenuModeEndEventId = 20019;
    private static final int SupportedTextSelection_None = 0;
    private static final int SupportedTextSelection_Single = 1;
    private static final int SupportedTextSelection_Multiple = 2;
    private static final int ExpandCollapseState_Collapsed = 0;
    private static final int ExpandCollapseState_Expanded = 1;
    private static final int ExpandCollapseState_PartiallyExpanded = 2;
    private static final int ExpandCollapseState_LeafNode = 3;
    private static final int ScrollAmount_LargeDecrement = 0;
    private static final int ScrollAmount_SmallDecrement = 1;
    private static final int ScrollAmount_NoAmount = 2;
    private static final int ScrollAmount_LargeIncrement = 3;
    private static final int ScrollAmount_SmallIncrement = 4;
    private static final int UIA_ScrollPatternNoScroll = -1;
    private static final int ToggleState_Off = 0;
    private static final int ToggleState_On = 1;
    private static final int ToggleState_Indeterminate = 2;
    private static final int UiaAppendRuntimeId = 3;
    private long peer = this._createGlassAccessible();
    private int id;
    private WinTextRangeProvider documentRange;
    private WinTextRangeProvider selectionRange;
    private int lastIndex = 0;

    private static native void _initIDs();

    private native long _createGlassAccessible();

    private native void _destroyGlassAccessible(long var1);

    private static native long UiaRaiseAutomationEvent(long var0, int var2);

    private static native long UiaRaiseAutomationPropertyChangedEvent(long var0, int var2, WinVariant var3, WinVariant var4);

    private static native boolean UiaClientsAreListening();

    WinAccessible() {
        if (this.peer == 0L) {
            throw new RuntimeException("could not create platform accessible");
        }
        this.id = idCount++;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.selectionRange != null) {
            this.selectionRange.dispose();
            this.selectionRange = null;
        }
        if (this.documentRange != null) {
            this.documentRange.dispose();
            this.documentRange = null;
        }
        if (this.peer != 0L) {
            this._destroyGlassAccessible(this.peer);
            this.peer = 0L;
        }
    }

    @Override
    public void sendNotification(AccessibleAttribute accessibleAttribute) {
        if (this.isDisposed()) {
            return;
        }
        switch (accessibleAttribute) {
            case FOCUS_NODE: {
                if (this.getView() != null) {
                    long l = this.GetFocus();
                    if (l == 0L) break;
                    WinAccessible.UiaRaiseAutomationEvent(l, 20005);
                    break;
                }
                Node node = (Node)this.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                if (node != null) {
                    WinAccessible.UiaRaiseAutomationEvent(this.getNativeAccessible(node), 20005);
                    break;
                }
                Scene scene = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]);
                Accessible accessible = this.getAccessible(scene);
                if (accessible == null) break;
                accessible.sendNotification(AccessibleAttribute.FOCUS_NODE);
                break;
            }
            case FOCUS_ITEM: {
                Node node = (Node)this.getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
                long l = this.getNativeAccessible(node);
                if (l == 0L) break;
                WinAccessible.UiaRaiseAutomationEvent(l, 20005);
                break;
            }
            case INDETERMINATE: {
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) != AccessibleRole.CHECK_BOX) break;
                this.notifyToggleState();
                break;
            }
            case SELECTED: {
                Object object = this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (object == AccessibleRole.CHECK_BOX || object == AccessibleRole.TOGGLE_BUTTON) {
                    this.notifyToggleState();
                    break;
                }
                Boolean bl = (Boolean)this.getAttribute(AccessibleAttribute.SELECTED, new Object[0]);
                if (bl == null) break;
                if (bl.booleanValue()) {
                    WinAccessible.UiaRaiseAutomationEvent(this.peer, 20012);
                    break;
                }
                WinAccessible.UiaRaiseAutomationEvent(this.peer, 20011);
                break;
            }
            case FOCUSED: {
                break;
            }
            case VALUE: {
                Double d = (Double)this.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
                if (d == null) break;
                WinVariant winVariant = new WinVariant();
                winVariant.vt = (short)5;
                winVariant.dblVal = 0.0;
                WinVariant winVariant2 = new WinVariant();
                winVariant2.vt = (short)5;
                winVariant2.dblVal = d;
                WinAccessible.UiaRaiseAutomationPropertyChangedEvent(this.peer, 30047, winVariant, winVariant2);
                break;
            }
            case SELECTION_START: 
            case SELECTION_END: {
                boolean bl;
                if (this.selectionRange == null) break;
                Integer n = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_START, new Object[0]);
                boolean bl2 = n != null && n.intValue() != this.selectionRange.getStart();
                Integer n2 = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                boolean bl3 = bl = n2 != null && n2.intValue() != this.selectionRange.getEnd();
                if (!bl2 && !bl) break;
                WinAccessible.UiaRaiseAutomationEvent(this.peer, 20014);
                break;
            }
            case TEXT: {
                String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                if (string != null) {
                    WinVariant winVariant = new WinVariant();
                    winVariant.vt = (short)8;
                    winVariant.bstrVal = "";
                    WinVariant winVariant3 = new WinVariant();
                    winVariant3.vt = (short)8;
                    winVariant3.bstrVal = string;
                    if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.SPINNER) {
                        WinAccessible.UiaRaiseAutomationPropertyChangedEvent(this.peer, 30005, winVariant, winVariant3);
                    } else {
                        WinAccessible.UiaRaiseAutomationPropertyChangedEvent(this.peer, 30045, winVariant, winVariant3);
                    }
                }
                if (this.selectionRange == null && this.documentRange == null) break;
                WinAccessible.UiaRaiseAutomationEvent(this.peer, 20015);
                break;
            }
            case EXPANDED: {
                Boolean bl = (Boolean)this.getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
                if (bl == null) break;
                WinVariant winVariant = new WinVariant();
                winVariant.vt = (short)3;
                winVariant.lVal = bl != false ? 0 : 1;
                WinVariant winVariant4 = new WinVariant();
                winVariant4.vt = (short)3;
                int n = winVariant4.lVal = bl != false ? 1 : 0;
                if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.TREE_TABLE_ROW) {
                    Accessible accessible = this.getContainer();
                    Integer n3 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                    if (accessible == null || n3 == null) break;
                    Node node = (Node)accessible.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, n3, 0);
                    if (node == null) break;
                    long l = ((WinAccessible)this.getAccessible(node)).getNativeAccessible();
                    WinAccessible.UiaRaiseAutomationPropertyChangedEvent(l, 30070, winVariant, winVariant4);
                    break;
                }
                WinAccessible.UiaRaiseAutomationPropertyChangedEvent(this.peer, 30070, winVariant, winVariant4);
                break;
            }
            case PARENT: {
                break;
            }
            default: {
                WinAccessible.UiaRaiseAutomationEvent(this.peer, 20004);
            }
        }
    }

    private void notifyToggleState() {
        int n = this.get_ToggleState();
        WinVariant winVariant = new WinVariant();
        winVariant.vt = (short)3;
        winVariant.lVal = n;
        WinVariant winVariant2 = new WinVariant();
        winVariant2.vt = (short)3;
        winVariant2.lVal = n;
        WinAccessible.UiaRaiseAutomationPropertyChangedEvent(this.peer, 30086, winVariant, winVariant2);
    }

    @Override
    protected long getNativeAccessible() {
        return this.peer;
    }

    private Accessible getContainer() {
        if (this.isDisposed()) {
            return null;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole != null) {
            switch (accessibleRole) {
                case TABLE_ROW: 
                case TABLE_CELL: {
                    return this.getContainerAccessible(AccessibleRole.TABLE_VIEW);
                }
                case LIST_ITEM: {
                    return this.getContainerAccessible(AccessibleRole.LIST_VIEW);
                }
                case TAB_ITEM: {
                    return this.getContainerAccessible(AccessibleRole.TAB_PANE);
                }
                case PAGE_ITEM: {
                    return this.getContainerAccessible(AccessibleRole.PAGINATION);
                }
                case TREE_ITEM: {
                    return this.getContainerAccessible(AccessibleRole.TREE_VIEW);
                }
                case TREE_TABLE_ROW: 
                case TREE_TABLE_CELL: {
                    return this.getContainerAccessible(AccessibleRole.TREE_TABLE_VIEW);
                }
            }
        }
        return null;
    }

    private int getControlType() {
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == null) {
            return 50026;
        }
        switch (accessibleRole) {
            case CONTEXT_MENU: {
                return 50009;
            }
            case RADIO_MENU_ITEM: 
            case CHECK_MENU_ITEM: 
            case MENU: 
            case MENU_ITEM: {
                return 50011;
            }
            case BUTTON: 
            case MENU_BUTTON: 
            case TOGGLE_BUTTON: 
            case INCREMENT_BUTTON: 
            case DECREMENT_BUTTON: {
                return 50000;
            }
            case SPLIT_MENU_BUTTON: {
                return 50031;
            }
            case PAGINATION: 
            case TAB_PANE: {
                return 50018;
            }
            case TAB_ITEM: 
            case PAGE_ITEM: {
                return 50019;
            }
            case SLIDER: {
                return 50015;
            }
            case PARENT: {
                return this.getView() != null ? 50032 : 50033;
            }
            case TEXT: {
                return 50020;
            }
            case TEXT_FIELD: 
            case PASSWORD_FIELD: 
            case TEXT_AREA: {
                return 50004;
            }
            case TREE_TABLE_VIEW: 
            case TABLE_VIEW: {
                return 50036;
            }
            case LIST_VIEW: {
                return 50008;
            }
            case LIST_ITEM: {
                return 50007;
            }
            case TABLE_CELL: 
            case TREE_TABLE_CELL: {
                return 50029;
            }
            case IMAGE_VIEW: {
                return 50006;
            }
            case RADIO_BUTTON: {
                return 50013;
            }
            case CHECK_BOX: {
                return 50002;
            }
            case COMBO_BOX: {
                return 50003;
            }
            case HYPERLINK: {
                return 50005;
            }
            case TREE_VIEW: {
                return 50023;
            }
            case TREE_ITEM: {
                return 50024;
            }
            case PROGRESS_INDICATOR: {
                return 50012;
            }
            case TOOL_BAR: {
                return 50021;
            }
            case TITLED_PANE: {
                return 50026;
            }
            case SCROLL_PANE: {
                return 50033;
            }
            case SCROLL_BAR: {
                return 50014;
            }
            case THUMB: {
                return 50027;
            }
            case MENU_BAR: {
                return 50010;
            }
            case DATE_PICKER: {
                return 50033;
            }
            case SPINNER: {
                return 50016;
            }
        }
        return 0;
    }

    private List<Node> getUnignoredChildren(WinAccessible winAccessible) {
        if (winAccessible == null) {
            return FXCollections.emptyObservableList();
        }
        ObservableList observableList = (ObservableList)winAccessible.getAttribute(AccessibleAttribute.CHILDREN, new Object[0]);
        if (observableList == null) {
            return FXCollections.emptyObservableList();
        }
        return observableList.stream().filter(Node::isVisible).collect(Collectors.toList());
    }

    private Accessible getRow() {
        Integer n = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
        if (n == null) {
            return null;
        }
        if (n != 0) {
            return null;
        }
        Integer n2 = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
        if (n2 == null) {
            return null;
        }
        Accessible accessible = this.getContainer();
        if (accessible == null) {
            return null;
        }
        Node node = (Node)accessible.getAttribute(AccessibleAttribute.ROW_AT_INDEX, n2);
        return this.getAccessible(node);
    }

    private void changeSelection(boolean bl, boolean bl2) {
        Object object;
        Object object2;
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == null) {
            return;
        }
        Accessible accessible = this.getContainer();
        if (accessible == null) {
            return;
        }
        Node node = null;
        switch (accessibleRole) {
            case LIST_ITEM: {
                object2 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (object2 == null) break;
                node = (Node)accessible.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, object2);
                break;
            }
            case TREE_ITEM: {
                object2 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (object2 == null) break;
                node = (Node)accessible.getAttribute(AccessibleAttribute.ROW_AT_INDEX, object2);
                break;
            }
            case TABLE_CELL: 
            case TREE_TABLE_CELL: {
                object2 = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                object = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
                if (object2 == null || object == null) break;
                node = (Node)accessible.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, object2, object);
                break;
            }
        }
        if (node != null) {
            object2 = FXCollections.observableArrayList();
            if (!bl2 && (object = (ObservableList)accessible.getAttribute(AccessibleAttribute.SELECTED_ITEMS, new Object[0])) != null) {
                object2.addAll((Collection)object);
            }
            if (bl) {
                object2.add(node);
            } else {
                object2.remove(node);
            }
            accessible.executeAction(AccessibleAction.SET_SELECTED_ITEMS, object2);
        }
    }

    private long GetPatternProvider(int n) {
        if (this.isDisposed()) {
            return 0L;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        boolean bl = false;
        switch (accessibleRole) {
            case MENU: 
            case SPLIT_MENU_BUTTON: {
                bl = n == 10000 || n == 10005;
                break;
            }
            case RADIO_MENU_ITEM: 
            case CHECK_MENU_ITEM: {
                bl = n == 10000 || n == 10015;
                break;
            }
            case MENU_ITEM: 
            case BUTTON: 
            case MENU_BUTTON: 
            case INCREMENT_BUTTON: 
            case DECREMENT_BUTTON: 
            case HYPERLINK: {
                bl = n == 10000;
                break;
            }
            case TAB_ITEM: 
            case PAGE_ITEM: {
                bl = n == 10010;
                break;
            }
            case PAGINATION: 
            case TAB_PANE: {
                bl = n == 10001;
                break;
            }
            case SCROLL_PANE: {
                bl = n == 10004;
                break;
            }
            case TREE_TABLE_VIEW: 
            case TABLE_VIEW: {
                bl = n == 10001 || n == 10006 || n == 10012 || n == 10004;
                break;
            }
            case TREE_TABLE_CELL: {
                bl = n == 10010 || n == 10007 || n == 10013 || n == 10005 || n == 10017;
                break;
            }
            case TABLE_CELL: {
                bl = n == 10010 || n == 10007 || n == 10013 || n == 10017;
                break;
            }
            case TREE_VIEW: {
                bl = n == 10001 || n == 10004;
                break;
            }
            case TREE_ITEM: {
                bl = n == 10010 || n == 10005 || n == 10017;
                break;
            }
            case LIST_VIEW: {
                bl = n == 10001 || n == 10004;
                break;
            }
            case LIST_ITEM: {
                bl = n == 10010 || n == 10017;
                break;
            }
            case TEXT_FIELD: 
            case TEXT_AREA: {
                bl = n == 10014 || n == 10002;
                break;
            }
            case TEXT: {
                break;
            }
            case RADIO_BUTTON: {
                bl = n == 10010;
                break;
            }
            case TOGGLE_BUTTON: 
            case CHECK_BOX: {
                bl = n == 10015;
                break;
            }
            case TOOL_BAR: 
            case TITLED_PANE: {
                bl = n == 10005;
                break;
            }
            case COMBO_BOX: {
                bl = n == 10005 || n == 10002;
                break;
            }
            case SLIDER: 
            case PROGRESS_INDICATOR: 
            case SCROLL_BAR: {
                bl = n == 10003;
                break;
            }
        }
        return bl ? this.getNativeAccessible() : 0L;
    }

    private long get_HostRawElementProvider() {
        if (this.isDisposed()) {
            return 0L;
        }
        View view = this.getView();
        return view != null ? view.getNativeView() : 0L;
    }

    private WinVariant GetPropertyValue(int n) {
        if (this.isDisposed()) {
            return null;
        }
        WinVariant winVariant = null;
        switch (n) {
            case 30003: {
                int n2 = this.getControlType();
                if (n2 == 0) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)3;
                winVariant.lVal = n2;
                break;
            }
            case 30007: {
                String string = (String)this.getAttribute(AccessibleAttribute.MNEMONIC, new Object[0]);
                if (string == null) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = "Alt+" + string.toLowerCase();
                break;
            }
            case 30006: {
                KeyCombination keyCombination = (KeyCombination)this.getAttribute(AccessibleAttribute.ACCELERATOR, new Object[0]);
                if (keyCombination == null) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = keyCombination.toString().replaceAll("Shortcut", "Ctrl");
                break;
            }
            case 30005: {
                Node node;
                String string;
                AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (accessibleRole == null) {
                    accessibleRole = AccessibleRole.NODE;
                }
                switch (accessibleRole) {
                    case TEXT_FIELD: 
                    case TEXT_AREA: 
                    case COMBO_BOX: {
                        string = null;
                        break;
                    }
                    case INCREMENT_BUTTON: 
                    case DECREMENT_BUTTON: {
                        string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                        if (string != null && string.length() != 0) break;
                        if (accessibleRole == AccessibleRole.INCREMENT_BUTTON) {
                            string = "increment";
                            break;
                        }
                        string = "decrement";
                        break;
                    }
                    default: {
                        string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                    }
                }
                if ((string == null || string.length() == 0) && (node = (Node)this.getAttribute(AccessibleAttribute.LABELED_BY, new Object[0])) != null) {
                    string = (String)this.getAccessible(node).getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                }
                if (string == null || string.length() == 0) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = string;
                break;
            }
            case 30013: {
                String string = (String)this.getAttribute(AccessibleAttribute.HELP, new Object[0]);
                if (string == null) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = string;
                break;
            }
            case 30004: {
                String string = (String)this.getAttribute(AccessibleAttribute.ROLE_DESCRIPTION, new Object[0]);
                if (string == null) {
                    AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                    if (accessibleRole == null) {
                        accessibleRole = AccessibleRole.NODE;
                    }
                    switch (accessibleRole) {
                        case TITLED_PANE: {
                            string = "title pane";
                            break;
                        }
                        case PAGE_ITEM: {
                            string = "page";
                            break;
                        }
                    }
                }
                if (string == null) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = string;
                break;
            }
            case 30008: {
                Node node;
                Node node2;
                Accessible accessible;
                Scene scene;
                Boolean bl = (Boolean)this.getAttribute(AccessibleAttribute.FOCUSED, new Object[0]);
                if (Boolean.FALSE.equals(bl) && (scene = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0])) != null && (accessible = this.getAccessible(scene)) != null && (node2 = (Node)accessible.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0])) != null && this.getNativeAccessible(node = (Node)this.getAccessible(node2).getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0])) == this.peer) {
                    bl = true;
                }
                winVariant = new WinVariant();
                winVariant.vt = (short)11;
                winVariant.boolVal = bl != null ? bl : false;
                break;
            }
            case 30016: 
            case 30017: {
                winVariant = new WinVariant();
                winVariant.vt = (short)11;
                winVariant.boolVal = this.getView() != null || !this.isIgnored();
                break;
            }
            case 30010: {
                Boolean bl = (Boolean)this.getAttribute(AccessibleAttribute.DISABLED, new Object[0]);
                winVariant = new WinVariant();
                winVariant.vt = (short)11;
                winVariant.boolVal = bl != null ? !bl.booleanValue() : true;
                break;
            }
            case 30009: {
                winVariant = new WinVariant();
                winVariant.vt = (short)11;
                winVariant.boolVal = true;
                break;
            }
            case 30019: {
                AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                winVariant = new WinVariant();
                winVariant.vt = (short)11;
                winVariant.boolVal = accessibleRole == AccessibleRole.PASSWORD_FIELD;
                break;
            }
            case 30011: {
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = "JavaFX" + this.id;
                break;
            }
            case 30107: {
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = "JavaFXProvider";
                break;
            }
        }
        return winVariant;
    }

    private float[] get_BoundingRectangle() {
        if (this.isDisposed()) {
            return null;
        }
        if (this.getView() != null) {
            return null;
        }
        Bounds bounds = (Bounds)this.getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
        if (bounds != null) {
            return new float[]{(float)bounds.getMinX(), (float)bounds.getMinY(), (float)bounds.getWidth(), (float)bounds.getHeight()};
        }
        return null;
    }

    private long get_FragmentRoot() {
        if (this.isDisposed()) {
            return 0L;
        }
        Scene scene = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]);
        if (scene == null) {
            return 0L;
        }
        WinAccessible winAccessible = (WinAccessible)this.getAccessible(scene);
        if (winAccessible == null || winAccessible.isDisposed()) {
            return 0L;
        }
        return winAccessible.getNativeAccessible();
    }

    private long[] GetEmbeddedFragmentRoots() {
        if (this.isDisposed()) {
            return null;
        }
        return null;
    }

    private int[] GetRuntimeId() {
        if (this.isDisposed()) {
            return null;
        }
        if (this.getView() != null) {
            return null;
        }
        return new int[]{3, this.id};
    }

    private long NavigateListView(WinAccessible winAccessible, int n) {
        Integer n2;
        Accessible accessible = winAccessible.getContainer();
        if (accessible == null) {
            return 0L;
        }
        Integer n3 = (Integer)accessible.getAttribute(AccessibleAttribute.ITEM_COUNT, new Object[0]);
        if (n3 == null || n3 == 0) {
            return 0L;
        }
        Integer n4 = (Integer)winAccessible.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
        if (n4 == null) {
            return 0L;
        }
        if (0 > n4 || n4 >= n3) {
            return 0L;
        }
        switch (n) {
            case 1: {
                n2 = n4;
                n4 = n4 + 1;
                break;
            }
            case 2: {
                n2 = n4;
                n4 = n4 - 1;
                break;
            }
            case 3: {
                n4 = 0;
                break;
            }
            case 4: {
                n4 = n3 - 1;
            }
        }
        if (0 > n4 || n4 >= n3) {
            return 0L;
        }
        n2 = (Node)accessible.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, n4);
        return this.getNativeAccessible((Node)n2);
    }

    private long Navigate(int n2) {
        if (this.isDisposed()) {
            return 0L;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        boolean bl = accessibleRole == AccessibleRole.TREE_ITEM;
        Node node = null;
        switch (n2) {
            case 0: {
                if (this.getView() != null) {
                    return 0L;
                }
                if (bl) {
                    node = (Node)this.getAttribute(AccessibleAttribute.TREE_ITEM_PARENT, new Object[0]);
                    if (node != null) break;
                    WinAccessible winAccessible = (WinAccessible)this.getContainer();
                    return winAccessible != null ? winAccessible.getNativeAccessible() : 0L;
                }
                node = (Node)this.getAttribute(AccessibleAttribute.PARENT, new Object[0]);
                if (node != null) break;
                Scene scene = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]);
                WinAccessible winAccessible = (WinAccessible)this.getAccessible(scene);
                if (winAccessible == null || winAccessible == this || winAccessible.isDisposed()) {
                    return 0L;
                }
                return winAccessible.getNativeAccessible();
            }
            case 1: 
            case 2: {
                Function<Integer, Node> function;
                List<Node> list;
                if (accessibleRole == AccessibleRole.LIST_ITEM) {
                    return this.NavigateListView(this, n2);
                }
                Node node2 = (Node)this.getAttribute(bl ? AccessibleAttribute.TREE_ITEM_PARENT : AccessibleAttribute.PARENT, new Object[0]);
                if (node2 == null) break;
                WinAccessible winAccessible = (WinAccessible)this.getAccessible(node2);
                int n3 = 0;
                if (bl) {
                    list = (Integer)winAccessible.getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                    if (list == null) {
                        return 0L;
                    }
                    n3 = (Integer)((Object)list);
                    function = n -> (Node)winAccessible.getAttribute(AccessibleAttribute.TREE_ITEM_AT_INDEX, n);
                } else {
                    list = this.getUnignoredChildren(winAccessible);
                    if (list == null) {
                        return 0L;
                    }
                    n3 = list.size();
                    function = n -> (Node)list.get((int)n);
                }
                int n4 = winAccessible.lastIndex;
                int n5 = -1;
                if (0 <= n4 && n4 < n3 && this.getNativeAccessible(function.apply(n4)) == this.peer) {
                    n5 = n4;
                } else {
                    for (int i = 0; i < n3; ++i) {
                        if (this.getNativeAccessible(function.apply(i)) != this.peer) continue;
                        n5 = i;
                        break;
                    }
                }
                if (n5 == -1) break;
                n5 = n2 == 1 ? ++n5 : --n5;
                if (0 > n5 || n5 >= n3) break;
                node = function.apply(n5);
                winAccessible.lastIndex = n5;
                break;
            }
            case 3: 
            case 4: {
                this.lastIndex = -1;
                if (accessibleRole == AccessibleRole.LIST_VIEW) {
                    this.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, 0);
                }
                if (accessibleRole == AccessibleRole.TREE_VIEW) {
                    this.lastIndex = 0;
                    node = (Node)this.getAttribute(AccessibleAttribute.ROW_AT_INDEX, 0);
                    break;
                }
                if (bl) {
                    Integer n6 = (Integer)this.getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                    if (n6 == null || n6 <= 0) break;
                    this.lastIndex = n2 == 3 ? 0 : n6 - 1;
                    node = (Node)this.getAttribute(AccessibleAttribute.TREE_ITEM_AT_INDEX, this.lastIndex);
                    break;
                }
                List<Node> list = this.getUnignoredChildren(this);
                if (list != null && list.size() > 0) {
                    this.lastIndex = n2 == 3 ? 0 : list.size() - 1;
                    node = list.get(this.lastIndex);
                }
                if (node == null || (accessibleRole = (AccessibleRole)this.getAccessible(node).getAttribute(AccessibleAttribute.ROLE, new Object[0])) != AccessibleRole.LIST_ITEM) break;
                WinAccessible winAccessible = (WinAccessible)this.getAccessible(node);
                return this.NavigateListView(winAccessible, n2);
            }
        }
        return this.getNativeAccessible(node);
    }

    private void SetFocus() {
        if (this.isDisposed()) {
            return;
        }
        this.executeAction(AccessibleAction.REQUEST_FOCUS, new Object[0]);
    }

    private long ElementProviderFromPoint(double d, double d2) {
        if (this.isDisposed()) {
            return 0L;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.NODE_AT_POINT, new Object[]{new Point2D(d, d2)});
        return this.getNativeAccessible(node);
    }

    private long GetFocus() {
        if (this.isDisposed()) {
            return 0L;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
        if (node == null) {
            return 0L;
        }
        Node node2 = (Node)this.getAccessible(node).getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
        if (node2 != null) {
            return this.getNativeAccessible(node2);
        }
        return this.getNativeAccessible(node);
    }

    private void AdviseEventAdded(int n, long l) {
    }

    private void AdviseEventRemoved(int n, long l) {
    }

    private void Invoke() {
        if (this.isDisposed()) {
            return;
        }
        this.executeAction(AccessibleAction.FIRE, new Object[0]);
    }

    private long[] GetSelection() {
        if (this.isDisposed()) {
            return null;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == null) {
            return null;
        }
        switch (accessibleRole) {
            case TREE_TABLE_VIEW: 
            case TABLE_VIEW: 
            case LIST_VIEW: 
            case TREE_VIEW: {
                ObservableList observableList = (ObservableList)this.getAttribute(AccessibleAttribute.SELECTED_ITEMS, new Object[0]);
                if (observableList == null) break;
                return observableList.stream().mapToLong(node -> this.getNativeAccessible((Node)node)).toArray();
            }
            case PAGINATION: 
            case TAB_PANE: {
                Node node2 = (Node)this.getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
                if (node2 == null) break;
                return new long[]{this.getNativeAccessible(node2)};
            }
            case TEXT_FIELD: 
            case TEXT_AREA: {
                Integer n;
                if (this.selectionRange == null) {
                    this.selectionRange = new WinTextRangeProvider(this);
                }
                int n2 = (n = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_START, new Object[0])) != null ? n : 0;
                int n3 = -1;
                int n4 = -1;
                if (n2 >= 0) {
                    n = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                    int n5 = n3 = n != null ? n : 0;
                    if (n3 >= n2) {
                        String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                        int n6 = n4 = string != null ? string.length() : 0;
                    }
                }
                if (n4 != -1 && n3 <= n4) {
                    this.selectionRange.setRange(n2, n3);
                } else {
                    this.selectionRange.setRange(0, 0);
                }
                return new long[]{this.selectionRange.getNativeProvider()};
            }
        }
        return null;
    }

    private boolean get_CanSelectMultiple() {
        if (this.isDisposed()) {
            return false;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole != null) {
            switch (accessibleRole) {
                case TREE_TABLE_VIEW: 
                case TABLE_VIEW: 
                case LIST_VIEW: 
                case TREE_VIEW: {
                    return Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.MULTIPLE_SELECTION, new Object[0]));
                }
            }
        }
        return false;
    }

    private boolean get_IsSelectionRequired() {
        return !this.isDisposed();
    }

    private void SetValue(double d) {
        if (this.isDisposed()) {
            return;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole != null) {
            switch (accessibleRole) {
                case SLIDER: 
                case SCROLL_BAR: {
                    this.executeAction(AccessibleAction.SET_VALUE, d);
                    break;
                }
            }
        }
    }

    private double get_Value() {
        if (this.isDisposed()) {
            return 0.0;
        }
        if (Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0]))) {
            return 0.0;
        }
        Double d = (Double)this.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
        return d != null ? d : 0.0;
    }

    private boolean get_IsReadOnly() {
        if (this.isDisposed()) {
            return false;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole != null) {
            switch (accessibleRole) {
                case SLIDER: {
                    return false;
                }
                case SCROLL_BAR: {
                    return true;
                }
                case TEXT_FIELD: 
                case TEXT_AREA: 
                case COMBO_BOX: {
                    return Boolean.FALSE.equals(this.getAttribute(AccessibleAttribute.EDITABLE, new Object[0]));
                }
            }
        }
        return true;
    }

    private double get_Maximum() {
        if (this.isDisposed()) {
            return 0.0;
        }
        Double d = (Double)this.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
        return d != null ? d : 0.0;
    }

    private double get_Minimum() {
        if (this.isDisposed()) {
            return 0.0;
        }
        Double d = (Double)this.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
        return d != null ? d : 0.0;
    }

    private double get_LargeChange() {
        if (this.isDisposed()) {
            return 0.0;
        }
        return 10.0;
    }

    private double get_SmallChange() {
        if (this.isDisposed()) {
            return 0.0;
        }
        return 3.0;
    }

    private void SetValueString(String string) {
        if (this.isDisposed()) {
            return;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole != null) {
            switch (accessibleRole) {
                case TEXT_FIELD: 
                case TEXT_AREA: {
                    this.executeAction(AccessibleAction.SET_TEXT, string);
                    break;
                }
            }
        }
    }

    private String get_ValueString() {
        if (this.isDisposed()) {
            return null;
        }
        return (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
    }

    private void Select() {
        if (this.isDisposed()) {
            return;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole != null) {
            switch (accessibleRole) {
                case TAB_ITEM: 
                case PAGE_ITEM: {
                    this.executeAction(AccessibleAction.REQUEST_FOCUS, new Object[0]);
                    break;
                }
                case BUTTON: 
                case TOGGLE_BUTTON: 
                case INCREMENT_BUTTON: 
                case DECREMENT_BUTTON: 
                case RADIO_BUTTON: {
                    this.executeAction(AccessibleAction.FIRE, new Object[0]);
                    break;
                }
                case TABLE_CELL: 
                case LIST_ITEM: 
                case TREE_ITEM: 
                case TREE_TABLE_CELL: {
                    this.changeSelection(true, true);
                    break;
                }
            }
        }
    }

    private void AddToSelection() {
        if (this.isDisposed()) {
            return;
        }
        this.changeSelection(true, false);
    }

    private void RemoveFromSelection() {
        if (this.isDisposed()) {
            return;
        }
        this.changeSelection(false, false);
    }

    private boolean get_IsSelected() {
        if (this.isDisposed()) {
            return false;
        }
        return Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.SELECTED, new Object[0]));
    }

    private long get_SelectionContainer() {
        if (this.isDisposed()) {
            return 0L;
        }
        WinAccessible winAccessible = (WinAccessible)this.getContainer();
        return winAccessible != null ? winAccessible.getNativeAccessible() : 0L;
    }

    private long[] GetVisibleRanges() {
        if (this.isDisposed()) {
            return null;
        }
        return new long[]{this.get_DocumentRange()};
    }

    private long RangeFromChild(long l) {
        if (this.isDisposed()) {
            return 0L;
        }
        return 0L;
    }

    private long RangeFromPoint(double d, double d2) {
        if (this.isDisposed()) {
            return 0L;
        }
        Integer n = (Integer)this.getAttribute(AccessibleAttribute.OFFSET_AT_POINT, new Object[]{new Point2D(d, d2)});
        if (n != null) {
            WinTextRangeProvider winTextRangeProvider = new WinTextRangeProvider(this);
            winTextRangeProvider.setRange(n, n);
            return winTextRangeProvider.getNativeProvider();
        }
        return 0L;
    }

    private long get_DocumentRange() {
        String string;
        if (this.isDisposed()) {
            return 0L;
        }
        if (this.documentRange == null) {
            this.documentRange = new WinTextRangeProvider(this);
        }
        if ((string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0])) == null) {
            return 0L;
        }
        this.documentRange.setRange(0, string.length());
        return this.documentRange.getNativeProvider();
    }

    private int get_SupportedTextSelection() {
        if (this.isDisposed()) {
            return 0;
        }
        return 1;
    }

    private int get_ColumnCount() {
        if (this.isDisposed()) {
            return 0;
        }
        Integer n = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_COUNT, new Object[0]);
        return n != null ? n : 1;
    }

    private int get_RowCount() {
        if (this.isDisposed()) {
            return 0;
        }
        Integer n = (Integer)this.getAttribute(AccessibleAttribute.ROW_COUNT, new Object[0]);
        return n != null ? n : 0;
    }

    private long GetItem(int n, int n2) {
        if (this.isDisposed()) {
            return 0L;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, n, n2);
        return this.getNativeAccessible(node);
    }

    private int get_Column() {
        if (this.isDisposed()) {
            return 0;
        }
        Integer n = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
        return n != null ? n : 0;
    }

    private int get_ColumnSpan() {
        if (this.isDisposed()) {
            return 0;
        }
        return 1;
    }

    private long get_ContainingGrid() {
        if (this.isDisposed()) {
            return 0L;
        }
        WinAccessible winAccessible = (WinAccessible)this.getContainer();
        return winAccessible != null ? winAccessible.getNativeAccessible() : 0L;
    }

    private int get_Row() {
        if (this.isDisposed()) {
            return 0;
        }
        Integer n = null;
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole != null) {
            switch (accessibleRole) {
                case TABLE_ROW: 
                case LIST_ITEM: 
                case TREE_TABLE_ROW: {
                    n = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                    break;
                }
                case TABLE_CELL: 
                case TREE_TABLE_CELL: {
                    n = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                    break;
                }
            }
        }
        return n != null ? n : 0;
    }

    private int get_RowSpan() {
        if (this.isDisposed()) {
            return 0;
        }
        return 1;
    }

    private long[] GetColumnHeaders() {
        if (this.isDisposed()) {
            return null;
        }
        return null;
    }

    private long[] GetRowHeaders() {
        if (this.isDisposed()) {
            return null;
        }
        return null;
    }

    private int get_RowOrColumnMajor() {
        if (this.isDisposed()) {
            return 0;
        }
        return 0;
    }

    private long[] GetColumnHeaderItems() {
        if (this.isDisposed()) {
            return null;
        }
        Integer n = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
        if (n == null) {
            return null;
        }
        Accessible accessible = this.getContainer();
        if (accessible == null) {
            return null;
        }
        Node node = (Node)accessible.getAttribute(AccessibleAttribute.COLUMN_AT_INDEX, n);
        if (node == null) {
            return null;
        }
        return new long[]{this.getNativeAccessible(node)};
    }

    private long[] GetRowHeaderItems() {
        if (this.isDisposed()) {
            return null;
        }
        return null;
    }

    private void Toggle() {
        if (this.isDisposed()) {
            return;
        }
        this.executeAction(AccessibleAction.FIRE, new Object[0]);
    }

    private int get_ToggleState() {
        if (this.isDisposed()) {
            return 0;
        }
        if (Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0]))) {
            return 2;
        }
        boolean bl = Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.SELECTED, new Object[0]));
        return bl ? 1 : 0;
    }

    private void Collapse() {
        if (this.isDisposed()) {
            return;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == AccessibleRole.TOOL_BAR) {
            Node node = (Node)this.getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0]);
            if (node != null) {
                this.getAccessible(node).executeAction(AccessibleAction.FIRE, new Object[0]);
            }
            return;
        }
        if (accessibleRole == AccessibleRole.TREE_TABLE_CELL) {
            Accessible accessible = this.getRow();
            if (accessible != null) {
                accessible.executeAction(AccessibleAction.COLLAPSE, new Object[0]);
            }
            return;
        }
        this.executeAction(AccessibleAction.COLLAPSE, new Object[0]);
    }

    private void Expand() {
        if (this.isDisposed()) {
            return;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == AccessibleRole.TOOL_BAR) {
            Node node = (Node)this.getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0]);
            if (node != null) {
                this.getAccessible(node).executeAction(AccessibleAction.FIRE, new Object[0]);
            }
            return;
        }
        if (accessibleRole == AccessibleRole.TREE_TABLE_CELL) {
            Accessible accessible = this.getRow();
            if (accessible != null) {
                accessible.executeAction(AccessibleAction.EXPAND, new Object[0]);
            }
            return;
        }
        this.executeAction(AccessibleAction.EXPAND, new Object[0]);
    }

    private int get_ExpandCollapseState() {
        Object object;
        if (this.isDisposed()) {
            return 0;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == AccessibleRole.TOOL_BAR && (object = (Node)this.getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0])) != null) {
            boolean bl = Boolean.TRUE.equals(this.getAccessible((Node)object).getAttribute(AccessibleAttribute.VISIBLE, new Object[0]));
            return bl ? 0 : 1;
        }
        if (accessibleRole == AccessibleRole.TREE_TABLE_CELL) {
            object = this.getRow();
            if (object == null) {
                return 3;
            }
            Object object2 = ((Accessible)object).getAttribute(AccessibleAttribute.LEAF, new Object[0]);
            if (Boolean.TRUE.equals(object2)) {
                return 3;
            }
            object2 = ((Accessible)object).getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
            boolean bl = Boolean.TRUE.equals(object2);
            return bl ? 1 : 0;
        }
        object = this.getAttribute(AccessibleAttribute.LEAF, new Object[0]);
        if (Boolean.TRUE.equals(object)) {
            return 3;
        }
        object = this.getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
        boolean bl = Boolean.TRUE.equals(object);
        return bl ? 1 : 0;
    }

    private boolean get_CanMove() {
        return false;
    }

    private boolean get_CanResize() {
        return false;
    }

    private boolean get_CanRotate() {
        return false;
    }

    private void Move(double d, double d2) {
    }

    private void Resize(double d, double d2) {
    }

    private void Rotate(double d) {
    }

    private void Scroll(int n, int n2) {
        Accessible accessible;
        Node node;
        if (this.isDisposed()) {
            return;
        }
        if (this.get_VerticallyScrollable()) {
            node = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
            accessible = this.getAccessible(node);
            if (accessible == null) {
                return;
            }
            switch (n2) {
                case 3: {
                    accessible.executeAction(AccessibleAction.BLOCK_INCREMENT, new Object[0]);
                    break;
                }
                case 4: {
                    accessible.executeAction(AccessibleAction.INCREMENT, new Object[0]);
                    break;
                }
                case 0: {
                    accessible.executeAction(AccessibleAction.BLOCK_DECREMENT, new Object[0]);
                    break;
                }
                case 1: {
                    accessible.executeAction(AccessibleAction.DECREMENT, new Object[0]);
                    break;
                }
            }
        }
        if (this.get_HorizontallyScrollable()) {
            node = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
            accessible = this.getAccessible(node);
            if (accessible == null) {
                return;
            }
            switch (n) {
                case 3: {
                    accessible.executeAction(AccessibleAction.BLOCK_INCREMENT, new Object[0]);
                    break;
                }
                case 4: {
                    accessible.executeAction(AccessibleAction.INCREMENT, new Object[0]);
                    break;
                }
                case 0: {
                    accessible.executeAction(AccessibleAction.BLOCK_DECREMENT, new Object[0]);
                    break;
                }
                case 1: {
                    accessible.executeAction(AccessibleAction.DECREMENT, new Object[0]);
                    break;
                }
            }
        }
    }

    private void SetScrollPercent(double d, double d2) {
        Double d3;
        Double d4;
        Accessible accessible;
        Node node;
        if (this.isDisposed()) {
            return;
        }
        if (d2 != -1.0 && this.get_VerticallyScrollable()) {
            node = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
            accessible = this.getAccessible(node);
            if (accessible == null) {
                return;
            }
            d4 = (Double)accessible.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
            d3 = (Double)accessible.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
            if (d4 != null && d3 != null) {
                accessible.executeAction(AccessibleAction.SET_VALUE, (d3 - d4) * (d2 / 100.0) + d4);
            }
        }
        if (d != -1.0 && this.get_HorizontallyScrollable()) {
            node = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
            accessible = this.getAccessible(node);
            if (accessible == null) {
                return;
            }
            d4 = (Double)accessible.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
            d3 = (Double)accessible.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
            if (d4 != null && d3 != null) {
                accessible.executeAction(AccessibleAction.SET_VALUE, (d3 - d4) * (d / 100.0) + d4);
            }
        }
    }

    private boolean get_HorizontallyScrollable() {
        if (this.isDisposed()) {
            return false;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
        if (node == null) {
            return false;
        }
        Boolean bl = (Boolean)this.getAccessible(node).getAttribute(AccessibleAttribute.VISIBLE, new Object[0]);
        return Boolean.TRUE.equals(bl);
    }

    private double get_HorizontalScrollPercent() {
        if (this.isDisposed()) {
            return 0.0;
        }
        if (!this.get_HorizontallyScrollable()) {
            return -1.0;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
        if (node != null) {
            Accessible accessible = this.getAccessible(node);
            Double d = (Double)accessible.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
            if (d == null) {
                return 0.0;
            }
            Double d2 = (Double)accessible.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
            if (d2 == null) {
                return 0.0;
            }
            Double d3 = (Double)accessible.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
            if (d3 == null) {
                return 0.0;
            }
            return 100.0 * (d - d3) / (d2 - d3);
        }
        return 0.0;
    }

    private double get_HorizontalViewSize() {
        if (this.isDisposed()) {
            return 0.0;
        }
        if (!this.get_HorizontallyScrollable()) {
            return 100.0;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.CONTENTS, new Object[0]);
        if (node == null) {
            return 100.0;
        }
        Bounds bounds = (Bounds)this.getAccessible(node).getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
        if (bounds == null) {
            return 0.0;
        }
        Bounds bounds2 = (Bounds)this.getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
        if (bounds2 == null) {
            return 0.0;
        }
        return bounds2.getWidth() / bounds.getWidth() * 100.0;
    }

    private boolean get_VerticallyScrollable() {
        if (this.isDisposed()) {
            return false;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
        if (node == null) {
            return false;
        }
        Boolean bl = (Boolean)this.getAccessible(node).getAttribute(AccessibleAttribute.VISIBLE, new Object[0]);
        return Boolean.TRUE.equals(bl);
    }

    private double get_VerticalScrollPercent() {
        if (this.isDisposed()) {
            return 0.0;
        }
        if (!this.get_VerticallyScrollable()) {
            return -1.0;
        }
        Node node = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
        if (node != null) {
            Accessible accessible = this.getAccessible(node);
            Double d = (Double)accessible.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
            if (d == null) {
                return 0.0;
            }
            Double d2 = (Double)accessible.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
            if (d2 == null) {
                return 0.0;
            }
            Double d3 = (Double)accessible.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
            if (d3 == null) {
                return 0.0;
            }
            return 100.0 * (d - d3) / (d2 - d3);
        }
        return 0.0;
    }

    private double get_VerticalViewSize() {
        if (this.isDisposed()) {
            return 0.0;
        }
        if (!this.get_VerticallyScrollable()) {
            return 100.0;
        }
        double d = 0.0;
        Bounds bounds = (Bounds)this.getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
        if (bounds == null) {
            return 0.0;
        }
        double d2 = bounds.getHeight();
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == null) {
            return 0.0;
        }
        if (accessibleRole == AccessibleRole.SCROLL_PANE) {
            Node node = (Node)this.getAttribute(AccessibleAttribute.CONTENTS, new Object[0]);
            if (node != null) {
                Bounds bounds2 = (Bounds)this.getAccessible(node).getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
                d = bounds2 == null ? 0.0 : bounds2.getHeight();
            }
        } else {
            Integer n = 0;
            switch (accessibleRole) {
                case LIST_VIEW: {
                    n = (Integer)this.getAttribute(AccessibleAttribute.ITEM_COUNT, new Object[0]);
                    break;
                }
                case TREE_TABLE_VIEW: 
                case TABLE_VIEW: 
                case TREE_VIEW: {
                    n = (Integer)this.getAttribute(AccessibleAttribute.ROW_COUNT, new Object[0]);
                    break;
                }
            }
            d = n == null ? 0.0 : (double)(n * 24);
        }
        return d == 0.0 ? 0.0 : d2 / d * 100.0;
    }

    private void ScrollIntoView() {
        if (this.isDisposed()) {
            return;
        }
        AccessibleRole accessibleRole = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (accessibleRole == null) {
            return;
        }
        Accessible accessible = this.getContainer();
        if (accessible == null) {
            return;
        }
        Node node = null;
        switch (accessibleRole) {
            case LIST_ITEM: {
                Integer n = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (n == null) break;
                node = (Node)accessible.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, n);
                break;
            }
            case TREE_ITEM: {
                Integer n = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (n == null) break;
                node = (Node)accessible.getAttribute(AccessibleAttribute.ROW_AT_INDEX, n);
                break;
            }
            case TABLE_CELL: 
            case TREE_TABLE_CELL: {
                Integer n = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                Integer n2 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
                if (n == null || n2 == null) break;
                node = (Node)accessible.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, n, n2);
                break;
            }
        }
        if (node != null) {
            accessible.executeAction(AccessibleAction.SHOW_ITEM, new Object[]{node});
        }
    }

    static {
        WinAccessible._initIDs();
        idCount = 1;
    }
}

