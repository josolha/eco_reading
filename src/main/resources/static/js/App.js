
// header 토글 js
function toggleDropdown(dropdownId) {
    var dropdownContent = document.getElementById(dropdownId);

    if (dropdownContent.style.display === "block" || dropdownContent.style.display === "") {
        dropdownContent.style.display = "none";
    } else {
        dropdownContent.style.display = "block";
    }
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropdown button')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.style.display === "block") {
                openDropdown.style.display = "none";
            }
        }
    }
}
// sidebar 토글 js
function toggleSubMenu() {
    var myPageSubMenu = document.getElementById('myPageSubMenu');
    myPageSubMenu.style.display = (myPageSubMenu.style.display === 'none' || myPageSubMenu.style.display === '') ? 'block' : 'none';
}

//==============================알림================================
const eventSource = new EventSource('http://localhost:8099/alert/init');

// 알림을 드롭다운 메뉴에 추가하는 함수
function addNotificationToMenu(message, id) {
    const notificationMenu = document.getElementById('notificationDropdownMenu');
    const newNotification = document.createElement('div');
    newNotification.classList.add('dropdown-item');
    newNotification.setAttribute('data-alert-id', id); // 알림 ID 설정

    // 알림 텍스트 추가
    const notificationText = document.createElement('span');
    notificationText.textContent = message;
    newNotification.appendChild(notificationText);

    // 삭제 버튼 추가
    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'X';
    deleteButton.classList.add('delete-notification');
    deleteButton.onclick = function() {
        removeNotification(newNotification, id); // 삭제 시 ID 사용
    };
    newNotification.appendChild(deleteButton);
    notificationMenu.appendChild(newNotification);
    // 알림 카운터 업데이트 로직 추가 (있다면)
    updateNotificationCount(1); // 알림 카운터 업데이트
}
eventSource.addEventListener('new-alert', event => {
    const newAlert = JSON.parse(event.data);
    addNotificationToMenu(newAlert.message, newAlert.alertId);
});


// 알림 카운터 업데이트 함수
function updateNotificationCount(increment) {
    const notificationCount = document.getElementById('notificationCount');
    let currentCount = parseInt(notificationCount.innerText) || 0;
    notificationCount.innerText = currentCount + increment;
}

eventSource.addEventListener('init', event => {
    //const message = event.data;
    const notifications = JSON.parse(event.data);
    //const { alertId, userId, status, message } = data;
    // if (message !== "연결되었습니다.") {
    //     addNotificationToMenu(message);
    // }
    notifications.forEach(notification => {
        const { alertId, userId, status, message } = notification;
        addNotificationToMenu(message, alertId);
    });
});

// 알림 제거 함수
function removeNotification(notificationElement, alertId) {
    // 알림 ID를 사용하여 서버에 삭제 요청
    deleteNotificationFromServer(alertId);

    // UI에서 알림 제거
    const notificationMenu = document.getElementById('notificationDropdownMenu');
    notificationMenu.removeChild(notificationElement);
    // 알림 카운터 업데이트 로직 추가 (있다면)
    updateNotificationCount(-1); // 알림 카운터 감소
}
function deleteNotificationFromServer(alertId) {
    fetch(`http://localhost:8099/alert/delete/${alertId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // 추가적인 성공 처리 로직
        })
        .catch(error => console.error('Error deleting notification:', error));
}
eventSource.onerror = error => {
    console.error('EventSource failed:', error);
    eventSource.close();
};
//==============================알림================================

// 책갈피 total point 갖고오기

// // 페이지 로딩 시에 실행
// window.onload = function() {
//     // 토큰에서 유저 ID 가져오기
//     var userId = getUserIdFromToken(); // 여기에서 실제 토큰에서 ID를 추출하는 함수를 호출해야 합니다.
//
//     // 서버에 유저 정보 요청
//     getUserInfo(userId);
// };
//
// // 토큰에서 유저 ID 추출하는 함수 (예제)
// function getUserIdFromToken() {
//     // 실제 토큰에서 ID를 추출하는 로직을 추가
//     // 예제에서는 임의로 "123"을 반환
//     return "123";
// }
//
// // 서버에 유저 정보 요청하는 함수 (예제)
// function getUserInfo(userId) {
//     // 서버에 요청을 보내고, 해당 ID의 유저 정보를 받아옴
//     // 이 예제에서는 간단하게 가정하고, 서버에서 반환되는 데이터가 JSON 형식이라고 가정
//     var userInfo = {
//         userId: userId,
//         totalPoints: 1000 // 서버에서 받아온 유저의 total points
//     };
//
//     // total point를 나타내는 영역에 정보를 표시
//     displayUserPoints(userInfo.totalPoints);
// }
//
// // total point를 나타내는 영역에 정보를 표시하는 함수
// function displayUserPoints(points) {
//     var userPointsElement = document.getElementById("userPoints");
//     userPointsElement.innerHTML = "Total Points: " + points;
// }