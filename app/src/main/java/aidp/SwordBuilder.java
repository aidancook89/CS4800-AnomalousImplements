package aidp;

import java.util.ArrayList;

import java.nio.file.Path;

public class SwordBuilder {

    private static String[][] rarityLookup = new String[][] {
        {"COMMON", "white"},
        {"UNCOMMON", "green"},
        {"RARE", "blue"},
        {"EPIC", "purple"},
        {"LEGENDARY", "gold"}
    };

    private static String requestJson = "{"
    + "name: <string>," 
    + "color: <hexcode>," 
    + "lore: <string>"
    + "enchantments: [<unbreaking,knockback,sharpness,fire_aspect,looting>],"
    + "held_effects: [<speed,slowness,jump_boost,levitation>]"
    + "held_particles: [<cloud,flame,barrier,bubble,enchant>]"
    + "attack_effects: [<speed,slowness,jump_boost,levitation>]"
    + "attack_particles: [<cloud,flame,bubble,enchant>]"
    + "} ";

    private static String restrictions = ""
    + "enchantments: length <= 2,"
    + "player_effects: length <= 2," 
    + "player_particles: length <= 2," 
    + "entity_effects: length <= 2,"
    + "entity_particles: length <= 2";

    public static Sword newSword(int id, int rarity, String theme) {

        // Make request
        Request request = RequestHandler.makeRequest(
            "Provide me with a JSON in the following format: " + requestJson + restrictions,
            String.format("Interesting sword with theme: %s", theme), 
            0.9
        );        
        System.out.println(request.getContentString());

        // Create new sword and add attributes
        Sword sword = new Sword("wooden_sword", id, rarity);
        sword.setName(request.getAsString("name"), request.getAsString("color"));
        sword.setLore(request.getAsString("lore"));
        addEnchantments(sword, request.getAsArrayList("enchantments"));
        addHeldEffects(sword, request.getAsArrayList("held_effects"));
        addAttackEffects(sword, request.getAsArrayList("attack_effects"));
        addHeldParticles(sword, request.getAsArrayList("held_particles"));
        addAttackParticles(sword, request.getAsArrayList("attack_particles"));

        // Add item to deal damage
        Structure.writeToLine(App.f_deal_damagemcfunction, getDealDamageString(sword), 2);
         
        // Create attack function 
        Path attackFunction = Structure.newDir(App.d_swords, String.format("sword%d.mcfunction", sword.getId()), true);
        Structure.writeTo(attackFunction, getAttackFunctionString(sword), true);
    
        // Add potion effects for player
        Structure.writeTo(App.f_item_tickmcfunction, getHeldEffectsString(sword), true);

        // Add give command on load
        Structure.writeTo(App.f_loadmcfunction, "\n" + getGiveCommand(sword), true);

        return sword;
    }


    public static String getGiveCommand(Sword sword) {
		return String.format("give @a %s%s 1", sword.getType(), getTag(sword)); 
	} 

	public static String getTag(Sword sword) {
        return String.format("{display:{%s,%s}%s%s,CustomID:%d}",
            getNameString(sword), getLoreString(sword), getEnchantmentsString(sword), 
            getAttributesString(sword), sword.getId());
	}

    public static String getNameString(Sword sword) {
        return String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\",\"bold\":\"true\",\"italic\":\"false\"}'", 
            sword.getName(), sword.getColor());
    }

    public static String getLoreString(Sword sword) {
        return String.format("Lore:['{\"text\":\"\"}'," +
        "'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\",\"underlined\":\"true\"}'," +
        "'{\"text\":\"\"}'," +
        "'{\"text\":\"%s\",\"color\":\"%s\"}']", 
        rarityLookup[sword.getRarity()][0], rarityLookup[sword.getRarity()][1], sword.getLore(), "gray");
    }




    public static void addEnchantments(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            sword.getEnchantments().add(new Enchantment(item, 1));
        }
    } 

    public static String getEnchantmentsString(Sword sword) {
        if (sword.getEnchantments().size() == 0) return "";
        return ",Enchantments:" + sword.getEnchantments();
    }



    public static void addHeldEffects(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            sword.getHeldEffects().add(new Effect(item, 1, 0, false));
        }
    }

    public static String getHeldEffectsString(Sword sword) {
        if (sword.getHeldEffects().size() == 0) return "";
        String output = String.format("############ %s (%s), id: %d ############\n", 
            sword.getName(), sword.getType(), sword.getId());
        for (Effect e : sword.getHeldEffects()) {
            output += String.format("execute as @a[nbt={SelectedItem:{tag:{CustomID:%d}}}] run effect give @s ", 
                sword.getId()) + e + "\n";
        }
        return output; 
    }
 


    public static void addAttackEffects(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            sword.getAttackEffects().add(new Effect(item, 1, 0, false));
        }
    }

    public static String getAttackEffectsString(Sword sword) {
        String output = "";
        for (Effect e : sword.getAttackEffects()) {
            output += "effect give @s " + e + "\n";
        }
        return output;
    }



    public static String getDealDamageString(Sword sword) {
        return String.format("execute as @s[nbt={SelectedItem:{tag:{CustomID:%d}}}] run execute as " +
        "@e[distance=..5,nbt={HurtTime:10s},tag=!am_the_attacker] run function aidp:swords/sword%d", sword.getId(), sword.getId());
    }

    public static String getAttackFunctionString(Sword sword) {
        return String.format("%s\n%s\n%s\n",
            getAttackEffectsString(sword), getAttackParticlesString(sword), getHeldParticlesString(sword));
    }


    public static void addHeldParticles(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            sword.getHeldParticles().add(new Particle(item));
        }
    }

    public static String getHeldParticlesString(Sword sword) {
        String output = "";
        for (Particle p : sword.getHeldParticles()) {
            output += p + "\n";
        }
        return output;
    }

    public static void addAttackParticles(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            sword.getHeldParticles().add(new Particle(item));
        }
    }

    public static String getAttackParticlesString(Sword sword) {
        String output = "";
        for (Particle p : sword.getAttackParticles()) {
            output += "execute as @s run " + p + "\n";
        }
        return output;
    }


    public static void addAttributeModifier(Sword sword) {}
 
    public static String getAttributesString(Sword sword) {
        String output = "";
        /* 
        if (attributeModifiersList.size() > 0) {
            output = String.format(",AttributeModifiers:%s", listToString(attributeModifiersList));
        }
        */
        return output;
    }



}



