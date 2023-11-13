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

//
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

// 폼 제출 이벤트 리스너 추가
$(document).ready(function() {
    $("#form").on("submit", function(e) {
        if (!submitValidateForm()) {
            e.preventDefault();  // 폼 제출 막기
        }
    });
});


// $(document).ready(function () {
//     $('#imageUpload').on('change', function () {
//         var existingFiles = $(this)[0].files;
//         var newFiles = $(this).prop('files');
//         var maxFiles = 5; // 최대 파일 수
//
//         if (existingFiles.length + newFiles.length > maxFiles) {
//             alert('이미지는 최대 5장까지 첨부할 수 있습니다.');
//             // 새로운 파일들을 추가하기 전에 input 값을 비움
//             $(this).val('');
//             return;
//         }
//
//         // 새로운 파일들을 기존 파일들에 추가
//         for (var i = 0; i < newFiles.length; i++) {
//             existingFiles.push(newFiles[i]);
//         }
//
//         // 여기에서 추가로 필요한 작업 수행
//     });
// });

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

$("#imageUpload").on("change", "input[type='file']", function(){
    var file_path = $(this).val();
    var reg = /(.*?)\.(jpg|bmp|jpeg|png|JPG|BMP|JPEG|PNG)$/;
    var maxSize = 5 * 1024 * 1024;
    var fileSize;

    if ($("#imageUpload")[0].files.length > 5) {
        alert("이미지는 최대 5장까지 첨부할 수 있습니다.");
    }

    if(file_path != "" && file_path != null) {
        fileSize = document.getElementById("imageUpload").files[0].size;

        if(!file_path.match(reg)) {
            alert("이미지 파일만 업로드 가능합니다. ");
            return;
        } else if(fileSize = maxSize) {
            alert("파일 사이즈는 5MB까지 가능합니다. ");
            return;
        }
    }
    // // 허용되지 않은 확장자일 경우
    // if (file_path != "" && (file_path.match(reg) == null || reg.test(file_path) == false)) {
    //     // 파일 입력 필드의 값을 비움
    //     $(this).val("");
    //
    //     // 알림 표시
    //     alert("이미지 파일만 업로드 가능합니다.");
    // }
});