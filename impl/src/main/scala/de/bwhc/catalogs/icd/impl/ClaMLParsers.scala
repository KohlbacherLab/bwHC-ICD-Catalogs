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

  def parse(in: InputStream): Iterable[(C,String)]

}


object ClaMLICD10GMParser extends ClaMLParser[ICD10GM.Code]
{

  private val icd10code = "([A-Z]\\d{2}\\.\\d{1,2})".r

  def parse(in: InputStream): Iterable[(ICD10GM.Code,String)] = {

    val claml = XML.load(in)

    val codings =
      (claml \\ "Class")
        .filter(
          cl => (cl \@ "kind") == "category"
        )
        .map { cl =>
          val code   = (cl \@ "code")
          val rubric = (cl \ "Rubric")
          val label  = (rubric.find(_ \@ "kind" == "preferredLong")
                          .orElse(rubric.find(_ \@ "kind" == "preferred")).get) \ "Label" text

          (ICD10GM.Code(code),label)
        }

    codings

  }

}



object ClaMLICDO3TParser extends ClaMLParser[ICDO3.TopographyCode]
{

  private val icdO3Tcode = "(C\\d{2}\\.\\d{1})".r

  def parse(in: InputStream): Iterable[(ICDO3.TopographyCode,String)] = {

    val claml = XML.load(in)

    val codings =
      (claml \\ "Class")
        .filter(
          cl => (cl \@ "kind") == "category" &&
                icdO3Tcode.findFirstIn((cl \@ "code")).isDefined
        )
        .map { cl =>
          val code   = (cl \@ "code")
          val label  = (cl \ "Rubric").find(_ \@ "kind" == "preferred").get \ "Label" text

          (ICDO3.TopographyCode(code),label)
        }

    codings

  }

}


object ClaMLICDO3MParser extends ClaMLParser[ICDO3.MorphologyCode]
{

  private val icdO3Mcode = "(\\d{4}\\:\\d{1})".r

  def parse(in: InputStream): Iterable[(ICDO3.MorphologyCode,String)] = {

    val claml = XML.load(in)

    val codings =
      (claml \\ "Class")
        .filter(
          cl => (cl \@ "kind") == "category" &&
                icdO3Mcode.findFirstIn((cl \@ "code")).isDefined
        )
        .map { cl =>
          val code   = (cl \@ "code")
          val label  = (cl \ "Rubric").find(_ \@ "kind" == "preferred").get \ "Label" text

          // The replacement of : (colon) with / (slash) is required
          // because ClaML ICD-O-3-M code have format xxxx:x but "official" display format is xxxx/x
          (ICDO3.MorphologyCode(code.replace(":","/")),label)
        }

    codings

  }

}

