package aidp;

import java.util.ArrayList;

public class Sword {
    private int id;
    private int rarity;
    private int credit;
    private Name name;
    private Lore lore;
    private Type type;
    private ArrayList<Enchantment> enchantments = new ArrayList<>();
    private ArrayList<Effect> heldEffects = new ArrayList<>();
    private ArrayList<Effect> attackEffects = new ArrayList<>();
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Modifier> modifiers = new ArrayList<>();
    private ArrayList<SwordAttribute> upgradeAttributes = new ArrayList<SwordAttribute>();

    public Sword(int id, int rarity) { 
        this.id = id; 
        this.rarity = rarity;
        credit = 20 + (rarity * 20);
    }

    public int getId() { return id; }
    public int getRarity() { return rarity; }

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }

    public Name getName() { return name; }
    public void setName(Name name) { this.name = name; }

    public Lore getLore() { return lore; }
    public void setLore(Lore lore) { this.lore = lore; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public ArrayList<SwordAttribute> getUpgradeAttributes() { return upgradeAttributes; }
    public void addUpgradeAttribute(SwordAttribute attribute) { upgradeAttributes.add(attribute); }

    public ArrayList<Enchantment> getEnchantments() { return enchantments; }
    public void addEnchantment(Enchantment attribute) { enchantments.add(attribute); }

    public ArrayList<Modifier> getModifiers() { return modifiers; }
    public void addModifier(Modifier attribute) { modifiers.add(attribute); }

    public ArrayList<Effect> getHeldEffects() { return heldEffects; }
    public void addHeldEffect(HeldEffect attribute) { heldEffects.add(attribute); }

    public ArrayList<Effect> getAttackEffects() { return attackEffects; }
    public void addAttackEffect(AttackEffect attribute) { attackEffects.add(attribute); }

    public ArrayList<Particle> getParticles() { return particles; }
    public void addParticle(Particle attribute) { particles.add(attribute); }

}