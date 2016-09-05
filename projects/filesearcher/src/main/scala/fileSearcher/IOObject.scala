package fileSearcher

trait IOObject {
  val name: String
}

class FileObject(val name: String) extends IOObject {}
class DirectoryObject(val name: String) extends IOObject {}
