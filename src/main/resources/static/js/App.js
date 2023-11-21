
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
window.onload = function() {
    getTotalPoint();
};


