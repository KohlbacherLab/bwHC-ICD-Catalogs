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


  override val availableVersions: List[(String,Year)] =
    List(
      "Erste Revision"  -> Year.of(2014),
      "Zweite Revision" -> Year.of(2019)
    )


  override def currentVersion: String =
    availableVersions.last._1


  private val topographyCatalogs: Map[String,Iterable[ICDO3TCoding]] =
    this.synchronized {
      (
        for {
          (version,year) <- availableVersions
        } yield {
        
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${year}.xml")
              
            val codings =
              ClaMLICDO3TParser.parse(inStream)
                .map {
                  case (code,display,superClass,subClasses) => ICDO3TCoding(code,display,version,superClass,subClasses)
                }
        
            inStream.close
        
           (version,codings)
        }
      )
      .toMap
    }


  private val morphologyCatalogs: Map[String,Iterable[ICDO3MCoding]] =
    this.synchronized {
      (
        for {
          (version,year) <- availableVersions
        } yield {
        
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${year}.xml")
        
          val codings =
            ClaMLICDO3MParser.parse(inStream)
              .map { case (code,display,superClass,subClasses) => ICDO3MCoding(code,display,version,superClass,subClasses) }
        
          inStream.close
        
          (version,codings)
        
        }
      )
      .toMap
    }


  override def topographyCodings(
    version: String
  ): Iterable[ICDO3TCoding] = 
    topographyCatalogs(version)


  override def topographyMatches(
    text: String,
    version: String
  ): Iterable[ICDO3TCoding] =
    topographyCodings(version).filter(_.display.contains(text))


  override def morphologyCodings(
    version: String
  ): Iterable[ICDO3MCoding] =
    morphologyCatalogs(version)


  override def morphologyMatches(
    text: String,
    version: String
  ): Iterable[ICDO3MCoding] =
    morphologyCodings(version)
      .filter(_.display.contains(text))

}

/*
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


  override def topographyCodings(
    version: Year
  ): Iterable[ICDO3TCoding] = 
    topographyCatalogs(version)


  override def topographyMatches(
    text: String,
    version: Year
  ): Iterable[ICDO3TCoding] =
    topographyCodings(version).filter(_.display.exists(_.contains(text)))



  override def morphologyCodings(
    version: Year
  ): Iterable[ICDO3MCoding] =
    morphologyCatalogs(version)


  override def morphologyMatches(
    text: String,
    version: Year
  ): Iterable[ICDO3MCoding] =
    morphologyCodings(version)
      .filter(_.display.exists(_.contains(text)))

}
*/
