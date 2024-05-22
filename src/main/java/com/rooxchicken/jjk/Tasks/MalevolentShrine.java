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

public class MalevolentShrine extends Task
{
    public int tickThreshold = 1;

    private Player player;
    private Location startPos;
    private Shrine shrine;

    private int i = 0;

    public MalevolentShrine(Plugin _plugin, Shrine _shrine, Player _player, Location _startPos)
    {
        super(_plugin);

        player = _player;
        shrine = _shrine;
        startPos = _startPos.clone();

        startPos.add(player.getLocation().clone().subtract(startPos)).setDirection(player.getLocation().getDirection());
        player.getWorld().playSound(startPos, Sound.BLOCK_BEACON_ACTIVATE, 3f, 0.5f);
    }

    @Override
    public void run()
    {
        for(int s = 0; s < 2; s++)
            shrine.slash(player, startPos, true, true, 9, (i % 3 == 0), 0.4f);

        i++;

        if(i > 200)
            cancel = true;
    }
}
