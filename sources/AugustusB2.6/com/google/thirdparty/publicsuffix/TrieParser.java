// 
// Decompiled by Procyon v0.5.36
// 

package com.google.thirdparty.publicsuffix;

import java.util.Deque;
import com.google.common.collect.Queues;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Joiner;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class TrieParser
{
    private static final Joiner PREFIX_JOINER;
    
    static ImmutableMap<String, PublicSuffixType> parseTrie(final CharSequence encoded) {
        final ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
        for (int encodedLen = encoded.length(), idx = 0; idx < encodedLen; idx += doParseTrieToBuilder((Deque<CharSequence>)Queues.newArrayDeque(), encoded, idx, builder)) {}
        return builder.build();
    }
    
    private static int doParseTrieToBuilder(final Deque<CharSequence> stack, final CharSequence encoded, final int start, final ImmutableMap.Builder<String, PublicSuffixType> builder) {
        final int encodedLen = encoded.length();
        int idx = start;
        char c = '\0';
        while (idx < encodedLen) {
            c = encoded.charAt(idx);
            if (c == '&' || c == '?' || c == '!' || c == ':') {
                break;
            }
            if (c == ',') {
                break;
            }
            ++idx;
        }
        stack.push(reverse(encoded.subSequence(start, idx)));
        if (c == '!' || c == '?' || c == ':' || c == ',') {
            final String domain = TrieParser.PREFIX_JOINER.join(stack);
            if (domain.length() > 0) {
                builder.put(domain, PublicSuffixType.fromCode(c));
            }
        }
        ++idx;
        if (c != '?' && c != ',') {
            while (idx < encodedLen) {
                idx += doParseTrieToBuilder(stack, encoded, idx, builder);
                if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
                    ++idx;
                    break;
                }
            }
        }
        stack.pop();
        return idx - start;
    }
    
    private static CharSequence reverse(final CharSequence s) {
        return new StringBuilder(s).reverse();
    }
    
    static {
        PREFIX_JOINER = Joiner.on("");
    }
}
