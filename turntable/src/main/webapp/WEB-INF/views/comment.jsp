<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="background.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>플레이리스트</title>
    <link rel="stylesheet" href="/css/comment.css">
    <link rel="stylesheet" href="/css/global.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Noto+Serif:ital,wght@0,100..900;1,100..900&family=Playwrite+IS:wght@100..400&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="comment-container">
    <div class="main-content">
        <% if (isOwner) { %>
        <div class="input-section">
            <input type="text" class="input-field" placeholder="오늘의 기분을 입력해주세요">
             <div id="selected-song-info"></div> <!-- 선택된 음악 정보를 표시할 요소 추가 -->
            <button class="song-button"><i class="fas fa-music music-icon"></i></button>
            <button class="submit-button"><i class="fa-solid fa-plus"></i></button>
        </div>
        <% } %>

        <div id="comments-container"></div>

        <!-- 페이지네이션 버튼 -->
        <div class="pagination-buttons">
            <button id="prev-page" class="pagination-button" disabled>이전 페이지</button>
            <button id="next-page" class="pagination-button">다음 페이지</button>
        </div>

        <!-- 모달 창 -->
        <div id="songModal" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="track-search" placeholder="노래 검색">
                </div>
                <div id="track-results" class="search-results"></div>
                <div id="selected-tracks" class="selected-container"></div>
                <button id="add-music-button">음악 추가하기</button>
            </div>
        </div>
    </div>
