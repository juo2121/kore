package json.juo.boundx15

import json.juo.trimAll
import kore.json.JSON
import kore.vo.VO
import kore.vo.field.enum
import kore.vo.field.value.boolean
import kore.vo.field.value.string
import kore.vo.field.vo
import kore.vo.field.voList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class InvoiceTest {
    @Test
    fun test() {
        runBlocking {
            val invoice = MdlInvoice().also {
                it.deliveryUpdateRowid = "1"
                it.deliveryUpdate3Rowid = "2"
                it.deliveryRowid = "3"
                it.deliveryUpdateCat = EnumDeliveryUpdateCat.DRAFT_REQUEST
                it.title = "title"
                it.userName = "userName"
                it.profileUrl = "profileUrl"
                it.contents = "contents"
                it.isHidden = true
                it.currencyCode = "c"
                it._fileId = "1"
                it.price = "price"
                it.isComplete = true
                it.trackingNum = "3"
                it.deliveryTitle = "deliveryTitle"
                it.scRowid = "2"
                it.scNum = "1"
                it._portRowid1 = "1"
                it._portRowid2 = "2"
            }
            val strtPort = MdlPort().also {
                it.portRowid = "1"
                it.code = "c"
                it.name = "n"
                it.country = "c"
                it.isAirport = true
                it.hasCfs = true
                it.timezoneCatRowid = "1"
                it.timezoneCatCode = "c"
                it.timezoneLangTitle = "t"
            }
            val endPort = MdlPort().also {
                it.portRowid = "2"
                it.code = "c"
                it.name = "b"
                it.country = "c"
                it.isAirport = true
                it.hasCfs = true
                it.timezoneCatRowid = "1"
                it.timezoneCatCode = "c"
                it.timezoneLangTitle = "t"
            }
            val file = MdlM42File().also { it.fileId = "1" }
            val invoiceResult = MdlInvoiceResult(invoiceList = listOf(invoice), portList = listOf(strtPort,endPort), fileList = listOf(file))
            val str = JSON.to(invoiceResult).fold(""){ acc, c-> acc + c }
            assertEquals(str,"""{
                "_invoiceList":[{
                    "deliveryUpdateRowid":"1","deliveryUpdate3Rowid":"2","deliveryRowid":"3","deliveryUpdateCat":0,"title":"title","userName":"userName","profileUrl":"profileUrl","contents":"contents","isHidden":true,"_fileId":"1",
                    "_file":{"fileId":"","pathPrefix":"","path":"","name":"","size":0,"extension":"","iconUrl":"","region":"","bucket":"","CDNHost":""},
                    "currencyCode":"c","price":"price","isComplete":true,"trackingNum":"3","deliveryTitle":"deliveryTitle","scRowid":"2","scNum":"1","_portRowid1":"1","_portRowid2":"2"}
                ],
                "_portList":[
                    {"portRowid":"1","code":"c","name":"n","country":"c","isAirport":true,"hasCfs":true,"timezoneCatRowid":"1","timezoneCatCode":"c","timezoneLangTitle":"t"},
                    {"portRowid":"2","code":"c","name":"b","country":"c","isAirport":true,"hasCfs":true,"timezoneCatRowid":"1","timezoneCatCode":"c","timezoneLangTitle":"t"}
                ],
                "_fileList":[{"fileId":"1","pathPrefix":"","path":"","name":"","size":0,"extension":"","iconUrl":"","region":"","bucket":"","CDNHost":""}]}""".trimAll())
            val parsed = JSON.from(MdlInvoiceResult(), flow{emit(str)}).last()
            parsed.invoiceList.forEach {it ->
                invoiceResult.invoiceList.forEachIndexed { _, mdlInvoice ->
                    assertEquals(it.deliveryUpdateRowid, mdlInvoice.deliveryUpdateRowid)
                    assertEquals(it.deliveryUpdate3Rowid, mdlInvoice.deliveryUpdate3Rowid)
                    assertEquals(it.deliveryRowid, mdlInvoice.deliveryRowid)
                    assertEquals(it.deliveryUpdateCat, mdlInvoice.deliveryUpdateCat)
                    assertEquals(it.title, mdlInvoice.title)
                    assertEquals(it.userName, mdlInvoice.userName)
                    assertEquals(it.profileUrl, mdlInvoice.profileUrl)
                    assertEquals(it.contents, mdlInvoice.contents)
                    assertEquals(it.isHidden, mdlInvoice.isHidden)
                    assertEquals(it._fileId, mdlInvoice._fileId)
                    assertEquals(it.currencyCode, mdlInvoice.currencyCode)
                    assertEquals(it.price, mdlInvoice.price)
                    assertEquals(it.isComplete, mdlInvoice.isComplete)
                    assertEquals(it.trackingNum, mdlInvoice.trackingNum)
                    assertEquals(it.deliveryTitle, mdlInvoice.deliveryTitle)
                    assertEquals(it.scRowid, mdlInvoice.scRowid)
                    assertEquals(it.scNum, mdlInvoice.scNum)
                    assertEquals(it._portRowid1, mdlInvoice._portRowid1)
                    assertEquals(it._portRowid2, mdlInvoice._portRowid2)
                    it.startPort.also { sp ->
                        assertEquals(sp.code,mdlInvoice.startPort.code)
                        assertEquals(sp.name,mdlInvoice.startPort.name)
                        assertEquals(sp.country,mdlInvoice.startPort.country)
                        assertEquals(sp.isAirport,mdlInvoice.startPort.isAirport)
                        assertEquals(sp.hasCfs,mdlInvoice.startPort.hasCfs)
                        assertEquals(sp.timezoneCatRowid,mdlInvoice.startPort.timezoneCatRowid)
                        assertEquals(sp.timezoneCatCode,mdlInvoice.startPort.timezoneCatCode)
                        assertEquals(sp.timezoneLangTitle,mdlInvoice.startPort.timezoneLangTitle)
                    }
                    it.endPort.also { sp ->
                        assertEquals(sp.code,mdlInvoice.endPort.code)
                        assertEquals(sp.name,mdlInvoice.endPort.name)
                        assertEquals(sp.country,mdlInvoice.endPort.country)
                        assertEquals(sp.isAirport,mdlInvoice.endPort.isAirport)
                        assertEquals(sp.hasCfs,mdlInvoice.endPort.hasCfs)
                        assertEquals(sp.timezoneCatRowid,mdlInvoice.endPort.timezoneCatRowid)
                        assertEquals(sp.timezoneCatCode,mdlInvoice.endPort.timezoneCatCode)
                        assertEquals(sp.timezoneLangTitle,mdlInvoice.endPort.timezoneLangTitle)
                    }
                }
            }
        }
    }
    enum class EnumDeliveryUpdateCat(val title: String) : memberTest.EnumRowid<Int> {
        DRAFT_REQUEST("c@견적 요청@cat/deliveryupdatecat/10") {
            override val rowid = 10
        },
        CANCEL_DRAFT_REQUEST("c@견적 요청 취소@cat/deliveryupdatecat/11") {
            override val rowid = 11
        },
        DRAFT_ARRIVAL("c@견적 등록@cat/deliveryupdatecat/20") {
            override val rowid = 20
        },
        RESERVATION_CONFIRMED("c@부킹 요청@cat/deliveryupdatecat/30") {
            override val rowid = 30
        },
        CANCEL_RESERVATION_CONFIRMED("c@부킹 요청 취소@cat/deliveryupdatecat/31") {
            override val rowid = 31
        },
        BOOKING_CONFIRMED("c@부킹 완료@cat/deliveryupdatecat/40") {
            override val rowid = 40
        },

        /**사용하지 않음 - 240111 ERD*/
        NOTICE_SENDED("c@안내문 발송@cat/deliveryupdatecat/50") {
            override val rowid = 50
        },
        BILLING("c@인보이스@cat/deliveryupdatecat/60") { //deliveryupdate3
            override val rowid = 60
        },

        /**사용하지 않음 - 240111 ERD*/
        DOCUMENT_SENDED("c@문서 전달@cat/deliveryupdatecat/70") { //deliveryupdate4
            override val rowid = 70
        },
        REQUEST_UPLOAD_FROM_SHIPPER("c@파일@cat/deliveryupdatecat/80") { //deliveryupdate5
            override val rowid = 80
        },

        /**메시지로 바뀔예정 - 240111 ERD*/
        QNA("c@1:1 문의@cat/deliveryupdatecat/90") {
            override val rowid = 90
        },

        /**메시지 댓글로 바뀔 예정 - 240111 ERD*/
        QNA_REPLY("c@1:1 문의 답변@cat/deliveryupdatecat/91") {
            override val rowid = 91
        },

        /**사용하지 않음 - 240111 ERD*/
        UPDATE_DELIVERY("c@운송 현황 업데이트@cat/deliveryupdatecat/100") {
            override val rowid = 100
        },
        DELIVERY_CANCEL("c@운송 취소@cat/deliveryupdatecat/110") {
            override val rowid = 110
        },

        /**사용하지 않음 - 240111 ERD*/
        DELIVERY_FAIL("c@운송 실패@cat/deliveryupdatecat/120") {
            override val rowid = 120
        };

        companion object {
            operator fun invoke(rowid: Int) =
                entries.find { it.rowid == rowid } ?: throw Throwable("Cannot found EnumUpdateCat. rowid=$rowid")
        }
    }
    class MdlPort : VO() {
        var portRowid by string
        var code by string
        var name by string
        var country by string
        var isAirport by boolean
        var hasCfs by boolean
        var timezoneCatRowid by string
        var timezoneCatCode by string
        var timezoneLangTitle by string
        val isKorea get() = code.indexOf("KR") == 0
        val timezoneTitle get() = if (timezoneLangTitle.isEmpty()) timezoneCatCode else timezoneLangTitle
    }
    class MdlInvoice : VO() {
        var deliveryUpdateRowid by string
        var deliveryUpdate3Rowid by string
        var deliveryRowid by string //운송 일련번호
        var deliveryUpdateCat by enum<EnumDeliveryUpdateCat>() //운송 카테고리
        var title by string
        var userName by string //작성자 이름. 없으면 빈값
        var profileUrl by string //작성자 프로필. 없으면 빈값
        var contents by string //내용
        var isHidden by boolean
        //인보이스
        var _fileId by string
        var _file by vo(::MdlM42File) {
            default(MdlM42File.NULL)
        }
        var currencyCode by string
        var price by string
        var isComplete by boolean
        var trackingNum by string
        var deliveryTitle by string
        var scRowid by string { default("0") }
        var scNum by string
        var _portRowid1 by string
        lateinit var startPort: MdlPort
            private set
        var _portRowid2 by string
        lateinit var endPort: MdlPort
            private set
        val isSc get() = scRowid != "0"
        val fileUrl get() = fileToPath(_file)
        val fileName get() = fileToFileName(_file)
        internal fun setData(result: MdlInvoiceResult) {
            _file = result._fileList.find { it.fileId == _fileId } ?: error("인보이스 파일을 찾지 못함")
            startPort = result._portList.find { it.portRowid == _portRowid1 } ?: error("인보이스 출발항 찾지 못함")
            endPort = result._portList.find { it.portRowid == _portRowid2 } ?: error("인보이스 출발항 찾지 못함")
        }
    }
    class MdlInvoiceResult : VO() {
        companion object {
            val none get() = invoke(listOf(), listOf(), listOf())
            operator fun invoke(
                invoiceList: List<MdlInvoice>,
                portList: List<MdlPort>,
                fileList: List<MdlM42File>
            ) = MdlInvoiceResult().also {
                it._invoiceList = invoiceList.toMutableList()
                it._portList = portList.toMutableList()
                it._fileList = fileList.toMutableList()
            }
        }
        private var _invoiceList by voList(::MdlInvoice)
        internal var _portList by voList(::MdlPort)
        internal var _fileList by voList(::MdlM42File)
        val invoiceList by lazy {
            _invoiceList.forEach {
                it.setData(this)
            }
            _invoiceList
        }
    }
}

