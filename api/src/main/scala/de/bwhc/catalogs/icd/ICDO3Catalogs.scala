package de.bwhc.catalogs.icd



import scala.util.Try
import scala.concurrent.{ExecutionContext,Future}
import java.util.ServiceLoader


trait ICDO3CatalogsProvider
{
  def getInstance: ICDO3Catalogs
}



trait ICDO3Catalogs
{

  def topographyCodings(
    version: ICDO3.Version.Value = ICDO3.Version.current
  ): Iterable[ICDO3TCoding]


  def topographyMatches(
    text: String,
    version: ICDO3.Version.Value = ICDO3.Version.current,
  ): Iterable[ICDO3TCoding]



  def morphologyCodings(
    version: ICDO3.Version.Value = ICDO3.Version.current
  ): Iterable[ICDO3MCoding]


  def morphologyMatches(
    text: String,
    version: ICDO3.Version.Value = ICDO3.Version.current
  ): Iterable[ICDO3MCoding]


/*
  def topographyCodings(
    version: ICDO3.Version.Value
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3TCoding]]

  def topographyCodings(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3TCoding]] =
    topographyCodings(ICDO3.Version.current)

  def topographyMatches(
    version: ICDO3.Version.Value,
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3TCoding]]

  def topographyMatches(
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3TCoding]] =
    topographyMatches(
      ICDO3.Version.current,
      text
    )


  def morphologyCodings(
    version: ICDO3.Version.Value
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3MCoding]]

  def morphologyCodings(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3MCoding]] =
    morphologyCodings(ICDO3.Version.current)

  def morphologyMatches(
    version: ICDO3.Version.Value,
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3MCoding]]

  def morphologyMatches(
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICDO3MCoding]] =
    morphologyMatches(
      ICDO3.Version.current,
      text
    )
*/
}


object ICDO3Catalogs
{

  def getInstance: Try[ICDO3Catalogs] = Try {
    ServiceLoader.load(classOf[ICDO3CatalogsProvider])
      .iterator
      .next
      .getInstance
  }

}
