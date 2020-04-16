package de.bwhc.catalogs.icd



import java.time.Year


import play.api.libs.json._

object ICD10GM
{

  case class Code(value: String)

  sealed trait Version
  case object Version2020 extends Version { override def toString = "2020" }
  case object Version2019 extends Version { override def toString = "2019" }
  //TODO: previous versions as well?


  object Version
  {

    def apply(y: Year): Version = {
      y.getValue match {
        case 2020 => Version2020
        case 2019 => Version2019
      }
    }

    def apply(y: Int): Version = apply(Year.of(y))

    def apply(y: String): Version = apply(y.toInt)

    def current: Version = apply(Year.now)

  }

  val versions: Set[Version] =
    Set(
      Version2020,
      Version2019
    )

  implicit val formatCode: Format[Code] =
    new Format[Code]{
      def reads(js: JsValue): JsResult[Code] = js.validate[String].map(Code(_))
      def writes(c: Code): JsValue = JsString(c.value)
    }

  implicit val formatVersion: Format[Version] =
    new Format[Version]{
      def reads(js: JsValue): JsResult[Version] = js.validate[String].map(Version(_))
      def writes(v: Version): JsValue = JsString(v.toString)
    }

}


case class ICD10GMCoding
(
  code: ICD10GM.Code,
  display: Option[String] = None,
  version: ICD10GM.Version = ICD10GM.Version.current
)

object ICD10GMCoding
{
  implicit val formatICD10GMCoding = Json.format[ICD10GMCoding]
}
