package gradle_test;

import java.nio.file.Path;
import java.nio.file.Paths;




public class App {
    public static String namespace = "aidp";
    public static Path template = Paths.get("src/template_files");
    public static Path downloadPath;

    public static Path rootDir;
    public static Path packmcmetaFile;
    public static Path dataDir;

    public static Path minecraftDir;
    public static Path minecraftLootTablesDir;
    public static Path minecraftLootTablesEntitiesDir;

    public static Path minecraftTagsDir;
    public static Path minecraftFunctionsDir;
    public static Path loadjsonFile;
    public static Path tickjsonFile;

    public static Path namespaceDir;

    public static Path namespaceAdvancementsDir;
    public static Path namespaceTagsDir;
    public static Path namespaceLootTablesDir;

    public static Path namespaceFunctionsDir;
    public static Path loadmcfunctionFile;
    public static Path tickmcfunctionFile;

    public App() {
        Path userHome = Paths.get(System.getProperty("user.home"));
        downloadPath = userHome.resolve("Downloads"); 

        rootDir = Structure.newDir(downloadPath, namespace, false);
        packmcmetaFile = Structure.newDir(rootDir, "pack.mcmeta", true);
        dataDir = Structure.newDir(rootDir, "data", false);

        minecraftDir = Structure.newDir(dataDir, "minecraft", false);
        minecraftTagsDir = Structure.newDir(minecraftDir, "tags", false);
        minecraftLootTablesDir = Structure.newDir(minecraftDir, "loot_tables", false);
        minecraftLootTablesEntitiesDir = Structure.newDir(minecraftLootTablesDir, "entities", false);

        minecraftFunctionsDir = Structure.newDir(minecraftTagsDir, "functions", false);
        loadjsonFile = Structure.newDir(minecraftFunctionsDir, "load.json", true);
        tickjsonFile = Structure.newDir(minecraftFunctionsDir, "tick.json", true);

        namespaceDir = Structure.newDir(dataDir, namespace, false);
        //namespaceAdvancementsDir = Structure.newDir(namespaceDir, "advancements", false);
        //namespaceLootTablesDir = Structure.newDir(namespaceDir, "loot_tables", false);
        //namespaceTagsDir = Structure.newDir(namespaceDir, "tags", false);

        namespaceFunctionsDir = Structure.newDir(namespaceDir, "functions", false);
        loadmcfunctionFile = Structure.newDir(namespaceFunctionsDir, "load.mcfunction", true);
        tickmcfunctionFile = Structure.newDir(namespaceFunctionsDir, "tick.mcfunction", true);

        Structure.copyContents(template.resolve("pack.mcmeta"), packmcmetaFile); 
        Structure.copyContents(template.resolve("load.mcfunction"), loadmcfunctionFile); 
        Structure.copyContents(template.resolve("tick.mcfunction"), tickmcfunctionFile); 
        Structure.copyContents(template.resolve("load.json"), loadjsonFile); 
        Structure.copyContents(template.resolve("tick.json"), tickjsonFile); 

        Item testItem = new Item("stone_sword");
        testItem.updateName("Sword of Stone", "dark_blue", true, true, false, false, false);
        testItem.updateLore("Although stone, this sword is a powerful additional to any players toolkit.", "white", false, true, false, false, false);
        testItem.addEnchantment("sharpness", 1);
        testItem.addEnchantment("knockback", 5);
        testItem.buildTag();

        Structure.writeTo(loadmcfunctionFile, "\n" + testItem.getGiveCommand());
    }

    public static void main(String[] args) {
        App test = new App();
    }


    /* 
    // Function to read JSON data from a file
    private JsonObject readJsonFromFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to write JSON data to a file
    private void writeJsonToFile(String filePath, JsonObject jsonData) {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
