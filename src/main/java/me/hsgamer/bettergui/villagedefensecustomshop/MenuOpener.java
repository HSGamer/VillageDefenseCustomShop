package me.hsgamer.bettergui.villagedefensecustomshop;

import me.hsgamer.bettergui.BetterGUI;
import org.bukkit.entity.Player;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaRegistry;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MenuOpener implements Consumer<Player> {
    private final TreeMap<Integer, String> map = new TreeMap<>();
    private final Arena arena;

    public MenuOpener(Arena arena) {
        this.arena = arena;
    }

    public TreeMap<Integer, String> getMap() {
        return map;
    }

    @Override
    public void accept(Player player) {
        if (!ArenaRegistry.isInArena(player)) {
            return;
        }
        int wave = arena.getWave();
        Optional.ofNullable(map.floorEntry(wave))
                .map(Map.Entry::getValue)
                .ifPresent(menu -> BetterGUI.getInstance().getMenuManager().openMenu(menu, player, new String[0], true));
    }
}
