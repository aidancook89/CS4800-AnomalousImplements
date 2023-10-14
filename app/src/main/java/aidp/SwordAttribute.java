package aidp;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public abstract class SwordAttribute {}

abstract class UpgradeAttribute extends SwordAttribute {
    protected int upgradePrice;
    protected int upgradeMaxLevel;
    protected int upgradeLevel = 0;

    public abstract void upgrade();

    // Checks if a upgrade can be made to this attribute
    // If it can, return the price of the upgrade, otherwise return 0
    // Keep in mind that an "upgrade" can be bad, and return a negative price
    public int canUpgrade(int credit) {
        if ((credit >= upgradePrice) && (upgradeLevel < upgradeMaxLevel)) return upgradePrice;
        else return 0;
    }
}



//////////////////////////////////////////////////
// NAME
//////////////////////////////////////////////////
//TODO ERROR WITH APOSTROPHES
class Name extends SwordAttribute {
    private String text = "default";
    private String color = "white";

    public Name(String text, String color) {
        this.text = text;
        this.color = color;
    }

    public String toString() {
        return String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\",\"bold\":\"true\",\"italic\":\"false\"}'", 
            text.replaceAll("'", "\'"), color);
    }
}



//////////////////////////////////////////////////
// LORE
//////////////////////////////////////////////////
class Lore extends SwordAttribute {
    private String text = "default";
    private int rarity = 0;
    private String color = "gray";
    private ArrayList<Effect> wielderEffects;
    private ArrayList<Effect> victimEffects;

    private static String[][] rarityLookup = new String[][] {
        {"COMMON", "white"},
        {"UNCOMMON", "green"},
        {"RARE", "blue"},
        {"EPIC", "purple"},
        {"LEGENDARY", "gold"}
    };

    public Lore(String text, int rarity, ArrayList<Effect> wielderEffects, ArrayList<Effect> victimEffects) {
        this.text = text;
        this.rarity = rarity;
        this.wielderEffects = wielderEffects;
        this.victimEffects = victimEffects;
    }

    public String toString() {
        // RARITY
        String output = "Lore:[";
        output += "'{\"text\":\"\"}'";
        output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\",\"underlined\":\"true\"}'", rarityLookup[rarity][0], rarityLookup[rarity][1]);
        output += ",'{\"text\":\"\"}'";

