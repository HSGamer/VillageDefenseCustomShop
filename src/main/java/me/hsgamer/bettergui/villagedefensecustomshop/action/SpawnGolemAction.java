package me.hsgamer.bettergui.villagedefensecustomshop.action;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plugily.projects.villagedefense.Main;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaRegistry;
import plugily.projects.villagedefense.handlers.language.Messages;

import java.util.UUID;

public class SpawnGolemAction extends BaseAction {
    private final Main plugin;

    public SpawnGolemAction(String string) {
        super(string);
        this.plugin = JavaPlugin.getPlugin(Main.class);
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
        taskChain.sync(() -> {
            arena.spawnGolem(arena.getStartLocation(), player);
            player.sendMessage(plugin.getChatManager().getPrefix() + plugin.getChatManager().colorMessage(Messages.GOLEM_SPAWNED));
        });
    }
}
