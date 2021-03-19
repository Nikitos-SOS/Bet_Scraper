package com.company;

import com.company.scraper.GGbetParser;
import com.company.scraper.ParimatchParser;
//import com.company.scraper.Scraper;

public class Main {

    public static void main(String[] args) {
	// write your code here



            Thread pari = new ParimatchParser();
            pari.start();

            Thread ggbet = new GGbetParser();
            ggbet.start();

    }
}
