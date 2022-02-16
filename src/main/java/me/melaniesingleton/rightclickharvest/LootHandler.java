package me.melaniesingleton.rightclickharvest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public enum LootHandler {

    //ihavenoideahwatimdoingplzhelpme

    /* Individual loot tables can be declared below. Use the following format:

        ENUMNAME("enumName",
            new Material[] {new ItemStack(Material.SOMETHING), new ItemStack(Material.SOMETHINGELSE), ...},
            new Double[] {1.0d, 0.5d, ...});
            new int[] {1,2}),

        when adding new enums remember to only have a semicolon after the final one.
    */

    WHEAT_RC("wheat_RC",
            new ItemStack[] {
                    new ItemStack(Material.WHEAT),
                    new ItemStack(Material.WHEAT_SEEDS)},
            new Double[] {1.0d, 0.57d},
            new int[] {1,2}),

    BEETROOT_RC("beetroot_RC",
             new ItemStack[] {
                    new ItemStack(Material.BEETROOT),
                    new ItemStack(Material.BEETROOT_SEEDS)},
            new Double[] {1.0d, 0.57d},
            new int[] {1,2}),

    CARROT_RC("carrot_RC",
            new ItemStack[] {
                    new ItemStack(Material.CARROT),
                    new ItemStack(Material.CARROT)},
            new Double[] {1.0d, 0.57d},
            new int[] {1,1}),

    POTATO_RC("potato_RC",
            new ItemStack[] {
                    new ItemStack(Material.POTATO),
                    new ItemStack(Material.POTATO)},
            new Double[] {1.0d, 0.57d},
            new int[] {1,1});



    private String name;
    public ItemStack[] itemStacks;
    public Double[] odds;
    public int[] rollCounts;

    LootHandler(String name, ItemStack[] itemStacks, Double[] odds, int[] rollCounts) {
        this.name = name;
        this.itemStacks = itemStacks;
        this.odds = odds;
        this.rollCounts = rollCounts;
    }

    // Tries to give the player loot according to an enum above. If they don't have space
    // in their inventory the extra loot is instead dropped at players feet.
    public void GiveLootPool(Player player) {

        int itemStackPointer = 0;

        for (ItemStack itemStack : itemStacks) {

            // Helps with debugging incorrectly set loot pools.
            if (itemStacks.length != odds.length) {
                System.out.println("ERROR: odds length for" + name + " does not match itemStacks length." +
                        " Ensure each itemStack has a corresponding chance of appearing.");
                return;
            }

            // Actual implementation goes below
            int rollCounter = 0;

            while (rollCounter < rollCounts[itemStackPointer]) {
                double randomNumber = Math.random();

                if (randomNumber <= odds[itemStackPointer]) {
                    GiveItem(player, itemStack);
                }
                rollCounter += 1;
            }
            itemStackPointer += 1;
        }
    }

    // Spawns loot at the given coordinates
    public void SpawnLoot(int x, int y, int z) {

    }

    // PRIVATE METHODS BELOW

    // gives the player the provided ItemStack. If unable to, drops the item on the ground.
    private void GiveItem(Player player, ItemStack itemStack) {
        HashMap lootAttempt = player.getInventory().addItem(itemStack);

        if (!lootAttempt.isEmpty()) {
            player.getInventory().addItem(itemStack);

            ItemStack excessLoot = (ItemStack) lootAttempt.get(0);
            World world = player.getWorld();

            world.dropItemNaturally((player.getLocation()), excessLoot);
        }
    }

}
