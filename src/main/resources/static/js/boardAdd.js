// 이미지 파일 첨부 확인 함수
function isImageAttached() {
    var imageInput = document.getElementById("imageUpload");
    return imageInput.files.length > 0;
}

// 수거장소 선택 여부 확인 함수
function isPlaceSelected() {
    var selectedPosition = $("input[name='place']:checked").val();
    return typeof selectedPosition !== "undefined" && selectedPosition !== "";
}

// 검색 유효성 검사
function searchValidateForm() {
    var searchInput = document.getElementById('searchInput').value;
    if (searchInput.trim() === "") {
        alert("검색어를 입력하세요.");
        event.preventDefault();
        return false; // 제출 막기
    }
    return true;
}

//폼 제출 시 등록폼 개별 확인 함수
function submitValidateForm() {
    // 책 등록했는지 검사
    if (!$("#title").val()) {
        alert("등록 도서를 검색 후 선택해주세요");
        $("#title").focus();
        return false;
    }

    // 이미지 파일 검사
    if (!isImageAttached()) {
        alert("도서상태를 나타내는 이미지를 첨부해주세요.");
        return false;
    }

    // 사용자 수거정보 검사
    var username = $("#username").val();
    var phone = $("#phone").val();
    var postcode = $("#postcode").val();
    var roadAddress = $("#roadAddress").val();
    var detailAddress = $("#detailAddress").val();
    if (!username||!phone||!postcode||!roadAddress||!detailAddress) {
        alert("수거정보를 입력해주세요.");
        return false;
    }

    // 수거장소 검사
    if (!isPlaceSelected()) {
        alert("정확한 수거 장소를 선택해주세요.");
        $("place").focus();
        return false;
    }

    var termsCheckbox = document.getElementById("termsCheckbox");
    // 약관 동의 여부 확인
    if (!termsCheckbox.checked) {
        alert("서비스 이용 약관에 동의해야 합니다.");
        return false;
    }

    // 모든 유효성 검사를 통과하면 폼 제출
    return true;
}

function submitValidateFormForUpdate() {
    // 책 등록했는지 검사
    if (!$("#title").val()) {
        alert("등록 도서를 검색 후 선택해주세요");
        $("#title").focus();
        return false;
    }

    // // 이미지 파일 검사
    // if (!isImageAttached()) {
    //     alert("도서상태를 나타내는 이미지를 첨부해주세요.");
    //     return false;
    // }

    // 사용자 수거정보 검사
    var username = $("#username").val();
    var phone = $("#phone").val();
    var postcode = $("#postcode").val();
    var roadAddress = $("#roadAddress").val();
    var detailAddress = $("#detailAddress").val();
    if (!username||!phone||!postcode||!roadAddress||!detailAddress) {
        alert("수거정보를 입력해주세요.");
        return false;
    }

    // 수거장소 검사
    if (!isPlaceSelected()) {
        alert("정확한 수거 장소를 선택해주세요.");
        $("place").focus();
        return false;
    }

    var termsCheckbox = document.getElementById("termsCheckbox");
    // 약관 동의 여부 확인
    if (!termsCheckbox.checked) {
        alert("서비스 이용 약관에 동의해야 합니다.");
        return false;
    }

    // 모든 유효성 검사를 통과하면 폼 제출
    return true;
}


function submitFormWithValidation() {
    event.preventDefault();

    if (submitValidateForm()) {
        // 유효성 검사 통과 시 모달 창 띄우기
        $("#myModal").modal('show');
    }
}

function submitFormWithValidationForUpdate() {
    event.preventDefault();

    if (submitValidateFormForUpdate()) {
        // 유효성 검사 통과 시 모달 창 띄우기
        $("#myModal").modal('show');
    }
}

$(document).ready(function() {
    //alert(111111)
    // 모달 창의 닫기 버튼 클릭 이벤트 처리
    $("#closeModalButton").on("click", function() {
        // 폼 수동으로 제출
        $("#form").submit();
    });
    //#currentImages > div:nth-child(1) > button
    /////삭제 클릭했을때 /////////////////////////////////
    $("#currentImages > div > button").click(function(){
       //   alert("this = " + $(this));

       // alert("$(this).attr(\"name\") = "+$(this).attr("name"))

        $("#imagesId").val($(this).attr("name"));

        //전송
        $("#delSubCheck").submit();
    })




    /////////////////////////////////////


});

function sample4_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode; // 우편번호 (5자리)
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            document.getElementById('roadAddress').value = roadAddr; // 도로명 주소 입력란에 값 설정

            // 상세주소 입력란을 비우고, 도로명 주소 입력란으로 포커스 이동
            document.getElementById('detailAddress').value = '';
            document.getElementById('detailAddress').focus();
        }
    }).open();
}

$("#imageUpload").on("change", function(){
    var fileInput = $("#imageUpload")[0];
    var files = fileInput.files;
    var reg = /(.*?)\.(jpg|bmp|jpeg|png|JPG|BMP|JPEG|PNG)$/;
    var maxSize = 5 * 1024 * 1024;

    // var file = event.target.files[0];
    var imageContainer = $("#imageContainer");
    imageContainer.empty();

    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        var reader = new FileReader();
        // reader.readAsDataURL(file);

        reader.onload = (function (file) {
            return function (e) {
                // 미리보기 이미지의 크기 조절
                var img = $("<img>").attr("src", e.target.result).css({
                    "max-width": "200px",
                    "max-height": "200px",
                    "margin": "5px"  // 이미지 간격을 조절하기 위한 스타일
                });
                // 이미지를 이미지 컨테이너에 추가
                imageContainer.append(img);
            };
        })(file);

        if (files.length > 5) {
            alert("이미지는 최대 5장까지 첨부할 수 있습니다.");
            fileInput.value = "";
            imageContainer.empty();
            return;
        }

        if (!file.name.match(reg)) {
            alert("이미지 파일만 업로드 가능합니다. ");
            fileInput.value = "";
            return;
        } else if (file.size >= maxSize) {
            alert("파일 사이즈는 5MB까지 가능합니다. ");
            fileInput.value = "";
            return;
        }
        reader.readAsDataURL(file);
    }
});

