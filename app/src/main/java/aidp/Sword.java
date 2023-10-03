package aidp;

import java.util.ArrayList;

public class Sword {
    private String type;
    private int id;
    private int rarity;
    private String name = "default";
    private String color = "white"; 
    private String lore = "default";

    private ArrayList<Attribute> allAttributes = new ArrayList<Attribute>();
    private ArrayList<Enchantment> enchantments = new ArrayList<>();
    private ArrayList<Effect> heldEffects = new ArrayList<>();
    private ArrayList<Effect> attackEffects = new ArrayList<>();
    private ArrayList<Particle> heldParticles = new ArrayList<>();
    private ArrayList<Particle> attackParticles = new ArrayList<>();
    private ArrayList<Modifier> modifiers = new ArrayList<>();

    public Sword(String type, int id, int rarity) {
		this.type = type;
		this.id = id;
        this.rarity = rarity;
    }

    public String getType() { return type; }
    public int getId() { return id; }
    public int getRarity() { return rarity; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public String getLore() { return lore; }
    public ArrayList<Attribute> getAllAttributes() { return allAttributes; }
    public ArrayList<Enchantment> getEnchantments() { return enchantments; }
    public ArrayList<Modifier> getModifiers() { return modifiers; }
    public ArrayList<Effect> getHeldEffects() { return heldEffects; }
    public ArrayList<Effect> getAttackEffects() { return attackEffects; }
    public ArrayList<Particle> getHeldParticles() { return heldParticles; }
    public ArrayList<Particle> getAttackParticles() { return attackParticles; }

    public void setName(String name, String color) {
		this.name = name;
		this.color = color;
	}

    public void setLore(String lore) {
		this.lore = lore;
	}
}