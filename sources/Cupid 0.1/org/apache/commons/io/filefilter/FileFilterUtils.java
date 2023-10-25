package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOCase;

public class FileFilterUtils {
  public static File[] filter(IOFileFilter filter, File... files) {
    if (filter == null)
      throw new IllegalArgumentException("file filter is null"); 
    if (files == null)
      return new File[0]; 
    List<File> acceptedFiles = new ArrayList<File>();
    for (File file : files) {
      if (file == null)
        throw new IllegalArgumentException("file array contains null"); 
      if (filter.accept(file))
        acceptedFiles.add(file); 
    } 
    return acceptedFiles.<File>toArray(new File[acceptedFiles.size()]);
  }
  
  public static File[] filter(IOFileFilter filter, Iterable<File> files) {
    List<File> acceptedFiles = filterList(filter, files);
    return acceptedFiles.<File>toArray(new File[acceptedFiles.size()]);
  }
  
  public static List<File> filterList(IOFileFilter filter, Iterable<File> files) {
    return filter(filter, files, new ArrayList<File>());
  }
  
  public static List<File> filterList(IOFileFilter filter, File... files) {
    File[] acceptedFiles = filter(filter, files);
    return Arrays.asList(acceptedFiles);
  }
  
  public static Set<File> filterSet(IOFileFilter filter, File... files) {
    File[] acceptedFiles = filter(filter, files);
    return new HashSet<File>(Arrays.asList(acceptedFiles));
  }
  
  public static Set<File> filterSet(IOFileFilter filter, Iterable<File> files) {
    return filter(filter, files, new HashSet<File>());
  }
  
  private static <T extends java.util.Collection<File>> T filter(IOFileFilter filter, Iterable<File> files, T acceptedFiles) {
    if (filter == null)
      throw new IllegalArgumentException("file filter is null"); 
    if (files != null)
      for (File file : files) {
        if (file == null)
          throw new IllegalArgumentException("file collection contains null"); 
        if (filter.accept(file))
          acceptedFiles.add(file); 
      }  
    return acceptedFiles;
  }
  
  public static IOFileFilter prefixFileFilter(String prefix) {
    return new PrefixFileFilter(prefix);
  }
  
  public static IOFileFilter prefixFileFilter(String prefix, IOCase caseSensitivity) {
    return new PrefixFileFilter(prefix, caseSensitivity);
  }
  
  public static IOFileFilter suffixFileFilter(String suffix) {
    return new SuffixFileFilter(suffix);
  }
  
  public static IOFileFilter suffixFileFilter(String suffix, IOCase caseSensitivity) {
    return new SuffixFileFilter(suffix, caseSensitivity);
  }
  
  public static IOFileFilter nameFileFilter(String name) {
    return new NameFileFilter(name);
  }
  
  public static IOFileFilter nameFileFilter(String name, IOCase caseSensitivity) {
    return new NameFileFilter(name, caseSensitivity);
  }
  
  public static IOFileFilter directoryFileFilter() {
    return DirectoryFileFilter.DIRECTORY;
  }
  
  public static IOFileFilter fileFileFilter() {
    return FileFileFilter.FILE;
  }
  
