package com.sun.javafx.css;

import com.sun.javafx.collections.TrackableObservableList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.scene.Node;

public final class Rule {
   private List selectors = null;
   private List declarations = null;
   private Observables observables = null;
   private Stylesheet stylesheet;
   private byte[] serializedDecls;
   private final int bssVersion;

   public List getUnobservedSelectorList() {
      if (this.selectors == null) {
         this.selectors = new ArrayList();
      }

      return this.selectors;
   }

   public List getUnobservedDeclarationList() {
      if (this.declarations == null && this.serializedDecls != null) {
         try {
            ByteArrayInputStream var1 = new ByteArrayInputStream(this.serializedDecls);
            DataInputStream var2 = new DataInputStream(var1);
            short var3 = var2.readShort();
            this.declarations = new ArrayList(var3);

            for(int var4 = 0; var4 < var3; ++var4) {
               Declaration var5 = Declaration.readBinary(this.bssVersion, var2, this.stylesheet.getStringStore());
               var5.rule = this;
               if (this.stylesheet != null && this.stylesheet.getUrl() != null) {
                  String var6 = this.stylesheet.getUrl();
                  var5.fixUrl(var6);
               }

               this.declarations.add(var5);
            }
         } catch (IOException var10) {
            this.declarations = new ArrayList();

            assert false;

            var10.getMessage();
         } finally {
            this.serializedDecls = null;
         }
      }

      return this.declarations;
   }

   public final ObservableList getDeclarations() {
      if (this.observables == null) {
         this.observables = new Observables(this);
      }

      return this.observables.getDeclarations();
   }

   public final ObservableList getSelectors() {
      if (this.observables == null) {
         this.observables = new Observables(this);
      }

      return this.observables.getSelectors();
   }

   public Stylesheet getStylesheet() {
      return this.stylesheet;
   }

   void setStylesheet(Stylesheet var1) {
      this.stylesheet = var1;
      if (var1 != null && var1.getUrl() != null) {
         String var2 = var1.getUrl();
         int var3 = this.declarations != null ? this.declarations.size() : 0;

         for(int var4 = 0; var4 < var3; ++var4) {
            ((Declaration)this.declarations.get(var4)).fixUrl(var2);
         }
      }

   }

   public StyleOrigin getOrigin() {
      return this.stylesheet != null ? this.stylesheet.getOrigin() : null;
   }

   public Rule(List var1, List var2) {
      this.selectors = var1;
      this.declarations = var2;
      this.serializedDecls = null;
      this.bssVersion = 5;
      int var3 = var1 != null ? var1.size() : 0;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Selector var5 = (Selector)var1.get(var4);
         var5.setRule(this);
      }

      var4 = var2 != null ? var2.size() : 0;

