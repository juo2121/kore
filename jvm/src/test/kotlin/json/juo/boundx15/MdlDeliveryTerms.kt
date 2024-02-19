package json.juo.boundx15

import kore.json.JSON
import kore.rule.Rule
import kore.rule.rule
import kore.vo.VO
import kore.vo.field.enum
import kore.vo.field.value.boolean
import kore.vo.field.value.string
import kore.vo.field.voList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal fun fileToPath(file:MdlM42File) = if(file.isNull) "" else file.url
internal fun fileToFileName(file:MdlM42File) = if(file.isNull) "" else file.name

val ValiDeliveryBoxMemo = Rule {
    rule {v,p ->
        all(v,p,trim,maxLength(100, "c@화물 요약 정보: 100자 이내로 입력해 주세요.@vali/ValiDeliveryBoxMemo/msg1"))
    }
}
val ValiDeliveryHsCode = Rule {
    rule(trim)
    rule(maxLength(20, "c@HS Code: 20자 이내로 입력해 주세요.@vali/ValiDeliveryHsCode/msg1"))
}
val ValiDeliveryContainerQty = Rule {
    rule(trim)
    group {
        rule {v,p ->
            if(v is String) {
                if ("""^[-+]?[0-9]+""".toRegex().matches(v)) v else fail(v, "int만 가능.")
            } else fail(v, "not a string")
        }
        rule {v,p ->
            if ("$v".toInt() == 0 || "$v" == "") fail(v, "0이거나 빈문자 안됨.") else v
        }
        rule { v, p ->
            if ("$v".toInt() > 99) fail(v, "99보다 크면 안됨") else v
        }
    }
}
val ValiIncotermsCat = Rule{
    rule {v,_ ->
        if(v is EnumIncotermsCat) v else fail(v,"EnumIncotermsCat가 아님")
    }
}
class MdlDeliveryTerms: VO() {
    class BoxMemo:VO() {
        var boxMemo by string{
            rule(ValiDeliveryBoxMemo)
        }
        var hsCode by string{
            rule(ValiDeliveryHsCode)
        }
    }
    var boxList by voList(::BoxMemo)
    var qty by string {
        rule(ValiDeliveryContainerQty)
    }
    var incotermsCat by enum<EnumIncotermsCat>{ rule(ValiIncotermsCat) }
    var startBriefAddr by string{
        optional()
        /*rule(ValiDeliveryEndBriefAddr) //todo ruleRouter도 필요한가?*/
    }
}

class TestDeliveryTerm {
    @Test
    fun test() {
        runBlocking {
            val vo = MdlDeliveryTerms().also {
                it.boxList = mutableListOf(MdlDeliveryTerms.BoxMemo().also {
                    it.boxMemo = "memo"
                    it.hsCode = "memo"
                })
                it.qty = "10"
                it.incotermsCat = EnumIncotermsCat.EXW
                it.startBriefAddr = "startBriefAddr"
            }
            val str = JSON.to(vo).fold(""){ acc, c-> acc + c }
            assertEquals(str,"""{"boxList":[{"boxMemo":"memo","hsCode":"memo"}],"qty":"10","incotermsCat":0,"startBriefAddr":"startBriefAddr"}""")
            val parsed = JSON.from(MdlDeliveryTerms(), flow{emit(str)}).last()
            assertEquals(parsed.boxList[0].boxMemo, "memo")
            assertEquals(parsed.boxList[0].hsCode, "memo")
            assertEquals(parsed.qty, "10")
            assertEquals(parsed.incotermsCat, EnumIncotermsCat.EXW)
            assertEquals(parsed.startBriefAddr, "startBriefAddr")
        }
    }
}

