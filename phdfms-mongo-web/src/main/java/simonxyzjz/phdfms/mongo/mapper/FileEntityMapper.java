package simonxyzjz.phdfms.mongo.mapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import simonxyzjz.phdfms.mongo.domain.FileEntity;


public class FileEntityMapper {

	public static FileEntity map(File file) throws IOException {
		FileEntity entity = new FileEntity();
		entity.setDirectory(file.isDirectory());
		entity.setAbsolute(file.isAbsolute());
		entity.setExecutable(file.canExecute());
		entity.setHidden(file.isHidden());
		entity.setReadable(file.canRead());
		entity.setLastModified(file.lastModified());
		entity.setLength(file.length());
		entity.setName(file.getName());
		entity.setParent(file.getParent());
		entity.setPath(file.getPath());
		entity.setWritable(file.canWrite());
		entity.setExists(file.exists());
		entity.setCreatedAt(System.currentTimeMillis());
		if (file.isFile()) {
			entity.setMd5(getMd5(file));
			String ext = getExt(file.getName());
			if (StringUtils.isNotBlank(ext)) {
				entity.setExt(ext);
			}
		}
		return entity;
	}

	private static String getExt(String fileName) {
		return StringUtils.substringAfterLast(fileName, ".");
	}

	public static String getMd5(File file) throws IOException {
		
		if(file.isDirectory()) {
			return null;
		}
		final int bufferSize = 256 * 1024;
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
			byte[] buffer = new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0);
			messageDigest = digestInputStream.getMessageDigest();
			byte[] resultByteArray = messageDigest.digest();
			return Hex.encodeHexString(resultByteArray);

		} catch (NoSuchAlgorithmException e) {
			return null;
		} finally {
			if(digestInputStream!=null) {
				digestInputStream.close();
			}
			if(fileInputStream!=null) {
				fileInputStream.close();
			}
		}
	}
}
