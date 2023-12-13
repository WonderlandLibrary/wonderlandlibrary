package com.sun.javafx.font.freetype;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

class PangoGlyphLayout extends GlyphLayout {
   private static final long fontmap = OSPango.pango_ft2_font_map_new();
   private Map runUtf8 = new LinkedHashMap();

   private int getSlot(PGFont var1, PangoGlyphString var2) {
      CompositeFontResource var3 = (CompositeFontResource)var1.getFontResource();
      long var4 = var2.font;
      long var6 = OSPango.pango_font_describe(var4);
      String var8 = OSPango.pango_font_description_get_family(var6);
      int var9 = OSPango.pango_font_description_get_style(var6);
      int var10 = OSPango.pango_font_description_get_weight(var6);
      OSPango.pango_font_description_free(var6);
      boolean var11 = var10 == 700;
      boolean var12 = var9 != 0;
      PrismFontFactory var13 = PrismFontFactory.getFontFactory();
      PGFont var14 = var13.createFont(var8, var11, var12, var1.getSize());
      String var15 = var14.getFullName();
      String var16 = var3.getSlotResource(0).getFullName();
      int var17 = 0;
      if (!var15.equalsIgnoreCase(var16)) {
         var17 = var3.getSlotForFont(var15);
         if (PrismFontFactory.debugFonts) {
            System.err.println("\tFallback font= " + var15 + " slot=" + (var17 >> 24));
         }
      }

      return var17;
   }

   private boolean check(long var1, String var3, long var4, long var6, long var8) {
      if (var1 != 0L) {
         return false;
      } else {
         if (var3 != null && PrismFontFactory.debugFonts) {
            System.err.println(var3);
         }

         if (var8 != 0L) {
            OSPango.pango_attr_list_unref(var8);
         }

         if (var6 != 0L) {
            OSPango.pango_font_description_free(var6);
         }

         if (var4 != 0L) {
            OSPango.g_object_unref(var4);
         }

         return true;
      }
   }

   public void layout(TextRun var1, PGFont var2, FontStrike var3, char[] var4) {
      FontResource var5 = var2.getFontResource();
      boolean var6 = var5 instanceof CompositeFontResource;
      if (var6) {
         var5 = ((CompositeFontResource)var5).getSlotResource(0);
      }

      if (!this.check(fontmap, "Failed allocating PangoFontMap.", 0L, 0L, 0L)) {
         long var7 = OSPango.pango_font_map_create_context(fontmap);
         if (!this.check(var7, "Failed allocating PangoContext.", 0L, 0L, 0L)) {
            boolean var9 = (var1.getLevel() & 1) != 0;
            if (var9) {
               OSPango.pango_context_set_base_dir(var7, 1);
            }

            float var10 = var2.getSize();
            int var11 = var5.isItalic() ? 2 : 0;
            int var12 = var5.isBold() ? 700 : 400;
            long var13 = OSPango.pango_font_description_new();
            if (!this.check(var13, "Failed allocating FontDescription.", var7, 0L, 0L)) {
               OSPango.pango_font_description_set_family(var13, var5.getFamilyName());
               OSPango.pango_font_description_set_absolute_size(var13, (double)(var10 * 1024.0F));
               OSPango.pango_font_description_set_stretch(var13, 4);
               OSPango.pango_font_description_set_style(var13, var11);
               OSPango.pango_font_description_set_weight(var13, var12);
               long var15 = OSPango.pango_attr_list_new();
               if (!this.check(var15, "Failed allocating PangoAttributeList.", var7, var13, 0L)) {
                  long var17 = OSPango.pango_attr_font_desc_new(var13);
                  if (!this.check(var17, "Failed allocating PangoAttribute.", var7, var13, var15)) {
                     OSPango.pango_attr_list_insert(var15, var17);
                     if (!var6) {
                        var17 = OSPango.pango_attr_fallback_new(false);
                        OSPango.pango_attr_list_insert(var15, var17);
                     }

                     Long var19 = (Long)this.runUtf8.get(var1);
                     if (var19 == null) {
                        char[] var20 = Arrays.copyOfRange(var4, var1.getStart(), var1.getEnd());
                        var19 = OSPango.g_utf16_to_utf8(var20);
                        if (this.check(var19, "Failed allocating UTF-8 buffer.", var7, var13, var15)) {
                           return;
                        }

                        this.runUtf8.put(var1, var19);
                     }

                     long var43 = OSPango.g_utf8_strlen(var19, -1L);
                     long var22 = OSPango.g_utf8_offset_to_pointer(var19, var43);
                     long var24 = OSPango.pango_itemize(var7, var19, 0, (int)(var22 - var19), var15, 0L);
                     if (var24 != 0L) {
                        int var26 = OSPango.g_list_length(var24);
                        PangoGlyphString[] var27 = new PangoGlyphString[var26];

                        int var28;
                        for(var28 = 0; var28 < var26; ++var28) {
                           long var29 = OSPango.g_list_nth_data(var24, var28);
                           if (var29 != 0L) {
                              var27[var28] = OSPango.pango_shape(var19, var29);
                              OSPango.pango_item_free(var29);
                           }
                        }

                        OSPango.g_list_free(var24);
                        var28 = 0;
                        PangoGlyphString[] var44 = var27;
                        int var30 = var27.length;

                        for(int var31 = 0; var31 < var30; ++var31) {
                           PangoGlyphString var32 = var44[var31];
                           if (var32 != null) {
                              var28 += var32.num_glyphs;
                           }
                        }

                        int[] var45 = new int[var28];
                        float[] var46 = new float[var28 * 2 + 2];
                        int[] var47 = new int[var28];
                        int var48 = 0;
                        int var33 = var9 ? var1.getLength() : 0;
                        int var34 = 0;
                        PangoGlyphString[] var35 = var27;
                        int var36 = var27.length;

                        for(int var37 = 0; var37 < var36; ++var37) {
                           PangoGlyphString var38 = var35[var37];
                           if (var38 != null) {
                              int var39 = var6 ? this.getSlot(var2, var38) : 0;
                              if (var9) {
                                 var33 -= var38.num_chars;
                              }

                              for(int var40 = 0; var40 < var38.num_glyphs; ++var40) {
                                 int var41 = var48 + var40;
                                 if (var39 != -1) {
                                    int var42 = var38.glyphs[var40];
                                    if (0 <= var42 && var42 <= 16777215) {
                                       var45[var41] = var39 << 24 | var42;
                                    }
                                 }

                                 if (var10 != 0.0F) {
                                    var34 += var38.widths[var40];
                                    var46[2 + (var41 << 1)] = (float)var34 / 1024.0F;
                                 }

                                 var47[var41] = var38.log_clusters[var40] + var33;
                              }

                              if (!var9) {
                                 var33 += var38.num_chars;
                              }

                              var48 += var38.num_glyphs;
                           }
                        }

                        var1.shape(var28, var45, var46, var47);
                     }

                     this.check(0L, (String)null, var7, var13, var15);
                  }
               }
            }
         }
      }
   }

   public void dispose() {
      super.dispose();
      Iterator var1 = this.runUtf8.values().iterator();

      while(var1.hasNext()) {
         Long var2 = (Long)var1.next();
         OSPango.g_free(var2);
      }

      this.runUtf8.clear();
   }
}
