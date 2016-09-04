package fileSearcher

import org.scalatest.FlatSpec

class FilterCheckerTests extends FlatSpec  {

  "FilterChecked passed a list where one file matches the filter" should "return a list with that file" in {
    val matchingFile = new FileObject("match")
    val listOfFiles = List(new FileObject("random"), matchingFile)
    val matchedFiles = new FilterChecker("match").findMatchedFiles(listOfFiles)
    assert(matchedFiles == List(matchingFile))
  }

}
