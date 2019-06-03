package model;

public class TeamModel
{
    private PlayerModel[] players;

    private String name;

    public PlayerModel[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerModel[] players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
