package aidp;

import java.util.ArrayList;
import java.nio.file.Path;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.function.Function;
import java.util.Random;

public class SwordBuilder {

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
        Sword sword = new Sword(id);
        sword.setName(new Name(request.getAsString("name"),request.getAsString("color")));
        sword.setLore(new Lore(request.getAsString("lore"), rarity));
        sword.setType(new Type(0));

        addAttributes(sword, request.getAsArrayList("enchantments"), Enchantment::new);
        addAttributes(sword, request.getAsArrayList("modifiers"), Modifier::new);
        addAttributes(sword, request.getAsArrayList("held_effects"), HeldEffect::new);
        addAttributes(sword, request.getAsArrayList("attack_effects"), AttackEffect::new);
        addAttributes(sword, request.getAsArrayList("held_particles"), HeldParticle::new);
        addAttributes(sword, request.getAsArrayList("attack_particles"), AttackParticle::new);

        sword.setCredit(10);
        balanceAttributes(sword);

        // Add item to deal damage
        Structure.writeToLine(App.f_deal_damagemcfunction, buildDealDamageString(sword), 2);
         
        // Create attack function 
        Path attackFunction = Structure.newDir(App.d_swords, String.format("sword%d.mcfunction", sword.getId()), true);
        Structure.writeTo(attackFunction, buildAttackFunction(sword), true);
    
        // Add potion effects for player
        Structure.writeTo(App.f_item_tickmcfunction, getHeldEffectString(sword), true);

        // Add give command on load
        Structure.writeTo(App.f_loadmcfunction, "\n" + buildGiveCommand(sword), true);

        return sword;
    }


    //////////////////////////////////////////////////
    // BUILDERS
    //////////////////////////////////////////////////

    public static void balanceAttributes(Sword sword) { 
        ArrayList<SwordAttribute> list = sword.getAllAttributes(); 
        Random random = new Random();

        for (int i = 0; i < sword.getCredit(); i++) {
            int randomIndex = random.nextInt(list.size());
            SwordAttribute attribute = list.get(randomIndex);
            int upgradePrice = attribute.canUpgrade(sword.getCredit());

            if (upgradePrice != 0) {
                attribute.upgrade();
                sword.setCredit(sword.getCredit() - upgradePrice);
                System.out.println(sword.getCredit());
            }
        }
    }
    

    public static String buildGiveCommand(Sword sword) {
		return String.format("give @a %s%s 1", sword.getType(), buildTag(sword)); 
	} 

	public static String buildTag(Sword sword) {
        return String.format("{display:{%s,%s}%s%s,CustomID:%d}",
            getNameString(sword), getLoreString(sword), getEnchantmentsString(sword), 
            getModifierString(sword), sword.getId());
	}

    public static String buildDealDamageString(Sword sword) {
        return String.format("execute as @s[nbt={SelectedItem:{tag:{CustomID:%d}}}] run execute as " +
        "@e[distance=..5,nbt={HurtTime:10s},tag=!am_the_attacker] run function aidp:swords/sword%d", sword.getId(), sword.getId());
    }

    public static String buildAttackFunction(Sword sword) {
        return String.format("%s\n%s\n%s\n",
            getAttackEffectString(sword), getAttackParticleString(sword), getHeldParticleString(sword));
    }

    

    //////////////////////////////////////////////////
    // ATTRIBUTE RETRIEVAL
    //////////////////////////////////////////////////
    public static String getNameString(Sword sword) {
        return sword.getName().toString();
    }

    public static String getLoreString(Sword sword) {
        return sword.getLore().toString();
    }

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
    public static void addAttributes(Sword sword, ArrayList<String> list, Function<String, SwordAttribute> attributeFactory) {
        for (String item : list) {
            SwordAttribute attribute = attributeFactory.apply(item);
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