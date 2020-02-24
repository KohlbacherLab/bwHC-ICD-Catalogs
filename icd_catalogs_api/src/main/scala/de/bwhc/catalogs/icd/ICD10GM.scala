package de.bwhc.catalogs.icd



import java.time.Year


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

    def current: Version = apply(Year.now)

  }

  val versions: Set[Version] =
    Set(
      Version2020,
      Version2019
    )

}


case class ICD10GMCoding
(
  code: ICD10GM.Code,
  display: String,
  version: ICD10GM.Version = ICD10GM.Version.current
)

