package aidp;

import java.util.ArrayList;

public class Sword {
    private String type;
    private int id;
    private int rarity;
    private String name = "default";
    private String color = "white"; 
    private String lore = "default";
    private ArrayList<Enchantment> enchantmentsList = new ArrayList<Enchantment>();
    private ArrayList<Effect> heldEffectsList = new ArrayList<Effect>();
    private ArrayList<Effect> attackEffectsList = new ArrayList<Effect>();
    private ArrayList<Particle> heldParticlesList = new ArrayList<Particle>();
    private ArrayList<Particle> attackParticlesList = new ArrayList<Particle>();
    private ArrayList<Attribute> attributesList = new ArrayList<Attribute>();

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

    public ArrayList<Enchantment> getEnchantments() {
        return enchantmentsList;
    }

    public ArrayList<Effect> getHeldEffects() {
        return heldEffectsList;
    }
    
    public ArrayList<Effect> getAttackEffects() {
        return attackEffectsList;
    }

    public ArrayList<Particle> getHeldParticles() {
        return heldParticlesList;
    }
    
    public ArrayList<Particle> getAttackParticles() {
        return attackParticlesList;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributesList;
    }

    public void setName(String name, String color) {
		this.name = name;
		this.color = color;
	}

    public void setLore(String lore) {
		this.lore = lore;
	}
}