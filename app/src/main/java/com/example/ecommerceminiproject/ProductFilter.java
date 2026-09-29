package com.example.ecommerceminiproject;
import com.example.ecommerceminiproject.Category;
import com.example.ecommerceminiproject.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
public class ProductFilter {
    private ProductFilter(){
    }
    public static String toDisplayName(String slug){
        if(slug==null || slug.isEmpty()) return "";
        String[] words = slug.replace('-',' ').split(" ");
        StringBuilder sb  = new StringBuilder();
        for(String word:words){
            if(word.isEmpty()) continue;
            if(sb.length()>0) sb.append(' ');
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1));
        }
        return sb.toString();
    }
    public static List<Category> extractCategories(List<Product> products){
        LinkedHashMap<String, Category> map= new LinkedHashMap<>();
        if(products==null) return new ArrayList<>();
        for(Product product: products)
        {
            if(product==null) continue;
            String slug = product.getCategory();
            if(slug==null || map.containsKey(slug)) continue;
            map.put(slug, new Category(toDisplayName(slug),slug));
        }
        List<Category> res=new ArrayList<>(map.values());
        Collections.sort(res,(a,b)->Integer.compare(GroceryCategorizer.orderOf(a.getSlug()),GroceryCategorizer.orderOf(b.getSlug())));
        return res;
    }
    public static List<Product> filter(List<Product> all, String categorySlug, String query)
    {
        List<Product> result= new ArrayList<>();
        if(all==null) return result;
        boolean allcategories=categorySlug==null || Constants.CATEGORY_ALL.equals(categorySlug);
        String q =query==null ? "":query.trim().toLowerCase(Locale.ROOT);
        for(Product product:all)
        {
            if(!allcategories && !categorySlug.equals(product.getCategory())) continue;
            if(!q.isEmpty()){
                String title= product.getTitle()==null? "":product.getTitle().toLowerCase(Locale.ROOT);
                String brand=product.getBrand().toLowerCase(Locale.ROOT);
                if(!title.contains(q) && !brand.contains(q)) continue;
            }
            result.add(product);
        }
        return result;
    }
    public static List<Product> topDeals(List<Product> all, int limit)
    {
        List<Product> copy = new ArrayList<>(all);
        Collections.sort(copy,(a,b)->Double.compare(b.getDiscountPercentage(), a.getDiscountPercentage()));
        return new ArrayList<>(copy.subList(0,Math.min(limit, copy.size())));
    }

    public static List<Product> topRated(List<Product> all, int limit)
    {
        List<Product> copy= new ArrayList<>(all);
        Collections.sort(copy,(a,b)->
                Double.compare(b.getRating(),a.getRating()));
        return new ArrayList<>(copy.subList(0,Math.min(limit,copy.size())));
    }
}
