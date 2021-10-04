package me.hsgamer.bettergui.villagedefensecustomshop.action;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaRegistry;

import java.util.UUID;

public class SpawnWolfAction extends BaseAction {
    public SpawnWolfAction(String string) {
        super(string);
    }

    @Override
    public void addToTaskChain(UUID uuid, TaskChain<?> taskChain) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        Arena arena = ArenaRegistry.getArena(player);
        if (arena == null) {
            return;
        }
        taskChain.sync(() -> arena.spawnWolf(player.getLocation(), player));
    }
}
