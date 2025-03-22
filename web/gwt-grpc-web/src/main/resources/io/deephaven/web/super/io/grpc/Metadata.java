package io.grpc;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Locale;
import java.util.logging.Level;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Metadata {

    public interface AsciiMarshaller<T> {
        /**
         * Serialize a metadata value to a ASCII string that contains only the characters listed in the
         * class comment of {@link AsciiMarshaller}. Otherwise the output may be considered invalid and
         * discarded by the transport, or the call may fail.
         *
         * @param value to serialize
         * @return serialized version of value, or null if value cannot be transmitted.
         */
        String toAsciiString(T value);

        /**
         * Parse a serialized metadata value from an ASCII string.
         *
         * @param serialized value of metadata to parse
         * @return a parsed instance of type T
         */
        T parseAsciiString(String serialized);
    }


    @Immutable
    public abstract static class Key<T> {

        /** Valid characters for field names as defined in RFC7230 and RFC5234. */
        private static final BitSet VALID_T_CHARS = generateValidTChars();

//        /**
//         * Creates a key for a binary header.
//         *
//         * @param name Must contain only the valid key characters as defined in the class comment. Must
//         *     end with {@link #BINARY_HEADER_SUFFIX}.
//         */
//        public static <T> Key<T> of(String name, BinaryMarshaller<T> marshaller) {
//            return new BinaryKey<>(name, marshaller);
//        }

        public static final String BINARY_HEADER_SUFFIX = "-bin";

//        /**
//         * Creates a key for a binary header, serializing to input streams.
//         *
//         * @param name Must contain only the valid key characters as defined in the class comment. Must
//         *     end with {@link #BINARY_HEADER_SUFFIX}.
//         */
//        @ExperimentalApi("https://github.com/grpc/grpc-java/issues/6575")
//        public static <T> Key<T> of(String name, BinaryStreamMarshaller<T> marshaller) {
//            return new LazyStreamBinaryKey<>(name, marshaller);
//        }

        /**
         * Creates a key for an ASCII header.
         *
         * @param name Must contain only the valid key characters as defined in the class comment. Must
         *     <b>not</b> end with {@link #BINARY_HEADER_SUFFIX}
         */
        public static <T> Key<T> of(String name, AsciiMarshaller<T> marshaller) {
            return of(name, false, marshaller);
        }

        static <T> Key<T> of(String name, boolean pseudo, AsciiMarshaller<T> marshaller) {
            return new AsciiKey<>(name, pseudo, marshaller);
        }

        static <T> Key<T> of(String name, boolean pseudo, TrustedAsciiMarshaller<T> marshaller) {
            return new TrustedAsciiKey<>(name, pseudo, marshaller);
        }

        private final String originalName;

        private final String name;
        private final byte[] nameBytes;
        private final Object marshaller;

        private static BitSet generateValidTChars() {
            BitSet valid = new BitSet(0x7f);
            valid.set('-');
            valid.set('_');
            valid.set('.');
            for (char c = '0'; c <= '9'; c++) {
                valid.set(c);
            }
            // Only validates after normalization, so we exclude uppercase.
            for (char c = 'a'; c <= 'z'; c++) {
                valid.set(c);
            }
            return valid;
        }

        private static String validateName(String n, boolean pseudo) {
            checkNotNull(n, "name");
            checkArgument(!n.isEmpty(), "token must have at least 1 tchar");
            if (n.equals("connection")) {
                //TODO logging
//                logger.log(
//                        Level.WARNING,
//                        "Metadata key is 'Connection', which should not be used. That is used by HTTP/1 for "
//                                + "connection-specific headers which are not to be forwarded. There is probably an "
//                                + "HTTP/1 conversion bug. Simply removing the Connection header is not enough; you "
//                                + "should remove all headers it references as well. See RFC 7230 section 6.1",
//                        new RuntimeException("exception to show backtrace"));
            }
            for (int i = 0; i < n.length(); i++) {
                char tChar = n.charAt(i);
                if (pseudo && tChar == ':' && i == 0) {
                    continue;
                }

                checkArgument(
                        VALID_T_CHARS.get(tChar), "Invalid character '%s' in key name '%s'", tChar, n);
            }
            return n;
        }

        private Key(String name, boolean pseudo, Object marshaller) {
            this.originalName = checkNotNull(name, "name");
            this.name = validateName(this.originalName.toLowerCase(Locale.ROOT), pseudo);
            // TODO actual ascii charset
            this.nameBytes = this.name.getBytes(StandardCharsets.ISO_8859_1);
            this.marshaller = marshaller;
        }

        /**
         * Returns the original name used to create this key.
         */
        public final String originalName() {
            return originalName;
        }

        /**
         * Returns the normalized name for this key.
         */
        public final String name() {
            return name;
        }

        /**
         * Get the name as bytes using ASCII-encoding.
         *
         * <p>The returned byte arrays <em>must not</em> be modified.
         *
         * <p>This method is intended for transport use only.
         */
        // TODO (louiscryan): Migrate to ByteString
        @VisibleForTesting
        byte[] asciiName() {
            return nameBytes;
        }

        /**
         * Returns true if the two objects are both Keys, and their names match (case insensitive).
         */
        @SuppressWarnings("EqualsGetClass")
        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key<?> key = (Key<?>) o;
            return name.equals(key.name);
        }

        @Override
        public final int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return "Key{name='" + name + "'}";
        }

        /**
         * Serialize a metadata value to bytes.
         *
         * @param value to serialize
         * @return serialized version of value
         */
        abstract byte[] toBytes(T value);

        /**
         * Parse a serialized metadata value from bytes.
         *
         * @param serialized value of metadata to parse
         * @return a parsed instance of type T
         */
        abstract T parseBytes(byte[] serialized);

        /**
         * Returns whether this key will be serialized to bytes lazily.
         */
        boolean serializesToStreams() {
            return false;
        }

