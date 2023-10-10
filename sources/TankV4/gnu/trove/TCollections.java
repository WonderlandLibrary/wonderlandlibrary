package gnu.trove;

import gnu.trove.impl.sync.TSynchronizedByteByteMap;
import gnu.trove.impl.sync.TSynchronizedByteCharMap;
import gnu.trove.impl.sync.TSynchronizedByteCollection;
import gnu.trove.impl.sync.TSynchronizedByteDoubleMap;
import gnu.trove.impl.sync.TSynchronizedByteFloatMap;
import gnu.trove.impl.sync.TSynchronizedByteIntMap;
import gnu.trove.impl.sync.TSynchronizedByteList;
import gnu.trove.impl.sync.TSynchronizedByteLongMap;
import gnu.trove.impl.sync.TSynchronizedByteObjectMap;
import gnu.trove.impl.sync.TSynchronizedByteSet;
import gnu.trove.impl.sync.TSynchronizedByteShortMap;
import gnu.trove.impl.sync.TSynchronizedCharByteMap;
import gnu.trove.impl.sync.TSynchronizedCharCharMap;
import gnu.trove.impl.sync.TSynchronizedCharCollection;
import gnu.trove.impl.sync.TSynchronizedCharDoubleMap;
import gnu.trove.impl.sync.TSynchronizedCharFloatMap;
import gnu.trove.impl.sync.TSynchronizedCharIntMap;
import gnu.trove.impl.sync.TSynchronizedCharList;
import gnu.trove.impl.sync.TSynchronizedCharLongMap;
import gnu.trove.impl.sync.TSynchronizedCharObjectMap;
import gnu.trove.impl.sync.TSynchronizedCharSet;
import gnu.trove.impl.sync.TSynchronizedCharShortMap;
import gnu.trove.impl.sync.TSynchronizedDoubleByteMap;
import gnu.trove.impl.sync.TSynchronizedDoubleCharMap;
import gnu.trove.impl.sync.TSynchronizedDoubleCollection;
import gnu.trove.impl.sync.TSynchronizedDoubleDoubleMap;
import gnu.trove.impl.sync.TSynchronizedDoubleFloatMap;
import gnu.trove.impl.sync.TSynchronizedDoubleIntMap;
import gnu.trove.impl.sync.TSynchronizedDoubleList;
import gnu.trove.impl.sync.TSynchronizedDoubleLongMap;
import gnu.trove.impl.sync.TSynchronizedDoubleObjectMap;
import gnu.trove.impl.sync.TSynchronizedDoubleSet;
import gnu.trove.impl.sync.TSynchronizedDoubleShortMap;
import gnu.trove.impl.sync.TSynchronizedFloatByteMap;
import gnu.trove.impl.sync.TSynchronizedFloatCharMap;
import gnu.trove.impl.sync.TSynchronizedFloatCollection;
import gnu.trove.impl.sync.TSynchronizedFloatDoubleMap;
import gnu.trove.impl.sync.TSynchronizedFloatFloatMap;
import gnu.trove.impl.sync.TSynchronizedFloatIntMap;
import gnu.trove.impl.sync.TSynchronizedFloatList;
import gnu.trove.impl.sync.TSynchronizedFloatLongMap;
import gnu.trove.impl.sync.TSynchronizedFloatObjectMap;
import gnu.trove.impl.sync.TSynchronizedFloatSet;
import gnu.trove.impl.sync.TSynchronizedFloatShortMap;
import gnu.trove.impl.sync.TSynchronizedIntByteMap;
import gnu.trove.impl.sync.TSynchronizedIntCharMap;
import gnu.trove.impl.sync.TSynchronizedIntCollection;
import gnu.trove.impl.sync.TSynchronizedIntDoubleMap;
import gnu.trove.impl.sync.TSynchronizedIntFloatMap;
import gnu.trove.impl.sync.TSynchronizedIntIntMap;
import gnu.trove.impl.sync.TSynchronizedIntList;
import gnu.trove.impl.sync.TSynchronizedIntLongMap;
import gnu.trove.impl.sync.TSynchronizedIntObjectMap;
import gnu.trove.impl.sync.TSynchronizedIntSet;
import gnu.trove.impl.sync.TSynchronizedIntShortMap;
import gnu.trove.impl.sync.TSynchronizedLongByteMap;
import gnu.trove.impl.sync.TSynchronizedLongCharMap;
import gnu.trove.impl.sync.TSynchronizedLongCollection;
import gnu.trove.impl.sync.TSynchronizedLongDoubleMap;
import gnu.trove.impl.sync.TSynchronizedLongFloatMap;
import gnu.trove.impl.sync.TSynchronizedLongIntMap;
import gnu.trove.impl.sync.TSynchronizedLongList;
import gnu.trove.impl.sync.TSynchronizedLongLongMap;
import gnu.trove.impl.sync.TSynchronizedLongObjectMap;
import gnu.trove.impl.sync.TSynchronizedLongSet;
import gnu.trove.impl.sync.TSynchronizedLongShortMap;
import gnu.trove.impl.sync.TSynchronizedObjectByteMap;
import gnu.trove.impl.sync.TSynchronizedObjectCharMap;
import gnu.trove.impl.sync.TSynchronizedObjectDoubleMap;
import gnu.trove.impl.sync.TSynchronizedObjectFloatMap;
import gnu.trove.impl.sync.TSynchronizedObjectIntMap;
import gnu.trove.impl.sync.TSynchronizedObjectLongMap;
import gnu.trove.impl.sync.TSynchronizedObjectShortMap;
import gnu.trove.impl.sync.TSynchronizedRandomAccessByteList;
import gnu.trove.impl.sync.TSynchronizedRandomAccessCharList;
import gnu.trove.impl.sync.TSynchronizedRandomAccessDoubleList;
import gnu.trove.impl.sync.TSynchronizedRandomAccessFloatList;
import gnu.trove.impl.sync.TSynchronizedRandomAccessIntList;
import gnu.trove.impl.sync.TSynchronizedRandomAccessLongList;
import gnu.trove.impl.sync.TSynchronizedRandomAccessShortList;
import gnu.trove.impl.sync.TSynchronizedShortByteMap;
import gnu.trove.impl.sync.TSynchronizedShortCharMap;
import gnu.trove.impl.sync.TSynchronizedShortCollection;
import gnu.trove.impl.sync.TSynchronizedShortDoubleMap;
import gnu.trove.impl.sync.TSynchronizedShortFloatMap;
import gnu.trove.impl.sync.TSynchronizedShortIntMap;
import gnu.trove.impl.sync.TSynchronizedShortList;
import gnu.trove.impl.sync.TSynchronizedShortLongMap;
import gnu.trove.impl.sync.TSynchronizedShortObjectMap;
import gnu.trove.impl.sync.TSynchronizedShortSet;
import gnu.trove.impl.sync.TSynchronizedShortShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteCollection;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteList;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteSet;
import gnu.trove.impl.unmodifiable.TUnmodifiableByteShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharCollection;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharList;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharSet;
import gnu.trove.impl.unmodifiable.TUnmodifiableCharShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleCollection;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleList;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleSet;
import gnu.trove.impl.unmodifiable.TUnmodifiableDoubleShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatCollection;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatList;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatSet;
import gnu.trove.impl.unmodifiable.TUnmodifiableFloatShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntCollection;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntList;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntSet;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongCollection;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongList;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongSet;
import gnu.trove.impl.unmodifiable.TUnmodifiableLongShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableObjectByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableObjectCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableObjectDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableObjectFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableObjectIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableObjectLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableObjectShortMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableRandomAccessByteList;
import gnu.trove.impl.unmodifiable.TUnmodifiableRandomAccessCharList;
import gnu.trove.impl.unmodifiable.TUnmodifiableRandomAccessDoubleList;
import gnu.trove.impl.unmodifiable.TUnmodifiableRandomAccessFloatList;
import gnu.trove.impl.unmodifiable.TUnmodifiableRandomAccessIntList;
import gnu.trove.impl.unmodifiable.TUnmodifiableRandomAccessLongList;
import gnu.trove.impl.unmodifiable.TUnmodifiableRandomAccessShortList;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortByteMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortCharMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortCollection;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortDoubleMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortFloatMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortIntMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortList;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortLongMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortSet;
import gnu.trove.impl.unmodifiable.TUnmodifiableShortShortMap;
import gnu.trove.list.TByteList;
import gnu.trove.list.TCharList;
import gnu.trove.list.TDoubleList;
import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.TLongList;
import gnu.trove.list.TShortList;
import gnu.trove.map.TByteByteMap;
import gnu.trove.map.TByteCharMap;
import gnu.trove.map.TByteDoubleMap;
import gnu.trove.map.TByteFloatMap;
import gnu.trove.map.TByteIntMap;
import gnu.trove.map.TByteLongMap;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.TByteShortMap;
import gnu.trove.map.TCharByteMap;
import gnu.trove.map.TCharCharMap;
import gnu.trove.map.TCharDoubleMap;
import gnu.trove.map.TCharFloatMap;
import gnu.trove.map.TCharIntMap;
import gnu.trove.map.TCharLongMap;
import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.TCharShortMap;
import gnu.trove.map.TDoubleByteMap;
import gnu.trove.map.TDoubleCharMap;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.TDoubleFloatMap;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.map.TDoubleLongMap;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.TFloatByteMap;
import gnu.trove.map.TFloatCharMap;
import gnu.trove.map.TFloatDoubleMap;
import gnu.trove.map.TFloatFloatMap;
import gnu.trove.map.TFloatIntMap;
import gnu.trove.map.TFloatLongMap;
import gnu.trove.map.TFloatObjectMap;
import gnu.trove.map.TFloatShortMap;
import gnu.trove.map.TIntByteMap;
import gnu.trove.map.TIntCharMap;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.TIntFloatMap;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntLongMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TIntShortMap;
import gnu.trove.map.TLongByteMap;
import gnu.trove.map.TLongCharMap;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.map.TLongFloatMap;
import gnu.trove.map.TLongIntMap;
import gnu.trove.map.TLongLongMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.TLongShortMap;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.map.TObjectCharMap;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortCharMap;
import gnu.trove.map.TShortDoubleMap;
import gnu.trove.map.TShortFloatMap;
import gnu.trove.map.TShortIntMap;
import gnu.trove.map.TShortLongMap;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.TShortShortMap;
import gnu.trove.set.TByteSet;
import gnu.trove.set.TCharSet;
import gnu.trove.set.TDoubleSet;
import gnu.trove.set.TFloatSet;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.TShortSet;
import java.util.RandomAccess;

