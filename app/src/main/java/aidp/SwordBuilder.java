package aidp;

import java.util.ArrayList;
import java.nio.file.Path;
import java.util.Random;

public class SwordBuilder {

    public static Sword newSword(SwordJson sj) {
        // Create new sword and add attributes
        Sword sword = new Sword(sj.id, sj.rarity);
        setType(sword, new Type(0));
        addModifier(sword, new AttackDamage(3));
        addModifier(sword, new AttackSpeed(-3));
        addEnchantments(sword, sj.enchantments);
        addModifiers(sword, sj.modifiers);
        addWielderEffects(sword, sj.wielder_effects);
        addVictimEffects(sword, sj.victim_effects);
        addParticles(sword, sj.particles);
        addSounds(sword, sj.sounds);
        balanceAttributes(sword);
        
        setName(sword, new Name(sj.name,sj.color));
        setLore(sword, new Lore(sj.lore, sj.rarity, sword.getVictimEffects()));

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