package me.melaniesingleton.rightclickharvest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public enum LootHandler {

    /* Individual loot tables can be declared below. Use the following format:

        ENUMNAME("ENUMNAME",
            new Material[] {new ItemStack(Material.SOMETHING), new ItemStack(Material.SOMETHINGELSE), ...},
            new Double[] {1.0d, 0.5d, ...}),
            new int[] {1,2}),

        when adding new enums remember to only have a semicolon after the final one.
    */

    WHEAT_RC("WHEAT_RC",
            new ItemStack[] {
                    new ItemStack(Material.WHEAT),
                    new ItemStack(Material.WHEAT_SEEDS)},
            new double[] {1.0d, 0.57d},
            new int[] {1,2}),

    BEETROOT_RC("BEETROOT_RC",
             new ItemStack[] {
                    new ItemStack(Material.BEETROOT),
                    new ItemStack(Material.BEETROOT_SEEDS)},
            new double[] {1.0d, 0.57d},
            new int[] {1,2}),

    CARROT_RC("CARROT_RC",
            new ItemStack[] {
                    new ItemStack(Material.CARROT),
                    new ItemStack(Material.CARROT)},
            new double[] {1.0d, 0.57d},
            new int[] {1,1}),

    POTATO_RC("POTATO_RC",
            new ItemStack[] {
                    new ItemStack(Material.POTATO),
                    new ItemStack(Material.POTATO)},
            new double[] {1.0d, 0.57d},
            new int[] {1,1});



    private final String name;
    public final ItemStack[] itemStacks;
    public final double[] odds;
    public final int[] rollCounts;

    LootHandler(String name, ItemStack[] itemStacks, double[] odds, int[] rollCounts) {
        this.name = name;
        this.itemStacks = itemStacks;
        this.odds = odds;
        this.rollCounts = rollCounts;
    }

    // Tries to give the player loot according to an enum above. If they don't have space
    // in their inventory the extra loot is instead dropped at players feet.
    public void giveLootPool(Player player) {

        int itemStackPointer = 0;

        for (ItemStack itemStack : itemStacks) {

            // Helps with debugging incorrectly set loot pools.
            if (itemStacks.length != odds.length || itemStacks.length != rollCounts.length) {
                Bukkit.getLogger().info("[RightClickHarvest] ERROR: length of one ore more supporting array for " + name + " does not match itemStacks length." +
                        " Ensure each itemStack has a corresponding chance of appearing.");
                return;
            }

            // Actual implementation goes below
            int rollCounter = 0;

            while (rollCounter < rollCounts[itemStackPointer]) {
                double randomNumber = Math.random();

                if (randomNumber <= odds[itemStackPointer]) {
                    forcedGiveItem(player, itemStack);
                }
                rollCounter += 1;
            }
            itemStackPointer += 1;
        }
    }

    // Spawns loot in the world at the given location as dropped items.
    public void spawnLoot(Location location) {
        World world = location.getWorld();

        int itemStackPointer = 0;

        for (ItemStack itemStack : itemStacks) {

            // Helps with debugging incorrectly set loot pools.
            if (itemStacks.length != odds.length || itemStacks.length != rollCounts.length) {
                Bukkit.getLogger().info("[RightClickHarvest] ERROR: length of one ore more supporting array for " + name + " does not match itemStacks length." +
                        " Ensure each itemStack has a corresponding chance of appearing.");
                return;
            }

            // Actual implementation goes below
            int rollCounter = 0;

            while (rollCounter < rollCounts[itemStackPointer]) {
                double randomNumber = Math.random();

                if (randomNumber <= odds[itemStackPointer]) {
                    world.dropItemNaturally(location, itemStack);
                }
                rollCounter += 1;
            }
            itemStackPointer += 1;
        }
    }

    // gives the player the provided ItemStack. If unable to, drops the item on the ground.
    // Player represents the player the item will be given to, and ItemStack is the item to be given.
    public void forcedGiveItem(Player player, ItemStack itemStack) {
        HashMap<Integer, ItemStack> lootAttempt = player.getInventory().addItem(itemStack);

        if (!lootAttempt.isEmpty()) {
            player.getInventory().addItem(itemStack);

            ItemStack excessLoot = lootAttempt.get(0);
            World world = player.getWorld();

            world.dropItemNaturally((player.getLocation()), excessLoot);
        }
    }

}
