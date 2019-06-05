package model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerModel implements Parcelable
{
    private String name;

    private String pid;

    private boolean is_selected;

    protected PlayerModel(Parcel in) {
        name = in.readString();
        pid = in.readString();
        is_selected = in.readByte() != 0;
    }

    public static final Creator<PlayerModel> CREATOR = new Creator<PlayerModel>() {
        @Override
        public PlayerModel createFromParcel(Parcel in) {
            return new PlayerModel(in);
        }

        @Override
        public PlayerModel[] newArray(int size) {
            return new PlayerModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(pid);
        dest.writeByte((byte) (is_selected ? 1 : 0));
    }
}
