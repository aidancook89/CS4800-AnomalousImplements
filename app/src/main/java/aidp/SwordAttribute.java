package aidp;

import java.util.Random;
import java.util.ArrayList;

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

class Lore extends SwordAttribute {
    private String text = "default";
    private int rarity = 0;
    private String color = "gray";

    private static String[][] rarityLookup = new String[][] {
        {"COMMON", "white"},
        {"UNCOMMON", "green"},
        {"RARE", "blue"},
        {"EPIC", "purple"},
        {"LEGENDARY", "gold"}
    };

    public Lore(String text, int rarity) {
        this.text = text;
        this.rarity = rarity;
    }

    public String toString() {
        String output = "Lore:[";
        output += "'{\"text\":\"\"}'";
        output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\",\"underlined\":\"true\"}'", rarityLookup[rarity][0], rarityLookup[rarity][1]);
        output += ",'{\"text\":\"\"}'";

        ArrayList<String> parts = breakUp(text, 40); 
        for (String part : parts) {
            output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"false\"}'", 
                part.replaceAll("'", "\'"), color);
        }

        return output + ",'{\"text\":\"\"}']";
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
}



abstract class Effect extends UpgradeAttribute {
    protected String effect;
    protected int length = 1;
    protected int amount = 0; 
    protected boolean hideParticles = true;

    public String toString() {
        return String.format("minecraft:%s %d %d %b",
            effect, length, amount, hideParticles);
    }
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


class Particle extends SwordAttribute {
    private String particle;

    public Particle(String particle) {
        this.particle = particle;
    }

    public String toString() {
        return String.format("execute as @s at @s run particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
    }
}

class Sound extends SwordAttribute {
    private String sound;

    public Sound(String sound) {
        this.sound = sound;
    }

    public String toString() {
        return String.format("playsound minecraft:%s ambient @p ~ ~ ~ 0.5 1", sound);
    }
}



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