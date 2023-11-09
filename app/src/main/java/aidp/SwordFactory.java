package aidp;

import java.util.ArrayList;
import java.util.Random;
import com.google.gson.Gson;

public class SwordFactory {

    private static Random rand = new Random();

    public static ArrayList<Sword> list = new ArrayList<Sword>();
    private static int swordCount = 0;
    private static int enchantmentsCount = 2;
    private static int modifiersCount = 1;
    private static int wielderEffectsCount = 2;
    private static int victimEffectsCount = 2;
    private static int particlesCount = 2;
    private static int soundsCount = 1;

    private static String enchantmentOptions = Enchantment.optionList.toString();
    private static String modifierOptions = Modifier.optionList.toString();
    private static String wielderEffectOptions = WielderEffect.optionList.toString();
    private static String victimEffectOptions = VictimEffect.optionList.toString();
    private static String particleOptions = Particle.optionList.toString();
    private static String soundOptions = Sound.optionList.toString();

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

    private static String rules = String.format("{" 
    + "name: string with ',"
    + "color: hexcode"
    + "lore: string with at least 140 characters,"
    + "enchantments: pick %d from [%s],"
    + "modifiers: pick %d from [%s],"
    + "wielder_effects: pick %d from [%s],"
    + "victim_effects: pick %d from [%s],"
    + "particles: pick %d from [%s],"
    + "sounds: pick %d from [%s],"
    + "}",
    enchantmentsCount, enchantmentOptions, 
    modifiersCount, modifierOptions,
    wielderEffectsCount, wielderEffectOptions,
    victimEffectsCount, victimEffectOptions,
    particlesCount, particleOptions,
    soundsCount, soundOptions
    );
    
    public static String[] themesList = {"Adventure", "Enigma", "Euphoria", "Serenity", "Intrigue", "Rendezvous", "Ecstasy", 
        "Radiance", "Whimsy", "Harmony", "Mystique", "Bewilderment", "Symphony", "Reverie", "Enchantment", 
        "Pinnacle", "Cascade", "Reverence", "Fascination", "Odyssey", "Vivid", "Infinity", "Whisper", 
        "Luminosity", "Labyrinth", "Synchronicity", "Elixir", "Melody", "Aurora", "Perseverance", 
        "Ethereal", "Euphoria", "Resonance", "Zeitgeist", "Rhapsody", "Cacophony", "Solace", 
        "Empyrean", "Panorama", "Serendipity", "Vortex", "Tranquility", "Infinitesimal", "Utopia", 
        "Pandemonium", "Epiphany", "Spectacle", "Benevolence", "Quixotic", "Elysium", "Veracity", 
        "Apotheosis", "Symbiosis", "Gossamer", "Luminous", "Ephemeral", "Phenomenon", "Mellifluous", 
        "Paradox", "Eclipse", "Paragon", "Halcyon", "Whimsical", "Resplendent", "Surreal", "Ethereal", 
        "Cascade", "Panacea", "Nebula", "Abyss", "Vorfreude", "Sonnet", "Ineffable", "Luminescence", 
        "Peregrination", "Eudaimonia", "Nirvana", "Obelisk", "Palimpsest", "Sempiternal", "Quasar", 
        "Xanadu", "Nostalgia", "Empyrean", "Pinnacle", "Breathtaking", "Felicity", "Enthralling", 
        "Sovereign", "Awe-inspiring", "Resonant", "Majestic", "Ebullient", "Exquisite", "Astonishing", 
        "Vivid", "Enigmatic", "Radiant", "Jubilant", "Captivating", "Harmonious", "Spellbinding", 
        "Ineffable", "Phenomenal", "Transcendent"};


   
    public static void create(int requests, int count) {
        for (int i = 0; i < requests; i++) {
            createJson(requestJson, rules, themesList, count); 
        }
    }
    


    public static void createJson(String requestJson, String rules, String[] themesList, int count) {
        // Generate AI JSON
        Request request = RequestHandler.makeRequest(
            String.format("Provide a JSON in the format: %s with the rules: %s", requestJson, rules),
            String.format("Sword with themes: %s", randomList(themesList, 3).toString()), 
            0.1);        
        String jsonString = request.getContentString(); 
        System.out.println(jsonString);

        // Parse json string from api into Java class
        Gson gson = new Gson();
        SwordJson sj = gson.fromJson(jsonString, SwordJson.class);

        // Create multiple rarities of the same sword, without duplicates
        int[] rarityList = getIntegerList(count, 0, 4);
        for (int i = 0; i < rarityList.length; i++) {
            sj.id = swordCount++;
            sj.rarity = rarityList[i];
            Sword sword = SwordBuilder.newSword(sj);
            list.add(sword);
        }
    }



    //  Returns a random list of unique integers between the range min and max (both inclusive)
    public static int[] getIntegerList(int size, int min, int max) {
        int[] output = new int[size];
        ArrayList<Integer> options = new ArrayList<Integer>();
        Random rand = new Random();
        for (int i = min; i <= max; i++) options.add(i);
        for (int i = 0; i < size; i++) {
            int index = rand.nextInt(options.size());
            output[i] = options.get(index);
            options.remove(index);
        }
        return output;
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



    public static ArrayList<String> randomList(String[] source, int count) {
        ArrayList<String> list = new ArrayList<String>();
        Random rand = new Random();
        int size = source.length;
        for (int i = 0; i < count; i++) {
            list.add(source[rand.nextInt(size)]);
        }
        return list;
    }

    public static void createJsonRandom() {
        int rarity = rand.nextInt(5);
        SwordJson sj = new SwordJson(
            rarity, "Red Sword", "Red", "This is test lore. The sword is very red. I want to get an idea of how longer lore is displayed.", 
            randomList(Enchantment.optionList, 3),
            randomList(Modifier.optionList, 2),
            randomList(WielderEffect.optionList, 2),
            randomList(VictimEffect.optionList, 2),
            randomList(Particle.optionList, 2),
            randomList(Sound.optionList, 1)
        );
        sj.id = swordCount++;
        Sword sword = SwordBuilder.newSword(sj);
        list.add(sword);
    }
}