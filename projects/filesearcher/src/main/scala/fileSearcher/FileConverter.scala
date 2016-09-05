package fileSearcher

import java.io.File

object FileConverter {
  def convertToIOObject(file: File) =
    if(file.isDirectory()) DirectoryObject(file) else FileObject(file)
}
