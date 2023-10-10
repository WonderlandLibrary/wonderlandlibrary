package io.netty.channel.sctp;

import com.sun.nio.sctp.AbstractNotificationHandler;
import com.sun.nio.sctp.AssociationChangeNotification;
import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.Notification;
import com.sun.nio.sctp.PeerAddressChangeNotification;
import com.sun.nio.sctp.SendFailedNotification;
import com.sun.nio.sctp.ShutdownNotification;

public final class SctpNotificationHandler extends AbstractNotificationHandler {
   private final SctpChannel sctpChannel;

   public SctpNotificationHandler(SctpChannel var1) {
      if (var1 == null) {
         throw new NullPointerException("sctpChannel");
      } else {
         this.sctpChannel = var1;
      }
   }

   public HandlerResult handleNotification(AssociationChangeNotification var1, Object var2) {
      this.fireEvent(var1);
      return HandlerResult.CONTINUE;
   }

   public HandlerResult handleNotification(PeerAddressChangeNotification var1, Object var2) {
      this.fireEvent(var1);
      return HandlerResult.CONTINUE;
   }

   public HandlerResult handleNotification(SendFailedNotification var1, Object var2) {
      this.fireEvent(var1);
      return HandlerResult.CONTINUE;
   }

   public HandlerResult handleNotification(ShutdownNotification var1, Object var2) {
      this.fireEvent(var1);
      this.sctpChannel.close();
      return HandlerResult.RETURN;
   }

   private void fireEvent(Notification var1) {
      this.sctpChannel.pipeline().fireUserEventTriggered(var1);
   }
}
