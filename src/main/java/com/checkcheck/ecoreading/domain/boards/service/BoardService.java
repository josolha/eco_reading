package com.checkcheck.ecoreading.domain.boards.service;

import com.checkcheck.ecoreading.domain.boards.dto.InsertBoardDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertBookDTO;
import com.checkcheck.ecoreading.domain.boards.dto.InsertDeliveryDTO;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.repository.BoardRepository;
import com.checkcheck.ecoreading.domain.books.entity.BookProcessingMethod;
import com.checkcheck.ecoreading.domain.books.dto.BookDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.books.repository.BookRepository;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.delivery.entity.DeliveryForm;
import com.checkcheck.ecoreading.domain.delivery.entity.DeliveryPlace;
import com.checkcheck.ecoreading.domain.delivery.repository.DeliveryRepository;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.io.InputStream;
import java.util.List;


// S3 활용하여 이미지 업로드 과정 구현
@Service
@RequiredArgsConstructor
public class BoardService {
    // amazonS3 불러오기
    private final S3Service s3Service;
    private final BookRepository bookRepository;
    private final BoardRepository boardRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;


    // db에 글 업로드시 필요한 과정 (into IMAGES, BOOKS, BOARDS, DELIVERY
    @Transactional
    public void uploadIntoDB(List<String> imgUrlList, InsertBookDTO bookDTO, InsertBoardDTO boardDTO, InsertDeliveryDTO deliveryDTO) {
        //todo: 로그인 정보 가져와서 아이디값 가져오기
        Long userId = 1L;

        // 유저 아이디 (로그인 받아온 정보 빌드) //todo: 고치기 (로그인 기능 완료후)
        Users user = new Users();

        // 1. boardDTO에서 받아온 정보 빌드
        Boards boards = Boards.builder()
                .message(boardDTO.getMessage())
                .build();

        String process = String.valueOf(bookDTO.getProcess());

        // 2. bookDTO에서 받아온 정보 빌드
        Books books = Books.builder()
                .title(bookDTO.getTitle())
                .isbn(bookDTO.getIsbn())
                .description(bookDTO.getDescription())
                .author(bookDTO.getAuthor())
                .publisher(bookDTO.getPublisher())
                .pubDate(bookDTO.getPubdate())
                .build();

        Transactions transactions = Transactions.builder()
                .giverId(userId)
                .status(TransactionStatus.신규등록).build();

        String place = String.valueOf(deliveryDTO.getPlace());

        // 3. deliveryDTO에서 받아온 정보 빌드
        Delivery delivery = Delivery.builder()
                .transactions(transactions)
                .postcode(deliveryDTO.getPostcode())
                .roadAddress(deliveryDTO.getRoadAddress())
                .detailAddress(deliveryDTO.getDetailAddress())
                .form(DeliveryForm.PICKUP)
                .place(DeliveryPlace.valueOf(place))
                .name(deliveryDTO.getName())
                .phone(deliveryDTO.getPhone())
                .build();

        user.addBoard(boards);
        // 4. board올리면서 book, delivery에도 함께 추가 (연관관계 메서드)
        boards.addBook(books);
        boards.addDelivery(delivery);


        // 이미지 urlList에서 각 url을 db에 넣어주는 과정
        for(String imgUrl : imgUrlList) {
            Images image = Images.builder()
                    .imagesUrl(imgUrl).build();

            // book에도 image 함께 저장해주기
            books.addImages(image);
        }

        books.addTransactions(transactions);
        // book정보는 DB에 저장.
        bookRepository.save(books);

        // 5. boards를 DB에 저장
        boardRepository.save(boards);
    }

    @Transactional
    public void uploadBoard(List<MultipartFile> multipartFileList, InsertBookDTO bookDTO, InsertBoardDTO boardDTO, InsertDeliveryDTO deliveryDTO) {
        List<String> imgUrlList = s3Service.uploadIntoS3(multipartFileList);
        uploadIntoDB(imgUrlList, bookDTO, boardDTO, deliveryDTO);
    }

    public List<Boards> findAll(){
        return boardRepository.findAll();
    }

    public Boards findAllByBoardId(Long boardId){
       return boardRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public boolean deleteBoardByBoardId(Long boardId){
        return boardRepository.deleteAllByBoardId(boardId) != 0 && boardRepository.deleteAllByBoardId(boardId) <= 1;
    }

    @Transactional
    public Model updateBoardByBoardId(Long boardId, InsertBookDTO bookDTO, InsertBoardDTO boardDTO, InsertDeliveryDTO deliveryDTO, Model model){
        Boards boards = boardRepository.findAllByBoardId(boardId);
        boards.setMessage(boardDTO.getMessage());
        List<Books> bookList = boards.getBooksList();
        Long bookId = bookList.get(0).getBooksId();
        Books books = bookRepository.findByBooksId(bookId);
        Delivery delivery = boards.getDelivery();
        delivery.setPostcode(deliveryDTO.getPostcode());
        delivery.setDetailAddress(deliveryDTO.getDetailAddress());
        delivery.setRoadAddress(deliveryDTO.getRoadAddress());

        books.setAuthor(bookDTO.getAuthor());
        books.setIsbn(bookDTO.getIsbn());
        books.setPubDate(bookDTO.getPubdate());
        books.setTitle(bookDTO.getTitle());
        books.setDescription(bookDTO.getDescription());
        books.setPublisher(bookDTO.getPublisher());

        model.addAttribute("books", bookRepository.save(books));
        model.addAttribute("boards", boardRepository.save(boards));
        model.addAttribute("delivery", deliveryRepository.save(delivery));
        return model;
    }
}
