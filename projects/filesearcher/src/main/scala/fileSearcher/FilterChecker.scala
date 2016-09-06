package fileSearcher

import java.io.File

import scala.util.control.NonFatal

class FilterChecker(filter: String) {

  def matches(content: String) = content contains filter

  // this is one complex expression, therefore brackets can be ommitted
  def findMatchedFiles(ioObjects: List[IOObject]) =
    for(ioObject <- ioObjects
      if(ioObject.isInstanceOf[FileObject])
      if(matches(ioObject.name)))
    yield ioObject

  def matchesFileContent(file: File) = {
    import scala.io.Source
    // nested try/catch is required so Source can be closed
    try {
      // convert the java file into a scala source
      val fileSource = Source.fromFile(file)
      try {
        // getLines retrieve an iterator of strings containing the file content
        // exists method will loop through each line, running it through "matches" methods
        // exists will immediately stop when "matches" returns true, otherwise continues looping
        // if no true is returned during entire loop, entire evaluation will return false
        fileSource.getLines() exists(line=>matches(line))
      } catch {
        case NonFatal(_) => false
      } finally {
        fileSource.close()
      }
    } catch {
      case NonFatal(_) => false
    }
  }
}

object FilterChecker {
  // apply method takes same parameters as FilterChecker constructor
  // and returns a new instance of FilterChecker.
  // Scala magic: Any object can omit "apply" and be acted on directly,
  // which triggers the apply function behind the scenes.
  def apply(filter: String) = new FilterChecker(filter)
}
