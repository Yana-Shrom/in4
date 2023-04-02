package src;

import java.util.ArrayList;

public class Task {
    private final int id;
    private final ArrayList<Integer> range;
    private ArrayList<Integer> results;
    private boolean completed;

    public Task(int id, ArrayList<Integer> range) {
        this.id = id;
        this.range = range;
        this.results = null;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getRange() {
        return range;
    }

    public ArrayList<Integer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Integer> results) {
        this.results = results;
    }

    public boolean isCompleted() {
        return completed;
    }

    public synchronized void markCompleted() {
        completed = true;
        notifyAll();
    }

    public synchronized void waitForCompletion() throws InterruptedException {
        while (!completed) {
            wait();
        }
    }
}