public class TCollections {
   private TCollections() {
   }

   public static TDoubleCollection unmodifiableCollection(TDoubleCollection var0) {
      return new TUnmodifiableDoubleCollection(var0);
   }

   public static TFloatCollection unmodifiableCollection(TFloatCollection var0) {
      return new TUnmodifiableFloatCollection(var0);
   }

   public static TIntCollection unmodifiableCollection(TIntCollection var0) {
      return new TUnmodifiableIntCollection(var0);
   }

   public static TLongCollection unmodifiableCollection(TLongCollection var0) {
      return new TUnmodifiableLongCollection(var0);
   }

   public static TByteCollection unmodifiableCollection(TByteCollection var0) {
      return new TUnmodifiableByteCollection(var0);
   }

   public static TShortCollection unmodifiableCollection(TShortCollection var0) {
      return new TUnmodifiableShortCollection(var0);
   }

   public static TCharCollection unmodifiableCollection(TCharCollection var0) {
      return new TUnmodifiableCharCollection(var0);
   }

   public static TDoubleSet unmodifiableSet(TDoubleSet var0) {
      return new TUnmodifiableDoubleSet(var0);
   }

   public static TFloatSet unmodifiableSet(TFloatSet var0) {
      return new TUnmodifiableFloatSet(var0);
   }

