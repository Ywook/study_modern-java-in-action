#스트림이란 ?
`스트림(Stream)`은 자바 8 API에 새로 추가된 기능이다. 스트림을 이용하면 선언형(즉, 데이터를 처리하는 임시 구현 코드 대신 질의로 표현할 수 있다)으로 
컬렉션 데이터를 처리할 수 있다. 또한, 스트림을 이용하면 멀티스레드 코드를 구현하지 않아도 데이터를 `투명하게` 병렬로 처리할 수 있다.

## 스트림 정의
데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소
* `연속된 요소`: 컬렉션과 마찬가지로 스트림은 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스를 정의한다. 컬렉션은 자료구조 이므로 컬렉션에서는 시간과 공간의 
  복잡성과 관련된 요소 저장 및 접근 연산이 주를 이룬다. 반면, 스트림은 filter, sorted, map 처럼 계산식이 주를 이룬다. 즉, 컬렉션의 주제는 데이터, 스트림의 주제는 계산이다.
* `소스`: 스트림은 컬렉션, 배열, I/O 자원 등의 데이터 제공 소스로부터 데이터를 소비한다. (정렬된 컬렉션으로 스트림을 생성하면 정렬이 유지된다.)  
* `데이터 처리 연산`: 스트림은 함수형 프로그래밍 언어에서 지원하는 연산 및 데이터베이스와 비슷한 연산을 지원한다. 스트림 연산은 순차적으로 또는 병렬로 실행할 수 있다.

### 스트림 API 특징
* `선언형`: 더 간결하고 가독성이 좋아진다.
* `조립할 수 있음` : 유연성이 좋아진다.
* `병렬화`: 성능이 좋아진다.
* `파이프라이닝(Pipelining)`: 대부분의 스트림 연산은 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수 있도록 스트림 자신을 반환한다. 
덕분에 `게으름(laziness)`, `쇼트서킷(short-circuiting`같은 최적화도 얻을 수 있다.
* `내부반복`: 반족자를 이용해서 명시적으로 반복하는 컬렉션과 달리 스트림은 내부 반복을 지원한다.


## 스트림과 컬렉션
자바의 기존 컬렉션과 새로운 스트림 모두 `연속된` 요소 형식의 값을 저장하는 자료구조의 인터페이스를 제공한다. 여기서 `연속된(sequenced)`이라는 표현은 순서와 상관없이
아무 값에나 접속하는 것이 아니라 순차적으로 값에 접근한다는 것을 의미한다.

### 스트림과 컬렉션의 차이

#### 데이터를 `언제` 계산하는가 ?

`컬렉션`
* 현재 자료구조가 포함하는 `모든` 값을 메모리에 저장하는 자료구조다. 즉, 컬렉션의 모든 요소는 컬렉션에 추가하기 전에 계산되어야 한다.
(Collection 요소를 추가, 삭제 연산을 수행할 때마다 컬렉션의 모든 요소를 메모리에 저장해야 하며, Collection에 추가하려는 요소는 미리 계산되어야 한다.)
* DVD처럼 컬렉션은 현재 자료구조에 포함된 모든 값을 계산한 다음에 컬렉션에 추가할 수 있다. (생산자 중심-supplier-driven)
* 

`스트림`
* 이론적으로 `요청할 때만 요소를 계산(게으른 생성)`하는 고정된 자료구조다. (스트림에 요소를 추가, 삭제할 수 없다.) 즉, 사용자가 요청하는 값만 스트림에서 추출한다는 것이 핵심이다.
* 스티리밍 비디오처럼 필요할 때 값을 계산한다.

#### 1번만 탐색할 수 있다.
탐색된 스트림의 요소는 소비된다. 반복자와 마찬가지로 한 번 탐색한 요소를 다시 탐색하려면 초기 데이터 소스에서 새로운 스트림을 만들어야한다.

#### 외부 반복과 내부 반복 

컬렉션 인터페이스를 사용하려면 사용자가 직접 요소를 반복해야 한다. (for-each 등을 사용) 이것을 `외부 반복` 이라고 한다.</br>
반면, 스트림 라이브러리는 (반복을 알아서 처리하고 결과 스트림 값을 어딘가에 저장해주는) `내부 반복`을 사용한다.

```java
// 외부 반복
List<String> names = new ArrayList<>();
Iterator<String> Iterator = menu.iterator();
while(iterator.hasNext()){
    Dish dish = iterator.next();
    names.add(dish.getName());
}
```

```java
// 내부 반복
List<String> names = menu.stream().map(Dish::getName).collect(toList());
```

## 스트림 연산
스트림 인터페이스의 연산을 크게 두가지로 구분할 수 있다.
* 중간 연산 : 연결할 수 있는 스트림 연산
* 최종 연산 : 스트림을 닫는 연산

### 중간 연산 
중간 연산의 특징은 단말 연산을 스트림 파이프라인에 실행하기 전까지는 아무 연산도 수행하지 않는다는 것이다. 즉 `게으르다(lazy)`는 것이다.
중간 연산을 합친 다음에 합쳐진 중간 연산을 최종 연산으로 한 번에 처리하기 때문이다.


```java
List<Dish> menu = Arrays.asList(
        new Dish("pork", false, 800, Type.MEAT),
        new Dish("beef", false, 700, Type.MEAT),
        new Dish("chicken", false, 400, Type.MEAT),
        new Dish("french fries", true, 530, Type.OTHER),
        new Dish("rice", true, 350, Type.OTHER),
        new Dish("season fruit", true, 120, Type.OTHER),
        new Dish("pizza", true, 550, Type.OTHER),
        new Dish("prawns", false, 400, Type.FISH),
        new Dish("salmon", false, 450, Type.FISH)
        );

List<String> names = menu.stream()
                        .filter(dish -> {
                                System.out.println("filtering: "+dish.getName());
                                return dish.getCalories() > 300;
                        })
                        .map(dish -> {
                            System.out.println("mapping: "+dish.getName());
                            return dish.getName() > 300;
                        })
                        .limit(3)
                        .collect(toList());
```

소스를 실행 결과이다. 스트림의 lazy 특성 때문에 300칼로리가 넘는 요리는 8개이지만, 처음 3개만 선택되었다 (쇼트서킷).

filtering:pork</br>
mapping:pork</br>
filtering:beef</br>
mapping:beef</br>
filtering:chicken</br>
mapping:chicken</br>

>  쇼트서킷: 모든 데이터 요소를 처리하지 않고 결과를 반환하는 것

### 최종 연산
최종 연산은 스트림 파이프라인에서 결과를 도출한다. 보통 최종 연산에 의해 List, Integer, void 등 스트림 이외의 결과가 반환된다.


