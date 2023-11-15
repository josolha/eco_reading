
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

//폼 제출 시 등록폼 개별 확인 함수
function validateForm() {

    // fullname 검사
    var fullname = $("#username").val();
    if (!fullname) {
        alert("이름을 입력해주세요.");
        $("#username").focus();
        return false;
    } else if (!/^[가-힣]{1,10}$/.test(fullname)) {
        alert("이름은 한글로 10자 이내로 입력해주세요.");
        $("#username").focus();
        return false;
    }

    //생년월일 검사
    var birthDateValue = $("#birthdate").val();
    if (birthDateValue) {
        var birthDate = new Date(birthDateValue);
        var yesterday = new Date();
        yesterday.setDate(yesterday.getDate() - 1);
        if (birthDate >= yesterday) {
            alert("생년월일은 어제 날짜 이전으로 선택해주세요.");
            $("#birthdate").focus();
            return false;
        }
    }
    // phone 검사
    var phone = $("#phone").val().replace(/-/g, ''); // '-' 문자 제거
    if (!$("#phone").val()) {
        alert("전화번호를 입력해주세요.");
        $("#phone").focus();
        return false;
    } else if (phone.length !== 11) {
        alert("전화번호는 '-'를 제외하고 11자리여야 합니다.");
        $("#phone").focus();
        return false;
    }
    // 모든 유효성 검사를 통과하면 폼 제출
    return true;
}

function validateAddress() {
    var postcode = document.getElementById('sample4_postcode').value.trim();
    var roadAddress = document.getElementById('roadAddress').value.trim();
    var detailAddress = document.getElementById('detailAddress').value.trim();

    // 우편번호와 도로명 주소가 있는지 확인
    if (!postcode || !roadAddress) {
        alert("주소를 검색하여 선택해주세요.");
        return false;
    }

    // 상세 주소가 입력되었는지 확인
    if (!detailAddress) {
        alert("상세 주소를 입력해주세요.");
        document.getElementById('detailAddress').focus();
        return false;
    }

    return true;
}

// 폼 제출 이벤트 리스너
$(document).ready(function () {
    $("form").on("submit", function (e) {
        var isFormValid = validateForm(); // 기존 유효성 검사
        var isAddressValid = validateAddress(); // 주소 유효성 검사

        // 두 검사 중 하나라도 실패하면 폼 제출을 막습니다.
        if (!isFormValid || !isAddressValid) {
            e.preventDefault();
        }
    });
});

$(document).ready(function () {
    $("form").on("submit", function (e) {
        var isFormValid = validateForm(); // 기존 유효성 검사
        var isAddressValid = validateAddress(); // 주소 유효성 검사

        // 두 검사 중 하나라도 실패하면 폼 제출을 막습니다.
        if (!isFormValid || !isAddressValid) {
            e.preventDefault();
        }
    });
});



