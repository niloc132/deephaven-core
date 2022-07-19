import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileTreeElement
import org.gradle.api.internal.file.DefaultFileLookup
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.provider.Property
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet

import javax.inject.Inject
import java.nio.file.Path

abstract class DiffTask extends DefaultTask {
    @Input
    abstract Property<Object> getExpectedContents()
    @Input
    abstract DirectoryProperty getActualContents()

    private final PatternSet ignoreInActual = new PatternSet();

    public DiffTask ignore(Action<? super PatternFilterable> action) {
        action.execute(this.ignoreInActual);
        return this;
    }

    @Input
    abstract Property<String> getGenerateTask()

    @Inject
    protected FileLookup getFileLookup() {
        throw new UnsupportedOperationException();
    }

    @TaskAction
    void diff() {
        def resolver = getFileLookup().getFileResolver(getActualContents().asFile.get())
        // for each file in the generated go output, make sure it exists and matches contents
        Set<Path> changed = []
        Set<Path> removed = []
        Set<Path> existingFiles = []

        def ignoreSpec = ignoreInActual.getAsSpec()

        getActualContents().asFileTree.visit { details ->
            if (ignoreSpec.isSatisfiedBy(details)) {
                return;
            }
            if (details.isDirectory()) {
                return;
            }
            existingFiles.add(details.file.toPath());
        }
        getExpectedContents().get().asFileTree.visit { details ->
            if (details.isDirectory()) {
                return;
            }

            // note the relative path of each generated file
            def pathString = details.relativePath.pathString

            def sourceFile = resolver.resolve(pathString)
            // if the file does not exist in our source dir, add an error
            if (!sourceFile.exists()) {
                removed.add(sourceFile.toPath())
            } else {
                // remove this from the "existing" collection so we can detect extra files later
                existingFiles.remove(sourceFile.toPath())

                // verify that the contents match
                if (sourceFile.text != details.file.text) {
                    changed.add(sourceFile.toPath())
                }
            }
        }
        if (!changed.isEmpty() || !removed.isEmpty() || !existingFiles.isEmpty()) {
            logger.error("Sources do not match expected files:")
            changed.each {
                logger.error("File has changes: $it")
            }
            removed.each {
                logger.error("File is missing: $it")
            }
            existingFiles.each {
                logger.error("File should not exist: $it")
            }
            throw new RuntimeException("Sources do not match expected, re-run ${generateTask.get()}")
        }
    }
}
