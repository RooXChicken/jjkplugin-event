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
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
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

public class CursedTools implements Listener
{
    private Plugin plugin;

    public CursedTools(Plugin _plugin)
    {
        plugin = _plugin;
    }

    @EventHandler
    public void useTool(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player)event.getDamager();
        ItemStack weapon = damager.getInventory().getItemInMainHand();

        if(weapon == null || !weapon.hasItemMeta())
            return;
        
        if(weapon.getItemMeta().getDisplayName().equals("§0§l§oSoul Split Katana"))
        {
            event.setDamage(DamageModifier.ARMOR, event.getDamage() * 0.05);
        }
    }
}
