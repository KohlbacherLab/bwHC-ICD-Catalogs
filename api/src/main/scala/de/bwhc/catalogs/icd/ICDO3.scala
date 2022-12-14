package de.bwhc.catalogs.icd



//import java.time.Year

import play.api.libs.json._


object ICDO3
{

  case class TopographyCode(value: String) extends AnyVal
  case class MorphologyCode(value: String) extends AnyVal

  type TCode = TopographyCode
  type MCode = MorphologyCode

  implicit val formatTCode: Format[TCode] =
    Json.valueFormat[TCode]

  implicit val formatMCode: Format[MCode] =
    Json.valueFormat[MCode]

}


case class ICDO3TCoding
(
  code: ICDO3.TopographyCode,
  display: String,
  version: String,
  superClass: Option[ICDO3.TopographyCode],
  subClasses: Set[ICDO3.TopographyCode]
)

object ICDO3TCoding
{
  implicit val formatICDO3TCoding = Json.format[ICDO3TCoding]
}

case class ICDO3MCoding
(
  code: ICDO3.MorphologyCode,
  display: String,
  version: String,
  superClass: Option[ICDO3.MorphologyCode],
  subClasses: Set[ICDO3.MorphologyCode]
)

object ICDO3MCoding
{
  implicit val formatICDO3MCoding = Json.format[ICDO3MCoding]
}