//운송 수단
enum class EnumTransportCat(val title:String, val shortTitle:String, val unknownTitle:String, val iconUrl:String, val isAirport: Boolean, val description: String): memberTest.EnumRowid<Int> {
    SEA_FCL(
        "c@해상 운송 - FCL@cat/transportCat/10", "cat/transportCat/10/shortTitle","c@항구가 정해지지 않았습니다.@cat/transportCat/10/unknownTitle",
        "//s3.ap-northeast-2.amazonaws.com/ips-upload.mobility42.io/resource/2023/08/21/655c83cfbc4fdcd65afbf05a5e94b063.svg",
        false,
        "c@FCL은 전용 컨테이너에 적재하여 운송하는<br>방식입니다. 대량의 수출 화물을 운송할 때 비용과<br>시간 측면에서 효율적입니다.@cat/transportCat/10/description"
    ){
        override val rowid = 10
    },
    SEA_LCL(
        "c@해상 운송 - LCL@cat/transportCat/11","cat/transportCat/10/shortTitle", "c@항구가 정해지지 않았습니다.@cat/transportCat/10/unknownTitle",
        "//s3.ap-northeast-2.amazonaws.com/ips-upload.mobility42.io/resource/2023/08/21/655c83cfbc4fdcd65afbf05a5e94b063.svg",
        false,
        "c@LCL은 수출 화물을 여러 고객의 화물과 함께 한 개의<br>컨테이너에 적재하여 운송하는 방식입니다. 적은 양의<br>화물을 운송할 때 효율적입니다.@cat/transportCat/11/description"
    ){
        override val rowid = 11
    },
    AIR(
        "c@항공 운송@cat/transportCat/20","c@항공@cat/transportCat/20/shortTitle", "c@공항이 정해지지 않았습니다.@cat/transportCat/20/unknownTitle",
        "//s3.ap-northeast-2.amazonaws.com/ips-upload.mobility42.io/resource/2023/08/31/50d95c9c61fe1b4c5bf9914fa6084531.svg",
        true,
        "c@소량의 화물을 빠르게 운송할 수 있습니다.@cat/transportCat/20/description"
    ){
        override val rowid = 20
    };
    companion object{
        operator fun invoke(rowid:Int) = values().find { it.rowid == rowid }?:throw Throwable("Cannot found EnumTransportCat. rowid=$rowid")
        val airTransportCat = EnumTransportCat.entries.filter { it.isAirport }.toSet()
        val seaTransportCat = EnumTransportCat.entries.filter { !it.isAirport }.toSet()
    }
}
enum class EnumIncotermsCat(val title:String): memberTest.EnumRowid<Int> {
    //수입용 인코텀스(isExport=false)
    EXW("c@EXW@cat/incotermsCat/10"){
        override val rowid = 10
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_START_PORT
        override val isExport: Boolean = false
        override val isForeignTrucking: Boolean = true
    },
    FCA("c@FCA@cat/incotermsCat/20"){
        override val rowid = 20
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_START_PORT
        override val isExport: Boolean = false
        override val isForeignTrucking: Boolean = false
    },
    FAS("c@FAS@cat/incotermsCat/30"){
        override val rowid = 30
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_START_PORT
        override val isExport: Boolean = false
        override val isForeignTrucking: Boolean = false
    },
    FOB("c@FOB@cat/incotermsCat/40"){
        override val rowid = 40
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_START_PORT
        override val isExport: Boolean = false
        override val isForeignTrucking: Boolean = false
    },
    //수출용 인코텀스(isExport=true)
    CFR("c@CFR@cat/incotermsCat/50"){
        override val rowid = 50
        //override val deliveryStepCat = EnumDeliveryStepCat.DEPARTURE
        override val isExport: Boolean = true
        override val isForeignTrucking: Boolean = false
    },
    CIF("c@CIF@cat/incotermsCat/60"){
        override val rowid = 60
        //override val deliveryStepCat = EnumDeliveryStepCat.DEPARTURE
        override val isExport: Boolean = true
        override val isForeignTrucking: Boolean = false
    },
    CPT("c@CPT@cat/incotermsCat/70"){
        override val rowid = 70
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_DESTINATION_PORT
        override val isExport: Boolean = true
        override val isForeignTrucking: Boolean = false
    },
    CIP("c@CIP@cat/incotermsCat/80"){
        override val rowid = 80
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_DESTINATION_PORT
        override val isExport: Boolean = true
        override val isForeignTrucking: Boolean = false
    },
    DAP("c@DAP@cat/incotermsCat/90"){
        override val rowid = 90
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_DESTINATION
        override val isExport: Boolean = true
        override val isForeignTrucking: Boolean = true
    },
    DPU("c@DPU@cat/incotermsCat/100"){
        override val rowid = 100
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_DESTINATION
        override val isExport: Boolean = true
        override val isForeignTrucking: Boolean = true
    },
    DDP("c@DDP@cat/incotermsCat/110"){
        override val rowid = 110
        //override val deliveryStepCat = EnumDeliveryStepCat.ARRIVAL_DESTINATION
        override val isExport: Boolean = true
        override val isForeignTrucking: Boolean = true
    };
    companion object{
        operator fun invoke(rowid:Int) = values().find { it.rowid == rowid }?:throw Throwable("Cannot found EnumIncotermsCat. rowid=$rowid")
        val exportIncotermsCat = values().filter { it.isExport }.toSet()
        val importIncotermsCat = values().filter { !it.isExport }.toSet()
    }
    abstract val isExport:Boolean // 수출인가(10~40: 0, 그외 1)
    abstract val isForeignTrucking:Boolean //타 국가의 내륙운송지정 여부 (EXW, DAP, DPU, DDP만 1)
}