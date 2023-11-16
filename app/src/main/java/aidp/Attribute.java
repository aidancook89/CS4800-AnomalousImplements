package aidp;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public abstract class Attribute {
    public abstract String toPretty(); 
    public abstract String toString();


    // HELPERS
    public String toTitleCase(String input) {
        String[] words = input.split(" ");
        String output = "";

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            output += word.substring(0,1).toUpperCase();
            output += word.substring(1,word.length()).toLowerCase();
            output += " ";
        }

        return output;
    }

    public String padString(String source, int padLength) {
        int add = padLength - source.length(); 
        for (int i = -1; i < add; i++) source += " ";
        return source;
    }

    public ArrayList<String> breakUpString(String text, int count) {
        int startIndex = 0;
        int length = text.length();
        ArrayList<String> parts = new ArrayList<>();

        while (startIndex < length) {
            int endIndex = startIndex + count;
            if (endIndex > length) endIndex = length;
            while (endIndex < length && endIndex > startIndex && text.charAt(endIndex - 0) != ' ') {
                endIndex--;
            }
            parts.add(text.substring(startIndex, endIndex));
            if (endIndex < length && text.charAt(endIndex) == ' ') {
                endIndex++;
            }
            startIndex = endIndex; 
        }
        
        return parts;
    }
}

abstract class UpgradeAttribute extends Attribute {
    protected int upgradePrice;
    protected int upgradeMaxLevel;
    protected int upgradeLevel = -1;

    public void upgrade() {
        upgradeLevel += 1;
        upgradePrice += Math.ceil(upgradePrice/2);
    }

    // Checks if a upgrade can be made to this attribute
    // If it can, return the price of the upgrade, otherwise return 0
    // Keep in mind that an "upgrade" can be bad, and return a negative price
    public int canUpgrade(int credit) {
        if ((credit >= upgradePrice) && (upgradeLevel < upgradeMaxLevel)) return upgradePrice;
        else return 0;
    }


    public static ArrayList<AttributeInstance> instanceList;
    public static ArrayList<String> optionList;

    protected static AttributeInstance instanceLookUp(String name, ArrayList<AttributeInstance> list) {
        for (AttributeInstance inst : list) {
            if (inst.name.equals(name)) return inst;
        }
        return null;
    }

    protected static ArrayList<String> getOptionsList(ArrayList<AttributeInstance> list) {
        ArrayList<String> output = new ArrayList<String>();
        for (AttributeInstance inst : list) output.add(inst.name);
        return output;
    }
}

abstract class AttributeInstance {
    public String name;
    public int price;
    public int upgradeMaxLevel;

    public AttributeInstance(String name, int upgradeMaxLevel, int price) {
        this.name = name;
        this.upgradeMaxLevel = upgradeMaxLevel;
        this.price = price;
    }
}


//////////////////////////////////////////////////
// NAME
//////////////////////////////////////////////////
class Name extends Attribute {
    public String text = "default";
    public String color = "white";

    public Name(String text, String color) {
        this.text = text;
        this.color = color;
    }

    public String toString() {
        return String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\",\"bold\":\"true\",\"italic\":\"false\"}'", 
            text.replaceAll("'", "\\\\'"), color);
    }

    public String toPretty() {
        return text;
    }
}



//////////////////////////////////////////////////
// LORE
//////////////////////////////////////////////////
class Lore extends Attribute {
    public String text = "default";
    public String color = "gray";
    private int lineLength = 70;
    private int rarity = 0;
    private ArrayList<WielderEffect> wielderEffects;
    private ArrayList<VictimEffect> victimEffects;

    private static String[][] rarityLookup = new String[][] {
        {"COMMON", "white"},
        {"UNCOMMON", "green"},
        {"RARE", "blue"},
        {"EPIC", "purple"},
        {"LEGENDARY", "gold"}
    };

    public Lore(String text, int rarity, ArrayList<WielderEffect> wielderEffects, ArrayList<VictimEffect> victimEffects) {
        this.text = text;
        this.rarity = rarity;
        this.wielderEffects = wielderEffects;
        this.victimEffects =  victimEffects;
    }

