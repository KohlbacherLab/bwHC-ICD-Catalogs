package de.bwhc.catalogs.icd.test



import scala.util.Success

import org.scalatest.AsyncFlatSpec

import de.bwhc.catalogs.icd._



object Catalogs
{

  lazy val icd10gm = ICD10GMCatalogs.getInstance.get

  lazy val icdO3 = ICDO3Catalogs.getInstance.get

}


class Tests extends AsyncFlatSpec
{


  "ICD10GMCatalogs" should "be loaded and return matches for 'Pankreas'" in {

    Catalogs.icd10gm.matches("Pankreas")
      .andThen {
        case Success(cs) => cs.foreach(println)
      }
      .map(cs => assert(!cs.isEmpty))

  }

  "ICDO3Catalogs" should "be loaded and return topography matches for 'Pankreas'" in {

    Catalogs.icdO3.topographyMatches("Pankreas")
      .andThen {
        case Success(cs) => cs.foreach(println)
      }
      .map(cs => assert(!cs.isEmpty))

  }


}
