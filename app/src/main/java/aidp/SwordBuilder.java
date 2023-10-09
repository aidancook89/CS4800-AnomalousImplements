package aidp;

import java.util.ArrayList;
import java.nio.file.Path;
import java.util.Random;

public class SwordBuilder {

    private static String particleList = "ambient_entity_effect,angry_villager,ash,bubble,bubble_pop,campfire_cosy_smoke," 
        + "campfire_signal_smoke,cherry_leaves,cloud,composter,crimson_spore,dolphin,dragon_breath,effect,egg_crack,elder_guardian,"
        + "electric_spark,enchant,enchanted_hit,end_rod,entity_effect,firework,fishing,flame,flash,glow,glow_squid_ink,happy_villager,"
        + "heart,instant_effect,item_slime,item_snowball,large_smoke,lava,mycelium,nautilus,note,poof,portal,rain,reverse_portal,"
        + "scrape,sculk_charge_pop,sculk_soul,smoke,sneeze,snowflake,sonic_boom,soul,soul_fire_flame,spit,splash,spore_blossom_air,"
        + "squid_ink,uderwater,warped_spore,wax_off,wax_on,white_ash,witch";

    private static String soundList = "block.amethyst_block.place, entity.arrow.shoot,"
        + "entity.firework_rocket.launch, block.piston.extend, block.bamboo.hit,"
        + "block.basalt.hit, block.beehive.drip, block.calcite.break, block.chain.fall,"
        + "firework_rocket.blast_far, block.ladder.step";

    private static String effectList = "speed, slowness, haste, mining_fatigue, strength, instant_health, instant_damage, jump_boost,"
        + "nausea, regeneration, resistances, fire_resistance, water_breathing, invisibility, blindness, night_vision, hunger, weakness,"
        + "poison, wither, health_boost, absorption, saturation, glowing, levitation, slow_falling, conduit_power, dolphins_grace,"
        + "bad_omen, hero_of_the_village";

    private static String requestJson = String.format("{"
    + "name: <string>," 
    + "color: <hexcode>," 
    + "lore: <string>"
    + "enchantments: [<binding_curse,sharpness,smite,bane_of_arthropods,knockback,fire_aspect,looting,sweeping,unbreaking,mending,vanishing_curse>]"
    + "modifiers: [<max_health,knockback_resistantce,movement_speed,armor,armor_touchness,luck,max_absorption>]"
    + "wielder_effects: [<speed,slowness,jump_boost>]"
    + "victim_effects: [<speed,slowness,jump_boost,levitation>]"
    + "particles: [<%s>]"
    + "sounds: [<%s>]"
    + "} ",
    particleList, soundList);
    
    private static String restrictions = ""
    + "enchantments: pick 2,"
    + "modifiers: pick 2,"
    + "wielder_effects: pick 2," 
    + "victim_effects: pick 2," 
    + "particles: pick 2," 
    + "effects: pick 2,"
    + "sounds: pick 1";


    public static Sword newSword(int id, int rarity, String theme) {

        /* 
        Request request = RequestHandler.makeRequest(
            "Provide a JSON in the following format: " + requestJson + restrictions,
            String.format("An interesting sword with themes: %s", theme), 
            0.9
        );        
        System.out.println(request.getContentString());
        */

        
        String fake = "{" 
        + "\"name\": \"Breakfast Sword\","
        + "\"color\": \"#FFFF00\","
        + "\"lore\": \"This sword fuels your hunger for victory! This is a test that will be useful for running the codebase multiple times. Extended lore will display on newlines, hopefully.\","
        + "\"enchantments\": [\"unbreaking\", \"looting\"],"
        + "\"modifiers\": [\"movement_speed\"],"
        + "\"wielder_effects\": [\"jump_boost\"],"
        + "\"victim_effects\": [\"levitation\"],"
        + "\"particles\": [\"cloud\", \"bubble\"],"
        + "\"sounds\": [\"entity.firework_rocket.blast_far\"]"
        + "}";
        Request request = new Request(fake);

        // Create new sword and add attributes
        Sword sword = new Sword(id, rarity);
        setName(sword, new Name(request.getAsString("name"),request.getAsString("color")));
        setLore(sword, new Lore(request.getAsString("lore"), rarity));
        setType(sword, new Type(0));
        addModifier(sword, new AttackDamage(3));
        addModifier(sword, new AttackSpeed(-3));

        addEnchantments(sword, request.getAsArrayList("enchantments"));
        addModifiers(sword, request.getAsArrayList("modifiers"));
        addWielderEffects(sword, request.getAsArrayList("wielder_effects"));
        addVictimEffects(sword, request.getAsArrayList("victim_effects"));
        addParticles(sword, request.getAsArrayList("particles"));
        addSounds(sword, request.getAsArrayList("sounds"));
        balanceAttributes(sword);

        // Add item to deal damage
        Structure.writeToLine(App.f_deal_damagemcfunction, buildDealDamageString(sword), 2);
         
        // Create attack function 
        Path attackFunction = Structure.newDir(App.d_swords, String.format("sword%d.mcfunction", sword.getId()), true);
        Structure.writeTo(attackFunction, buildAttackFunction(sword), true);
    
        // Add potion effects for player
        Structure.writeTo(App.f_item_tickmcfunction, getWielderEffectString(sword), true);

        // Add give command on load
        Structure.writeTo(App.f_loadmcfunction, "\n" + buildGiveCommand(sword), true);

        return sword;
    }



