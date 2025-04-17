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

  private static final long BUFFER_ADDRESS_OFFSET = -1;//fieldOffset(bufferAddressField());

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
}
