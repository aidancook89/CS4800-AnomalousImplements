package aidp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EntityFactory {

    private static String requestJson = "{"
    + "type: ,"
    + "name: ,"
    + "color: ,"
    + "modifiers: [],"
    + "potion_effects: [],"
    + "generic_effects: [],"
    //+ "gen_values: [],"
    + "}";
    public static ArrayList<Entity> list = new ArrayList<Entity>();
    public static ArrayList<String> themes = new ArrayList<String>();

    public static ArrayList<String> modifiers = new ArrayList<String>(List.of(
        "Silent",
        "Glowing"
    ));

    public static ArrayList<String> potionEff = new ArrayList<String>(List.of(
        "speed",
        "slowness",
        "haste",
        "mining_fatigue",
        "strength",
        "instant_health",
        "instant_damage",
        "jump_boost",
        "nausea",
        "regeneration",
        "resistance",
        "fire_resistance",
        "water_breathing",
        "invisibility",
        "blindness",
        "night_vision",
        "hunger",
        "weakness",
        "poison",
        "wither",
        "health_boost",
        "absorption",
        "saturation",
        "glowing",
        "levitation",
        "luck",
        "unluck",
        "slow_falling"
    ));

    public static ArrayList <String> genericList = new ArrayList<String>(List.of(
        "generic.knockback_resistance",
        "generic.movement_speed",
        "generic.attack_damage",
        "generic.armor",
        "generic.armor_toughness",
        "generic.attack_knockback"
    ));

    public static ArrayList <String> validTypes = new ArrayList<String>(List.of(
        "blaze",
        "cave_spider",
        "creeper",
        "enderman",
        "endermite",
        "evoker",
        "hoglin",
        "iron_golem",
        "llama",
        "magma_cube",
        "pillager",
        "piglin",
        "ravager",
        "shulker",
        "spider",
        "skeleton",
        "zombie",
        "witch",
        "wither_skeleton"
    ));

    public static Random rand = new Random();

    public static void create(int count) {
        boolean requestsEnabled = false;

        if (requestsEnabled) {
            themes = RequestHandler.makeRequest("Provide a JSON in the format: {themes: []} where themes has 100 interesting words", 
            "", 
            0.1).getAsArrayList("themes");
        }

        int modifiersCount = 0;
        int potionEffectCount = 0;
        int genEffectCount = 0;

        for (int i = 0; i < count; i++) {
            modifiersCount = rand.nextInt(2);
            potionEffectCount = rand.nextInt(3);
            genEffectCount = rand.nextInt(3);

            String rules = String.format("{"
            + "type: pick 1 from [%s]"
            + "name: string,"
            + "color: hexcode,"
            + "modifiers: pick %d from [%s],"
            + "potion_effects: pick %d from [%s],"
            + "generic_effects: pick %d from [%s]"
            + "}", validTypes.toString(), modifiersCount, modifiers.toString(),
            potionEffectCount, potionEff.toString(),
            genEffectCount, genericList.toString());

            if (requestsEnabled) createAiJson(i, requestJson, rules, themes);
            else createRandomJson(i);

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
