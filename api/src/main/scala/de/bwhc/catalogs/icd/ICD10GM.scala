package de.bwhc.catalogs.icd



import java.time.Year

import play.api.libs.json._



object ICD10GM
{

  case class Code(value: String)

/*
  object Version extends Enumeration
  {
    type Version = Value

    val Version2021 = Value("2021")
    val Version2020 = Value("2020")
    val Version2019 = Value("2019")

    def apply(y: Year): Version = withName(y.toString)

    def apply(y: String): Version = apply(Year.of(y.toInt))

    def current: Version = apply(Year.now)

  }
*/

  implicit val formatCode: Format[Code] =
    Format(
      Reads(_.validate[String].map(Code(_))),
      Writes(c => JsString(c.value))
    )
/*
    new Format[Code]{
      def reads(js: JsValue): JsResult[Code] = js.validate[String].map(Code(_))
      def writes(c: Code): JsValue = JsString(c.value)
    }
*/
//  implicit val formatVersion: Format[Version.Value] =
//    Json.formatEnum(Version)


}


case class ICD10GMCoding
(
  code: ICD10GM.Code,
  display: Option[String] = None,
  version: Year = Year.now
//  version: ICD10GM.Version.Value = ICD10GM.Version.current
)

object ICD10GMCoding
{
/*
  implicit val formatYear: Format[Year] =
    Format(
      Reads(_.validate[Int].map(Year.of)),
      Writes(y => JsString(y.toString))
    )
*/
  import Formats._

  implicit val formatICD10GMCoding = Json.format[ICD10GMCoding]
}
