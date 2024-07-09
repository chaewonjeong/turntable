<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="background.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>플레이리스트</title>
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/global.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Noto+Serif:ital,wght@0,100..900;1,100..900&family=Playwrite+IS:wght@100..400&display=swap" rel="stylesheet">
    <script>
      document.addEventListener('DOMContentLoaded', (event) => {
        const pageOwnerId = "<%= pageOwnerId %>";
        console.log("Page Owner from URL or Session: " + pageOwnerId); // 콘솔에 출력

        // 최신 댓글 데이터를 가져와서 HTML에 추가
        fetchLatestComment(pageOwnerId);
        fetchUserName(pageOwnerId);
      });

      function fetchUserName(pageOwnerId){
        $.ajax({
          url: '/username',
          method: 'GET',
          data: { memberId: pageOwnerId },
          success: function(response) {
            console.log(response.memberName);
            if (response) {
              document.querySelector('.username-container').textContent="@"+response.memberName;
            }
          },
          error: function(error) {
            console.error('Error fetching latest comment:', error);
          }
        });
      }

      function fetchLatestComment(userId) {
        $.ajax({
          url: '/comment/latest',
          method: 'GET',
          data: { memberId: userId },
          success: function(response) {
            if (response) {
              const artists = response.artists.join(', '); // 아티스트 리스트를 콤마로 구분된 문자열로 변환
              const commentInfo = document.querySelector('.icon-commentinfo');
              commentInfo.innerHTML = `
                <div class="icon"><i class="fas fa-music music-icon"></i></div>
                <div class="comment-song">
                  <div class="message">
                    ${'${response.comment}'}
                  </div>
                  <div class="song-info">▶ ${'${response.title}'} - ${'${artists}'}</div>
                </div>
              `;
            }
          },
          error: function(error) {
            console.error('Error fetching latest comment:', error);
          }
        });
      }
    </script>
</head>
<body>
<div class="container">
    <div id="main-content">
        <div class="playlist-info" id="loadNextScreen">
            <div class="icon-commentinfo">
                <!-- 최신 댓글이 여기 동적으로 로드됩니다 -->
            </div>
        </div>
        <div class="username-container"></div>

        <% if (isOwner) { %>
        <!-- 현재 사용자의 페이지일 때만 표시 -->
        <button class="playlist-button" id="todayPlaylistBtn"><i class="fa-solid fa-compact-disc" id="settings-icon"></i>Today Playlist</button>
        <% } %>
        <div class="input-section-container">
            <div class="input-section">
                <input type="text" class="input-field" placeholder="댓글을 입력하세요">
                <button class="submit-button">등록</button>
            </div>
        </div>

        <div class="comments">
            <!-- 댓글들이 여기에 동적으로 로드됩니다 -->
        </div>
    </div>
    <iframe id="comment-frame" style="width:100%; height:100%; border:none; display:none;"></iframe>
</div>

<div id="settings-drawer" class="settings-drawer">
    <div class="drawer-content">
        <ul>
            <li id="daily-turntable">dailyturntable</li>
            <li id="my-turntable">myturntable</li>
        </ul>
        <div id="playlist-container">
            <!-- 플레이리스트 아이템들이 여기에 동적으로 로드됩니다 -->
        </div>
    </div>
</div>

<script>
  document.addEventListener("DOMContentLoaded", function(){
    const pageOwnerId = "<%= pageOwnerId %>";
    document.getElementById("loadNextScreen").addEventListener("click", function() {
      window.location.href = "/comment?pageOwnerId="+pageOwnerId;
    });

    <% if (isOwner) { %>
    document.getElementById("todayPlaylistBtn").addEventListener("click", function() {
      window.location.href = "/todayplaylist";
    });
    <% } %>
  });

  // 이벤트 위임을 사용하여 동적으로 생성된 playlist-item에 이벤트 리스너 추가
  document.querySelector(".drawer-content").addEventListener("click", function(e) {
    if (e.target.closest(".playlist-item")) {
      var playlistItem = e.target.closest(".playlist-item");
      var playlistId = playlistItem.getAttribute("data-playlist-id");
      togglePlaylistDetails(playlistItem, playlistId);
    }
  });

  document.getElementById("daily-turntable").addEventListener("click", function() {
    loadPlaylist('daily');
  });

  document.getElementById("my-turntable").addEventListener("click", function() {
    loadPlaylist('my');
  });

  // 기본으로 dailyturntable 로드
  loadPlaylist('daily');

  function loadPlaylist(type) {
    var playlistContainer = document.getElementById("playlist-container");
    playlistContainer.innerHTML = ''; // 기존 콘텐츠 제거

    var playlistData;
    if (type === 'daily') {
      playlistData = [
        { id: 1, date: '2024.06.01', madeBy: 'made by @codnjs_99' },
        { id: 2, date: '2024.06.02', madeBy: 'made by @codnjs_99' },
        { id: 3, date: '2024.06.03', madeBy: 'made by @codnjs_99' }
      ];
    } else if (type === 'my') {
      playlistData = [
        { id: 4, date: '2024.07.01', madeBy: 'made by @codnjs_99' },
        { id: 5, date: '2024.07.02', madeBy: 'made by @codnjs_99' },
        { id: 6, date: '2024.07.03', madeBy: 'made by @codnjs_99' }
      ];
    }

    playlistData.forEach(function(item) {
      var playlistItem = document.createElement("div");
      playlistItem.classList.add("playlist-item");
      playlistItem.setAttribute("data-playlist-id", item.id);
      playlistItem.innerHTML = `
                        <div class="item-header">
                            <div class="item-left">
                                <i class="fas fa-heart"></i>
                                <div class="item-text">
                                    <div class="playlist-name">${'${item.date}'}</div>
                                    <div class="madeby">${'${item.madeBy}'}</div>
                                </div>
                            </div>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    `;
      playlistContainer.appendChild(playlistItem);
    });
  }

  function togglePlaylistDetails(item, id) {
    // 기존의 상세 목록이 있는지 확인하고 있으면 제거
    var existingDetails = item.nextElementSibling;
    if (existingDetails && existingDetails.classList.contains("playlist-details")) {
      existingDetails.remove();
      return;
    }

    // 예시 데이터를 사용하여 상세 목록 생성
    var details = document.createElement("div");
    details.classList.add("playlist-details");
    details.innerHTML = `
                    <div class="song-item"><i class="fas fa-music"></i> Song 1</div>
                    <div class="song-item"><i class="fas fa-music"></i> Song 2</div>
                    <div class="song-item"><i class="fas fa-music"></i> Song 3</div>`;

    item.insertAdjacentElement("afterend", details);
  }
</script>
</body>
</html>