    //////////////////////////////////////////////////
    // BALANCE
    //////////////////////////////////////////////////
    public static void balanceAttributes(Sword sword) { 
        // Create a random object for getting indecies
        Random random = new Random();

        // Copy our attribute list
        ArrayList<UpgradeAttribute> list = sword.getUpgradeAttributes(); 

        // While our list is not empty (i.e. we can upgrade an attribute)
        while (list.size() > 0) {
            // Get a random attribute and get the price
            int randomIndex = random.nextInt(list.size());
            UpgradeAttribute attribute = list.get(randomIndex);
            int upgradePrice = attribute.canUpgrade(sword.getCredit());

            // If we cannot upgrade the attribute, remove it from our list
            if (upgradePrice == 0) list.remove(randomIndex);

            // If we can upgrade the attribute, upgrade an update our credit
            if (upgradePrice != 0) {
                attribute.upgrade();
                sword.setCredit(sword.getCredit() - upgradePrice);
                
                /* 
                System.out.println("UPGRADED: " + attribute);
                System.out.println("upgradePrice: " + upgradePrice);
                System.out.println("Credit: " + sword.getCredit());
                System.out.println("List size: " + list.size());
                System.out.println();
                */

                // If our upgrade gave use credit (the price of the upgrade was negative)
                // Add all attributes back into the list (chance that we may be able to upgrade some of them)
                if (upgradePrice < 0) list = sword.getUpgradeAttributes();
            }
        }
    }

    

    //////////////////////////////////////////////////
    // BUILDERS
    //////////////////////////////////////////////////
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
            getVictimEffectString(sword), getParticleString(sword), getSoundString(sword));
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

    public static String getWielderEffectString(Sword sword) {
        if (sword.getWielderEffects().size() == 0) return "";
        String output = String.format("############ %s (%s), id: %d ############\n", 
            sword.getName(), sword.getType(), sword.getId());
        for (Effect e : sword.getWielderEffects()) {
            output += String.format("execute as @a[nbt={SelectedItem:{tag:{CustomID:%d}}}] run effect give @s ", 
                sword.getId()) + e + "\n";
        }
        return output; 
    }

    public static String getVictimEffectString(Sword sword) {
        String output = "";
        for (Effect e : sword.getVictimEffects()) {
            output += "effect give @s " + e + "\n";
        }
        return output;
    }

    public static String getParticleString(Sword sword) {
        String output = "";
        for (Particle p : sword.getParticles()) {
            output += p + "\n";
        }
        return output;
    }

    public static String getSoundString(Sword sword) {
        String output = "";
        for (Sound s : sword.getSounds()) {
            output += s + "\n";
        }
        return output;
    }

        

    //////////////////////////////////////////////////
    // ADDING ATTRIBUTES
    //////////////////////////////////////////////////
    public static void setName(Sword sword, Name name) { sword.setName(name); }
    public static void setLore(Sword sword, Lore lore) { sword.setLore(lore); }
    public static void setType(Sword sword, Type type) { 
        sword.setType(type); 
        sword.getUpgradeAttributes().add(type);
    }
    public static void setCredit(Sword sword, int credit) { sword.setCredit(credit); }
    public static void addModifier(Sword sword, Modifier modifier) { 
        sword.getModifiers().add(modifier); 
        sword.getUpgradeAttributes().add(modifier);
    }

    public static void addEnchantments(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Enchantment attribute = new Enchantment(item);
            sword.addEnchantment(attribute);
            sword.addUpgradeAttribute(attribute);
        }
    }

    public static void addModifiers(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Modifier attribute = new Modifier(item);
            sword.addModifier(attribute);
            sword.addUpgradeAttribute(attribute);
        }
    }

    public static void addVictimEffects(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            VictimEffect attribute = new VictimEffect(item);
            sword.addVictimEffect(attribute);
            sword.addUpgradeAttribute(attribute);
        }
    }

    public static void addWielderEffects(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            WielderEffect attribute = new WielderEffect(item);
            sword.addWielderEffect(attribute);
            sword.addUpgradeAttribute(attribute);
        }
    }

    public static void addParticles(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Particle attribute = new Particle(item);
            sword.addParticle(attribute);
        }
    }

    public static void addSounds(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            Sound attribute = new Sound(item);
            sword.addSound(attribute);
        }
    }
}