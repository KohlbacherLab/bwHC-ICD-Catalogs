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

  "ICDO3Catalogs" should "be loaded and return topography matches for 'Pankreas'" in {

    icdO3.topographyMatches("Pankreas")
      .map(cs => assert(!cs.isEmpty))

  }

}
