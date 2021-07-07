package de.bwhc.catalogs.icd



import java.time.Year

import play.api.libs.json._


object ICDO3
{

  case class TopographyCode(value: String)
  case class MorphologyCode(value: String)

/*
  object Version extends Enumeration
  {
    type Version = Value

//    val Version2003 = Value("2003")
    val Version2014 = Value("2014")

    def apply(y: Year): Version = withName(y.toString)

    def apply(y: String): Version = apply(Year.of(y.toInt))

    def current: Version = Version2014

  }
*/

  type TCode = TopographyCode
  type MCode = MorphologyCode

  implicit val formatTCode: Format[TCode] =
    Format(
      Reads(_.validate[String].map(TopographyCode(_))),
      Writes(t => JsString(t.value))
    )
/*
    new Format[TCode]{
      def reads(js: JsValue): JsResult[TCode] = js.validate[String].map(TopographyCode(_))
      def writes(t: TCode): JsValue = JsString(t.value)
    }
*/

  implicit val formatMCode: Format[MCode] =
    Format(
      Reads(_.validate[String].map(MorphologyCode(_))),
      Writes(m => JsString(m.value))
    )
/*
    new Format[MCode]{
      def reads(js: JsValue): JsResult[MCode] = js.validate[String].map(MorphologyCode(_))
      def writes(m: MCode): JsValue = JsString(m.value)
    }
*/
//  implicit val formatVersion: Format[Version.Value] =
//    Json.formatEnum(Version)

}


case class ICDO3TCoding
(
  code: ICDO3.TopographyCode,
  display: Option[String] = None,
  version: Year = Year.now
//  version: ICDO3.Version.Value = ICDO3.Version.current
)

object ICDO3TCoding
{
  import Formats._

  implicit val formatICDO3TCoding = Json.format[ICDO3TCoding]
}

case class ICDO3MCoding
(
  code: ICDO3.MorphologyCode,
  display: Option[String] = None,
  version: Year = Year.now
//  version: ICDO3.Version.Value = ICDO3.Version.current
)

object ICDO3MCoding
{
  import Formats._

  implicit val formatICDO3MCoding = Json.format[ICDO3MCoding]
}