   public static TIntSet unmodifiableSet(TIntSet var0) {
      return new TUnmodifiableIntSet(var0);
   }

   public static TLongSet unmodifiableSet(TLongSet var0) {
      return new TUnmodifiableLongSet(var0);
   }

   public static TByteSet unmodifiableSet(TByteSet var0) {
      return new TUnmodifiableByteSet(var0);
   }

   public static TShortSet unmodifiableSet(TShortSet var0) {
      return new TUnmodifiableShortSet(var0);
   }

   public static TCharSet unmodifiableSet(TCharSet var0) {
      return new TUnmodifiableCharSet(var0);
   }

   public static TDoubleList unmodifiableList(TDoubleList var0) {
      return (TDoubleList)(var0 instanceof RandomAccess ? new TUnmodifiableRandomAccessDoubleList(var0) : new TUnmodifiableDoubleList(var0));
   }

   public static TFloatList unmodifiableList(TFloatList var0) {
      return (TFloatList)(var0 instanceof RandomAccess ? new TUnmodifiableRandomAccessFloatList(var0) : new TUnmodifiableFloatList(var0));
   }

   public static TIntList unmodifiableList(TIntList var0) {
      return (TIntList)(var0 instanceof RandomAccess ? new TUnmodifiableRandomAccessIntList(var0) : new TUnmodifiableIntList(var0));
   }

   public static TLongList unmodifiableList(TLongList var0) {
      return (TLongList)(var0 instanceof RandomAccess ? new TUnmodifiableRandomAccessLongList(var0) : new TUnmodifiableLongList(var0));
   }

   public static TByteList unmodifiableList(TByteList var0) {
      return (TByteList)(var0 instanceof RandomAccess ? new TUnmodifiableRandomAccessByteList(var0) : new TUnmodifiableByteList(var0));
   }

   public static TShortList unmodifiableList(TShortList var0) {
      return (TShortList)(var0 instanceof RandomAccess ? new TUnmodifiableRandomAccessShortList(var0) : new TUnmodifiableShortList(var0));
   }

   public static TCharList unmodifiableList(TCharList var0) {
      return (TCharList)(var0 instanceof RandomAccess ? new TUnmodifiableRandomAccessCharList(var0) : new TUnmodifiableCharList(var0));
   }

   public static TDoubleDoubleMap unmodifiableMap(TDoubleDoubleMap var0) {
      return new TUnmodifiableDoubleDoubleMap(var0);
   }

