// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.tokens.Token;

public interface Scanner
{
    boolean checkToken(final Token.ID... p0);
    
    Token peekToken();
    
    Token getToken();
}
