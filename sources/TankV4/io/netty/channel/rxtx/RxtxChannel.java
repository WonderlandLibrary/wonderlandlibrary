package io.netty.channel.rxtx;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPromise;
import io.netty.channel.oio.OioByteStreamChannel;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class RxtxChannel extends OioByteStreamChannel {
   private static final RxtxDeviceAddress LOCAL_ADDRESS = new RxtxDeviceAddress("localhost");
   private final RxtxChannelConfig config = new DefaultRxtxChannelConfig(this);
   private boolean open = true;
   private RxtxDeviceAddress deviceAddress;
   private SerialPort serialPort;

   public RxtxChannel() {
      super((Channel)null);
   }

   public RxtxChannelConfig config() {
      return this.config;
   }

   public boolean isOpen() {
      return this.open;
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new RxtxChannel.RxtxUnsafe(this);
   }

   protected void doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      RxtxDeviceAddress var3 = (RxtxDeviceAddress)var1;
      CommPortIdentifier var4 = CommPortIdentifier.getPortIdentifier(var3.value());
      CommPort var5 = var4.open(this.getClass().getName(), 1000);
      this.deviceAddress = var3;
      this.serialPort = (SerialPort)var5;
   }

   protected void doInit() throws Exception {
      this.serialPort.setSerialPortParams((Integer)this.config().getOption(RxtxChannelOption.BAUD_RATE), ((RxtxChannelConfig.Databits)this.config().getOption(RxtxChannelOption.DATA_BITS)).value(), ((RxtxChannelConfig.Stopbits)this.config().getOption(RxtxChannelOption.STOP_BITS)).value(), ((RxtxChannelConfig.Paritybit)this.config().getOption(RxtxChannelOption.PARITY_BIT)).value());
      this.serialPort.setDTR((Boolean)this.config().getOption(RxtxChannelOption.DTR));
      this.serialPort.setRTS((Boolean)this.config().getOption(RxtxChannelOption.RTS));
      this.activate(this.serialPort.getInputStream(), this.serialPort.getOutputStream());
   }

   public RxtxDeviceAddress localAddress() {
      return (RxtxDeviceAddress)super.localAddress();
   }

   public RxtxDeviceAddress remoteAddress() {
      return (RxtxDeviceAddress)super.remoteAddress();
   }

   protected RxtxDeviceAddress localAddress0() {
      return LOCAL_ADDRESS;
   }

   protected RxtxDeviceAddress remoteAddress0() {
      return this.deviceAddress;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      this.open = false;
      super.doClose();
      if (this.serialPort != null) {
         this.serialPort.removeEventListener();
         this.serialPort.close();
         this.serialPort = null;
      }

   }

   protected SocketAddress remoteAddress0() {
      return this.remoteAddress0();
   }

   protected SocketAddress localAddress0() {
      return this.localAddress0();
   }

   public SocketAddress remoteAddress() {
      return this.remoteAddress();
   }

   public SocketAddress localAddress() {
      return this.localAddress();
   }

   public ChannelConfig config() {
      return this.config();
   }

   private final class RxtxUnsafe extends AbstractChannel.AbstractUnsafe {
      final RxtxChannel this$0;

      private RxtxUnsafe(RxtxChannel var1) {
         super();
         this.this$0 = var1;
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if (this.ensureOpen(var3)) {
            try {
               boolean var4 = this.this$0.isActive();
               this.this$0.doConnect(var1, var2);
               int var5 = (Integer)this.this$0.config().getOption(RxtxChannelOption.WAIT_TIME);
               if (var5 > 0) {
                  this.this$0.eventLoop().schedule(new Runnable(this, var3, var4) {
                     final ChannelPromise val$promise;
                     final boolean val$wasActive;
                     final RxtxChannel.RxtxUnsafe this$1;

                     {
                        this.this$1 = var1;
                        this.val$promise = var2;
                        this.val$wasActive = var3;
                     }

                     public void run() {
                        try {
                           this.this$1.this$0.doInit();
                           this.val$promise.setSuccess();
                           if (!this.val$wasActive && this.this$1.this$0.isActive()) {
                              this.this$1.this$0.pipeline().fireChannelActive();
                           }
                        } catch (Throwable var2) {
                           this.val$promise.setFailure(var2);
                           RxtxChannel.RxtxUnsafe.access$100(this.this$1);
                        }

                     }
                  }, (long)var5, TimeUnit.MILLISECONDS);
               } else {
                  this.this$0.doInit();
                  var3.setSuccess();
                  if (!var4 && this.this$0.isActive()) {
                     this.this$0.pipeline().fireChannelActive();
                  }
               }
            } catch (Throwable var6) {
               var3.setFailure(var6);
               this.closeIfClosed();
            }

         }
      }

      RxtxUnsafe(RxtxChannel var1, Object var2) {
         this(var1);
      }

      static void access$100(RxtxChannel.RxtxUnsafe var0) {
         var0.closeIfClosed();
      }
   }
}
