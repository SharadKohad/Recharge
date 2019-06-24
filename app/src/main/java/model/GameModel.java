package model;

public class GameModel
{
    public String srno;
    public String game_type;
    public String flag;
    public String logo;
    public int joinMember;
    public int winner;

    public int getJoinMember() {
        return joinMember;
    }

    public void setJoinMember(int joinMember) {
        this.joinMember = joinMember;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
