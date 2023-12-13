package org.apache.http.impl.auth;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

/** @deprecated */
@Deprecated
public class NegotiateScheme extends GGSSchemeBase {
   private final Log log;
   private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
   private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
   private final SpnegoTokenGenerator spengoGenerator;

   public NegotiateScheme(SpnegoTokenGenerator var1, boolean var2) {
      super(var2);
      this.log = LogFactory.getLog(this.getClass());
      this.spengoGenerator = var1;
   }

   public NegotiateScheme(SpnegoTokenGenerator var1) {
      this(var1, false);
   }

   public NegotiateScheme() {
      this((SpnegoTokenGenerator)null, false);
   }

   public String getSchemeName() {
      return "Negotiate";
   }

   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      return this.authenticate(var1, var2, (HttpContext)null);
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      return super.authenticate(var1, var2, var3);
   }

   protected byte[] generateToken(byte[] var1, String var2) throws GSSException {
      Oid var3 = new Oid("1.3.6.1.5.5.2");
      byte[] var4 = var1;
      boolean var5 = false;

      try {
         var4 = this.generateGSSToken(var4, var3, var2);
      } catch (GSSException var8) {
         if (var8.getMajor() != 2) {
            throw var8;
         }

         this.log.debug("GSSException BAD_MECH, retry with Kerberos MECH");
         var5 = true;
      }

      if (var5) {
         this.log.debug("Using Kerberos MECH 1.2.840.113554.1.2.2");
         var3 = new Oid("1.2.840.113554.1.2.2");
         var4 = this.generateGSSToken(var4, var3, var2);
         if (var4 != null && this.spengoGenerator != null) {
            try {
               var4 = this.spengoGenerator.generateSpnegoDERObject(var4);
            } catch (IOException var7) {
               this.log.error(var7.getMessage(), var7);
            }
         }
      }

      return var4;
   }

   public String getParameter(String var1) {
      Args.notNull(var1, "Parameter name");
      return null;
   }

   public String getRealm() {
      return null;
   }

   public boolean isConnectionBased() {
      return true;
   }
}