   public static TDoubleFloatMap unmodifiableMap(TDoubleFloatMap var0) {
      return new TUnmodifiableDoubleFloatMap(var0);
   }

   public static TDoubleIntMap unmodifiableMap(TDoubleIntMap var0) {
      return new TUnmodifiableDoubleIntMap(var0);
   }

   public static TDoubleLongMap unmodifiableMap(TDoubleLongMap var0) {
      return new TUnmodifiableDoubleLongMap(var0);
   }

   public static TDoubleByteMap unmodifiableMap(TDoubleByteMap var0) {
      return new TUnmodifiableDoubleByteMap(var0);
   }

   public static TDoubleShortMap unmodifiableMap(TDoubleShortMap var0) {
      return new TUnmodifiableDoubleShortMap(var0);
   }

   public static TDoubleCharMap unmodifiableMap(TDoubleCharMap var0) {
      return new TUnmodifiableDoubleCharMap(var0);
   }

   public static TFloatDoubleMap unmodifiableMap(TFloatDoubleMap var0) {
      return new TUnmodifiableFloatDoubleMap(var0);
   }

   public static TFloatFloatMap unmodifiableMap(TFloatFloatMap var0) {
      return new TUnmodifiableFloatFloatMap(var0);
   }

   public static TFloatIntMap unmodifiableMap(TFloatIntMap var0) {
      return new TUnmodifiableFloatIntMap(var0);
   }

   public static TFloatLongMap unmodifiableMap(TFloatLongMap var0) {
      return new TUnmodifiableFloatLongMap(var0);
   }

   public static TFloatByteMap unmodifiableMap(TFloatByteMap var0) {
      return new TUnmodifiableFloatByteMap(var0);
   }

   public static TFloatShortMap unmodifiableMap(TFloatShortMap var0) {
      return new TUnmodifiableFloatShortMap(var0);
   }

   public static TFloatCharMap unmodifiableMap(TFloatCharMap var0) {
      return new TUnmodifiableFloatCharMap(var0);
   }

   public static TIntDoubleMap unmodifiableMap(TIntDoubleMap var0) {
      return new TUnmodifiableIntDoubleMap(var0);
   }

   public static TIntFloatMap unmodifiableMap(TIntFloatMap var0) {
      return new TUnmodifiableIntFloatMap(var0);
   }

   public static TIntIntMap unmodifiableMap(TIntIntMap var0) {
      return new TUnmodifiableIntIntMap(var0);
   }

   public static TIntLongMap unmodifiableMap(TIntLongMap var0) {
      return new TUnmodifiableIntLongMap(var0);
   }

   public static TIntByteMap unmodifiableMap(TIntByteMap var0) {
      return new TUnmodifiableIntByteMap(var0);
   }

   public static TIntShortMap unmodifiableMap(TIntShortMap var0) {
      return new TUnmodifiableIntShortMap(var0);
   }

   public static TIntCharMap unmodifiableMap(TIntCharMap var0) {
      return new TUnmodifiableIntCharMap(var0);
   }

   public static TLongDoubleMap unmodifiableMap(TLongDoubleMap var0) {
      return new TUnmodifiableLongDoubleMap(var0);
   }

   public static TLongFloatMap unmodifiableMap(TLongFloatMap var0) {
      return new TUnmodifiableLongFloatMap(var0);
   }

   public static TLongIntMap unmodifiableMap(TLongIntMap var0) {
      return new TUnmodifiableLongIntMap(var0);
   }

   public static TLongLongMap unmodifiableMap(TLongLongMap var0) {
      return new TUnmodifiableLongLongMap(var0);
   }

   public static TLongByteMap unmodifiableMap(TLongByteMap var0) {
      return new TUnmodifiableLongByteMap(var0);
   }

   public static TLongShortMap unmodifiableMap(TLongShortMap var0) {
      return new TUnmodifiableLongShortMap(var0);
   }

   public static TLongCharMap unmodifiableMap(TLongCharMap var0) {
      return new TUnmodifiableLongCharMap(var0);
   }

   public static TByteDoubleMap unmodifiableMap(TByteDoubleMap var0) {
      return new TUnmodifiableByteDoubleMap(var0);
   }

   public static TByteFloatMap unmodifiableMap(TByteFloatMap var0) {
      return new TUnmodifiableByteFloatMap(var0);
   }

   public static TByteIntMap unmodifiableMap(TByteIntMap var0) {
      return new TUnmodifiableByteIntMap(var0);
   }

   public static TByteLongMap unmodifiableMap(TByteLongMap var0) {
      return new TUnmodifiableByteLongMap(var0);
   }

   public static TByteByteMap unmodifiableMap(TByteByteMap var0) {
      return new TUnmodifiableByteByteMap(var0);
   }

   public static TByteShortMap unmodifiableMap(TByteShortMap var0) {
      return new TUnmodifiableByteShortMap(var0);
   }

