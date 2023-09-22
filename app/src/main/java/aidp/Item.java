package aidp;

import java.util.ArrayList;

public class Item {
    public String tag;
    public String type;

    public String dealDamageString = "";
    public int customID = 0;

    public String nameText = "default";
    public String nameColor = "white"; 
    public boolean nameBold = false;
    public boolean nameItalic = false;
    public boolean nameUnderlined = false;
    public boolean nameStrikethrough = false;
    public boolean nameObfuscated = false;

    public String loreText = "default";
    public String loreColor = "white";
    public boolean loreBold = false;
    public boolean loreItalic = false;
    public boolean loreUnderlined = false;
    public boolean loreStrikethrough = false;
    public boolean loreObfuscated = false;

    public String playerParticle = "";
    public String entityParticle = "";

    public ArrayList<String> enchantmentsList = new ArrayList<String>();
    public ArrayList<String> attributeModifiersList = new ArrayList<String>();
    public ArrayList<String> playerPotionEffectList = new ArrayList<String>();
    public ArrayList<String> entityPotionEffectList = new ArrayList<String>();

    public Item(String type, int customID) {
		this.type = type;
		this.customID = customID;
    }

	public String getGiveCommand() {
		buildTag();
		return "give @a " + type + tag + " 1"; 
	} 

	public void buildTag() {
        tag = "{display:{";
        tag += getNameString() + ",";
        tag += getLoreString() + "}";
        tag += getEnchantmentsString();
        tag += getAttributesString();
        tag += String.format(",CustomID:%d", customID);
        tag += "}";
	}
    
        

    ////////////////////////////////
    // NAME
    //////////////////////////////// 
    public void setName(String name, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.nameText = name;
		nameColor = color;
		nameBold = bold;
		nameItalic = italic;
		nameUnderlined = underlined;
		nameStrikethrough = strikethrough;
		nameObfuscated = obfuscated;
	}

    public String getNameString() {
        String output = String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\"", nameText, nameColor);
        if (nameBold) output += String.format(",\"bold\":%b", nameBold);
        if (nameItalic) output += String.format(",\"italic\":%b", nameItalic);
        if (nameUnderlined) output += String.format(",\"underline\":%b", nameUnderlined);
        if (nameStrikethrough) output += String.format(",\"strikethrough\":%b", nameStrikethrough);
        if (nameObfuscated) output += String.format(",\"obfuscated\":%b", nameObfuscated);
        output += "}'";
        return output;
    }

	

    ////////////////////////////////
    // LORE
    //////////////////////////////// 
    public void setLore(String lore, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.loreText = lore;
		loreColor = color;
		loreBold = bold;
		loreItalic = italic;
		loreUnderlined = underlined;
		loreStrikethrough = strikethrough;
		loreObfuscated = obfuscated;
	}

    public String getLoreString() {
        String output = String.format("Lore:['{\"text\":\"%s\",\"color\":\"%s\"", loreText, loreColor);
        if (loreBold) output += String.format(",\"bold\":%b", loreBold);
        if (loreItalic) output += String.format(",\"italic\":%b", loreItalic);
        if (loreUnderlined) output += String.format(",\"underline\":%b", loreUnderlined);
        if (loreStrikethrough) output += String.format(",\"strikethrough\":%b", loreStrikethrough);
        if (loreObfuscated) output += String.format(",\"obfuscated\":%b", loreObfuscated);
        output += "}']";
        return output;
    }



    ////////////////////////////////
    // ATTACK
    //////////////////////////////// 
    public String getDealDamageString() {
        return String.format("execute as @s[nbt={SelectedItem:{tag:{CustomID:%d}}}] run execute as @e[distance=..5,nbt={HurtTime:10s},tag=!am_the_attacker] run function aidp:swords/%s", customID, getAttackFunctionName());
    }

    public String getAttackFunctionName() {
        return String.format("sword%s", customID);
    }

    public String getAttackFunctionString() {
        return String.format("%s\n%s\n%s\n",
            getEntityPotionEffectString(),
            getEntityParticleString(),
            getPlayerParticleString());
    }


    ////////////////////////////////
    // ENCHANTMENTS
    //////////////////////////////// 
    public void addEnchantment(String enchantment, int level) {
		enchantmentsList.add(String.format(
			"{id:\"minecraft:%s\",lvl:%ds}", 
			enchantment, level));
	}

    public String getEnchantmentsString() {
        String output = "";
        if (enchantmentsList.size() > 0) {
            output = String.format(",Enchantments:%s", listToString(enchantmentsList));
        }
        return output;
    }



    ////////////////////////////////
    // ATTRIBUTES
    //////////////////////////////// 
    public void addAttributeModifier(String modifier, int amount) {}
 
    public String getAttributesString() {
        String output = "";
        if (attributeModifiersList.size() > 0) {
            output = String.format(",AttributeModifiers:%s", listToString(attributeModifiersList));
        }
        return output;
    }

	

    ////////////////////////////////
    // HELD POTION EFFECTS
    //////////////////////////////// 
	public void addPlayerPotionEffect(String effect, int amount, boolean hideParticles) {
        String potionCommand = String.format(
            "execute as @a[nbt={SelectedItem:{tag:{CustomID:%d}}}] run effect give @s minecraft:%s 1 %d %b",
            customID, effect, amount, hideParticles);
        playerPotionEffectList.add(potionCommand);
	}

    public String getPlayerPotionEffectString() {
        String output = String.format("############ %s:%s:%d ############\n", 
            type, nameText, customID);
        for (int i = 0; i < playerPotionEffectList.size(); i++) {
            output += playerPotionEffectList.get(i) + "\n";
        }
        return output;
    }
    


    ////////////////////////////////
    // ATTACK POTION EFFECTS
    //////////////////////////////// 
	public void addEntityPotionEffect(String effect, int amount, boolean hideParticles) {
        String command = String.format("effect give @s %s 1 %d %b", effect, amount, hideParticles);
        entityPotionEffectList.add(command);
	}

    public String getEntityPotionEffectString() {
        String output = String.format("");
        for (int i = 0; i < entityPotionEffectList.size(); i++) {
            output += entityPotionEffectList.get(i) + "\n";
        }
        return output;
    }



	////////////////////////////////
    // PARTICLES
    //////////////////////////////// 
    public void setPlayerParticle(String particle) {
        playerParticle = particle; 
    }

    public String getPlayerParticleString() {
        return String.format("particle %s ~ ~1 ~ 0 0 0 0.3 20 force", playerParticle);
    }

    public void setEntityParticle(String particle) {
        entityParticle = particle; 
    }

    public String getEntityParticleString() {
        return String.format("execute as @s run particle %s ~ ~1 ~ 0 0 0 0.3 20 force", entityParticle);
    }



	////////////////////////////////
    // HELPER FUNCTIONS
    //////////////////////////////// 
	public static String listToString(ArrayList<String> arrayList) {
		String output = "[";
		for (String element : arrayList) {
			output += element + ",";	
		}
		if (output.length() > 1) output = output.substring(0, output.length()-1) + "]";
		else output += "]";
		return output;
	}
}


