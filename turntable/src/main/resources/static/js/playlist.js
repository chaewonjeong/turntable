
$(document).ready(function() {
  // 페이지 로드 시 필요한 데이터 가져오기
  let latestCommentId = null;
  let userId = $("#userId").val();
  let pageOwnerId = $("#pageOwnerId").val();
  let currentPage = 0;

  fetchLatestComment(userId);
  fetchUserName(pageOwnerId);

  // 이벤트 리스너 설정
  $("#add-playlist-btn").click(function() {
    $("#add-playlist-modal").show();
  });

  $("#cancel-btn").click(function() {
    $("#add-playlist-modal").hide();
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
});

function fetchUserName(pageOwnerId){
  $.ajax({
    url: '/username',
    method: 'GET',
    data: { memberId: pageOwnerId },
    success: function(response) {
      if (response) {
        $('.username-container').text("@" + response.memberName);
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
                            ${response.comment}
                        </div>
                        <div class="song-info">▶ ${"${response.title}"} - ${"${artists}"}</div>
                    </div>
                `);
        fetchComments(currentPage);
      }
    },
    error: function(error) {
      console.error('Error fetching latest comment:', error);
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
                        <div class="comment-item">
                            <div class="comment-box">
                                <div class="comment-profile">
                                    <img src="${comment.guestBgImgUrl}" alt="Profile Image">
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
      $("#add-playlist-modal").hide();
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
    url: "/api/playlists/" + type,
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
      var details = $("<div>").addClass("playlist-details");
      data.songs.forEach(function(song) {
        const songArtists = song.artists.join(", ");
        details.append(`
                    <div class="song-item">
                        <i class="fas fa-music"></i><strong>${"${song.name}"}</strong> <span class="song-artists"> - ${"${songArtists}"}</span>
                    </div>
                `);
      });
      item.after(details);
      details.hide().slideDown();
    },
    error: function(error) {
      console.error("Error loading playlist details:", error);
    }
  });
}
