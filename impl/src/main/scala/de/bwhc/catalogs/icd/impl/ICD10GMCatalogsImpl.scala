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
    List(2019,2020,2021,2022).map(Year.of)


  private val catalogs: Map[Year,Iterable[ICD10GMCoding]] =
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
              .map {
                case (code,label,superClass,subClasses) => ICD10GMCoding(code,label,version,superClass,subClasses)
              }

          inStream.close

          (version,codings)
      }
      .toMap
    }


  override def codings(
    version: Year
  ): Iterable[ICD10GMCoding] =
    catalogs(version)


  override def matches(
    text: String,
    version: Year
  ): Iterable[ICD10GMCoding] =
    codings(version).filter(_.display.contains(text))


}
