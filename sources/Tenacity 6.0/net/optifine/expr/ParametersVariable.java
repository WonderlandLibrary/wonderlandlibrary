// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class ParametersVariable implements IParameters
{
    private ExpressionType[] first;
    private ExpressionType[] repeat;
    private ExpressionType[] last;
    private int maxCount;
    private static final ExpressionType[] EMPTY;
    
    public ParametersVariable() {
        this(null, null, null);
    }
    
    public ParametersVariable(final ExpressionType[] first, final ExpressionType[] repeat, final ExpressionType[] last) {
        this(first, repeat, last, Integer.MAX_VALUE);
    }
    
    public ParametersVariable(final ExpressionType[] first, final ExpressionType[] repeat, final ExpressionType[] last, final int maxCount) {
        this.maxCount = Integer.MAX_VALUE;
        this.first = normalize(first);
        this.repeat = normalize(repeat);
        this.last = normalize(last);
        this.maxCount = maxCount;
    }
    
    private static ExpressionType[] normalize(final ExpressionType[] exprs) {
        return (exprs == null) ? ParametersVariable.EMPTY : exprs;
    }
    
    public ExpressionType[] getFirst() {
        return this.first;
    }
    
    public ExpressionType[] getRepeat() {
        return this.repeat;
    }
    
    public ExpressionType[] getLast() {
        return this.last;
    }
    
    public int getCountRepeat() {
        return (this.first == null) ? 0 : this.first.length;
    }
    
    @Override
    public ExpressionType[] getParameterTypes(final IExpression[] arguments) {
        final int i = this.first.length + this.last.length;
        final int j = arguments.length - i;
        int k = 0;
        for (int l = 0; l + this.repeat.length <= j && i + l + this.repeat.length <= this.maxCount; l += this.repeat.length) {
            ++k;
        }
        final List<ExpressionType> list = new ArrayList<ExpressionType>();
        list.addAll(Arrays.asList(this.first));
        for (int i2 = 0; i2 < k; ++i2) {
            list.addAll(Arrays.asList(this.repeat));
        }
        list.addAll(Arrays.asList(this.last));
        final ExpressionType[] aexpressiontype = list.toArray(new ExpressionType[list.size()]);
        return aexpressiontype;
    }
    
    public ParametersVariable first(final ExpressionType... first) {
        return new ParametersVariable(first, this.repeat, this.last);
    }
    
    public ParametersVariable repeat(final ExpressionType... repeat) {
        return new ParametersVariable(this.first, repeat, this.last);
    }
    
    public ParametersVariable last(final ExpressionType... last) {
        return new ParametersVariable(this.first, this.repeat, last);
    }
    
    public ParametersVariable maxCount(final int maxCount) {
        return new ParametersVariable(this.first, this.repeat, this.last, maxCount);
    }
    
    static {
        EMPTY = new ExpressionType[0];
    }
}
