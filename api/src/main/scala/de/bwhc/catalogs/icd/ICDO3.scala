package de.bwhc.catalogs.icd



import java.time.Year

import play.api.libs.json._


object ICDO3
{

  case class TopographyCode(value: String)
  case class MorphologyCode(value: String)

  sealed trait Version
  case object Version2014 extends Version { override def toString = "2014" }
//  case object Version2003 extends Version { override def toString = "2003" }


  object Version
  {

    def apply(y: Year): Version = {
      y.getValue match {
        case 2014 => Version2014
      }
    }

    def apply(y: Int): Version = apply(Year.of(y))

    def apply(y: String): Version = apply(y.toInt)

    def current: Version = Version2014

  }

  val versions: Set[Version] =
    Set(
      Version2014
    )

  type TCode = TopographyCode
  type MCode = MorphologyCode

  implicit val formatTCode: Format[TCode] =
    new Format[TCode]{
      def reads(js: JsValue): JsResult[TCode] = js.validate[String].map(TopographyCode(_))
      def writes(t: TCode): JsValue = JsString(t.value)
    }

  implicit val formatMCode: Format[MCode] =
    new Format[MCode]{
      def reads(js: JsValue): JsResult[MCode] = js.validate[String].map(MorphologyCode(_))
      def writes(m: MCode): JsValue = JsString(m.value)
    }

  implicit val formatVersion: Format[Version] =
    new Format[Version]{
      def reads(js: JsValue): JsResult[Version] = js.validate[String].map(Version(_))
      def writes(v: Version): JsValue = JsString(v.toString)
    }


}


case class ICDO3TCoding
(
  code: ICDO3.TopographyCode,
  display: Option[String] = None,
  version: ICDO3.Version = ICDO3.Version.current
)

object ICDO3TCoding
{
  implicit val formatICDO3TCoding = Json.format[ICDO3TCoding]
}

case class ICDO3MCoding
(
  code: ICDO3.MorphologyCode,
  display: Option[String] = None,
  version: ICDO3.Version = ICDO3.Version.current
)

object ICDO3MCoding
{
  implicit val formatICDO3MCoding = Json.format[ICDO3MCoding]
}

