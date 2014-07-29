package utils;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class AntZip {

	public void zip(String srcDir, String dstZip) {
		Project prj = new Project();
		
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(new File(dstZip));
		
		FileSet fs = new FileSet();
		fs.setProject(prj);
		fs.setDir(new File(srcDir));
		zip.addFileset(fs);
		zip.execute();
	}
	
	public void unzip(String dstDir, String srcZip) {
		Project prj = new Project();
		
		Expand expand = new Expand();
		expand.setProject(prj);
		expand.setSrc(new File(srcZip));
		expand.setOverwrite(false);
		
		expand.setDest(new File(dstDir));
		expand.execute();
	}
	
	public static void main(String[] args) {
		AntZip antZip = new AntZip();
		antZip.zip("D:\\tmpRepo\\ls\\project1", "D:\\tmpZipDir\\project1.zip");
		System.out.println("ant zip over");
		antZip.unzip("D:\\tmpZipDir\\project1", "D:\\tmpZipDir\\project1.zip");
		System.out.println("ant unzip over");
	}
}
