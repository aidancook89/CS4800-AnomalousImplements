package gradle_test;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;


public class App {
    public String namespace = "aidp";
    public Path template = Paths.get("src/template_files");

    public Path rootDir;
    public Path packmcmetaFile;
    public Path dataDir;

    public Path minecraftDir;
    public Path tagsDir;
    public Path tagsFunctionsDir;
    public Path loadjsonFile;
    public Path tickjsonFile;

    public Path namespaceDir;
    public Path namespaceFunctionsDir;
    public Path loadmcfunctionFile;
    public Path tickmcfunctionFile;

    public App() {
        Path userHome = Paths.get(System.getProperty("user.home"));
        Path downloadsPath = userHome.resolve("Downloads"); 

        rootDir = Structure.newDir(downloadsPath, namespace, false);
        packmcmetaFile = Structure.newDir(rootDir, "pack.mcmeta", true);
        dataDir = Structure.newDir(rootDir, "data", false);

        minecraftDir = Structure.newDir(dataDir, "minecraft", false);
        tagsDir = Structure.newDir(minecraftDir, "tags", false);
        tagsFunctionsDir = Structure.newDir(tagsDir, "functions", false);
        loadjsonFile = Structure.newDir(tagsFunctionsDir, "load.json", true);
        tickjsonFile = Structure.newDir(tagsFunctionsDir, "tick.json", true);

        namespaceDir = Structure.newDir(dataDir, namespace, false);
        namespaceFunctionsDir = Structure.newDir(namespaceDir, "functions", false);
        loadmcfunctionFile = Structure.newDir(namespaceFunctionsDir, "load.mcfunction", true);
        tickmcfunctionFile = Structure.newDir(namespaceFunctionsDir, "tick.mcfunction", true);

        Structure.copyContents(template.resolve("pack.mcmeta"), packmcmetaFile); 
        Structure.copyContents(template.resolve("load.mcfunction"), loadmcfunctionFile); 
        Structure.copyContents(template.resolve("tick.mcfunction"), tickmcfunctionFile); 
        Structure.copyContents(template.resolve("load.json"), loadjsonFile); 
        Structure.copyContents(template.resolve("tick.json"), tickjsonFile); 

        JsonObject loadDataJSON = readJsonFromFile(loadjsonFile.toString());
        loadDataJSON.addProperty("testkey", "testvalue");
        writeJsonToFile(loadjsonFile.toString(), loadDataJSON);
    }

    // Function to read JSON data from a file
    private static JsonObject readJsonFromFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to write JSON data to a file
    private static void writeJsonToFile(String filePath, JsonObject jsonData) {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        App test = new App();
    }
}
