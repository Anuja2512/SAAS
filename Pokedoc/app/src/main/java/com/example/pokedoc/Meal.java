package com.example.pokedoc;

public class Meal {

    String mealName;
    String mealAmt;

    public Meal(String mealName, String mealAmt) {
        this.mealName = mealName;
        this.mealAmt = mealAmt;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealAmt() {
        return mealAmt;
    }

    public void setMealAmt(String mealAmt) {
        this.mealAmt = mealAmt;
    }

    public Meal() {
    }
}
