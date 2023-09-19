package gradle_test;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Structure {
    public static Path newDir(Path parent, String name, Boolean isFile) {
        Path path = parent.resolve(name);
        
        try {
            if (!Files.exists(path)) {
                if (isFile) path = Files.createFile(path);
                else path = Files.createDirectories(path);
            }
        } 
        catch (IOException e) {
            System.err.println(path + ": failed to create directory. Error: " + e.getMessage());
            return null;
        }
        
        return path;
    }



    public static void writeTo(Path file, String content) {
        if (file == null) {
            System.err.println("File null");
            return;
        }

        try {
            // Create a FileWriter object to write to the file (you can pass 'true' for append mode)
            String filePath = file.toString(); 
            FileWriter fileWriter = new FileWriter(filePath, true);

            // Wrap the FileWriter in a BufferedWriter for better performance
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write data to the file
            bufferedWriter.write(content);

            // Close the BufferedWriter and FileWriter to release resources
            bufferedWriter.close();
            fileWriter.close();
        } 
        catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }



    public static void copyContents(Path source, Path dest) {
        try {
            // Create input and output streams
            FileInputStream inputStream = new FileInputStream(source.toString());
            FileOutputStream outputStream = new FileOutputStream(dest.toString());

            // Create a buffer to hold the data being read from the source file
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read from the source file and write to the destination file
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}