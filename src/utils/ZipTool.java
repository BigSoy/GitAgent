package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipTool {

	private static int bufSize = 1024;
	
	private byte[] buf;
	private ZipOutputStream zos;

	public ZipTool() {
		this.buf = new byte[bufSize];
	}

	public void zip(String dir, String dstDir) {
		File zipDir = new File(dir);
		String zipFileName;
		if(StringUtils.isEmpty(dstDir))
			zipFileName	= zipDir.getAbsolutePath() + ".zip";
		else
			zipFileName = dstDir + File.separator + zipDir.getName() + ".zip";
		
		try {
			zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
			handleDir(zipDir, "");
			zos.close();
		} catch (IOException ioe) {
			System.out.println("zip io exception");
		}
	}

	private void handleDir(File dir, String baseDir) throws IOException {
		File[] files = dir.listFiles();
		if (files.length == 0) {
			zos.putNextEntry(new ZipEntry(baseDir + dir.getName() + File.separator));
			zos.closeEntry();
		} else {
			for (File file : files) {
				if (file.isDirectory()) {
					handleDir(file, baseDir + dir.getName() + File.separator);
				} else {
					zos.putNextEntry(new ZipEntry(baseDir + dir.getName() + File.separator + file.getName()));
					FileInputStream fis = new FileInputStream(file);
					int length = -1;
					while ((length = fis.read(buf)) > 0) {
						zos.write(buf, 0, length);
					}
					zos.flush();
					zos.closeEntry();
				}
			}
		}
	}

	public void unZip(String zipfileName, String dstDir) {
		try {
			ZipFile zipFile = new ZipFile(zipfileName);
			for (Enumeration<ZipEntry> entries = zipFile.getEntries(); entries.hasMoreElements();) {
				
				ZipEntry entry = (ZipEntry) entries.nextElement();
				File file = new File(dstDir + File.separator + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					InputStream is = zipFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(file);
					int length = -1;
					while ((length = is.read(buf)) > 0) {
						fos.write(buf, 0, length);
					}
					fos.flush();
					fos.close();
					is.close();
				}
			}
			zipFile.close();
		} catch (IOException ioe) {
			System.out.println("unzip io exception");
		}
	}

}
