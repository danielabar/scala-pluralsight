package fileSearcher

class FilterChecker(filter: String) {

  def matches(content: String) = content contains filter

  // this is one complex expression, therefore brackets can be ommitted
  def findMatchedFiles(ioObjects: List[IOObject]) =
    for(ioObject <- ioObjects
      if(ioObject.isInstanceOf[FileObject])
      if(matches(ioObject.name)))
    yield ioObject
}

object FilterChecker {
  // apply method takes same parameters as FilterChecker constructor
  // and returns a new instance of FilterChecker.
  // Scala magic: Any object can omit "apply" and be acted on directly,
  // which triggers the apply function behind the scenes.
  def apply(filter: String) = new FilterChecker(filter)
}
