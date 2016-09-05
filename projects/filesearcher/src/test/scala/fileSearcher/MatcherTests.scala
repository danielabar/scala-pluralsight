package fileSearcher

import java.io.File

import org.scalatest.FlatSpec

class MatcherTests extends FlatSpec {
  "Matcher that is passed a file matching the filter" should "return a list with that file name" in {
    val matcher = new Matcher("fake", "fakePath")
    // Note we do not use infix notation here because this runs against the file system, therefore has side effects
    val results = matcher.execute()
    assert(results == List("fakePath"))
  }

  // Integration test
  "Matcher using a directory containing one file matching the filter" should "return a list with that file name" in {
    val pathToSearch: String = new File(".//testfiles//").getCanonicalPath()
    println("=== searching: " + pathToSearch)
    val matcher = new Matcher("txt", pathToSearch)
    val results = matcher.execute()

    assert(results == List("readme.txt")
    )
  }
}
