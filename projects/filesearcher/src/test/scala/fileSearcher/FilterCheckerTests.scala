package fileSearcher

import java.io.File

import org.scalatest.FlatSpec

class FilterCheckerTests extends FlatSpec  {

  "FilterChecked passed a list where one file matches the filter" should "return a list with that file" in {
    val listOfFiles = List(FileObject(new File("random")), FileObject(new File("match")))
    val matchedFiles = FilterChecker("match") findMatchedFiles listOfFiles
    assert(matchedFiles == List(FileObject(new File("match"))))
  }

  "FilterChecker passed a list with a directory that matches the filter" should "not return the directory" in {
    val listOfIOObjects = List(FileObject(new File("random")), new DirectoryObject(new File("match")))
    val matchedFiles = FilterChecker("match") findMatchedFiles listOfIOObjects
    assert(matchedFiles.length == 0)
  }

  // Integration test (runs against the file system)
  "FilterChecker passed a file with content that matches the filter 3 times" should "return a 3" in {
    val isContentMatched = FilterChecker("pluralsight").findMatchedContentCount(new File("./testfiles/pluralsight.data"))
    assert(isContentMatched == 3)
  }

  // Integration test (runs against the file system)
  "FilterChecker passed a file with content that does not matches the filter" should "return a 0" in {
    val isContentMatched = FilterChecker("pluralsight").findMatchedContentCount(new File("./testfiles/readme.txt"))
    assert(isContentMatched == 0)
  }

}
