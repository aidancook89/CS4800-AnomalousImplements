package aidp;

import java.util.Random;
import java.util.ArrayList;

public abstract class SwordAttribute {
    protected int upgradePrice;
    protected int upgradeLevel = 0;
    protected int upgradeMaxLevel;

    public abstract void upgrade();

    // Checks if a upgrade can be made to this attribute
    // If it can, return the price of the upgrade, otherwise return 0
    // Keep in mind that an "upgrade" can be bad, and return a negative price
    public int canUpgrade(int credit) {
        if ((credit >= upgradePrice) && (upgradeLevel < upgradeMaxLevel)) return upgradePrice;
        else return 0;
    }
}


class Name extends SwordAttribute {
    private String text = "default";
    private String color = "white";

    public Name(String text, String color) {
        this.text = text;
        this.color = color;
        upgradeMaxLevel = 0; // Setting upgradeMaxLevel to zero means that this cannot be upgraded
    }

    public void upgrade() {}

    public String toString() {
        return String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\",\"bold\":\"true\",\"italic\":\"false\"}'", text, color);
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
        upgradeMaxLevel = 0; 
    }

    public void upgrade() {}

    public String toString() {
        String output = "Lore:[";
        output += "'{\"text\":\"\"}'";
        output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\",\"underlined\":\"true\"}'", rarityLookup[rarity][0], rarityLookup[rarity][1]);
        output += ",'{\"text\":\"\"}'";

        ArrayList<String> parts = breakUp(text, 40); 
        for (String part : parts) {
            output += String.format(",'{\"text\":\"%s\",\"color\":\"%s\"}'", part, color);
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



class Type extends SwordAttribute {
    private int type = 0;
    private String[] typeList = {"wooden_sword", "stone_sword", "iron_sword"};

    public Type(int type) {
        this.type = type % typeList.length;
        upgradeMaxLevel = typeList.length-1;
    }

    public void upgrade() {
        type += 1;
        upgradeLevel += 1;
        upgradePrice += 5;
    }

    public String toString() {
        return typeList[type];
    }
}



class Enchantment extends SwordAttribute {
    private String enchant;
    private int level = 1;

    public Enchantment(String enchant) {
        this.enchant = enchant;
        upgradeMaxLevel = 3;
        upgradePrice = 2;
    }

    public void upgrade() {
        level += 1;
        upgradePrice += 1;
        upgradeLevel += 1;
    }

    public String toString() {
        return String.format("{id:\"minecraft:%s\",lvl:%ds}", enchant, level);
    }
}



abstract class Effect extends SwordAttribute {
    protected String effect;
    protected int length = 1;
    protected int amount = 0; 
    protected boolean hideParticles = true;

    public String toString() {
        return String.format("minecraft:%s %d %d %b",
            effect, length, amount, hideParticles);
    }
}

class HeldEffect extends Effect {
    public HeldEffect(String effect) {
        this.effect = effect;
        upgradeMaxLevel = 3;
        upgradePrice = 2;
    }

    public void upgrade() {
        amount += 1;
        upgradePrice += 1;
        upgradeLevel += 1;
    }
}

class AttackEffect extends Effect {
    public AttackEffect(String effect) {
        this.effect = effect;
        upgradeMaxLevel = 3;
        upgradePrice = 2;
    }

    public void upgrade() {
        amount += 1;
        upgradePrice += 1;
        upgradeLevel += 1;
    }
}


abstract class Particle extends SwordAttribute {
    protected String particle;

    public String toString() {
        return String.format("particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
    }
}

class HeldParticle extends Particle {
    public HeldParticle(String particle) {
        this.particle = particle;
    }

    public void upgrade() {}
}

class AttackParticle extends Particle {
    public AttackParticle(String particle) {
        this.particle = particle;
    }

    public void upgrade() {}
}


class Modifier extends SwordAttribute {
    private String name;
    private double amount = 0.1;
    private int operation = 1;
    private String slot = "mainhand";
    private long uuid;

    public Modifier(String name) {
        this.name = name;
        uuid = new Random().nextLong() % 10000000000L;
    }

    public void upgrade() {

    }

    //REQUIRED: NOT FUNCTIONING
    public String toString() {
        return String.format("{AttributeName:\"generic.%s\",Name:\"generic.%s\"," +
        "Amount:%f,Operation:%d,UUID:[I;469651195,-1054259622,-1995201699,-859124615],Slot:\"%s\"}",
        name, name, amount, operation, uuid, slot);
    }
}