    public String toString() {
        // RARITY
        String output = "Lore:[";
        output += "'{\"text\":\"\"}'";
        output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\",\"underlined\":\"true\"}'", rarityLookup[rarity][0], rarityLookup[rarity][1]);
        output += ",'{\"text\":\"\"}'";

        // LORE
        ArrayList<String> parts = breakUpString(text, lineLength); 
        for (String part : parts) {
            output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"false\"}'", 
                part.replaceAll("'", "\\\\'"), color);
        }
        output += ",'{\"text\":\"\"}'";

        // WIELDER EFFECTS
        if (wielderEffects != null) {
            String title = "Wielder Effects: ";
            int size = wielderEffects.size(); 
            if (size == 0) title += "None";

            output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"false\"}'", title, "white");
            for (int i = 0; i < size; i++) {
                output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\"}'", 
                    " * " + wielderEffects.get(i).toPretty(), color);
            }
        }

        // VICTIM EFFECTS
        if (victimEffects != null) {
            String title = "Victim Effects: ";
            int size = victimEffects.size(); 
            if (size == 0) title += "None";

            output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"false\"}'", title, "white");
            for (int i = 0; i < size; i++) {
                output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\"}'", 
                    " * " + victimEffects.get(i).toPretty(), color);
            }
        }
         
        return output + "]";
    }

    public String toPretty() {
        return String.format("Rarity: %s\nLore: %s", rarityLookup[rarity][0], text);
    }
}



//////////////////////////////////////////////////
// PARTICLE
//////////////////////////////////////////////////
class Particle extends Attribute {
    private String particle;

    public Particle(String particle) {
        this.particle = particle;
    }

    public String toString() {
        return String.format("execute as @s at @s run particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
    }

    public String toPretty() {
        return toTitleCase(particle.replaceAll("_", " ")); 
    }

    public static ArrayList<String> optionList = new ArrayList<String>(List.of(
        "ambient_entity_effect","angry_villager","ash","bubble","bubble_pop","campfire_cosy_smoke", 
        "campfire_signal_smoke","cherry_leaves","cloud","composter","crimson_spore","dolphin",
        "dragon_breath","effect","egg_crack","elder_guardian","electric_spark","enchant","enchanted_hit",
        "end_rod","entity_effect","firework","fishing","flame","flash","glow","glow_squid_ink",
        "happy_villager","heart","instant_effect","item_slime","item_snowball","large_smoke","lava",
        "mycelium","nautilus","note","poof","portal","rain","reverse_portal","scrape","sculk_charge_pop",
        "sculk_soul","smoke","sneeze","snowflake","sonic_boom","soul","soul_fire_flame","spit",
        "splash","spore_blossom_air","squid_ink","uderwater","warped_spore","wax_off","wax_on","white_ash","witch"
    ));
}



//////////////////////////////////////////////////
// SOUND
//////////////////////////////////////////////////
class Sound extends Attribute {
    private String sound;

    public Sound(String sound) {
        this.sound = sound;
    }

    public String toString() {
        return String.format("playsound minecraft:%s ambient @p ~ ~ ~ 0.5 1", sound);
    }

    public String toPretty() {
        return toTitleCase(sound);
    }

    public static ArrayList<String> optionList = new ArrayList<String>(List.of(
        "block.amethyst_block.place","entity.arrow.shoot","entity.firework_rocket.launch", 
        "block.piston.extend", "block.bamboo.hit","block.basalt.hit", "block.beehive.drip",
        "block.calcite.break", "block.chain.fall","firework_rocket.blast_far","block.ladder.step"
    ));
}



//////////////////////////////////////////////////
// TYPE
//////////////////////////////////////////////////
class Type extends UpgradeAttribute {
    private String[] typeList = {"wooden_sword", "stone_sword", "iron_sword", "golden_sword", "diamond_sword", "netherite_sword"};

    public Type() {
        upgradeMaxLevel = typeList.length-1;
        upgradePrice = 10;
        upgradeLevel = 0;
    }

