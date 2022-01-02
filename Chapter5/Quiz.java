package Chapter5;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Quiz {
    public static void main(String ... args) {
        //Quiz 5-2_1
        /*
        List<Integer> list= Arrays.asList(1, 2, 3, 4, 5);
        */
        /*
        list.stream().map(n -> n * n).collect(toList());

        //Quiz 5-2_2
        List<Integer> list_1 = Arrays.asList(1, 2, 3);
        List<Integer> list_2 = Arrays.asList(3,4);

        List<int[]> answer2 = list_1.stream().flatMap(a -> list_2.stream().map(b -> new int[]{a, b})).collect(toList());

        answer2.stream().forEach(a -> System.out.println(a[0] + " " + a[1]));

        //Quiz 5-2_3

        List<int[]> answer3 = list_1.stream().flatMap(a -> list_2.stream().map(b -> new int[]{a, b})).filter(a -> (a[0] + a[1] ) % 3 == 0).collect(toList());
        answer3.stream().forEach(a -> System.out.println(a[0] + " " + a[1]));
        */
        /*
            Practice
         */
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
        // 1
        transactions.stream().filter(a -> a.getYear() == 2011).sorted(Comparator.comparing(Transaction::getValue)).collect(toList());
        // 2
        transactions.stream().map(a -> a.getTrader().getCity()).distinct().collect(toList());
        //3
        transactions.stream().filter(a -> a.getTrader().getCity().equals("Cambridge")).map(a -> a.getTrader()).sorted(Comparator.comparing(Trader::getName)).collect(toList());
        //4
        transactions.stream().map(a -> a.getTrader().getName()).sorted().reduce("", (n1, n2) -> n1 + n2);
        //5
        boolean match = transactions.stream().anyMatch(a -> a.getTrader().getCity().equals("Milan"));
        //6
        transactions.stream().filter(a -> a.getTrader().getCity().equals("Cambridge")).forEach(a -> System.out.println(a.getTrader().getName()));
        transactions.stream().filter(a -> a.getTrader().getCity().equals("Cambridge")).map(Transaction::getValue).forEach(System.out::println);
        //7
        Optional<Integer> value = transactions.stream().map(Transaction::getValue).reduce(Integer::max);
        //8
        Optional<Integer> value2 = transactions.stream().map(Transaction::getValue).reduce(Integer::min);

    }
}
