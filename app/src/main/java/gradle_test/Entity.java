package gradle_test;

import java.util.ArrayList;

//To do: Gather identifiers that are variable in spawn command.
public class Entity {
    public String type;
    public String tag;
    public String name;
    public String color;

    public boolean nameBold;
    public boolean nameItalic;
    public boolean nameUnderlined;
    public boolean nameStrikethrough;
    public boolean nameObfuscated;
    
    public String entityDeathLootTable;

    public ArrayList<String> potionList = new ArrayList<String>();

    //The following variables are only set to 0 or 1
    public int entityVisualFire;
    public int entityNoGravity;
    public int entitySilent;
    public int entityInvulnerable;
    public int entityGlowing;
    public int entityCustomNameVisible;
    public int entityFallFlying;
    public int entityHealth;
    // End of boolean ints

    //Set from 0.0 to 1.0
    public double genericKnockbackResistance;

    //Can be up to approx 43, but should be kept well under that.
    public int genericMovementSpeed;

    public int genericAttackDamage;

    //Low bound is 0, and cap is 30
    public int genericArmor;

    public int genericArmorToughness;
    public int genericAttackKnockback;

    public Entity(String type, String lootTable) {
        this.type = type;
        entityDeathLootTable = lootTable;
    }

    public void setEntityAttributes(int visualFire, int noGravity, int silent, int invulnerable, int glowing, int customNameVisible, int fallFlying, int health) {
        entityVisualFire = visualFire;
        entityNoGravity = noGravity;
        entitySilent = silent;
        entityInvulnerable = invulnerable;
        entityGlowing = glowing;
        entityCustomNameVisible = customNameVisible;
        entityFallFlying = fallFlying;
        entityHealth = health;
    }

    public void setGenericAttributes(double knockbackResistance, int movementSpeed, int attackDamage, int armor, int armorToughness, int attackKnockback) {
        genericKnockbackResistance = knockbackResistance;
        genericMovementSpeed = movementSpeed;
        genericAttackDamage = attackDamage;
        genericArmor = armor;
        genericArmorToughness = armorToughness;
        genericAttackKnockback = attackKnockback;
    }

    public String getSpawnCommand() {
        buildTag();
        return "summon @p " + type + tag;
    }

    public void buildTag() {
        tag = "{";
        tag += getEntityModifiers();
        tag += getEntityNameString();
        tag += getPotionEffects();
        tag += getMiscEffects();

    }

     /*
     * ENTITY NAME AND MODIFIERS
     */

    public void updateEntityName(String entityName, String nameColor, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
        name = entityName;
        color = nameColor;
        nameBold = bold;
        nameItalic = italic;
        nameUnderlined = underlined;
        nameStrikethrough = strikethrough;
        nameObfuscated = obfuscated;
    }

    public String getEntityNameString() {
        String output = String.format("CustomName:'{\"text\":\"%s\",\"color\":\"%s\"", name, color);
        if (nameBold)
            output += String.format(",\"bold\":%b", nameBold);
        if (nameItalic)
            output += String.format(",\"italic\":%b", nameItalic);
        if (nameUnderlined)
            output += String.format(",\"underline\":%b", nameUnderlined);
        if (nameStrikethrough)
            output += String.format(",\"strikethrough\":%b", nameStrikethrough);
        if (nameObfuscated)
            output += String.format(",\"obfuscated\":%b", nameObfuscated);
        output += "'}";
        return output;
    }

    public String getEntityModifiers() {
        String output = "";
        if (entityVisualFire == 1)
            output += String.format("HasVisualFire:%db,", entityVisualFire);
        if (entityNoGravity == 1)
            output += String.format("NoGravity:%db,", entityNoGravity);
        if (entitySilent == 1)
            output += String.format("Silent:%db,", entitySilent);
        if (entityInvulnerable == 1)
            output += String.format("Invulnerable:%db,", entityInvulnerable);
        if (entityGlowing == 1)
            output += String.format("Glowing:%db,", entityGlowing);
        if (entityCustomNameVisible == 1)
            output += String.format("CustomNameVisible:%db,", entityCustomNameVisible);

        output += String.format("DeathLootTable:\"%s\",", entityDeathLootTable);

        if (entityFallFlying == 1)
            output += String.format("FallFlying:%db,", entityFallFlying);
        output += String.format("Health:%df,", entityHealth);
        return output;
    }

    /*
     * POTION AND GENERIC EFFECTS
     */

    public void addPotionEffects(int id, int amplifier, int duration, int showParticles) {
        String output = "";
        output += String.format("{Id:%d,Amplifier:%db,Duration:%d,ShowParticles:%db}", id, amplifier, duration, showParticles);
        potionList.add(output);
    }

    public String getPotionEffects() {
        String output="[";
        int size = potionList.size();
        for (int i = 0; i < size-2; i++) {
            output += potionList.get(i) + ",";
        }
        return output += potionList.get(size-1) + "],";
    }

    public String getMiscEffects() {
        String output = "Attributes:[";
        if (genericKnockbackResistance > 0)
            output += String.format("{Name:generic.knockback_resistance,Base:%f}", genericKnockbackResistance);
        if (genericMovementSpeed > 0)
            output += String.format(",{Name:generic.movement_speed,Base:%d}", genericMovementSpeed);
        if (genericAttackDamage > 0)
            output += String.format(",{Name:generic.attack_damage,Base:%d}", genericAttackDamage);
        if (genericArmor > 0)
            output += String.format(",{Name:generic.armor,Base:%d}", genericArmor);
        if (genericArmorToughness > 0)
            output += String.format(",{Name:generic.armor_toughness,Base:%d}", genericArmorToughness);
        if (genericAttackKnockback > 0)
            output += String.format(",{Name:generic.attack_knockback,Base:%d}", genericAttackKnockback);
        return output;
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

}
