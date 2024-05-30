package ru.netology.deadlock

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

fun main() {
    val resourceA = ReentrantLock()
    val resourceB = ReentrantLock()

    val consumerA = Consumer("A")
    val consumerB = Consumer("B")

    val t1 = thread {
        consumerA.lockFirstAndTrySecond(resourceA, resourceB)
    }
    val t2 = thread {
        consumerB.lockFirstAndTrySecond(resourceB, resourceA)
    }

    t1.join()
    t2.join()

    println("main successfully finished")
}

class Consumer(private val name: String) {
    fun lockFirstAndTrySecond(first: ReentrantLock, second: ReentrantLock) {
        while (true) {
            if (first.tryLock()) {
                if (second.tryLock()) {
                    try {
                        println("$name locked first, sleep and wait for second")
                        Thread.sleep(1000)
                        println("$name locked second")
                        return
                    } finally {
                        second.unlock()
                        first.unlock()
                    }
                }else{
                    first.unlock()
                }
            }
            Thread.sleep(100)
        }
    }
}