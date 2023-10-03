package aidp;

import java.util.ArrayList;
import java.nio.file.Path;
import java.util.Random;

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
    + "enchantments: [<binding_curse,sharpness,smite,bane_of_arthropods,knockback,fire_aspect,looting,sweeping,unbreaking,mending,vanishing_curse>]"
    + "modifiers: [<max_health,knockback_resistantce,movement_speed,attack_damage,armor,armor_touchness,attack_speed,luck,max_absorption>]"
    + "held_effects: [<speed,slowness,jump_boost,levitation>]"
    + "attack_effects: [<speed,slowness,jump_boost,levitation>]"
    + "held_particles: [<cloud,flame,barrier,bubble,enchant>]"
    + "attack_particles: [<cloud,flame,bubble,enchant>]"
    + "} ";
    
    private static String restrictions = ""
    + "enchantments: pick <= 2,"
    + "attributes: pick <= 1"
    + "player_effects: pick <= 2," 
    + "player_particles: pick <= 2," 
    + "entity_effects: pick <= 2,"
    + "entity_particles: pick <= 2";



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
        sword.setName(request.getAsString("name"),request.getAsString("color"));
        sword.setLore(request.getAsString("lore"));
        addEnchantments(sword, request.getAsArrayList("enchantments"));
        addModifiers(sword, request.getAsArrayList("modifiers"));
        addHeldEffects(sword, request.getAsArrayList("held_effects"));
        addAttackEffects(sword, request.getAsArrayList("attack_effects"));
        addHeldParticles(sword, request.getAsArrayList("held_particles"));
        addAttackParticles(sword, request.getAsArrayList("attack_particles"));
        balanceAttributes(sword);

        // Add item to deal damage
        Structure.writeToLine(App.f_deal_damagemcfunction, getDealDamageString(sword), 2);
         
        // Create attack function 
        Path attackFunction = Structure.newDir(App.d_swords, String.format("sword%d.mcfunction", sword.getId()), true);
        Structure.writeTo(attackFunction, getAttackFunctionString(sword), true);
    
        // Add potion effects for player
        Structure.writeTo(App.f_item_tickmcfunction, getHeldEffectString(sword), true);

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
            getModifierString(sword), sword.getId());
	}

    // REQUIRED: ADD PARSING TO VERIFY THAT THERE ARE NO CHARACTERS THAT BREAK FORMATING FOR MCFUNCTION (', ", maybe more...)
    public static String getNameString(Sword sword) {
        return String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\",\"bold\":\"true\",\"italic\":\"false\"}'", 
            sword.getName().replace("'", "\'"), 
            sword.getColor()
        );
    }

    // REQUIRED: ADD FUNCTIONALITY TO ADD "NEWLINES" INTO LORE
    public static String getLoreString(Sword sword) {
        return String.format("Lore:['{\"text\":\"\"}'," +
        "'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\",\"underlined\":\"true\"}'," +
        "'{\"text\":\"\"}'," +
        "'{\"text\":\"%s\",\"color\":\"%s\"}']", 
        rarityLookup[sword.getRarity()][0], rarityLookup[sword.getRarity()][1], sword.getLore(), "gray");
    }

    public static String getDealDamageString(Sword sword) {
        return String.format("execute as @s[nbt={SelectedItem:{tag:{CustomID:%d}}}] run execute as " +
        "@e[distance=..5,nbt={HurtTime:10s},tag=!am_the_attacker] run function aidp:swords/sword%d", sword.getId(), sword.getId());
    }

    public static String getAttackFunctionString(Sword sword) {
        return String.format("%s\n%s\n%s\n",
            getAttackEffectString(sword), getAttackParticleString(sword), getHeldParticleString(sword));
    }


    
    /*
     * Balance Attributes
     */
    public static void balanceAttributes(Sword sword) { }



    /*
     * Get attribute string
     */
    public static String getEnchantmentsString(Sword sword) {
        if (sword.getEnchantments().size() == 0) return "";
        return ",Enchantments:" + sword.getEnchantments();
    }

    
    public static String getModifierString(Sword sword) {
        if (sword.getModifiers().size() == 0) return "";
        return ",AttributeModifiers:" + sword.getModifiers();
    }


    public static String getHeldEffectString(Sword sword) {
        if (sword.getHeldEffects().size() == 0) return "";
        String output = String.format("############ %s (%s), id: %d ############\n", 
            sword.getName(), sword.getType(), sword.getId());
        for (Effect e : sword.getHeldEffects()) {
            output += String.format("execute as @a[nbt={SelectedItem:{tag:{CustomID:%d}}}] run effect give @s ", 
                sword.getId()) + e + "\n";
        }
        return output; 
    }

 
    public static String getAttackEffectString(Sword sword) {
        String output = "";
        for (Effect e : sword.getAttackEffects()) {
            output += "effect give @s " + e + "\n";
        }
        return output;
    }


    public static String getHeldParticleString(Sword sword) {
        String output = "";
        for (Particle p : sword.getHeldParticles()) {
            output += p + "\n";
        }
        return output;
    }


    public static String getAttackParticleString(Sword sword) {
        String output = "";
        for (Particle p : sword.getAttackParticles()) {
            output += "execute as @s run " + p + "\n";
        }
        return output;
    }



    /*
     * Add Attributes
     * 
     * REQUIRED: SIMPLY INTO ONE FUNCTION...
     */
    public static void addEnchantments(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Attribute attribute = new Enchantment(item);
            sword.getEnchantments().add((Enchantment) attribute);
            sword.getAllAttributes().add(attribute);
        }
    }
     
    public static void addModifiers(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Attribute attribute = new Modifier(item);
            sword.getModifiers().add((Modifier) attribute);
            sword.getAllAttributes().add(attribute);
        }
    } 
    
    public static void addHeldEffects(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Attribute attribute = new HeldEffect(item);
            sword.getHeldEffects().add((HeldEffect) attribute);
            sword.getAllAttributes().add(attribute);
        }
    }

    public static void addAttackEffects(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Attribute attribute = new AttackEffect(item);
            sword.getAttackEffects().add((AttackEffect) attribute);
            sword.getAllAttributes().add(attribute);
        }
    }
 
    public static void addHeldParticles(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Attribute attribute = new HeldParticle(item);
            sword.getHeldParticles().add((HeldParticle) attribute);
            sword.getAllAttributes().add(attribute);
        }
    }

    public static void addAttackParticles(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Attribute attribute = new AttackParticle(item);
            sword.getAttackParticles().add((AttackParticle) attribute);
            sword.getAllAttributes().add(attribute);
        }
    }
}



