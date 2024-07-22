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
    <script src="https://www.youtube.com/iframe_api"></script>
</head>
<script>
  let latestCommentId = null;
  let userId = "<%= userId %>"; // pageOwnerId를 userId로 사용
  let pageOwnerName = null
  let pageOwnerId = "<%= pageOwnerId %>"; // 실제 값 설정
  let currentPage = 0; // 현재 페이지 번호를 저장할 변수
</script>
<body>
<div class="container">
    <input type="hidden" id="userId" value="<%= userId %>">
    <input type="hidden" id="pageOwnerId" value="<%= pageOwnerId %>">
    <div id="main-content">
        <div class="playlist-info" id="loadNextScreen">
            <div class="icon-commentinfo">
                <!-- 최신 댓글이 여기 동적으로 로드됩니다 -->
            </div>
        </div>
        <div class="username-container"></div>

        <% if (isOwner) { %>
        <!-- 현재 사용자의 페이지일 때만 표시 -->
        <button class="playlist-button" id="todayPlaylistBtn">
            <span class="bg"></span>
            <span class="text"><i class="fa-solid fa-compact-disc" id="settings-icon"></i> Today Playlist!</span>
        </button>
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

        <% if (isOwner) { %>
        <button id="add-playlist-btn">+</button>
        <% } %>

        <div id="player"></div>
        <div id="playlist-container">
            <!-- 플레이리스트 아이템들이 여기에 동적으로 로드됩니다 -->
        </div>

        <!-- YouTube Video Input and Display -->
            <div id="youtube-video-display" class="mt-3">
                <!-- YouTube Video will be displayed here -->
            </div>

        <!-- 플레이리스트 추가 모달 -->
        <div id="add-playlist-modal" class="modal">
            <div class="modal-content">
                <span class="close-btn" id="modal-close-btn">&times;</span>
                <h2>새 플레이리스트</h2>
                <label for="playlist-name">플레이리스트 이름:</label>
                <input type="text" id="playlist-name" name="playlist-name" required>
                <input type="text" id="track-search" placeholder="Search for music...">
                <div id="track-results">
                    <!-- 음악 검색 결과가 여기에 표시됩니다. -->
                </div>
                <div id="selected-tracks">
                    <!-- 선택된 음악들이 여기에 추가됩니다. -->
                </div>
                <div class="modal-buttons">
                    <button id="create-playlist-btn" class="create-btn">Create Playlist</button>
                </div>
            </div>
        </div>

    </div>
</div>

<script>
  var player;
  var videoIdList = [];
  var currentVideoIndex = 0;

  function onYouTubeIframeAPIReady() {
    // 플레이어를 생성하되 처음에는 숨겨둔다
    createPlayer();
  }

  function createPlayer() {
    player = new YT.Player('youtube-video-display', {
      height: '200',
      width: '300',
      events: {
        'onReady': onPlayerReady,
        'onStateChange': onPlayerStateChange
      }
    });
  }

  function onPlayerReady(event) {
    // 처음에는 아무 것도 하지 않음
  }

  function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.ENDED) {
      // 영상이 종료되면 다음 영상 재생
      currentVideoIndex++;
      if (currentVideoIndex < videoIdList.length) {
        player.loadVideoById(videoIdList[currentVideoIndex]);
      } else {
        currentVideoIndex = 0;
        player.loadVideoById(videoIdList[currentVideoIndex]);
      }
    }
  }

  $(document).ready(function() {
    // YouTube API가 이미 로드되었는지 확인
    if (window.YT && window.YT.Player) {
      createPlayer();
    } else {
      // YouTube API가 아직 로드되지 않았을 경우
      window.onYouTubeIframeAPIReady = function() {
        createPlayer();
      };
    }

    fetchUserName(pageOwnerId);
    fetchLatestComment(pageOwnerId);

    // 이벤트 리스너 설정
    $("#add-playlist-btn").click(function() {
      $("#add-playlist-modal").addClass('show');
    });

    $("#modal-close-btn").click(function() {
      $("#add-playlist-modal").removeClass('show');
    });

    $("#create-playlist-btn").click(function() {
      createPlaylist();
    });

    $(".submit-button").click(function () {
      var commentInput = $('.input-field').val().trim();
      if (commentInput) {
        var currentDate = new Date().toISOString().slice(0, -1);
        $.ajax({
          type: 'POST',
          url: '/comment/guest',
          contentType: 'application/json',
          data: JSON.stringify({
            commentId: latestCommentId,
            comment: commentInput,
            date: currentDate,
            guestId: userId
          }),
          success: function () {
            window.location.href = "/main?pageOwnerId=" + pageOwnerId;
          },
          error: function (error) {
            alert("현재 페이지의 게시글이 존재하지 않아 댓글을 작성할 수 없습니다.");
            console.error('Error saving comment:', error);
          }
        });
      }
    });

    $(window).scroll(function() {
      if ($(window).scrollTop() + $(window).height() == $(document).height()) {
        currentPage++;
        fetchComments(currentPage);
      }
    });

    setupSearch('track');

    $('#loadNextScreen').click(function() {
      window.location.href = "/comment?pageOwnerId=" + pageOwnerId;
    });

    <% if (isOwner) { %>
    $('#todayPlaylistBtn').click(function() {
      window.location.href = "/todayplaylist";
    });
    <% } %>

    // 이벤트 위임을 사용하여 동적으로 생성된 playlist-item에 이벤트 리스너 추가
    $(".drawer-content").on("click", ".playlist-item", function() {
      var playlistItem = $(this);
      var playlistId = playlistItem.data("playlist-id");
      togglePlaylistDetails(playlistItem, playlistId);
    });

    $('#daily-turntable').click(function() {
      $("#add-playlist-btn").removeClass("show");
      loadPlaylist('DAILY');
    });

    $('#my-turntable').click(function() {
      $("#add-playlist-btn").addClass("show");
      loadPlaylist('MY');
    });

    // 기본으로 dailyturntable 로드
    loadPlaylist('DAILY');

  });

  function getYouTubeVideoId(url) {
    var match = url.match(/(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/);
    return (match && match[1]) ? match[1] : null;
  }

  function fetchUserName(pageOwnerId){
    $.ajax({
      url: '/username',
      method: 'GET',
      data: { memberId: pageOwnerId },
      success: function(response) {
        if (response) {
          $('.username-container').text("@" + response.memberNickname);
          pageOwnerName = response.memberNickname;
        }
      },
      error: function(error) {
        console.error('Error fetching username:', error);
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
          const artists = response.artists.join(', ');
          const commentInfo = $('.icon-commentinfo');
          latestCommentId = response.id;
          commentInfo.html(`
                    <div class="icon"><i class="fas fa-music music-icon"></i></div>
                    <div class="comment-song">
                        <div class="message">
                            ${"${response.comment}"}
                        </div>
                        <div class="song-info">▶ ${"${response.title}"} - ${"${artists}"}</div>
                        <div class="youtube-video" id="latest-comment-video" style="display: none;"></div>
                    </div>
                `);
          fetchComments(currentPage);

          // 마우스 오버 이벤트 추가
          commentInfo.on('mouseenter', function(event) {
            console.log("hover");
            if (response.youtubeUrl!=null) {
              var videoId = getYouTubeVideoId(response.youtubeUrl); // response에 youtubeUrl이 있다고 가정
              $('#latest-comment-video').html(`
                  <iframe width="560" height="200" src="https://www.youtube.com/embed/${"${videoId}"}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                `).show();
            }
            else {
              // 서버에 유튜브 URL 재크롤링 요청
              $.ajax({
                url: '/song/youtube-url',
                method: 'POST',
                data: { songId: response.songId },
                success: function() {
                  $('#latest-comment-video').html(`
                                    <div style="font-size:20px; height:20%;">아직 영상을 준비중입니다 조금만 기다려 주세요 !</div>
                                `).show();
                },
                error: function() {
                  $('#latest-comment-video').html(`
                                    <div style="font-size:20px; height:20%;">아직 영상을 준비중입니다 조금만 기다려 주세요 !</div>
                                `).show();
                }
              });
            }
          })
          .on('mouseleave', function() {
            $('#latest-comment-video').hide();
          });
        }
      },
      error: function(error) {
        console.error('Error fetching latest comment:', error);
        const commentInfo = $('.icon-commentinfo');
        <% if (isOwner) { %>
        commentInfo.html(`
                    <div class="none-article" style="font-size : 12px; color:black">
                     <span> 최근 작성된 게시글이 없습니다. 이곳을 클릭해 오늘의 기분을 작성하고 원하는 음악을 공유해보세요 !
                    </div>
                `);
        <%}%>
        <% if (!isOwner) { %>
        commentInfo.html(`
                    <div class="none-article" style="font-size : 12px; color:black">
                     <span> 최근 ${"${pageOwnerName}"}님의 게시글이 없습니다.
                    </div>
                `);
        <%}%>
      }
    });
  }

  function fetchComments(page) {
    $.ajax({
      url: '/comments/guest',
      method: 'GET',
      data: { page: page, commentId: latestCommentId },
      success: function(response) {
        if (response && response.content) {
          const commentsContainer = $('.comments');
          response.content.forEach(comment => {
            const commentElement = $(`
                        <div class="comment-item" data-comment-id = ${"${comment.id}"}>
                            <div class="comment-box">
                                <div class="comment-profile">
                                    <img src="${"${comment.guestBgImgUrl}"}" alt="Profile Image" data-guest-id = ${"${comment.guestId}"}>
                                </div>
                                <div class="comment-content">
                                    <div class="comment-header">
                                        <div class="comment-user">@${"${comment.guestName}"}</div>
                                    </div>
                                    <div class="comment-body">
                                        <div class="comment-text">${"${comment.comment}"}</div>
                                    </div>
                                    <div class="comment-footer">
                                        <div class="comment-date">${"${new Date(comment.date).toLocaleString()}"}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `);
            commentsContainer.append(commentElement);
          });

            $('.comment-profile img').click(function () {
                const guestId = $(this).data('guest-id');
            window.location.href = `/main?pageOwnerId=${"${guestId}"}`
            });
        }
      },
      error: function(error) {
        console.error('Error fetching comments:', error);
      }
    });
  }

  function setupSearch(type) {
    const searchInput = document.getElementById(type + '-search');
    const resultsContainer = document.getElementById(type + '-results');
    const selectedContainer = document.getElementById('selected-' + type + 's');

    function search() {
      const query = searchInput.value;
      if (!query.trim()) return; // 입력값이 비어있을 경우 요청하지 않음
      resultsContainer.innerHTML = ''; // 기존 결과 초기화

      // AJAX 요청을 통해 서버에서 검색 결과를 가져옴
      $.ajax({
        url: `/search/` + type,
        method: 'GET',
        data: { keyword: query },
        success: function(data) {
          data.forEach(item => {
            const artists = item.artists.join(", ");
            const displayName = item.name + ' - ' + artists;
            const resultItem = document.createElement('div');
            resultItem.textContent = displayName;
            resultItem.classList.add('result-item');
            resultItem.dataset.id = item.id;
            resultItem.dataset.name = item.name;
            resultItem.dataset.artists = artists;
            resultItem.dataset.albumName = item.albumName;
            resultItem.dataset.albumImgUrl = item.albumImgUrl;
            resultItem.addEventListener('click', function() {
              const selectedItem = document.createElement('div');
              selectedItem.textContent = displayName;
              selectedItem.classList.add('selected-item');
              selectedItem.dataset.id = item.id;
              selectedItem.dataset.name = item.name;
              selectedItem.dataset.artists = artists;
              selectedItem.dataset.albumName = item.albumName;
              selectedItem.dataset.albumImgUrl = item.albumImgUrl;
              selectedItem.addEventListener('click', function() {
                selectedItem.remove();
              });
              selectedContainer.appendChild(selectedItem);
              resultsContainer.innerHTML = '';
            });
            resultsContainer.appendChild(resultItem);
          });
        },
        error: function(xhr, status, error) {
          console.error(`Error: ${status}, ${error}`);
        }
      });
    }

    searchInput.addEventListener('keypress', function(e) {
      if (e.key === 'Enter') {
        search();
      }
    });

    $('#search-music-btn').click(function() {
      search();
    });
  }

  function createPlaylist() {
    var playlistName = $("#playlist-name").val();

    const savetracks = Array.from(document.querySelectorAll('.selected-item')).map(item => {
      return {
        name: item.dataset.name,
        artists: item.dataset.artists.split(', '),
        albumName: item.dataset.albumName,
        albumImgUrl: item.dataset.albumImgUrl
      };
    });

    var newPlaylist = {
      name: playlistName,
      tracks: savetracks
    };

    // AJAX 요청으로 새로운 플레이리스트 생성
    $.ajax({
      url: "/api/playlists/create?state=MY",
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(newPlaylist),
      success: function(response) {
        alert("플레이리스트가 성공적으로 저장되었습니다!");
        $("#add-playlist-modal").removeClass('show');
        loadPlaylist('MY'); // 새로 생성된 플레이리스트를 표시하기 위해 목록을 새로 고침
      },
      error: function(xhr, status, error) {
        console.error(`Error: ${status}, ${error}`);
        alert("플레이리스트 저장 중 오류가 발생했습니다.");
      }
    });
  }

  function loadPlaylist(type) {
    var playlistContainer = $("#playlist-container");
    playlistContainer.empty(); // 기존 콘텐츠 제거

    $.ajax({
      url: "/api/playlists/" +type+"/"+pageOwnerId,
      type: "GET",
      success: function(data) {
        data.forEach(function(item) {
          var playlistItem = $("<div>").addClass("playlist-item").attr("data-playlist-id", item.id);
          playlistItem.html(`
                    <div class="item-header">
                        <div class="item-left">
                            <i class="fas fa-heart"></i>
                            <div class="item-text">
                                <div class="playlist-name">${"${item.name}"}</div>
                                <div class="madeby">madeby @${"${item.madeBy}"}</div>
                            </div>
                        </div>
                        <i class="fas fa-chevron-right"></i>
                    </div>
                `);
          playlistContainer.append(playlistItem);
        });
      },
      error: function(error) {
        console.error("Error loading playlist:", error);
      }
    });
  }

  function togglePlaylistDetails(item, id) {
    var existingDetails = item.next(".playlist-details");
    if (existingDetails.length) {
      existingDetails.remove();
      return;
    }
    $.ajax({
      url: "/api/playlists/detail",
      type: "GET",
      data: { playListId: id },
      success: function(data) {
        videoIdList=[];
        var details = $("<div>").addClass("playlist-details");
        data.songs.forEach(function(song) {
          const songArtists = song.artists.join(", ");
          var videoId = null;
          if (song.youtubeUrl!=null) {
            console.log("youtubeUrl"+song.youtubeUrl);
            videoId = getYouTubeVideoId(song.youtubeUrl);
            videoIdList.push(videoId); // 유튜브 영상 ID를 리스트에 추가
          }else {
            // 서버에 유튜브 URL 재크롤링 요청
            $.ajax({
              url: '/song/youtube-url',
              method: 'POST',
              data: { songId: song.id },
              success: function() {
                const songItem = $(`
                                <div class="song-item" data-video-id="">
                                    <i class="fas fa-music"></i><strong>${"${song.name}"}</strong> <span class="song-artists"> - ${"${songArtists}"}</span>
                                </div>
                            `);
                details.append(songItem);
                songItem.click(function() {
                  alert("YouTube URL을 찾을 수 없습니다. 영상을 준비중입니다.");
                });
              },
              error: function() {
                const songItem = $(`
                                <div class="song-item" data-video-id="">
                                    <i class="fas fa-music"></i><strong>${"${song.name}"}</strong> <span class="song-artists"> - ${"${songArtists}"}</span>
                                </div>
                            `);
                details.append(songItem);
                songItem.click(function() {
                  alert("YouTube URL을 찾을 수 없습니다. 영상을 준비중입니다.");
                });
              }
            });
          }
          if (videoId != null) {
            const songItem = $(`
                        <div class="song-item" data-video-id="${"${videoId}"}">
                            <i class="fas fa-music"></i><strong>${"${song.name}"}</strong> <span class="song-artists"> - ${"${songArtists}"}</span>
                        </div>
                    `);
            details.append(songItem);
            console.log(videoIdList);
            // 노래 항목 클릭 이벤트 추가
            songItem.click(function() {
              var selectedVideoId = $(this).data("video-id");
              if (selectedVideoId) {
                player.loadVideoById(selectedVideoId);
                $("#youtube-video-container").show(); // 플레이어를 표시
              } else {
                alert("Invalid YouTube URL for this song.");
              }
            });
          }
        });

        console.log(videoIdList);
        item.after(details);
        details.hide().slideDown();

        // YouTube 비디오 재생 초기화
        if (videoIdList.length > 0) {
          currentVideoIndex = 0;
          player.loadVideoById(videoIdList[currentVideoIndex]);
          $("#youtube-video-container").show(); // 플레이어를 표시
        }
      },
      error: function(error) {
        console.error("Error loading playlist details:", error);
      }
    });
  }
</script>
</body>
</html>
