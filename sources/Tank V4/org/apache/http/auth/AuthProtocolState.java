package org.apache.http.auth;

public enum AuthProtocolState {
   UNCHALLENGED,
   CHALLENGED,
   HANDSHAKE,
   FAILURE,
   SUCCESS;

   private static final AuthProtocolState[] $VALUES = new AuthProtocolState[]{UNCHALLENGED, CHALLENGED, HANDSHAKE, FAILURE, SUCCESS};
}
