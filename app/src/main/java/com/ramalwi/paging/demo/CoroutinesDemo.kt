package com.ramalwi.paging.demo

import kotlinx.coroutines.*

suspend fun main() {
    cancellation()
}

@OptIn(DelicateCoroutinesApi::class)
fun compareScopes(){
    val g = GlobalScope
    val m = MainScope()

    println(g.coroutineContext)
    println("**********************************")
    println(m.coroutineContext)
}

suspend fun throwExceptions(){
    val job = GlobalScope.launch { // root coroutine with launch
        println("Throwing exception from launch")
        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
    }
    job.join()
    println("Joined failed job")
    val deferred = GlobalScope.async { // root coroutine with async
        println("Throwing exception from async")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
    }

    try {
        deferred.await()
        println("Unreached")
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
}

suspend fun exceptionHandler(){
    val scope = CoroutineScope(Job())
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }

    val job = scope.launch(handler) {
        println("first job")
        throw AssertionError()
    }
    val deferred = scope.async(handler) {
        println("second job")
        throw ArithmeticException()
    }
    joinAll(job, deferred)
}

suspend fun exceptionHandlerWithSupervisorJob(){
    val scope = CoroutineScope(SupervisorJob())
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }

    with(scope){
        val job = launch(handler) {
            println("first job")
            throw AssertionError()
        }
        val deferred = async(handler) {
            println("second job is not cancelled")
        }
        joinAll(job, deferred)
    }
}

suspend fun cancellation(){
    val scope = CoroutineScope(Job())

    with(scope){
        val job = launch {
            val child = launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    println("Child is cancelled")
                }
            }
            yield()
            println("Cancelling child")
            child.cancel()
            child.join()
            yield()
            println("Parent is not cancelled")
        }
        job.join()
    }
}

