package de.bwhc.catalogs.icd.impl



//import scala.concurrent.{ExecutionContext,Future}
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

  import java.time.Year
  import cats.Eval
  import scala.util.Using
  import scala.collection.concurrent.{Map,TrieMap}


  override val availableVersions: List[(String,Year)] =
    List(
      "Erste Revision"  -> Year.of(2014),
      "Zweite Revision" -> Year.of(2019)
    )


  override def currentVersion: String =
    availableVersions.maxBy { case (_,year) => year }._1


  private val topographyCatalogs: Map[String,Eval[Iterable[ICDO3TCoding]]] =
    TrieMap.from(  
      for {
        (version,year) <- availableVersions
      } yield {
        version -> Eval.later {
      
          Using.resource(
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${year}.xml")
          )(    
            in =>
              ClaMLICDO3TParser.parse(in)
                .map {
                  case (code,display,superClass,subClasses) =>
                    ICDO3TCoding(code,display,version,superClass,subClasses)
                }
          )
        }
      }
    )

  private val morphologyCatalogs: Map[String,Eval[Iterable[ICDO3MCoding]]] =
    TrieMap.from(
      for {
        (version,year) <- availableVersions
      } yield {
        version -> Eval.later {
          Using.resource(
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icdo3${year}.xml")
       
          )(
            in =>
            ClaMLICDO3MParser.parse(in)
              .map {
                case (code,display,superClass,subClasses) =>
                  ICDO3MCoding(code,display,version,superClass,subClasses)
              }
          )
        }
      }
    )
    

  override def topographyCodings(
    version: String
  ): Iterable[ICDO3TCoding] = 
    topographyCatalogs(version).value


  override def topographyMatches(
    text: String,
    version: String
  ): Iterable[ICDO3TCoding] =
    topographyCodings(version).filter(_.display.contains(text))


  override def morphologyCodings(
    version: String
  ): Iterable[ICDO3MCoding] =
    morphologyCatalogs(version).value


  override def morphologyMatches(
    text: String,
    version: String
  ): Iterable[ICDO3MCoding] =
    morphologyCodings(version)
      .filter(_.display.contains(text))

}