        // LORE
        ArrayList<String> parts = breakUp(text, 40); 
        for (String part : parts) {
            output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"false\"}'", 
                part.replaceAll("'", "\'"), color);
        }
        output += ",'{\"text\":\"\"}'";

        // EFFECTS
        String wielderTitle = "Wielder Effects:";
        String victimTitle = "Victim Effects:";
        int padLength = wielderEffects.get(0).toPretty().length();
        for (int i = 0; i < wielderEffects.size(); i++) {
            padLength = Math.max(padLength, wielderEffects.get(i).toPretty().length());
        }
        padLength = Math.max(padLength, wielderTitle.length()) + 3;
        String title = padString(wielderTitle, padLength) + victimTitle;
        output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"false\"}'", title, color);

        int wielderSize = wielderEffects.size();
        int victimSize = victimEffects.size();
        int loopCount = Math.max(wielderSize, victimSize);
        for (int i = 0; i < loopCount; i++) {
            String wielderPretty = "";
            String victimPretty = "";
            if (i < wielderSize) wielderPretty = wielderEffects.get(i).toPretty();
            if (i < victimSize) victimPretty = victimEffects.get(i).toPretty();
            System.out.println(padLength);
            String str = padString(wielderPretty, padLength) + victimPretty;
            System.out.println(str);
            output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\"}'", str, color);
        }

        return output + "]";
    }

    public String padString(String source, int padLength) {
        int add = padLength - source.length(); 
        for (int i = 0; i < add; i++) source += " ";
        return source;
    }

    public ArrayList<String> breakUp(String text, int count) {
        int startIndex = 0;
        int length = text.length();
        ArrayList<String> parts = new ArrayList<>();

        while (startIndex < length) {
            int endIndex = startIndex + count;
            if (endIndex > length) endIndex = length;
            while (endIndex < length && endIndex > startIndex && text.charAt(endIndex - 1) != ' ') {
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



//////////////////////////////////////////////////
// TYPE
//////////////////////////////////////////////////
class Type extends UpgradeAttribute {
    private int type = 0;
    private String[] typeList = {"wooden_sword", "stone_sword", "iron_sword", "gold_sword", "diamond_sword", "netherite_sword"};

    public Type(int type) {
        this.type = type % typeList.length;
        upgradeMaxLevel = typeList.length-1;
        upgradePrice = 10;
    }

    public void upgrade() {
        type += 1;

        upgradeLevel += 1;
        upgradePrice += 10;
    }

    public String toString() {
        return typeList[type];
    }
}



//////////////////////////////////////////////////
// ENCHANTMENT
//////////////////////////////////////////////////
class Enchantment extends UpgradeAttribute {
    private String enchant;
    private int level = 1;

    public Enchantment(String enchant) {
        this.enchant = enchant;
        upgradeMaxLevel = 5;
        upgradePrice = 2;
    }

    public void upgrade() {
        level += 1;

        upgradeLevel += 1;
        upgradePrice += 2;
    }

    public String toString() {
        return String.format("{id:\"minecraft:%s\",lvl:%ds}", enchant, level);
    }


    public static ArrayList<String> list = new ArrayList<String>(List.of(
        "binding_curse","sharpness","smite","bane_of_arthropods","knockback","fire_aspect","looting","sweeping","unbreaking","mending","vanishing_curse"
    ));
}


//////////////////////////////////////////////////
// MODIFIER
//////////////////////////////////////////////////
class Modifier extends UpgradeAttribute {
    protected String name;
    protected double amount = 0.1;
    protected int operation = 1;
    protected String slot = "mainhand";
    protected long uuid;

    public Modifier(String name) {
        this.name = name;
        uuid = new Random().nextLong() % 1000000000L;
        upgradePrice = 3;
        upgradeMaxLevel = 3;
    }

    public void upgrade() {
        amount += 0.1;

        upgradeLevel += 1;
        upgradePrice += 3;
    }

    public String toString() {
        return String.format("{AttributeName:\"generic.%s\",Name:\"generic.%s\",Amount:%f,Operation:%d,"
        + "UUID:[I;%d,2081703129,-1522820555,-530514328],Slot:\"%s\"}", 
            name, name, amount, operation, uuid, slot);
    }

    public static ArrayList<String> list = new ArrayList<String>(List.of(
        "max_health","knockback_resistantce","movement_speed","armor","armor_touchness","luck","max_absorption"
    ));
}

class AttackDamage extends Modifier {

    public AttackDamage(double amount) {
        super("attack_damage");
        this.amount = amount;
        operation = 0;
        upgradePrice = 3;
        upgradeMaxLevel = 10;
    }

    public void upgrade() {
        amount += 1;

        upgradeLevel += 1;
        upgradePrice += 3;
    }
}

class AttackSpeed extends Modifier {

    public AttackSpeed(double amount) {
        super("attack_speed");
        this.amount = amount;
        operation = 0;
        upgradePrice = 3;
        upgradeMaxLevel = 10;
    }

    public void upgrade() {
        amount += 0.4;

        upgradeLevel += 1;
        upgradePrice += 3;
    }
}



//////////////////////////////////////////////////
// EFFECT
//////////////////////////////////////////////////
abstract class Effect extends UpgradeAttribute {
    protected String effect;
    protected int length = 1;
    protected int amount = 0; 
    protected boolean hideParticles = true;

    public String toString() {
        return String.format("minecraft:%s %d %d %b",
            effect, length, amount, hideParticles);
    }

    public String toPretty() {
        String name = effect;
        String[] words = name.split("_");
        name = "";
        for (String word : words) {
            name += word.substring(0,1).toUpperCase() 
                + word.substring(1).toLowerCase() + " ";
        }
        return String.format("* %s %s", name, amount+1);
    }

    public static ArrayList<String> list = new ArrayList<String>(List.of(
        "speed","slowness","haste","mining_fatigue","strength","instant_health","instant_damage", 
        "jump_boost","nausea","regeneration","resistances","fire_resistance","water_breathing", 
        "invisibility","blindness","night_vision","hunger","weakness","poison","wither","health_boost",
        "absorption","saturation","glowing","levitation","slow_falling","conduit_power","dolphins_grace",
        "bad_omen","hero_of_the_village"
    ));
}

class WielderEffect extends Effect {
    public WielderEffect(String effect) {
        this.effect = effect;
        upgradeMaxLevel = 5;
        upgradePrice = 3;
    }

    public void upgrade() {
        amount += 1;

        upgradeLevel += 1;
        upgradePrice += 3;
    }
}

class VictimEffect extends Effect {
    public VictimEffect(String effect) {
        this.effect = effect;
        upgradeMaxLevel = 5;
        upgradePrice = 3;
    }

    public void upgrade() {
        amount += 1;

        upgradeLevel += 1;
        upgradePrice += 3;
    }
}



//////////////////////////////////////////////////
// PARTICLE
//////////////////////////////////////////////////
class Particle extends SwordAttribute {
    private String particle;

    public Particle(String particle) {
        this.particle = particle;
    }

    public String toString() {
        return String.format("execute as @s at @s run particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
    }

    public static ArrayList<String> list = new ArrayList<String>(List.of(
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
class Sound extends SwordAttribute {
    private String sound;

    public Sound(String sound) {
        this.sound = sound;
    }

    public String toString() {
        return String.format("playsound minecraft:%s ambient @p ~ ~ ~ 0.5 1", sound);
    }

    public static ArrayList<String> list = new ArrayList<String>(List.of(
        "block.amethyst_block.place","entity.arrow.shoot","entity.firework_rocket.launch", 
        "block.piston.extend", "block.bamboo.hit","block.basalt.hit", "block.beehive.drip",
        "block.calcite.break", "block.chain.fall","firework_rocket.blast_far","block.ladder.step"
    ));
}



