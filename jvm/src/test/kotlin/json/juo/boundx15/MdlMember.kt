package json.juo.boundx15

import json.juo.testJuo
import json.juo.trimAll
import kore.json.JSON
import kore.vo.VO
import kore.vo.field.enum
import kore.vo.field.value.boolean
import kore.vo.field.value.int
import kore.vo.field.value.long
import kore.vo.field.value.string
import kore.vo.field.vo
import kore.vo.field.voList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class memberTest {
    class MdlMemberForwarderJoinParam : VO() {
        var email by string {
            addSetTask { vo, s, v ->
                if (v is String) v else false
            }
        }
        var name by string
    }
    @Test
    fun testForwarderJoinParam() {
        runBlocking {
            val vo = MdlMemberForwarderJoinParam().also {
                it.email = "jykim2121@naver.com"
                it.name = "kim"
            }
            val str = JSON.to(vo).fold("") { acc, c -> acc + c }
            println("str : $str")
            assertEquals(
                str, """{"email":"jykim2121@naver.com","name":"kim"}""".trimIndent()
            )
        }
    }

    interface EnumRowid<T>{
        val rowid:T
        fun toDbString() = "$rowid"
    }
    enum class EnumM42MemberAcCat(val title:String): EnumRowid<Int> {
        REQUEST("가입요청"){
            override val rowid = 10
        },
        ACTIVE("가입(활동중)"){
            override val rowid = 20
        },
        WITHDRAW("탈퇴"){
            override val rowid = 30
        },
        REQUESTWITHDRAW("탈퇴 요청"){
            override val rowid = 31
        },
        DORMANT("휴면"){
            override val rowid = 40
        };
        companion object{
            operator fun invoke(rowid:Int) = values().find { it.rowid == rowid }?:throw Throwable("찾을 수 없는 계정상태입니다. rowid=$rowid")
        }
    }
    class Member : VO() {
        var memberAcRowid by long
        var memberAcCat by enum<EnumM42MemberAcCat>{default(EnumM42MemberAcCat.ACTIVE)}
        var profileUrl by string
        var groupList by voList(::MdlMemberShipperGrp)
        var bizFile by vo(::MdlM42File) { default(MdlM42File.NULL) }
    }
    @Test
    fun test() {
        runBlocking {
            val vo = Member().also {
                it.memberAcRowid = 1
                it.profileUrl = "url123"
                it.groupList = mutableListOf(
                    MdlMemberShipperGrp(MdlM42MemberGrpWithCnt().also {
                        it.memberGrpRowid = 123
                        it.grpCatTitle = "grpCat"
                        it.isEditable = true
                        it.memberCnt = 1
                    }), MdlMemberShipperGrp(MdlM42MemberGrpWithCnt().also {
                        it.memberGrpRowid = 123
                        it.grpCatTitle = "grpCat"
                        it.isEditable = false
                        it.memberCnt = 1
                    })
                )
            }
            val str = JSON.to(vo).fold("") { acc, c -> acc + c }
            assertEquals(
                str,
                """{
                    "memberAcRowid":1,
                    "memberAcCat":1,
                    "profileUrl":"url123",
                    "groupList":[{"memberGrpRowid":123,"memberCnt":1,"grpCatTitle":"grpCat","isEditable":true},{"memberGrpRowid":123,"memberCnt":1,"grpCatTitle":"grpCat","isEditable":false}],
                    "bizFile":{"fileId":"","pathPrefix":"","path":"","name":"","size":0,"extension":"","iconUrl":"","region":"","bucket":"","CDNHost":""}
                    }
                """.trimAll()
            )
            val parsed = JSON.from(Member(), flow{emit(str)}).last()
            assertEquals(vo.memberAcRowid, parsed.memberAcRowid)
            assertEquals(vo.profileUrl, parsed.profileUrl)
            parsed.groupList.forEachIndexed{idx,it ->
                assertEquals(vo.groupList[idx].memberGrpRowid , it.memberGrpRowid)
                assertEquals(vo.groupList[idx].grpCatTitle , it.grpCatTitle)
                assertEquals(vo.groupList[idx].isEditable , it.isEditable)
                assertEquals(vo.groupList[idx].memberCnt, it.memberCnt)
            }
        }
    }
}


open class MdlM42MemberGrp : VO() {
    var memberGrpRowid by long
    var grpCatTitle by string
    var isEditable by boolean
}

class MdlM42MemberGrpWithCnt : MdlM42MemberGrp() {
    var memberCnt by int
}

class MdlMemberShipperGrp : VO() {
    companion object {
        operator fun invoke(g: MdlM42MemberGrp) = MdlMemberShipperGrp().also {
            it.grpCatTitle = g.grpCatTitle
            it.isEditable = g.isEditable
            it.memberGrpRowid = g.memberGrpRowid
        }

        operator fun invoke(g: MdlM42MemberGrpWithCnt) = MdlMemberShipperGrp().also {
            it.grpCatTitle = g.grpCatTitle
            it.isEditable = g.isEditable
            it.memberGrpRowid = g.memberGrpRowid
            it.memberCnt = g.memberCnt
        }
    }

    var memberGrpRowid by long
    var memberCnt by int { default(0) }
    var grpCatTitle by string
    var isEditable by boolean
}
