package aidp;

import java.util.ArrayList;
import java.nio.file.Path;
import java.util.Random;

public class SwordBuilder {

    public static Sword newSword(SwordJson sj) {
        // Create new sword and add attributes
        Sword sword = new Sword(sj.id, sj.rarity);
        ArrayList<UpgradeAttribute> upgradeAttributes = new ArrayList<UpgradeAttribute>();

        upgradeAttributes.add(new Type());
        upgradeAttributes.add(new AttackDamage(3));
        upgradeAttributes.add(new AttackSpeed(-3));
        addEnchantments(upgradeAttributes, sj.enchantments);
        addModifiers(upgradeAttributes, sj.modifiers);
        addWielderEffects(upgradeAttributes, sj.wielder_effects);
        addVictimEffects(upgradeAttributes, sj.victim_effects);

        // Generate credit and balance attributes
        int credit = 10 + (int) Math.floor(Math.pow((double) sj.rarity, 1.6) * 15);
        upgradeAttributes = balanceAttributes(upgradeAttributes, credit);
        transferAttributes(sword, upgradeAttributes);

        sword.setName(new Name(sj.name,sj.color));
        sword.setLore(new Lore(sj.lore, sj.rarity, sword.getWielderEffects(), sword.getVictimEffects()));
        addParticles(sword, sj.particles);
        addSounds(sword, sj.sounds);


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
    public static ArrayList<UpgradeAttribute> balanceAttributes(ArrayList<UpgradeAttribute> all, int credit) { 
        // Create a random object for getting indecies
        Random random = new Random();

        // Copy our list
        ArrayList<UpgradeAttribute> list = new ArrayList<>(all);

        // While our list is not empty (i.e. we can upgrade an attribute)
        while (list.size() > 0) {
            // Get a random attribute and get the price
            int randomIndex = random.nextInt(list.size());
            UpgradeAttribute attribute = list.get(randomIndex);
            int upgradePrice = attribute.canUpgrade(credit);

            // If we cannot upgrade the attribute, remove it from our list
            if (upgradePrice <= 0) list.remove(randomIndex);

            // If we can upgrade the attribute, upgrade an update our credit
            if (upgradePrice != 0) {
                //System.out.println("PURCHASE: Attribute: " + attribute.toPretty() + " Price: " + upgradePrice);
                //System.out.println("Currernt Credit: " + credit);
                attribute.upgrade();
                credit -= upgradePrice;
                
                // If our upgrade gave use credit (the price of the upgrade was negative)
                // Add all attributes back into the list (chance that we may be able to upgrade some of them)
                if (upgradePrice < 0) list = new ArrayList<UpgradeAttribute>(all);
            }
        }

        // Remove attributes that were not "purchased"
        list = new ArrayList<UpgradeAttribute>(all);
        for (UpgradeAttribute attribute : list) {
            if (attribute.upgradeLevel == -1) all.remove(attribute);
        }

        // Return the balanced attributes list
        return all;
    }

    public static void transferAttributes(Sword sword, ArrayList<UpgradeAttribute> list) {
        for (UpgradeAttribute attribute : list) {
            if (attribute instanceof Type) sword.setType((Type) attribute);
            else if (attribute instanceof Modifier) sword.addModifier((Modifier) attribute);
            else if (attribute instanceof Enchantment) sword.addEnchantment((Enchantment) attribute);
            else if (attribute instanceof WielderEffect) sword.addWielderEffect((WielderEffect) attribute);
            else if (attribute instanceof VictimEffect) sword.addVictimEffect((VictimEffect) attribute);
        }
    }

    

    //////////////////////////////////////////////////
    // BUILDERS
    //////////////////////////////////////////////////
    public static String buildGiveCommand(Sword sword) {
		return String.format("give @a %s%s 1", sword.getType(), buildTag(sword)); 
	} 

	public static String buildTag(Sword sword) {
        return String.format("{display:{%s,%s}%s%s,RepairCost:%d,CustomID:%d}",
            getNameString(sword), getLoreString(sword), getEnchantmentsString(sword), 
            getModifierString(sword), sword.getRarity() * 3, sword.getId());
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
            sword.getName().text, sword.getType(), sword.getId());
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
    public static void addParticles(Sword sword, ArrayList<String> list) {
        for (String item : list) sword.addParticle(new Particle(item));
    }

    public static void addSounds(Sword sword, ArrayList<String> list) {
        for (String item : list) sword.addSound(new Sound(item));
    }

    public static void addModifiers(ArrayList<UpgradeAttribute> upgrades, ArrayList<String> list) {
        for (String item : list) upgrades.add(new Modifier(item));
    }

    public static void addEnchantments(ArrayList<UpgradeAttribute> upgrades, ArrayList<String> list) {
        for (String item : list) upgrades.add(new Enchantment(item));
    }

    public static void addVictimEffects(ArrayList<UpgradeAttribute> upgrades, ArrayList<String> list) {
        for (String item : list) upgrades.add(new VictimEffect(item));
    }

    public static void addWielderEffects(ArrayList<UpgradeAttribute> upgrades, ArrayList<String> list) {
        for (String item : list) upgrades.add(new WielderEffect(item));
    }
}