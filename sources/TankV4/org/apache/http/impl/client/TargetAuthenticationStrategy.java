package org.apache.http.impl.client;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;

@Immutable
public class TargetAuthenticationStrategy extends AuthenticationStrategyImpl {
   public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();

   public TargetAuthenticationStrategy() {
      super(401, "WWW-Authenticate");
   }

   Collection getPreferredAuthSchemes(RequestConfig var1) {
      return var1.getTargetPreferredAuthSchemes();
   }

   public void authFailed(HttpHost var1, AuthScheme var2, HttpContext var3) {
      super.authFailed(var1, var2, var3);
   }

   public void authSucceeded(HttpHost var1, AuthScheme var2, HttpContext var3) {
      super.authSucceeded(var1, var2, var3);
   }

   public Queue select(Map var1, HttpHost var2, HttpResponse var3, HttpContext var4) throws MalformedChallengeException {
      return super.select(var1, var2, var3, var4);
   }

   public Map getChallenges(HttpHost var1, HttpResponse var2, HttpContext var3) throws MalformedChallengeException {
      return super.getChallenges(var1, var2, var3);
   }

   public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, HttpContext var3) {
      return super.isAuthenticationRequested(var1, var2, var3);
   }
}
