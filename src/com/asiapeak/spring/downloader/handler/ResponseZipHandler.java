package com.asiapeak.spring.downloader.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.asiapeak.spring.downloader.abstracts.AbstractDownloadHandler;
import com.asiapeak.spring.downloader.dto.ResponseZip;
import com.asiapeak.spring.downloader.function.ZipFilterPredicate;

public class ResponseZipHandler extends AbstractDownloadHandler<ResponseZip> {

	@Override
	public Class<?> supportType() {
		return ResponseZip.class;
	}

	@Override
	protected void write(ResponseZip responseZip, HttpServletRequest request, HttpServletResponse response) throws IOException {

		String zipName = responseZip.getZipName();

		if (zipName == null || "".equals(zipName)) {
			if (responseZip.getFolder() != null) {
				zipName = responseZip.getFolder().getName();
			} else {
				zipName = UUID.randomUUID().toString();
			}
		}

		String extension = ".zip";
		if (zipName.toLowerCase().endsWith(extension)) {
			extension = "";
		}

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + encodeFileName(zipName) + extension + "\"");
		OutputStream output = response.getOutputStream();

		ZipFilterPredicate filter = responseZip.getFilter();
		if (filter == null) {
			filter = (f) -> true;
		}

		try (ZipOutputStream zs = new ZipOutputStream(output)) {
			zs.setLevel(responseZip.getLevel());

			if (responseZip.getFolder() != null) { // 下載資料夾
				if (!responseZip.getFolder().exists() || !responseZip.getFolder().isDirectory()) {
					throw new FileNotFoundException(responseZip.getFolder().getPath());
				}
				Path pp = responseZip.getFolder().toPath();
				List<Path> paths = Files.walk(pp).filter(path -> !Files.isDirectory(path)).collect(Collectors.toList());
				for (Path path : paths) {
					if (filter.test(path.toFile())) {
						ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
						zs.putNextEntry(zipEntry);
						Files.copy(path, zs);
						zs.closeEntry();
					}
				}
				zs.finish();
			} else if (responseZip.getFiles() != null) { // 下載檔案列表
				List<File> responsefiles = responseZip.getFiles();
				if (responsefiles.stream().anyMatch(f -> !f.exists())) {
					String notExists = responsefiles.stream().filter(f -> !f.exists()).map(f -> f.getPath()).collect(Collectors.joining(", "));
					throw new FileNotFoundException(notExists);
				}
				if (!responseZip.getFolder().exists() || !responseZip.getFolder().isDirectory()) {
					throw new FileNotFoundException(responseZip.getFolder().getPath());
				}

				// 判斷 response files 是否有包含資料夾
				if (responsefiles.stream().anyMatch(f -> f.isDirectory())) {
					// 有包含資料夾 照原本流程
					for (File f : responsefiles) {
						if (f.isDirectory()) {
							Path pp = f.toPath();
							List<Path> paths = Files.walk(pp).filter(path -> !Files.isDirectory(path)).collect(Collectors.toList());
							for (Path path : paths) {
								if (filter.test(path.toFile())) { // 過濾
									ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
									zs.putNextEntry(zipEntry);
									Files.copy((Path) path, zs);
									zs.closeEntry();
								}
							}
						} else {
							if (filter.test(f)) { // 過濾
								ZipEntry zipEntry = new ZipEntry(f.getName());
								zs.putNextEntry(zipEntry);
								Files.copy(f.toPath(), zs);
								zs.closeEntry();
							}
						}
					}
				} else {
					// 只有檔案
					List<File> parents = new ArrayList<>();
					responsefiles.stream().collect(Collectors.groupingBy(File::getParentFile)).forEach((patent, files) -> {
						parents.add(patent);
					});

					// 將相同的 子目錄 歸類在底下
					for (int i = 0; i < parents.size(); i++) {
						final int index = i;
						List<File> sameParents = parents.stream().filter(parent -> parent.getParentFile().compareTo(parents.get(index)) == 0).collect(Collectors.toList());
						if (sameParents.size() > 0) {
							parents.removeAll(sameParents);
							i = 0;
						}
					}

					for (File parent : parents) {
						Path parentPath = parent.toPath();
						List<Path> paths = Files.walk(parentPath).filter(filePath -> responsefiles.stream().anyMatch(f -> f.compareTo(filePath.toFile()) == 0)).collect(Collectors.toList());

						for (Path path : paths) {
							if (filter.test(path.toFile())) { // 過濾
								ZipEntry zipEntry = new ZipEntry(parentPath.relativize(path).toString());
								zs.putNextEntry(zipEntry);
								Files.copy(path, zs);
								zs.closeEntry();
							}
						}

					}

				}
				zs.finish();
			}
		}
		close(output);
		response.setStatus(HttpStatus.OK.value());
	}

}
