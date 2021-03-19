package com.company;

import com.company.scraper.GGbetParser;
import com.company.scraper.ParimatchParser;
//import com.company.scraper.Scraper;

public class Main {

    public static void main(String[] args) {
	// write your code here

        GGbetParser ggbet = new GGbetParser();
        Thread ggbetThread = new Thread(ggbet);
        ggbetThread.start();

//        PariMatch thread and waiting for finish of thread
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

        System.out.println(pari.getElementStructList().get(0).getTeam1());
        System.out.println(ggbet.getElementStructList().get(0).getTeam1());

    }
}
