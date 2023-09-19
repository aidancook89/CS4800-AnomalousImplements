package gradle_test;

import java.util.ArrayList;

public class Item {
    public String tag;
    public String type;

    public int customModelData;

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

    public ArrayList<String> enchantmentsList = new ArrayList<String>();
    public ArrayList<String> attributeModifiersList = new ArrayList<String>();
    public ArrayList<String> potionEffectList = new ArrayList<String>();

    public Item(String type, int customModelData) {
		this.type = type;
		this.customModelData = customModelData;
    }

	public void updateName(String name, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.nameText = name;
		nameColor = color;
		nameBold = bold;
		nameItalic = italic;
		nameUnderlined = underlined;
		nameStrikethrough = strikethrough;
		nameObfuscated = obfuscated;
	}

	public void updateLore(String lore, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.loreText = lore;
		loreColor = color;
		loreBold = bold;
		loreItalic = italic;
		loreUnderlined = underlined;
		loreStrikethrough = strikethrough;
		loreObfuscated = obfuscated;
	}

	public void buildTag() {
        tag = "{display:{";

        String nameAppend = String.format("Name:['{\"text\":\"%s\",\"color\":\"%s\"", nameText, nameColor);
        if (nameBold) nameAppend += String.format(",\"bold\":%b", nameBold);
        if (nameItalic) nameAppend += String.format(",\"italic\":%b", nameItalic);
        if (nameUnderlined) nameAppend += String.format(",\"underline\":%b", nameUnderlined);
        if (nameStrikethrough) nameAppend += String.format(",\"strikethrough\":%b", nameStrikethrough);
        if (nameObfuscated) nameAppend += String.format(",\"obfuscated\":%b", nameObfuscated);
        nameAppend += "}']";
        tag += nameAppend + ",";

        String loreAppend = String.format("Lore:['{\"text\":\"%s\",\"color\":\"%s\"", loreText, loreColor);
        if (loreBold) loreAppend += String.format(",\"bold\":%b", loreBold);
        if (loreItalic) loreAppend += String.format(",\"italic\":%b", loreItalic);
        if (loreUnderlined) loreAppend += String.format(",\"underline\":%b", loreUnderlined);
        if (loreStrikethrough) loreAppend += String.format(",\"strikethrough\":%b", loreStrikethrough);
        if (loreObfuscated) loreAppend += String.format(",\"obfuscated\":%b", loreObfuscated);
        loreAppend += "}']";
        tag += loreAppend + "},";

        if (enchantmentsList.size() > 0) {
            tag += String.format("Enchantments:%s,", listToString(enchantmentsList));
        }

        if (attributeModifiersList.size() > 0) {
            tag += String.format("AttributeModifiers:%s,", listToString(attributeModifiersList));
        }

        tag += String.format("CustomModelData:%d,", customModelData);
        tag += "custom_item:1" + "}";
	}

	public String getGiveCommand() {
		buildTag();
		return "give @a " + type + tag + " 1"; 
	} 

	public void addEnchantment(String enchantment, int level) {
		enchantmentsList.add(String.format(
			"{id:\"minecraft:%s\",lvl:%ds}", 
			enchantment, level));
	}

	public void addPotionEffect(String effect, int amount, boolean showParticles) {
        String potionCommand = String.format(
            "execute as @a[nbt={SelectedItem:{tag:{CustomModelData:%d}}}] run effect give @s minecraft:%s 1 %d %b",
            customModelData, effect, amount, showParticles);
        //execute as @a[nbt={Inventory:[{id:"minecraft:leather_leggings",Slot:101b}]}] run effect give @s minecraft:jump_boost 1 3 true
        potionEffectList.add(potionCommand);
	}

    public String getPotionEffectString() {
        String output = String.format("############ %s:%s:%d ############\n", 
            type, nameText, customModelData);
        for (int i = 0; i < potionEffectList.size(); i++) {
            output += potionEffectList.get(i) + "\n";
        }
        return output;
    }
    /*
    * off hand: -106
    * boots: 100
    * leggings: 101
    * chestplate: 102
    * helmet: 103
    */

    
	public void addAttributeModifier(String modifier, int amount) {}

	public static String listToString(ArrayList<String> arrayList) {
		String output = "[";
		for (String element : arrayList) {
			output += element + ",";	
		}
		if (output.length() > 1) output = output.substring(0, output.length()-1) + "]";
		else output += "]";
		return output;
	}

    /* 
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
}

