package aidp;

import java.util.ArrayList;

public class Sword {
    private int id;
    private int rarity;
    private Name name;
    private Lore lore;
    private Type type;
    private ArrayList<Enchantment> enchantments = new ArrayList<>();
    private ArrayList<Modifier> modifiers = new ArrayList<>();
    private ArrayList<WielderEffect> wielderEffects = new ArrayList<>();
    private ArrayList<VictimEffect> victimEffects = new ArrayList<>();
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Sound> sounds = new ArrayList<>();

    public Sword(int id, int rarity) { 
        this.id = id; 
        this.rarity = rarity;
    }

    public int getId() { return id; }
    public int getRarity() { return rarity; }

    public Name getName() { return name; }
    public void setName(Name name) { this.name = name; }

    public Lore getLore() { return lore; }
    public void setLore(Lore lore) { this.lore = lore; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public ArrayList<Modifier> getModifiers() { return modifiers; }
    public void addModifier(Modifier attribute) { modifiers.add(attribute); }

    public ArrayList<Enchantment> getEnchantments() { return enchantments; }
    public void addEnchantment(Enchantment attribute) { enchantments.add(attribute); }

    public ArrayList<WielderEffect> getWielderEffects() { return wielderEffects; }
    public void addWielderEffect(WielderEffect attribute) { wielderEffects.add(attribute); }

    public ArrayList<VictimEffect> getVictimEffects() { return victimEffects; }
    public void addVictimEffect(VictimEffect attribute) { victimEffects.add(attribute); }

    public ArrayList<Particle> getParticles() { return particles; }
    public void addParticle(Particle attribute) { particles.add(attribute); }

    public ArrayList<Sound> getSounds() { return sounds; }
    public void addSound(Sound attribute) { sounds.add(attribute); }

    public String toString() {
        String output = String.format(
        "%s (id: %s)\n"
        + "Type: %s\n"
        + "%s"
        + "\n",
        name.toPretty(), id, 
        type.toPretty(),
        lore.toPretty()
        );

        output += "Modifiers: \n";
        for (Modifier m : modifiers) {
            output += String.format(" * %s\n", m.toPretty());
        }

        output += "Enchantments: \n";
        for (Enchantment m : enchantments) {
            output += String.format(" * %s\n", m.toPretty());
        }

        output += "Wielder Effects: \n";
        for (WielderEffect m : wielderEffects) {
            output += String.format(" * %s\n", m.toPretty());
        }

        output += "Victim Effects: \n";
        for (WielderEffect m : wielderEffects) {
            output += String.format(" * %s\n", m.toPretty());
        }

        output += "Particle Effects: \n";
        for (Particle m : particles) {
            output += String.format(" * %s\n", m.toPretty());
        }

        output += "Sounds: \n";
        for (Sound m : sounds) {
            output += String.format(" * %s\n", m.toPretty());
        }

        return output;
    }
}