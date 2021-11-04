package de.bwhc.catalogs.icd



import java.time.Year

import play.api.libs.json._



object ICD10GM
{

  case class Code(value: String) extends AnyVal

  implicit val formatCode = Json.valueFormat[Code]
}


case class ICD10GMCoding
(
  code: ICD10GM.Code,
  display: String,
  version: Year
)

object ICD10GMCoding
{
  import Formats._

  implicit val formatICD10GMCoding = Json.format[ICD10GMCoding]
}
