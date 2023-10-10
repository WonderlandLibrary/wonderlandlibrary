// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.net;

import javax.annotation.CheckForNull;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import com.google.thirdparty.publicsuffix.PublicSuffixType;
import com.google.common.base.Optional;
import java.util.List;
import com.google.common.base.Preconditions;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.CharMatcher;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.Immutable;

@Immutable
@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible(emulated = true)
public final class InternetDomainName
{
    private static final CharMatcher DOTS_MATCHER;
    private static final Splitter DOT_SPLITTER;
    private static final Joiner DOT_JOINER;
    private static final int NO_SUFFIX_FOUND = -1;
    private static final int MAX_PARTS = 127;
    private static final int MAX_LENGTH = 253;
    private static final int MAX_DOMAIN_PART_LENGTH = 63;
    private final String name;
    private final ImmutableList<String> parts;
    private final int publicSuffixIndex;
    private final int registrySuffixIndex;
    private static final CharMatcher DASH_MATCHER;
    private static final CharMatcher DIGIT_MATCHER;
    private static final CharMatcher LETTER_MATCHER;
    private static final CharMatcher PART_CHAR_MATCHER;
    
    InternetDomainName(String name) {
        name = Ascii.toLowerCase(InternetDomainName.DOTS_MATCHER.replaceFrom(name, '.'));
        if (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1);
        }
        Preconditions.checkArgument(name.length() <= 253, "Domain name too long: '%s':", name);
        this.name = name;
        this.parts = ImmutableList.copyOf((Iterable<? extends String>)InternetDomainName.DOT_SPLITTER.split(name));
        Preconditions.checkArgument(this.parts.size() <= 127, "Domain has too many parts: '%s'", name);
        Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", name);
        this.publicSuffixIndex = this.findSuffixOfType(Optional.absent());
        this.registrySuffixIndex = this.findSuffixOfType(Optional.of(PublicSuffixType.REGISTRY));
    }
    
    private int findSuffixOfType(final Optional<PublicSuffixType> desiredType) {
        for (int partsSize = this.parts.size(), i = 0; i < partsSize; ++i) {
            final String ancestorName = InternetDomainName.DOT_JOINER.join(this.parts.subList(i, partsSize));
            if (matchesType(desiredType, Optional.fromNullable(PublicSuffixPatterns.EXACT.get(ancestorName)))) {
                return i;
            }
            if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
                return i + 1;
            }
            if (matchesWildcardSuffixType(desiredType, ancestorName)) {
                return i;
            }
        }
        return -1;
    }
    
    public static InternetDomainName from(final String domain) {
        return new InternetDomainName(Preconditions.checkNotNull(domain));
    }
    
    private static boolean validateSyntax(final List<String> parts) {
        final int lastIndex = parts.size() - 1;
        if (!validatePart(parts.get(lastIndex), true)) {
            return false;
        }
        for (int i = 0; i < lastIndex; ++i) {
            final String part = parts.get(i);
            if (!validatePart(part, false)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean validatePart(final String part, final boolean isFinalPart) {
        if (part.length() < 1 || part.length() > 63) {
            return false;
        }
        final String asciiChars = CharMatcher.ascii().retainFrom(part);
        return InternetDomainName.PART_CHAR_MATCHER.matchesAllOf(asciiChars) && !InternetDomainName.DASH_MATCHER.matches(part.charAt(0)) && !InternetDomainName.DASH_MATCHER.matches(part.charAt(part.length() - 1)) && (!isFinalPart || !InternetDomainName.DIGIT_MATCHER.matches(part.charAt(0)));
    }
    
    public ImmutableList<String> parts() {
        return this.parts;
    }
    
    public boolean isPublicSuffix() {
        return this.publicSuffixIndex == 0;
    }
    
    public boolean hasPublicSuffix() {
        return this.publicSuffixIndex != -1;
    }
    
    @CheckForNull
    public InternetDomainName publicSuffix() {
        return this.hasPublicSuffix() ? this.ancestor(this.publicSuffixIndex) : null;
    }
    
    public boolean isUnderPublicSuffix() {
        return this.publicSuffixIndex > 0;
    }
    
    public boolean isTopPrivateDomain() {
        return this.publicSuffixIndex == 1;
    }
    
    public InternetDomainName topPrivateDomain() {
        if (this.isTopPrivateDomain()) {
            return this;
        }
        Preconditions.checkState(this.isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
        return this.ancestor(this.publicSuffixIndex - 1);
    }
    
    public boolean isRegistrySuffix() {
        return this.registrySuffixIndex == 0;
    }
    
    public boolean hasRegistrySuffix() {
        return this.registrySuffixIndex != -1;
    }
    
    @CheckForNull
    public InternetDomainName registrySuffix() {
        return this.hasRegistrySuffix() ? this.ancestor(this.registrySuffixIndex) : null;
    }
    
    public boolean isUnderRegistrySuffix() {
        return this.registrySuffixIndex > 0;
    }
    
    public boolean isTopDomainUnderRegistrySuffix() {
        return this.registrySuffixIndex == 1;
    }
    
    public InternetDomainName topDomainUnderRegistrySuffix() {
        if (this.isTopDomainUnderRegistrySuffix()) {
            return this;
        }
        Preconditions.checkState(this.isUnderRegistrySuffix(), "Not under a registry suffix: %s", this.name);
        return this.ancestor(this.registrySuffixIndex - 1);
    }
    
    public boolean hasParent() {
        return this.parts.size() > 1;
    }
    
    public InternetDomainName parent() {
        Preconditions.checkState(this.hasParent(), "Domain '%s' has no parent", this.name);
        return this.ancestor(1);
    }
    
    private InternetDomainName ancestor(final int levels) {
        return from(InternetDomainName.DOT_JOINER.join(this.parts.subList(levels, this.parts.size())));
    }
    
    public InternetDomainName child(final String leftParts) {
        final String s = Preconditions.checkNotNull(leftParts);
        final String name = this.name;
        return from(new StringBuilder(1 + String.valueOf(s).length() + String.valueOf(name).length()).append(s).append(".").append(name).toString());
    }
    
    public static boolean isValid(final String name) {
        try {
            from(name);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private static boolean matchesWildcardSuffixType(final Optional<PublicSuffixType> desiredType, final String domain) {
        final List<String> pieces = InternetDomainName.DOT_SPLITTER.limit(2).splitToList(domain);
        return pieces.size() == 2 && matchesType(desiredType, Optional.fromNullable(PublicSuffixPatterns.UNDER.get(pieces.get(1))));
    }
    
    private static boolean matchesType(final Optional<PublicSuffixType> desiredType, final Optional<PublicSuffixType> actualType) {
        return desiredType.isPresent() ? desiredType.equals(actualType) : actualType.isPresent();
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof InternetDomainName) {
            final InternetDomainName that = (InternetDomainName)object;
            return this.name.equals(that.name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    static {
        DOTS_MATCHER = CharMatcher.anyOf(".\u3002\uff0e\uff61");
        DOT_SPLITTER = Splitter.on('.');
        DOT_JOINER = Joiner.on('.');
        DASH_MATCHER = CharMatcher.anyOf("-_");
        DIGIT_MATCHER = CharMatcher.inRange('0', '9');
        LETTER_MATCHER = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z'));
        PART_CHAR_MATCHER = InternetDomainName.DIGIT_MATCHER.or(InternetDomainName.LETTER_MATCHER).or(InternetDomainName.DASH_MATCHER);
    }
}
