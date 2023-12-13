package org.apache.http;

import java.io.Serializable;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ProtocolVersion implements Serializable, Cloneable {
   private static final long serialVersionUID = 8950662842175091068L;
   protected final String protocol;
   protected final int major;
   protected final int minor;

   public ProtocolVersion(String var1, int var2, int var3) {
      this.protocol = (String)Args.notNull(var1, "Protocol name");
      this.major = Args.notNegative(var2, "Protocol minor version");
      this.minor = Args.notNegative(var3, "Protocol minor version");
   }

   public final String getProtocol() {
      return this.protocol;
   }

   public final int getMajor() {
      return this.major;
   }

   public final int getMinor() {
      return this.minor;
   }

   public ProtocolVersion forVersion(int var1, int var2) {
      return var1 == this.major && var2 == this.minor ? this : new ProtocolVersion(this.protocol, var1, var2);
   }

   public final int hashCode() {
      return this.protocol.hashCode() ^ this.major * 100000 ^ this.minor;
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ProtocolVersion)) {
         return false;
      } else {
         ProtocolVersion var2 = (ProtocolVersion)var1;
         return this.protocol.equals(var2.protocol) && this.major == var2.major && this.minor == var2.minor;
      }
   }

   public int compareToVersion(ProtocolVersion var1) {
      Args.notNull(var1, "Protocol version");
      Args.check(this.protocol.equals(var1.protocol), "Versions for different protocols cannot be compared: %s %s", this, var1);
      int var2 = this.getMajor() - var1.getMajor();
      if (var2 == 0) {
         var2 = this.getMinor() - var1.getMinor();
      }

      return var2;
   }

   public final boolean greaterEquals(ProtocolVersion var1) {
      return var1 != null && this.compareToVersion(var1) >= 0;
   }

   public final boolean lessEquals(ProtocolVersion var1) {
      return var1 != null && this.compareToVersion(var1) <= 0;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.protocol);
      var1.append('/');
      var1.append(Integer.toString(this.major));
      var1.append('.');
      var1.append(Integer.toString(this.minor));
      return var1.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
