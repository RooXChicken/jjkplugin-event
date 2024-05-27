package com.rooxchicken.jjk.Tasks;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.CursedTechniques.Shrine;

import net.minecraft.server.network.TextFilter.e;

public class Dismantle extends Task
{
    private Player player;
    private Location startPos;
    private LivingEntity target;
    private Shrine shrine;

    private int i = 0;

    public Dismantle(Plugin _plugin, Shrine _shrine, Player _player, Location _startPos, LivingEntity _target)
    {
        super(_plugin);

        player = _player;
        shrine = _shrine;
        startPos = _startPos.clone();
        target = _target;

        tickThreshold = 2;

        startPos.add(player.getLocation().clone().subtract(startPos)).setDirection(player.getLocation().getDirection());
    }

    @Override
    public void run()
    {
        target.setMaximumNoDamageTicks(1);
        target.setNoDamageTicks(0);

        target.damage(3);

        exec();

        if(i > 12)
            cancel = true;
    }

    private void exec()
    {
        shrine.slash(player, startPos, false, false, 0, true, 0.6f);
        player.getWorld().playSound(target.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.4f, 1.2f);
        i++;
    }
    
}
