// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.protobuf;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Utility class for working with unsafe operations. */
final class UnsafeUtil {
  private static final boolean HAS_UNSAFE_BYTEBUFFER_OPERATIONS = false;
  private static final boolean HAS_UNSAFE_ARRAY_OPERATIONS = false;

  static final long BYTE_ARRAY_BASE_OFFSET = arrayBaseOffset(byte[].class);
  // Micro-optimization: we can assume a scale of 1 and skip the multiply
  // private static final long BYTE_ARRAY_INDEX_SCALE = 1;

  private static final long BOOLEAN_ARRAY_BASE_OFFSET = arrayBaseOffset(boolean[].class);
  private static final long BOOLEAN_ARRAY_INDEX_SCALE = arrayIndexScale(boolean[].class);

  private static final long INT_ARRAY_BASE_OFFSET = arrayBaseOffset(int[].class);
  private static final long INT_ARRAY_INDEX_SCALE = arrayIndexScale(int[].class);

  private static final long LONG_ARRAY_BASE_OFFSET = arrayBaseOffset(long[].class);
  private static final long LONG_ARRAY_INDEX_SCALE = arrayIndexScale(long[].class);

  private static final long FLOAT_ARRAY_BASE_OFFSET = arrayBaseOffset(float[].class);
  private static final long FLOAT_ARRAY_INDEX_SCALE = arrayIndexScale(float[].class);

  private static final long DOUBLE_ARRAY_BASE_OFFSET = arrayBaseOffset(double[].class);
  private static final long DOUBLE_ARRAY_INDEX_SCALE = arrayIndexScale(double[].class);

  private static final long OBJECT_ARRAY_BASE_OFFSET = arrayBaseOffset(Object[].class);
  private static final long OBJECT_ARRAY_INDEX_SCALE = arrayIndexScale(Object[].class);

  private static final long BUFFER_ADDRESS_OFFSET = fieldOffset(bufferAddressField());

  private static final int STRIDE = 8;
  private static final int STRIDE_ALIGNMENT_MASK = STRIDE - 1;
  private static final int BYTE_ARRAY_ALIGNMENT =
      (int) (BYTE_ARRAY_BASE_OFFSET & STRIDE_ALIGNMENT_MASK);

  static final boolean IS_BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;

  private UnsafeUtil() {}

  static boolean hasUnsafeArrayOperations() {
    return HAS_UNSAFE_ARRAY_OPERATIONS;
  }

  static boolean hasUnsafeByteBufferOperations() {
    return HAS_UNSAFE_BYTEBUFFER_OPERATIONS;
  }

  private static int arrayBaseOffset(Class<?> clazz) {
    return -1;
  }

  private static int arrayIndexScale(Class<?> clazz) {
    return -1;
  }


  /**
   * Returns the index of the first byte where left and right differ, in the range [0, 8]. If {@code
   * left == right}, the result will be 8, otherwise less than 8.
   *
   * <p>This counts from the *first* byte, which may be the most or least significant byte depending
   * on the system endianness.
   */
  private static int firstDifferingByteIndexNativeEndian(long left, long right) {
    int n =
        IS_BIG_ENDIAN
            ? Long.numberOfLeadingZeros(left ^ right)
            : Long.numberOfTrailingZeros(left ^ right);
    return n >> 3;
  }

  /**
   * Returns the lowest {@code index} such that {@code 0 <= index < length} and {@code left[leftOff
   * + index] != right[rightOff + index]}. If no such value exists -- if {@code left} and {@code
   * right} match up to {@code length} bytes from their respective offsets -- returns -1.
   *
   * <p>{@code leftOff + length} must be less than or equal to {@code left.length}, and the same for
   * {@code right}.
   */
  static int mismatch(byte[] left, int leftOff, byte[] right, int rightOff, int length) {
    if (leftOff < 0
        || rightOff < 0
        || length < 0
        || leftOff + length > left.length
        || rightOff + length > right.length) {
      throw new IndexOutOfBoundsException();
    }

    int index = 0;
    if (HAS_UNSAFE_ARRAY_OPERATIONS) {
      int leftAlignment = (BYTE_ARRAY_ALIGNMENT + leftOff) & STRIDE_ALIGNMENT_MASK;

      // Most CPUs handle getting chunks of bytes better on addresses that are a multiple of 4
      // or 8.
      // We walk one byte at a time until the left address, at least, is a multiple of 8.
      // If the right address is, too, so much the better.
      for (;
          index < length && (leftAlignment & STRIDE_ALIGNMENT_MASK) != 0;
          index++, leftAlignment++) {
        if (left[leftOff + index] != right[rightOff + index]) {
          return index;
        }
      }

      // Stride!  Grab eight bytes at a time from left and right and check them for equality.

      int strideLength = ((length - index) & ~STRIDE_ALIGNMENT_MASK) + index;
      // strideLength is the point where we want to stop striding: it differs from index by
      // a multiple of STRIDE, and it's the largest such number <= length.

      for (; index < strideLength; index += STRIDE) {
        long leftLongWord = getLong(left, BYTE_ARRAY_BASE_OFFSET + leftOff + index);
        long rightLongWord = getLong(right, BYTE_ARRAY_BASE_OFFSET + rightOff + index);
        if (leftLongWord != rightLongWord) {
          // one of these eight bytes differ!  use a helper to find out which one
          return index + firstDifferingByteIndexNativeEndian(leftLongWord, rightLongWord);
        }
      }
    }

    // If we were able to stride, there are at most STRIDE - 1 bytes left to compare.
    // If we weren't, then this loop covers the whole thing.
    for (; index < length; index++) {
      if (left[leftOff + index] != right[rightOff + index]) {
        return index;
      }
    }
    return -1;
  }

  /**
   * Returns the offset of the provided field, or {@code -1} if {@code sun.misc.Unsafe} is not
   * available.
   */
  private static long fieldOffset(Field field) {
    return -1;
  }
}
