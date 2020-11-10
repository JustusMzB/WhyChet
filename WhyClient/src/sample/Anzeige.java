package sample;

import java.util.Date;
import java.util.StringTokenizer;

public class Anzeige {
    private String tag;
    private String uhrzeit;

    public Anzeige(Date date){
        StringTokenizer tokenizer = new StringTokenizer(date.toString());
        String[] tokens = new String[6];
        int i = 0;
        while(tokenizer.hasMoreTokens()){
            tokens[i]= tokenizer.nextToken();
            i++;
        }

        this.tag= tokens[2] + ". " +tokens[1] + " " + tokens[5];
        this.uhrzeit = tokens[3];
    }

    @Override
    public String toString(){
        return uhrzeit +" "+tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public void setUhrzeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }
}
