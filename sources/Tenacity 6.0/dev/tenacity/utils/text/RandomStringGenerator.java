// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.lang3.Validate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.Set;

public final class RandomStringGenerator
{
    private final int minimumCodePoint;
    private final int maximumCodePoint;
    private final Set<CharacterPredicate> inclusivePredicates;
    private final TextRandomProvider random;
    private final List<Character> characterList;
    
    private RandomStringGenerator(final int minimumCodePoint, final int maximumCodePoint, final Set<CharacterPredicate> inclusivePredicates, final TextRandomProvider random, final List<Character> characterList) {
        this.minimumCodePoint = minimumCodePoint;
        this.maximumCodePoint = maximumCodePoint;
        this.inclusivePredicates = inclusivePredicates;
        this.random = random;
        this.characterList = characterList;
    }
    
    private int generateRandomNumber(final int minInclusive, final int maxInclusive) {
        if (this.random != null) {
            return this.random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
        }
        return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
    }
    
    private int generateRandomNumber(final List<Character> characterList) {
        final int listSize = characterList.size();
        if (this.random != null) {
            return String.valueOf(characterList.get(this.random.nextInt(listSize))).codePointAt(0);
        }
        return String.valueOf(characterList.get(ThreadLocalRandom.current().nextInt(0, listSize))).codePointAt(0);
    }
    
    public String generate(final int length) {
        if (length == 0) {
            return "";
        }
        Validate.isTrue(length > 0, "Length %d is smaller than zero.", (long)length);
        final StringBuilder builder = new StringBuilder(length);
        long remaining = length;
        do {
            int codePoint;
            if (this.characterList != null && !this.characterList.isEmpty()) {
                codePoint = this.generateRandomNumber(this.characterList);
            }
            else {
                codePoint = this.generateRandomNumber(this.minimumCodePoint, this.maximumCodePoint);
            }
            switch (Character.getType(codePoint)) {
                case 0:
                case 18:
                case 19: {
                    continue;
                }
                default: {
                    if (this.inclusivePredicates != null) {
                        boolean matchedFilter = false;
                        for (final CharacterPredicate predicate : this.inclusivePredicates) {
                            if (predicate.test(codePoint)) {
                                matchedFilter = true;
                                break;
                            }
                        }
                        if (!matchedFilter) {
                            continue;
                        }
                    }
                    builder.appendCodePoint(codePoint);
                    --remaining;
                    continue;
                }
            }
        } while (remaining != 0L);
        return builder.toString();
    }
    
    public String generate(final int minLengthInclusive, final int maxLengthInclusive) {
        Validate.isTrue(minLengthInclusive >= 0, "Minimum length %d is smaller than zero.", (long)minLengthInclusive);
        Validate.isTrue(minLengthInclusive <= maxLengthInclusive, "Maximum length %d is smaller than minimum length %d.", new Object[] { maxLengthInclusive, minLengthInclusive });
        return this.generate(this.generateRandomNumber(minLengthInclusive, maxLengthInclusive));
    }
    
    public static class Builder implements dev.tenacity.utils.text.Builder<RandomStringGenerator>
    {
        public static final int DEFAULT_MAXIMUM_CODE_POINT = 1114111;
        public static final int DEFAULT_LENGTH = 0;
        public static final int DEFAULT_MINIMUM_CODE_POINT = 0;
        private int minimumCodePoint;
        private int maximumCodePoint;
        private Set<CharacterPredicate> inclusivePredicates;
        private TextRandomProvider random;
        private List<Character> characterList;
        
        public Builder() {
            this.minimumCodePoint = 0;
            this.maximumCodePoint = 1114111;
        }
        
        public Builder withinRange(final int minimumCodePoint, final int maximumCodePoint) {
            Validate.isTrue(minimumCodePoint <= maximumCodePoint, "Minimum code point %d is larger than maximum code point %d", new Object[] { minimumCodePoint, maximumCodePoint });
            Validate.isTrue(minimumCodePoint >= 0, "Minimum code point %d is negative", (long)minimumCodePoint);
            Validate.isTrue(maximumCodePoint <= 1114111, "Value %d is larger than Character.MAX_CODE_POINT.", (long)maximumCodePoint);
            this.minimumCodePoint = minimumCodePoint;
            this.maximumCodePoint = maximumCodePoint;
            return this;
        }
        
        public Builder withinRange(final char[]... pairs) {
            this.characterList = new ArrayList<Character>();
            for (final char[] pair : pairs) {
                Validate.isTrue(pair.length == 2, "Each pair must contain minimum and maximum code point", new Object[0]);
                final int minimumCodePoint = pair[0];
                final int maximumCodePoint = pair[1];
                Validate.isTrue(minimumCodePoint <= maximumCodePoint, "Minimum code point %d is larger than maximum code point %d", new Object[] { minimumCodePoint, maximumCodePoint });
                for (int index = minimumCodePoint; index <= maximumCodePoint; ++index) {
                    this.characterList.add((char)index);
                }
            }
            return this;
        }
        
        public Builder filteredBy(final CharacterPredicate... predicates) {
            if (ArrayUtils.isEmpty((Object[])predicates)) {
                this.inclusivePredicates = null;
                return this;
            }
            if (this.inclusivePredicates == null) {
                this.inclusivePredicates = new HashSet<CharacterPredicate>();
            }
            else {
                this.inclusivePredicates.clear();
            }
            Collections.addAll(this.inclusivePredicates, predicates);
            return this;
        }
        
        public Builder usingRandom(final TextRandomProvider random) {
            this.random = random;
            return this;
        }
        
        public Builder selectFrom(final char... chars) {
            this.characterList = new ArrayList<Character>();
            for (final char c : chars) {
                this.characterList.add(c);
            }
            return this;
        }
        
        @Override
        public RandomStringGenerator build() {
            return new RandomStringGenerator(this.minimumCodePoint, this.maximumCodePoint, this.inclusivePredicates, this.random, this.characterList, null);
        }
    }
}
