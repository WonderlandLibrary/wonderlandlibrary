package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsTermsScreen;
import java.io.IOException;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsConnect;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsConnectTask extends LongRunningTask {
   private static final Logger LOGGER = LogManager.getLogger();
   private final RealmsConnect realmsConnect;
   private final RealmsServer data;
   private final RealmsScreen onlineScreen;

   public RealmsConnectTask(RealmsScreen var1, RealmsServer var2) {
      this.onlineScreen = var1;
      this.realmsConnect = new RealmsConnect(var1);
      this.data = var2;
   }

   public void run() {
      this.setTitle(RealmsScreen.getLocalizedString("mco.connect.connecting"));
      RealmsClient var1 = RealmsClient.createRealmsClient();
      boolean var2 = false;
      boolean var3 = false;
      int var4 = 5;
      RealmsServerAddress var5 = null;
      boolean var6 = false;

      for(int var7 = 0; var7 < 10 && !this.aborted(); ++var7) {
         try {
            var5 = var1.join(this.data.id);
            var2 = true;
         } catch (RetryCallException var9) {
            var4 = var9.delaySeconds;
         } catch (RealmsServiceException var10) {
            if (var10.errorCode == 6002) {
               var6 = true;
            } else {
               var3 = true;
               this.error(var10.toString());
               LOGGER.error((String)"Couldn't connect to world", (Throwable)var10);
            }
            break;
         } catch (IOException var11) {
            LOGGER.error((String)"Couldn't parse response connecting to world", (Throwable)var11);
         } catch (Exception var12) {
            var3 = true;
            LOGGER.error((String)"Couldn't connect to world", (Throwable)var12);
            this.error(var12.getLocalizedMessage());
         }

         if (var2) {
            break;
         }

         this.sleep(var4);
      }

      if (var6) {
         Realms.setScreen(new RealmsTermsScreen(this.onlineScreen, this.data));
      } else if (!this.aborted() && !var3) {
         if (var2) {
            net.minecraft.realms.RealmsServerAddress var13 = net.minecraft.realms.RealmsServerAddress.parseString(var5.address);
            this.realmsConnect.connect(var13.getHost(), var13.getPort());
         } else {
            this.error(RealmsScreen.getLocalizedString("mco.errorMessage.connectionFailure"));
         }
      }

   }

   private void sleep(int var1) {
      try {
         Thread.sleep((long)(var1 * 1000));
      } catch (InterruptedException var3) {
         LOGGER.warn(var3.getLocalizedMessage());
      }

   }

   public void abortTask() {
      this.realmsConnect.abort();
   }

   public void tick() {
      this.realmsConnect.tick();
   }
}
