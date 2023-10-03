package aidp;

import java.util.ArrayList;
import java.nio.file.Path;
import java.util.Random;
import java.util.function.Function;

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
    + "enchantments: size <= 2,"
    + "attributes: size <= 2"
    + "player_effects: size <= 2," 
    + "player_particles: size <= 2," 
    + "entity_effects: size <= 2,"
    + "entity_particles: size <= 2";

    public static Sword newSword(int id, int rarity, String theme) {

        // Make request
        Request request = RequestHandler.makeRequest(
            "Provide me with a JSON in the following format: " + requestJson + restrictions,
            String.format("Sword with themes: %s", theme), 
            0.9
        );        
        System.out.println(request.getContentString());

        // Create new sword and add attributes
        Sword sword = new Sword("wooden_sword", id, rarity);
        sword.setName(request.getAsString("name"),request.getAsString("color"));
        sword.setLore(request.getAsString("lore"));
        addAttributes(sword, request.getAsArrayList("enchantments"), Enchantment::new);
        addAttributes(sword, request.getAsArrayList("modifiers"), Modifier::new);
        addAttributes(sword, request.getAsArrayList("held_effects"), HeldEffect::new);
        addAttributes(sword, request.getAsArrayList("attack_effects"), AttackEffect::new);
        addAttributes(sword, request.getAsArrayList("held_particles"), HeldParticle::new);
        addAttributes(sword, request.getAsArrayList("attack_particles"), AttackParticle::new);
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
     * getAttributeString
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
     * addAttributes
     * 
     * Accepts a list of strings and the desired attribute target
     * and adds a new attribute of that type into the corresponding 
     * list of our sword object.
     */
    public static void addAttributes(Sword sword, ArrayList<String> list, Function<String, Attribute> attributeFactory) {
        for (String item : list) {
            Attribute attribute = attributeFactory.apply(item);
            sword.getAllAttributes().add(attribute);

            if (attribute instanceof Enchantment) sword.getEnchantments().add((Enchantment) attribute);
            if (attribute instanceof Modifier) sword.getModifiers().add((Modifier) attribute);
            if (attribute instanceof HeldEffect) sword.getHeldEffects().add((HeldEffect) attribute);
            if (attribute instanceof AttackEffect) sword.getAttackEffects().add((AttackEffect) attribute);
            if (attribute instanceof HeldParticle) sword.getHeldParticles().add((HeldParticle) attribute);
            if (attribute instanceof AttackParticle) sword.getAttackParticles().add((AttackParticle) attribute);
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
        name, name, amount, operation, uuid, slot);
    }
}