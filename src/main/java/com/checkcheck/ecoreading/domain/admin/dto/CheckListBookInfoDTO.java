package com.checkcheck.ecoreading.domain.admin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString

//체크리스트에 넘겨줄 책 아이디, 책 제목, 사용자 이름
public class CheckListBookInfoDTO {
    Long boardId;
    String bookName;
    String userName;
}
