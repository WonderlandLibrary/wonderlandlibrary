package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ParsedValueImpl extends ParsedValue {
   private final boolean lookup;
   private final boolean containsLookups;
   private static int indent = 0;
   private int hash;
   private static final byte NULL_VALUE = 0;
   private static final byte VALUE = 1;
   private static final byte VALUE_ARRAY = 2;
   private static final byte ARRAY_OF_VALUE_ARRAY = 3;
   private static final byte STRING = 4;
   private static final byte COLOR = 5;
   private static final byte ENUM = 6;
   private static final byte BOOLEAN = 7;
   private static final byte URL = 8;
   private static final byte SIZE = 9;

   public final boolean isLookup() {
      return this.lookup;
   }

   public final boolean isContainsLookups() {
      return this.containsLookups;
   }

   private static boolean getContainsLookupsFlag(Object var0) {
      boolean var1 = false;
      if (var0 instanceof Size) {
         var1 = false;
      } else if (var0 instanceof ParsedValueImpl) {
         ParsedValueImpl var2 = (ParsedValueImpl)var0;
         var1 = var2.lookup || var2.containsLookups;
      } else {
         int var3;
         if (var0 instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] var5 = (ParsedValueImpl[])((ParsedValueImpl[])var0);

            for(var3 = 0; var3 < var5.length && !var1; ++var3) {
               if (var5[var3] != null) {
                  var1 = var1 || var5[var3].lookup || var5[var3].containsLookups;
               }
            }
         } else if (var0 instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] var6 = (ParsedValueImpl[][])((ParsedValueImpl[][])var0);

            for(var3 = 0; var3 < var6.length && !var1; ++var3) {
               if (var6[var3] != null) {
                  for(int var4 = 0; var4 < var6[var3].length && !var1; ++var4) {
                     if (var6[var3][var4] != null) {
                        var1 = var1 || var6[var3][var4].lookup || var6[var3][var4].containsLookups;
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   public static boolean containsFontRelativeSize(ParsedValue var0, boolean var1) {
      boolean var2 = false;
      Object var3 = var0.getValue();
      if (var3 instanceof Size) {
         Size var4 = (Size)var3;
         var2 = var4.getUnits() == SizeUnits.PERCENT ? var1 : !var4.isAbsolute();
      } else if (var3 instanceof ParsedValue) {
         ParsedValueImpl var7 = (ParsedValueImpl)var3;
         var2 = containsFontRelativeSize(var7, var1);
      } else {
         int var5;
         if (var3 instanceof ParsedValue[]) {
            ParsedValue[] var8 = (ParsedValue[])((ParsedValue[])var3);

            for(var5 = 0; var5 < var8.length && !var2; ++var5) {
               if (var8[var5] != null) {
                  var2 = containsFontRelativeSize(var8[var5], var1);
               }
            }
         } else if (var3 instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] var9 = (ParsedValueImpl[][])((ParsedValueImpl[][])var3);

            for(var5 = 0; var5 < var9.length && !var2; ++var5) {
               if (var9[var5] != null) {
                  for(int var6 = 0; var6 < var9[var5].length && !var2; ++var6) {
                     if (var9[var5][var6] != null) {
                        var2 = containsFontRelativeSize(var9[var5][var6], var1);
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   public ParsedValueImpl(Object var1, StyleConverter var2, boolean var3) {
      super(var1, var2);
      this.hash = Integer.MIN_VALUE;
      this.lookup = var3;
      this.containsLookups = var3 || getContainsLookupsFlag(var1);
   }

   public ParsedValueImpl(Object var1, StyleConverter var2) {
      this(var1, var2, false);
   }

   public Object convert(Font var1) {
      return this.converter != null ? this.converter.convert(this, var1) : this.value;
   }

   private static String spaces() {
      return (new String(new char[indent])).replace('\u0000', ' ');
   }

   private static void indent() {
      indent += 2;
   }

   private static void outdent() {
      indent = Math.max(0, indent - 2);
   }

   public String toString() {
      String var1 = System.lineSeparator();
      StringBuilder var2 = new StringBuilder();
      var2.append(spaces()).append(this.lookup ? "<Value lookup=\"true\">" : "<Value>").append(var1);
      indent();
      if (this.value != null) {
         this.appendValue(var2, this.value, "value");
      } else {
         this.appendValue(var2, "null", "value");
      }

      var2.append(spaces()).append("<converter>").append(this.converter).append("</converter>").append(var1);
      outdent();
      var2.append(spaces()).append("</Value>");
      return var2.toString();
   }

   private void appendValue(StringBuilder var1, Object var2, String var3) {
      String var4 = System.lineSeparator();
      int var7;
      int var8;
      if (var2 instanceof ParsedValueImpl[][]) {
         ParsedValueImpl[][] var5 = (ParsedValueImpl[][])((ParsedValueImpl[][])var2);
         var1.append(spaces()).append('<').append(var3).append(" layers=\"").append(var5.length).append("\">").append(var4);
         indent();
         ParsedValueImpl[][] var6 = var5;
         var7 = var5.length;

         for(var8 = 0; var8 < var7; ++var8) {
            ParsedValueImpl[] var9 = var6[var8];
            var1.append(spaces()).append("<layer>").append(var4);
            indent();
            if (var9 == null) {
               var1.append(spaces()).append("null").append(var4);
            } else {
               ParsedValueImpl[] var10 = var9;
               int var11 = var9.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  ParsedValueImpl var13 = var10[var12];
                  if (var13 == null) {
                     var1.append(spaces()).append("null").append(var4);
                  } else {
                     var1.append(var13);
                  }
               }

               outdent();
               var1.append(spaces()).append("</layer>").append(var4);
            }
         }

         outdent();
         var1.append(spaces()).append("</").append(var3).append('>').append(var4);
      } else if (var2 instanceof ParsedValueImpl[]) {
         ParsedValueImpl[] var14 = (ParsedValueImpl[])((ParsedValueImpl[])var2);
         var1.append(spaces()).append('<').append(var3).append(" values=\"").append(var14.length).append("\">").append(var4);
         indent();
         ParsedValueImpl[] var15 = var14;
         var7 = var14.length;

         for(var8 = 0; var8 < var7; ++var8) {
            ParsedValueImpl var16 = var15[var8];
            if (var16 == null) {
               var1.append(spaces()).append("null").append(var4);
            } else {
               var1.append(var16);
            }
         }

         outdent();
         var1.append(spaces()).append("</").append(var3).append('>').append(var4);
      } else if (var2 instanceof ParsedValueImpl) {
         var1.append(spaces()).append('<').append(var3).append('>').append(var4);
         indent();
         var1.append(var2);
         outdent();
         var1.append(spaces()).append("</").append(var3).append('>').append(var4);
      } else {
         var1.append(spaces()).append('<').append(var3).append('>');
         var1.append(var2);
         var1.append("</").append(var3).append('>').append(var4);
      }

   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && var1.getClass() == this.getClass()) {
         ParsedValueImpl var2 = (ParsedValueImpl)var1;
         if (this.hash != var2.hash) {
            return false;
         } else {
            int var5;
            ParsedValueImpl var7;
            if (this.value instanceof ParsedValueImpl[][]) {
               if (!(var2.value instanceof ParsedValueImpl[][])) {
                  return false;
               } else {
                  ParsedValueImpl[][] var9 = (ParsedValueImpl[][])((ParsedValueImpl[][])this.value);
                  ParsedValueImpl[][] var10 = (ParsedValueImpl[][])((ParsedValueImpl[][])var2.value);
                  if (var9.length != var10.length) {
                     return false;
                  } else {
                     for(var5 = 0; var5 < var9.length; ++var5) {
                        if (var9[var5] != null || var10[var5] != null) {
                           if (var9[var5] == null || var10[var5] == null) {
                              return false;
                           }

                           if (var9[var5].length != var10[var5].length) {
                              return false;
                           }

                           for(int var11 = 0; var11 < var9[var5].length; ++var11) {
                              var7 = var9[var5][var11];
                              ParsedValueImpl var8 = var10[var5][var11];
                              if (var7 != null) {
                                 if (!var7.equals(var8)) {
                                    return false;
                                 }
                              } else if (var8 != null) {
                                 return false;
                              }
                           }
                        }
                     }

                     return true;
                  }
               }
            } else if (!(this.value instanceof ParsedValueImpl[])) {
               if (this.value instanceof String && var2.value instanceof String) {
                  return this.value.toString().equalsIgnoreCase(var2.value.toString());
               } else {
                  return this.value != null ? this.value.equals(var2.value) : var2.value == null;
               }
            } else if (!(var2.value instanceof ParsedValueImpl[])) {
               return false;
            } else {
               ParsedValueImpl[] var3 = (ParsedValueImpl[])((ParsedValueImpl[])this.value);
               ParsedValueImpl[] var4 = (ParsedValueImpl[])((ParsedValueImpl[])var2.value);
               if (var3.length != var4.length) {
                  return false;
               } else {
                  var5 = 0;

                  while(true) {
                     if (var5 >= var3.length) {
                        return true;
                     }

                     ParsedValueImpl var6 = var3[var5];
                     var7 = var4[var5];
                     if (var6 != null) {
                        if (!var6.equals(var7)) {
                           break;
                        }
                     } else if (var7 != null) {
                        break;
                     }

                     ++var5;
                  }

                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hash == Integer.MIN_VALUE) {
         this.hash = 17;
         int var2;
         if (this.value instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] var1 = (ParsedValueImpl[][])((ParsedValueImpl[][])this.value);

            for(var2 = 0; var2 < var1.length; ++var2) {
               for(int var3 = 0; var3 < var1[var2].length; ++var3) {
                  ParsedValueImpl var4 = var1[var2][var3];
                  this.hash = 37 * this.hash + (var4 != null && var4.value != null ? var4.value.hashCode() : 0);
               }
            }
         } else if (this.value instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] var5 = (ParsedValueImpl[])((ParsedValueImpl[])this.value);

            for(var2 = 0; var2 < var5.length; ++var2) {
               if (var5[var2] != null && var5[var2].value != null) {
                  ParsedValueImpl var6 = var5[var2];
                  this.hash = 37 * this.hash + (var6 != null && var6.value != null ? var6.value.hashCode() : 0);
               }
            }
         } else {
            this.hash = 37 * this.hash + (this.value != null ? this.value.hashCode() : 0);
         }
      }

      return this.hash;
   }

   public final void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      var1.writeBoolean(this.lookup);
      if (this.converter instanceof StyleConverterImpl) {
         var1.writeBoolean(true);
         ((StyleConverterImpl)this.converter).writeBinary(var1, var2);
      } else {
         var1.writeBoolean(false);
         if (this.converter != null) {
            System.err.println("cannot writeBinary " + this.converter.getClass().getName());
         }
      }

      if (this.value instanceof ParsedValue) {
         var1.writeByte(1);
         ParsedValue var3 = (ParsedValue)this.value;
         if (var3 instanceof ParsedValueImpl) {
            ((ParsedValueImpl)var3).writeBinary(var1, var2);
         } else {
            ParsedValueImpl var4 = new ParsedValueImpl(var3.getValue(), var3.getConverter());
            var4.writeBinary(var1, var2);
         }
      } else {
         int var5;
         int var14;
         if (this.value instanceof ParsedValue[]) {
            var1.writeByte(2);
            ParsedValue[] var11 = (ParsedValue[])((ParsedValue[])this.value);
            if (var11 != null) {
               var1.writeByte(1);
            } else {
               var1.writeByte(0);
            }

            var14 = var11 != null ? var11.length : 0;
            var1.writeInt(var14);

            for(var5 = 0; var5 < var14; ++var5) {
               if (var11[var5] != null) {
                  var1.writeByte(1);
                  ParsedValue var6 = var11[var5];
                  if (var6 instanceof ParsedValueImpl) {
                     ((ParsedValueImpl)var6).writeBinary(var1, var2);
                  } else {
                     ParsedValueImpl var7 = new ParsedValueImpl(var6.getValue(), var6.getConverter());
                     var7.writeBinary(var1, var2);
                  }
               } else {
                  var1.writeByte(0);
               }
            }
         } else {
            int var8;
            if (this.value instanceof ParsedValue[][]) {
               var1.writeByte(3);
               ParsedValue[][] var12 = (ParsedValue[][])((ParsedValue[][])this.value);
               if (var12 != null) {
                  var1.writeByte(1);
               } else {
                  var1.writeByte(0);
               }

               var14 = var12 != null ? var12.length : 0;
               var1.writeInt(var14);

               for(var5 = 0; var5 < var14; ++var5) {
                  ParsedValue[] var20 = var12[var5];
                  if (var20 != null) {
                     var1.writeByte(1);
                  } else {
                     var1.writeByte(0);
                  }

                  int var22 = var20 != null ? var20.length : 0;
                  var1.writeInt(var22);

                  for(var8 = 0; var8 < var22; ++var8) {
                     if (var20[var8] != null) {
                        var1.writeByte(1);
                        ParsedValue var9 = var20[var8];
                        if (var9 instanceof ParsedValueImpl) {
                           ((ParsedValueImpl)var9).writeBinary(var1, var2);
                        } else {
                           ParsedValueImpl var10 = new ParsedValueImpl(var9.getValue(), var9.getConverter());
                           var10.writeBinary(var1, var2);
                        }
                     } else {
                        var1.writeByte(0);
                     }
                  }
               }
            } else if (this.value instanceof Color) {
               Color var13 = (Color)this.value;
               var1.writeByte(5);
               var1.writeLong(Double.doubleToLongBits(var13.getRed()));
               var1.writeLong(Double.doubleToLongBits(var13.getGreen()));
               var1.writeLong(Double.doubleToLongBits(var13.getBlue()));
               var1.writeLong(Double.doubleToLongBits(var13.getOpacity()));
            } else if (this.value instanceof Enum) {
               Enum var15 = (Enum)this.value;
               var14 = var2.addString(var15.name());
               var1.writeByte(6);
               var1.writeShort(var14);
            } else if (this.value instanceof Boolean) {
               Boolean var16 = (Boolean)this.value;
               var1.writeByte(7);
               var1.writeBoolean(var16);
            } else if (this.value instanceof Size) {
               Size var17 = (Size)this.value;
               var1.writeByte(9);
               double var18 = var17.getValue();
               long var21 = Double.doubleToLongBits(var18);
               var1.writeLong(var21);
               var8 = var2.addString(var17.getUnits().name());
               var1.writeShort(var8);
            } else {
               int var19;
               if (this.value instanceof String) {
                  var1.writeByte(4);
                  var19 = var2.addString((String)this.value);
                  var1.writeShort(var19);
               } else if (this.value instanceof URL) {
                  var1.writeByte(8);
                  var19 = var2.addString(this.value.toString());
                  var1.writeShort(var19);
               } else {
                  if (this.value != null) {
                     throw new InternalError("cannot writeBinary " + this);
                  }

                  var1.writeByte(0);
               }
            }
         }
      }

   }

   public static ParsedValueImpl readBinary(int var0, DataInputStream var1, String[] var2) throws IOException {
      boolean var3 = var1.readBoolean();
      boolean var4 = var1.readBoolean();
      StyleConverter var5 = var4 ? StyleConverterImpl.readBinary(var1, var2) : null;
      byte var6 = var1.readByte();
      if (var6 == 1) {
         ParsedValueImpl var25 = readBinary(var0, var1, var2);
         return new ParsedValueImpl(var25, var5, var3);
      } else {
         int var7;
         int var9;
         if (var6 == 2) {
            if (var0 >= 4) {
               var1.readByte();
            }

            var7 = var1.readInt();
            ParsedValueImpl[] var24 = var7 > 0 ? new ParsedValueImpl[var7] : null;

            for(var9 = 0; var9 < var7; ++var9) {
               byte var28 = var1.readByte();
               if (var28 == 1) {
                  var24[var9] = readBinary(var0, var1, var2);
               } else {
                  var24[var9] = null;
               }
            }

            return new ParsedValueImpl(var24, var5, var3);
         } else if (var6 != 3) {
            double var19;
            if (var6 == 5) {
               var19 = Double.longBitsToDouble(var1.readLong());
               double var31 = Double.longBitsToDouble(var1.readLong());
               double var32 = Double.longBitsToDouble(var1.readLong());
               double var13 = Double.longBitsToDouble(var1.readLong());
               return new ParsedValueImpl(Color.color(var19, var31, var32, var13), var5, var3);
            } else if (var6 == 6) {
               short var22 = var1.readShort();
               String var23 = var2[var22];
               if (var0 == 2) {
                  short var29 = var1.readShort();
                  if (var29 >= var2.length) {
                     throw new IllegalArgumentException("bad version " + var0);
                  }
               }

               ParsedValueImpl var30 = new ParsedValueImpl(var23, var5, var3);
               return var30;
            } else if (var6 == 7) {
               Boolean var20 = var1.readBoolean();
               return new ParsedValueImpl(var20, var5, var3);
            } else if (var6 == 9) {
               var19 = Double.longBitsToDouble(var1.readLong());
               SizeUnits var26 = SizeUnits.PX;
               String var27 = var2[var1.readShort()];

               try {
                  var26 = (SizeUnits)Enum.valueOf(SizeUnits.class, var27);
               } catch (IllegalArgumentException var15) {
                  System.err.println(var15.toString());
               } catch (NullPointerException var16) {
                  System.err.println(var16.toString());
               }

               return new ParsedValueImpl(new Size(var19, var26), var5, var3);
            } else {
               String var18;
               if (var6 == 4) {
                  var18 = var2[var1.readShort()];
                  return new ParsedValueImpl(var18, var5, var3);
               } else if (var6 == 8) {
                  var18 = var2[var1.readShort()];

                  try {
                     URL var21 = new URL(var18);
                     return new ParsedValueImpl(var21, var5, var3);
                  } catch (MalformedURLException var17) {
                     throw new InternalError("Excpeption in Value.readBinary: " + var17);
                  }
               } else if (var6 == 0) {
                  return new ParsedValueImpl((Object)null, var5, var3);
               } else {
                  throw new InternalError("unknown type: " + var6);
               }
            }
         } else {
            if (var0 >= 4) {
               var1.readByte();
            }

            var7 = var1.readInt();
            ParsedValueImpl[][] var8 = var7 > 0 ? new ParsedValueImpl[var7][0] : (ParsedValueImpl[][])null;

            for(var9 = 0; var9 < var7; ++var9) {
               if (var0 >= 4) {
                  var1.readByte();
               }

               int var10 = var1.readInt();
               var8[var9] = var10 > 0 ? new ParsedValueImpl[var10] : null;

               for(int var11 = 0; var11 < var10; ++var11) {
                  byte var12 = var1.readByte();
                  if (var12 == 1) {
                     var8[var9][var11] = readBinary(var0, var1, var2);
                  } else {
                     var8[var9][var11] = null;
                  }
               }
            }

            return new ParsedValueImpl(var8, var5, var3);
         }
      }
   }
}
