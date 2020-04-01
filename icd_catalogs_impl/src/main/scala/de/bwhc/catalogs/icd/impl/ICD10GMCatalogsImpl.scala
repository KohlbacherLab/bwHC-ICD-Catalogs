package de.bwhc.catalogs.icd.impl



import scala.concurrent.Future

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

  import scala.concurrent.ExecutionContext.Implicits._

  private val catalogs: Map[ICD10GM.Version,Iterable[ICD10GMCoding]] =
    ICD10GM.versions
      .map {
        version =>
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream("icd10gm" + version + ".xml")

          val codings =
            ClaMLICD10GMParser.parse(inStream)
              .map(cd => ICD10GMCoding(cd._1,cd._2,version))

          inStream.close

          (version,codings)
      }
      .toMap


  def codings(
    version: ICD10GM.Version
  ): Future[Iterable[ICD10GMCoding]] = {
    Future.successful(catalogs(version))
  }  


  def matches(
    version: ICD10GM.Version,
    text: String
  ): Future[Iterable[ICD10GMCoding]] =
    codings(version).map(_.filter(_.display.contains(text)))

}
