package coroutine

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class Test1 {
    val channel = Channel<Int>(5)
    suspend fun r1(){
        println("R1 ${channel.receive()}")
        r2()
    }
    suspend fun r2(){
        println("R2 ${channel.receive()}")
        r1()
    }
    @Test
    fun test1(){
        runBlocking {
            launch {
                for (i in 1..5) {
                    println("send1 $i")
                    channel.send(i)
                }
            }
            launch {
                for (i in 1..5) {
                    println("send2 $i")
                    channel.send(i*i)
                }
            }
            delay(10)
            r1()
        }
    }
}