abstract class Attribute {
    protected int price;

    public abstract void upgrade();

    public int getPrice() {
        return price;
    }
}



class Enchantment extends Attribute {
    private String enchant;
    private int level = 1;

    public Enchantment(String enchant) {
        this.enchant = enchant;
    }

    public void upgrade() {
        level += 1;
        price += 1;
    }

    public String toString() {
        return String.format("{id:\"minecraft:%s\",lvl:%ds}", enchant, level);
    }
}



abstract class Effect extends Attribute {
    protected String effect;
    protected int length = 1;
    protected int amount = 0; 
    protected boolean hideParticles = true;

    public String toString() {
        return String.format("minecraft:%s %d %d %b",
            effect, length, amount, hideParticles);
    }
}

class HeldEffect extends Effect {
    public HeldEffect(String effect) {
        this.effect = effect;
        price = 2;
    }

    public void upgrade() {}
}

class AttackEffect extends Effect {
    public AttackEffect(String effect) {
        this.effect = effect;
        price = 2;
    }

    public void upgrade() {}
}


abstract class Particle extends Attribute {
    protected String particle;

    public String toString() {
        return String.format("particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
    }
}

class HeldParticle extends Particle {
    public HeldParticle(String particle) {
        this.particle = particle;
        price = 1;
    }

    public void upgrade() {}
}

class AttackParticle extends Particle {
    public AttackParticle(String particle) {
        this.particle = particle;
        price = 1;
    }

    public void upgrade() {}
}


class Modifier extends Attribute {
    private String name;
    private double amount = 0.1;
    private int operation = 1;
    private String slot = "mainhand";
    private long uuid;

    public Modifier(String name) {
        this.name = name;
        uuid = new Random().nextLong() % 10000000000L;
    }

    public void upgrade() {

    }

    //REQUIRED: NOT FUNCTIONING
    public String toString() {
        return String.format("{AttributeName:\"generic.%s\",Name:\"generic.%s\"," +
        "Amount:%f,Operation:%d,UUID:[I;469651195,-1054259622,-1995201699,-859124615],Slot:\"%s\"}",
        //"Amount:%f,Operation:%d,UUID:[I;%010d,111111111,2222222222,3333333333],Slot:\"%s\"}",
        name, name, amount, operation, uuid, slot);
    }
}