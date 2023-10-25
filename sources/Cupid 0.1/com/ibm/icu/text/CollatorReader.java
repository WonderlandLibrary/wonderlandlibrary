package com.ibm.icu.text;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.IntTrie;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.VersionInfo;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class CollatorReader {
  static char[] read(RuleBasedCollator rbc, RuleBasedCollator.UCAConstants ucac, RuleBasedCollator.LeadByteConstants leadByteConstants, Output<Integer> maxUCAContractionLength) throws IOException {
    InputStream i = ICUData.getRequiredStream("data/icudt51b/coll/ucadata.icu");
    BufferedInputStream b = new BufferedInputStream(i, 90000);
    CollatorReader reader = new CollatorReader(b);
    char[] ucaContractions = reader.readImp(rbc, ucac, leadByteConstants, maxUCAContractionLength);
    b.close();
    return ucaContractions;
  }
  
  public static InputStream makeByteBufferInputStream(final ByteBuffer buf) {
    return new InputStream() {
        public int read() throws IOException {
          if (!buf.hasRemaining())
            return -1; 
          return buf.get() & 0xFF;
        }
        
        public int read(byte[] bytes, int off, int len) throws IOException {
          len = Math.min(len, buf.remaining());
          buf.get(bytes, off, len);
          return len;
        }
      };
  }
  
  static void initRBC(RuleBasedCollator rbc, ByteBuffer data) throws IOException {
    int MIN_BINARY_DATA_SIZE_ = 268;
    int dataLength = data.remaining();
    CollatorReader reader = new CollatorReader(makeByteBufferInputStream(data), false);
    if (dataLength > 268) {
      reader.readImp(rbc, null, null, null);
    } else {
      reader.readHeader(rbc, null);
      reader.readOptions(rbc);
      rbc.setWithUCATables();
    } 
  }
  
  static CollationParsedRuleBuilder.InverseUCA getInverseUCA() throws IOException {
    CollationParsedRuleBuilder.InverseUCA result = null;
    InputStream i = ICUData.getRequiredStream("data/icudt51b/coll/invuca.icu");
    BufferedInputStream b = new BufferedInputStream(i, 110000);
    result = readInverseUCA(b);
    b.close();
    i.close();
    return result;
  }
  
  private CollatorReader(InputStream inputStream) throws IOException {
    this(inputStream, true);
  }
  
  private CollatorReader(InputStream inputStream, boolean readICUHeader) throws IOException {
    if (readICUHeader) {
      byte[] UnicodeVersion = ICUBinary.readHeader(inputStream, DATA_FORMAT_ID_, UCA_AUTHENTICATE_);
      VersionInfo UCDVersion = UCharacter.getUnicodeVersion();
      if (UnicodeVersion[0] != UCDVersion.getMajor() || UnicodeVersion[1] != UCDVersion.getMinor())
        throw new IOException("Unicode version in binary image is not compatible with the current Unicode version"); 
    } 
    this.m_dataInputStream_ = new DataInputStream(inputStream);
  }
  
  private void readHeader(RuleBasedCollator rbc, Output<Integer> maxUCAContractionLength) throws IOException {
    this.m_size_ = this.m_dataInputStream_.readInt();
    this.m_headerSize_ = this.m_dataInputStream_.readInt();
    int readcount = 8;
    this.m_UCAConstOffset_ = this.m_dataInputStream_.readInt();
    readcount += 4;
    this.m_dataInputStream_.readInt();
    readcount += 4;
    this.m_dataInputStream_.skipBytes(4);
    readcount += 4;
    int mapping = this.m_dataInputStream_.readInt();
    readcount += 4;
    rbc.m_expansionOffset_ = this.m_dataInputStream_.readInt();
    readcount += 4;
    rbc.m_contractionOffset_ = this.m_dataInputStream_.readInt();
    readcount += 4;
    int contractionCE = this.m_dataInputStream_.readInt();
    readcount += 4;
    int contractionSize = this.m_dataInputStream_.readInt();
    readcount += 4;
    int expansionEndCE = this.m_dataInputStream_.readInt();
    readcount += 4;
    int expansionEndCEMaxSize = this.m_dataInputStream_.readInt();
    readcount += 4;
    this.m_dataInputStream_.readInt();
    readcount += 4;
    int unsafe = this.m_dataInputStream_.readInt();
    readcount += 4;
    int contractionEnd = this.m_dataInputStream_.readInt();
    readcount += 4;
    int contractionUCACombosSize = this.m_dataInputStream_.readInt();
    readcount += 4;
    rbc.m_isJamoSpecial_ = this.m_dataInputStream_.readBoolean();
    readcount++;
    this.m_dataInputStream_.skipBytes(2);
    readcount += 2;
    int contractionUCACombosWidth = this.m_dataInputStream_.readByte();
    if (maxUCAContractionLength != null)
      maxUCAContractionLength.value = Integer.valueOf(contractionUCACombosWidth); 
    assert contractionUCACombosWidth == 0 || maxUCAContractionLength != null;
    readcount++;
    rbc.m_version_ = readVersion(this.m_dataInputStream_);
    readcount += 4;
    rbc.m_UCA_version_ = readVersion(this.m_dataInputStream_);
    readcount += 4;
    rbc.m_UCD_version_ = readVersion(this.m_dataInputStream_);
    readcount += 4;
    readVersion(this.m_dataInputStream_);
    readcount += 4;
    rbc.m_scriptToLeadBytes = this.m_dataInputStream_.readInt();
    readcount += 4;
    rbc.m_leadByteToScripts = this.m_dataInputStream_.readInt();
    readcount += 4;
    this.m_dataInputStream_.skipBytes(32);
    readcount += 32;
    this.m_dataInputStream_.skipBytes(44);
    readcount += 44;
    if (this.m_headerSize_ < readcount)
      throw new IOException("Internal Error: Header size error"); 
    this.m_dataInputStream_.skipBytes(this.m_headerSize_ - readcount);
    if (rbc.m_contractionOffset_ == 0) {
      rbc.m_contractionOffset_ = mapping;
      contractionCE = mapping;
    } 
    this.m_optionSize_ = rbc.m_expansionOffset_ - this.m_headerSize_;
    this.m_expansionSize_ = rbc.m_contractionOffset_ - rbc.m_expansionOffset_;
    this.m_contractionIndexSize_ = contractionCE - rbc.m_contractionOffset_;
    this.m_contractionCESize_ = mapping - contractionCE;
    this.m_expansionEndCESize_ = expansionEndCEMaxSize - expansionEndCE;
    this.m_expansionEndCEMaxSizeSize_ = unsafe - expansionEndCEMaxSize;
    this.m_unsafeSize_ = contractionEnd - unsafe;
    this.m_UCAcontractionSize_ = contractionUCACombosSize * contractionUCACombosWidth * 2;
    this.m_contractionSize_ = contractionSize * 2 + contractionSize * 4;
    rbc.m_contractionOffset_ >>= 1;
    rbc.m_expansionOffset_ >>= 2;
  }
  
  private void readOptions(RuleBasedCollator rbc) throws IOException {
    int readcount = 0;
    rbc.m_defaultVariableTopValue_ = this.m_dataInputStream_.readInt();
    readcount += 4;
    rbc.m_defaultIsFrenchCollation_ = (this.m_dataInputStream_.readInt() == 17);
    readcount += 4;
    rbc.m_defaultIsAlternateHandlingShifted_ = (this.m_dataInputStream_.readInt() == 20);
    readcount += 4;
    rbc.m_defaultCaseFirst_ = this.m_dataInputStream_.readInt();
    readcount += 4;
    int defaultIsCaseLevel = this.m_dataInputStream_.readInt();
    rbc.m_defaultIsCaseLevel_ = (defaultIsCaseLevel == 17);
    readcount += 4;
    int value = this.m_dataInputStream_.readInt();
    readcount += 4;
    if (value == 17) {
      value = 17;
    } else {
      value = 16;
    } 
    rbc.m_defaultDecomposition_ = value;
    rbc.m_defaultStrength_ = this.m_dataInputStream_.readInt();
    readcount += 4;
    rbc.m_defaultIsHiragana4_ = (this.m_dataInputStream_.readInt() == 17);
    readcount += 4;
    rbc.m_defaultIsNumericCollation_ = (this.m_dataInputStream_.readInt() == 17);
    readcount += 4;
    this.m_dataInputStream_.skip(60L);
    readcount += 60;
    this.m_dataInputStream_.skipBytes(this.m_optionSize_ - readcount);
    if (this.m_optionSize_ < readcount)
      throw new IOException("Internal Error: Option size error"); 
  }
  
  private char[] readImp(RuleBasedCollator rbc, RuleBasedCollator.UCAConstants UCAConst, RuleBasedCollator.LeadByteConstants leadByteConstants, Output<Integer> maxUCAContractionLength) throws IOException {
    char[] ucaContractions = null;
    readHeader(rbc, maxUCAContractionLength);
    int readcount = this.m_headerSize_;
    readOptions(rbc);
    readcount += this.m_optionSize_;
    this.m_expansionSize_ >>= 2;
    rbc.m_expansion_ = new int[this.m_expansionSize_];
    int i;
    for (i = 0; i < this.m_expansionSize_; i++)
      rbc.m_expansion_[i] = this.m_dataInputStream_.readInt(); 
    readcount += this.m_expansionSize_ << 2;
    if (this.m_contractionIndexSize_ > 0) {
      this.m_contractionIndexSize_ >>= 1;
      rbc.m_contractionIndex_ = new char[this.m_contractionIndexSize_];
      for (i = 0; i < this.m_contractionIndexSize_; i++)
        rbc.m_contractionIndex_[i] = this.m_dataInputStream_.readChar(); 
      readcount += this.m_contractionIndexSize_ << 1;
      this.m_contractionCESize_ >>= 2;
      rbc.m_contractionCE_ = new int[this.m_contractionCESize_];
      for (i = 0; i < this.m_contractionCESize_; i++)
        rbc.m_contractionCE_[i] = this.m_dataInputStream_.readInt(); 
      readcount += this.m_contractionCESize_ << 2;
    } 
    rbc.m_trie_ = new IntTrie(this.m_dataInputStream_, RuleBasedCollator.DataManipulate.getInstance());
    if (!rbc.m_trie_.isLatin1Linear())
      throw new IOException("Data corrupted, Collator Tries expected to have linear latin one data arrays"); 
    readcount += rbc.m_trie_.getSerializedDataSize();
    this.m_expansionEndCESize_ >>= 2;
    rbc.m_expansionEndCE_ = new int[this.m_expansionEndCESize_];
    for (i = 0; i < this.m_expansionEndCESize_; i++)
      rbc.m_expansionEndCE_[i] = this.m_dataInputStream_.readInt(); 
    readcount += this.m_expansionEndCESize_ << 2;
    rbc.m_expansionEndCEMaxSize_ = new byte[this.m_expansionEndCEMaxSizeSize_];
    for (i = 0; i < this.m_expansionEndCEMaxSizeSize_; i++)
      rbc.m_expansionEndCEMaxSize_[i] = this.m_dataInputStream_.readByte(); 
    readcount += this.m_expansionEndCEMaxSizeSize_;
    rbc.m_unsafe_ = new byte[this.m_unsafeSize_];
    for (i = 0; i < this.m_unsafeSize_; i++)
      rbc.m_unsafe_[i] = this.m_dataInputStream_.readByte(); 
    readcount += this.m_unsafeSize_;
    if (UCAConst != null) {
      this.m_contractionSize_ = this.m_UCAConstOffset_ - readcount;
    } else {
      this.m_contractionSize_ = this.m_size_ - readcount;
    } 
    rbc.m_contractionEnd_ = new byte[this.m_contractionSize_];
    for (i = 0; i < this.m_contractionSize_; i++)
      rbc.m_contractionEnd_[i] = this.m_dataInputStream_.readByte(); 
    readcount += this.m_contractionSize_;
    if (UCAConst != null) {
      UCAConst.FIRST_TERTIARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
      int readUCAConstcount = 4;
      UCAConst.FIRST_TERTIARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_TERTIARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_TERTIARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_PRIMARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_PRIMARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_SECONDARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_SECONDARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_SECONDARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_SECONDARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_PRIMARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_PRIMARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_VARIABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_VARIABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_VARIABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_VARIABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_NON_VARIABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_NON_VARIABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_NON_VARIABLE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_NON_VARIABLE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.RESET_TOP_VALUE_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.RESET_TOP_VALUE_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_IMPLICIT_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_IMPLICIT_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_IMPLICIT_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_IMPLICIT_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_TRAILING_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.FIRST_TRAILING_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_TRAILING_[0] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.LAST_TRAILING_[1] = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.PRIMARY_TOP_MIN_ = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.PRIMARY_IMPLICIT_MIN_ = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.PRIMARY_IMPLICIT_MAX_ = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.PRIMARY_TRAILING_MIN_ = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.PRIMARY_TRAILING_MAX_ = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.PRIMARY_SPECIAL_MIN_ = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      UCAConst.PRIMARY_SPECIAL_MAX_ = this.m_dataInputStream_.readInt();
      readUCAConstcount += 4;
      readcount += readUCAConstcount;
      int resultsize = (rbc.m_scriptToLeadBytes - readcount) / 2;
      assert resultsize == this.m_UCAcontractionSize_ / 2;
      ucaContractions = new char[resultsize];
      for (int j = 0; j < resultsize; j++)
        ucaContractions[j] = this.m_dataInputStream_.readChar(); 
      readcount += this.m_UCAcontractionSize_;
    } 
    if (leadByteConstants != null) {
      readcount = (int)(readcount + this.m_dataInputStream_.skip((rbc.m_scriptToLeadBytes - readcount)));
      leadByteConstants.read(this.m_dataInputStream_);
      readcount += leadByteConstants.getSerializedDataSize();
    } 
    if (readcount != this.m_size_)
      throw new IOException("Internal Error: Data file size error"); 
    return ucaContractions;
  }
  
  private static CollationParsedRuleBuilder.InverseUCA readInverseUCA(InputStream inputStream) throws IOException {
    byte[] UnicodeVersion = ICUBinary.readHeader(inputStream, INVERSE_UCA_DATA_FORMAT_ID_, INVERSE_UCA_AUTHENTICATE_);
    VersionInfo UCDVersion = UCharacter.getUnicodeVersion();
    if (UnicodeVersion[0] != UCDVersion.getMajor() || UnicodeVersion[1] != UCDVersion.getMinor())
      throw new IOException("Unicode version in binary image is not compatible with the current Unicode version"); 
    CollationParsedRuleBuilder.InverseUCA result = new CollationParsedRuleBuilder.InverseUCA();
    DataInputStream input = new DataInputStream(inputStream);
    input.readInt();
    int tablesize = input.readInt();
    int contsize = input.readInt();
    input.readInt();
    input.readInt();
    result.m_UCA_version_ = readVersion(input);
    input.skipBytes(8);
    int size = tablesize * 3;
    result.m_table_ = new int[size];
    result.m_continuations_ = new char[contsize];
    int i;
    for (i = 0; i < size; i++)
      result.m_table_[i] = input.readInt(); 
    for (i = 0; i < contsize; i++)
      result.m_continuations_[i] = input.readChar(); 
    input.close();
    return result;
  }
  
  protected static VersionInfo readVersion(DataInputStream input) throws IOException {
    byte[] version = new byte[4];
    version[0] = input.readByte();
    version[1] = input.readByte();
    version[2] = input.readByte();
    version[3] = input.readByte();
    VersionInfo result = VersionInfo.getInstance(version[0], version[1], version[2], version[3]);
    return result;
  }
  
  private static final ICUBinary.Authenticate UCA_AUTHENTICATE_ = new ICUBinary.Authenticate() {
      public boolean isDataVersionAcceptable(byte[] version) {
        return (version[0] == CollatorReader.DATA_FORMAT_VERSION_[0] && version[1] >= CollatorReader.DATA_FORMAT_VERSION_[1]);
      }
    };
  
  private static final ICUBinary.Authenticate INVERSE_UCA_AUTHENTICATE_ = new ICUBinary.Authenticate() {
      public boolean isDataVersionAcceptable(byte[] version) {
        return (version[0] == CollatorReader.INVERSE_UCA_DATA_FORMAT_VERSION_[0] && version[1] >= CollatorReader.INVERSE_UCA_DATA_FORMAT_VERSION_[1]);
      }
    };
  
  private DataInputStream m_dataInputStream_;
  
  private static final byte[] DATA_FORMAT_VERSION_ = new byte[] { 3, 0, 0, 0 };
  
  private static final byte[] DATA_FORMAT_ID_ = new byte[] { 85, 67, 111, 108 };
  
  private static final byte[] INVERSE_UCA_DATA_FORMAT_VERSION_ = new byte[] { 2, 1, 0, 0 };
  
  private static final byte[] INVERSE_UCA_DATA_FORMAT_ID_ = new byte[] { 73, 110, 118, 67 };
  
  private static final String WRONG_UNICODE_VERSION_ERROR_ = "Unicode version in binary image is not compatible with the current Unicode version";
  
  private int m_expansionSize_;
  
  private int m_contractionIndexSize_;
  
  private int m_contractionCESize_;
  
  private int m_expansionEndCESize_;
  
  private int m_expansionEndCEMaxSizeSize_;
  
  private int m_optionSize_;
  
  private int m_size_;
  
  private int m_headerSize_;
  
  private int m_unsafeSize_;
  
  private int m_contractionSize_;
  
  private int m_UCAcontractionSize_;
  
  private int m_UCAConstOffset_;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CollatorReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */