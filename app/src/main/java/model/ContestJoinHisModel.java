package model;

public class ContestJoinHisModel
{
    public String srno;
    public String ttime;
    public String entry_amt;
    public String amount;
    public String description;
    public String remark;
    public String colorcode;

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getTtime() {
        return ttime;
    }

    public void setTtime(String ttime) {
        this.ttime = ttime;
    }

    public String getEntry_amt() {
        return entry_amt;
    }

    public void setEntry_amt(String entry_amt) {
        this.entry_amt = entry_amt;
    }
}
