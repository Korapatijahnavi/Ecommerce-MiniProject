package com.example.ecommerceminiproject;

import com.example.ecommerceminiproject.Product;
import java.util.Locale;

public final class GroceryCategorizer {
    public static final String FRUITS = "fruits";
    public static final String VEGETABLES = "vegetables";
    public static final String DAIRY = "dairy";
    public static final String MEAT = "meat";
    public static final String BEVERAGES = "beverages";
    public static final String PANTRY = "pantry";
    public static final String PET_CARE = "pet-care";
    public static final String HOUSEHOLD = "household";
    public static final String OTHERS = "others";

    private static final String[] ORDER = {
            FRUITS, VEGETABLES, DAIRY, MEAT, BEVERAGES, PANTRY, PET_CARE, HOUSEHOLD, OTHERS
    };

    private static final String[][] KEYWORDS = {
            {"cat food", PET_CARE}, {"dog food", PET_CARE},
            {"ice cream", DAIRY}, {"milk", DAIRY}, {"egg", DAIRY}, {"cheese", DAIRY},
            {"butter", DAIRY}, {"yogurt", DAIRY},
            {"apple", FRUITS}, {"kiwi", FRUITS}, {"lemon", FRUITS}, {"mulberry", FRUITS},
            {"strawberr", FRUITS}, {"banana", FRUITS}, {"orange", FRUITS}, {"mango", FRUITS},
            {"grape", FRUITS},
            {"pepper", VEGETABLES}, {"chili", VEGETABLES}, {"cucumber", VEGETABLES},
            {"potato", VEGETABLES}, {"onion", VEGETABLES}, {"tomato", VEGETABLES},
            {"carrot", VEGETABLES},
            {"beef", MEAT}, {"chicken", MEAT}, {"fish", MEAT}, {"steak", MEAT}, {"meat", MEAT},
            {"juice", BEVERAGES}, {"coffee", BEVERAGES}, {"drink", BEVERAGES},
            {"water", BEVERAGES}, {"tea", BEVERAGES},
            {"rice", PANTRY}, {"oil", PANTRY}, {"honey", PANTRY}, {"protein", PANTRY},
            {"flour", PANTRY}, {"sugar", PANTRY},
            {"tissue", HOUSEHOLD}, {"soap", HOUSEHOLD}, {"detergent", HOUSEHOLD}
    };
    private GroceryCategorizer() {
    }
    public static String categorize(String title) {
        if (title == null) return OTHERS;
        String lower = title.toLowerCase(Locale.ROOT);
        for (String[] pair : KEYWORDS) {
            if (lower.contains(pair[0])) return pair[1];
        }
        return OTHERS;
    }

    public static void assignCategory(Product product) {
        product.setCategory(categorize(product.getTitle()));
    }
    public static int iconFor(String slug)
    {
        if(slug==null) return R.drawable.cat_others;
        switch(slug){
            case FRUITS:
                return R.drawable.cat_fruits;
            case VEGETABLES:
                return R.drawable.cat_vegetables;
            case DAIRY:
                return R.drawable.cat_dairy;
            case MEAT:
                return R.drawable.cat_meat;
            case BEVERAGES:
                return R.drawable.cat_beverages;
            case PANTRY:
                return R.drawable.cat_pantry;
            case PET_CARE:
                return R.drawable.cat_petcare;
            case HOUSEHOLD:
                return R.drawable.cat_household;
            default:
                return R.drawable.cat_others;
        }
    }


    public static int orderOf(String slug) {
        for (int i = 0; i < ORDER.length; i++) {
            if (ORDER[i].equals(slug)) return i;
        }
        return ORDER.length;
    }
}