   public static TByteCharMap unmodifiableMap(TByteCharMap var0) {
      return new TUnmodifiableByteCharMap(var0);
   }

   public static TShortDoubleMap unmodifiableMap(TShortDoubleMap var0) {
      return new TUnmodifiableShortDoubleMap(var0);
   }

   public static TShortFloatMap unmodifiableMap(TShortFloatMap var0) {
      return new TUnmodifiableShortFloatMap(var0);
   }

   public static TShortIntMap unmodifiableMap(TShortIntMap var0) {
      return new TUnmodifiableShortIntMap(var0);
   }

   public static TShortLongMap unmodifiableMap(TShortLongMap var0) {
      return new TUnmodifiableShortLongMap(var0);
   }

   public static TShortByteMap unmodifiableMap(TShortByteMap var0) {
      return new TUnmodifiableShortByteMap(var0);
   }

   public static TShortShortMap unmodifiableMap(TShortShortMap var0) {
      return new TUnmodifiableShortShortMap(var0);
   }

   public static TShortCharMap unmodifiableMap(TShortCharMap var0) {
      return new TUnmodifiableShortCharMap(var0);
   }

   public static TCharDoubleMap unmodifiableMap(TCharDoubleMap var0) {
      return new TUnmodifiableCharDoubleMap(var0);
   }

   public static TCharFloatMap unmodifiableMap(TCharFloatMap var0) {
      return new TUnmodifiableCharFloatMap(var0);
   }

   public static TCharIntMap unmodifiableMap(TCharIntMap var0) {
      return new TUnmodifiableCharIntMap(var0);
   }

   public static TCharLongMap unmodifiableMap(TCharLongMap var0) {
      return new TUnmodifiableCharLongMap(var0);
   }

   public static TCharByteMap unmodifiableMap(TCharByteMap var0) {
      return new TUnmodifiableCharByteMap(var0);
   }

   public static TCharShortMap unmodifiableMap(TCharShortMap var0) {
      return new TUnmodifiableCharShortMap(var0);
   }

   public static TCharCharMap unmodifiableMap(TCharCharMap var0) {
      return new TUnmodifiableCharCharMap(var0);
   }

   public static TDoubleObjectMap unmodifiableMap(TDoubleObjectMap var0) {
      return new TUnmodifiableDoubleObjectMap(var0);
   }

   public static TFloatObjectMap unmodifiableMap(TFloatObjectMap var0) {
      return new TUnmodifiableFloatObjectMap(var0);
   }

   public static TIntObjectMap unmodifiableMap(TIntObjectMap var0) {
      return new TUnmodifiableIntObjectMap(var0);
   }

   public static TLongObjectMap unmodifiableMap(TLongObjectMap var0) {
      return new TUnmodifiableLongObjectMap(var0);
   }

   public static TByteObjectMap unmodifiableMap(TByteObjectMap var0) {
      return new TUnmodifiableByteObjectMap(var0);
   }

   public static TShortObjectMap unmodifiableMap(TShortObjectMap var0) {
      return new TUnmodifiableShortObjectMap(var0);
   }

   public static TCharObjectMap unmodifiableMap(TCharObjectMap var0) {
      return new TUnmodifiableCharObjectMap(var0);
   }

   public static TObjectDoubleMap unmodifiableMap(TObjectDoubleMap var0) {
      return new TUnmodifiableObjectDoubleMap(var0);
   }

   public static TObjectFloatMap unmodifiableMap(TObjectFloatMap var0) {
      return new TUnmodifiableObjectFloatMap(var0);
   }

   public static TObjectIntMap unmodifiableMap(TObjectIntMap var0) {
      return new TUnmodifiableObjectIntMap(var0);
   }

   public static TObjectLongMap unmodifiableMap(TObjectLongMap var0) {
      return new TUnmodifiableObjectLongMap(var0);
   }

   public static TObjectByteMap unmodifiableMap(TObjectByteMap var0) {
      return new TUnmodifiableObjectByteMap(var0);
   }

   public static TObjectShortMap unmodifiableMap(TObjectShortMap var0) {
      return new TUnmodifiableObjectShortMap(var0);
   }

   public static TObjectCharMap unmodifiableMap(TObjectCharMap var0) {
      return new TUnmodifiableObjectCharMap(var0);
   }

   public static TDoubleCollection synchronizedCollection(TDoubleCollection var0) {
      return new TSynchronizedDoubleCollection(var0);
   }

   static TDoubleCollection synchronizedCollection(TDoubleCollection var0, Object var1) {
      return new TSynchronizedDoubleCollection(var0, var1);
   }

   public static TFloatCollection synchronizedCollection(TFloatCollection var0) {
      return new TSynchronizedFloatCollection(var0);
   }

   static TFloatCollection synchronizedCollection(TFloatCollection var0, Object var1) {
      return new TSynchronizedFloatCollection(var0, var1);
   }

