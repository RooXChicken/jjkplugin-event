package com.rooxchicken.jjk;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.apache.commons.io.filefilter.CanExecuteFileFilter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.base.Predicate;
import com.rooxchicken.jjk.Commands.GiveItems;
import com.rooxchicken.jjk.Commands.ResetCE;
import com.rooxchicken.jjk.CursedTechniques.BoogieWoogie;
import com.rooxchicken.jjk.CursedTechniques.CursedTools;
import com.rooxchicken.jjk.CursedTechniques.Infinity;
import com.rooxchicken.jjk.CursedTechniques.Inverse;
import com.rooxchicken.jjk.CursedTechniques.ProjectionSorcery;
import com.rooxchicken.jjk.CursedTechniques.Shrine;
import com.rooxchicken.jjk.Tasks.HandleCE;
import com.rooxchicken.jjk.Tasks.HandleDoubleJump;
import com.rooxchicken.jjk.Tasks.Task;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.advancements.critereon.CriterionConditionPlayer.d;
import net.minecraft.world.entity.animal.EntityBee.f;
import oshi.jna.platform.windows.NtDll.PEB;

public class JJKPlugin extends JavaPlugin implements Listener
{
    public static NamespacedKey cursedEnergyKey;
    public static NamespacedKey maxCursedEnergyKey;
    public static int scheduleScale = 4;

    public static ArrayList<Task> tasks;

    public static ProtocolManager protocolManager;
    
    private Shrine shrineHandler;
    private Infinity infinityHandler;

    private BoogieWoogie boogieWoogieHandler;
    private ProjectionSorcery projectionSorceryHandler;
    private Inverse inverseHandler;
    private CursedTools cursedToolsHandler;

    // private ArrayList<String> blocked;

    @Override
    public void onEnable()
    {
        protocolManager = ProtocolLibrary.getProtocolManager();
        
        tasks = new ArrayList<Task>();
        tasks.add(new HandleCE(this));
        tasks.add(new HandleDoubleJump(this));

        cursedEnergyKey = new NamespacedKey(this, "jjk_cursedEnergy");
        maxCursedEnergyKey = new NamespacedKey(this, "jjk_maxCursedEnergy");

        shrineHandler = new Shrine(this);
        infinityHandler = new Infinity(this);

        boogieWoogieHandler = new BoogieWoogie(this);
        projectionSorceryHandler = new ProjectionSorcery(this);
        inverseHandler = new Inverse(this);
        cursedToolsHandler = new CursedTools(this);

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(shrineHandler, this);
        getServer().getPluginManager().registerEvents(infinityHandler, this);

        getServer().getPluginManager().registerEvents(boogieWoogieHandler, this);
        getServer().getPluginManager().registerEvents(projectionSorceryHandler, this);
        getServer().getPluginManager().registerEvents(inverseHandler, this);
        getServer().getPluginManager().registerEvents(cursedToolsHandler, this);

        getServer().getPluginManager().registerEvents((Listener)tasks.get(1), this);
        
        this.getCommand("giveitems").setExecutor(new GiveItems());
        this.getCommand("resetce").setExecutor(new ResetCE());

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                ArrayList<Task> _tasks = new ArrayList<Task>();
                for(Task t : tasks)
                    _tasks.add(t);
                
                ArrayList<Task> toRemove = new ArrayList<Task>();
                for(Task t : _tasks)
                {
                    t.tick();

                    if(t.cancel)
                        toRemove.add(t);
                }

                for(Task t : toRemove)
                    tasks.remove(t);

                infinityHandler.tick();
            }
        }, 0, 1);


        getLogger().info("Adding JJK abilities to Minecraft since 1987 (made by roo)");
    }

    @Override
    public void onDisable()
    {
        getServer().getScoreboardManager().getMainScoreboard().getTeam("BoogieWoogieSelect").unregister();
    }

    @EventHandler
    public void checkHasCE(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(cursedEnergyKey, PersistentDataType.INTEGER))
            data.set(cursedEnergyKey, PersistentDataType.INTEGER, 200);
        
        if(!data.has(maxCursedEnergyKey, PersistentDataType.INTEGER))
            data.set(maxCursedEnergyKey, PersistentDataType.INTEGER, 200);
    }

    public static boolean useCursedEnergy(Player player, int amount)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if(!data.has(cursedEnergyKey, PersistentDataType.INTEGER))
            data.set(cursedEnergyKey, PersistentDataType.INTEGER, 200);
        
        if(!data.has(maxCursedEnergyKey, PersistentDataType.INTEGER))
            data.set(maxCursedEnergyKey, PersistentDataType.INTEGER, 200);
        
        int ce = data.get(cursedEnergyKey, PersistentDataType.INTEGER);
        ce -= amount;
        if(ce < 0)
            return false;
        
        data.set(cursedEnergyKey, PersistentDataType.INTEGER, ce);
        return true;
    }


    public static Entity getTarget(Player player, int range)
    {
        Predicate<Entity> p = new Predicate<Entity>() {

            @Override
            public boolean apply(Entity input)
            {
                return(input != player);
            }
            
        };
        RayTraceResult ray = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(), range, FluidCollisionMode.NEVER, true, 0.2, p);
        
        if(ray != null)
            return ray.getHitEntity();
        else
            return null;

    }

    public static Block getBlock(Player player, int range)
    {
        Predicate<Entity> p = new Predicate<Entity>() {

            @Override
            public boolean apply(Entity input)
            {
                return(input != player);
            }
            
        };
        RayTraceResult ray = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(), range, FluidCollisionMode.NEVER, true, 0.2, p);
        
        if(ray != null)
            return ray.getHitBlock();
        else
            return null;
    }

    public static Object[] getNearbyEntities(Location where, int range)
    {
        return where.getWorld().getNearbyEntities(where, range, range, range).toArray();
    }

    public static double ClampD(double v, double min, double max)
    {
        if(v < min)
            return min;
        else if(v > max)
            return max;
        else
            return v;
    }
}
