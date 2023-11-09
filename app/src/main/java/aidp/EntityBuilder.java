package aidp;

import java.util.ArrayList;
import java.util.Random;
import java.nio.file.Path;

public class EntityBuilder {

    
    public static Entity newEntity(int entityNumber, EntityJson ej) {
        
        if (ej == null) {
            String fakeJson = "{" 
            + "\"type\": \"creeper\","
            + "\"name\": \"Legendary Creeper\","
            + "\"nameColor\": \"#FFFF00\","
            + "\"modifiers\": [\"Glowing\", \"Silent\"],"
            + "\"potions\": [\"regeneration\", \"fire_resistance\"],"
            + "\"generic\": [\"knockback_resistance\", \"movement_speed\"],"
            + "\"genValues\": [\"1\", \"2\"]"
            + "}";
            Request request = new Request(fakeJson);
            Entity e1 = new Entity(request.getAsString("type"));
            e1.setName(request.getAsString("name"), request.getAsString("nameColor"));
            e1.setLootTable("\"aidp:entities/" + e1.getType() + entityNumber + "\"");
            addEntityModifiers(e1, request.getAsArrayList("modifiers"));
            addPotionEffects(e1, request.getAsArrayList("potions"));
            addGenEffects(e1, request.getAsArrayList("generic"), request.getAsArrayList("genValues"));
            buildTag(e1);
            return e1;
        }
        Random rand = new Random();
        ArrayList<String> genValues = new ArrayList<String>();
        for (int i = 0; i < ej.generic_effects.size(); i++) {
            genValues.add(Integer.toString(rand.nextInt(ej.generic_effects.size() + 1)));
        }

        Entity e1 = new Entity(ej.type);
        e1.setName(ej.name, ej.color);
        e1.setLootTable("\"aidp:entities/" + e1.getType() + entityNumber + "\"");
        addEntityModifiers(e1, ej.modifiers);
        addPotionEffects(e1, ej.potion_effects);
        addGenEffects(e1, ej.generic_effects, genValues);
        buildTag(e1);
        return e1;
    }

    public static void writeToFunc(Entity e1, Path file) {
        Structure.writeTo(file, "\n" + getSpawnCommand(e1), true);
    }

    public static String getSpawnCommand(Entity e1) {
        return "summon " + e1.getType() + " ~ ~ ~ " + e1.getTag();
    }

    public static void buildTag(Entity e1) {
        String tag = "{";
        tag += getEntityModifiers(e1.getModifiers());
        tag += e1.getLootTable() + ",";
        tag += getEntityNameString(e1);
        tag += getPotionEffects(e1.getEffects());
        tag += getGenEffects(e1.getGenericMods());
        tag += "}";
        e1.setTag(tag);

    }

    /*
     * ENTITY NAME AND MODIFIERS
     */

    public static String getEntityNameString(Entity e1) {
        return String.format("CustomName:'{\"text\":\"%s\",\"color\":\"%s\"," +
            "\"bold\":true,\"italic\":true,\"underline\":true}',", e1.getName(), e1.getColor());
    }

    public static void addEntityModifiers(Entity e1, ArrayList<String> modList) {
        for (String item : modList)
            e1.getModifiers().add(new EntityModifiers(item, 1));
    }

    public static String getEntityModifiers(ArrayList<EntityModifiers> list) {
        if (list.isEmpty() || list == null) {
            return "";
        }
        String output = "";
        int size = list.size();
        for (int i = 0; i < size - 1; i++)
            output += list.get(i).toString() + ",";
        output += "CustomNameVisible:1b,";
        return output += list.get(size - 1).toString() + ",";
    }

     /*
     * POTION AND GENERIC EFFECTS
     */

     public static void addPotionEffects(Entity e1, ArrayList<String> effectList) {

        for (String item : effectList)
            e1.getEffects().add(new PotionEffect(item, -1, 2));
        //String output = "";
        //output += String.format("{minecraft:%s,amplifier:%db,duration:%d,show_particles:%db}", id, amplifier, -1, 1);
        //e1.getEffects().add(output);
    }

    public static String getPotionEffects(ArrayList<PotionEffect> potionList) {
        if (potionList.isEmpty() || potionList == null) {
            return "";
        }
        String output="active_effects:[";
        int size = potionList.size();
        for (int i = 0; i < size-1; i++) {
            output += potionList.get(i).toString() + ",";
        }
        return output += potionList.get(size-1) + "],";
    }

    public static void addGenEffects(Entity e1, ArrayList<String> genList, ArrayList<String> genValues) {
        for (int i = 0; i < genList.size(); i++)
            e1.getGenericMods().add(new GenericModifiers(genList.get(i), genValues.get(i)));
    }

    public static String getGenEffects(ArrayList<GenericModifiers> list) {
        if (list.isEmpty() || list == null) {
            return "";
        }
        String output = "Attributes:[";
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            output += list.get(i).toString() + ",";
        }
        output += list.get(size - 1);
        return output += "]";
    }
}

class EntityModifiers {
    private String effect;
    private int active;

    public EntityModifiers(String effect, int active) {
        this.effect = effect;
        this.active = active;
    }

    public String toString() {
        return String.format("%s:%db", effect, active);
    }
}

class GenericModifiers {
    private String mod;
    private String base;

    public GenericModifiers(String mod, String base) {
        this.mod = "generic." + mod;
        this.base = base;
    }

    public String toString() {
        if (mod.equals("generic.knockback_resistance")) {
            float fBase = Float.parseFloat(base);
            if (fBase <= 1.0 && fBase >= 0.0)
                return String.format("{Name:%s,Base:%f}", mod, fBase);
            else
                return String.format("{Name:%s,Base:%f}", mod, 0.5);
        }
        else
            return String.format("{Name:%s,Base:%d}", mod, Integer.parseInt(base));
    }
}

class PotionEffect {
    private String effect;
    private int length;
    private int amount;
    private boolean hideParticles;

    public PotionEffect(String effect, int length, int amount) {
        this.effect = effect;
        this.length = length;
        this.amount = amount;
        hideParticles = true;
    }

    public String toString() {
        return String.format("{id:\"minecraft:%s\",amplifier:%d,duration:%d,show_particles:%b}",
            effect, amount, length, hideParticles);
    }
}


