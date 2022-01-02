# 스트림 활용

## 필터링

### 프레디케이트로 필터링
filter 메서드는 `프레디케이트`(불리언을 반환하는 함수)를 인수로 받아서 프레디케이트와 잋리하는 모든 요소를 포함하는 스트림을 반환한다.
```java
List<Dish> vegetarianMenu = menu.stream()
                                .filter(Dish::isVegetarian)
                                .collect(toList())
```

### 고유 요소 필터링
스트림은 고유 요소로 이루어진 스트림을 반환하는 distinct 메소드도 지원한다.(고유 여부는 스트림에서 만든 객채의 hashCode, equals로 결정된다)
```java
List<Integer> numbers = Array.asList(1,2,1,3,3,2,4);
numbers.stream().filter(i -> i % 2 == 0).distinct().forEach(System.out::println);
```

## 스트림 슬라이싱
스트림의 요소를 선택하거나 스킵하는 방법을 설명한다.

### 프레디케이트를 이용한 슬라이링
자바 9는은 스트림의 요소를 효과적으로 선택할 수 있도록 `takeWhile`, `dropWhile` 두 가지 새로운 메서드를 지원한다.

다음과 같은 데이터를 갖고 있다고 가정하자.
````java
List<Dish> menu = Arrays.asList(
        new Dish("pork", false, 120, Type.MEAT),
        new Dish("beef", false, 300, Type.MEAT),
        new Dish("chicken", false, 350, Type.MEAT),
        new Dish("french fries", true, 400, Type.OTHER),
        new Dish("rice", true, 530, Type.OTHER),
        );
````

위 리스트는 이미 칼로리 순으로 정렬되어있다.

filter 연산을 이용하면 전체 스트림을 반복하면서 각 요소에 프레디케이트를 적용하게 된다.

리스트가 이미 정렬되어 있다는 사실을 이용해 320칼로리보다 크거나 같은 요리가 나왔을 때 반복 작업을 중단할 수 있다. 이때 `takeWhile`을 사용하면 된다.

```java
List<Dish> slicedMenu1 = specialMenu.stream().takeWhile(dish -> dish.getCalories() < 320).collect(toList());
```

나머지 요소를 선택하려면 `dropWhile`을 이용하면 된다.
```java
List<Dish> slicedMenu1 = specialMenu.stream().dropWhile(dish -> dish.getCalories() < 320).collect(toList());
```

dropWhile은 takeWhile과 정반대로 프레디케이트가 처음 거짓이 되는 지점까지 발견된 요소를 버린다. 프레디케이트가 거짓이 되면 그 지점에서 작업을 중단하고
남은 모든 요소를 반환한다.

### 스트림 축소
스트림은 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환하는 limit(n) 메서드를 지원한다.

```java
List<Dish> dishes = specialMenu.stream()
                                .filter(dish -> dish.getCalories() > 300)
                                .limit(3)
                                .collect(toList());
```

### 요소 건너뛰기
스트림은 처음 n개 요소를 제외한 스트림을 반환하는 skip(n) 메서드를 지원한다. n개 이하의 요소를 포함하는 스트림에 skip(n)을 호출하면 빈 스트림이 반환된다.

```java
List<Dish> dishes = menu.stream().filter(d-> d.getCalories() > 300).skip(2).collect(toList());
```

## 매핑
스트림 API의 map과 flatMap 메서드는 특정 데이터를 선택하는 기능을 적용한다.

### flatMap
flatMap은 각 배열을 스트림이 아니라 스트림의 콘텐츠로 매핑한다. 즉, map(Arrays::stream)과 달리 flatMap은  `하나의 평면화된 스트림을 반환한다.`

```java
List<String> uniqueCharacters = words.stream()
                                    .map(work -> word.split("")) // 각 단어를 개별 문자를 포함하는 배열로 변환 - Stream<String[]>
                                    .flatMap(Arrays::Stream) //생성된 스트림을 하나의 스트림으로 평면화 - Stream<String>
                                    .distinct()
                                    .collect(toList());
```

## 검색과 매칭 (쇼트서킷)
스트림 API는 allMatch, anyMatch, noneMatch, findFirst, findAny 등 다양한 유틸리티 메서드를 제공한다.

* anyMatch: 적어도 한 요소와 일치하는지 확인하는 최종 연산 (불리언 반환)
```java
boolean anyMatch(Predicate<? super T> predicate);
```
* allMatch: 모든 요소가 주어진 프레디케이트와 일치하는지 확인하는 최종 연산 (불리언 반환)
```java
boolean allMatch(Predicate<? super T> predicate);
```
* noneMatch: 주어진 프레디케이트와 일치하는 요소가 없는지 확인하는 최종 연산 (불리언 반환)
```java
boolean noneMatch(Predicate<? super T> predicate);
```
* findAny: 현재 스트림에서 임의의 요소를 반환한다. 반환 순서가 없을 때 사용한다.
```java
Optional<T> findAny();
```
* findFirst: 현재 스트림에서 첫번째 요소를 반환한다. 
```java
Optional<T> findFirst();
```

> Optional이란 ?</br>
Optional<T> 클래스는 값의 존재나 부재 여부를 표현하는 `컨테이너 클래스`다. findAny는 아무 요소도 반환하지 않을 수 있다. Optional을 활용해 null 확인
관련 버그를 피할 수 있다.

## 리듀싱
모든 스트림 요소를 처리해서 값으로 결과를 도출하는 것을 리듀싱 연산이라고 한다.

### reduce
모든 스트림 요소를 BinaryOperator로 처리해 값을 도출한다. 초기 값이 없는 경우에는 Optional로 감싼 결과를 반환한다.

