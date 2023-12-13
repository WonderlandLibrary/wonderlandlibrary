package javafx.scene;

import com.sun.javafx.css.CalculatedValue;
import com.sun.javafx.css.CascadingStyle;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Style;
import com.sun.javafx.css.StyleCache;
import com.sun.javafx.css.StyleCacheEntry;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.StyleMap;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.ParsedValue;
import javafx.css.PseudoClass;
import javafx.css.StyleConverter;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

final class CssStyleHelper {
   private static final PlatformLogger LOGGER = Logging.getCSSLogger();
   private CacheContainer cacheContainer;
   private PseudoClassState triggerStates = new PseudoClassState();
   private static final Set NULL_PSEUDO_CLASS_STATE = null;
   private static final CssMetaData dummyFontProperty = new FontCssMetaData("-fx-font", Font.getDefault()) {
      public boolean isSettable(Node var1) {
         return true;
      }

      public StyleableProperty getStyleableProperty(Node var1) {
         return null;
      }
   };

   private CssStyleHelper() {
      this.triggerStates = new PseudoClassState();
   }

   static CssStyleHelper createStyleHelper(Node var0) {
      Object var1 = var0;

      int var2;
      for(var2 = 0; var1 != null; var1 = ((Styleable)var1).getStyleableParent()) {
         ++var2;
      }

      PseudoClassState[] var3 = new PseudoClassState[var2];
      StyleMap var4 = StyleManager.getInstance().findMatchingStyles(var0, var0.getSubScene(), var3);
      if (canReuseStyleHelper(var0, var4)) {
         if (var0.styleHelper.cacheContainer != null && var0.styleHelper.isUserSetFont(var0)) {
            var0.styleHelper.cacheContainer.fontSizeCache.clear();
         }

         var0.styleHelper.cacheContainer.forceSlowpath = true;
         var0.styleHelper.triggerStates.addAll(var3[0]);
         updateParentTriggerStates(var0, var2, var3);
         return var0.styleHelper;
      } else {
         if (var4 == null || var4.isEmpty()) {
            boolean var5 = false;
            List var6 = var0.getCssMetaData();
            int var7 = var6 != null ? var6.size() : 0;

            for(int var8 = 0; var8 < var7; ++var8) {
               CssMetaData var9 = (CssMetaData)var6.get(var8);
               if (var9.isInherits()) {
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               if (var0.styleHelper != null) {
                  var0.styleHelper.resetToInitialValues(var0);
               }

               return null;
            }
         }

         CssStyleHelper var10 = new CssStyleHelper();
         var10.triggerStates.addAll(var3[0]);
         updateParentTriggerStates(var0, var2, var3);
         var10.cacheContainer = new CacheContainer(var0, var4, var2);
         if (var0.styleHelper != null) {
            var0.styleHelper.resetToInitialValues(var0);
         }

         return var10;
      }
   }

   private static void updateParentTriggerStates(Styleable var0, int var1, PseudoClassState[] var2) {
      Styleable var3 = var0.getStyleableParent();

      for(int var4 = 1; var4 < var1; ++var4) {
         if (!(var3 instanceof Node)) {
            var3 = var3.getStyleableParent();
         } else {
            Node var5 = (Node)var3;
            PseudoClassState var6 = var2[var4];
            if (var6 != null && var6.size() > 0) {
               if (var5.styleHelper == null) {
                  var5.styleHelper = new CssStyleHelper();
               }

               var5.styleHelper.triggerStates.addAll(var6);
            }

            var3 = var3.getStyleableParent();
         }
      }

   }

   private boolean isUserSetFont(Styleable var1) {
      if (var1 == null) {
         return false;
      } else {
         CssMetaData var2 = this.cacheContainer != null ? this.cacheContainer.fontProp : null;
         if (var2 != null) {
            StyleableProperty var3 = var2 != null ? var2.getStyleableProperty(var1) : null;
            if (var3 != null && var3.getStyleOrigin() == StyleOrigin.USER) {
               return true;
            }
         }

         CssStyleHelper var5 = null;
         Styleable var4 = var1;

         do {
            var4 = var4.getStyleableParent();
            if (var4 instanceof Node) {
               var5 = ((Node)var4).styleHelper;
            }
         } while(var5 == null && var4 != null);

         return var5 != null ? var5.isUserSetFont(var4) : false;
      }
   }

   private static boolean isTrue(WritableValue var0) {
      return var0 != null && (Boolean)var0.getValue();
   }

   private static void setTrue(WritableValue var0) {
      if (var0 != null) {
         var0.setValue(true);
      }

   }

   private static boolean canReuseStyleHelper(Node var0, StyleMap var1) {
      if (var0 != null && var0.styleHelper != null) {
         if (var1 == null) {
            return false;
         } else {
            StyleMap var2 = var0.styleHelper.getStyleMap(var0);
            if (var2 != var1) {
               return false;
            } else if (var0.styleHelper.cacheContainer == null) {
               return true;
            } else {
               CssStyleHelper var3 = null;
               Styleable var4 = var0.getStyleableParent();
               if (var4 == null) {
                  return true;
               } else {
                  for(; var4 != null; var4 = var4.getStyleableParent()) {
                     if (var4 instanceof Node) {
                        var3 = ((Node)var4).styleHelper;
                        if (var3 != null) {
                           break;
                        }
                     }
                  }

                  if (var3 != null && var3.cacheContainer != null) {
                     int[] var5 = var3.cacheContainer.styleCacheKey.getStyleMapIds();
                     int[] var6 = var0.styleHelper.cacheContainer.styleCacheKey.getStyleMapIds();
                     if (var5.length == var6.length - 1) {
                        boolean var7 = true;

                        for(int var8 = 0; var8 < var5.length; ++var8) {
                           if (var6[var8 + 1] != var5[var8]) {
                              var7 = false;
                              break;
                           }
                        }

                        return var7;
                     }
                  }

                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   private void resetToInitialValues(Styleable var1) {
      if (this.cacheContainer != null && this.cacheContainer.cssSetProperties != null && !this.cacheContainer.cssSetProperties.isEmpty()) {
         HashSet var2 = new HashSet(this.cacheContainer.cssSetProperties.entrySet());
         this.cacheContainer.cssSetProperties.clear();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            CssMetaData var5 = (CssMetaData)var4.getKey();
            StyleableProperty var6 = var5.getStyleableProperty(var1);
            StyleOrigin var7 = var6.getStyleOrigin();
            if (var7 != null && var7 != StyleOrigin.USER) {
               CalculatedValue var8 = (CalculatedValue)var4.getValue();
               var6.applyStyle(var8.getOrigin(), var8.getValue());
            }
         }

      }
   }

   private StyleMap getStyleMap(Styleable var1) {
      return this.cacheContainer != null && var1 != null ? this.cacheContainer.getStyleMap(var1) : null;
   }

   boolean pseudoClassStateChanged(PseudoClass var1) {
      return this.triggerStates.contains(var1);
   }

   private Set[] getTransitionStates(Node var1) {
      if (this.cacheContainer == null) {
         return null;
      } else {
         int var2 = 0;

         Object var3;
         for(var3 = var1; var3 != null; var3 = ((Node)var3).getParent()) {
            ++var2;
         }

         PseudoClassState[] var4 = new PseudoClassState[var2];
         int var5 = 0;

         for(var3 = var1; var3 != null; var3 = ((Node)var3).getParent()) {
            CssStyleHelper var6 = var3 instanceof Node ? ((Node)var3).styleHelper : null;
            if (var6 != null) {
               ObservableSet var7 = ((Node)var3).pseudoClassStates;
               var4[var5] = new PseudoClassState();
               var4[var5].addAll(var7);
               var4[var5].retainAll(var6.triggerStates);
               ++var5;
            }
         }

         PseudoClassState[] var8 = new PseudoClassState[var5];
         System.arraycopy(var4, 0, var8, 0, var5);
         return var8;
      }
   }

   void transitionToState(Node var1) {
      if (this.cacheContainer != null) {
         StyleMap var2 = this.getStyleMap(var1);
         if (var2 == null) {
            this.cacheContainer = null;
            var1.impl_reapplyCSS();
         } else {
            boolean var3 = var2.isEmpty();
            StyleCache var4 = StyleManager.getInstance().getSharedCache(var1, var1.getSubScene(), this.cacheContainer.styleCacheKey);
            if (var4 == null) {
               this.cacheContainer = null;
               var1.impl_reapplyCSS();
            } else {
               Set[] var5 = this.getTransitionStates(var1);
               StyleCacheEntry.Key var6 = new StyleCacheEntry.Key(var5, Font.getDefault());
               CalculatedValue var7 = (CalculatedValue)this.cacheContainer.fontSizeCache.get(var6);
               if (var7 == null) {
                  var7 = this.lookupFont(var1, "-fx-font", var2, var7);
                  if (var7 == CalculatedValue.SKIP) {
                     var7 = this.getCachedFont(var1.getStyleableParent());
                  }

                  if (var7 == null) {
                     var7 = new CalculatedValue(Font.getDefault(), (StyleOrigin)null, false);
                  }

                  this.cacheContainer.fontSizeCache.put(var6, var7);
               }

               Font var8 = (Font)var7.getValue();
               StyleCacheEntry.Key var9 = new StyleCacheEntry.Key(var5, var8);
               StyleCacheEntry var10 = var4.getStyleCacheEntry(var9);
               boolean var11 = var10 != null;
               if (var10 == null) {
                  var10 = new StyleCacheEntry();
                  var4.addStyleCacheEntry(var9, var10);
               }

               List var12 = var1.getCssMetaData();
               int var13 = var12.size();
               boolean var14 = this.cacheContainer.forceSlowpath;
               this.cacheContainer.forceSlowpath = false;
               CssError.setCurrentScene(var1.getScene());

               for(int var15 = 0; var15 < var13; ++var15) {
                  CssMetaData var16 = (CssMetaData)var12.get(var15);
                  if ((!var3 || var16.isInherits()) && var16.isSettable(var1)) {
                     String var17 = var16.getProperty();
                     CalculatedValue var18 = var10.get(var17);
                     boolean var19 = var11 && var18 == null && var14;
                     boolean var20 = !var11 && var18 == null || var19;
                     if (var11 && !var19) {
                        if (var18 == CalculatedValue.SKIP) {
                           continue;
                        }
                     } else if (var18 == null) {
                        var18 = this.lookup(var1, var16, var2, var5[0], var1, var7);
                        if (var18 == null) {
                           assert false : "lookup returned null for " + var17;
                           continue;
                        }
                     }

                     StyleableProperty var22;
                     CalculatedValue var26;
                     try {
                        if (var18 != null && var18 != CalculatedValue.SKIP) {
                           if (var20) {
                              var10.put(var17, var18);
                           }

                           StyleableProperty var32 = var16.getStyleableProperty(var1);
                           StyleOrigin var33 = var32.getStyleOrigin();
                           StyleOrigin var34 = var18.getOrigin();
                           if (var34 == null) {
                              assert false : var32.toString();
                           } else if (var33 != StyleOrigin.USER || var34 != StyleOrigin.USER_AGENT) {
                              Object var35 = var18.getValue();
                              Object var37 = var32.getValue();
                              if (var33 == var34) {
                                 if (var37 != null) {
                                    if (var37.equals(var35)) {
                                       continue;
                                    }
                                 } else if (var35 == null) {
                                    continue;
                                 }
                              }

                              if (LOGGER.isLoggable(Level.FINER)) {
                                 LOGGER.finer(var17 + ", call applyStyle: " + var32 + ", value =" + var35 + ", originOfCalculatedValue=" + var34);
                              }

                              var32.applyStyle(var34, var35);
                              if (!this.cacheContainer.cssSetProperties.containsKey(var16)) {
                                 var26 = new CalculatedValue(var37, var33, false);
                                 this.cacheContainer.cssSetProperties.put(var16, var26);
                              }
                           }
                        } else {
                           CalculatedValue var21 = (CalculatedValue)this.cacheContainer.cssSetProperties.get(var16);
                           if (var21 != null) {
                              var22 = var16.getStyleableProperty(var1);
                              if (var22.getStyleOrigin() != StyleOrigin.USER) {
                                 var22.applyStyle(var21.getOrigin(), var21.getValue());
                              }
                           }
                        }
                     } catch (Exception var31) {
                        var22 = var16.getStyleableProperty(var1);
                        String var23 = String.format("Failed to set css [%s] on [%s] due to '%s'\n", var16.getProperty(), var22, var31.getMessage());
                        ObservableList var24 = null;
                        if ((var24 = StyleManager.getErrors()) != null) {
                           CssError.PropertySetError var25 = new CssError.PropertySetError(var16, var1, var23);
                           var24.add(var25);
                        }

                        PlatformLogger var36 = Logging.getCSSLogger();
                        if (var36.isLoggable(Level.WARNING)) {
                           var36.warning(var23);
                        }

                        var10.put(var17, CalculatedValue.SKIP);
                        var26 = null;
                        if (this.cacheContainer != null && this.cacheContainer.cssSetProperties != null) {
                           var26 = (CalculatedValue)this.cacheContainer.cssSetProperties.get(var16);
                        }

                        Object var27 = var26 != null ? var26.getValue() : var16.getInitialValue(var1);
                        StyleOrigin var28 = var26 != null ? var26.getOrigin() : null;

                        try {
                           var22.applyStyle(var28, var27);
                        } catch (Exception var30) {
                           if (var36.isLoggable(Level.SEVERE)) {
                              var36.severe(String.format("Could not reset [%s] on [%s] due to %s\n", var16.getProperty(), var22, var31.getMessage()));
                           }
                        }
                     }
                  }
               }

               CssError.setCurrentScene((Scene)null);
            }
         }
      }
   }

   private CascadingStyle getStyle(Styleable var1, String var2, StyleMap var3, Set var4) {
      if (var3 != null && !var3.isEmpty()) {
         Map var5 = var3.getCascadingStyles();
         if (var5 != null && !var5.isEmpty()) {
            List var6 = (List)var5.get(var2);
            if (var6 != null && !var6.isEmpty()) {
               CascadingStyle var7 = null;
               int var8 = var6 == null ? 0 : var6.size();

               for(int var9 = 0; var9 < var8; ++var9) {
                  CascadingStyle var10 = (CascadingStyle)var6.get(var9);
                  Selector var11 = var10 == null ? null : var10.getSelector();
                  if (var11 != null && var11.stateMatches(var1, var4)) {
                     var7 = var10;
                     break;
                  }
               }

               return var7;
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private CalculatedValue lookup(Styleable var1, CssMetaData var2, StyleMap var3, Set var4, Styleable var5, CalculatedValue var6) {
      if (var2.getConverter() == FontConverter.getInstance()) {
         return this.lookupFont(var1, var2.getProperty(), var3, var6);
      } else {
         String var7 = var2.getProperty();
         CascadingStyle var8 = this.getStyle(var1, var7, var3, var4);
         List var9 = var2.getSubProperties();
         int var10 = var9 != null ? var9.size() : 0;
         if (var8 != null) {
            if (var8.getOrigin() == StyleOrigin.USER_AGENT) {
               StyleableProperty var19 = var2.getStyleableProperty(var5);
               if (var19 != null && var19.getStyleOrigin() == StyleOrigin.USER) {
                  return CalculatedValue.SKIP;
               }
            }

            ParsedValueImpl var20 = var8.getParsedValueImpl();
            if (var20 != null && "inherit".equals(var20.getValue())) {
               var8 = this.getInheritedStyle(var1, var7);
               if (var8 == null) {
                  return CalculatedValue.SKIP;
               }
            }

            return this.calculateValue(var8, var1, var2, var3, var4, var5, var6);
         } else if (var10 == 0) {
            return this.handleNoStyleFound(var1, var2, var3, var4, var5, var6);
         } else {
            HashMap var11 = null;
            StyleOrigin var12 = null;
            boolean var13 = false;

            CalculatedValue var16;
            for(int var14 = 0; var14 < var10; ++var14) {
               CssMetaData var15 = (CssMetaData)var9.get(var14);
               var16 = this.lookup(var1, var15, var3, var4, var5, var6);
               if (var16 != CalculatedValue.SKIP) {
                  if (var11 == null) {
                     var11 = new HashMap();
                  }

                  label126: {
                     var11.put(var15, var16.getValue());
                     if (var12 != null && var16.getOrigin() != null) {
                        if (var12.compareTo(var16.getOrigin()) >= 0) {
                           break label126;
                        }
                     } else if (var16.getOrigin() == null) {
                        break label126;
                     }

                     var12 = var16.getOrigin();
                  }

                  var13 = var13 || var16.isRelative();
               }
            }

            if (var11 != null && !var11.isEmpty()) {
               try {
                  StyleConverter var21 = var2.getConverter();
                  if (var21 instanceof StyleConverterImpl) {
                     Object var23 = ((StyleConverterImpl)var21).convert(var11);
                     return new CalculatedValue(var23, var12, var13);
                  } else {
                     assert false;

                     return CalculatedValue.SKIP;
                  }
               } catch (ClassCastException var18) {
                  String var22 = this.formatExceptionMessage(var1, var2, (Style)null, var18);
                  var16 = null;
                  ObservableList var24;
                  if ((var24 = StyleManager.getErrors()) != null) {
                     CssError.PropertySetError var17 = new CssError.PropertySetError(var2, var1, var22);
                     var24.add(var17);
                  }

                  if (LOGGER.isLoggable(Level.WARNING)) {
                     LOGGER.warning(var22);
                     LOGGER.fine("caught: ", var18);
                     LOGGER.fine("styleable = " + var2);
                     LOGGER.fine("node = " + var1.toString());
                  }

                  return CalculatedValue.SKIP;
               }
            } else {
               return this.handleNoStyleFound(var1, var2, var3, var4, var5, var6);
            }
         }
      }
   }

   private CalculatedValue handleNoStyleFound(Styleable var1, CssMetaData var2, StyleMap var3, Set var4, Styleable var5, CalculatedValue var6) {
      if (var2.isInherits()) {
         StyleableProperty var7 = var2.getStyleableProperty(var1);
         StyleOrigin var8 = var7 != null ? var7.getStyleOrigin() : null;
         if (var8 == StyleOrigin.USER) {
            return CalculatedValue.SKIP;
         } else {
            CascadingStyle var9 = this.getInheritedStyle(var1, var2.getProperty());
            if (var9 == null) {
               return CalculatedValue.SKIP;
            } else {
               CalculatedValue var10 = this.calculateValue(var9, var1, var2, var3, var4, var5, var6);
               return var10;
            }
         }
      } else {
         return CalculatedValue.SKIP;
      }
   }

   private CascadingStyle getInheritedStyle(Styleable var1, String var2) {
      for(Styleable var3 = var1 != null ? var1.getStyleableParent() : null; var3 != null; var3 = var3.getStyleableParent()) {
         CssStyleHelper var4 = var3 instanceof Node ? ((Node)var3).styleHelper : null;
         if (var4 != null) {
            StyleMap var5 = var4.getStyleMap(var3);
            ObservableSet var6 = ((Node)var3).pseudoClassStates;
            CascadingStyle var7 = var4.getStyle(var3, var2, var5, var6);
            if (var7 != null) {
               ParsedValueImpl var8 = var7.getParsedValueImpl();
               if ("inherit".equals(var8.getValue())) {
                  return this.getInheritedStyle(var3, var2);
               }

               return var7;
            }

            return null;
         }
      }

      return null;
   }

   private CascadingStyle resolveRef(Styleable var1, String var2, StyleMap var3, Set var4) {
      CascadingStyle var5 = this.getStyle(var1, var2, var3, var4);
      if (var5 != null) {
         return var5;
      } else if (var4 != null && var4.size() > 0) {
         return this.resolveRef(var1, var2, var3, NULL_PSEUDO_CLASS_STATE);
      } else {
         Styleable var6 = var1.getStyleableParent();
         CssStyleHelper var7 = null;
         if (var6 != null && var6 instanceof Node) {
            var7 = ((Node)var6).styleHelper;
         }

         while(var6 != null && var7 == null) {
            var6 = var6.getStyleableParent();
            if (var6 != null && var6 instanceof Node) {
               var7 = ((Node)var6).styleHelper;
            }
         }

         if (var6 != null && var7 != null) {
            StyleMap var8 = var7.getStyleMap(var6);
            ObservableSet var9 = var6 instanceof Node ? ((Node)var6).pseudoClassStates : var1.getPseudoClassStates();
            return var7.resolveRef(var6, var2, var8, var9);
         } else {
            return null;
         }
      }
   }

   private ParsedValueImpl resolveLookups(Styleable var1, ParsedValueImpl var2, StyleMap var3, Set var4, ObjectProperty var5, Set var6) {
      Object var7;
      if (var2.isLookup()) {
         var7 = var2.getValue();
         if (var7 instanceof String) {
            String var8 = ((String)var7).toLowerCase(Locale.ROOT);
            CascadingStyle var9 = this.resolveRef(var1, var8, var3, var4);
            if (var9 != null) {
               if (var6.contains(var9.getParsedValueImpl())) {
                  if (LOGGER.isLoggable(Level.WARNING)) {
                     LOGGER.warning("Loop detected in " + var9.getRule().toString() + " while resolving '" + var8 + "'");
                  }

                  throw new IllegalArgumentException("Loop detected in " + var9.getRule().toString() + " while resolving '" + var8 + "'");
               }

               var6.add(var2);
               StyleOrigin var17 = (StyleOrigin)var5.get();
               StyleOrigin var18 = var9.getOrigin();
               if (var18 != null && (var17 == null || var17.compareTo(var18) < 0)) {
                  var5.set(var18);
               }

               ParsedValueImpl var12 = this.resolveLookups(var1, var9.getParsedValueImpl(), var3, var4, var5, var6);
               if (var6 != null) {
                  var6.remove(var2);
               }

               return var12;
            }
         }
      }

      if (!var2.isContainsLookups()) {
         return var2;
      } else {
         var7 = var2.getValue();
         int var10;
         if (!(var7 instanceof ParsedValueImpl[][])) {
            if (var7 instanceof ParsedValueImpl[]) {
               ParsedValueImpl[] var14 = (ParsedValueImpl[])((ParsedValueImpl[])var7);
               ParsedValueImpl[] var16 = new ParsedValueImpl[var14.length];

               for(var10 = 0; var10 < var14.length; ++var10) {
                  if (var14[var10] != null) {
                     var16[var10] = this.resolveLookups(var1, var14[var10], var3, var4, var5, var6);
                  }
               }

               var6.clear();
               return new ParsedValueImpl(var16, var2.getConverter(), false);
            } else {
               return var2;
            }
         } else {
            ParsedValueImpl[][] var13 = (ParsedValueImpl[][])((ParsedValueImpl[][])var7);
            ParsedValueImpl[][] var15 = new ParsedValueImpl[var13.length][0];

            for(var10 = 0; var10 < var13.length; ++var10) {
               var15[var10] = new ParsedValueImpl[var13[var10].length];

               for(int var11 = 0; var11 < var13[var10].length; ++var11) {
                  if (var13[var10][var11] != null) {
                     var15[var10][var11] = this.resolveLookups(var1, var13[var10][var11], var3, var4, var5, var6);
                  }
               }
            }

            var6.clear();
            return new ParsedValueImpl(var15, var2.getConverter(), false);
         }
      }
   }

   private String getUnresolvedLookup(ParsedValueImpl var1) {
      Object var2 = var1.getValue();
      if (var1.isLookup() && var2 instanceof String) {
         return (String)var2;
      } else {
         int var4;
         if (var2 instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] var3 = (ParsedValueImpl[][])((ParsedValueImpl[][])var2);

            for(var4 = 0; var4 < var3.length; ++var4) {
               for(int var5 = 0; var5 < var3[var4].length; ++var5) {
                  if (var3[var4][var5] != null) {
                     String var6 = this.getUnresolvedLookup(var3[var4][var5]);
                     if (var6 != null) {
                        return var6;
                     }
                  }
               }
            }
         } else if (var2 instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] var7 = (ParsedValueImpl[])((ParsedValueImpl[])var2);

            for(var4 = 0; var4 < var7.length; ++var4) {
               if (var7[var4] != null) {
                  String var8 = this.getUnresolvedLookup(var7[var4]);
                  if (var8 != null) {
                     return var8;
                  }
               }
            }
         }

         return null;
      }
   }

   private String formatUnresolvedLookupMessage(Styleable var1, CssMetaData var2, Style var3, ParsedValueImpl var4, ClassCastException var5) {
      String var6 = var4 != null && var4.isContainsLookups() ? this.getUnresolvedLookup(var4) : null;
      StringBuilder var7 = new StringBuilder();
      if (var6 != null) {
         var7.append("Could not resolve '").append(var6).append("'").append(" while resolving lookups for '").append(var2.getProperty()).append("'");
      } else {
         var7.append("Caught '").append(var5).append("'").append(" while converting value for '").append(var2.getProperty()).append("'");
      }

      Rule var8 = var3 != null ? var3.getDeclaration().getRule() : null;
      Stylesheet var9 = var8 != null ? var8.getStylesheet() : null;
      String var10 = var9 != null ? var9.getUrl() : null;
      if (var10 != null) {
         var7.append(" from rule '").append(var3.getSelector()).append("' in stylesheet ").append(var10);
      } else if (var9 != null && StyleOrigin.INLINE == var9.getOrigin()) {
         var7.append(" from inline style on ").append(var1.toString());
      }

      return var7.toString();
   }

   private String formatExceptionMessage(Styleable var1, CssMetaData var2, Style var3, Exception var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append("Caught ").append(String.valueOf(var4));
      if (var2 != null) {
         var5.append("'").append(" while calculating value for '").append(var2.getProperty()).append("'");
      }

      if (var3 != null) {
         Rule var6 = var3.getDeclaration().getRule();
         Stylesheet var7 = var6 != null ? var6.getStylesheet() : null;
         String var8 = var7 != null ? var7.getUrl() : null;
         if (var8 != null) {
            var5.append(" from rule '").append(var3.getSelector()).append("' in stylesheet ").append(var8);
         } else if (var1 != null && var7 != null && StyleOrigin.INLINE == var7.getOrigin()) {
            var5.append(" from inline style on ").append(var1.toString());
         } else {
            var5.append(" from style '").append(String.valueOf(var3)).append("'");
         }
      }

      return var5.toString();
   }

   private CalculatedValue calculateValue(CascadingStyle var1, Styleable var2, CssMetaData var3, StyleMap var4, Set var5, Styleable var6, CalculatedValue var7) {
      ParsedValueImpl var8 = var1.getParsedValueImpl();
      if (var8 != null && !"null".equals(var8.getValue()) && !"none".equals(var8.getValue())) {
         ParsedValueImpl var9 = null;

         String var11;
         ObservableList var12;
         CssError.PropertySetError var13;
         try {
            SimpleObjectProperty var10 = new SimpleObjectProperty(var1.getOrigin());
            var9 = this.resolveLookups(var2, var8, var4, var5, var10, new HashSet());
            var11 = var3.getProperty();
            var12 = null;
            boolean var22 = "-fx-font".equals(var11) || "-fx-font-size".equals(var11);
            boolean var14 = ParsedValueImpl.containsFontRelativeSize(var9, var22);
            Font var15 = null;
            if (var14 && var22 && (var7 == null || var7.isRelative())) {
               Styleable var16 = var2;
               CalculatedValue var17 = var7;

               do {
                  CalculatedValue var18 = this.getCachedFont(var16.getStyleableParent());
                  if (var18 != null) {
                     if (var18.isRelative()) {
                        if (var17 != null && !var18.equals(var17)) {
                           var15 = (Font)var18.getValue();
                        } else {
                           var17 = var18;
                        }
                     } else {
                        var15 = (Font)var18.getValue();
                     }
                  }
               } while(var15 == null && (var16 = var16.getStyleableParent()) != null);
            }

            if (var15 == null) {
               if (var7 != null && !var7.isRelative()) {
                  var15 = (Font)var7.getValue();
               } else {
                  var15 = Font.getDefault();
               }
            }

            StyleConverter var24 = var3.getConverter();
            Object var23;
            if (var24 == StyleConverter.getInsetsConverter()) {
               if (var9.getValue() instanceof ParsedValue) {
                  var9 = new ParsedValueImpl(new ParsedValue[]{(ParsedValue)var9.getValue()}, (StyleConverter)null, false);
               }

               var23 = var24.convert(var9, var15);
            } else if (var9.getConverter() != null) {
               var23 = var9.convert(var15);
            } else {
               var23 = var3.getConverter().convert(var9, var15);
            }

            StyleOrigin var25 = (StyleOrigin)var10.get();
            return new CalculatedValue(var23, var25, var14);
         } catch (ClassCastException var19) {
            var11 = this.formatUnresolvedLookupMessage(var2, var3, var1.getStyle(), var9, var19);
            var12 = null;
            if ((var12 = StyleManager.getErrors()) != null) {
               var13 = new CssError.PropertySetError(var3, var2, var11);
               var12.add(var13);
            }

            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var11);
               LOGGER.fine("node = " + var2.toString());
               LOGGER.fine("cssMetaData = " + var3);
               LOGGER.fine("styles = " + getMatchingStyles(var2, var3));
            }

            return CalculatedValue.SKIP;
         } catch (IllegalArgumentException var20) {
            var11 = this.formatExceptionMessage(var2, var3, var1.getStyle(), var20);
            var12 = null;
            if ((var12 = StyleManager.getErrors()) != null) {
               var13 = new CssError.PropertySetError(var3, var2, var11);
               var12.add(var13);
            }

            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var11);
               LOGGER.fine("caught: ", var20);
               LOGGER.fine("styleable = " + var3);
               LOGGER.fine("node = " + var2.toString());
            }

            return CalculatedValue.SKIP;
         } catch (NullPointerException var21) {
            var11 = this.formatExceptionMessage(var2, var3, var1.getStyle(), var21);
            var12 = null;
            if ((var12 = StyleManager.getErrors()) != null) {
               var13 = new CssError.PropertySetError(var3, var2, var11);
               var12.add(var13);
            }

            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning(var11);
               LOGGER.fine("caught: ", var21);
               LOGGER.fine("styleable = " + var3);
               LOGGER.fine("node = " + var2.toString());
            }

            return CalculatedValue.SKIP;
         }
      } else {
         return new CalculatedValue((Object)null, var1.getOrigin(), false);
      }
   }

   private CalculatedValue getCachedFont(Styleable var1) {
      if (!(var1 instanceof Node)) {
         return null;
      } else {
         CalculatedValue var2 = null;
         Node var3 = (Node)var1;
         CssStyleHelper var4 = var3.styleHelper;
         if (var4 != null && var4.cacheContainer != null) {
            CacheContainer var5 = var4.cacheContainer;
            if (var5 != null && var5.fontSizeCache != null && !var5.fontSizeCache.isEmpty()) {
               Set[] var6 = var4.getTransitionStates(var3);
               StyleCacheEntry.Key var7 = new StyleCacheEntry.Key(var6, Font.getDefault());
               var2 = (CalculatedValue)var5.fontSizeCache.get(var7);
            }

            if (var2 == null) {
               StyleMap var8 = var4.getStyleMap(var3);
               var2 = var4.lookupFont(var3, "-fx-font", var8, (CalculatedValue)null);
            }
         } else {
            var2 = this.getCachedFont(var3.getStyleableParent());
         }

         return var2 != CalculatedValue.SKIP ? var2 : null;
      }
   }

   FontPosture getFontPosture(Font var1) {
      if (var1 == null) {
         return FontPosture.REGULAR;
      } else {
         String var2 = var1.getName().toLowerCase(Locale.ROOT);
         return var2.contains("italic") ? FontPosture.ITALIC : FontPosture.REGULAR;
      }
   }

   FontWeight getFontWeight(Font var1) {
      if (var1 == null) {
         return FontWeight.NORMAL;
      } else {
         String var2 = var1.getName().toLowerCase(Locale.ROOT);
         if (var2.contains("bold")) {
            if (var2.contains("extra")) {
               return FontWeight.EXTRA_BOLD;
            } else if (var2.contains("ultra")) {
               return FontWeight.EXTRA_BOLD;
            } else if (var2.contains("semi")) {
               return FontWeight.SEMI_BOLD;
            } else {
               return var2.contains("demi") ? FontWeight.SEMI_BOLD : FontWeight.BOLD;
            }
         } else if (var2.contains("light")) {
            if (var2.contains("extra")) {
               return FontWeight.EXTRA_LIGHT;
            } else {
               return var2.contains("ultra") ? FontWeight.EXTRA_LIGHT : FontWeight.LIGHT;
            }
         } else if (var2.contains("black")) {
            return FontWeight.BLACK;
         } else if (var2.contains("heavy")) {
            return FontWeight.BLACK;
         } else {
            return var2.contains("medium") ? FontWeight.MEDIUM : FontWeight.NORMAL;
         }
      }
   }

   String getFontFamily(Font var1) {
      return var1 == null ? Font.getDefault().getFamily() : var1.getFamily();
   }

   Font deriveFont(Font var1, String var2, FontWeight var3, FontPosture var4, double var5) {
      if (var1 != null && var2 == null) {
         var2 = this.getFontFamily(var1);
      } else if (var2 != null) {
         var2 = Utils.stripQuotes(var2);
      }

      if (var1 != null && var3 == null) {
         var3 = this.getFontWeight(var1);
      }

      if (var1 != null && var4 == null) {
         var4 = this.getFontPosture(var1);
      }

      if (var1 != null && var5 <= 0.0) {
         var5 = var1.getSize();
      }

      return Font.font(var2, var3, var4, var5);
   }

   CalculatedValue lookupFont(Styleable var1, String var2, StyleMap var3, CalculatedValue var4) {
      StyleOrigin var5 = null;
      int var6 = 0;
      boolean var7 = false;
      String var8 = null;
      double var9 = -1.0;
      FontWeight var11 = null;
      FontPosture var12 = null;
      CalculatedValue var13 = var4;
      ObservableSet var14 = var1 instanceof Node ? ((Node)var1).pseudoClassStates : var1.getPseudoClassStates();
      if (this.cacheContainer.fontProp != null) {
         StyleableProperty var15 = this.cacheContainer.fontProp.getStyleableProperty(var1);
         StyleOrigin var16 = var15.getStyleOrigin();
         Font var17 = (Font)var15.getValue();
         if (var17 == null) {
            var17 = Font.getDefault();
         }

         if (var16 == StyleOrigin.USER) {
            var5 = var16;
            var8 = this.getFontFamily(var17);
            var9 = var17.getSize();
            var11 = this.getFontWeight(var17);
            var12 = this.getFontPosture(var17);
            var13 = new CalculatedValue(var17, var16, false);
         }
      }

      CalculatedValue var23 = this.getCachedFont(var1.getStyleableParent());
      if (var23 == null) {
         var23 = new CalculatedValue(Font.getDefault(), (StyleOrigin)null, false);
      }

      CascadingStyle var24 = this.getStyle(var1, var2, var3, var14);
      if (var24 == null && var5 != StyleOrigin.USER) {
         for(Styleable var25 = var1 != null ? var1.getStyleableParent() : null; var25 != null; var25 = var25.getStyleableParent()) {
            CssStyleHelper var18 = var25 instanceof Node ? ((Node)var25).styleHelper : null;
            if (var18 != null) {
               ++var6;
               StyleMap var19 = var18.getStyleMap(var25);
               ObservableSet var20 = ((Node)var25).pseudoClassStates;
               CascadingStyle var21 = var18.getStyle(var25, var2, var19, var20);
               if (var21 != null) {
                  ParsedValueImpl var22 = var21.getParsedValueImpl();
                  if (!"inherit".equals(var22.getValue())) {
                     var24 = var21;
                     break;
                  }
               }
            }
         }
      }

      if (var24 != null && (var5 == null || var5.compareTo(var24.getOrigin()) <= 0)) {
         CalculatedValue var27 = this.calculateValue(var24, var1, dummyFontProperty, var3, var14, var1, var23);
         if (var27.getValue() instanceof Font) {
            var5 = var27.getOrigin();
            Font var26 = (Font)var27.getValue();
            var8 = this.getFontFamily(var26);
            var9 = var26.getSize();
            var11 = this.getFontWeight(var26);
            var12 = this.getFontPosture(var26);
            var13 = var27;
            var7 = true;
         }
      }

      CascadingStyle var29 = this.getStyle(var1, var2.concat("-size"), var3, var14);
      if (var29 != null) {
         if (var24 != null && var24.compareTo(var29) < 0) {
            var29 = null;
         } else if (var5 == StyleOrigin.USER && StyleOrigin.USER.compareTo(var29.getOrigin()) > 0) {
            var29 = null;
         }
      } else if (var5 != StyleOrigin.USER) {
         var29 = this.lookupInheritedFontProperty(var1, var2.concat("-size"), var3, var6, var24);
      }

      if (var29 != null) {
         CalculatedValue var28 = this.calculateValue(var29, var1, dummyFontProperty, var3, var14, var1, var23);
         if (var28.getValue() instanceof Double) {
            if (var5 == null || var5.compareTo(var29.getOrigin()) <= 0) {
               var5 = var28.getOrigin();
            }

            var9 = (Double)var28.getValue();
            boolean var31;
            Font var33;
            if (var13 == null) {
               var31 = var28.isRelative();
               var33 = this.deriveFont(Font.getDefault(), var8, var11, var12, var9);
               var13 = new CalculatedValue(var33, var5, var31);
            } else {
               var31 = var13.isRelative() || var28.isRelative();
               var33 = this.deriveFont((Font)var13.getValue(), var8, var11, var12, var9);
               var13 = new CalculatedValue(var33, var5, var31);
            }

            var7 = true;
         }
      }

      if (var4 == null) {
         return var13 != null ? var13 : CalculatedValue.SKIP;
      } else {
         CascadingStyle var30 = this.getStyle(var1, var2.concat("-weight"), var3, var14);
         if (var30 != null) {
            if (var24 != null && var24.compareTo(var30) < 0) {
               var30 = null;
            }
         } else if (var5 != StyleOrigin.USER) {
            var30 = this.lookupInheritedFontProperty(var1, var2.concat("-weight"), var3, var6, var24);
         }

         if (var30 != null) {
            CalculatedValue var32 = this.calculateValue(var30, var1, dummyFontProperty, var3, var14, var1, (CalculatedValue)null);
            if (var32.getValue() instanceof FontWeight) {
               if (var5 == null || var5.compareTo(var30.getOrigin()) <= 0) {
                  var5 = var32.getOrigin();
               }

               var11 = (FontWeight)var32.getValue();
               var7 = true;
            }
         }

         CascadingStyle var34 = this.getStyle(var1, var2.concat("-style"), var3, var14);
         if (var34 != null) {
            if (var24 != null && var24.compareTo(var34) < 0) {
               var34 = null;
            }
         } else if (var5 != StyleOrigin.USER) {
            var34 = this.lookupInheritedFontProperty(var1, var2.concat("-style"), var3, var6, var24);
         }

         if (var34 != null) {
            CalculatedValue var35 = this.calculateValue(var34, var1, dummyFontProperty, var3, var14, var1, (CalculatedValue)null);
            if (var35.getValue() instanceof FontPosture) {
               if (var5 == null || var5.compareTo(var34.getOrigin()) <= 0) {
                  var5 = var35.getOrigin();
               }

               var12 = (FontPosture)var35.getValue();
               var7 = true;
            }
         }

         CascadingStyle var36 = this.getStyle(var1, var2.concat("-family"), var3, var14);
         if (var36 != null) {
            if (var24 != null && var24.compareTo(var36) < 0) {
               var36 = null;
            }
         } else if (var5 != StyleOrigin.USER) {
            var36 = this.lookupInheritedFontProperty(var1, var2.concat("-family"), var3, var6, var24);
         }

         if (var36 != null) {
            CalculatedValue var37 = this.calculateValue(var36, var1, dummyFontProperty, var3, var14, var1, (CalculatedValue)null);
            if (var37.getValue() instanceof String) {
               if (var5 == null || var5.compareTo(var36.getOrigin()) <= 0) {
                  var5 = var37.getOrigin();
               }

               var8 = (String)var37.getValue();
               var7 = true;
            }
         }

         if (var7) {
            Font var38 = var13 != null ? (Font)var13.getValue() : Font.getDefault();
            Font var39 = this.deriveFont(var38, var8, var11, var12, var9);
            return new CalculatedValue(var39, var5, false);
         } else {
            return CalculatedValue.SKIP;
         }
      }
   }

   private CascadingStyle lookupInheritedFontProperty(Styleable var1, String var2, StyleMap var3, int var4, CascadingStyle var5) {
      Styleable var6 = var1 != null ? var1.getStyleableParent() : null;

      for(int var7 = var4; var6 != null && var7 > 0; var6 = var6.getStyleableParent()) {
         CssStyleHelper var8 = var6 instanceof Node ? ((Node)var6).styleHelper : null;
         if (var8 != null) {
            --var7;
            StyleMap var9 = var8.getStyleMap(var6);
            ObservableSet var10 = ((Node)var6).pseudoClassStates;
            CascadingStyle var11 = var8.getStyle(var6, var2, var9, var10);
            if (var11 != null) {
               if (var5 != null && var7 == 0 && var5.compareTo(var11) < 0) {
                  return null;
               }

               ParsedValueImpl var12 = var11.getParsedValueImpl();
               if (!"inherit".equals(var12.getValue())) {
                  return var11;
               }
            }
         }
      }

      return null;
   }

   static List getMatchingStyles(Styleable var0, CssMetaData var1) {
      if (!(var0 instanceof Node)) {
         return Collections.emptyList();
      } else {
         Node var2 = (Node)var0;
         CssStyleHelper var3 = var2.styleHelper != null ? var2.styleHelper : createStyleHelper(var2);
         return var3 != null ? var3.getMatchingStyles(var2, var1, false) : Collections.emptyList();
      }
   }

   static Map getMatchingStyles(Map var0, Node var1) {
      CssStyleHelper var2 = var1.styleHelper != null ? var1.styleHelper : createStyleHelper(var1);
      Iterator var3;
      if (var2 != null) {
         if (var0 == null) {
            var0 = new HashMap();
         }

         var3 = var1.getCssMetaData().iterator();

         while(var3.hasNext()) {
            CssMetaData var4 = (CssMetaData)var3.next();
            List var5 = var2.getMatchingStyles(var1, var4, true);
            if (var5 != null && !var5.isEmpty()) {
               StyleableProperty var6 = var4.getStyleableProperty(var1);
               ((Map)var0).put(var6, var5);
            }
         }
      }

      Node var7;
      if (var1 instanceof Parent) {
         for(var3 = ((Parent)var1).getChildren().iterator(); var3.hasNext(); var0 = getMatchingStyles((Map)var0, (Node)var7)) {
            var7 = (Node)var3.next();
         }
      }

      return (Map)var0;
   }

   private List getMatchingStyles(Styleable var1, CssMetaData var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      this.getMatchingStyles(var1, var2, var4, var3);
      List var5 = var2.getSubProperties();
      int var7;
      if (var5 != null) {
         int var6 = 0;

         for(var7 = var5.size(); var6 < var7; ++var6) {
            CssMetaData var8 = (CssMetaData)var5.get(var6);
            this.getMatchingStyles(var1, var8, var4, var3);
         }
      }

      Collections.sort(var4);
      ArrayList var10 = new ArrayList(var4.size());
      var7 = 0;

      for(int var11 = var4.size(); var7 < var11; ++var7) {
         Style var9 = ((CascadingStyle)var4.get(var7)).getStyle();
         if (!var10.contains(var9)) {
            var10.add(var9);
         }
      }

      return var10;
   }

   private void getMatchingStyles(Styleable var1, CssMetaData var2, List var3, boolean var4) {
      if (var1 != null) {
         String var5 = var2.getProperty();
         Node var6 = var1 instanceof Node ? (Node)var1 : null;
         StyleMap var7 = this.getStyleMap(var6);
         if (var7 == null) {
            return;
         }

         if (var4) {
            CascadingStyle var14 = this.getStyle(var1, var2.getProperty(), var7, var6.pseudoClassStates);
            if (var14 != null) {
               var3.add(var14);
               ParsedValueImpl var16 = var14.getParsedValueImpl();
               this.getMatchingLookupStyles(var1, var16, var3, var4);
            }
         } else {
            Map var8 = var7.getCascadingStyles();
            List var9 = (List)var8.get(var5);
            if (var9 != null) {
               var3.addAll(var9);
               int var10 = 0;

               for(int var11 = var9.size(); var10 < var11; ++var10) {
                  CascadingStyle var12 = (CascadingStyle)var9.get(var10);
                  ParsedValueImpl var13 = var12.getParsedValueImpl();
                  this.getMatchingLookupStyles(var1, var13, var3, var4);
               }
            }
         }

         if (var2.isInherits()) {
            for(Styleable var15 = var1.getStyleableParent(); var15 != null; var15 = var15.getStyleableParent()) {
               CssStyleHelper var17 = var15 instanceof Node ? ((Node)var15).styleHelper : null;
               if (var17 != null) {
                  var17.getMatchingStyles(var15, var2, var3, var4);
               }
            }
         }
      }

   }

   private void getMatchingLookupStyles(Styleable var1, ParsedValueImpl var2, List var3, boolean var4) {
      Object var5;
      if (var2.isLookup()) {
         var5 = var2.getValue();
         if (var5 instanceof String) {
            String var6 = (String)var5;
            Styleable var7 = var1;

            do {
               Node var8 = var7 instanceof Node ? (Node)var7 : null;
               CssStyleHelper var9 = var8 != null ? var8.styleHelper : null;
               if (var9 != null) {
                  StyleMap var10 = var9.getStyleMap(var7);
                  if (var10 != null && !var10.isEmpty()) {
                     int var11 = var3.size();
                     if (var4) {
                        CascadingStyle var12 = var9.resolveRef(var8, var6, var10, var8.pseudoClassStates);
                        if (var12 != null) {
                           var3.add(var12);
                        }
                     } else {
                        Map var19 = var10.getCascadingStyles();
                        List var13 = (List)var19.get(var6);
                        if (var13 != null) {
                           var3.addAll(var13);
                        }
                     }

                     int var20 = var3.size();

                     for(int var21 = var11; var21 < var20; ++var21) {
                        CascadingStyle var14 = (CascadingStyle)var3.get(var21);
                        this.getMatchingLookupStyles(var7, var14.getParsedValueImpl(), var3, var4);
                     }
                  }
               }
            } while((var7 = var7.getStyleableParent()) != null);
         }
      }

      if (var2.isContainsLookups()) {
         var5 = var2.getValue();
         int var17;
         if (var5 instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] var15 = (ParsedValueImpl[][])((ParsedValueImpl[][])var5);

            for(var17 = 0; var17 < var15.length; ++var17) {
               for(int var18 = 0; var18 < var15[var17].length; ++var18) {
                  if (var15[var17][var18] != null) {
                     this.getMatchingLookupStyles(var1, var15[var17][var18], var3, var4);
                  }
               }
            }
         } else if (var5 instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] var16 = (ParsedValueImpl[])((ParsedValueImpl[])var5);

            for(var17 = 0; var17 < var16.length; ++var17) {
               if (var16[var17] != null) {
                  this.getMatchingLookupStyles(var1, var16[var17], var3, var4);
               }
            }
         }

      }
   }

