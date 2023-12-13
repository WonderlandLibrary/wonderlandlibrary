package com.ibm.icu.util;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public final class StringTokenizer implements Enumeration {
   private int m_tokenOffset_;
   private int m_tokenSize_;
   private int[] m_tokenStart_;
   private int[] m_tokenLimit_;
   private UnicodeSet m_delimiters_;
   private String m_source_;
   private int m_length_;
   private int m_nextOffset_;
   private boolean m_returnDelimiters_;
   private boolean m_coalesceDelimiters_;
   private static final UnicodeSet DEFAULT_DELIMITERS_ = new UnicodeSet(new int[]{9, 10, 12, 13, 32, 32});
   private static final int TOKEN_SIZE_ = 100;
   private static final UnicodeSet EMPTY_DELIMITER_;
   private boolean[] delims;

   public StringTokenizer(String var1, UnicodeSet var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   /** @deprecated */
   public StringTokenizer(String var1, UnicodeSet var2, boolean var3, boolean var4) {
      this.m_source_ = var1;
      this.m_length_ = var1.length();
      if (var2 == null) {
         this.m_delimiters_ = EMPTY_DELIMITER_;
      } else {
         this.m_delimiters_ = var2;
      }

      this.m_returnDelimiters_ = var3;
      this.m_coalesceDelimiters_ = var4;
      this.m_tokenOffset_ = -1;
      this.m_tokenSize_ = -1;
      if (this.m_length_ == 0) {
         this.m_nextOffset_ = -1;
      } else {
         this.m_nextOffset_ = 0;
         if (!var3) {
            this.m_nextOffset_ = this.getNextNonDelimiter(0);
         }
      }

   }

   public StringTokenizer(String var1, UnicodeSet var2) {
      this(var1, var2, false, false);
   }

   public StringTokenizer(String var1, String var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   /** @deprecated */
   public StringTokenizer(String var1, String var2, boolean var3, boolean var4) {
      this.m_delimiters_ = EMPTY_DELIMITER_;
      if (var2 != null && var2.length() > 0) {
         this.m_delimiters_ = new UnicodeSet();
         this.m_delimiters_.addAll((CharSequence)var2);
         this.checkDelimiters();
      }

      this.m_coalesceDelimiters_ = var4;
      this.m_source_ = var1;
      this.m_length_ = var1.length();
      this.m_returnDelimiters_ = var3;
      this.m_tokenOffset_ = -1;
      this.m_tokenSize_ = -1;
      if (this.m_length_ == 0) {
         this.m_nextOffset_ = -1;
      } else {
         this.m_nextOffset_ = 0;
         if (!var3) {
            this.m_nextOffset_ = this.getNextNonDelimiter(0);
         }
      }

   }

   public StringTokenizer(String var1, String var2) {
      this(var1, var2, false, false);
   }

   public StringTokenizer(String var1) {
      this(var1, DEFAULT_DELIMITERS_, false, false);
   }

   public String nextToken() {
      if (this.m_tokenOffset_ >= 0) {
         if (this.m_tokenOffset_ >= this.m_tokenSize_) {
            throw new NoSuchElementException("No more tokens in String");
         } else {
            String var7;
            if (this.m_tokenLimit_[this.m_tokenOffset_] >= 0) {
               var7 = this.m_source_.substring(this.m_tokenStart_[this.m_tokenOffset_], this.m_tokenLimit_[this.m_tokenOffset_]);
            } else {
               var7 = this.m_source_.substring(this.m_tokenStart_[this.m_tokenOffset_]);
            }

            ++this.m_tokenOffset_;
            this.m_nextOffset_ = -1;
            if (this.m_tokenOffset_ < this.m_tokenSize_) {
               this.m_nextOffset_ = this.m_tokenStart_[this.m_tokenOffset_];
            }

            return var7;
         }
      } else if (this.m_nextOffset_ < 0) {
         throw new NoSuchElementException("No more tokens in String");
      } else {
         int var5;
         if (!this.m_returnDelimiters_) {
            var5 = this.getNextDelimiter(this.m_nextOffset_);
            String var6;
            if (var5 < 0) {
               var6 = this.m_source_.substring(this.m_nextOffset_);
               this.m_nextOffset_ = var5;
            } else {
               var6 = this.m_source_.substring(this.m_nextOffset_, var5);
               this.m_nextOffset_ = this.getNextNonDelimiter(var5);
            }

            return var6;
         } else {
            boolean var1 = false;
            int var2 = UTF16.charAt(this.m_source_, this.m_nextOffset_);
            boolean var3 = this.delims == null ? this.m_delimiters_.contains(var2) : var2 < this.delims.length && this.delims[var2];
            if (var3) {
               if (this.m_coalesceDelimiters_) {
                  var5 = this.getNextNonDelimiter(this.m_nextOffset_);
               } else {
                  var5 = this.m_nextOffset_ + UTF16.getCharCount(var2);
                  if (var5 == this.m_length_) {
                     var5 = -1;
                  }
               }
            } else {
               var5 = this.getNextDelimiter(this.m_nextOffset_);
            }

            String var4;
            if (var5 < 0) {
               var4 = this.m_source_.substring(this.m_nextOffset_);
            } else {
               var4 = this.m_source_.substring(this.m_nextOffset_, var5);
            }

            this.m_nextOffset_ = var5;
            return var4;
         }
      }
   }

   public String nextToken(String var1) {
      this.m_delimiters_ = EMPTY_DELIMITER_;
      if (var1 != null && var1.length() > 0) {
         this.m_delimiters_ = new UnicodeSet();
         this.m_delimiters_.addAll((CharSequence)var1);
      }

      return this.nextToken(this.m_delimiters_);
   }

   public String nextToken(UnicodeSet var1) {
      this.m_delimiters_ = var1;
      this.checkDelimiters();
      this.m_tokenOffset_ = -1;
      this.m_tokenSize_ = -1;
      if (!this.m_returnDelimiters_) {
         this.m_nextOffset_ = this.getNextNonDelimiter(this.m_nextOffset_);
      }

      return this.nextToken();
   }

   public boolean hasMoreElements() {
      return this.hasMoreTokens();
   }

   public Object nextElement() {
      return this.nextToken();
   }

   public int countTokens() {
      int var1 = 0;
      if (this >= 0) {
         if (this.m_tokenOffset_ >= 0) {
            return this.m_tokenSize_ - this.m_tokenOffset_;
         }

         if (this.m_tokenStart_ == null) {
            this.m_tokenStart_ = new int[100];
            this.m_tokenLimit_ = new int[100];
         }

         do {
            int var4;
            if (this.m_tokenStart_.length == var1) {
               int[] var2 = this.m_tokenStart_;
               int[] var3 = this.m_tokenLimit_;
               var4 = var2.length;
               int var5 = var4 + 100;
               this.m_tokenStart_ = new int[var5];
               this.m_tokenLimit_ = new int[var5];
               System.arraycopy(var2, 0, this.m_tokenStart_, 0, var4);
               System.arraycopy(var3, 0, this.m_tokenLimit_, 0, var4);
            }

            this.m_tokenStart_[var1] = this.m_nextOffset_;
            if (!this.m_returnDelimiters_) {
               this.m_tokenLimit_[var1] = this.getNextDelimiter(this.m_nextOffset_);
               this.m_nextOffset_ = this.getNextNonDelimiter(this.m_tokenLimit_[var1]);
            } else {
               int var6 = UTF16.charAt(this.m_source_, this.m_nextOffset_);
               boolean var7 = this.delims == null ? this.m_delimiters_.contains(var6) : var6 < this.delims.length && this.delims[var6];
               if (var7) {
                  if (this.m_coalesceDelimiters_) {
                     this.m_tokenLimit_[var1] = this.getNextNonDelimiter(this.m_nextOffset_);
                  } else {
                     var4 = this.m_nextOffset_ + 1;
                     if (var4 == this.m_length_) {
                        var4 = -1;
                     }

                     this.m_tokenLimit_[var1] = var4;
                  }
               } else {
                  this.m_tokenLimit_[var1] = this.getNextDelimiter(this.m_nextOffset_);
               }

               this.m_nextOffset_ = this.m_tokenLimit_[var1];
            }

            ++var1;
         } while(this.m_nextOffset_ >= 0);

         this.m_tokenOffset_ = 0;
         this.m_tokenSize_ = var1;
         this.m_nextOffset_ = this.m_tokenStart_[0];
      }

      return var1;
   }

   private int getNextDelimiter(int var1) {
      if (var1 >= 0) {
         int var2 = var1;
         boolean var3 = false;
         int var4;
         if (this.delims == null) {
            do {
               var4 = UTF16.charAt(this.m_source_, var2);
               if (this.m_delimiters_.contains(var4)) {
                  break;
               }

               ++var2;
            } while(var2 < this.m_length_);
         } else {
            do {
               var4 = UTF16.charAt(this.m_source_, var2);
               if (var4 < this.delims.length && this.delims[var4]) {
                  break;
               }

               ++var2;
            } while(var2 < this.m_length_);
         }

         if (var2 < this.m_length_) {
            return var2;
         }
      }

      return -1 - this.m_length_;
   }

   private int getNextNonDelimiter(int var1) {
      if (var1 >= 0) {
         int var2 = var1;
         boolean var3 = false;
         int var4;
         if (this.delims == null) {
            do {
               var4 = UTF16.charAt(this.m_source_, var2);
               if (!this.m_delimiters_.contains(var4)) {
                  break;
               }

               ++var2;
            } while(var2 < this.m_length_);
         } else {
            do {
               var4 = UTF16.charAt(this.m_source_, var2);
               if (var4 >= this.delims.length || !this.delims[var4]) {
                  break;
               }

               ++var2;
            } while(var2 < this.m_length_);
         }

         if (var2 < this.m_length_) {
            return var2;
         }
      }

      return -1 - this.m_length_;
   }

   void checkDelimiters() {
      if (this.m_delimiters_ != null && this.m_delimiters_.size() != 0) {
         int var1 = this.m_delimiters_.getRangeEnd(this.m_delimiters_.getRangeCount() - 1);
         if (var1 < 127) {
            this.delims = new boolean[var1 + 1];

            int var3;
            for(int var2 = 0; -1 != (var3 = this.m_delimiters_.charAt(var2)); ++var2) {
               this.delims[var3] = true;
            }
         } else {
            this.delims = null;
         }
      } else {
         this.delims = new boolean[0];
      }

   }

   static {
      EMPTY_DELIMITER_ = UnicodeSet.EMPTY;
   }
}