    public void upgrade() {
        upgradeLevel += 1;
        upgradePrice += 15;
    }

    public String toString() {
        return typeList[upgradeLevel];
    }

    public String toPretty() {
        return toTitleCase(typeList[upgradeLevel].replaceAll("_", " "));
    }
}



//////////////////////////////////////////////////
// MODIFIER
//////////////////////////////////////////////////
class Modifier extends UpgradeAttribute {
    protected String name;
    protected double amount = 0;
    protected int operation = 1;
    protected String slot = "mainhand";
    protected long uuid;

    public Modifier(String name) {
        this.name = name;
        uuid = new Random().nextLong() % 1000000000L;
        upgradePrice = 2;
        upgradeMaxLevel = 5;
    }

    public void upgrade() {
        super.upgrade();
        amount += 0.05;
    }

    public String toString() {
        return String.format("{AttributeName:\"generic.%s\",Name:\"generic.%s\",Amount:%f,Operation:%d,"
        + "UUID:[I;%d,2081703129,-1522820555,-530514328],Slot:\"%s\"}", 
            name, name, amount, operation, uuid, slot);
    }

    public String toPretty() {
        String attributeName = toTitleCase(name.replaceAll("_", " "));
        return String.format("%s: %f ", attributeName, amount);
    }

    public static ArrayList<String> optionList = new ArrayList<String>(List.of(
        "max_health","knockback_resistance","movement_speed","armor","armor_touchness","luck","max_absorption"
    ));
}

class AttackDamage extends Modifier {

    public AttackDamage(double amount) {
        super("attack_damage");
        this.amount = amount;
        operation = 0;
        upgradeLevel = 0;
        upgradePrice = 3;
        upgradeMaxLevel = 10;
    }

    public void upgrade() {
        upgradeLevel += 1;
        upgradePrice += Math.ceil(upgradePrice/2);
        amount += 1;
    }
}

class AttackSpeed extends Modifier {

    public AttackSpeed(double amount) {
        super("attack_speed");
        this.amount = amount;
        operation = 0;
        upgradeLevel = 0;
        upgradePrice = 2;
        upgradeMaxLevel = 10;
    }

    public void upgrade() {
        upgradeLevel += 1;
        upgradePrice += Math.ceil(upgradePrice/2);
        amount += 0.5;
    }
}



//////////////////////////////////////////////////
// ENCHANTMENT
//////////////////////////////////////////////////
class Enchantment extends UpgradeAttribute {
    private EnchantmentInstance enchant;

    public Enchantment(String name) {
        enchant = (EnchantmentInstance) instanceLookUp(name, instanceList); 
        if (enchant == null) enchant = new EnchantmentInstance("unbreaking", 0, 0);

        upgradePrice = enchant.price;
        upgradeMaxLevel = enchant.upgradeMaxLevel;
    }

    public String toString() {
        return String.format("{id:\"minecraft:%s\",lvl:%ds}", enchant.name, upgradeLevel+1);
    }

    public String toPretty() {
        String enchantmentName = toTitleCase(enchant.name.replaceAll("_", " "));
        return String.format("%s %d", enchantmentName, upgradeLevel);
    }

    public static ArrayList<AttributeInstance> instanceList = new ArrayList<AttributeInstance>(List.of(
        new EnchantmentInstance("sharpness",5,4),
        new EnchantmentInstance("smite",5,3),
        new EnchantmentInstance("bane_of_arthropods",5,2),
        new EnchantmentInstance("knockback",5,3),
        new EnchantmentInstance("fire_aspect",1,2),
        new EnchantmentInstance("looting",3,4),
        new EnchantmentInstance("sweeping",3,3),
        new EnchantmentInstance("unbreaking",3,4),
        new EnchantmentInstance("mending",1,6),
        new EnchantmentInstance("vanishing_curse",0,-6)
    ));

    public static ArrayList<String> optionList = getOptionsList(instanceList);
}

class EnchantmentInstance extends AttributeInstance {
    public EnchantmentInstance(String name, int upgradeMaxLevel, int price) {
        super(name, upgradeMaxLevel, price);
    }
} 



//////////////////////////////////////////////////
// EFFECT
//////////////////////////////////////////////////
abstract class Effect extends UpgradeAttribute {
    protected EffectInstance effect;

