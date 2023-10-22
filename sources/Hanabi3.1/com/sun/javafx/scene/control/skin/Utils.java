package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.TextBinding;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.tk.Toolkit;
import java.net.URL;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.function.Consumer;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class Utils {
   static final Text helper = new Text();
   static final double DEFAULT_WRAPPING_WIDTH;
   static final double DEFAULT_LINE_SPACING;
   static final String DEFAULT_TEXT;
   static final TextBoundsType DEFAULT_BOUNDS_TYPE;
   static final TextLayout layout;
   private static BreakIterator charIterator;

   static double getAscent(Font var0, TextBoundsType var1) {
      layout.setContent("", var0.impl_getNativeFont());
      layout.setWrapWidth(0.0F);
      layout.setLineSpacing(0.0F);
      if (var1 == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
         layout.setBoundsType(16384);
      } else {
         layout.setBoundsType(0);
      }

      return (double)(-layout.getBounds().getMinY());
   }

   static double getLineHeight(Font var0, TextBoundsType var1) {
      layout.setContent("", var0.impl_getNativeFont());
      layout.setWrapWidth(0.0F);
      layout.setLineSpacing(0.0F);
      if (var1 == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
         layout.setBoundsType(16384);
      } else {
         layout.setBoundsType(0);
      }

      return (double)layout.getLines()[0].getBounds().getHeight();
   }

   static double computeTextWidth(Font var0, String var1, double var2) {
      layout.setContent(var1 != null ? var1 : "", var0.impl_getNativeFont());
      layout.setWrapWidth((float)var2);
      return (double)layout.getBounds().getWidth();
   }

   static double computeTextHeight(Font var0, String var1, double var2, TextBoundsType var4) {
      return computeTextHeight(var0, var1, var2, 0.0, var4);
   }

   static double computeTextHeight(Font var0, String var1, double var2, double var4, TextBoundsType var6) {
      layout.setContent(var1 != null ? var1 : "", var0.impl_getNativeFont());
      layout.setWrapWidth((float)var2);
      layout.setLineSpacing((float)var4);
      if (var6 == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
         layout.setBoundsType(16384);
      } else {
         layout.setBoundsType(0);
      }

      return (double)layout.getBounds().getHeight();
   }

   static int computeTruncationIndex(Font var0, String var1, double var2) {
      helper.setText(var1);
      helper.setFont(var0);
      helper.setWrappingWidth(0.0);
      helper.setLineSpacing(0.0);
      Bounds var4 = helper.getLayoutBounds();
      Point2D var5 = new Point2D(var2 - 2.0, var4.getMinY() + var4.getHeight() / 2.0);
      int var6 = helper.impl_hitTestChar(var5).getCharIndex();
      helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
      helper.setLineSpacing(DEFAULT_LINE_SPACING);
      helper.setText(DEFAULT_TEXT);
      return var6;
   }

   static String computeClippedText(Font var0, String var1, double var2, OverrunStyle var4, String var5) {
      if (var0 == null) {
         throw new IllegalArgumentException("Must specify a font");
      } else {
         OverrunStyle var6 = var4 != null && var4 != OverrunStyle.CLIP ? var4 : OverrunStyle.ELLIPSIS;
         String var7 = var4 == OverrunStyle.CLIP ? "" : var5;
         if (var1 != null && !"".equals(var1)) {
            double var8 = computeTextWidth(var0, var1, 0.0);
            if (var8 - var2 < 0.0010000000474974513) {
               return var1;
            } else {
               double var10 = computeTextWidth(var0, var7, 0.0);
               double var12 = var2 - var10;
               if (var2 < var10) {
                  return "";
               } else {
                  boolean var15;
                  int var20;
                  int var21;
                  int var23;
                  int var30;
                  if (var6 != OverrunStyle.ELLIPSIS && var6 != OverrunStyle.WORD_ELLIPSIS && var6 != OverrunStyle.LEADING_ELLIPSIS && var6 != OverrunStyle.LEADING_WORD_ELLIPSIS) {
                     var15 = false;
                     boolean var28 = false;
                     var30 = -1;
                     int var18 = -1;
                     int var27 = -1;
                     int var29 = -1;
                     double var31 = 0.0;

                     for(var21 = 0; var21 <= var1.length() - 1; ++var21) {
                        char var34 = var1.charAt(var21);
                        var31 += computeTextWidth(var0, "" + var34, 0.0);
                        if (var31 > var12) {
                           break;
                        }

                        var27 = var21;
                        if (Character.isWhitespace(var34)) {
                           var30 = var21;
                        }

                        var23 = var1.length() - 1 - var21;
                        var34 = var1.charAt(var23);
                        var31 += computeTextWidth(var0, "" + var34, 0.0);
                        if (var31 > var12) {
                           break;
                        }

                        var29 = var23;
                        if (Character.isWhitespace(var34)) {
                           var18 = var23;
                        }
                     }

                     if (var27 < 0) {
                        return var7;
                     } else if (var6 == OverrunStyle.CENTER_ELLIPSIS) {
                        return var29 < 0 ? var1.substring(0, var27 + 1) + var7 : var1.substring(0, var27 + 1) + var7 + var1.substring(var29);
                     } else {
                        boolean var32 = Character.isWhitespace(var1.charAt(var27 + 1));
                        var20 = var30 != -1 && !var32 ? var30 : var27 + 1;
                        String var33 = var1.substring(0, var20);
                        if (var29 < 0) {
                           return var33 + var7;
                        } else {
                           boolean var35 = Character.isWhitespace(var1.charAt(var29 - 1));
                           var20 = var18 != -1 && !var35 ? var18 + 1 : var29;
                           String var36 = var1.substring(var20);
                           return var33 + var7 + var36;
                        }
                     }
                  } else {
                     var15 = var6 == OverrunStyle.WORD_ELLIPSIS || var6 == OverrunStyle.LEADING_WORD_ELLIPSIS;
                     if (var6 == OverrunStyle.ELLIPSIS && !(new Bidi(var1, 0)).isMixed()) {
                        var30 = computeTruncationIndex(var0, var1, var2 - var10);
                        return var30 >= 0 && var30 < var1.length() ? var1.substring(0, var30) + var7 : var1;
                     } else {
                        double var17 = 0.0;
                        int var19 = -1;
                        var20 = 0;
                        var21 = var6 != OverrunStyle.LEADING_ELLIPSIS && var6 != OverrunStyle.LEADING_WORD_ELLIPSIS ? 0 : var1.length() - 1;
                        int var22 = var21 == 0 ? var1.length() - 1 : 0;
                        var23 = var21 == 0 ? 1 : -1;
                        boolean var24 = var21 == 0 ? var21 > var22 : var21 < var22;

                        for(int var25 = var21; !var24; var25 += var23) {
                           var20 = var25;
                           char var26 = var1.charAt(var25);
                           var17 = computeTextWidth(var0, var21 == 0 ? var1.substring(0, var25 + 1) : var1.substring(var25, var21 + 1), 0.0);
                           if (Character.isWhitespace(var26)) {
                              var19 = var25;
                           }

                           if (var17 > var12) {
                              break;
                           }

                           var24 = var21 == 0 ? var25 >= var22 : var25 <= var22;
                        }

                        boolean var37 = !var15 || var19 == -1;
                        String var16 = var21 == 0 ? var1.substring(0, var37 ? var20 : var19) : var1.substring((var37 ? var20 : var19) + 1);

                        assert !var1.equals(var16);

                        return var6 != OverrunStyle.ELLIPSIS && var6 != OverrunStyle.WORD_ELLIPSIS ? var7 + var16 : var16 + var7;
                     }
                  }
               }
            }
         } else {
            return var1;
         }
      }
   }

   static String computeClippedWrappedText(Font var0, String var1, double var2, double var4, OverrunStyle var6, String var7, TextBoundsType var8) {
      if (var0 == null) {
         throw new IllegalArgumentException("Must specify a font");
      } else {
         String var9 = var6 == OverrunStyle.CLIP ? "" : var7;
         int var10 = var9.length();
         double var11 = computeTextWidth(var0, var9, 0.0);
         double var13 = computeTextHeight(var0, var9, 0.0, var8);
         if (!(var2 < var11) && !(var4 < var13)) {
            helper.setText(var1);
            helper.setFont(var0);
            helper.setWrappingWidth((double)((int)Math.ceil(var2)));
            helper.setBoundsType(var8);
            helper.setLineSpacing(0.0);
            boolean var15 = var6 == OverrunStyle.LEADING_ELLIPSIS || var6 == OverrunStyle.LEADING_WORD_ELLIPSIS;
            boolean var16 = var6 == OverrunStyle.CENTER_ELLIPSIS || var6 == OverrunStyle.CENTER_WORD_ELLIPSIS;
            boolean var17 = !var15 && !var16;
            boolean var18 = var6 == OverrunStyle.WORD_ELLIPSIS || var6 == OverrunStyle.LEADING_WORD_ELLIPSIS || var6 == OverrunStyle.CENTER_WORD_ELLIPSIS;
            String var19 = var1;
            int var20 = var1 != null ? var1.length() : 0;
            int var21 = -1;
            Point2D var22 = null;
            if (var16) {
               var22 = new Point2D((var2 - var11) / 2.0, var4 / 2.0 - helper.getBaselineOffset());
            }

            Point2D var23 = new Point2D(0.0, var4 - helper.getBaselineOffset());
            int var24 = helper.impl_hitTestChar(var23).getCharIndex();
            if (var24 >= var20) {
               helper.setBoundsType(TextBoundsType.LOGICAL);
               return var1;
            } else {
               if (var16) {
                  var24 = helper.impl_hitTestChar(var22).getCharIndex();
               }

               if (var24 > 0 && var24 < var20) {
                  int var25;
                  int var26;
                  if (var16 || var17) {
                     var25 = var24;
                     if (var16) {
                        if (var18) {
                           var26 = lastBreakCharIndex(var1, var24 + 1);
                           if (var26 >= 0) {
                              var25 = var26 + 1;
                           } else {
                              var26 = firstBreakCharIndex(var1, var24);
                              if (var26 >= 0) {
                                 var25 = var26 + 1;
                              }
                           }
                        }

                        var21 = var25 + var10;
                     }

                     var19 = var1.substring(0, var25) + var9;
                  }

                  if (var15 || var16) {
                     var25 = Math.max(0, var20 - var24 - 10);
                     if (var25 > 0 && var18) {
                        var26 = lastBreakCharIndex(var1, var25 + 1);
                        if (var26 >= 0) {
                           var25 = var26 + 1;
                        } else {
                           var26 = firstBreakCharIndex(var1, var25);
                           if (var26 >= 0) {
                              var25 = var26 + 1;
                           }
                        }
                     }

                     if (var16) {
                        var19 = var19 + var1.substring(var25);
                     } else {
                        var19 = var9 + var1.substring(var25);
                     }
                  }

                  while(true) {
                     helper.setText(var19);
                     var25 = helper.impl_hitTestChar(var23).getCharIndex();
                     if (var16 && var25 < var21) {
                        if (var25 > 0 && var19.charAt(var25 - 1) == '\n') {
                           --var25;
                        }

                        var19 = var1.substring(0, var25) + var9;
                        break;
                     }

                     if (var25 <= 0 || var25 >= var19.length()) {
                        break;
                     }

                     int var27;
                     if (var15) {
                        var26 = var10 + 1;
                        if (var18) {
                           var27 = firstBreakCharIndex(var19, var26);
                           if (var27 >= 0) {
                              var26 = var27 + 1;
                           }
                        }

                        var19 = var9 + var19.substring(var26);
                     } else if (var16) {
                        var26 = var21 + 1;
                        if (var18) {
                           var27 = firstBreakCharIndex(var19, var26);
                           if (var27 >= 0) {
                              var26 = var27 + 1;
                           }
                        }

                        var19 = var19.substring(0, var21) + var19.substring(var26);
                     } else {
                        var26 = var19.length() - var10 - 1;
                        if (var18) {
                           var27 = lastBreakCharIndex(var19, var26);
                           if (var27 >= 0) {
                              var26 = var27;
                           }
                        }

                        var19 = var19.substring(0, var26) + var9;
                     }
                  }
               }

               helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
               helper.setLineSpacing(DEFAULT_LINE_SPACING);
               helper.setText(DEFAULT_TEXT);
               helper.setBoundsType(DEFAULT_BOUNDS_TYPE);
               return var19;
            }
         } else {
            return var1;
         }
      }
   }

   private static int firstBreakCharIndex(String var0, int var1) {
      char[] var2 = var0.toCharArray();

      for(int var3 = var1; var3 < var2.length; ++var3) {
         if (isPreferredBreakCharacter(var2[var3])) {
            return var3;
         }
      }

      return -1;
   }

   private static int lastBreakCharIndex(String var0, int var1) {
      char[] var2 = var0.toCharArray();

      for(int var3 = var1; var3 >= 0; --var3) {
         if (isPreferredBreakCharacter(var2[var3])) {
            return var3;
         }
      }

      return -1;
   }

   private static boolean isPreferredBreakCharacter(char var0) {
      if (Character.isWhitespace(var0)) {
         return true;
      } else {
         switch (var0) {
            case '.':
            case ':':
            case ';':
               return true;
            default:
               return false;
         }
      }
   }

   private static boolean requiresComplexLayout(Font var0, String var1) {
      return false;
   }

   static int computeStartOfWord(Font var0, String var1, int var2) {
      if (!"".equals(var1) && var2 >= 0) {
         if (var1.length() <= var2) {
            return var1.length();
         } else if (Character.isWhitespace(var1.charAt(var2))) {
            return var2;
         } else {
            boolean var3 = requiresComplexLayout(var0, var1);
            if (var3) {
               return 0;
            } else {
               int var4 = var2;

               do {
                  --var4;
                  if (var4 < 0) {
                     return 0;
                  }
               } while(!Character.isWhitespace(var1.charAt(var4)));

               return var4 + 1;
            }
         }
      } else {
         return 0;
      }
   }

   static int computeEndOfWord(Font var0, String var1, int var2) {
      if (!var1.equals("") && var2 >= 0) {
         if (var1.length() <= var2) {
            return var1.length();
         } else if (Character.isWhitespace(var1.charAt(var2))) {
            return var2;
         } else {
            boolean var3 = requiresComplexLayout(var0, var1);
            if (var3) {
               return var1.length();
            } else {
               int var4 = var2;

               do {
                  ++var4;
                  if (var4 >= var1.length()) {
                     return var1.length();
                  }
               } while(!Character.isWhitespace(var1.charAt(var4)));

               return var4;
            }
         }
      } else {
         return 0;
      }
   }

   public static double boundedSize(double var0, double var2, double var4) {
      return Math.min(Math.max(var0, var2), Math.max(var2, var4));
   }

   static void addMnemonics(ContextMenu var0, Scene var1) {
      addMnemonics(var0, var1, false);
   }

   static void addMnemonics(ContextMenu var0, Scene var1, boolean var2) {
      if (!PlatformUtil.isMac()) {
         ContextMenuContent var3 = (ContextMenuContent)var0.getSkin().getNode();

         for(int var5 = 0; var5 < var0.getItems().size(); ++var5) {
            MenuItem var4 = (MenuItem)var0.getItems().get(var5);
            if (var4.isMnemonicParsing()) {
               TextBinding var6 = new TextBinding(var4.getText());
               int var7 = var6.getMnemonicIndex();
               if (var7 >= 0) {
                  KeyCombination var8 = var6.getMnemonicKeyCombination();
                  Mnemonic var9 = new Mnemonic(var3.getLabelAt(var5), var8);
                  var1.addMnemonic(var9);
                  var3.getLabelAt(var5).impl_setShowMnemonics(var2);
               }
            }
         }
      }

   }

   static void removeMnemonics(ContextMenu var0, Scene var1) {
      if (!PlatformUtil.isMac()) {
         ContextMenuContent var2 = (ContextMenuContent)var0.getSkin().getNode();

         for(int var4 = 0; var4 < var0.getItems().size(); ++var4) {
            MenuItem var3 = (MenuItem)var0.getItems().get(var4);
            if (var3.isMnemonicParsing()) {
               TextBinding var5 = new TextBinding(var3.getText());
               int var6 = var5.getMnemonicIndex();
               if (var6 >= 0) {
                  KeyCombination var7 = var5.getMnemonicKeyCombination();
                  ObservableList var8 = (ObservableList)var1.getMnemonics().get(var7);
                  if (var8 != null) {
                     for(int var9 = 0; var9 < var8.size(); ++var9) {
                        if (((Mnemonic)var8.get(var9)).getNode() == var2.getLabelAt(var4)) {
                           var8.remove(var9);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   static double computeXOffset(double var0, double var2, HPos var4) {
      if (var4 == null) {
         return 0.0;
      } else {
         switch (var4) {
            case LEFT:
               return 0.0;
            case CENTER:
               return (var0 - var2) / 2.0;
            case RIGHT:
               return var0 - var2;
            default:
               return 0.0;
         }
      }
   }

   static double computeYOffset(double var0, double var2, VPos var4) {
      if (var4 == null) {
         return 0.0;
      } else {
         switch (var4) {
            case TOP:
               return 0.0;
            case CENTER:
               return (var0 - var2) / 2.0;
            case BOTTOM:
               return var0 - var2;
            default:
               return 0.0;
         }
      }
   }

   public static boolean isTwoLevelFocus() {
      return Platform.isSupported(ConditionalFeature.TWO_LEVEL_FOCUS);
   }

   public static int getHitInsertionIndex(HitInfo var0, String var1) {
      int var2 = var0.getCharIndex();
      if (var1 != null && !var0.isLeading()) {
         if (charIterator == null) {
            charIterator = BreakIterator.getCharacterInstance();
         }

         charIterator.setText(var1);
         int var3 = charIterator.following(var2);
         if (var3 == -1) {
            var2 = var0.getInsertionIndex();
         } else {
            var2 = var3;
         }
      }

      return var2;
   }

   public static void executeOnceWhenPropertyIsNonNull(final ObservableValue var0, final Consumer var1) {
      if (var0 != null) {
         Object var2 = var0.getValue();
         if (var2 != null) {
            var1.accept(var2);
         } else {
            InvalidationListener var3 = new InvalidationListener() {
               public void invalidated(Observable var1x) {
                  Object var2 = var0.getValue();
                  if (var2 != null) {
                     var0.removeListener(this);
                     var1.accept(var2);
                  }

               }
            };
            var0.addListener(var3);
         }

      }
   }

   public static URL getResource(String var0) {
      return Utils.class.getResource(var0);
   }

   static {
      DEFAULT_WRAPPING_WIDTH = helper.getWrappingWidth();
      DEFAULT_LINE_SPACING = helper.getLineSpacing();
      DEFAULT_TEXT = helper.getText();
      DEFAULT_BOUNDS_TYPE = helper.getBoundsType();
      layout = Toolkit.getToolkit().getTextLayoutFactory().createLayout();
      charIterator = null;
   }
}
