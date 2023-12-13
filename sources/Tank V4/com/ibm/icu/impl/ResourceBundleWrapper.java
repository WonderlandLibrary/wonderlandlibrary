package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleWrapper extends UResourceBundle {
   private ResourceBundle bundle = null;
   private String localeID = null;
   private String baseName = null;
   private List keys = null;
   private static final boolean DEBUG = ICUDebug.enabled("resourceBundleWrapper");

   private ResourceBundleWrapper(ResourceBundle var1) {
      this.bundle = var1;
   }

   protected void setLoadingStatus(int var1) {
   }

   protected Object handleGetObject(String var1) {
      ResourceBundleWrapper var2 = this;
      Object var3 = null;

      while(var2 != null) {
         try {
            var3 = var2.bundle.getObject(var1);
            break;
         } catch (MissingResourceException var5) {
            var2 = (ResourceBundleWrapper)var2.getParent();
         }
      }

      if (var3 == null) {
         throw new MissingResourceException("Can't find resource for bundle " + this.baseName + ", key " + var1, this.getClass().getName(), var1);
      } else {
         return var3;
      }
   }

   public Enumeration getKeys() {
      return Collections.enumeration(this.keys);
   }

   private void initKeysVector() {
      ResourceBundleWrapper var1 = this;

      for(this.keys = new ArrayList(); var1 != null; var1 = (ResourceBundleWrapper)var1.getParent()) {
         Enumeration var2 = var1.bundle.getKeys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            if (!this.keys.contains(var3)) {
               this.keys.add(var3);
            }
         }
      }

   }

   protected String getLocaleID() {
      return this.localeID;
   }

   protected String getBaseName() {
      return this.bundle.getClass().getName().replace('.', '/');
   }

   public ULocale getULocale() {
      return new ULocale(this.localeID);
   }

   public UResourceBundle getParent() {
      return (UResourceBundle)this.parent;
   }

   public static UResourceBundle getBundleInstance(String var0, String var1, ClassLoader var2, boolean var3) {
      UResourceBundle var4 = instantiateBundle(var0, var1, var2, var3);
      if (var4 == null) {
         String var5 = "_";
         if (var0.indexOf(47) >= 0) {
            var5 = "/";
         }

         throw new MissingResourceException("Could not find the bundle " + var0 + var5 + var1, "", "");
      } else {
         return var4;
      }
   }

   protected static synchronized UResourceBundle instantiateBundle(String var0, String var1, ClassLoader var2, boolean var3) {
      if (var2 == null) {
         var2 = Utility.getFallbackClassLoader();
      }

      ClassLoader var4 = var2;
      String var5 = var0;
      ULocale var6 = ULocale.getDefault();
      if (var1.length() != 0) {
         var5 = var0 + "_" + var1;
      }

      ResourceBundleWrapper var7 = (ResourceBundleWrapper)loadFromCache(var2, var5, var6);
      if (var7 == null) {
         ResourceBundleWrapper var8 = null;
         int var9 = var1.lastIndexOf(95);
         boolean var10 = false;
         String var11;
         if (var9 != -1) {
            var11 = var1.substring(0, var9);
            var8 = (ResourceBundleWrapper)loadFromCache(var2, var0 + "_" + var11, var6);
            if (var8 == null) {
               var8 = (ResourceBundleWrapper)instantiateBundle(var0, var11, var2, var3);
            }
         } else if (var1.length() > 0) {
            var8 = (ResourceBundleWrapper)loadFromCache(var2, var0, var6);
            if (var8 == null) {
               var8 = (ResourceBundleWrapper)instantiateBundle(var0, "", var2, var3);
            }
         }

         try {
            Class var23 = var4.loadClass(var5).asSubclass(ResourceBundle.class);
            ResourceBundle var12 = (ResourceBundle)var23.newInstance();
            var7 = new ResourceBundleWrapper(var12);
            if (var8 != null) {
               var7.setParent(var8);
            }

            var7.baseName = var0;
            var7.localeID = var1;
         } catch (ClassNotFoundException var20) {
            var10 = true;
         } catch (NoClassDefFoundError var21) {
            var10 = true;
         } catch (Exception var22) {
            if (DEBUG) {
               System.out.println("failure");
            }

            if (DEBUG) {
               System.out.println(var22);
            }
         }

         if (var10) {
            try {
               var11 = var5.replace('.', '/') + ".properties";
               InputStream var24 = (InputStream)AccessController.doPrivileged(new PrivilegedAction(var4, var11) {
                  final ClassLoader val$cl;
                  final String val$resName;

                  {
                     this.val$cl = var1;
                     this.val$resName = var2;
                  }

                  public InputStream run() {
                     return this.val$cl != null ? this.val$cl.getResourceAsStream(this.val$resName) : ClassLoader.getSystemResourceAsStream(this.val$resName);
                  }

                  public Object run() {
                     return this.run();
                  }
               });
               if (var24 != null) {
                  label128: {
                     BufferedInputStream var25 = new BufferedInputStream(var24);

                     try {
                        var7 = new ResourceBundleWrapper(new PropertyResourceBundle(var25));
                        if (var8 != null) {
                           var7.setParent(var8);
                        }

                        var7.baseName = var0;
                        var7.localeID = var1;
                     } catch (Exception var18) {
                        try {
                           var25.close();
                        } catch (Exception var16) {
                        }
                        break label128;
                     }

                     try {
                        var25.close();
                     } catch (Exception var17) {
                     }
                  }
               }

               if (var7 == null) {
                  String var13 = var6.toString();
                  if (var1.length() > 0 && var1.indexOf(95) < 0 && var13.indexOf(var1) == -1) {
                     var7 = (ResourceBundleWrapper)loadFromCache(var4, var0 + "_" + var13, var6);
                     if (var7 == null) {
                        var7 = (ResourceBundleWrapper)instantiateBundle(var0, var13, var4, var3);
                     }
                  }
               }

               if (var7 == null) {
                  var7 = var8;
               }
            } catch (Exception var19) {
               if (DEBUG) {
                  System.out.println("failure");
               }

               if (DEBUG) {
                  System.out.println(var19);
               }
            }
         }

         var7 = (ResourceBundleWrapper)addToCache(var2, var5, var6, var7);
      }

      if (var7 != null) {
         var7.initKeysVector();
      } else if (DEBUG) {
         System.out.println("Returning null for " + var0 + "_" + var1);
      }

      return var7;
   }
}
