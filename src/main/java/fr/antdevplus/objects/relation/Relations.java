package fr.antdevplus.objects.relation;

import org.bukkit.ChatColor;

public enum Relations {
    //gray
    NEUTRAL(ChatColor.GRAY+""),
    //RED
    ENEMY(ChatColor.DARK_RED+""),
    // Light_GREEN
    ALLY(ChatColor.GREEN+""),
    // Light_RED
    WARLIKE(ChatColor.RED +""),
    // BLUE
    CORDIAL(ChatColor.BLUE+""),
    // Purple
    MASTERFUL(ChatColor.DARK_PURPLE+""),
    // Gold
    MASTERED(ChatColor.GOLD+"");

    private String color;
    Relations(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
