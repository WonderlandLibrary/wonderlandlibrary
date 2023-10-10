package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingList extends ForwardingCollection implements List {
   protected ForwardingList() {
   }

   protected abstract List delegate();

   public void add(int var1, Object var2) {
      this.delegate().add(var1, var2);
   }

   public boolean addAll(int var1, Collection var2) {
      return this.delegate().addAll(var1, var2);
   }

   public Object get(int var1) {
      return this.delegate().get(var1);
   }

   public int indexOf(Object var1) {
      return this.delegate().indexOf(var1);
   }

   public int lastIndexOf(Object var1) {
      return this.delegate().lastIndexOf(var1);
   }

   public ListIterator listIterator() {
      return this.delegate().listIterator();
   }

   public ListIterator listIterator(int var1) {
      return this.delegate().listIterator(var1);
   }

   public Object remove(int var1) {
      return this.delegate().remove(var1);
   }

   public Object set(int var1, Object var2) {
      return this.delegate().set(var1, var2);
   }

   public List subList(int var1, int var2) {
      return this.delegate().subList(var1, var2);
   }

   public boolean equals(@Nullable Object var1) {
      return var1 == this || this.delegate().equals(var1);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }

   protected boolean standardAdd(Object var1) {
      this.add(this.size(), var1);
      return true;
   }

   protected boolean standardAddAll(int var1, Iterable var2) {
      return Lists.addAllImpl(this, var1, var2);
   }

   protected int standardIndexOf(@Nullable Object var1) {
      return Lists.indexOfImpl(this, var1);
   }

   protected int standardLastIndexOf(@Nullable Object var1) {
      return Lists.lastIndexOfImpl(this, var1);
   }

   protected Iterator standardIterator() {
      return this.listIterator();
   }

   protected ListIterator standardListIterator() {
      return this.listIterator(0);
   }

   @Beta
   protected ListIterator standardListIterator(int var1) {
      return Lists.listIteratorImpl(this, var1);
   }

   @Beta
   protected List standardSubList(int var1, int var2) {
      return Lists.subListImpl(this, var1, var2);
   }

   @Beta
   protected boolean standardEquals(@Nullable Object var1) {
      return Lists.equalsImpl(this, var1);
   }

   @Beta
   protected int standardHashCode() {
      return Lists.hashCodeImpl(this);
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
