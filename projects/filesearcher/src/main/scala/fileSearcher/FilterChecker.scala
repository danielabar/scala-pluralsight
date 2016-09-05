package fileSearcher

class FilterChecker(filter: String) {

  def matches(content: String) = content.contains(filter)

  def  findMatchedFiles(ioObjects: List[IOObject]) = {
    for(ioObject <- ioObjects
      if(ioObject.isInstanceOf[FileObject])
      if(matches(ioObject.name)))
    yield ioObject
  }

}
