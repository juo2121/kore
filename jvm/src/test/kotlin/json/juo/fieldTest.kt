package json.juo

import kore.error.E
import kore.json.JSON
import kore.vo.VO
import kore.vo.VOSum
import kore.vo.field.*
import kore.vo.field.list.booleanList
import kore.vo.field.map.*
import kore.vo.field.value.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class testJuo {
    /** vo value 테스트*/
    class Test1:VO(){
        var a by boolean
        var b by double
        var c by float
        var d by int
        var e by long
        var f by short
        var g by string
        var h by uint
        var i by ulong
        var j by ushort
    }
    @Test
    fun test1(){
        runBlocking{
            val vo = Test1().also {
                it.a = true
                it.b = 2.1
                it.c = 1.2f
                it.d = 4
                it.e = 4L
                it.f = 11.toShort()
                it.g = "a"
                it.h = 2u
                it.i = 2u
                it.j = 2u
            }
            val str = JSON.to(vo).fold(""){acc, c-> acc + c }
            assertEquals(str, """{"a":true,"b":2.1,"c":1.2,"d":4,"e":4,"f":11,"g":"a","h":2,"i":2,"j":2}""")
            val parsed = JSON.from(Test1(), flow{emit(str)}).last()
            assertEquals(vo.a, parsed.a)
            assertEquals(vo.b, parsed.b)
            assertEquals(vo.c, parsed.c)
            assertEquals(vo.d, parsed.d)
            assertEquals(vo.e, parsed.e)
        }
    }
    /** vo list 테스트*/
    class Test2:VO(){
        var a by booleanList
        /* var b by doubleList
         var c by floatList
         var d by intList
         var e by longList
         var f by shortList
         var g by stringList
         var h by uintList
         var i by ulongList
         var j by ushortList*/
    }
    @Test
    fun test2(){
        runBlocking{
            val vo = Test2().also {
                it.a = mutableListOf(false)
                /* it.b = mutableListOf(1.2,1.2)
                 it.c = mutableListOf(1.1f,1.2f)
                 it.d = mutableListOf(1,2)
                 it.e = mutableListOf(1L,2L)
                 it.f = mutableListOf(11.toShort(),12.toShort())
                 it.g = mutableListOf("11","12")
                 it.h = mutableListOf(1u,2u)
                 it.i = mutableListOf(1u,2u)
                 it.j = mutableListOf(1u,2u)*/
            }
            val str = JSON.to(vo).fold(""){acc, c-> acc + c }
            assertEquals(str, """{"a":[false]}""")
            /*assertEquals(str, """{"a":[false],"b":[1.2,1.2],"c":[1.1,1.2],"d":[1,2],"e":[1,2],"f":[11,12],"g":["11","12"],"h":[1,2],"i":[1,2],"j":[1,2]}""")*/
            val parsed = JSON.from(Test2(), flow{emit(str)}).last()
            assertEquals(vo.a, parsed.a)
          /*  assertEquals(vo.b, parsed.b)
            assertEquals(vo.c, parsed.c)
            assertEquals(vo.d, parsed.d)
            assertEquals(vo.e, parsed.e)
            assertEquals(vo.f, parsed.f)
            assertEquals(vo.g, parsed.g)
            assertEquals(vo.h, parsed.h)
            assertEquals(vo.i, parsed.i)
            assertEquals(vo.j, parsed.j)*/
        }
    }
    /** vo map 테스트*/
    class Test3:VO(){
        var a by booleanMap
        var b by doubleMap
        var c by floatMap
        var d by intMap
        var e by longMap
        var f by shortMap
        var g by stringMap
        var h by uintMap
        var i by ulongMap
        var j by ushortMap
    }
    @Test
    fun test3(){
    /** vo enum 테스트*/
        runBlocking{
            val vo = Test3().also {
                it.a = mutableMapOf("true" to true)
                it.b = mutableMapOf( "1.2" to 1.2)
                it.c = mutableMapOf( "1.2f" to 1.2f)
                it.d = mutableMapOf( "12" to 12)
                it.e = mutableMapOf( "12L" to 12L)
                it.f = mutableMapOf( "12.toShort()" to 12.toShort())
                it.g = mutableMapOf( "1.2" to "1.2")
                it.h = mutableMapOf( "12Int" to 12u)
                it.i = mutableMapOf( "12uLong" to 12u)
                it.j = mutableMapOf( "12Short" to 12u)
            }
            val str = JSON.to(vo).fold(""){acc, c-> acc + c }
            assertEquals(str, """{"a":{"true":true},"b":{"1.2":1.2},"c":{"1.2f":1.2},"d":{"12":12},"e":{"12L":12},"f":{"12.toShort()":12},"g":{"1.2":"1.2"},"h":{"12Int":12},"i":{"12uLong":12},"j":{"12Short":12}}""")
            val parsed = JSON.from(Test3(), flow{emit(str)}).last()
            assertEquals(vo.a, parsed.a)
        }
    }

    /** vo enum 테스트*/
    class Test4:VO() {
        enum class E{A,B,C}
      /*  var a by enum<E>()
        var b by enum<E>()
        var c by enum<E>()
        var d by enum<E>(E.A)
        var e by enum<E>(E.B)
        var f by enum<E>(E.C)
        var g by enum<E> { default(E.A) }
        var h by enum<E> {
            exclude()
            default(E.C)
        }
        var i by enum<E> {
            optional() //Todo optional확인!
            default(E.C)
        }
        var j by enumList<E>()
        var k by enumList<E>(mutableListOf(E.A,E.B,E.C))
        var l by enumList<E>(E.A,E.B,E.C)
        var m by enumList<E> { default(E.A,E.B,E.C) }
        var n by enumList<E> { default(mutableListOf(E.A,E.B,E.C)) }
        var o by enumList<E> {
            exclude()
            default(E.A)
        }
        var p by enumMap<E>()
        var q by enumMap<E>(mutableMapOf("key2" to E.B))*/
        var r by enumMap<E>("key1" to E.B,"key2" to E.C)
        var s by enumMap<E>{
            exclude()
            default("key1" to E.A)
        }
    }
    @Test
    fun test4() {
        runBlocking{
            val vo = Test4().also {
               /* it.a = Test4.E.A
                it.b = Test4.E.B
                it.c = Test4.E.C
                it.i = Test4.E.C*/
            }
            val str = JSON.to(vo).fold(""){acc, c-> acc + c }
            assertEquals(str, """
                {
                "r":{"key1":1,"key2":2}
                }""".trimIndent().replace("\n",""))
            val parsed = JSON.from(Test4(), flow{emit(str)}).last()
            /*assertEquals(vo.a, parsed.a)
            assertEquals(vo.b, parsed.b)
            assertEquals(vo.c, parsed.c)
            assertEquals(vo.d, parsed.d)
            assertEquals(vo.e, parsed.e)
            assertEquals(vo.f, parsed.f)
            assertEquals(vo.g, parsed.g)
            assertEquals(vo.h, parsed.h)
            assertEquals(vo.i, parsed.i)
            assertEquals(vo.j, parsed.j)
            (0 until vo.k.size).forEach { assertEquals(vo.k[it], parsed.k[it]) }
            (0 until vo.l.size).forEach { assertEquals(vo.l[it], parsed.l[it]) }
            (0 until vo.m.size).forEach { assertEquals(vo.m[it], parsed.m[it]) }
            (0 until vo.n.size).forEach { assertEquals(vo.n[it], parsed.n[it]) }
            assertEquals(vo.o[0], parsed.o[0])
            assertEquals(vo.p["key1"], parsed.p["key1"])
            assertEquals(vo.q["key2"], parsed.q["key2"])*/
            assertEquals(vo.r["key1"], parsed.r["key1"])
            assertEquals(vo.r["key2"], parsed.r["key2"])
            assertEquals(vo.s["key1"], parsed.s["key1"])
        }
    }
    /**vo 테스트*/
    class Test5:VO() {
        class MdlMember():VO() {
            var name by string
            var addr by string
            var age by int
        }
        var member by vo(::MdlMember)
        var member2 by vo(::MdlMember) {
            default {
                MdlMember().also {
                    it.name = "k"
                    it.age = 20
                    it.addr = "한국"
                }
            }
        }
        var memberList by voList(::MdlMember)
        var memberList2 by voListDefault(::MdlMember) {
            mutableListOf(
                MdlMember().also {
                    it.name = "park"
                    it.age = 20
                    it.addr = "한국"
                },
                MdlMember().also {
                    it.name = "kim"
                    it.age = 20
                    it.addr = "한국"
                },
                MdlMember().also {
                    it.name = "k3"
                    it.age = 20
                    it.addr = "한국"
                }
            )
        }
        var memberList3 by voList(::MdlMember) {
            this.exclude()
            this.default { mutableListOf(
                MdlMember().also {
                    it.name = "k2"
                    it.age = 20
                    it.addr = "한국"
                }
            )
            }
        }
        var memberMap by voMap(::MdlMember)
        var memberMap2 by voMapDefault(::MdlMember) {
            mutableMapOf("mem2" to MdlMember().also {
                it.name = "kim"
                it.addr = "suwon"
                it.age = 18
            })
        }
        var memberMap3 by voMap(::MdlMember) {
            default {
                mutableMapOf("mem2" to MdlMember().also {
                    it.name = "kim"
                    it.addr = "suwon"
                    it.age = 18
                })
            }
        }
    }
    @Test
    fun test5() {
        runBlocking{
            val vo = Test5().also {
                it.member = Test5.MdlMember().also {
                    it.name = "kim"
                    it.addr = "suwon"
                    it.age = 18
                }
                it.memberList = mutableListOf(
                    Test5.MdlMember().also {
                        it.name = "m1"
                        it.addr = "incheon"
                        it.age = 20
                    },
                    Test5.MdlMember().also {
                        it.name = "m2"
                        it.addr = "busan"
                        it.age = 18
                    },
                    Test5.MdlMember().also {
                        it.name = "m3"
                        it.addr = "jeju"
                        it.age = 19
                    }
                )
                it.memberMap = mutableMapOf("mem" to Test5.MdlMember().also {
                    it.name = "kim"
                    it.addr = "suwon"
                    it.age = 18
                })
            }
            val str = JSON.to(vo).fold(""){acc, c-> acc + c }
            assertEquals(str, """
                {
                "member":{"name":"kim","addr":"suwon","age":18},
                "member2":{"name":"k","addr":"한국","age":20},
                "memberList":[{"name":"m1","addr":"incheon","age":20},{"name":"m2","addr":"busan","age":18},{"name":"m3","addr":"jeju","age":19}],
                "memberList2":[{"name":"park","addr":"한국","age":20},{"name":"kim","addr":"한국","age":20},{"name":"k3","addr":"한국","age":20}],
                "memberMap":{"mem":{"name":"kim","addr":"suwon","age":18}},
                "memberMap2":{"mem2":{"name":"kim","addr":"suwon","age":18}},
                "memberMap3":{"mem2":{"name":"kim","addr":"suwon","age":18}}
                }
                """.trimIndent().replace("\n",""))
            val parsed = JSON.from(Test5(), flow{
                //read dbRecord1
                emit(str.substring(0,10))
                //read dbRecord2
                emit(str.substring(10))
            }).last()
            assertEquals(vo.member.name,parsed.member.name)
            assertEquals(vo.member.age,parsed.member.age)
            assertEquals(vo.member.addr,parsed.member.addr)
            assertEquals(vo.member2.name,parsed.member2.name)
            assertEquals(vo.member2.age,parsed.member2.age)
            assertEquals(vo.member2.addr,parsed.member2.addr)
            parsed.memberList.forEachIndexed{idx,it ->
                assertEquals(vo.memberList[idx].name,parsed.memberList[idx].name)
                assertEquals(vo.memberList[idx].age,parsed.memberList[idx].age)
                assertEquals(vo.memberList[idx].addr,parsed.memberList[idx].addr)
            }
            parsed.memberList2.forEachIndexed{idx,it ->
                assertEquals(vo.memberList2[idx].name,parsed.memberList2[idx].name)
                assertEquals(vo.memberList2[idx].age,parsed.memberList2[idx].age)
                assertEquals(vo.memberList2[idx].addr,parsed.memberList2[idx].addr)
            }
            assertEquals(vo.memberMap["mem"]?.name,parsed.memberMap["mem"]?.name)
            assertEquals(vo.memberMap["mem"]?.age,parsed.memberMap["mem"]?.age)
            assertEquals(vo.memberMap["mem"]?.addr,parsed.memberMap["mem"]?.addr)
            assertEquals(vo.memberMap2["mem2"]?.name,parsed.memberMap2["mem2"]?.name)
            assertEquals(vo.memberMap2["mem2"]?.age,parsed.memberMap2["mem2"]?.age)
            assertEquals(vo.memberMap2["mem2"]?.addr,parsed.memberMap2["mem2"]?.addr)
            assertEquals(vo.memberMap3["mem2"]?.name,parsed.memberMap3["mem2"]?.name)
            assertEquals(vo.memberMap3["mem2"]?.age,parsed.memberMap3["mem2"]?.age)
            assertEquals(vo.memberMap3["mem2"]?.addr,parsed.memberMap3["mem2"]?.addr)
        }
    }
    /**sum 테스트*/
    class Test6:VO() {
        sealed class Sum:VO(){
            companion object: VOSum<Sum>(::A, ::B)
            class A:Sum(){
                var name by string
            }
            class B:Sum(){
                var age by int
            }
        }
        var sum by sum(Sum)
        var sumList by sumList(Sum)
        var sumListDefault by sumListDefault(Sum) {
            mutableListOf(Sum.A().also { it.name = "a" })
        }
        var sumList2 by sumList(Sum) {
            this.default {
                mutableListOf(Sum.A().also {
                    it.name = "a"
                })
            }
        }
        var sumList3 by sumList(Sum) {
            this.exclude()
            this.default {
                mutableListOf(Sum.A().also {
                    it.name = "a"
                })
            }
        }
        var sumMap by sumMap(Sum)
        var sumMapDefault by sumMapDefault(Sum) {
            mutableMapOf("map2" to Test6.Sum.A().also {
                it.name = "a"
            })
        }
        var sumMap2 by sumMap(Sum) {
            this.default {
                mutableMapOf("map2" to Test6.Sum.A().also {
                    it.name = "a"
                })
            }
        }
        var sumMap3 by sumMap(Sum) {
            this.exclude()
            this.default {
                mutableMapOf("map2" to Test6.Sum.A().also {
                    it.name = "a"
                })
            }
        }
    }
    @Test
    fun test6() {
        runBlocking{
            val vo = Test6().also {
                it.sum = Test6.Sum.A().also {
                    it.name = "a"
                }
                it.sumList = mutableListOf(Test6.Sum.A().also {
                    it.name = "b"
                })
                it.sumMap = mutableMapOf("map1" to Test6.Sum.A().also {
                    it.name = "a"
                })
            }
            val str = JSON.to(vo).fold(""){acc, c-> acc + c }
            assertEquals(str, """
                {
                "sum":{"name":"a"},
                "sumList":[{"name":"b"}],
                "sumListDefault":[{"name":"a"}],
                "sumList2":[{"name":"a"}],
                "sumMap":{"map1":{"name":"a"}},
                "sumMapDefault":{"map2":{"name":"a"}},
                "sumMap2":{"map2":{"name":"a"}}
                }
                """.trimIndent().replace("\n",""))
            val parsed = JSON.from(Test6(), flow{emit(str)}).last()
            assertEquals(vo.sum["name"],parsed.sum["name"])
            parsed.sumList.forEachIndexed {idx ,_-> assertEquals(vo.sumList[idx]["name"],parsed.sumList[idx]["name"]) }
            parsed.sumListDefault.forEachIndexed {idx ,_-> assertEquals(vo.sumListDefault[idx]["name"],parsed.sumListDefault[idx]["name"]) }
            parsed.sumList2.forEachIndexed {idx ,_-> assertEquals(vo.sumList2[idx]["name"],parsed.sumList2[idx]["name"]) }
            parsed.sumList3.forEachIndexed {idx ,_-> assertEquals(vo.sumList3[idx]["name"],parsed.sumList3[idx]["name"]) }
            assertEquals(vo.sumMap["map1"]?.get("name"), parsed.sumMap["map1"]?.get("name"))
            assertEquals(vo.sumMapDefault["map1"]?.get("name"), parsed.sumMapDefault["map1"]?.get("name"))
            assertEquals(vo.sumMap2["map1"]?.get("name"), parsed.sumMap2["map1"]?.get("name"))
            assertEquals(vo.sumMap3["map1"]?.get("name"), parsed.sumMap3["map1"]?.get("name"))
        }
    }
}