</div>
<script>
  $(document).ready(function () {
    let selectedTitle = ""; // 선택된 노래의 제목을 저장할 변수
    let selectedArtists = []; // 선택된 노래의 아티스트를 저장할 배열
    let currentPage = 0;
    const pageOwnerId = "<%= pageOwnerId %>";

    // 페이지 로드 시 댓글 목록 불러오기
    loadComments(currentPage);

    function loadComments(page) {
      $.ajax({
        type: 'GET',
        url: '/comments',
        data: {
          page: page,
          memberId:pageOwnerId},
        success: function (response) {
          const commentsContainer = document.getElementById('comments-container');
          commentsContainer.innerHTML = '';
          response.content.forEach(comment => {
            console.log(comment);
            const artists = comment.artists.join(', '); // 아티스트 리스트를 콤마로 구분된 문자열로 변환
            const commentElement = document.createElement('div');
            commentElement.classList.add('comment-info');
            commentElement.dataset.commentId = comment.id; // comment-id 속성 추가
            commentElement.innerHTML = `
              <div class="icon-commentinfo">
                <i class="fas fa-music music-icon"></i>
                <div class="comment-song">
                  <div class="comment-message">${'${comment.comment}'}</div>
                  <div class="comment-song-info">▶${'${comment.title}'}-${'${artists}'}</div>
                </div>
              </div>
              <div class="comment-footer">
                <a class="comment-count">댓글 ${'${comment.commentCount}'}</a>
                <div class="comment-date">${'${new Date(comment.date).toLocaleString()}'}</div>
              </div>
            `;
            commentsContainer.appendChild(commentElement);
          });
          document.getElementById('prev-page').disabled = page <= 0;
          document.getElementById('next-page').disabled = response.last;
        },
        error: function (error) {
          console.error('Error loading comments:', error);
        }
      });
    }

    // 이전 페이지 버튼 클릭 이벤트 처리
    $('#prev-page').click(function () {
      if (currentPage > 0) {
        currentPage--;
        loadComments(currentPage);
      }
    });

    // 다음 페이지 버튼 클릭 이벤트 처리
    $('#next-page').click(function () {
      currentPage++;
      loadComments(currentPage);
    });

    // 댓글 클릭 이벤트 처리
    $(document).on('click', '.icon-commentinfo', function () {
      const commentId = $(this).closest('.comment-info').data('comment-id');
      const commentBox = $(this).closest('.comment-info');
      const repliesContainer = commentBox.find('.replies-container');

      if (repliesContainer.length > 0) {
        // 대댓글 컨테이너가 이미 있으면 제거하여 닫기
        repliesContainer.remove();
      } else {
        // 대댓글 컨테이너가 없으면 대댓글 불러오기
        const currentReplyPage = 0;
        loadReplies(commentId, commentBox, currentReplyPage);
      }
    });

    // 대댓글 목록 불러오기
    function loadReplies(commentId, commentBox, page) {
      $.ajax({
        url: '/comments/guest',
        method: 'GET',
        data: { page: page, commentId: commentId},
        success: function(response) {
          if (response && response.content) {
            let repliesContainer = commentBox.find('.replies-container');
            if (repliesContainer.length === 0) {
              repliesContainer = $('<div>').addClass('replies-container');
              commentBox.append(repliesContainer);
            }
            repliesContainer.html(''); // 기존 대댓글 초기화
            response.content.forEach(reply => {
              const replyElement = $(`
              <div class="reply-item">
                <div class="reply-box">
                  <div class="reply-profile">
                    <img src="${"${reply.guestBgImgUrl}"}" alt="Profile Image">
                  </div>
                  <div class="reply-content">
                    <div class="reply-header">
                      <div class="reply-user">@${"${reply.guestName}"}</div>
                    </div>
                    <div class="reply-body">
                      <div class="reply-text">${"${reply.comment}"}</div>
                    </div>
                    <div class="reply-footer">
                      <div class="reply-date">${"${new Date(reply.date).toLocaleString()}"}</div>
                      ${"${reply.owner ? `<button>삭제</button>` : `''`}"}
                    </div>
                  </div>
                </div>
              </div>
            `);
              repliesContainer.append(replyElement);
            });

            // 페이지네이션 버튼 추가
            const paginationButtons = $(`
            <div class="reply-pagination-buttons">
              <button class="reply-prev-page" ${"${page <= 0 ? 'disabled' : ''}"}>이전 페이지</button>
              <button class="reply-next-page" ${"${response.last ? 'disabled' : ''}"}>다음 페이지</button>
            </div>
          `);

            // 기존 페이지네이션 버튼 제거 및 새로 추가
            commentBox.find('.reply-pagination-buttons').remove();
            repliesContainer.append(paginationButtons);

            // 이전 페이지 버튼 클릭 이벤트 처리
            paginationButtons.find('.reply-prev-page').click(function () {
              if (page > 0) {
                loadReplies(commentId, commentBox, page - 1);
              }
            });

            // 다음 페이지 버튼 클릭 이벤트 처리
            paginationButtons.find('.reply-next-page').click(function () {
              if (!response.last) {
                loadReplies(commentId, commentBox, page + 1);
              }
            });
          }
        },
        error: function(error) {
          console.error('Error fetching replies:', error);
        }
      });
    }

    <% if (isOwner) { %>
    // 댓글 작성 버튼 클릭 이벤트 처리
    $('.submit-button').click(function () {
      var commentInput = $('.input-field').val().trim();

      if (commentInput) {
        // 현재 날짜를 가져오기
        var currentDate = new Date().toISOString().slice(0, -1);

        // 서버에 댓글을 저장하는 AJAX 호출
        $.ajax({
          type: 'POST',
          url: '/comment', // 서버의 댓글 저장 엔드포인트
          contentType: 'application/json',
          data: JSON.stringify({
            comment: commentInput,
            date: currentDate,
            title: selectedTitle,
            artists: selectedArtists
          }),
          success: function () {
            // 성공 시 페이지 리다이렉트
            window.location.href = "/comment?pageOwnerId="+pageOwnerId; // 댓글 목록 페이지로 리다이렉트
          },
          error: function (error) {
            console.error('Error saving comment:', error);
          }
        });
      }
    });

    // 모달 창 열기
    $('.song-button').click(function () {
      $('#songModal').css('display', 'block');
      setupSearch('track');
    });

    // 모달 창 닫기
    $('.close').click(function () {
      $('#songModal').css('display', 'none');
    });

    // 모달 창 외부 클릭 시 닫기
    $(window).click(function (event) {
      if (event.target.id === 'songModal') {
        $('#songModal').css('display', 'none');
      }
    });

    // "음악 추가하기" 버튼 클릭 이벤트 처리
    $('#add-music-button').click(function () {
      const selectedItem = $('.selected-item').first();
      if (selectedItem.length) {
        selectedTitle = selectedItem.text().split('-')[0];
        selectedArtists = selectedItem.text().split('-')[1].split(', ');

        // 선택된 음악 정보를 표시
        $('#selected-song-info').text('▶'+selectedTitle + '-' + selectedArtists.join(', '));

        $('#songModal').css('display', 'none');
      } else {
        alert("음악을 선택해주세요.");
      }
    });

    // 노래 검색 설정
    function setupSearch(type) {
      console.log(type);
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
          data: {keyword: query},
          success: function (data) {
            data.forEach(item => {
              const artists = item.artists.join(", ");
              const displayName = item.name + '-' + artists;
              const resultItem = document.createElement('div');
              resultItem.textContent = displayName;
              resultItem.classList.add('result-item');
              resultItem.dataset.id = item.id;
              resultItem.addEventListener('click', function () {
                const selectedItemsCount = document.querySelectorAll('.selected-item').length;
                if (selectedItemsCount >= 5) {
                  alert("최대 5개의 아이템만 선택할 수 있습니다.");
                  return;
                }
                const selectedItem = document.createElement('div');
                selectedItem.textContent = displayName;
                selectedItem.classList.add('selected-item');
                selectedItem.dataset.id = item.id;
                selectedItem.addEventListener('click', function () {
                  selectedItem.remove();
                });
                selectedContainer.appendChild(selectedItem);
                resultsContainer.innerHTML = '';
              });
              resultsContainer.appendChild(resultItem);
            });
          },
          error: function (xhr, status, error) {
            console.error(`Error: ${status}, ${error}`);
          }
        });
      }

      // 검색 입력의 Enter 키 이벤트 처리
      searchInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
          console.log(`Enter key pressed for ${type} search`);
          search();
        }
      });
    }
    <% } %>



  }); // 닫는 중괄호 추가
</script>
</body>
</html>
