package aidp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class LootTableBuilder
 */
public class LootTableBuilder {

    private String table;

    /**
     * buildTable
     * @param s1 - Sword Object to be dropped by Entity
     * @param e1 - Entity Object for which this LootTable is being generated
     * @param newTable - File to be written to
     */
    public void buildTable(Sword s1, Entity e1, Path newTable) {
        table = createTable() + createPools(s1) + createConditions();
        try {
            Files.writeString(newTable, table);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * createTable
     * @return String designating LootTable for an entity
     */
    public String createTable() {
        String output = "{\n\t\"type\":\"minecraft:entity\",\n";
        return output;
    }

    /**
     * createPools
     * @param s1 - Sword Object whose tag will be inserted into the LootTable
     * @return LootTable pool containing s1 tag
     */
    public String createPools(Sword s1) {
        return "\t\"pools\": \t\t[{\n\t\t\t\"rolls\" : 1,\n\t\t\t\"entries\" : [\n\t\t\t\t{"
        + "\n\t\t\t\t\t\"type\" : \"item\", \n\t\t\t\t\t\"name\" : \"" + s1.getType() + "\",\n\t\t\t\t\t\"functions\" : ["
        + "\n\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\"function\" : \"set_nbt\",\n\t\t\t\t\t\t\t\"tag\" : \"" + formatSwordString(s1) 
        + "\"\n\t\t\t\t\t\t}\n\t\t\t\t\t]\n\t\t\t\t\n\t\t\t}\n\t\t\t],";
    }

    /**
     * createConditions
     * @return - LootTable conditions for sword drop
     */
    public String createConditions() {
        return "\n\t\t\t\"conditions\" : [\n\t\t\t\t{\n\t\t\t\t\t\"condition\" : "
        + "\"minecraft:random_chance\",\n\t\t\t\t\t\"chance\" : 1\n\t\t\t\t}\n\t\t\t]\n\t}\n]}";
    }

    /**
     * formatSwordString
     * @param s1 - Sword Object 
     * @return - JSON friendly String containing s1's tag
     */
    private String formatSwordString(Sword s1) {
        String oldTag = SwordBuilder.buildTag(s1);
        String newTag = "";
        for (int i = 0; i < oldTag.length(); i++) {
            if (oldTag.charAt(i) == '"') {
                newTag += "\\" + "\"";
                continue;
            }
            newTag += oldTag.charAt(i);
        }

        return newTag;
    }
}
