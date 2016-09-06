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
  "FilterChecker passed a file with content that matches the filter" should "return that the match succeeded" in {
    assert(isContentMatched == true)
  }

  // Integration test (runs against the file system)
  val isContentMatched = FilterChecker("pluralsight").matchesFileContent(new File("./testfiles/pluralsight.data"))
  "FilterChecker passed a file with content that does not matches the filter" should "return that the match failed" in {
    val isContentMatched = FilterChecker("pluralsight").matchesFileContent(new File("./testfiles/readme.txt"))
    assert(isContentMatched == false)
  }

  "Matcher given a path that has one file that matches file filter and content filter" should "return a list with that file name" in {
    val matcher = new Matcher("data", new File(".//testfiles//").getCanonicalPath(), true, Some("pluralsight"))
    val matchedFiles = matcher.execute()
    assert(matchedFiles == List("pluralsight.data"))
  }

  "Matcher given a path that has no file that matches file filter and content filter" should "return an empty list" in {
    val matcher = new Matcher("txt", new File(".//testfiles//").getCanonicalPath(), true, Some("pluralsight"))
    val matchedFiles = matcher.execute()
    assert(matchedFiles == List())
  }

}
