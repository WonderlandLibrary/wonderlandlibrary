package com.mojang.realmsclient.gui.screens;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.ServerActivity;
import com.mojang.realmsclient.dto.ServerActivityList;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ActivityScreen extends RealmsScreen {
   private final RealmsScreen lastScreen;
   private final long serverId;
   private volatile List activityMap = new ArrayList();
   private ActivityScreen.DetailsList list;
   private int matrixWidth;
   private int matrixHeight;
   private String toolTip;
   private volatile List dayList = new ArrayList();
   private List colors = Arrays.asList(new ActivityScreen.Color(79, 243, 29), new ActivityScreen.Color(243, 175, 29), new ActivityScreen.Color(243, 29, 190), new ActivityScreen.Color(29, 165, 243), new ActivityScreen.Color(29, 243, 130), new ActivityScreen.Color(243, 29, 64), new ActivityScreen.Color(29, 74, 243));
   private int colorIndex = 0;
   private long periodInMillis;
   private int fontWidth;
   private int maxKeyWidth = 0;
   private Boolean noActivity = false;
   private static LoadingCache activitiesNameCache = CacheBuilder.newBuilder().build(new CacheLoader() {
      public String load(String var1) throws Exception {
         return Realms.uuidToName(var1);
      }

      public Object load(Object var1) throws Exception {
         return this.load((String)var1);
      }
   });

   public ActivityScreen(RealmsScreen var1, long var2) {
      this.lastScreen = var1;
      this.serverId = var2;
      this.getActivities();
   }

   public void mouseEvent() {
      super.mouseEvent();
      this.list.mouseEvent();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.matrixWidth = this.width();
      this.matrixHeight = this.height() - 40;
      this.fontWidth = this.fontWidth("A");
      this.list = new ActivityScreen.DetailsList(this);
      this.buttonsAdd(newButton(1, this.width() / 2 - 100, this.height() - 30, 200, 20, getLocalizedString("gui.back")));
   }

   private ActivityScreen.Color getColor() {
      if (this.colorIndex > this.colors.size() - 1) {
         this.colorIndex = 0;
      }

      ActivityScreen.Color var1 = (ActivityScreen.Color)this.colors.get(this.colorIndex);
      ++this.colorIndex;
      return var1;
   }

   private void getActivities() {
      (new Thread(this) {
         final ActivityScreen this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            RealmsClient var1 = RealmsClient.createRealmsClient();

            try {
               ServerActivityList var2 = var1.getActivity(ActivityScreen.access$000(this.this$0));
               ActivityScreen.access$102(this.this$0, ActivityScreen.access$200(this.this$0, var2));
               ArrayList var3 = new ArrayList();
               Iterator var4 = ActivityScreen.access$100(this.this$0).iterator();

               while(var4.hasNext()) {
                  ActivityScreen.ActivityRow var5 = (ActivityScreen.ActivityRow)var4.next();
                  Iterator var6 = var5.activities.iterator();

                  while(var6.hasNext()) {
                     ActivityScreen.Activity var7 = (ActivityScreen.Activity)var6.next();
                     String var8 = (new SimpleDateFormat("dd/MM")).format(new Date(var7.start));
                     ActivityScreen.Day var9 = new ActivityScreen.Day(var8, var7.start);
                     if (!var3.contains(var9)) {
                        var3.add(var9);
                     }
                  }
               }

               Collections.sort(var3, new Comparator(this) {
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                  }

                  public int compare(ActivityScreen.Day var1, ActivityScreen.Day var2) {
                     return var1.timestamp.compareTo(var2.timestamp);
                  }

                  public int compare(Object var1, Object var2) {
                     return this.compare((ActivityScreen.Day)var1, (ActivityScreen.Day)var2);
                  }
               });
               ActivityScreen.access$302(this.this$0, var3);
            } catch (RealmsServiceException var10) {
               var10.printStackTrace();
            }

         }
      }).start();
   }

   private List convertToActivityMatrix(ServerActivityList var1) {
      ArrayList var2 = Lists.newArrayList();
      this.periodInMillis = var1.periodInMillis;
      long var3 = System.currentTimeMillis() - var1.periodInMillis;
      Iterator var5 = var1.serverActivities.iterator();

      while(var5.hasNext()) {
         ServerActivity var6 = (ServerActivity)var5.next();
         ActivityScreen.ActivityRow var7 = this.find(var6.profileUuid, var2);
         Calendar var8 = Calendar.getInstance(TimeZone.getDefault());
         var8.setTimeInMillis(var6.joinTime);
         Calendar var9 = Calendar.getInstance(TimeZone.getDefault());
         var9.setTimeInMillis(var6.leaveTime);
         ActivityScreen.Activity var10 = new ActivityScreen.Activity(var3, var8.getTimeInMillis(), var9.getTimeInMillis());
         if (var7 == null) {
            String var11 = "";

            try {
               var11 = (String)activitiesNameCache.get(var6.profileUuid);
            } catch (Exception var13) {
               var13.printStackTrace();
            }

            var7 = new ActivityScreen.ActivityRow(var6.profileUuid, new ArrayList(), this.getColor(), var11, var6.profileUuid);
            var7.activities.add(var10);
            var2.add(var7);
         } else {
            var7.activities.add(var10);
         }
      }

      var5 = var2.iterator();

      while(var5.hasNext()) {
         ActivityScreen.ActivityRow var14 = (ActivityScreen.ActivityRow)var5.next();
         Collections.sort(var14.activities);
      }

      this.noActivity = var2.size() == 0;
      return var2;
   }

   private ActivityScreen.ActivityRow find(String var1, List var2) {
      Iterator var3 = var2.iterator();

      ActivityScreen.ActivityRow var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (ActivityScreen.ActivityRow)var3.next();
      } while(!var4.key.equals(var1));

      return var4;
   }

   public void tick() {
      super.tick();
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.id() == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.toolTip = null;
      this.renderBackground();
      this.list.render(var1, var2, var3);
      this.drawCenteredString(getLocalizedString("mco.activity.title"), this.width() / 2, 10, 16777215);
      if (this.toolTip != null) {
         this.renderMousehoverTooltip(this.toolTip, var1, var2);
      }

      if (this.noActivity) {
         this.drawCenteredString(getLocalizedString("mco.activity.noactivity", new Object[]{TimeUnit.DAYS.convert(this.periodInMillis, TimeUnit.MILLISECONDS)}), this.width() / 2, this.height() / 2 - 20, 16777215);
      }

      super.render(var1, var2, var3);
   }

   protected void renderMousehoverTooltip(String var1, int var2, int var3) {
      if (var1 != null) {
         int var4 = var2 - 80;
         int var5 = var3 - 12;
         int var6 = 0;
         int var7 = 0;
         String[] var8 = var1.split("\n");
         int var9 = var8.length;

         int var10;
         String var11;
         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            int var12 = this.fontWidth(var11);
            if (var12 > var7) {
               var7 = var12;
            }
         }

         var8 = var1.split("\n");
         var9 = var8.length;

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            this.fillGradient(var4 - 3, var5 - (var6 == 0 ? 3 : 0) + var6, var4 + var7 + 3, var5 + 8 + 3 + var6, -1073741824, -1073741824);
            this.fontDrawShadow(var11, var4, var5 + var6, -1);
            var6 += 10;
         }

      }
   }

   static long access$000(ActivityScreen var0) {
      return var0.serverId;
   }

   static List access$102(ActivityScreen var0, List var1) {
      return var0.activityMap = var1;
   }

   static List access$200(ActivityScreen var0, ServerActivityList var1) {
      return var0.convertToActivityMatrix(var1);
   }

   static List access$100(ActivityScreen var0) {
      return var0.activityMap;
   }

   static List access$302(ActivityScreen var0, List var1) {
      return var0.dayList = var1;
   }

   static int access$500(ActivityScreen var0) {
      return var0.fontWidth;
   }

   static int access$600(ActivityScreen var0) {
      return var0.maxKeyWidth;
   }

   static int access$602(ActivityScreen var0, int var1) {
      return var0.maxKeyWidth = var1;
   }

   static int access$700(ActivityScreen var0) {
      return var0.matrixWidth;
   }

   static List access$300(ActivityScreen var0) {
      return var0.dayList;
   }

   static String access$802(ActivityScreen var0, String var1) {
      return var0.toolTip = var1;
   }

   class DetailsList extends RealmsScrolledSelectionList {
      final ActivityScreen this$0;

      public DetailsList(ActivityScreen var1) {
         super(var1.width(), var1.height(), 30, var1.height() - 40, var1.fontLineHeight() + 1);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return ActivityScreen.access$100(this.this$0).size();
      }

      public void selectItem(int var1, boolean var2, int var3, int var4) {
      }

      public boolean isSelectedItem(int var1) {
         return false;
      }

      public void renderBackground() {
      }

      public int getMaxPosition() {
         return this.getItemCount() * (this.this$0.fontLineHeight() + 1) + 10;
      }

      protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
         if (ActivityScreen.access$100(this.this$0) != null && ActivityScreen.access$100(this.this$0).size() > 0) {
            this.this$0.drawString(((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).name, 20, var3, 16777215);
            int var8 = ActivityScreen.access$500(this.this$0) * ((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).name.length();
            if (var8 > ActivityScreen.access$600(this.this$0)) {
               ActivityScreen.access$602(this.this$0, var8 + 5);
            }

            byte var9 = 25;
            int var10 = ActivityScreen.access$600(this.this$0) + var9;
            int var11 = ActivityScreen.access$700(this.this$0) - var10 - 10;
            int var12 = ActivityScreen.access$300(this.this$0).size() < 1 ? 1 : ActivityScreen.access$300(this.this$0).size();
            int var13 = var11 / var12;
            double var14 = (double)var13 / 24.0D;
            double var16 = var14 / 60.0D;
            GL11.glDisable(3553);
            var5.begin();
            var5.color(((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).color.r, ((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).color.g, ((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).color.b);
            var5.vertex((double)(var10 - 8), (double)(var3 + 7), 0.0D);
            var5.vertex((double)(var10 - 3), (double)(var3 + 7), 0.0D);
            var5.vertex((double)(var10 - 3), (double)(var3 + 2), 0.0D);
            var5.vertex((double)(var10 - 8), (double)(var3 + 2), 0.0D);
            var5.end();
            GL11.glEnable(3553);
            GL11.glDisable(3553);
            var5.begin();
            var5.color(8421504);
            var5.vertex((double)var10, (double)(this.this$0.height() - 40), 0.0D);
            var5.vertex((double)(var10 + 1), (double)(this.this$0.height() - 40), 0.0D);
            var5.vertex((double)(var10 + 1), (double)(-this.this$0.height()), 0.0D);
            var5.vertex((double)var10, (double)(-this.this$0.height()), 0.0D);
            var5.end();
            GL11.glEnable(3553);
            int var18 = 1;

            for(Iterator var19 = ActivityScreen.access$300(this.this$0).iterator(); var19.hasNext(); ++var18) {
               ActivityScreen.Day var20 = (ActivityScreen.Day)var19.next();
               this.this$0.drawString(var20.day, var10 + (var18 - 1) * var13 + (var13 - this.this$0.fontWidth(var20.day)) / 2 + 2, this.this$0.height() - 52, 16777215);
               GL11.glDisable(3553);
               var5.begin();
               var5.color(8421504);
               var5.vertex((double)(var10 + var18 * var13), (double)(this.this$0.height() - 40), 0.0D);
               var5.vertex((double)(var10 + var18 * var13 + 1), (double)(this.this$0.height() - 40), 0.0D);
               var5.vertex((double)(var10 + var18 * var13 + 1), (double)(-this.this$0.height()), 0.0D);
               var5.vertex((double)(var10 + var18 * var13), (double)(-this.this$0.height()), 0.0D);
               var5.end();
               GL11.glEnable(3553);
               Iterator var21 = ((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).activities.iterator();

               while(var21.hasNext()) {
                  ActivityScreen.Activity var22 = (ActivityScreen.Activity)var21.next();
                  String var23 = (new SimpleDateFormat("dd/MM")).format(new Date(var22.start));
                  if (var23.equals(var20.day)) {
                     int var24 = var22.minuteIndice();
                     int var25 = var22.hourIndice();
                     double var26 = var16 * (double)TimeUnit.MINUTES.convert(var22.end - var22.start, TimeUnit.MILLISECONDS);
                     if (var26 < 3.0D) {
                        var26 = 3.0D;
                     }

                     GL11.glDisable(3553);
                     var5.begin();
                     var5.color(((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).color.r, ((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).color.g, ((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).color.b);
                     var5.vertex((double)(var10 + (var13 * var18 - var13)) + (double)var25 * var14 + (double)var24 * var16, (double)(var3 + 7), 0.0D);
                     var5.vertex((double)(var10 + (var13 * var18 - var13)) + (double)var25 * var14 + (double)var24 * var16 + var26, (double)(var3 + 7), 0.0D);
                     var5.vertex((double)(var10 + (var13 * var18 - var13)) + (double)var25 * var14 + (double)var24 * var16 + var26, (double)(var3 + 2), 0.0D);
                     var5.vertex((double)(var10 + (var13 * var18 - var13)) + (double)var25 * var14 + (double)var24 * var16, (double)(var3 + 2), 0.0D);
                     var5.end();
                     GL11.glEnable(3553);
                     if ((double)this.xm() >= (double)(var10 + (var13 * var18 - var13)) + (double)var25 * var14 + (double)var24 * var16 && (double)this.xm() <= (double)(var10 + (var13 * var18 - var13)) + (double)var25 * var14 + (double)var24 * var16 + var26 && this.ym() >= var3 && this.ym() <= var3 + 10) {
                        SimpleDateFormat var28 = new SimpleDateFormat("HH:mm");
                        Date var29 = new Date(var22.start);
                        Date var30 = new Date(var22.end);
                        int var31 = (int)Math.ceil((double)TimeUnit.SECONDS.convert(var22.end - var22.start, TimeUnit.MILLISECONDS) / 60.0D);
                        if (var31 < 1) {
                           var31 = 1;
                        }

                        ActivityScreen.access$802(this.this$0, "[" + var28.format(var29) + " - " + var28.format(var30) + "]\n" + var31 + (var31 > 1 ? " minutes" : " minute"));
                     }
                  }
               }
            }

            RealmsScreen.bindFace(((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).uuid, ((ActivityScreen.ActivityRow)ActivityScreen.access$100(this.this$0).get(var1)).name);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RealmsScreen.blit(10, var3, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
            RealmsScreen.blit(10, var3, 40.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
         }

      }

      public int getScrollbarPosition() {
         return this.width() - 7;
      }
   }

   static class Activity implements Comparable {
      long base;
      long start;
      long end;

      private Activity(long var1, long var3, long var5) {
         this.base = var1;
         this.start = var3;
         this.end = var5;
      }

      public int compareTo(ActivityScreen.Activity var1) {
         return (int)(this.start - var1.start);
      }

      public int hourIndice() {
         String var1 = (new SimpleDateFormat("HH")).format(new Date(this.start));
         return Integer.parseInt(var1);
      }

      public int minuteIndice() {
         String var1 = (new SimpleDateFormat("mm")).format(new Date(this.start));
         return Integer.parseInt(var1);
      }

      public int compareTo(Object var1) {
         return this.compareTo((ActivityScreen.Activity)var1);
      }

      Activity(long var1, long var3, long var5, Object var7) {
         this(var1, var3, var5);
      }
   }

   static class ActivityRow {
      String key;
      List activities;
      ActivityScreen.Color color;
      String name;
      String uuid;

      ActivityRow(String var1, List var2, ActivityScreen.Color var3, String var4, String var5) {
         this.key = var1;
         this.activities = var2;
         this.color = var3;
         this.name = var4;
         this.uuid = var5;
      }
   }

   static class Day {
      String day;
      Long timestamp;

      Day(String var1, Long var2) {
         this.day = var1;
         this.timestamp = var2;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof ActivityScreen.Day)) {
            return false;
         } else {
            ActivityScreen.Day var2 = (ActivityScreen.Day)var1;
            return this.day.equals(var2.day);
         }
      }
   }

   static class Color {
      int r;
      int g;
      int b;

      Color(int var1, int var2, int var3) {
         this.r = var1;
         this.g = var2;
         this.b = var3;
      }
   }
}