  @Deprecated
  public static IOFileFilter andFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
    return new AndFileFilter(filter1, filter2);
  }
  
  @Deprecated
  public static IOFileFilter orFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
    return new OrFileFilter(filter1, filter2);
  }
  
  public static IOFileFilter and(IOFileFilter... filters) {
    return new AndFileFilter(toList(filters));
  }
  
  public static IOFileFilter or(IOFileFilter... filters) {
    return new OrFileFilter(toList(filters));
  }
  
  public static List<IOFileFilter> toList(IOFileFilter... filters) {
    if (filters == null)
      throw new IllegalArgumentException("The filters must not be null"); 
    List<IOFileFilter> list = new ArrayList<IOFileFilter>(filters.length);
    for (int i = 0; i < filters.length; i++) {
      if (filters[i] == null)
        throw new IllegalArgumentException("The filter[" + i + "] is null"); 
      list.add(filters[i]);
    } 
    return list;
  }
  
  public static IOFileFilter notFileFilter(IOFileFilter filter) {
    return new NotFileFilter(filter);
  }
  
  public static IOFileFilter trueFileFilter() {
    return TrueFileFilter.TRUE;
  }
  
  public static IOFileFilter falseFileFilter() {
    return FalseFileFilter.FALSE;
  }
  
  public static IOFileFilter asFileFilter(FileFilter filter) {
    return new DelegateFileFilter(filter);
  }
  
  public static IOFileFilter asFileFilter(FilenameFilter filter) {
    return new DelegateFileFilter(filter);
  }
  
  public static IOFileFilter ageFileFilter(long cutoff) {
    return new AgeFileFilter(cutoff);
  }
  
  public static IOFileFilter ageFileFilter(long cutoff, boolean acceptOlder) {
    return new AgeFileFilter(cutoff, acceptOlder);
  }
  
  public static IOFileFilter ageFileFilter(Date cutoffDate) {
    return new AgeFileFilter(cutoffDate);
  }
  
  public static IOFileFilter ageFileFilter(Date cutoffDate, boolean acceptOlder) {
    return new AgeFileFilter(cutoffDate, acceptOlder);
  }
  
  public static IOFileFilter ageFileFilter(File cutoffReference) {
    return new AgeFileFilter(cutoffReference);
  }
  
  public static IOFileFilter ageFileFilter(File cutoffReference, boolean acceptOlder) {
    return new AgeFileFilter(cutoffReference, acceptOlder);
  }
  
  public static IOFileFilter sizeFileFilter(long threshold) {
    return new SizeFileFilter(threshold);
  }
  
  public static IOFileFilter sizeFileFilter(long threshold, boolean acceptLarger) {
    return new SizeFileFilter(threshold, acceptLarger);
  }
  
  public static IOFileFilter sizeRangeFileFilter(long minSizeInclusive, long maxSizeInclusive) {
    IOFileFilter minimumFilter = new SizeFileFilter(minSizeInclusive, true);
    IOFileFilter maximumFilter = new SizeFileFilter(maxSizeInclusive + 1L, false);
    return new AndFileFilter(minimumFilter, maximumFilter);
  }
  
  public static IOFileFilter magicNumberFileFilter(String magicNumber) {
    return new MagicNumberFileFilter(magicNumber);
  }
  
  public static IOFileFilter magicNumberFileFilter(String magicNumber, long offset) {
    return new MagicNumberFileFilter(magicNumber, offset);
  }
  
  public static IOFileFilter magicNumberFileFilter(byte[] magicNumber) {
    return new MagicNumberFileFilter(magicNumber);
  }
  
  public static IOFileFilter magicNumberFileFilter(byte[] magicNumber, long offset) {
    return new MagicNumberFileFilter(magicNumber, offset);
  }
  
  private static final IOFileFilter cvsFilter = notFileFilter(and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter("CVS") }));
  
  private static final IOFileFilter svnFilter = notFileFilter(and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter(".svn") }));
  
  public static IOFileFilter makeCVSAware(IOFileFilter filter) {
    if (filter == null)
      return cvsFilter; 
    return and(new IOFileFilter[] { filter, cvsFilter });
  }
  
  public static IOFileFilter makeSVNAware(IOFileFilter filter) {
    if (filter == null)
      return svnFilter; 
    return and(new IOFileFilter[] { filter, svnFilter });
  }
  
  public static IOFileFilter makeDirectoryOnly(IOFileFilter filter) {
    if (filter == null)
      return DirectoryFileFilter.DIRECTORY; 
    return new AndFileFilter(DirectoryFileFilter.DIRECTORY, filter);
  }
  
  public static IOFileFilter makeFileOnly(IOFileFilter filter) {
    if (filter == null)
      return FileFileFilter.FILE; 
    return new AndFileFilter(FileFileFilter.FILE, filter);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\filefilter\FileFilterUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */