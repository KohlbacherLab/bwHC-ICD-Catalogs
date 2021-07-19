package de.bwhc.catalogs.icd.impl



import scala.concurrent.{ExecutionContext,Future}

import de.bwhc.catalogs.icd.{
  ICDO3,
  ICDO3TCoding,
  ICDO3MCoding,
  ICDO3CatalogsProvider,
  ICDO3Catalogs
}

import java.time.Year


class ICDO3CatalogsProviderImpl extends ICDO3CatalogsProvider
{

  def getInstance: ICDO3Catalogs = {
    ICDO3CatalogsImpl
  }

}


object ICDO3CatalogsImpl extends ICDO3Catalogs
{

  override def availableVersions: List[Year] =
    List(Year.of(2014))

  override def currentVersion: Year =
    availableVersions.max

  private val topographyCatalogs: Map[Year,Iterable[ICDO3TCoding]] =
    this.synchronized {
    availableVersions
      .map {
        version =>

          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${version.toString}.xml")
            
          val codings =
            ClaMLICDO3TParser.parse(inStream)
              .map(cd => ICDO3TCoding(cd._1,Some(cd._2),version))

          inStream.close
 
         (version,codings)
      }
      .toMap
    }

  private val morphologyCatalogs: Map[Year,Iterable[ICDO3MCoding]] =
    this.synchronized {
    availableVersions
      .map {
        version =>
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${version.toString}.xml")

          val codings =
            ClaMLICDO3MParser.parse(inStream)
              .map(cd => ICDO3MCoding(cd._1,Some(cd._2),version))

          inStream.close

          (version,codings)
      }
      .toMap
    }

/*
  private val topographyCatalogs: Map[ICDO3.Version.Value,Iterable[ICDO3TCoding]] =
    this.synchronized {
    ICDO3.Version.values
      .toList
      .map {
        version =>

          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${version}.xml")
            
          val codings =
            ClaMLICDO3TParser.parse(inStream)
              .map(cd => ICDO3TCoding(cd._1,Some(cd._2),version))

          inStream.close
 
         (version,codings)
      }
      .toMap
    }

  private val morphologyCatalogs: Map[ICDO3.Version.Value,Iterable[ICDO3MCoding]] =
    this.synchronized {
    ICDO3.Version.values
      .toList
      .map {
        version =>
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${version}.xml")

          val codings =
            ClaMLICDO3MParser.parse(inStream)
              .map(cd => ICDO3MCoding(cd._1,Some(cd._2),version))

          inStream.close

          (version,codings)
      }
      .toMap
    }
*/

  override def topographyCodings(
    version: Year
//    version: ICDO3.Version.Value = ICDO3.Version.current
  ): Iterable[ICDO3TCoding] = 
    topographyCatalogs(version)


  override def topographyMatches(
    text: String,
    version: Year
//    version: ICDO3.Version.Value = ICDO3.Version.current,
  ): Iterable[ICDO3TCoding] =
    topographyCodings(version).filter(_.display.exists(_.contains(text)))



  override def morphologyCodings(
    version: Year
//    version: ICDO3.Version.Value = ICDO3.Version.current
  ): Iterable[ICDO3MCoding] =
    morphologyCatalogs(version)


  override def morphologyMatches(
    text: String,
    version: Year
//    version: ICDO3.Version.Value = ICDO3.Version.current
  ): Iterable[ICDO3MCoding] =
    morphologyCodings(version)
      .filter(_.display.exists(_.contains(text)))



/*
  def topographyCodings(
    version: ICDO3.Version.Value
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3TCoding]] =
    Future { topographyCatalogs(version) }

  def topographyMatches(
    version: ICDO3.Version.Value,
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3TCoding]] =
    topographyCodings(version)
      .map(
        _.filter(_.display.exists(_.contains(text)))
      )


  def morphologyCodings(
    version: ICDO3.Version.Value
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3MCoding]] =
    Future { morphologyCatalogs(version) }

  def morphologyMatches(
    version: ICDO3.Version.Value,
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3MCoding]] =
    morphologyCodings(version)
      .map(
        _.filter(_.display.exists(_.contains(text)))
      )
*/

}
