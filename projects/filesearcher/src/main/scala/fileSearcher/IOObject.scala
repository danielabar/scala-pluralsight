package fileSearcher

trait IOObject {
  val name: String
}

// empty brackets not required for classes with no implementation
//class FileObject(val name: String) extends IOObject {}
class FileObject(val name: String) extends IOObject
class DirectoryObject(val name: String) extends IOObject
