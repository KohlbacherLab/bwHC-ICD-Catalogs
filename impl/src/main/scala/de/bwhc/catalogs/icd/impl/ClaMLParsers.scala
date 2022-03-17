package de.bwhc.catalogs.icd.impl


import scala.util.matching.Regex

import java.io.InputStream

import scala.xml._

import de.bwhc.catalogs.icd.{
  ICDO3,
  ICD10GM,
  ICD10GMCoding,
  ICDO3TCoding,
  ICDO3MCoding
}


trait ClaMLParser[C]
{

  def parse(in: InputStream): Iterable[(C,String,Option[C],Set[C])]

}


object ClaMLICD10GMParser extends ClaMLParser[ICD10GM.Code]
{

//  private val icd10code = "([A-Z]\\d{2}\\.\\d{1,2})".r

  def parse(in: InputStream): Iterable[(ICD10GM.Code,String,Option[ICD10GM.Code],Set[ICD10GM.Code])] = {

    val claml = XML.load(in)

    val codings =
      (claml \\ "Class")
        .filter(
          cl => (cl \@ "kind") == "category"
        )
        .map { cl =>
          val code   = ICD10GM.Code(cl \@ "code")
          val rubric = (cl \ "Rubric")
          val label  = (rubric.find(_ \@ "kind" == "preferredLong")
                          .orElse(rubric.find(_ \@ "kind" == "preferred")).get) \ "Label" text

          val superClass = Option(cl \ "SuperClass" \@ "code").map(ICD10GM.Code(_))

          val subClasses = (cl \ "SubClass").map((_ \@ "code")).toSet.map(ICD10GM.Code(_))

          (code,label,superClass,subClasses)
        }

    codings

  }

}



object ClaMLICDO3TParser extends ClaMLParser[ICDO3.TopographyCode]
{

//  private val icdO3Tcode = "(C\\d{2}\\.\\d{1})".r
  private val icdO3Tcode = "(C\\d{2})".r

  def parse(in: InputStream): Iterable[(ICDO3.TopographyCode,String,Option[ICDO3.TopographyCode],Set[ICDO3.TopographyCode])] = {

    val claml = XML.load(in)

    val codings =
      (claml \\ "Class")
        .filter(
          cl => (cl \@ "kind") == "category" &&
                icdO3Tcode.findPrefixOf((cl \@ "code")).isDefined
//                icdO3Tcode.findFirstIn((cl \@ "code")).isDefined
        )
        .map { cl =>
          val code   = ICDO3.TopographyCode(cl \@ "code")
          val label  = (cl \ "Rubric").find(_ \@ "kind" == "preferred").get \ "Label" text

          val superClass = Option(cl \ "SuperClass" \@ "code").map(ICDO3.TopographyCode(_))

          val subClasses = (cl \ "SubClass").map((_ \@ "code")).toSet.map(ICDO3.TopographyCode(_))

          (code,label,superClass,subClasses)
        }

    codings

  }

}


object ClaMLICDO3MParser extends ClaMLParser[ICDO3.MorphologyCode]
{

//  private val icdO3Mcode = "(\\d{4}\\:\\d{1})".r
  private val icdO3Mcode = "(\\d{3})".r
//  private val icdO3Mcode = "(\\d{4})".r

  def parse(in: InputStream): Iterable[(ICDO3.MorphologyCode,String,Option[ICDO3.MorphologyCode],Set[ICDO3.MorphologyCode])] = {

    val claml = XML.load(in)

    val codings =
      (claml \\ "Class")
        .filter(
          cl => (cl \@ "kind") == "category" &&
                icdO3Mcode.findPrefixOf((cl \@ "code")).isDefined
//                icdO3Mcode.findFirstIn((cl \@ "code")).isDefined
        )
        .map { cl =>

          // The replacement of : (colon) with / (slash) is required
          // because ClaML ICD-O-3-M code have format xxxx:x but "official" display format is xxxx/x
          val code   = ICDO3.MorphologyCode((cl \@ "code").replace(":","/"))

          val label  = (cl \ "Rubric").find(_ \@ "kind" == "preferred").get \ "Label" text

          val superClass = Option(cl \ "SuperClass" \@ "code").map(ICDO3.MorphologyCode(_))

          val subClasses = (cl \ "SubClass").map((_ \@ "code")).toSet.map(ICDO3.MorphologyCode(_))

          (code,label,superClass,subClasses)
        }

    codings

  }

}

