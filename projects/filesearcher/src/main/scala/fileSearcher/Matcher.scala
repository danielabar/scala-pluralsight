package fileSearcher

import java.io.File

// rootLocation could be a file or director
class Matcher(filter: String, rootLocation: String) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))

  def execute() = {
    val matchedFiles = rootIOObject match {
      case file: FileObject if FilterChecker(filter) matches file.name => List(file)
      case directory: DirectoryObject => ???
      case _ => List()
    }

    // project only the name value of each object using List's "map" operation
    matchedFiles map(ioObject => ioObject.name)
  }
}
