package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.deploy.uitoolkit.ui.ConsoleController;
import com.sun.deploy.uitoolkit.ui.ConsoleHelper;
import com.sun.deploy.uitoolkit.ui.ConsoleWindow;
import com.sun.deploy.util.DeploySysAction;
import com.sun.deploy.util.DeploySysRun;
import java.text.MessageFormat;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class FXConsole implements ConsoleWindow {
   private final boolean USE_TEXT_AREA = true;
   private final ConsoleController controller;
   private FXDialog dialog;
   private ScrollPane sp;
   private TextArea textArea;
   private Label textAreaLabel;

   public static FXConsole create(final ConsoleController var0) throws Exception {
      return (FXConsole)DeploySysRun.execute(new DeploySysAction() {
         public Object execute() {
            return new FXConsole(var0);
         }
      });
   }

   public FXConsole(ConsoleController var1) {
      this.controller = var1;
      invokeLater(new Runnable() {
         public void run() {
            FXConsole.this.dialog = new FXDialog(ResourceManager.getMessage("console.caption"), (Window)null, false, StageStyle.DECORATED, false);
            FXConsole.this.dialog.setResizable(true);
            FXConsole.this.dialog.impl_setImportant(false);
            BorderPane var1 = new BorderPane();
            FXConsole.this.dialog.setContentPane(var1);
            FXConsole.this.dialog.setWidth(470.0);
            FXConsole.this.dialog.setHeight(430.0);
            FXConsole.this.textArea = new TextArea();
            FXConsole.this.textArea.setEditable(false);
            FXConsole.this.textArea.setWrapText(true);
            FXConsole.this.textArea.getStyleClass().add("multiline-text");
            var1.setCenter(FXConsole.this.textArea);
            EventHandler var2 = new EventHandler() {
               public void handle(KeyEvent var1) {
                  String var2 = var1.getCharacter();
                  if (var2 != null && var2.length() == 1) {
                     switch (var2.charAt(0)) {
                        case '0':
                           FXConsole.this.traceLevel0();
                           break;
                        case '1':
                           FXConsole.this.traceLevel1();
                           break;
                        case '2':
                           FXConsole.this.traceLevel2();
                           break;
                        case '3':
                           FXConsole.this.traceLevel3();
                           break;
                        case '4':
                           FXConsole.this.traceLevel4();
                           break;
                        case '5':
                           FXConsole.this.traceLevel5();
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case ':':
                        case ';':
                        case '<':
                        case '=':
                        case '>':
                        case '?':
                        case '@':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                        case 'G':
                        case 'H':
                        case 'I':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'Q':
                        case 'R':
                        case 'S':
                        case 'T':
                        case 'U':
                        case 'V':
                        case 'W':
                        case 'X':
                        case 'Y':
                        case 'Z':
                        case '[':
                        case '\\':
                        case ']':
                        case '^':
                        case '_':
                        case '`':
                        case 'a':
                        case 'b':
                        case 'd':
                        case 'e':
                        case 'i':
                        case 'k':
                        case 'n':
                        case 'u':
                        case 'w':
                        default:
                           break;
                        case 'c':
                           FXConsole.this.clearConsole();
                           break;
                        case 'f':
                           FXConsole.this.runFinalize();
                           break;
                        case 'g':
                           FXConsole.this.runGC();
                           break;
                        case 'h':
                           FXConsole.this.showHelp();
                           break;
                        case 'j':
                           FXConsole.this.dumpJCovData();
                           break;
                        case 'l':
                           FXConsole.this.showClassLoaderCache();
                           break;
                        case 'm':
                           FXConsole.this.showMemory();
                           break;
                        case 'o':
                           FXConsole.this.logging();
                           break;
                        case 'p':
                           FXConsole.this.reloadProxyConfig();
                           break;
                        case 'q':
                           FXConsole.this.closeConsole();
                           break;
                        case 'r':
                           FXConsole.this.reloadPolicyConfig();
                           break;
                        case 's':
                           FXConsole.this.showSystemProperties();
                           break;
                        case 't':
                           FXConsole.this.showThreads();
                           break;
                        case 'v':
                           FXConsole.this.dumpThreadStack();
                           break;
                        case 'x':
                           FXConsole.this.clearClassLoaderCache();
                     }
                  }

               }
            };
            FXConsole.this.dialog.getScene().setOnKeyTyped(var2);
            FXConsole.this.textArea.setOnKeyTyped(var2);
            Button var3 = new Button(ResourceManager.getMessage("console.clear"));
            Button var4 = new Button(ResourceManager.getMessage("console.copy"));
            Button var5 = new Button(ResourceManager.getMessage("console.close"));
            FlowPane var6 = new FlowPane();
            var6.getStyleClass().add("button-bar");
            var6.setId("console-dialog-button-bar");
            var6.getChildren().add(var3);
            var6.getChildren().add(new Label("    "));
            var6.getChildren().add(var4);
            var6.getChildren().add(new Label("    "));
            var6.getChildren().add(var5);
            var1.setBottom(var6);
            var3.setOnAction(new EventHandler() {
               public void handle(ActionEvent var1) {
                  FXConsole.this.clearConsole();
               }
            });
            var4.setOnAction(new EventHandler() {
               public void handle(ActionEvent var1) {
                  FXConsole.this.copyConsole();
               }
            });
            var5.setOnAction(new EventHandler() {
               public void handle(ActionEvent var1) {
                  FXConsole.this.closeConsole();
               }
            });
         }
      });
   }

   private void dumpJCovData() {
      if (this.controller.isJCovSupported()) {
         if (this.controller.dumpJCovData()) {
            System.out.println(ResourceManager.getMessage("console.jcov.info"));
         } else {
            System.out.println(ResourceManager.getMessage("console.jcov.error"));
         }
      }

   }

   private void dumpThreadStack() {
      if (this.controller.isDumpStackSupported()) {
         System.out.print(ResourceManager.getMessage("console.dump.stack"));
         System.out.print(ResourceManager.getMessage("console.menu.text.top"));
         ConsoleHelper.dumpAllStacks(this.controller);
         System.out.print(ResourceManager.getMessage("console.menu.text.tail"));
         System.out.print(ResourceManager.getMessage("console.done"));
      }

   }

   private void showThreads() {
      System.out.print(ResourceManager.getMessage("console.dump.thread"));
      ThreadGroup var1 = this.controller.getMainThreadGroup();
      ConsoleHelper.dumpThreadGroup(var1);
      System.out.println(ResourceManager.getMessage("console.done"));
   }

   private void reloadPolicyConfig() {
      if (this.controller.isSecurityPolicyReloadSupported()) {
         System.out.print(ResourceManager.getMessage("console.reload.policy"));
         this.controller.reloadSecurityPolicy();
         System.out.println(ResourceManager.getMessage("console.completed"));
      }

   }

   private void reloadProxyConfig() {
      if (this.controller.isProxyConfigReloadSupported()) {
         System.out.println(ResourceManager.getMessage("console.reload.proxy"));
         this.controller.reloadProxyConfig();
         System.out.println(ResourceManager.getMessage("console.done"));
      }

   }

   private void showSystemProperties() {
      ConsoleHelper.displaySystemProperties();
   }

   private void showHelp() {
      ConsoleHelper.displayHelp(this.controller, this);
   }

   private void showClassLoaderCache() {
      if (this.controller.isDumpClassLoaderSupported()) {
         System.out.println(this.controller.dumpClassLoaders());
      }

   }

   private void clearClassLoaderCache() {
      if (this.controller.isClearClassLoaderSupported()) {
         this.controller.clearClassLoaders();
         System.out.println(ResourceManager.getMessage("console.clear.classloader"));
      }

   }

   private void clearConsole() {
      this.clear();
   }

   private void copyConsole() {
      int var1 = this.textArea.getSelection().getStart();
      int var2 = this.textArea.getSelection().getEnd();
      if (var2 - var1 <= 0) {
         ClipboardContent var3 = new ClipboardContent();
         var3.putString(this.textArea.getText());
         Clipboard.getSystemClipboard().setContent(var3);
      } else {
         this.textArea.copy();
      }

   }

   private void closeConsole() {
      if (this.controller.isIconifiedOnClose()) {
         this.dialog.setIconified(true);
      } else {
         this.dialog.hide();
      }

      this.controller.notifyConsoleClosed();
   }

   private void showMemory() {
      long var1 = Runtime.getRuntime().freeMemory() / 1024L;
      long var3 = Runtime.getRuntime().totalMemory() / 1024L;
      long var5 = (long)(100.0 / ((double)var3 / (double)var1));
      MessageFormat var7 = new MessageFormat(ResourceManager.getMessage("console.memory"));
      Object[] var8 = new Object[]{new Long(var3), new Long(var1), new Long(var5)};
      System.out.print(var7.format(var8));
      System.out.println(ResourceManager.getMessage("console.completed"));
   }

   private void runFinalize() {
      System.out.print(ResourceManager.getMessage("console.finalize"));
      System.runFinalization();
      System.out.println(ResourceManager.getMessage("console.completed"));
      this.showMemory();
   }

   private void runGC() {
      System.out.print(ResourceManager.getMessage("console.gc"));
      System.gc();
      System.out.println(ResourceManager.getMessage("console.completed"));
      this.showMemory();
   }

   private void traceLevel0() {
      ConsoleHelper.setTraceLevel(0);
   }

   private void traceLevel1() {
      ConsoleHelper.setTraceLevel(1);
   }

   private void traceLevel2() {
      ConsoleHelper.setTraceLevel(2);
   }

   private void traceLevel3() {
      ConsoleHelper.setTraceLevel(3);
   }

   private void traceLevel4() {
      ConsoleHelper.setTraceLevel(4);
   }

   private void traceLevel5() {
      ConsoleHelper.setTraceLevel(5);
   }

   private void logging() {
      if (this.controller.isLoggingSupported()) {
         System.out.println(ResourceManager.getMessage("console.log") + this.controller.toggleLogging() + ResourceManager.getMessage("console.completed"));
      }

   }

   public void clear() {
      invokeLater(new Runnable() {
         public void run() {
            FXConsole.this.textArea.setText("");
            ConsoleHelper.displayVersion(FXConsole.this.controller, FXConsole.this);
            FXConsole.this.append("\n");
            ConsoleHelper.displayHelp(FXConsole.this.controller, FXConsole.this);
         }
      });
   }

   private static void invokeLater(Runnable var0) {
      Platform.runLater(var0);
   }

   public void append(final String var1) {
      invokeLater(new Runnable() {
         public void run() {
            ScrollBar var1x = FXConsole.this.getVerticalScrollBar();
            boolean var2 = var1x == null || !var1x.isVisible() || var1x.getValue() == var1x.getMax();
            int var3 = FXConsole.this.textArea.getText().length();
            if (var3 > 1) {
               FXConsole.this.textArea.insertText(var3, var1);
            } else {
               FXConsole.this.textArea.setText(var1);
            }

            if (var2) {
               FXConsole.this.setScrollPosition();
            }

         }
      });
   }

   private void setScrollPosition() {
      ScrollBar var1 = this.getVerticalScrollBar();
      if (var1 != null) {
         double var2 = var1.getMax();
         double var4 = var1.getValue();
         if (var4 < var2) {
            var1.setValue(var2);
         }
      }

   }

   private ScrollBar getVerticalScrollBar() {
      return this.findScrollBar(this.textArea, true);
   }

   private ScrollBar findScrollBar(Parent var1, boolean var2) {
      if (var1 instanceof ScrollBar) {
         ScrollBar var6 = (ScrollBar)var1;
         return var6.getOrientation() == Orientation.VERTICAL == var2 ? (ScrollBar)var1 : null;
      } else {
         Iterator var3 = var1.getChildrenUnmodifiable().iterator();

         while(var3.hasNext()) {
            Node var4 = (Node)var3.next();
            if (var4 instanceof Parent) {
               ScrollBar var5 = this.findScrollBar((Parent)var4, var2);
               if (var5 != null) {
                  return var5;
               }
            }
         }

         return null;
      }
   }

   public void setVisible(final boolean var1) {
      invokeLater(new Runnable() {
         public void run() {
            FXConsole.this.setVisibleImpl(var1);
         }
      });
   }

   private void setVisibleImpl(boolean var1) {
      if (this.controller.isIconifiedOnClose()) {
         this.dialog.setIconified(!var1);
         this.dialog.show();
      } else {
         if (this.isVisible() != var1) {
            if (var1) {
               this.dialog.show();
            } else {
               this.dialog.hide();
            }
         }

         if (var1) {
            this.dialog.toFront();
         }
      }

   }

   public void setTitle(final String var1) {
      invokeLater(new Runnable() {
         public void run() {
            FXConsole.this.setTitleImpl(var1);
         }
      });
   }

   private void setTitleImpl(String var1) {
      this.dialog.setTitle(var1);
   }

   public String getRecentLog() {
      return "Not supported yet.";
   }

   public boolean isVisible() {
      if (this.controller.isIconifiedOnClose()) {
         return !this.dialog.isIconified();
      } else {
         return this.dialog.isShowing();
      }
   }

   public void dispose() {
   }
}
