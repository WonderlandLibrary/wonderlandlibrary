/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.sctp;

import com.sun.nio.sctp.AbstractNotificationHandler;
import com.sun.nio.sctp.AssociationChangeNotification;
import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.Notification;
import com.sun.nio.sctp.PeerAddressChangeNotification;
import com.sun.nio.sctp.SendFailedNotification;
import com.sun.nio.sctp.ShutdownNotification;
import io.netty.channel.sctp.SctpChannel;

public final class SctpNotificationHandler
extends AbstractNotificationHandler<Object> {
    private final SctpChannel sctpChannel;

    private void fireEvent(Notification notification) {
        this.sctpChannel.pipeline().fireUserEventTriggered(notification);
    }

    @Override
    public HandlerResult handleNotification(PeerAddressChangeNotification peerAddressChangeNotification, Object object) {
        this.fireEvent(peerAddressChangeNotification);
        return HandlerResult.CONTINUE;
    }

    public SctpNotificationHandler(SctpChannel sctpChannel) {
        if (sctpChannel == null) {
            throw new NullPointerException("sctpChannel");
        }
        this.sctpChannel = sctpChannel;
    }

    @Override
    public HandlerResult handleNotification(ShutdownNotification shutdownNotification, Object object) {
        this.fireEvent(shutdownNotification);
        this.sctpChannel.close();
        return HandlerResult.RETURN;
    }

    @Override
    public HandlerResult handleNotification(SendFailedNotification sendFailedNotification, Object object) {
        this.fireEvent(sendFailedNotification);
        return HandlerResult.CONTINUE;
    }

    @Override
    public HandlerResult handleNotification(AssociationChangeNotification associationChangeNotification, Object object) {
        this.fireEvent(associationChangeNotification);
        return HandlerResult.CONTINUE;
    }
}

