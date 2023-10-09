package aidp;

import java.util.ArrayList;


//To do: Gather identifiers that are variable in spawn command.
public class Entity {
    private String type;
    private String tag;
    private String name;
    private String color;
    
    private String entityDeathLootTable = "DeathLootTable:";

    private ArrayList<PotionEffect> potionList = new ArrayList<PotionEffect>();

    //The following variables are only set to 0 or 1
    /*
     * Silent
     * Glowing
     * CustomNameVisible (should always be 1)
     * FallFlying
     * Health
     */
    private ArrayList<EntityModifiers> modifiers = new ArrayList<EntityModifiers>();

    /*
     * generic.knockback_resistance
     * generic.movement_speed
     * generic.attack_damage
     * generic.armor
     * generic.armor_toughness
     * generic.attack_knockback
     */
    private ArrayList<GenericModifiers> generic = new ArrayList<GenericModifiers>();

    public Entity(String type, String lootTable) {
        this.type = type;
        entityDeathLootTable += lootTable;
    }

    public String getType() { return type;}
    public String getTag() { return tag;}
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getName() { return name;}
    public void setName(String name, String color) { 
        this.name = name;
        this.color = color;
    }
    public String getColor() { return color;}
    public String getLootTable() { return entityDeathLootTable;}

    public void setLootTable(String lootTable) {
        entityDeathLootTable += lootTable;
    }

    public ArrayList<PotionEffect> getEffects() {
        return potionList;
    }

    public ArrayList<EntityModifiers> getModifiers() {
        return modifiers;
    }

    public ArrayList<GenericModifiers> getGenericMods() {
        return generic;
    }

}
    /*
     * Speed : id 1
     * Slowness : id 2
     * Haste : id 3
     * Mining Fatigue : id 4
     * Strength : id 5
     * Instant Health : id 6
     * Instant Damage : id 7
     * Jump Boost : id 8
     * Nausea : id 9
     * Regeneration : id 10
     * Resistance : id 11
     * Fire Resistance : id 12
     * Water Breathing : id 13
     * Invisibility : id 14
     * Blindness : id 15
     * Night Vision : id 16
     * Hunger : id 17
     * Weakness : id 18
     * Poison : id 19
     * Wither : id 20
     * Health Boost : id 21
     * Absorption : id 22
     * Saturation : id 23
     * Levitation : id 24
     * Glowing : id 25
     * Luck : id 26
     * Bad Luck : id 27
     * Slow Falling : id 28
     * Conduit Power : id 29
     * Dolphins Grace : id 30
     * Bad Omen : id 31
     * Hero of the Village : id 32
     * Darkness : id 33
     */

