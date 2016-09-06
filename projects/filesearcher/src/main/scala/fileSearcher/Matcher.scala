package fileSearcher

import java.io.File

// rootLocation is optional, could be a file or directory
// rootLocation is made public so it can be verified in tests (does Scala have protected?)
class Matcher(filter: String, val rootLocation: String = new File(".").getCanonicalPath(), checkSubFolders : Boolean = false) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))

  // tail recursive method, files is list to be searched, currentList is accumulator fo results
  def recursiveMatch(files: List[IOObject], currentList: List[FileObject]): List[FileObject] =
    files match {
      // empty list is terminator
      case List() => currentList
      // extract (destructure?) head of list (ioObject) followed by rest of list (i.e. tail)
      case ioObject :: rest =>
        ioObject match {
          case file : FileObject if FilterChecker(filter) matches file.name =>
            // recursive call with rest of list, append current file to accumulated matches in currentList
            recursiveMatch(rest, file :: currentList)
          case directory : DirectoryObject =>
            recursiveMatch(rest ::: directory.children(), currentList)
          case _ => recursiveMatch(rest, currentList)
        }
    }

  def execute() = {
    val matchedFiles = rootIOObject match {
      case file: FileObject if FilterChecker(filter) matches file.name => List(file)
      case directory: DirectoryObject =>
        if(checkSubFolders) recursiveMatch(directory.children(), List())
        else FilterChecker(filter) findMatchedFiles directory.children()
      case _ => List()
    }

    // project only the name value of each object using List's "map" operation
    matchedFiles map(ioObject => ioObject.name)
  }
}
