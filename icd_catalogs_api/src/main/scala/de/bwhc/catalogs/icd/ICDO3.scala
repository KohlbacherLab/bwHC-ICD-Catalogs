package de.bwhc.catalogs.icd



import java.time.Year



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

    def current: Version = Version2014

  }

  val versions: Set[Version] =
    Set(
      Version2014
    )

}


case class ICDO3TCoding
(
  code: ICDO3.TopographyCode,
  display: String,
  version: ICDO3.Version = ICDO3.Version2014
)

case class ICDO3MCoding
(
  code: ICDO3.MorphologyCode,
  display: String,
  version: ICDO3.Version = ICDO3.Version2014
)

