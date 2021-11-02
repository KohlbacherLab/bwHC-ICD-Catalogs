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
  version: String
)

object ICDO3TCoding
{
  import Formats._

  implicit val formatICDO3TCoding = Json.format[ICDO3TCoding]
}

case class ICDO3MCoding
(
  code: ICDO3.MorphologyCode,
  display: String,
  version: String
)

object ICDO3MCoding
{
  import Formats._

  implicit val formatICDO3MCoding = Json.format[ICDO3MCoding]
}

