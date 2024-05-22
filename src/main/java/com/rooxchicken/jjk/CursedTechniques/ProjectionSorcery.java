package com.rooxchicken.jjk.CursedTechniques;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.Data.BoogieWoogieTargets;
import com.rooxchicken.jjk.Tasks.InfinityBarrier;
import com.rooxchicken.jjk.Tasks.PSFreeze;
import com.rooxchicken.jjk.Tasks.Task;

public class ProjectionSorcery implements Listener
{
    private Plugin plugin;
    public static NamespacedKey freezeTimeKey;
    public static HashMap<Entity, Location> playerFrozenMap;

    public ProjectionSorcery(Plugin _plugin)
    {
        plugin = _plugin;
        freezeTimeKey = new NamespacedKey(plugin, "psFreezeTime");

        playerFrozenMap = new HashMap<Entity, Location>();
        JJKPlugin.tasks.add(new PSFreeze(plugin));
    }

    @EventHandler
    public void activateProjection(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if(item == null || !item.hasItemMeta())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§8§l§oProjection Sorcery"))
        {
            if(!JJKPlugin.useCursedEnergy(player, 80))
                return;

            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 20, 5);
            player.addPotionEffect(speed);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
        }
    }

    @EventHandler
    public void freezePlayer(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player)event.getDamager();
        Entity player = event.getEntity();
        ItemStack item = damager.getInventory().getItemInMainHand();
        
        if(item == null || !item.hasItemMeta())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§8§l§oProjection Sorcery"))
        {
            if(!JJKPlugin.useCursedEnergy(damager, 120))
                return;

            player.getPersistentDataContainer().set(freezeTimeKey, PersistentDataType.INTEGER, 20);

            playerFrozenMap.put(player, player.getLocation().clone());
            //player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
        }
    }
}
