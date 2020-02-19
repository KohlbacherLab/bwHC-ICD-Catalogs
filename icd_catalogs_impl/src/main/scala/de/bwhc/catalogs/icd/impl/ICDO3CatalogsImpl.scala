package de.bwhc.catalogs.icd.impl



import scala.concurrent.Future

import de.bwhc.catalogs.icd.{
  ICDO3,
  ICDO3TCoding,
  ICDO3MCoding,
  ICDO3CatalogsProvider,
  ICDO3Catalogs
}



class ICDO3CatalogsProviderImpl extends ICDO3CatalogsProvider
{
  def getInstance: ICDO3Catalogs = ICDO3CatalogsImpl
}


object ICDO3CatalogsImpl extends ICDO3Catalogs
{

  import scala.concurrent.ExecutionContext.Implicits._

  private lazy val topographyCatalogs: Map[ICDO3.Version,Iterable[ICDO3TCoding]] =
    ICDO3.versions
      .map {
        version =>
          val codings =
            ClaMLICDO3TParser.parse(
              this.getClass
                .getClassLoader
                .getResourceAsStream("icdo3" + version + ".xml")
            )
            .map(cd => ICDO3TCoding(cd._1,cd._2,version))
        (version,codings)
      }
      .toMap

  private lazy val morphologyCatalogs: Map[ICDO3.Version,Iterable[ICDO3MCoding]] =
    ICDO3.versions
      .map {
        version =>
          val codings =
            ClaMLICDO3MParser.parse(
              this.getClass
                .getClassLoader
                .getResourceAsStream("icdo3" + version + ".xml")
            )
            .map(cd => ICDO3MCoding(cd._1,cd._2,version))
        (version,codings)
      }
      .toMap


  def topographyCodings(
    version: ICDO3.Version
  ): Future[Iterable[ICDO3TCoding]] =
    Future { topographyCatalogs(version) }

  def topographyMatches(
    version: ICDO3.Version,
    text: String
  ): Future[Iterable[ICDO3TCoding]] =
    topographyCodings(version).map(_.filter(_.display.contains(text)))


  def morphologyCodings(
    version: ICDO3.Version
  ): Future[Iterable[ICDO3MCoding]] =
    Future { morphologyCatalogs(version) }

  def morphologyMatches(
    version: ICDO3.Version,
    text: String
  ): Future[Iterable[ICDO3MCoding]] =
    morphologyCodings(version).map(_.filter(_.display.contains(text)))


}
