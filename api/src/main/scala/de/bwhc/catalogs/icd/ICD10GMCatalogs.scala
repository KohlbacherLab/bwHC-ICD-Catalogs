package de.bwhc.catalogs.icd



import scala.util.Try

import scala.concurrent.{
  ExecutionContext,
  Future
}

import java.util.ServiceLoader


trait ICD10GMCatalogsProvider
{
  def getInstance: ICD10GMCatalogs
}



trait ICD10GMCatalogs
{
/*
  def codings(
    version: ICD10GM.Version
  ): Iterable[ICD10GMCoding]

  def codings: Iterable[ICD10GMCoding] =
    codings(ICD10GM.Version.current)


  def matches(
    version: ICD10GM.Version,
    text: String
  ): Iterable[ICD10GMCoding]


  def matches(
    text: String
  ): Iterable[ICD10GMCoding] =
    matches(
      ICD10GM.Version.current,
      text
    )
*/

  def codings(
    version: ICD10GM.Version.Value
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICD10GMCoding]]

  def codings(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICD10GMCoding]] =
    codings(ICD10GM.Version.current)


  def matches(
    version: ICD10GM.Version.Value,
    text: String
  )(
    implicit ec: ExecutionContext
  ): Future[Iterable[ICD10GMCoding]]


  def matches(
    text: String
  )(
    implicit ec: ExecutionContext
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

