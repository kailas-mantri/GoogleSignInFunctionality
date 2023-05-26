package com.speedsol.generations.future.expandablelistscrima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> child = new HashMap<>();

        List<String> parent = new ArrayList<>();
        parent.add("Actors");
        parent.add("Cars");
        parent.add("Movies");
        parent.add("Singers");

        List<String> actors = new ArrayList<>();
        actors.add("Vishal Suryawanshi");
        actors.add("Hrithik Roshan");
        actors.add("Salman Khan");
        actors.add("Rajesh Khanna");
        actors.add("Shah Rukh Khan");
        actors.add("Paresh Rawal");

        List<String> cars = new ArrayList<>();
        cars.add("Lamborghini");
        cars.add("Tesla");
        cars.add("Audi");
        cars.add("BMW");
        cars.add("Ertiga");

        List<String> movies = new ArrayList<>();
        movies.add("War");
        movies.add("Saaho");
        movies.add("Chhichhore");
        movies.add("Pagalpanti");
        movies.add("Dhamaal");
        movies.add("Golmaal");

        List<String> singers = new ArrayList<>();
        singers.add("Sonu Nigam");
        singers.add("Udit Narayan");
        singers.add("Arijit Singh");
        singers.add("Papon");
        singers.add("Kumar Sanu");
        singers.add("Kishor Kumar");
        singers.add("Mohd. Rafi");

        child.put(parent.get(0),actors);
        child.put(parent.get(1),cars);
        child.put(parent.get(2),movies);
        child.put(parent.get(3),singers);

        return child;
    }
}
