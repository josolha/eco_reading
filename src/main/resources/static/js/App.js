
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
function getTotalPoint() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 세션 값 가져와서 화면에 표시
            var totalPointValue = xhr.responseText;
            console.log('totalPoint', totalPointValue);
            document.getElementById('totalPointContainer').textContent = totalPointValue;
        }
    };
    xhr.open("GET", "/user/getTotalPoint", true);
    xhr.send();
}
// 페이지 로드 시 세션 값 초기화
document.addEventListener('DOMContentLoaded', function() {
    getTotalPoint();
});


