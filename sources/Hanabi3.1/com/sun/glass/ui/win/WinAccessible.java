package com.sun.glass.ui.win;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.View;
import java.util.function.Function;
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

final class WinAccessible extends Accessible {
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
      } else {
         this.id = idCount++;
      }
   }

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

   public void sendNotification(AccessibleAttribute var1) {
      if (!this.isDisposed()) {
         Boolean var3;
         WinVariant var4;
         Node var14;
         WinVariant var15;
         switch (var1) {
            case FOCUS_NODE:
               if (this.getView() != null) {
                  long var16 = this.GetFocus();
                  if (var16 != 0L) {
                     UiaRaiseAutomationEvent(var16, 20005);
                  }
               } else {
                  var14 = (Node)this.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                  if (var14 != null) {
                     UiaRaiseAutomationEvent(this.getNativeAccessible(var14), 20005);
                  } else {
                     Scene var20 = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]);
                     Accessible var22 = this.getAccessible(var20);
                     if (var22 != null) {
                        var22.sendNotification(AccessibleAttribute.FOCUS_NODE);
                     }
                  }
               }
               break;
            case FOCUS_ITEM:
               var14 = (Node)this.getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
               long var18 = this.getNativeAccessible(var14);
               if (var18 != 0L) {
                  UiaRaiseAutomationEvent(var18, 20005);
               }
               break;
            case INDETERMINATE:
               if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.CHECK_BOX) {
                  this.notifyToggleState();
               }
               break;
            case SELECTED:
               Object var13 = this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
               if (var13 != AccessibleRole.CHECK_BOX && var13 != AccessibleRole.TOGGLE_BUTTON) {
                  var3 = (Boolean)this.getAttribute(AccessibleAttribute.SELECTED, new Object[0]);
                  if (var3 != null) {
                     if (var3) {
                        UiaRaiseAutomationEvent(this.peer, 20012);
                     } else {
                        UiaRaiseAutomationEvent(this.peer, 20011);
                     }
                  }
               } else {
                  this.notifyToggleState();
               }
            case FOCUSED:
            case PARENT:
               break;
            case VALUE:
               Double var12 = (Double)this.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
               if (var12 != null) {
                  var15 = new WinVariant();
                  var15.vt = 5;
                  var15.dblVal = 0.0;
                  var4 = new WinVariant();
                  var4.vt = 5;
                  var4.dblVal = var12;
                  UiaRaiseAutomationPropertyChangedEvent(this.peer, 30047, var15, var4);
               }
               break;
            case SELECTION_START:
            case SELECTION_END:
               if (this.selectionRange != null) {
                  Integer var11 = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_START, new Object[0]);
                  boolean var17 = var11 != null && var11 != this.selectionRange.getStart();
                  Integer var19 = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                  boolean var21 = var19 != null && var19 != this.selectionRange.getEnd();
                  if (var17 || var21) {
                     UiaRaiseAutomationEvent(this.peer, 20014);
                  }
               }
               break;
            case TEXT:
               String var2 = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
               if (var2 != null) {
                  var15 = new WinVariant();
                  var15.vt = 8;
                  var15.bstrVal = "";
                  var4 = new WinVariant();
                  var4.vt = 8;
                  var4.bstrVal = var2;
                  if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.SPINNER) {
                     UiaRaiseAutomationPropertyChangedEvent(this.peer, 30005, var15, var4);
                  } else {
                     UiaRaiseAutomationPropertyChangedEvent(this.peer, 30045, var15, var4);
                  }
               }

               if (this.selectionRange != null || this.documentRange != null) {
                  UiaRaiseAutomationEvent(this.peer, 20015);
               }
               break;
            case EXPANDED:
               var3 = (Boolean)this.getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
               if (var3 != null) {
                  var4 = new WinVariant();
                  var4.vt = 3;
                  var4.lVal = var3 ? 0 : 1;
                  WinVariant var5 = new WinVariant();
                  var5.vt = 3;
                  var5.lVal = var3 ? 1 : 0;
                  if (this.getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.TREE_TABLE_ROW) {
                     Accessible var6 = this.getContainer();
                     Integer var7 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                     if (var6 != null && var7 != null) {
                        Node var8 = (Node)var6.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, var7, 0);
                        if (var8 != null) {
                           long var9 = ((WinAccessible)this.getAccessible(var8)).getNativeAccessible();
                           UiaRaiseAutomationPropertyChangedEvent(var9, 30070, var4, var5);
                        }
                     }
                  } else {
                     UiaRaiseAutomationPropertyChangedEvent(this.peer, 30070, var4, var5);
                  }
               }
               break;
            default:
               UiaRaiseAutomationEvent(this.peer, 20004);
         }

      }
   }

   private void notifyToggleState() {
      int var1 = this.get_ToggleState();
      WinVariant var2 = new WinVariant();
      var2.vt = 3;
      var2.lVal = var1;
      WinVariant var3 = new WinVariant();
      var3.vt = 3;
      var3.lVal = var1;
      UiaRaiseAutomationPropertyChangedEvent(this.peer, 30086, var2, var3);
   }

   protected long getNativeAccessible() {
      return this.peer;
   }

   private Accessible getContainer() {
      if (this.isDisposed()) {
         return null;
      } else {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 != null) {
            switch (var1) {
               case TABLE_ROW:
               case TABLE_CELL:
                  return this.getContainerAccessible(AccessibleRole.TABLE_VIEW);
               case LIST_ITEM:
                  return this.getContainerAccessible(AccessibleRole.LIST_VIEW);
               case TAB_ITEM:
                  return this.getContainerAccessible(AccessibleRole.TAB_PANE);
               case PAGE_ITEM:
                  return this.getContainerAccessible(AccessibleRole.PAGINATION);
               case TREE_ITEM:
                  return this.getContainerAccessible(AccessibleRole.TREE_VIEW);
               case TREE_TABLE_ROW:
               case TREE_TABLE_CELL:
                  return this.getContainerAccessible(AccessibleRole.TREE_TABLE_VIEW);
            }
         }

         return null;
      }
   }

   private int getControlType() {
      AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
      if (var1 == null) {
         return 50026;
      } else {
         switch (var1) {
            case TABLE_CELL:
            case TREE_TABLE_CELL:
               return 50029;
            case LIST_ITEM:
               return 50007;
            case TAB_ITEM:
            case PAGE_ITEM:
               return 50019;
            case TREE_ITEM:
               return 50024;
            case TREE_TABLE_ROW:
            default:
               return 0;
            case CONTEXT_MENU:
               return 50009;
            case RADIO_MENU_ITEM:
            case CHECK_MENU_ITEM:
            case MENU:
            case MENU_ITEM:
               return 50011;
            case BUTTON:
            case MENU_BUTTON:
            case TOGGLE_BUTTON:
            case INCREMENT_BUTTON:
            case DECREMENT_BUTTON:
               return 50000;
            case SPLIT_MENU_BUTTON:
               return 50031;
            case PAGINATION:
            case TAB_PANE:
               return 50018;
            case SLIDER:
               return 50015;
            case PARENT:
               return this.getView() != null ? '썰' : '썱';
            case TEXT:
               return 50020;
            case TEXT_FIELD:
            case PASSWORD_FIELD:
            case TEXT_AREA:
               return 50004;
            case TREE_TABLE_VIEW:
            case TABLE_VIEW:
               return 50036;
            case LIST_VIEW:
               return 50008;
            case IMAGE_VIEW:
               return 50006;
            case RADIO_BUTTON:
               return 50013;
            case CHECK_BOX:
               return 50002;
            case COMBO_BOX:
               return 50003;
            case HYPERLINK:
               return 50005;
            case TREE_VIEW:
               return 50023;
            case PROGRESS_INDICATOR:
               return 50012;
            case TOOL_BAR:
               return 50021;
            case TITLED_PANE:
               return 50026;
            case SCROLL_PANE:
               return 50033;
            case SCROLL_BAR:
               return 50014;
            case THUMB:
               return 50027;
            case MENU_BAR:
               return 50010;
            case DATE_PICKER:
               return 50033;
            case SPINNER:
               return 50016;
         }
      }
   }

   private Accessible getRow() {
      Integer var1 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
      if (var1 == null) {
         return null;
      } else if (var1 != 0) {
         return null;
      } else {
         Integer var2 = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
         if (var2 == null) {
            return null;
         } else {
            Accessible var3 = this.getContainer();
            if (var3 == null) {
               return null;
            } else {
               Node var4 = (Node)var3.getAttribute(AccessibleAttribute.ROW_AT_INDEX, var2);
               return this.getAccessible(var4);
            }
         }
      }
   }

   private void changeSelection(boolean var1, boolean var2) {
      AccessibleRole var3 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
      if (var3 != null) {
         Accessible var4 = this.getContainer();
         if (var4 != null) {
            Node var5 = null;
            Integer var6;
            switch (var3) {
               case TABLE_CELL:
               case TREE_TABLE_CELL:
                  var6 = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                  Integer var7 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
                  if (var6 != null && var7 != null) {
                     var5 = (Node)var4.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, var6, var7);
                  }
                  break;
               case LIST_ITEM:
                  var6 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                  if (var6 != null) {
                     var5 = (Node)var4.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, var6);
                  }
               case TAB_ITEM:
               case PAGE_ITEM:
               case TREE_TABLE_ROW:
               default:
                  break;
               case TREE_ITEM:
                  var6 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                  if (var6 != null) {
                     var5 = (Node)var4.getAttribute(AccessibleAttribute.ROW_AT_INDEX, var6);
                  }
            }

            if (var5 != null) {
               ObservableList var8 = FXCollections.observableArrayList();
               if (!var2) {
                  ObservableList var9 = (ObservableList)var4.getAttribute(AccessibleAttribute.SELECTED_ITEMS);
                  if (var9 != null) {
                     var8.addAll(var9);
                  }
               }

               if (var1) {
                  var8.add(var5);
               } else {
                  var8.remove(var5);
               }

               var4.executeAction(AccessibleAction.SET_SELECTED_ITEMS, var8);
            }

         }
      }
   }

   private long GetPatternProvider(int var1) {
      if (this.isDisposed()) {
         return 0L;
      } else {
         AccessibleRole var2 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         boolean var3 = false;
         switch (var2) {
            case TABLE_CELL:
               var3 = var1 == 10010 || var1 == 10007 || var1 == 10013 || var1 == 10017;
               break;
            case LIST_ITEM:
               var3 = var1 == 10010 || var1 == 10017;
               break;
            case TAB_ITEM:
            case PAGE_ITEM:
               var3 = var1 == 10010;
               break;
            case TREE_ITEM:
               var3 = var1 == 10010 || var1 == 10005 || var1 == 10017;
            case TREE_TABLE_ROW:
            case CONTEXT_MENU:
            case PARENT:
            case TEXT:
            case PASSWORD_FIELD:
            case IMAGE_VIEW:
            default:
               break;
            case TREE_TABLE_CELL:
               var3 = var1 == 10010 || var1 == 10007 || var1 == 10013 || var1 == 10005 || var1 == 10017;
               break;
            case RADIO_MENU_ITEM:
            case CHECK_MENU_ITEM:
               var3 = var1 == 10000 || var1 == 10015;
               break;
            case MENU:
            case SPLIT_MENU_BUTTON:
               var3 = var1 == 10000 || var1 == 10005;
               break;
            case MENU_ITEM:
            case BUTTON:
            case MENU_BUTTON:
            case INCREMENT_BUTTON:
            case DECREMENT_BUTTON:
            case HYPERLINK:
               var3 = var1 == 10000;
               break;
            case TOGGLE_BUTTON:
            case CHECK_BOX:
               var3 = var1 == 10015;
               break;
            case PAGINATION:
            case TAB_PANE:
               var3 = var1 == 10001;
               break;
            case SLIDER:
            case PROGRESS_INDICATOR:
            case SCROLL_BAR:
               var3 = var1 == 10003;
               break;
            case TEXT_FIELD:
            case TEXT_AREA:
               var3 = var1 == 10014 || var1 == 10002;
               break;
            case TREE_TABLE_VIEW:
            case TABLE_VIEW:
               var3 = var1 == 10001 || var1 == 10006 || var1 == 10012 || var1 == 10004;
               break;
            case LIST_VIEW:
               var3 = var1 == 10001 || var1 == 10004;
               break;
            case RADIO_BUTTON:
               var3 = var1 == 10010;
               break;
            case COMBO_BOX:
               var3 = var1 == 10005 || var1 == 10002;
               break;
            case TREE_VIEW:
               var3 = var1 == 10001 || var1 == 10004;
               break;
            case TOOL_BAR:
            case TITLED_PANE:
               var3 = var1 == 10005;
               break;
            case SCROLL_PANE:
               var3 = var1 == 10004;
         }

         return var3 ? this.getNativeAccessible() : 0L;
      }
   }

   private long get_HostRawElementProvider() {
      if (this.isDisposed()) {
         return 0L;
      } else {
         View var1 = this.getView();
         return var1 != null ? var1.getNativeView() : 0L;
      }
   }

   private WinVariant GetPropertyValue(int var1) {
      if (this.isDisposed()) {
         return null;
      } else {
         WinVariant var2 = null;
         String var8;
         Boolean var9;
         AccessibleRole var10;
         switch (var1) {
            case 30003:
               int var13 = this.getControlType();
               if (var13 != 0) {
                  var2 = new WinVariant();
                  var2.vt = 3;
                  var2.lVal = var13;
               }
               break;
            case 30004:
               var8 = (String)this.getAttribute(AccessibleAttribute.ROLE_DESCRIPTION, new Object[0]);
               if (var8 == null) {
                  var10 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                  if (var10 == null) {
                     var10 = AccessibleRole.NODE;
                  }

                  switch (var10) {
                     case PAGE_ITEM:
                        var8 = "page";
                        break;
                     case TITLED_PANE:
                        var8 = "title pane";
                  }
               }

               if (var8 != null) {
                  var2 = new WinVariant();
                  var2.vt = 8;
                  var2.bstrVal = var8;
               }
               break;
            case 30005:
               var10 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
               if (var10 == null) {
                  var10 = AccessibleRole.NODE;
               }

               switch (var10) {
                  case INCREMENT_BUTTON:
                  case DECREMENT_BUTTON:
                     var8 = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                     if (var8 == null || var8.length() == 0) {
                        if (var10 == AccessibleRole.INCREMENT_BUTTON) {
                           var8 = "increment";
                        } else {
                           var8 = "decrement";
                        }
                     }
                     break;
                  case TEXT_FIELD:
                  case TEXT_AREA:
                  case COMBO_BOX:
                     var8 = null;
                     break;
                  default:
                     var8 = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
               }

               if (var8 == null || var8.length() == 0) {
                  Node var12 = (Node)this.getAttribute(AccessibleAttribute.LABELED_BY, new Object[0]);
                  if (var12 != null) {
                     var8 = (String)this.getAccessible(var12).getAttribute(AccessibleAttribute.TEXT);
                  }
               }

               if (var8 != null && var8.length() != 0) {
                  var2 = new WinVariant();
                  var2.vt = 8;
                  var2.bstrVal = var8;
               }
               break;
            case 30006:
               KeyCombination var11 = (KeyCombination)this.getAttribute(AccessibleAttribute.ACCELERATOR, new Object[0]);
               if (var11 != null) {
                  var2 = new WinVariant();
                  var2.vt = 8;
                  var2.bstrVal = var11.toString().replaceAll("Shortcut", "Ctrl");
               }
               break;
            case 30007:
               var8 = (String)this.getAttribute(AccessibleAttribute.MNEMONIC, new Object[0]);
               if (var8 != null) {
                  var2 = new WinVariant();
                  var2.vt = 8;
                  var2.bstrVal = "Alt " + var8.toLowerCase();
               }
               break;
            case 30008:
               var9 = (Boolean)this.getAttribute(AccessibleAttribute.FOCUSED, new Object[0]);
               if (Boolean.FALSE.equals(var9)) {
                  Scene var4 = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]);
                  if (var4 != null) {
                     Accessible var5 = this.getAccessible(var4);
                     if (var5 != null) {
                        Node var6 = (Node)var5.getAttribute(AccessibleAttribute.FOCUS_NODE);
                        if (var6 != null) {
                           Node var7 = (Node)this.getAccessible(var6).getAttribute(AccessibleAttribute.FOCUS_ITEM);
                           if (this.getNativeAccessible(var7) == this.peer) {
                              var9 = true;
                           }
                        }
                     }
                  }
               }

               var2 = new WinVariant();
               var2.vt = 11;
               var2.boolVal = var9 != null ? var9 : false;
               break;
            case 30009:
               var2 = new WinVariant();
               var2.vt = 11;
               var2.boolVal = true;
               break;
            case 30010:
               var9 = (Boolean)this.getAttribute(AccessibleAttribute.DISABLED, new Object[0]);
               var2 = new WinVariant();
               var2.vt = 11;
               var2.boolVal = var9 != null ? !var9 : true;
               break;
            case 30011:
               var2 = new WinVariant();
               var2.vt = 8;
               var2.bstrVal = "JavaFX" + this.id;
               break;
            case 30013:
               var8 = (String)this.getAttribute(AccessibleAttribute.HELP, new Object[0]);
               if (var8 != null) {
                  var2 = new WinVariant();
                  var2.vt = 8;
                  var2.bstrVal = var8;
               }
               break;
            case 30016:
            case 30017:
               var2 = new WinVariant();
               var2.vt = 11;
               var2.boolVal = this.getView() != null || !this.isIgnored();
               break;
            case 30019:
               AccessibleRole var3 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
               var2 = new WinVariant();
               var2.vt = 11;
               var2.boolVal = var3 == AccessibleRole.PASSWORD_FIELD;
               break;
            case 30107:
               var2 = new WinVariant();
               var2.vt = 8;
               var2.bstrVal = "JavaFXProvider";
         }

         return var2;
      }
   }

   private float[] get_BoundingRectangle() {
      if (this.isDisposed()) {
         return null;
      } else if (this.getView() != null) {
         return null;
      } else {
         Bounds var1 = (Bounds)this.getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
         return var1 != null ? new float[]{(float)var1.getMinX(), (float)var1.getMinY(), (float)var1.getWidth(), (float)var1.getHeight()} : null;
      }
   }

   private long get_FragmentRoot() {
      if (this.isDisposed()) {
         return 0L;
      } else {
         Scene var1 = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]);
         if (var1 == null) {
            return 0L;
         } else {
            WinAccessible var2 = (WinAccessible)this.getAccessible(var1);
            return var2 != null && !var2.isDisposed() ? var2.getNativeAccessible() : 0L;
         }
      }
   }

   private long[] GetEmbeddedFragmentRoots() {
      return this.isDisposed() ? null : null;
   }

   private int[] GetRuntimeId() {
      if (this.isDisposed()) {
         return null;
      } else {
         return this.getView() != null ? null : new int[]{3, this.id};
      }
   }

   private long NavigateListView(WinAccessible var1, int var2) {
      Accessible var3 = var1.getContainer();
      if (var3 == null) {
         return 0L;
      } else {
         Integer var4 = (Integer)var3.getAttribute(AccessibleAttribute.ITEM_COUNT);
         if (var4 != null && var4 != 0) {
            Integer var5 = (Integer)var1.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
            if (var5 == null) {
               return 0L;
            } else if (0 <= var5 && var5 < var4) {
               switch (var2) {
                  case 1:
                     var5 = var5 + 1;
                     break;
                  case 2:
                     var5 = var5 - 1;
                     break;
                  case 3:
                     var5 = 0;
                     break;
                  case 4:
                     var5 = var4 - 1;
               }

               if (0 <= var5 && var5 < var4) {
                  Node var6 = (Node)var3.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, var5);
                  return this.getNativeAccessible(var6);
               } else {
                  return 0L;
               }
            } else {
               return 0L;
            }
         } else {
            return 0L;
         }
      }
   }

   private long Navigate(int var1) {
      if (this.isDisposed()) {
         return 0L;
      } else {
         AccessibleRole var2 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         boolean var3 = var2 == AccessibleRole.TREE_ITEM;
         Node var4 = null;
         WinAccessible var6;
         switch (var1) {
            case 0:
               if (this.getView() != null) {
                  return 0L;
               }

               if (var3) {
                  var4 = (Node)this.getAttribute(AccessibleAttribute.TREE_ITEM_PARENT, new Object[0]);
                  if (var4 == null) {
                     WinAccessible var14 = (WinAccessible)this.getContainer();
                     return var14 != null ? var14.getNativeAccessible() : 0L;
                  }
               } else {
                  var4 = (Node)this.getAttribute(AccessibleAttribute.PARENT, new Object[0]);
                  if (var4 == null) {
                     Scene var15 = (Scene)this.getAttribute(AccessibleAttribute.SCENE, new Object[0]);
                     var6 = (WinAccessible)this.getAccessible(var15);
                     if (var6 != null && var6 != this && !var6.isDisposed()) {
                        return var6.getNativeAccessible();
                     }

                     return 0L;
                  }
               }
               break;
            case 1:
            case 2:
               if (var2 == AccessibleRole.LIST_ITEM) {
                  return this.NavigateListView(this, var1);
               }

               Node var13 = (Node)this.getAttribute(var3 ? AccessibleAttribute.TREE_ITEM_PARENT : AccessibleAttribute.PARENT, new Object[0]);
               if (var13 != null) {
                  var6 = (WinAccessible)this.getAccessible(var13);
                  boolean var8 = false;
                  Function var7;
                  int var16;
                  if (var3) {
                     Integer var9 = (Integer)var6.getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                     if (var9 == null) {
                        return 0L;
                     }

                     var16 = var9;
                     var7 = (var1x) -> {
                        return (Node)var6.getAttribute(AccessibleAttribute.TREE_ITEM_AT_INDEX, new Object[]{var1x});
                     };
                  } else {
                     ObservableList var17 = (ObservableList)var6.getAttribute(AccessibleAttribute.CHILDREN, new Object[0]);
                     if (var17 == null) {
                        return 0L;
                     }

                     var16 = var17.size();
                     var7 = (var1x) -> {
                        return (Node)var17.get(var1x);
                     };
                  }

                  int var18 = var6.lastIndex;
                  int var10 = -1;
                  if (0 <= var18 && var18 < var16 && this.getNativeAccessible((Node)var7.apply(var18)) == this.peer) {
                     var10 = var18;
                  } else {
                     for(int var11 = 0; var11 < var16; ++var11) {
                        if (this.getNativeAccessible((Node)var7.apply(var11)) == this.peer) {
                           var10 = var11;
                           break;
                        }
                     }
                  }

                  if (var10 != -1) {
                     if (var1 == 1) {
                        ++var10;
                     } else {
                        --var10;
                     }

                     if (0 <= var10 && var10 < var16) {
                        var4 = (Node)var7.apply(var10);
                        var6.lastIndex = var10;
                     }
                  }
               }
               break;
            case 3:
            case 4:
               this.lastIndex = -1;
               if (var2 == AccessibleRole.LIST_VIEW) {
                  this.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, new Object[]{0});
               }

               if (var2 == AccessibleRole.TREE_VIEW) {
                  this.lastIndex = 0;
                  var4 = (Node)this.getAttribute(AccessibleAttribute.ROW_AT_INDEX, new Object[]{0});
               } else if (var3) {
                  Integer var5 = (Integer)this.getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                  if (var5 != null && var5 > 0) {
                     this.lastIndex = var1 == 3 ? 0 : var5 - 1;
                     var4 = (Node)this.getAttribute(AccessibleAttribute.TREE_ITEM_AT_INDEX, new Object[]{this.lastIndex});
                  }
               } else {
                  ObservableList var12 = (ObservableList)this.getAttribute(AccessibleAttribute.CHILDREN, new Object[0]);
                  if (var12 != null && var12.size() > 0) {
                     this.lastIndex = var1 == 3 ? 0 : var12.size() - 1;
                     var4 = (Node)var12.get(this.lastIndex);
                  }

                  if (var4 != null) {
                     var2 = (AccessibleRole)this.getAccessible(var4).getAttribute(AccessibleAttribute.ROLE);
                     if (var2 == AccessibleRole.LIST_ITEM) {
                        var6 = (WinAccessible)this.getAccessible(var4);
                        return this.NavigateListView(var6, var1);
                     }
                  }
               }
         }

         return this.getNativeAccessible(var4);
      }
   }

   private void SetFocus() {
      if (!this.isDisposed()) {
         this.executeAction(AccessibleAction.REQUEST_FOCUS, new Object[0]);
      }
   }

   private long ElementProviderFromPoint(double var1, double var3) {
      if (this.isDisposed()) {
         return 0L;
      } else {
         Node var5 = (Node)this.getAttribute(AccessibleAttribute.NODE_AT_POINT, new Object[]{new Point2D(var1, var3)});
         return this.getNativeAccessible(var5);
      }
   }

   private long GetFocus() {
      if (this.isDisposed()) {
         return 0L;
      } else {
         Node var1 = (Node)this.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
         if (var1 == null) {
            return 0L;
         } else {
            Node var2 = (Node)this.getAccessible(var1).getAttribute(AccessibleAttribute.FOCUS_ITEM);
            return var2 != null ? this.getNativeAccessible(var2) : this.getNativeAccessible(var1);
         }
      }
   }

   private void AdviseEventAdded(int var1, long var2) {
   }

   private void AdviseEventRemoved(int var1, long var2) {
   }

   private void Invoke() {
      if (!this.isDisposed()) {
         this.executeAction(AccessibleAction.FIRE, new Object[0]);
      }
   }

   private long[] GetSelection() {
      if (this.isDisposed()) {
         return null;
      } else {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 == null) {
            return null;
         } else {
            switch (var1) {
               case PAGINATION:
               case TAB_PANE:
                  Node var8 = (Node)this.getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
                  if (var8 != null) {
                     return new long[]{this.getNativeAccessible(var8)};
                  }
               case SLIDER:
               case PARENT:
               case TEXT:
               case PASSWORD_FIELD:
               case IMAGE_VIEW:
               case RADIO_BUTTON:
               case CHECK_BOX:
               case COMBO_BOX:
               case HYPERLINK:
               default:
                  break;
               case TEXT_FIELD:
               case TEXT_AREA:
                  if (this.selectionRange == null) {
                     this.selectionRange = new WinTextRangeProvider(this);
                  }

                  Integer var7 = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_START, new Object[0]);
                  int var3 = var7 != null ? var7 : 0;
                  int var4 = -1;
                  int var5 = -1;
                  if (var3 >= 0) {
                     var7 = (Integer)this.getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                     var4 = var7 != null ? var7 : 0;
                     if (var4 >= var3) {
                        String var6 = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                        var5 = var6 != null ? var6.length() : 0;
                     }
                  }

                  if (var5 != -1 && var4 <= var5) {
                     this.selectionRange.setRange(var3, var4);
                  } else {
                     this.selectionRange.setRange(0, 0);
                  }

                  return new long[]{this.selectionRange.getNativeProvider()};
               case TREE_TABLE_VIEW:
               case TABLE_VIEW:
               case LIST_VIEW:
               case TREE_VIEW:
                  ObservableList var2 = (ObservableList)this.getAttribute(AccessibleAttribute.SELECTED_ITEMS, new Object[0]);
                  if (var2 != null) {
                     return var2.stream().mapToLong((var1x) -> {
                        return this.getNativeAccessible(var1x);
                     }).toArray();
                  }
            }

            return null;
         }
      }
   }

   private boolean get_CanSelectMultiple() {
      if (this.isDisposed()) {
         return false;
      } else {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 != null) {
            switch (var1) {
               case TREE_TABLE_VIEW:
               case TABLE_VIEW:
               case LIST_VIEW:
               case TREE_VIEW:
                  return Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.MULTIPLE_SELECTION, new Object[0]));
               case IMAGE_VIEW:
               case RADIO_BUTTON:
               case CHECK_BOX:
               case COMBO_BOX:
               case HYPERLINK:
            }
         }

         return false;
      }
   }

   private boolean get_IsSelectionRequired() {
      return !this.isDisposed();
   }

   private void SetValue(double var1) {
      if (!this.isDisposed()) {
         AccessibleRole var3 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var3 != null) {
            switch (var3) {
               case SLIDER:
               case SCROLL_BAR:
                  this.executeAction(AccessibleAction.SET_VALUE, new Object[]{var1});
            }
         }

      }
   }

   private double get_Value() {
      if (this.isDisposed()) {
         return 0.0;
      } else if (Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0]))) {
         return 0.0;
      } else {
         Double var1 = (Double)this.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
         return var1 != null ? var1 : 0.0;
      }
   }

   private boolean get_IsReadOnly() {
      if (this.isDisposed()) {
         return false;
      } else {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 != null) {
            switch (var1) {
               case SLIDER:
                  return false;
               case TEXT_FIELD:
               case TEXT_AREA:
               case COMBO_BOX:
                  return Boolean.FALSE.equals(this.getAttribute(AccessibleAttribute.EDITABLE, new Object[0]));
               case SCROLL_BAR:
                  return true;
            }
         }

         return true;
      }
   }

   private double get_Maximum() {
      if (this.isDisposed()) {
         return 0.0;
      } else {
         Double var1 = (Double)this.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
         return var1 != null ? var1 : 0.0;
      }
   }

   private double get_Minimum() {
      if (this.isDisposed()) {
         return 0.0;
      } else {
         Double var1 = (Double)this.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
         return var1 != null ? var1 : 0.0;
      }
   }

   private double get_LargeChange() {
      return this.isDisposed() ? 0.0 : 10.0;
   }

   private double get_SmallChange() {
      return this.isDisposed() ? 0.0 : 3.0;
   }

   private void SetValueString(String var1) {
      if (!this.isDisposed()) {
         AccessibleRole var2 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var2 != null) {
            switch (var2) {
               case TEXT_FIELD:
               case TEXT_AREA:
                  this.executeAction(AccessibleAction.SET_TEXT, new Object[]{var1});
            }
         }

      }
   }

   private String get_ValueString() {
      return this.isDisposed() ? null : (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
   }

   private void Select() {
      if (!this.isDisposed()) {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 != null) {
            switch (var1) {
               case TABLE_CELL:
               case LIST_ITEM:
               case TREE_ITEM:
               case TREE_TABLE_CELL:
                  this.changeSelection(true, true);
                  break;
               case TAB_ITEM:
               case PAGE_ITEM:
                  this.executeAction(AccessibleAction.REQUEST_FOCUS, new Object[0]);
               case TREE_TABLE_ROW:
               case CONTEXT_MENU:
               case RADIO_MENU_ITEM:
               case CHECK_MENU_ITEM:
               case MENU:
               case MENU_ITEM:
               case MENU_BUTTON:
               case SPLIT_MENU_BUTTON:
               case PAGINATION:
               case TAB_PANE:
               case SLIDER:
               case PARENT:
               case TEXT:
               case TEXT_FIELD:
               case PASSWORD_FIELD:
               case TEXT_AREA:
               case TREE_TABLE_VIEW:
               case TABLE_VIEW:
               case LIST_VIEW:
               case IMAGE_VIEW:
               default:
                  break;
               case BUTTON:
               case TOGGLE_BUTTON:
               case INCREMENT_BUTTON:
               case DECREMENT_BUTTON:
               case RADIO_BUTTON:
                  this.executeAction(AccessibleAction.FIRE, new Object[0]);
            }
         }

      }
   }

   private void AddToSelection() {
      if (!this.isDisposed()) {
         this.changeSelection(true, false);
      }
   }

   private void RemoveFromSelection() {
      if (!this.isDisposed()) {
         this.changeSelection(false, false);
      }
   }

   private boolean get_IsSelected() {
      return this.isDisposed() ? false : Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.SELECTED, new Object[0]));
   }

   private long get_SelectionContainer() {
      if (this.isDisposed()) {
         return 0L;
      } else {
         WinAccessible var1 = (WinAccessible)this.getContainer();
         return var1 != null ? var1.getNativeAccessible() : 0L;
      }
   }

   private long[] GetVisibleRanges() {
      return this.isDisposed() ? null : new long[]{this.get_DocumentRange()};
   }

   private long RangeFromChild(long var1) {
      return this.isDisposed() ? 0L : 0L;
   }

   private long RangeFromPoint(double var1, double var3) {
      if (this.isDisposed()) {
         return 0L;
      } else {
         Integer var5 = (Integer)this.getAttribute(AccessibleAttribute.OFFSET_AT_POINT, new Object[]{new Point2D(var1, var3)});
         if (var5 != null) {
            WinTextRangeProvider var6 = new WinTextRangeProvider(this);
            var6.setRange(var5, var5);
            return var6.getNativeProvider();
         } else {
            return 0L;
         }
      }
   }

   private long get_DocumentRange() {
      if (this.isDisposed()) {
         return 0L;
      } else {
         if (this.documentRange == null) {
            this.documentRange = new WinTextRangeProvider(this);
         }

         String var1 = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
         this.documentRange.setRange(0, var1.length());
         return this.documentRange.getNativeProvider();
      }
   }

   private int get_SupportedTextSelection() {
      return this.isDisposed() ? 0 : 1;
   }

   private int get_ColumnCount() {
      if (this.isDisposed()) {
         return 0;
      } else {
         Integer var1 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_COUNT, new Object[0]);
         return var1 != null ? var1 : 1;
      }
   }

   private int get_RowCount() {
      if (this.isDisposed()) {
         return 0;
      } else {
         Integer var1 = (Integer)this.getAttribute(AccessibleAttribute.ROW_COUNT, new Object[0]);
         return var1 != null ? var1 : 0;
      }
   }

   private long GetItem(int var1, int var2) {
      if (this.isDisposed()) {
         return 0L;
      } else {
         Node var3 = (Node)this.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, new Object[]{var1, var2});
         return this.getNativeAccessible(var3);
      }
   }

   private int get_Column() {
      if (this.isDisposed()) {
         return 0;
      } else {
         Integer var1 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
         return var1 != null ? var1 : 0;
      }
   }

   private int get_ColumnSpan() {
      return this.isDisposed() ? 0 : 1;
   }

   private long get_ContainingGrid() {
      if (this.isDisposed()) {
         return 0L;
      } else {
         WinAccessible var1 = (WinAccessible)this.getContainer();
         return var1 != null ? var1.getNativeAccessible() : 0L;
      }
   }

   private int get_Row() {
      if (this.isDisposed()) {
         return 0;
      } else {
         Integer var1 = null;
         AccessibleRole var2 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var2 != null) {
            switch (var2) {
               case TABLE_ROW:
               case LIST_ITEM:
               case TREE_TABLE_ROW:
                  var1 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                  break;
               case TABLE_CELL:
               case TREE_TABLE_CELL:
                  var1 = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
               case TAB_ITEM:
               case PAGE_ITEM:
               case TREE_ITEM:
            }
         }

         return var1 != null ? var1 : 0;
      }
   }

   private int get_RowSpan() {
      return this.isDisposed() ? 0 : 1;
   }

   private long[] GetColumnHeaders() {
      return this.isDisposed() ? null : null;
   }

   private long[] GetRowHeaders() {
      return this.isDisposed() ? null : null;
   }

   private int get_RowOrColumnMajor() {
      return this.isDisposed() ? 0 : 0;
   }

   private long[] GetColumnHeaderItems() {
      if (this.isDisposed()) {
         return null;
      } else {
         Integer var1 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
         if (var1 == null) {
            return null;
         } else {
            Accessible var2 = this.getContainer();
            if (var2 == null) {
               return null;
            } else {
               Node var3 = (Node)var2.getAttribute(AccessibleAttribute.COLUMN_AT_INDEX, var1);
               return var3 == null ? null : new long[]{this.getNativeAccessible(var3)};
            }
         }
      }
   }

   private long[] GetRowHeaderItems() {
      return this.isDisposed() ? null : null;
   }

   private void Toggle() {
      if (!this.isDisposed()) {
         this.executeAction(AccessibleAction.FIRE, new Object[0]);
      }
   }

   private int get_ToggleState() {
      if (this.isDisposed()) {
         return 0;
      } else if (Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0]))) {
         return 2;
      } else {
         boolean var1 = Boolean.TRUE.equals(this.getAttribute(AccessibleAttribute.SELECTED, new Object[0]));
         return var1 ? 1 : 0;
      }
   }

   private void Collapse() {
      if (!this.isDisposed()) {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 == AccessibleRole.TOOL_BAR) {
            Node var3 = (Node)this.getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0]);
            if (var3 != null) {
               this.getAccessible(var3).executeAction(AccessibleAction.FIRE);
            }

         } else if (var1 == AccessibleRole.TREE_TABLE_CELL) {
            Accessible var2 = this.getRow();
            if (var2 != null) {
               var2.executeAction(AccessibleAction.COLLAPSE);
            }

         } else {
            this.executeAction(AccessibleAction.COLLAPSE, new Object[0]);
         }
      }
   }

   private void Expand() {
      if (!this.isDisposed()) {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 == AccessibleRole.TOOL_BAR) {
            Node var3 = (Node)this.getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0]);
            if (var3 != null) {
               this.getAccessible(var3).executeAction(AccessibleAction.FIRE);
            }

         } else if (var1 == AccessibleRole.TREE_TABLE_CELL) {
            Accessible var2 = this.getRow();
            if (var2 != null) {
               var2.executeAction(AccessibleAction.EXPAND);
            }

         } else {
            this.executeAction(AccessibleAction.EXPAND, new Object[0]);
         }
      }
   }

   private int get_ExpandCollapseState() {
      if (this.isDisposed()) {
         return 0;
      } else {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         boolean var3;
         if (var1 == AccessibleRole.TOOL_BAR) {
            Node var2 = (Node)this.getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0]);
            if (var2 != null) {
               var3 = Boolean.TRUE.equals(this.getAccessible(var2).getAttribute(AccessibleAttribute.VISIBLE));
               return var3 ? 0 : 1;
            }
         }

         if (var1 == AccessibleRole.TREE_TABLE_CELL) {
            Accessible var6 = this.getRow();
            if (var6 == null) {
               return 3;
            } else {
               Object var7 = var6.getAttribute(AccessibleAttribute.LEAF);
               if (Boolean.TRUE.equals(var7)) {
                  return 3;
               } else {
                  var7 = var6.getAttribute(AccessibleAttribute.EXPANDED);
                  boolean var4 = Boolean.TRUE.equals(var7);
                  return var4 ? 1 : 0;
               }
            }
         } else {
            Object var5 = this.getAttribute(AccessibleAttribute.LEAF, new Object[0]);
            if (Boolean.TRUE.equals(var5)) {
               return 3;
            } else {
               var5 = this.getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
               var3 = Boolean.TRUE.equals(var5);
               return var3 ? 1 : 0;
            }
         }
      }
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

   private void Move(double var1, double var3) {
   }

   private void Resize(double var1, double var3) {
   }

   private void Rotate(double var1) {
   }

   private void Scroll(int var1, int var2) {
      if (!this.isDisposed()) {
         Node var3;
         Accessible var4;
         if (this.get_VerticallyScrollable()) {
            var3 = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
            var4 = this.getAccessible(var3);
            if (var4 == null) {
               return;
            }

            switch (var2) {
               case 0:
                  var4.executeAction(AccessibleAction.BLOCK_DECREMENT);
                  break;
               case 1:
                  var4.executeAction(AccessibleAction.DECREMENT);
               case 2:
               default:
                  break;
               case 3:
                  var4.executeAction(AccessibleAction.BLOCK_INCREMENT);
                  break;
               case 4:
                  var4.executeAction(AccessibleAction.INCREMENT);
            }
         }

         if (this.get_HorizontallyScrollable()) {
            var3 = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
            var4 = this.getAccessible(var3);
            if (var4 == null) {
               return;
            }

            switch (var1) {
               case 0:
                  var4.executeAction(AccessibleAction.BLOCK_DECREMENT);
                  break;
               case 1:
                  var4.executeAction(AccessibleAction.DECREMENT);
               case 2:
               default:
                  break;
               case 3:
                  var4.executeAction(AccessibleAction.BLOCK_INCREMENT);
                  break;
               case 4:
                  var4.executeAction(AccessibleAction.INCREMENT);
            }
         }

      }
   }

   private void SetScrollPercent(double var1, double var3) {
      if (!this.isDisposed()) {
         Node var5;
         Accessible var6;
         Double var7;
         Double var8;
         if (var3 != -1.0 && this.get_VerticallyScrollable()) {
            var5 = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
            var6 = this.getAccessible(var5);
            if (var6 == null) {
               return;
            }

            var7 = (Double)var6.getAttribute(AccessibleAttribute.MIN_VALUE);
            var8 = (Double)var6.getAttribute(AccessibleAttribute.MAX_VALUE);
            if (var7 != null && var8 != null) {
               var6.executeAction(AccessibleAction.SET_VALUE, (var8 - var7) * (var3 / 100.0) + var7);
            }
         }

         if (var1 != -1.0 && this.get_HorizontallyScrollable()) {
            var5 = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
            var6 = this.getAccessible(var5);
            if (var6 == null) {
               return;
            }

            var7 = (Double)var6.getAttribute(AccessibleAttribute.MIN_VALUE);
            var8 = (Double)var6.getAttribute(AccessibleAttribute.MAX_VALUE);
            if (var7 != null && var8 != null) {
               var6.executeAction(AccessibleAction.SET_VALUE, (var8 - var7) * (var1 / 100.0) + var7);
            }
         }

      }
   }

   private boolean get_HorizontallyScrollable() {
      if (this.isDisposed()) {
         return false;
      } else {
         Node var1 = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
         if (var1 == null) {
            return false;
         } else {
            Boolean var2 = (Boolean)this.getAccessible(var1).getAttribute(AccessibleAttribute.VISIBLE);
            return Boolean.TRUE.equals(var2);
         }
      }
   }

   private double get_HorizontalScrollPercent() {
      if (this.isDisposed()) {
         return 0.0;
      } else if (!this.get_HorizontallyScrollable()) {
         return -1.0;
      } else {
         Node var1 = (Node)this.getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
         if (var1 != null) {
            Accessible var2 = this.getAccessible(var1);
            Double var3 = (Double)var2.getAttribute(AccessibleAttribute.VALUE);
            if (var3 == null) {
               return 0.0;
            } else {
               Double var4 = (Double)var2.getAttribute(AccessibleAttribute.MAX_VALUE);
               if (var4 == null) {
                  return 0.0;
               } else {
                  Double var5 = (Double)var2.getAttribute(AccessibleAttribute.MIN_VALUE);
                  return var5 == null ? 0.0 : 100.0 * (var3 - var5) / (var4 - var5);
               }
            }
         } else {
            return 0.0;
         }
      }
   }

   private double get_HorizontalViewSize() {
      if (this.isDisposed()) {
         return 0.0;
      } else if (!this.get_HorizontallyScrollable()) {
         return 100.0;
      } else {
         Node var1 = (Node)this.getAttribute(AccessibleAttribute.CONTENTS, new Object[0]);
         if (var1 == null) {
            return 100.0;
         } else {
            Bounds var2 = (Bounds)this.getAccessible(var1).getAttribute(AccessibleAttribute.BOUNDS);
            if (var2 == null) {
               return 0.0;
            } else {
               Bounds var3 = (Bounds)this.getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
               return var3 == null ? 0.0 : var3.getWidth() / var2.getWidth() * 100.0;
            }
         }
      }
   }

   private boolean get_VerticallyScrollable() {
      if (this.isDisposed()) {
         return false;
      } else {
         Node var1 = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
         if (var1 == null) {
            return false;
         } else {
            Boolean var2 = (Boolean)this.getAccessible(var1).getAttribute(AccessibleAttribute.VISIBLE);
            return Boolean.TRUE.equals(var2);
         }
      }
   }

   private double get_VerticalScrollPercent() {
      if (this.isDisposed()) {
         return 0.0;
      } else if (!this.get_VerticallyScrollable()) {
         return -1.0;
      } else {
         Node var1 = (Node)this.getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
         if (var1 != null) {
            Accessible var2 = this.getAccessible(var1);
            Double var3 = (Double)var2.getAttribute(AccessibleAttribute.VALUE);
            if (var3 == null) {
               return 0.0;
            } else {
               Double var4 = (Double)var2.getAttribute(AccessibleAttribute.MAX_VALUE);
               if (var4 == null) {
                  return 0.0;
               } else {
                  Double var5 = (Double)var2.getAttribute(AccessibleAttribute.MIN_VALUE);
                  return var5 == null ? 0.0 : 100.0 * (var3 - var5) / (var4 - var5);
               }
            }
         } else {
            return 0.0;
         }
      }
   }

   private double get_VerticalViewSize() {
      if (this.isDisposed()) {
         return 0.0;
      } else if (!this.get_VerticallyScrollable()) {
         return 100.0;
      } else {
         double var1 = 0.0;
         Bounds var3 = (Bounds)this.getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
         if (var3 == null) {
            return 0.0;
         } else {
            double var4 = var3.getHeight();
            AccessibleRole var6 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
            if (var6 == null) {
               return 0.0;
            } else {
               if (var6 == AccessibleRole.SCROLL_PANE) {
                  Node var7 = (Node)this.getAttribute(AccessibleAttribute.CONTENTS, new Object[0]);
                  if (var7 != null) {
                     Bounds var8 = (Bounds)this.getAccessible(var7).getAttribute(AccessibleAttribute.BOUNDS);
                     var1 = var8 == null ? 0.0 : var8.getHeight();
                  }
               } else {
                  Integer var9 = 0;
                  switch (var6) {
                     case TREE_TABLE_VIEW:
                     case TABLE_VIEW:
                     case TREE_VIEW:
                        var9 = (Integer)this.getAttribute(AccessibleAttribute.ROW_COUNT, new Object[0]);
                        break;
                     case LIST_VIEW:
                        var9 = (Integer)this.getAttribute(AccessibleAttribute.ITEM_COUNT, new Object[0]);
                     case IMAGE_VIEW:
                     case RADIO_BUTTON:
                     case CHECK_BOX:
                     case COMBO_BOX:
                     case HYPERLINK:
                  }

                  var1 = var9 == null ? 0.0 : (double)(var9 * 24);
               }

               return var1 == 0.0 ? 0.0 : var4 / var1 * 100.0;
            }
         }
      }
   }

   private void ScrollIntoView() {
      if (!this.isDisposed()) {
         AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
         if (var1 != null) {
            Accessible var2 = this.getContainer();
            if (var2 != null) {
               Node var3 = null;
               Integer var4;
               switch (var1) {
                  case TABLE_CELL:
                  case TREE_TABLE_CELL:
                     var4 = (Integer)this.getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                     Integer var5 = (Integer)this.getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
                     if (var4 != null && var5 != null) {
                        var3 = (Node)var2.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, var4, var5);
                     }
                     break;
                  case LIST_ITEM:
                     var4 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                     if (var4 != null) {
                        var3 = (Node)var2.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, var4);
                     }
                  case TAB_ITEM:
                  case PAGE_ITEM:
                  case TREE_TABLE_ROW:
                  default:
                     break;
                  case TREE_ITEM:
                     var4 = (Integer)this.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                     if (var4 != null) {
                        var3 = (Node)var2.getAttribute(AccessibleAttribute.ROW_AT_INDEX, var4);
                     }
               }

               if (var3 != null) {
                  var2.executeAction(AccessibleAction.SHOW_ITEM, var3);
               }

            }
         }
      }
   }

   static {
      _initIDs();
      idCount = 1;
   }
}
