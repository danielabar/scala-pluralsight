package fileSearcher

import java.io.File

trait IOObject {
  val file: File
  val name = file.getName()
}

// empty brackets not required for classes with no implementation
//class FileObject(val name: String) extends IOObject {}
case class FileObject(file: File) extends IOObject
case class DirectoryObject(file: File) extends IOObject
