// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.protobuf;

/** A factory for message info that is generated into the message itself. */
@ExperimentalApi
class GeneratedMessageInfoFactory implements MessageInfoFactory {

    private static final GeneratedMessageInfoFactory instance = new GeneratedMessageInfoFactory();

    // Disallow construction - it's a singleton.
    private GeneratedMessageInfoFactory() {}

    public static GeneratedMessageInfoFactory getInstance() {
        return instance;
    }

    @Override
    public boolean isSupported(Class<?> messageType) {
        return false;
    }

    @Override
    public MessageInfo messageInfoFor(Class<?> messageType) {
        throw new UnsupportedOperationException("messageInfoFor() is not supported.");
    }
}
