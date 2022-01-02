package Chapter4;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static Chapter4.Dish.menu;


public class HighCaloriesNames {

  public static void main(String[] args) {
      //Quiz 1
      List<String> highCaloricDishes = menu.stream().filter(dish -> dish.getCalories() > 300).map(Dish::getName).collect(toList());


      //System.out.println(names);
  }

}
