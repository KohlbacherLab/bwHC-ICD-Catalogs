package de.bwhc.catalogs.icd



import java.time.Year


import play.api.libs.json._

object ICD10GM
{

  case class Code(value: String)

  object Version extends Enumeration
  {
    type Version = Value

    val Version2020 = Value("2020")
    val Version2019 = Value("2019")
    //TODO: previous versions

    def apply(y: Year): Version = withName(y.toString)

    def apply(y: String): Version = apply(Year.of(y.toInt))

    def current: Version = apply(Year.now)

  }

  implicit val formatCode: Format[Code] =
    new Format[Code]{
      def reads(js: JsValue): JsResult[Code] = js.validate[String].map(Code(_))
      def writes(c: Code): JsValue = JsString(c.value)
    }

  implicit val formatVersion: Format[Version.Value] =
    Json.formatEnum(Version)

}


case class ICD10GMCoding
(
  code: ICD10GM.Code,
  display: Option[String] = None,
  version: ICD10GM.Version.Value = ICD10GM.Version.current
)

object ICD10GMCoding
{
  implicit val formatICD10GMCoding = Json.format[ICD10GMCoding]
}