    public String toString() {
        return String.format("minecraft:%s %d %d %b",
            effect.name, effect.length, upgradeLevel, true);
    }

    public String toPretty() {
        String name = effect.name;
        String[] words = name.split("_");
        name = "";
        for (String word : words) {
            name += word.substring(0,1).toUpperCase() 
                + word.substring(1).toLowerCase() + " ";
        }
        return String.format("%s %d", name, upgradeLevel + 1);
    }
}

class EffectInstance extends AttributeInstance {
    int length;

    public EffectInstance(String name, int upgradeMaxLevel, int price, int length) {
        super(name, upgradeMaxLevel, price);
        this.length = length;
    }
}

class WielderEffect extends Effect {
    public WielderEffect(String name) {
        // If we passed an improper name, add a null effect
        effect = (EffectInstance) instanceLookUp(name, instanceList); 
        if (effect == null) effect = new EffectInstance("speed", 0, 0, 0);

        upgradePrice = effect.price;
        upgradeMaxLevel = effect.upgradeMaxLevel;
    }

    public static ArrayList<AttributeInstance> instanceList = new ArrayList<AttributeInstance>(List.of(
        new EffectInstance("speed", 3, 5, 1),
        new EffectInstance("slowness", 2, -5, 1),
        new EffectInstance("haste", 2, 3, 1),
        new EffectInstance("mining_fatigue", 2, 5, 2),
        new EffectInstance("strength", 2, 5, 1),
        new EffectInstance("jump_boost", 4, 5, 1),
        new EffectInstance("resistance", 2, 8, 1),
        new EffectInstance("fire_resistance", 2, 5, 1),
        new EffectInstance("water_breathing", 0, 5, 1),
        new EffectInstance("invisibility", 0, 10, 1),
        new EffectInstance("blindness", 0, -15, 2),
        new EffectInstance("night_vision", 2, 5, 1),
        new EffectInstance("weakness", 1, -7, 1),
        new EffectInstance("health_boost", 2, 10, 1),
        new EffectInstance("absorption", 2, 5, 1),
        new EffectInstance("saturation", 2, 5, 1),
        new EffectInstance("glowing", 0, -5, 1),
        new EffectInstance("levitation", 3, 2, 1),
        new EffectInstance("slow_falling", 0, 5, 1),
        new EffectInstance("conduit_power", 2, 10, 1),
        new EffectInstance("dolphins_grace", 2, 5, 1)
    ));
    
    public static ArrayList<String> optionList = getOptionsList(instanceList);
}

class VictimEffect extends Effect {
    public VictimEffect(String name) {
        // If we passed an improper name, add a null effect
        effect = (EffectInstance) instanceLookUp(name, instanceList); 
        if (effect == null) effect = new EffectInstance("speed", 0, 0, 0);

        upgradePrice = effect.price;
        upgradeMaxLevel = effect.upgradeMaxLevel;
    }

    public static ArrayList<AttributeInstance> instanceList = new ArrayList<AttributeInstance>(List.of(
        new EffectInstance("speed", 2, -5, 3),
        new EffectInstance("slowness", 2, 5, 3),
        new EffectInstance("strength", 1, -5, 3),
        new EffectInstance("jump_boost", 2, -2, 3),
        new EffectInstance("regeneration", 1, -5, 3),
        new EffectInstance("resistance", 1, -5, 3),
        new EffectInstance("fire_resistance", 1, -5, 3),
        new EffectInstance("invisibility", 0, -5, 2),
        new EffectInstance("weakness", 2, 5, 3),
        new EffectInstance("poison", 0, 10, 3),
        new EffectInstance("glowing", 0, 3, 3),
        new EffectInstance("levitation", 4, 2, 1),
        new EffectInstance("slow_falling", 0, 2, 3)
    ));
    
    public static ArrayList<String> optionList = getOptionsList(instanceList);
}