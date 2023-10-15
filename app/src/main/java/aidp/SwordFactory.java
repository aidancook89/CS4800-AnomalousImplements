package aidp;

import java.util.ArrayList;
import java.util.Random;
import com.google.gson.Gson;

public class SwordFactory {

    private static String requestJson = "{"
    + "name: ,"
    + "color: ,"
    + "lore: ,"
    + "enchantments: [],"
    + "modifiers: [],"
    + "wielder_effects: [],"
    + "victim_effects: [],"
    + "particles: [],"
    + "sounds: [],"
    + "}";

    

    public static ArrayList<Sword> list = new ArrayList<Sword>();
    public static ArrayList<String> themesList;

    private static Random rand = new Random();


    public static void create(int count) {
        boolean makeRequests = false;

        if (makeRequests) {
            themesList = RequestHandler.makeRequest(
                "Provide a JSON in the format: {themes: []} where themes has 100 interesting words", 
                "", 
                0.1).getAsArrayList("themes");
        }

        for (int i = 0; i < count; i++) {
            int enchantmentsCount = rand.nextInt(3);
            int modifierCount = rand.nextInt(3);
            int wielderEffectsCount = rand.nextInt(3);
            int victimEffectsCount = rand.nextInt(3);

            String rules = String.format("{" 
                + "name: string,"
                + "color: hexcode"
                + "lore: two sentences,"
                + "enchantments: pick %d from [%s],"
                + "modifiers: pick %d from [%s],"
                + "wielder_effects: pick %d from [%s],"
                + "victim_effects: pick %d from [%s],"
                + "particles: pick %d from [%s],"
                + "sounds: pick %d from [%s],"
                + "}",
                enchantmentsCount, Enchantment.list.toString(), 
                modifierCount, Modifier.list.toString(), 
                wielderEffectsCount, Effect.list.toString(), 
                victimEffectsCount, Effect.list.toString(), 
                2, Particle.list.toString(), 
                1, Sound.list.toString()
            );

            if (makeRequests) createJsonAI(i, requestJson, rules, themesList); 
            else createJsonRandom(i);
        }
    }

    public static void createJsonRandom(int id) {
        SwordJson sj = new SwordJson(
            rand.nextInt(5), "Red Sword", "Red", "This is test lore. The sword is very red. I want to get an idea of how longer lore is displayed.", 
            randomList(Enchantment.list, 3),
            randomList(Modifier.list, 2),
            randomList(Effect.list, 2),
            randomList(Effect.list, 2),
            randomList(Particle.list, 2),
            randomList(Sound.list, 2)
        );
        sj.id = id;
        Sword sword = SwordBuilder.newSword(sj);
        list.add(sword);
    }

    public static void createJsonAI(int id, String requestJson, String rules, ArrayList<String> themesList) {
        Gson gson = new Gson();
        Request request = RequestHandler.makeRequest(
            String.format("Provide a JSON in the format: %s with the rules: %s", requestJson, rules),
            String.format("Sword with themes: %s", randomList(themesList, 3).toString()), 
            0.1);        
        System.out.println(request.getContentString());
        String jsonString = request.getContentString(); 
        SwordJson sj = gson.fromJson(jsonString, SwordJson.class);
        sj.id = id;
        Sword sword = SwordBuilder.newSword(sj);
        list.add(sword);
    }

    public static ArrayList<String> randomList(ArrayList<String> source, int count) {
        ArrayList<String> list = new ArrayList<String>();
        Random rand = new Random();
        int size = source.size();
        for (int i = 0; i < count; i++) {
            list.add(source.get(rand.nextInt(size)));
        }
        return list;
    }
}
