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

  def getInstance: ICDO3Catalogs = {
    ICDO3CatalogsImpl
  }

}


object ICDO3CatalogsImpl extends ICDO3Catalogs
{

  import scala.concurrent.ExecutionContext.Implicits._

  private val topographyCatalogs: Map[ICDO3.Version,Iterable[ICDO3TCoding]] =
    ICDO3.versions
      .map {
        version =>
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream("icdo3" + version + ".xml")
            
          val codings =
            ClaMLICDO3TParser.parse(inStream)
              .map(cd => ICDO3TCoding(cd._1,cd._2,version))

          inStream.close
 
         (version,codings)
      }
      .toMap
    

  private val morphologyCatalogs: Map[ICDO3.Version,Iterable[ICDO3MCoding]] =
    ICDO3.versions
      .map {
        version =>
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream("icdo3" + version + ".xml")

          val codings =
            ClaMLICDO3MParser.parse(inStream)
              .map(cd => ICDO3MCoding(cd._1,cd._2,version))

          inStream.close

          (version,codings)
      }
      .toMap


  def topographyCodings(
    version: ICDO3.Version
  ): Future[Iterable[ICDO3TCoding]] =
    Future.successful(topographyCatalogs(version))

  def topographyMatches(
    version: ICDO3.Version,
    text: String
  ): Future[Iterable[ICDO3TCoding]] =
    topographyCodings(version).map(_.filter(_.display.contains(text)))


  def morphologyCodings(
    version: ICDO3.Version
  ): Future[Iterable[ICDO3MCoding]] =
    Future.successful(morphologyCatalogs(version))

  def morphologyMatches(
    version: ICDO3.Version,
    text: String
  ): Future[Iterable[ICDO3MCoding]] =
    morphologyCodings(version).map(_.filter(_.display.contains(text)))


}
