package io.tech1.framework.domain.tests.io;

import io.tech1.framework.domain.constants.StringConstants;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.file.Files.readAllLines;
import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class TestsIOUtils {

    @SneakyThrows
    public static String readFile(String folder, String fileName) {
        var path = folder + "/" + fileName;
        var resource = TestsIOUtils.class.getClassLoader().getResource(path);
        if (isNull(resource)) {
            throw new IllegalArgumentException("Please check resource exists. Path: `" + path + "`");
        }
        var file = new File(resource.getFile());
        var lines = readAllLines(Paths.get(file.getAbsolutePath()), defaultCharset());
        return String.join(StringConstants.NEWLINE, lines);
    }
}
