package com.speedsol.generations.future.expandablelistview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Chapter1 = new ArrayList<String>();
        Chapter1.add("Title1");
        Chapter1.add("Title2");
        Chapter1.add("Title3");

        List<String> Chapter2 = new ArrayList<String>();
        Chapter2.add("Title1");
        Chapter2.add("Title2");
        Chapter2.add("Title3");


        List<String> Chapter3 = new ArrayList<String>();
        Chapter3.add("Title1");
        Chapter3.add("Title2");
        Chapter3.add("Title3");


        List<String> Chapter4 = new ArrayList<String>();
        Chapter4.add("Title1");
        Chapter4.add("Title2");
        Chapter4.add("Title3");

        List<String> Chapter5 = new ArrayList<String>();
        Chapter5.add("Title1");
        Chapter5.add("Title2");
        Chapter5.add("Title3");




        expandableListDetail.put("Chapter1", Chapter1);
        expandableListDetail.put("Chapter2", Chapter2);
        expandableListDetail.put("Chapter3", Chapter3);
        expandableListDetail.put("Chapter4", Chapter4);
        expandableListDetail.put("Chapter5", Chapter5);
        return expandableListDetail;
    }
}

