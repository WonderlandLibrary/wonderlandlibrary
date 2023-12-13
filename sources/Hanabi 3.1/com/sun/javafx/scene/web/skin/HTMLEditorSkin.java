package com.sun.javafx.scene.web.skin;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.sun.javafx.scene.control.skin.FXVK;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import com.sun.javafx.scene.web.behavior.HTMLEditorBehavior;
import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

public class HTMLEditorSkin extends BehaviorSkinBase {
   private GridPane gridPane;
   private ToolBar toolbar1;
   private ToolBar toolbar2;
   private Button cutButton;
   private Button copyButton;
   private Button pasteButton;
   private Button insertHorizontalRuleButton;
   private ToggleGroup alignmentToggleGroup;
   private ToggleButton alignLeftButton;
   private ToggleButton alignCenterButton;
   private ToggleButton alignRightButton;
   private ToggleButton alignJustifyButton;
   private ToggleButton bulletsButton;
   private ToggleButton numbersButton;
   private Button indentButton;
   private Button outdentButton;
   private ComboBox formatComboBox;
   private Map formatStyleMap;
   private Map styleFormatMap;
   private ComboBox fontFamilyComboBox;
   private ComboBox fontSizeComboBox;
   private Map fontSizeMap;
   private Map sizeFontMap;
   private ToggleButton boldButton;
   private ToggleButton italicButton;
   private ToggleButton underlineButton;
   private ToggleButton strikethroughButton;
   private ColorPicker fgColorButton;
   private ColorPicker bgColorButton;
   private WebView webView;
   private WebPage webPage;
   private static final String CUT_COMMAND = "cut";
   private static final String COPY_COMMAND = "copy";
   private static final String PASTE_COMMAND = "paste";
   private static final String UNDO_COMMAND = "undo";
   private static final String REDO_COMMAND = "redo";
   private static final String INSERT_HORIZONTAL_RULE_COMMAND = "inserthorizontalrule";
   private static final String ALIGN_LEFT_COMMAND = "justifyleft";
   private static final String ALIGN_CENTER_COMMAND = "justifycenter";
   private static final String ALIGN_RIGHT_COMMAND = "justifyright";
   private static final String ALIGN_JUSTIFY_COMMAND = "justifyfull";
   private static final String BULLETS_COMMAND = "insertUnorderedList";
   private static final String NUMBERS_COMMAND = "insertOrderedList";
   private static final String INDENT_COMMAND = "indent";
   private static final String OUTDENT_COMMAND = "outdent";
   private static final String FORMAT_COMMAND = "formatblock";
   private static final String FONT_FAMILY_COMMAND = "fontname";
   private static final String FONT_SIZE_COMMAND = "fontsize";
   private static final String BOLD_COMMAND = "bold";
   private static final String ITALIC_COMMAND = "italic";
   private static final String UNDERLINE_COMMAND = "underline";
   private static final String STRIKETHROUGH_COMMAND = "strikethrough";
   private static final String FOREGROUND_COLOR_COMMAND = "forecolor";
   private static final String BACKGROUND_COLOR_COMMAND = "backcolor";
   private static final Color DEFAULT_BG_COLOR;
   private static final Color DEFAULT_FG_COLOR;
   private static final String FORMAT_PARAGRAPH = "<p>";
   private static final String FORMAT_HEADING_1 = "<h1>";
   private static final String FORMAT_HEADING_2 = "<h2>";
   private static final String FORMAT_HEADING_3 = "<h3>";
   private static final String FORMAT_HEADING_4 = "<h4>";
   private static final String FORMAT_HEADING_5 = "<h5>";
   private static final String FORMAT_HEADING_6 = "<h6>";
   private static final String SIZE_XX_SMALL = "1";
   private static final String SIZE_X_SMALL = "2";
   private static final String SIZE_SMALL = "3";
   private static final String SIZE_MEDIUM = "4";
   private static final String SIZE_LARGE = "5";
   private static final String SIZE_X_LARGE = "6";
   private static final String SIZE_XX_LARGE = "7";
   private static final String INSERT_NEW_LINE_COMMAND = "insertnewline";
   private static final String INSERT_TAB_COMMAND = "inserttab";
   private static final String[][] DEFAULT_FORMAT_MAPPINGS;
   private static final String[] DEFAULT_WINDOWS_7_MAPPINGS;
   private static final String[][] DEFAULT_OS_MAPPINGS;
   private static final String DEFAULT_OS_FONT;
   private ParentTraversalEngine engine;
   private boolean resetToolbarState = false;
   private String cachedHTMLText = "<html><head></head><body contenteditable=\"true\"></body></html>";
   private ListChangeListener itemsListener = (var1x) -> {
      label22:
      while(true) {
         if (var1x.next()) {
            if (var1x.getRemovedSize() <= 0) {
               continue;
            }

            Iterator var2 = var1x.getList().iterator();

            while(true) {
               if (!var2.hasNext()) {
                  continue label22;
               }

               Node var3 = (Node)var2.next();
               if (var3 instanceof WebView) {
                  this.webPage.dispose();
               }
            }
         }

         return;
      }
   };
   private ResourceBundle resources;
   private boolean enableAtomicityCheck = false;
   private int atomicityCount = 0;
   private boolean isFirstRun = true;
   private static final int FONT_FAMILY_MENUBUTTON_WIDTH = 150;
   private static final int FONT_FAMILY_MENU_WIDTH = 100;
   private static final int FONT_SIZE_MENUBUTTON_WIDTH = 80;
   private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE;