   private static final class CacheContainer {
      private final StyleCache.Key styleCacheKey;
      private final CssMetaData fontProp;
      private final int smapId;
      private final Map fontSizeCache;
      private final Map cssSetProperties;
      private boolean forceSlowpath;

      private CacheContainer(Node var1, StyleMap var2, int var3) {
         this.forceSlowpath = false;
         int var4 = 0;
         int[] var5 = new int[var3];
         var5[var4++] = this.smapId = var2.getId();
         Styleable var6 = var1.getStyleableParent();

         for(int var7 = 1; var7 < var3; ++var7) {
            if (var6 instanceof Node) {
               Node var8 = (Node)var6;
               CssStyleHelper var9 = var8.styleHelper;
               if (var9 != null && var9.cacheContainer != null) {
                  var5[var4++] = var9.cacheContainer.smapId;
               }
            }

            var6 = var6.getStyleableParent();
         }

         this.styleCacheKey = new StyleCache.Key(var5, var4);
         CssMetaData var12 = null;
         List var13 = var1.getCssMetaData();
         int var14 = var13 != null ? var13.size() : 0;

         for(int var10 = 0; var10 < var14; ++var10) {
            CssMetaData var11 = (CssMetaData)var13.get(var10);
            if ("-fx-font".equals(var11.getProperty())) {
               var12 = var11;
               break;
            }
         }

         this.fontProp = var12;
         this.fontSizeCache = new HashMap();
         this.cssSetProperties = new HashMap();
      }

      private StyleMap getStyleMap(Styleable var1) {
         if (var1 != null) {
            SubScene var2 = var1 instanceof Node ? ((Node)var1).getSubScene() : null;
            return StyleManager.getInstance().getStyleMap(var1, var2, this.smapId);
         } else {
            return StyleMap.EMPTY_MAP;
         }
      }

      // $FF: synthetic method
      CacheContainer(Node var1, StyleMap var2, int var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