   public static TIntCollection synchronizedCollection(TIntCollection var0) {
      return new TSynchronizedIntCollection(var0);
   }

   static TIntCollection synchronizedCollection(TIntCollection var0, Object var1) {
      return new TSynchronizedIntCollection(var0, var1);
   }

   public static TLongCollection synchronizedCollection(TLongCollection var0) {
      return new TSynchronizedLongCollection(var0);
   }

   static TLongCollection synchronizedCollection(TLongCollection var0, Object var1) {
      return new TSynchronizedLongCollection(var0, var1);
   }

   public static TByteCollection synchronizedCollection(TByteCollection var0) {
      return new TSynchronizedByteCollection(var0);
   }

   static TByteCollection synchronizedCollection(TByteCollection var0, Object var1) {
      return new TSynchronizedByteCollection(var0, var1);
   }

   public static TShortCollection synchronizedCollection(TShortCollection var0) {
      return new TSynchronizedShortCollection(var0);
   }

   static TShortCollection synchronizedCollection(TShortCollection var0, Object var1) {
      return new TSynchronizedShortCollection(var0, var1);
   }

   public static TCharCollection synchronizedCollection(TCharCollection var0) {
      return new TSynchronizedCharCollection(var0);
   }

   static TCharCollection synchronizedCollection(TCharCollection var0, Object var1) {
      return new TSynchronizedCharCollection(var0, var1);
   }

   public static TDoubleSet synchronizedSet(TDoubleSet var0) {
      return new TSynchronizedDoubleSet(var0);
   }

   static TDoubleSet synchronizedSet(TDoubleSet var0, Object var1) {
      return new TSynchronizedDoubleSet(var0, var1);
   }

   public static TFloatSet synchronizedSet(TFloatSet var0) {
      return new TSynchronizedFloatSet(var0);
   }

   static TFloatSet synchronizedSet(TFloatSet var0, Object var1) {
      return new TSynchronizedFloatSet(var0, var1);
   }

   public static TIntSet synchronizedSet(TIntSet var0) {
      return new TSynchronizedIntSet(var0);
   }

   static TIntSet synchronizedSet(TIntSet var0, Object var1) {
      return new TSynchronizedIntSet(var0, var1);
   }

   public static TLongSet synchronizedSet(TLongSet var0) {
      return new TSynchronizedLongSet(var0);
   }

   static TLongSet synchronizedSet(TLongSet var0, Object var1) {
      return new TSynchronizedLongSet(var0, var1);
   }

   public static TByteSet synchronizedSet(TByteSet var0) {
      return new TSynchronizedByteSet(var0);
   }

   static TByteSet synchronizedSet(TByteSet var0, Object var1) {
      return new TSynchronizedByteSet(var0, var1);
   }

   public static TShortSet synchronizedSet(TShortSet var0) {
      return new TSynchronizedShortSet(var0);
   }

   static TShortSet synchronizedSet(TShortSet var0, Object var1) {
      return new TSynchronizedShortSet(var0, var1);
   }

   public static TCharSet synchronizedSet(TCharSet var0) {
      return new TSynchronizedCharSet(var0);
   }

   static TCharSet synchronizedSet(TCharSet var0, Object var1) {
      return new TSynchronizedCharSet(var0, var1);
   }

   public static TDoubleList synchronizedList(TDoubleList var0) {
      return (TDoubleList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessDoubleList(var0) : new TSynchronizedDoubleList(var0));
   }

   static TDoubleList synchronizedList(TDoubleList var0, Object var1) {
      return (TDoubleList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessDoubleList(var0, var1) : new TSynchronizedDoubleList(var0, var1));
   }

   public static TFloatList synchronizedList(TFloatList var0) {
      return (TFloatList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessFloatList(var0) : new TSynchronizedFloatList(var0));
   }

   static TFloatList synchronizedList(TFloatList var0, Object var1) {
      return (TFloatList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessFloatList(var0, var1) : new TSynchronizedFloatList(var0, var1));
   }

   public static TIntList synchronizedList(TIntList var0) {
      return (TIntList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessIntList(var0) : new TSynchronizedIntList(var0));
   }

   static TIntList synchronizedList(TIntList var0, Object var1) {
      return (TIntList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessIntList(var0, var1) : new TSynchronizedIntList(var0, var1));
   }

   public static TLongList synchronizedList(TLongList var0) {
      return (TLongList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessLongList(var0) : new TSynchronizedLongList(var0));
   }

   static TLongList synchronizedList(TLongList var0, Object var1) {
      return (TLongList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessLongList(var0, var1) : new TSynchronizedLongList(var0, var1));
   }

   public static TByteList synchronizedList(TByteList var0) {
      return (TByteList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessByteList(var0) : new TSynchronizedByteList(var0));
   }

   static TByteList synchronizedList(TByteList var0, Object var1) {
      return (TByteList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessByteList(var0, var1) : new TSynchronizedByteList(var0, var1));
   }

