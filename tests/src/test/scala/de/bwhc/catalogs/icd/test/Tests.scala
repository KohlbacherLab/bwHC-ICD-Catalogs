package de.bwhc.catalogs.icd.test



import scala.util.Success

import org.scalatest.{FlatSpec,AsyncFlatSpec}

import de.bwhc.catalogs.icd._



object Catalogs
{

  lazy val icd10gm = ICD10GMCatalogs.getInstance.get

  lazy val icdO3 = ICDO3Catalogs.getInstance.get

}

class Tests extends AsyncFlatSpec
{

  import Catalogs._


  "ICD10GMCatalogs" should "be loaded and return matches for 'Pankreas'" in {

    icd10gm.matches("Pankreas")
      .map(cs => assert(!cs.isEmpty))

  }

/*
  "ICD10GMCatalogs" should "be loaded and printed" in {

    for {
      cs <- icd10gm.codings
      oncos = cs.filter(_.code.value.startsWith("C"))
      _ = oncos.foreach(c => println(c.code.value + "   " + c.display.get))
    } yield assert(!oncos.isEmpty)

  }
*/


  "ICDO3Catalogs" should "be loaded and return topography matches for 'Pankreas'" in {

    icdO3.topographyMatches("Pankreas")
      .map(cs => assert(!cs.isEmpty))

  }

/*
  "ICDO3Catalogs" should "be loaded and print topography codes" in {

    for {
      cs <- icdO3.topographyCodings
      _ = cs.foreach(c => println(c.code.value + "   " + c.display.get))
    } yield assert(!cs.isEmpty)

  }

  "ICDO3Catalogs" should "be loaded and print Morphology codes" in {

    for {
      cs <- icdO3.morphologyCodings
      _ = cs.foreach(c => println(c.code.value + "   " + c.display.get))
    } yield assert(!cs.isEmpty)

  }
*/


}
