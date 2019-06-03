package model;

public class MatchesPlayers
{
    public String pid;
    public String name;
    private boolean isSelected = false;
    public boolean isSelected()
    {
        return isSelected;
    }
    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }
    public String getPid()
    {
        return pid;
    }
    public void setPid(String pid)
    {
        this.pid = pid;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
}
