package javaewah;

/*
 * Copyright 2009-2012, Daniel Lemire
 * Licensed under APL 2.0.
 */



/**
 * Mostly for internal use. Similar to RunningLengthWord, but can
 * be modified without access to the array, and has faster access.
 */
public final class BufferedRunningLengthWord {

  /**
   * Instantiates a new buffered running length word.
   *
   * @param rlw the rlw
   */
  public BufferedRunningLengthWord(final RunningLengthWord rlw) {
    this(rlw.array[rlw.position]);
  }

  /**
   * Instantiates a new buffered running length word.
   *
   * @param a the word
   */
  public BufferedRunningLengthWord(final long a) {
    this.NumberOfLiteralWords = (int) (a >>> (1 + RunningLengthWord.runninglengthbits));
    this.RunningBit = (a & 1) != 0;
    this.RunningLength = (int) ((a >>> 1) & RunningLengthWord.largestrunninglengthcount);
  }

  /**
   * Reset the values of this running length word so that it has the same values
   * as the other running length word.
   *
   * @param rlw the other running length word 
   */
  public void reset(final RunningLengthWord rlw) {
    reset(rlw.array[rlw.position]);
  }

  /**
   * Reset the values using the provided word.
   *
   * @param a the word
   */
  public void reset(final long a) {
    this.NumberOfLiteralWords = (int) (a >>> (1 + RunningLengthWord.runninglengthbits));
    this.RunningBit = (a & 1) != 0;
    this.RunningLength = (int) ((a >>> 1) & RunningLengthWord.largestrunninglengthcount);
    this.dirtywordoffset = 0;
  }

  /**
   * Gets the number of literal words.
   *
   * @return the number of literal words
   */
  public int getNumberOfLiteralWords() {
    return this.NumberOfLiteralWords;
  }

  /**
   * Sets the number of literal words.
   *
   * @param number the new number of literal words
   */
  public void setNumberOfLiteralWords(final int number) {
    this.NumberOfLiteralWords = number;
  }

  /**
   * Sets the running bit.
   *
   * @param b the new running bit
   */
  public void setRunningBit(final boolean b) {
    this.RunningBit = b;
  }

  /**
   * Gets the running bit.
   *
   * @return the running bit
   */
  public boolean getRunningBit() {
    return this.RunningBit;
  }

  /**
   * Gets the running length.
   *
   * @return the running length
   */
  public long getRunningLength() {
    return this.RunningLength;
  }

  /**
   * Sets the running length.
   *
   * @param number the new running length
   */
  public void setRunningLength(final long number) {
    this.RunningLength = number;
  }

  /**
   * Size in uncompressed words.
   *
   * @return the long
   */
  public long size() {
    return this.RunningLength + this.NumberOfLiteralWords;
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

  /**
   * Discard first words.
   *
   * @param x the x
   */
  public void discardFirstWords(long x) {
    if (this.RunningLength >= x) {
      this.RunningLength -= x;
      return;
    }
    x -= this.RunningLength;
    this.RunningLength = 0;
    this.dirtywordoffset += x;
    this.NumberOfLiteralWords -= x;
  }

  /** The Number of literal words. */
  public int NumberOfLiteralWords;
  
  /** The Running bit. */
  public boolean RunningBit;
  
  /** The Running length. */
  public long RunningLength;
  
  /** how many dirty words have we read so far? */
  public int dirtywordoffset = 0;

  
}