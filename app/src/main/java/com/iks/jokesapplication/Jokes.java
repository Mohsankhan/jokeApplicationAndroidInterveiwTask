package com.iks.jokesapplication;

import java.util.ArrayList;
import java.util.List;

public class Jokes {
    public List<Joke> getJokes() {
        return jokes;
    }

    public void setJokes(List<Joke> jokes) {
        this.jokes = jokes;
    }

    List<Joke> jokes = new ArrayList<>();

}
