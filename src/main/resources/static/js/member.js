//타이머 전역변수
var timerId;

//이메일 인증 전역변슈
var isVerified = false;


document.getElementById('sendCodeButton').addEventListener('click', function () {
    var sendCodeButton = this; // sendCodeButton이라는 변수로 this 참조 저장
    sendCodeButton.disabled = true; // 버튼을 먼저 비활성화
    var email = document.getElementById('email').value; // 이메일 값 가져오기
    // axios를 사용하여 서버에 POST 요청 보내기
    axios.post('/user/emails/verification-requests', {
        email: email
    })
        .then(function (response) {
            // 성공적으로 이메일 인증 요청이 처리된 경우
            alert("인증 코드가 이메일로 발송되었습니다.");

            document.getElementById('verificationCodeInputGroup').style.display = 'flex';
            // 3분 타이머 설정
            var timeLeft = 180;
            var timerInput = document.getElementById('timer');
            timerId = setInterval(function () {
                timeLeft--;
                var minutes = Math.floor(timeLeft / 60);
                var seconds = timeLeft % 60;
                timerInput.value = minutes + " : " + seconds + "(time)";

                if (timeLeft === 0) {
                    clearInterval(timerId);
                    // 시간이 다 되면 버튼을 다시 활성화
                    sendCodeButton.disabled = false;
                    // 인증코드 입력 필드 숨기기
                    document.getElementById('verificationCodeInputGroup').style.display = 'none';
                }
            }, 1000);
        })
        .catch(function (error) {
            console.log(error.response.data)
            console.log(error.message)
            // HTTP 상태 코드가 있는지 확인합니다.
            if (error.response && error.response.status) {

                // 서버가 응답으로 보낸 오류 메시지를 사용합니다.
                alert(error.response.data.message);
            } else {
                // 서버에서 응답이 오지 않았거나 상태 코드가 없는 경우 일반 오류 메시지를 사용합니다.
                alert("이메일 인증 요청 중 오류가 발생했습니다.");
            }
            sendCodeButton.disabled = false; // 버튼을 다시 활성화합니다.
        });
});

document.getElementById('checkCodeButton').addEventListener('click', function () {
    var email = document.getElementById('email').value;
    var code = document.getElementById('emailcode').value;
    axios.post('/user/emails/verifications', {
        email: email,
        code: code
    })
        .then(function (response) {
            alert("인증 코드 확인이 완료되었습니다.");
            isVerified = true; // 인증 상태를 true로 설정합니다.
            clearInterval(timerId); // 타이머 중지

            document.getElementById('timer').style.display = 'none'; // 타이머를 숨깁니다.
        })
        .catch(function (error) {
            if (error.response && error.response.status) {
                alert(error.response.data.message);
                $("#verificationCodeInputGroup").removeClass('border-success').addClass('border-danger');

            } else {
                alert("인증 코드 확인 중 오류가 발생했습니다.");
            }

        });
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

    if (!isVerified) {
        alert("이메일 인증이 완료되지 않았습니다. 인증을 완료해주세요.");
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



