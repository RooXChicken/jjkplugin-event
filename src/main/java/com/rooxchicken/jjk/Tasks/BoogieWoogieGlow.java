package com.rooxchicken.jjk.Tasks;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.CursedTechniques.BoogieWoogie;
import com.rooxchicken.jjk.Data.BoogieWoogieTargets;

import net.minecraft.server.network.TextFilter.e;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.level.block.SoundEffectType;

public class BoogieWoogieGlow extends Task
{
    public BoogieWoogieGlow(Plugin _plugin)
    {
        super(_plugin);
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        for(BoogieWoogieTargets targets: BoogieWoogie.targets.values())
        {
            targets.update();
        }
    }
    
}