```java
Optional<T> reduce(BinaryOperator<T> accumulator) // 초기 값이 없으므로 아무 요소가 없을 수 있기 때문에 Optional로 감싼 결과를 반환한다.
T reduce(T identity, BinaryOperator<T> accumulator)
<U> U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)
```

> reduce 메서드의 장점과 병렬화</br>
기존의 단계적 반복으로 합계를 구하는 것보다 reduce를 이용하면 내부 반복이 추상화되면에서 내부 구현에서 병렬로 reduce를 실행할 수 있게 된다.
반복적인 합계는 sum 변수를 공유해야 하므로 병렬화하기 어렵다. 스트림은 포크/조인 프레임워크(fork/join framework)로  처리한다.

> 스트림 연산 : 상태 없음과 상태 있음</br> 
map, filter 등은 입력 스트림에서 각 요소를 받아 0 또는 결과를 출력 스트림으로 보낸다. 따라서 `내부 상태를 가지 않는 연산(stateless operation)`이다
</br>
하지만 reduce, sum, max 같은 연산은 `결과를 누적할 내부 상태`가 필요하다. 스트림에서 처리하는 요소 수와 관계없이 내부 상태의 크기는 `한정(bounded)`되어 있다.
</br>
반면 sorted나 ditinct 같은 연산은 filter나 map처럼 스트림을 입력으로 받아 다른 스트림을 출력하는 것처럼 보일 수 있다. 하지만 filter나 map과는 다르다.</br>
스트림의 요소를 정렬하거나 중복을 제거하려면 과거의 이력을 알고 있어야 한다. 따라서 스트림의 크기가 크거나 무한이라면 문제가 생길 수 있다. </br>
이러한 연산을 `내부 상태를 갖는 연산(stateful operation)`이라 한다.


## 숫자형 스트림
스트림 API는 숫자 스트림을 효율적으로 처리할 수 있도록 기본형 특화 스트림(primitive stream specialization)을 제공한다.

### 기본형 특화 스트림
스트림 API는 boxing 비용을 피할 수 있도록 IntStream, DoubleStream, LongStream을 제공한다. 각각의 인터페이스는 숫자 스트림의 합계를 
계산하는 sum, 최대값 요소를 검색하는 max 같ㄴ이 자주 사용하는 숫자 관련 리듀싱 연산 수행 메서드를 제공한다. 특화 스트림은 오직 boxing 과정에서 일어나는
효율성에만 관련이 있다. (추가 기능 제공 X)

### 숫자 스트림 매핑
스트림을 특화 스트림으로 변환할 때는 mapToInt, mapToDouble, mapToLong 세 가지 메서드가 가장 많이 사용된다.</br>
map과 같은 기능을 수행하지만 Stream<T> 가 아닌 특화된 스트림을 반환한다.(IntStream 등)

```java
int calories = menu.stream()
                    .mapToInt(Dish::getCalories) // IntStream 반환
                    .sum(); 
```

### 객체 스트림으로 복원하기
boxed 메서드를 이용하면 특화 스트림을 일반 스트림으로 변환할 수 있다.
```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
Stream<Integer> stream = intStream.boxed();
```

### 기본 값 : OptionalInt
Optional도 기본형에 대하여 지원한다. OptionalInt, OptionalDouble, OptionalLong 세 가지 기본형 특화 스트림 버전의 Optional이 제공된다.
```java
OptionalInt maxCalories = menu.stream().mapToInt(Dish::getCalories).max();
```

### 숫자 범위
특정 범위의 숫자를 이용해야 할 때 자바 8의 IntStream과 LongStream에서는 range와 rangeClosed라는 두 가지 정적 메서드를 제공한다.
range는 시작값과 종료값이 결과에 포함되지 않는 반면 rangeCLosed는 시작값과 결과값이 결과에 포함된다.
```java
IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(n -> n% 2 == 0); // 짝수 50개만 포함된다.
```

## 스트림 만들기

### 값으로 스트림 만들기
정적 메서드 `Stream.of`를 잉요해서 스트림을 만들 수 있다.
```java
Stream<String> stream = Stream.of("Modern ", "Java" , "In" , "Action");
```
### null이 될 수 있는 객체로 스트림 만들기
자바 9에서는 null이 될 수 있는 개체를 스트림으로 만들 수 있는 새로운 `Stream.ofNullable` 메서드가 추가되었다.

```java
Stream<String> homeValueStream = Stream.ofNullable(System.getProperty("home"));
```

### 배열로 스트림 만들기
배열을 인수로 받는 정적 메서드 `Arrays.stream`을 이용해서 스트림을 만들 수 있다.

### 파일로 스트림 만들기
파일을 처리하는 등의 I/O 연산에 사용하는 자바의 NIO API(비블록 I/O)도 스트림 API를 활용할 수 있도록 업데이트되었다. 
java.nio.file.Files의 많은 정적 메서드가 스트림을 반환한다. 예를 들어 Files.lines는 주어진 파일의 행 스트림을 문자열로 반환한다.

### 함수로 무한 스트림 만들기
Stream.iterate와 Stream.generate를 통해 무한스트림(크기가 고정되지 않은 스트림)을 만들 수 있다.
iterate와 generate에서 만든 스트림은 요청할 때마다 주어진 함수를 이용해서 값을 만든다. 


#### generate 메서드
generate는 iterate와 달리 생상된 각 값을 연속적으로 계산하지 않는다. generate는 Supplier<T>를 인수로 받아서 새로운 값을 생산한다.
```java
Stream.generate(Math::Stream)
        .limit(5)
        .forEach(System.out::println);
```