   public static TShortList synchronizedList(TShortList var0) {
      return (TShortList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessShortList(var0) : new TSynchronizedShortList(var0));
   }

   static TShortList synchronizedList(TShortList var0, Object var1) {
      return (TShortList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessShortList(var0, var1) : new TSynchronizedShortList(var0, var1));
   }

   public static TCharList synchronizedList(TCharList var0) {
      return (TCharList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessCharList(var0) : new TSynchronizedCharList(var0));
   }

   static TCharList synchronizedList(TCharList var0, Object var1) {
      return (TCharList)(var0 instanceof RandomAccess ? new TSynchronizedRandomAccessCharList(var0, var1) : new TSynchronizedCharList(var0, var1));
   }

   public static TDoubleDoubleMap synchronizedMap(TDoubleDoubleMap var0) {
      return new TSynchronizedDoubleDoubleMap(var0);
   }

   public static TDoubleFloatMap synchronizedMap(TDoubleFloatMap var0) {
      return new TSynchronizedDoubleFloatMap(var0);
   }

   public static TDoubleIntMap synchronizedMap(TDoubleIntMap var0) {
      return new TSynchronizedDoubleIntMap(var0);
   }

   public static TDoubleLongMap synchronizedMap(TDoubleLongMap var0) {
      return new TSynchronizedDoubleLongMap(var0);
   }

   public static TDoubleByteMap synchronizedMap(TDoubleByteMap var0) {
      return new TSynchronizedDoubleByteMap(var0);
   }

   public static TDoubleShortMap synchronizedMap(TDoubleShortMap var0) {
      return new TSynchronizedDoubleShortMap(var0);
   }

   public static TDoubleCharMap synchronizedMap(TDoubleCharMap var0) {
      return new TSynchronizedDoubleCharMap(var0);
   }

   public static TFloatDoubleMap synchronizedMap(TFloatDoubleMap var0) {
      return new TSynchronizedFloatDoubleMap(var0);
   }

   public static TFloatFloatMap synchronizedMap(TFloatFloatMap var0) {
      return new TSynchronizedFloatFloatMap(var0);
   }

   public static TFloatIntMap synchronizedMap(TFloatIntMap var0) {
      return new TSynchronizedFloatIntMap(var0);
   }

   public static TFloatLongMap synchronizedMap(TFloatLongMap var0) {
      return new TSynchronizedFloatLongMap(var0);
   }

   public static TFloatByteMap synchronizedMap(TFloatByteMap var0) {
      return new TSynchronizedFloatByteMap(var0);
   }

   public static TFloatShortMap synchronizedMap(TFloatShortMap var0) {
      return new TSynchronizedFloatShortMap(var0);
   }

   public static TFloatCharMap synchronizedMap(TFloatCharMap var0) {
      return new TSynchronizedFloatCharMap(var0);
   }

   public static TIntDoubleMap synchronizedMap(TIntDoubleMap var0) {
      return new TSynchronizedIntDoubleMap(var0);
   }

   public static TIntFloatMap synchronizedMap(TIntFloatMap var0) {
      return new TSynchronizedIntFloatMap(var0);
   }

   public static TIntIntMap synchronizedMap(TIntIntMap var0) {
      return new TSynchronizedIntIntMap(var0);
   }

   public static TIntLongMap synchronizedMap(TIntLongMap var0) {
      return new TSynchronizedIntLongMap(var0);
   }

   public static TIntByteMap synchronizedMap(TIntByteMap var0) {
      return new TSynchronizedIntByteMap(var0);
   }

   public static TIntShortMap synchronizedMap(TIntShortMap var0) {
      return new TSynchronizedIntShortMap(var0);
   }

   public static TIntCharMap synchronizedMap(TIntCharMap var0) {
      return new TSynchronizedIntCharMap(var0);
   }

   public static TLongDoubleMap synchronizedMap(TLongDoubleMap var0) {
      return new TSynchronizedLongDoubleMap(var0);
   }

   public static TLongFloatMap synchronizedMap(TLongFloatMap var0) {
      return new TSynchronizedLongFloatMap(var0);
   }

   public static TLongIntMap synchronizedMap(TLongIntMap var0) {
      return new TSynchronizedLongIntMap(var0);
   }

   public static TLongLongMap synchronizedMap(TLongLongMap var0) {
      return new TSynchronizedLongLongMap(var0);
   }

   public static TLongByteMap synchronizedMap(TLongByteMap var0) {
      return new TSynchronizedLongByteMap(var0);
   }

   public static TLongShortMap synchronizedMap(TLongShortMap var0) {
      return new TSynchronizedLongShortMap(var0);
   }

   public static TLongCharMap synchronizedMap(TLongCharMap var0) {
      return new TSynchronizedLongCharMap(var0);
   }

   public static TByteDoubleMap synchronizedMap(TByteDoubleMap var0) {
      return new TSynchronizedByteDoubleMap(var0);
   }

