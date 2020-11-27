package fr.antdevplus.objects.relation;

import fr.antdevplus.objects.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.Set;

public class Relation {

    private double influence;
    private double experience;
    private int money, claimed_block, kills, members, allied_or_opponent_guilds;
    private String relation;

    public double Influence(Guild guild) {
        experience = guild.getExperience();
            return loadInfluence(guild.getPlayers());
    }

    private double loadInfluence(Set<String> players) {
        for (String playername : players){
            Player player = Bukkit.getPlayer(playername);
            claimed_block = claimed_block + player.getStatistic(Statistic.WALK_ON_WATER_ONE_CM) + player.getStatistic(Statistic.WALK_ONE_CM) + player.getStatistic(Statistic.WALK_UNDER_WATER_ONE_CM);
            kills = kills + player.getStatistic(Statistic.PLAYER_KILLS);
            members = members + 1;
        }
        influence = claimed_block/10000 + kills + members + experience;
        return influence;
    }

    public String getRelation() {
        return relation;
    }
}
