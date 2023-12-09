package hrahman.ddp.rest.utils;

import hrahman.ddp.rest.dto.NYCViolationsFileInfo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.Optional;

public final class Utilities {

    public static void downloadNycViolationsFile(NYCViolationsFileInfo nycViolationsFileInfo) {
        new Thread(() -> {
            try {
                downloadFile(nycViolationsFileInfo);
            }
            catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void downloadFile(NYCViolationsFileInfo nycViolationsFileInfo) throws InterruptedException, IOException {

        if (!Files.isDirectory(Paths.get(nycViolationsFileInfo.getDownloadDir()))) {
            System.out.println("Directory: " + nycViolationsFileInfo.getDownloadDir() + " does not exist.");
            return;
        }

        Optional<Path> filePath = join(nycViolationsFileInfo.getDownloadDir(), nycViolationsFileInfo.getFileName());

        if (!filePath.isPresent()) {
            System.out.println("Unexpected downloadDir or fileName detected.");
            return;
        }

        URL url = new URL(nycViolationsFileInfo.getUrl());

        try (InputStream inputStream = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath.get().toString())) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }

        Optional<Path> success = join(nycViolationsFileInfo.getDownloadDir(), Constants.SUCCESS);

        if (success.isPresent() && Files.exists(success.get())) {
            Files.deleteIfExists(success.get());
        }

        if (success.isPresent() && !Files.exists(success.get()) && Files.exists(filePath.get())) {
            Files.createFile(success.get());
        }
    }

    public static Optional<Path> join(String dir, String file) {
        if (dir != null && file != null) {
            return Optional.of(Paths.get(dir + FileSystems.getDefault().getSeparator() + file));
        }
        return Optional.empty();
    }
}
