package de.bwhc.catalogs.icd.impl


import scala.util.Try
import scala.util.control.Breaks._
import scala.util.matching.Regex
import scala.collection.mutable.MutableList

import java.io.{
  File,
  InputStream,
  FileInputStream
}

/*
import javax.xml.stream.{
  XMLInputFactory,
  XMLStreamReader => XMLStream
}
*/

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

//  val factory = XMLInputFactory.newFactory

  def parse(in: InputStream): Iterable[(C,String)]

/*
  def readCoding(xml: XMLStream): (String,String) = {

    var code  = xml.getAttributeValue(null,"code")
    var label = new String

    breakable {

      while (!(xml.isEndElement && xml.getLocalName.equals("Class"))){

        xml.next

        if (xml.isStartElement
           && xml.getLocalName.equals("Rubric")
           && Option(xml.getAttributeValue(null,"kind")).exists(k => k.equals("preferredLong") || k.equals("preferred"))){

           label = readLabel(xml)
           break
        }
      }
    }
    (code,label)
  }


  def readLabel(xml: XMLStream): String = {

    var label = new String

    breakable {
      while (!(xml.isEndElement && xml.getLocalName.equals("Rubric"))){
      
        xml.next

        if (xml.isStartElement && xml.getLocalName.equals("Label")){

          xml.next

          if (xml.isCharacters){
             label = xml.getText
             break
          }
        }
      }
    }
    label
  }
*/

}


object ClaMLICD10GMParser extends ClaMLParser[ICD10GM.Code]
{

  private val icd10code = "([A-Z]\\d{2}\\.\\d{1,2})".r

  def parse(in: InputStream): Iterable[(ICD10GM.Code,String)] = {

    val claml = XML.load(in)

    val codings =
      (claml \\ "Class")
        .filter(
          cl => (cl \@ "kind") == "category" &&
                icd10code.findFirstIn((cl \@ "code")).isDefined
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


/*
  def parse(in: InputStream): Iterable[(ICD10GM.Code,String)] = {

    val xml = factory.createXMLStreamReader(in)

    val codings = MutableList.empty[(ICD10GM.Code,String)]

    while (xml.hasNext){

      xml.next

      if (xml.isStartElement
         && xml.getLocalName.equals("Class")
         && Option(xml.getAttributeValue(null,"kind")).exists(_.equals("category"))
         && icd10code.findFirstIn(xml.getAttributeValue(null,"code")).isDefined ){ // read only "leaf" categories, e.g. C04.5

         val (code,display) = readCoding(xml)

         val cd  = (ICD10GM.Code(code), display)
         codings += cd
      }
    }
    codings

  }
*/

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

/*
    val xml = factory.createXMLStreamReader(in)

    val codings = MutableList.empty[(ICDO3.TopographyCode,String)]

    // Read all Classes until the start of the M-code chapter
    while (xml.hasNext
           && !(xml.isStartElement
                && xml.getLocalName.equals("Class")
                && Option(xml.getAttributeValue(null,"code")).exists(_.equals("M")))){

      xml.next

      if (xml.isStartElement
         && xml.getLocalName.equals("Class")
         && Option(xml.getAttributeValue(null,"kind")).exists(_.equals("category"))
         && icdO3Tcode.findFirstIn(xml.getAttributeValue(null,"code")).isDefined ){ // read only "leaf" categories, e.g. C04.5

         val (code,display) = readCoding(xml)

         val cd = (ICDO3.TopographyCode(code), display)
         codings += cd
      }
    }
    codings
*/
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

          (ICDO3.MorphologyCode(code),label)
        }

    codings

/*
    val xml = factory.createXMLStreamReader(in)

    val codings = MutableList.empty[(ICDO3.MorphologyCode,String)]

    // Skip all T-code Classes
    while (xml.hasNext
           && !(xml.isStartElement
                && xml.getLocalName.equals("Class")
                && Option(xml.getAttributeValue(null,"code")).exists(_.equals("M")))){

      xml.next
    }

    while (xml.hasNext){

      xml.next

      if (xml.isStartElement
         && xml.getLocalName.equals("Class")
         && Option(xml.getAttributeValue(null,"kind")).exists(_.equals("category"))){

         val (code,display) = readCoding(xml)

         val cd = (ICDO3.MorphologyCode(code), display)
         codings += cd
      }
    }
    codings
*/
  }

}