   private static String[] getOSMappings() {
      String var0 = System.getProperty("os.name");

      for(int var1 = 0; var1 < DEFAULT_OS_MAPPINGS.length; ++var1) {
         if (var0.equals(DEFAULT_OS_MAPPINGS[var1][0])) {
            return DEFAULT_OS_MAPPINGS[var1];
         }
      }

      return DEFAULT_WINDOWS_7_MAPPINGS;
   }

   public HTMLEditorSkin(HTMLEditor var1) {
      super(var1, new HTMLEditorBehavior(var1));
      this.getChildren().clear();
      this.gridPane = new GridPane();
      this.gridPane.getStyleClass().add("grid");
      this.getChildren().addAll(this.gridPane);
      this.toolbar1 = new ToolBar();
      this.toolbar1.getStyleClass().add("top-toolbar");
      this.gridPane.add(this.toolbar1, 0, 0);
      this.toolbar2 = new ToolBar();
      this.toolbar2.getStyleClass().add("bottom-toolbar");
      this.gridPane.add(this.toolbar2, 0, 1);
      this.webView = new WebView();
      this.gridPane.add(this.webView, 0, 2);
      ColumnConstraints var2 = new ColumnConstraints();
      var2.setHgrow(Priority.ALWAYS);
      this.gridPane.getColumnConstraints().add(var2);
      this.webPage = Accessor.getPageFor(this.webView.getEngine());
      this.webView.addEventHandler(MouseEvent.MOUSE_RELEASED, (var1x) -> {
         Platform.runLater(new Runnable() {
            public void run() {
               HTMLEditorSkin.this.enableAtomicityCheck = true;
               HTMLEditorSkin.this.updateToolbarState(true);
               HTMLEditorSkin.this.enableAtomicityCheck = false;
            }
         });
      });
      this.webView.addEventHandler(KeyEvent.KEY_PRESSED, (var1x) -> {
         this.applyTextFormatting();
         if (var1x.getCode() != KeyCode.CONTROL && var1x.getCode() != KeyCode.META) {
            if (var1x.getCode() == KeyCode.TAB && !var1x.isControlDown()) {
               if (!var1x.isShiftDown()) {
                  if (!this.getCommandState("insertUnorderedList") && !this.getCommandState("insertOrderedList")) {
                     this.executeCommand("inserttab", (String)null);
                  } else {
                     this.executeCommand("indent", (String)null);
                  }
               } else if (this.getCommandState("insertUnorderedList") || this.getCommandState("insertOrderedList")) {
                  this.executeCommand("outdent", (String)null);
               }

            } else if ((this.fgColorButton == null || !this.fgColorButton.isShowing()) && (this.bgColorButton == null || !this.bgColorButton.isShowing())) {
               Platform.runLater(() -> {
                  if (this.webPage.getClientSelectedText().isEmpty()) {
                     if (var1x.getCode() != KeyCode.UP && var1x.getCode() != KeyCode.DOWN && var1x.getCode() != KeyCode.LEFT && var1x.getCode() != KeyCode.RIGHT && var1x.getCode() != KeyCode.HOME && var1x.getCode() != KeyCode.END) {
                        if (!var1x.isControlDown() && !var1x.isMetaDown()) {
                           this.resetToolbarState = var1x.getCode() == KeyCode.ENTER;
                           if (this.resetToolbarState && this.getCommandState("bold") != this.boldButton.selectedProperty().getValue()) {
                              this.executeCommand("bold", this.boldButton.selectedProperty().getValue().toString());
                           }

                           this.updateToolbarState(false);
                        } else {
                           if (var1x.getCode() == KeyCode.B) {
                              this.keyboardShortcuts("bold");
                           } else if (var1x.getCode() == KeyCode.I) {
                              this.keyboardShortcuts("italic");
                           } else if (var1x.getCode() == KeyCode.U) {
                              this.keyboardShortcuts("underline");
                           }

                           this.updateToolbarState(true);
                        }
                     } else {
                        this.updateToolbarState(true);
                     }

                     this.resetToolbarState = false;
                  } else if (var1x.isShiftDown() && (var1x.getCode() == KeyCode.UP || var1x.getCode() == KeyCode.DOWN || var1x.getCode() == KeyCode.LEFT || var1x.getCode() == KeyCode.RIGHT)) {
                     this.updateToolbarState(true);
                  }

               });
            }
         }
      });
      this.webView.addEventHandler(KeyEvent.KEY_RELEASED, (var1x) -> {
         if (var1x.getCode() != KeyCode.CONTROL && var1x.getCode() != KeyCode.META) {
            if ((this.fgColorButton == null || !this.fgColorButton.isShowing()) && (this.bgColorButton == null || !this.bgColorButton.isShowing())) {
               Platform.runLater(() -> {
                  if (this.webPage.getClientSelectedText().isEmpty()) {
                     if (var1x.getCode() != KeyCode.UP && var1x.getCode() != KeyCode.DOWN && var1x.getCode() != KeyCode.LEFT && var1x.getCode() != KeyCode.RIGHT && var1x.getCode() != KeyCode.HOME && var1x.getCode() != KeyCode.END) {
                        if (!var1x.isControlDown() && !var1x.isMetaDown()) {
                           this.resetToolbarState = var1x.getCode() == KeyCode.ENTER;
                           if (!this.resetToolbarState) {
                              this.updateToolbarState(false);
                           }
                        } else {
                           if (var1x.getCode() == KeyCode.B) {
                              this.keyboardShortcuts("bold");
                           } else if (var1x.getCode() == KeyCode.I) {
                              this.keyboardShortcuts("italic");
                           } else if (var1x.getCode() == KeyCode.U) {
                              this.keyboardShortcuts("underline");
                           }

                           this.updateToolbarState(true);
                        }
                     } else {
                        this.updateToolbarState(true);
                     }

                     this.resetToolbarState = false;
                  }

               });
            }
         }
      });
      ((HTMLEditor)this.getSkinnable()).focusedProperty().addListener((var1x, var2x, var3) -> {
         Platform.runLater(new Runnable() {
            public void run() {
               if (var3) {
                  HTMLEditorSkin.this.webView.requestFocus();
               }

            }
         });
      });
      this.webView.focusedProperty().addListener((var1x, var2x, var3) -> {
         this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, var3);
         Platform.runLater(new Runnable() {
            public void run() {
               HTMLEditorSkin.this.updateToolbarState(true);
               if (PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                  Scene var1 = ((HTMLEditor)HTMLEditorSkin.this.getSkinnable()).getScene();
                  if (var3) {
                     FXVK.attach(HTMLEditorSkin.this.webView);
                  } else if (var1 == null || var1.getWindow() == null || !var1.getWindow().isFocused() || !(var1.getFocusOwner() instanceof TextInputControl)) {
                     FXVK.detach();
                  }
               }

            }
         });
      });
      this.webView.getEngine().getLoadWorker().workDoneProperty().addListener((var1x, var2x, var3) -> {
         Platform.runLater(() -> {
            this.webView.requestLayout();
         });
         double var4 = this.webView.getEngine().getLoadWorker().getTotalWork();
         if (var3.doubleValue() == var4) {
            this.cachedHTMLText = null;
            Platform.runLater(() -> {
               this.setContentEditable(true);
               this.updateToolbarState(true);
               this.updateNodeOrientation();
            });
         }

      });
      this.enableToolbar(true);
      this.setHTMLText(this.cachedHTMLText);
      this.engine = new ParentTraversalEngine(this.getSkinnable(), new Algorithm() {
         public Node select(Node var1, Direction var2, TraversalContext var3) {
            return HTMLEditorSkin.this.cutButton;
         }

         public Node selectFirst(TraversalContext var1) {
            return HTMLEditorSkin.this.cutButton;
         }

         public Node selectLast(TraversalContext var1) {
            return HTMLEditorSkin.this.cutButton;
         }
      });
      ((HTMLEditor)this.getSkinnable()).setImpl_traversalEngine(this.engine);
      this.webView.setFocusTraversable(true);
      this.gridPane.getChildren().addListener(this.itemsListener);
   }

   public final String getHTMLText() {
      return this.cachedHTMLText != null ? this.cachedHTMLText : this.webPage.getHtml(this.webPage.getMainFrame());
   }

   public final void setHTMLText(String var1) {
      this.cachedHTMLText = var1;
      this.webPage.load(this.webPage.getMainFrame(), var1, "text/html");
      Platform.runLater(() -> {
         this.updateToolbarState(true);
      });
   }

   private void populateToolbars() {
      this.resources = ResourceBundle.getBundle(HTMLEditorSkin.class.getName());
      this.cutButton = this.addButton(this.toolbar1, this.resources.getString("cutIcon"), this.resources.getString("cut"), "cut", "html-editor-cut");
      this.copyButton = this.addButton(this.toolbar1, this.resources.getString("copyIcon"), this.resources.getString("copy"), "copy", "html-editor-copy");
      this.pasteButton = this.addButton(this.toolbar1, this.resources.getString("pasteIcon"), this.resources.getString("paste"), "paste", "html-editor-paste");
      this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
      this.alignmentToggleGroup = new ToggleGroup();
      this.alignLeftButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignLeftIcon"), this.resources.getString("alignLeft"), "justifyleft", "html-editor-align-left");
      this.alignCenterButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignCenterIcon"), this.resources.getString("alignCenter"), "justifycenter", "html-editor-align-center");
      this.alignRightButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignRightIcon"), this.resources.getString("alignRight"), "justifyright", "html-editor-align-right");
      this.alignJustifyButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignJustifyIcon"), this.resources.getString("alignJustify"), "justifyfull", "html-editor-align-justify");
      this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
      this.outdentButton = this.addButton(this.toolbar1, this.resources.getString("outdentIcon"), this.resources.getString("outdent"), "outdent", "html-editor-outdent");
      if (this.outdentButton.getGraphic() != null) {
         this.outdentButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
      }

      this.indentButton = this.addButton(this.toolbar1, this.resources.getString("indentIcon"), this.resources.getString("indent"), "indent", "html-editor-indent");
      if (this.indentButton.getGraphic() != null) {
         this.indentButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
      }

      this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
      ToggleGroup var1 = new ToggleGroup();
      this.bulletsButton = this.addToggleButton(this.toolbar1, var1, this.resources.getString("bulletsIcon"), this.resources.getString("bullets"), "insertUnorderedList", "html-editor-bullets");
      if (this.bulletsButton.getGraphic() != null) {
         this.bulletsButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
      }

      this.numbersButton = this.addToggleButton(this.toolbar1, var1, this.resources.getString("numbersIcon"), this.resources.getString("numbers"), "insertOrderedList", "html-editor-numbers");
      this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
      this.formatComboBox = new ComboBox();
      this.formatComboBox.getStyleClass().add("font-menu-button");
      this.formatComboBox.setFocusTraversable(false);
      this.formatComboBox.setMinWidth(Double.NEGATIVE_INFINITY);
      this.toolbar2.getItems().add(this.formatComboBox);
      this.formatStyleMap = new HashMap();
      this.styleFormatMap = new HashMap();
      this.createFormatMenuItem("<p>", this.resources.getString("paragraph"));
      Platform.runLater(() -> {
         this.formatComboBox.setValue(this.resources.getString("paragraph"));
      });
      this.createFormatMenuItem("<h1>", this.resources.getString("heading1"));
      this.createFormatMenuItem("<h2>", this.resources.getString("heading2"));
      this.createFormatMenuItem("<h3>", this.resources.getString("heading3"));
      this.createFormatMenuItem("<h4>", this.resources.getString("heading4"));
      this.createFormatMenuItem("<h5>", this.resources.getString("heading5"));
      this.createFormatMenuItem("<h6>", this.resources.getString("heading6"));
      this.formatComboBox.setTooltip(new Tooltip(this.resources.getString("format")));
      this.formatComboBox.valueProperty().addListener((var1x, var2x, var3x) -> {
         if (var3x == null) {
            this.formatComboBox.setValue((Object)null);
         } else {
            String var4 = (String)this.formatStyleMap.get(var3x);
            this.executeCommand("formatblock", var4);
            this.updateToolbarState(false);

            for(int var5 = 0; var5 < DEFAULT_FORMAT_MAPPINGS.length; ++var5) {
               String[] var6 = DEFAULT_FORMAT_MAPPINGS[var5];
               if (var6[0].equalsIgnoreCase(var4)) {
                  this.executeCommand("fontsize", var6[2]);
                  this.updateToolbarState(false);
                  break;
               }
            }
         }

      });
      this.fontFamilyComboBox = new ComboBox();
      this.fontFamilyComboBox.getStyleClass().add("font-menu-button");
      this.fontFamilyComboBox.setMinWidth(150.0);
      this.fontFamilyComboBox.setPrefWidth(150.0);
      this.fontFamilyComboBox.setMaxWidth(150.0);
      this.fontFamilyComboBox.setFocusTraversable(false);
      this.fontFamilyComboBox.setTooltip(new Tooltip(this.resources.getString("fontFamily")));
      this.toolbar2.getItems().add(this.fontFamilyComboBox);
      this.fontFamilyComboBox.getProperties().put("comboBoxRowsToMeasureWidth", 0);
      this.fontFamilyComboBox.setCellFactory(new Callback() {
         public ListCell call(ListView var1) {
            ListCell var2 = new ListCell() {
               public void updateItem(String var1, boolean var2) {
                  super.updateItem(var1, var2);
                  if (var1 != null) {
                     this.setText(var1);
                     this.setFont(new Font(var1, 12.0));
                  }

               }
            };
            var2.setMinWidth(100.0);
            var2.setPrefWidth(100.0);
            var2.setMaxWidth(100.0);
            return var2;
         }
      });
      Platform.runLater(() -> {
         ObservableList var1 = FXCollections.observableArrayList((Collection)Font.getFamilies());

         for(Iterator var2 = var1.iterator(); var2.hasNext(); this.fontFamilyComboBox.setItems(var1)) {
            String var3 = (String)var2.next();
            if (DEFAULT_OS_FONT.equals(var3)) {
               this.fontFamilyComboBox.setValue(var3);
            }
         }

      });
      this.fontFamilyComboBox.valueProperty().addListener((var1x, var2x, var3x) -> {
         this.executeCommand("fontname", var3x);
      });
      this.fontSizeComboBox = new ComboBox();
      this.fontSizeComboBox.getStyleClass().add("font-menu-button");
      this.fontSizeComboBox.setFocusTraversable(false);
      this.toolbar2.getItems().add(this.fontSizeComboBox);
      this.fontSizeMap = new HashMap();
      this.sizeFontMap = new HashMap();
      this.createFontSizeMenuItem("1", this.resources.getString("extraExtraSmall"));
      this.createFontSizeMenuItem("2", this.resources.getString("extraSmall"));
      this.createFontSizeMenuItem("3", this.resources.getString("small"));
      Platform.runLater(() -> {
         this.fontSizeComboBox.setValue(this.resources.getString("small"));
      });
      this.createFontSizeMenuItem("4", this.resources.getString("medium"));
      this.createFontSizeMenuItem("5", this.resources.getString("large"));
      this.createFontSizeMenuItem("6", this.resources.getString("extraLarge"));
      this.createFontSizeMenuItem("7", this.resources.getString("extraExtraLarge"));
      this.fontSizeComboBox.setTooltip(new Tooltip(this.resources.getString("fontSize")));
      this.fontSizeComboBox.setCellFactory(new Callback() {
         public ListCell call(ListView var1) {
            ListCell var2 = new ListCell() {
               public void updateItem(String var1, boolean var2) {
                  super.updateItem(var1, var2);
                  if (var1 != null) {
                     this.setText(var1);
                     String var3 = var1.replaceFirst("[^0-9.].*$", "");
                     this.setFont(new Font((String)HTMLEditorSkin.this.fontFamilyComboBox.getValue(), Double.valueOf(var3)));
                  }

               }
            };
            return var2;
         }
      });
      this.fontSizeComboBox.valueProperty().addListener((var1x, var2x, var3x) -> {
         String var4 = this.getCommandValue("fontsize");
         if (!var3x.equals(var4)) {
            this.executeCommand("fontsize", (String)this.fontSizeMap.get(var3x));
         }

      });
      this.toolbar2.getItems().add(new Separator(Orientation.VERTICAL));
      this.boldButton = this.addToggleButton(this.toolbar2, (ToggleGroup)null, this.resources.getString("boldIcon"), this.resources.getString("bold"), "bold", "html-editor-bold");
      this.boldButton.setOnAction((var1x) -> {
         if ("<p>".equals(this.formatStyleMap.get(this.formatComboBox.getValue()))) {
            this.executeCommand("bold", this.boldButton.selectedProperty().getValue().toString());
         }

      });
      this.italicButton = this.addToggleButton(this.toolbar2, (ToggleGroup)null, this.resources.getString("italicIcon"), this.resources.getString("italic"), "italic", "html-editor-italic");
      this.underlineButton = this.addToggleButton(this.toolbar2, (ToggleGroup)null, this.resources.getString("underlineIcon"), this.resources.getString("underline"), "underline", "html-editor-underline");
      this.strikethroughButton = this.addToggleButton(this.toolbar2, (ToggleGroup)null, this.resources.getString("strikethroughIcon"), this.resources.getString("strikethrough"), "strikethrough", "html-editor-strike");
      this.toolbar2.getItems().add(new Separator(Orientation.VERTICAL));
      this.insertHorizontalRuleButton = this.addButton(this.toolbar2, this.resources.getString("insertHorizontalRuleIcon"), this.resources.getString("insertHorizontalRule"), "inserthorizontalrule", "html-editor-hr");
      this.insertHorizontalRuleButton.setOnAction((var1x) -> {
         this.executeCommand("insertnewline", (String)null);
         this.executeCommand("inserthorizontalrule", (String)null);
         this.updateToolbarState(false);
      });
      this.fgColorButton = new ColorPicker();
      this.fgColorButton.getStyleClass().add("html-editor-foreground");
      this.fgColorButton.setFocusTraversable(false);
      this.toolbar1.getItems().add(this.fgColorButton);
      this.fgColorButton.applyCss();
      ColorPickerSkin var2 = (ColorPickerSkin)this.fgColorButton.getSkin();
      String var3 = (String)AccessController.doPrivileged(() -> {
         return HTMLEditorSkin.class.getResource(this.resources.getString("foregroundColorIcon")).toString();
      });
      ((StyleableProperty)var2.imageUrlProperty()).applyStyle((StyleOrigin)null, var3);
      this.fgColorButton.setValue(DEFAULT_FG_COLOR);
      this.fgColorButton.setTooltip(new Tooltip(this.resources.getString("foregroundColor")));
      this.fgColorButton.setOnAction((var1x) -> {
         Color var2 = (Color)this.fgColorButton.getValue();
         if (var2 != null) {
            this.executeCommand("forecolor", this.colorValueToHex(var2));
            this.fgColorButton.hide();
         }

      });
      this.bgColorButton = new ColorPicker();
      this.bgColorButton.getStyleClass().add("html-editor-background");
      this.bgColorButton.setFocusTraversable(false);
      this.toolbar1.getItems().add(this.bgColorButton);
      this.bgColorButton.applyCss();
      ColorPickerSkin var4 = (ColorPickerSkin)this.bgColorButton.getSkin();
      String var5 = (String)AccessController.doPrivileged(() -> {
         return HTMLEditorSkin.class.getResource(this.resources.getString("backgroundColorIcon")).toString();
      });
      ((StyleableProperty)var4.imageUrlProperty()).applyStyle((StyleOrigin)null, var5);
      this.bgColorButton.setValue(DEFAULT_BG_COLOR);
      this.bgColorButton.setTooltip(new Tooltip(this.resources.getString("backgroundColor")));
      this.bgColorButton.setOnAction((var1x) -> {
         Color var2 = (Color)this.bgColorButton.getValue();
         if (var2 != null) {
            this.executeCommand("backcolor", this.colorValueToHex(var2));
            this.bgColorButton.hide();
         }

      });
   }

   private String colorValueToHex(Color var1) {
      return String.format((Locale)null, "#%02x%02x%02x", Math.round(var1.getRed() * 255.0), Math.round(var1.getGreen() * 255.0), Math.round(var1.getBlue() * 255.0));
   }

   private Button addButton(ToolBar var1, String var2, String var3, String var4, String var5) {
      Button var6 = new Button();
      var6.setFocusTraversable(false);
      var6.getStyleClass().add(var5);
      var1.getItems().add(var6);
      Image var7 = (Image)AccessController.doPrivileged(() -> {
         return new Image(HTMLEditorSkin.class.getResource(var2).toString());
      });
      ((StyleableProperty)var6.graphicProperty()).applyStyle((StyleOrigin)null, new ImageView(var7));
      var6.setTooltip(new Tooltip(var3));
      var6.setOnAction((var2x) -> {
         this.executeCommand(var4, (String)null);
         this.updateToolbarState(false);
      });
      return var6;
   }

   private ToggleButton addToggleButton(ToolBar var1, ToggleGroup var2, String var3, String var4, String var5, String var6) {
      ToggleButton var7 = new ToggleButton();
      var7.setUserData(var5);
      var7.setFocusTraversable(false);
      var7.getStyleClass().add(var6);
      var1.getItems().add(var7);
      if (var2 != null) {
         var7.setToggleGroup(var2);
      }

      Image var8 = (Image)AccessController.doPrivileged(() -> {
         return new Image(HTMLEditorSkin.class.getResource(var3).toString());
      });
      ((StyleableProperty)var7.graphicProperty()).applyStyle((StyleOrigin)null, new ImageView(var8));
      var7.setTooltip(new Tooltip(var4));
      if (!"bold".equals(var5)) {
         var7.selectedProperty().addListener((var2x, var3x, var4x) -> {
            if (this.getCommandState(var5) != var4x) {
               this.executeCommand(var5, (String)null);
            }

         });
      }

      return var7;
   }

   private void createFormatMenuItem(String var1, String var2) {
      this.formatComboBox.getItems().add(var2);
      this.formatStyleMap.put(var2, var1);
      this.styleFormatMap.put(var1, var2);
   }

   private void createFontSizeMenuItem(String var1, String var2) {
      this.fontSizeComboBox.getItems().add(var2);
      this.fontSizeMap.put(var2, var1);
      this.sizeFontMap.put(var1, var2);
   }

   private void updateNodeOrientation() {
      NodeOrientation var1 = ((HTMLEditor)this.getSkinnable()).getEffectiveNodeOrientation();
      HTMLDocument var2 = (HTMLDocument)this.webPage.getDocument(this.webPage.getMainFrame());
      HTMLElement var3 = (HTMLElement)var2.getDocumentElement();
      if (var3.getAttribute("dir") == null) {
         var3.setAttribute("dir", var1 == NodeOrientation.RIGHT_TO_LEFT ? "rtl" : "ltr");
      }

   }

   private void updateToolbarState(boolean var1) {
      if (this.webView.isFocused()) {
         ++this.atomicityCount;
         this.copyButton.setDisable(!this.isCommandEnabled("cut"));
         this.cutButton.setDisable(!this.isCommandEnabled("copy"));
         this.pasteButton.setDisable(!this.isCommandEnabled("paste"));
         this.insertHorizontalRuleButton.setDisable(!this.isCommandEnabled("inserthorizontalrule"));
         String var2;
         if (var1) {
            this.alignLeftButton.setDisable(!this.isCommandEnabled("justifyleft"));
            this.alignLeftButton.setSelected(this.getCommandState("justifyleft"));
            this.alignCenterButton.setDisable(!this.isCommandEnabled("justifycenter"));
            this.alignCenterButton.setSelected(this.getCommandState("justifycenter"));
            this.alignRightButton.setDisable(!this.isCommandEnabled("justifyright"));
            this.alignRightButton.setSelected(this.getCommandState("justifyright"));
            this.alignJustifyButton.setDisable(!this.isCommandEnabled("justifyfull"));
            this.alignJustifyButton.setSelected(this.getCommandState("justifyfull"));
         } else if (this.alignmentToggleGroup.getSelectedToggle() != null) {
            var2 = this.alignmentToggleGroup.getSelectedToggle().getUserData().toString();
            if (this.isCommandEnabled(var2) && !this.getCommandState(var2)) {
               this.executeCommand(var2, (String)null);
            }
         }

         if (this.alignmentToggleGroup.getSelectedToggle() == null) {
            this.alignmentToggleGroup.selectToggle(this.alignLeftButton);
         }

         this.bulletsButton.setDisable(!this.isCommandEnabled("insertUnorderedList"));
         this.bulletsButton.setSelected(this.getCommandState("insertUnorderedList"));
         this.numbersButton.setDisable(!this.isCommandEnabled("insertOrderedList"));
         this.numbersButton.setSelected(this.getCommandState("insertOrderedList"));
         this.indentButton.setDisable(!this.isCommandEnabled("indent"));
         this.outdentButton.setDisable(!this.isCommandEnabled("outdent"));
         this.formatComboBox.setDisable(!this.isCommandEnabled("formatblock"));
         var2 = this.getCommandValue("formatblock");
         String var3;
         String var4;
         String var5;
         if (var2 != null) {
            var3 = "<" + var2 + ">";
            var4 = (String)this.styleFormatMap.get(var3);
            var5 = (String)this.formatComboBox.getValue();
            if (!this.resetToolbarState && !var3.equals("<>") && !var3.equalsIgnoreCase("<div>") && !var3.equalsIgnoreCase("<blockquote>")) {
               if (var5 != null && !var5.equalsIgnoreCase(var4)) {
                  this.formatComboBox.setValue(var4);
               }
            } else {
               this.formatComboBox.setValue(this.resources.getString("paragraph"));
            }
         }

         this.fontFamilyComboBox.setDisable(!this.isCommandEnabled("fontname"));
         var3 = this.getCommandValue("fontname");
         if (var3 != null) {
            var4 = var3;
            if (var3.startsWith("'")) {
               var4 = var3.substring(1);
            }

            if (var4.endsWith("'")) {
               var4 = var4.substring(0, var4.length() - 1);
            }

            Object var10 = this.fontFamilyComboBox.getValue();
            if (var10 instanceof String && !var10.equals(var4)) {
               ObservableList var6 = this.fontFamilyComboBox.getItems();
               String var7 = null;
               Iterator var8 = var6.iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  if (var9.equals(var4)) {
                     var7 = var9;
                     break;
                  }

                  if (var9.equals(DEFAULT_OS_FONT) && var4.equals("Dialog")) {
                     var7 = var9;
                     break;
                  }
               }

               if (var7 != null) {
                  this.fontFamilyComboBox.setValue(var7);
               }
            }
         }

         this.fontSizeComboBox.setDisable(!this.isCommandEnabled("fontsize"));
         var4 = this.getCommandValue("fontsize");
         if (this.resetToolbarState && var4 == null) {
            this.fontSizeComboBox.setValue(this.sizeFontMap.get("3"));
         } else if (var4 != null) {
            if (!((String)this.fontSizeComboBox.getValue()).equals(this.sizeFontMap.get(var4))) {
               this.fontSizeComboBox.setValue(this.sizeFontMap.get(var4));
            }
         } else if (this.fontSizeComboBox.getValue() == null || !((String)this.fontSizeComboBox.getValue()).equals(this.sizeFontMap.get("3"))) {
            this.fontSizeComboBox.setValue(this.sizeFontMap.get("3"));
         }

         this.boldButton.setDisable(!this.isCommandEnabled("bold"));
         this.boldButton.setSelected(this.getCommandState("bold"));
         this.italicButton.setDisable(!this.isCommandEnabled("italic"));
         this.italicButton.setSelected(this.getCommandState("italic"));
         this.underlineButton.setDisable(!this.isCommandEnabled("underline"));
         this.underlineButton.setSelected(this.getCommandState("underline"));
         this.strikethroughButton.setDisable(!this.isCommandEnabled("strikethrough"));
         this.strikethroughButton.setSelected(this.getCommandState("strikethrough"));
         this.fgColorButton.setDisable(!this.isCommandEnabled("forecolor"));
         var5 = this.getCommandValue("forecolor");
         if (var5 != null) {
            this.fgColorButton.setValue(this.getColor(var5));
         }

         this.bgColorButton.setDisable(!this.isCommandEnabled("backcolor"));
         String var11 = this.getCommandValue("backcolor");
         if (var11 != null) {
            this.bgColorButton.setValue(this.getColor(var11));
         }

         this.atomicityCount = this.atomicityCount == 0 ? 0 : --this.atomicityCount;
      }
   }

   private void enableToolbar(boolean var1) {
      Platform.runLater(() -> {
         if (this.copyButton != null) {
            if (var1) {
               this.copyButton.setDisable(!this.isCommandEnabled("copy"));
               this.cutButton.setDisable(!this.isCommandEnabled("cut"));
               this.pasteButton.setDisable(!this.isCommandEnabled("paste"));
            } else {
               this.copyButton.setDisable(true);
               this.cutButton.setDisable(true);
               this.pasteButton.setDisable(true);
            }

            this.insertHorizontalRuleButton.setDisable(!var1);
            this.alignLeftButton.setDisable(!var1);
            this.alignCenterButton.setDisable(!var1);
            this.alignRightButton.setDisable(!var1);
            this.alignJustifyButton.setDisable(!var1);
            this.bulletsButton.setDisable(!var1);
            this.numbersButton.setDisable(!var1);
            this.indentButton.setDisable(!var1);
            this.outdentButton.setDisable(!var1);
            this.formatComboBox.setDisable(!var1);
            this.fontFamilyComboBox.setDisable(!var1);
            this.fontSizeComboBox.setDisable(!var1);
            this.boldButton.setDisable(!var1);
            this.italicButton.setDisable(!var1);
            this.underlineButton.setDisable(!var1);
            this.strikethroughButton.setDisable(!var1);
            this.fgColorButton.setDisable(!var1);
            this.bgColorButton.setDisable(!var1);
         }
      });
   }

   private boolean executeCommand(String var1, String var2) {
      return this.enableAtomicityCheck && (!this.enableAtomicityCheck || this.atomicityCount != 0) ? false : this.webPage.executeCommand(var1, var2);
   }

   private boolean isCommandEnabled(String var1) {
      return this.webPage.queryCommandEnabled(var1);
   }

   private void setContentEditable(boolean var1) {
      HTMLDocument var2 = (HTMLDocument)this.webPage.getDocument(this.webPage.getMainFrame());
      HTMLElement var3 = (HTMLElement)var2.getDocumentElement();
      HTMLElement var4 = (HTMLElement)var3.getElementsByTagName("body").item(0);
      var4.setAttribute("contenteditable", Boolean.toString(var1));
   }

   private boolean getCommandState(String var1) {
      return this.webPage.queryCommandState(var1);
   }

   private String getCommandValue(String var1) {
      return this.webPage.queryCommandValue(var1);
   }

   private Color getColor(String var1) {
      Color var2 = Color.web(var1);
      if (var2.equals(Color.TRANSPARENT)) {
         var2 = Color.WHITE;
      }

      return var2;
   }

   private void applyTextFormatting() {
      if (!this.getCommandState("insertUnorderedList") && !this.getCommandState("insertOrderedList")) {
         if (this.webPage.getClientCommittedTextLength() == 0) {
            String var1 = (String)this.formatStyleMap.get(this.formatComboBox.getValue());
            String var2 = ((String)this.fontFamilyComboBox.getValue()).toString();
            this.executeCommand("formatblock", var1);
            this.executeCommand("fontname", var2);
         }

      }
   }

   public void keyboardShortcuts(String var1) {
      if ("bold".equals(var1)) {
         this.boldButton.fire();
      } else if ("italic".equals(var1)) {
         this.italicButton.setSelected(!this.italicButton.isSelected());
      } else if ("underline".equals(var1)) {
         this.underlineButton.setSelected(!this.underlineButton.isSelected());
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      if (this.isFirstRun) {
         this.populateToolbars();
         this.isFirstRun = false;
      }

      super.layoutChildren(var1, var3, var5, var7);
      double var9 = Math.max(this.toolbar1.prefWidth(-1.0), this.toolbar2.prefWidth(-1.0));
      this.toolbar1.setMinWidth(var9);
      this.toolbar1.setPrefWidth(var9);
      this.toolbar2.setMinWidth(var9);
      this.toolbar2.setPrefWidth(var9);
   }

   public void print(PrinterJob var1) {
      this.webView.getEngine().print(var1);
   }

   static {
      DEFAULT_BG_COLOR = Color.WHITE;
      DEFAULT_FG_COLOR = Color.BLACK;
      DEFAULT_FORMAT_MAPPINGS = new String[][]{{"<p>", "", "3"}, {"<h1>", "bold", "6"}, {"<h2>", "bold", "5"}, {"<h3>", "bold", "4"}, {"<h4>", "bold", "3"}, {"<h5>", "bold", "2"}, {"<h6>", "bold", "1"}};
      DEFAULT_WINDOWS_7_MAPPINGS = new String[]{"Windows 7", "Segoe UI", "12px", "", "120"};
      DEFAULT_OS_MAPPINGS = new String[][]{{"Windows XP", "Tahoma", "12px", "", "96"}, {"Windows Vista", "Segoe UI", "12px", "", "96"}, DEFAULT_WINDOWS_7_MAPPINGS, {"Mac OS X", "Lucida Grande", "12px", "", "72"}, {"Linux", "Lucida Sans", "12px", "", "96"}};
      DEFAULT_OS_FONT = getOSMappings()[1];
      CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("contains-focus");
   }
}
