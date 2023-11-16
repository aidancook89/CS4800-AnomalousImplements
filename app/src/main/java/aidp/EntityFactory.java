package aidp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

/**
 * Class EntityFactory
 */
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

    //ArrayList of desired Minecraft potion effects
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

    //ArrayList of desired Minecraft entity generic modifications
    public static ArrayList <String> genericList = new ArrayList<String>(List.of(
        "generic.knockback_resistance",
        "generic.movement_speed",
        "generic.attack_damage",
        "generic.armor",
        "generic.armor_toughness",
        "generic.attack_knockback"
    ));

    //ArrayList of acceptable entity types, i.e. ones that fight back against player
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

    /**
     * create
     * @param count - Number of Entity Objects to be created and stored
     * 
     * Creates AI enhanced Entities, number determined by count parameter.
     * Once Entity is created, loot table is generated and assigned, then spawn command is 
     * written to mcfunction file.
     */
    public static void create(int count) {

        //flag variable for using OpenAI API requests
        //true = use API, false = use default "dummy" JSONS built in
        boolean requestsEnabled = true;

       
        if (requestsEnabled) {
            themes = RequestHandler.makeRequest("Provide a JSON in the format: {themes: []} where themes has 100 interesting words", 
            "", 
            0.1).getAsArrayList("themes");
        }

        int modifiersCount = 0;
        int potionEffectCount = 0;
        int genEffectCount = 0;

        //For loop to generate count-many Entity Objects
        for (int j = 0; j < count; j++) {
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

            System.out.println("Making request");
            if (requestsEnabled) {
                Gson gson = new Gson();
                Request request = RequestHandler.makeRequest(
                String.format("Provide a JSON in the format: %s with the rules: %s", requestJson, rules),
                String.format("Monster with themes: %s", SwordFactory.randomList(themes, 3).toString()), 
                0.1);        
                System.out.println(request.getContentString());
                String JsonString = request.getContentString();
                EntityJson ej = gson.fromJson(JsonString, EntityJson.class);
                Entity e1 = EntityBuilder.newEntity(j, ej);
                list.add(e1);
            }
            else {
                Entity e1 = EntityBuilder.newEntity(j, null);
                list.add(e1);
            }

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

        ArrayList<Sword> swordList = getRandomSwordListHelper(listLen);
        for (int i = 0; i < listLen; i++) {
            e1 = EntityFactory.list.get(i);
            build.buildTable(swordList.get(i), e1, lootTables.get(i));
            EntityBuilder.writeToFunc(e1, App.f_loadmcfunction);
        }
    }

    private static ArrayList<Sword> getRandomSwordListHelper(int count) {
        System.out.println("Creating sword list");
        ArrayList<Sword> full = new ArrayList<Sword>();
        Random rand = new Random();
        for (int i = 0; i < SwordFactory.swordList.size(); i++) {
            ArrayList<Sword> curList = SwordFactory.swordList.get(i);
            for (int j = 0; j < curList.size(); j++) {
                full.add(curList.get(j));
            }
        }
       
        ArrayList<Sword> out = new ArrayList<Sword>();
        for (int i = 0; i < count; i++) {
            int index = rand.nextInt(full.size());
            out.add(full.get(index));
            full.remove(index);
        }

        return out;
    }

}
