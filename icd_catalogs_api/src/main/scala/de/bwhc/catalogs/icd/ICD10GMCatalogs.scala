package de.bwhc.catalogs.icd



import scala.util.Try
import scala.concurrent.Future
import java.util.ServiceLoader


trait ICD10GMCatalogsProvider
{
  def getInstance: ICD10GMCatalogs
}



trait ICD10GMCatalogs
{

  def codings(
    version: ICD10GM.Version
  ): Future[Iterable[ICD10GMCoding]]

  def codings: Future[Iterable[ICD10GMCoding]] =
    codings(ICD10GM.Version.current)


  def matches(
    version: ICD10GM.Version,
    text: String
  ): Future[Iterable[ICD10GMCoding]]


  def matches(
    text: String
  ): Future[Iterable[ICD10GMCoding]] =
    matches(
      ICD10GM.Version.current,
      text
    )

}


object ICD10GMCatalogs
{

  def getInstance: Try[ICD10GMCatalogs] =
    Try {
      ServiceLoader.load(classOf[ICD10GMCatalogsProvider])
        .iterator
        .next
        .getInstance
    }

}

