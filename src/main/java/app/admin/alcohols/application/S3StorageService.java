package app.admin.alcohols.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService {

    @Value("${aws.s3.bucket-name}")
    private String bucket;
    @Value("${aws.cloudfront.domain}")
    private String cdn;

    private final AmazonS3Client amazonS3Client;


    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String uploadWhiskyImage(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile);
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + changedImageName(uploadFile.getName());
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile); // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl; // 업로드된 파일의 S3 URL 주소 반환
    }

    // 실질적인 s3 업로드 부분
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );
        return cdn + "/" + fileName;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        // 확장자 추출
        String original = multipartFile.getOriginalFilename();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf('.'))
                : "";
        // OS 임시 디렉토리에 랜덤 이름의 파일 생성
        File tempFile = File.createTempFile("upload-", ext);
        // MultipartFile 내용을 임시 파일에 옮김
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

    private String changedImageName(String originName) {
        String ext = "";
        int idx = originName.lastIndexOf('.');
        if (idx > 0) {
            ext = originName.substring(idx);
        }
        // [UUID].jpg
        return UUID.randomUUID().toString() + ext;
    }
}
