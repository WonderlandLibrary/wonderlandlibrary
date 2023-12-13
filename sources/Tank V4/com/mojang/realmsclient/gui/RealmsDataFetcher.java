package com.mojang.realmsclient.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import net.minecraft.realms.Realms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

public class RealmsDataFetcher {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
   private static final int SERVER_UPDATE_INTERVAL = 60;
   private static final int PENDING_INVITES_INTERVAL = 10;
   private volatile boolean stopped = true;
   private RealmsDataFetcher.ServerListUpdateTask serverListUpdateTask = new RealmsDataFetcher.ServerListUpdateTask(this);
   private RealmsDataFetcher.PendingInviteUpdateTask pendingInviteUpdateTask = new RealmsDataFetcher.PendingInviteUpdateTask(this);
   private Set removedServers = Sets.newHashSet();
   private List servers = Lists.newArrayList();
   private int pendingInvitesCount;
   private ScheduledFuture serverListScheduledFuture;
   private ScheduledFuture pendingInviteScheduledFuture;
   private Map fetchStatus = new ConcurrentHashMap(RealmsDataFetcher.Task.values().length);

   public RealmsDataFetcher() {
      this.scheduleTasks();
   }

   public synchronized void init() {
      if (this.stopped) {
         this.stopped = false;
         this.cancelTasks();
         this.scheduleTasks();
      }

   }

   public synchronized boolean isFetchedSinceLastTry(RealmsDataFetcher.Task var1) {
      Boolean var2 = (Boolean)this.fetchStatus.get(var1.toString());
      return var2 == null ? false : var2;
   }

   public synchronized void markClean() {
      Iterator var1 = this.fetchStatus.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         this.fetchStatus.put(var2, false);
      }

   }

   public synchronized List getServers() {
      return Lists.newArrayList((Iterable)this.servers);
   }

   public int getPendingInvitesCount() {
      return this.pendingInvitesCount;
   }

   public synchronized void stop() {
      this.stopped = true;
      this.cancelTasks();
   }

   private void scheduleTasks() {
      this.serverListScheduledFuture = this.scheduler.scheduleAtFixedRate(this.serverListUpdateTask, 0L, 60L, TimeUnit.SECONDS);
      this.pendingInviteScheduledFuture = this.scheduler.scheduleAtFixedRate(this.pendingInviteUpdateTask, 0L, 10L, TimeUnit.SECONDS);
      RealmsDataFetcher.Task[] var1 = RealmsDataFetcher.Task.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         RealmsDataFetcher.Task var4 = var1[var3];
         this.fetchStatus.put(var4.toString(), false);
      }

   }

   private void cancelTasks() {
      try {
         this.serverListScheduledFuture.cancel(false);
         this.pendingInviteScheduledFuture.cancel(false);
      } catch (Exception var2) {
         LOGGER.error("Failed to cancel Realms tasks");
      }

   }

   private synchronized void setServers(List var1) {
      int var2 = 0;
      Iterator var3 = this.removedServers.iterator();

      while(var3.hasNext()) {
         RealmsServer var4 = (RealmsServer)var3.next();
         if (var1.remove(var4)) {
            ++var2;
         }
      }

      if (var2 == 0) {
         this.removedServers.clear();
      }

      this.servers = var1;
   }

   public synchronized void removeItem(RealmsServer var1) {
      this.servers.remove(var1);
      this.removedServers.add(var1);
   }

   private void sort(List var1) {
      Collections.sort(var1, new RealmsServer.McoServerComparator(Realms.getName()));
   }

   private boolean isActive() {
      return !this.stopped && Display.isActive();
   }

   static boolean access$200(RealmsDataFetcher var0) {
      return var0.isActive();
   }

   static void access$300(RealmsDataFetcher var0, List var1) {
      var0.sort(var1);
   }

   static void access$400(RealmsDataFetcher var0, List var1) {
      var0.setServers(var1);
   }

   static Map access$500(RealmsDataFetcher var0) {
      return var0.fetchStatus;
   }

   static Logger access$600() {
      return LOGGER;
   }

   static int access$702(RealmsDataFetcher var0, int var1) {
      return var0.pendingInvitesCount = var1;
   }

   public static enum Task {
      SERVER_LIST,
      PENDING_INVITE;

      private static final RealmsDataFetcher.Task[] $VALUES = new RealmsDataFetcher.Task[]{SERVER_LIST, PENDING_INVITE};
   }

   private class PendingInviteUpdateTask implements Runnable {
      final RealmsDataFetcher this$0;

      private PendingInviteUpdateTask(RealmsDataFetcher var1) {
         this.this$0 = var1;
      }

      public void run() {
         if (RealmsDataFetcher.access$200(this.this$0)) {
            this.updatePendingInvites();
         }

      }

      private void updatePendingInvites() {
         try {
            RealmsClient var1 = RealmsClient.createRealmsClient();
            if (var1 != null) {
               RealmsDataFetcher.access$702(this.this$0, var1.pendingInvitesCount());
               RealmsDataFetcher.access$500(this.this$0).put(RealmsDataFetcher.Task.PENDING_INVITE.toString(), true);
            }
         } catch (RealmsServiceException var2) {
            RealmsDataFetcher.access$600().error((String)"Couldn't get pending invite count", (Throwable)var2);
         }

      }

      PendingInviteUpdateTask(RealmsDataFetcher var1, Object var2) {
         this(var1);
      }
   }

   private class ServerListUpdateTask implements Runnable {
      final RealmsDataFetcher this$0;

      private ServerListUpdateTask(RealmsDataFetcher var1) {
         this.this$0 = var1;
      }

      public void run() {
         if (RealmsDataFetcher.access$200(this.this$0)) {
            this.updateServersList();
         }

      }

      private void updateServersList() {
         try {
            RealmsClient var1 = RealmsClient.createRealmsClient();
            if (var1 != null) {
               List var2 = var1.listWorlds().servers;
               if (var2 != null) {
                  RealmsDataFetcher.access$300(this.this$0, var2);
                  RealmsDataFetcher.access$400(this.this$0, var2);
                  RealmsDataFetcher.access$500(this.this$0).put(RealmsDataFetcher.Task.SERVER_LIST.toString(), true);
               }
            }
         } catch (RealmsServiceException var3) {
            RealmsDataFetcher.access$600().error((String)"Couldn't get server list", (Throwable)var3);
         } catch (IOException var4) {
            RealmsDataFetcher.access$600().error("Couldn't parse response from server getting list");
         }

      }

      ServerListUpdateTask(RealmsDataFetcher var1, Object var2) {
         this(var1);
      }
   }
}
