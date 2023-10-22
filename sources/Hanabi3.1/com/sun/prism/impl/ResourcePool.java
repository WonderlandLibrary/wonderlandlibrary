package com.sun.prism.impl;

public interface ResourcePool {
   void freeDisposalRequestedAndCheckResources(boolean var1);

   boolean isManagerThread();

   long used();

   long managed();

   long max();

   long target();

   long origTarget();

   void setTarget(long var1);

   long size(Object var1);

   void recordAllocated(long var1);

   void recordFree(long var1);

   void resourceManaged(ManagedResource var1);

   void resourceFreed(ManagedResource var1);

   boolean prepareForAllocation(long var1);
}