class Enchantment {
    private String enchant;
    private int level;

    public Enchantment(String enchant, int level) {
        this.enchant = enchant;
        this.level = level;
    }

    public String toString() {
        return String.format("{id:\"minecraft:%s\",lvl:%ds}", enchant, level);
    }
}



class Effect {
    private String effect;
    private int length;
    private int amount; 
    private boolean showParticles;

    public Effect(String effect, int length, int amount, boolean showParticles) {
        this.effect = effect;
        this.length = length;
        this.amount = amount;
        this.showParticles = showParticles;
    }

    public String toString() {
        return String.format("minecraft:%s %d %d %b",
            effect, length, amount, showParticles);
    }
}


class Particle {
    private String particle;

    public Particle(String particle) {
        this.particle = particle;
    }

    public String toString() {
        return String.format("particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
    }
}



/*

{id:"minecraft:binding_curse",lvl:1s} 2
{id:"minecraft:sharpness",lvl:1s} -2 -4 -6 -8 -10
{id:"minecraft:smite",lvl:1s}
{id:"minecraft:bane_of_arthropods",lvl:1s}
{id:"minecraft:knockback",lvl:1s}
{id:"minecraft:fire_aspect",lvl:1s}
{id:"minecraft:looting",lvl:1s} -3 -6 -9
{id:"minecraft:sweeping",lvl:1s} -
{id:"minecraft:unbreaking",lvl:1s} -3 -6 -9
{id:"minecraft:mending",lvl:1s} -15
{id:"minecraft:vanishing_curse",lvl:1s} 5
    
{AttributeName:"generic.max_health",Name:"generic.max_health",Amount:1,Operation:2,UUID:[I;-2048207306,2079277823,-1829575274,1514256703],Slot:"mainhand"}
{AttributeName:"generic.follow_range",Name:"generic.follow_range",Amount:1,Operation:2,UUID:[I;-1071343243,-632797570,-1634786299,-992118208],Slot:"offhand"}
{AttributeName:"generic.knockback_resistance",Name:"generic.knockback_resistance",Amount:1,Operation:2,UUID:[I;-576070441,-180925074,-1983950958,2016677081],Slot:"feet"}
{AttributeName:"generic.movement_speed",Name:"generic.movement_speed",Amount:1,Operation:2,UUID:[I;-1616763457,652756468,-1255044040,495331673],Slot:"legs"}
{AttributeName:"generic.attack_damage",Name:"generic.attack_damage",Amount:1,Operation:2,UUID:[I;1039631330,-1392164380,-1685550125,2052723954],Slot:"chest"}
{AttributeName:"generic.armor",Name:"generic.armor",Amount:1,Operation:2,UUID:[I;-966410269,367413497,-1322336267,-879490572],Slot:"head"}
{AttributeName:"generic.armor_toughness",Name:"generic.armor_toughness",Amount:1,Operation:2,UUID:[I;1853727647,-1374141926,-1240270752,1854574506]}
{AttributeName:"generic.attack_speed",Name:"generic.attack_speed",Amount:1,Operation:2,UUID:[I;333910313,-860730335,-1454187290,-1632758482]}" 
{AttributeName:"generic.luck",Name:"generic.luck",Amount:1,Operation:2,UUID:[I;555339872,202067683,-1188013754,-448789942]}
 

*/