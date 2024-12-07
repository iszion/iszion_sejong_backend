package com.iszion.api.comn;
import java.io.File;

public class FileNameUtil {
  // Method to generate a unique file name if there are duplicates
  public static String getUniqueFileName(File folder, String originalFileName) {
    String name = originalFileName;
    String extension = "";
    int dotIndex = originalFileName.lastIndexOf('.');

    if (dotIndex != -1) {
      name = originalFileName.substring(0, dotIndex);
      extension = originalFileName.substring(dotIndex);
    }

    File file = new File(folder, originalFileName);
    int count = 1;

    while (file.exists()) {
      String newFileName = String.format("%s(%d)%s", name, count++, extension);
      file = new File(folder, newFileName);
    }

    return file.getName();
  }
}