   public static TByteFloatMap synchronizedMap(TByteFloatMap var0) {
      return new TSynchronizedByteFloatMap(var0);
   }

   public static TByteIntMap synchronizedMap(TByteIntMap var0) {
      return new TSynchronizedByteIntMap(var0);
   }

   public static TByteLongMap synchronizedMap(TByteLongMap var0) {
      return new TSynchronizedByteLongMap(var0);
   }

   public static TByteByteMap synchronizedMap(TByteByteMap var0) {
      return new TSynchronizedByteByteMap(var0);
   }

   public static TByteShortMap synchronizedMap(TByteShortMap var0) {
      return new TSynchronizedByteShortMap(var0);
   }

   public static TByteCharMap synchronizedMap(TByteCharMap var0) {
      return new TSynchronizedByteCharMap(var0);
   }

   public static TShortDoubleMap synchronizedMap(TShortDoubleMap var0) {
      return new TSynchronizedShortDoubleMap(var0);
   }

   public static TShortFloatMap synchronizedMap(TShortFloatMap var0) {
      return new TSynchronizedShortFloatMap(var0);
   }

   public static TShortIntMap synchronizedMap(TShortIntMap var0) {
      return new TSynchronizedShortIntMap(var0);
   }

   public static TShortLongMap synchronizedMap(TShortLongMap var0) {
      return new TSynchronizedShortLongMap(var0);
   }

   public static TShortByteMap synchronizedMap(TShortByteMap var0) {
      return new TSynchronizedShortByteMap(var0);
   }

   public static TShortShortMap synchronizedMap(TShortShortMap var0) {
      return new TSynchronizedShortShortMap(var0);
   }

   public static TShortCharMap synchronizedMap(TShortCharMap var0) {
      return new TSynchronizedShortCharMap(var0);
   }

   public static TCharDoubleMap synchronizedMap(TCharDoubleMap var0) {
      return new TSynchronizedCharDoubleMap(var0);
   }

   public static TCharFloatMap synchronizedMap(TCharFloatMap var0) {
      return new TSynchronizedCharFloatMap(var0);
   }

   public static TCharIntMap synchronizedMap(TCharIntMap var0) {
      return new TSynchronizedCharIntMap(var0);
   }

   public static TCharLongMap synchronizedMap(TCharLongMap var0) {
      return new TSynchronizedCharLongMap(var0);
   }

   public static TCharByteMap synchronizedMap(TCharByteMap var0) {
      return new TSynchronizedCharByteMap(var0);
   }

   public static TCharShortMap synchronizedMap(TCharShortMap var0) {
      return new TSynchronizedCharShortMap(var0);
   }

   public static TCharCharMap synchronizedMap(TCharCharMap var0) {
      return new TSynchronizedCharCharMap(var0);
   }

   public static TDoubleObjectMap synchronizedMap(TDoubleObjectMap var0) {
      return new TSynchronizedDoubleObjectMap(var0);
   }

   public static TFloatObjectMap synchronizedMap(TFloatObjectMap var0) {
      return new TSynchronizedFloatObjectMap(var0);
   }

   public static TIntObjectMap synchronizedMap(TIntObjectMap var0) {
      return new TSynchronizedIntObjectMap(var0);
   }

   public static TLongObjectMap synchronizedMap(TLongObjectMap var0) {
      return new TSynchronizedLongObjectMap(var0);
   }

   public static TByteObjectMap synchronizedMap(TByteObjectMap var0) {
      return new TSynchronizedByteObjectMap(var0);
   }

   public static TShortObjectMap synchronizedMap(TShortObjectMap var0) {
      return new TSynchronizedShortObjectMap(var0);
   }

   public static TCharObjectMap synchronizedMap(TCharObjectMap var0) {
      return new TSynchronizedCharObjectMap(var0);
   }

   public static TObjectDoubleMap synchronizedMap(TObjectDoubleMap var0) {
      return new TSynchronizedObjectDoubleMap(var0);
   }

   public static TObjectFloatMap synchronizedMap(TObjectFloatMap var0) {
      return new TSynchronizedObjectFloatMap(var0);
   }

   public static TObjectIntMap synchronizedMap(TObjectIntMap var0) {
      return new TSynchronizedObjectIntMap(var0);
   }

   public static TObjectLongMap synchronizedMap(TObjectLongMap var0) {
      return new TSynchronizedObjectLongMap(var0);
   }

   public static TObjectByteMap synchronizedMap(TObjectByteMap var0) {
      return new TSynchronizedObjectByteMap(var0);
   }

   public static TObjectShortMap synchronizedMap(TObjectShortMap var0) {
      return new TSynchronizedObjectShortMap(var0);
   }

   public static TObjectCharMap synchronizedMap(TObjectCharMap var0) {
      return new TSynchronizedObjectCharMap(var0);
   }
}
