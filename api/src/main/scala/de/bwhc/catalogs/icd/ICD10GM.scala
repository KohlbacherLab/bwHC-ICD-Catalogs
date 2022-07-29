package de.bwhc.catalogs.icd



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
  version: String,
  superClass: Option[ICD10GM.Code],
  subClasses: Set[ICD10GM.Code]
)

object ICD10GMCoding
{

  implicit val formatICD10GMCoding = Json.format[ICD10GMCoding]
}
