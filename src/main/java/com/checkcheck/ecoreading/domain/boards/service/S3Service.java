package com.checkcheck.ecoreading.domain.boards.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


// S3 활용하여 이미지 업로드 과정 구현
@Service
@RequiredArgsConstructor
public class S3Service {
    // amazonS3 불러오기
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 업로드할 이미지 파일의 목록을 받아서 이미지 업로드, 업로드된 이미지 URL 목록 반환하기.
    protected List<String> uploadIntoS3(List<MultipartFile> multipartFileList) {
        List<String> imgUrlList = new ArrayList<>();

        //forEach 구문을 통해 multipartFile로 넘어온 파일들을 하나씩 fileNameList에 추가
        for(MultipartFile file: multipartFileList) {
            // 업로드될 이미지 파일의 고유한 파일 이름 생성하기 (UUID 활용)
            String fileName = createFileName(file.getOriginalFilename());

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                // inputstream을 통해 읽어들이고, 이를 S3에 PutObjectRequest를 사용하여 업로드~
                amazonS3.putObject(new PutObjectRequest(bucket+"/book", fileName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                // 이미지가 성공적으로 업로드되면 해당 이미지의 url을 리스트에 추가.
                imgUrlList.add(amazonS3.getUrl(bucket+"/book/", fileName).toString());
            } catch (IOException e) {
                // todo: 에러메시지 뜨도록 파일 만들기
                //throw new PrivateException(Code.IMAGE_UPLOAD_ERROR);
                e.printStackTrace();
            }
        }
        return imgUrlList;
    }

    // 이미지 파일명 중복 방지 (랜덤UUID와 파일확장명 합치기)
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일이름에서 확장명 추출하는 메서드
    private String getFileExtension(String fileName) {
        if (fileName.length()==0) {
            //TODO: 예외처리
            //throw new PrivateException(Code.WRONG_INPUT_IMAGE);
        }

        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".gif");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        fileValidate.add(".GIF");

        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if(!fileValidate.contains(idxFileName)) {
            // TODO: 예외처리
            //throw new PrivateException(Code.WRONG_IMAGE_FORMAT);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }


    // 이미지 파일 삭제하기
    @Transactional
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket+"/book", fileName));
        System.out.println(bucket);
    }

}
