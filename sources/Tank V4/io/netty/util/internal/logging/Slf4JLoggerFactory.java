package io.netty.util.internal.logging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

public class Slf4JLoggerFactory extends InternalLoggerFactory {
   static final boolean $assertionsDisabled = !Slf4JLoggerFactory.class.desiredAssertionStatus();

   public Slf4JLoggerFactory() {
   }

   Slf4JLoggerFactory(boolean var1) {
      if (!$assertionsDisabled && !var1) {
         throw new AssertionError();
      } else {
         StringBuffer var2 = new StringBuffer();
         PrintStream var3 = System.err;

         try {
            System.setErr(new PrintStream(new OutputStream(this, var2) {
               final StringBuffer val$buf;
               final Slf4JLoggerFactory this$0;

               {
                  this.this$0 = var1;
                  this.val$buf = var2;
               }

               public void write(int var1) {
                  this.val$buf.append((char)var1);
               }
            }, true, "US-ASCII"));
         } catch (UnsupportedEncodingException var6) {
            throw new Error(var6);
         }

         if (LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
            throw new NoClassDefFoundError(var2.toString());
         } else {
            var3.print(var2.toString());
            var3.flush();
            System.setErr(var3);
         }
      }
   }

   public InternalLogger newInstance(String var1) {
      return new Slf4JLogger(LoggerFactory.getLogger(var1));
   }
}