      for(int var7 = 0; var7 < var4; ++var7) {
         Declaration var6 = (Declaration)var2.get(var7);
         var6.rule = this;
      }

   }

   private Rule(List var1, byte[] var2, int var3) {
      this.selectors = var1;
      this.declarations = null;
      this.serializedDecls = var2;
      this.bssVersion = var3;
      int var4 = var1 != null ? var1.size() : 0;

      for(int var5 = 0; var5 < var4; ++var5) {
         Selector var6 = (Selector)var1.get(var5);
         var6.setRule(this);
      }

   }

   long applies(Node var1, Set[] var2) {
      long var3 = 0L;

      for(int var5 = 0; var5 < this.selectors.size(); ++var5) {
         Selector var6 = (Selector)this.selectors.get(var5);
         if (var6.applies(var1, var2, 0)) {
            var3 |= 1L << var5;
         }
      }

      return var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.selectors.size() > 0) {
         var1.append(this.selectors.get(0));
      }

      int var2;
      for(var2 = 1; var2 < this.selectors.size(); ++var2) {
         var1.append(',');
         var1.append(this.selectors.get(var2));
      }

      var1.append("{\n");
      var2 = this.declarations != null ? this.declarations.size() : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.append("\t");
         var1.append(this.declarations.get(var3));
         var1.append("\n");
      }

      var1.append("}");
      return var1.toString();
   }

   final void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      int var3 = this.selectors != null ? this.selectors.size() : 0;
      var1.writeShort(var3);

      for(int var4 = 0; var4 < var3; ++var4) {
         Selector var5 = (Selector)this.selectors.get(var4);
         var5.writeBinary(var1, var2);
      }

      List var10 = this.getUnobservedDeclarationList();
      if (var10 != null) {
         ByteArrayOutputStream var11 = new ByteArrayOutputStream(5192);
         DataOutputStream var6 = new DataOutputStream(var11);
         int var7 = var10.size();
         var6.writeShort(var7);

         for(int var8 = 0; var8 < var7; ++var8) {
            Declaration var9 = (Declaration)this.declarations.get(var8);
            var9.writeBinary(var6, var2);
         }

         var1.writeInt(var11.size());
         var1.write(var11.toByteArray());
      } else {
         var1.writeShort(0);
      }

   }

   static Rule readBinary(int var0, DataInputStream var1, String[] var2) throws IOException {
      short var3 = var1.readShort();
      ArrayList var4 = new ArrayList(var3);

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         Selector var6 = Selector.readBinary(var0, var1, var2);
         var4.add(var6);
      }

      if (var0 >= 4) {
         var5 = var1.readInt();
         byte[] var11 = new byte[var5];
         if (var5 > 0) {
            var1.readFully(var11);
         }

         return new Rule(var4, var11, var0);
      } else {
         short var9 = var1.readShort();
         ArrayList var10 = new ArrayList(var9);

         for(int var7 = 0; var7 < var9; ++var7) {
            Declaration var8 = Declaration.readBinary(var0, var1, var2);
            var10.add(var8);
         }

         return new Rule(var4, var10);
      }
   }

   private static final class Observables {
      private final Rule rule;
      private final ObservableList selectorObservableList;
      private final ObservableList declarationObservableList;

      private Observables(Rule var1) {
         this.rule = var1;
         this.selectorObservableList = new TrackableObservableList(var1.getUnobservedSelectorList()) {
            protected void onChanged(ListChangeListener.Change var1) {
               while(var1.next()) {
                  List var2;
                  int var3;
                  int var4;
                  Selector var5;
                  if (var1.wasAdded()) {
                     var2 = var1.getAddedSubList();
                     var3 = 0;

                     for(var4 = var2.size(); var3 < var4; ++var3) {
                        var5 = (Selector)var2.get(var3);
                        var5.setRule(Observables.this.rule);
                     }
                  }

                  if (var1.wasRemoved()) {
                     var2 = var1.getAddedSubList();
                     var3 = 0;

                     for(var4 = var2.size(); var3 < var4; ++var3) {
                        var5 = (Selector)var2.get(var3);
                        if (var5.getRule() == Observables.this.rule) {
                           var5.setRule((Rule)null);
                        }
                     }
                  }
               }

            }
         };
         this.declarationObservableList = new TrackableObservableList(var1.getUnobservedDeclarationList()) {
            protected void onChanged(ListChangeListener.Change var1) {
               while(var1.next()) {
                  List var2;
                  int var3;
                  int var4;
                  Declaration var5;
                  if (var1.wasAdded()) {
                     var2 = var1.getAddedSubList();
                     var3 = 0;

                     for(var4 = var2.size(); var3 < var4; ++var3) {
                        var5 = (Declaration)var2.get(var3);
                        var5.rule = Observables.this.rule;
                        Stylesheet var6 = Observables.this.rule.stylesheet;
                        if (var6 != null && var6.getUrl() != null) {
                           String var7 = var6.getUrl();
                           var5.fixUrl(var7);
                        }
                     }
                  }

                  if (var1.wasRemoved()) {
                     var2 = var1.getRemoved();
                     var3 = 0;

                     for(var4 = var2.size(); var3 < var4; ++var3) {
                        var5 = (Declaration)var2.get(var3);
                        if (var5.rule == Observables.this.rule) {
                           var5.rule = null;
                        }
                     }
                  }
               }

            }
         };
      }

      private ObservableList getSelectors() {
         return this.selectorObservableList;
      }

      private ObservableList getDeclarations() {
         return this.declarationObservableList;
      }

      // $FF: synthetic method
      Observables(Rule var1, Object var2) {
         this(var1);
      }
   }
}
