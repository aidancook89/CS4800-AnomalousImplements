package aidp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;


public class EntityFactory {

    private static String requestJson = "{"
    + "type: ,"
    + "name: ,"
    + "color: ,"
    + "modifiers: [],"
    + "potion_effects: [],"
    + "generic_effects: [],"
    + "gen_values: [],"
    + "}";
    public static ArrayList<Entity> list = new ArrayList<Entity>();
    public static ArrayList<String> themes = new ArrayList<String>();

    public static Random rand = new Random();

    public static void create(int count) {
        boolean requestsEnabled = false;

        if (requestsEnabled) {
            themes = RequestHandler.makeRequest("Provide a JSON in the format: {themes: []} where themes has 100 interesting words", 
            "", 
            0.1).getAsArrayList("themes");
        }

        Entity newEnt = null;
        for (int i = 0; i < count; i++) {
            newEnt = EntityBuilder.newEntity(i);
            list.add(newEnt);
        }

        LootTableBuilder build = new LootTableBuilder();

        ArrayList<Path> lootTables = new ArrayList<Path>();

        Path newTable;

        String fileName;

        for (int i = 0; i < EntityFactory.list.size(); i++) {
           fileName = EntityFactory.list.get(i).getType()+ i + ".json";
           newTable = Structure.newDir(App.d_ns_loot_tables_entities, fileName, true);
           lootTables.add(newTable);
        }

        int listLen = EntityFactory.list.size();
        Entity e1;

        for (int i = 0; i < listLen; i++) {
            e1 = EntityFactory.list.get(i);
            build.buildTable(SwordFactory.list.get(i), e1, lootTables.get(i));
            EntityBuilder.writeToFunc(e1, App.f_loadmcfunction);
        }
    }

}
