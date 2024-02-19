package coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class Test2{
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)
    @Test
    fun test1(){
        runBlocking {
            launch {
                for(i in 1..5) {
                    println("send1 $i")
                    channel1.send(i)
                }
            }
            launch {
                for(i in channel1) {
                    println("send2 $i")
                    channel2.send(i * i)
                }
            }
            launch {
                for(i in channel2) {
                    println("receive $i")
                }
            }
        }
    }
    @Test
    fun test2(){
        runBlocking {

            launch {
                for(i in 1..5) {
                    println("send1 $i")
                    channel1.send(i)
                }
            }
            var c:Channel<Int> = Channel<Int>(1)
            launch {
                c = pipe(channel1){it*it}
            }
            launch {
                for(i in c) {
                    println("receive $i")
                    channel2.send(i * i)
                }
            }
        }
    }
    suspend fun <FROM, TO> CoroutineScope.pipe(from:Channel<FROM>, block:suspend (FROM)->TO):Channel<TO> = Channel<TO>(1).also{
        println("1")
        for(i in from){
            println("2 - $i, ${block(i)}")
            it.send(block(i))
        }
    }
}