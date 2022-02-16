package me.melaniesingleton.rightclickharvest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Random;

public final class RightClickHarvest extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        System.out.println("RightClickHarvest has (probably) loaded correctly!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.getBlock().getDrops();

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        // if fully grown block implementing Ageable is right clock, reset its age to 0
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            Player player = e.getPlayer();
            BlockState state = e.getClickedBlock().getState();
            BlockData data = state.getBlockData();

            if (data instanceof Ageable) {
                if (((Ageable) data).getMaximumAge() == ((Ageable) data).getAge()) {

                    breakCrop(player, state, data, e);

                    int fortuneLevel = player.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

                    switch (data.getMaterial()) {
                        case WHEAT -> {
                            LootHandler.WHEAT_RC.rollCounts[1] = 2 + fortuneLevel;
                            LootHandler.WHEAT_RC.GiveLootPool(player);
                        }
                        case CARROTS -> {
                            LootHandler.CARROT_RC.rollCounts[1] = 1 + fortuneLevel;
                            LootHandler.CARROT_RC.GiveLootPool(player);
                        }
                        case POTATOES -> {
                            LootHandler.POTATO_RC.rollCounts[1] = 1 + fortuneLevel;
                            LootHandler.POTATO_RC.GiveLootPool(player);
                        }
                        case BEETROOTS -> {
                            LootHandler.BEETROOT_RC.rollCounts[1] = 2 + fortuneLevel;
                            LootHandler.BEETROOT_RC.GiveLootPool(player);
                        }
                    }
                }
            }
        }
    }

    // method that compartmentalizes other methods and resets crop age to zero.
    private void breakCrop(Player player, BlockState state, BlockData data, PlayerInteractEvent e) {
        spawnCropParticles(player, data, e);

        ((Ageable) data).setAge(0);
        state.setBlockData(data);
        state.update();

        playCropPickupSound(player);
    }

    // helper method that makes code more readable by handling particle spawning
    private void spawnCropParticles(Player player, BlockData data, PlayerInteractEvent e) {
        player.getWorld().spawnParticle(Particle.BLOCK_DUST,
                e.getClickedBlock().getLocation().add(0.5,0.5,0.5), 7, 0.1, 0.1, 0.1, 0.1, data);
    }

    // helper method that plays crop pickup sounds, mimicking item pickup sounds from vanilla
    private void playCropPickupSound(Player player) {
        Random random = new Random();
        float pitch = 1 + random.nextFloat();
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 0.20f, pitch);
    }

}

