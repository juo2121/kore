package rule

import kore.rule.*
import kore.vo.VO
import kore.vo.field.value.int
import kore.vo.field.value.string
import kotlin.test.Test
import kotlin.test.assertEquals

class Test1 {
    val toString:Rule.Item = Rule.Item{v, _->
        v.toString()
    }
    @Test
    fun test1() {
        /**a && (b || c)*/
        val r = Rule{
            rule{v, param->
                any(v, param, string, int)
            }
            group {
                rule{v, _->
                    if(v is String) "$v*" else fail(v, "not a string")
                }
                or
                rule{v, _->
                    if(v is Int) v * 2 else fail(v, "not an int")
                }
            }
        }
        assertEquals("abc*", r("abc")())
        /*assertEquals("a*", r(1.1)())*/
        /*assertEquals(12, r(6)())*/
    }
    class Test2:VO(){
        var a by string{
            rule(Rule{
                rule(Rule.minLength(2, "최소 길이보다 작습니다."))
            })
        }
        var b by int{
            anyRuleItem(Rule.rangeNumber(1,5), Rule.Item{v, _->
                if((v as Int) > 10) v else fail(v, "10보다 작습니다.")
            })
        }
    }
    @Test
    fun test2() {
        val r = Rule{
            rule(rangeNumber(1, 10))
        }
        assertEquals(6, r(6)())
        assertEquals("not in range 1..10 : value:11", "${r(11).isEffected()?.message}")
    }
    /**rule TEST*/
    @Test
    fun test3() {
        val ruleLong = Rule{ rule(long) }
        val ruleFloat = Rule{ rule(float) }
        val ruleDouble = Rule{ rule(double) }
        val ruleChar = Rule{ rule(char) }
        val ruleBoolean = Rule{ rule(boolean) }
        val ruleTrim1 = Rule{ rule(trim) }
        val ruleBlank1 = Rule{ rule(blank()) }
        val ruleBlank2 = Rule{ rule(blank("blank아님")) }
        val ruleNotBlank1 = Rule{ rule(notBlank()) }
        val ruleNotBlank2 = Rule{ rule(notBlank("블랭크!")) }
        val ruleEmpty1 = Rule{ rule(empty()) }
        val ruleEmpty2 = Rule{ rule(empty("empty아님")) }
        val ruleMinLength1 = Rule{rule(minLength(3))}
        val ruleMinLength2 = Rule{rule(minLength(3,"shorter than 3"))}
        val ruleMaxLength1 = Rule{rule(maxLength(3))}
        val ruleMaxLength2 = Rule{rule(maxLength(3,"longer than 3"))}
        val ruleEmail = Rule{rule(email)}
        assertEquals("not a long : value:10.0", "${ruleLong(10.0).isEffected()?.message}")
        assertEquals("not a float : value:10", "${ruleFloat(10).isEffected()?.message}")
        assertEquals("not a double : value:10", "${ruleDouble(10).isEffected()?.message}")
        assertEquals("not a char : value:1", "${ruleChar("1").isEffected()?.message}")
        assertEquals("not a boolean : value:1", "${ruleBoolean("1").isEffected()?.message}")
        assertEquals("not a string : value:1", "${ruleTrim1(1).isEffected()?.message}")
        assertEquals("not a string : value:1", "${ruleBlank1(1).isEffected()?.message}")
        assertEquals("not blank : value:1", "${ruleBlank1("1").isEffected()?.message}")
        assertEquals("blank아님 : value:1", "${ruleBlank2("1").isEffected()?.message}")
        assertEquals("not a string : value:1", "${ruleNotBlank1(1).isEffected()?.message}")
        assertEquals("blank : value:", "${ruleNotBlank1("").isEffected()?.message}")
        assertEquals("블랭크! : value:", "${ruleNotBlank2("").isEffected()?.message}")
        assertEquals("not a string : value:1", "${ruleEmpty1(1).isEffected()?.message}")
        assertEquals("not empty : value:1", "${ruleEmpty1("1").isEffected()?.message}")
        assertEquals("empty아님 : value:1", "${ruleEmpty2("1").isEffected()?.message}")
        assertEquals("not a string : value:1", "${ruleMinLength1(1).isEffected()?.message}")
        assertEquals("최소 길이보다 짧습니다. : value:11", "${ruleMinLength1("11").isEffected()?.message}")
        assertEquals("shorter than 3 : value:11", "${ruleMinLength2("11").isEffected()?.message}")
        assertEquals("not a string : value:1", "${ruleMaxLength1(1).isEffected()?.message}")
        assertEquals("최대 길이보다 깁니다. : value:1111", "${ruleMaxLength1("1111").isEffected()?.message}")
        assertEquals("longer than 3 : value:1111", "${ruleMaxLength2("1111").isEffected()?.message}")
        assertEquals("not a string : value:1", "${ruleEmail(1).isEffected()?.message}")
        assertEquals("이메일 형식이 아닙니다. : value:abcd@naver", "${ruleEmail("abcd@naver").isEffected()?.message}")
        assertEquals("이메일 형식이 아닙니다. : value:abcd.com", "${ruleEmail("abcd.com").isEffected()?.message}")
        assertEquals("이메일 형식이 아닙니다. : value:@abcd.com", "${ruleEmail("@abcd.com").isEffected()?.message}")
        assertEquals("이메일 형식이 아닙니다. : value:@abcd.com", "${ruleEmail("@abcd.com").isEffected()?.message}")
    }
    @Test
    fun test4(){
        class Test4:VO(){
            var name by string{
                anyRule(
                    Rule {
                        rule(int)
                    },
                    Rule {
                        rule(minLength(2, "2글자 이상이어야 합니다."))
                    },
                )
            }
        }
        Test4().also {
            it.name = "aa"
        }
    }
    @Test
    fun test5(){
        class Test5:VO(){
            var name by string{
                allRule(
                    Rule {
                        rule(string)
                    },
                    Rule {
                        rule(minLength(1, "2글자 이상이어야 합니다."))
                    },
                )
            }
        }
        Test5().also {
            it.name = "a"
        }
    }
}





