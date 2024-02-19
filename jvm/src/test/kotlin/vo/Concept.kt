package vo

import kotlin.reflect.KProperty

inline val Concept.VO.int get() = Concept.FieldInt
class Concept {
    interface Field<VALUE:Any>{
        operator fun getValue(target:VO, prop:KProperty<*>):VALUE = target[prop.name] as VALUE
        operator fun setValue(target:VO, prop:KProperty<*>, value:VALUE){
            target[prop.name] = value
        }
    }
    abstract class VO{

        private val values:HashMap<String, Any> = hashMapOf()
        operator fun get(key:String):Any? = values[key]
        operator fun set(key:String, value:Any){
            values[key] = value
        }
    }

    object FieldString:Field<String>
    object FieldInt:Field<Int>
    class Test1:VO(){
        var a1 by FieldString

    }
    fun test(){
        val t1 = Test1()
        val t2 = Test1()
        val t3 = Test1()

    }


}