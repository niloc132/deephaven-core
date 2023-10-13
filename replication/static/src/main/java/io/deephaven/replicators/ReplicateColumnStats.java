package io.deephaven.replicators;

import io.deephaven.replication.ReplicatePrimitiveCode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static io.deephaven.replication.ReplicatePrimitiveCode.shortToAllIntegralTypes;
import static io.deephaven.replication.ReplicationUtils.globalReplacements;

public class ReplicateColumnStats {
    public static void main(String[] args) throws IOException {
        final List<String> paths = shortToAllIntegralTypes("server/src/main/java/io/deephaven/server/table/stats/ShortChunkedNumericalStats.java");
        final String intPath = paths.stream().filter(p -> p.contains("Integer")).findFirst().orElseThrow(FileNotFoundException::new);
        fixupIntegerChunkName(intPath);

        ReplicatePrimitiveCode.floatToAllFloatingPoints("server/src/main/java/io/deephaven/server/table/stats/FloatChunkedNumericalStats.java");

        final String objectPath = ReplicatePrimitiveCode.charToObject("server/src/main/java/io/deephaven/server/table/stats/CharacterChunkedComparableStats.java");
        fixupObjectChunk(objectPath);
    }

    private static void fixupIntegerChunkName(final String intPath) throws IOException {
        final File objectFile = new File(intPath);
        final List<String> lines = FileUtils.readLines(objectFile, Charset.defaultCharset());
        FileUtils.writeLines(objectFile, globalReplacements(lines,"chunk.IntegerChunk", "chunk.IntChunk", "final IntegerChunk", "final IntChunk", "asIntegerChunk", "asIntChunk"));
    }

    private static void fixupObjectChunk(final String objectPath) throws IOException {
        final File objectFile = new File(objectPath);
        final List<String> lines = FileUtils.readLines(objectFile, Charset.defaultCharset());
        FileUtils.writeLines(objectFile, globalReplacements(lines,
                "QueryConstants.NULL_OBJECT", "null",
                "\\? extends Attributes.Values", "?, ? extends Attributes.Values",
                "ObjectChunk<[?] ", "ObjectChunk<?, ? "
                ));
    }
}
