package fileSearcher

import java.io.File

import scala.annotation.tailrec

// rootLocation is optional, could be a file or directory
// rootLocation is made public so it can be verified in tests (does Scala have protected?)
class Matcher(filter: String,
              val rootLocation: String = new File(".").getCanonicalPath(),
              checkSubFolders : Boolean = false,
              contentFilter: Option[String] = None) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))

  // tail recursive method, files is list to be searched, currentList is accumulator for results
  @tailrec
  final def recursiveMatch(files: List[IOObject], currentList: List[FileObject]): List[FileObject] =
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

    // Notice not using variable colon type syntax used in ohter pattern matchers
    val contentFilteredFiles = contentFilter match {
      case Some(dataFilter) =>
        // "filter" method loops through each file object and uses "matchesFileContent" method to check the file
        // "filter" will look through ALL objects, whether match is found or not (unllike "exists" that stops at first true)
        // "filter" returns a list containing objects that match the predicate (unlike "exists" that returns a boolean)
        matchedFiles filter(ioObject => FilterChecker(dataFilter).matchesFileContent(ioObject.file))
      case None => matchedFiles
    }

    // project only the name value of each object using List's "map" operation
    contentFilteredFiles map(ioObject => ioObject.name)
  }
}
