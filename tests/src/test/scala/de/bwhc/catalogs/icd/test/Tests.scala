package de.bwhc.catalogs.icd.test



import scala.util.Success
import scala.util.matching.Regex
import org.scalatest.{FlatSpec,AsyncFlatSpec}
import org.scalatest.OptionValues._

import de.bwhc.catalogs.icd._



object Catalogs
{

  lazy val icd10gm = ICD10GMCatalogs.getInstance.get

  lazy val icdO3 = ICDO3Catalogs.getInstance.get

}

//class Tests extends AsyncFlatSpec
class Tests extends FlatSpec
{

  import Catalogs._


  "ICD10GMCatalogs" should "be loaded and printed" in {

    assert(
      !icd10gm.codings()
        .filter(_.code.value.startsWith("C"))
        .isEmpty
    )
  }


  "ICDO3Catalogs" should "be loaded and return topography matches for 'Pankreas'" in {

    assert(!icdO3.topographyMatches("Pankreas").isEmpty)

  }


  val bracketRef = """(\[.+\])""".r.unanchored

  "ICD-O-3 Morphology entries" should "correctly contain references to T-Code" in {

    val coding =
      icdO3.morphologyCodings()
        .find(_.code.value == "9382/3")
        .value

    assert(
      bracketRef.matches(coding.display)
    )

  }


}
