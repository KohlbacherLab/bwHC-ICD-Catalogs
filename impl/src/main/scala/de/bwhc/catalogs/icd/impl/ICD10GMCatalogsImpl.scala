package de.bwhc.catalogs.icd.impl



import scala.concurrent.{
  ExecutionContext,
  Future
}

import java.time.Year

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

  override def availableVersions: List[Year] =
    List(2019,2020,2021).map(Year.of)


  private lazy val catalogs: Map[Year,Iterable[ICD10GMCoding]] =
    this.synchronized {
    availableVersions
      .map {
        version =>
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icd10gm${version.toString}.xml")

          val codings =
            ClaMLICD10GMParser.parse(inStream)
              .map(cd => ICD10GMCoding(cd._1,Some(cd._2),version))

          inStream.close

          (version,codings)
      }
      .toMap
    }

/*
  private lazy val catalogs: Map[ICD10GM.Version.Value,Iterable[ICD10GMCoding]] =
    this.synchronized {
    ICD10GM.Version.values
      .toList
      .map {
        version =>
          val inStream =
            this.getClass
              .getClassLoader
              .getResourceAsStream(s"icd10gm$version.xml")

          val codings =
            ClaMLICD10GMParser.parse(inStream)
              .map(cd => ICD10GMCoding(cd._1,Some(cd._2),version))

          inStream.close

          (version,codings)
      }
      .toMap
    }
*/

  override def codings(
    version: Year
//    version: ICD10GM.Version.Value
  ): Iterable[ICD10GMCoding] =
    catalogs(version)

  override def matches(
    text: String,
    version: Year
//    version: ICD10GM.Version.Value
  ): Iterable[ICD10GMCoding] =
    codings(version).filter(_.display.exists(_.contains(text)))

/*
  def codings(
    version: ICD10GM.Version.Value
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICD10GMCoding]] = {
    Future { catalogs(version) }
  }  


  def matches(
    version: ICD10GM.Version.Value,
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICD10GMCoding]] =
    codings(version)
      .map(_.filter(_.display.exists(_.contains(text))))
*/

}
