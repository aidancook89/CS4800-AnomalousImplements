package aidp;

import java.util.ArrayList;

public class SwordJson {
    public int id;
    public int rarity;
    public String name;
    public String color;
    public String lore;
    public ArrayList<String> enchantments;
    public ArrayList<String> modifiers;
    public ArrayList<String> wielder_effects;
    public ArrayList<String> victim_effects;
    public ArrayList<String> particles;
    public ArrayList<String> sounds;

    public SwordJson(
        int rarity, String name, String color, String lore,
        ArrayList<String> enchantments, 
        ArrayList<String> modifiers, 
        ArrayList<String> wielder_effects, 
        ArrayList<String> victim_effects, 
        ArrayList<String> particles, 
        ArrayList<String> sounds 
    ) {
        this.rarity = rarity;
        this.name = name;
        this.color = color;
        this.lore = lore;
        this.enchantments = enchantments;
        this.modifiers = modifiers;
        this.wielder_effects = wielder_effects;
        this.victim_effects = victim_effects;
        this.particles = particles;
        this.sounds = sounds;
    }
}
