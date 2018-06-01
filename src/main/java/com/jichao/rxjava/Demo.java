package com.jichao.rxjava;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Callable;

///https://praveer09.github.io/technology/2016/02/29/rxjava-part-3-multithreading/
public class Demo {
    public static void main(String[] args) throws InterruptedException {

        exampleObserveOn();
        Thread.sleep(5*1000);
    }

    public static void exampleSubscribeOn(){
        Observable.fromCallable(thatReturnsNumberOne())
                .subscribeOn(Schedulers.newThread())
                .map(numberToString())
                .subscribe(printResult());
    }

    public static void exampleObserveOn() {
//        Observable.fromCallable(thatReturnsNumberOne())
//                .map(numberToString())
//                .observeOn(Schedulers.newThread()) //subscriber on different thread
//                .subscribe(printResult());

        Observable.fromCallable(thatReturnsNumberOne())
                .observeOn(Schedulers.newThread()) // operator on differnt thread
                .map(numberToString())
                .subscribe(printResult());

        Observable.fromCallable(thatReturnsNumberOne())
                .observeOn(Schedulers.newThread()) // operator on differnt thread
                .map(numberToString())
                .observeOn(Schedulers.newThread()) //subscriber on different thread
                .subscribe(printResult());

    }

    private static Callable<Integer> thatReturnsNumberOne() {
        return () -> {
            System.out.println("Observable thread: " + Thread.currentThread().getName());
            return 1;
        };
    }

    private static Function<Integer, String> numberToString() {
        return number -> {
            System.out.println("Operator thread: " + Thread.currentThread().getName());
            return String.valueOf(number);
        };
    }

    private static Consumer<String> printResult() {
        return s -> {
            System.out.println("Subscriber thread: " + Thread.currentThread().getName());
            System.out.println(s);
        };


    }
}
