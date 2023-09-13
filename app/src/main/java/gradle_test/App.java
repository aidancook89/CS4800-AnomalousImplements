package gradle_test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public String namespace = "aidp";
    public Path template = Paths.get("src/main/java/gradle_test/template");

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
    }

    public static void main(String[] args) {
        App test = new App();
    }
}
