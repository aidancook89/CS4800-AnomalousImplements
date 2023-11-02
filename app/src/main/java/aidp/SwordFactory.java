package aidp;

import java.util.ArrayList;
import java.util.Random;
import com.google.gson.Gson;

public class SwordFactory {

    private static Random rand = new Random();

    public static ArrayList<Sword> list = new ArrayList<Sword>();

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

    private static String rulesJson = "{" 
    + "name: string,"
    + "color: hexcode"
    + "lore: two sentences,"
    + "enchantments: pick %d from [%s],"
    + "modifiers: pick %d from [%s],"
    + "wielder_effects: pick %d from [%s],"
    + "victim_effects: pick %d from [%s],"
    + "particles: pick %d from [%s],"
    + "sounds: pick %d from [%s],"
    + "}";
    
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

   
    public static void create(int count) {
        int enchantmentsCount = 0;
        int modifiersCount = 2;
        int wielderEffectsCount = 0;
        int victimEffectsCount = 0;
        int particlesCount = 2;
        int soundsCount = 1;

        for (int i = 0; i < count; i++) {
            enchantmentsCount = 1 + rand.nextInt(2);
            wielderEffectsCount = 1 + rand.nextInt(2);
            victimEffectsCount = 1 + rand.nextInt(2);
            

            String rules = String.format(rulesJson,
                enchantmentsCount, enchantmentOptions, 
                modifiersCount, modifierOptions,
                wielderEffectsCount, wielderEffectOptions,
                victimEffectsCount, victimEffectOptions,
                particlesCount, particleOptions,
                soundsCount, soundOptions
            );

            createJson(i, requestJson, rules, themesList); 
        }
    }

    public static void createJsonRandom(int id) {
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
        sj.id = id;
        Sword sword = SwordBuilder.newSword(sj);
        list.add(sword);
    }

    public static void createJson(int id, String requestJson, String rules, String[] themesList) {
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

        for (int i = 0; i < 3; i++) {
            sj.id = id + i;
            sj.rarity = rand.nextInt(5);
            Sword sword = SwordBuilder.newSword(sj);
            list.add(sword);
        }
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
}