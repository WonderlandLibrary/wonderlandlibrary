// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import com.google.common.collect.AbstractIterator;
import java.util.Locale;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import java.util.function.Function;

public class SearchTree<T> implements ISearchTree<T>
{
    protected SuffixArray<T> byName;
    protected SuffixArray<T> byId;
    private final Function<T, Iterable<String>> nameFunc;
    private final Function<T, Iterable<ResourceLocation>> idFunc;
    private final List<T> contents;
    private Object2IntMap<T> numericContents;
    
    public SearchTree(final Function<T, Iterable<String>> nameFuncIn, final Function<T, Iterable<ResourceLocation>> idFuncIn) {
        this.byName = new SuffixArray<T>();
        this.byId = new SuffixArray<T>();
        this.contents = (List<T>)Lists.newArrayList();
        this.numericContents = (Object2IntMap<T>)new Object2IntOpenHashMap();
        this.nameFunc = nameFuncIn;
        this.idFunc = idFuncIn;
    }
    
    public void recalculate() {
        this.byName = new SuffixArray<T>();
        this.byId = new SuffixArray<T>();
        for (final T t : this.contents) {
            this.index(t);
        }
        this.byName.generate();
        this.byId.generate();
    }
    
    public void add(final T element) {
        this.numericContents.put((Object)element, this.contents.size());
        this.contents.add(element);
        this.index(element);
    }
    
    private void index(final T element) {
        this.idFunc.apply(element).forEach(p_194039_2_ -> this.byId.add(element, p_194039_2_.toString().toLowerCase(Locale.ROOT)));
        this.nameFunc.apply(element).forEach(p_194041_2_ -> this.byName.add(element, p_194041_2_.toLowerCase(Locale.ROOT)));
    }
    
    @Override
    public List<T> search(final String searchText) {
        final List<T> list = this.byName.search(searchText);
        if (searchText.indexOf(58) < 0) {
            return list;
        }
        final List<T> list2 = this.byId.search(searchText);
        return list2.isEmpty() ? list : Lists.newArrayList((Iterator)new MergingIterator((Iterator<Object>)list.iterator(), (Iterator<Object>)list2.iterator(), (it.unimi.dsi.fastutil.objects.Object2IntMap<Object>)this.numericContents));
    }
    
    static class MergingIterator<T> extends AbstractIterator<T>
    {
        private final Iterator<T> leftItr;
        private final Iterator<T> rightItr;
        private final Object2IntMap<T> numbers;
        private T left;
        private T right;
        
        public MergingIterator(final Iterator<T> leftIn, final Iterator<T> rightIn, final Object2IntMap<T> numbersIn) {
            this.leftItr = leftIn;
            this.rightItr = rightIn;
            this.numbers = numbersIn;
            this.left = (leftIn.hasNext() ? leftIn.next() : null);
            this.right = (rightIn.hasNext() ? rightIn.next() : null);
        }
        
        protected T computeNext() {
            if (this.left == null && this.right == null) {
                return (T)this.endOfData();
            }
            int i;
            if (this.left == this.right) {
                i = 0;
            }
            else if (this.left == null) {
                i = 1;
            }
            else if (this.right == null) {
                i = -1;
            }
            else {
                i = Integer.compare(this.numbers.getInt((Object)this.left), this.numbers.getInt((Object)this.right));
            }
            final T t = (i <= 0) ? this.left : this.right;
            if (i <= 0) {
                this.left = (this.leftItr.hasNext() ? this.leftItr.next() : null);
            }
            if (i >= 0) {
                this.right = (this.rightItr.hasNext() ? this.rightItr.next() : null);
            }
            return t;
        }
    }
}
