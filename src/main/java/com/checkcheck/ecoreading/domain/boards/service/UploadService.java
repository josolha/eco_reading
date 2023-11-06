package com.checkcheck.ecoreading.domain.boards.service;

import com.checkcheck.ecoreading.domain.boards.dto.InsertBoardBookDTO;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.repository.BoardRepository;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.images.repository.ImageRepository;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;
    private final S3Service s3Service;
    
    @Transactional
    public void uploadBoard(InsertBoardBookDTO dto, List<String> imgPaths) {
        postBlankCheck(imgPaths);
        // TODO: 잘 모르겠지만 일단 유저 아이디나 닉네임을 가져와야 함....
        // getCurrentUsername();
//        Users user = userRepository.findUsersByNickName(nickName).orElseThrow(
//                () -> new PrivateException(Code.NOT_FOUND_USER)
//        );
//        String content = dto.getContent();
//        Boards board = new Boards(content, user);
//        boardRepository.save(board);
//
//        List<String> imgList = new ArrayList<>();
//        for(String imgUrl : imgPaths) {
//            Images image = new Images(imgUrl, board);
//            imageRepository.save(image);
//            imgList.add(image.getImage_url());
//        }
    }

    private void postBlankCheck(List<String> imgPaths) {
        if(imgPaths == null || imgPaths.isEmpty()) {
            // TODO: 예외 처리
            //throw new PrivateException(Code.WRONG_INPUT_IMAGE);
        }
    }


}
