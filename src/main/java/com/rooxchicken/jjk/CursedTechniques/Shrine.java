package com.rooxchicken.jjk.CursedTechniques;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Math;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.Tasks.Dismantle;
import com.rooxchicken.jjk.Tasks.MalevolentShrine;
import com.rooxchicken.jjk.Tasks.Slash;

public class Shrine implements Listener
{
    private Plugin plugin;

    public Shrine(Plugin _plugin)
    {
        plugin = _plugin;
    }

    public void slash(Player player, Location pos, boolean random, boolean dealDamage, int damage, boolean playSound, float volume)
    {
        JJKPlugin.tasks.add(new Slash(plugin, player, pos, random, dealDamage, damage, playSound, volume));
    }

    // @EventHandler
    // public void bypassShield()

    @EventHandler
    public void preventDamage(EntityDamageEvent event)
    {
        if(((LivingEntity)event.getEntity()).getMaximumNoDamageTicks() == 0)
        {
            event.setCancelled(event.getDamage() != 4);
            return;
        }
    }

    @EventHandler
    public void cleave(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
            
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if(item == null || !item.hasItemMeta())
            return;

        if(item.getItemMeta().getDisplayName().equals("§7§l§oShrine"))
        {
            if(player.isSneaking())
                shrine(player, item, event);
            if(!JJKPlugin.useCursedEnergy(player, 30))
                return;

            slash(player, player.getLocation(), false, true, 9, true, 1);
        }
    }
    
    @EventHandler
    public void dismantle(EntityDamageByEntityEvent event)
    {
        if(((LivingEntity)event.getEntity()).getMaximumNoDamageTicks() == 0)
        {
            event.setCancelled(true);
            return;
        }

        if(!(event.getDamager() instanceof Player))
            return;
        
        Player player = (Player)event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null || !item.hasItemMeta())
            return;

        if(item.getItemMeta().getDisplayName().equals("§7§l§oShrine"))
        {
            Entity target = event.getEntity();
            if(target == null)
                return;

            if(!JJKPlugin.useCursedEnergy(player, 300))
                return;

            JJKPlugin.tasks.add(new Dismantle(plugin, this, player, target.getLocation(), (LivingEntity)target));
        }
    }


    public void shrine(Player player, ItemStack item, Event event)
    {
        if(!JJKPlugin.useCursedEnergy(player, 1800))
            return;

        JJKPlugin.tasks.add(new MalevolentShrine(plugin, this, player, player.getLocation().add(0, 10, 0)));
    }
}
