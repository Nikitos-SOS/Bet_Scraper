package com.company;

import com.company.scraper.GGbetParser;
import com.company.scraper.ParimatchParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
//import com.company.scraper.Scraper;

public class Main {

    public static void main(String[] args) {

//        GGBet thread
        GGbetParser ggbet = new GGbetParser();
        Thread ggbetThread = new Thread(ggbet);
        ggbetThread.start();

//        PariMatch thread
        ParimatchParser pari = new ParimatchParser();
        Thread pariThread = new Thread(pari);
        pariThread.start();

//        Waiting for all thread finished
        try {
            ggbetThread.join();
            pariThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JSONArray bkEvents = new JSONArray();
        bkEvents.put(pari.getJsonEvents());
        bkEvents.put(ggbet.getJsonEvents());

//        JSONArray test
        try {
            System.out.println(bkEvents.getJSONObject(0));
            System.out.println(bkEvents.getJSONObject(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
