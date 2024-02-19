package json.juo.boundx15

import kore.vo.VO
import kore.vo.field.value.int
import kore.vo.field.value.string

class MdlM42File: VO() {
    companion object{
        val NULL = {MdlM42File()}
    }
    var fileId by string{default("")}
    var pathPrefix by string{default("")}
    var path by string{default("")} //경로
    var name by string{default("")} //파일명(myimage.png)
    var size by int{default(0)} //파일크기 단위 byte
    //From FileCat
    var extension by string{default("")} //확장자. jpg, png, pdf, xlsx....
    var iconUrl by string{default("")} //파일 종류에 대한 아이콘 이미지 URL
    //From FileLocation
    var region by string{default("")} //S3 Region
    var bucket by string{default("")} //S3 Bucket
    var CDNHost by string{default("")} //S3 CDN

    val url:String  get() =
        if(isNull) ""
        else if(CDNHost.isNotEmpty()) "//${CDNHost}/${if(pathPrefix.isEmpty()) "" else "$pathPrefix/"}${path}"
        else "//s3.${region}.amazonaws.com/${bucket}/${if(pathPrefix.isEmpty()) "" else "$pathPrefix/"}${path}"

    val isNull get() = fileId.isEmpty()
}