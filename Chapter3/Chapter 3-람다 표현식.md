# 람다 표현식
* 람다 표현식은 메서드로 전달할 수 있는 익명 함수를 단순화한 것이라고 할 수 있다. 
* 람다 표현식에는 이름은 없지만, 파라미터 리스트, 바디, 반환 형식, 발생할 수 있는 예외 리스트는 가질 수 있다.
* 함수형 인터페이스를 기대하는 곳에서만 람다 표현식을 사용할 수 있다.


## 람다의 특징
* 익명 - 보통 메서드와 달리 이름이 없으므로 익명이라 표현한다.
* 함수 - 람다는 메서드처럼 특정 클래스에 종속되지 않으므로 함수라고 부른다. 하지만 메서드처럼 파라미터 리스트, 바디, 반환 형식, 가능한 예외 리스트를 포함한다.
* 전달 - 람다 표현식을 메서드 인수로 전달하거나 변수로 저장할 수 있다.
* 간결성 - 익명 클래스처럼 자질구레한 코드를 구현할 필요가 없다. 


## 람다의 구성
```java
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```
* 파라미터 리스트 - 바디에서 사용할 파라미터를 선언한다.
* 화살표(->) - 람다의 파라미터 리스트와 바디를 구분한다.
* 람다 바디 - 람다의 반환값에 해당하는 표현식이다.

## 함수형 인터페이스
`함수형 인터페이스는 정확히 하나의 추상 메서드만 가지는 인터페이스`이다(함수형 인터페이스는 디폴트 메서드를 포함할 수 있다). 
자바 API 함수형 인터페이스로 Comparator, Runnable, Predicate 등이 있다.

> @FunctionalInterface</br>
@FunctionalInterface는 함수형 인터페이스 임을 가리키는 어노테이션이다. 만약 어노테이션을 사용했지만 실제로 함수형 인터페이스가 아니면 컴파일 에러를
발생 시키기 때문에 함수형 인터페이스에는 꼭 써주는게 좋다. (런타임 에러 방지)

## 함수 디스크립터
함수형 인터페이스의 추상 메서드 시그니처는 해당 추상 메서드를 구현하는 람다 표현식의 시그니처와 같다. 

람다 표현식의 시그니처를 서술하는 메서드를 함수 디스크립터라고 부른다.
즉, Comparator의 comparable 메서드의 함수 디스크립터는 `(T, T) -> int` 이다. 

## 함수형 인터페이스의 사용

### Predicate
(T) -> boolean

````java
@FunctionalInterface
public interface Predicate<T>{
    boolean test(T t);
}
````

### Consumer
(T) -> void
````java
@FunctionalInterface
public interface Consumer<T>{
    void accept(T t);
}
````

### Function
(T) -> R
````java
@FunctionalInterface
public interface Function<T,R>{
    R apply(T t);
}
````

## 기본형 특화
제네릭에는 Reference Type만 사용할 수 있다. 그래서 자바에서는 Primitive Type을 Reference Type으로 자옹으로 변환하는 기능(autoboxing)을 제공한다. 
boxing한 값은 Primitive Type을 감싸는 래퍼로 힙에 저장되므로 메모리를 더 소비하며 Primitive Type을 가져올 때도 메모리를 탐색하는 과정이 필요하다.
따라서 자바 8에서는 Primitive Type을 입출력으로 사용하는 상황에서 오토박싱을 피할 수 잇도록 특별한 버전의 함수형 인터페이스를 제공한다.
```java
public interface IntPredicate{
    boolean test(int t);
}
```
일반적으로 특정 형식을 받는 함수형 인터페이스의 이름 앞에는 DoublePredicate, IntConsumer처럼 형식명이 붙는다. 
Function 인터페이스는 ToIntFunction<T>m, IntToDoubleFunction 등의 다양한 출력 형식 파라미터를 제공한다.

## 예외, 람다, 함수형 인터페이스의 관계
함수형 인터페이스는 Checked Exception을 던지는 동작을 허용하지 않는다. 즉, 예외를 던지는 람다 표현식을 만들려면
Checked Exception을 선언하는 함수형 인터페이스를 직접 정의하거나 람다를 try/catch 블록으로 깜싸야한다.

## 람다 표현식에서 지역 변수 사용
람다 표현식에서는 익명 함수가 하는 것처럼 자유 변수 (파라미터로 넘겨진 변수가 아닌 외부에서 정의된 변수)를 활용할 수 있다.
이와 같은 동작을 `람다 캡처링(capturing lambda)`이라고 부른다. 람다는 인스턴스 변수와 정적 변수를 자유롭게 캡처할 수 있다. 하지만 그러려면 지역 변수는 명시적으로 final로 선언되어 있거나
실질적으로 final로 선언된 변수와 똑같이 사용되어야 한다. 

## 지역 변수에 제약이 필요한 이유 
JVM에서 인스턴스 변수는 `힙`에 저장되는 반면 지역 변수는 `스택`에 저장된다.
그리고 JVM에서 쓰레드마다 별도의 스택 영역이 생성된다. 따라서 스택 영역은 공유가 안되고 인스턴스 변수는 공유할 수 있다.

람다에서 지역 변수에 바로 접근할 수 있다는 가정하에 람다가 스레드에서 실행된다면 변수를 할당한 스레드가 사라져서
변수 할당이 헤제되었는데도 람다를 실행하는 스레드에서는 해당 변수에 접근하려 할 수 있다.
따라서 자바 구현에서는 원래 변수에 접근을 허용하는 것이 아니라 `자유 지역 변수의 복사본`을 제공한다.
그러므로 복사본의 값이 바뀌지 않아야 하므로 지역 변수에는 한 번만 값을 할당해야 한다는 제약이 생긴 것이다.

### 클로저란 ?
클로저(Closure)란 함수의 비지역 변수를 지유롭게 참조할 수 있는 함수의 인스턴스를 가리킨다. 
예를 들어 클로저를 다른 함수의 인수로 전달할 수 있다. 클로저는 클로저 외부에 정의된 변수의 값에 접근하고,
값을 변경할 수 있다. 

>자바 8의 람다와 익명 클래스는 클로저와 비슷한 동작을 수행한다. 람다와 익명 클래스는 모두 메서드의 인수로
전달될 수 있으며 자신의 외부 영역의 변수에 접근할 수 있다. 다만 람다와 익명 클래스는 람다가 정의된 메서드의 `지역
변수의 값은 변경할 수 없다`. 덕분에 람다는 변수가 아닌 `값`에 국한되어 어떤 동작을 수행한다는 사실이 명확해진다.

## 메서드 참조 `::`
메서드 참조를 이용하면 기존의 메서드 정의를 재활용해서 람다처럼 전달할 수 있다.

* 람다 표현식
```java
inventory.sort((a1, a2) -> a1.getWeight().compare(a2.getWeight()));
```
* 메서드 참조
```java
inventory.sort(comparing(Apple::getWeight));
```

### 메서드 참조 유형
1. 정적 메서드 참조</br>
```java
(args) -> ClassName.staticMethod(args)
ClassName::staticMethod
```
2. 다양한 형식의 인스턴스 메서드 참조</br>
```java
(arg0, rest) -> arg0.instanceMethod(rest)
ClassName::instanceMethod
```
3. 기존 객체의 인스턴스 메서드 참조
```java
(args) -> expr.instanceMethod(args)
expr::instanceMethod
```




