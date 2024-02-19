package json.juo

import kore.json.JSON
import kore.vo.VO
import kore.vo.field.value.int
import kore.vo.field.value.string
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class TestForDoc {
    @Test
    fun test1(){
        runBlocking{
            println("test1")
        }
    }
    @Test
    fun test2(){
        runBlocking{
            println("test2")
        }
    }
    class member:VO(){
        var name by string
        var age by int
    }
    @Test
    fun test3(){
        runBlocking {
            val vo = member().also {
                it.name = "kim"
                it.age = 10
            }
            val str = JSON.to(vo).fold(""){acc, c-> acc + c }
            assertEquals(str, """{"name":"kim","age":10}""")
        }
    }
}


