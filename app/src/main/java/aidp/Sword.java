package aidp;

import java.util.ArrayList;

public class Sword {
    private ArrayList<SwordAttribute> allAttributes = new ArrayList<SwordAttribute>();
    private int id;
    private int rarity;
    private int credit;
    private Name name;
    private Lore lore;
    private Type type;
    private ArrayList<Enchantment> enchantments = new ArrayList<>();
    private ArrayList<Effect> heldEffects = new ArrayList<>();
    private ArrayList<Effect> attackEffects = new ArrayList<>();
    private ArrayList<Particle> heldParticles = new ArrayList<>();
    private ArrayList<Particle> attackParticles = new ArrayList<>();
    private ArrayList<Modifier> modifiers = new ArrayList<>();

    public Sword(int id, int rarity) { 
        this.id = id; 
        this.rarity = rarity;
        credit = 20 + (rarity * 20);
    }

    public int getId() { return id; }
    public int getRarity() { return rarity; }
    public int getCredit() { return credit; }
    public Name getName() { return name; }
    public Lore getLore() { return lore; }
    public Type getType() { return type; }
    public ArrayList<SwordAttribute> getAllAttributes() { return allAttributes; }
    public ArrayList<Enchantment> getEnchantments() { return enchantments; }
    public ArrayList<Modifier> getModifiers() { return modifiers; }
    public ArrayList<Effect> getHeldEffects() { return heldEffects; }
    public ArrayList<Effect> getAttackEffects() { return attackEffects; }
    public ArrayList<Particle> getHeldParticles() { return heldParticles; }
    public ArrayList<Particle> getAttackParticles() { return attackParticles; }

    public void setName(Name name) { this.name = name; }
    public void setLore(Lore lore) { this.lore = lore; }
    public void setType(Type type) { this.type = type; }
    public void setCredit(int credit) { this.credit = credit; }
}