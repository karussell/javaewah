package javaewah;

/*
 * Copyright 2009-2012, Daniel Lemire
 * Licensed under APL 2.0.
 */

/**
 * Mostly for internal use.
 */
public final class RunningLengthWord {

  /**
   * Instantiates a new running length word.
   *
   * @param a an array of 64-bit words
   * @param p position in the array where the running length word is located.
   */
  RunningLengthWord(final long[] a, final int p) {
    this.array = a;
    this.position = p;
  }

  /**
   * Gets the number of literal words.
   *
   * @return the number of literal words
   */
  public int getNumberOfLiteralWords() {
    return (int) (this.array[this.position] >>> (1 + runninglengthbits));
  }

  /**
   * Sets the number of literal words.
   *
   * @param number the new number of literal words
   */
  public void setNumberOfLiteralWords(final long number) {
    this.array[this.position] |= notrunninglengthplusrunningbit;
    this.array[this.position] &= (number << (runninglengthbits + 1))
      | runninglengthplusrunningbit;
  }

  /**
   * Sets the running bit.
   *
   * @param b the new running bit
   */
  public void setRunningBit(final boolean b) {
    if (b)
      this.array[this.position] |= 1l;
    else
      this.array[this.position] &= ~1l;
  }

  /**
   * Gets the running bit.
   *
   * @return the running bit
   */
  public boolean getRunningBit() {
    return (this.array[this.position] & 1) != 0;
  }

  /**
   * Gets the running length.
   *
   * @return the running length
   */
  public long getRunningLength() {
    return (this.array[this.position] >>> 1) & largestrunninglengthcount;
  }

  /**
   * Sets the running length.
   *
   * @param number the new running length
   */
  public void setRunningLength(final long number) {
    this.array[this.position] |= shiftedlargestrunninglengthcount;
    this.array[this.position] &= (number << 1)
      | notshiftedlargestrunninglengthcount;
  }

  /**
   * Return the size in uncompressed words represented by
   * this running length word.
   *
   * @return the long
   */
  public long size() {
    return getRunningLength() + getNumberOfLiteralWords();
  }

  /* 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "running bit = " + getRunningBit() + " running length = "
      + getRunningLength() + " number of lit. words "
      + getNumberOfLiteralWords();
  }


  /** The array of words. */
  public long[] array;
  
  /** The position in array. */
  public int position;
  
  /** number of bits dedicated to marking  of the running length of clean words */  
  public static final int runninglengthbits = 32;
  
  private static final int literalbits = 64 - 1 - runninglengthbits;
  
  /** largest number of dirty words in a run. */
  public static final long largestliteralcount = (1l << literalbits) - 1;
  
  /** largest number of clean words in a run */
  public static final long largestrunninglengthcount = (1l << runninglengthbits) - 1;
  
  private static final long shiftedlargestrunninglengthcount = largestrunninglengthcount << 1;
  
  private static final long notshiftedlargestrunninglengthcount = ~shiftedlargestrunninglengthcount;
  
  private static final long runninglengthplusrunningbit = (1l << (runninglengthbits + 1)) - 1;
  
  private static final long notrunninglengthplusrunningbit = ~runninglengthplusrunningbit;
  
}