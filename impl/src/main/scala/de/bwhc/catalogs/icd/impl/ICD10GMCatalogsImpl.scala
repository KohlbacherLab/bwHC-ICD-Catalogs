package de.bwhc.catalogs.icd.impl



import scala.concurrent.{
  ExecutionContext,
  Future
}
import cats.Eval
import de.bwhc.catalogs.icd.{
  ICD10GM,
  ICD10GMCoding,
  ICD10GMCatalogsProvider,
  ICD10GMCatalogs
}


class ICD10GMCatalogsProviderImpl extends ICD10GMCatalogsProvider
{

  def getInstance: ICD10GMCatalogs = {
    ICD10GMCatalogsImpl
  }

}



object ICD10GMCatalogsImpl extends ICD10GMCatalogs
{

  import java.time.Year
  import scala.util.Using
  import scala.collection.concurrent.{Map,TrieMap}


  private val years =
    for { y <- 2019 to 2023 } yield Year.of(y)


  override val availableVersions: List[String] =
    years.map(_.toString).toList


  override val latestVersion: String =
    years.max.toString


  private val catalogs: Map[String,Eval[Iterable[ICD10GMCoding]]] =
    TrieMap.from(
      for {
        version <- availableVersions
      } yield {
        version -> Eval.later(
          Using.resource(
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icd10gm${version}.xml")
          ){
            in =>
              ClaMLICD10GMParser.parse(in)
                .map {
                  case (code,label,superClass,subClasses) =>
                    ICD10GMCoding(code,label,version,superClass,subClasses)
                }
          } 
        )
      }
    )


  override def codings(
    version: String
  ): Iterable[ICD10GMCoding] =
    catalogs(version).value


  override def matches(
    text: String,
    version: String
  ): Iterable[ICD10GMCoding] =
    codings(version).filter(_.display.contains(text))


}
