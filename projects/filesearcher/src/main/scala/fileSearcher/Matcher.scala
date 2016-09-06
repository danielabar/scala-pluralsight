package fileSearcher

import java.io.File

// rootLocation is optional, could be a file or directory
// rootLocation is made public so it can be verified in tests (does Scala have protected?)
class Matcher(filter: String, val rootLocation: String = new File(".").getCanonicalPath()) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))

  def execute() = {
    val matchedFiles = rootIOObject match {
      case file: FileObject if FilterChecker(filter) matches file.name => List(file)
      case directory: DirectoryObject =>
        FilterChecker(filter) findMatchedFiles directory.children()
      case _ => List()
    }

    // project only the name value of each object using List's "map" operation
    matchedFiles map(ioObject => ioObject.name)
  }
}
