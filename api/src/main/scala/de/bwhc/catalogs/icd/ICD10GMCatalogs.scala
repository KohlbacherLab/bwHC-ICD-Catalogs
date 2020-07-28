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

  def codings(
    version: ICD10GM.Version.Value = ICD10GM.Version.current
  ): Iterable[ICD10GMCoding]


  def code(
    code: ICD10GM.Code,
    version: ICD10GM.Version.Value = ICD10GM.Version.current
  ): Option[ICD10GMCoding] =
    codings(version).find(_.code == code)


  def matches(
    text: String,
    version: ICD10GM.Version.Value = ICD10GM.Version.current
  ): Iterable[ICD10GMCoding]


/*
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
*/

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

