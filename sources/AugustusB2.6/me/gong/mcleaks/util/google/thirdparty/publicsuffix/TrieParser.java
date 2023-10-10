// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.thirdparty.publicsuffix;

import java.util.List;
import me.gong.mcleaks.util.google.common.collect.Lists;
import me.gong.mcleaks.util.google.common.collect.ImmutableMap;
import me.gong.mcleaks.util.google.common.base.Joiner;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
final class TrieParser
{
    private static final Joiner PREFIX_JOINER;
    
    static ImmutableMap<String, PublicSuffixType> parseTrie(final CharSequence encoded) {
        final ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
        for (int encodedLen = encoded.length(), idx = 0; idx < encodedLen; idx += doParseTrieToBuilder((List<CharSequence>)Lists.newLinkedList(), encoded.subSequence(idx, encodedLen), builder)) {}
        return builder.build();
    }
    
    private static int doParseTrieToBuilder(final List<CharSequence> stack, final CharSequence encoded, final ImmutableMap.Builder<String, PublicSuffixType> builder) {
        final int encodedLen = encoded.length();
        int idx = 0;
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
        stack.add(0, reverse(encoded.subSequence(0, idx)));
        if (c == '!' || c == '?' || c == ':' || c == ',') {
            final String domain = TrieParser.PREFIX_JOINER.join(stack);
            if (domain.length() > 0) {
                builder.put(domain, PublicSuffixType.fromCode(c));
            }
        }
        ++idx;
        if (c != '?' && c != ',') {
            while (idx < encodedLen) {
                idx += doParseTrieToBuilder(stack, encoded.subSequence(idx, encodedLen), builder);
                if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
                    ++idx;
                    break;
                }
            }
        }
        stack.remove(0);
        return idx;
    }
    
    private static CharSequence reverse(final CharSequence s) {
        return new StringBuilder(s).reverse();
    }
    
    static {
        PREFIX_JOINER = Joiner.on("");
    }
}
