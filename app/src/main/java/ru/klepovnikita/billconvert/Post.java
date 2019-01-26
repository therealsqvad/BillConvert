package ru.klepovnikita.billconvert;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private Map<String, Double> langs = new HashMap<String, Double>();


    public Map<String, Double> getLangs() {
        return langs;
    }

    public void setLangs( Map<String, Double> langs) {
        this.langs = langs;
    }
   /* @SerializedName("val")
    @Expose
    private double bill;

    public double getBills() {
        return bill;
    }

    public void setUserId(int userId) {
        this.bill = userId;
    }

    @SerializedName("results")
    @Expose
    private Map<String, Object> bills = new HashMap<String, Object>();


    public Map<String, Object> getLangs() {
        return bills;
    }

    public void setLangs( Map<String, Object> bills) {
        this.bills = bills;
    }
*/

}
