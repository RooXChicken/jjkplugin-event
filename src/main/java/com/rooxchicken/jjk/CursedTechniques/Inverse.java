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

public class Inverse implements Listener
{
    private Plugin plugin;
    private ArrayList<Player> inversed;

    public Inverse(Plugin _plugin)
    {
        plugin = _plugin;
        inversed = new ArrayList<Player>();
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
        
        if(item.getItemMeta().getDisplayName().equals("§c§l§oInverse"))
        {
            if(inversed.contains(player))
            {
                inversed.remove(player);
                return;
            }

            if(!JJKPlugin.useCursedEnergy(player, 10))
                return;

            inversed.add(player);
        }
    }

    @EventHandler
    public void cancelDamage(EntityDamageEvent event)
    {
        if(!(event.getEntity() instanceof Player))
            return;

        if(useIfInverse((Player)event.getEntity(), event.getDamage()))
        {
            event.setDamage(JJKPlugin.ClampD(18-event.getDamage(), 1, 999));
        }
    }

    private boolean useIfInverse(Player player, double damage)
    {
        if(inversed.contains(player))
        {
            return (JJKPlugin.useCursedEnergy(player, 20));
        }
        else
            return false;
    }
}
