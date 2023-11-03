/**
 *
 */
var idChecked = false;  // 아이디 중복 검사 상태 저장 변수
$("#userId").on("change", function() {
    idChecked = false;  // 아이디가 변경되었으므로 상태 초기화
    idCheck();  // 아이디 중복 검사 실행
});

// 아이디 체크 실행 ajax 함수
function idCheck() {
    if (idChecked) {
        return;  // 이미 중복 검사를 통과한 경우, 검사 건너뛰기
    }

    var userId = $("#userId").val();

    $.ajax({
        url: idCheckURL,
        type: "get",
        data: {"userId": userId},
        success: function(responseData) {
            if (responseData == "yes") {
                $("#userId").css("border", "1px solid green");  // 테두리 초록색으로
                $("#idWarning").removeClass('text-danger').addClass('text-success');
                $("#idWarning").text("사용 가능한 아이디 입니다.");  // 사용 가능 메시지 출력
                idChecked = true;  // 중복 검사 통과 상태 저장
            } else {
                $("#userId").css("border", "1px solid red");  // 테두리 빨간색으로
                $("#idWarning").removeClass('text-danger').addClass('text-danger');
                $("#idWarning").text("사용할 수 없는 아이디 입니다.");  // 사용 불가 메시지 출력
                idChecked = false;  // 중복 검사 실패 상태 저장
            }
        },
        error: function() {
            alert("error!!");
        }
    });
}

$("#password").on("input", checkPasswordMatch);  // #password 내용이 변경될 때마다 checkPasswordMatch() 함수를 호출


function validatePassword() {
    var password = $("#password").val();
    var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/;

    if (!regex.test(password)) {
        $("#password").css("border", "1px solid red");
        $("#passwordMsg").removeClass('text-success').addClass('text-danger');
        $("#passwordMsg").text("비밀번호는 최소 8자리, 하나의 대문자, 하나의 소문자, 숫자, 특수 문자를 포함해야 합니다.");
        return false;
    } else {
        $("#password").css("border", "1px solid green");
        $("#passwordMsg").removeClass('text-danger').addClass('text-success');
        $("#passwordMsg").text("사용 가능합니다.");  // <--- 이 부분이 '사용 가능합니다' 메시지를 출력하는 부분입니다.
        return true;
    }
}

//비밀번호 일치 함수
function checkPasswordMatch() {
    var password = $("#password").val();
    var confirmPassword = $("#passwordconfirm").val();

    if (confirmPassword === "") {  // 비밀번호 재확인란이 비어 있으면 아무런 메시지도 출력하지 않습니다.
        $("#passwordconfirm").css("border", "");
        $("#passwordConfirmMsg").text("");
        return;
    }

    if (password !== confirmPassword) {
        $("#passwordconfirm").css("border", "1px solid red");
        $("#passwordConfirmMsg").removeClass('text-success').addClass('text-danger');
        $("#passwordConfirmMsg").text("비밀번호가 일치하지 않습니다.");
    } else {
        $("#passwordconfirm").css("border", "1px solid green");
        $("#passwordConfirmMsg").removeClass('text-danger').addClass('text-success');
        $("#passwordConfirmMsg").text("비밀번호가 일치합니다.");
    }
}

//폼 제출 시 등록폼 개별 확인 함수
function validateForm() {
    // 아이디 검사
    if (!$("#userId").val()) {
        alert("아이디를 입력해주세요.");
        $("#userId").focus();
        return false;
    } else if (!idChecked) {
        alert("아이디 중복 확인이 필요합니다.");
        $("#userId").focus();
        return false;
    }

    // 비밀번호 검사
    if (!$("#password").val()) {
        alert("비밀번호를 입력해주세요.");
        $("#password").focus();
        return false;
    } else if (!validatePassword()) {
        alert("비밀번호 형식이 올바르지 않습니다.");
        $("#password").focus();
        return false;
    }

    // 비밀번호 재확인 검사
    if (!$("#passwordconfirm").val()) {
        alert("비밀번호 재확인을 입력해주세요.");
        $("#passwordconfirm").focus();
        return false;
    } else if ($("#password").val() !== $("#passwordconfirm").val()) {
        alert("비밀번호가 일치하지 않습니다.");
        $("#passwordconfirm").focus();
        return false;
    }

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

    // nickname 검사
    var nickname = $("#nickname").val();
    if (!nickname) {
        alert("닉네임을 입력해주세요.");
        $("#nickname").focus();
        return false;
    } else if (nickname.length > 10) {
        alert("닉네임은 10자 이내로 입력해주세요.");
        $("#nickname").focus();
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

    // 이메일 검사
    var email = $("#email").val();
    if (email && !/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email)) {
        alert("올바른 이메일 형식을 입력해주세요.");
        $("#email").focus();
        return false;
    }

    // 모든 유효성 검사를 통과하면 폼 제출
    return true;
}


// 폼 제출 이벤트 리스너 추가
$(document).ready(function() {
    $("form").on("submit", function(e) {
        if (!validateForm()) {
            e.preventDefault();  // 폼 제출 막기
        }
    });
});