//        /**
//         * Gets this keys (implementation-specific) marshaller, or null if the
//         * marshaller is not of the given type.
//         *
//         * @param marshallerClass The type we expect the marshaller to be.
//         * @return the marshaller object for this key, or null.
//         */
//        @Nullable
//        final <M> M getMarshaller(Class<M> marshallerClass) {
//            if (marshallerClass.isInstance(marshaller)) {
//                return marshallerClass.cast(marshaller);
//            }
//            return null;
//        }
    }

    private static class AsciiKey<T> extends Key<T> {
        private final AsciiMarshaller<T> marshaller;

        /** Keys have a name and an ASCII marshaller used for serialization. */
        private AsciiKey(String name, boolean pseudo, AsciiMarshaller<T> marshaller) {
            super(name, pseudo, marshaller);
            Preconditions.checkArgument(
                    !name.endsWith(BINARY_HEADER_SUFFIX),
                    "ASCII header is named %s.  Only binary headers may end with %s",
                    name,
                    BINARY_HEADER_SUFFIX);
            this.marshaller = Preconditions.checkNotNull(marshaller, "marshaller");
        }

        @Override
        byte[] toBytes(T value) {
            String encoded = Preconditions.checkNotNull(
                    marshaller.toAsciiString(value), "null marshaller.toAsciiString()");
            //TODO actual ascii charset
            return encoded.getBytes(StandardCharsets.ISO_8859_1);
        }

        @Override
        T parseBytes(byte[] serialized) {
            // TODO actual ascii charset
            return marshaller.parseAsciiString(new String(serialized, StandardCharsets.ISO_8859_1));
        }
    }

    private static final class TrustedAsciiKey<T> extends Key<T> {
        private final TrustedAsciiMarshaller<T> marshaller;

        /** Keys have a name and an ASCII marshaller used for serialization. */
        private TrustedAsciiKey(String name, boolean pseudo, TrustedAsciiMarshaller<T> marshaller) {
            super(name, pseudo, marshaller);
            Preconditions.checkArgument(
                    !name.endsWith(BINARY_HEADER_SUFFIX),
                    "ASCII header is named %s.  Only binary headers may end with %s",
                    name,
                    BINARY_HEADER_SUFFIX);
            this.marshaller = Preconditions.checkNotNull(marshaller, "marshaller");
        }

        @Override
        byte[] toBytes(T value) {
            return Preconditions.checkNotNull(
                    marshaller.toAsciiString(value), "null marshaller.toAsciiString()");
        }

        @Override
        T parseBytes(byte[] serialized) {
            return marshaller.parseAsciiString(serialized);
        }
    }

    @Immutable
    interface TrustedAsciiMarshaller<T> {
        /**
         * Serialize a metadata value to a ASCII string that contains only the characters listed in the
         * class comment of {@link io.grpc.Metadata.AsciiMarshaller}. Otherwise the output may be
         * considered invalid and discarded by the transport, or the call may fail.
         *
         * @param value to serialize
         * @return serialized version of value, or null if value cannot be transmitted.
         */
        byte[] toAsciiString(T value);

        /**
         * Parse a serialized metadata value from an ASCII string.
         *
         * @param serialized value of metadata to parse
         * @return a parsed instance of type T
         */
        T parseAsciiString(byte[] serialized);
    }

}
