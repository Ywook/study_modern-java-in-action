# 동작 파라미터화 코드 전달하기
동작 파라미터화란 다양한 동작을 수행할 수 있도록 메서드의 인수로 코드를 전달하는 것을 말한다.</br>
동작 파라미터화를 이용하면 자주 바뀌는 요구사항에 효과적으로 대응할 수 있고 코드의 중복을 방지할 수 있다.

## 동작 파라미터화
자바 8 이전에는 코드를 지저분하게 구현해야 했다. 익명 클래스로도 어느 정도 코드를 깔끔하게 만들 수 있지만 자바 8에서는 인터페이스를 상속받아 여러 클래스를 구현해야
하는 수고를 없앨 수 있는 방법을 제공한다.
### 선택 조건을 결정하는 인터페이스 선언(Predicate) - 전략 디자인 패턴
전략 디자인 패턴은 각 알고리즘을 캡슐화하는 알고리즘 패밀리를 정의해둔 다음에 런타임에 알고리즘을 선택하는 기법이다.

* 알고리즘 패밀리
```java
public interface ApplePredicate{
    boolean test (Apple apple);
}
```
* 알고리즘 전략
```java
public class AppleHeavyWeightPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple){
        return apple.getWeight() > 150;
    }
}
```

### 익명 클래스
```java
List<Apple> redApples = filterApples(inventory, new ApplePredicate(){
    public boolean test(Apple apple){
        return RED.equals(apple.getColor());    
    }
});
```

### 람다 표현식 사용
```java
List<Apple> redApples = filterApples(inventory, (Apple a) -> RED.equals(a.getColor()));
```

### 리스트 형식으로 추상화
```java
public interface Predicate<T> {
    boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p) {
    List<T> result = new Array<>();
    for(T e : list){
        if(e.test()){
            result.add(e);
        }
    }
    return result;
}
```
