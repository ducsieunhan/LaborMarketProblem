package org.example.OOP_LMP;

public class Person {
    String name;
    int[] prefForFirstSet;
    int[] prefForSecondSet;
    boolean isFirstMatched = false;
    boolean isSecondMatched = false;
    int firstMatch = -1;
    int secondMatch = -1;

    public Person(String name, int[] prefForFirstSet, int[] prefForSecondSet) {
        this.prefForFirstSet = prefForFirstSet;
        this.prefForSecondSet = prefForSecondSet;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public int[] getPrefForFirstSet() {
        return prefForFirstSet;
    }

    public int[] getPrefForSecondSet() {
        return prefForSecondSet;
    }

    public boolean isFirstMatched() {
        return isFirstMatched;
    }

    public boolean isSecondMatched() {
        return isSecondMatched;
    }

    public int getFirstMatch() {
        return firstMatch;
    }

    public int getSecondMatch() {
        return secondMatch;
    }

    public void setFirstMatched(boolean firstMatched) {
        isFirstMatched = firstMatched;
    }

    public void setSecondMatched(boolean secondMatched) {
        isSecondMatched = secondMatched;
    }

    public void setFirstMatch(int firstMatch) {
        this.firstMatch = firstMatch;
    }

    public void setSecondMatch(int secondMatch) {
        this.secondMatch = secondMatch;
    }

    @Override
    public String toString() {
        return "Person" + name + '\'' +
                ", firstMatch=" + firstMatch +
                ", secondMatch=" + secondMatch +
                '}';
    }
}
