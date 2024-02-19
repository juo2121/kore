package json.juo.boundx15
import kore.json.JSON
import kore.rule.*
import kore.rule.Rule.DSL.all
import kore.vo.VO
import kore.vo.field.value.long
import kore.vo.field.value.string
import kore.vo.field.voList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddressTEST {
    @Test
    fun test() {
        runBlocking {
            val vo = MdlAddressAddStart().also {
                it.title = "123"
                it.addr1 = "dsf"
                it.addr2 = "1"
                it.contactList = mutableListOf(MdlAddressCommonVali.Contact().also {
                    it._addressRowid = 1L
                    it.name = "kim"
                    it.phone = "01088889999"
                    it.email = "email11@naver.com"
                })
                it.contactListStart = mutableListOf(MdlAddressAddStart.ContactStart().also {
                    it.name = "kim"
                    it.phone = "01088889999"
                    it.email = "email@naver.com"
                })
            }
            val str = JSON.to(vo).fold(""){ acc, c-> acc + c }
            assertEquals(str,"""{"title":"123","addr1":"dsf","addr2":"1","contactList":[{"name":"kim","phone":"01088889999","email":"email11@naver.com"}],"contactListStart":[{"name":"kim","phone":"01088889999","email":"email@naver.com"}]}""")
            val parsed = JSON.from(MdlAddressAddStart(), flow{emit(str)}).last()
            assertEquals(vo.title, parsed.title)
            assertEquals(vo.addr1, parsed.addr1)
            assertEquals(vo.addr2, parsed.addr2)
            assertEquals(vo.contactList[0].name, parsed.contactList[0].name)
            assertEquals(vo.contactList[0].phone, parsed.contactList[0].phone)
            assertEquals(vo.contactList[0].email, parsed.contactList[0].email)
            assertEquals(vo.contactListStart[0].name, parsed.contactListStart[0].name)
            assertEquals(vo.contactListStart[0].phone, parsed.contactListStart[0].phone)
            assertEquals(vo.contactListStart[0].email, parsed.contactListStart[0].email)
        }
    }
}

abstract class MdlAddressCommonVali:VO(){
    open class Contact:VO(true){
        companion object {
            val DEFAULT_LIST = { mutableListOf<Contact>() }
        }
        var _addressRowid by long{
            exclude()
        }
        var name by string
        var phone by string {
            val phoneR = Rule {
                rule(Rule.minLength(11, "11자리보다 짧음."))
                group {
                    group {
                        rule {v, _ ->
                            if((v as String).startsWith("010")) v else fail(v, "010으로 시작 안합니다.")
                        }
                        or
                        rule {v, _ ->
                            if((v as String).endsWith("8888")) v else fail(v, "8888로 끝나지 않습니다.")
                        }
                    }
                    or
                    group {
                        rule {v, _ ->
                            if((v as String).startsWith("070")) v else fail(v, "070으로 시작 안합니다.")
                        }
                        rule {v, _ ->
                            if((v as String).endsWith("9999")) v else fail(v, "9999로 끝나지 않습니다.")
                        }
                    }
                }
            }
            rule(phoneR)
        }
        var email by string {
            ruleItem(Rule.email)
        }
    }
    var title by string {
        ruleItem(Rule.minMax(1, 64, "주소 이름을 입력해 주세요.", "주소 이름: 64자 이내로 입력해 주세요."))
    }
    var addr1 by string {
        val addrRule =  Rule {
            rule { v, param ->
                all(
                    v, param,
                    Rule.trim,
                    Rule.minLength(1, "주소를 입력해 주세요."),
                    Rule.maxLength(300, "주소: 300자 이내로 입력해 주세요.")
                )
            }

        }
        rule(addrRule)
    }
    var addr2 by string {
        val addrRule = Rule {
            rule(Rule.trim)
            rule(Rule.minLength(1, "주소를 입력해 주세요."))
            rule(Rule.maxLength(300, "주소: 300자 이내로 입력해 주세요."))
        }
        rule(addrRule)
    }
    var contactList by voList(::Contact)
}
//주소록 등록
open class MdlAddressAdd:MdlAddressCommonVali(){
    //서버에서 할당해 줘야함! 프론트에서 쓰는 게 아님
    //여기에 vali 넣지 마세요
    var _masterRowid by long{
        exclude()
        default(0L)
    }

    //서버에서 할당해 줘야함! 프론트에서 쓰는 게 아님
    //여기에 vali 넣지 마세요
    var _addressRowid by long{
        exclude()
        default(0L)
    }
}
class MdlAddressAddStart:MdlAddressAdd() {
    class ContactStart:Contact()
    var contactListStart by voList(::ContactStart)

}