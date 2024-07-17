<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="background.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User List</title>
    <link rel="stylesheet" href="/css/global.css">
    <link rel="stylesheet" href="/css/users.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
    </style>
</head>
<body>
    <div class="container">
        <input type="text" id="search" placeholder="Search..." class="search-box">
        <div class="grid" id="userGrid"></div>
        <div class="pagination" id="pagination"></div>
    </div>
<script>
  $(document).ready(function() {
    const userGrid = $('#userGrid');
    const searchInput = $('#search');
    const pagination = $('#pagination');
    let users = [];
    let currentPage = 0;

    // Fetch users from server
    function fetchUsers(page) {
      $.ajax({
        url: '/users/all',
        method: 'GET',
        data: { page: page },
        success: function(data) {
          console.log(data)
          users = data.content;
          displayUsers();
          setupPagination(data.totalPages);
        },
        error: function(error) {
          console.error('Error fetching users:', error);
        }
      });
    }

    // Display users in grid
    function displayUsers() {
      userGrid.empty();
      users.forEach(function(user) {
        const userCard = $(`
          <div class="user-card">
            <div class="user-card-content">
              <img class="profile-picture" src="${"${user.bgImgUrl}"}" alt="Profile Picture" width="50" height="50">
              <div class="user-info">
                <h3 class="username">@${"${user.memberNickname}"}</h3>
                <p class="email">${"${user.playlistCount}"}개의 플레이리스트</p>
              </div>
            </div>
          </div>
        `);

        // 사용자 카드에 클릭 이벤트 추가
        userCard.on('click', function() {
          window.location.href = '/main?pageOwnerId=' + user.memberId;
        });

        userGrid.append(userCard);
      });
    }

    // Setup pagination controls
    function setupPagination(totalPages) {
      pagination.empty();
      for (let i = 0; i <= totalPages; i++) {
        const pageLink = $('<a href="#"></a>').text(i);
        pageLink.on('click', function(e) {
          e.preventDefault();
          currentPage = i;
          fetchUsers(currentPage);
        });
        pagination.append(pageLink);
      }
    }

    // Initial fetch
    fetchUsers(currentPage);

    // Search functionality
    searchInput.on('keypress', function(e) {
      if (e.which == 13) { // Enter key pressed
        const nickname = searchInput.val().trim();
        if (nickname) {
          searchUsersByNickname(nickname);
        }
      }
    });

    function searchUsersByNickname(nickname) {
      $.ajax({
        url: '/users/nickname',
        method: 'GET',
        data: { page: currentPage, name: nickname },
        success: function(data) {
          users = data.content;
          console.log(users)
          displayUsers();
          setupPagination(data.totalPages);
        },
        error: function(error) {
          console.error('Error searching users by nickname:', error);
        }
      });
    }

  });
</script>
</body>
</html>