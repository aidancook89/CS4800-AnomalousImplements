package aidp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App {
    public static String namespace = "aidp";
    public static Path template = Paths.get("src/template_files");
    public static Path d_download;

    public static Path d_root;
    public static Path f_packmcmeta;
    public static Path d_data;

    public static Path d_mc; // Minecraft
    public static Path d_mc_loot_tables;
    public static Path d_mc_loot_tables_entities;

    public static Path d_mc_tags;
    public static Path d_mc_functions;
    public static Path f_loadjson;
    public static Path f_tickjson;

    public static Path d_ns; // Namespace

    public static Path d_ns_advancements;
    public static Path d_ns_advancements_deal_damage;

    public static Path d_ns_tags;
    public static Path d_ns_loot_tables;
    public static Path d_ns_loot_tables_entities;
    public static Path f_loot_table;

    public static Path d_ns_functions;
    public static Path d_swords;
    public static Path f_swords_sword1;

    public static Path f_loadmcfunction;
    public static Path f_tickmcfunction;
    public static Path f_item_tickmcfunction;
    public static Path f_sword0mcfunction;
    public static Path f_deal_damagemcfunction;

    public App() {
        Path userHome = Paths.get(System.getProperty("user.home"));
        d_download = userHome.resolve("Downloads"); 

        d_root = Structure.newDir(d_download, namespace, false);
        f_packmcmeta = Structure.newDir(d_root, "pack.mcmeta", true);
        Structure.copyContents(template.resolve("pack.mcmeta"), f_packmcmeta); 

        d_data = Structure.newDir(d_root, "data", false);

        d_mc = Structure.newDir(d_data, "minecraft", false);
        d_mc_tags = Structure.newDir(d_mc, "tags", false);
        //d_mc_loot_tables = Structure.newDir(d_mc, "loot_tables", false);
        //d_mc_loot_tables_entities = Structure.newDir(d_mc_loot_tables, "entities", false);

        d_mc_functions = Structure.newDir(d_mc_tags, "functions", false);
        f_loadjson = Structure.newDir(d_mc_functions, "load.json", true);
        Structure.copyContents(template.resolve("load.json"), f_loadjson); 
        f_tickjson = Structure.newDir(d_mc_functions, "tick.json", true);
        Structure.copyContents(template.resolve("tick.json"), f_tickjson); 

        d_ns = Structure.newDir(d_data, namespace, false);
        //namespaceLootTablesDir = Structure.newDir(d_ns, "loot_tables", false);
        //namespaceTagsDir = Structure.newDir(d_ns, "tags", false);
        d_ns_loot_tables = Structure.newDir(d_ns, "loot_tables", false);
        d_ns_loot_tables_entities = Structure.newDir(d_ns_loot_tables, "entities", false);
        //f_loot_table = Structure.newDir(d_ns_loot_tables_entities, "creeper.json", true);
        //Structure.copyContents(template.resolve("loot_table.json"), f_loot_table);

        d_ns_functions = Structure.newDir(d_ns, "functions", false);

        f_deal_damagemcfunction = Structure.newDir(d_ns_functions, "deal_damage.mcfunction", true);
        Structure.copyContents(template.resolve("deal_damage.mcfunction"), f_deal_damagemcfunction); 

        f_loadmcfunction = Structure.newDir(d_ns_functions, "load.mcfunction", true);
        Structure.copyContents(template.resolve("load.mcfunction"), f_loadmcfunction); 

        f_tickmcfunction = Structure.newDir(d_ns_functions, "tick.mcfunction", true);
        Structure.copyContents(template.resolve("tick.mcfunction"), f_tickmcfunction); 

        f_item_tickmcfunction = Structure.newDir(d_ns_functions, "item_tick.mcfunction", true);
        d_swords = Structure.newDir(d_ns_functions, "swords", false);

        d_ns_advancements = Structure.newDir(d_ns, "advancements", false);
        d_ns_advancements_deal_damage = Structure.newDir(d_ns_advancements, "deal_damage.json", true);
        Structure.copyContents(template.resolve("deal_damage.json"), d_ns_advancements_deal_damage); 

        SwordFactory.create(7);
        
        EntityFactory.create(5);

        LootTableBuilder build = new LootTableBuilder();

        ArrayList<Path> lootTables = new ArrayList<Path>();

        Path newTable;

        String fileName;

        for (int i = 0; i < EntityFactory.list.size(); i++) {
           fileName = EntityFactory.list.get(i).getType() + ".json";
           newTable = Structure.newDir(d_ns_loot_tables_entities, fileName, true);
           lootTables.add(newTable);
        }

        int listLen = EntityFactory.list.size();
        Entity e1;

        for (int i = 0; i < listLen; i++) {
            e1 = EntityFactory.list.get(i);
            build.buildTable(SwordFactory.list.get(i), e1, lootTables.get(i));
            e1.setLootTable("\"aidp:entities/" + e1.getType() + "\"");
            EntityBuilder.writeToFunc(e1, f_loadmcfunction);
        }
    }

    public static void main(String[] args) {
        App test = new App();
    }
}