/* 
Slots:
* off hand: -106
* boots: 100
* leggings: 101
* chestplate: 102
* helmet: 103

{id:"minecraft:protection",lvl:1s}
{id:"minecraft:fire_protection",lvl:1s}
{id:"minecraft:feather_falling",lvl:1s}
{id:"minecraft:blast_protection",lvl:1s}
{id:"minecraft:projectile_protection",lvl:1s}
{id:"minecraft:respiration",lvl:1s}
{id:"minecraft:aqua_affinity",lvl:1s}
{id:"minecraft:thorns",lvl:1s}
{id:"minecraft:depth_strider",lvl:1s}
{id:"minecraft:frost_walker",lvl:1s}
{id:"minecraft:binding_curse",lvl:1s}
{id:"minecraft:sharpness",lvl:1s}
{id:"minecraft:smite",lvl:1s}
{id:"minecraft:bane_of_arthropods",lvl:1s}
{id:"minecraft:knockback",lvl:1s}
{id:"minecraft:fire_aspect",lvl:1s}
{id:"minecraft:looting",lvl:1s}
{id:"minecraft:sweeping",lvl:1s}
{id:"minecraft:efficiency",lvl:1s}
{id:"minecraft:silk_touch",lvl:1s}
{id:"minecraft:unbreaking",lvl:1s}
{id:"minecraft:fortune",lvl:1s}
{id:"minecraft:power",lvl:1s}
{id:"minecraft:punch",lvl:1s}
{id:"minecraft:flame",lvl:1s}
{id:"minecraft:infinity",lvl:1s}
{id:"minecraft:luck_of_the_sea",lvl:1s}
{id:"minecraft:lure",lvl:1s}
{id:"minecraft:loyalty",lvl:1s}
{id:"minecraft:impaling",lvl:1s}
{id:"minecraft:riptide",lvl:1s}
{id:"minecraft:channeling",lvl:1s}
{id:"minecraft:mending",lvl:1s}
{id:"minecraft:vanishing_curse",lvl:1s}
{id:"minecraft:multishot",lvl:1s}
{id:"minecraft:piercing",lvl:1s}
{id:"minecraft:quick_charge",lvl:1s}
{id:"minecraft:soul_speed",lvl:1s}
{id:"minecraft:swift_sneak",lvl:1s}
    
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